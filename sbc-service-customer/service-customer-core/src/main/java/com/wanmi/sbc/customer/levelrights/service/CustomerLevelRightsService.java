package com.wanmi.sbc.customer.levelrights.service;

import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.constant.CustomerLevelRightsErrorCode;
import com.wanmi.sbc.customer.api.request.levelrights.CustomerLevelRightsQueryRequest;
import com.wanmi.sbc.customer.bean.vo.CustomerLevelRightsVO;
import com.wanmi.sbc.customer.levelrights.model.root.CustomerLevelRights;
import com.wanmi.sbc.customer.levelrights.model.root.CustomerLevelRightsRel;
import com.wanmi.sbc.customer.levelrights.repository.CustomerLevelRightsRelRepository;
import com.wanmi.sbc.customer.levelrights.repository.CustomerLevelRightsRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * <p>会员等级权益表业务逻辑</p>
 */
@Service("CustomerLevelRightsService")
public class CustomerLevelRightsService {
    @Autowired
    private CustomerLevelRightsRepository customerLevelRightsRepository;

    @Autowired
    private CustomerLevelRightsRelRepository customerLevelRightsRelRepository;

    /**
     * 新增会员等级权益表
     *
     * @author minchen
     */
    @Transactional
    public CustomerLevelRights add(CustomerLevelRights entity) {
        List<CustomerLevelRights> rightsList = customerLevelRightsRepository.findAllList();
        if (CollectionUtils.isNotEmpty(rightsList)) {
            // 权益名称不能已经存在
            if (rightsList.stream().anyMatch(rights ->
                    StringUtils.equals(entity.getRightsName(), rights.getRightsName()))) {
                throw new SbcRuntimeException(CustomerLevelRightsErrorCode.RIGHTS_ALREADY_EXISTS);
            }
        }
        return customerLevelRightsRepository.save(entity);
    }

    /**
     * 修改会员等级权益表
     *
     * @author minchen
     */
    @Transactional
    public CustomerLevelRights modify(CustomerLevelRights entity) {
        // 根据id查询详情信息
        CustomerLevelRights rights = customerLevelRightsRepository.findByRightsId(entity.getRightsId());
        // 权益不存在
        if (Objects.isNull(rights)) {
            throw new SbcRuntimeException(CustomerLevelRightsErrorCode.RIGHTS_NO_EXIST);
        }
        // 除自身外, 权益名称不能重复
        if (customerLevelRightsRepository.findByRightsNameNotSelf(
                entity.getRightsId(), entity.getRightsName()).size() > 0) {
            throw new SbcRuntimeException(CustomerLevelRightsErrorCode.RIGHTS_ALREADY_EXISTS);
        }
        // 已关联客户等级情况下不可禁用
        List<CustomerLevelRightsRel> rels = customerLevelRightsRelRepository.findByRightsId(rights.getRightsId());
        if (entity.getStatus() == 0 && CollectionUtils.isNotEmpty(rels)) {
            throw new SbcRuntimeException(CustomerLevelRightsErrorCode.UPDATE_RELATED_TO_LEVEL);
        }
        return customerLevelRightsRepository.save(entity);
    }

    /**
     * 单个删除会员等级权益表
     *
     * @author minchen
     */
    @Transactional
    public void deleteById(Integer id) {
        List<CustomerLevelRightsRel> rels = customerLevelRightsRelRepository.findByRightsId(id);
        if (CollectionUtils.isNotEmpty(rels)) {
            throw new SbcRuntimeException(CustomerLevelRightsErrorCode.DEL_RELATED_TO_LEVEL);
        }
        // 删除权益
        customerLevelRightsRepository.deleteRightsById(id);
    }

    /**
     * 单个查询会员等级权益表
     *
     * @author minchen
     */
    public CustomerLevelRights getById(Integer id) {
        return customerLevelRightsRepository.findById(id).orElse(null);
    }

    /**
     * 分页查询会员等级权益表
     *
     * @author minchen
     */
    public Page<CustomerLevelRights> page(CustomerLevelRightsQueryRequest queryReq) {
        return customerLevelRightsRepository.findAll(
                CustomerLevelRightsWhereCriteriaBuilder.build(queryReq),
                queryReq.getPageRequest());
    }

    /**
     * 列表查询会员等级权益表
     *
     * @author minchen
     */
    public List<CustomerLevelRights> list(CustomerLevelRightsQueryRequest queryReq) {
        return customerLevelRightsRepository.findAll(CustomerLevelRightsWhereCriteriaBuilder.build(queryReq));
    }

    /**
     * 拖拽排序
     *
     * @param queryReq
     */
    @Transactional
    public void editSort(CustomerLevelRightsQueryRequest queryReq) {
        List<Integer> idList = queryReq.getRightsIdList();
        for (int i = 0; i < idList.size(); i++) {
            CustomerLevelRights rights = customerLevelRightsRepository.findById(idList.get(i)).orElse(null);
            if (Objects.nonNull(rights)) {
                rights.setUpdateTime(LocalDateTime.now());
                rights.setSort(i + 1);
                customerLevelRightsRepository.save(rights);
            }
        }
    }

    /**
     * 将实体包装成VO
     *
     * @author minchen
     */
    public CustomerLevelRightsVO wrapperVo(CustomerLevelRights customerLevelRights) {
        if (customerLevelRights != null) {
            CustomerLevelRightsVO customerLevelRightsVO = new CustomerLevelRightsVO();
            KsBeanUtil.copyPropertiesThird(customerLevelRights, customerLevelRightsVO);
            return customerLevelRightsVO;
        }
        return null;
    }
}
