package com.pepic.TravelPlanner.repositories;

import com.pepic.TravelPlanner.models.Comment;
import com.pepic.TravelPlanner.models.Trip;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;

public interface CommentRepository extends CrudRepository<Comment, Long> {

    @Query(value = "SELECT * FROM comments " +
            "WHERE trip_id = :tripId", nativeQuery = true)
    Collection<Comment> getAllCommentsForTrip(Long tripId);

    @Query(value = "SELECT * FROM comments " +
            "WHERE user_id = :username " +
            "AND INSTR('content', ':content') > 0", nativeQuery = true)
    Collection<Comment> getAllCommentsContaining(String username, String content);

    @Query(value = "DELETE * FROM comments " +
            "WHERE user_id = :username " +
            "AND id = :id", nativeQuery = true)
    void deleteByIdForUser(Long id, String username);
}
