package com.gohul.springSecurity.notice;

import org.aspectj.weaver.ast.Not;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NoticeRepo extends JpaRepository<Notice,Long> {

    @Query(value = "from Notice n where CURDATE() BETWEEN noticeBeginDate AND noticeEndDate")
    List<Notice> findAllActiveNotes();
}
