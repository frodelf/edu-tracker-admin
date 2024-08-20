package ua.kpi.edutrackeradmin.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import ua.kpi.edutrackeradmin.dto.manager.ManagerRequestForAdd;
import ua.kpi.edutrackeradmin.dto.manager.ManagerRequestForFilter;
import ua.kpi.edutrackeradmin.dto.manager.ManagerResponseForViewAll;
import ua.kpi.edutrackeradmin.service.ManagerService;
import ua.kpi.edutrackeradmin.validation.ContactValidator;

@Controller
@RequiredArgsConstructor
@RequestMapping("/manager")
public class ManagerController {
    private final ManagerService managerService;
    private final ContactValidator contactValidator;
    @GetMapping("/get-all")
    public ResponseEntity<Page<ManagerResponseForViewAll>> getAll(@ModelAttribute @Valid ManagerRequestForFilter managerRequestForFilter){
        return ResponseEntity.ok(managerService.getAll(managerRequestForFilter));
    }
    @GetMapping
    public ModelAndView index() {
        return new ModelAndView("manager/index");
    }
    @PostMapping("/add")
    public ResponseEntity<Long> add(@ModelAttribute @Valid ManagerRequestForAdd managerRequestForAdd, BindingResult bindingResult) throws MethodArgumentNotValidException, NoSuchMethodException {
        contactValidator.validate(managerRequestForAdd, bindingResult);
        if (bindingResult.hasErrors()) {
            MethodParameter methodParameter = new MethodParameter(this.getClass().getDeclaredMethod("add", ManagerRequestForAdd.class, BindingResult.class), 0);
            throw new MethodArgumentNotValidException(methodParameter, bindingResult);
        }
        return ResponseEntity.ok(managerService.add(managerRequestForAdd));
    }
    @ModelAttribute
    public void activeMenuItem(Model model) {
        model.addAttribute("managerActive", true);
    }
}