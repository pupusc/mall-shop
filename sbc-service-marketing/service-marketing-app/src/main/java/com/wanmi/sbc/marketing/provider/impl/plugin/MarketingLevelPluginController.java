package com.wanmi.sbc.marketing.provider.impl.plugin;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.customer.api.provider.paidcardcustomerrel.PaidCardCustomerRelQueryProvider;
import com.wanmi.sbc.customer.bean.vo.CustomerVO;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.marketing.MarketingPluginService;
import com.wanmi.sbc.marketing.api.provider.plugin.MarketingLevelPluginProvider;
import com.wanmi.sbc.marketing.api.request.plugin.MarketingLevelGoodsListFilterRequest;
import com.wanmi.sbc.marketing.api.response.plugin.MarketingLevelGoodsListFilterResponse;
import com.wanmi.sbc.marketing.common.mapper.CustomerMapper;
import com.wanmi.sbc.marketing.common.mapper.GoodsInfoMapper;
import com.wanmi.sbc.marketing.plugin.impl.CustomerLevelPlugin;
import com.wanmi.sbc.marketing.plugin.impl.EnterprisePlugin;
import com.wanmi.sbc.marketing.plugin.impl.PaidCardPlugin;
import com.wanmi.sbc.marketing.request.MarketingPluginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>主插件服务操作接口</p>
 * author: daiyitian
 * Date: 2018-11-28
 */
@Validated
@RestController
public class MarketingLevelPluginController implements MarketingLevelPluginProvider {

    @Autowired
    private CustomerLevelPlugin customerLevelPlugin;

    @Autowired
    private PaidCardPlugin paidCardPlugin;

    @Autowired
    private MarketingPluginService marketingPluginService;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private GoodsInfoMapper goodsInfoMapper;

    @Autowired
    private EnterprisePlugin enterprisePlugin;

    /**
     * 商品列表处理
     * @param request 商品列表处理结构 {@link MarketingLevelGoodsListFilterRequest}
     * @return 处理后的商品列表 {@link MarketingLevelGoodsListFilterResponse}
     */
    @Override
    public BaseResponse<MarketingLevelGoodsListFilterResponse> goodsListFilter(@RequestBody @Valid
                                                                                      MarketingLevelGoodsListFilterRequest
                                                                                          request){
        CustomerVO customer = customerMapper.customerDTOToCustomerVO(request.getCustomerDTO());
        List<GoodsInfoVO> voList = goodsInfoMapper.goodsInfoDTOsToGoodsInfoVOs(request.getGoodsInfos());
        MarketingPluginRequest pluginRequest = new MarketingPluginRequest();
        //设定等级
        pluginRequest.setLevelMap(marketingPluginService.getCustomerLevels(voList, customer));
        //设定营销
        pluginRequest.setMarketingMap(marketingPluginService.getMarketing(voList, pluginRequest.getLevelMap(), request.getCustomerDTO()));
        pluginRequest.setCustomer(customer);
        customerLevelPlugin.goodsListFilter(voList, pluginRequest);
        paidCardPlugin.goodsListFilter(voList, pluginRequest);
        enterprisePlugin.goodsListFilter(voList, pluginRequest);
        return BaseResponse.success(new MarketingLevelGoodsListFilterResponse(voList));
    }

}
