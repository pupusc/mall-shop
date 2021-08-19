package com.wanmi.sbc.crm.rfmstatistic.service;

import com.github.pagehelper.PageHelper;
import com.wanmi.sbc.common.util.DateUtil;
import com.wanmi.sbc.crm.api.constant.RfmConstant;
import com.wanmi.sbc.crm.api.request.rfmsetting.RfmSettingQueryRequest;
import com.wanmi.sbc.crm.bean.enums.Period;
import com.wanmi.sbc.crm.bean.enums.RFMType;
import com.wanmi.sbc.crm.entity.BaseParam;
import com.wanmi.sbc.crm.rfmsetting.model.root.RfmSetting;
import com.wanmi.sbc.crm.rfmsetting.service.RfmSettingService;
import com.wanmi.sbc.crm.rfmstatistic.mapper.RfmCustomerDetailMapper;
import com.wanmi.sbc.crm.rfmstatistic.model.RfmCustomerDetail;
import com.wanmi.sbc.crm.utils.RfmDateUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: zgl
 * \* Date: 2019-10-14
 * \* Time: 14:55
 * \* To change this template use File | Settings | File Templates.
 * \* Description:
 * \
 */
@Service
public class RfmCustomerDetailService {

    @Autowired
    private RfmCustomerDetailMapper rfmCustomerDetailMapper;
    @Autowired
    private RfmSettingService rfmSettingService;

    public void generate(LocalDateTime localDateTime) {
        BaseParam baseParam = getParam(localDateTime);
        saveValues(baseParam);
        saveScore(RfmCustomerDetail.builder().statDate(LocalDate.parse(baseParam.getStatDate())).build());

    }

    public BaseParam getParam(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            localDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        }
        BaseParam baseParam = new BaseParam();
        baseParam.setStartTime(DateUtil.format(localDateTime.minusDays(RfmConstant.MAX_R_VALUE), DateUtil.FMT_TIME_1));
        baseParam.setEndTime(DateUtil.format(localDateTime, DateUtil.FMT_TIME_1));
        baseParam.setStatDate(DateUtil.format(localDateTime.minusDays(1), DateUtil.FMT_DATE_1));

        //设置R分的最大值，如果为空则设置默认值
        RfmSetting rfmSetting = this.rfmSettingService.getMaxParamSetting(RFMType.R);
        if (rfmSetting != null) {
            baseParam.setMaxValue(rfmSetting.getParam());
        } else {
            baseParam.setMaxValue(RfmConstant.MAX_R_VALUE);
        }


