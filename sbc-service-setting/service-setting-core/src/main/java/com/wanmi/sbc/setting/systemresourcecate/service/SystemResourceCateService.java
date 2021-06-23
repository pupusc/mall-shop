package com.wanmi.sbc.setting.systemresourcecate.service;

import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.setting.api.constant.SettingErrorCode;
import com.wanmi.sbc.setting.api.request.systemresource.SystemResourceQueryRequest;
import com.wanmi.sbc.setting.api.request.systemresourcecate.SystemResourceCateQueryRequest;
import com.wanmi.sbc.setting.bean.enums.CateParentTop;
import com.wanmi.sbc.setting.bean.vo.SystemResourceCateVO;
import com.wanmi.sbc.setting.systemresource.repository.SystemResourceRepository;
import com.wanmi.sbc.setting.systemresource.service.SystemResourceWhereCriteriaBuilder;
import com.wanmi.sbc.setting.systemresourcecate.model.root.SystemResourceCate;
import com.wanmi.sbc.setting.systemresourcecate.repository.SystemResourceCateRepository;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <p>平台素材资源分类业务逻辑</p>
 *
 * @author lq
 * @date 2019-11-05 16:14:55
 */
@Service("SystemResourceCateService")
public class SystemResourceCateService {


    private final String SPLIT_CHAR = "|";

    @Autowired
    private SystemResourceCateRepository systemResourceCateRepository;

    @Autowired
    private SystemResourceRepository systemResourceRepository;

    /**
     * 单个删除平台素材资源分类
     *
     * @author lq
     */
    @Transactional
    public void deleteById(Long id) {
        systemResourceCateRepository.deleteById(id);
    }

    /**
     * 批量删除平台素材资源分类
     *
     * @author lq
     */
    @Transactional
    public void deleteByIdList(List<Long> ids) {
        systemResourceCateRepository.deleteByIdList(ids);
    }

    /**
     * 单个查询平台素材资源分类
     *
     * @author lq
     */
    public SystemResourceCate getById(Long id) {
        return systemResourceCateRepository.findById(id).orElse(null);
    }

    /**
     * 分页查询平台素材资源分类
     *
     * @author lq
     */
    public Page<SystemResourceCate> page(SystemResourceCateQueryRequest queryReq) {
        return systemResourceCateRepository.findAll(
                SystemResourceCateWhereCriteriaBuilder.build(queryReq),
                queryReq.getPageRequest());
    }

    /**
     * 列表查询平台素材资源分类
     *
     * @author lq
     */
    public List<SystemResourceCate> list(SystemResourceCateQueryRequest queryReq) {
        return systemResourceCateRepository.findAll(
                SystemResourceCateWhereCriteriaBuilder.build(queryReq),
                queryReq.getSort());
    }

    /**
     * 将实体包装成VO
     *
     * @author lq
     */
    public SystemResourceCateVO wrapperVo(SystemResourceCate systemResourceCate) {
        if (systemResourceCate != null) {
            SystemResourceCateVO systemResourceCateVO = new SystemResourceCateVO();
            KsBeanUtil.copyPropertiesThird(systemResourceCate, systemResourceCateVO);
            return systemResourceCateVO;
        }
        return null;
    }


    /**
     * 条件查询素材分类
     *
     * @param resourceCate 参数
     * @return list
     */
    public List<SystemResourceCate> query(SystemResourceCateQueryRequest resourceCate) {
        List<SystemResourceCate> list = systemResourceCateRepository.findAll(SystemResourceCateWhereCriteriaBuilder
                .build(resourceCate));
        return list == null ? Collections.emptyList() : list;
    }

    /**
     * 根据ID查询素材分类
     *
     * @param cateId 分类ID
     * @return list
     */
    public SystemResourceCate findById(Long cateId) {
        return systemResourceCateRepository.findByCateId(cateId);
    }

