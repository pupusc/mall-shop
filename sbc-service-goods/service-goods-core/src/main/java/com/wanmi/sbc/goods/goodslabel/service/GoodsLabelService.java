package com.wanmi.sbc.goods.goodslabel.service;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.goods.api.constant.GoodsLabelErrorCode;
import com.wanmi.sbc.goods.api.request.goodslabel.GoodsLabelModifyVisibleRequest;
import com.wanmi.sbc.goods.api.request.goodslabel.GoodsLabelQueryRequest;
import com.wanmi.sbc.goods.api.request.goodslabel.GoodsLabelSortRequest;
import com.wanmi.sbc.goods.api.response.goodslabel.GoodsLabelModifySortResponse;
import com.wanmi.sbc.goods.bean.vo.GoodsLabelVO;
import com.wanmi.sbc.goods.bean.vo.GoodsVO;
import com.wanmi.sbc.goods.goodslabel.model.root.GoodsLabel;
import com.wanmi.sbc.goods.goodslabel.repository.GoodsLabelRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>商品标签业务逻辑</p>
 *
 * @author dyt
 * @date 2020-09-29 13:57:19
 */
@Service("GoodsLabelService")
public class GoodsLabelService {

    public static final Integer LABEL_MAX_LENGTH = 20;

    @Autowired
    private GoodsLabelRepository goodsLabelRepository;

    /**
     * 新增商品标签
     */
    @CacheEvict(value = "GOODS_LABEL_LIST",allEntries = true)
    @Transactional
    public GoodsLabel add(GoodsLabel entity) {
        // 查询已有标签
        List<GoodsLabel> existLabel = this.list(GoodsLabelQueryRequest.builder().delFlag(DeleteFlag.NO)
                .storeId(entity.getStoreId()).build());
        List<String> labelNameList = existLabel.stream().map(GoodsLabel::getLabelName).collect(Collectors.toList());
        // 不允许创建相同名称的标签
        if (labelNameList.contains(entity.getLabelName())) {
            throw new SbcRuntimeException(GoodsLabelErrorCode.LABEL_NAME_ALREADY_EXIST);
        }
        // 标签限制数量20个
        if (existLabel.size() == LABEL_MAX_LENGTH) {
            throw new SbcRuntimeException(GoodsLabelErrorCode.LABEL_MAX_LENGTH_LIMIT);
        }
        entity.setLabelName(StringUtils.trim(entity.getLabelName()));
        entity.setLabelVisible(true);
        entity.setLabelSort(0);
        entity.setUpdateTime(LocalDateTime.now());
        goodsLabelRepository.save(entity);
        return entity;
    }

    /**
     * 修改商品标签
     */
    @CacheEvict(value = "GOODS_LABEL_LIST",allEntries = true)
    @Transactional
    public GoodsLabel modify(GoodsLabel entity) {
        List<GoodsLabel> list = this.list(GoodsLabelQueryRequest.builder().delFlag(DeleteFlag.NO)
                .storeId(entity.getStoreId()).build());
        GoodsLabel old;
        if (CollectionUtils.isEmpty(list)) {
            // 商品标签不存在
            throw new SbcRuntimeException(GoodsLabelErrorCode.LABEL_NOT_EXIST);
        } else {
            // 获取原始标签
            old = list.stream().filter(label -> Objects.equals(entity.getGoodsLabelId(), label.getGoodsLabelId())).findAny().orElse(null);
            if (Objects.isNull(old)) {
                throw new SbcRuntimeException(GoodsLabelErrorCode.LABEL_NOT_EXIST);
            }
        }
        // 不允许创建相同名称的标签
        if (list.stream().filter(label -> !Objects.equals(entity.getGoodsLabelId(), label.getGoodsLabelId()))
                .anyMatch(label -> Objects.equals(entity.getLabelName(), label.getLabelName()))) {
            throw new SbcRuntimeException(GoodsLabelErrorCode.LABEL_NAME_ALREADY_EXIST);
        }
        old.setUpdateTime(LocalDateTime.now());
        old.setLabelName(StringUtils.trim(entity.getLabelName()));
        old.setLabelVisible(entity.getLabelVisible());
        old = goodsLabelRepository.saveAndFlush(old);
        return old;
    }

    /**
     * 修改商品标签
     */
    @CacheEvict(value = "GOODS_LABEL_LIST",allEntries = true)
    @Transactional
    public void modifyVisible(GoodsLabelModifyVisibleRequest request) {
        List<GoodsLabel> list = this.list(GoodsLabelQueryRequest.builder().delFlag(DeleteFlag.NO)
                .goodsLabelId(request.getGoodsLabelId())
                .storeId(request.getStoreId()).build());
        // 商品标签不存在
        if (CollectionUtils.isEmpty(list)) {
            throw new SbcRuntimeException(GoodsLabelErrorCode.LABEL_NOT_EXIST);
        }
        GoodsLabel old = list.get(0);
        old.setLabelVisible(request.getLabelVisible());
        goodsLabelRepository.saveAndFlush(old);
    }

