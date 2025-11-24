package com.example.lms.Controllers;

import com.example.lms.Service.BookService;
import com.example.lms.Service.MemberService;
import com.example.lms.Service.StuffService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    private final BookService bookService;
    private final MemberService memberService;
    private final StuffService staffService;

    public DashboardController(BookService bookService, MemberService memberService,
                               StuffService staffService) {
        this.bookService = bookService;
        this.memberService = memberService;
        this.staffService = staffService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {

        model.addAttribute("totalBooks", bookService.countAllBooks());
        model.addAttribute("totalMembers", memberService.countAllMembers());
        model.addAttribute("totalStaff", staffService.countAllStuffs());

        return "dashboard";
    }
}
