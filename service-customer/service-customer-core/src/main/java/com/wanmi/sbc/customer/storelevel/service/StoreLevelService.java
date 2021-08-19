package com.wanmi.sbc.customer.storelevel.service;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.request.storelevel.StoreLevelQueryRequest;
import com.wanmi.sbc.customer.bean.vo.StoreLevelVO;
import com.wanmi.sbc.customer.storelevel.model.root.StoreLevel;
import com.wanmi.sbc.customer.storelevel.repository.StoreLevelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>商户客户等级表业务逻辑</p>
 *
 * @author yang
 * @date 2019-02-27 19:51:30
 */
@Service("StoreLevelService")
public class StoreLevelService {
    @Autowired
    private StoreLevelRepository storeLevelRepository;

    /**
     * 新增商户客户等级表
     *
     * @author yang
     */
    @Transactional
    public StoreLevel add(StoreLevel entity) {
        entity.setDelFlag(DeleteFlag.NO.toValue());
        storeLevelRepository.save(entity);
        return entity;
    }

    /**
     * 修改商户客户等级表
     *
     * @author yang
     */
    @Transactional
    public StoreLevel modify(StoreLevel entity) {
        StoreLevel save = storeLevelRepository.save(entity);
        return save;
    }

    @Transactional
    public void modifyList(List<StoreLevel> storeLevels){
        storeLevelRepository.saveAll(storeLevels);
    }

    /**
     * 单个删除商户客户等级表
     *
     * @author yang
     */
    @Transactional
    public void deleteById(Long storeLevelId) {
        storeLevelRepository.deleteStoreLevel(storeLevelId);
    }

    /**
     * 单个查询商户客户等级表
     *
     * @author yang
     */
    public StoreLevel getById(Long storeLevelId) {
        return storeLevelRepository.findById(storeLevelId).orElse(null);
    }

    /**
     * 分页查询商户客户等级表
     *
     * @author yang
     */
    public Page<StoreLevel> page(StoreLevelQueryRequest queryReq) {
        return storeLevelRepository.findAll(
                StoreLevelWhereCriteriaBuilder.build(queryReq),
                queryReq.getPageRequest());
    }

    /**
     * 列表查询商户客户等级表
     *
     * @author yang
     */
    public List<StoreLevel> list(StoreLevelQueryRequest queryReq) {
        return storeLevelRepository.findAll(StoreLevelWhereCriteriaBuilder.build(queryReq));
    }

    /**
     * 根据等级名称查询
     *
     * @param storeId
     * @param levelName
     * @return
     */
    public List<StoreLevel> getByStoreIdAndLevelName(Long storeId, String levelName) {
        return storeLevelRepository.findByStoreIdAndLevelName(storeId, levelName);
    }

    /**
     * 查询某商户客户等级列表
     *
     * @param storeId
     * @return
     */
    public List<StoreLevel> findAllLevelByStoreId(Long storeId) {
        return storeLevelRepository.findByStoreIdOrderByCreateTimeAsc(storeId);
    }


    /**
     * 查询某商户会员等级
     *
     * @return
     */
    public List<StoreLevel> findByStoreIdOrderByCreateTimeAsc(List<Long> storeIds){
       return storeLevelRepository.findByStoreIdOrderByCreateTimeAsc(storeIds);
    }

    /**
     * 查询满足升级条件的店铺等级列表
     */
    public List<StoreLevel> queryByLevelUpCondition(Long storeId, BigDecimal amountConditions, Integer orderConditions) {
        return storeLevelRepository.queryByLevelUpCondition(storeId, amountConditions, orderConditions);
    }


    /**
     * 将实体包装成VO
     *
     * @author yang
     */
    public StoreLevelVO wrapperVo(StoreLevel storeLevel) {
        if (storeLevel != null) {
            StoreLevelVO storeLevelVO = new StoreLevelVO();
            KsBeanUtil.copyPropertiesThird(storeLevel, storeLevelVO);
            return storeLevelVO;
        }
        return null;
    }
}
