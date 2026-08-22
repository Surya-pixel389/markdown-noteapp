import React, { useState, useEffect, useRef } from 'react';
import { Editor } from './Editor';
import { noteService } from '../../services/noteService';
import './EditorContainer.css';

export const EditorContainer = ({ noteId, onClose }) => {
  const [note, setNote] = useState({
    title: 'Untitled',
    content: '',
  });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const saveTimeout = useRef(null);

  useEffect(() => {
    if (noteId) {
      loadNote();
    }
  }, [noteId]);

  const loadNote = async () => {
    try {
      setLoading(true);
      setError('');  // clear old error before loading
      const response = await noteService.getNote(noteId);
      setNote(response.data);
    } catch (err) {
      setError('Failed to load note');
    } finally {
      setLoading(false);
    }
  };

  const handleTitleChange = (e) => {
    const updated = { ...note, title: e.target.value };
    setNote(updated);
    scheduleSave(updated);
  };

  const handleContentChange = (value) => {
    const updated = { ...note, content: value };
    setNote(updated);
    scheduleSave(updated);
  };

  const scheduleSave = (updatedNote) => {
    if (saveTimeout.current) clearTimeout(saveTimeout.current);
    saveTimeout.current = setTimeout(() => {
      handleSave(updatedNote);
    }, 1000);
  };

  const handleSave = async (noteToSave = note) => {
    try {
      if (noteId) {
        await noteService.updateNote(noteId, noteToSave.title, noteToSave.content);
        if (onNoteUpdate) onNoteUpdate();
      }
    } catch (err) {
      setError('Failed to save note');
    }
  };

  const handleExport = async () => {
    try {
      const response = await noteService.exportMarkdown(noteId);
      const blob = new Blob([response.data]);
      const url = window.URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;
      a.download = `${note.title}.md`;
      a.click();
      window.URL.revokeObjectURL(url);
    } catch (err) {
      setError('Failed to export note');
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
              placeholder="Untitled"
          />
          <button onClick={handleExport} className="editor-export">Export .md</button>
          <button onClick={onClose} className="editor-close">×</button>
        </div>
        <div className="editor-body">
          <Editor content={note.content} onChange={handleContentChange} />
        </div>
      </div>
  );
};