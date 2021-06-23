package com.wanmi.sbc.pointsmall;

import com.wanmi.sbc.customer.api.provider.fandeng.ExternalProvider;
import com.wanmi.sbc.customer.api.request.fandeng.FanDengPointRequest;
import com.wanmi.sbc.customer.bean.vo.CustomerSimplifyOrderCommitVO;
import io.seata.spring.annotation.GlobalTransactional;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.EnableStatus;
import com.wanmi.sbc.common.enums.SortType;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.customer.CustomerQueryProvider;
import com.wanmi.sbc.customer.api.request.customer.CustomerGetByIdRequest;
import com.wanmi.sbc.customer.api.response.customer.CustomerGetByIdResponse;
import com.wanmi.sbc.goods.api.provider.pointsgoods.PointsGoodsQueryProvider;
import com.wanmi.sbc.goods.api.provider.pointsgoodscate.PointsGoodsCateQueryProvider;
import com.wanmi.sbc.goods.api.request.pointsgoods.PointsGoodsPageRequest;
import com.wanmi.sbc.goods.api.request.pointsgoodscate.PointsGoodsCateListRequest;
import com.wanmi.sbc.goods.api.response.pointsgoods.PointsGoodsPageResponse;
import com.wanmi.sbc.goods.api.response.pointsgoodscate.PointsGoodsCateListResponse;
import com.wanmi.sbc.marketing.api.provider.pointscoupon.PointsCouponQueryProvider;
import com.wanmi.sbc.marketing.api.provider.pointscoupon.PointsCouponSaveProvider;
import com.wanmi.sbc.marketing.api.request.pointscoupon.PointsCouponByIdRequest;
import com.wanmi.sbc.marketing.api.request.pointscoupon.PointsCouponFetchRequest;
import com.wanmi.sbc.marketing.api.request.pointscoupon.PointsCouponPageRequest;
import com.wanmi.sbc.marketing.api.response.pointscoupon.PointsCouponByIdResponse;
import com.wanmi.sbc.marketing.api.response.pointscoupon.PointsCouponPageResponse;
import com.wanmi.sbc.marketing.api.response.pointscoupon.PointsCouponSendCodeResponse;
import com.wanmi.sbc.order.api.provider.trade.TradeProvider;
import com.wanmi.sbc.order.api.request.trade.PointsCouponTradeCommitRequest;
import com.wanmi.sbc.order.bean.vo.PointsTradeCommitResultVO;
import com.wanmi.sbc.pointsmall.response.CustomerInfoResponse;
import com.wanmi.sbc.util.CommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import com.wanmi.sbc.customer.bean.vo.CustomerVO;
import javax.validation.Valid;
import java.time.LocalDateTime;


@Api(description = "积分商城API", tags = "PointsMallController")
@RestController
@RequestMapping(value = "/pointsMall")
public class PointsMallController {

    @Autowired
    private PointsGoodsQueryProvider pointsGoodsQueryProvider;

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private CustomerQueryProvider customerQueryProvider;

    @Autowired
    private PointsGoodsCateQueryProvider pointsGoodsCateQueryProvider;

    @Autowired
    private PointsCouponQueryProvider pointsCouponQueryProvider;

    @Autowired
    private PointsCouponSaveProvider pointsCouponSaveProvider;

    @Autowired
    private TradeProvider tradeProvider;
    @Autowired
    private ExternalProvider externalProvider;
    /**
     * 积分兑换优惠券
     */
    @GlobalTransactional
    @Transactional(rollbackFor = Exception.class)
    @ApiOperation(value = "积分兑换优惠券")
    @PostMapping(value = "/fetchPointsCoupon/{pointsCouponId}")
    public BaseResponse<PointsTradeCommitResultVO> fetchPointsCoupon(@PathVariable Long pointsCouponId) {
        // 生成优惠券券码 扣除积分优惠券库存
        PointsCouponSendCodeResponse response = pointsCouponSaveProvider.exchangePointsCoupon(PointsCouponFetchRequest.builder()
                .customerId(commonUtil.getOperatorId())
                .pointsCouponId(pointsCouponId)
                .build())
                .getContext();

        PointsCouponTradeCommitRequest commitRequest = new PointsCouponTradeCommitRequest();
        KsBeanUtil.copyPropertiesThird(response, commitRequest);
        CustomerSimplifyOrderCommitVO customer = new CustomerSimplifyOrderCommitVO();
        customer.setCustomerId(response.getCustomer().getCustomerId());
        commitRequest.setCustomer(customer);
        commitRequest.setOperator(commonUtil.getOperator());

        // 生成积分订单 扣除用户积分
        PointsTradeCommitResultVO successResult =
                tradeProvider.pointsCouponCommit(commitRequest).getContext().getPointsTradeCommitResult();
        return BaseResponse.success(successResult);
    }

