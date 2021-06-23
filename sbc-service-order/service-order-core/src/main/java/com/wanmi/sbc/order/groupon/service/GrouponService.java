package com.wanmi.sbc.order.groupon.service;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.SortType;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.common.util.SensitiveUtils;
import com.wanmi.sbc.customer.api.provider.quicklogin.ThirdLoginRelationQueryProvider;
import com.wanmi.sbc.customer.api.request.customer.CustomerIdsListRequest;
import com.wanmi.sbc.customer.api.response.customer.CustomerIdsListResponse;
import com.wanmi.sbc.customer.bean.vo.CustomerDetailWithImgVO;
import com.wanmi.sbc.goods.api.provider.groupongoodsinfo.GrouponGoodsInfoQueryProvider;
import com.wanmi.sbc.goods.api.request.groupongoodsinfo.GrouponGoodsInfoListRequest;
import com.wanmi.sbc.goods.bean.enums.GoodsStatus;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.goods.bean.vo.GrouponGoodsInfoVO;
import com.wanmi.sbc.marketing.api.provider.grouponactivity.GrouponActivityQueryProvider;
import com.wanmi.sbc.marketing.api.provider.grouponrecord.GrouponRecordQueryProvider;
import com.wanmi.sbc.marketing.api.request.grouponactivity.GrouponActivityByIdRequest;
import com.wanmi.sbc.marketing.api.request.grouponactivity.GrouponActivityListRequest;
import com.wanmi.sbc.marketing.api.request.grouponrecord.GrouponRecordListRequest;
import com.wanmi.sbc.marketing.bean.constant.GrouponErrorCode;
import com.wanmi.sbc.marketing.bean.enums.GrouponDetailOptStatus;
import com.wanmi.sbc.marketing.bean.enums.GrouponDetailOptType;
import com.wanmi.sbc.marketing.bean.enums.GrouponOrderStatus;
import com.wanmi.sbc.marketing.bean.enums.GrouponTabTypeStatus;
import com.wanmi.sbc.marketing.bean.vo.GrouponActivityVO;
import com.wanmi.sbc.marketing.bean.vo.GrouponRecordVO;
import com.wanmi.sbc.marketing.bean.vo.TradeGrouponVO;
import com.wanmi.sbc.order.api.response.groupon.GrouponDetailQueryResponse;
import com.wanmi.sbc.order.api.response.groupon.GrouponDetailWithGoodsResponse;
import com.wanmi.sbc.order.bean.dto.GrouponDetailDTO;
import com.wanmi.sbc.order.bean.enums.PayState;
import com.wanmi.sbc.order.bean.vo.GrouponDetailVO;
import com.wanmi.sbc.order.bean.vo.GrouponInstanceWithCustomerInfoVO;
import com.wanmi.sbc.order.bean.vo.TradeVO;
import com.wanmi.sbc.order.trade.model.entity.TradeState;
import com.wanmi.sbc.order.trade.model.root.GrouponInstance;
import com.wanmi.sbc.order.trade.model.root.Trade;
import com.wanmi.sbc.order.trade.model.root.TradeGroupon;
import com.wanmi.sbc.order.trade.request.GrouponInstanceQueryRequest;
import com.wanmi.sbc.order.trade.request.TradeQueryRequest;
import com.wanmi.sbc.order.trade.service.GrouponInstanceService;
import com.wanmi.sbc.order.trade.service.TradeService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Description: C端拼团活动
 * @Autho qiaokang
 * @Date：2019-03-05 16:41:48
 */
@Service
@Transactional
public class GrouponService {

    @Autowired
    private TradeService tradeService;

    @Autowired
    private GrouponActivityQueryProvider grouponActivityQueryProvider;

    @Autowired
    private GrouponGoodsInfoQueryProvider grouponGoodsInfoQueryProvider;


    @Autowired
    private GrouponRecordQueryProvider grouponRecordQueryProvider;

    @Autowired
    private GrouponInstanceService grouponInstanceService;


