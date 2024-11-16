package com.swaggerExample.swaggerExample.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.swaggerExample.swaggerExample.model.Employee;

@Service
public class EmployeeScheduler {

	@Autowired
	private EmployeeService employeeService;
	
	@Scheduled(fixedRate = 10000)
	public void fetchAllEmployees() {
		@SuppressWarnings("unused")
		List<Employee> employees = employeeService.getAllEmployees();
//	    System.out.println("Fetched employees: " + employees);
	}
}
