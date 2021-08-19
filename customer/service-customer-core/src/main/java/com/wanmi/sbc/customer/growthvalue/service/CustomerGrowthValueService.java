package com.wanmi.sbc.customer.growthvalue.service;

import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.request.growthvalue.CustomerGrowthValueQueryRequest;
import com.wanmi.sbc.customer.bean.vo.CustomerGrowthValueVO;
import com.wanmi.sbc.customer.growthvalue.model.root.CustomerGrowthValue;
import com.wanmi.sbc.customer.growthvalue.repository.CustomerGrowthValueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

/**
 * <p>客户成长值明细表业务逻辑</p>
 *
 * @author yang
 * @since 2019/2/23
 */
@Service("CustomerGrowthValueService")
@EnableBinding
public class CustomerGrowthValueService {

    @Autowired
    private CustomerGrowthValueRepository customerGrowthValueRepository;

    /**
     * 分页查询客户成长值明细表
     *
     * @param queryReq
     * @return
     */
    public Page<CustomerGrowthValue> page(CustomerGrowthValueQueryRequest queryReq) {
        return customerGrowthValueRepository.findAll(
                CustomerGrowthValueWhereCriteriaBuilder.build(queryReq),
                queryReq.getPageRequest());
    }


    /**
     * 将实体包装成VO
     *
     * @author minchen
     */
    public CustomerGrowthValueVO wrapperVo(CustomerGrowthValue customerGrowthValue) {
        if (customerGrowthValue != null) {
            CustomerGrowthValueVO customerGrowthValueVO = new CustomerGrowthValueVO();
            KsBeanUtil.copyPropertiesThird(customerGrowthValue, customerGrowthValueVO);
            return customerGrowthValueVO;
        }
        return null;
    }

    /**
     * 计算当天到账的成长值
     * @return
     */
    public Integer getGrowthValueForMessage(CustomerGrowthValueQueryRequest request){
        return customerGrowthValueRepository.getGrowthValueToday(
                request.getCustomerId(),
                request.getType(),
                request.getGteGainStartDate(),
                request.getLteGainEndDate());
    }

}