    @Autowired
    private ThirdLoginRelationQueryProvider thirdLoginRelationQueryProvider;

    @Autowired
    private GrouponOrderService grouponOrderService;

    /**
     * 商品信息处理拼团逻辑
     *
     * @param grouponDetail
     * @return
     */
    public GrouponDetailWithGoodsResponse getGrouponDetailWithGoodsInfo(GrouponDetailDTO grouponDetail) {
        //拼团业务信息（spu维度）
        //设置起订量限定量（sku维度）
        setGrouponGoodsInfo(grouponDetail);
        GrouponDetailWithGoodsResponse res = GrouponDetailWithGoodsResponse.builder().goodsInfoVOList(grouponDetail
                .getGoodsInfoList())
                .build();
        return res;
    }

    /**
     * 同活动
     * 正在进行中的俩条记录
     *
     * @return
     */
    public List<GrouponInstanceWithCustomerInfoVO> grouponInstanceQuery(String grouponActivityId) {
        //最新开团
        GrouponInstanceQueryRequest grouponInstanceQueryRequest = new GrouponInstanceQueryRequest();
        grouponInstanceQueryRequest.setGrouponStatus(GrouponOrderStatus.WAIT);
        grouponInstanceQueryRequest.setGrouponActivityId(grouponActivityId);
        grouponInstanceQueryRequest.setPageNum(0);
        grouponInstanceQueryRequest.setPageSize(2);
        grouponInstanceQueryRequest.setSortColumn("createTime");
        grouponInstanceQueryRequest.setSortRole(SortType.DESC.toValue());
        //最新开团团实例
        Page<GrouponInstance> page = grouponInstanceService.page(grouponInstanceQueryRequest.getWhereCriteria(),
                grouponInstanceQueryRequest);
        List<GrouponInstance> grouponInstanceList = page.getContent();
        List<GrouponInstanceWithCustomerInfoVO> grouponInstanceWithCustomerInfos = KsBeanUtil.copyListProperties
                (grouponInstanceList, GrouponInstanceWithCustomerInfoVO.class);
        if (CollectionUtils.isNotEmpty(grouponInstanceList)) {
            //获取用户信息-头像、昵称
            CustomerIdsListRequest customerIdsListRequest = new CustomerIdsListRequest();
            List<String> customerIds = page.getContent().stream().map(GrouponInstance::getCustomerId).collect
                    (Collectors.toList());
            customerIdsListRequest.setCustomerIds(customerIds);
            BaseResponse<CustomerIdsListResponse> listByCustomerIds = thirdLoginRelationQueryProvider
                    .listWithImgByCustomerIds(customerIdsListRequest);
            List<CustomerDetailWithImgVO> customerVOList = listByCustomerIds.getContext().getCustomerVOList();
            grouponInstanceWithCustomerInfos.forEach(vo -> {
                CustomerDetailWithImgVO customerDetailWithImgVO = customerVOList.stream().filter(ivo -> ivo
                        .getCustomerId().equals(vo.getCustomerId())).findFirst().orElse(new CustomerDetailWithImgVO());
                //头像
                vo.setHeadimgurl(customerDetailWithImgVO.getHeadimgurl());
                vo.setCustomerName(SensitiveUtils.handlerMobilePhone(customerDetailWithImgVO.getCustomerName()));
            });
        }
        return grouponInstanceWithCustomerInfos;
    }


