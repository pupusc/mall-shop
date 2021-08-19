package com.wanmi.sbc.crm.customgroup.service;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.wanmi.sbc.common.util.DateUtil;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.crm.api.constant.RfmConstant;
import com.wanmi.sbc.crm.bean.vo.CustomGroupDetailVo;
import com.wanmi.sbc.crm.bean.vo.CustomGroupVo;
import com.wanmi.sbc.crm.constant.CustomGroupConstant;
import com.wanmi.sbc.crm.customgroup.mapper.*;
import com.wanmi.sbc.crm.customgroup.model.CustomGroup;
import com.wanmi.sbc.crm.customgroup.model.CustomGroupDetail;
import com.wanmi.sbc.crm.customgroup.model.CustomGroupParam;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: zgl
 * \* Date: 2019-11-12
 * \* Time: 15:29
 * \* To change this template use File | Settings | File Templates.
 * \* Description:
 * \
 */
@Service
public class CustomGroupGenerateService {
    @Autowired
    private CustomerTradeStatisticsMapper customerTradeStatisticsMapper;
    @Autowired
    private CustomerBaseInfoMapper customerBaseInfoMapper;
    @Autowired
    private CustomGroupRelMapper customGroupCustomerRelMapper;
    @Autowired
    private CustomGroupMapper customGroupMapper;
    @Autowired
    private CustomGroupStatisticsMapper customGroupStatisticsMapper;
    @Autowired
    private CustomerRecentParamStatisticsMapper customerRecentParamStatisticsMapper;


    @Transactional
    public void generateCustomerTradeStatistics(LocalDate localDate){
        if(localDate == null){
            localDate = LocalDate.now();
        }
        CustomGroupParam param = null;
        this.customerTradeStatisticsMapper.delete();
        for(Integer period : CustomGroupConstant.DEFAULT_PERIOD){
            param = new CustomGroupParam();
            param.setPeriod(period);
            getParam(param,localDate);
            this.customerTradeStatistics(param);
        }
    }

    @Transactional
    public void generateCustomerBaseInfoStatistics(){
        this.customerBaseInfoMapper.delete();
        this.customerBaseInfoMapper.save();
    }

    @Transactional
    public void generateCustomerRecentParamStatistics(LocalDate localDate){
        CustomGroupParam param = new CustomGroupParam();
        param.setPeriod(RfmConstant.MAX_R_VALUE);
        param.setMaxValue(RfmConstant.MAX_R_VALUE);
        param = getParam(param,localDate);
        this.customerRecentParamStatisticsMapper.delete();
        this.customerRecentParamStatisticsMapper.save(param);
    }

    @Transactional
    public void generateCustomGroupCustomerRelStatistics(){
        int totalCount = this.customGroupMapper.selectCount();
        if(totalCount>0){
            //清空表
            this.customGroupCustomerRelMapper.delete();
            int pageSize = 10;
            int totalPage = totalCount%pageSize==0?totalCount/pageSize:totalCount/pageSize+1;
            for (int pageNum = 1;pageNum<=totalPage;pageNum++){
                PageHelper.startPage(pageNum,pageSize);
                List<CustomGroup> list = this.customGroupMapper.selectList();
                for(CustomGroup customGroup : list){
                    if(customGroup.getGroupDetail()!=null) {
                        //CustomGroupVo customGroupVo = KsBeanUtil.convert(customGroup,CustomGroupVo.class);
                        CustomGroupDetail customGroupDetail = JSONObject.parseObject(customGroup.getGroupDetail(),CustomGroupDetail.class);
                     /*   customGroupDetail.setProvinceIdList(customGroupDetail.getProvinceIdList());
                        customGroupDetail.setCityIdList(customGroupDetail.getCityIdList());*/
                        customGroupDetail.setId(customGroup.getId());
                        this.customGroupCustomerRelMapper.insertBySelect(customGroupDetail);
                    }
                }
            }
        }
    }


    public void generateCustomGroupStatistics(LocalDate localDate){
        if(localDate == null){
            localDate = LocalDate.now().minusDays(1);
        }
        String statDate = localDate.format(DateTimeFormatter.ofPattern(DateUtil.FMT_DATE_1));
        this.customGroupStatisticsMapper.deleteByStatDate(statDate);
        this.customGroupStatisticsMapper.insertBySelect(statDate);
    }
    public CustomGroupParam getParam(CustomGroupParam param,LocalDate localDate){

        LocalDateTime localDateTime = LocalDateTime.of(localDate,LocalTime.MIN);
        param.setStartTime(DateUtil.format(localDateTime.minusDays(param.getPeriod()),DateUtil.FMT_TIME_1));
        param.setEndTime(DateUtil.format(localDateTime,DateUtil.FMT_TIME_1));
        param.setStatDate(localDate.minusDays(1).format(DateTimeFormatter.ofPattern(DateUtil.FMT_DATE_1)));
        return param;
    }

    public void customerTradeStatistics(CustomGroupParam param){

        this.customerTradeStatisticsMapper.save(param);
    }


}
