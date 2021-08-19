package com.wanmi.sbc.setting.storeresourcecate.service;

import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.setting.api.constant.SettingErrorCode;
import com.wanmi.sbc.setting.api.request.storeresource.StoreResourceQueryRequest;
import com.wanmi.sbc.setting.api.request.storeresourcecate.StoreResourceCateInitRequest;
import com.wanmi.sbc.setting.api.request.storeresourcecate.StoreResourceCateQueryRequest;
import com.wanmi.sbc.setting.bean.enums.CateParentTop;
import com.wanmi.sbc.setting.bean.vo.StoreResourceCateVO;
import com.wanmi.sbc.setting.storeresource.repository.StoreResourceRepository;
import com.wanmi.sbc.setting.storeresource.service.StoreResourceWhereCriteriaBuilder;
import com.wanmi.sbc.setting.storeresourcecate.model.root.StoreResourceCate;
import com.wanmi.sbc.setting.storeresourcecate.repository.StoreResourceCateRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>店铺资源资源分类表业务逻辑</p>
 *
 * @author lq
 * @date 2019-11-05 16:13:19
 */
@Service("StoreResourceCateService")
public class StoreResourceCateService {

    private final String SPLIT_CHAR = "|";

    @Autowired
    private StoreResourceCateRepository storeResourceCateRepository;


    @Autowired
    private StoreResourceRepository storeResourceRepository;

    /**
     * 新增店铺资源资源分类表
     *
     * @author lq
     */
    @Transactional(rollbackFor = Exception.class)
    public StoreResourceCate add(StoreResourceCate storeResourceCate) {
        if (storeResourceCate.getCateParentId() == null) {
            storeResourceCate.setCateParentId((long) CateParentTop.ZERO.toValue());
        }

        //验证重复名称
        StoreResourceCateQueryRequest resourceCateQueryRequest = StoreResourceCateQueryRequest.builder()
                .cateName(storeResourceCate.getCateName())
                .storeId(storeResourceCate.getStoreId())
                .companyInfoId(storeResourceCate.getCompanyInfoId())
                .delFlag(DeleteFlag.NO).build();

        if (storeResourceCateRepository.count(StoreResourceCateWhereCriteriaBuilder.build(resourceCateQueryRequest))
                > 0) {
            throw new SbcRuntimeException(SettingErrorCode.RESOURCE_CATE_NAME_EXIST_ERROR);
        }

        //验证在同一父类下是否超过20个分类
        resourceCateQueryRequest.setCateName(null);
        resourceCateQueryRequest.setCateParentId(storeResourceCate.getCateParentId());
        if (storeResourceCateRepository.count(StoreResourceCateWhereCriteriaBuilder.build(resourceCateQueryRequest))
                >= 20) {
            throw new SbcRuntimeException(SettingErrorCode.RESOURCE_CHILD_CATE_MAX_COUNT_ERROR);
        }

        storeResourceCate.setDelFlag(DeleteFlag.NO);
        storeResourceCate.setIsDefault(DefaultFlag.NO);
        storeResourceCate.setCreateTime(LocalDateTime.now());
        storeResourceCate.setUpdateTime(LocalDateTime.now());
        storeResourceCate.setCateGrade(1);
        storeResourceCate.setSort(0);

        //填充分类路径，获取父类的分类路径进行拼凑,例01|001|0001
        String catePath = String.valueOf(CateParentTop.ZERO.toValue()).concat(SPLIT_CHAR);
        if (storeResourceCate.getCateParentId() != CateParentTop.ZERO.toValue()) {
            StoreResourceCate parentResourceCate = storeResourceCateRepository.findById(storeResourceCate
                    .getCateParentId()).orElse(null);
            if (parentResourceCate == null || parentResourceCate.getDelFlag().compareTo(DeleteFlag.YES) == 0) {
                throw new SbcRuntimeException(SettingErrorCode.PARENT_RESOURCE_CATE_NOT_EXIST_ERROR);
            }
            catePath = parentResourceCate.getCatePath().concat(String.valueOf(parentResourceCate.getCateId())).concat
                    (SPLIT_CHAR);
            storeResourceCate.setCateGrade(parentResourceCate.getCateGrade() + 1);
        }
        storeResourceCate.setCatePath(catePath);
        return storeResourceCateRepository.save(storeResourceCate);
    }

