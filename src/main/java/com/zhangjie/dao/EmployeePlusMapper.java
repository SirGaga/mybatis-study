package com.zhangjie.dao;

import com.zhangjie.bean.Employee;

import java.util.List;

public interface EmployeePlusMapper {

    public Employee getEmployeeById(Integer id);

    public List<Employee> getDetailEmployeeInfoById(String lastName);

    public List<Employee> getDetailEmployeeInfoByIdStep(String lastName);

}
