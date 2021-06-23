package com.wanmi.sbc.goods;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.elastic.api.provider.goods.EsGoodsInfoElasticProvider;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsInfoEnterpriseAuditRequest;
import com.wanmi.sbc.goods.api.provider.enterprise.EnterpriseGoodsInfoProvider;
import com.wanmi.sbc.goods.api.provider.enterprise.EnterpriseGoodsInfoQueryProvider;
import com.wanmi.sbc.goods.api.request.enterprise.goods.EnterpriseAuditCheckRequest;
import com.wanmi.sbc.goods.api.request.enterprise.goods.EnterpriseAuditStatusBatchRequest;
import com.wanmi.sbc.goods.api.request.enterprise.goods.EnterpriseGoodsInfoPageRequest;
import com.wanmi.sbc.goods.api.response.enterprise.EnterpriseGoodsAuditResponse;
import com.wanmi.sbc.goods.api.response.enterprise.EnterpriseGoodsInfoPageResponse;
import com.wanmi.sbc.goods.bean.enums.EnterpriseAuditState;
import com.wanmi.sbc.util.CommonUtil;
import com.wanmi.sbc.util.OperateLogMQUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Arrays;

/**
 * @author baijianzhong
 * @ClassName EnterpriseGoodsInfoController
 * @Date 2020-03-05 11:30
 * @Description TODO
 **/
@RestController
@RequestMapping(value = "/enterprise/goodsInfo")
public class EnterpriseGoodsInfoController {

    @Autowired
    private EnterpriseGoodsInfoQueryProvider enterpriseGoodsInfoQueryProvider;

    @Autowired
    private OperateLogMQUtil operateLogMQUtil;

    @Autowired
    private EsGoodsInfoElasticProvider esGoodsInfoElasticProvider;

    @Autowired
    private EnterpriseGoodsInfoProvider enterpriseGoodsInfoProvider;

    @Autowired
    private CommonUtil commonUtil;


    @ApiOperation(value = "分页查询企业购商品")
    @PostMapping("/page")
    public BaseResponse<EnterpriseGoodsInfoPageResponse> pageEnterpriseGoodsInfo(@RequestBody @Valid EnterpriseGoodsInfoPageRequest request){
        return enterpriseGoodsInfoQueryProvider.page(request);
    }

    /**
     * 单个审核接口
     * @param request
     * @return
     */
    @ApiOperation(value = "单个审核接口")
    @PostMapping("/audit")
    public BaseResponse modifyEnterpriseGoodsInfo(@RequestBody @Valid EnterpriseAuditCheckRequest request){
        //验证是否购买了企业购服务
        commonUtil.getIepSettingInfo();
        String operateStr = EnterpriseAuditState.NOT_PASS.equals(request.getEnterpriseAuditState()) ? "未通过" : "通过";
        operateLogMQUtil.convertAndSend("应用", "审核"+operateStr+"单个企业购商品", "商品SKUId:"+request.getGoodsInfoId());
        EnterpriseGoodsAuditResponse response = enterpriseGoodsInfoProvider.auditEnterpriseGoods(request).getContext();
        if(CommonErrorCode.SUCCESSFUL.equals(response.getBackErrorCode())){
            //更新es
            esGoodsInfoElasticProvider.modifyEnterpriseAuditStatus(EsGoodsInfoEnterpriseAuditRequest.builder()
                    .enterPriseGoodsAuditReason(request.getEnterPriseGoodsAuditReason())
                    .enterPriseAuditStatus(request.getEnterpriseAuditState())
                    .goodsInfoIds(Arrays.asList(request.getGoodsInfoId()))
                    .build());
            return BaseResponse.SUCCESSFUL();
        }
        return BaseResponse.info(CommonErrorCode.STATUS_HAS_BEEN_CHANGED_ERROR,
                "提示该商品状态已发生变更请刷新列表");
    }


    /**
     * 批量审核接口
     * @param request
     * @return
     */
    @ApiOperation(value = "批量审核接口")
    @PostMapping("/batchAudit")
    public BaseResponse batchAuditEnterpriseGoods(@RequestBody @Valid EnterpriseAuditStatusBatchRequest request){
        //验证是否购买了企业购服务
        commonUtil.getIepSettingInfo();
        String operateStr = EnterpriseAuditState.NOT_PASS.equals(request.getEnterpriseGoodsAuditFlag()) ? "未通过" : "通过";
        operateLogMQUtil.convertAndSend("应用", "审核"+operateStr+"单个企业购商品", "商品SKUId:"+request.getGoodsInfoIds().toString());
        BaseResponse response = enterpriseGoodsInfoProvider.batchAuditEnterpriseGoods(request);
        if(CommonErrorCode.SUCCESSFUL.equals(response.getCode())){
            //更新es
            esGoodsInfoElasticProvider.modifyEnterpriseAuditStatus(EsGoodsInfoEnterpriseAuditRequest.builder()
                    .enterPriseGoodsAuditReason(request.getEnterPriseGoodsAuditReason())
                    .enterPriseAuditStatus(request.getEnterpriseGoodsAuditFlag())
                    .goodsInfoIds(request.getGoodsInfoIds())
                    .build());
        }
        return BaseResponse.SUCCESSFUL();
    }
}
