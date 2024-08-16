package ua.kpi.edutrackeradmin.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import ua.kpi.edutrackerentity.entity.Course;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.nonNull;

public class CourseSpecification implements Specification<Course> {
    private final String query;
    public CourseSpecification(String query) {
        this.query = query;
    }
    @Override
    public Predicate toPredicate(Root<Course> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();
        if(nonNull(query)){
            predicates.add(criteriaBuilder.like(root.get("name"), "%"+this.query+"%"));
        }
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}