    /**
     * 新增素材分类
     *
     * @param resourceCate 素材分类
     */
    @Transactional(rollbackFor = Exception.class)
    public SystemResourceCate add(SystemResourceCate resourceCate) {
        if (resourceCate.getCateParentId() == null) {
            resourceCate.setCateParentId((long) CateParentTop.ZERO.toValue());
        }

        //验证重复名称
        SystemResourceCateQueryRequest resourceCateQueryRequest = SystemResourceCateQueryRequest.builder()
                .cateName(resourceCate.getCateName())
                .delFlag(DeleteFlag.NO).build();
        if (systemResourceCateRepository.count(SystemResourceCateWhereCriteriaBuilder.build(resourceCateQueryRequest)
        ) > 0) {
            throw new SbcRuntimeException(SettingErrorCode.RESOURCE_CATE_NAME_EXIST_ERROR);
        }

        //验证在同一父类下是否超过20个分类
        resourceCateQueryRequest.setCateName(null);
        resourceCateQueryRequest.setCateParentId(resourceCate.getCateParentId());
        if (systemResourceCateRepository.count(SystemResourceCateWhereCriteriaBuilder.build(resourceCateQueryRequest)
        ) >= 20) {
            throw new SbcRuntimeException(SettingErrorCode.RESOURCE_CHILD_CATE_MAX_COUNT_ERROR);
        }

        resourceCate.setDelFlag(DeleteFlag.NO);
        resourceCate.setIsDefault(DefaultFlag.NO);
        resourceCate.setCreateTime(LocalDateTime.now());
        resourceCate.setUpdateTime(LocalDateTime.now());
        resourceCate.setCateGrade(1);
        resourceCate.setSort(0);

        //填充分类路径，获取父类的分类路径进行拼凑,例01|001|0001
        String catePath = String.valueOf(CateParentTop.ZERO.toValue()).concat
                (SPLIT_CHAR);
        if (resourceCate.getCateParentId() != CateParentTop.ZERO.toValue()) {
            SystemResourceCate parentResourceCate = systemResourceCateRepository.
                    findById(resourceCate.getCateParentId()).orElse(null);
            if (parentResourceCate == null || parentResourceCate.getDelFlag().compareTo(DeleteFlag.YES) == 0) {
                throw new SbcRuntimeException(SettingErrorCode.PARENT_RESOURCE_CATE_NOT_EXIST_ERROR);
            }
            catePath = parentResourceCate.getCatePath().concat(String.valueOf(parentResourceCate.getCateId())).concat
                    (SPLIT_CHAR);
            resourceCate.setCateGrade(parentResourceCate.getCateGrade() + 1);
        }
        resourceCate.setCatePath(catePath);
        return systemResourceCateRepository.save(resourceCate);
    }

