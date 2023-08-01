package com.example.base.service;

import com.example.base.dto.FileDTO;
import com.example.base.dto.FileInfoDTO;

import java.io.File;

public interface AmazonS3Service {
    Boolean uploadFile(FileInfoDTO fileDTO);

    FileDTO getS3ObjectFile(String fileName);
}
