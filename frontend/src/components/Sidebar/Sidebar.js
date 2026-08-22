import React, { useState, useEffect } from 'react';
import { NoteTree } from './NoteTree';
import { noteService } from '../../services/noteService';
import './Sidebar.css';

export const Sidebar = ({ onSelectNote, selectedNoteId }) => {
  const [notes, setNotes] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadNotes();
  }, []);

  const loadNotes = async () => {
    try {
      setLoading(true);
      const response = await noteService.getRootNotes();
      setNotes(response.data);
    } catch (err) {
      console.error('Failed to load notes');
    } finally {
      setLoading(false);
    }
  };

  const [searchQuery, setSearchQuery] = useState('');
  const [searchResults, setSearchResults] = useState(null);

  const handleSearch = async (e) => {
    const query = e.target.value;
    setSearchQuery(query);
    if (query.trim() === '') {
      setSearchResults(null);
      return;
    }
    try {
      const response = await noteService.searchNotes(query);
      setSearchResults(response.data);
    } catch (err) {
      console.error('Search failed');
    }
  };

  const handleCreateNote = async () => {
    try {
      const response = await noteService.createNote('New Note', '');
      setNotes([...notes, response.data]);
      onSelectNote(response.data.id);
    } catch (err) {
      console.error('Failed to create note');
    }
  };

  return (
      <div className="sidebar">
        <div className="sidebar-header">
          <h2>Notes</h2>
          <button onClick={handleCreateNote} className="btn-create" title="Create note">
            +
          </button>
        </div>
        <input
            type="text"
            placeholder="Search notes..."
            value={searchQuery}
            onChange={handleSearch}
            className="search-input"
        />
        <div className="sidebar-content">
          {searchResults !== null ? (
              searchResults.length === 0 ? (
                  <p className="empty">No results</p>
              ) : (
                  <ul className="note-tree">
                    {searchResults.map((note) => (
                        <li
                            key={note.id}
                            className={`note-item-content ${selectedNoteId === note.id ? 'selected' : ''}`}
                            onClick={() => onSelectNote(note.id)}
                        >
                          <span className="note-title">{note.title}</span>
                        </li>
                    ))}
                  </ul>
              )
          ) : loading ? (
              <p className="loading">Loading...</p>
          ) : notes.length === 0 ? (
              <p className="empty">No notes yet</p>
          ) : (
              <NoteTree notes={notes} onSelectNote={onSelectNote} selectedId={selectedNoteId} />
          )}
        </div>
      </div>
  );
};