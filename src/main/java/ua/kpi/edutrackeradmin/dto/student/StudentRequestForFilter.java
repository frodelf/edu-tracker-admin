package ua.kpi.edutrackeradmin.dto.student;

import lombok.Data;

@Data
public class StudentRequestForFilter {
    private Integer page;
    private Integer pageSize;
    private String fullName;
    private String email;
    private String telegram;
    private String phone;
}