    public void setGrouponGoodsInfo(GrouponDetailDTO grouponDetail) {
        List<GoodsInfoVO> goods = grouponDetail.getGoodsInfoList();
        String customerId = grouponDetail.getCustomerId();
        GrouponActivityVO grouponActivityVO = grouponDetail.getGrouponActivity();
        //拼团活动商品
        List<GrouponGoodsInfoVO> grouponGoodsInfoVOList = grouponDetail.getGrouponGoodsInfoVOList();
        if (CollectionUtils.isNotEmpty(goods)) {
            //批量查询限定量
            GrouponRecordListRequest grouponRecordListRequest = GrouponRecordListRequest.builder().customerId
                    (customerId)
                    .grouponActivityId
                            (grouponActivityVO.getGrouponActivityId()).goodsId(goods.get(0).getGoodsId())
                    .build();
            List<GrouponRecordVO> grouponRecordVOList = grouponRecordQueryProvider.list(grouponRecordListRequest)
                    .getContext().getGrouponRecordVOList();

            goods.forEach(goodsInfoVO -> {
                GrouponGoodsInfoVO grouponGoodsInfo = grouponGoodsInfoVOList.stream().filter(grouponGoodsInfoVO ->
                        Objects.equals(grouponGoodsInfoVO.getGoodsInfoId(), goodsInfoVO.getGoodsInfoId())).findFirst()
                        .orElse(new GrouponGoodsInfoVO());
                //处理起订量限定量count maxCount
                goodsInfoVO.setCount(null == grouponGoodsInfo.getStartSellingNum() ? 1 : grouponGoodsInfo
                        .getStartSellingNum().longValue());
                goodsInfoVO.setMaxCount(null == grouponGoodsInfo.getLimitSellingNum() ? null : grouponGoodsInfo
                        .getLimitSellingNum().longValue());
                //无货或库存低于起订量
                if (goodsInfoVO.getStock() < 1 || (goodsInfoVO.getCount() != null && goodsInfoVO.getCount() >
                        goodsInfoVO.getStock())) {
                    //无效
                    goodsInfoVO.setValidFlag(Constants.no);
                    goodsInfoVO.setGoodsStatus(GoodsStatus.OUT_STOCK);
                }
                //拼团价
                goodsInfoVO.setGrouponPrice(grouponGoodsInfo.getGrouponPrice());
                //用户登录-且限购
                if (Objects.nonNull(goodsInfoVO.getMaxCount()) && Objects.nonNull(customerId)) {
                    GrouponRecordVO grouponRecordVO = grouponRecordVOList.stream().filter(grouponRecord ->
                            Objects.equals(grouponRecord.getGoodsInfoId(), goodsInfoVO.getGoodsInfoId())).findFirst()
                            .orElse(null);
                    if (Objects.nonNull(grouponRecordVO) && Objects.nonNull(grouponRecordVO.getLimitSellingNum())) {
                        if (grouponRecordVO.getLimitSellingNum()==null||grouponRecordVO.getLimitSellingNum()==0){
                            goodsInfoVO.setMaxCount(null);
                        }else {
                            int canBuy = grouponRecordVO.getLimitSellingNum() - grouponRecordVO.getBuyNum();
                            goodsInfoVO.setMaxCount(Long.valueOf(canBuy>0?canBuy:0));
                        }
                    }

                }
            });
        }

    }


    /**
     * 获取拼团详细信息
     *
     * @param grouponDetail
     * @return
     */
    public GrouponDetailQueryResponse getGrouponDetail(GrouponDetailDTO grouponDetail) {
        GrouponDetailQueryResponse res = GrouponDetailQueryResponse.builder().grouponDetail(new GrouponDetailVO())
                .build();

        //商品详情页-是否有拼团活动
        if (GrouponDetailOptType.GOODS_DETAIL.equals(grouponDetail.getOptType())) {
            setActivityInfo4GoodsDetail(grouponDetail, res);
        }
        //拼团商品详情页-是否有拼团活动、团长订单（spu）
        if (GrouponDetailOptType.GROUPON_GOODS_DETAIL.equals(grouponDetail.getOptType())) {
            setActivityInfo4GrouponGoodsDetail(grouponDetail, res);
        }
        //参团商品详情页-是否有拼团活动、团长订单、同团已支付订单
        if (GrouponDetailOptType.GROUPON_JOIN.equals(grouponDetail.getOptType())) {
            setActivityInfo4GrouponJoin(grouponDetail, res);
        }

        return res;
    }


