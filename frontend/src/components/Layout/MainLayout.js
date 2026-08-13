import React, { useState } from 'react';
import { Navbar } from '../Navbar/Navbar';
import { Sidebar } from '../Sidebar/Sidebar';
import { EditorContainer } from '../Editor/EditorContainer';
import './MainLayout.css';

export const MainLayout = () => {
  const [selectedNoteId, setSelectedNoteId] = useState(null);

  return (
    <div className="main-layout">
      <Navbar />
      <div className="layout-body">
        <Sidebar onSelectNote={setSelectedNoteId} selectedNoteId={selectedNoteId} />
        {selectedNoteId ? (
          <EditorContainer
            noteId={selectedNoteId}
            onClose={() => setSelectedNoteId(null)}
          />
        ) : (
          <div className="welcome">
            <h2>Welcome to NoteApp</h2>
            <p>Create or select a note to get started</p>
          </div>
        )}
      </div>
    </div>
  );
};