package com.wanmi.sbc.marketing.grouponactivity.service;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.handler.aop.MasterRouteOnly;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.goods.api.provider.goods.GoodsQueryProvider;
import com.wanmi.sbc.goods.api.provider.groupongoodsinfo.GrouponGoodsInfoQueryProvider;
import com.wanmi.sbc.goods.api.provider.groupongoodsinfo.GrouponGoodsInfoSaveProvider;
import com.wanmi.sbc.goods.api.provider.info.GoodsInfoQueryProvider;
import com.wanmi.sbc.goods.api.request.goods.GoodsListByIdsRequest;
import com.wanmi.sbc.goods.api.request.groupongoodsinfo.GrouponGoodsByGoodsInfoIdAndTimeRequest;
import com.wanmi.sbc.goods.api.request.groupongoodsinfo.GrouponGoodsInfoBatchAddRequest;
import com.wanmi.sbc.goods.api.request.groupongoodsinfo.GrouponGoodsInfoBatchEditRequest;
import com.wanmi.sbc.goods.api.request.groupongoodsinfo.GrouponGoodsInfoListRequest;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoListByIdsRequest;
import com.wanmi.sbc.goods.bean.dto.GrouponGoodsInfoForAddDTO;
import com.wanmi.sbc.goods.bean.dto.GrouponGoodsInfoForEditDTO;
import com.wanmi.sbc.goods.bean.enums.CheckStatus;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.goods.bean.vo.GoodsVO;
import com.wanmi.sbc.goods.bean.vo.GrouponGoodsInfoVO;
import com.wanmi.sbc.goods.bean.vo.GrouponGoodsVO;
import com.wanmi.sbc.marketing.api.provider.grouponcate.GrouponCateQueryProvider;
import com.wanmi.sbc.marketing.api.request.grouponactivity.GrouponActivityBatchCheckRequest;
import com.wanmi.sbc.marketing.api.request.grouponactivity.GrouponActivityBatchStickyRequest;
import com.wanmi.sbc.marketing.api.request.grouponactivity.GrouponActivityQueryRequest;
import com.wanmi.sbc.marketing.api.request.grouponcate.GrouponCateByIdRequest;
import com.wanmi.sbc.marketing.api.response.grouponactivity.GrouponActivityAddResponse;
import com.wanmi.sbc.marketing.api.response.grouponactivity.GrouponActivityModifyResponse;
import com.wanmi.sbc.marketing.bean.enums.AuditStatus;
import com.wanmi.sbc.marketing.bean.enums.GrouponOrderStatus;
import com.wanmi.sbc.marketing.bean.vo.*;
import com.wanmi.sbc.marketing.grouponactivity.model.entity.GrouponActivityAdd;
import com.wanmi.sbc.marketing.grouponactivity.model.entity.GrouponActivityEdit;
import com.wanmi.sbc.marketing.grouponactivity.model.root.GrouponActivity;
import com.wanmi.sbc.marketing.grouponactivity.repository.GrouponActivityRepository;
import com.wanmi.sbc.marketing.grouponsetting.service.GrouponSettingService;
import com.wanmi.sbc.marketing.util.error.MarketingErrorCode;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>拼团活动信息表业务逻辑</p>
 *
 * @author groupon
 * @date 2019-05-15 14:02:38
 */
@Slf4j
@Service("GrouponActivityService")
public class GrouponActivityService {

    @Autowired
    private GrouponActivityRepository grouponActivityRepository;

    @Autowired
    private GrouponGoodsInfoSaveProvider grouponGoodsInfoSaveProvider;

    @Autowired
    private GrouponGoodsInfoQueryProvider grouponGoodsInfoQueryProvider;

    @Autowired
    private GoodsInfoQueryProvider goodsInfoQueryProvider;

    @Autowired
    private GoodsQueryProvider goodsQueryProvider;

    @Autowired
    private GrouponSettingService grouponSettingService;

    @Autowired
    private GrouponCateQueryProvider grouponCateQueryProvider;

