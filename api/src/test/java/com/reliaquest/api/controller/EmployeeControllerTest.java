package com.reliaquest.api.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.reliaquest.api.model.EmployeeDto;
import com.reliaquest.api.service.EmployeeService;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class EmployeeControllerTest {

    private MockMvc mockMvc;

    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private EmployeeController employeeController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(employeeController).build();
    }

    @Test
    void getAllEmployees() throws Exception {
        List<EmployeeDto> employeeDtos = List.of(new EmployeeDto(UUID.randomUUID(), "John", 5000, 25, "", ""));

        when(employeeService.getAllEmployees()).thenReturn(employeeDtos);

        mockMvc.perform(get("/api/v1/employee"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].employee_name").value("John"))
                .andExpect(jsonPath("$[0].employee_salary").value(5000));

        verify(employeeService, times(1)).getAllEmployees();
    }

    @Test
    void getEmployeeById() throws Exception {
        UUID employeeId = UUID.randomUUID();
        EmployeeDto employeeDto = new EmployeeDto(employeeId, "John", 5000, 20, "", "");

        when(employeeService.getEmployeeById(employeeId)).thenReturn(employeeDto);

        mockMvc.perform(get("/api/v1/employee/{id}", employeeId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.employee_name").value("John"))
                .andExpect(jsonPath("$.employee_salary").value(5000));

        verify(employeeService, times(1)).getEmployeeById(employeeId);
    }

    @Test
    void getEmployeesByNameSearch() throws Exception {
        String searchString = "John";
        List<EmployeeDto> employeeDtos = List.of(new EmployeeDto(UUID.randomUUID(), "John", 5000, 30, "", ""));

        when(employeeService.searchEmployeesByName(searchString)).thenReturn(employeeDtos);

        mockMvc.perform(get("/api/v1/employee/by-name/{name}", searchString))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].employee_name").value("John"))
                .andExpect(jsonPath("$[0].employee_salary").value(5000));

        verify(employeeService, times(1)).searchEmployeesByName(searchString);
    }

    @Test
    void getHighestSalaryOfEmployees() throws Exception {
        int highestSalary = 10000;

        when(employeeService.getHighestSalary()).thenReturn(highestSalary);

        mockMvc.perform(get("/api/v1/employee/highest-salary"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(10000));

        verify(employeeService, times(1)).getHighestSalary();
    }

    @Test
    void getTopTenHighestEarningEmployeeNames() throws Exception {
        List<String> topEmployeeNames = List.of("John", "Jane");

        when(employeeService.getTopTenHighestEarningEmployeeNames()).thenReturn(topEmployeeNames);

        mockMvc.perform(get("/api/v1/employee/top-ten-earning-employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("John"))
                .andExpect(jsonPath("$[1]").value("Jane"));

        verify(employeeService, times(1)).getTopTenHighestEarningEmployeeNames();
    }

    //    @Test
    //    void createEmployee() throws Exception {
    //        EmployeeInputDto employeeInputDto = new EmployeeInputDto("John", 5000, 25, "");
    //        EmployeeDto createdEmployee = new EmployeeDto(UUID.randomUUID(), "John", 5000, 25, "", "");
    //
    //        when(employeeService.createEmployee(employeeInputDto)).thenReturn(createdEmployee);
    //
    //        mockMvc.perform(post("/api/v1/employee")
    //                        .contentType("application/json")
    //                        .content("{\"name\":\"John\", \"salary\":5000}"))
    //                .andExpect(status().isCreated())
    //                .andExpect(jsonPath("$.name").value("John"))
    //                .andExpect(jsonPath("$.salary").value(5000));
    //
    //        verify(employeeService, times(1)).createEmployee(employeeInputDto);
    //    }

    //    @Test
    //    void deleteEmployeeById() throws Exception {
    //        String employeeId = UUID.randomUUID().toString();
    //        String employeeName = "Jane";
    //
    //        when(employeeService.deleteEmployeeById(employeeId)).thenReturn(employeeName);
    //
    //        mockMvc.perform(delete("/api/v1/employee/{id}", employeeId))
    //                .andExpect(status().isOk())
    //                .andExpect(jsonPath("$").value(employeeName));
    //
    //        verify(employeeService, times(1)).deleteEmployeeById(employeeId);
    //    }
}
