package com.wanmi.sbc.crm.rfmstatistic.service;

import com.wanmi.sbc.crm.api.request.rfmstatistic.RfmScoreStatisticRequest;
import com.wanmi.sbc.crm.bean.enums.RFMType;
import com.wanmi.sbc.crm.bean.enums.ScoreType;
import com.wanmi.sbc.crm.bean.vo.DataVo;
import com.wanmi.sbc.crm.bean.vo.RfmStatisticVo;
import com.wanmi.sbc.crm.rfmstatistic.mapper.RfmScoreStatisticMapper;
import com.wanmi.sbc.crm.rfmstatistic.model.RfmScoreStatistic;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: zgl
 * \* Date: 2019-10-15
 * \* Time: 17:39
 * \* To change this template use File | Settings | File Templates.
 * \* Description:
 * \
 */
@Service
public class RfmScoreStatisticService {

    @Autowired
    private RfmScoreStatisticMapper rfmScoreStatisticMapper;
    @Autowired
    private RfmCustomerDetailService rfmCustomerDetailService;

    /**
     * 插入数据
     * @param statDate
     */
    public void generate(String statDate){
        this.rfmScoreStatisticMapper.deleteByDate(statDate);
        this.rfmScoreStatisticMapper.saveRStatistic(statDate);
        this.rfmScoreStatisticMapper.saveFStatistic(statDate);
        this.rfmScoreStatisticMapper.saveMStatistic(statDate);
        this.rfmScoreStatisticMapper.saveAvgStatistic(statDate);

    }

    /**
     * 会员rfm分组统计
     * @param statDate
     */
    public void customerRfmGroupGenerate(String statDate){
        rfmCustomerDetailService.updateCustomerSystemGroupId(statDate);
    }
    /**
     *
     * @return
     */
    public List<RfmStatisticVo> queryScoreList(RfmScoreStatisticRequest request){
        List<RfmStatisticVo> retList = new ArrayList<>();
        if(request==null){
            request = RfmScoreStatisticRequest
                    .builder()
                        .statDate(LocalDate.now().minusDays(1))
                            .typeList(Arrays.asList(RFMType.values()))
                                .build();
        }
        if(request.getStatDate()==null){
            //为了测试进行测试，上线前打开，并注释后面一个
            request.setStatDate(LocalDate.now().minusDays(1));
           // request.setStatDate(LocalDate.now());
        }
        if(CollectionUtils.isEmpty(request.getTypeList())){
            request.setTypeList(Arrays.asList(RFMType.values()));
        }
        for(RFMType type: request.getTypeList()){
            request.setType(type.toValue());
            List<RfmScoreStatistic> list = rfmScoreStatisticMapper.queryList(request);
            retList.add(dataProcess(list,type));
        }

        return retList;
    }

    private RfmStatisticVo dataProcess(List<RfmScoreStatistic> list, RFMType type){
        RfmStatisticVo rfmStatisticVo = new RfmStatisticVo();
        rfmStatisticVo.setTitle(type.name()+"分分布情况");
        rfmStatisticVo.setType(type.toValue());
        List<DataVo> dataVos = new ArrayList<>();
        list.stream().forEach(bean->{
                DataVo dataVo = new DataVo();
                if(bean.getScoreType()==ScoreType.AVG_SCORE){
                    dataVo.setName("平均分");
                    dataVo.setValue(bean.getScore());
                }else{
                    dataVo.setName(bean.getScore().setScale(0, BigDecimal.ROUND_DOWN).toString()+"分");
                    dataVo.setValue(bean.getPersonNum());
                }
                dataVos.add(dataVo);
            }
        );
        rfmStatisticVo.setData(dataVos);
        return rfmStatisticVo;
    }
}
