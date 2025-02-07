package ua.kpi.edutrackeradmin.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import ua.kpi.edutrackerentity.entity.User;
import ua.kpi.edutrackeradmin.dto.ContactDataDto;
import ua.kpi.edutrackeradmin.repository.UserRepository;
import ua.kpi.edutrackeradmin.service.UserService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public boolean validPhoneByExist(ContactDataDto contactDataDto) {
        log.info("UserServiceImpl validPhoneByExist start");
        Optional<User> user = getByPhone(contactDataDto.getPhone());
        boolean result = user.isEmpty() || user.get().getId().equals(contactDataDto.getId());
        log.info("UserServiceImpl validPhoneByExist finish");
        return result;
    }

    @Override
    public boolean validEmailByExist(ContactDataDto contactDataDto) {
        log.info("UserServiceImpl validEmailByExist start");
        Optional<User> user = getByEmail(contactDataDto.getPhone());
        boolean result = user.isEmpty() || user.get().getId().equals(contactDataDto.getId());
        log.info("UserServiceImpl validEmailByExist finish");
        return result;
    }

    @Override
    public boolean validTelegramByExist(ContactDataDto contactDataDto) {
        log.info("UserServiceImpl validTelegramByExist start");
        Optional<User> user = getByTelegram(contactDataDto.getTelegram());
        boolean result = user.isEmpty() || user.get().getId().equals(contactDataDto.getId());
        log.info("UserServiceImpl validTelegramByExist finish");
        return result;
    }

    @Override
    public boolean existByPhone(String phone) {
        log.info("UserServiceImpl existByPhone start");
        boolean result = userRepository.existsByPhone(phone);
        log.info("UserServiceImpl existByPhone finish");
        return result;
    }

    @Override
    public boolean existByTelegram(String telegram) {
        log.info("UserServiceImpl existByTelegram start");
        boolean result = userRepository.existsByTelegram(telegram);
        log.info("UserServiceImpl existByTelegram finish");
        return result;
    }

    @Override
    public boolean existByEmail(String email) {
        log.info("UserServiceImpl existByEmail start");
        boolean result = userRepository.existsByEmail(email);
        log.info("UserServiceImpl existByEmail finish");
        return result;
    }

    @Override
    public Optional<User> getByPhone(String phone) {
        log.info("UserServiceImpl getByPhone start");
        Optional<User> result = userRepository.findFirstByPhone(phone);
        log.info("UserServiceImpl getByPhone finish");
        return result;
    }

    @Override
    public Optional<User> getByEmail(String email) {
        log.info("UserServiceImpl getByEmail start");
        Optional<User> result = userRepository.findFirstByEmail(email);
        log.info("UserServiceImpl getByEmail finish");
        return result;
    }

    @Override
    public Optional<User> getByTelegram(String telegram) {
        log.info("UserServiceImpl getByTelegram start");
        Optional<User> result = userRepository.findFirstByTelegram(telegram);
        log.info("UserServiceImpl getByTelegram finish");
        return result;
    }
}