package ua.kpi.edutrackeradmin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import ua.kpi.edutrackeradmin.dto.manager.ManagerResponseForGlobal;
import ua.kpi.edutrackeradmin.service.ManagerService;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalController {
    @Value("${server.servlet.host}")
    private String host;
    @Value("${server.servlet.context-path}")
    private String contextPath;
    @Value("${server.port}")
    private String port;
    private final ManagerService managerService;

    @ModelAttribute("manager")
    public ManagerResponseForGlobal globalTeamLeadAttribute() {
        return managerService.getAuthManagerForGlobal();
    }
    @ModelAttribute("host")
    public String globalHostAttribute() {return host;}
    @ModelAttribute("contextPath")
    public String globalContextPathAttribute() {return contextPath;}
    @ModelAttribute("port")
    public String globalPortAttribute() {return port;}
}