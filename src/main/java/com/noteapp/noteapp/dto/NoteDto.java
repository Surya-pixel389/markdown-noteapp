package com.noteapp.noteapp.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NoteDto {
    private Long id;   //fucking id
    private String title;
    private String content;
    private Long parentId;   //fucked parents
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<NoteDto> children;   //offspring
}
