package com.example.lms.Controllers;

import com.example.lms.Model.BorrowRecord;
import com.example.lms.Service.BorrowService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class BorrowController {

    private final BorrowService borrowService;

    public BorrowController(BorrowService borrowService) {
        this.borrowService = borrowService;
    }

    @GetMapping("/borrow")
    public String borrowPage(Model model) {
        model.addAttribute("borrowRecords", borrowService.getAllBorrowRecords());
        model.addAttribute("borrowRecord", new BorrowRecord());
        return "borrow";
    }

    @PostMapping("/borrow/add")
    public String addBorrow(@ModelAttribute BorrowRecord record, Model model) {
        boolean success = Boolean.parseBoolean(borrowService.addBorrowRecord(record));
        if (!success) {
            model.addAttribute("error", "Student cannot borrow more than 3 books!");
        }
        return "redirect:/borrow";
    }
}
