package com.example.lms.Controllers;

import com.example.lms.Model.Stuff;
import com.example.lms.Service.StuffService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class StuffController {

    private final StuffService stuffService;

    public StuffController(StuffService stuffService) {
        this.stuffService = stuffService;
    }

    @GetMapping("/stuff")
    public String stuffPage(Model model) {
        model.addAttribute("stuffs", stuffService.getAllStuffs());
        model.addAttribute("stuff", new Stuff());
        return "stuff";
    }

    @PostMapping("/stuff/add")
    public String addStuff(@ModelAttribute Stuff stuff) {
        stuffService.addStuff(stuff);
        return "redirect:/stuff";
    }
}
