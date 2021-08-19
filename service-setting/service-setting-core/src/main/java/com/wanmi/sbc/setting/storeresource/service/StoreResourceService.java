package com.wanmi.sbc.setting.storeresource.service;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.setting.api.constant.SettingErrorCode;
import com.wanmi.sbc.setting.api.request.storeresource.StoreResourceQueryRequest;
import com.wanmi.sbc.setting.bean.vo.StoreResourceVO;
import com.wanmi.sbc.setting.storeresource.model.root.StoreResource;
import com.wanmi.sbc.setting.storeresource.repository.StoreResourceRepository;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>店铺资源库业务逻辑</p>
 *
 * @author lq
 * @date 2019-11-05 16:12:49
 */
@Service("StoreResourceService")
public class StoreResourceService {
    @Autowired
    private StoreResourceRepository storeResourceRepository;

    /**
     * 新增店铺资源库
     *
     * @author lq
     */
    @Transactional(rollbackFor = Exception.class)
    public StoreResource add(StoreResource entity) {
        entity.setDelFlag(DeleteFlag.NO);
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());
        storeResourceRepository.save(entity);
        return entity;
    }

    /**
     * 修改店铺资源库
     *
     * @author lq
     */
    @Transactional(rollbackFor = Exception.class)
    public StoreResource modify(StoreResource newStoreResource) {
        StoreResource oldStoreResource = storeResourceRepository.findById(newStoreResource.getResourceId()).orElse(null);
        if (oldStoreResource == null || oldStoreResource.getDelFlag().compareTo(DeleteFlag.YES) == 0) {
            throw new SbcRuntimeException(SettingErrorCode.RESOURCE_NOT_EXIST_ERROR);
        }
        //更新素材
        newStoreResource.setUpdateTime(LocalDateTime.now());
        KsBeanUtil.copyProperties(newStoreResource, oldStoreResource);
        storeResourceRepository.save(oldStoreResource);
        return oldStoreResource;
    }

    /**
     * 单个删除店铺资源库
     *
     * @author lq
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Long id) {
        storeResourceRepository.deleteById(id);
    }

    /**
     * 批量更新素材的分类
     *
     * @param resourceIds
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateCateByIds(Long cateId, List<Long> resourceIds, Long storeId) {
        storeResourceRepository.updateCateByIds(cateId, resourceIds, storeId);
    }

    /**
     * 批量删除店铺资源库
     *
     * @author lq
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(List<Long> resourceIds, Long storeId) {
        StoreResourceQueryRequest queryRequest = StoreResourceQueryRequest.builder()
                .resourceIdList(resourceIds)
                .storeId(storeId).build();
        List<StoreResource> resources = storeResourceRepository.findAll(StoreResourceWhereCriteriaBuilder.build(queryRequest));
        if (CollectionUtils.isNotEmpty(resources)) {
            //删除素材
//            yunService.deleteResources(resourceIds, storeId);
            //逻辑删除素材库
            storeResourceRepository.deleteByIdList(resourceIds, storeId);
        }
    }

    /**
     * 批量删除店铺资源库
     *
     * @author lq
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteByIdList(List<Long> ids, Long storeId) {
        storeResourceRepository.deleteByIdList(ids, storeId);
    }


    /**
     * 单个查询店铺资源库
     *
     * @author lq
     */
    public StoreResource getById(Long id) {
        return storeResourceRepository.findById(id).orElse(null);
    }

    /**
     * 分页查询店铺资源库
     *
     * @author lq
     */
    public Page<StoreResource> page(StoreResourceQueryRequest queryReq) {
        return storeResourceRepository.findAll(
                StoreResourceWhereCriteriaBuilder.build(queryReq),
                queryReq.getPageRequest());
    }

    /**
     * 列表查询店铺资源库
     *
     * @author lq
     */
    public List<StoreResource> list(StoreResourceQueryRequest queryReq) {
        return storeResourceRepository.findAll(
                StoreResourceWhereCriteriaBuilder.build(queryReq),
                queryReq.getSort());
    }

    /**
     * 将实体包装成VO
     *
     * @author lq
     */
    public StoreResourceVO wrapperVo(StoreResource storeResource) {
        if (storeResource != null) {
            StoreResourceVO storeResourceVO = new StoreResourceVO();
            KsBeanUtil.copyPropertiesThird(storeResource, storeResourceVO);
            return storeResourceVO;
        }
        return null;
    }
}
