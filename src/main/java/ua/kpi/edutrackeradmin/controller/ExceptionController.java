package ua.kpi.edutrackeradmin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
@RequestMapping("/error")
public class ExceptionController {
    @GetMapping("/access-denied")
    public ModelAndView accessDenied() {
        return new ModelAndView("error/access_denied", "message", "You don't have access to this page");
    }

}