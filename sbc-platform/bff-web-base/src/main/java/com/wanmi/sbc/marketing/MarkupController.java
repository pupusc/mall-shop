package com.wanmi.sbc.marketing;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.bean.dto.CustomerDTO;
import com.wanmi.sbc.goods.api.provider.info.GoodsInfoQueryProvider;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoViewByIdsRequest;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.marketing.api.provider.gift.FullGiftQueryProvider;
import com.wanmi.sbc.marketing.api.provider.market.MarketingQueryProvider;
import com.wanmi.sbc.marketing.api.provider.markup.MarkupQueryProvider;
import com.wanmi.sbc.marketing.api.request.gift.FullGiftLevelListByMarketingIdAndCustomerRequest;
import com.wanmi.sbc.marketing.api.request.gift.FullGiftLevelListByMarketingIdRequest;
import com.wanmi.sbc.marketing.api.request.market.MarketingGetByIdRequest;
import com.wanmi.sbc.marketing.api.request.markup.MarkupLevelByIdRequest;
import com.wanmi.sbc.marketing.api.response.gift.FullGiftLevelListByMarketingIdAndCustomerResponse;
import com.wanmi.sbc.marketing.api.response.markup.MarkupLevelByIdResponse;
import com.wanmi.sbc.marketing.bean.enums.MarketingSubType;
import com.wanmi.sbc.marketing.bean.vo.*;
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


@Api(tags = "MarkupController", description = "加价购营销服务API")
@RestController
@RequestMapping("/markup")
@Validated
public class MarkupController {

    @Autowired
    private MarkupQueryProvider markupQueryProvider;

    @Autowired
    private GoodsInfoQueryProvider goodsInfoQueryProvider;

    @Autowired
    private MarketingQueryProvider marketingQueryProvider;

    @Autowired
    private CommonUtil commonUtil;

    /**
     * 根据营销Id获取加价购信息
     *
     * @param marketingId 活动ID
     * @return
     */
    @ApiOperation(value = "根据营销Id获取加价购信息")
    @ApiImplicitParam(paramType = "path", dataType = "Long", name = "marketingId", value = "营销Id", required = true)
    @RequestMapping(value = "/{marketingId}", method = RequestMethod.GET)
    public BaseResponse<MarkupLevelByIdResponse> getGiftByMarketingId(@PathVariable("marketingId") Long marketingId) {
        MarkupLevelByIdRequest markupLevelByIdRequest = MarkupLevelByIdRequest
                .builder().customer(KsBeanUtil.convert(commonUtil.getCustomer(), CustomerDTO.class)).build();
        markupLevelByIdRequest.setMarketingId(marketingId);
        return markupQueryProvider.getLevelById(markupLevelByIdRequest);
    }

    /**
     * 未登录时根据营销Id获取加价购信息
     * @param marketingId 活动ID
     * @return
     */
    @ApiOperation(value = "未登录时根据营销Id获取加价购信息")
    @ApiImplicitParam(paramType = "path", dataType = "Long", name = "marketingId", value = "营销Id", required = true)
    @RequestMapping(value = "/unLogin/{marketingId}", method = RequestMethod.GET)
    public BaseResponse<MarkupLevelByIdResponse> getGiftByMarketingIdWithOutLogin(@PathVariable("marketingId")Long marketingId) {
        MarkupLevelByIdRequest markupLevelByIdRequest = MarkupLevelByIdRequest.builder().build();
        markupLevelByIdRequest.setMarketingId(marketingId);
        return markupQueryProvider.getLevelById(markupLevelByIdRequest);
    }

    /**
     * 根据营销Id和规则id获取加价购信息（提供给立即购买->确认订单->加价购选择查询）
     *
     * @param marketingId 活动ID
     * @param levelId 最高规则ID
     * @return
     */
    @ApiOperation(value = "根据营销Id获取加价购信息")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "path", dataType = "Long", name = "marketingId", value = "营销Id", required = true),
        @ApiImplicitParam(paramType = "path", dataType = "Long", name = "levelId", value = "最高规则Id", required = true)
    })
    @RequestMapping(value = "/{marketingId}/{levelId}", method = RequestMethod.GET)
    public BaseResponse<MarkupLevelByIdResponse> getGiftByMarketingId(
            @PathVariable("marketingId") Long marketingId, @PathVariable("levelId") Long levelId) {
        MarkupLevelByIdResponse response = new MarkupLevelByIdResponse();
        response.setLevelList(Collections.emptyList());
        response.setGoodsInfoVOList(Collections.emptyList());
        MarketingGetByIdRequest idRequest = new MarketingGetByIdRequest();
        idRequest.setMarketingId(marketingId);
        MarketingVO marketingVO = marketingQueryProvider.getById(idRequest).getContext().getMarketingVO();
        if(Objects.isNull(marketingVO)){
            return BaseResponse.success(response);
        }
        response.setMarketingSubType(marketingVO.getSubType());

        MarkupLevelByIdRequest request = new MarkupLevelByIdRequest();
        request.setMarketingId(marketingId);
        //所有加价购
        List<MarkupLevelVO> levelList = markupQueryProvider.getLevelById(request).getContext().getLevelList();

        if (CollectionUtils.isNotEmpty(levelList)) {
            // 按子类型 由升序排序
            if (marketingVO.getSubType() == MarketingSubType.MARKUP) {
                levelList.sort(Comparator.comparing(MarkupLevelVO::getLevelAmount));
            }

            //只返回当前等级以及低级活动加价购规则
            List<MarkupLevelVO> levelVos = new ArrayList<>();
            for (MarkupLevelVO level : levelList) {
                if (level.getId().equals(levelId)) {
                    levelVos.add(level);
                    break;
                }
                levelVos.add(level);
            }
            response.setLevelList(levelVos);
            //加价购详情信息
            if (CollectionUtils.isNotEmpty(response.getLevelList())) {
                List<String> skuIds = response.getLevelList().stream().filter(s -> CollectionUtils.isNotEmpty(s.getMarkupLevelDetailList()))
                        .flatMap(s -> s.getMarkupLevelDetailList().stream())
                        .map(MarkupLevelDetailVO::getGoodsInfoId).collect(Collectors.toList());
                response.setGoodsInfoVOList(goodsInfoQueryProvider.listViewByIds(GoodsInfoViewByIdsRequest.builder()
                        .goodsInfoIds(skuIds).isHavSpecText(Constants.yes).build()).getContext().getGoodsInfos());
            }
        }
        return BaseResponse.success(response);
    }
}