    /**
     * 新增拼团活动信息表
     *
     * @return 拼团活动信息列表
     */
    @Transactional
    @GlobalTransactional
    public GrouponActivityAddResponse add(GrouponActivityAdd entity) {

        // 1.校验
        // 1.1.传入的单品列表，必须真实有效
        GrouponActivity activity = entity.getGrouponActivity();
        List<String> goodsInfoIds = entity.getGoodsInfos().stream()
                .map(item -> item.getGoodsInfoId()).distinct().collect(Collectors.toList());
        GoodsInfoListByIdsRequest goodsInfoListByIdsRequest = new GoodsInfoListByIdsRequest();
        goodsInfoListByIdsRequest.setGoodsInfoIds(goodsInfoIds);
        List<GoodsInfoVO> goodsInfos = goodsInfoQueryProvider.listByIds(goodsInfoListByIdsRequest).getContext().getGoodsInfos();

        if (goodsInfos.size() != goodsInfoIds.size()) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }

        // 1.2.验证商品上下架、禁售状态
        goodsInfos.forEach(item -> {
            if (!NumberUtils.INTEGER_ONE.equals(item.getAddedFlag())
                    || !CheckStatus.CHECKED.equals(item.getAuditStatus())) {
                throw new SbcRuntimeException(MarketingErrorCode.GROUPON_GOODS_ALREADY_DISABLED);
            }
        });

        // 1.3.传入的单品列表，必须没有关联正在进行的活动
        List<String> goodsIds = goodsInfos.stream().map(GoodsInfoVO::getGoodsId).distinct().collect(Collectors.toList());
        List<GrouponGoodsInfoVO> grouponGoodsInfoVOList = grouponGoodsInfoQueryProvider.listActivitying(GrouponGoodsByGoodsInfoIdAndTimeRequest.builder()
                .goodsInfoIds(goodsInfoIds).startTime(activity.getStartTime()).endTime(activity.getEndTime()).build())
                .getContext().getGrouponGoodsInfoVOList();
        if (CollectionUtils.isNotEmpty(grouponGoodsInfoVOList)) {
            throw new SbcRuntimeException(MarketingErrorCode.GROUPON_GOODS_ALREADY_INUSE);
        }

        // 1.4.校验拼团活动分类
        GrouponCateVO grouponCateVO = grouponCateQueryProvider.getById(
                new GrouponCateByIdRequest(activity.getGrouponCateId())).getContext().getGrouponCateVO();
        if (Objects.isNull(grouponCateVO)) {
            throw new SbcRuntimeException(MarketingErrorCode.GROUPON_CATE_NOT_EXIST);
        }

        // 2.保存拼团活动列表
        List<GrouponActivity> newActivitys = new ArrayList<>();
        LocalDateTime nowTime = LocalDateTime.now();
        activity.setCreateTime(nowTime);
        activity.setUpdateTime(nowTime);
        if (DefaultFlag.YES.equals(grouponSettingService.getGoodsAuditFlag())) {
            activity.setAuditStatus(AuditStatus.WAIT_CHECK);
        } else {
            activity.setAuditStatus(AuditStatus.CHECKED);
        }

        List<GoodsVO> goodsList = goodsQueryProvider.listByIds(new GoodsListByIdsRequest(goodsIds)).getContext().getGoodsVOList();

        // 有几个spu就插入几个活动
        goodsIds.forEach(goodsId -> {
            GrouponActivity newActivity = KsBeanUtil.convert(activity, GrouponActivity.class);
            // 这边取第一个sku的storeId和商品名称
            GoodsVO goods = goodsList.stream().filter(i -> i.getGoodsId().equals(goodsId)).findFirst().get();
            newActivity.setGoodsId(goodsId);
            newActivity.setGoodsName(goods.getGoodsName());
            newActivity.setGoodsNo(goods.getGoodsNo());
            newActivity.setStoreId(goods.getStoreId().toString());
            newActivitys.add(newActivity);
        });
        List<GrouponActivity> activityResults = grouponActivityRepository.saveAll(newActivitys);

        List<EsGrouponActivityVO> newList = KsBeanUtil.convert(activityResults, EsGrouponActivityVO.class);

