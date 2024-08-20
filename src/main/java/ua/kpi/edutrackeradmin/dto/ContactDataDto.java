package ua.kpi.edutrackeradmin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.kpi.edutrackeradmin.validation.annotation.EmailFormat;
import ua.kpi.edutrackeradmin.validation.annotation.PhoneFormat;
import ua.kpi.edutrackeradmin.validation.annotation.TelegramFormat;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContactDataDto {
    private Long id;
    @EmailFormat
    private String email;
    @PhoneFormat
    private String phone;
    @TelegramFormat
    private String telegram;
}