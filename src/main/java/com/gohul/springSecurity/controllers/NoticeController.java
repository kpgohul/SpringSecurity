package com.gohul.springSecurity.controllers;


import com.gohul.springSecurity.notice.Notice;
import com.gohul.springSecurity.notice.NoticeRepo;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Not;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.SecureRandom;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("notices")
@RequiredArgsConstructor
public class NoticeController {
    private final NoticeRepo repo;
    private static final SecureRandom random = new SecureRandom();

    @GetMapping("/notice")
    public ResponseEntity<List<Notice>> getMyAccount()
    {
        List<Notice> notices = repo.findAllActiveNotes();
        if (notices != null) {
            return ResponseEntity.ok()
                    .cacheControl(CacheControl.maxAge(60, TimeUnit.SECONDS))
                    .body(notices);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
    @PostMapping("/notice")
    public ResponseEntity<?> saveNotice(@RequestBody Notice notice)
    {
        notice.setNoticeId(generateUniqueNoticeNumber());
        return ResponseEntity.ok(repo.save(notice).getNoticeId());
    }

    private Long generateUniqueNoticeNumber() {
        long number;
        do {
            number = 1000000000000L + (Math.abs(random.nextLong()) % 9000000000000L);
        } while (repo.existsById(number)); // Retry if exists
        return number;
    }
}
