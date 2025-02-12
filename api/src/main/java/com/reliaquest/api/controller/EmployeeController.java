package com.reliaquest.api.controller;

import com.reliaquest.api.model.EmployeeDto;
import com.reliaquest.api.model.EmployeeInputDto;
import com.reliaquest.api.service.EmployeeService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/employee")
@Slf4j
public class EmployeeController implements IEmployeeController<EmployeeDto, EmployeeInputDto> {

    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Override
    @GetMapping
    @RateLimiter(name = "employeeController", fallbackMethod = "fallbackMethod")
    public ResponseEntity<List<EmployeeDto>> getAllEmployees() {
        log.info("Processing all employees request");
        List<EmployeeDto> employeeDtos = employeeService.getAllEmployees();
        return ResponseEntity.ok(employeeDtos);
    }

    @Override
    @GetMapping("/by-name/{name}")
    @RateLimiter(name = "rateLimiter", fallbackMethod = "fallbackMethod")
    public ResponseEntity<List<EmployeeDto>> getEmployeesByNameSearch(@PathVariable("name") String searchString) {
        log.info("Processing all employees request with name {}", searchString);
        List<EmployeeDto> employeeDtos = employeeService.searchEmployeesByName(searchString);
        return ResponseEntity.ok(employeeDtos);
    }

    @Override
    @GetMapping("/{id}")
    @RateLimiter(name = "rateLimiter", fallbackMethod = "fallbackMethod")
    public ResponseEntity<EmployeeDto> getEmployeeById(@PathVariable("id") String id) {
        log.info("Processing employee request with id {}", id);
        EmployeeDto employeeDto = employeeService.getEmployeeById(UUID.fromString(id));
        return ResponseEntity.ok(employeeDto);
    }

    @Override
    @GetMapping("/highest-salary")
    @RateLimiter(name = "rateLimiter", fallbackMethod = "fallbackMethod")
    public ResponseEntity<Integer> getHighestSalaryOfEmployees() {
        log.info("Processing highest salary request");
        Integer highestSalary = employeeService.getHighestSalary();
        return ResponseEntity.ok(highestSalary);
    }

    @Override
    @GetMapping("/top-ten-earning-employees")
    @RateLimiter(name = "rateLimiter", fallbackMethod = "fallbackMethod")
    public ResponseEntity<List<String>> getTopTenHighestEarningEmployeeNames() {
        log.info("Processing top ten earning employees request");
        List<String> topEmployeeNames = employeeService.getTopTenHighestEarningEmployeeNames();
        return ResponseEntity.ok(topEmployeeNames);
    }

    @Override
    @PostMapping
    @RateLimiter(name = "rateLimiter", fallbackMethod = "fallbackMethod")
    public ResponseEntity<EmployeeDto> createEmployee(EmployeeInputDto employeeInput) {
        log.info("Processing create employee request: {}", employeeInput);
        EmployeeDto createdEmployeeDto = employeeService.createEmployee(employeeInput);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdEmployeeDto);
    }

    @Override
    @DeleteMapping("/{id}")
    @RateLimiter(name = "rateLimiter", fallbackMethod = "fallbackMethod")
    public ResponseEntity<String> deleteEmployeeById(@PathVariable String id) {
        log.info("Processing delete employee request with id {}", id);
        return ResponseEntity.ok(employeeService.deleteEmployeeById(id));
    }

    public ResponseEntity<String> fallbackMethod(Throwable throwable) {
        log.warn("Rate limit exceeded: {}", throwable.getMessage());
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body("Too many requests in short interval! Please try again later.");
    }
}