    /**
     * 拖拽排序
     *
     * @param request
     */
    @CacheEvict(value = "GOODS_LABEL_LIST",allEntries = true)
    @Transactional
    public GoodsLabelModifySortResponse editSort(GoodsLabelSortRequest request) {
        List<Long> labelIdList = request.getLabelIdList();
        List<GoodsLabel> existLabel = this.list(GoodsLabelQueryRequest.builder().delFlag(DeleteFlag.NO)
                .storeId(request.getStoreId()).build());
        if (CollectionUtils.isEmpty(existLabel)) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        List<GoodsLabel> newList = new ArrayList<>();
        for (int i = 0; i < labelIdList.size(); i++) {
            Long labelId = labelIdList.get(i);
            GoodsLabel goodsLabel = existLabel.stream().filter(label -> Objects.equals(label.getGoodsLabelId(),
                    labelId)).findAny().orElseThrow(() -> new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR));
            goodsLabel.setLabelSort(i + 1);
            newList.add(goodsLabel);
        }
        newList = goodsLabelRepository.saveAll(newList);
        return GoodsLabelModifySortResponse.builder().list(KsBeanUtil.convert(newList, GoodsLabelVO.class)).build();
    }

    /**
     * 单个删除商品标签
     */
    @CacheEvict(value = "GOODS_LABEL_LIST",allEntries = true)
    @Transactional
    public void deleteById(Long id, Long storeId) {
        // 删除标签
        goodsLabelRepository.deleteById(id, storeId);
    }

    /**
     * 批量删除商品标签
     */
    @CacheEvict(value = "GOODS_LABEL_LIST",allEntries = true)
    @Transactional
    public void deleteByIdList(List<Long> ids, Long storeId) {
        goodsLabelRepository.deleteByIdList(ids, storeId);
    }

    /**
     * 单个查询商品标签
     */
    public GoodsLabel getById(Long id) {
        return goodsLabelRepository.findById(id).orElse(null);
    }

    /**
     * 分页查询商品标签
     */
    public Page<GoodsLabel> page(GoodsLabelQueryRequest queryReq) {
        return goodsLabelRepository.findAll(
                GoodsLabelWhereCriteriaBuilder.build(queryReq),
                queryReq.getPageRequest());
    }

    /**
     * 列表查询商品标签
     */
    public List<GoodsLabel> list(GoodsLabelQueryRequest queryReq) {
        return goodsLabelRepository.findAll(
                GoodsLabelWhereCriteriaBuilder.build(queryReq),
                queryReq.getSort());
    }

    /**
     * 缓存级列表查询
     * @return
     */
    @Cacheable(value = "GOODS_LABEL_LIST")
    public List<GoodsLabel> listWithCache() {
        GoodsLabelQueryRequest request = GoodsLabelQueryRequest.builder()
                .delFlag(DeleteFlag.NO).build();
        request.putSort("labelSort", "asc");
        request.putSort("goodsLabelId", "desc");
        return this.list(request);
    }


    /**
     * 填充商品标签Label
     * @param goodsList 商品列表
     * @param visibleFlag 是否显示
     */
    public void fillGoodsLabel(List<GoodsVO> goodsList, Boolean visibleFlag) {
        //如果商品为空，或商品没有相关标签的
        if (CollectionUtils.isEmpty(goodsList) || goodsList.stream().noneMatch(g -> StringUtils.isNotBlank(g.getLabelIdStr()))) {
            return;
        }

        List<GoodsLabel> labels = this.listWithCache();
        if (CollectionUtils.isEmpty(labels)) {
            return;
        }

        //过滤前端展示
        if (Boolean.TRUE.equals(visibleFlag)) {
            labels = labels.stream().filter(l -> Boolean.TRUE.equals(l.getLabelVisible())).collect(Collectors.toList());
        }

        final List<GoodsLabelVO> finalLabel = labels.stream().map(this::wrapperVo).collect(Collectors.toList());

        //填充标签List
        goodsList.stream().filter(g -> StringUtils.isNotBlank(g.getLabelIdStr()))
                .forEach(g -> {
                    String[] labelIds = g.getLabelIdStr().split(",");
                    g.setGoodsLabelList(finalLabel.stream()
                            .filter(i -> ArrayUtils.contains(labelIds, String.valueOf(i.getGoodsLabelId())))
                            .collect(Collectors.toList()));
                });
    }

    /**
     * 将实体包装成VO
     */
    public GoodsLabelVO wrapperVo(GoodsLabel goodsLabel) {
        if (goodsLabel != null) {
            GoodsLabelVO goodsLabelVO = new GoodsLabelVO();
            KsBeanUtil.copyPropertiesThird(goodsLabel, goodsLabelVO);
            return goodsLabelVO;
        }
        return null;
    }
}
