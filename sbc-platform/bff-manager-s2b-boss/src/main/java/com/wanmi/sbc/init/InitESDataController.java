package com.wanmi.sbc.init;

import com.wanmi.sbc.common.annotation.MultiSubmit;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.customer.api.provider.ares.CustomerAresProvider;
import com.wanmi.sbc.elastic.api.provider.coupon.EsCouponActivityProvider;
import com.wanmi.sbc.elastic.api.provider.coupon.EsCouponInfoProvider;
import com.wanmi.sbc.elastic.api.provider.customer.EsCustomerDetailProvider;
import com.wanmi.sbc.elastic.api.provider.customer.EsDistributionCustomerProvider;
import com.wanmi.sbc.elastic.api.provider.customer.EsStoreEvaluateSumQueryProvider;
import com.wanmi.sbc.elastic.api.provider.goods.EsGoodsInfoElasticProvider;
import com.wanmi.sbc.elastic.api.provider.groupon.EsGrouponActivityProvider;
import com.wanmi.sbc.elastic.api.provider.operationlog.EsOperationLogQueryProvider;
import com.wanmi.sbc.elastic.api.provider.searchterms.EsSearchAssociationalWordProvider;
import com.wanmi.sbc.elastic.api.provider.sensitivewords.EsSensitiveWordsProvider;
import com.wanmi.sbc.elastic.api.provider.standard.EsStandardProvider;
import com.wanmi.sbc.elastic.api.provider.systemresource.EsSystemResourceProvider;
import com.wanmi.sbc.elastic.api.request.coupon.EsCouponActivityInitRequest;
import com.wanmi.sbc.elastic.api.request.coupon.EsCouponInfoInitRequest;
import com.wanmi.sbc.elastic.api.request.customer.EsCustomerDetailInitRequest;
import com.wanmi.sbc.elastic.api.request.customer.EsDistributionCustomerPageRequest;
import com.wanmi.sbc.elastic.api.request.customer.EsStoreEvaluateSumPageRequest;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsInfoRequest;
import com.wanmi.sbc.elastic.api.request.groupon.EsGrouponActivityPageRequest;
import com.wanmi.sbc.elastic.api.request.operationlog.EsOperationLogListRequest;
import com.wanmi.sbc.elastic.api.request.searchterms.EsSearchAssociationalWordPageRequest;
import com.wanmi.sbc.elastic.api.request.sensitivewords.EsSensitiveWordsQueryRequest;
import com.wanmi.sbc.elastic.api.request.standard.EsStandardInitRequest;
import com.wanmi.sbc.elastic.api.request.systemresource.EsSystemResourcePageRequest;
import com.wanmi.sbc.goods.api.provider.ares.GoodsAresProvider;
import com.wanmi.sbc.order.api.provider.returnorder.ReturnOrderProvider;
import com.wanmi.sbc.order.api.provider.trade.TradeProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Author: bail
 * Time: 2017/10/23.8:42
 */
@RestController
@RequestMapping("/init")
@Slf4j
@Api(description = "初始化ES服务",tags ="InitESDataController")
public class InitESDataController {
    @Autowired
    private CustomerAresProvider customerAresProvider;

    @Autowired
    private GoodsAresProvider goodsAresProvider;

    @Autowired
    private TradeProvider tradeProvider;

    @Autowired
    private ReturnOrderProvider returnOrderProvider;

    @Autowired
    private EsGoodsInfoElasticProvider esGoodsInfoElasticProvider;

    @Autowired
    private EsStandardProvider esStandardProvider;

    @Autowired
    private EsCouponActivityProvider esCouponActivityProvider;

    @Autowired
    private EsCouponInfoProvider esCouponInfoProvider;

    @Autowired
    private EsCustomerDetailProvider esCustomerDetailProvider;

    @Autowired
    private EsDistributionCustomerProvider esDistributionCustomerProvider;

    @Autowired
    private EsStoreEvaluateSumQueryProvider esStoreEvaluateSumQueryProvider;

    @Autowired
    private EsGrouponActivityProvider esGrouponActivityProvider;

    @Autowired
    private EsOperationLogQueryProvider esOperationLogQueryProvider;

    @Autowired
    private EsSearchAssociationalWordProvider esSearchAssociationalWordProvider;

    @Autowired
    private EsSensitiveWordsProvider esSensitiveWordsProvider;

