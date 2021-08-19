package com.wanmi.sbc.marketing;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.bean.dto.CustomerDTO;
import com.wanmi.sbc.goods.api.provider.info.GoodsInfoQueryProvider;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoViewByIdsRequest;
import com.wanmi.sbc.marketing.api.provider.gift.FullGiftQueryProvider;
import com.wanmi.sbc.marketing.api.provider.market.MarketingQueryProvider;
import com.wanmi.sbc.marketing.api.request.gift.FullGiftLevelListByMarketingIdAndCustomerRequest;
import com.wanmi.sbc.marketing.api.request.gift.FullGiftLevelListByMarketingIdRequest;
import com.wanmi.sbc.marketing.api.request.market.MarketingGetByIdRequest;
import com.wanmi.sbc.marketing.api.response.gift.FullGiftLevelListByMarketingIdAndCustomerResponse;
import com.wanmi.sbc.marketing.bean.enums.MarketingSubType;
import com.wanmi.sbc.marketing.bean.vo.MarketingFullGiftDetailVO;
import com.wanmi.sbc.marketing.bean.vo.MarketingFullGiftLevelVO;
import com.wanmi.sbc.marketing.bean.vo.MarketingVO;
import com.wanmi.sbc.util.CommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;


@Api(tags = "MarketingFullGiftController", description = "满赠营销服务API")
@RestController
@RequestMapping("/gift")
@Validated
public class MarketingFullGiftController {

    @Autowired
    private FullGiftQueryProvider fullGiftQueryProvider;

    @Autowired
    private GoodsInfoQueryProvider goodsInfoQueryProvider;

    @Autowired
    private MarketingQueryProvider marketingQueryProvider;

    @Autowired
    private CommonUtil commonUtil;

    /**
     * 根据营销Id获取赠品信息
     *
     * @param marketingId 活动ID
     * @return
     */
    @ApiOperation(value = "根据营销Id获取赠品信息")
    @ApiImplicitParam(paramType = "path", dataType = "Long", name = "marketingId", value = "营销Id", required = true)
    @RequestMapping(value = "/{marketingId}", method = RequestMethod.GET)
    public BaseResponse<FullGiftLevelListByMarketingIdAndCustomerResponse> getGiftByMarketingId(@PathVariable("marketingId") Long marketingId) {
        FullGiftLevelListByMarketingIdAndCustomerRequest fullgiftRequest = FullGiftLevelListByMarketingIdAndCustomerRequest
                .builder().customer(KsBeanUtil.convert(commonUtil.getCustomer(), CustomerDTO.class)).build();
        fullgiftRequest.setMarketingId(marketingId);
        return fullGiftQueryProvider.listGiftByMarketingIdAndCustomer(fullgiftRequest);
    }

    /**
     * 未登录时根据营销Id获取赠品信息
     * @param marketingId 活动ID
     * @return
     */
    @ApiOperation(value = "未登录时根据营销Id获取赠品信息")
    @ApiImplicitParam(paramType = "path", dataType = "Long", name = "marketingId", value = "营销Id", required = true)
    @RequestMapping(value = "/unLogin/{marketingId}", method = RequestMethod.GET)
    public BaseResponse<FullGiftLevelListByMarketingIdAndCustomerResponse> getGiftByMarketingIdWithOutLogin(@PathVariable("marketingId")Long marketingId) {
        FullGiftLevelListByMarketingIdAndCustomerRequest fullgiftRequest = FullGiftLevelListByMarketingIdAndCustomerRequest.builder().build();
        fullgiftRequest.setMarketingId(marketingId);
        return fullGiftQueryProvider.listGiftByMarketingIdAndCustomer(fullgiftRequest);
    }

    /**
     * 根据营销Id和规则id获取赠品信息（提供给立即购买->确认订单->赠品选择查询）
     *
     * @param marketingId 活动ID
     * @param levelId 最高规则ID
     * @return
     */
    @ApiOperation(value = "根据营销Id获取赠品信息")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "path", dataType = "Long", name = "marketingId", value = "营销Id", required = true),
        @ApiImplicitParam(paramType = "path", dataType = "Long", name = "levelId", value = "最高规则Id", required = true)
    })
    @RequestMapping(value = "/{marketingId}/{levelId}", method = RequestMethod.GET)
    public BaseResponse<FullGiftLevelListByMarketingIdAndCustomerResponse> getGiftByMarketingId(
            @PathVariable("marketingId") Long marketingId, @PathVariable("levelId") Long levelId) {
        FullGiftLevelListByMarketingIdAndCustomerResponse response = new FullGiftLevelListByMarketingIdAndCustomerResponse();
        response.setLevelList(Collections.emptyList());
        response.setGiftList(Collections.emptyList());
        MarketingGetByIdRequest idRequest = new MarketingGetByIdRequest();
        idRequest.setMarketingId(marketingId);
        MarketingVO marketingVO = marketingQueryProvider.getById(idRequest).getContext().getMarketingVO();
        if(Objects.isNull(marketingVO)){
            return BaseResponse.success(response);
        }
        response.setMarketingSubType(marketingVO.getSubType());

        FullGiftLevelListByMarketingIdRequest request = new FullGiftLevelListByMarketingIdRequest();
        request.setMarketingId(marketingId);
        //所有赠品
        List<MarketingFullGiftLevelVO> levels = fullGiftQueryProvider.listLevelByMarketingId(request).getContext().getFullGiftLevelVOList();
        if (CollectionUtils.isNotEmpty(levels)) {
            // 按子类型 由升序排序
            if (marketingVO.getSubType() == MarketingSubType.GIFT_FULL_AMOUNT) {
                levels.sort(Comparator.comparing(MarketingFullGiftLevelVO::getFullAmount));
            }else if (marketingVO.getSubType() == MarketingSubType.GIFT_FULL_COUNT) {
                levels.sort(Comparator.comparing(MarketingFullGiftLevelVO::getFullCount));
            }

            //只返回当前等级以及低级活动赠品规则
            List<MarketingFullGiftLevelVO> levelVos = new ArrayList<>();
            for (MarketingFullGiftLevelVO level : levels) {
                if (level.getGiftLevelId().equals(levelId)) {
                    levelVos.add(level);
                    break;
                }
                levelVos.add(level);
            }
            response.setLevelList(levelVos);
            //赠品详情信息
            if (CollectionUtils.isNotEmpty(response.getLevelList())) {
                List<String> skuIds = response.getLevelList().stream().filter(s -> CollectionUtils.isNotEmpty(s.getFullGiftDetailList()))
                        .flatMap(s -> s.getFullGiftDetailList().stream())
                        .map(MarketingFullGiftDetailVO::getProductId).collect(Collectors.toList());
                response.setGiftList(goodsInfoQueryProvider.listViewByIds(GoodsInfoViewByIdsRequest.builder()
                        .goodsInfoIds(skuIds).isHavSpecText(Constants.yes).build()).getContext().getGoodsInfos());
            }
        }
        return BaseResponse.success(response);
    }
}
