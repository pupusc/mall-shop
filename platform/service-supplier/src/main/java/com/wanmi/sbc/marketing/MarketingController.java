package com.wanmi.sbc.marketing;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.enums.BoolFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.elastic.api.provider.goods.EsGoodsInfoElasticProvider;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsInfoRequest;
import com.wanmi.sbc.goods.api.response.info.GoodsInfoResponse;
import com.wanmi.sbc.goods.api.response.price.GoodsIntervalPriceResponse;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoResponseVO;
import com.wanmi.sbc.intervalprice.GoodsIntervalPriceService;
import com.wanmi.sbc.marketing.api.provider.market.MarketingProvider;
import com.wanmi.sbc.marketing.api.provider.market.MarketingQueryProvider;
import com.wanmi.sbc.marketing.api.provider.marketingsuits.MarketingSuitsProvider;
import com.wanmi.sbc.marketing.api.provider.marketingsuits.MarketingSuitsQueryProvider;
import com.wanmi.sbc.marketing.api.request.market.*;
import com.wanmi.sbc.marketing.api.request.marketingsuits.MarketingSuitsByMarketingIdRequest;
import com.wanmi.sbc.marketing.api.request.marketingsuits.MarketingSuitsSaveRequest;
import com.wanmi.sbc.marketing.api.response.market.MarketingGetByIdResponse;
import com.wanmi.sbc.marketing.api.response.marketingsuits.MarketingSuitsByMarketingIdResponse;
import com.wanmi.sbc.marketing.bean.constant.MarketingErrorCode;
import com.wanmi.sbc.marketing.bean.dto.SkuExistsDTO;
import com.wanmi.sbc.marketing.bean.vo.MarketingForEndVO;
import com.wanmi.sbc.marketing.bean.vo.MarketingScopeVO;
import com.wanmi.sbc.marketing.bean.vo.MarketingVO;
import com.wanmi.sbc.util.CommonUtil;
import com.wanmi.sbc.util.OperateLogMQUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Api(tags = "MarketingController", description = "营销服务API")
@RestController
@RequestMapping("/marketing")
public class MarketingController {

    @Autowired
    private MarketingQueryProvider marketingQueryProvider;

    @Autowired
    private MarketingProvider marketingProvider;

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private EsGoodsInfoElasticProvider esGoodsInfoElasticProvider;

    @Autowired
    private GoodsIntervalPriceService goodsIntervalPriceService;

    @Autowired
    private OperateLogMQUtil operateLogMQUtil;

    @Autowired
    private MarketingSuitsProvider marketingSuitsSaveProvider;
    @Autowired
    private MarketingSuitsQueryProvider marketingSuitsQueryProvider;


    /**
     * 查询在相同类型的营销活动中，skuList是否存在重复
     * @param skuExistsDTO {@link SkuExistsDTO}
     * @return
     */
    @ApiOperation(value = "查询在相同类型的营销活动中，skuList是否存在重复")
    @RequestMapping(value = "/sku/exists", method = RequestMethod.POST)
    public BaseResponse<List<String>> ifSkuExists(@RequestBody @Valid SkuExistsDTO skuExistsDTO) {
        return BaseResponse.success(marketingQueryProvider.queryExistsSkuByMarketingType(ExistsSkuByMarketingTypeRequest.builder()
                .storeId(commonUtil.getStoreId())
                .skuExistsDTO(skuExistsDTO)
                .build()).getContext());
    }

    /**
     * 删除营销活动
     * @param marketingId
     * @return
     */
    @ApiOperation(value = "删除营销活动")
    @ApiImplicitParam(paramType = "path", dataType = "Long", name = "marketingId", value = "营销Id", required = true)
    @RequestMapping(value = "/delete/{marketingId}", method = RequestMethod.DELETE)
    @Transactional
    public BaseResponse deleteMarketingId(@PathVariable("marketingId")Long marketingId){
        //未开始的营销活动，才能删除
        MarketingGetByIdRequest marketingGetByIdRequest = new MarketingGetByIdRequest();
        marketingGetByIdRequest.setMarketingId(marketingId);
        MarketingVO marketing = marketingQueryProvider.getById(marketingGetByIdRequest).getContext().getMarketingVO();
        if(marketing != null){
            if(LocalDateTime.now().isBefore(marketing.getBeginTime())){
                MarketingDeleteByIdRequest marketingDeleteByIdRequest = new MarketingDeleteByIdRequest();
                marketingDeleteByIdRequest.setMarketingId(marketingId);
                marketingProvider.deleteById(marketingDeleteByIdRequest);
                List<String> skuIds = marketing.getMarketingScopeList().stream().map(MarketingScopeVO::getScopeId).collect(Collectors.toList());
                // 更新es
                if(CollectionUtils.isNotEmpty(skuIds)){
                    esGoodsInfoElasticProvider.initEsGoodsInfo(EsGoodsInfoRequest.builder().skuIds(skuIds).build());
                }
                String name = getMarketingName(marketingId);
                operateLogMQUtil.convertAndSend("营销","删除促销活动","删除促销活动："+ name);
                return BaseResponse.SUCCESSFUL();

            }else{
                throw new SbcRuntimeException(MarketingErrorCode.MARKETING_CANNOT_DELETE);
            }
        }else{
            throw new SbcRuntimeException(MarketingErrorCode.NOT_EXIST);
        }
    }

