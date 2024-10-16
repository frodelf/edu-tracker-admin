package ua.kpi.edutrackeradmin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import ua.kpi.edutrackeradmin.elasticsearch.Reindex;

@Controller
@RequiredArgsConstructor
public class MainController {
    private final Reindex reindex;
    @GetMapping({"/", ""})
    public ModelAndView main() {
        return new ModelAndView("redirect:/course");
    }
    @GetMapping("/reindex")
    public ModelAndView reindex() {
        reindex.reindex();
        return new ModelAndView("redirect:/course");
    }
}