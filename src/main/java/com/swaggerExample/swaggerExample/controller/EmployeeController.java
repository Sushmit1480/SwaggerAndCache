package com.swaggerExample.swaggerExample.controller;

import com.swaggerExample.swaggerExample.model.Employee;
import com.swaggerExample.swaggerExample.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/employees")
@CrossOrigin(origins = "http://localhost:4200")
public class EmployeeController {
    @Autowired
    private EmployeeService service;

    @Operation(summary = "Create a new employee", description = "Adds a new employee to the database")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Employee created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping("/create")
    public ResponseEntity<Integer> createEmployee(@RequestParam("firstName") String firstName,
    		@RequestParam("lastName") String lastName,
    		@RequestParam("mobile") String mobile,
    		@RequestParam("email") String email,
    		@RequestParam("salary") double salary,
    		@RequestParam("photo") MultipartFile photoFile) throws IOException {
    	byte[] photoBytes = photoFile.getBytes();
    	Employee employee = new Employee(firstName, lastName, mobile, email, salary, photoBytes);
    	Employee create = service.createEmployee(employee);
        return ResponseEntity.ok(create.getId());
    }

    @Operation(summary = "Get all employees", description = "Retrieves a list of all employees")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved list of employees"),
    })
    @GetMapping()
    public List<Employee> getAllEmployees() {
        return service.getAllEmployees();
    }

    @Operation(summary = "Get employee by ID", description = "Retrieves a single employee by their ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved employee"),
        @ApiResponse(responseCode = "404", description = "Employee not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable("id") Integer id) {
        Optional<Employee> employee = service.getEmployeeById(id);
        return employee.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Update an employee", description = "Updates an existing employee by their ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Employee updated successfully"),
        @ApiResponse(responseCode = "404", description = "Employee not found")
    })
    @PutMapping("/update/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable("id") Integer id,
                                                   @RequestParam("firstName") String firstName,
                                                   @RequestParam("lastName") String lastName,
                                                   @RequestParam("mobile") String mobile,
                                                   @RequestParam("email") String email,
                                                   @RequestParam("salary") double salary,
                                                   @RequestParam(value = "photo", required = false) MultipartFile photoFile) throws IOException {
        // Retrieve the existing employee details from the database
        Optional<Employee> existingEmployeeOptional = service.getEmployeeById(id);
        if (!existingEmployeeOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        
        Employee existingEmployee = existingEmployeeOptional.get();
        
        // If a new photo is provided, update the photo; otherwise keep the old one
        byte[] photoBytes = (photoFile != null) ? photoFile.getBytes() : existingEmployee.getPhoto();

        // Update the employee details
        existingEmployee.setFirstName(firstName);
        existingEmployee.setLastName(lastName);
        existingEmployee.setMobile(mobile);
        existingEmployee.setEmail(email);
        existingEmployee.setSalary(salary);
        existingEmployee.setPhoto(photoBytes); // This will either be the new photo or the old one

        // Save the updated employee
        Employee updatedEmployee = service.updateEmployee(id, existingEmployee);
        return ResponseEntity.ok(updatedEmployee);
    }


    @Operation(summary = "Delete an employee", description = "Deletes an existing employee by their ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Employee deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Employee not found")
    })
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable("id") Integer id) {
        try {
            service.deleteEmployee(id);
            return ResponseEntity.ok("Employee with ID: " + id + " has been deleted successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body("Employee with ID: " + id + " not found.");
        }
    }
    
}
