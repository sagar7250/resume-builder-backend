package com.resumebuilder.dto;

import lombok.*;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;
    private int statusCode;

    public static <T> ApiResponse<T> success(T data, String message) {
        return ApiResponse.<T>builder().success(true).message(message).data(data).statusCode(200).build();
    }
    public static <T> ApiResponse<T> error(String message, int statusCode) {
        return ApiResponse.<T>builder().success(false).message(message).statusCode(statusCode).build();
    }
}
