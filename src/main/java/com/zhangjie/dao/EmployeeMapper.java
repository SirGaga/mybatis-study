package com.zhangjie.dao;

import com.zhangjie.bean.Employee;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface EmployeeMapper {

    // 返回多条记录封装成一个 Map<Integer,Employee> ，键是这条记录的主键，值是记录封装后的JavaBean
    @MapKey("id") // 告诉mybatis封装这个map的时候使用哪个属性作为主键
    public Map<Integer,Employee> getEmployeeMapList(String lastName);
    // 返回一条记录的map，key是列名字，value是对应的值
    public Map<String,Object> getEmployeeMap(Integer id);

    public List<Employee> getEmployeeList(String lastName);

    public Employee getEmployeeByConditions(Map<String,Object> map);

    public Employee getEmployeeByIdAndLastName(@Param("id") Integer id, @Param("lastName") String lastName);

    public Employee getEmployeeById(Integer id);

    public Long addEmployee(Employee employee);

    public Boolean delEmployeeById(Integer id);

    public Integer editEmployeeById(Employee employee);

}