        return baseParam;
    }

    /**
     * 根据会员插入对应的RFM统计的原始值
     *
     * @param baseParam
     */
    @Transactional
    public void saveValues(BaseParam baseParam) {
        this.rfmCustomerDetailMapper.deleteByDate(baseParam.getStatDate());
        this.rfmCustomerDetailMapper.saveRValues(baseParam);

        //根据F或者M的设置的时间区间来设置统计的开始时间
        RfmSetting rfmSetting = this.rfmSettingService.getMaxParamSetting(RFMType.F);
        if (rfmSetting != null && rfmSetting.getPeriod() != null) {
            baseParam.setStartTime(RfmDateUtil.getDayTime(rfmSetting.getPeriod()));
        }
        this.rfmCustomerDetailMapper.saveFAndMValues(baseParam);

    }

    /**
     * 根据values统计RFM的得分
     *
     * @param rfmCustomerDetail
     */
    public void saveScore(RfmCustomerDetail rfmCustomerDetail) {
        Long totalCount = this.rfmCustomerDetailMapper.queryCount(rfmCustomerDetail);
        if (totalCount > 0) {
            Map<String, List<RfmSetting>> rfmMap = new HashMap<>();
            rfmMap = this.getRfmMap(Arrays.asList(RFMType.R, RFMType.F, RFMType.M));
            int pageSize = 2000;
            long totalPage = totalCount / pageSize;
            totalPage = totalCount % pageSize > 0 ? totalPage + 1 : totalPage;
            // Page page = new Page();
            for (int pageNum = 1; pageNum <= totalPage; pageNum++) {
                //  page
                List<RfmCustomerDetail> list = queryList(rfmCustomerDetail, pageNum, pageSize);
                ScoreProcess(list, rfmMap);
                this.rfmCustomerDetailMapper.batchUpdateScore(list);
            }
        }
    }

    /**
     * 分页查询
     *
     * @param rfmCustomerDetail
     * @return
     */
    public List<RfmCustomerDetail> queryList(RfmCustomerDetail rfmCustomerDetail, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<RfmCustomerDetail> list = this.rfmCustomerDetailMapper.queryAll(rfmCustomerDetail);
        return list;
    }

    /**
     * 对会员进行分组
     */
    public void updateCustomerSystemGroupId(String statDate) {
        this.rfmCustomerDetailMapper.updateCustomerSystemGroupId(statDate);
    }

    /**
     * 根据会员id获取系统推荐
     *
     * @param customerId
     * @return
     */
    public RfmCustomerDetail getCustomerDetailByCustomerId(String customerId) {
        return this.rfmCustomerDetailMapper.getCustomerDetail(customerId);
    }

    public Map<String, Object> queryCustomerDetail(String customerId) {
        Map<String, Object> retMap = new LinkedHashMap<>();
        RfmCustomerDetail rfmCustomerDetail = this.rfmCustomerDetailMapper.queryCustomerDetail(customerId);
        //   RfmSettingQueryRequest rfmSettingQueryRequest = RfmSettingQueryRequest.builder().type(RFMType.M).build();
        //   List<RfmSetting>  list = this.rfmSettingService.list(rfmSettingQueryRequest);
        RfmSetting rSetting = this.rfmSettingService.getMaxScoreSetting(RFMType.R);
        RfmSetting fSetting = this.rfmSettingService.getMaxScoreSetting(RFMType.F);
        RfmSetting mSetting = this.rfmSettingService.getMaxScoreSetting(RFMType.M);
        Period period = null;
        if (fSetting != null) {
            period = fSetting.getPeriod();
        }
        if (rfmCustomerDetail != null) {
            String lastOrderTime = "";
            if (rfmCustomerDetail.getRValue() < RfmConstant.MAX_R_VALUE) {
                lastOrderTime = rfmCustomerDetail.getRValue() + "天前";
            } else {
                lastOrderTime = "1年前";
            }
            Integer rfmTotal = Objects.nonNull(rfmCustomerDetail.getRScore()) ? rfmCustomerDetail.getRScore() : 0;
            if (Objects.nonNull(rfmCustomerDetail.getFScore())) {
                rfmTotal += rfmCustomerDetail.getFScore();
            }
            if (Objects.nonNull(rfmCustomerDetail.getMScore())) {
                rfmTotal += rfmCustomerDetail.getMScore();
            }
            retMap.put("rfmTotal", rfmTotal);
            retMap.put("lastTradeTime", lastOrderTime);
            retMap.put("tradeCount", rfmCustomerDetail.getFValue() + "次");
            retMap.put("tradeAmount", rfmCustomerDetail.getMValue());
            retMap.put("avgTradeAmount", rfmCustomerDetail.getFValue() == 0 ? 0 :
                    rfmCustomerDetail.getMValue().divide(new BigDecimal(rfmCustomerDetail.getFValue()),
                            BigDecimal.ROUND_DOWN));
            retMap.put("period", period.getContent());
            retMap.put("rScore", rfmCustomerDetail.getRScore());
            retMap.put("fScore", rfmCustomerDetail.getFScore());
            retMap.put("mScore", rfmCustomerDetail.getMScore());
            retMap.put("rMaxScore", rSetting.getScore());
            retMap.put("fMaxScore", fSetting.getScore());
            retMap.put("mMaxScore", mSetting.getScore());

        }
        return retMap;
    }

    /**
     * 根据Value得出Score
     *
     * @param list
     * @param rfmMap
     */
    private void ScoreProcess(List<RfmCustomerDetail> list, Map<String, List<RfmSetting>> rfmMap) {
        if (CollectionUtils.isNotEmpty(list)) {
            if (MapUtils.isNotEmpty(rfmMap)) {
                List<RfmSetting> rList = rfmMap.get(RFMType.R.name());
                List<RfmSetting> fList = rfmMap.get(RFMType.F.name());
                List<RfmSetting> mList = rfmMap.get(RFMType.M.name());
                for (RfmCustomerDetail rfmCustomerDetail : list) {
                    if (CollectionUtils.isNotEmpty(rList)) {
                        rfmCustomerDetail.setRScore(getScore(rfmCustomerDetail.getRValue().doubleValue(), rList));
                    }
                    if (CollectionUtils.isNotEmpty(fList)) {
                        rfmCustomerDetail.setFScore(getScore(rfmCustomerDetail.getFValue().doubleValue(), fList));
                    }
                    if (CollectionUtils.isNotEmpty(mList)) {
                        rfmCustomerDetail.setMScore(getScore(rfmCustomerDetail.getMValue().doubleValue(), mList));
                    }
                }
            }
        }
    }


    /**
     * 获取RFM对应设置的list
     *
     * @param typeList
     * @return
     */
    private Map<String, List<RfmSetting>> getRfmMap(List<RFMType> typeList) {
        Map<String, List<RfmSetting>> rfmMap = new HashMap<>();
        for (RFMType type : typeList) {
            RfmSettingQueryRequest rfmSettingQueryRequest = RfmSettingQueryRequest.builder().type(type).build();
            rfmSettingQueryRequest.putSort("param", "asc");
            rfmMap.put(type.name(), this.rfmSettingService.list(rfmSettingQueryRequest));
        }
        return rfmMap;
    }

    private Integer getScore(Double value, List<RfmSetting> settingList) {
        for (int i = 0; i < settingList.size(); i++) {
            if (i < (settingList.size() - 1)) {
                if (value >= settingList.get(i).getParam() && value < settingList.get(i + 1).getParam()) {
                    return settingList.get(i).getScore();
                }
            } else {
                if (value >= settingList.get(i).getParam()) {
                    return settingList.get(i).getScore();
                }
            }
        }
        return 0;
    }
}
