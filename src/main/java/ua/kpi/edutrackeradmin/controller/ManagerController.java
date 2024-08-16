package ua.kpi.edutrackeradmin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import ua.kpi.edutrackeradmin.service.ManagerService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/manager")
public class ManagerController {
    private final ManagerService managerService;

    @GetMapping
    public ModelAndView index() {
        return new ModelAndView("manager/index");
    }
}