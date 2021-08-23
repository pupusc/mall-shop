package com.wanmi.ares.report.customer.dao;

import com.wanmi.ares.report.customer.model.request.CustomerAreaDistributeRequest;
import com.wanmi.ares.report.customer.model.root.AreaDistrReport;
import com.wanmi.ares.source.model.root.region.City;
import com.wanmi.ares.source.model.root.region.Province;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * <p>客户地区分布报表持久化Mapper</p>
 * Created by of628-wenzhi on 2017-10-14-下午5:34.
 */
@Repository
public interface AreaDistributeReportMapper {

    /**
     *  保存昨天报表
     */
    int insertReport(List<AreaDistrReport> list);

    int generateThirdShopCustomerAreaDistribute(CustomerAreaDistributeRequest areaRequest);

    int generateBossCustomerAreaDistribute(CustomerAreaDistributeRequest areaRequest);

    /**
     * 删除指定日期的数据
     */
    int deleteReport(String dateStr);

    /**
     * 报表查询
     */
    List<AreaDistrReport> query(@Param("companyId") String companyId, @Param("date") LocalDate date);

    /**
     * @Author lvzhenwei
     * @Description 查询地区城市信息
     * @Date 10:21 2019/9/30
     * @Param [code]
     * @return com.wanmi.ares.source.model.root.region.City
     **/
    City queryCityInfo(@Param("code") String code);

    /**
     * @Author lvzhenwei
     * @Description 查询地区省份信息
     * @Date 10:21 2019/9/30
     * @Param [code]
     * @return com.wanmi.ares.source.model.root.region.Province
     **/
    Province queryProvinceInfo(@Param("code") String code);

    /**
     * 清理过期报表数据
     * @param pname -Range分区名
     */
    int clearExpire(String pname);
}
