package com.reliaquest.api.controller;

import com.reliaquest.api.model.EmployeeDto;
import com.reliaquest.api.model.EmployeeInputDto;
import com.reliaquest.api.service.EmployeeService;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private EmployeeDto employeeDto;

    @Mock
    private EmployeeService employeeService;
    @Mock
    private RateLimiter rateLimiter;

    @InjectMocks
    private EmployeeController employeeController;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(employeeController).build();
        employeeDto = new EmployeeDto(UUID.randomUUID(), "John Doe", 100000, 50, "", null);
    }

    @Test
    public void testGetAllEmployees() throws Exception {

        when(employeeService.getAllEmployees()).thenReturn(Collections.singletonList(employeeDto));

        mockMvc.perform(get("/api/v1/employee"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].employee_name").value("John Doe"))
            .andExpect(jsonPath("$[0].employee_salary").value(100000));
    }

    @Test
    public void testGetEmployeeById() throws Exception {

        UUID employeeId = UUID.randomUUID();
        when(employeeService.getEmployeeById(employeeId)).thenReturn(employeeDto);

        mockMvc.perform(get("/api/v1/employee/{id}", employeeId.toString()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.employee_name").value("John Doe"))
            .andExpect(jsonPath("$.employee_salary").value(100000));
    }

    @Test
    public void testCreateEmployee() throws Exception {

        EmployeeInputDto employeeInputDto = new EmployeeInputDto("John Doe", 100000, 50, "");

        when(employeeService.createEmployee(employeeInputDto)).thenReturn(employeeDto);

        mockMvc.perform(post("/api/v1/employee")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"John Doe\", \"salary\": 100000, \"age\": 50, \"title\": \"\"}"))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.employee_name").value("John Doe"))
            .andExpect(jsonPath("$.employee_salary").value(100000));
    }

    @Test
    public void testGetEmployeesByNameSearch() throws Exception {

        String searchString = "John";
        when(employeeService.searchEmployeesByName(searchString)).thenReturn(Arrays.asList(employeeDto));

        mockMvc.perform(get("/api/v1/employee/by-name/{name}", searchString))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].employee_name").value("John Doe"));
    }

    @Test
    public void testGetHighestSalaryOfEmployees() throws Exception {

        when(employeeService.getHighestSalary()).thenReturn(100000);

        mockMvc.perform(get("/api/v1/employee/highest-salary"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").value(100000));
    }

    @Test
    public void testGetTopTenHighestEarningEmployeeNames() throws Exception {

        when(employeeService.getTopTenHighestEarningEmployeeNames()).thenReturn(Arrays.asList("John Doe", "Jane Doe"));

        mockMvc.perform(get("/api/v1/employee/top-ten-earning-employees"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0]").value("John Doe"))
            .andExpect(jsonPath("$[1]").value("Jane Doe"));
    }

    @Test
    public void testDeleteEmployeeById() throws Exception {

        UUID employeeId = UUID.randomUUID();
        when(employeeService.deleteEmployeeById(employeeId.toString())).thenReturn("John Doe");

        mockMvc.perform(delete("/api/v1/employee/{id}", employeeId.toString()))
            .andExpect(status().isOk())
            .andExpect(content().string("John Doe"));
    }

//    @Test
//    public void testFallbackMethod() throws Exception {
//        String rateLimiterErrorMessage = "Too many requests in short interval! Please try again later.";
//        when(employeeService.getAllEmployees()).thenThrow(RequestNotPermitted.createRequestNotPermitted(rateLimiter));
//
//        mockMvc.perform(get("/api/v1/employee"))
//            .andExpect(status().isTooManyRequests())
//            .andExpect(content().string(rateLimiterErrorMessage));
//    }
}