    @Autowired
    private EsSystemResourceProvider esSystemResourceProvider;

    @ApiOperation(value = "初始化ES")
    @RequestMapping(value = "/initES", method = RequestMethod.GET)
    public BaseResponse initES(String type) {
        try {
            //1.不传type默认初始化所有数据
            if(StringUtils.isEmpty(type)){
                customerAresProvider.initCustomerData();
                customerAresProvider.initCustomerLevelData();
                customerAresProvider.initStoreCustomerRelaData();
                customerAresProvider.initEmployeeData();
                customerAresProvider.initStoreData();
                goodsAresProvider.initGoodsES();
                goodsAresProvider.initGoodsBrandES();
                goodsAresProvider.initGoodsCateES();
                goodsAresProvider.initStoreCateES();
            }

            //2.传入type,根据type进行初始化数据
            if("customer".equals(type)){
                customerAresProvider.initCustomerData();
            }else if("customer_level".equals(type)){
                customerAresProvider.initCustomerLevelData();
            }else if("store_customer".equals(type)){
                customerAresProvider.initStoreCustomerRelaData();
            }else if("employee".equals(type)){
                customerAresProvider.initEmployeeData();
            }else if("store".equals(type)){
                customerAresProvider.initStoreData();
            }else if("goods".equals(type)){
                goodsAresProvider.initGoodsES();
            }else if("goods_brand".equals(type)){
                goodsAresProvider.initGoodsBrandES();
            }else if("goods_cate".equals(type)){
                goodsAresProvider.initGoodsCateES();
            }else if("store_cate".equals(type)){
                goodsAresProvider.initStoreCateES();
            }
            return BaseResponse.SUCCESSFUL();
        } catch (Exception e) {
            log.error("Get customer level distribute view fail,the thrift request error,", e);
            return BaseResponse.FAILED();
        }
    }

