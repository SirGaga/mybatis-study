package com.zhangjie.dao;

import com.zhangjie.bean.Employee;
import org.apache.ibatis.annotations.Select;

public interface EmployeeMapperAnnotation {
    @Select("select * from tbl_employee where id = #{id}")
    public Employee getEmployeeById(Integer id);
}
