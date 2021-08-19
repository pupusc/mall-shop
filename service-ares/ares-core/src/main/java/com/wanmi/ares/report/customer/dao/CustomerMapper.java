package com.wanmi.ares.report.customer.dao;

import com.wanmi.ares.report.customer.model.root.AreaDistrReport;
import com.wanmi.ares.report.customer.model.root.CustomerGrowthReport;
import com.wanmi.ares.report.customer.model.root.LevelDistrReport;
import com.wanmi.ares.report.employee.model.root.EmployeeClientReport;
import com.wanmi.ares.request.CustomerQueryRequest;
import com.wanmi.ares.source.model.root.Customer;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CustomerMapper {

    int insert(@Param("customer") Customer customer);

    /**
     * 参数查询，employeeId，keyWord
     *
     * @param request
     * @return
     */
    List<Customer> queryByParams(@Param("request") CustomerQueryRequest request);

    Customer queryById(@Param("customerId") String customerId);

    int deleteByIds(@Param("customerIds") List<String> customerIds);

    int updateById(@Param("customer") Customer customer);

    /**
     * 解绑客户(将本来属于某个业务员的客户的业务员设置为null)
     *
     * @param employeeId
     * @return
     */
    int unbindCustomer(@Param("employeeId") String employeeId);

    /**
     * 查询商家新增用户数
     *
     * @return List<CustomerGrowthReport>
     */
    @Select("select count(DISTINCT(a.customer_id)) as increase, b.company_info_id as company_info_id from " +
            " replay_customer a inner join replay_store_customer_rela b on a.customer_id = b.customer_id " +
            " where " +
            " b.create_time >= #{beginTime} and b.create_time <= #{endTime} and " +
            " a.del_flag= 0 GROUP BY b.company_info_id")
    @Results({
            @Result(column = "company_info_id", property = "companyId"),
            @Result(column = "increase", property = "customerDayGrowthCount")
    })
    List<CustomerGrowthReport> queryGrowthCustomerCount(@Param("beginTime") String beginTime,@Param("endTime") String endTime);
    /*@Select("select count(DISTINCT(a.id)) as increase, b.company_info_id as company_info_id from \n" +
            "customer a inner join customer_and_level b on a.id = b.customer_id" +
            " where b.bind_time = #{now} " +
            "and a.del_flag= '0' GROUP BY b.company_info_id")
    @Results({
            @Result(column = "company_info_id", property = "companyId"),
            @Result(column = "increase", property = "customerDayGrowthCount")
    })
    List<CustomerGrowthReport> queryGrowthCustomerCount(@Param("now") String now);
*/
    /**
     * 查询平台新增用户数
     *
     * @return List<CustomerGrowthReport>
     */
    @Select("select count(a.customer_id) as increase, 0 as company_info_id from \n" +
            " replay_customer a " +
            " where a.check_state =1 and a.check_time >= #{beginTime} and a.check_time <= #{endTime} " +
            " and a.del_flag= 0")
    @Results({
            @Result(column = "company_info_id", property = "companyId"),
            @Result(column = "increase", property = "customerDayGrowthCount")
    })
    List<CustomerGrowthReport> queryGrowthCustomerCountByBoss(@Param("beginTime") String beginTime,@Param("endTime") String endTime);

    /**
     * 查询商家注册用户数
     *
     * @return List<CustomerGrowthReport>
     */
    @Select("select count(DISTINCT(a.customer_id)) as register, b.company_info_id as company_info_id from \n" +
            " replay_customer a inner join replay_store_customer_rela b on a.customer_id = b.customer_id " +
            " where a.create_person is null and a.create_time >= #{beginTime} and a.create_time <= #{endTime} " +
            " and a.del_flag= 0 GROUP BY b.company_info_id")
    @Results({
            @Result(column = "company_info_id", property = "companyId"),
            @Result(column = "register", property = "customerDayRegisterCount")
    })
    List<CustomerGrowthReport> queryRegisterCustomerCount(@Param("beginTime") String beginTime,@Param("endTime") String endTime);

    /**
     * 查询平台注册用户数
     *
     * @return List<CustomerGrowthReport>
     */
    @Select("select count(a.customer_id) as register, 0 as company_info_id from \n" +
            " replay_customer a " +
            " where  a.create_person is null and a.create_time >= #{beginTime} and a.create_time <= #{endTime}  " +
            " and a.del_flag= 0 ")
    @Results({
            @Result(column = "company_info_id", property = "companyId"),
            @Result(column = "register", property = "customerDayRegisterCount")
    })
    List<CustomerGrowthReport> queryRegisterCustomerCountByBoss(@Param("beginTime") String beginTime,@Param("endTime") String endTime);


    /**
     * 查询商家总用户数
     *
     * @return List<CustomerGrowthReport>
     */
    @Select("select count(DISTINCT(a.customer_id)) as allCount, b.company_info_id as company_info_id from \n" +
            " replay_customer a inner join replay_store_customer_rela b on a.customer_id = b.customer_id " +
            " where a.check_state =1 and  a.check_time <= #{endTime} " +
            " and a.del_flag= 0 GROUP BY b.company_info_id")
    @Results({
            @Result(column = "company_info_id", property = "companyId"),
            @Result(column = "allCount", property = "customerAllCount")
    })
    List<CustomerGrowthReport> queryAllCustomerCount(@Param("endTime") String endTime);

    /**
     * 查询平台总用户数
     *
     * @return List<CustomerGrowthReport>
     */
    @Select("select count(a.customer_id) as allCount, 0 as company_info_id from \n" +
            " replay_customer a " +
            " where a.check_state =1  and a.check_time <= #{endTime} " +
            " and a.del_flag= 0 ")
    @Results({
            @Result(column = "company_info_id", property = "companyId"),
            @Result(column = "allCount", property = "customerAllCount")
    })
    List<CustomerGrowthReport> queryAllCustomerCountByBoss(@Param("endTime") String endTime);

    List<EmployeeClientReport> queryTotalByEmployee(@Param("date") LocalDate date);

    List<EmployeeClientReport> queryNewlyByEmployee(
            @Param("dateFrom") LocalDate dateFrom,
            @Param("dateTo") LocalDate dateTo);

    List<LevelDistrReport> queryTotalByLevel(LocalDate date);

    List<AreaDistrReport> queryTotalByArea(LocalDate date);

    int queryTotalByCompany(@Param("date") LocalDate date, @Param("companyId") String companyId);

    int queryTotal(@Param("date") LocalDate date);


    List<String> queryPhone(CustomerQueryRequest request);

    long queryPhoneCount(CustomerQueryRequest request);
}

