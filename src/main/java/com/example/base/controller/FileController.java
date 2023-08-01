package com.example.base.controller;

import com.example.base.constants.Constant;
import com.example.base.dto.FileInfoDTO;
import com.example.base.service.AmazonS3Service;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.InputStream;
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
            @RequestParam(value = "attrachmentFile", required = false)MultipartFile attachmentFile
            ) {
        String uri = httpServletRequest.getRequestURI();
        log.info("Process upload file from uri {}", uri);
        String exchangeId = UUID.randomUUID().toString();
        MDC.put(Constant.EXCHANGE_ID, exchangeId);
        Boolean result;
        HttpStatus httpStatus;
        try {
            FileInfoDTO fileDTO = new FileInfoDTO();
            fileDTO.setAttachmentFile(attachmentFile);
            fileDTO.setFileName(attachmentFile.getOriginalFilename());
            fileDTO.setDirectory("sample");
            fileDTO.setContentType(attachmentFile.getContentType());

            result = amazonS3Service.uploadFile(fileDTO);
            if (result) {
                httpStatus = HttpStatus.OK;
            } else {
                httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            }
        } catch (Exception ex) {
            result = Boolean.FALSE;
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            log.error("Upload file to amazone s3 failed {}", ex.getMessage());
            ex.printStackTrace();
        } finally {
            MDC.clear();
        }

        return new ResponseEntity<>(result, httpStatus);
    }
}
