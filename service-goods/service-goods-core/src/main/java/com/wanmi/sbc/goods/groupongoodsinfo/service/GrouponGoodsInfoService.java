package com.wanmi.sbc.goods.groupongoodsinfo.service;

import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.goods.api.request.groupongoodsinfo.GrouponGoodsInfoBatchByActivityIdAndGoodsIdRequest;
import com.wanmi.sbc.goods.api.request.groupongoodsinfo.GrouponGoodsInfoQueryRequest;
import com.wanmi.sbc.goods.bean.dto.GrouponGoodsInfoByActivityIdAndGoodsIdDTO;
import com.wanmi.sbc.goods.bean.enums.AuditStatus;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.goods.bean.vo.GrouponGoodsInfoByActivityIdAndGoodsIdVO;
import com.wanmi.sbc.goods.bean.vo.GrouponGoodsInfoVO;
import com.wanmi.sbc.goods.bean.vo.GrouponGoodsVO;
import com.wanmi.sbc.goods.groupongoodsinfo.model.root.GrouponGoodsInfo;
import com.wanmi.sbc.goods.groupongoodsinfo.repository.GrouponGoodsInfoRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>拼团活动商品信息表业务逻辑</p>
 *
 * @author groupon
 * @date 2019-05-15 14:49:12
 */
@Service("GrouponGoodsInfoService")
public class GrouponGoodsInfoService {
    @Autowired
    private GrouponGoodsInfoRepository grouponGoodsInfoRepository;

    @Autowired
    private EntityManager entityManager;

    /**
     * 新增拼团活动商品信息表
     *
     * @author groupon
     */
    @Transactional
    public void batchAdd(List<GrouponGoodsInfo> items) {
        LocalDateTime nowTime = LocalDateTime.now();
        items.forEach(item -> {
            item.setCreateTime(nowTime);
            item.setUpdateTime(nowTime);
            item.setSticky(Boolean.FALSE);
        });
        grouponGoodsInfoRepository.saveAll(items);
    }

    /**
     * 修改拼团活动商品信息表
     *
     * @author groupon
     */
    @Transactional
    public void batchEdit(List<GrouponGoodsInfo> items) {
        // 1.查询拼团商品，并校验
        List<String> ids = items.stream().map(GrouponGoodsInfo::getGrouponGoodsId).collect(Collectors.toList());
        List<GrouponGoodsInfo> goodsInfos = grouponGoodsInfoRepository.findAllById(ids);
        if (goodsInfos.size() != ids.size()) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }

        // 2.修改拼团商品，并保存
        goodsInfos.forEach(goodsInfo -> {
            GrouponGoodsInfo item = items.stream().filter(i ->
                    i.getGrouponGoodsId().equals(goodsInfo.getGrouponGoodsId())).findFirst().get();
            goodsInfo.setGrouponPrice(item.getGrouponPrice());
            goodsInfo.setStartSellingNum(item.getStartSellingNum());
            goodsInfo.setLimitSellingNum(item.getLimitSellingNum());
            goodsInfo.setGrouponCateId(item.getGrouponCateId());
            goodsInfo.setAuditStatus(item.getAuditStatus());
            goodsInfo.setStartTime(item.getStartTime());
            goodsInfo.setEndTime(item.getEndTime());
            item.setUpdateTime(LocalDateTime.now());
        });

