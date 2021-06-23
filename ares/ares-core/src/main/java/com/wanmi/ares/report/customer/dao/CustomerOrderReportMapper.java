package com.wanmi.ares.report.customer.dao;

import com.wanmi.ares.base.PageRequest;
import com.wanmi.ares.report.base.model.ExportQuery;
import com.wanmi.ares.report.customer.model.request.CustomerOrderDataRequest;
import com.wanmi.ares.report.customer.model.root.CustomerAreaReport;
import com.wanmi.ares.report.customer.model.root.CustomerLevelReport;
import com.wanmi.ares.report.customer.model.root.CustomerReport;
import com.wanmi.ares.request.customer.CustomerOrderQueryRequest;
import com.wanmi.ares.source.model.root.Customer;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerOrderReportMapper {
    /**
     * 日报表
     *
     * @param customerReports
     * @return rows
     */
    int saveCustomerReport(@Param("customerReports") List<CustomerReport> customerReports, @Param("tableName") String tableName);

    /**
     * 当天报表生成前清除
     *
     * @param tableName
     * @param deleteDate
     * @return
     */
    int deleteCustomerReportByDate(@Param("tableName") String tableName, @Param("deleteDate") String deleteDate);

    /**
     * @Author lvzhenwei
     * @Description 清空某张表
     * @Date 18:10 2019/10/8
     * @Param [tableName]
     * @return int
     **/
    int deleteCustomerReport(@Param("tableName") String tableName);

    /**
     * 生成会员等级报表
     *
     * @param customerLevelReports customerLevelReports
     * @return rows
     */
    int saveCustomerLevelReport(@Param("customerLevelReports") List<CustomerLevelReport> customerLevelReports, @Param("tableName") String tableName);

    /**
     * 生成会员地区报表
     *
     * @param customerAreaReports customerAreaReports
     * @param tableName           tableName
     * @return rows
     */
    int saveCustomerAreaReport(@Param("customerAreaReports") List<CustomerAreaReport> customerAreaReports, @Param("tableName") String tableName);

    /**
     * @Author lvzhenwei
     * @Description 运营后台客户统计--按天数据统计--客户订货报表按客户查看数据--预统计
     * @Date 15:03 2019/9/18
     * @Param []
     * @return int
     **/
    int generateCustomerOrderForBossForDay(CustomerOrderDataRequest customerOrderDataRequest);

    /**
     * @Author lvzhenwei
     * @Description 运营后台客户统计--最近7天数据统计--客户订货报表按客户查看数据--预统计
     * @Date 15:04 2019/9/18
     * @Param []
     * @return int
     **/
    int generateCustomerOrderForBossForSeven(CustomerOrderDataRequest customerOrderDataRequest);

    /**
     * @Author lvzhenwei
     * @Description 运营后台客户统计--最近30天数据统计--客户订货报表按客户查看数据--预统计
     * @Date 15:05 2019/9/18
     * @Param []
     * @return int
     **/
    int generateCustomerOrderForBossForThirty(CustomerOrderDataRequest customerOrderDataRequest);

    /**
     * @Author lvzhenwei
     * @Description 运营后台客户统计--按月统计数据统计--客户订货报表按客户查看数据--预统计
     * @Date 15:06 2019/9/18
     * @Param []
     * @return int
     **/
    int generateCustomerOrderForBossForMonth(CustomerOrderDataRequest customerOrderDataRequest);

    /**
     * @Author lvzhenwei
     * @Description 第三方商家客户统计--按天数据统计--客户订货报表按客户查看数据--预统计
     * @Date 15:03 2019/9/18
     * @Param []
     * @return int
     **/
    int generateCustomerOrderForSupplierForDay(CustomerOrderDataRequest customerOrderDataRequest);

    /**
     * @Author lvzhenwei
     * @Description 第三方商家客户统计--最近7天数据统计--客户订货报表按客户查看数据--预统计
     * @Date 15:04 2019/9/18
     * @Param []
     * @return int
     **/
    int generateCustomerOrderForSupplierForSeven(CustomerOrderDataRequest customerOrderDataRequest);

    /**
     * @Author lvzhenwei
     * @Description 第三方商家客户统计--最近30天数据统计--客户订货报表按客户查看数据--预统计
     * @Date 15:05 2019/9/18
     * @Param []
     * @return int
     **/
    int generateCustomerOrderForSupplierForThirty(CustomerOrderDataRequest customerOrderDataRequest);

    /**
     * @Author lvzhenwei
     * @Description 第三方商家客户统计--按月统计数据统计--客户订货报表按客户查看数据--预统计
     * @Date 15:06 2019/9/18
     * @Param []
     * @return int
     **/
    int generateCustomerOrderForSupplierForMonth(CustomerOrderDataRequest customerOrderDataRequest);

    /**
     * @Author lvzhenwei
     * @Description 第三方商家客户统计--按天统计数据统计--客户订货报表按等级查看数据--预统计
     * @Date 17:28 2019/9/18
     * @Param [customerOrderDataRequest]
     * @return int
     **/
    int generateCustomerLevelOrderForSupplierForDay(CustomerOrderDataRequest customerOrderDataRequest);

    /**
     * @Author lvzhenwei
     * @Description 第三方商家客户统计--最近7天统计数据统计--客户订货报表按等级查看数据--预统计
     * @Date 17:28 2019/9/18
     * @Param [customerOrderDataRequest]
     * @return int
     **/
    int generateCustomerLevelOrderForSupplierForSeven(CustomerOrderDataRequest customerOrderDataRequest);

    /**
     * @Author lvzhenwei
     * @Description 第三方商家客户统计--最近30天统计数据统计--客户订货报表按等级查看数据--预统计
     * @Date 17:28 2019/9/18
     * @Param [customerOrderDataRequest]
     * @return int
     **/
    int generateCustomerLevelOrderForSupplierForThirty(CustomerOrderDataRequest customerOrderDataRequest);

    /**
     * @Author lvzhenwei
     * @Description 第三方商家客户统计--按月统计数据统计--客户订货报表按等级查看数据--预统计
     * @Date 17:28 2019/9/18
     * @Param [customerOrderDataRequest]
     * @return int
     **/
    int generateCustomerLevelOrderForSupplierForMonth(CustomerOrderDataRequest customerOrderDataRequest);

    /**
     * @Author lvzhenwei
     * @Description 运营后台商客户统计--按天统计数据统计--客户订货报表按等级查看数据--预统计
     * @Date 17:28 2019/9/18
     * @Param [customerOrderDataRequest]
     * @return int
     **/
    int generateCustomerLevelOrderForBossForDay(CustomerOrderDataRequest customerOrderDataRequest);

    /**
     * @Author lvzhenwei
     * @Description 运营后台客户统计--最近7天统计数据统计--客户订货报表按等级查看数据--预统计
     * @Date 17:28 2019/9/18
     * @Param [customerOrderDataRequest]
     * @return int
     **/
    int generateCustomerLevelOrderForBossForSeven(CustomerOrderDataRequest customerOrderDataRequest);

    /**
     * @Author lvzhenwei
     * @Description 运营后台客户统计--最近30天统计数据统计--客户订货报表按等级查看数据--预统计
     * @Date 17:28 2019/9/18
     * @Param [customerOrderDataRequest]
     * @return int
     **/
    int generateCustomerLevelOrderForBossForThirty(CustomerOrderDataRequest customerOrderDataRequest);

    /**
     * @Author lvzhenwei
     * @Description 运营后台客户统计--按月统计数据统计--客户订货报表按等级查看数据--预统计
     * @Date 17:28 2019/9/18
     * @Param [customerOrderDataRequest]
     * @return int
     **/
    int generateCustomerLevelOrderForBossForMonth(CustomerOrderDataRequest customerOrderDataRequest);

    /**
     * @Author lvzhenwei
     * @Description 运营后台客户统计--按天统计数据统计--客户订货报表按区域查看数据--预统计
     * @Date 17:28 2019/9/18
     * @Param [customerOrderDataRequest]
     * @return int
     **/
    int generateCustomerAreaOrderForBossForDay(CustomerOrderDataRequest customerOrderDataRequest);

    /**
     * @Author lvzhenwei
     * @Description 运营后台客户统计--最近7天统计数据统计--客户订货报表按区域查看数据--预统计
     * @Date 17:28 2019/9/18
     * @Param [customerOrderDataRequest]
     * @return int
     **/
    int generateCustomerAreaOrderForBossForSeven(CustomerOrderDataRequest customerOrderDataRequest);

    /**
     * @Author lvzhenwei
     * @Description 运营后台客户统计--最近30天统计数据统计--客户订货报表按区域查看数据--预统计
     * @Date 17:28 2019/9/18
     * @Param [customerOrderDataRequest]
     * @return int
     **/
    int generateCustomerAreaOrderForBossForThirty(CustomerOrderDataRequest customerOrderDataRequest);

    /**
     * @Author lvzhenwei
     * @Description 运营后台客户统计--按月统计数据统计--客户订货报表按区域查看数据--预统计
     * @Date 17:28 2019/9/18
     * @Param [customerOrderDataRequest]
     * @return int
     **/
    int generateCustomerAreaOrderForBossForMonth(CustomerOrderDataRequest customerOrderDataRequest);

    /**
     * @Author lvzhenwei
     * @Description 第三方商家客户统计--按天统计数据统计--客户订货报表按区域查看数据--预统计
     * @Date 17:28 2019/9/18
     * @Param [customerOrderDataRequest]
     * @return int
     **/
    int generateCustomerAreaOrderForSupplierForDay(CustomerOrderDataRequest customerOrderDataRequest);

    /**
     * @Author lvzhenwei
     * @Description 第三方商家客户统计--最近7天统计数据统计--客户订货报表按区域查看数据--预统计
     * @Date 17:28 2019/9/18
     * @Param [customerOrderDataRequest]
     * @return int
     **/
    int generateCustomerAreaOrderForSupplierForSeven(CustomerOrderDataRequest customerOrderDataRequest);

    /**
     * @Author lvzhenwei
     * @Description 第三方商家客户统计--最近30天统计数据统计--客户订货报表按区域查看数据--预统计
     * @Date 17:28 2019/9/18
     * @Param [customerOrderDataRequest]
     * @return int
     **/
    int generateCustomerAreaOrderForSupplierForThirty(CustomerOrderDataRequest customerOrderDataRequest);

    /**
     * @Author lvzhenwei
     * @Description 第三方商家客户统计--按月统计数据统计--客户订货报表按区域查看数据--预统计
     * @Date 17:28 2019/9/18
     * @Param [customerOrderDataRequest]
     * @return int
     **/
    int generateCustomerAreaOrderForSupplierForMonth(CustomerOrderDataRequest customerOrderDataRequest);

    /**
     * 查询会员报表
     *
     * @param customerOrderQueryRequest customerOrderQueryRequest
     * @param pageRequest               pageRequest
     * @param tableName                 tableName
     * @return 会员报表
     */
    List<CustomerReport> queryCustomerReport(@Param("customerOrderQueryRequest") CustomerOrderQueryRequest customerOrderQueryRequest
            , @Param("pageRequest") PageRequest pageRequest, @Param("tableName") String tableName, @Param("daylyDate") String daylyDate);


    /**
     * 查询数据总条数
     *
     * @param customerOrderQueryRequest customerOrderQueryRequest
     * @param tableName                 tableName
     * @return total
     */
    Integer countCustomerReport(@Param("customerOrderQueryRequest") CustomerOrderQueryRequest customerOrderQueryRequest
            , @Param("tableName") String tableName, @Param("daylyDate") String daylyDate);

    /**
     * 查询数据总条数
     *
     * @param customerOrderQueryRequest customerOrderQueryRequest
     * @param tableName                 tableName
     * @return total
     */
    @Select("select count(1) from ${tableName} customer_level\n" +
            "LEFT JOIN replay_customer_level level on customer_level.CUSTOMER_LEVEL_ID = level.customer_level_id\n" +
            "where (#{customerOrderQueryRequest.companyId} is null or customer_level.SHOP_ID = #{customerOrderQueryRequest.companyId}) " +
            "and\n" +
            "(\n" +
            "  #{customerOrderQueryRequest.queryText} is null or level.customer_level_name = #{customerOrderQueryRequest.queryText}\n" +
            ")\n" +
            "and\n" +
            "(\n" +
            "  #{daylyDate} is null or customer_level.STAT_DATE = #{daylyDate}\n" +
            ");")
    Integer countCustomerLevelReport(@Param("customerOrderQueryRequest") CustomerOrderQueryRequest customerOrderQueryRequest,
                                     @Param("tableName") String tableName, @Param("daylyDate") String daylyDate);

    /**
     * 查询数据总条数
     *
     * @param customerOrderQueryRequest customerOrderQueryRequest
     * @param tableName                 tableName
     * @return total
     */
    @Select("select count(1) from ${tableName} customer_level\n" +
            "LEFT JOIN replay_store_level level on customer_level.CUSTOMER_LEVEL_ID = level.store_level_id\n" +
            "where (#{customerOrderQueryRequest.companyId} is null or customer_level.SHOP_ID = #{customerOrderQueryRequest.companyId}) " +
            "and\n" +
            "(\n" +
            "  #{customerOrderQueryRequest.queryText} is null or level.level_name = #{customerOrderQueryRequest.queryText}\n" +
            ")\n" +
            "and\n" +
            "(\n" +
            "  #{daylyDate} is null or customer_level.STAT_DATE = #{daylyDate}\n" +
            ");")
    Integer countCustomerStoreLevelReport(@Param("customerOrderQueryRequest") CustomerOrderQueryRequest customerOrderQueryRequest,
                                     @Param("tableName") String tableName, @Param("daylyDate") String daylyDate);

    /**
     * 查询数据总条数
     *
     * @param customerOrderQueryRequest customerOrderQueryRequest
     * @param tableName                 tableName
     * @return total
     */
    Integer countCustomerAreaReport(@Param("customerOrderQueryRequest") CustomerOrderQueryRequest customerOrderQueryRequest
            , @Param("tableName") String tableName, @Param("daylyDate") String daylyDate);

    /**
     * 查询客户级别报表
     *
     * @param customerOrderQueryRequest customerOrderQueryRequest
     * @param pageRequest               pageRequest
     * @param tableName                 tableName
     * @return List<CustomerLevelReport>
     */
    List<CustomerLevelReport> queryCustomerLevelReport(@Param("customerOrderQueryRequest") CustomerOrderQueryRequest customerOrderQueryRequest
            , @Param("pageRequest") PageRequest pageRequest, @Param("tableName") String tableName,
                                                       @Param("daylyDate") String daylyDate);

    /**
     * 查询会员地区报表
     *
     * @param customerOrderQueryRequest customerOrderQueryRequest
     * @param pageRequest               pageRequest
     * @param tableName                 tableName
     * @return List<CustomerAreaReport>
     */
    List<CustomerAreaReport> queryCustomerAreaReport(@Param("customerOrderQueryRequest") CustomerOrderQueryRequest customerOrderQueryRequest
            , @Param("pageRequest") PageRequest pageRequest, @Param("tableName") String tableName, @Param("daylyDate") String daylyDate);

    /**
     * 清理最近几天的报表
     * @param tableName
     * @return
     */
    int truncateTable(@Param("tableName") String tableName);

    /**
     * 导出会员订货报表
     * @param exportQuery exportQuery
     * @return 列表
     */
    List<CustomerReport> exportCustomerReport(@Param("exportQuery") ExportQuery exportQuery);

    /**
     * 统计导出数量
     * @param exportQuery exportQuery
     * @return rows
     */
    int countExportCustomerReport(@Param("exportQuery") ExportQuery exportQuery);

    /**
     * 导出
     * @param exportQuery
     * @return
     */
    List<CustomerAreaReport> exportCustomerAreaReport(@Param("exportQuery") ExportQuery exportQuery);

    /**
     * da
     * @param exportQuery
     * @return
     */
    int countExportCustomerAreaReport(@Param("exportQuery") ExportQuery exportQuery);

    /**
     * 导出客户订货级别报表
     * @param exportQuery exportQuery
     * @return List<CustomerLevelReport>
     */
    List<CustomerLevelReport> exportCustomerLevelReport(@Param("exportQuery") ExportQuery exportQuery);

    /**
     * 数量
     * @param exportQuery
     * @return 总数
     */
    int countExportCustomerLevelReport(@Param("exportQuery") ExportQuery exportQuery);

    /**
     * 修改日报表中用户的名称或者账号变为最新
     * @param customer customer
     * @return rows
     */
    int updateCustomerDayReport(@Param("customer") Customer customer);

    /**
     * 会员报表数据清理
     * @param pname pname
     * @return rows
     */
    int clearCustomerReport(@Param("pname") String pname);

    /**
     * @Author lvzhenwei
     * @Description 运营后台客户统计--导出按客户查看数据客户订货报表数据总条数
     * @Date 11:13 2019/9/21
     * @Param [customerOrderDataRequest]
     * @return java.util.List<com.wanmi.ares.report.customer.model.root.CustomerReport>
     **/
    int exportCustomerOrderTotalForBoss(CustomerOrderDataRequest customerOrderDataRequest);

    /**
     * @Author lvzhenwei
     * @Description 运营后台客户统计--导出按客户查看数据客户订货报表数据
     * @Date 11:13 2019/9/21
     * @Param [customerOrderDataRequest]
     * @return java.util.List<com.wanmi.ares.report.customer.model.root.CustomerReport>
     **/
    List<CustomerReport> exportCustomerOrderForBoss(CustomerOrderDataRequest customerOrderDataRequest);

    /**
     * @Author lvzhenwei
     * @Description 第三方店铺客户统计--导出按客户查看数据客户订货报表数据总条数
     * @Date 11:13 2019/9/21
     * @Param [customerOrderDataRequest]
     * @return java.util.List<com.wanmi.ares.report.customer.model.root.CustomerReport>
     **/
    int exportCustomerOrderTotalForSupplier(CustomerOrderDataRequest customerOrderDataRequest);

    /**
     * @Author lvzhenwei
     * @Description 第三方店铺客户统计--导出按客户查看数据客户订货报表数据
     * @Date 11:13 2019/9/21
     * @Param [customerOrderDataRequest]
     * @return java.util.List<com.wanmi.ares.report.customer.model.root.CustomerReport>
     **/
    List<CustomerReport> exportCustomerOrderForSupplier(CustomerOrderDataRequest customerOrderDataRequest);

    /**
     * @Author lvzhenwei
     * @Description 运营后台客户统计--导出按等级查看数据客户订货报表数据总条数
     * @Date 11:13 2019/9/21
     * @Param [customerOrderDataRequest]
     * @return java.util.List<com.wanmi.ares.report.customer.model.root.CustomerReport>
     **/
    int exportCustomerLevelOrderTotalForBoss(CustomerOrderDataRequest customerOrderDataRequest);

    /**
     * @Author lvzhenwei
     * @Description 运营后台客户统计--导出按等级查看数据客户订货报表数据
     * @Date 11:13 2019/9/21
     * @Param [customerOrderDataRequest]
     * @return java.util.List<com.wanmi.ares.report.customer.model.root.CustomerReport>
     **/
    List<CustomerLevelReport> exportCustomerLevelOrderForBoss(CustomerOrderDataRequest customerOrderDataRequest);

    /**
     * @Author lvzhenwei
     * @Description 第三方店铺客户统计--导出按等级查看数据客户订货报表数据总条数
     * @Date 11:13 2019/9/21
     * @Param [customerOrderDataRequest]
     * @return java.util.List<com.wanmi.ares.report.customer.model.root.CustomerReport>
     **/
    int exportCustomerLevelOrderTotalForSupplier(CustomerOrderDataRequest customerOrderDataRequest);

    /**
     * @Author lvzhenwei
     * @Description 第三方店铺客户统计--导出按等级查看数据客户订货报表数据
     * @Date 11:13 2019/9/21
     * @Param [customerOrderDataRequest]
     * @return java.util.List<com.wanmi.ares.report.customer.model.root.CustomerReport>
     **/
    List<CustomerLevelReport> exportCustomerLevelOrderForSupplier(CustomerOrderDataRequest customerOrderDataRequest);

    /**
     * @Author lvzhenwei
     * @Description 运营后台客户统计--导出按区域查看数据客户订货报表数据总条数
     * @Date 11:13 2019/9/21
     * @Param [customerOrderDataRequest]
     * @return java.util.List<com.wanmi.ares.report.customer.model.root.CustomerReport>
     **/
    int exportCustomerAreaOrderTotalForBoss(CustomerOrderDataRequest customerOrderDataRequest);

    /**
     * @Author lvzhenwei
     * @Description 运营后台客户统计--导出按区域查看数据客户订货报表数据
     * @Date 11:13 2019/9/21
     * @Param [customerOrderDataRequest]
     * @return java.util.List<com.wanmi.ares.report.customer.model.root.CustomerReport>
     **/
    List<CustomerAreaReport> exportCustomerAreaOrderForBoss(CustomerOrderDataRequest customerOrderDataRequest);

    /**
     * @Author lvzhenwei
     * @Description 第三方店铺客户统计--导出按区域查看数据客户订货报表数据总条数
     * @Date 11:13 2019/9/21
     * @Param [customerOrderDataRequest]
     * @return java.util.List<com.wanmi.ares.report.customer.model.root.CustomerReport>
     **/
    int exportCustomerAreaOrderTotalForSupplier(CustomerOrderDataRequest customerOrderDataRequest);

    /**
     * @Author lvzhenwei
     * @Description 第三方店铺客户统计--导出按区域查看数据客户订货报表数据
     * @Date 11:13 2019/9/21
     * @Param [customerOrderDataRequest]
     * @return java.util.List<com.wanmi.ares.report.customer.model.root.CustomerReport>
     **/
    List<CustomerAreaReport> exportCustomerAreaOrderForSupplier(CustomerOrderDataRequest customerOrderDataRequest);

}

