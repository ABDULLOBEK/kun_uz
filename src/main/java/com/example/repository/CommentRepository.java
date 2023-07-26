package com.example.repository;

import com.example.entity.CommentEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends CrudRepository<CommentEntity, Long> {

    @Query("SELECT c FROM CommentEntity c WHERE c.article.id = :articleId")
    List<CommentEntity> findByArticleId(@Param("articleId") Long articleId);
}