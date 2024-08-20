package ua.kpi.edutrackeradmin.dto.manager;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ua.kpi.edutrackeradmin.dto.ContactDataDto;

@Data
public class ManagerRequestForAdd extends ContactDataDto {
    @NotBlank(message = "{error.field.empty}")
    @Size(max = 100, message = "{error.field.size.max}")
    private final String lastName;
    @NotBlank(message = "{error.field.empty}")
    @Size(max = 100, message = "{error.field.size.max}")
    private final String name;
    @NotBlank(message = "{error.field.empty}")
    @Size(max = 100, message = "{error.field.size.max}")
    private final String middleName;
}