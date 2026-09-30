package com.project.library.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.project.library.Entity.Student;
import com.project.library.Repository.StudentRepo;

@Service
public class StudentService {

    private final StudentRepo studentRepository;

    public StudentService(StudentRepo studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student addStudent(Student student) {

        if (student.getName() == null || student.getName().isBlank()) {
            throw new RuntimeException("Student name is required");
        }

        student.setId(getNextId());

        return studentRepository.save(student);
    }

    public Student updateStudent(Integer id, Student updatedStudent) {

        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        student.setName(updatedStudent.getName());
        student.setEmail(updatedStudent.getEmail());
        student.setPhone(updatedStudent.getPhone());

        return studentRepository.save(student);
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Student getStudentById(Integer id) {

        return studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found"));
    }

    public void deleteStudent(Integer id) {

        if (!studentRepository.existsById(id)) {
            throw new RuntimeException("Student not found");
        }

        studentRepository.deleteById(id);
    }

    private Integer getNextId() {

        List<Student> students = studentRepository.findAll();

        Integer maxId = 0;

        for (Student student : students) {
            if (student.getId() > maxId) {
                maxId = student.getId();
            }
        }

        return maxId + 1;
    }
}