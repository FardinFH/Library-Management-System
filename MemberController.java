package com.example.lms.Controllers;

import com.example.lms.Model.Member;
import com.example.lms.Service.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/members")
    public String membersPage(Model model) {
        model.addAttribute("members", memberService.getAllMembers());
        model.addAttribute("member", new Member());
        return "members";
    }

    @PostMapping("/members/add")
    public String addMember(@ModelAttribute Member member) {
        memberService.addMember(member);
        return "redirect:/members";
    }
}
