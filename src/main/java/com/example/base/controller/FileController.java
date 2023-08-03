package com.example.base.controller;

import com.example.base.constants.Constant;
import com.example.base.dto.FileDTO;
import com.example.base.dto.FileInfoDTO;
import com.example.base.entities.mongo.FileImportData;
import com.example.base.service.AmazonS3Service;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.MDC;
import org.springframework.core.env.Environment;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/file")
public class FileController {
    private final Environment env;
    private final HttpServletRequest httpServletRequest;
    private final AmazonS3Service amazonS3Service;

    @PostMapping(value = "/import-file-to-amazon-s3", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> importFileToStoreS3(
            @RequestParam(value = "attachmentFile", required = false)MultipartFile attachmentFile
            ) {
        String uri = httpServletRequest.getRequestURI();
        log.info("Process upload file from uri {}", uri);
        String exchangeId = UUID.randomUUID().toString();
        MDC.put(Constant.EXCHANGE_ID, exchangeId);
        FileImportData result;
        HttpStatus httpStatus;
        try {
            FileInfoDTO fileDTO = new FileInfoDTO();
            fileDTO.setAttachmentFile(attachmentFile);
            fileDTO.setFileName(attachmentFile.getOriginalFilename());
            fileDTO.setContentType(attachmentFile.getContentType());

            result = amazonS3Service.uploadFile(fileDTO);
            if (ObjectUtils.isNotEmpty(result)) {
                httpStatus = HttpStatus.OK;
            } else {
                httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            }
        } catch (Exception ex) {
            result = null;
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            log.error("Upload file to amazone s3 failed {}", ex.getMessage());
            ex.printStackTrace();
        } finally {
            MDC.clear();
        }

        return new ResponseEntity<>(result, httpStatus);
    }

    @GetMapping(value = "/get-list-s3-bucket", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<String>> getListS3Bucket() {
        String uri = httpServletRequest.getRequestURI();
        log.info("get-list-s3-bucket from uri {}", uri);
        String exchangeId = UUID.randomUUID().toString();
        MDC.put(Constant.EXCHANGE_ID, exchangeId);
        List<String> result = null;
        HttpStatus httpStatus;
        try {
            result = amazonS3Service.getListBucket();
            httpStatus = HttpStatus.OK;
        } catch (Exception ex) {
            result = null;
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            log.error("getListBucket from amazone s3 failed {}", ex.getMessage());
            ex.printStackTrace();
        } finally {
            MDC.clear();
        }

        return new ResponseEntity<>(result, httpStatus);
    }

    @GetMapping(value = "/get-s3-object-by-key", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Resource> getFile(@RequestParam String key) {
        String uri = httpServletRequest.getRequestURI();
        log.info("get-file-by-key from uri {}", uri);
        String exchangeId = UUID.randomUUID().toString();
        MDC.put(Constant.EXCHANGE_ID, exchangeId);
        HttpStatus httpStatus;
        Resource resource = null;
        try {
            FileDTO fileDTO = amazonS3Service.getS3ObjectFile(key);

            if (ObjectUtils.isNotEmpty(fileDTO)) {
                String returnFileName = fileDTO.getName();
                if (ObjectUtils.isNotEmpty(fileDTO.getOriginalfileName()))
                    returnFileName = fileDTO.getOriginalfileName();
                httpStatus = HttpStatus.OK;
                resource = new InputStreamResource(fileDTO.getInputStream());

                return ResponseEntity.ok()
                        .contentLength(fileDTO.getFileSize())
                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + returnFileName + "\"")
                        .body(resource);
            } else {
                httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            }
        } catch (Exception ex) {
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            log.error("get-file-by-key from amazone s3 failed {}", ex.getMessage());
            ex.printStackTrace();
        } finally {
            MDC.clear();
        }

        return new ResponseEntity<>(null, httpStatus);
    }

    @GetMapping(value = "/get-list-s3-object", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<String>> getListS3Object(@RequestParam String bucketName) {
        String uri = httpServletRequest.getRequestURI();
        log.info("get-list-s3-bucket from uri {}", uri);
        String exchangeId = UUID.randomUUID().toString();
        MDC.put(Constant.EXCHANGE_ID, exchangeId);
        List<String> result = null;
        HttpStatus httpStatus;
        try {
            result = amazonS3Service.getListObject(bucketName);
            httpStatus = HttpStatus.OK;
        } catch (Exception ex) {
            result = null;
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            log.error("getListBucket from amazone s3 failed {}", ex.getMessage());
            ex.printStackTrace();
        } finally {
            MDC.clear();
        }

        return new ResponseEntity<>(result, httpStatus);
    }
}
