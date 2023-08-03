package com.example.base.service.Impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.example.base.dto.FileDTO;
import com.example.base.dto.FileInfoDTO;
import com.example.base.entities.mongo.FileImportData;
import com.example.base.repository.mongo.FileImportDataRepository;
import com.example.base.service.AmazonS3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AmazonS3ServiceImpl implements AmazonS3Service {
    private static String FOLDER = "sample/";

    private final AmazonS3 amazonS3;

    private final FileImportDataRepository fileImportDataRepository;

    @Value("${amazon.s3.bucket.sample}")
    private String sampleBucket;

    @Override
    public FileImportData uploadFile(FileInfoDTO fileDTO) {
        try {
            String bucketName = sampleBucket;
            String fileName = fileDTO.getFileName();
            MultipartFile attachmentFile = fileDTO.getAttachmentFile();

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(attachmentFile.getSize());
            metadata.setContentType(attachmentFile.getContentType());

            InputStream inputStream = attachmentFile.getInputStream();

            String key = FOLDER + UUID.randomUUID() + "-" + fileName;

            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, inputStream, metadata);
            PutObjectResult result = amazonS3.putObject(putObjectRequest);

            if (ObjectUtils.isNotEmpty(result)) {
                FileImportData data = new FileImportData();
                data.setFilePath(key);
                data.setOriginalFileName(fileName);
                data.setFileSize(attachmentFile.getSize());
                data.setContentType(attachmentFile.getContentType());
                data.setCreatedDate(LocalDateTime.now());

                fileImportDataRepository.insert(data);

                return data;
            }

            return null;
        } catch (Exception ex) {
            log.error("upload file error {}", ex.getMessage());
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public FileDTO getS3ObjectFile(String key) {
        FileDTO fileDTO = null;
        try {
            S3Object s3Object = amazonS3.getObject(sampleBucket, key);

            if (ObjectUtils.isNotEmpty(s3Object)) {
                fileDTO = new FileDTO();
                fileDTO.setInputStream(s3Object.getObjectContent());
                fileDTO.setName(s3Object.getKey());
                if (ObjectUtils.isNotEmpty(s3Object.getObjectMetadata())) {
                    fileDTO.setContentType(s3Object.getObjectMetadata().getContentType());
                    fileDTO.setFileSize(s3Object.getObjectMetadata().getContentLength());
                }
                FileImportData fileImportData = fileImportDataRepository.findByFilePath(key);
                if (ObjectUtils.isNotEmpty(fileImportData)) {
                    fileDTO.setOriginalfileName(fileImportData.getOriginalFileName());
                }
            }
        } catch (Exception ex) {
            log.error("getS3ObjectFile failed {}", ex.getMessage());
        }

        return fileDTO;
    }

    @Override
    public List<String> getListBucket() {
        List<String> result = new ArrayList<>();
        List<Bucket> buckets = amazonS3.listBuckets();

        if (ObjectUtils.isEmpty(buckets))
            return result;

        for(Bucket bucket : buckets) {
            result.add(bucket.getName());
        }

        return result;
    }

    @Override
    public List<String> getListObject(String bucketName) {
        List<String> result = new ArrayList<>();
        ObjectListing objectListing = amazonS3.listObjects(bucketName);

        if (ObjectUtils.isEmpty(objectListing))
            return result;

        for(S3ObjectSummary os : objectListing.getObjectSummaries()) {
            result.add(os.getKey());
        }
        return result;
    }
}
