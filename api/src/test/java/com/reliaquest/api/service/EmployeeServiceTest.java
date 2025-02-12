package com.reliaquest.api.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.reliaquest.api.client.MockEmployeeClient;
import com.reliaquest.api.model.DeleteEmployeeDto;
import com.reliaquest.api.model.EmployeeDto;
import com.reliaquest.api.model.EmployeeInputDto;
import com.reliaquest.api.model.Response;
import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

class EmployeeServiceTest {

    @Mock
    private MockEmployeeClient employeeClient;

    @InjectMocks
    private EmployeeService employeeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllEmployees() {
        EmployeeDto employee1 = new EmployeeDto(UUID.randomUUID(), "John", 5000, 25, "", "");
        EmployeeDto employee2 = new EmployeeDto(UUID.randomUUID(), "Jane", 6000, 25, "", "");
        List<EmployeeDto> employeeDtos = Arrays.asList(employee1, employee2);

        // Mocking the response with data and status
        Response<List<EmployeeDto>> mockResponse = new Response<>(employeeDtos, Response.Status.HANDLED, null);
        when(employeeClient.getAllEmployees()).thenReturn(mockResponse);

        List<EmployeeDto> result = employeeService.getAllEmployees();
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("John", result.get(0).getName());
    }

    @Test
    void testSearchEmployeesByName() {
        EmployeeDto employee1 = new EmployeeDto(UUID.randomUUID(), "John", 5000, 25, "", "");
        EmployeeDto employee2 = new EmployeeDto(UUID.randomUUID(), "Jane", 6000, 25, "", "");
        List<EmployeeDto> employeeDtos = Arrays.asList(employee1, employee2);

        // Mocking the response with data and status
        Response<List<EmployeeDto>> mockResponse = new Response<>(employeeDtos, Response.Status.HANDLED, null);
        when(employeeClient.getAllEmployees()).thenReturn(mockResponse);

        List<EmployeeDto> result = employeeService.searchEmployeesByName("John");
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("John", result.get(0).getName());
    }

    @Test
    void testGetEmployeeById() {
        UUID employeeId = UUID.randomUUID();
        EmployeeDto employee = new EmployeeDto(employeeId, "John", 5000, 25, "", "");

        // Mocking the response with a single employee data
        Response<EmployeeDto> mockResponse = new Response<>(employee, Response.Status.HANDLED, null);
        when(employeeClient.getEmployeeById(employeeId)).thenReturn(mockResponse);

        EmployeeDto result = employeeService.getEmployeeById(employeeId);
        assertNotNull(result);
        assertEquals("John", result.getName());
    }

    @Test
    void testGetHighestSalary() {
        EmployeeDto employee1 = new EmployeeDto(UUID.randomUUID(), "John", 5000, 25, "", "");
        EmployeeDto employee2 = new EmployeeDto(UUID.randomUUID(), "Jane", 6000, 25, "", "");
        List<EmployeeDto> employeeDtos = Arrays.asList(employee1, employee2);

        // Mocking the response with data and status
        Response<List<EmployeeDto>> mockResponse = new Response<>(employeeDtos, Response.Status.HANDLED, null);
        when(employeeClient.getAllEmployees()).thenReturn(mockResponse);

        Integer result = employeeService.getHighestSalary();
        assertEquals(6000, result);
    }

    @Test
    void testGetTopTenHighestEarningEmployeeNames() {
        EmployeeDto employee1 = new EmployeeDto(UUID.randomUUID(), "John", 5000, 25, "", "");
        EmployeeDto employee2 = new EmployeeDto(UUID.randomUUID(), "Jane", 6000, 25, "", "");
        EmployeeDto employee3 = new EmployeeDto(UUID.randomUUID(), "Alice", 7000, 25, "", "");
        List<EmployeeDto> employeeDtos = Arrays.asList(employee1, employee2, employee3);

        // Mocking the response with data and status
        Response<List<EmployeeDto>> mockResponse = new Response<>(employeeDtos, Response.Status.HANDLED, null);
        when(employeeClient.getAllEmployees()).thenReturn(mockResponse);

        List<String> result = employeeService.getTopTenHighestEarningEmployeeNames();
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals("Alice", result.get(0));
    }

    @Test
    void testCreateEmployee() {
        EmployeeInputDto input = new EmployeeInputDto("John", 5000, 25, "");
        EmployeeDto createdEmployee = new EmployeeDto(UUID.randomUUID(), "John", 5000, 25, "", "");

        // Mocking the response with data and status
        Response<EmployeeDto> mockResponse = new Response<>(createdEmployee, Response.Status.HANDLED, null);
        when(employeeClient.createEmployee(input)).thenReturn(mockResponse);

        EmployeeDto result = employeeService.createEmployee(input);
        assertNotNull(result);
        assertEquals("John", result.getName());
    }

    @Test
    void testDeleteEmployeeById() {
        String employeeId = UUID.randomUUID().toString();
        EmployeeDto employee = new EmployeeDto(UUID.fromString(employeeId), "John", 5000, 25, "", "");
        DeleteEmployeeDto deleteEmployeeDto = new DeleteEmployeeDto("John");

        // Mocking the response with employee data
        Response<EmployeeDto> mockResponse = new Response<>(employee, Response.Status.HANDLED, null);
        when(employeeClient.getEmployeeById(UUID.fromString(employeeId))).thenReturn(mockResponse);

        String result = employeeService.deleteEmployeeById(employeeId);
        assertNotNull(result);
        assertEquals("John", result);
        verify(employeeClient, times(1)).deleteEmployee(deleteEmployeeDto);
    }
}