    /**
     * 修改店铺资源资源分类表
     *
     * @author lq
     */
    @Transactional(rollbackFor = Exception.class)
    public StoreResourceCate modify(StoreResourceCate newResourceCate) {
        StoreResourceCate oldResourceCate = storeResourceCateRepository.findByCateIdAndStoreId(newResourceCate
                .getCateId(), newResourceCate.getStoreId());
        if (oldResourceCate == null || oldResourceCate.getDelFlag().compareTo(DeleteFlag.YES) == 0) {
            throw new SbcRuntimeException(SettingErrorCode.INVALID_PARAMETER);
        }
        if (newResourceCate.getCateParentId() == null) {
            newResourceCate.setCateParentId((long) CateParentTop.ZERO.toValue());
            newResourceCate.setCateGrade(1);
        }

        //验证重复名称
        StoreResourceCateQueryRequest resourceCateQueryRequest = StoreResourceCateQueryRequest.builder()
                .cateName(newResourceCate.getCateName())
                .storeId(newResourceCate.getStoreId())
                .companyInfoId(newResourceCate.getCompanyInfoId())
                .notCateId(newResourceCate.getCateId())
                .delFlag(DeleteFlag.NO).build();

        if (storeResourceCateRepository.count(StoreResourceCateWhereCriteriaBuilder.build(resourceCateQueryRequest))
                > 0) {
            throw new SbcRuntimeException(SettingErrorCode.RESOURCE_CATE_NAME_EXIST_ERROR);
        }

        //验证在同一父类下是否超过20个分类
        resourceCateQueryRequest.setCateName(null);
        resourceCateQueryRequest.setCateParentId(newResourceCate.getCateParentId());
        if (storeResourceCateRepository.count(StoreResourceCateWhereCriteriaBuilder.build(resourceCateQueryRequest))
                >= 20) {
            throw new SbcRuntimeException(SettingErrorCode.RESOURCE_CHILD_CATE_MAX_COUNT_ERROR);
        }


        //填充分类路径，获取父类的分类路径进行拼凑,例01|001|0001
        String catePath = String.valueOf(CateParentTop.ZERO.toValue()).concat(SPLIT_CHAR);
        if (newResourceCate.getCateParentId() != CateParentTop.ZERO.toValue()) {
            StoreResourceCate parentResourceCate = storeResourceCateRepository.findByCateIdAndStoreId(newResourceCate
                    .getCateParentId(), newResourceCate.getStoreId());
            if (parentResourceCate == null || parentResourceCate.getDelFlag().compareTo(DeleteFlag.YES) == 0) {
                throw new SbcRuntimeException(SettingErrorCode.PARENT_RESOURCE_CATE_NOT_EXIST_ERROR);
            }
            catePath = parentResourceCate.getCatePath().concat(String.valueOf(parentResourceCate.getCateId())).concat
                    (SPLIT_CHAR);
            newResourceCate.setCateGrade(parentResourceCate.getCateGrade() + 1);
        } else {
            newResourceCate.setCateGrade(1);
        }
        newResourceCate.setCatePath(catePath);


        //历史原因：一级路径存在俩种格式（1： 0 ，2： 0|--现在统一为0|）
        //如果分类路径有变化，将所有子类进行更新路径
        if (!catePath.equals(oldResourceCate.getCatePath()) && !catePath.equals(oldResourceCate.getCatePath().concat(SPLIT_CHAR))) {
            final String newCatePath = catePath.concat(String.valueOf(oldResourceCate.getCateId())).concat(SPLIT_CHAR);

            String likeCatePath = oldResourceCate.getCatePath().concat(String.valueOf(oldResourceCate.getCateId()))
                    .concat(SPLIT_CHAR);
            StoreResourceCateQueryRequest resourceCate1 = StoreResourceCateQueryRequest.builder()
                    .likeCatePath(likeCatePath)
                    .build();
            List<StoreResourceCate> ResourceCateList = storeResourceCateRepository.findAll
                    (StoreResourceCateWhereCriteriaBuilder.build(resourceCate1));
            if (CollectionUtils.isNotEmpty(ResourceCateList)) {
                ResourceCateList.stream().forEach(resourceCate2 -> {
                    resourceCate2.setCatePath(resourceCate2.getCatePath().replace(likeCatePath, newCatePath));
                    resourceCate2.setCateGrade(resourceCate2.getCatePath().split("\\" + SPLIT_CHAR).length - 1);
                    resourceCate2.setUpdateTime(LocalDateTime.now());
                });
            }
            this.storeResourceCateRepository.saveAll(ResourceCateList);
        }

        //更新分类
        newResourceCate.setUpdateTime(LocalDateTime.now());
        KsBeanUtil.copyProperties(newResourceCate, oldResourceCate);
        return storeResourceCateRepository.save(oldResourceCate);
    }

