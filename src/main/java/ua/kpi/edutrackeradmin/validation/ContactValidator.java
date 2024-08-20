package ua.kpi.edutrackeradmin.validation;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ua.kpi.edutrackeradmin.dto.ContactDataDto;
import ua.kpi.edutrackeradmin.service.UserService;

import java.util.Locale;

import static ua.kpi.edutrackeradmin.validation.ValidUtil.notNullAndBlank;

@Component
@RequiredArgsConstructor
public class ContactValidator implements Validator {
    private final UserService userService;
    private final MessageSource messageSource;

    private String getMessage(String key) {
        return messageSource.getMessage(key, null, Locale.getDefault());
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return false;
    }

    @Override
    public void validate(Object target, Errors errors) {
        ContactDataDto contactData = (ContactDataDto) target;

        if (!userService.validEmailByExist(contactData) && !contactData.getEmail().isBlank()) {
            errors.rejectValue("email", "", getMessage("error.field.email.exist"));
        }

        if (!userService.validPhoneByExist(contactData) && !contactData.getPhone().isBlank()) {
            errors.rejectValue("phone", "", getMessage("error.field.phone.exist"));
        }

        if (!userService.validTelegramByExist(contactData)  && !contactData.getTelegram().isBlank()) {
            errors.rejectValue("telegram", "", getMessage("error.field.telegram.exist"));
        }

        if(!notNullAndBlank(contactData.getEmail())){
            errors.rejectValue("email", "", getMessage("error.field.empty"));
        }
        if(!notNullAndBlank(contactData.getTelegram())){
            errors.rejectValue("telegram", "", getMessage("error.field.empty"));
        }
        if(!notNullAndBlank(contactData.getPhone())){
            errors.rejectValue("phone", "", getMessage("error.field.empty"));
        }
    }
}
