package com.wanmi.ares.scheduled.flow;

import com.wanmi.ares.enums.StatisticsDataType;
import com.wanmi.ares.enums.StatisticsWeekType;
import com.wanmi.ares.report.flow.dao.*;
import com.wanmi.ares.report.flow.model.reponse.FlowDataInfoResponse;
import com.wanmi.ares.report.flow.model.request.FlowDataRequest;
import com.wanmi.ares.report.flow.model.root.FlowMonth;
import com.wanmi.ares.report.flow.model.root.FlowSeven;
import com.wanmi.ares.report.flow.model.root.FlowThirty;
import com.wanmi.ares.report.flow.model.root.FlowWeek;
import com.wanmi.ares.report.flow.service.FlowDataStrategy;
import com.wanmi.ares.report.flow.service.FlowDataStrategyChooser;
import com.wanmi.ares.utils.DateUtil;
import com.wanmi.ares.utils.WeekDate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName FlowDataStatisticsService
 * @Description 流量统计统计最近7天、最近30天、最近半年按月统计及按周统计数据
 * @Author lvzhenwei
 * @Date 2019/8/22 15:10
 **/
@Service
public class FlowDataStatisticsService {

    @Resource
    private FlowReportMapper flowReportMapper;

    @Resource
    private FlowSevenMapper flowSevenMapper;

    @Resource
    private FlowThirtyMapper flowThirtyMapper;

    @Resource
    private FlowMonthMapper flowMonthMapper;

    @Resource
    private FlowWeekMapper flowWeekMapper;

    @Resource
    private FlowDataStrategyChooser saveFlowDataStrategyChooser;


    /**
     * @return void
     * @Author lvzhenwei
     * @Description 流量统计最近七天、最近30天以及最近半年的自然月数据汇总
     * @Date 15:52 2019/8/22
     * @Param []
     **/
    public void generateFlowData(String type) {
        if(StringUtils.isNotBlank(type)){
            String[] typeArr = type.split(",");
            for(String generateType:typeArr){
                if(generateType.equals(StatisticsDataType.SEVEN.toValue()+"")){
                    queryFlowDataInfo(StatisticsDataType.SEVEN);
                } else if(generateType.equals(StatisticsDataType.THIRTY.toValue()+"")){
                    queryFlowDataInfo(StatisticsDataType.THIRTY);
                } else if(generateType.equals(StatisticsDataType.MONTH.toValue()+"")){
                    queryFlowDataInfoForMonth();
                }
            }
        }
    }

    /**
     * @return void
     * @Author lvzhenwei
     * @Description 每次插入数据之前先将以前的老数据进行删除
     * @Date 14:48 2019/8/23
     * @Param []
     **/
    private void deleteFlowData() {
        flowSevenMapper.deleteByPrimary(new FlowSeven());
        flowThirtyMapper.deleteByPrimary(new FlowThirty());
        FlowWeek flowWeek = new FlowWeek();
        flowWeek.setType(StatisticsWeekType.THIRTY_WEEk.toValue());
        flowWeekMapper.deleteByPrimary(flowWeek);
        //每个月的1号删除七个月前的月数据--自然月数据和自然月按周统计数据
        if(LocalDate.now().getDayOfMonth()==1){
            flowWeek.setType(StatisticsWeekType.MONTH_WEEk.toValue());
            flowWeek.setMonth(DateUtil.formatLocalDate(LocalDate.now().minusMonths(7), DateUtil.FMT_MONTH_2));
            flowWeekMapper.deleteByPrimary(flowWeek);
            FlowMonth flowMonth = new FlowMonth();
            flowMonth.setMonth(DateUtil.formatLocalDate(LocalDate.now().minusMonths(7), DateUtil.FMT_MONTH_2));
            flowMonthMapper.deleteByPrimary(flowMonth);
        }
    }

    /**
     * @return void
     * @Author lvzhenwei
     * @Description 流量统计最近七天、最近30天数据汇总
     * @Date 14:10 2019/8/22
     * @Param [flowDataType]
     **/
    @Transactional(rollbackFor = Exception.class)
    public void queryFlowDataInfo(StatisticsDataType flowDataType) {
        FlowDataRequest flowDataRequest = new FlowDataRequest();
        if (StatisticsDataType.SEVEN == flowDataType) {
            //删除最近7天历史数据
            flowSevenMapper.deleteByPrimary(new FlowSeven());
            flowDataRequest.setFlowDataType(StatisticsDataType.SEVEN);
            flowDataRequest.setBeginDate(LocalDate.now().minusDays(7));
        } else if (StatisticsDataType.THIRTY == flowDataType) {
            //删除最近30天历史数据
            flowThirtyMapper.deleteByPrimary(new FlowThirty());
            FlowWeek flowWeek = new FlowWeek();
            flowWeek.setType(StatisticsWeekType.THIRTY_WEEk.toValue());
            //删除最近30天周历史数据
            flowWeekMapper.deleteByPrimary(flowWeek);
            flowDataRequest.setFlowDataType(StatisticsDataType.THIRTY);
            flowDataRequest.setBeginDate(LocalDate.now().minusDays(30));
        }
        flowDataRequest.setEndDate(LocalDate.now().minusDays(1));
        queryFlowDataForBoss(flowDataRequest);
        queryFlowDataForSupplier(flowDataRequest);
        if (StatisticsDataType.THIRTY == flowDataType) {
            flowDataRequest.setFlowDataType(StatisticsDataType.THIRTY_WEEk);
            queryFlowDataInfoForWeek(flowDataRequest);
        }
    }

