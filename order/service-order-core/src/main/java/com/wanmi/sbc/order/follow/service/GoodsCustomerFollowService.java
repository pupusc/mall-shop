package com.wanmi.sbc.order.follow.service;

import com.aliyuncs.linkedmall.model.v20180116.QueryItemInventoryResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.enums.ThirdPlatformType;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.common.util.SiteResultCode;
import com.wanmi.sbc.goods.api.provider.info.GoodsInfoProvider;
import com.wanmi.sbc.goods.api.provider.info.GoodsInfoQueryProvider;
import com.wanmi.sbc.goods.api.request.goods.GoodsModifyCollectNumRequest;
import com.wanmi.sbc.goods.api.request.info.*;
import com.wanmi.sbc.goods.api.response.info.GoodsInfoByIdResponse;
import com.wanmi.sbc.goods.api.response.info.GoodsInfoViewByIdsResponse;
import com.wanmi.sbc.goods.bean.dto.GoodsInfoDTO;
import com.wanmi.sbc.goods.bean.enums.GoodsStatus;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.goods.bean.vo.GoodsVO;
import com.wanmi.sbc.linkedmall.api.provider.stock.LinkedMallStockQueryProvider;
import com.wanmi.sbc.linkedmall.api.request.stock.GoodsStockGetRequest;
import com.wanmi.sbc.order.common.GoodsCollectNumMq;
import com.wanmi.sbc.order.enums.FollowFlag;
import com.wanmi.sbc.order.follow.model.root.GoodsCustomerFollow;
import com.wanmi.sbc.order.follow.reponse.GoodsCustomerFollowResponse;
import com.wanmi.sbc.order.follow.repository.GoodsCustomerFollowRepository;
import com.wanmi.sbc.order.follow.request.GoodsCustomerFollowQueryRequest;
import com.wanmi.sbc.order.follow.request.GoodsCustomerFollowRequest;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 商品收藏服务
 * Created by daiyitian on 2017/4/11.
 */
@Service
@Transactional(readOnly = true, timeout = 10)
public class GoodsCustomerFollowService {

    private static final String GOODSFOLLOW = "follow";

    private static final String CAMCELGOODSFOLLOW = "cancel";

    @Autowired
    private GoodsCustomerFollowRepository goodsCustomerFollowRepository;

    @Autowired
    private GoodsInfoProvider goodsInfoProvider;

    @Autowired
    private GoodsInfoQueryProvider goodsInfoQueryProvider;

    @Autowired
    private GoodsCollectNumMq goodsCollectNumMq;

    @Autowired
    private LinkedMallStockQueryProvider linkedMallStockQueryProvider;

    /**
     * 新增商品收藏
     *
     * @param request 参数
     */
    @Transactional
    public void save(GoodsCustomerFollowRequest request) {
        GoodsInfoVO goodsInfo = goodsInfoQueryProvider.getById(
                GoodsInfoByIdRequest.builder().goodsInfoId(request.getGoodsInfoId()).build()
        ).getContext();
        if (goodsInfo == null) {
            throw new SbcRuntimeException(SiteResultCode.ERROR_000009);
        }

        //限制个数
        GoodsCustomerFollowQueryRequest queryRequest = GoodsCustomerFollowQueryRequest.builder()
                .customerId(request.getCustomerId())
                .build();
        Long count = goodsCustomerFollowRepository.count(queryRequest.getWhereCriteria());
        if (count >= Constants.FOLLOW_MAX_SIZE) {
            throw new SbcRuntimeException(SiteResultCode.ERROR_030401, new Object[]{Constants.FOLLOW_MAX_SIZE});
        }

        //查询是否存在
        queryRequest.setFollowFlag(null);
        queryRequest.setGoodsInfoId(request.getGoodsInfoId());
        List<GoodsCustomerFollow> followList = goodsCustomerFollowRepository.findAll(queryRequest.getWhereCriteria());
        //如果不存在新增
        if (CollectionUtils.isEmpty(followList)) {
            GoodsCustomerFollow follow = new GoodsCustomerFollow();
            follow.setCompanyInfoId(goodsInfo.getCompanyInfoId());
            follow.setGoodsInfoId(request.getGoodsInfoId());
            follow.setCustomerId(request.getCustomerId());
            follow.setGoodsId(goodsInfo.getGoodsId());
            follow.setFollowTime(LocalDateTime.now());
            follow.setCreateTime(follow.getFollowTime());
            follow.setGoodsNum(1L);
            follow.setFollowFlag(FollowFlag.FOLLOW);
            follow.setStoreId(goodsInfo.getStoreId());
            follow.setCateTopId(goodsInfo.getCateTopId());
            follow.setCateId(goodsInfo.getCateId());
            follow.setBrandId(goodsInfo.getBrandId());
            follow.setTerminalSource(request.getTerminalSource());
            goodsCustomerFollowRepository.save(follow);
            //更新商品收藏
            updateGoodsCollectNum(request.getGoodsInfoId(), GOODSFOLLOW);
        }
    }