    /**
     * 暂停营销活动
     * @param marketingId
     * @return
     */
    @ApiOperation(value = "暂停营销活动")
    @ApiImplicitParam(paramType = "path", dataType = "Long", name = "marketingId", value = "营销Id", required = true)
    @RequestMapping(value = "/pause/{marketingId}", method = RequestMethod.PUT)
    @Transactional
    public BaseResponse pauseMarketingId(@PathVariable("marketingId")Long marketingId){
        //进行中的营销活动，才能暂停
        MarketingGetByIdRequest marketingGetByIdRequest = new MarketingGetByIdRequest();
        marketingGetByIdRequest.setMarketingId(marketingId);
        MarketingVO marketing = marketingQueryProvider.getById(marketingGetByIdRequest).getContext().getMarketingVO();
        if(marketing != null){
            //如果现在时间在活动开始之前或者活动已经结束
            if(LocalDateTime.now().isBefore(marketing.getBeginTime()) || LocalDateTime.now().isAfter(marketing.getEndTime())){
                throw new SbcRuntimeException(MarketingErrorCode.MARKETING_CANNOT_PAUSE);
            }else{
                MarketingPauseByIdRequest marketingPauseByIdRequest = MarketingPauseByIdRequest.builder().build();
                marketingPauseByIdRequest.setMarketingId(marketingId);
                marketingProvider.pauseById(marketingPauseByIdRequest);

                List<String> skuIds = marketing.getMarketingScopeList().stream().map(MarketingScopeVO::getScopeId).collect(Collectors.toList());

                // 更新es
                if(CollectionUtils.isNotEmpty(skuIds)){
                    esGoodsInfoElasticProvider.initEsGoodsInfo(EsGoodsInfoRequest.builder().skuIds(skuIds).build());
                }
                operateLogMQUtil.convertAndSend("营销","暂停促销活动",
                        "暂停促销活动："+ getMarketingName(marketingId));
                return BaseResponse.SUCCESSFUL();
            }
        }else{
            throw new SbcRuntimeException(MarketingErrorCode.NOT_EXIST);
        }
    }

    /**
     * 开始营销活动
     * @param marketingId
     * @return
     */
    @ApiOperation(value = "开始营销活动")
    @ApiImplicitParam(paramType = "path", dataType = "Long", name = "marketingId", value = "营销Id", required = true)
    @RequestMapping(value = "/start/{marketingId}", method = RequestMethod.PUT)
    @Transactional
    public BaseResponse startMarketingId(@PathVariable("marketingId")Long marketingId){
        //进行中的营销活动，才能暂停
        MarketingGetByIdRequest marketingGetByIdRequest = new MarketingGetByIdRequest();
        marketingGetByIdRequest.setMarketingId(marketingId);
        MarketingVO marketing = marketingQueryProvider.getById(marketingGetByIdRequest).getContext().getMarketingVO();
        if(marketing != null){
            //如果现在时间在活动开始之前或者活动已经结束，或者当前状态是开始状态
            if(LocalDateTime.now().isBefore(marketing.getBeginTime()) || LocalDateTime.now().isAfter(marketing.getEndTime())
                    || marketing.getIsPause() == BoolFlag.NO){
                throw new SbcRuntimeException(MarketingErrorCode.MARKETING_CANNOT_START);
            }else{
                MarketingStartByIdRequest marketingStartByIdRequest = new MarketingStartByIdRequest();
                marketingStartByIdRequest.setMarketingId(marketingId);
                marketingProvider.startById(marketingStartByIdRequest);
                List<String> skuIds = marketing.getMarketingScopeList().stream().map(MarketingScopeVO::getScopeId).collect(Collectors.toList());
                // 更新es
                if(CollectionUtils.isNotEmpty(skuIds)){
                    esGoodsInfoElasticProvider.initEsGoodsInfo(EsGoodsInfoRequest.builder().skuIds(skuIds).build());
                }
                operateLogMQUtil.convertAndSend("营销","开启促销活动",
                        "开启促销活动："+ getMarketingName(marketingId));
                return BaseResponse.SUCCESSFUL();
            }
        }else{
            throw new SbcRuntimeException(MarketingErrorCode.NOT_EXIST);
        }
    }

    /**
     * 根据营销Id获取营销详细信息
     * @param marketingId
     * @return
     */
    @ApiOperation(value = "根据营销Id获取营销详细信息")
    @ApiImplicitParam(paramType = "path", dataType = "Long", name = "marketingId", value = "营销Id", required = true)
    @RequestMapping(value = "/{marketingId}", method = RequestMethod.GET)
    public BaseResponse<MarketingForEndVO> getMarketingById(@PathVariable("marketingId")Long marketingId){
        MarketingGetByIdRequest marketingGetByIdRequest = new MarketingGetByIdRequest();
        marketingGetByIdRequest.setMarketingId(marketingId);
        MarketingForEndVO marketingResponse = marketingQueryProvider.getByIdForSupplier(marketingGetByIdRequest)
                .getContext().getMarketingForEndVO();
        if(marketingResponse.getStoreId() != null && commonUtil.getStoreId() != null && commonUtil.getStoreId().longValue() != marketingResponse.getStoreId()){
            throw new SbcRuntimeException(MarketingErrorCode.MARKETING_NO_AUTH_TO_VIEW);
        }else{
            return BaseResponse.success(marketingResponse);
        }
    }

