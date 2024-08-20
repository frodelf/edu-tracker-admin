package ua.kpi.edutrackeradmin.dto.manager;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ManagerRequestForFilter {
    @Min(value = 0, message = "{error.field.min-value}")
    private Integer page;
    @Min(value = 1, message = "{error.field.min-value}")
    private Integer pageSize;
    @Size(max = 100, message = "{error.field.size.max}")
    private String fullName;
    @Size(max = 100, message = "{error.field.size.max}")
    private String email;
    @Size(max = 100, message = "{error.field.size.max}")
    private String telegram;
    @Size(max = 100, message = "{error.field.size.max}")
    private String phone;
}