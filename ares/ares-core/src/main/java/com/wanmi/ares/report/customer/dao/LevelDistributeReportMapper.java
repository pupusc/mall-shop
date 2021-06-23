package com.wanmi.ares.report.customer.dao;

import com.wanmi.ares.report.customer.model.request.CustomerLevelDistributeRequest;
import com.wanmi.ares.report.customer.model.root.LevelDistrReport;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * <p>客户级别分布报表持久化Mapper</p>
 * Created by of628-wenzhi on 2017-10-14-下午5:34.
 */
@Repository
public interface LevelDistributeReportMapper {

    /**
     * 保存昨天报表
     */
    int insertReport(@Param("list") List<LevelDistrReport> list);

    /**
     * @Author lvzhenwei
     * @Description 统计生成第三方店铺客户等级分布数据
     * @Date 14:54 2019/9/12
     * @Param []
     * @return int
     **/
    int generateThirdShopCustomerLevelDistribute(CustomerLevelDistributeRequest request);

    /**
     * @Author lvzhenwei
     * @Description 统计生成运营后台客户等级分布数据
     * @Date 10:30 2019/9/16
     * @Param [levelDistrReport]
     * @return int
     **/
    int generateBossCustomerLevelDistribute(CustomerLevelDistributeRequest request);

    /**
     * 删除指定日期的数据
     */
    int deleteReport(String dateStr);

    /**
     * 报表查询
     *
     * @param companyId
     * @param date
     */
    List<LevelDistrReport> query(@Param("companyId") String companyId, @Param("date") LocalDate date);

    /**
     * 清理过期报表数据
     * @param pname -Range分区名
     */
    int clearExpire(String pname);
}
