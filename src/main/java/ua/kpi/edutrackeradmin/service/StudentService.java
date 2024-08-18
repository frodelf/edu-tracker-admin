package ua.kpi.edutrackeradmin.service;

import org.springframework.data.domain.Page;
import ua.kpi.edutrackeradmin.dto.ForSelect2Dto;
import ua.kpi.edutrackerentity.entity.Student;

import java.util.List;
import java.util.Map;

public interface StudentService {
    Page<Map<String, String>> getAllByGroupForSelect(ForSelect2Dto forSelect2Dto);
    List<Student> getAllStudentsByGroup(String groupName);
}