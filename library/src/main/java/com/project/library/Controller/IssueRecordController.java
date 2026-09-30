package com.project.library.Controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.library.Entity.IssueRecord;
import com.project.library.Service.IssueRecordService;

@RestController
@RequestMapping("/api/issues")
public class IssueRecordController {

    private final IssueRecordService issueRecordService;

    public IssueRecordController(IssueRecordService issueRecordService) {
        this.issueRecordService = issueRecordService;
    }

    @PostMapping("/issue")
    public IssueRecord issueBook(@RequestBody IssueRecord issueRecord) {
        return issueRecordService.issueBook(issueRecord);
    }

    @PostMapping("/return/{id}")
    public IssueRecord returnBook(@PathVariable Integer id) {
        return issueRecordService.returnBook(id);
    }

    @GetMapping("/all")
    public List<IssueRecord> getAllIssueRecords() {
        return issueRecordService.getAllIssueRecords();
    }

    @GetMapping("/get/{id}")
    public IssueRecord getIssueRecordById(@PathVariable Integer id) {
        return issueRecordService.getIssueRecordById(id);
    }

    @GetMapping("/student/{studentId}")
    public List<IssueRecord> getIssuedBooksByStudent(
            @PathVariable Integer studentId) {

        return issueRecordService.getIssuedBooksByStudent(studentId);
    }

    @GetMapping("/issued")
    public List<IssueRecord> getIssuedBooks() {
        return issueRecordService.getIssuedBooks();
    }

    @DeleteMapping("/delete/{id}")
    public String deleteIssueRecord(@PathVariable Integer id) {

        issueRecordService.deleteIssueRecord(id);

        return "Issue record deleted successfully";
    }
}