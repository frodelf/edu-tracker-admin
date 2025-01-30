package ua.kpi.edutrackeradmin.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import ua.kpi.edutrackeradmin.dto.ForSelect2Dto;
import ua.kpi.edutrackeradmin.service.ProfessorService;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProfessorControllerTest {
    @Mock
    private ProfessorService professorService;
    @InjectMocks
    private ProfessorController professorController;

    @Test
    void getForSelect() {
        ForSelect2Dto forSelect2Dto = new ForSelect2Dto();
        Page<Map<String, String>> expectedPage = mock(Page.class);

        when(professorService.getAllByGroupForSelect(forSelect2Dto)).thenReturn(expectedPage);

        ResponseEntity<Page<Map<String, String>>> response = professorController.getForSelect(forSelect2Dto);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expectedPage, response.getBody());
        verify(professorService).getAllByGroupForSelect(forSelect2Dto);
    }
}