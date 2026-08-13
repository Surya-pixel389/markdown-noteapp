import React from 'react';
import { NoteItem } from './NoteItem';

export const NoteTree = ({ notes, onSelectNote, selectedId }) => {
  return (
    <ul className="note-tree">
      {notes.map((note) => (
        <NoteItem
          key={note.id}
          note={note}
          onSelectNote={onSelectNote}
          isSelected={selectedId === note.id}
        />
      ))}
    </ul>
  );
};