import React, { useState, useEffect } from 'react';
import { Editor } from './Editor';
import { Preview } from './Preview';
import { noteService } from '../../services/noteService';
import './EditorContainer.css';

export const EditorContainer = ({ noteId, onClose }) => {
  const [note, setNote] = useState({
    title: 'Untitled',
    content: '',
  });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  useEffect(() => {
    if (noteId) {
      loadNote();
    }
  }, [noteId]);

  const loadNote = async () => {
    try {
      setLoading(true);
      const response = await noteService.getNote(noteId);
      setNote(response.data);
    } catch (err) {
      setError('Failed to load note');
    } finally {
      setLoading(false);
    }
  };

  const handleTitleChange = (e) => {
    setNote({ ...note, title: e.target.value });
  };

  const handleContentChange = (value) => {
    setNote({ ...note, content: value });
  };

  const handleSave = async () => {
    try {
      if (noteId) {
        await noteService.updateNote(noteId, note.title, note.content);
      }
    } catch (err) {
      setError('Failed to save note');
    }
  };

  if (loading) return <div className="editor-loading">Loading...</div>;

  return (
    <div className="editor-container">
      {error && <div className="editor-error">{error}</div>}
      <div className="editor-header">
        <input
          type="text"
          className="editor-title"
          value={note.title}
          onChange={handleTitleChange}
          onBlur={handleSave}
          placeholder="Untitled"
        />
        <button onClick={onClose} className="editor-close">×</button>
      </div>
      <div className="editor-body">
        <Editor content={note.content} onChange={handleContentChange} onSave={handleSave} />
        <Preview content={note.content} />
      </div>
    </div>
  );
};