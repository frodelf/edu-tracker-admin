package ua.kpi.edutrackeradmin.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.servlet.ModelAndView;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class MainControllerTest {
    @InjectMocks
    private MainController mainController;

    @Test
    void main() {
        ModelAndView result = mainController.main();
        assertEquals("redirect:/course", result.getViewName());
    }
}