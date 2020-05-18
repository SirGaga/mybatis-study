package com.zhangjie.test;

import com.zhangjie.bean.Department;
import com.zhangjie.bean.Employee;
import com.zhangjie.dao.*;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyBatisTest {
    /**
     * 1.接口式编程
     * 原生：      Dao---->DaoImpl
     * mybatis:   Mapper--->XxxMapper.xml
     * 2.SqlSession 代表和数据库的一次会话，用完必须关闭
     * 3.SqlSession 和 Connection 一样，非线程安全，每次使用都应该去获取对象
     * 4.mapper接口没有实现类，但是 mybatis 会为接口生成一个代理对象
     *      将接口和xml文件绑定
     *      EmployeeMapper employeeMapper = sqlSession.getMapper(EmployeeMapper.class);
     * 5.两个重要的配置文件：
     *      mybatis的全局配置文件：包含数据库连接池信息，事务管理器等系统运行环境
     *      sql映射文件：保存了每一个sql语句的映射信息，mybatis会从映射信息中将sql抽取出来
     * ===================================================================================
     * 1.根据配置文件（全局配置文件）创建一个 SqlSessionFactory 对象,有数据源一些运行环境信息
     * 2.sql映射文件，配置每一个sql已经sql的封装规则等
     * 3.将sql映射文件注册在全局配置文件中
     * 4.写代码，
     *      1.根据全局配置文件得到 SqlSessionFactory
     *      2.使用 SqlSessionFactory 获取到 SqlSession ,用它来进行增删改查,一个sqlSession代表和数据库的一次会话，用完关闭
     *      3.使用sql的唯一标识来告诉 mybatis 执行哪个sql，sql都是保存在 sql 映射文件中的
     * @throws IOException 抛出异常
     */
    @Test
    public void test() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        // 2.获取 SqlSession 实例，能直接执行已经映射的sql语句
        //      statement Unique identifier matching the statement to use. sql的唯一标识符，一般情况下是 namespace+.+id
        //      parameter A parameter object to pass to the statement. 执行sql要用的参数
        try (SqlSession session = sqlSessionFactory.openSession()) {
            Employee employee = session.selectOne("com.zhangjie.bean.EmployeeMapper.getEmployeeById", 1);
            System.out.println(employee);
        }

    }

    @Test
    public void test01() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();

        // 获取 SqlSession对象
        try(SqlSession sqlSession = sqlSessionFactory.openSession()){
            // 获取接口的实现类对象
            // 为接口自动地创建一个代理对象，代理对象进行增删改查
            EmployeeMapper employeeMapper = sqlSession.getMapper(EmployeeMapper.class);
            System.out.println(employeeMapper.getClass());
            System.out.println(employeeMapper.getEmployeeById(1));

        }

    }

    @Test
    public void test02() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();

        // 获取 SqlSession对象
        try(SqlSession sqlSession = sqlSessionFactory.openSession()){
            // 获取接口的实现类对象
            // 为接口自动地创建一个代理对象，代理对象进行增删改查
            EmployeeMapperAnnotation employeeMapperAnnotation = sqlSession.getMapper(EmployeeMapperAnnotation.class);
            System.out.println(employeeMapperAnnotation.getClass());
            System.out.println(employeeMapperAnnotation.getEmployeeById(1));

        }

    }

    /**
     * 1.mybatis 允许增删改定义以下返回值
     *      Integer、Long、Boolean
     * 2.需要手动提交数据
     * @throws IOException 抛出异常
     */
    @Test
    public void testAdd() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        // 获取 SqlSession对象,不带参数调用，不会自动提交
        try(SqlSession sqlSession = sqlSessionFactory.openSession()){
            // 获取接口的实现类对象
            // 为接口自动地创建一个代理对象，代理对象进行增删改查
            Employee employee = new Employee();
            employee.setLastName("Json");
            employee.setGender("0");
            employee.setEmail("Json@zhangjie.com");
            EmployeeMapper employeeMapper = sqlSession.getMapper(EmployeeMapper.class);
            Long rows = employeeMapper.addEmployee(employee);
            System.out.println(rows+"====="+employee.getId()+"=====");
            sqlSession.commit();

        }
    }

    @Test
    public void testUpdate() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        // 获取 SqlSession对象,不带参数调用，不会自动提交
        try(SqlSession sqlSession = sqlSessionFactory.openSession()){
            // 获取接口的实现类对象
            // 为接口自动地创建一个代理对象，代理对象进行增删改查
            EmployeeMapper employeeMapper = sqlSession.getMapper(EmployeeMapper.class);
            Employee employee = employeeMapper.getEmployeeById(3);
            employee.setGender("1");
            Integer rows = employeeMapper.editEmployeeById(employee);
            sqlSession.commit();
            System.out.println(rows);

        }
    }

    @Test
    public void testDel() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        // 获取 SqlSession对象,不带参数调用，不会自动提交
        try(SqlSession sqlSession = sqlSessionFactory.openSession()){
            // 获取接口的实现类对象
            // 为接口自动地创建一个代理对象，代理对象进行增删改查

            EmployeeMapper employeeMapper = sqlSession.getMapper(EmployeeMapper.class);
            Boolean success = employeeMapper.delEmployeeById(3);
            sqlSession.commit();
            System.out.println(success);

        }
    }

    @Test
    public void testMultiParams() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();

        // 获取 SqlSession对象
        try(SqlSession sqlSession = sqlSessionFactory.openSession()){
            // 获取接口的实现类对象
            // 为接口自动地创建一个代理对象，代理对象进行增删改查
            EmployeeMapper employeeMapper = sqlSession.getMapper(EmployeeMapper.class);
            employeeMapper.getEmployeeByIdAndLastName(1,"Tom");

        }

    }

    @Test
    public void testMap() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();

        // 获取 SqlSession对象
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            // 获取接口的实现类对象
            // 为接口自动地创建一个代理对象，代理对象进行增删改查
            EmployeeMapper employeeMapper = sqlSession.getMapper(EmployeeMapper.class);
            HashMap<String, Object> map = new HashMap<>();
            map.put("id", 1);
            map.put("lastName", "Tom");
            employeeMapper.getEmployeeByConditions(map);

        }
    }

    @Test
    public void testListReturn() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();

        // 获取 SqlSession对象
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            // 获取接口的实现类对象
            // 为接口自动地创建一个代理对象，代理对象进行增删改查
            EmployeeMapper employeeMapper = sqlSession.getMapper(EmployeeMapper.class);

            List<Employee> employeeList = employeeMapper.getEmployeeList("%o%");

            System.out.println(employeeList.size());

        }
    }

    @Test
    public void testMapReturn() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();

        // 获取 SqlSession对象
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            // 获取接口的实现类对象
            // 为接口自动地创建一个代理对象，代理对象进行增删改查
            EmployeeMapper employeeMapper = sqlSession.getMapper(EmployeeMapper.class);

            Map<String, Object> employeeMap = employeeMapper.getEmployeeMap(1);

            System.out.println(employeeMap.toString());


        }
    }
    @Test
    public void testMapListReturn() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();

        // 获取 SqlSession对象
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            // 获取接口的实现类对象
            // 为接口自动地创建一个代理对象，代理对象进行增删改查
            EmployeeMapper employeeMapper = sqlSession.getMapper(EmployeeMapper.class);

            Map<Integer, Employee> employeeMapList = employeeMapper.getEmployeeMapList("%o%");

            System.out.println(employeeMapList.toString());


        }
    }
    @Test
    public void testSelectInPlus() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        // 获取 SqlSession对象
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            EmployeePlusMapper plusMapper = sqlSession.getMapper(EmployeePlusMapper.class);
            Employee employee = plusMapper.getEmployeeById(1);
            System.out.println(employee);
        }
    }
    @Test
    public void testGetDetailEmployeeInfoById() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        // 获取 SqlSession对象
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            EmployeePlusMapper plusMapper = sqlSession.getMapper(EmployeePlusMapper.class);
            List<Employee> employeeList = plusMapper.getDetailEmployeeInfoById("%o%");
            System.out.println(employeeList);
        }
    }

    @Test
    public void testGetDetailEmployeeInfoByIdStep() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        // 获取 SqlSession对象
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            EmployeePlusMapper plusMapper = sqlSession.getMapper(EmployeePlusMapper.class);
            List<Employee> employeeList = plusMapper.getDetailEmployeeInfoByIdStep("%o%");
            System.out.println(employeeList);
        }
    }

    @Test
    public void testGetAdvancedDeptById() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        // 获取 SqlSession对象
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            DepartmentMapper departmentMapper = sqlSession.getMapper(DepartmentMapper.class);
            Department department = departmentMapper.getAdvancedDeptById(1);
            System.out.println(department);
        }
    }

    @Test
    public void testGetAdvancedDeptByIdWithStep() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        // 获取 SqlSession对象
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            DepartmentMapper departmentMapper = sqlSession.getMapper(DepartmentMapper.class);
            Department department = departmentMapper.getAdvancedDeptByIdWithStep(1);
            System.out.println(department);
            System.out.println(department.getEmployees());
        }
    }

    @Test
    public void testGetEmployeeByCondition() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        // 获取 SqlSession对象
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            EmployeeDynamicMapper mapper = sqlSession.getMapper(EmployeeDynamicMapper.class);
            Employee employee = new Employee();
            employee.setLastName("%o%");
            employee.setGender("0");
            List<Employee> employees = mapper.getEmployeeByCondition(employee);
            System.out.println(employees);

        }
    }


    public SqlSessionFactory getSqlSessionFactory() throws IOException {
        String resource = "mybatis-config.xml";
        try (InputStream inputStream = Resources.getResourceAsStream(resource)) {
            return new SqlSessionFactoryBuilder().build(inputStream);
        }
    }




}
