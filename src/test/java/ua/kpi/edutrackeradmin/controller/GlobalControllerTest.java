package ua.kpi.edutrackeradmin.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.kpi.edutrackeradmin.dto.manager.ManagerResponseForGlobal;
import ua.kpi.edutrackeradmin.service.ManagerService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GlobalControllerTest {
    @Mock
    private ManagerService managerService;
    @InjectMocks
    private GlobalController globalController;

    @Test
    void globalTeamLeadAttribute() {
        ManagerResponseForGlobal mockManager = new ManagerResponseForGlobal();
        mockManager.setName("Test Manager");
        mockManager.setEmail("test@example.com");

        when(managerService.getAuthManagerForGlobal()).thenReturn(mockManager);

        ManagerResponseForGlobal result = globalController.globalTeamLeadAttribute();

        assertEquals("Test Manager", result.getName());
        assertEquals("test@example.com", result.getEmail());
    }
}