    /**
     * 商品收藏
     *
     * @param queryRequest 参数
     * @return 商品收藏列表结果
     * @throws SbcRuntimeException
     */
    public GoodsCustomerFollowResponse list(GoodsCustomerFollowQueryRequest queryRequest) throws SbcRuntimeException {
        //分页查询SKU信息列表
        Page<GoodsCustomerFollow> follows = goodsCustomerFollowRepository.findAll(queryRequest.getWhereCriteria(), queryRequest.getPageRequest());
        if (CollectionUtils.isEmpty(follows.getContent())) {
            return GoodsCustomerFollowResponse.builder().goodsInfos(new MicroServicePage<>(Collections.emptyList(), queryRequest.getPageRequest(), follows.getTotalElements())).build();
        }

        GoodsInfoViewByIdsRequest goodsInfoRequest = new GoodsInfoViewByIdsRequest();
        goodsInfoRequest.setGoodsInfoIds(follows.getContent().stream().map(GoodsCustomerFollow::getGoodsInfoId).collect(Collectors.toList()));
        goodsInfoRequest.setIsHavSpecText(Constants.yes);//需要显示规格值
        goodsInfoRequest.setShowLabelFlag(true);
        goodsInfoRequest.setShowSiteLabelFlag(true);
        GoodsInfoViewByIdsResponse response = goodsInfoQueryProvider.listViewByIds(goodsInfoRequest).getContext();

        //如果是linkedmall商品，实时查库存
        List<GoodsVO> goodses = response.getGoodses();
        List<Long> itemIds = goodses.stream().filter(v -> ThirdPlatformType.LINKED_MALL.equals(v.getThirdPlatformType())).map(v -> Long.valueOf(v.getThirdPlatformSpuId())).collect(Collectors.toList());
        if (itemIds.size() > 0) {
            List<QueryItemInventoryResponse.Item> stocks = linkedMallStockQueryProvider.batchGoodsStockByDivisionCode(new GoodsStockGetRequest(itemIds, "0", null)).getContext();
            if (stocks != null && stocks.size() > 0) {
                for (GoodsInfoVO goodsInfo : response.getGoodsInfos()) {
                    if (ThirdPlatformType.LINKED_MALL.equals(goodsInfo.getThirdPlatformType())) {

                        for (QueryItemInventoryResponse.Item spuStock : stocks) {
                            Optional<QueryItemInventoryResponse.Item.Sku> stock = spuStock.getSkuList().stream().filter(v -> String.valueOf(spuStock.getItemId()).equals(goodsInfo.getThirdPlatformSpuId()) && String.valueOf(v.getSkuId()).equals(goodsInfo.getThirdPlatformSkuId())).findFirst();
                            if (stock.isPresent()) {
//                            goodsInfo.setStock(stock.get().getInventory().getQuantity());
                                Long quantity = stock.get().getInventory().getQuantity();
                                goodsInfo.setStock(quantity);
                                if (!GoodsStatus.INVALID.equals(goodsInfo.getGoodsStatus())) {
                                    goodsInfo.setGoodsStatus(quantity > 0 ? GoodsStatus.OK : GoodsStatus.OUT_STOCK);
                                }
                            }
                        }
                    }
                }
                for (GoodsVO goods : goodses) {
                    if (ThirdPlatformType.LINKED_MALL.equals(goods.getThirdPlatformType())) {

                        QueryItemInventoryResponse.Item item = stocks.stream()
                                .filter(v -> String.valueOf(v.getItemId()).equals(goods.getThirdPlatformSpuId()))
                                .findFirst().orElse(null);
                        Long spuStock = item.getSkuList().stream()
                                .map(v -> v.getInventory().getQuantity())
                                .reduce(0L, (aLong, aLong2) -> aLong + aLong2);
                        goods.setStock(spuStock);
                    }
                }
            }
        }
        Map<String, GoodsInfoVO> goodsInfoMap = response.getGoodsInfos().stream()
                .collect(Collectors.toMap(GoodsInfoVO::getGoodsInfoId, g -> g));
        List<GoodsInfoVO> goodsInfos = follows.getContent().stream()
                .map(goodsCustomerFollow -> goodsInfoMap.get(goodsCustomerFollow.getGoodsInfoId()))
                .filter(goodsInfoVO -> Objects.nonNull(goodsInfoVO)).collect(Collectors.toList());
        List<GoodsInfoDTO> goodsInfoDTOS = KsBeanUtil.convert(goodsInfos, GoodsInfoDTO.class);
        List<GoodsVO> goodsVOList = response.getGoodses().stream().map(v->{
            if(Objects.isNull(v.getShamSalesNum())){
                v.setShamSalesNum(NumberUtils.LONG_ZERO);
            }
            return v;
        }).collect(Collectors.toList());


        goodsInfos = goodsInfoProvider.providerGoodsStockSync(new ProviderGoodsStockSyncRequest(goodsInfoDTOS)).getContext().getGoodsInfoList();


        Map<String, Long> shamSalesNumMap = goodsVOList.stream().collect(Collectors.toMap(GoodsVO::getGoodsId, GoodsVO::getShamSalesNum));
        goodsInfos = goodsInfos.stream().peek(goodsInfo -> {
            String goodsId = goodsInfo.getGoodsId();
            Long goodsSalesNum = shamSalesNumMap.get(goodsId);
            goodsSalesNum = Objects.nonNull(goodsSalesNum) ? goodsSalesNum : 0L;
            goodsInfo.setGoodsSalesNum(goodsSalesNum);
        }).collect(Collectors.toList());


        return GoodsCustomerFollowResponse.builder()
                .goodses(response.getGoodses())
                .goodsInfos(new MicroServicePage<>(goodsInfos, queryRequest.getPageRequest(), follows.getTotalElements()))
                .build();
    }

