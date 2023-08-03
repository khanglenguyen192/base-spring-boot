package com.example.base.entities.mongo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
public class FileImportData {
    private ObjectId _id;
    private String filePath;
    private String originalFileName;
    private long fileSize;
    private String contentType;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
