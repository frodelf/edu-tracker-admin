package ua.kpi.edutrackeradmin.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import ua.kpi.edutrackeradmin.dto.ForSelect2Dto;
import ua.kpi.edutrackeradmin.repository.StudentRepository;
import ua.kpi.edutrackeradmin.service.StudentService;
import ua.kpi.edutrackerentity.entity.Student;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;
    @Override
    public Page<Map<String, String>> getAllByGroupForSelect(ForSelect2Dto forSelect2Dto) {
        Pageable pageable = PageRequest.of(forSelect2Dto.getPage(), forSelect2Dto.getSize(), Sort.by(Sort.Order.desc("id")));
        Page<String> groups = studentRepository.findAllGroupNamesByGroupNameLikeIgnoreCase(forSelect2Dto.getQuery(), pageable);
        List<Map<String, String>> list = new ArrayList<>();
        for (String group : groups.getContent()) {
            Map<String, String> map = new HashMap<>();
            map.put(group, group);
            list.add(map);
        }
        return new PageImpl<>(list, pageable, groups.getTotalElements());
    }
    @Override
    public List<Student> getAllStudentsByGroup(String groupName) {
        return studentRepository.findAllByGroupName(groupName);
    }
}