package com.example.base.service.Impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.example.base.dto.FileDTO;
import com.example.base.dto.FileInfoDTO;
import com.example.base.service.AmazonS3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class AmazonS3ServiceImpl implements AmazonS3Service {
    private final AmazonS3 amazonS3;

    @Value("${amazon.s3.bucket.sample}")
    private String sampleBucket;

    @Override
    public Boolean uploadFile(FileInfoDTO fileDTO) {
        try {
            String bucketName = sampleBucket;
            String fileName = fileDTO.getFileName();
            MultipartFile attachmentFile = fileDTO.getAttachmentFile();

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(attachmentFile.getSize());
            metadata.setContentType(attachmentFile.getContentType());

            InputStream inputStream = attachmentFile.getInputStream();
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, fileName, inputStream, metadata);
            amazonS3.putObject(putObjectRequest);
            return Boolean.TRUE;
        } catch (Exception ex) {
            log.error("upload file error {}", ex.getMessage());
            ex.printStackTrace();
            return Boolean.FALSE;
        }
    }

    @Override
    public FileDTO getS3ObjectFile(String fileName) {
        try {
            FileDTO fileDTO = new FileDTO();
            S3Object s3Object = amazonS3.getObject(sampleBucket, fileName);
            fileDTO.setContentType(s3Object.getKey());
            fileDTO.setInputStream(s3Object.getObjectContent());
            fileDTO.setName(s3Object.getKey());
            return fileDTO;
        } catch (Exception ex) {
            log.error("getS3ObjectFile failed {}", ex.getMessage());
            return null;
        }
    }
}
