package com.wanmi.sbc.marketing.provider.impl.plugin;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.bean.vo.CommonLevelVO;
import com.wanmi.sbc.customer.bean.vo.CustomerVO;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.marketing.MarketingPluginService;
import com.wanmi.sbc.marketing.api.provider.plugin.MarketingPluginQueryProvider;
import com.wanmi.sbc.marketing.api.request.plugin.MarketingPluginByGoodsInfoListAndCustomerRequest;
import com.wanmi.sbc.marketing.api.request.plugin.MarketingPluginGetCustomerLevelsByStoreIdsRequest;
import com.wanmi.sbc.marketing.api.request.plugin.MarketingPluginGetCustomerLevelsRequest;
import com.wanmi.sbc.marketing.api.response.plugin.MarketingPluginByGoodsInfoListAndCustomerLevelResponse;
import com.wanmi.sbc.marketing.api.response.plugin.MarketingPluginGetCustomerLevelsResponse;
import com.wanmi.sbc.marketing.bean.vo.MarketingViewVO;
import com.wanmi.sbc.marketing.common.mapper.CustomerMapper;
import com.wanmi.sbc.marketing.common.mapper.GoodsInfoMapper;
import com.wanmi.sbc.marketing.common.mapper.MarketingViewMapper;
import com.wanmi.sbc.marketing.common.response.MarketingResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p></p>
 * author: sunkun
 * Date: 2018-11-22
 */
@RestController
@Validated
public class MarketingPluginQueryController implements MarketingPluginQueryProvider {

    @Autowired
    private MarketingPluginService marketingPluginService;

    @Autowired
    private GoodsInfoMapper goodsInfoMapper;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private MarketingViewMapper marketingViewMapper;

    /**
     * 获取营销
     *
     * @param request 获取营销请求结构 {@link MarketingPluginByGoodsInfoListAndCustomerRequest}
     * @return
     */
    @Override
    public BaseResponse<MarketingPluginByGoodsInfoListAndCustomerLevelResponse> getByGoodsInfoListAndCustomer(@RequestBody @Valid MarketingPluginByGoodsInfoListAndCustomerRequest request) {
        List<GoodsInfoVO> voList =goodsInfoMapper.goodsInfoDTOsToGoodsInfoVOs(request.getGoodsInfoList());
        Map<String, List<MarketingResponse>> marketing = marketingPluginService.getMarketing(voList,
                marketingPluginService.getCustomerLevels(voList, customerMapper.customerDTOToCustomerVO(request.getCustomerDTO())),
                request.getCustomerDTO());
        HashMap<String, List<MarketingViewVO>> marketingMap = new HashMap<>();
        marketing.forEach((k, v) -> {
            marketingMap.put(k, marketingViewMapper.marketingResponsesToMarketingViewVOs(v));
        });
        return BaseResponse.success(new MarketingPluginByGoodsInfoListAndCustomerLevelResponse(marketingMap));
    }

    /**
     * 获取会员等级
     *
     * @param request 获取会员等级请求结构 {@link MarketingPluginGetCustomerLevelsRequest}
     * @return
     */
    @Override
    public BaseResponse<MarketingPluginGetCustomerLevelsResponse> getCustomerLevelsByGoodsInfoListAndCustomer(@RequestBody @Valid MarketingPluginGetCustomerLevelsRequest request) {
        HashMap<Long, CommonLevelVO> customerLevels = marketingPluginService.getCustomerLevels(
                KsBeanUtil.convertList(request.getGoodsInfoList(), GoodsInfoVO.class),
                KsBeanUtil.convert(request.getCustomer(), CustomerVO.class));
        return BaseResponse.success(MarketingPluginGetCustomerLevelsResponse.builder().commonLevelVOMap(customerLevels).build());
    }

    /**
     * 获取会员等级
     * @param request 获取会员等级请求结构 {@link MarketingPluginGetCustomerLevelsByStoreIdsRequest}
     * @return
     */
    @Override
    public BaseResponse<MarketingPluginGetCustomerLevelsResponse> getCustomerLevelsByStoreIds(@RequestBody @Valid MarketingPluginGetCustomerLevelsByStoreIdsRequest request) {
        HashMap<Long, CommonLevelVO> customerLevelsMap = marketingPluginService.getCustomerLevelsByStoreIds(request.getStoreIds(),
                KsBeanUtil.convert(request.getCustomer(), CustomerVO.class));
        return BaseResponse.success(MarketingPluginGetCustomerLevelsResponse.builder().commonLevelVOMap(customerLevelsMap).build());
    }
}