    /**
     * 单个删除店铺资源资源分类表
     *
     * @author lq
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long cateId, Long storeId) {
        StoreResourceCate storeResourceCate = storeResourceCateRepository.findByCateIdAndStoreId(cateId, storeId);
        if (storeResourceCate == null || storeResourceCate.getDelFlag().compareTo(DeleteFlag.YES) == 0) {
            throw new SbcRuntimeException(SettingErrorCode.INVALID_PARAMETER);
        }
        //查询默认分类
        StoreResourceCateQueryRequest resourceCate1 = StoreResourceCateQueryRequest.builder()
                .storeId(storeId)
                .isDefault(DefaultFlag.YES).build();

        List<StoreResourceCate> ResourceCateList = storeResourceCateRepository.findAll
                (StoreResourceCateWhereCriteriaBuilder.build(resourceCate1));
        //如果默认分类不存在，不允许删除
        if (org.apache.commons.collections.CollectionUtils.isEmpty(ResourceCateList)) {
            throw new SbcRuntimeException(SettingErrorCode.DEFAULT_RESOURCE_CATE_NOT_EXIST_ERROR);
        }

        List<Long> allCate = new ArrayList<>();
        allCate.add(storeResourceCate.getCateId());

        String oldCatePath = storeResourceCate.getCatePath().concat(String.valueOf(storeResourceCate.getCateId()))
                .concat(SPLIT_CHAR);
        //将所有子类也更新为删除
        resourceCate1.setIsDefault(null);
        resourceCate1.setLikeCatePath(oldCatePath);
        resourceCate1.setStoreId(storeId);
        List<StoreResourceCate> childCateList = storeResourceCateRepository.findAll
                (StoreResourceCateWhereCriteriaBuilder.build(resourceCate1));
        if (CollectionUtils.isNotEmpty(childCateList)) {
            childCateList.stream().forEach(cate -> {
                cate.setDelFlag(DeleteFlag.YES);
                allCate.add(cate.getCateId());
            });
            storeResourceCateRepository.saveAll(childCateList);
        }
        //更新分类
        storeResourceCate.setDelFlag(DeleteFlag.YES);
        storeResourceCateRepository.save(storeResourceCate);
        //迁移分类至默认分类
        storeResourceRepository.updateCateByCateIds(ResourceCateList.get(0).getCateId(), allCate, storeId);
    }


    /**
     * 单个查询店铺资源资源分类表
     *
     * @author lq
     */
    public StoreResourceCate getById(Long id) {
        return storeResourceCateRepository.findById(id).orElse(null);
    }

    /**
     * 分页查询店铺资源资源分类表
     *
     * @author lq
     */
    public Page<StoreResourceCate> page(StoreResourceCateQueryRequest queryReq) {
        return storeResourceCateRepository.findAll(
                StoreResourceCateWhereCriteriaBuilder.build(queryReq),
                queryReq.getPageRequest());
    }

    /**
     * 列表查询店铺资源资源分类表
     *
     * @author lq
     */
    public List<StoreResourceCate> list(StoreResourceCateQueryRequest queryReq) {
        return storeResourceCateRepository.findAll(
                StoreResourceCateWhereCriteriaBuilder.build(queryReq),
                queryReq.getSort());
    }

    /**
     * 将实体包装成VO
     *
     * @author lq
     */
    public StoreResourceCateVO wrapperVo(StoreResourceCate storeResourceCate) {
        if (storeResourceCate != null) {
            StoreResourceCateVO storeResourceCateVO = new StoreResourceCateVO();
            KsBeanUtil.copyPropertiesThird(storeResourceCate, storeResourceCateVO);
            return storeResourceCateVO;
        }
        return null;
    }