        grouponGoodsInfoRepository.saveAll(goodsInfos);
    }

    /**
     * 单个查询拼团活动商品信息表
     *
     * @author groupon
     */
    public GrouponGoodsInfo getById(String id) {
        return grouponGoodsInfoRepository.findById(id).orElse(null);
    }

    /**
     * 分页查询拼团活动商品信息表
     *
     * @author groupon
     */
    public Page<GrouponGoodsInfo> page(GrouponGoodsInfoQueryRequest queryReq) {
        return grouponGoodsInfoRepository.findAll(
                GrouponGoodsInfoWhereCriteriaBuilder.build(queryReq),
                queryReq.getPageRequest());
    }

    /**
     * 列表查询拼团活动商品信息表
     *
     * @author groupon
     */
    public List<GrouponGoodsInfo> list(GrouponGoodsInfoQueryRequest queryReq) {
        return grouponGoodsInfoRepository.findAll(GrouponGoodsInfoWhereCriteriaBuilder.build(queryReq));
    }

    /**
     * 根据拼团活动ID、SPU编号查询拼团价格最小的拼团SKU信息（单个查询）
     *
     * @param grouponActivityId
     * @param goodsId
     * @return
     */
    public GrouponGoodsInfo findTop1ByGrouponActivityIdAndGoodsId(String grouponActivityId, String goodsId) {
        return grouponGoodsInfoRepository.findTop1ByGrouponActivityIdAndGoodsIdOrderByGrouponPriceAsc(grouponActivityId, goodsId);
    }

    /**
     * 根据拼团活动ID、SPU编号查询最低拼团价格的拼团信息（批量查询）
     *
     * @param request
     * @return
     */
    public List<GrouponGoodsInfoByActivityIdAndGoodsIdVO> batchByGrouponActivityIdAndGoodsId(GrouponGoodsInfoBatchByActivityIdAndGoodsIdRequest request) {
        List<GrouponGoodsInfoByActivityIdAndGoodsIdDTO> list = request.getList();
        GrouponGoodsInfo grouponGoodsInfo;
        List<GrouponGoodsInfoByActivityIdAndGoodsIdVO> result = new ArrayList<>(list.size());
        for (GrouponGoodsInfoByActivityIdAndGoodsIdDTO dto : list) {
            grouponGoodsInfo = grouponGoodsInfoRepository.findTop1ByGrouponActivityIdAndGoodsIdOrderByGrouponPriceAsc(dto.getGrouponActivityId(), dto.getGoodsId());
            if (Objects.nonNull(grouponGoodsInfo)) {
                result.add(GrouponGoodsInfoByActivityIdAndGoodsIdVO.builder().grouponActivityId(grouponGoodsInfo.getGrouponActivityId())
                        .goodsId(grouponGoodsInfo.getGoodsId())
                        .grouponPrice(grouponGoodsInfo.getGrouponPrice())
                        .goosInfoId(grouponGoodsInfo.getGoodsInfoId())
                        .build());
            }
        }
        return result;
    }

    /**
     * 根据拼团活动ID删除拼团活动商品
     *
     * @param grouponActivityId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Integer deleteByGrouponActivityId(String grouponActivityId) {
        return grouponGoodsInfoRepository.deleteByGrouponActivityId(grouponActivityId);
    }

    /**
     * 分页查询拼团活动spu列表
     * @param request
     * @return
     */
    public Page<GrouponGoodsVO> pageGrouponGoods(GrouponGoodsWhereCriteriaBuilder request) {

        Query query = entityManager.createNativeQuery(request.getQuerySql().concat(request.getQueryConditionSql()).concat(request.getQuerySort()));
        if (StringUtils.isNoneBlank(request.getGoodsName())) {
            query.setParameter("goodsName", request.getGoodsName());
        }
        // 拼团分类
        if (StringUtils.isNoneBlank(request.getGrouponCateId())) {
            query.setParameter("grouponCateId", request.getGrouponCateId());
        }
        query.setFirstResult(request.getPageNum() * request.getPageSize());
        query.setMaxResults(request.getPageSize());
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        // 查询拼团活动spu列表
        List<GrouponGoodsVO> grouponGoodsVOS = GrouponGoodsWhereCriteriaBuilder.converter(query.getResultList());

        //查询拼团活动spu列表总数
        Query totalCountRes =
                entityManager.createNativeQuery(request.getQueryTotalCountSql().concat(request.getQueryConditionSql()).concat(request.getQueryTotalTemp()));
        if (StringUtils.isNoneBlank(request.getGoodsName())) {
            totalCountRes.setParameter("goodsName", request.getGoodsName());
        }
        // 拼团分类
        if (StringUtils.isNoneBlank(request.getGrouponCateId())) {
            totalCountRes.setParameter("grouponCateId", request.getGrouponCateId());
        }
        long totalCount = Long.parseLong(totalCountRes.getSingleResult().toString());

        return new PageImpl<>(grouponGoodsVOS, request.getPageable(), totalCount);
    }

    /**
     * 分页查询拼团活动spu列表
     * @param request
     * @return
     */
    public Page<GrouponGoodsVO> pageGrouponGoodsInfo(GrouponGoodsInfoSqlCriteriaBuilder request) {

        Query query = entityManager.createNativeQuery(request.getQuerySql().concat(request.getQueryConditionSql()).concat(request.getQuerySort()));
        //组装查询参数
        this.wrapperQueryParam(query, request);
        query.setFirstResult(request.getPageNum() * request.getPageSize());
        query.setMaxResults(request.getPageSize());
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        // 查询拼团活动spu列表
        List<GrouponGoodsVO> grouponGoodsVOS = GrouponGoodsInfoSqlCriteriaBuilder.converter(query.getResultList());

        //查询拼团活动spu列表总数
        Query totalCountRes =
                entityManager.createNativeQuery(request.getQueryTotalCountSql().concat(request.getQueryConditionSql()).concat(request.getQueryTotalTemp()));
        //组装查询参数
        wrapperQueryParam(totalCountRes, request);
        long totalCount = Long.parseLong(totalCountRes.getSingleResult().toString());

        return new PageImpl<>(grouponGoodsVOS, request.getPageable(), totalCount);
    }

    /**
     * 将实体包装成VO
     *
     * @author groupon
     */
    public GrouponGoodsInfoVO wrapperVo(GrouponGoodsInfo grouponGoodsInfo) {
        if (grouponGoodsInfo != null) {
            GrouponGoodsInfoVO grouponGoodsInfoVO = new GrouponGoodsInfoVO();
            KsBeanUtil.copyPropertiesThird(grouponGoodsInfo, grouponGoodsInfoVO);
            if (Objects.nonNull(grouponGoodsInfo.getGoodsInfo())){
                GoodsInfoVO vo = new GoodsInfoVO();
                KsBeanUtil.copyPropertiesThird(grouponGoodsInfo.getGoodsInfo(), vo);
                grouponGoodsInfoVO.setGoodsInfo(vo);
            }
            return grouponGoodsInfoVO;
        }
        return null;
    }

	/**
	 * 根据活动ID、SKU编号更新已成团人数
	 * @param grouponActivityId
	 * @param goodsInfoIds
	 * @param alreadyGrouponNum
	 * @return
	 */
	@Transactional(rollbackFor = Exception.class)
	public int updateAlreadyGrouponNumByGrouponActivityIdAndGoodsInfoId(final String grouponActivityId, List<String> goodsInfoIds, final Integer alreadyGrouponNum){
        goodsInfoIds.stream().forEach(goodsInfoId ->
            grouponGoodsInfoRepository.updateAlreadyGrouponNumByGrouponActivityIdAndGoodsInfoId(grouponActivityId,goodsInfoId,alreadyGrouponNum,LocalDateTime.now())
        );
        return goodsInfoIds.size();
	}

    /**
     * 根据活动ID、SKU编号更新商品销售量、订单量、交易额
     * @param grouponActivityId
     * @param goodsInfoId
     * @param goodsSalesNum
     * @param orderSalesNum
     * @param tradeAmount
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public int updateOrderPayStatisticNumByGrouponActivityIdAndGoodsInfoId(String grouponActivityId, String goodsInfoId, Integer goodsSalesNum,
                                                            Integer orderSalesNum, BigDecimal tradeAmount){
        return grouponGoodsInfoRepository.updateOrderPayStatisticNumByGrouponActivityIdAndGoodsInfoId(grouponActivityId,goodsInfoId,goodsSalesNum,orderSalesNum,tradeAmount,LocalDateTime.now());
    }

    /**
     * 根据活动ID、SKU编号更新成团后退单数量、成团后退单金额
     * @param grouponActivityId
     * @param goodsInfoId
     * @param refundNum
     * @param refundAmount
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public int updateReturnOrderStatisticNumByGrouponActivityIdAndGoodsInfoId(String grouponActivityId, String goodsInfoId,
                                                                       Integer refundNum, BigDecimal refundAmount){
        return grouponGoodsInfoRepository.updateReturnOrderStatisticNumByGrouponActivityIdAndGoodsInfoId(grouponActivityId,goodsInfoId,refundNum,refundAmount,LocalDateTime.now());
    }

    /**
     * 根据活动ID批量更新审核状态
     * @param grouponActivityIds
     * @param auditStatus
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public int updateAuditStatusByGrouponActivityIds(List<String> grouponActivityIds, AuditStatus auditStatus){
        return grouponGoodsInfoRepository.updateAuditStatusByGrouponActivityIds(grouponActivityIds,auditStatus,LocalDateTime.now());
    }

    /**
     * 根据活动ID批量更新是否精选
     * @param grouponActivityIds
     * @param sticky
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public int updateStickyByGrouponActivityIds(List<String> grouponActivityIds, Boolean sticky){
        return grouponGoodsInfoRepository.updateStickyByGrouponActivityIds(grouponActivityIds,sticky,LocalDateTime.now());
    }

    /**
     * 根据活动ID、SKU编号查询拼团商品信息
     * @param grouponActivityId
     * @param goodsInfoId
     * @return
     */
    public GrouponGoodsInfo findByGrouponActivityIdAndGoodsInfoId(String grouponActivityId, String goodsInfoId){
        return grouponGoodsInfoRepository.findByGrouponActivityIdAndGoodsInfoId(grouponActivityId, goodsInfoId);
    }

    /**
     * 根据skuIds查询商品是否处于活动中
     * @return
     */
    public List<GrouponGoodsInfo> listActivitying(List<String> goodsInfoIds, LocalDateTime startTime, LocalDateTime endTime){
        return grouponGoodsInfoRepository.listActivitying(goodsInfoIds, startTime, endTime);
    }

    /**
     * 组装查询参数
     *
     * @param query
     * @param request
     */
    private void wrapperQueryParam(Query query, GrouponGoodsInfoSqlCriteriaBuilder request) {
        if(CollectionUtils.isNotEmpty(request.getGoodsInfoIds())){
            query.setParameter("goodsInfoIds", request.getGoodsInfoIds());
        }

        // 商品名称模糊查询
        if (StringUtils.isNoneBlank(request.getGoodsName())) {
            query.setParameter("goodsName", request.getGoodsName());
        }
        // 拼团分类
        if (StringUtils.isNoneBlank(request.getGrouponCateId())) {
            query.setParameter("grouponCateId", request.getGrouponCateId());
        }
    }
}
