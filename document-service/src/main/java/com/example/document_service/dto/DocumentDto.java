package com.example.document_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class DocumentDto {

    private String documentDtoId;
    private String content;
    private String userId;
    private String title;
    private Boolean available;
}
