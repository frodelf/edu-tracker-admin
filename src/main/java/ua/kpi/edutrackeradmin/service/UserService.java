package ua.kpi.edutrackeradmin.service;

import ua.kpi.edutrackerentity.entity.User;
import ua.kpi.edutrackeradmin.dto.ContactDataDto;

import java.util.Optional;

public interface UserService {
    boolean validPhoneByExist(ContactDataDto contactDataDto);
    boolean validEmailByExist(ContactDataDto contactDataDto);
    boolean validTelegramByExist(ContactDataDto contactDataDto);

    boolean existByPhone(String phone);
    boolean existByTelegram(String telegram);
    boolean existByEmail(String email);

    Optional<User> getByPhone(String phone);
    Optional<User> getByEmail(String email);
    Optional<User> getByTelegram(String telegram);
}