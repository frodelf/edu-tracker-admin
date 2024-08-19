package ua.kpi.edutrackeradmin.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ua.kpi.edutrackeradmin.dto.ForSelect2Dto;
import ua.kpi.edutrackeradmin.dto.student.StudentRequestForFilter;
import ua.kpi.edutrackeradmin.dto.student.StudentResponseForViewAll;
import ua.kpi.edutrackeradmin.mapper.StudentMapper;
import ua.kpi.edutrackeradmin.repository.StudentRepository;
import ua.kpi.edutrackeradmin.service.StudentService;
import ua.kpi.edutrackeradmin.specification.CourseSpecification;
import ua.kpi.edutrackeradmin.specification.StudentSpecification;
import ua.kpi.edutrackerentity.entity.Course;
import ua.kpi.edutrackerentity.entity.Student;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper = new StudentMapper();
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

    @Override
    public Page<StudentResponseForViewAll> getAll(StudentRequestForFilter studentRequestForFilter) {
        Pageable pageable = PageRequest.of(studentRequestForFilter.getPage(), studentRequestForFilter.getPageSize(), Sort.by(Sort.Order.desc("id")));
        Specification<Student> specification = new StudentSpecification(studentRequestForFilter);
        return studentMapper.toDtoList(studentRepository.findAll(specification, pageable));
    }
}