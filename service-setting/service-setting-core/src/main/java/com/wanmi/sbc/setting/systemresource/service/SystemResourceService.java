package com.wanmi.sbc.setting.systemresource.service;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.setting.api.constant.SettingErrorCode;
import com.wanmi.sbc.setting.api.request.systemresource.SystemResourceQueryRequest;
import com.wanmi.sbc.setting.bean.vo.SystemResourceVO;
import com.wanmi.sbc.setting.systemresource.model.root.SystemResource;
import com.wanmi.sbc.setting.systemresource.repository.SystemResourceRepository;
import com.wanmi.sbc.setting.yunservice.YunService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>平台素材资源业务逻辑</p>
 *
 * @author lq
 * @date 2019-11-05 16:14:27
 */
@Service("SystemResourceService")
public class SystemResourceService {
    @Autowired
    private SystemResourceRepository systemResourceRepository;

    @Autowired
    private YunService yunService;

    /**
     * 新增平台素材资源
     *
     * @author lq
     */
    @Transactional(rollbackFor = Exception.class)
    public SystemResource add(SystemResource entity) {
        entity.setDelFlag(DeleteFlag.NO);
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());
        systemResourceRepository.save(entity);
        return entity;
    }

    /**
     * 修改平台素材资源
     *
     * @author lq
     */
    @Transactional(rollbackFor = Exception.class)
    public SystemResource modify(SystemResource newResource) {
        SystemResource oldResource = systemResourceRepository.findById(newResource.getResourceId()).orElse(null);
        if (oldResource == null || oldResource.getDelFlag().compareTo(DeleteFlag.YES) == 0) {
            throw new SbcRuntimeException(SettingErrorCode.RESOURCE_NOT_EXIST_ERROR);
        }
        //更新素材
        newResource.setUpdateTime(LocalDateTime.now());
        KsBeanUtil.copyProperties(newResource, oldResource);
        systemResourceRepository.save(oldResource);
        return oldResource;
    }

    /**
     * 单个删除平台素材资源
     *
     * @author lq
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Long id) {
        systemResourceRepository.deleteById(id);
    }

    /**
     * 批量删除平台素材资源
     *
     * @author lq
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteByIdList(List<Long> ids) {
        systemResourceRepository.deleteByIdList(ids);
    }


    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    public List<SystemResourceVO> findByIdList(List<Long> ids) {
        List<SystemResource> systemResourceList = systemResourceRepository.findAllById(ids);
        return KsBeanUtil.convert(systemResourceList, SystemResourceVO.class);
    }


    /**
     * 单个查询平台素材资源
     *
     * @author lq
     */
    public SystemResource getById(Long id) {
        return systemResourceRepository.getOne(id);
    }

    /**
     * 分页查询平台素材资源
     *
     * @author lq
     */
    public Page<SystemResource> page(SystemResourceQueryRequest queryReq) {
        return systemResourceRepository.findAll(
                SystemResourceWhereCriteriaBuilder.build(queryReq),
                queryReq.getPageRequest());
    }

    /**
     * 列表查询平台素材资源
     *
     * @author lq
     */
    public List<SystemResource> list(SystemResourceQueryRequest queryReq) {
        return systemResourceRepository.findAll(
                SystemResourceWhereCriteriaBuilder.build(queryReq),
                queryReq.getSort());
    }

    /**
     * 将实体包装成VO
     *
     * @author lq
     */
    public SystemResourceVO wrapperVo(SystemResource systemResource) {
        if (systemResource != null) {
            SystemResourceVO systemResourceVO = new SystemResourceVO();
            KsBeanUtil.copyPropertiesThird(systemResource, systemResourceVO);
            return systemResourceVO;
        }
        return null;
    }


    /**
     * 批量更新素材的分类
     *
     * @param resourceIds
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateCateByIds(Long cateId, List<Long> resourceIds) {
        systemResourceRepository.updateCateByIds(cateId, resourceIds);
    }

    /**
     * 逻辑删除素材
     *
     * @param resourceIds
     */
    @Transactional(rollbackFor = Exception.class)
    public List<SystemResourceVO> delete(List<Long> resourceIds) {
        SystemResourceQueryRequest queryRequest = SystemResourceQueryRequest.builder()
                .resourceIdList(resourceIds).build();
        List<SystemResource> resources = systemResourceRepository.findAll(SystemResourceWhereCriteriaBuilder.build(queryRequest));
//        if (CollectionUtils.isNotEmpty(resources)) {
//            //删除素材
//            yunService.deleteResources(resourceIds, null);
//        }
        List<SystemResource> systemResourceList = resources.stream().peek(entity->entity.setDelFlag(DeleteFlag.YES)).collect(Collectors.toList());
        return KsBeanUtil.convert(systemResourceList, SystemResourceVO.class);
    }
}
