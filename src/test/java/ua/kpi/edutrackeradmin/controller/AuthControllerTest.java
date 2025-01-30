package ua.kpi.edutrackeradmin.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.servlet.ModelAndView;
import ua.kpi.edutrackeradmin.service.ManagerService;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {
    @Mock
    private ManagerService managerService;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @InjectMocks
    private AuthController authController;

    @Test
    void login() {
        when(managerService.isAuthenticated()).thenReturn(false);
        ModelAndView result = authController.login();
        assertEquals("auth/login", result.getViewName());

        when(managerService.isAuthenticated()).thenReturn(true);
        result = authController.login();
        assertEquals("redirect:/course", result.getViewName());
    }

    @Test
    void logoutPage() throws IOException {
        Authentication authentication = mock(Authentication.class);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        authController.logoutPage(request, response);

        verify(request, times(2)).getSession(false);
        verify(response, times(0)).sendRedirect(anyString());
    }

    @Test
    void checkAuthentication() {
        when(managerService.isAuthenticated()).thenReturn(true);
        ResponseEntity<Boolean> result = authController.checkAuthentication();
        assertEquals(true, result.getBody());

        when(managerService.isAuthenticated()).thenReturn(false);
        result = authController.checkAuthentication();
        assertEquals(false, result.getBody());
    }
}