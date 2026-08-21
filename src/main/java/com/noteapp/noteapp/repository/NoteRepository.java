// repository handles Save, Find, update ,delete

package com.noteapp.noteapp.repository;

import com.noteapp.noteapp.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NoteRepository extends JpaRepository<Note,Long> {
    List<Note> findByUser_Id(Long userId);

    List<Note> findByUser_IdAndParentIsNull(Long userId);  //Root level notes only, sidebar top view

   Optional<Note> findByIdAndUser_Id(Long id, Long userId);    //one note safely

    List<Note> findByParent_IdAndUser_Id(Long parentId, Long userId);  //children of note

    @Query("SELECT n FROM Note n WHERE n.user.id = :userId AND" +
            "(LOWER(n.title) LIKE LOWER (CONCAT('%', :keyword, '%')) OR " +
            "LOWER(n.content) LIKE LOWER (CONCAT('%', :keyword, '%')))")
    List<Note> searchNotes(@Param("userId") Long userId, @Param("keyword") String keyword);

}
