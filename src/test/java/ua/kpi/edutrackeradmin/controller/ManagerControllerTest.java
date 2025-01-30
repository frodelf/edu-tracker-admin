package ua.kpi.edutrackeradmin.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.servlet.ModelAndView;
import ua.kpi.edutrackeradmin.dto.manager.ManagerRequestForAdd;
import ua.kpi.edutrackeradmin.dto.manager.ManagerRequestForFilter;
import ua.kpi.edutrackeradmin.dto.manager.ManagerResponseForViewAll;
import ua.kpi.edutrackeradmin.service.ManagerService;
import ua.kpi.edutrackeradmin.validation.ContactValidator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class ManagerControllerTest {
    @Mock
    private ManagerService managerService;
    @Mock
    private ContactValidator contactValidator;
    @Mock
    private BindingResult bindingResult;
    @Mock
    private Model model;
    @InjectMocks
    private ManagerController managerController;

    @Test
    void getAll() {
        ManagerRequestForFilter filter = new ManagerRequestForFilter();
        Page<ManagerResponseForViewAll> mockPage = mock(Page.class);

        when(managerService.getAll(filter)).thenReturn(mockPage);

        ResponseEntity<Page<ManagerResponseForViewAll>> response = managerController.getAll(filter);

        assertEquals(mockPage, response.getBody());
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void index() {
        ModelAndView result = managerController.index();
        assertEquals("manager/index", result.getViewName());
    }

    @Test
    void add_valid() throws MethodArgumentNotValidException, NoSuchMethodException {
        ManagerRequestForAdd requestForAdd = mock(ManagerRequestForAdd.class);

        when(bindingResult.hasErrors()).thenReturn(false);
        when(managerService.add(requestForAdd)).thenReturn(1L);

        ResponseEntity<Long> response = managerController.add(requestForAdd, bindingResult);

        assertEquals(1L, response.getBody());
        assertEquals(200, response.getStatusCodeValue());
        verify(contactValidator).validate(requestForAdd, bindingResult);
    }

    @Test
    void add_invalid() {
        ManagerRequestForAdd requestForAdd = mock(ManagerRequestForAdd.class);

        when(bindingResult.hasErrors()).thenReturn(true);

        assertThrows(MethodArgumentNotValidException.class, () -> {
            managerController.add(requestForAdd, bindingResult);
        });

        verify(contactValidator).validate(requestForAdd, bindingResult);
        verifyNoInteractions(managerService);
    }

    @Test
    void activeMenuItem() {
        managerController.activeMenuItem(model);
        verify(model).addAttribute("managerActive", true);
    }
}