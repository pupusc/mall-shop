package com.wanmi.sbc.follow;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.enums.SortType;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.enterpriseinfo.EnterpriseInfoQueryProvider;
import com.wanmi.sbc.customer.api.request.enterpriseinfo.EnterpriseInfoByCustomerIdRequest;
import com.wanmi.sbc.customer.bean.dto.CustomerDTO;
import com.wanmi.sbc.customer.bean.enums.EnterpriseCheckState;
import com.wanmi.sbc.customer.bean.vo.CustomerVO;
import com.wanmi.sbc.customer.bean.vo.EnterpriseInfoVO;
import com.wanmi.sbc.distribute.DistributionService;
import com.wanmi.sbc.goods.api.provider.appointmentsale.AppointmentSaleQueryProvider;
import com.wanmi.sbc.goods.api.provider.bookingsale.BookingSaleQueryProvider;
import com.wanmi.sbc.goods.api.provider.info.GoodsInfoProvider;
import com.wanmi.sbc.goods.api.provider.price.GoodsLevelPriceQueryProvider;
import com.wanmi.sbc.goods.api.request.appointmentsale.AppointmentSaleInProgressRequest;
import com.wanmi.sbc.goods.api.request.bookingsale.BookingSaleInProgressRequest;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoFillGoodsStatusRequest;
import com.wanmi.sbc.goods.api.request.price.GoodsLevelPriceBySkuIdsRequest;
import com.wanmi.sbc.goods.api.response.price.GoodsIntervalPriceByCustomerIdResponse;
import com.wanmi.sbc.goods.bean.dto.GoodsInfoDTO;
import com.wanmi.sbc.goods.bean.enums.PriceType;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.goods.bean.vo.GoodsLevelPriceVO;
import com.wanmi.sbc.intervalprice.GoodsIntervalPriceService;
import com.wanmi.sbc.marketing.api.provider.plugin.MarketingPluginProvider;
import com.wanmi.sbc.marketing.api.request.plugin.MarketingPluginGoodsListFilterRequest;
import com.wanmi.sbc.order.api.provider.follow.FollowProvider;
import com.wanmi.sbc.order.api.provider.follow.FollowQueryProvider;
import com.wanmi.sbc.order.api.provider.purchase.PurchaseProvider;
import com.wanmi.sbc.order.api.request.follow.*;
import com.wanmi.sbc.order.api.request.follow.validGroups.FollowAdd;
import com.wanmi.sbc.order.api.request.follow.validGroups.FollowDelete;
import com.wanmi.sbc.order.api.request.follow.validGroups.FollowFilter;
import com.wanmi.sbc.order.api.request.purchase.PurchaseFillBuyCountRequest;
import com.wanmi.sbc.order.api.response.follow.FollowListResponse;
import com.wanmi.sbc.order.api.response.purchase.PurchaseFillBuyCountResponse;
import com.wanmi.sbc.util.CommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 商品收藏Controller
 * Created by daiyitian on 17/4/12.
 */
@RestController
@RequestMapping("/goods")
@Api(tags = "GoodsFollowBaseController", description = "S2B web公用-商品收藏信息API")
public class GoodsFollowBaseController {

    @Autowired
    private FollowProvider followProvider;

    @Autowired
    private FollowQueryProvider followQueryProvider;

    @Autowired
    private PurchaseProvider purchaseProvider;

    @Autowired
    private GoodsInfoProvider goodsInfoProvider;

    @Autowired
    private MarketingPluginProvider marketingPluginProvider;

    @Autowired
    private GoodsIntervalPriceService goodsIntervalPriceService;

    @Autowired
    private DistributionService distributionService;

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private AppointmentSaleQueryProvider appointmentSaleQueryProvider;

    @Autowired
    private BookingSaleQueryProvider bookingSaleQueryProvider;

    @Autowired
    private GoodsLevelPriceQueryProvider goodsLevelPriceQueryProvider;

    @Autowired
    private EnterpriseInfoQueryProvider enterpriseInfoQueryProvider;

