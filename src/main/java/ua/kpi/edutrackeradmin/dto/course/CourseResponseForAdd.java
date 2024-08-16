package ua.kpi.edutrackeradmin.dto.course;

import lombok.Data;

@Data
public class CourseResponseForAdd {
    private Long id;
    private String name;
    private String image;
    private Double maximumMark;
    private String goal;
    private Long professorId;
}