    /**
     * 编辑素材分类
     *
     * @param newResourceCate 素材分类
     * @throws SbcRuntimeException
     */
    @Transactional(rollbackFor = Exception.class)
    public SystemResourceCate edit(SystemResourceCate newResourceCate) throws SbcRuntimeException {
        SystemResourceCate oldResourceCate = systemResourceCateRepository.findByCateId(newResourceCate.getCateId());
        if (oldResourceCate == null || oldResourceCate.getDelFlag().compareTo(DeleteFlag.YES) == 0) {
            throw new SbcRuntimeException(SettingErrorCode.INVALID_PARAMETER);
        }
        if (newResourceCate.getCateParentId() == null) {
            newResourceCate.setCateParentId((long) CateParentTop.ZERO.toValue());
            newResourceCate.setCateGrade(1);
        }

        //验证重复名称
        SystemResourceCateQueryRequest resourceCateQueryRequest = SystemResourceCateQueryRequest.builder()
                .cateName(newResourceCate.getCateName())
                .notCateId(newResourceCate
                        .getCateId())
                .delFlag(DeleteFlag.NO).build();
        if (systemResourceCateRepository.count(SystemResourceCateWhereCriteriaBuilder.build(resourceCateQueryRequest)
        ) > 0) {
            throw new SbcRuntimeException(SettingErrorCode.RESOURCE_CATE_NAME_EXIST_ERROR);
        }

        //验证在同一父类下是否超过20个分类
        resourceCateQueryRequest.setCateName(null);
        resourceCateQueryRequest.setCateParentId(newResourceCate.getCateParentId());
        if (systemResourceCateRepository.count(SystemResourceCateWhereCriteriaBuilder.build(resourceCateQueryRequest)
        ) >= 20) {
            throw new SbcRuntimeException(SettingErrorCode.RESOURCE_CHILD_CATE_MAX_COUNT_ERROR);
        }


        //填充分类路径，获取父类的分类路径进行拼凑,例01|001|0001|
        String catePath = String.valueOf(CateParentTop.ZERO.toValue()).concat
                (SPLIT_CHAR);
        if (newResourceCate.getCateParentId() != CateParentTop.ZERO.toValue()) {
            SystemResourceCate parentResourceCate = systemResourceCateRepository.findByCateId(newResourceCate
                    .getCateParentId());
            if (parentResourceCate == null || parentResourceCate.getDelFlag().compareTo(DeleteFlag.YES) == 0) {
                throw new SbcRuntimeException(SettingErrorCode.PARENT_RESOURCE_CATE_NOT_EXIST_ERROR);
            }
            catePath = parentResourceCate.getCatePath().concat(String.valueOf(parentResourceCate.getCateId())).concat
                    (SPLIT_CHAR);
            newResourceCate.setCateGrade(parentResourceCate.getCateGrade() + 1);
        }
        newResourceCate.setCatePath(catePath);

        //如果分类路径有变化，将所有子类进行更新路径
        if (!catePath.equals(oldResourceCate.getCatePath())&& !catePath.equals(oldResourceCate.getCatePath().concat
                (SPLIT_CHAR))) {
            final String newCatePath = catePath.concat(String.valueOf(oldResourceCate.getCateId())).concat(SPLIT_CHAR);

            String likeCatePath = oldResourceCate.getCatePath().concat(String.valueOf(oldResourceCate.getCateId()))
                    .concat(SPLIT_CHAR);
            SystemResourceCateQueryRequest resourceCate1 = SystemResourceCateQueryRequest.builder()
                    .likeCatePath(likeCatePath)
                    .build();
            List<SystemResourceCate> resourceCateList = systemResourceCateRepository.findAll
                    (SystemResourceCateWhereCriteriaBuilder.build(resourceCate1));
            if (CollectionUtils.isNotEmpty(resourceCateList)) {
                resourceCateList.stream().forEach(resourceCate2 -> {
                    resourceCate2.setCatePath(resourceCate2.getCatePath().replace(likeCatePath, newCatePath));
                    resourceCate2.setCateGrade(resourceCate2.getCatePath().split("\\" + SPLIT_CHAR).length - 1);
                    resourceCate2.setUpdateTime(LocalDateTime.now());
                });
            }
            this.systemResourceCateRepository.saveAll(resourceCateList);
        }

        //更新分类
        newResourceCate.setUpdateTime(LocalDateTime.now());
        KsBeanUtil.copyProperties(newResourceCate, oldResourceCate);
        return systemResourceCateRepository.save(oldResourceCate);
    }

