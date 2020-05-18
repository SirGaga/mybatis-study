package com.zhangjie.dao;

import com.zhangjie.bean.Department;
import com.zhangjie.bean.Employee;

import java.util.List;

public interface DepartmentMapper {

    public Department getDeptById(Integer id);

    public Department getAdvancedDeptById(Integer id);

    public Department getAdvancedDeptByIdWithStep(Integer id);


}