    /**
     * 验证是否有子类
     *
     * @param cateId  素材分类id
     * @param storeId 店铺id
     */
    public Integer checkChild(Long cateId, Long storeId) {
        StoreResourceCate storeResourceCate = storeResourceCateRepository.findByCateIdAndStoreId(cateId, storeId);
        if (storeResourceCate == null || storeResourceCate.getDelFlag().compareTo(DeleteFlag.YES) == 0) {
            throw new SbcRuntimeException(SettingErrorCode.INVALID_PARAMETER);
        }

        String oldCatePath = storeResourceCate.getCatePath().concat(String.valueOf(storeResourceCate.getCateId())
                .concat(SPLIT_CHAR));
        StoreResourceCateQueryRequest resourceCateQueryRequest = StoreResourceCateQueryRequest.builder()
                .delFlag(DeleteFlag.NO).likeCatePath(oldCatePath).storeId(storeId).build();
        if (storeResourceCateRepository.count(StoreResourceCateWhereCriteriaBuilder.build(resourceCateQueryRequest))
                > 0) {
            return DefaultFlag.YES.toValue();
        }
        return DefaultFlag.NO.toValue();
    }

    /**
     * 验证是否有素材
     *
     * @param cateId  素材分类id
     * @param storeId 店铺id
     */
    public Integer checkResource(Long cateId, Long storeId) {
        StoreResourceCate storeResourceCate = storeResourceCateRepository.findByCateIdAndStoreId(cateId, storeId);
        if (storeResourceCate == null || storeResourceCate.getDelFlag().compareTo(DeleteFlag.YES) == 0) {
            throw new SbcRuntimeException(SettingErrorCode.INVALID_PARAMETER);
        }
        List<Long> allCate = new ArrayList<>();
        allCate.add(storeResourceCate.getCateId());
        String oldCatePath = storeResourceCate.getCatePath().concat(String.valueOf(storeResourceCate.getCateId()))
                .concat(SPLIT_CHAR);

        StoreResourceCateQueryRequest resourceCateQueryRequest = StoreResourceCateQueryRequest.builder()
                .delFlag(DeleteFlag.NO).likeCatePath(oldCatePath).storeId(storeId).build();
        List<StoreResourceCate> childCateList = storeResourceCateRepository.findAll
                (StoreResourceCateWhereCriteriaBuilder.build(resourceCateQueryRequest));
        if (CollectionUtils.isNotEmpty(childCateList)) {
            childCateList.stream().forEach(cate -> {
                allCate.add(cate.getCateId());
            });
        }
        //素材
        StoreResourceQueryRequest resource = StoreResourceQueryRequest.builder()
                .delFlag(DeleteFlag.NO).cateIds(allCate).storeId(storeId).build();
        if (storeResourceRepository.count(StoreResourceWhereCriteriaBuilder.build(resource)) > 0) {
            return DefaultFlag.YES.toValue();
        }
        return DefaultFlag.NO.toValue();
    }


    /**
     * 初始化分类，生成默认分类
     */
    @Transactional(rollbackFor = Exception.class)
    public void init(StoreResourceCateInitRequest storeResourceCate) {
        StoreResourceCateQueryRequest resourceCateQueryRequest = StoreResourceCateQueryRequest.builder()
                .storeId(storeResourceCate.getStoreId())
                .companyInfoId(storeResourceCate.getCompanyInfoId())
                .cateParentId(storeResourceCate.getCateParentId()).build();
        List<StoreResourceCate> ResourceCateList = storeResourceCateRepository.findAll
                (StoreResourceCateWhereCriteriaBuilder.build(resourceCateQueryRequest));

        if (CollectionUtils.isEmpty(ResourceCateList)) {
            StoreResourceCate storeResourceCate1 = new StoreResourceCate();
            storeResourceCate1.setCateName("全部分类");
            storeResourceCate1.setCateParentId((long) (CateParentTop.ZERO.toValue()));
            storeResourceCate1.setIsDefault(DefaultFlag.YES);
            storeResourceCate1.setDelFlag(DeleteFlag.NO);
            storeResourceCate1.setCateGrade(1);
            storeResourceCate1.setCatePath(String.valueOf(storeResourceCate.getCateParentId()).concat(SPLIT_CHAR));


            //商家和店铺idstoreImgCate.init
            storeResourceCate1.setCompanyInfoId(storeResourceCate.getCompanyInfoId());
            storeResourceCate1.setStoreId(storeResourceCate.getStoreId());
            storeResourceCate1.setSort(0);
            storeResourceCate1.setCreateTime(LocalDateTime.now());
            storeResourceCate1.setUpdateTime(LocalDateTime.now());
            storeResourceCateRepository.save(storeResourceCate1);
        }
    }

}
