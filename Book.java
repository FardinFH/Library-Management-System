package com.example.lms.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    private String id;
    private String vendorName;
    private String title;
    private String author;
    private int quantity;
    private double price;
}
