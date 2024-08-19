package ua.kpi.edutrackeradmin.mapper;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import ua.kpi.edutrackeradmin.dto.student.StudentResponseForViewAll;
import ua.kpi.edutrackerentity.entity.Student;

import java.util.stream.Collectors;

public class StudentMapper {
    public Page<StudentResponseForViewAll> toDtoList(Page<Student> students) {
        return new PageImpl<>(students.getContent().stream()
                .map(this::toDtoForViewAll)
                .collect(Collectors.toList()), students.getPageable(), students.getTotalElements());
    }
    private StudentResponseForViewAll toDtoForViewAll(Student student) {
        StudentResponseForViewAll studentResponseForViewAll = new StudentResponseForViewAll();
        studentResponseForViewAll.setId(student.getId());
        studentResponseForViewAll.setFullName(student.getGroupName() + " " + student.getLastName() + " " + student.getName() + " " + student.getMiddleName());
        studentResponseForViewAll.setTelegram(student.getTelegram());
        studentResponseForViewAll.setPhone(student.getPhone());
        studentResponseForViewAll.setEmail(student.getEmail());
        return studentResponseForViewAll;
    }
}
