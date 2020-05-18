package com.zhangjie.dao;

import com.zhangjie.bean.Employee;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface EmployeeDynamicMapper {
    List<Employee> getEmployeeByCondition(Employee employee);
    List<Employee> getEmployeeByConditionTrim(Employee employee);

    List<Employee> getEmployeeByConditionChoose(Employee employee);

    void updateEmployee(Employee e);

    List<Employee> getEmployeeByConditionForeach(@Param("ids") List<Integer> ids);
}
