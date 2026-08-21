package com.noteapp.noteapp.service;

import com.noteapp.noteapp.dto.NoteDto;
import com.noteapp.noteapp.entity.Note;
import com.noteapp.noteapp.entity.User;
import com.noteapp.noteapp.exception.ResourceNotFoundException;
import com.noteapp.noteapp.repository.NoteRepository;
import com.noteapp.noteapp.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoteService {
    private final NoteRepository noteRepository;
    private final UserRepository userRepository;

    @Transactional
    public NoteDto createNote(Long userId, NoteDto noteDto) {
        //finding the user who's creating the notes
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new RuntimeException("User not found"));

        //Building a new note object
        Note note = Note.builder()
                .title(noteDto.getTitle())
                .content(noteDto.getContent() != null ? noteDto.getContent() :"")
                .user(user)
                .build();

        //note should live in a folder
        if(noteDto.getParentId() != null){
            Note parent = noteRepository.findByIdAndUser_Id(noteDto.getParentId(), userId)
                    .orElseThrow(() -> new ResourceNotFoundException("Parent Not found"));
            note.setParent(parent);
        }
        //save note in DB
        Note saved = noteRepository.save(note);

        return convertToDto(saved);

    }

    public NoteDto getNoteById(Long noteId, Long userId) {
        Note note = noteRepository.findByIdAndUser_Id(noteId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Note not found"));
        return convertToDto(note);
    }

    //to fetch all top notes (which is orphan)
   public List<NoteDto> getUserRootNotes(Long userId) {
        List<Note> notes = noteRepository.findByUser_IdAndParentIsNull(userId);
        return notes.stream()  //convert list to stream to transform
                .map(this::convertToDto) //convert every note to dto
                .collect(Collectors.toList());
   }
   public List<NoteDto> getNoteChildren(Long noteId, Long userId) {
        Note note = noteRepository.findByIdAndUser_Id(noteId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Note not found"));
        List<Note> children = noteRepository.findByParent_IdAndUser_Id(noteId, userId);
        return children.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
   }

   public List<NoteDto> searchNotes(Long userId, String keyword) {
        return noteRepository.searchNotes(userId, keyword)
                .stream().map(this::convertToDto).collect(Collectors.toList());
   }
   @Transactional
   public NoteDto updateNote(Long noteId, Long userId, NoteDto noteDto) {
        Note note = noteRepository.findByIdAndUser_Id(noteId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Note not found"));
        note.setTitle(noteDto.getTitle());
        note.setContent(noteDto.getContent());

        Note updatedNote = noteRepository.save(note);
        return convertToDto(updatedNote);
   }

   @Transactional
   public void deleteNote(Long noteId, Long userId) {
        Note note = noteRepository.findByIdAndUser_Id(noteId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Note not found"));
        noteRepository.delete(note);
   }

    private NoteDto convertToDto(Note note) {
        return NoteDto.builder()
                .id(note.getId())
                .title(note.getTitle())
                .content(note.getContent())
                .parentId(note.getParent() != null ? note.getParent().getId() : null)
                .createdAt(note.getCreatedAt())
                .updatedAt(note.getUpdatedAt())
                .build();

    }
}