    /**
     * @return void
     * @Author lvzhenwei
     * @Description 流量统计最近六个月，自然月数据统计,每个月的1号统计上个月的数据
     * @Date 14:43 2019/8/22
     * @Param []
     **/
    @Transactional(rollbackFor = Exception.class)
    public void queryFlowDataInfoForMonth() {
        FlowWeek flowWeek = new FlowWeek();
        flowWeek.setType(StatisticsWeekType.MONTH_WEEk.toValue());
        flowWeek.setMonth(DateUtil.formatLocalDate(LocalDate.now().minusMonths(7), DateUtil.FMT_MONTH_2));
        flowWeekMapper.deleteByPrimary(flowWeek);
        FlowMonth flowMonth = new FlowMonth();
        flowMonth.setMonth(DateUtil.formatLocalDate(LocalDate.now().minusMonths(7), DateUtil.FMT_MONTH_2));
        flowMonthMapper.deleteByPrimary(flowMonth);
        FlowDataRequest flowDataRequest = new FlowDataRequest();
        LocalDate beginDate = DateUtil.getMonthFirstAndLastDayDate(1, 0);
        LocalDate endDate = DateUtil.getMonthFirstAndLastDayDate(1, 1);
        flowDataRequest.setBeginDate(beginDate);
        flowDataRequest.setEndDate(endDate);
        flowDataRequest.setFlowDataType(StatisticsDataType.MONTH);
        flowDataRequest.setMonth(DateUtil.formatLocalDate(DateUtil.getMonthFirstAndLastDayDate(1, 0), DateUtil.FMT_MONTH_2));
        queryFlowDataForBoss(flowDataRequest);
        queryFlowDataForSupplier(flowDataRequest);
        flowDataRequest.setFlowDataType(StatisticsDataType.MONTH_WEEk);
        queryFlowDataInfoForWeek(flowDataRequest);
    }

    /**
     * @return void
     * @Author lvzhenwei
     * @Description 按周统计数据
     * @Date 18:54 2019/8/22
     * @Param [flowDataRequest]
     **/
    private void queryFlowDataInfoForWeek(FlowDataRequest flowDataRequest) {
        List<WeekDate> weekDateList = DateUtil.getWeekLastDay(flowDataRequest.getBeginDate(), flowDataRequest.getEndDate());
        weekDateList.forEach(weekDate -> {
            FlowDataRequest request = new FlowDataRequest();
            request.setBeginDate(weekDate.getBeginDate());
            request.setEndDate(weekDate.getEndDate());
            request.setFlowDataType(flowDataRequest.getFlowDataType());
            request.setMonth(DateUtil.formatLocalDate(flowDataRequest.getBeginDate(), DateUtil.FMT_MONTH_2));
            request.setWeekStartDate(weekDate.getBeginDate());
            request.setWeekEndDate(weekDate.getEndDate());
            queryFlowDataForBoss(request);
            queryFlowDataForSupplier(request);
        });
    }

    /**
     * @return void
     * @Author lvzhenwei
     * @Description 对应统计周期流量统计--boss运营后台
     * @Date 14:47 2019/8/22
     * @Param [flowReportRequest]
     **/
    private void queryFlowDataForBoss(FlowDataRequest flowDataRequest) {
        flowDataRequest.setCompanyId("0");
        queryFlowData(flowDataRequest);
    }

    /**
     * @return void
     * @Author lvzhenwei
     * @Description 对应统计周期流量统计--supplier商家后台
     * @Date 14:47 2019/8/22
     * @Param [flowReportRequest]
     **/
    private void queryFlowDataForSupplier(FlowDataRequest flowDataRequest) {
        flowDataRequest.setCompanyId("-1");
        queryFlowData(flowDataRequest);
    }