    @ApiOperation(value = "查询热门兑换")
    @PostMapping(value = "/hotExchange")
    public BaseResponse<PointsGoodsPageResponse> hotExchange(@RequestBody @Valid PointsGoodsPageRequest pointsGoodsPageReq) {

        //可兑换时间内启动状态下的商品
        wrapperConditionToPointsGoodsPageRequest(pointsGoodsPageReq);
        pointsGoodsPageReq.putSort("sales", SortType.DESC.toValue());
        pointsGoodsPageReq.putSort("createTime", SortType.DESC.toValue());

        return pointsGoodsQueryProvider.page(pointsGoodsPageReq);
    }

    @ApiOperation(value = "查询会员信息")
    @RequestMapping(value = "/customerInfo", method = RequestMethod.GET)
    public BaseResponse<CustomerInfoResponse> findCustomerInfo() {
        String customerId = commonUtil.getOperatorId();
        CustomerGetByIdResponse customer = customerQueryProvider.getCustomerById(new CustomerGetByIdRequest
                (customerId)).getContext();

        return BaseResponse.success(CustomerInfoResponse.builder()
                .customerId(customerId)
                .customerName(customer.getCustomerDetail().getCustomerName())
                .pointsAvailable(customer.getPointsAvailable())
                .build()
        );
    }

    /**
     * 查询积分商品分类
     *
     * @return
     */
    @ApiOperation(value = "查询积分商品分类")
    @RequestMapping(value = "/cateList", method = RequestMethod.GET)
    public BaseResponse<PointsGoodsCateListResponse> getCateList() {
        PointsGoodsCateListRequest listReq = new PointsGoodsCateListRequest();
        listReq.setDelFlag(DeleteFlag.NO);
        listReq.putSort("sort", "asc");
        return pointsGoodsCateQueryProvider.list(listReq);
    }

    /**
     * 查询我能兑换的积分商品
     *
     * @return
     */
    @ApiOperation(value = "查询我能兑换的积分商品")
    @PostMapping("/pageCanExchange")
    public BaseResponse<PointsGoodsPageResponse> pageCanExchange(@RequestBody @Valid PointsGoodsPageRequest pointsGoodsPageReq) {

        //可兑换时间内启动状态下的商品
        wrapperConditionToPointsGoodsPageRequest(pointsGoodsPageReq);
        //根据是否推荐和销量排序
        wrapperSortToPointsGoodsPageRequest(pointsGoodsPageReq);

        //查询会员可用积分
//        String customerId = commonUtil.getOperatorId();
//        CustomerGetByIdResponse customer = customerQueryProvider.getCustomerById(new CustomerGetByIdRequest
//                (customerId)).getContext();
        CustomerVO customer = commonUtil.getCustomer();
        Long pointsAvailable=0L;
        if (StringUtils.isNotBlank(customer.getFanDengUserNo())){
            //会员积分余额
            pointsAvailable =  externalProvider.getByUserNoPoint(FanDengPointRequest.builder()
                    .userNo(customer.getFanDengUserNo()).build()
            ).getContext().getCurrentPoint();
        }
        //设置积分兑换最大值
        pointsGoodsPageReq.setMaxPoints(pointsAvailable);

        return pointsGoodsQueryProvider.page(pointsGoodsPageReq);
    }

    /**
     * 仅展示可兑换时间内且启用状态下的商品 按不同排序规则进行排序
     *
     * @param pointsGoodsPageReq
     * @return
     */
    @ApiOperation(value = "查询积分商品")
    @PostMapping("/page")
    public BaseResponse<PointsGoodsPageResponse> page(@RequestBody @Valid PointsGoodsPageRequest pointsGoodsPageReq) {
        wrapperConditionToPointsGoodsPageRequest(pointsGoodsPageReq);
        wrapperSortToPointsGoodsPageRequest(pointsGoodsPageReq);
        return pointsGoodsQueryProvider.page(pointsGoodsPageReq);
    }

    /**
     * 查询我能兑换的积分优惠券
     */
    @ApiOperation(value = "查询我能兑换的积分优惠券")
    @PostMapping("/pageCanExchangeCoupon")
    public BaseResponse<PointsCouponPageResponse> pageCanExchangeCoupon(@RequestBody @Valid PointsCouponPageRequest pointsCouponPageReq) {
        // 未删除、兑换时间内、启动状态下的优惠券
        pointsCouponPageReq.setDelFlag(DeleteFlag.NO);
        pointsCouponPageReq.setStatus(EnableStatus.ENABLE);
        pointsCouponPageReq.setBeginTimeEnd(LocalDateTime.now());
        pointsCouponPageReq.setEndTimeBegin(LocalDateTime.now());

        // 根据积分及市场价排序
        wrapperSortToPointsCouponPageRequest(pointsCouponPageReq);

        // 查询会员可用积分
        CustomerGetByIdResponse customer = customerQueryProvider.getCustomerById(new CustomerGetByIdRequest
                (commonUtil.getOperatorId())).getContext();
        // 设置积分可兑换最大值
        pointsCouponPageReq.setPoints(customer.getPointsAvailable());

        return pointsCouponQueryProvider.page(pointsCouponPageReq);
    }