    /**
     * 取消商品收藏
     *
     * @param request 参数
     */
    @Transactional
    public void delete(GoodsCustomerFollowRequest request) {
        List<GoodsCustomerFollow> followList = goodsCustomerFollowRepository.findAll(GoodsCustomerFollowQueryRequest.builder()
                .goodsInfoIds(request.getGoodsInfoIds())
                .customerId(request.getCustomerId())
                .build().getWhereCriteria());
        if (CollectionUtils.isNotEmpty(followList)) {
            //物理删除
            List<Long> followIds = followList.stream().map(GoodsCustomerFollow::getFollowId).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(followIds)) {
                goodsCustomerFollowRepository.deleteByFollowIds(followIds, request.getCustomerId());
                updateGoodsCollectNum(request.getGoodsInfoIds().get(0), CAMCELGOODSFOLLOW);
            }
        }
    }

    /**
     * 删除失效商品
     *
     * @param request 参数
     */
    @Transactional
    public void deleteInvalidGoods(GoodsCustomerFollowRequest request) {
        List<GoodsCustomerFollow> followList = goodsCustomerFollowRepository.findAll(GoodsCustomerFollowQueryRequest.builder()
                .customerId(request.getCustomerId())
                .build().getWhereCriteria());
        if (CollectionUtils.isEmpty(followList)) {
            return;
        }

        Map<String, Boolean> infoIds = getInvalidGoods(followList);
        if (MapUtils.isEmpty(infoIds)) {
            return;
        }
        //物理删除
        List<Long> followIds = followList.stream().filter(goodsCustomerFollow -> infoIds.containsKey(goodsCustomerFollow.getGoodsInfoId()))
                .map(GoodsCustomerFollow::getFollowId)
                .collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(followIds)) {
            goodsCustomerFollowRepository.deleteByFollowIds(followIds, request.getCustomerId());
        }
    }

    /**
     * 是否有失效商品
     *
     * @param request 参数
     */
    public boolean haveInvalidGoods(GoodsCustomerFollowRequest request) {
        List<GoodsCustomerFollow> followList = goodsCustomerFollowRepository.findAll(GoodsCustomerFollowQueryRequest.builder()
                .customerId(request.getCustomerId())
                .build().getWhereCriteria());
        return CollectionUtils.isNotEmpty(followList) && MapUtils.isNotEmpty(getInvalidGoods(followList));
    }


    /**
     * 获取失效商品
     *
     * @param followList 收藏数据
     * @return 失效商品Map
     */
    private Map<String, Boolean> getInvalidGoods(List<GoodsCustomerFollow> followList) {
        //提取SKU
        List<String> goodsInfoIds = followList.stream().map(GoodsCustomerFollow::getGoodsInfoId)
                .collect(Collectors.toList());

        List<GoodsInfoVO> goodsInfos = goodsInfoQueryProvider.listByIds(
                GoodsInfoListByIdsRequest.builder().goodsInfoIds(goodsInfoIds).build()
        ).getContext().getGoodsInfos();
        if (CollectionUtils.isEmpty(goodsInfos)) {
            return null;
        }
        //填充失效状态
        goodsInfos = goodsInfoProvider.fillGoodsStatus(
                GoodsInfoFillGoodsStatusRequest.builder()
                        .goodsInfos(KsBeanUtil.convertList(goodsInfos, GoodsInfoDTO.class))
                        .build()
        ).getContext().getGoodsInfos();
        //填装Map
        return goodsInfos.stream()
                .filter(goodsInfo -> Objects.equals(GoodsStatus.INVALID, goodsInfo.getGoodsStatus()))
                .collect(Collectors.toMap(GoodsInfoVO::getGoodsInfoId, c -> Boolean.TRUE));
    }

    /**
     * 验证SKU是否已收藏
     *
     * @param request 参数
     * @return 已收藏的SkuId
     */
    public List<String> isFollow(GoodsCustomerFollowRequest request) {
        List<GoodsCustomerFollow> follows = goodsCustomerFollowRepository.findAll(GoodsCustomerFollowQueryRequest.builder()
                .customerId(request.getCustomerId())
                .goodsInfoIds(request.getGoodsInfoIds())
                .build().getWhereCriteria());
        if (CollectionUtils.isEmpty(follows)) {
            return Collections.emptyList();
        }
        return follows.stream().map(GoodsCustomerFollow::getGoodsInfoId).collect(Collectors.toList());
    }

    /**
     * 统计收藏表
     *
     * @param queryRequest
     * @return
     */
    public Long count(GoodsCustomerFollowQueryRequest queryRequest) {
        return goodsCustomerFollowRepository.count(queryRequest.getWhereCriteria());
    }

    /**
     * @return void
     * @Author lvzhenwei
     * @Description 更新商品收藏量
     * @Date 16:22 2019/4/11
     * @Param [request]
     **/
    private void updateGoodsCollectNum(String goodsInfoId, String followType) {
        GoodsInfoByIdRequest goodsInfoByIdRequest = new GoodsInfoByIdRequest();
        goodsInfoByIdRequest.setGoodsInfoId(goodsInfoId);
        GoodsInfoByIdResponse goodsInfoByIdResponse = goodsInfoQueryProvider.getById(goodsInfoByIdRequest).getContext();
        GoodsModifyCollectNumRequest goodsModifyCollectNumRequest = new GoodsModifyCollectNumRequest();
        if (followType.equals(GOODSFOLLOW)) {
            goodsModifyCollectNumRequest.setGoodsCollectNum(1L);
        } else {
            goodsModifyCollectNumRequest.setGoodsCollectNum(-1L);
        }
        goodsModifyCollectNumRequest.setGoodsId(goodsInfoByIdResponse.getGoodsId());
        goodsCollectNumMq.updateGoodsCollectNum(goodsModifyCollectNumRequest);
    }
}
