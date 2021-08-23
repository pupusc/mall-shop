package com.wanmi.ares.report.customer.service;

import com.wanmi.ares.report.customer.dao.AreaDistributeReportMapper;
import com.wanmi.ares.report.customer.dao.CustomerMapper;
import com.wanmi.ares.report.customer.dao.LevelDistributeReportMapper;
import com.wanmi.ares.report.customer.model.root.AreaDistrReport;
import com.wanmi.ares.report.customer.model.root.LevelDistrReport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>用户分布报表生成Service</p>
 * Created by of628-wenzhi on 2017-10-11-下午9:44.
 */
@Service
@Slf4j
public class DistributeReportGenerateService {

    @Resource
    private LevelDistributeReportMapper levelDistrMapper;

    @Resource
    private AreaDistributeReportMapper areaDistrMapper;

    @Resource
    private CustomerMapper customerMapper;

    /**
     * 客户等级分布报表生成
     */
    public void generateByLevel() {
        try {
            List<LevelDistrReport> reports = customerMapper.queryTotalByLevel(LocalDate.now());
            if (reports.isEmpty()) {
                return;
            }
            Map<String, List<LevelDistrReport>> groupList = reports.stream().collect(Collectors.groupingBy(
                    LevelDistrReport::getCompanyId));
            groupList.values().forEach(
                    i -> {
                        long total = i.stream().mapToLong(LevelDistrReport::getNum).sum();
                        NumberFormat nt = NumberFormat.getPercentInstance();
                        //设置百分数精确度2即保留两位小数
                        nt.setMinimumFractionDigits(2);
                        i.forEach(
                                r -> {
                                    r.setTotal(total);
                                    r.setCentage(nt.format((double) r.getNum() / total));
                                }
                        );
                    }
            );
            //报表入库
            levelDistrMapper.deleteReport(LocalDate.now().toString());
            levelDistrMapper.insertReport(reports);
        } catch (Exception e) {
            log.error("The [customer-level distribute] generate report exception,date:{},\n",
                    LocalDate.now(), e);
        }
    }

    /**
     * 客户地区分布报表生成
     */
    public void generateByArea() {

        try {
            List<AreaDistrReport> reports = customerMapper.queryTotalByArea(LocalDate.now());
            if (reports.isEmpty()) {
                return;
            }
            //报表入库
            areaDistrMapper.deleteReport(LocalDate.now().toString());
            areaDistrMapper.insertReport(reports);
        } catch (Exception e) {
            log.error("The [customer-region distribute] generate report exception,date:{},\n",
                    LocalDate.now(), e);
        }
    }

    /**
     * 客户分布报表DB过期数据清理
     *
     * @param date
     */
    public void clearDistributeReport(LocalDate date) {
        String pname = "p".concat(String.format("%d", date.getYear())).concat(String.format("%d", date.getMonth().getValue()));
        try {
            areaDistrMapper.clearExpire(pname);
        } catch (Exception e) {
            log.error("The [customer-region distribute] Clean out report expiration data exception,date:{},\n",
                    date, e);
        }
        try {
            levelDistrMapper.clearExpire(pname);
        } catch (Exception e) {
            log.error("The [customer-level distribute] Clean out report expiration data exception,date:{},\n",
                    date, e);
        }
    }

}
