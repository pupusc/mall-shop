package com.wanmi.sbc.order;

import com.wanmi.sbc.common.annotation.MultiSubmit;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.Operator;
import com.wanmi.sbc.common.enums.BoolFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.EnableStatus;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.customer.CustomerQueryProvider;
import com.wanmi.sbc.customer.api.provider.fandeng.ExternalProvider;
import com.wanmi.sbc.customer.api.provider.store.StoreQueryProvider;
import com.wanmi.sbc.customer.api.request.customer.CustomerGetByIdRequest;
import com.wanmi.sbc.customer.api.request.fandeng.FanDengPointRequest;
import com.wanmi.sbc.customer.api.request.store.NoDeleteStoreByIdRequest;
import com.wanmi.sbc.customer.api.response.customer.CustomerGetByIdResponse;
import com.wanmi.sbc.customer.bean.vo.StoreVO;
import com.wanmi.sbc.goods.api.provider.info.GoodsInfoQueryProvider;
import com.wanmi.sbc.goods.api.provider.pointsgoods.PointsGoodsQueryProvider;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoByIdRequest;
import com.wanmi.sbc.goods.api.request.pointsgoods.PointsGoodsByIdRequest;
import com.wanmi.sbc.goods.api.response.info.GoodsInfoByIdResponse;
import com.wanmi.sbc.goods.api.response.info.GoodsInfoResponse;
import com.wanmi.sbc.goods.bean.enums.AddedFlag;
import com.wanmi.sbc.goods.bean.vo.PointsGoodsVO;
import com.wanmi.sbc.customer.bean.vo.CustomerVO;
import com.wanmi.sbc.order.api.provider.trade.TradeProvider;
import com.wanmi.sbc.order.api.provider.trade.TradeQueryProvider;
import com.wanmi.sbc.order.api.provider.trade.VerifyQueryProvider;
import com.wanmi.sbc.order.api.request.trade.PointsTradeCommitRequest;
import com.wanmi.sbc.order.api.request.trade.TradeGetGoodsRequest;
import com.wanmi.sbc.order.api.request.trade.VerifyPointsGoodsRequest;
import com.wanmi.sbc.order.api.response.trade.TradeGetGoodsResponse;
import com.wanmi.sbc.order.bean.dto.TradeGoodsInfoPageDTO;
import com.wanmi.sbc.order.bean.dto.TradeItemDTO;
import com.wanmi.sbc.order.bean.vo.PointsTradeCommitResultVO;
import com.wanmi.sbc.order.bean.vo.PointsTradeItemGroupVO;
import com.wanmi.sbc.order.bean.vo.SupplierVO;
import com.wanmi.sbc.order.bean.vo.TradeItemVO;
import com.wanmi.sbc.order.request.ImmediateExchangeRequest;
import com.wanmi.sbc.order.request.PointsTradeItemRequest;
import com.wanmi.sbc.order.response.PointsTradeConfirmResponse;
import com.wanmi.sbc.util.CommonUtil;
import io.seata.spring.annotation.GlobalTransactional;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Objects;

/**
 * <p>积分订单Controller</p>
 * Created by yinxianzhi on 2019-05-20-下午4:12.
 */
@Api(tags = "PointsTradeController", description = "积分订单服务API")
@RestController
@RequestMapping("/pointsTrade")
@Slf4j
@Validated
public class PointsTradeController {

    @Autowired
    private TradeQueryProvider tradeQueryProvider;

    @Autowired
    private TradeProvider tradeProvider;

    @Resource
    private VerifyQueryProvider verifyQueryProvider;

    @Autowired
    private CustomerQueryProvider customerQueryProvider;

    @Resource
    private CommonUtil commonUtil;

    @Resource
    private StoreQueryProvider storeQueryProvider;

    @Autowired
    private PointsGoodsQueryProvider pointsGoodsQueryProvider;

    @Autowired
    private GoodsInfoQueryProvider goodsInfoQueryProvider;
    @Autowired
    private ExternalProvider externalProvider;