    /**
     * 仅展示可兑换时间内且启用状态下的优惠券 按不同排序规则进行排序
     *
     * @param pointsCouponPageReq
     * @return
     */
    @ApiOperation(value = "查询积分优惠券")
    @PostMapping("/pageCoupon")
    public BaseResponse<PointsCouponPageResponse> page(@RequestBody @Valid PointsCouponPageRequest pointsCouponPageReq) {
        // 未删除、兑换时间内、启动状态下的优惠券
        pointsCouponPageReq.setDelFlag(DeleteFlag.NO);
        pointsCouponPageReq.setStatus(EnableStatus.ENABLE);
        pointsCouponPageReq.setBeginTimeEnd(LocalDateTime.now());
        pointsCouponPageReq.setEndTimeBegin(LocalDateTime.now());

        // 根据积分及市场价排序
        wrapperSortToPointsCouponPageRequest(pointsCouponPageReq);

        return pointsCouponQueryProvider.page(pointsCouponPageReq);
    }

    @ApiOperation(value = "根据id查询积分兑换券表")
    @GetMapping("/coupon/{pointsCouponId}")
    public BaseResponse<PointsCouponByIdResponse> getById(@PathVariable Long pointsCouponId) {
        if (pointsCouponId == null) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        PointsCouponByIdRequest idReq = new PointsCouponByIdRequest();
        idReq.setPointsCouponId(pointsCouponId);
        return pointsCouponQueryProvider.getById(idReq);
    }

    /**
     * 包装筛选条件到PointsGoodsPageRequest查询对象中
     *
     * @param queryRequest
     */
    private void wrapperConditionToPointsGoodsPageRequest(PointsGoodsPageRequest queryRequest) {
        queryRequest.setStatus(EnableStatus.ENABLE);
        queryRequest.setBeginTimeEnd(LocalDateTime.now());
        queryRequest.setEndTimeBegin(LocalDateTime.now());
        queryRequest.setMinStock(1L);
        queryRequest.setSaleFlag(Boolean.TRUE);
    }

    /**
     * 包装排序字段到PointsGoodsPageRequest查询对象中
     *
     * @param queryRequest
     * @return
     */
    private void wrapperSortToPointsGoodsPageRequest(PointsGoodsPageRequest queryRequest) {
        if (queryRequest.getSortFlag() != null) {
            switch (queryRequest.getSortFlag()) {
                case 0:
                    //积分价格数升序
                    queryRequest.putSort("points", SortType.ASC.toValue());
                    break;
                case 1:
                    //积分价格数倒序
                    queryRequest.putSort("points", SortType.DESC.toValue());
                    break;
                case 2:
                    //市场价升序
                    queryRequest.putSort("goodsInfo.marketPrice", SortType.ASC.toValue());
                    break;
                case 3:
                    //市场价倒序
                    queryRequest.putSort("goodsInfo.marketPrice", SortType.DESC.toValue());
                    break;
                default:
                    break;
            }
        }

        //根据是否推荐和销量排序
        queryRequest.putSort("recommendFlag", SortType.DESC.toValue());
        queryRequest.putSort("sales", SortType.DESC.toValue());
        queryRequest.putSort("createTime", SortType.DESC.toValue());
    }

    /**
     * 包装排序字段到PointsGoodsPageRequest查询对象中
     *
     * @param queryRequest
     * @return
     */
    private void wrapperSortToPointsCouponPageRequest(PointsCouponPageRequest queryRequest) {
        queryRequest.putSort("sellOutFlag", SortType.ASC.toValue());
        if (queryRequest.getSortFlag() != null) {
            switch (queryRequest.getSortFlag()) {
                case 0:
                    //积分价格数升序
                    queryRequest.putSort("points", SortType.ASC.toValue());
                    break;
                case 1:
                    //积分价格数倒序
                    queryRequest.putSort("points", SortType.DESC.toValue());
                    break;
                case 2:
                    //市场价升序
                    queryRequest.putSort("couponInfo.denomination", SortType.ASC.toValue());
                    break;
                case 3:
                    //市场价倒序
                    queryRequest.putSort("couponInfo.denomination", SortType.DESC.toValue());
                    break;
                default:
                    break;
            }
        } else {
            queryRequest.putSort("couponInfo.denomination", SortType.DESC.toValue());
        }

    }
}
