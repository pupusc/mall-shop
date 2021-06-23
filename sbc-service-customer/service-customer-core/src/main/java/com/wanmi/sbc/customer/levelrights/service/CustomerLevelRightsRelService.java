package com.wanmi.sbc.customer.levelrights.service;

import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.request.levelrights.CustomerLevelRightsRelRequest;
import com.wanmi.sbc.customer.bean.vo.CustomerLevelRightsRelVO;
import com.wanmi.sbc.customer.levelrights.model.root.CustomerLevelRightsRel;
import com.wanmi.sbc.customer.levelrights.repository.CustomerLevelRightsRelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>会员等级与权益关联表业务逻辑</p>
 *
 * @author yang
 * @since 2019/2/27
 */
@Service("CustomerLevelRightsRelService")
public class CustomerLevelRightsRelService {

    @Autowired
    private CustomerLevelRightsRelRepository customerLevelRightsRelRepository;

    /**
     * 根据权益id列表查询会员等级与权益关联表
     *
     * @param queryReq
     * @return
     */
    public List<CustomerLevelRightsRel> listByRightsId(CustomerLevelRightsRelRequest queryReq) {
        return customerLevelRightsRelRepository.findByRightsId(queryReq.getRightsId());
    }

    /**
     * 根据等级id列表查询会员等级与权益关联表
     *
     * @param queryReq
     * @return
     */
    public List<CustomerLevelRightsRel> listByLevelId(CustomerLevelRightsRelRequest queryReq) {
        return customerLevelRightsRelRepository.findByCustomerLevelId(queryReq.getCustomerLevelId());
    }

    /**
     * 将实体包装成VO
     *
     * @author minchen
     */
    public CustomerLevelRightsRelVO wrapperVo(CustomerLevelRightsRel customerLevelRightsRel) {
        if (customerLevelRightsRel != null) {
            CustomerLevelRightsRelVO customerLevelRightsRelVO = new CustomerLevelRightsRelVO();
            KsBeanUtil.copyPropertiesThird(customerLevelRightsRel, customerLevelRightsRelVO);
            return customerLevelRightsRelVO;
        }
        return null;
    }

}
