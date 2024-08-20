package ua.kpi.edutrackeradmin.dto.manager;

import lombok.Data;

@Data
public class ManagerResponseForViewAll {
    private Long id;
    private String fullName;
    private String email;
    private String telegram;
    private String phone;
}