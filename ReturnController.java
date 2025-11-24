package com.example.lms.Controllers;

import com.example.lms.Model.BorrowRecord;
import com.example.lms.Model.ReturnRecord;
import com.example.lms.Service.BorrowService;
import com.example.lms.Service.ReturnService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ReturnController {

    private final BorrowService borrowService;
    private final ReturnService returnService;

    public ReturnController(BorrowService borrowService, ReturnService returnService) {
        this.borrowService = borrowService;
        this.returnService = returnService;
    }

    @GetMapping("/return")
    public String returnPage(Model model) {
        model.addAttribute("borrowRecords", borrowService.getAllBorrowRecords());
        model.addAttribute("returnRecord", new ReturnRecord());
        model.addAttribute("returnRecords", returnService.getAllReturnRecords());
        return "return";
    }

    @PostMapping("/return/add")
    public String returnBook(@ModelAttribute ReturnRecord record) {
        BorrowRecord borrowRecord = borrowService.getAllBorrowRecords().stream()
                .filter(b -> b.getStudentId().equals(record.getStudentId())
                        && b.getBookName().equals(record.getBookName()))
                .findFirst()
                .orElse(null);

        if (borrowRecord != null) {
            returnService.returnBook(borrowRecord, record.getReturnDate());
            borrowService.removeBorrowRecord(borrowRecord);
        }

        return "redirect:/return";
    }
}
