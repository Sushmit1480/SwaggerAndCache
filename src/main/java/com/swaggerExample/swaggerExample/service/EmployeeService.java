package com.swaggerExample.swaggerExample.service;

import java.util.List;
import java.util.Optional;

import com.swaggerExample.swaggerExample.model.Employee;

public interface EmployeeService {
	Employee createEmployee(Employee employee);
	List<Employee> getAllEmployees();
	Optional<Employee> getEmployeeById(Integer id);
	Employee updateEmployee(Integer id, Employee employee);
	void deleteEmployee(Integer id);
}
