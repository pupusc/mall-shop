package com.wanmi.sbc.distribution;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.customer.api.provider.company.CompanyInfoQueryProvider;
import com.wanmi.sbc.customer.api.provider.customer.CustomerQueryProvider;
import com.wanmi.sbc.customer.api.provider.store.StoreQueryProvider;
import com.wanmi.sbc.customer.api.request.company.CompanyInfoForDistributionRecordRequest;
import com.wanmi.sbc.customer.api.request.customer.CustomerDetailListByPageFordrRequest;
import com.wanmi.sbc.customer.api.request.store.ListStoreByNameRequest;
import com.wanmi.sbc.customer.api.response.customer.CustomerDetailListByPageResponse;
import com.wanmi.sbc.customer.api.response.store.StoreListForDistributionResponse;
import com.wanmi.sbc.goods.api.provider.info.GoodsInfoQueryProvider;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoPageRequest;
import com.wanmi.sbc.goods.api.response.info.GoodsInfoPageResponse;
import com.wanmi.sbc.marketing.api.provider.distributionrecord.DistributionRecordQueryProvider;
import com.wanmi.sbc.marketing.api.request.distributionrecord.DistributionRecordPageRequest;
import com.wanmi.sbc.marketing.api.response.distributionrecord.DistributionRecordPageResponse;
import com.wanmi.sbc.util.OperateLogMQUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author : baijz
 * @Date : 2019/2/25 10 46
 * @Description : 分销记录控制器
 */
@Api(tags = "DistributionRecordController", description = "分销记录控制器")
@Slf4j
@RestController
@RequestMapping("/distribution/record")
public class DistributionRecordController {

    @Autowired
    private DistributionRecordQueryProvider distributionRecordQueryProvider;

    @Autowired
    private StoreQueryProvider storeQueryProvider;

    @Autowired
    private CompanyInfoQueryProvider companyInfoQueryProvider;

    @Autowired
    private CustomerQueryProvider customerQueryProvider;

    @Autowired
    private GoodsInfoQueryProvider goodsInfoQueryProvider;


    /**
     * 分页查询分销记录信息
     * @param distributionRecordPageRequest
     * @return
     */
    @ApiModelProperty(value = "分页查询分销记录信息")
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public BaseResponse<DistributionRecordPageResponse> page(@RequestBody @Valid DistributionRecordPageRequest distributionRecordPageRequest){
        BaseResponse<DistributionRecordPageResponse> res = distributionRecordQueryProvider.page(distributionRecordPageRequest);
        return res;
    }

    /**
     * 根据店铺名称模糊查询名称
     * @return
     */
    @ApiModelProperty(value = "根据名称模糊查询店铺信息")
    @RequestMapping(value = "/listStore" ,method = RequestMethod.POST)
    public BaseResponse<StoreListForDistributionResponse> listStoreInfo(@RequestBody @Valid ListStoreByNameRequest request){
        return storeQueryProvider.listByStoreName(request);
    }

    /**
     * 根据company编号模糊查询company信息
     * @param recordRequest
     * @return
     */
    @ApiModelProperty(value = "根据编码模糊查询company信息")
    @RequestMapping(value = "/listCompany" ,method = RequestMethod.POST)
    public BaseResponse<StoreListForDistributionResponse> listCompanyInfo
            (@RequestBody @Valid CompanyInfoForDistributionRecordRequest recordRequest){
        return companyInfoQueryProvider.queryByCompanyCode(recordRequest);

    }


    /**
     *  模糊查询会员信息
     * @param recordRequest
     * @return
     */
    @ApiModelProperty(value = "根据会员的名称或账号模糊查询会员信息")
    @RequestMapping(value = "/listCustomer" ,method = RequestMethod.POST)
    public BaseResponse<CustomerDetailListByPageResponse> pageCustomerInfo(@RequestBody @Valid
                                                                                   CustomerDetailListByPageFordrRequest recordRequest){
        return customerQueryProvider.listCustomerDetailForDistributionRecord(recordRequest);
    }


    /**
     * 根据商品的名称和编码模糊查询商品信息
     * @param request
     * @return
     */
    @ApiModelProperty(value = "根据商品的名称和编码模糊查询商品信息")
    @RequestMapping(value = "/listGoodsInfo" ,method = RequestMethod.POST)
    public BaseResponse<GoodsInfoPageResponse> queryGoodsInfoListByNameOrNo(@RequestBody @Valid GoodsInfoPageRequest
                                                                                    request){
        return goodsInfoQueryProvider.page(request);
    }
}
