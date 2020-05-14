package com.zhangjie.dao;

import com.zhangjie.bean.Employee;
import org.apache.ibatis.annotations.Param;

public interface EmployeeMapper {

    public Employee getEmployeeByIdAndLastName(@Param("id") Integer id, @Param("lastName") String lastName);

    public Employee getEmployeeById(Integer id);

    public Long addEmployee(Employee employee);

    public Boolean delEmployeeById(Integer id);

    public Integer editEmployeeById(Employee employee);

}
