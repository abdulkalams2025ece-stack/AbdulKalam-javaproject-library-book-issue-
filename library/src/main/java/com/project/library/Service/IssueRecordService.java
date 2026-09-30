package com.project.library.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.project.library.Entity.Book;
import com.project.library.Entity.IssueRecord;
import com.project.library.Entity.Student;
import com.project.library.Repository.BookRepo;
import com.project.library.Repository.IssueRecordRepo;
import com.project.library.Repository.StudentRepo;

@Service
public class IssueRecordService {

    private static final double FINE_PER_DAY = 10.0;

    private final IssueRecordRepo issueRecordRepository;
    private final BookRepo bookRepository;
    private final StudentRepo studentRepository;

    public IssueRecordService(IssueRecordRepo issueRecordRepository,
                              BookRepo bookRepository,
                              StudentRepo studentRepository) {

        this.issueRecordRepository = issueRecordRepository;
        this.bookRepository = bookRepository;
        this.studentRepository = studentRepository;
    }

    public IssueRecord issueBook(IssueRecord issueRecord) {

        if (issueRecord.getBookId() == null) {
            throw new RuntimeException("Book ID is required");
        }

        if (issueRecord.getStudentId() == null) {
            throw new RuntimeException("Student ID is required");
        }

        Book book = bookRepository.findById(issueRecord.getBookId())
                .orElseThrow(() -> new RuntimeException("Book not found"));

        Student student = studentRepository.findById(issueRecord.getStudentId())
                .orElseThrow(() -> new RuntimeException("Student not found"));

        List<IssueRecord> records = issueRecordRepository.findAll();

        int issuedCopies = 0;

        for (IssueRecord record : records) {

            if (record.getBookId().equals(book.getId())
                    && "ISSUED".equalsIgnoreCase(record.getStatus())) {

                issuedCopies++;
            }
        }

        if (issuedCopies >= book.getNumberOfCopies()) {
            throw new RuntimeException("All copies of this book are currently issued");
        }

        LocalDate issueDate;

        if (issueRecord.getIssueDate() == null
                || issueRecord.getIssueDate().isBlank()) {

            issueDate = LocalDate.now();
        } else {
            issueDate = LocalDate.parse(issueRecord.getIssueDate());
        }

        LocalDate dueDate = issueDate.plusDays(14);

        issueRecord.setId(getNextId());
        issueRecord.setIssueDate(issueDate.toString());
        issueRecord.setDueDate(dueDate.toString());
        issueRecord.setReturnDate(null);
        issueRecord.setFine(0.0);
        issueRecord.setStatus("ISSUED");

        return issueRecordRepository.save(issueRecord);
    }

    public IssueRecord returnBook(Integer id) {

        IssueRecord record = issueRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Issue record not found"));

        if (!"ISSUED".equalsIgnoreCase(record.getStatus())) {
            throw new RuntimeException("Book is already returned");
        }

        LocalDate returnDate = LocalDate.now();
        LocalDate dueDate = LocalDate.parse(record.getDueDate());

        double fine = 0.0;

        if (returnDate.isAfter(dueDate)) {

            long overdueDays = ChronoUnit.DAYS.between(dueDate, returnDate);

            fine = overdueDays * FINE_PER_DAY;
        }

        record.setReturnDate(returnDate.toString());
        record.setFine(fine);
        record.setStatus("RETURNED");

        return issueRecordRepository.save(record);
    }

    public List<IssueRecord> getAllIssueRecords() {
        return issueRecordRepository.findAll();
    }

    public IssueRecord getIssueRecordById(Integer id) {

        return issueRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Issue record not found"));
    }

    public List<IssueRecord> getIssuedBooksByStudent(Integer studentId) {

        if (!studentRepository.existsById(studentId)) {
            throw new RuntimeException("Student not found");
        }

        List<IssueRecord> records = issueRecordRepository.findAll();
        List<IssueRecord> result = new ArrayList<>();

        for (IssueRecord record : records) {

            if (record.getStudentId().equals(studentId)
                    && "ISSUED".equalsIgnoreCase(record.getStatus())) {

                result.add(record);
            }
        }

        return result;
    }

    public List<IssueRecord> getIssuedBooks() {

        List<IssueRecord> records = issueRecordRepository.findAll();
        List<IssueRecord> result = new ArrayList<>();

        for (IssueRecord record : records) {

            if ("ISSUED".equalsIgnoreCase(record.getStatus())) {
                result.add(record);
            }
        }

        return result;
    }

    public void deleteIssueRecord(Integer id) {

        if (!issueRecordRepository.existsById(id)) {
            throw new RuntimeException("Issue record not found");
        }

        issueRecordRepository.deleteById(id);
    }

    private Integer getNextId() {

        List<IssueRecord> records = issueRecordRepository.findAll();

        Integer maxId = 0;

        for (IssueRecord record : records) {

            if (record.getId() > maxId) {
                maxId = record.getId();
            }
        }

        return maxId + 1;
    }
}