package com.wanmi.sbc.distribution;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.BoolFlag;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.elastic.api.provider.goods.EsGoodsInfoElasticProvider;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsInfoModifyDistributionBySpuIdRequest;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsInfoModifyDistributionCommissionRequest;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsInfoModifyDistributionGoodsAuditRequest;
import com.wanmi.sbc.goods.api.constant.DistributionGoodsErrorCode;
import com.wanmi.sbc.goods.api.provider.info.GoodsInfoProvider;
import com.wanmi.sbc.goods.api.provider.info.GoodsInfoQueryProvider;
import com.wanmi.sbc.goods.api.request.info.*;
import com.wanmi.sbc.goods.api.response.info.DistributionGoodsAddResponse;
import com.wanmi.sbc.goods.api.response.info.DistributionGoodsInfoPageResponse;
import com.wanmi.sbc.goods.api.response.info.GoodsInfoByGoodsIdresponse;
import com.wanmi.sbc.goods.bean.dto.DistributionGoodsInfoModifyDTO;
import com.wanmi.sbc.goods.bean.enums.DistributionGoodsAudit;
import com.wanmi.sbc.goods.bean.enums.SaleType;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.marketing.api.provider.distribution.DistributionSettingQueryProvider;
import com.wanmi.sbc.util.CommonUtil;
import com.wanmi.sbc.util.OperateLogMQUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

/**
 * S2B的supplier分销商品服务
 * Created by CHENLI on 19/2/22.
 */
@RestController
@RequestMapping("/goods")
@Api(description = "S2B的supplier分销商品服务" ,tags ="DistributionSupplierGoodsController")
public class DistributionSupplierGoodsController {

    @Autowired
    private GoodsInfoQueryProvider goodsInfoQueryProvider;

    @Autowired
    private GoodsInfoProvider goodsInfoProvider;

    @Autowired
    private DistributionSettingQueryProvider settingQueryProvider;

    @Autowired
    private EsGoodsInfoElasticProvider esGoodsInfoElasticProvider;

    @Autowired
    private DistributionCacheService distributionCacheService;

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private OperateLogMQUtil operateLogMQUtil;

