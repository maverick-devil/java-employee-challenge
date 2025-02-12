package com.reliaquest.api.client;

import com.reliaquest.api.model.DeleteEmployeeDto;
import com.reliaquest.api.model.EmployeeDto;
import com.reliaquest.api.model.EmployeeInputDto;
import com.reliaquest.api.model.Response;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

@Service
public class MockEmployeeClient {

    private final RestTemplate restTemplate;

    @Value("${mock.employee.api.url}")
    private String apiUrl;

    @Autowired
    public MockEmployeeClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Response<List<EmployeeDto>> getAllEmployees() {
        String url = apiUrl + "/api/v1/employee";

        try {
            ResponseEntity<Response<List<EmployeeDto>>> response =
                    restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<>() {});
            return Response.handledWith(response.getBody().data());
        } catch (HttpStatusCodeException e) {
            return Response.error("Failed to fetch employees: " + e.getMessage());
        }
    }

    public Response<EmployeeDto> getEmployeeById(UUID id) {
        String url = apiUrl + "/api/v1/employee/" + id;

        try {
            ResponseEntity<EmployeeDto> response = restTemplate.exchange(url, HttpMethod.GET, null, EmployeeDto.class);
            return Response.handledWith(response.getBody());
        } catch (HttpStatusCodeException e) {
            return Response.error("Employee not found: " + e.getMessage());
        }
    }

    public Response<EmployeeDto> createEmployee(EmployeeInputDto input) {
        String url = apiUrl + "/api/v1/employee";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<EmployeeInputDto> entity = new HttpEntity<>(input, headers);

        try {
            ResponseEntity<EmployeeDto> response =
                    restTemplate.exchange(url, HttpMethod.POST, entity, EmployeeDto.class);
            return Response.handledWith(response.getBody());
        } catch (HttpStatusCodeException e) {
            return Response.error("Failed to create employee: " + e.getMessage());
        }
    }

    public Response<Boolean> deleteEmployee(DeleteEmployeeDto input) {
        String url = apiUrl + "/api/v1/employee";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<DeleteEmployeeDto> entity = new HttpEntity<>(input, headers);

        try {
            ResponseEntity<Boolean> response = restTemplate.exchange(url, HttpMethod.DELETE, entity, Boolean.class);
            return Response.handledWith(response.getBody());
        } catch (HttpStatusCodeException e) {
            return Response.error("Failed to delete employee: " + e.getMessage());
        }
    }
}