        // 3.保存拼团活动商品列表添加拼团活动
        List<GrouponGoodsInfoForAddDTO> newGoodsInfos = goodsInfos.stream().map(goodsInfo -> {
            GrouponGoodsInfoForAddDTO newGoodsInfo = entity.getGoodsInfos().stream().filter(
                    item -> goodsInfo.getGoodsInfoId().equals(item.getGoodsInfoId())).findFirst().get();
            GrouponActivity activityResult = activityResults.stream().filter(
                    item -> goodsInfo.getGoodsId().equals(item.getGoodsId())).findFirst().get();
            newGoodsInfo.setGoodsId(goodsInfo.getGoodsId());
            newGoodsInfo.setStoreId(goodsInfo.getStoreId().toString());
            newGoodsInfo.setGrouponCateId(activityResult.getGrouponCateId());
            newGoodsInfo.setGrouponActivityId(activityResult.getGrouponActivityId());
            newGoodsInfo.setAuditStatus(
                    com.wanmi.sbc.goods.bean.enums.AuditStatus.fromValue(new Integer(activity.getAuditStatus().toValue())));
            newGoodsInfo.setStartTime(activityResult.getStartTime());
            newGoodsInfo.setEndTime(activityResult.getEndTime());
            return newGoodsInfo;
        }).collect(Collectors.toList());
        grouponGoodsInfoSaveProvider.batchAdd(new GrouponGoodsInfoBatchAddRequest(newGoodsInfos));

        // 5.返回结果，供记录日志用
        List<String> stringList = activityResults.stream().map(
                result -> result.getGoodsName() + result.getGrouponNum() + "人团").collect(Collectors.toList());

