package com.reliaquest.api.service;

import com.reliaquest.api.client.MockEmployeeClient;
import com.reliaquest.api.model.DeleteEmployeeDto;
import com.reliaquest.api.model.EmployeeDto;
import com.reliaquest.api.model.EmployeeInputDto;
import java.util.*;
import java.util.stream.Collectors;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@NoArgsConstructor
public class EmployeeService {

    private MockEmployeeClient employeeClient;

    @Autowired
    public EmployeeService(MockEmployeeClient employeeClient) {
        this.employeeClient = employeeClient;
    }

    public List<EmployeeDto> getAllEmployees() {
        return employeeClient.getAllEmployees().data();
    }

    public List<EmployeeDto> searchEmployeesByName(String searchString) {
        List<EmployeeDto> employeeDtos = employeeClient.getAllEmployees().data();
        return employeeDtos.stream()
                .filter(emp -> emp.getName().toLowerCase().contains(searchString.toLowerCase()))
                .collect(Collectors.toList());
    }

    public EmployeeDto getEmployeeById(UUID id) {
        return employeeClient.getEmployeeById(id).data();
    }

    public Integer getHighestSalary() {
        List<EmployeeDto> employeeDtos = employeeClient.getAllEmployees().data();
        return employeeDtos.stream().mapToInt(EmployeeDto::getSalary).max().orElse(0);
    }

    public List<String> getTopTenHighestEarningEmployeeNames() {
        List<EmployeeDto> employeeDtos = employeeClient.getAllEmployees().data();
        return employeeDtos.stream()
                .sorted((emp1, emp2) ->
                        Integer.compare(emp2.getSalary(), emp1.getSalary())) // Sort in descending order of salary
                .limit(10)
                .map(EmployeeDto::getName)
                .collect(Collectors.toList());
    }

    public EmployeeDto createEmployee(EmployeeInputDto employeeInput) {
        return employeeClient.createEmployee(employeeInput).data();
    }

    public String deleteEmployeeById(String id) {
        EmployeeDto employeeDto =
                employeeClient.getEmployeeById(UUID.fromString(id)).data();
        DeleteEmployeeDto deleteEmployeeDto =
                DeleteEmployeeDto.builder().name(employeeDto.getName()).build();
        employeeClient.deleteEmployee(deleteEmployeeDto);
        return employeeDto.getName();
    }
}
