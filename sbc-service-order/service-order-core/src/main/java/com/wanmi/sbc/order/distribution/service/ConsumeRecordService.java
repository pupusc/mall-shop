package com.wanmi.sbc.order.distribution.service;

import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.common.util.StringUtil;
import com.wanmi.sbc.order.api.request.distribution.PageByCustomerIdRequest;
import com.wanmi.sbc.order.bean.vo.ConsumeRecordVo;
import com.wanmi.sbc.order.bean.vo.CountCustomerConsumeVO;
import com.wanmi.sbc.order.distribution.model.root.ConsumeRecord;
import com.wanmi.sbc.order.distribution.repository.ConsumeRecordRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @Description: 消费记录service
 * @Autho qiaokang
 * @Date：2019-03-05 16:41:48
 */
@Service
@Transactional
public class ConsumeRecordService {

    /**
     * 注入消费记录Repository
     */
    @Autowired
    private ConsumeRecordRepository consumeRecordRepository;

    /**
     * 新增消费记录
     *
     * @param consumeRecord
     * @return
     */
    public ConsumeRecordVo addConsumeRecord(ConsumeRecord consumeRecord) {
        ConsumeRecord record = consumeRecordRepository.save(consumeRecord);
        ConsumeRecordVo consumeRecordVo = this.wrapperVo(record);

        return consumeRecordVo;
    }

    /**
     * 更新消费记录
     *
     * @param consumeRecord
     * @return
     */
    public int modifyConsumeRecord(ConsumeRecord consumeRecord) {
        return consumeRecordRepository.modifyConsumeRecord(consumeRecord);
    }

    /**
     * 消费记录实体转VO
     *
     * @param consumeRecord
     * @return
     */
    private ConsumeRecordVo wrapperVo(ConsumeRecord consumeRecord) {
        if (consumeRecord != null) {
            ConsumeRecordVo consumeRecordVo = new ConsumeRecordVo();
            KsBeanUtil.copyPropertiesThird(consumeRecord, consumeRecordVo);
            return consumeRecordVo;
        }
        return null;
    }

    /**
     * 统计分销员的顾客
     */
    public int countByDistributionCustomerId(String id) {
        return consumeRecordRepository.pageByDistributionCustomerIdCountNum(id);
    }

    /**
     * 统计客户累计的有效消费金额，订单数，
     */
    public List<CountCustomerConsumeVO> countValidConsume(List<String> ids) {
        List<Object> resultList = consumeRecordRepository.countValidConsume(ids);
        List<CountCustomerConsumeVO> voList = convertFromNativeSQLResult(resultList);
        return voList;
    }

    /**
     * 统计客户累计的消费金额，订单数，
     */
    public List<CountCustomerConsumeVO> countConsume(List<String> ids) {
        List<Object> resultList = consumeRecordRepository.countConsume(ids);
        List<CountCustomerConsumeVO> voList = convertFromNativeSQLResult(resultList);
        return voList;
    }

    public MicroServicePage<CountCustomerConsumeVO> pageByCustomerId(PageByCustomerIdRequest request) {
        int startNum = request.getPageNum() * request.getPageSize();
        int endNum = (request.getPageNum() + 1) * request.getPageSize();
        List<Object> page = consumeRecordRepository.pageByCustomerId(request.getCustomerId(), startNum, endNum);
        List<CountCustomerConsumeVO> content = this.convertFromNativeSQLResult(page);
        Integer total = consumeRecordRepository.pageByDistributionCustomerIdCountNum(request.getCustomerId());
       // 兼容有赞
        content.forEach(c-> {
            if(c.getOrderNum().intValue() == 0){
                c.setFirstOrderTime(null);
            }
        });
        return new MicroServicePage<>(content, request.getPageable(), total);
    }

    private List<CountCustomerConsumeVO> convertFromNativeSQLResult(List<Object> resultList) {
        List<CountCustomerConsumeVO> voList = new ArrayList<>();
        if (CollectionUtils.isEmpty(resultList)) {
            return voList;
        }
        for (Object obj : resultList) {
            Object[] result = (Object[]) obj;
            CountCustomerConsumeVO vo = new CountCustomerConsumeVO();
            vo.setCustomerId(StringUtil.cast(result, 0, String.class));
            vo.setOrderNum(StringUtil.cast(result, 1, BigInteger.class));
            vo.setAmount(StringUtil.cast(result, 2, BigDecimal.class));
            vo.setCustomerName(StringUtil.cast(result, 3, String.class));
            if (result.length > 4) {
                vo.setHeadImg(StringUtil.cast(result, 4, String.class));
            }
            // 兼容有赞
            if (result.length > 5 && Objects.nonNull(result[5]) && StringUtils.isNotBlank(result[5].toString())) {
                String str = result[5].toString().trim();
                LocalDateTime time =  LocalDateTime.parse(str, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S"));
                vo.setFirstOrderTime(time);
            }
            voList.add(vo);
        }
        return voList;
    }
}
