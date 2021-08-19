package com.wanmi.sbc.distribution;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.elastic.api.provider.goods.EsGoodsInfoElasticProvider;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsInfoModifyDistributionGoodsStatusRequest;
import com.wanmi.sbc.goods.api.provider.distributor.goods.DistributorGoodsInfoProvider;
import com.wanmi.sbc.goods.api.request.distributor.goods.DistributorGoodsInfoModifyByStoreIdAndStatusRequest;
import com.wanmi.sbc.marketing.api.provider.distribution.DistributionSettingProvider;
import com.wanmi.sbc.marketing.api.provider.distribution.DistributionSettingQueryProvider;
import com.wanmi.sbc.marketing.api.request.distribution.DistributionStoreSettingGetByStoreIdRequest;
import com.wanmi.sbc.marketing.api.request.distribution.DistributionStoreSettingSaveRequest;
import com.wanmi.sbc.marketing.api.response.distribution.DistributionStoreSettingGetByStoreIdResponse;
import com.wanmi.sbc.util.CommonUtil;
import com.wanmi.sbc.util.OperateLogMQUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 分销设置controller
 *
 * @Author: gaomuwei
 * @Date: Created In 下午2:44 2019/2/19
 * @Description:
 */
@Api(description = "分销设置服务" ,tags ="DistributionSettingController")
@RestController
@RequestMapping("/distribution-setting")
@Validated
public class DistributionSettingController {

    @Autowired
    private DistributionSettingQueryProvider distributionSettingQueryProvider;

    @Autowired
    private DistributionSettingProvider distributionSettingProvider;

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private DistributorGoodsInfoProvider distributorGoodsInfoProvider;

    @Autowired
    private EsGoodsInfoElasticProvider esGoodsInfoElasticProvider;

    @Autowired
    private OperateLogMQUtil operateLogMQUtil;

    /**
     * 查询分销设置API
     *
     * @return
     */
    @ApiOperation(value = "查询分销设置")
    @RequestMapping(method = RequestMethod.GET)
    public BaseResponse<DistributionStoreSettingGetByStoreIdResponse> findOne() {
        DistributionStoreSettingGetByStoreIdRequest request = new DistributionStoreSettingGetByStoreIdRequest();
        request.setStoreId(commonUtil.getStoreId().toString());
        return distributionSettingQueryProvider.getStoreSettingByStoreId(request);
    }

    /**
     * 保存分销设置API
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "保存分销设置")
    @RequestMapping(method = RequestMethod.PUT)
    public BaseResponse save(@RequestBody @Valid DistributionStoreSettingSaveRequest request) {
        request.setStoreId(commonUtil.getStoreId().toString());
        BaseResponse baseResponse = distributionSettingProvider.saveStoreSetting(request);
        //记录操作日志
        operateLogMQUtil.convertAndSend("营销", "保存分销设置", "店铺ID:"+request.getStoreId()+
                " 是否开启社交分销:"+request.getOpenFlag()+" 是否开启通用佣金:"+request.getCommissionFlag()+" 通用佣金比例:"+request.getCommissionRate());
        Integer status = NumberUtils.INTEGER_ZERO;
        //社交分销开关-未开始时，需将对应的分销员商品表信息标记为删除
        if (DefaultFlag.NO == request.getOpenFlag()){
            status = NumberUtils.INTEGER_ONE;
        }
        distributorGoodsInfoProvider.modifyByStoreIdAndStatus(new DistributorGoodsInfoModifyByStoreIdAndStatusRequest(commonUtil.getStoreId(),status));
        esGoodsInfoElasticProvider.modifyDistributionGoodsStatus(new EsGoodsInfoModifyDistributionGoodsStatusRequest(commonUtil.getStoreId(),status));
        //记录操作日志
        operateLogMQUtil.convertAndSend("营销", "根据分销设置开关--更新分销商品状态",
                "店铺ID:"+commonUtil.getStoreId()+" 分销商品状态:"+status);
        return baseResponse;
    }
}