    /**
     * 删除素材分类
     *
     * @param cateId 分类编号
     * @throws SbcRuntimeException
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long cateId) throws SbcRuntimeException {
        SystemResourceCate resourceCate = systemResourceCateRepository.findByCateId(cateId);
        if (resourceCate == null || resourceCate.getDelFlag().compareTo(DeleteFlag.YES) == 0) {
            throw new SbcRuntimeException(SettingErrorCode.INVALID_PARAMETER);
        }

        //查询默认分类
        SystemResourceCateQueryRequest resourceCate1 = SystemResourceCateQueryRequest.builder()
                .isDefault(DefaultFlag.YES).build();
        List<SystemResourceCate> resourceCateList = systemResourceCateRepository.findAll
                (SystemResourceCateWhereCriteriaBuilder.build(resourceCate1));
        //如果默认分类不存在，不允许删除
        if (CollectionUtils.isEmpty(resourceCateList)) {
            throw new SbcRuntimeException(SettingErrorCode.DEFAULT_RESOURCE_CATE_NOT_EXIST_ERROR);
        }

        List<Long> allCate = new ArrayList<>();
        allCate.add(resourceCate.getCateId());

        String oldCatePath = resourceCate.getCatePath().concat(String.valueOf(resourceCate.getCateId())).concat
                (SPLIT_CHAR);
        //将所有子类也更新为删除
        resourceCate1.setIsDefault(null);
        resourceCate1.setLikeCatePath(oldCatePath);
        List<SystemResourceCate> childCateList = systemResourceCateRepository.findAll
                (SystemResourceCateWhereCriteriaBuilder.build(resourceCate1));
        if (CollectionUtils.isNotEmpty(childCateList)) {
            childCateList.stream().forEach(cate -> {
                cate.setDelFlag(DeleteFlag.YES);
                allCate.add(cate.getCateId());
            });
            systemResourceCateRepository.saveAll(childCateList);
        }
        //更新分类
        resourceCate.setDelFlag(DeleteFlag.YES);
        systemResourceCateRepository.save(resourceCate);
        //迁移分类至默认分类
        systemResourceRepository.updateCateByCateIds(resourceCateList.get(0).getCateId(), allCate);
    }

    /**
     * 验证是否有子类
     *
     * @param cateId
     */
    public Integer checkChild(Long cateId) {
        SystemResourceCate resourceCate = systemResourceCateRepository.findByCateId(cateId);
        if (resourceCate == null || resourceCate.getDelFlag().compareTo(DeleteFlag.YES) == 0) {
            throw new SbcRuntimeException(SettingErrorCode.INVALID_PARAMETER);
        }

        String oldCatePath = resourceCate.getCatePath().concat(String.valueOf(resourceCate.getCateId())).concat
                (SPLIT_CHAR);
        SystemResourceCateQueryRequest resourceCateQueryRequest = SystemResourceCateQueryRequest.builder()
                .delFlag(DeleteFlag.NO).likeCatePath(oldCatePath).build();
        if (systemResourceCateRepository.count(SystemResourceCateWhereCriteriaBuilder.build(resourceCateQueryRequest)
        ) > 0) {
            return DefaultFlag.YES.toValue();
        }
        return DefaultFlag.NO.toValue();
    }

    /**
     * 验证是否有素材
     *
     * @param cateId
     */
    public Integer checkResource(Long cateId) {
        SystemResourceCate resourceCate = systemResourceCateRepository.findByCateId(cateId);
        if (resourceCate == null || resourceCate.getDelFlag().compareTo(DeleteFlag.YES) == 0) {
            throw new SbcRuntimeException(SettingErrorCode.INVALID_PARAMETER);
        }

        List<Long> allCate = new ArrayList<>();
        allCate.add(resourceCate.getCateId());
        String oldCatePath = resourceCate.getCatePath().concat(String.valueOf(resourceCate.getCateId())).concat
                (SPLIT_CHAR);
        SystemResourceCateQueryRequest resourceCateQueryRequest = SystemResourceCateQueryRequest.builder()
                .delFlag(DeleteFlag.NO).likeCatePath(oldCatePath).build();

        List<SystemResourceCate> childCateList = systemResourceCateRepository.findAll
                (SystemResourceCateWhereCriteriaBuilder.build(resourceCateQueryRequest));
        if (CollectionUtils.isNotEmpty(childCateList)) {
            childCateList.stream().forEach(cate -> {
                allCate.add(cate.getCateId());
            });
        }
        //素材
        SystemResourceQueryRequest resource = SystemResourceQueryRequest.builder()
                .delFlag(DeleteFlag.NO).cateIds(allCate).build();

        if (systemResourceRepository.count(SystemResourceWhereCriteriaBuilder.build(resource)) > 0) {
            return DefaultFlag.YES.toValue();
        }
        return DefaultFlag.NO.toValue();
    }


    /**
     * 初始化分类，生成默认分类
     */
    @Transactional(rollbackFor = Exception.class)
    public void init(SystemResourceCateQueryRequest resourceCate) {

        List<SystemResourceCate> resourceCateList = systemResourceCateRepository.findAll
                (SystemResourceCateWhereCriteriaBuilder.build(resourceCate));

        if (CollectionUtils.isEmpty(resourceCateList)) {
            SystemResourceCate resourceCate1 = new SystemResourceCate();
            resourceCate1.setCateName("全部分类");
            resourceCate1.setCateParentId((long) (CateParentTop.ZERO.toValue()));
            resourceCate1.setIsDefault(DefaultFlag.YES);
            resourceCate1.setDelFlag(DeleteFlag.NO);
            resourceCate1.setCateGrade(0);
            resourceCate1.setCatePath(String.valueOf(resourceCate1.getCateParentId()).concat(SPLIT_CHAR));
            systemResourceCateRepository.save(resourceCate1);
        }
    }
}
