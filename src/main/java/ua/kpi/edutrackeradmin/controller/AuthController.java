package ua.kpi.edutrackeradmin.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import ua.kpi.edutrackeradmin.service.ManagerService;

@Controller
@RequiredArgsConstructor
public class AuthController {
    private final ManagerService managerService;
    @GetMapping("/login")
    public ModelAndView login() {
        if(managerService.isAuthenticated()) {return new ModelAndView("redirect:/course");}
        return new ModelAndView("auth/login");
    }
    @GetMapping("/logout")
    public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
        return "redirect:/login";
    }
    @GetMapping("/checkAuth")
    public ResponseEntity<Boolean> checkAuthentication() {
        return ResponseEntity.ok(managerService.isAuthenticated());
    }
}