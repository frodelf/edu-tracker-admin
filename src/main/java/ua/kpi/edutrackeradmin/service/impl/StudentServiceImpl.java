package ua.kpi.edutrackeradmin.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ua.kpi.edutrackeradmin.dto.ForSelect2Dto;
import ua.kpi.edutrackeradmin.dto.student.StudentRequestForFilter;
import ua.kpi.edutrackeradmin.dto.student.StudentResponseForViewAll;
import ua.kpi.edutrackeradmin.mapper.StudentMapper;
import ua.kpi.edutrackeradmin.repository.StudentRepository;
import ua.kpi.edutrackeradmin.service.EmailService;
import ua.kpi.edutrackeradmin.service.StudentService;
import ua.kpi.edutrackeradmin.service.UserService;
import ua.kpi.edutrackeradmin.specification.StudentSpecification;
import ua.kpi.edutrackerentity.entity.Student;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Log4j2
@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;
    private final UserService userService;
    private final EmailService emailService;
    private final StudentMapper studentMapper = new StudentMapper();

    @Override
    public Page<Map<String, String>> getAllByGroupForSelect(ForSelect2Dto forSelect2Dto) {
        log.info("StudentServiceImpl getAllByGroupForSelect start");
        Pageable pageable = PageRequest.of(forSelect2Dto.getPage(), forSelect2Dto.getSize(), Sort.unsorted());
        Page<String> groups = studentRepository.findAllGroupNamesByGroupNameLikeIgnoreCase(forSelect2Dto.getQuery(), pageable);
        List<Map<String, String>> list = new ArrayList<>();
        for (String group : groups.getContent()) {
            Map<String, String> map = new HashMap<>();
            map.put(group, group);
            list.add(map);
        }
        log.info("StudentServiceImpl getAllByGroupForSelect finish");
        return new PageImpl<>(list, pageable, groups.getTotalElements());
    }

    @Override
    public List<Student> getAllStudentsByGroup(String groupName) {
        log.info("StudentServiceImpl getAllStudentsByGroup start");
        List<Student> students = studentRepository.findAllByGroupName(groupName);
        log.info("StudentServiceImpl getAllStudentsByGroup finish");
        return students;
    }

    @Override
    public Page<StudentResponseForViewAll> getAll(StudentRequestForFilter studentRequestForFilter) {
        log.info("StudentServiceImpl getAll start");
        Pageable pageable = PageRequest.of(studentRequestForFilter.getPage(), studentRequestForFilter.getPageSize(), Sort.by(Sort.Order.desc("id")));
        Specification<Student> specification = new StudentSpecification(studentRequestForFilter);
        Page<StudentResponseForViewAll> response = studentMapper.toDtoList(studentRepository.findAll(specification, pageable));
        log.info("StudentServiceImpl getAll finish");
        return response;
    }

    @Override
    @Transactional
    public void addAllFromFile(MultipartFile multipartFile) throws IOException {
        log.info("StudentServiceImpl addAllFromFile start");
        Workbook workbook = new XSSFWorkbook(multipartFile.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);
        for (Row row : sheet) {
            if (row.getCell(0).toString().isBlank()) break;
            Student student = new Student();
            student.setGroupName(row.getCell(0).toString());
            student.setLastName(row.getCell(1).toString());
            student.setName(row.getCell(2).toString());
            student.setMiddleName(row.getCell(3).toString());
            student.setTelegram(row.getCell(4).toString());
            student.setEmail(row.getCell(5).toString());
            student.setPhone(row.getCell(6).toString());

            Pattern EMAIL_PATTERN = Pattern.compile(
                    "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
            );
            Pattern PHONE_PATTERN = Pattern.compile(
                    "^\\+380(31|32|33|34|35|36|37|38|39|41|42|43|44|45|46|47|48|49|50|59|61|63|66|67|68|73|89|91|92|93|94|95|96|97|98|99)\\d{7}$"
            );
            Pattern USERNAME_PATTERN = Pattern.compile("^@[a-zA-Z0-9_]{5,32}$");

            if (!EMAIL_PATTERN.matcher(student.getEmail()).matches()) {
                throw new IllegalArgumentException("Студент в рядку " + (1 + row.getRowNum()) + " має некоректний адрес");
            }
            if (!PHONE_PATTERN.matcher(student.getPhone()).matches()) {
                throw new IllegalArgumentException("Студент в рядку " + (1 + row.getRowNum()) + " має некоректний телефон");
            }
            if (!USERNAME_PATTERN.matcher(student.getTelegram()).matches()) {
                throw new IllegalArgumentException("Студент в рядку " + (1 + row.getRowNum()) + " має некоректний телеграм");
            }

            if (userService.existByPhone(student.getPhone())) {
                throw new IllegalArgumentException("У студента в рядку " + (row.getRowNum() + 1) + " вказаний телефон, який вже зайнятий.");
            }
            if (userService.existByEmail(student.getEmail())) {
                throw new IllegalArgumentException("У студента в рядку " + (row.getRowNum() + 1) + " вказана електронна пошта, яка вже зайнятий.");
            }
            if (userService.existByTelegram(student.getTelegram())) {
                throw new IllegalArgumentException("У студента в рядку " + (row.getRowNum() + 1) + " вказаний Telegram, який вже зайнятий.");
            }

            student.setPassword(new BCryptPasswordEncoder().encode(generateAndSendPassword(student.getEmail())));
            save(student);
        }
        workbook.close();
        log.info("StudentServiceImpl addAllFromFile finish");
    }

    @Override
    public void save(Student student) {
        log.info("StudentServiceImpl save start");
        studentRepository.save(student);
        log.info("StudentServiceImpl save finish");
    }

    private String generateAndSendPassword(String email) {
        log.info("StudentServiceImpl generateAndSendPassword start");
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder(8);

        for (int i = 0; i < 8; i++) {
            int digit = random.nextInt(10);
            password.append(digit);
        }
        String passwordText = password.toString();
        emailService.sendEmail("password", passwordText, email);
        log.info("StudentServiceImpl generateAndSendPassword finish");
        return passwordText;
    }
}
