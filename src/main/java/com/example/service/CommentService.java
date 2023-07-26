package com.example.service;

import com.example.dto.CommentDTO;
import com.example.entity.ArticleEntity;
import com.example.entity.CommentEntity;
import com.example.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;

    public CommentDTO createComment(CommentDTO commentDTO) {
        CommentEntity comment = new CommentEntity();
        comment.setContent(commentDTO.getContent());

        ArticleEntity article = new ArticleEntity();
        article.setId(commentDTO.getArticleId());
        comment.setArticle(article);

        if (commentDTO.getReplyId() != null) {
            CommentEntity reply = commentRepository.findById(commentDTO.getReplyId()).orElse(null);
            comment.setReply(reply);
        }

        // Set other fields like createdDate, profile, etc.

        comment = commentRepository.save(comment);

        return mapCommentToDTO(comment);
    }

    public CommentDTO updateComment(Long id, CommentDTO commentDTO) {
        CommentEntity existingComment = commentRepository.findById(id).orElse(null);

        if (existingComment == null) {
            return null; // You may handle this differently based on your application's requirements
        }

        // Check if the authenticated user owns the comment
//        boolean isOwner = isCommentOwner(authenticatedUserId, existingComment);

//        if (!isOwner) {
//            return null; // You may handle this differently based on your application's requirements
//        }

        existingComment.setContent(commentDTO.getContent());

        ArticleEntity article = new ArticleEntity();
        article.setId(commentDTO.getArticleId());
        existingComment.setArticle(article);

        commentRepository.save(existingComment);

        return mapCommentToDTO(existingComment);
    }

    public boolean deleteComment(Long id) {
        CommentEntity existingComment = commentRepository.findById(id).orElse(null);

        if (existingComment == null) {
            return false;
        }

        // Check if the authenticated user is an ADMIN or the owner of the comment
//        boolean isAdmin = /* Implement ADMIN check based on your authentication mechanism */;
//        boolean isOwner = isCommentOwner(authenticatedUserId, existingComment);

//        if (isAdmin || isOwner) {
//            commentRepository.delete(existingComment);
//            return true;
//        }

        return false;
    }

    public List<CommentDTO> getArticleComments(Long articleId) {
        List<CommentEntity> comments = commentRepository.findByArticleId(articleId);

        return comments.stream()
                .map(this::mapCommentToDTO)
                .collect(Collectors.toList());
    }

    private CommentDTO mapCommentToDTO(CommentEntity comment) {
        CommentDTO dto = new CommentDTO();
        // Map relevant fields from the comment entity to the DTO
        // Add mapping for id, createdDate, updateDate, profile.id, profile.name, profile.surname, etc.
        return dto;
    }


}
