package com.vinutna.hrms.service.impl;

import com.vinutna.hrms.entity.Employee;
import com.vinutna.hrms.exception.DuplicateResourceException;
import com.vinutna.hrms.exception.ResourceNotFoundException;
import com.vinutna.hrms.repository.EmployeeRepository;
import com.vinutna.hrms.service.EmployeeService;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
    }

    @Override
    public Employee createEmployee(Employee employee) {
        if (employeeRepository.existsByEmail(employee.getEmail())) {
            throw new DuplicateResourceException("Employee already exists with email: " + employee.getEmail());
        }
        return employeeRepository.save(employee);
    }

    @Override
    public Employee updateEmployee(Long id, Employee employeeDetails) {
        Employee existingEmployee = getEmployeeById(id);

        if (!existingEmployee.getEmail().equalsIgnoreCase(employeeDetails.getEmail())
                && employeeRepository.existsByEmail(employeeDetails.getEmail())) {
            throw new DuplicateResourceException("Employee already exists with email: " + employeeDetails.getEmail());
        }

        existingEmployee.setFirstName(employeeDetails.getFirstName());
        existingEmployee.setLastName(employeeDetails.getLastName());
        existingEmployee.setEmail(employeeDetails.getEmail());
        existingEmployee.setDepartment(employeeDetails.getDepartment());

        return employeeRepository.save(existingEmployee);
    }

    @Override
    public void deleteEmployee(Long id) {
        Employee existingEmployee = getEmployeeById(id);
        employeeRepository.delete(existingEmployee);
    }
}
