package com.project.library.Service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.project.library.Entity.Book;
import com.project.library.Repository.BookRepo;
import com.project.library.Repository.IssueRecordRepo;

@Service
public class BookService {

    private final BookRepo bookRepository;
    private final IssueRecordRepo issueRecordRepository;

    public BookService(BookRepo bookRepository,
                       IssueRecordRepo issueRecordRepository) {
        this.bookRepository = bookRepository;
        this.issueRecordRepository = issueRecordRepository;
    }

    public Book addBook(Book book) {

        if (book.getTitle() == null || book.getTitle().isBlank()) {
            throw new RuntimeException("Book title is required");
        }

        if (book.getAuthor() == null || book.getAuthor().isBlank()) {
            throw new RuntimeException("Book author is required");
        }

        if (book.getNumberOfCopies() == null || book.getNumberOfCopies() <= 0) {
            throw new RuntimeException("Number of copies must be greater than zero");
        }

        book.setId(getNextId());

        return bookRepository.save(book);
    }

    public Book updateBook(Integer id, Book updatedBook) {

        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        book.setTitle(updatedBook.getTitle());
        book.setAuthor(updatedBook.getAuthor());
        book.setIsbn(updatedBook.getIsbn());
        book.setCategory(updatedBook.getCategory());
        book.setNumberOfCopies(updatedBook.getNumberOfCopies());

        return bookRepository.save(book);
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Book getBookById(Integer id) {

        return bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found"));
    }

    public List<Book> searchBooks(String title, String author, String category) {

        List<Book> books = bookRepository.findAll();
        List<Book> result = new ArrayList<>();

        for (Book book : books) {

            boolean matches = true;

            if (title != null && !title.isBlank()
                    && !book.getTitle().toLowerCase().contains(title.toLowerCase())) {
                matches = false;
            }

            if (author != null && !author.isBlank()
                    && !book.getAuthor().toLowerCase().contains(author.toLowerCase())) {
                matches = false;
            }

            if (category != null && !category.isBlank()
                    && !book.getCategory().toLowerCase().contains(category.toLowerCase())) {
                matches = false;
            }

            if (matches) {
                result.add(book);
            }
        }

        return result;
    }

    public void deleteBook(Integer id) {

        if (!bookRepository.existsById(id)) {
            throw new RuntimeException("Book not found");
        }

        bookRepository.deleteById(id);
    }

    private Integer getNextId() {

        List<Book> books = bookRepository.findAll();

        Integer maxId = 0;

        for (Book book : books) {
            if (book.getId() > maxId) {
                maxId = book.getId();
            }
        }

        return maxId + 1;
    }
}