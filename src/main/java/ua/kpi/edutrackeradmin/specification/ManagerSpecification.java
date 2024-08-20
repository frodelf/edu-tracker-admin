package ua.kpi.edutrackeradmin.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import ua.kpi.edutrackeradmin.dto.manager.ManagerRequestForFilter;
import ua.kpi.edutrackerentity.entity.Manager;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.nonNull;

public class ManagerSpecification implements Specification<Manager> {
    private final ManagerRequestForFilter managerRequestForFilter;
    public ManagerSpecification(ManagerRequestForFilter managerRequestForFilter) {
        this.managerRequestForFilter = managerRequestForFilter;
    }

    @Override
    public Predicate toPredicate(Root<Manager> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();
        if (!managerRequestForFilter.getFullName().isBlank()) {
            predicates.add(
                    criteriaBuilder.like(
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
                                    ),
                            "%" + managerRequestForFilter.getFullName().toLowerCase() + "%"
                    )
            );
        }
        if (!managerRequestForFilter.getEmail().isBlank()) {
            predicates.add(criteriaBuilder.like(root.get("email"), "%" + managerRequestForFilter.getEmail() + "%"));
        }
        if (!managerRequestForFilter.getTelegram().isBlank()) {
            predicates.add(criteriaBuilder.like(root.get("telegram"), "%" + managerRequestForFilter.getTelegram() + "%"));
        }
        if (!managerRequestForFilter.getPhone().isBlank()) {
            predicates.add(criteriaBuilder.like(root.get("phone"), "%" + managerRequestForFilter.getPhone() + "%"));
        }
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}
