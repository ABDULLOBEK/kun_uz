package com.example.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class CommentDTO {
    private Long id;
    private Date createdDate;
    private Date updateDate;
    private Long profileId;
    private String content;
    private String articleId;
    private Long replyId;
    private boolean visible;
}
