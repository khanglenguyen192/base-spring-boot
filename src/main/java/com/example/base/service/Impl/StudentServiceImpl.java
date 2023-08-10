package com.example.base.service.Impl;

import com.example.base.entities.Student;
import com.example.base.mapper.StudentMapper;
import com.example.base.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {
    private final StudentMapper studentMapper;

    @Override
    public int insertStudent(Student student) {
        return studentMapper.insertStudent(student);
    }

    @Override
    public int updateStudent(Student student) {
        return studentMapper.updateStudent(student);
    }

    @Override
    public int deleteStudentById(int studentId) {
        return studentMapper.deleteStudentById(studentId);
    }

    @Override
    public List<Student> selectAllStudent() {
        return studentMapper.selectAllStudent();
    }

    @Override
    public Student selectStudentById(int studentId) {
        return studentMapper.selectStudentById(studentId);
    }
}
