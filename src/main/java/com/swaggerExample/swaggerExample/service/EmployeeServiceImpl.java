package com.swaggerExample.swaggerExample.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.swaggerExample.swaggerExample.model.Employee;
import com.swaggerExample.swaggerExample.repository.EmployeeRepo;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepo repo;

    // Cache the newly created employee
    @Override
    @CachePut(value = "employee", key = "#employee.id")
    public Employee createEmployee(Employee employee) {
        return repo.save(employee);
    }

    // Cache the list of all employees
    public List<Employee> getAllEmployees() {
        return repo.findAll();
    }

    // Cache employee by id
    @Override
    @Cacheable(value = "employee", key = "#id")
    public Optional<Employee> getEmployeeById(Integer id) {
        return repo.findById(id);
    }

    // Update the cached employee by id after updating
    @Override
    @CachePut(value = "employee", key = "#id")
    public Employee updateEmployee(Integer id, Employee employee) {
        Optional<Employee> emp = repo.findById(id);

        if (emp.isPresent()) {
            Employee update = emp.get();
            update.setFirstName(employee.getFirstName());
            update.setLastName(employee.getLastName());
            update.setEmail(employee.getEmail());
            update.setSalary(employee.getSalary());
            update.setMobile(employee.getMobile());
            update.setPhoto(employee.getPhoto());
            return repo.save(update);
        } else {
            throw new RuntimeException("Employee not found with id: " + id);
        }
    }

    // Evict the employee from the cache after deleting
    @Override
    @CacheEvict(value = "employee", key = "#id")
    public void deleteEmployee(Integer id) {
        Optional<Employee> employee = repo.findById(id);
        if (employee.isPresent()) {
            repo.delete(employee.get());
        } else {
            throw new RuntimeException("Employee not found with id: " + id);
        }
    }
    
}