    /**
     * @return void
     * @Author lvzhenwei
     * @Description 对应统计周期流量统计
     * @Date 14:44 2019/8/22
     * @Param [flowReportRequest]
     **/
    private void queryFlowData(FlowDataRequest flowDataRequest) {
        flowDataRequest.setPageSize(500);
        int num = flowReportMapper.queryFlowDataNum(flowDataRequest);
        if (num > 0) {
            //进行分页处理
            int pageNum = (int) Math.ceil((double) num / 500);
            for (int i = 0; i < pageNum; i++) {
                flowDataRequest.setPageNum(i*500);
                List<FlowDataInfoResponse> flowDataResponseList = flowReportMapper.queryFlowDataInfo(flowDataRequest);
                FlowDataStrategy flowDataInspectionSolver = saveFlowDataStrategyChooser.choose(flowDataRequest.getFlowDataType());
                flowDataInspectionSolver.saveFlowData(flowDataResponseList, flowDataRequest);
            }
        }
    }

    /**
     * @return void
     * @Author lvzhenwei
     * @Description 保存最近7天统计流量统计数据
     * @Date 14:23 2019/8/23
     * @Param [flowDataResponseList, flowDataRequest]
     **/
    private void saveFlowSeven(List<FlowDataInfoResponse> flowDataResponseList) {
        List<FlowSeven> flowSevenList = new ArrayList<>();
        flowDataResponseList.forEach(info -> {
            FlowSeven flowSeven = new FlowSeven();
            BeanUtils.copyProperties(info, flowSeven);
            flowSeven.setDate(LocalDate.now());
            flowSeven.setCreatTime(LocalDateTime.now());
            flowSevenList.add(flowSeven);
        });
        flowSevenMapper.insertFlowSevenList(flowSevenList);
    }

    /**
     * @return void
     * @Author lvzhenwei
     * @Description 保存最近7天统计流量统计数据
     * @Date 14:23 2019/8/23
     * @Param [flowDataResponseList, flowDataRequest]
     **/
    private void saveFlowThirty(List<FlowDataInfoResponse> flowDataResponseList) {
        List<FlowThirty> flowThirtyList = new ArrayList<>();
        flowDataResponseList.forEach(info -> {
            FlowThirty flowThirty = new FlowThirty();
            BeanUtils.copyProperties(info, flowThirty);
            flowThirty.setDate(LocalDate.now());
            flowThirty.setCreatTime(LocalDateTime.now());
            flowThirtyList.add(flowThirty);
        });
        flowThirtyMapper.insertFlowThirtyList(flowThirtyList);
    }

    /**
     * @return void
     * @Author lvzhenwei
     * @Description 保存按月统计流量统计数据
     * @Date 14:23 2019/8/23
     * @Param [flowDataResponseList, flowDataRequest]
     **/
    private void saveFlowMonth(List<FlowDataInfoResponse> flowDataResponseList, FlowDataRequest flowDataRequest) {
        List<FlowMonth> flowDataList = new ArrayList<>();
        flowDataResponseList.forEach(info -> {
            FlowMonth flowMonth = new FlowMonth();
            BeanUtils.copyProperties(info, flowMonth);
            flowMonth.setDate(LocalDate.now());
            flowMonth.setMonth(flowDataRequest.getMonth());
            flowMonth.setCreatTime(LocalDateTime.now());
            flowDataList.add(flowMonth);
        });
        flowMonthMapper.insertFlowMonthList(flowDataList);
    }

    /**
     * @return void
     * @Author lvzhenwei
     * @Description 保存按月统计流量统计数据
     * @Date 14:23 2019/8/23
     * @Param [flowDataResponseList, flowDataRequest]
     **/
    private void saveFlowWeek(List<FlowDataInfoResponse> flowDataResponseList, FlowDataRequest flowDataRequest) {
        List<FlowWeek> flowWeekList = new ArrayList<>();
        flowDataResponseList.forEach(info -> {
            FlowWeek flowWeek = new FlowWeek();
            BeanUtils.copyProperties(info, flowWeek);
            flowWeek.setDate(LocalDate.now());
            flowWeek.setType(StatisticsWeekType.THIRTY_WEEk.toValue());
            if (StatisticsDataType.MONTH_WEEk == flowDataRequest.getFlowDataType()) {
                flowWeek.setType(StatisticsWeekType.MONTH_WEEk.toValue());
                flowWeek.setMonth(flowDataRequest.getMonth());
            }
            flowWeek.setWeekStartDate(flowDataRequest.getWeekStartDate());
            flowWeek.setWeekEndDate(flowDataRequest.getWeekEndDate());
            flowWeek.setCreatTime(LocalDateTime.now());
            flowWeekList.add(flowWeek);
        });
        flowWeekMapper.insertFlowWeekList(flowWeekList);
    }

}