    /**
     *  商家-营销中心-促销活动-组合购活动详情
     *  @param marketingId
     *  @return
     */
    @ApiOperation(value = "组合购活动详情")
    @ApiImplicitParam(paramType = "path", dataType = "Long", name = "marketingId", value = "营销Id", required = true)
    @RequestMapping(value = "/getGoodsSuitsDetail/{marketingId}", method = RequestMethod.GET)
    public BaseResponse<MarketingSuitsByMarketingIdResponse> getGoodsSuitsDetail(@PathVariable("marketingId")Long marketingId){
        MarketingSuitsByMarketingIdResponse response = marketingSuitsQueryProvider.getByMarketingIdForSupplier(
                MarketingSuitsByMarketingIdRequest.builder().marketingId(marketingId).build()).getContext();
        return BaseResponse.success(response);
    }


    /**
     * 根据营销Id获取关联商品信息
     * @param marketingId
     * @return
     */
    @ApiOperation(value = "根据营销Id获取关联商品信息")
    @ApiImplicitParam(paramType = "path", dataType = "Long", name = "marketingId", value = "营销Id", required = true)
    @RequestMapping(value = "/goods/{marketingId}", method = RequestMethod.GET)
    public BaseResponse<GoodsInfoResponse> getGoodsByMarketingId(@PathVariable("marketingId")Long marketingId){
        MarketingGetByIdRequest marketingGetByIdRequest = new MarketingGetByIdRequest();
        marketingGetByIdRequest.setMarketingId(marketingId);
        GoodsInfoResponseVO response = marketingQueryProvider.getGoodsById(marketingGetByIdRequest)
                .getContext().getGoodsInfoResponseVO();
        if(Objects.nonNull(response.getGoodsInfoPage()) && CollectionUtils.isNotEmpty(response.getGoodsInfoPage().getContent())) {
            GoodsIntervalPriceResponse priceResponse =
                    goodsIntervalPriceService.getGoodsIntervalPriceVOList(response.getGoodsInfoPage().getContent());
            response.setGoodsIntervalPrices(priceResponse.getGoodsIntervalPriceVOList());
            response.setGoodsInfoPage(new MicroServicePage<>(priceResponse.getGoodsInfoVOList()));
        }
        return BaseResponse.success(KsBeanUtil.convert(response, GoodsInfoResponse.class));
    }

    /**
     * 根据营销Id获取正进行中的营销Id
     * @param request 参数
     * @return
     */
    @ApiOperation(value = "根据营销Id获取正进行中的营销Id")
    @RequestMapping(value = "/isStart", method = RequestMethod.POST)
    public BaseResponse<List<String>> getGoodsByMarketingId(@RequestBody @Valid MarketingQueryByIdsRequest request){
        return BaseResponse.success(marketingQueryProvider.queryStartingByIds(request).getContext().getMarketingIdList());
    }

    /**
     *  公共方法获取促销活动名称
     * @param marketId
     * @return
     */
    private String getMarketingName(long marketId){
        MarketingGetByIdRequest request = new MarketingGetByIdRequest();
        request.setMarketingId(marketId);
        MarketingGetByIdResponse marketing = marketingQueryProvider.getById(request).getContext();
        return Objects.nonNull(marketing) ? marketing.getMarketingVO().getMarketingName() : " ";
    }

    /**
     *  创建组合购活动
     * @Param MarketingSuitsSaveRequest
     * @Return
     */
    @ApiOperation(value = "创建组合活动")
    @RequestMapping(value = "/saveSuits", method = RequestMethod.POST)
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse addSuits(@RequestBody @Valid MarketingSuitsSaveRequest marketingSuitsSaveRequest){
        marketingSuitsSaveRequest.setBaseStoreId(commonUtil.getStoreIdWithDefault());
        marketingSuitsSaveProvider.add(marketingSuitsSaveRequest);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     *  修改组合购活动
     * @Param MarketingSuitsSaveRequest
     * @Return
     */
    @ApiOperation(value = "修改组合活动")
    @RequestMapping(value = "/modifySuits", method = RequestMethod.POST)
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse modifySuits(@RequestBody @Valid MarketingSuitsSaveRequest marketingSuitsSaveRequest){
        if(Objects.isNull(marketingSuitsSaveRequest.getMarketingId())){
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        marketingSuitsSaveRequest.setBaseStoreId(commonUtil.getStoreIdWithDefault());
        marketingSuitsSaveProvider.modify(marketingSuitsSaveRequest);
        return BaseResponse.SUCCESSFUL();
    }



}
