package com.noteapp.noteapp.controller;

import com.noteapp.noteapp.dto.NoteDto;
import com.noteapp.noteapp.service.JwtService;
import com.noteapp.noteapp.service.NoteService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notes")
@CrossOrigin(origins = "https://localhost/3000")
public class NoteController {

    private final NoteService noteService;
    private final JwtService jwtService;

    public Long getUserId(HttpServletRequest request) {
        String AuthHeader = request.getHeader("Authorization");
        if(AuthHeader != null && AuthHeader.startsWith("Bearer ")) {
            String token = AuthHeader.substring(7);
            return jwtService.extractUserId(token);
        }
        throw new RuntimeException("No token found");
    }

    @PostMapping
    public ResponseEntity<NoteDto> createNote(@RequestBody NoteDto noteDto, HttpServletRequest request){
        Long userId = getUserId(request);
        NoteDto note = noteService.createNote(userId, noteDto);
        return new ResponseEntity<>(note, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NoteDto> getNote(@PathVariable Long id, HttpServletRequest request){
        Long userId = getUserId(request);
        NoteDto note = noteService.getNoteById(userId, id);
        return ResponseEntity.ok(note);
    }

    @GetMapping
    public ResponseEntity<List<NoteDto>> getRootNodes(HttpServletRequest request){
        Long userId = getUserId(request);
        List<NoteDto> notes = noteService.getUserRootNotes(userId);
        return ResponseEntity.ok(notes);
    }
    @GetMapping("/{id}children")
    public ResponseEntity<List<NoteDto>> getChildNotes(@PathVariable Long id, HttpServletRequest request){
        Long userId = getUserId(request);
        List<NoteDto> child = noteService.getNoteChildren(userId, id);
        return ResponseEntity.ok(child);
    }
    @PutMapping("/{id}")
    public ResponseEntity<NoteDto> updateNote(@PathVariable Long id, @RequestBody NoteDto noteDto, HttpServletRequest request){
        Long userId = getUserId(request);
        NoteDto updated =  noteService.updateNote(userId, id, noteDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<NoteDto> deleteNote(@PathVariable Long id, HttpServletRequest request){
        Long UserId = getUserId(request);
        noteService.deleteNote(id,UserId);
        return ResponseEntity.noContent().build();
    }
}
