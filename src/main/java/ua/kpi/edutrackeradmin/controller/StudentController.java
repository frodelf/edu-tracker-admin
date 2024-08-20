package ua.kpi.edutrackeradmin.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import ua.kpi.edutrackeradmin.dto.ForSelect2Dto;
import ua.kpi.edutrackeradmin.dto.student.StudentRequestForFilter;
import ua.kpi.edutrackeradmin.dto.student.StudentResponseForViewAll;
import ua.kpi.edutrackeradmin.service.StudentService;

import java.io.IOException;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/student")
public class StudentController {
    private final StudentService studentService;

    @PostMapping("/add-all-from-file")
    public ResponseEntity<String> getAllFromFile(@ModelAttribute MultipartFile file) throws IOException {
        studentService.addAllFromFile(file);
        return ResponseEntity.ok("saved");
    }
    @GetMapping("/get-group-for-select")
    public ResponseEntity<Page<Map<String, String>>> getGroupForSelect(@ModelAttribute ForSelect2Dto forSelect2Dto){
        return ResponseEntity.ok(studentService.getAllByGroupForSelect(forSelect2Dto));
    }
    @GetMapping
    public ModelAndView index(){
        return new ModelAndView("student/index");
    }
    @GetMapping("/get-all")
    public ResponseEntity<Page<StudentResponseForViewAll>> getAll(@ModelAttribute @Valid StudentRequestForFilter studentRequestForFilter){
        return ResponseEntity.ok(studentService.getAll(studentRequestForFilter));
    }
    @ModelAttribute
    public void activeMenuItem(Model model) {
        model.addAttribute("studentActive", true);
    }
}