package com.example.mapper;

import java.time.LocalDateTime;

public interface ArticleShortInfoIMapper {
    String getId();

    String getTitle();

    String getDescription();

    String getImageId();

    LocalDateTime publishedDate();
}
