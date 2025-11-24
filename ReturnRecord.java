package com.example.lms.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReturnRecord {
    private String studentName;
    private String studentId;
    private String batch;
    private String department;
    private String phone;
    private String bookName;
    private String authorName;
    private int quantity;
    private String borrowDate;
    private String returnDate;
    private int penalty;
}