    /**
     * 商品详情页-是否有拼团活动
     *
     * @param grouponDetail
     * @param grouponDetailQueryResponse
     */
    private void setActivityInfo4GoodsDetail(GrouponDetailDTO grouponDetail, GrouponDetailQueryResponse
            grouponDetailQueryResponse) {
        GrouponActivityVO grouponActivityVO = getGrouponActivityByGoodsInfoId(grouponDetail.getGoodsInfoId());
        if (Objects.nonNull(grouponActivityVO)) {
            //填充活动信息
            grouponDetailQueryResponse.getGrouponDetail().setGrouponActivity(grouponActivityVO);
        }

    }

    /**
     * 拼团商品详情页-是否有拼团活动、团长订单
     *
     * @param grouponDetail
     * @param grouponDetailQueryResponse
     */
    private void setActivityInfo4GrouponGoodsDetail(GrouponDetailDTO grouponDetail, GrouponDetailQueryResponse
            grouponDetailQueryResponse) {
        GrouponActivityVO grouponActivityVO = getGrouponActivityByGoodsId(grouponDetail.getGoodsId());
        if (Objects.isNull(grouponActivityVO)) {
            throw new SbcRuntimeException(GrouponErrorCode.MARKETING_GROUPON_GOODS_NON_EXIST);
        }
        if (Objects.nonNull(grouponActivityVO)) {
            GrouponDetailVO grouponDetailVO = grouponDetailQueryResponse.getGrouponDetail();
            //填充活动信息
            grouponDetailVO.setGrouponActivity(grouponActivityVO);
            //设置活动
            grouponDetailVO.setGrouponGoodsInfos(getGrouponGoodsInfoIdByActivityId(grouponActivityVO
                    .getGrouponActivityId()));
            //开团-正在进行中的该spu的开团订单
            if (Objects.nonNull(grouponDetail.getCustomerId())) {
                TradeGroupon tradeGroupon = getTradeInActivity(grouponDetail
                        .getGoodsId(), grouponDetail.getCustomerId(), grouponActivityVO.getGrouponActivityId());
                if (Objects.nonNull(tradeGroupon)) {
                    //填充团长开团信息
                    grouponDetailVO.setTradeGroupon(TradeGrouponVO.builder().grouponNo
                            (tradeGroupon.getGrouponNo()).build());

                }
            }
            grouponDetailVO.setGrouponDetailOptStatus(grouponDetailQueryResponse.getGrouponDetail()
                    .getGrouponDetailOptStatus());
            grouponDetailVO.setGrouponInstanceList(grouponInstanceQuery(grouponDetailVO.getGrouponActivity()
                    .getGrouponActivityId()));
        }
        setValidateInfoInfo4GoodsDetail(grouponDetailQueryResponse);

    }

