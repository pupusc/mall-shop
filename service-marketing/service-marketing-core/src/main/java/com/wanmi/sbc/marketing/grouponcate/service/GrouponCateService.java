package com.wanmi.sbc.marketing.grouponcate.service;

import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.marketing.api.request.grouponactivity.GrouponActivityQueryRequest;
import com.wanmi.sbc.marketing.bean.constant.Constant;
import com.wanmi.sbc.marketing.bean.constant.GrouponErrorCode;
import com.wanmi.sbc.marketing.bean.vo.GrouponCateSortVO;
import com.wanmi.sbc.marketing.bean.vo.GrouponCateVO;
import com.wanmi.sbc.marketing.grouponactivity.repository.GrouponActivityRepository;
import com.wanmi.sbc.marketing.grouponactivity.service.GrouponActivityWhereCriteriaBuilder;
import com.wanmi.sbc.marketing.grouponcate.model.entity.GrouponCateSort;
import com.wanmi.sbc.marketing.grouponcate.model.root.GrouponCate;
import com.wanmi.sbc.marketing.grouponcate.repository.GrouponCateRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * <p>拼团分类信息表业务逻辑</p>
 *
 * @author groupon
 * @date 2019-05-15 14:13:58
 */
@Service("GrouponCateService")
public class GrouponCateService {
    @Autowired
    private GrouponCateRepository grouponCateRepository;

    @Autowired
    private GrouponActivityRepository grouponActivityRepository;

    /**
     * 列表查询拼团分类信息表
     *
     * @author groupon
     */
    public List<GrouponCate> list() {
        return grouponCateRepository.listGrouponCate();
    }

    /**
     * 单个查询拼团分类信息
     *
     * @author groupon
     */
    public GrouponCate getById(String id) {
        return grouponCateRepository.findByGrouponCateIdAndDelFlag(id, DeleteFlag.NO);
    }

    /**
     * 根据拼团分类ID集合批量查询拼团分类信息
     * @param grouponCateIds
     * @return
     */
    public List<GrouponCate> findByGrouponCateIdIn(List<String> grouponCateIds){
        return grouponCateRepository.findByGrouponCateIdIn(grouponCateIds);
    }

    /**
     * 新增拼团分类信息
     *
     * @author groupon
     */
    @Transactional
    public void add(GrouponCate entity) {
        List<GrouponCate> grouponCates = grouponCateRepository.listGrouponCate();
        if (CollectionUtils.isNotEmpty(grouponCates)) {
            // 拼团分类数量不能超过30
            if (grouponCates.size() >= Constant.MAX_GROUPON_CATE_COUNT) {
                throw new SbcRuntimeException(GrouponErrorCode.GROUPON_CATE_ALREADY_MAX);
            }
            // 拼团名称不能重复，已经存在
            if (grouponCates.stream().anyMatch(cate ->
                    StringUtils.equals(entity.getGrouponCateName(), cate.getGrouponCateName()))) {
                throw new SbcRuntimeException(GrouponErrorCode.GROUPON_CATE_NAME_EXIST);
            }
        }
        entity.setDefaultCate(DefaultFlag.NO);
        entity.setDelFlag(DeleteFlag.NO);
        entity.setCateSort(Constant.MIN_GROUPON_CATE_SORT);
        entity.setCreateTime(LocalDateTime.now());

        grouponCateRepository.save(entity);
    }

    /**
     * 修改拼团分类信息
     *
     * @param entity
     * @return
     */
    @Transactional
    public void modify(GrouponCate entity) {
        if (StringUtils.isBlank(entity.getGrouponCateId())) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        GrouponCate grouponCate = grouponCateRepository.findByGrouponCateIdAndDelFlag(entity.getGrouponCateId(), DeleteFlag.NO);
        if (Objects.isNull(grouponCate)) {
            throw new SbcRuntimeException(GrouponErrorCode.GROUPON_CATE_NOT_EXIST);
        }
        // 除自己外，根据分类名称查询其他拼团分类是否存在
        Integer repeatName = grouponCateRepository.findByNameExceptSelf(entity.getGrouponCateId(), entity.getGrouponCateName());
        if (Objects.nonNull(repeatName) && repeatName > 0) {
            throw new SbcRuntimeException(GrouponErrorCode.GROUPON_CATE_NAME_EXIST);
        }
        KsBeanUtil.copyProperties(entity, grouponCate);
        grouponCate.setUpdateTime(LocalDateTime.now());

        grouponCateRepository.save(grouponCate);
    }

    /**
     * 删除拼团分类信息
     *
     * @author groupon
     */
    @Transactional
    public void deleteById(GrouponCate entity) {
        if (StringUtils.isBlank(entity.getGrouponCateId())) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        GrouponCate grouponCate = grouponCateRepository.findByGrouponCateIdAndDelFlag(entity.getGrouponCateId(), DeleteFlag.NO);
        if (Objects.isNull(grouponCate) || (DefaultFlag.YES.equals(grouponCate.getDefaultCate()))) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        // 封装查询参数
        GrouponActivityQueryRequest queryReq = new GrouponActivityQueryRequest();
        queryReq.setGrouponCateId(entity.getGrouponCateId());
        queryReq.setDelFlag(DeleteFlag.NO);
        // 查询该拼团分类下是否关联了商品
        long result = grouponActivityRepository.count(GrouponActivityWhereCriteriaBuilder.build(queryReq));
        if (Objects.nonNull(result) && result > 0) {
            throw new SbcRuntimeException(GrouponErrorCode.EXIST_GOODS_FORBID_DELETE);
        }

        grouponCate.setDelFlag(DeleteFlag.YES);
        grouponCate.setDelPerson(entity.getDelPerson());
        grouponCate.setDelTime(LocalDateTime.now());

        grouponCateRepository.save(grouponCate);
    }

    /**
     * 拼团分类拖拽排序
     *
     * @param sortRequestList
     */
    @Transactional
    public void dragSort(List<GrouponCateSort> sortRequestList) {
        if (CollectionUtils.isEmpty(sortRequestList) || sortRequestList.size() > Constant.MAX_GROUPON_CATE_COUNT) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        sortRequestList.forEach(cate ->
                grouponCateRepository.updateCateSort(cate.getGrouponCateId(), cate.getCateSort()));
    }

    /**
     * 将实体包装成VO
     *
     * @author groupon
     */
    public GrouponCateVO wrapperVo(GrouponCate grouponCate) {
        if (grouponCate != null) {
            GrouponCateVO grouponCateVO = new GrouponCateVO();
            KsBeanUtil.copyPropertiesThird(grouponCate, grouponCateVO);
            return grouponCateVO;
        }
        return null;
    }

    /**
     * 将实体包装成VO
     *
     * @author groupon
     */
    public GrouponCateSort wrapperSortVo(GrouponCateSortVO grouponCate) {
        if (grouponCate != null) {
            GrouponCateSort grouponCateSort = new GrouponCateSort();
            KsBeanUtil.copyProperties(grouponCate, grouponCateSort);
            return grouponCateSort;
        }
        return null;
    }
}
