package com.example.base.service;

import com.example.base.dto.FileDTO;
import com.example.base.dto.FileInfoDTO;
import com.example.base.entities.mongo.FileImportData;

import java.io.File;
import java.util.List;

public interface AmazonS3Service {
    FileImportData uploadFile(FileInfoDTO fileDTO);

    FileDTO getS3ObjectFile(String key);

    List<String> getListBucket();

    List<String> getListObject(String bucketName);
}
