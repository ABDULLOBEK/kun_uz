package com.example.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CategoryDTO {
    //    id,order_number,name_uz, name_ru, name_en,visible,created_date
    private Integer id;
    private Integer orderNum;
    private String name;
    private String nameUz;
    private String nameEn;
    private String nameRu;
    private Boolean visible;
    private String jwt;
    private LocalDateTime createdDate;

    public CategoryDTO() {
    }

    public CategoryDTO(Integer id, Integer orderNum, String name) {
        this.id = id;
        this.orderNum = orderNum;
        this.name = name;
    }
}
