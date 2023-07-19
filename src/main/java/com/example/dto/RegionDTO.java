package com.example.dto;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
public class RegionDTO {
    private Integer id;
    private Integer orderNum;
    private String name;
    private String nameUz;
    private String nameRu;
    private String nameEn;
    private Boolean visible;
    private String jwt;
    private LocalDateTime createdDate;

    public RegionDTO() {
    }

    public RegionDTO(Integer id, Integer orderNumber, String name) {
        this.id = id;
        this.orderNum = orderNumber;
        this.name = name;
    }
}
