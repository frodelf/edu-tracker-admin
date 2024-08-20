package ua.kpi.edutrackeradmin.dto.manager;

import lombok.Data;

@Data
public class ManagerResponseForGlobal {
    private Long id;
    private String lastName;
    private String name;
    private String email;
    private String image;
    private Boolean isMainAdmin;
}