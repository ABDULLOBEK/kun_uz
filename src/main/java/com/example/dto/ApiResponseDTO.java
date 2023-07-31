package com.example.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponseDTO {
    private boolean status; // false->error; true->good
    private String message;
    private Object data;

    public ApiResponseDTO(boolean status, String message) {
        this.status = status;
        this.message = message;
    }

    public ApiResponseDTO(boolean status, Object data) {
        this.status = status;
        this.data = data;
    }


}
