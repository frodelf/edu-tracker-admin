package ua.kpi.edutrackeradmin.controller;

import io.minio.errors.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ua.kpi.edutrackeradmin.dto.course.CourseRequestForAdd;
import ua.kpi.edutrackeradmin.dto.course.CourseResponseViewAll;
import ua.kpi.edutrackeradmin.service.CourseService;
import ua.kpi.edutrackeradmin.service.LessonService;
import ua.kpi.edutrackeradmin.service.TaskService;
import ua.kpi.edutrackerentity.entity.Course;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/course")
public class CourseController {
    private final CourseService courseService;
    private final LessonService lessonService;
    private final TaskService taskService;
    @GetMapping
    public ModelAndView index() {
        return new ModelAndView("course/index");
    }
    @GetMapping("/get-all")
    public ResponseEntity<Page<CourseResponseViewAll>> getAll(@RequestParam int page, @RequestParam int pageSize, @RequestParam String query){
        return ResponseEntity.ok(courseService.getAll(page, pageSize, query));
    }
    @GetMapping("/statistic")
    public ResponseEntity<Map<String, String>> statistic(@RequestParam long id){
        Map<String, String> statistic = new HashMap<>();
        Course course = courseService.getById(id);

        if(course.getStudents() != null)statistic.put("students", String.valueOf(course.getStudents().size()));
        else statistic.put("students", "0");
        if(course.getLiteratures() != null)statistic.put("literatures", String.valueOf(course.getLiteratures().size()));
        else statistic.put("literatures", "0");
        statistic.put("lessons", String.valueOf(lessonService.countByCourseId(course.getId())));
        statistic.put("allTasks", String.valueOf(taskService.countAllTasksByCourseId(course.getId())));
        statistic.put("openTasks", String.valueOf(taskService.countAllOpenTasksByCourseId(course.getId())));
        statistic.put("closeTasks", String.valueOf(taskService.countAllCloseTasksByCourseId(course.getId())));

        return ResponseEntity.ok(statistic);
    }
    @GetMapping("/add")
    public ModelAndView add() {
        return new ModelAndView("course/add");
    }
    @GetMapping("/edit/{id}")
    public ModelAndView edit(@PathVariable Long id) {
        return new ModelAndView("course/add", "course", courseService.getByIdForAdd(id));
    }
    @PostMapping("/add")
    public ResponseEntity<Long> add(@ModelAttribute @Valid CourseRequestForAdd courseRequestForAdd) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        return ResponseEntity.ok(courseService.add(courseRequestForAdd));
    }
    @DeleteMapping("/remove")
    public ResponseEntity<String> remove(@RequestParam long id) {
        courseService.removeById(id);
        return ResponseEntity.ok("removed");
    }
    @ModelAttribute
    public void activeMenuItem(Model model) {
        model.addAttribute("courseActive", true);
    }
}