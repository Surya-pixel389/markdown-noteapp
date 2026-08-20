package com.noteapp.noteapp.service;

import com.noteapp.noteapp.entity.Note;
import com.noteapp.noteapp.repository.NoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExportService {

    private final NoteRepository noteRepository;

    public byte[] exportAsMarkdown(Long noteId, Long userId ) {
        Note note = noteRepository.findByIdAndUser_Id(noteId, userId)
                .orElseThrow(() -> new RuntimeException("Note not found"));

        String markDown= "# " + note.getTitle() + "\n\n" + note.getContent();
        return markDown.getBytes();
    }

}
