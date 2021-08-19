package com.wanmi.sbc.distribution;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.BoolFlag;
import com.wanmi.sbc.elastic.api.provider.goods.EsGoodsInfoElasticProvider;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsInfoModifyDistributionGoodsAuditRequest;
import com.wanmi.sbc.goods.api.provider.info.GoodsInfoProvider;
import com.wanmi.sbc.goods.api.provider.info.GoodsInfoQueryProvider;
import com.wanmi.sbc.goods.api.request.info.*;
import com.wanmi.sbc.goods.api.response.info.DistributionGoodsInfoPageResponse;
import com.wanmi.sbc.goods.bean.enums.DistributionGoodsAudit;
import com.wanmi.sbc.goods.bean.enums.SaleType;
import com.wanmi.sbc.util.OperateLogMQUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Objects;

/**
 * S2B的BOSS分销商品服务
 * Created by CHENLI on 19/2/22.
 */
@RestController
@RequestMapping("/goods")
@Api(description = "S2B的BOSS分销商品服务" ,tags ="DistributionBossGoodsController")
public class DistributionBossGoodsController {

    @Autowired
    private GoodsInfoQueryProvider goodsInfoQueryProvider;

    @Autowired
    private GoodsInfoProvider goodsInfoProvider;

    @Autowired
    private EsGoodsInfoElasticProvider esGoodsInfoElasticProvider;

    @Autowired
    private OperateLogMQUtil operateLogMQUtil;

    /**
     * 分页查询分销商品
     *
     * @param pageRequest 商品 {@link DistributionGoodsPageRequest}
     * @return 分销商品分页
     */
    @ApiOperation(value = "分页查询分销商品")
    @RequestMapping(value = "/distribution-sku", method = RequestMethod.POST)
    public BaseResponse<DistributionGoodsInfoPageResponse> page(@RequestBody DistributionGoodsPageRequest pageRequest) {

        pageRequest.setGoodsStatus(BoolFlag.YES.toValue());
        pageRequest.setSaleType(SaleType.RETAIL.toValue());

        if(Objects.nonNull(pageRequest.getCommissionRateFirst())){
            pageRequest.setCommissionRateFirst(pageRequest.getCommissionRateFirst().divide(BigDecimal.valueOf(100)));
        }
        if(Objects.nonNull(pageRequest.getCommissionRateLast())){
            pageRequest.setCommissionRateLast(pageRequest.getCommissionRateLast().divide(BigDecimal.valueOf(100)));
        }
        return goodsInfoQueryProvider.distributionGoodsInfoPage(pageRequest);
    }

    /**
     * 分销商品审核通过(单个)
     * @param request
     * @return
     */
    @ApiOperation(value = "分销商品审核通过(单个)")
    @RequestMapping(value = "/distribution-check", method = RequestMethod.POST)
    public BaseResponse checkDistributionGoods(@RequestBody DistributionGoodsCheckRequest request) {
        goodsInfoProvider.checkDistributionGoods(request);
        //记录操作日志
        operateLogMQUtil.convertAndSend("营销", "审核通过单个分销商品", "商品SKU编码:"+request.getGoodsInfoId());

        EsGoodsInfoModifyDistributionGoodsAuditRequest auditRequest = new EsGoodsInfoModifyDistributionGoodsAuditRequest();
        auditRequest.setDistributionGoodsAudit(DistributionGoodsAudit.CHECKED.toValue());
        auditRequest.setGoodsInfoIds(Collections.singletonList(request.getGoodsInfoId()));
        esGoodsInfoElasticProvider.modifyDistributionGoodsAudit(auditRequest);

        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 批量审核分销商品
     * @param request
     * @return
     */
    @ApiOperation(value = "批量审核分销商品")
    @RequestMapping(value = "/distribution-batch-check", method = RequestMethod.POST)
    public BaseResponse batchCheckDistributionGoods(@RequestBody DistributionGoodsBatchCheckRequest request) {
        goodsInfoProvider.batchCheckDistributionGoods(request);

        EsGoodsInfoModifyDistributionGoodsAuditRequest auditRequest = new EsGoodsInfoModifyDistributionGoodsAuditRequest();
        auditRequest.setDistributionGoodsAudit(DistributionGoodsAudit.CHECKED.toValue());
        auditRequest.setGoodsInfoIds(request.getGoodsInfoIds());
        esGoodsInfoElasticProvider.modifyDistributionGoodsAudit(auditRequest);
        //记录操作日志
        operateLogMQUtil.convertAndSend("营销", "批量审核分销商品", "批量审核分销商品通过");
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 驳回分销商品
     * @param request
     * @return
     */
    @ApiOperation(value = "驳回分销商品")
    @RequestMapping(value = "/distribution-refuse", method = RequestMethod.POST)
    public BaseResponse refuseCheckDistributionGoods(@RequestBody DistributionGoodsRefuseRequest request) {
        goodsInfoProvider.refuseCheckDistributionGoods(request);

        EsGoodsInfoModifyDistributionGoodsAuditRequest auditRequest = new EsGoodsInfoModifyDistributionGoodsAuditRequest();
        auditRequest.setDistributionGoodsAudit(DistributionGoodsAudit.NOT_PASS.toValue());
        auditRequest.setGoodsInfoIds(Collections.singletonList(request.getGoodsInfoId()));
        auditRequest.setDistributionGoodsAuditReason(request.getDistributionGoodsAuditReason());
        esGoodsInfoElasticProvider.modifyDistributionGoodsAudit(auditRequest);

        //记录操作日志
        operateLogMQUtil.convertAndSend("营销", "驳回分销商品",
                "商品SKU编码:"+request.getGoodsInfoId()+"原因:"+request.getDistributionGoodsAuditReason());

        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 禁止分销商品
     * @param request
     * @return
     */
    @ApiOperation(value = "禁止分销商品")
    @RequestMapping(value = "/distribution-forbid", method = RequestMethod.POST)
    public BaseResponse forbidCheckDistributionGoods(@RequestBody DistributionGoodsForbidRequest request) {
         goodsInfoProvider.forbidCheckDistributionGoods(request);

        EsGoodsInfoModifyDistributionGoodsAuditRequest auditRequest = new EsGoodsInfoModifyDistributionGoodsAuditRequest();
        auditRequest.setDistributionGoodsAudit(DistributionGoodsAudit.FORBID.toValue());
        auditRequest.setGoodsInfoIds(Collections.singletonList(request.getGoodsInfoId()));
        auditRequest.setDistributionGoodsAuditReason(request.getDistributionGoodsAuditReason());
        esGoodsInfoElasticProvider.modifyDistributionGoodsAudit(auditRequest);

        //记录操作日志
        operateLogMQUtil.convertAndSend("营销", "禁止分销商品",
                "商品SKU编码:"+request.getGoodsInfoId()+"原因:"+request.getDistributionGoodsAuditReason());

        return BaseResponse.SUCCESSFUL();
    }
}
