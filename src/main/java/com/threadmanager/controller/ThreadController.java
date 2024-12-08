package com.threadmanager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import com.threadmanager.service.ThreadManagerService;
import com.threadmanager.model.ThreadInfo;

@RestController
@RequestMapping("/api/threads")
@CrossOrigin(origins = "http://localhost:3000")
public class ThreadController {
    private final ThreadManagerService threadManagerService;
    
    @Autowired
    public ThreadController(ThreadManagerService threadManagerService) {
        this.threadManagerService = threadManagerService;
    }

    @GetMapping
    public ResponseEntity<List<ThreadInfo>> getAllThreads() {
        try {
            List<ThreadInfo> threads = threadManagerService.getAllThreadsInfo();
            return ResponseEntity.ok(threads);
        } catch (Exception e) {
            System.out.println("Error while fetching threads: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
    
    
    // /api/threads/create?senderCount=5&receiverCount=3
    @PostMapping("/create")
    public ResponseEntity<String> createThreads(
        @RequestParam int senderCount,
        @RequestParam int receiverCount
    ) {
        try {
            threadManagerService.createThreads(senderCount, receiverCount);
            return ResponseEntity.ok("Threads created successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Thread creation failed: " + e.getMessage());
        }
    }
    
    // /api/threads/active
    @GetMapping("/active")
    public ResponseEntity<List<ThreadInfo>> getActiveThreads() {
        try {
            List<ThreadInfo> threads = threadManagerService.getActiveThreadsInfo();
            return ResponseEntity.ok(threads);
        } catch (Exception e) {
            System.out.println("Error while fetching active threads: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    // /api/threads/{threadId}?active=true
    @PutMapping("/{threadId}/active")
    public ResponseEntity<String> updateThreadStatus(
            @PathVariable Long threadId,
            @RequestParam boolean active) {
        try {
            threadManagerService.updateThreadStatus(threadId, active);
            String status = active ? "resumed" : "paused";
            return ResponseEntity.ok("Thread " + status + " successfully");
        } catch (Exception e) {
            System.out.println("Error while updating thread status: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating thread status: " + e.getMessage());
        }
    }

    // /api/threads/{threadId}?priority=1
    @PutMapping("/{threadId}/priority")
    public ResponseEntity<String> updatePriority(
        @PathVariable Long threadId,
        @RequestParam int priority
    ) {
        try {
            threadManagerService.updateThreadPriority(threadId, priority);
            return ResponseEntity.ok("Thread priority updated successfully");
        } catch (Exception e) {
            System.out.println("Error while updating thread priority: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping
    // /api/threads 
    public ResponseEntity<String> deleteAllThreads() {
        try {
            threadManagerService.deleteAllThreads();
            return ResponseEntity.ok("All threads deleted successfully");
        } catch (Exception e) {
            System.out.println("Error while deleting threads: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting threads: " + e.getMessage());
        }
    }
} 