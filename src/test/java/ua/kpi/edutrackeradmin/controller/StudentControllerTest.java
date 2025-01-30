package ua.kpi.edutrackeradmin.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import ua.kpi.edutrackeradmin.dto.ForSelect2Dto;
import ua.kpi.edutrackeradmin.dto.student.StudentRequestForFilter;
import ua.kpi.edutrackeradmin.dto.student.StudentResponseForViewAll;
import ua.kpi.edutrackeradmin.service.StudentService;

import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentControllerTest {
    @Mock
    private StudentService studentService;
    @Mock
    private MultipartFile file;
    @InjectMocks
    private StudentController studentController;

    @Test
    void getAllFromFile() throws IOException {
        ResponseEntity<String> response = studentController.getAllFromFile(file);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("saved", response.getBody());
        verify(studentService).addAllFromFile(file);
    }

    @Test
    void getGroupForSelect() {
        ForSelect2Dto forSelect2Dto = new ForSelect2Dto();
        Page<Map<String, String>> expectedPage = mock(Page.class);

        when(studentService.getAllByGroupForSelect(forSelect2Dto)).thenReturn(expectedPage);

        ResponseEntity<Page<Map<String, String>>> response = studentController.getGroupForSelect(forSelect2Dto);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expectedPage, response.getBody());
        verify(studentService).getAllByGroupForSelect(forSelect2Dto);
    }

    @Test
    void index() {
        ModelAndView response = studentController.index();

        assertEquals("student/index", response.getViewName());
    }

    @Test
    void getAll() {
        StudentRequestForFilter studentRequestForFilter = new StudentRequestForFilter();
        Page<StudentResponseForViewAll> expectedPage = mock(Page.class);

        when(studentService.getAll(studentRequestForFilter)).thenReturn(expectedPage);

        ResponseEntity<Page<StudentResponseForViewAll>> response = studentController.getAll(studentRequestForFilter);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expectedPage, response.getBody());
        verify(studentService).getAll(studentRequestForFilter);
    }

    @Test
    void activeMenuItem() {
        Model model = mock(Model.class);
        studentController.activeMenuItem(model);

        verify(model).addAttribute("studentActive", true);
    }
}