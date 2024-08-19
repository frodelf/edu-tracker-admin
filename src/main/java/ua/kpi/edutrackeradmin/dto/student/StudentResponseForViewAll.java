package ua.kpi.edutrackeradmin.dto.student;

import lombok.Data;

@Data
public class StudentResponseForViewAll {
    private Long id;
    private String fullName;
    private String email;
    private String telegram;
    private String phone;
}