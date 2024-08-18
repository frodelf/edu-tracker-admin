package ua.kpi.edutrackeradmin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.kpi.edutrackeradmin.dto.ForSelect2Dto;
import ua.kpi.edutrackeradmin.service.ProfessorService;

import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/professor")
public class ProfessorController {
    private final ProfessorService professorService;
    @GetMapping("/get-for-select")
    public ResponseEntity<Page<Map<String, String>>> getForSelect(@ModelAttribute ForSelect2Dto forSelect2Dto){
        return ResponseEntity.ok(professorService.getAllByGroupForSelect(forSelect2Dto));
    }
}