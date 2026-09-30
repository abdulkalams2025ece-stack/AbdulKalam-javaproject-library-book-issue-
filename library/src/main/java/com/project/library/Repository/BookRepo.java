package com.project.library.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.library.Entity.Book;

public interface BookRepo extends JpaRepository<Book, Integer> {

}