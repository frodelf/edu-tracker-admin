package ua.kpi.edutrackeradmin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MainController {
    @GetMapping({"/", ""})
    public ModelAndView main() {
        return new ModelAndView("redirect:/course");
    }
}