    @ApiOperation(value = "初始化订单")
    @RequestMapping(value = "/initOrderEmployee", method = RequestMethod.GET)
    public BaseResponse initOrderEmployee() {
        tradeProvider.fillEmployeeId();
        returnOrderProvider.fillEmployeeId();
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 将mysql 商品信息 同步到-> ES中
     */
    @MultiSubmit
    @ApiOperation(value = "将mysql 商品信息 同步到-> ES中")
    @RequestMapping(value = "/goodsES", method = RequestMethod.POST)
    public BaseResponse initAllGoodsES(@RequestBody EsGoodsInfoRequest queryRequest) {
        esGoodsInfoElasticProvider.initEsGoodsInfo(queryRequest);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 将mysql 商品信息 同步到-> ES中
     */
    @MultiSubmit
    @ApiOperation(value = "将mysql 商品库信息 同步到-> ES中")
    @RequestMapping(value = "/standardES", method = RequestMethod.POST)
    public BaseResponse initAllGoodsES(@RequestBody EsStandardInitRequest queryRequest) {
        esStandardProvider.init(queryRequest);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 将mysql 优惠券信息 同步到-> ES中
     */
    @MultiSubmit
    @ApiOperation(value = "将mysql 优惠券信息 同步到-> ES中")
    @RequestMapping(value = "/couponInfoES", method = RequestMethod.POST)
    public BaseResponse initAllCouponInfoES(@RequestBody EsCouponInfoInitRequest queryRequest) {
        esCouponInfoProvider.init(queryRequest);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 将mysql 优惠券活动信息 同步到-> ES中
     */
    @MultiSubmit
    @ApiOperation(value = "将mysql 优惠券活动信息 同步到-> ES中")
    @RequestMapping(value = "/couponActivityES", method = RequestMethod.POST)
    public BaseResponse initAllCouponActivityES(@RequestBody EsCouponActivityInitRequest queryRequest) {
        esCouponActivityProvider.init(queryRequest);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 将mysql 会员信息 同步到-> ES中
     */
    @MultiSubmit
    @ApiOperation(value = "将mysql 会员信息 同步到-> ES中")
    @RequestMapping(value = "/customerES", method = RequestMethod.POST)
    public BaseResponse initAllCustomerES(@RequestBody EsCustomerDetailInitRequest queryRequest) {
        esCustomerDetailProvider.init(queryRequest);
        return BaseResponse.SUCCESSFUL();
    }


    /**
     * 将mysql 会员信息 同步到-> ES中
     */
    @MultiSubmit
    @ApiOperation(value = "将mysql 会员信息 同步到-> ES中")
    @RequestMapping(value = "/delete-customer", method = RequestMethod.POST)
    public BaseResponse deleteCustomerES(@RequestBody EsCustomerDetailInitRequest queryRequest) {
        esCustomerDetailProvider.deleteCustomer(queryRequest);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 将mysql 付费会员信息 同步到-> ES中
     */
    @MultiSubmit
    @ApiOperation(value = "将mysql 会员信息 同步到-> ES中")
    @RequestMapping(value = "/initPaidMembersES", method = RequestMethod.POST)
    public BaseResponse initPaidMembersES(@RequestBody EsCustomerDetailInitRequest queryRequest) {
        esCustomerDetailProvider.initPaidMembers(queryRequest);
        return BaseResponse.SUCCESSFUL();
    }


    /**
     * 将mysql 分销员信息 同步到-> ES中
     */
    @MultiSubmit
    @ApiOperation(value = "将mysql 分销员信息 同步到-> ES中")
    @RequestMapping(value = "/initDistributionCustomerES", method = RequestMethod.POST)
    public BaseResponse initEsDistributionCustomer(@RequestBody EsDistributionCustomerPageRequest queryRequest) {
        esDistributionCustomerProvider.init(queryRequest);
        return BaseResponse.SUCCESSFUL();
    }


    /**
     * 将mysql 拼团信息 同步到-> ES中
     */
    @MultiSubmit
    @ApiOperation(value = "将mysql 拼团信息 同步到-> ES中")
    @RequestMapping(value = "/initGrouponActivityES", method = RequestMethod.POST)
    public BaseResponse initEsGrouponActivity(@RequestBody EsGrouponActivityPageRequest queryRequest) {
        esGrouponActivityProvider.init(queryRequest);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 将mysql 商家评价信息 同步到-> ES中
     */
    @MultiSubmit
    @ApiOperation(value = "将mysql 商家评价信息 同步到-> ES中")
    @RequestMapping(value = "/initStoreEvaluateSumES", method = RequestMethod.POST)
    public BaseResponse initStoreEvaluateSum(@RequestBody EsStoreEvaluateSumPageRequest queryRequest) {
        esStoreEvaluateSumQueryProvider.init(queryRequest);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 将mysql 操作日志信息 同步到-> ES中
     */
    @MultiSubmit
    @ApiOperation(value = "将mysql 操作日志信息 同步到-> ES中")
    @RequestMapping(value = "/initOperationLogQueryES", method = RequestMethod.POST)
    public BaseResponse initOperationLog(@RequestBody EsOperationLogListRequest queryRequest) {
        esOperationLogQueryProvider.init(queryRequest);
        return BaseResponse.SUCCESSFUL();
    }


    /**
     * 将mysql  搜索词 同步到-> ES中
     */
    @MultiSubmit
    @ApiOperation(value = "将mysql 搜索词 同步到-> ES中")
    @RequestMapping(value = "/initSearchAssociationalWordES", method = RequestMethod.POST)
    public BaseResponse initSearchAssociationalWord(@RequestBody EsSearchAssociationalWordPageRequest queryRequest) {
        esSearchAssociationalWordProvider.init(queryRequest);
        return BaseResponse.SUCCESSFUL();
    }


    /**
     * 将mysql  素材 同步到-> ES中
     */
    @MultiSubmit
    @ApiOperation(value = "将mysql 素材 同步到-> ES中")
    @RequestMapping(value = "/initSystemResourceES", method = RequestMethod.POST)
    public BaseResponse initSystemResource(@RequestBody EsSystemResourcePageRequest queryRequest) {
        esSystemResourceProvider.init(queryRequest);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 将mysql  敏感词 同步到-> ES中
     */
    @MultiSubmit
    @ApiOperation(value = "将mysql 敏感词 同步到-> ES中")
    @RequestMapping(value = "/initSensitiveWordsES", method = RequestMethod.POST)
    public BaseResponse initSensitiveWords(@RequestBody EsSensitiveWordsQueryRequest queryRequest) {
        esSensitiveWordsProvider.init(queryRequest);
        return BaseResponse.SUCCESSFUL();
    }


}
