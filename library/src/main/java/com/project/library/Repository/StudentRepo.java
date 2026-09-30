package com.project.library.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.library.Entity.Student;

public interface StudentRepo extends JpaRepository<Student, Integer> {

}