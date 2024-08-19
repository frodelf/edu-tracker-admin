package ua.kpi.edutrackeradmin.specification;

import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;
import ua.kpi.edutrackeradmin.dto.student.StudentRequestForFilter;
import ua.kpi.edutrackerentity.entity.Course;
import ua.kpi.edutrackerentity.entity.Student;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.nonNull;

public class StudentSpecification implements Specification<Student> {
    private final StudentRequestForFilter studentRequestForFilter;
    public StudentSpecification(StudentRequestForFilter studentRequestForFilter) {
        this.studentRequestForFilter = studentRequestForFilter;
    }
    @Override
    public Predicate toPredicate(Root<Student> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();
        if (nonNull(studentRequestForFilter.getFullName())) {
            predicates.add(
                    criteriaBuilder.like(
                            criteriaBuilder.concat(
                                    criteriaBuilder.concat(
                                            criteriaBuilder.lower(root.get("groupName")),
                                            " "
                                    ),
                                    criteriaBuilder.concat(
                                            criteriaBuilder.concat(
                                                    criteriaBuilder.concat(
                                                            criteriaBuilder.lower(root.get("lastName")),
                                                            " "
                                                    ),
                                                    criteriaBuilder.lower(root.get("name"))
                                            ),
                                            criteriaBuilder.concat(
                                                    " ",
                                                    criteriaBuilder.lower(root.get("middleName"))
                                            )
                                    )
                            ),
                            "%" + studentRequestForFilter.getFullName().toLowerCase() + "%"
                    )
            );
        }
        if (nonNull(studentRequestForFilter.getEmail())) {
            predicates.add(criteriaBuilder.like(root.get("email"), "%" + studentRequestForFilter.getEmail() + "%"));
        }
        if (nonNull(studentRequestForFilter.getTelegram())) {
            predicates.add(criteriaBuilder.like(root.get("telegram"), "%" + studentRequestForFilter.getTelegram() + "%"));
        }
        if (nonNull(studentRequestForFilter.getPhone())) {
            predicates.add(criteriaBuilder.like(root.get("phone"), "%" + studentRequestForFilter.getPhone() + "%"));
        }
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}