    /**
     * 分页查询商家的分销商品
     *
     * @param pageRequest 商品 {@link DistributionGoodsPageRequest}
     * @return 分销商品分页
     */
    @ApiOperation(value = "分页查询商家的分销商品")
    @RequestMapping(value = "/distribution-sku", method = RequestMethod.POST)
    public BaseResponse<DistributionGoodsInfoPageResponse> page(@RequestBody DistributionGoodsPageRequest pageRequest) {
        pageRequest.setStoreId(commonUtil.getStoreId());
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
     * 删除分销商品
     * @param request
     * @return
     */
    @ApiOperation(value = "删除分销商品")
    @RequestMapping(value = "/distribution-del", method = RequestMethod.POST)
    public BaseResponse delDistributionGoods(@RequestBody DistributionGoodsDeleteRequest request) {
        goodsInfoProvider.delDistributionGoods(request);
        //记录操作日志
        operateLogMQUtil.convertAndSend("营销", "删除分销商品",
                "商品SKU编号:"+request.getGoodsInfoNo());

        // 同步到ES
        EsGoodsInfoModifyDistributionGoodsAuditRequest auditRequest = new EsGoodsInfoModifyDistributionGoodsAuditRequest();
        auditRequest.setDistributionGoodsAudit(DistributionGoodsAudit.COMMON_GOODS.toValue());
        auditRequest.setGoodsInfoIds(Collections.singletonList(request.getGoodsInfoId()));
        esGoodsInfoElasticProvider.modifyDistributionGoodsAudit(auditRequest);

        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 添加分销商品
     * @param request
     * @return
     */
    @ApiOperation(value = "添加分销商品")
    @RequestMapping(value = "/distribution-add", method = RequestMethod.POST)
    public BaseResponse<DistributionGoodsAddResponse> addDistributionGoods(@RequestBody DistributionGoodsAddRequest request) {
        BaseResponse<Boolean> auditSwitch = settingQueryProvider.getDistributionGoodsSwitch();
        // 不用审核
        if (auditSwitch.getContext()){
            request.setDistributionGoodsAudit(DistributionGoodsAudit.CHECKED);
        }else{
            // 需要审核
            request.setDistributionGoodsAudit(DistributionGoodsAudit.WAIT_CHECK);
        }
        DistributionGoodsAddResponse goodsAddResponse = goodsInfoProvider.addDistributionGoods(request).getContext();

        //记录操作日志
        operateLogMQUtil.convertAndSend("营销", "添加分销商品",
                "商品skuIds:"
                        + Arrays.toString(request.getDistributionGoodsInfoModifyDTOS()
                        .stream().map(DistributionGoodsInfoModifyDTO::getGoodsInfoId).toArray())
                        +"是否需要审核:"+request.getDistributionGoodsAudit()
        );
        if (CollectionUtils.isNotEmpty(goodsAddResponse.getGoodsInfoIds())){
            return BaseResponse.info(DistributionGoodsErrorCode.GOODS_INVALID,"存在失效或者批发模式的商品，请删除后再保存", goodsAddResponse.getGoodsInfoIds());
        }

        // 查询店铺是否开启社交分销
        DefaultFlag defaultFlag = distributionCacheService.queryStoreOpenFlag(String.valueOf(commonUtil.getStoreId()));
        // 同步到ES
        EsGoodsInfoModifyDistributionCommissionRequest commissionRequest = new EsGoodsInfoModifyDistributionCommissionRequest();
        commissionRequest.setDistributionGoodsInfoDTOList(request.getDistributionGoodsInfoModifyDTOS());
        commissionRequest.setDistributionGoodsAudit(request.getDistributionGoodsAudit());
        // 开关开：0，关1
        commissionRequest.setDistributionGoodsStatus(defaultFlag == DefaultFlag.NO ? 1 : 0);
        esGoodsInfoElasticProvider.modifyDistributionCommission(commissionRequest);

        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 已审核通过 编辑分销商品佣金
     * @param request
     * @return
     */
    @ApiOperation(value = "已审核通过 编辑分销商品佣金")
    @RequestMapping(value = "/distribution-modify-commission", method = RequestMethod.POST)
    public BaseResponse modifyDistributionGoodsCommission(@RequestBody DistributionGoodsModifyRequest request) {
        goodsInfoProvider.modifyDistributionGoodsCommission(request);
        //记录操作日志
        operateLogMQUtil.convertAndSend("营销", "编辑分销商品佣金",
                "商品SKU编号:"+request.getGoodsInfoId()+" 佣金:"+request.getDistributionCommission());

        // 同步到ES
        DistributionGoodsInfoModifyDTO modifyDTO = new DistributionGoodsInfoModifyDTO();
        modifyDTO.setGoodsInfoId(request.getGoodsInfoId());
        modifyDTO.setCommissionRate(request.getCommissionRate());
        modifyDTO.setDistributionCommission(request.getDistributionCommission());
        List<DistributionGoodsInfoModifyDTO> modifyDTOList = new ArrayList<>();
        modifyDTOList.add(modifyDTO);

        EsGoodsInfoModifyDistributionCommissionRequest commissionRequest = new EsGoodsInfoModifyDistributionCommissionRequest();
        commissionRequest.setDistributionGoodsInfoDTOList(modifyDTOList);
        commissionRequest.setDistributionGoodsAudit(DistributionGoodsAudit.CHECKED);
        esGoodsInfoElasticProvider.modifyDistributionCommission(commissionRequest);

        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 审核未通过或禁止分销的商品重新编辑后，状态为待审核
     * @param request
     * @return
     */
    @ApiOperation(value = "审核未通过或禁止分销的商品重新编辑后，状态为待审核")
    @RequestMapping(value = "/distribution-modify", method = RequestMethod.POST)
    public BaseResponse modifyDistributionGoods(@RequestBody DistributionGoodsModifyRequest request) {
         goodsInfoProvider.modifyDistributionGoods(request);
        //记录操作日志
        operateLogMQUtil.convertAndSend("营销", "更新分销商品状态--待审核",
                "商品SKU编号:"+request.getGoodsInfoId());

        // 同步到ES
        DistributionGoodsInfoModifyDTO modifyDTO = new DistributionGoodsInfoModifyDTO();
        modifyDTO.setGoodsInfoId(request.getGoodsInfoId());
        modifyDTO.setCommissionRate(request.getCommissionRate());
        modifyDTO.setDistributionCommission(request.getDistributionCommission());


        List<DistributionGoodsInfoModifyDTO> modifyDTOList = new ArrayList<>();
        modifyDTOList.add(modifyDTO);

        EsGoodsInfoModifyDistributionCommissionRequest commissionRequest = new EsGoodsInfoModifyDistributionCommissionRequest();
        commissionRequest.setDistributionGoodsInfoDTOList(modifyDTOList);
        commissionRequest.setDistributionGoodsAudit(DistributionGoodsAudit.WAIT_CHECK);
        esGoodsInfoElasticProvider.modifyDistributionCommission(commissionRequest);

        return BaseResponse.SUCCESSFUL();
    }

    @ApiOperation(value = "商品是否是分销商品")
    @RequestMapping(value = "/distribution-check/{goodsId}", method = RequestMethod.GET)
    public BaseResponse<String> checkDistributionGoodsAudit(@PathVariable String goodsId){
        String flag = "flase";
        DistributionGoodsChangeRequest request = new DistributionGoodsChangeRequest();
        request.setGoodsId(goodsId);
        GoodsInfoByGoodsIdresponse respose = goodsInfoQueryProvider.getByGoodsId(request).getContext();
        List<GoodsInfoVO> goodsInfoVOs = respose.getGoodsInfoVOList();
        for (GoodsInfoVO info : goodsInfoVOs) {
            if (DistributionGoodsAudit.CHECKED.equals(info.getDistributionGoodsAudit())){
                flag = "true";
            }
        }
        return BaseResponse.success(flag);
    }

    /*
     * @Description: 分销商品改为普通商品
     * @Author: Bob
     * @Date: 2019-03-11 15:43
     */
    @ApiOperation(value = "分销商品改为普通商品")
    @RequestMapping(value = "/distribution-change/{goodsId}", method = RequestMethod.PATCH)
    public BaseResponse distributionToGeneralgoods (@PathVariable String goodsId) {
        DistributionGoodsChangeRequest request = new DistributionGoodsChangeRequest();
        request.setGoodsId(goodsId);
        goodsInfoProvider.distributeTogeneralGoods(request);
        //记录操作日志
        operateLogMQUtil.convertAndSend("营销", "分销商品改为普通商品",
                "商品SPU编码:"+request.getGoodsId());
        esGoodsInfoElasticProvider.modifyDistributionGoodsAuditBySpuId(EsGoodsInfoModifyDistributionBySpuIdRequest.
                builder().spuId(goodsId).build());
        return BaseResponse.SUCCESSFUL();
    }
}
