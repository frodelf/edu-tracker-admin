package ua.kpi.edutrackeradmin.dto.course;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import ua.kpi.edutrackeradmin.validation.annotation.ImageExtension;

import java.util.List;

@Data
public class CourseRequestForAdd {
    private Long id;
    @NotBlank(message = "{error.field.empty}")
    @Size(max = 100, message = "{error.field.size.max}")
    private String name;
    @ImageExtension
    private MultipartFile image;
    @NotNull(message = "{error.field.empty}")
    @Min(value = 0, message = "{error.field.min-value}")
    @Max(value = 100, message = "{error.field.max-value}")
    private Double maximumMark;
    @NotBlank(message = "{error.field.empty}")
    @Size(max = 1000, message = "{error.field.size.max}")
    private String goal;
    private List<String> groups;
    @NotNull(message = "{error.field.empty}")
    private Long professorId;
    private Boolean isForChoosing;
}