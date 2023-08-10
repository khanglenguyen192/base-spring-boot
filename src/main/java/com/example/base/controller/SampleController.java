package com.example.base.controller;

import com.example.base.constants.Constant;
import com.example.base.entities.Student;
import com.example.base.service.StudentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/sample")
public class SampleController {
    private final HttpServletRequest httpServletRequest;
    private final StudentService studentService;

    @GetMapping(value = "/get-students", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Student>> getStudents() {
        String uri = httpServletRequest.getRequestURI();
        log.info("getStudents from uri {}", uri);
        String exchangeId = UUID.randomUUID().toString();
        MDC.put(Constant.EXCHANGE_ID, exchangeId);
        List<Student> result = null;
        HttpStatus httpStatus;
        try {
            result = studentService.selectAllStudent();
            httpStatus = HttpStatus.OK;
        } catch (Exception ex) {
            result = null;
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            log.error("getStudents failed {}", ex.getMessage());
            ex.printStackTrace();
        } finally {
            MDC.clear();
        }

        return new ResponseEntity<>(result, httpStatus);
    }
}
