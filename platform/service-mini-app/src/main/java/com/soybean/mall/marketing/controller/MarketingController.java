package com.soybean.mall.marketing.controller;
import com.google.common.collect.Lists;

import com.soybean.mall.common.CommonUtil;
import com.soybean.mall.marketing.request.CouponByCustomerReq;
import com.soybean.mall.marketing.request.TradeItemSimpleRequest;
import com.soybean.mall.marketing.response.CouponByCustomerResp;
import com.soybean.mall.service.CommonService;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.customer.CustomerQueryProvider;
import com.wanmi.sbc.customer.api.provider.store.StoreQueryProvider;
import com.wanmi.sbc.customer.api.request.customer.CustomerGetByIdRequest;
import com.wanmi.sbc.customer.api.request.store.NoDeleteStoreByIdRequest;
import com.wanmi.sbc.customer.api.response.customer.CustomerGetByIdResponse;
import com.wanmi.sbc.customer.bean.dto.CustomerDTO;
import com.wanmi.sbc.customer.bean.vo.StoreVO;
import com.wanmi.sbc.goods.api.constant.GoodsErrorCode;
import com.wanmi.sbc.goods.api.provider.info.GoodsInfoQueryProvider;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoViewByIdsRequest;
import com.wanmi.sbc.goods.api.response.info.GoodsInfoResponse;
import com.wanmi.sbc.goods.api.response.info.GoodsInfoViewByIdsResponse;
import com.wanmi.sbc.goods.bean.dto.GoodsInfoDTO;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.marketing.api.provider.coupon.CouponCodeQueryProvider;
import com.wanmi.sbc.marketing.api.provider.plugin.MarketingLevelPluginProvider;
import com.wanmi.sbc.marketing.api.request.coupon.CouponCodeListForUseByCustomerIdRequest;
import com.wanmi.sbc.marketing.api.request.plugin.MarketingLevelGoodsListFilterRequest;
import com.wanmi.sbc.marketing.bean.dto.TradeItemInfoDTO;
import com.wanmi.sbc.marketing.bean.enums.FullBuyType;
import com.wanmi.sbc.marketing.bean.enums.ScopeType;
import com.wanmi.sbc.marketing.bean.vo.CouponCodeVO;
import com.wanmi.sbc.order.api.provider.trade.VerifyQueryProvider;
import com.wanmi.sbc.order.api.request.trade.VerifyGoodsRequest;
import com.wanmi.sbc.order.bean.dto.TradeGoodsInfoPageDTO;
import com.wanmi.sbc.order.bean.dto.TradeItemDTO;
import com.wanmi.sbc.order.bean.vo.TradeItemVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/4/20 10:37 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Slf4j
@RequestMapping("/wx/marketing")
@RestController
public class MarketingController {

    @Autowired
    private CommonUtil commonUtil;

    @Resource
    private CouponCodeQueryProvider couponCodeQueryProvider;

    @Autowired
    private CustomerQueryProvider customerQueryProvider;

    @Autowired
    private VerifyQueryProvider verifyQueryProvider;

    @Autowired
    private StoreQueryProvider storeQueryProvider;

    @Autowired
    private GoodsInfoQueryProvider goodsInfoQueryProvider;

    @Autowired
    private MarketingLevelPluginProvider marketingLevelPluginProvider;

    @Autowired
    private CommonService commonService;

