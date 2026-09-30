package com.project.library.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.library.Entity.IssueRecord;

public interface IssueRecordRepo extends JpaRepository<IssueRecord, Integer> {

}