package com.reliaquest.api.client;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import com.reliaquest.api.model.DeleteEmployeeDto;
import com.reliaquest.api.model.EmployeeDto;
import com.reliaquest.api.model.EmployeeInputDto;
import com.reliaquest.api.model.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;

class MockEmployeeClientTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private MockEmployeeClient mockEmployeeClient;

    private EmployeeDto employeeDto;
    private EmployeeInputDto employeeInputDto;
    private UUID employeeId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        employeeId = UUID.randomUUID();
        employeeDto = EmployeeDto.builder()
            .id(employeeId)
            .name("John Doe")
            .salary(50000)
            .age(30)
            .title("Software Engineer")
            .email("johndoe@example.com")
            .build();

        employeeInputDto = EmployeeInputDto.builder()
            .name("John Doe")
            .salary(50000)
            .age(30)
            .title("Software Engineer")
            .build();
    }

    @Test
    void testGetAllEmployeesSuccess() {

        List<EmployeeDto> employeeList = List.of(employeeDto);
        Response<List<EmployeeDto>> expectedResponse = Response.handledWith(employeeList);

        when(restTemplate.exchange(any(String.class), eq(org.springframework.http.HttpMethod.GET), any(), eq(new ParameterizedTypeReference<Response<List<EmployeeDto>>>() {})))
            .thenReturn(ResponseEntity.ok(expectedResponse));

        Response<List<EmployeeDto>> response = mockEmployeeClient.getAllEmployees();

        assertEquals(response.status(), expectedResponse.status());
        assertEquals(1, response.data().size());
        assertEquals(employeeDto, response.data().get(0));
    }

    @Test
    void testGetEmployeeByIdSuccess() {

        Response<EmployeeDto> expectedResponse = Response.handledWith(employeeDto);

        when(restTemplate.exchange(any(String.class), eq(org.springframework.http.HttpMethod.GET), any(), eq(EmployeeDto.class)))
            .thenReturn(ResponseEntity.ok(employeeDto));

        Response<EmployeeDto> response = mockEmployeeClient.getEmployeeById(employeeId);

        assertFalse(response.status().equals(HttpStatus.OK));
        assertEquals(employeeDto, response.data());
    }

    @Test
    void testCreateEmployeeSuccess() {

        Response<EmployeeDto> expectedResponse = Response.handledWith(employeeDto);

        when(restTemplate.exchange(any(String.class), eq(org.springframework.http.HttpMethod.POST), any(), eq(EmployeeDto.class)))
            .thenReturn(ResponseEntity.ok(employeeDto));

        Response<EmployeeDto> response = mockEmployeeClient.createEmployee(employeeInputDto);

        assertFalse(response.status().equals(HttpStatus.OK));
        assertEquals(employeeDto, response.data());
    }

    @Test
    void testDeleteEmployeeSuccess() {

        DeleteEmployeeDto deleteEmployeeDto = new DeleteEmployeeDto(employeeDto.getName());
        Response<Boolean> expectedResponse = Response.handledWith(true);

        when(restTemplate.exchange(any(String.class), eq(org.springframework.http.HttpMethod.DELETE), any(), eq(Boolean.class)))
            .thenReturn(ResponseEntity.ok(true));

        Response<Boolean> response = mockEmployeeClient.deleteEmployee(deleteEmployeeDto);

        assertFalse(response.status().equals(HttpStatus.OK));
        assertTrue(response.data());
    }

    @Test
    void testGetAllEmployeesFailure() {

        when(restTemplate.exchange(any(String.class), eq(org.springframework.http.HttpMethod.GET), any(), eq(new ParameterizedTypeReference<Response<List<EmployeeDto>>>() {})))
            .thenThrow(new HttpStatusCodeException(HttpStatus.BAD_REQUEST) {});

        Response<List<EmployeeDto>> response = mockEmployeeClient.getAllEmployees();

        assertFalse(response.status().equals(HttpStatus.BAD_REQUEST));
        assertEquals("Failed to fetch employees: 400 BAD_REQUEST", response.error());
    }

    @Test
    void testGetEmployeeByIdFailure() {

        when(restTemplate.exchange(any(String.class), eq(org.springframework.http.HttpMethod.GET), any(), eq(EmployeeDto.class)))
            .thenThrow(new HttpStatusCodeException(HttpStatus.NOT_FOUND) {});

        Response<EmployeeDto> response = mockEmployeeClient.getEmployeeById(employeeId);

        assertFalse(response.status().equals(HttpStatus.NOT_FOUND));
        assertEquals("Employee not found: 404 NOT_FOUND", response.error());
    }

    @Test
    void testCreateEmployeeFailure() {

        when(restTemplate.exchange(any(String.class), eq(org.springframework.http.HttpMethod.POST), any(), eq(EmployeeDto.class)))
            .thenThrow(new HttpStatusCodeException(HttpStatus.BAD_REQUEST) {});

        Response<EmployeeDto> response = mockEmployeeClient.createEmployee(employeeInputDto);

        assertFalse(response.status().equals(HttpStatus.BAD_REQUEST));
        assertEquals("Failed to create employee: 400 BAD_REQUEST", response.error());
    }

    @Test
    void testDeleteEmployeeFailure() {

        DeleteEmployeeDto deleteEmployeeDto = new DeleteEmployeeDto(employeeDto.getName());
        when(restTemplate.exchange(any(String.class), eq(org.springframework.http.HttpMethod.DELETE), any(), eq(Boolean.class)))
            .thenThrow(new HttpStatusCodeException(HttpStatus.NOT_FOUND) {});

        Response<Boolean> response = mockEmployeeClient.deleteEmployee(deleteEmployeeDto);

        assertFalse(response.status().equals(HttpStatus.NOT_FOUND));
        assertEquals("Failed to delete employee: 404 NOT_FOUND", response.error());
    }
}