    /**
     * 获取用户优惠券列表
     * @return
     */
    @PostMapping("list-coupon-by-customer")
    public BaseResponse listCouponByCustomer(@RequestBody List<TradeItemSimpleRequest> tradeItemSimpleList) {
        if (CollectionUtils.isEmpty(tradeItemSimpleList)) {
            throw new SbcRuntimeException("K-000009");
        }
        //获取客户id 验证客户信息
        String customerId = commonUtil.getOperatorId();
        CustomerGetByIdResponse customer = customerQueryProvider.getCustomerById(new CustomerGetByIdRequest(customerId)).getContext();

        List<TradeItemDTO> tradeItemList = new ArrayList<>();
        List<String> skuIdList = new ArrayList<>();
        for (TradeItemSimpleRequest tradeItemSimpleRequestParam : tradeItemSimpleList) {
            TradeItemDTO tradeItemDTO = new TradeItemDTO();
            tradeItemDTO.setSkuId(tradeItemSimpleRequestParam.getSkuId());
            tradeItemDTO.setNum(tradeItemSimpleRequestParam.getNum());
            tradeItemList.add(tradeItemDTO);
            skuIdList.add(tradeItemSimpleRequestParam.getSkuId());
        }


        GoodsInfoViewByIdsRequest goodsInfoRequest = GoodsInfoViewByIdsRequest.builder().goodsInfoIds(skuIdList).isHavSpecText(Constants.yes).build();
        GoodsInfoViewByIdsResponse response = goodsInfoQueryProvider.listViewByIds(goodsInfoRequest).getContext();

        //获取客户的等级
        if (customer!=null && StringUtils.isNotBlank(customer.getCustomerId())) {
            //计算会员价
            List<GoodsInfoVO> goodsInfoVOList = marketingLevelPluginProvider.goodsListFilter(MarketingLevelGoodsListFilterRequest.builder()
                            .goodsInfos(KsBeanUtil.convertList(response.getGoodsInfos(), GoodsInfoDTO.class))
                            .customerDTO(KsBeanUtil.convert(customer, CustomerDTO.class)).build())
                    .getContext().getGoodsInfoVOList();
            response.setGoodsInfos(goodsInfoVOList);
        }
        GoodsInfoResponse goodsInfoResponse = GoodsInfoResponse.builder().goodsInfos(response.getGoodsInfos()).goodses(response.getGoodses()).build();

        StoreVO store = storeQueryProvider.getNoDeleteStoreById(NoDeleteStoreByIdRequest.builder().storeId(goodsInfoResponse.getGoodsInfos().get(0).getStoreId()).build())
                .getContext().getStoreVO();

        VerifyGoodsRequest verifyGoodsRequest = new VerifyGoodsRequest();
        verifyGoodsRequest.setTradeItems(tradeItemList);
        verifyGoodsRequest.setOldTradeItems(Lists.newArrayList());
        verifyGoodsRequest.setGoodsInfoResponse(KsBeanUtil.convert(goodsInfoResponse, TradeGoodsInfoPageDTO.class));
        verifyGoodsRequest.setStoreId(store.getStoreId());
        verifyGoodsRequest.setIsFull(true);
        List<TradeItemVO> tradeItemVOList =  verifyQueryProvider.verifyGoods(verifyGoodsRequest).getContext().getTradeItems();

        List<TradeItemInfoDTO> tradeItemInfoDTOList = new ArrayList<>();
        for (TradeItemVO tradeItemVOParam : tradeItemVOList) {
            TradeItemInfoDTO dto = new TradeItemInfoDTO();
            dto.setBrandId(tradeItemVOParam.getBrand());
            dto.setCateId(tradeItemVOParam.getCateId());
            dto.setSpuId(tradeItemVOParam.getSpuId());
            dto.setSkuId(tradeItemVOParam.getSkuId());
            dto.setStoreId(store.getStoreId());
            dto.setPrice(tradeItemVOParam.getSplitPrice());
            tradeItemInfoDTOList.add(dto);
        }

        CouponCodeListForUseByCustomerIdRequest couponCodeListForUseByCustomerIdRequest = new CouponCodeListForUseByCustomerIdRequest();
        couponCodeListForUseByCustomerIdRequest.setCustomerId(customer.getCustomerId());
        couponCodeListForUseByCustomerIdRequest.setTradeItems(tradeItemInfoDTOList);
        couponCodeListForUseByCustomerIdRequest.setPrice(commonService.calPrice(tradeItemVOList).getTotalPrice());
        List<CouponCodeVO> couponCodeList = couponCodeQueryProvider.listForUseByCustomerId(couponCodeListForUseByCustomerIdRequest).getContext().getCouponCodeList();
        List<CouponByCustomerResp> result = new ArrayList<>();
        for (CouponCodeVO couponCodeVO : couponCodeList) {
            //过滤掉已经使用的优惠券
            if (DefaultFlag.YES.equals(couponCodeVO.getUseStatus())) {
                continue;
            }

            CouponByCustomerResp couponByCustomerResp = new CouponByCustomerResp();
            couponByCustomerResp.setStartTime(couponCodeVO.getStartTime());
            couponByCustomerResp.setEndTime(couponCodeVO.getEndTime());
            couponByCustomerResp.setCouponId(couponCodeVO.getCouponId());
            couponByCustomerResp.setCouponName(couponCodeVO.getCouponName());
            couponByCustomerResp.setDenomination(couponCodeVO.getDenomination());
            String fullContent = Objects.equals(couponCodeVO.getFullBuyType(), FullBuyType.NO_THRESHOLD) ? "无门槛" : "满 " + couponCodeVO.getFullBuyPrice() + " 可用";
            couponByCustomerResp.setFullContent(fullContent);
            String platformContent = Objects.equals(couponCodeVO.getPlatformFlag(), DefaultFlag.YES) ? "全平台可用" : "仅" + couponCodeVO.getStoreName() + " 可用";
            couponByCustomerResp.setPlatformContent(platformContent);
            StringBuilder scopeContent = new StringBuilder();
            if (Objects.equals(couponCodeVO.getScopeType(), ScopeType.ALL)) {
                scopeContent.append("商品: ").append("全部商品");
            } else if (Objects.equals(couponCodeVO.getScopeType(), ScopeType.BRAND)) {
                scopeContent.append("品牌: ").append("仅限 ");
                for (String brandName : couponCodeVO.getBrandNames()) {
                    scopeContent.append(brandName).append(" ");
                }
            } else if (Objects.equals(couponCodeVO.getScopeType(), ScopeType.BOSS_CATE)) {
                scopeContent.append("品类: ").append("仅限 ");
                for (String cateName : couponCodeVO.getGoodsCateNames()) {
                    scopeContent.append(cateName).append(" ");
                }
            } else if (Objects.equals(couponCodeVO.getScopeType(), ScopeType.STORE_CATE)) {
                scopeContent.append("分类: ").append("仅限 ");
                for (String cateName : couponCodeVO.getStoreCateNames()) {
                    scopeContent.append(cateName).append(" ");
                }
            } else {
                scopeContent.append("商品: ").append("部分商品");
            }
            couponByCustomerResp.setScopeContent(scopeContent.toString());
            couponByCustomerResp.setCouponType(couponCodeVO.getCouponType());
            couponByCustomerResp.setNearOverdue(couponCodeVO.isNearOverdue());
            couponByCustomerResp.setCouponCanUse(couponCodeVO.isCouponCanUse());
            couponByCustomerResp.setStatus(couponCodeVO.getStatus());
            result.add(couponByCustomerResp);
        }
        return BaseResponse.success(result);
    }
}