    /**
     * 获取商品收藏列表
     * @param queryRequest 查询条件
     * @return 商品收藏分页
     */
    @ApiOperation(value = "获取商品收藏列表")
    @RequestMapping(value = "/goodsFollows", method = RequestMethod.POST)
    public BaseResponse<FollowListResponse> info(@RequestBody FollowListRequest queryRequest) {
        //获取会员
        CustomerVO customer = commonUtil.getCustomer();
        queryRequest.setCustomerId(customer.getCustomerId());
        //按创建时间倒序
        queryRequest.putSort("followTime", SortType.DESC.toValue());
        queryRequest.putSort("followId", SortType.DESC.toValue());

        FollowListResponse  response = followQueryProvider.list(queryRequest).getContext();

        if(CollectionUtils.isNotEmpty(response.getGoodsInfos().getContent())) {
            List<GoodsInfoDTO> dtoList = KsBeanUtil.convertList(response.getGoodsInfos().getContent(), GoodsInfoDTO.class);

            //设定SKU状态
            List<GoodsInfoVO> goodsInfoVOList = goodsInfoProvider.fillGoodsStatus(
                    GoodsInfoFillGoodsStatusRequest.builder().goodsInfos(dtoList).build()).getContext().getGoodsInfos();
            //根据开关重新设置分销商品标识
            distributionService.checkDistributionSwitch(goodsInfoVOList);
            //计算区间价
            GoodsIntervalPriceByCustomerIdResponse priceResponse =
                    goodsIntervalPriceService.getGoodsIntervalPriceVOList(goodsInfoVOList, customer.getCustomerId());
            response.setGoodsIntervalPrices(priceResponse.getGoodsIntervalPriceVOList());
            goodsInfoVOList = priceResponse.getGoodsInfoVOList();
            //计算营销价格
            MarketingPluginGoodsListFilterRequest filterRequest = new MarketingPluginGoodsListFilterRequest();
            filterRequest.setGoodsInfos(KsBeanUtil.convert(goodsInfoVOList, GoodsInfoDTO.class));
            filterRequest.setCustomerDTO(KsBeanUtil.convert(customer, CustomerDTO.class));
            goodsInfoVOList = marketingPluginProvider.goodsListFilter(filterRequest).getContext().getGoodsInfoVOList();

            //填充购买数
            PurchaseFillBuyCountRequest purchaseFillBuyCountRequest = new PurchaseFillBuyCountRequest();
            purchaseFillBuyCountRequest.setCustomerId(customer.getCustomerId());
            purchaseFillBuyCountRequest.setGoodsInfoList(goodsInfoVOList);
            purchaseFillBuyCountRequest.setInviteeId(commonUtil.getPurchaseInviteeId());
            PurchaseFillBuyCountResponse purchaseFillBuyCountResponse = purchaseProvider.fillBuyCount(purchaseFillBuyCountRequest).getContext();
            goodsInfoVOList = purchaseFillBuyCountResponse.getGoodsInfoList();
            response.setGoodsInfos(new MicroServicePage<GoodsInfoVO>(goodsInfoVOList,  queryRequest.getPageRequest(),
                    response.getGoodsInfos().getTotalElements()));
            // 预约抢购中商品/预售中的商品
            List<String> goodInfoIdList = response.getGoodsInfos().getContent().stream().map(GoodsInfoVO::getGoodsInfoId).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(goodInfoIdList)) {
                response.setAppointmentSaleVOList(appointmentSaleQueryProvider.inProgressAppointmentSaleInfoByGoodsInfoIdList
                        (AppointmentSaleInProgressRequest.builder().goodsInfoIdList(goodInfoIdList).build()).getContext().getAppointmentSaleVOList());
                response.setBookingSaleVOList(bookingSaleQueryProvider.inProgressBookingSaleInfoByGoodsInfoIdList(
                        BookingSaleInProgressRequest.builder().goodsInfoIdList(goodInfoIdList).build()).getContext().getBookingSaleVOList());
            }
        }
        if (CollectionUtils.isNotEmpty(response.getGoodsInfos().getContent())) {
            //判断当前用户对应企业购商品等级企业价
            List<GoodsLevelPriceVO> goodsLevelPrices = this.getGoodsLevelPrices(response.getGoodsInfos().getContent().stream()
                    .map(GoodsInfoVO::getGoodsInfoId).collect(Collectors.toList()), customer);
            if (CollectionUtils.isNotEmpty(goodsLevelPrices)) {
                response.getGoodsInfos().getContent().forEach(goodsInfo -> {
                    Optional<GoodsLevelPriceVO> first = goodsLevelPrices.stream()
                            .filter(goodsLevelPrice -> goodsLevelPrice.getGoodsInfoId().equals(goodsInfo.getGoodsInfoId()))
                            .findFirst();
                    goodsInfo.setEnterPrisePrice(first.isPresent() ? first.get().getPrice() : goodsInfo.getEnterPrisePrice());
                });
            }
        }