    /**
     * 参团商品详情页-是否有拼团活动、团长订单、同团已支付订单
     *
     * @param grouponDetail
     * @param grouponDetailQueryResponse
     */
    private void setActivityInfo4GrouponJoin(GrouponDetailDTO grouponDetail, GrouponDetailQueryResponse
            grouponDetailQueryResponse) {
        //根据团编号反查同团订单
        TradeQueryRequest request = TradeQueryRequest.builder().grouponFlag(Boolean.TRUE)
                .tradeState(TradeState.builder().payState(PayState.PAID).build())
                .tradeGroupon(TradeGrouponVO.builder().grouponNo(grouponDetail.getGrouponNo()).build())
                .build();
        List<Trade> tradeList = tradeService.getTradeList(request.getWhereCriteria());
        // 排除拼团失败的数据
        tradeList=tradeList.stream().filter(trade ->!Objects.equals(GrouponOrderStatus.FAIL,trade.getTradeGroupon().getGrouponOrderStatus()) ).collect(Collectors.toList());;
        if (CollectionUtils.isNotEmpty(tradeList)) {
            GrouponDetailVO grouponDetailVO = grouponDetailQueryResponse.getGrouponDetail();
            GrouponActivityVO grouponActivityVO = grouponActivityQueryProvider.getById
                    (GrouponActivityByIdRequest.builder()
                            .grouponActivityId(tradeList.get(0).getTradeGroupon().getGrouponActivityId()).build()
                    ).getContext()
                    .getGrouponActivity();
            if (Objects.isNull(grouponActivityVO)) {
                throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
            }
            //查询团实例
            GrouponInstance grouponInstance = grouponInstanceService.detailByGrouponNo(grouponDetail.getGrouponNo());
            if (Objects.isNull(grouponInstance)) {
                throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
            }

            //填充活动信息
            grouponDetailVO.setGrouponActivity(grouponActivityVO);
            //设置活动skus
            grouponDetailVO.setGrouponGoodsInfos(getGrouponGoodsInfoIdByActivityId(grouponActivityVO
                    .getGrouponActivityId()));
            Trade leaderTrade = tradeList.stream().filter(trade -> trade.getTradeGroupon().getLeader())
                    .findFirst().orElse(null);
            //设置团长订单-不限成团状态
            grouponDetailVO.setTradeInGroupon(KsBeanUtil.convert
                    (leaderTrade, TradeVO.class));
            //同团订单-已支付
            grouponDetailVO.setTradeList(KsBeanUtil.convert(tradeList, TradeVO
                    .class));
            //同团订单头像信息
            List<String> customerIds = tradeList.stream().map(trade -> trade.getBuyer().getId()).collect(Collectors
                    .toList());
            grouponDetailVO.setCustomerVOList(getGrouponCustomerInfos(customerIds, grouponActivityVO.getGrouponNum(),
                    grouponInstance
                            .getGrouponStatus()));

            grouponActivityVO.setEndTime(grouponInstance.getEndTime());
            //差几人成团
            grouponActivityVO.setWaitGrouponNum(grouponActivityVO.getGrouponNum() - customerIds.size());
            //同spu推荐参团
            grouponDetailVO.setGrouponInstanceList(grouponInstanceQuery(grouponDetailVO.getGrouponActivity()
                    .getGrouponActivityId()));
        }

        setValidateInfoInfo4GrouponJoin(grouponDetail, grouponDetailQueryResponse);
    }

    /**
     * 同团参与用户信息
     * 用户名、头像
     * 已完成拼团自动补足
     *
     * @return
     */
    private List<CustomerDetailWithImgVO> getGrouponCustomerInfos(List<String> customerIds, Integer grouponNum,
                                                                  GrouponOrderStatus grouponStatus) {
        //获取用户信息-头像、昵称
        CustomerIdsListRequest customerIdsListRequest = new CustomerIdsListRequest();
        customerIdsListRequest.setCustomerIds(customerIds);
        BaseResponse<CustomerIdsListResponse> listByCustomerIds = thirdLoginRelationQueryProvider
                .listWithImgByCustomerIds(customerIdsListRequest);
        List<CustomerDetailWithImgVO> customerVOList = listByCustomerIds.getContext().getCustomerVOList();
        //自动补足
        if (Objects.equals(grouponStatus, GrouponOrderStatus.COMPLETE)) {
            if (grouponNum.compareTo(customerIds.size()) > 0) {
                for (int i = 0; i < grouponNum - customerIds.size(); i++) {
                    customerVOList.add(new CustomerDetailWithImgVO());
                }
            }
        }
        return customerVOList;
    }

    /**
     * 根据grouponNo查询拼团活动信息
     *
     * @param grouponNo
     * @return
     */
    public GrouponActivityVO getGrouponActivityByGrouponNo(String grouponNo) {
        //根据团编号反查同团订单
        TradeQueryRequest request = TradeQueryRequest.builder().grouponFlag(Boolean.TRUE)
                .tradeState(TradeState.builder().payState(PayState.PAID).build())
                .tradeGroupon(TradeGrouponVO.builder().grouponNo(grouponNo).build())
                .build();
        List<Trade> tradeList = tradeService.getTradeList(request.getWhereCriteria());
        if (CollectionUtils.isNotEmpty(tradeList)) {
            GrouponActivityVO grouponActivityVO = grouponActivityQueryProvider.getById
                    (GrouponActivityByIdRequest.builder()
                            .grouponActivityId(tradeList.get(0).getTradeGroupon().getGrouponActivityId()).build()
                    ).getContext()
                    .getGrouponActivity();
            return grouponActivityVO;
        }
        return null;
    }

