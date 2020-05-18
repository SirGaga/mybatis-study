package com.zhangjie.dao;

import com.zhangjie.bean.Employee;

import java.util.List;

public interface EmployeeDynamicMapper {
    List<Employee> getEmployeeByCondition(Employee employee);
}