        //设置商品副标题
        if (CollectionUtils.isNotEmpty(  response.getGoodsInfos().getContent()) &&   CollectionUtils.isNotEmpty(response.getGoodses())){
            response.getGoodsInfos().getContent().forEach(goodsInfoVO -> {
                response.getGoodses().forEach(goodsVO -> {
                    if (Objects.equals(goodsInfoVO.getGoodsId(),goodsVO.getGoodsId()) && StringUtils.isNotBlank(goodsVO.getGoodsSubtitle())) {
                        goodsInfoVO.setGoodsSubtitle(goodsVO.getGoodsSubtitle());
                    }
                });
            });
        }
        return BaseResponse.success(response);
    }

    private List<GoodsLevelPriceVO> getGoodsLevelPrices(List<String> skuIds, CustomerVO customer) {
        //等级价格
        List<GoodsLevelPriceVO> goodsLevelPriceList = new ArrayList<>();
        if (Objects.nonNull(customer)) {
            GoodsLevelPriceBySkuIdsRequest goodsLevelPriceBySkuIdsRequest = new GoodsLevelPriceBySkuIdsRequest();
            goodsLevelPriceBySkuIdsRequest.setSkuIds(skuIds);
            goodsLevelPriceBySkuIdsRequest.setType(PriceType.ENTERPRISE_SKU);
            goodsLevelPriceList = goodsLevelPriceQueryProvider
                    .listBySkuIds(goodsLevelPriceBySkuIdsRequest).getContext().getGoodsLevelPriceList();
            if (CollectionUtils.isNotEmpty(goodsLevelPriceList)) {
                goodsLevelPriceList = goodsLevelPriceList.stream()
                        .filter(goodsLevelPrice -> goodsLevelPrice.getLevelId().equals(customer.getCustomerLevelId()))
                        .collect(Collectors.toList());
            }
        }
        return goodsLevelPriceList;
    }

    /**
     * 新增商品收藏
     * @param request 数据
     * @return 结果
     */
    @ApiOperation(value = "新增商品收藏")
    @RequestMapping(value = "/goodsFollow", method = RequestMethod.POST)
    public BaseResponse add(@Validated({FollowAdd.class}) @RequestBody FollowSaveRequest request) {
        request.setCustomerId(commonUtil.getOperatorId());
        request.setTerminalSource(commonUtil.getTerminal());
        if (StringUtils.isBlank(request.getCustomerId())) {
            throw new SbcRuntimeException("K-010110");
        }
        followProvider.save(request);
       //goodsCustomerFollowService.save(request);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 取消商品收藏
     * @param request 数据
     * @return 结果
     */
    @ApiOperation(value = "取消商品收藏")
    @RequestMapping(value = "/goodsFollow", method = RequestMethod.DELETE)
    public BaseResponse delete(@Validated({FollowDelete.class}) @RequestBody FollowDeleteRequest request) {
        request.setCustomerId(commonUtil.getOperatorId());
        followProvider.delete(request);
       // goodsCustomerFollowService.delete(request);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 删除失效商品
     * @return 结果
     */
    @ApiOperation(value = "删除失效商品")
    @RequestMapping(value = "/goodsFollows", method = RequestMethod.DELETE)
    public BaseResponse delete() {
        followProvider.deleteInvalidGoods(InvalidGoodsDeleteRequest.builder().customerId(commonUtil.getOperatorId()).build());
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 是否含有失效商品
     * @return 结果
     */
    @ApiOperation(value = "是否含有失效商品")
    @RequestMapping(value = "/hasInvalidGoods", method = RequestMethod.GET)
    public BaseResponse<Boolean> hasInvalid() {
        return BaseResponse.success(followQueryProvider.haveInvalidGoods(HaveInvalidGoodsRequest.builder().customerId(commonUtil.getOperatorId()).build()).getContext().getBoolValue());
    }

    /**
     * 批量验证是否是收藏商品
     * @return 结果，相应的SkuId就是已收藏的商品ID
     */
    @ApiOperation(value = "批量验证是否是收藏商品<List<String>,相应的SkuId就是已收藏的商品ID>")
    @RequestMapping(value = "/isGoodsFollow", method = RequestMethod.POST)
    public BaseResponse<List<String>> isGoodsFollow(@Validated({FollowFilter.class}) @RequestBody IsFollowRequest request) {
        request.setCustomerId(commonUtil.getOperatorId());
        return BaseResponse.success(followQueryProvider.isFollow(request).getContext().getValue());
    }

    /**
     * 获取商品收藏个数
     * @return 商品收藏个数
     */
    @ApiOperation(value = "获取商品收藏个数")
    @RequestMapping(value = "/goodsFollowNum", method = RequestMethod.GET)
    public BaseResponse<Long> count() {
        FollowCountRequest request = FollowCountRequest.builder()
                .customerId(commonUtil.getOperatorId())
                .build();
        return BaseResponse.success( followQueryProvider.count(request).getContext().getValue());
    }
}