    /**
     * 根据spu查询活动中的团详情
     *
     * @param goodsInfoId
     * @return
     */
    public GrouponActivityVO getGrouponActivityByGoodsInfoId(String goodsInfoId) {
        //查询正在进行中的ids
        GrouponGoodsInfoListRequest grouponGoodsInfoListReq = GrouponGoodsInfoListRequest.builder()
                .goodsInfoIdList(Collections.singletonList(goodsInfoId)).started(Boolean.TRUE).build();
        List<GrouponGoodsInfoVO> grouponGoodsInfoVOList = grouponGoodsInfoQueryProvider.list(grouponGoodsInfoListReq)
                .getContext().getGrouponGoodsInfoVOList();

        if (CollectionUtils.isNotEmpty(grouponGoodsInfoVOList)) {
            GrouponActivityVO grouponActivityVO = grouponActivityQueryProvider.getById
                    (GrouponActivityByIdRequest.builder()
                            .grouponActivityId(grouponGoodsInfoVOList.get(0)
                                    .getGrouponActivityId()).build()).getContext().getGrouponActivity();
            return grouponActivityVO;
        }
        return null;
    }

    /**
     * 根据spuId查询活动中的团详情
     *
     * @param goodsId
     * @return
     */
    public GrouponActivityVO getGrouponActivityByGoodsId(String goodsId) {

        List<GrouponActivityVO> grouponActivityVOList = grouponActivityQueryProvider.list
                (GrouponActivityListRequest.builder()
                        .spuIdList(Collections.singletonList(goodsId)).tabType(GrouponTabTypeStatus.STARTED)
                        .build()).getContext().getGrouponActivityVOList();
        if (CollectionUtils.isNotEmpty(grouponActivityVOList)) {
            return grouponActivityVOList.get(0);
        }
        return null;
    }


    private TradeGroupon getTradeInActivity(String goodsId, String customerId, String grouponActivityId) {
        //检查用户是否有正在进行中的该spu的开团订单
        TradeQueryRequest request = TradeQueryRequest.builder().grouponFlag(Boolean.TRUE).buyerId
                (customerId)
                .tradeState(TradeState.builder().payState(PayState.PAID).build())
                .tradeGroupon(TradeGrouponVO.builder().leader(Boolean.TRUE).grouponOrderStatus
                        (GrouponOrderStatus.WAIT).goodId(goodsId)
                        .grouponActivityId(grouponActivityId)
                        .build())
                .build();
        List<Trade> tradeList = tradeService.getTradeList(request.getWhereCriteria());
        if (CollectionUtils.isNotEmpty(tradeList)) {
            return KsBeanUtil.copyPropertiesThird(tradeList.get(0).getTradeGroupon(), TradeGroupon.class);
        }
        return null;
    }


    public List<GrouponGoodsInfoVO> getGrouponGoodsInfoIdByActivityId(String grouponActivityId) {
        GrouponGoodsInfoListRequest grouponGoodsInfoListReq = GrouponGoodsInfoListRequest.builder()
                .grouponActivityId(grouponActivityId).build();
        List<GrouponGoodsInfoVO> grouponGoodsInfoVOList = grouponGoodsInfoQueryProvider.list(grouponGoodsInfoListReq)
                .getContext().getGrouponGoodsInfoVOList();
        return grouponGoodsInfoVOList;
    }

