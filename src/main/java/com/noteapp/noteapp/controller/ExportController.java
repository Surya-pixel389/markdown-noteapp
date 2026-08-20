package com.noteapp.noteapp.controller;

import com.noteapp.noteapp.service.ExportService;
import com.noteapp.noteapp.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notes")
@RequiredArgsConstructor
public class ExportController {

    private final ExportService exportService;
    private final JwtService jwtService;

    private Long getUserId(HttpServletRequest request) {
        String aheader = request.getHeader("Authorization");
        if (aheader != null && aheader.startsWith("Bearer ")) {
            return jwtService.extractUserId(aheader.substring(7));
        }
        throw new RuntimeException("No token found");
    }

    @GetMapping("/{id}/export/markdown")
    public ResponseEntity<byte[]> exportMarkdown(@PathVariable Long id, HttpServletRequest request) {
        Long userId = getUserId(request);
        byte[] content = exportService.exportAsMarkdown(id, userId);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; file = note.md")
                .header(HttpHeaders.CONTENT_TYPE, "text/markdown")
                .body(content);
    }
}
