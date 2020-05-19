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
import java.util.*;

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
    @Test
    public void testGetEmployeeByConditionTrim() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        // 获取 SqlSession对象
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            EmployeeDynamicMapper mapper = sqlSession.getMapper(EmployeeDynamicMapper.class);
            Employee employee = new Employee();
            employee.setLastName("%o%");
            employee.setGender("0");
            List<Employee> employees = mapper.getEmployeeByConditionTrim(employee);
            System.out.println(employees);
        }
    }
    @Test
    public void testGetEmployeeByConditionChoose() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        // 获取 SqlSession对象
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            EmployeeDynamicMapper mapper = sqlSession.getMapper(EmployeeDynamicMapper.class);
            Employee employee = new Employee();
            employee.setId(1);
            employee.setLastName("%o%");

            List<Employee> employees = mapper.getEmployeeByConditionChoose(employee);
            System.out.println(employees);
        }
    }
    @Test
    public void testGetEmployeeByConditionSet() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        // 获取 SqlSession对象
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            EmployeeDynamicMapper mapper = sqlSession.getMapper(EmployeeDynamicMapper.class);
            Employee employee = new Employee();
            employee.setId(1);
            employee.setLastName("Tommy");
            employee.setGender("1");

            mapper.updateEmployee(employee);
            sqlSession.commit();
        }
    }
    @Test
    public void testGetEmployeeByConditionForeach() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        // 获取 SqlSession对象
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            EmployeeDynamicMapper mapper = sqlSession.getMapper(EmployeeDynamicMapper.class);

            List<Employee> employees = mapper.getEmployeeByConditionForeach(Arrays.asList(1, 2, 3, 4, 5, 6, 7));
            System.out.println(employees);
        }
    }

    @Test
    public void testAddEmployees() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        // 获取 SqlSession对象
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            EmployeeDynamicMapper mapper = sqlSession.getMapper(EmployeeDynamicMapper.class);
            Employee employee = new Employee();
            Department department = new Department();
            department.setId(1);
            employee.setLastName("Tommy");
            employee.setEmail("Tommy@zhangjie.com");
            employee.setGender("1");
            employee.setDept(department);
            Employee employee2 = new Employee();
            employee2.setLastName("David");
            employee2.setEmail("David@zhangjie.com");
            employee2.setGender("1");
            employee2.setDept(department);
            List<Employee> employeeList = new ArrayList<>();
            employeeList.add(employee);
            employeeList.add(employee2);
            mapper.addEmployees(employeeList);
            sqlSession.commit();
        }
    }
    @Test
    public void testGetEmployeeWithInnerParameter() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        // 获取 SqlSession对象
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            EmployeeDynamicMapper mapper = sqlSession.getMapper(EmployeeDynamicMapper.class);
            Employee employee = new Employee();
            Department department = new Department();
            department.setId(1);
            employee.setLastName("o");
            employee.setEmail("Tommy@zhangjie.com");
            employee.setGender("1");
            employee.setDept(department);

            List<Employee> employees = mapper.getEmployeeWithInnerParameter(employee);//null
            System.out.println(employees);
        }
    }

    public SqlSessionFactory getSqlSessionFactory() throws IOException {
        String resource = "mybatis-config.xml";
        try (InputStream inputStream = Resources.getResourceAsStream(resource)) {
            return new SqlSessionFactoryBuilder().build(inputStream);
        }
    }
    /**
     * mybatis提供两级缓存
     * 一级缓存 本地缓存 sqlSession级别的缓存，是一直开启的，无法关闭，本质上来时就是基于sqlSession的一个Map
     *  与数据库同一次会话期间查询到的数据会放在本地缓存中，如果要获取相同的数据，直接从缓存中拿，没必要去查询数据库
     *  一级缓存的失效情况（需要向数据库再次发送sql进行查询）：
     *      1.sqlSession不同
     *      2.sqlSession相同，查询条件不同（当前一级缓存中还没有这个数据）
     *      3.sqlSession相同，两次查询之间执行了增删改操作(这次增删改可能对当前查询到的数据有影响，缓存失效)
     *      4.sqlSession相同，手动清除了一级缓存
     * 二级缓存 全局缓存 基于namespace级别的缓存，一个名称namespace对应一个二级缓存
     *  工作机制：
     *      1.一个会话查询一条数据，这个数据就会被放在当前会话的一级缓存中；
     *      2.如果会话关闭的话，一级缓存中的数据会被保存到二级缓存中，新的会话查询就可以参考二级缓存中的内容
     *      3.不同namespace查出的数据会放在自己对应的缓存中（map）
     *  注意：查出的数据都会被默认放在一级缓存中，只有会话提交或者关闭以后，一级缓存中的数据才会转移到二级缓存中
     *  使用：
     *      1.开启全局二级缓存配置 cacheEnabled
     *      2.去mapper.xml中配置使用二级缓存 <mapper><cache></cache></mapper>
     *      3.POJO需要实现序列化接口
     *  和缓存有关的设置和属性
     *      1.全局配置中的 cacheEnabled=true;false:关闭缓存（二级缓存关闭、一级缓存未关闭）
     *      2.mapper.xml中的每个 select 标签都有 useCache="true" 这个属性，false 代表不使用二级缓存、一级缓存可以照常使用
     *      3.mapper.xml中的每个 增删改 标签都有 flushCache="true" 这个属性，增删改之后就会清除缓存，一级缓存会清空，二级缓存也会清空
     *      4.mapper.xml中的每个 select 标签都有 flushCache="false"这个属性，如果设置为 true，那么每次查询都会清空一二级缓存
     *      5.sqlSession.clearCache();方法只会清空当前session的一级缓存，不会清空二级缓存
     *      6.全局配置中的 localCacheScope 本地缓存作用域，影响一级缓存，默认Session，即当前会话的所有数据保存在会话缓存中；
     *          取值为STATEMENT表示可以禁用一级缓存
     */
    @Test
    public void testFirstLevelCache() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        Employee employee01;
        Employee employee03;

        //1.
        /*try(SqlSession sqlSession = sqlSessionFactory.openSession()){
            EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);
            employee01 = mapper.getEmployeeById(1);
            System.out.println(employee01);

            Employee employee02 = mapper.getEmployeeById(2);
            System.out.println(employee02);
            System.out.println(employee01 == employee02);
        }
        try(SqlSession sqlSession = sqlSessionFactory.openSession()){
            EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);
            employee03 = mapper.getEmployeeById(1);
            System.out.println(employee03);

            Employee employee04 = mapper.getEmployeeById(1);
            System.out.println(employee03);
            System.out.println(employee03 == employee04);
            System.out.println(employee01 == employee03);
        }*/

        //2.
        /*try(SqlSession sqlSession = sqlSessionFactory.openSession()){
            EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);
            employee01 = mapper.getEmployeeById(1);
            System.out.println(employee01);

            Employee employee02 = mapper.getEmployeeById(2);
            System.out.println(employee02);
            System.out.println(employee01 == employee02);
            employee03 = mapper.getEmployeeById(1);
            System.out.println(employee03);
            System.out.println(employee01 == employee03);
        }*/
        //3.
        /*try(SqlSession sqlSession = sqlSessionFactory.openSession()){
            EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);
            employee01 = mapper.getEmployeeById(1);
            System.out.println(employee01);
            Employee employee = new Employee();
            employee.setLastName("Paul");
            employee.setEmail("Paul@zhangjie.com");
            employee.setGender("1");
            mapper.addEmployee(employee);
            sqlSession.commit();
            Employee employee02 = mapper.getEmployeeById(1);
            System.out.println(employee02);
            System.out.println(employee01 == employee02);
        }*/
        try(SqlSession sqlSession = sqlSessionFactory.openSession()){
            EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);
            employee01 = mapper.getEmployeeById(1);
            System.out.println(employee01);
            sqlSession.clearCache();
            Employee employee02 = mapper.getEmployeeById(1);
            System.out.println(employee02);
            System.out.println(employee01 == employee02);
        }

    }
    @Test
    public void testSecondLevelCache() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        Employee employee01;
        Employee employee03;
        try(SqlSession sqlSession = sqlSessionFactory.openSession()){
            EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);
            employee01 = mapper.getEmployeeById(1);
            System.out.println(employee01);
        }
        // 这一次的查询是从二级缓存中拿出来的数据
        try(SqlSession sqlSession = sqlSessionFactory.openSession()){
            EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);
            employee03 = mapper.getEmployeeById(1);
            System.out.println(employee03);
        }
    }



}