    /**
     * 商品详情页拼团业务验证信息
     *
     * @param grouponDetailQueryResponse
     */
    private void setValidateInfoInfo4GoodsDetail(GrouponDetailQueryResponse grouponDetailQueryResponse) {
        GrouponDetailVO grouponDetail = grouponDetailQueryResponse.getGrouponDetail();
        //判断活动状态
        GrouponActivityVO grouponActivityVO = grouponDetail.getGrouponActivity();
        //活动不存在
        if (Objects.isNull(grouponActivityVO)) {
            grouponDetail.setGrouponDetailOptStatus(GrouponDetailOptStatus.ACTIVITY_END);
            return;
        }
        if (!(LocalDateTime.now().isAfter(grouponActivityVO.getStartTime()) && LocalDateTime.now().isBefore
                (grouponActivityVO.getEndTime()))) {
            grouponDetail.setGrouponDetailOptStatus(GrouponDetailOptStatus.ACTIVITY_END);
            return;
        }
        //开团-查看团详情
        if (Objects.nonNull(grouponDetail.getTradeGroupon())) {
            grouponDetail.setGrouponDetailOptStatus(GrouponDetailOptStatus.LEADER_OPEN_EXIT);
            return;
        }
        //开团-可以开团
        if (Objects.isNull(grouponDetail.getTradeGroupon())) {
            grouponDetail.setGrouponDetailOptStatus(GrouponDetailOptStatus.LEADER_OPEN_ABLE);
            return;
        }
    }

    /**
     * 参团详情页拼团业务验证信息
     *
     * @param grouponDetailDTO
     * @param grouponDetailQueryResponse
     */
    private void setValidateInfoInfo4GrouponJoin(GrouponDetailDTO grouponDetailDTO, GrouponDetailQueryResponse
            grouponDetailQueryResponse) {

        GrouponDetailVO grouponDetail = grouponDetailQueryResponse.getGrouponDetail();

        //验证团长订单状态
        TradeVO tradeInGroupon = grouponDetail.getTradeInGroupon();
        if (Objects.isNull(tradeInGroupon.getTradeGroupon())) {
            grouponDetail.setGrouponDetailOptStatus(GrouponDetailOptStatus.GROUPON_PARAMS_ERROR);
            return;
        }
        if (Objects.nonNull(tradeInGroupon.getTradeGroupon())) {
            GrouponOrderStatus grouponOrderStatus = tradeInGroupon.getTradeGroupon().getGrouponOrderStatus();
            //我要参团
            if (Objects.equals(grouponOrderStatus, GrouponOrderStatus.WAIT)) {
                grouponDetail.setGrouponDetailOptStatus(GrouponDetailOptStatus.JOIN_GROUPON_JOIN_ABLE);

            }
            //团已结束（失败）、看看其他团购
            if (Objects.equals(grouponOrderStatus, GrouponOrderStatus.FAIL)) {
                grouponDetail.setGrouponDetailOptStatus(GrouponDetailOptStatus.JOIN_GROUPON_FAILED_OPEN_OTHER);

            }
            //团已结束、看看其他团购
            if (Objects.equals(grouponOrderStatus, GrouponOrderStatus.COMPLETE)) {
                grouponDetail.setGrouponDetailOptStatus(GrouponDetailOptStatus.JOIN_GROUPON_FINISH_OPEN_OTHER);
            }

            if (Objects.nonNull(grouponDetailDTO.getCustomerId())) {
                //验证自己是否已参团
                List<TradeVO> tradeList = grouponDetail.getTradeList();
                Boolean join = tradeList.stream().anyMatch(tradeVO -> Objects.equals(grouponDetailDTO.getCustomerId(),
                        tradeVO.getBuyer().getId()));
                if (join) {
                    //邀请参团
                    if (Objects.equals(grouponOrderStatus, GrouponOrderStatus.WAIT)) {
                        grouponDetail.setGrouponDetailOptStatus(GrouponDetailOptStatus.JOIN_GROUPON_JOINED_AND_SHARE);
                    }
                    //看看其他团购
                    if (Objects.equals(grouponOrderStatus, GrouponOrderStatus.COMPLETE)) {
                        grouponDetail.setGrouponDetailOptStatus(GrouponDetailOptStatus.JOIN_GROUPON_JOINED_AND_SUCCESS);
                    }
                }
            }
        }

    }


}
