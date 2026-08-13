import React, { useState } from 'react';
import { ChevronDown, ChevronRight } from 'lucide-react';
import { noteService } from '../../services/noteService';

export const NoteItem = ({ note, onSelectNote, isSelected }) => {
  const [isExpanded, setIsExpanded] = useState(false);
  const [children, setChildren] = useState([]);
  const [isLoadingChildren, setIsLoadingChildren] = useState(false);

  const handleExpand = async () => {
    if (!isExpanded && children.length === 0) {
      try {
        setIsLoadingChildren(true);
        const response = await noteService.getNoteChildren(note.id);
        setChildren(response.data);
      } catch (err) {
        console.error('Failed to load children');
      } finally {
        setIsLoadingChildren(false);
      }
    }
    setIsExpanded(!isExpanded);
  };

  return (
    <li className="note-item">
      <div
        className={`note-item-content ${isSelected ? 'selected' : ''}`}
        onClick={() => onSelectNote(note.id)}
      >
        <button
          onClick={(e) => {
            e.stopPropagation();
            handleExpand();
          }}
          className="expand-btn"
        >
          {isExpanded ? <ChevronDown size={16} /> : <ChevronRight size={16} />}
        </button>
        <span className="note-title">{note.title}</span>
      </div>
      {isExpanded && (
        <ul className="note-children">
          {isLoadingChildren ? (
            <li className="loading">Loading...</li>
          ) : children.length > 0 ? (
            children.map((child) => (
              <NoteItem
                key={child.id}
                note={child}
                onSelectNote={onSelectNote}
                isSelected={isSelected}
              />
            ))
          ) : (
            <li className="empty">No notes</li>
          )}
        </ul>
      )}
    </li>
  );
};