    /**
     * 立即兑换
     */
    @ApiOperation(value = "立即兑换")
    @RequestMapping(value = "/exchange", method = RequestMethod.POST)
    public BaseResponse exchange(@RequestBody @Valid ImmediateExchangeRequest exchangeRequest) {
        // 1.获取积分商品信息
        PointsGoodsByIdRequest idReq = new PointsGoodsByIdRequest();
        idReq.setPointsGoodsId(exchangeRequest.getPointsGoodsId());
        PointsGoodsVO pointsGoodsVO = pointsGoodsQueryProvider.getById(idReq).getContext().getPointsGoodsVO();

        //商品原始信息
        if(Objects.nonNull(pointsGoodsVO)) {
            GoodsInfoByIdResponse goodsInfo = goodsInfoQueryProvider
                    .getById(GoodsInfoByIdRequest.builder().goodsInfoId(pointsGoodsVO.getGoodsInfoId()).build())
                    .getContext();
            //校验原始商品的删除、上架、可售状态
            if(Objects.isNull(goodsInfo)
                    || DeleteFlag.YES.equals(goodsInfo.getDelFlag())
                    || AddedFlag.NO.equals(goodsInfo.getAddedFlag())
                    || Constants.no.equals(goodsInfo.getVendibility())) {
                throw new SbcRuntimeException("K-120001");
            }
        }

        // 2.验证积分商品(校验积分商品库存，删除，启用停用状态，兑换时间)
        if (DeleteFlag.YES.equals(pointsGoodsVO.getDelFlag()) || EnableStatus.DISABLE.equals(pointsGoodsVO.getStatus())) {
            throw new SbcRuntimeException("K-120001");
        }
        if (pointsGoodsVO.getStock() < exchangeRequest.getNum()) {
            throw new SbcRuntimeException("K-120002");
        }
        if (pointsGoodsVO.getEndTime().isBefore(LocalDateTime.now())) {
            throw new SbcRuntimeException("K-120003");
        }
        // 3.验证用户积分
        //查询会员可用积分
//        CustomerGetByIdResponse customer = customerQueryProvider.getCustomerById(new CustomerGetByIdRequest
//                (commonUtil.getCustomer().getCustomerId())).getContext();

        CustomerVO customer = commonUtil.getCustomer();
        Long pointsAvailable=0L;
        if (StringUtils.isNotBlank(customer.getFanDengUserNo())){
        //会员积分余额
            pointsAvailable =  externalProvider.getByUserNoPoint(FanDengPointRequest.builder()
                            .userNo(customer.getFanDengUserNo()).build()
                ).getContext().getCurrentPoint();
        }
        if (pointsAvailable.compareTo(pointsGoodsVO.getPoints() * exchangeRequest.getNum()) == -1) {
            throw new SbcRuntimeException("K-120004");
        }

        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 用于立即兑换后，创建订单前的获取订单积分商品信息
     *
     * @param pointsTradeItemRequest
     * @return
     */
    @ApiOperation(value = "用于确认订单后，创建订单前的获取积分商品信息")
    @RequestMapping(value = "/getExchangeItem", method = RequestMethod.POST)
    @GlobalTransactional
    public BaseResponse<PointsTradeConfirmResponse> getExchangeItem(@RequestBody @Valid PointsTradeItemRequest pointsTradeItemRequest) {
        PointsTradeConfirmResponse confirmResponse = new PointsTradeConfirmResponse();
        PointsTradeItemGroupVO pointsTradeItemGroupVO = wrapperGroupVO(pointsTradeItemRequest.getPointsGoodsId(),
                pointsTradeItemRequest.getNum());

        confirmResponse.setPointsTradeConfirmItem(pointsTradeItemGroupVO);
        confirmResponse.setTotalPoints(pointsTradeItemGroupVO.getTradeItem().getPoints() * pointsTradeItemGroupVO
                .getTradeItem().getNum());

        return BaseResponse.success(confirmResponse);
    }

//    /**
//     * 提交积分订单，用于生成积分订单操作
//     */
//    @ApiOperation(value = "提交订单，用于生成订单操作")
//    @RequestMapping(value = "/commit", method = RequestMethod.POST)
//    @MultiSubmit
//    @GlobalTransactional
//    public BaseResponse<PointsTradeCommitResultVO> commit(@RequestBody @Valid PointsTradeCommitRequest
//                                                                  pointsTradeCommitRequest) {
//        Operator operator = commonUtil.getOperator();
//        pointsTradeCommitRequest.setOperator(operator);
//
//        PointsTradeItemGroupVO pointsTradeItemGroup = wrapperGroupVO(pointsTradeCommitRequest.getPointsGoodsId(),
//                pointsTradeCommitRequest.getNum());
//        pointsTradeCommitRequest.setPointsTradeItemGroup(pointsTradeItemGroup);
//
//        PointsTradeCommitResultVO successResult =
//                tradeProvider.pointsCommit(pointsTradeCommitRequest).getContext().getPointsTradeCommitResult();
//        return BaseResponse.success(successResult);
//    }

    /**
     * 包装积分订单项数据
     * 普通订单从快照中读取 积分订单根据前台数据组装
     *
     * @param pointsGoodsId
     * @param num
     * @return
     */
    private PointsTradeItemGroupVO wrapperGroupVO(String pointsGoodsId, Long num) {
        // 1.获取积分商品信息
        PointsGoodsVO pointsGoodsVO = pointsGoodsQueryProvider.getById(new PointsGoodsByIdRequest
                (pointsGoodsId)).getContext().getPointsGoodsVO();
        // 2.验证用户积分
        //查询会员可用积分
        CustomerVO customer = commonUtil.getCustomer();
        Long pointsAvailable=0L;
        if (StringUtils.isNotBlank(customer.getFanDengUserNo())){
            //会员积分余额
            pointsAvailable =  externalProvider.getByUserNoPoint(FanDengPointRequest.builder()
                    .userNo(customer.getFanDengUserNo()).build()
            ).getContext().getCurrentPoint();
        }
  /*
        CustomerGetByIdResponse customer = customerQueryProvider.getCustomerById(new CustomerGetByIdRequest
                (commonUtil.getCustomer().getCustomerId())).getContext();
        //会员积分余额
        Long pointsAvailable = customer.getPointsAvailable();*/
        if (pointsAvailable.compareTo(pointsGoodsVO.getPoints() * num) == -1) {
            throw new SbcRuntimeException("K-120004");
        }
        // 3.获取商品信息
        GoodsInfoResponse skuResp = getGoodsResponse(pointsGoodsVO.getGoodsInfoId());
        // 4.获取商品所属商家，店铺信息
        Long storeId = skuResp.getGoodses().get(0).getStoreId();

        StoreVO store = storeQueryProvider.getNoDeleteStoreById(NoDeleteStoreByIdRequest.builder().storeId(storeId)
                .build())
                .getContext().getStoreVO();
        SupplierVO supplierVO = SupplierVO.builder()
                .storeId(store.getStoreId())
                .storeName(store.getStoreName())
                .isSelf(store.getCompanyType() == BoolFlag.NO)
                .supplierCode(store.getCompanyInfo().getCompanyCode())
                .supplierId(store.getCompanyInfo().getCompanyInfoId())
                .supplierName(store.getCompanyInfo().getSupplierName())
                .freightTemplateType(store.getFreightTemplateType())
                .build();
        // 4.验证包装积分订单商品信息
        TradeItemVO tradeItemVO = verifyQueryProvider.verifyPointsGoods(new VerifyPointsGoodsRequest(
                TradeItemDTO.builder().num(num).skuId(pointsGoodsVO.getGoodsInfoId()).points(pointsGoodsVO.getPoints())
                        .pointsGoodsId(pointsGoodsVO.getPointsGoodsId()).settlementPrice(pointsGoodsVO.getSettlementPrice()).build(),
                pointsGoodsVO,
                KsBeanUtil.convert(skuResp, TradeGoodsInfoPageDTO.class),
                storeId)).getContext().getTradeItem();
        // 5.包装返回值
        PointsTradeItemGroupVO pointsTradeItemGroupVO = new PointsTradeItemGroupVO();
        pointsTradeItemGroupVO.setSupplier(supplierVO);
        pointsTradeItemGroupVO.setTradeItem(tradeItemVO);

        return pointsTradeItemGroupVO;
    }

    /**
     * 获取订单商品详情
     */
    private GoodsInfoResponse getGoodsResponse(String skuId) {
        TradeGetGoodsResponse response =
                tradeQueryProvider.getGoods(TradeGetGoodsRequest.builder().skuIds(Collections.singletonList(skuId)).build
                        ()).getContext();

        return GoodsInfoResponse.builder().goodsInfos(response.getGoodsInfos())
                .goodses(response.getGoodses())
                .goodsIntervalPrices(response.getGoodsIntervalPrices())
                .build();
    }


}