        return new GrouponActivityAddResponse(stringList, newList);
    }

    /**
     * 修改拼团活动信息表
     *
     * @return 拼团活动信息
     */
    @Transactional
    @GlobalTransactional
    public GrouponActivityModifyResponse edit(GrouponActivityEdit entity) {
        GrouponActivity activityParam = entity.getGrouponActivity();
        GrouponActivity activity = grouponActivityRepository.findById(activityParam.getGrouponActivityId())
                .orElseThrow(() -> new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR));
        // 校验修改后的活动时间会不会导致商品冲突
        List<GrouponGoodsInfoVO> grouponGoodsInfoVOList = grouponGoodsInfoQueryProvider
                .list(GrouponGoodsInfoListRequest.builder().grouponActivityId(activityParam.getGrouponActivityId()).build())
                .getContext().getGrouponGoodsInfoVOList();
        List<String> goodsInfoIds = grouponGoodsInfoVOList.stream().map(GrouponGoodsInfoVO::getGoodsInfoId).collect(Collectors.toList());
        List<GrouponGoodsInfoVO> clashGrouponGoodsInfoVOList = grouponGoodsInfoQueryProvider.listActivitying(GrouponGoodsByGoodsInfoIdAndTimeRequest.builder()
                .goodsInfoIds(goodsInfoIds).startTime(activityParam.getStartTime()).endTime(activityParam.getEndTime()).build())
                .getContext().getGrouponGoodsInfoVOList();
        List<GrouponGoodsInfoVO> clashList = clashGrouponGoodsInfoVOList.stream()
                .filter(v -> !activityParam.getGrouponActivityId().equals(v.getGrouponActivityId())).collect(Collectors.toList());
        //存在商品冲突的拼团活动
        if (CollectionUtils.isNotEmpty(clashList)) {
            throw new SbcRuntimeException(MarketingErrorCode.GROUPON_GOODS_ALREADY_INUSE);
        }

        // 1.保存拼团活动信息
        activity.setGrouponNum(activityParam.getGrouponNum());
        activity.setStartTime(activityParam.getStartTime());
        activity.setEndTime(activityParam.getEndTime());
        activity.setGrouponCateId(activityParam.getGrouponCateId());
        activity.setAutoGroupon(activityParam.isAutoGroupon());
        activity.setFreeDelivery(activityParam.isFreeDelivery());
        if (DefaultFlag.YES.equals(grouponSettingService.getGoodsAuditFlag())) {
            activity.setAuditStatus(AuditStatus.WAIT_CHECK);
        } else {
            activity.setAuditStatus(AuditStatus.CHECKED);
        }
        GrouponActivity grouponActivity = grouponActivityRepository.save(activity);

        EsGrouponActivityVO grouponActivityVO = new EsGrouponActivityVO();
        BeanUtils.copyProperties(grouponActivity, grouponActivityVO);

        // 2.保存拼团活动商品
        List<GrouponGoodsInfoForEditDTO> goodsInfos = entity.getGoodsInfos();
        goodsInfos.forEach(item -> {
            item.setGrouponCateId(activity.getGrouponCateId());
            item.setStartTime(activity.getStartTime());
            item.setEndTime(activity.getEndTime());
            item.setAuditStatus(
                    com.wanmi.sbc.goods.bean.enums.AuditStatus.fromValue(new Integer(activity.getAuditStatus().toValue())));
        });
        grouponGoodsInfoSaveProvider.batchEdit(new GrouponGoodsInfoBatchEditRequest(goodsInfos));

        String result = activity.getGoodsName() + activity.getGrouponNum() + "人团";

        return new GrouponActivityModifyResponse(result, grouponActivityVO);
    }

    /**
     * 根据商品ids，查询正在进行的活动的商品ids(时间段)
     */
    public List<String> listActivityingSpuIds(List<String> goodsIds, LocalDateTime startTime, LocalDateTime endTime) {
        return this.listActivitying(
                goodsIds, startTime, endTime).stream().map(GrouponActivity::getGoodsId).collect(Collectors.toList());
    }

    /**
     * 根据商品ids，查询正在进行的活动的商品ids(当前时间)
     */
    public List<String> listActivityingSpuIds(List<String> goodsIds) {
        return grouponActivityRepository.listActivityingSpuIds(goodsIds);
    }

    /**
     * 单个删除拼团活动信息表
     *
     * @author groupon
     */
    @Transactional
    public void deleteById(String id) {
        grouponActivityRepository.deleteById(id);
    }

    /**
     * 批量删除拼团活动信息表
     *
     * @author groupon
     */
    @Transactional
    public void deleteByIdList(List<String> ids) {
        grouponActivityRepository.deleteAll(ids.stream().map(id -> {
            GrouponActivity entity = new GrouponActivity();
            entity.setGrouponActivityId(id);
            return entity;
        }).collect(Collectors.toList()));
    }

    /**
     * 单个查询拼团活动信息表
     *
     * @author groupon
     */
    public GrouponActivity getById(String id) {
        Optional<GrouponActivity> byId = grouponActivityRepository.findById(id);
        return byId.orElse(null);
    }

    /**
     * 根据活动id，查询活动是否包邮
     */
    public boolean getFreeDeliveryById(String id) {
        GrouponActivity activity = grouponActivityRepository.findById(id).get();
        return activity.isFreeDelivery();
    }

    /**
     * 分页查询拼团活动信息表
     *
     * @author groupon
     */
    public Page<GrouponActivity> page(GrouponActivityQueryRequest queryReq) {
        return grouponActivityRepository.findAll(
                GrouponActivityWhereCriteriaBuilder.build(queryReq),
                queryReq.getPageRequest());
    }

    /**
     * 列表查询拼团活动信息表
     *
     * @author groupon
     */
    public List<GrouponActivity> list(GrouponActivityQueryRequest queryReq) {
        return grouponActivityRepository.findAll(GrouponActivityWhereCriteriaBuilder.build(queryReq));
    }

    /**
     * 将实体包装成VO
     *
     * @author groupon
     */
    public GrouponActivityVO wrapperVo(GrouponActivity grouponActivity) {
        if (grouponActivity != null) {
            GrouponActivityVO grouponActivityVO = new GrouponActivityVO();
            KsBeanUtil.copyPropertiesThird(grouponActivity, grouponActivityVO);
            return grouponActivityVO;
        }
        return null;
    }

    /**
     * 将实体包装成VO
     *
     * @author groupon
     */
    public GrouponActivityForManagerVO wrapperMangerVo(GrouponActivity grouponActivity) {
        if (grouponActivity != null) {
            GrouponActivityForManagerVO grouponActivityVO = new GrouponActivityForManagerVO();
            KsBeanUtil.copyPropertiesThird(grouponActivity, grouponActivityVO);
            return grouponActivityVO;
        }
        return null;
    }

    /**
     * 批量审核拼团活动
     *
     * @param request
     * @return
     */
    @Transactional
    public BaseResponse batchCheckMarketing(@RequestBody @Valid GrouponActivityBatchCheckRequest request) {
        grouponActivityRepository.batchCheckMarketing(request.getGrouponActivityIdList());
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 驳回或禁止拼团活动
     *
     * @param grouponActivityId
     * @param auditReason
     */
    @Transactional(rollbackFor = Exception.class)
    public void refuseCheckMarketing(String grouponActivityId, AuditStatus auditStatus,
                                     String auditReason) {
        int checkResult = grouponActivityRepository.refuseCheckMarketing(grouponActivityId, auditStatus, auditReason);
        if (0 >= checkResult) {
            throw new SbcRuntimeException(CommonErrorCode.FAILED);
        }
    }


    /**
     * 批量修改拼团活动精选状态
     *
     * @param request
     * @return
     */
    @Transactional
    public BaseResponse batchStickyMarketing(@RequestBody @Valid GrouponActivityBatchStickyRequest request) {
        grouponActivityRepository.batchStickyMarketing(request.getGrouponActivityIdList(), request.getSticky());
        return BaseResponse.SUCCESSFUL();
    }


    /**
     * 根据商品ids，查询正在进行的活动
     *
     * @param goodsInfoIds
     * @return
     */
    public Map<String, GrouponGoodsInfoVO> listActivityingWithGoodsInfo(List<String> goodsInfoIds) {
        if (CollectionUtils.isEmpty(goodsInfoIds)) {
            return new HashMap<>();
        }
        //查询正在进行中的ids
        GrouponGoodsInfoListRequest grouponGoodsInfoListReq = GrouponGoodsInfoListRequest.builder()
                .goodsInfoIdList(goodsInfoIds).started(Boolean.TRUE).build();
        List<GrouponGoodsInfoVO> grouponGoodsInfoVOList = grouponGoodsInfoQueryProvider.list(grouponGoodsInfoListReq)
                .getContext().getGrouponGoodsInfoVOList();
        if (CollectionUtils.isEmpty(grouponGoodsInfoVOList)) {
            return new HashMap<>();
        }
        //参与拼团活动sku
        Map<String, GrouponGoodsInfoVO> goodsInfoMap = grouponGoodsInfoVOList.stream().collect(Collectors.toMap(GrouponGoodsInfoVO::getGoodsInfoId, c -> c));
        return goodsInfoMap;
    }

    /**
     * 将拼团商品vo包装成拼团中心需要的数据VO
     *
     * @author groupon
     */
    public GrouponCenterVO wrapperGrouponCenterVo(GrouponGoodsVO grouponGoodsVO) {
        if (grouponGoodsVO != null) {
            GrouponCenterVO grouponCenterVO = new GrouponCenterVO();
            KsBeanUtil.copyProperties(grouponGoodsVO, grouponCenterVO);
            return grouponCenterVO;
        }
        return null;
    }

    /**
     * 根据不同拼团状态更新不同的统计数据（已成团、待成团、团失败人数）
     *
     * @param grouponActivityId
     * @param grouponNum
     * @param grouponOrderStatus
     * @return
     */
    @Transactional
    public int updateStatisticsNumByGrouponActivityId(String grouponActivityId, Integer grouponNum, GrouponOrderStatus grouponOrderStatus) {
        if (GrouponOrderStatus.WAIT == grouponOrderStatus) {
            return grouponActivityRepository.updateWaitGrouponNumByGrouponActivityId(grouponActivityId, grouponNum, LocalDateTime.now());
        } else if (GrouponOrderStatus.COMPLETE == grouponOrderStatus) {
            return grouponActivityRepository.updateAlreadyGrouponNumByGrouponActivityId(grouponActivityId, grouponNum, LocalDateTime.now());
        } else if (GrouponOrderStatus.FAIL == grouponOrderStatus) {
            return grouponActivityRepository.updateFailGrouponNumByGrouponActivityId(grouponActivityId, grouponNum, LocalDateTime.now());
        }
        return NumberUtils.INTEGER_ZERO;
    }

    /**
     * 去重查询正在进行中的活动
     *
     * @author groupon
     */
    public List<GrouponActivity> listByUniqueSupplier(GrouponActivityQueryRequest queryReq) {
        return grouponActivityRepository.findAll(GrouponActivityWhereCriteriaBuilder.build(queryReq));
    }


    public int querySupplierNum(AuditStatus status) {
        return grouponActivityRepository.querySupplierNum(status);
    }

    /**
     * 更新待成团人数
     *
     * @param grouponActivityId 活动id
     * @param num               增加数（若要减数，传负值）
     */
    @Transactional
    public void updateWaitGrouponNumByGrouponActivityId(String grouponActivityId, Integer num) {
        grouponActivityRepository.updateWaitGrouponNumByGrouponActivityId(grouponActivityId, num, LocalDateTime.now());
    }

    /**
     * 根据商品ids，查询正在进行的活动(时间段)
     *
     * @param goodsIds  商品ids
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return
     */
    @MasterRouteOnly
    public List<GrouponActivity> listActivitying(List<String> goodsIds, LocalDateTime startTime, LocalDateTime endTime) {
        return grouponActivityRepository.listActivitying(goodsIds, startTime, endTime);
    }

}
