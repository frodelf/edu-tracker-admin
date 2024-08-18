package ua.kpi.edutrackeradmin.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ua.kpi.edutrackerentity.entity.Student;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {
    @Query("SELECT DISTINCT s.groupName FROM Student s WHERE LOWER(s.groupName) LIKE LOWER(CONCAT('%', :groupName, '%'))")
    Page<String> findAllGroupNamesByGroupNameLikeIgnoreCase(@Param("groupName") String groupName, Pageable pageable);
    List<Student> findAllByGroupName(String groupName);
}