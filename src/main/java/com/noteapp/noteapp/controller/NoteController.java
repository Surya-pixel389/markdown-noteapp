package com.noteapp.noteapp.controller;

import com.noteapp.noteapp.dto.NoteDto;
import com.noteapp.noteapp.service.JwtService;
import com.noteapp.noteapp.service.NoteService;
import com.noteapp.noteapp.service.TagService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notes")
@CrossOrigin(origins = "http://localhost:3000")
public class NoteController {

    private final NoteService noteService;
    private final JwtService jwtService;
    private final TagService tagService;

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
        NoteDto note = noteService.getNoteById(id, userId);
        return ResponseEntity.ok(note);
    }

    @GetMapping("/search")
    public ResponseEntity<List<NoteDto>> searchNotes(@RequestParam String query,  HttpServletRequest request){
        Long userId = getUserId(request);
        return  ResponseEntity.ok(noteService.searchNotes(userId, query));
    }
    @GetMapping
    public ResponseEntity<List<NoteDto>> getRootNodes(HttpServletRequest request){
        Long userId = getUserId(request);
        List<NoteDto> notes = noteService.getUserRootNotes(userId);
        return ResponseEntity.ok(notes);
    }
    @GetMapping("/{id}/children")
    public ResponseEntity<List<NoteDto>> getChildNotes(@PathVariable Long id, HttpServletRequest request){
        Long userId = getUserId(request);
        List<NoteDto> child = noteService.getNoteChildren( id, userId);
        return ResponseEntity.ok(child);
    }
    @PutMapping("/{id}")
    public ResponseEntity<NoteDto> updateNote(@PathVariable Long id, @RequestBody NoteDto noteDto, HttpServletRequest request){
        Long userId = getUserId(request);
        NoteDto updated =  noteService.updateNote( id,userId, noteDto);
        return ResponseEntity.ok(updated);
    }

    @PostMapping("/{id}/tags")
    public ResponseEntity<Void> addTag(@PathVariable Long id, @RequestBody Map<String, String> body, HttpServletRequest request){
        Long userId = getUserId(request);
        tagService.addTag(id, userId, body.get("name"));
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/tags/{tagsId}")
    public ResponseEntity<Void> deleteTag(@PathVariable Long id, @PathVariable Long tagsId, HttpServletRequest request){
        Long userId = getUserId(request);
        tagService.deleteTag(id, userId, tagsId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<NoteDto> deleteNote(@PathVariable Long id, HttpServletRequest request){
        Long UserId = getUserId(request);
        noteService.deleteNote(id,UserId);
        return ResponseEntity.noContent().build();
    }
}
