package com.noteapp.noteapp.service;

import com.noteapp.noteapp.entity.Note;
import com.noteapp.noteapp.entity.Tag;
import com.noteapp.noteapp.exception.ResourceNotFoundException;
import com.noteapp.noteapp.repository.NoteRepository;
import com.noteapp.noteapp.repository.TagRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TagService {
    private final TagRepository tagRepository;
    private final NoteRepository noteRepository;

    @Transactional
    public void addTag(Long noteId, Long userId, String tagName) {
        Note note = noteRepository.findByIdAndUser_Id(noteId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Note not found"));

        Tag tag = tagRepository.findByName(tagName)
                .orElseGet(() -> tagRepository.save(Tag.builder().name(tagName).build()));

        if(!note.getTags().contains(tag)) {
            note.getTags().add(tag);
            noteRepository.save(note);
        }
    }
    @Transactional
    public void deleteTag(Long noteId, Long userId, Long tagId) {
        Note note = noteRepository.findByIdAndUser_Id(noteId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Note not found"));
        note.getTags().removeIf(tag -> tag.getId() == tagId);
    }
}
