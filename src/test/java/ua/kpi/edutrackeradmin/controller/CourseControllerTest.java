package ua.kpi.edutrackeradmin.controller;

import io.minio.errors.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;
import ua.kpi.edutrackeradmin.dto.course.CourseRequestForAdd;
import ua.kpi.edutrackeradmin.dto.course.CourseResponseForAdd;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourseControllerTest {
    @Mock
    private CourseService courseService;

    @Mock
    private LessonService lessonService;

    @Mock
    private TaskService taskService;

    @InjectMocks
    private CourseController courseController;

    @Test
    void index() {
        ModelAndView modelAndView = courseController.index();
        assertEquals("course/index", modelAndView.getViewName());
    }

    @Test
    void getAll() {
        Page<CourseResponseViewAll> coursePage = mock(Page.class);
        when(courseService.getAll(0, 10, "query")).thenReturn(coursePage);

        ResponseEntity<Page<CourseResponseViewAll>> response = courseController.getAll(0, 10, "query");

        assertNotNull(response);
        assertEquals(coursePage, response.getBody());
    }

    @Test
    void statistic() {
        Map<String, String> expectedStatistic = new HashMap<>();
        expectedStatistic.put("students", "0");
        expectedStatistic.put("literatures", "0");
        expectedStatistic.put("lessons", "0");
        expectedStatistic.put("allTasks", "0");
        expectedStatistic.put("openTasks", "0");
        expectedStatistic.put("closeTasks", "0");

        Course course = new Course();
        course.setId(1L);

        when(courseService.getById(1L)).thenReturn(course);
        when(lessonService.countByCourseId(1L)).thenReturn(0L);
        when(taskService.countAllTasksByCourseId(1L)).thenReturn(0L);
        when(taskService.countAllOpenTasksByCourseId(1L)).thenReturn(0L);
        when(taskService.countAllCloseTasksByCourseId(1L)).thenReturn(0L);

        ResponseEntity<Map<String, String>> response = courseController.statistic(1L);

        assertNotNull(response);
        assertEquals(expectedStatistic, response.getBody());
    }

    @Test
    void add() {
        ModelAndView modelAndView = courseController.add();
        assertEquals("course/add", modelAndView.getViewName());
    }

    @Test
    void edit() {
        CourseResponseForAdd courseResponseForAdd = mock(CourseResponseForAdd.class);
        when(courseService.getByIdForAdd(1L)).thenReturn(courseResponseForAdd);
        ModelAndView modelAndView = courseController.edit(1L);
        assertEquals("course/add", modelAndView.getViewName());
        assertEquals(courseResponseForAdd, modelAndView.getModel().get("course"));
    }

    @Test
    void testAdd() throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        CourseRequestForAdd courseRequestForAdd = mock(CourseRequestForAdd.class);
        when(courseService.add(courseRequestForAdd)).thenReturn(1L);

        ResponseEntity<Long> response = courseController.add(courseRequestForAdd);

        assertNotNull(response);
        assertEquals(1L, response.getBody());
    }

    @Test
    void remove() {
        long courseId = 1L;
        doNothing().when(courseService).removeById(courseId);

        ResponseEntity<String> response = courseController.remove(courseId);

        assertNotNull(response);
        assertEquals("removed", response.getBody());
    }

    @Test
    void activeMenuItem() {
        Model model = mock(Model.class);
        courseController.activeMenuItem(model);
        verify(model, times(1)).addAttribute("courseActive", true);
    }
}