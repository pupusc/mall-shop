package com.wanmi.sbc.enterprise;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.BoolFlag;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.customer.api.provider.detail.CustomerDetailQueryProvider;
import com.wanmi.sbc.customer.api.provider.level.CustomerLevelQueryProvider;
import com.wanmi.sbc.customer.api.provider.store.StoreCustomerQueryProvider;
import com.wanmi.sbc.customer.api.provider.store.StoreQueryProvider;
import com.wanmi.sbc.customer.api.provider.storelevel.StoreLevelQueryProvider;
import com.wanmi.sbc.customer.api.request.customer.CustomerIdsListRequest;
import com.wanmi.sbc.customer.api.request.detail.CustomerDetailListByConditionRequest;
import com.wanmi.sbc.customer.api.request.store.StoreByIdRequest;
import com.wanmi.sbc.customer.api.request.store.StoreCustomerQueryRequest;
import com.wanmi.sbc.customer.api.request.storelevel.StoreLevelListRequest;
import com.wanmi.sbc.customer.api.response.level.CustomerLevelWithRightsResponse;
import com.wanmi.sbc.customer.api.response.store.StoreByIdResponse;
import com.wanmi.sbc.customer.bean.vo.CustomerDetailVO;
import com.wanmi.sbc.customer.bean.vo.CustomerLevelVO;
import com.wanmi.sbc.customer.bean.vo.StoreCustomerVO;
import com.wanmi.sbc.customer.bean.vo.StoreLevelVO;
import com.wanmi.sbc.elastic.api.provider.goods.EsGoodsInfoElasticProvider;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsDeleteByIdsRequest;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsInfoEnterpriseBatchAuditRequest;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsInfoRequest;
import com.wanmi.sbc.goods.api.constant.GoodsErrorCode;
import com.wanmi.sbc.goods.api.provider.enterprise.EnterpriseGoodsInfoProvider;
import com.wanmi.sbc.goods.api.provider.enterprise.EnterpriseGoodsInfoQueryProvider;
import com.wanmi.sbc.goods.api.provider.info.GoodsInfoQueryProvider;
import com.wanmi.sbc.goods.api.request.enterprise.goods.*;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoByIdRequest;
import com.wanmi.sbc.goods.api.response.enterprise.*;
import com.wanmi.sbc.goods.api.response.info.GoodsInfoByIdResponse;
import com.wanmi.sbc.goods.bean.dto.BatchEnterPrisePriceDTO;
import com.wanmi.sbc.goods.bean.enums.EnterpriseAuditState;
import com.wanmi.sbc.goods.bean.vo.GoodsCustomerPriceVO;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.goods.bean.vo.GoodsLevelPriceVO;
import com.wanmi.sbc.util.CommonUtil;
import com.wanmi.sbc.util.OperateLogMQUtil;
import com.wanmi.sbc.vas.bean.vo.IepSettingVO;
import io.seata.spring.annotation.GlobalTransactional;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author baijianzhong
 * @ClassName EnterPriseGoodsInfoController
 * @Date 2020-03-03 16:19
 * @Description TODO
 **/
@Api
@RestController
@RequestMapping("/enterprise")
public class EnterpriseGoodsInfoController {

    @Autowired
    private EnterpriseGoodsInfoQueryProvider enterpriseGoodsInfoQueryProvider;

    @Autowired
    private GoodsInfoQueryProvider goodsInfoQueryProvider;

    @Autowired
    private EnterpriseGoodsInfoProvider enterpriseGoodsInfoProvider;

    @Autowired
    private EsGoodsInfoElasticProvider esGoodsInfoElasticProvider;

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private OperateLogMQUtil operateLogMQUtil;

    @Autowired
    private StoreLevelQueryProvider storeLevelQueryProvider;

    @Autowired
    private CustomerDetailQueryProvider customerDetailQueryProvider;

    @Autowired
    private StoreCustomerQueryProvider storeCustomerQueryProvider;

    @Autowired
    private CustomerLevelQueryProvider customerLevelQueryProvider;

    @Autowired
    private StoreQueryProvider storeQueryProvider;

    @ApiOperation(value = "分页查询企业购商品")
    @PostMapping("/goodsInfo/page")
    public BaseResponse<EnterpriseGoodsInfoPageResponse> pageEnterpriseGoodsInfo(@RequestBody @Valid EnterpriseGoodsInfoPageRequest request) {
        request.setStoreId(commonUtil.getStoreId());
        return enterpriseGoodsInfoQueryProvider.page(request);
    }

    @ApiOperation(value = "企业购商品设价详情页")
    @GetMapping("{goodsInfoId}")
    public BaseResponse<EnterpriseByIdResponse> detail(@PathVariable String goodsInfoId) {
        EnterpriseByIdRequest request = new EnterpriseByIdRequest();
        request.setGoodsInfoId(goodsInfoId);
        request.setStoreId(commonUtil.getStoreId());
        BaseResponse<EnterpriseByIdResponse> detail = enterpriseGoodsInfoQueryProvider.detail(request);
        GoodsInfoVO goodsInfo = detail.getContext().getGoodsInfo();
        List<GoodsLevelPriceVO> goodsLevelPrices = detail.getContext().getGoodsLevelPrices();
        StoreByIdResponse store = storeQueryProvider.getById(StoreByIdRequest.builder().storeId(goodsInfo.getStoreId()).build()).getContext();

        //获取全部客户等级
        //处理等级+折扣+企业价格
        List<StoreLevelVO> storeLevelVOList = new ArrayList<>();
        if (store.getStoreVO().getCompanyType() == BoolFlag.NO) {
            //自营
            CustomerLevelWithRightsResponse context = customerLevelQueryProvider.listCustomerLevelRightsInfo().getContext();
            List<CustomerLevelVO> customerLevelVOList = context.getCustomerLevelVOList();
            customerLevelVOList.forEach(customerLevelVO -> {
                StoreLevelVO storeLevelVO = new StoreLevelVO();
                storeLevelVO.setStoreLevelId(customerLevelVO.getCustomerLevelId());
                storeLevelVO.setLevelName(customerLevelVO.getCustomerLevelName());
                if (goodsLevelPrices != null) {
                    Optional<GoodsLevelPriceVO> first = goodsLevelPrices.stream().filter((e) -> e.getLevelId().equals(customerLevelVO.getCustomerLevelId())).findFirst();
                    if (first.isPresent()) {
                        storeLevelVO.setPrice(first.get().getPrice());
                        storeLevelVO.setDiscount(first.get().getDiscount());
                    } else {
                        storeLevelVO.setPrice(goodsInfo.getEnterPrisePrice());
                        storeLevelVO.setDiscount(100);
                    }
                } else {
                    storeLevelVO.setPrice(goodsInfo.getEnterPrisePrice());
                    storeLevelVO.setDiscount(100);
                }
                storeLevelVOList.add(storeLevelVO);
            });
        } else {
            //第三方
            StoreLevelListRequest storeLevelListRequest = StoreLevelListRequest.builder().storeId(commonUtil.getStoreId()).build();
            List<StoreLevelVO> storeLevelVOListTmp = storeLevelQueryProvider.listAllStoreLevelByStoreId(storeLevelListRequest).getContext().getStoreLevelVOList();
            storeLevelVOListTmp.forEach(storeLevelVO -> {
                if (goodsLevelPrices != null) {
                    Optional<GoodsLevelPriceVO> first = goodsLevelPrices.stream().filter((e) -> e.getLevelId().equals(storeLevelVO.getStoreLevelId())).findFirst();
                    if (first.isPresent()) {
                        storeLevelVO.setPrice(first.get().getPrice());
                        storeLevelVO.setDiscount(first.get().getDiscount());
                    } else {
                        storeLevelVO.setPrice(goodsInfo.getEnterPrisePrice());
                        storeLevelVO.setDiscount(100);
                    }
                } else {
                    storeLevelVO.setPrice(goodsInfo.getEnterPrisePrice());
                    storeLevelVO.setDiscount(100);
                }
            });
            storeLevelVOList.addAll(storeLevelVOListTmp);
        }
        detail.getContext().setStoreLevels(storeLevelVOList);
        //给特定客户折扣 设置 客户名称何等级名称
        List<GoodsCustomerPriceVO> goodsCustomerPrices = detail.getContext().getGoodsCustomerPrices();
        if (goodsCustomerPrices != null && !goodsCustomerPrices.isEmpty()) {
            List<String> customerIds = goodsCustomerPrices.stream().map(GoodsCustomerPriceVO::getCustomerId).collect(Collectors.toList());
            CustomerIdsListRequest idsListRequest = new CustomerIdsListRequest();
            idsListRequest.setCustomerIds(customerIds);

            StoreCustomerQueryRequest storeCustomerQueryRequest = new StoreCustomerQueryRequest();
            storeCustomerQueryRequest.setStoreId(commonUtil.getStoreId());
            storeCustomerQueryRequest.setCustomerIds(customerIds);
            List<StoreCustomerVO> storeCustomerVOList = storeCustomerQueryProvider.listCustomerByIds(storeCustomerQueryRequest).getContext().getStoreCustomerVOList();

            CustomerDetailListByConditionRequest conditionRequest = new CustomerDetailListByConditionRequest();
            conditionRequest.setCustomerIds(customerIds);
            List<CustomerDetailVO> customerDetails = customerDetailQueryProvider.listCustomerDetailByCondition(conditionRequest).getContext().getCustomerDetailVOList();
            goodsCustomerPrices.forEach(goodsCustomerPriceVO -> {

                Optional<CustomerDetailVO> first = customerDetails.stream().filter(e -> e.getCustomerId().equals(goodsCustomerPriceVO.getCustomerId())).findFirst();
                first.ifPresent(vo -> {
                    goodsCustomerPriceVO.setCustomerName(vo.getCustomerName());
                });
                Optional<StoreCustomerVO> first1 = storeCustomerVOList.stream().filter(e -> e.getCustomerId().equals(goodsCustomerPriceVO.getCustomerId())).findFirst();
                first1.ifPresent(vo -> {
                    goodsCustomerPriceVO.setLevelName(vo.getCustomerLevelName());
                });
            });
        }
        return detail;
    }

    @ApiOperation(value = "企业购商品设价保存")
    @PostMapping("/goodsInfo/save")
    @GlobalTransactional
    public BaseResponse savePrice(@RequestBody @Valid EnterprisePriceSaveRequest request) {
        request.setStoreId(commonUtil.getStoreId());
        enterpriseGoodsInfoProvider.enterpriseSavePrice(request);
        esGoodsInfoElasticProvider.delete(EsGoodsDeleteByIdsRequest.builder().deleteIds(Collections.singletonList(request.getGoodsInfoId())).build());
        esGoodsInfoElasticProvider.initEsGoodsInfo(EsGoodsInfoRequest.builder().skuIds(Collections.singletonList(request.getGoodsInfoId())).build());
        operateLogMQUtil.convertAndSend("应用", "添加企业购商品",
                "skuIds: " + request.getGoodsInfoId() +
                        "\n 审核状态：" + "审核通过");
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 批量新增企业购商品
     *
     * @param batchUpdateRequest
     * @return
     */
    @ApiOperation(value = "批量新增企业购商品")
    @PostMapping(value = "/batchAdd")
    public BaseResponse batchAddEnterpriseGoodsInfo(@RequestBody @Valid EnterprisePriceBatchUpdateRequest batchUpdateRequest) {
        //判断是否购买了企业购服务
        IepSettingVO iepSettingVO = commonUtil.getIepSettingInfo();
        //审核开关入参
        batchUpdateRequest.setEnterpriseGoodsAuditFlag(iepSettingVO.getEnterpriseGoodsAuditFlag());
        EnterpriseGoodsAddResponse response = enterpriseGoodsInfoProvider.batchUpdateEnterprisePrice(batchUpdateRequest).getContext();
        if (CollectionUtils.isNotEmpty(response.getGoodsInfoIds())) {
            return BaseResponse.info(GoodsErrorCode.ENTERPRISE_INVALID_ERROR, "存在失效的商品，请删除后再保存",
                    response.getGoodsInfoIds());
        }
        //入日志
        operateLogMQUtil.convertAndSend("应用", "添加企业购商品",
                "skuIds: " + Arrays.toString(batchUpdateRequest.getBatchEnterPrisePriceDTOS().stream().map(BatchEnterPrisePriceDTO::getGoodsInfoId).toArray()) +
                        "\n 审核状态：" + iepSettingVO.getEnterpriseGoodsAuditFlag());
        if (DefaultFlag.NO.equals(iepSettingVO.getEnterpriseGoodsAuditFlag())) {
            //更新es
            esGoodsInfoElasticProvider.updateEnterpriseGoodsInfo(EsGoodsInfoEnterpriseBatchAuditRequest.builder().
                    batchEnterPrisePriceDTOS(batchUpdateRequest.getBatchEnterPrisePriceDTOS()).
                    enterpriseAuditState(EnterpriseAuditState.CHECKED).build());
        } else {
            esGoodsInfoElasticProvider.updateEnterpriseGoodsInfo(EsGoodsInfoEnterpriseBatchAuditRequest.builder().
                    batchEnterPrisePriceDTOS(batchUpdateRequest.getBatchEnterPrisePriceDTOS()).
                    enterpriseAuditState(EnterpriseAuditState.WAIT_CHECK).build());
        }
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 单个修改企业购商品的价格
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "单个修改企业购商品的价格")
    @PostMapping(value = "/modify")
    @GlobalTransactional
    public BaseResponse modifyEnterpriseGoodsInfoPrice(@RequestBody @Valid EnterprisePriceUpdateRequest request) {
        //判断是否购买了企业购服务
        commonUtil.getIepSettingInfo();
        GoodsInfoByIdResponse goodsInfoByIdResponse = goodsInfoQueryProvider.getById(GoodsInfoByIdRequest.builder()
                .goodsInfoId(request.getGoodsInfoId()).build()).getContext();
        if (Objects.isNull(goodsInfoByIdResponse)) {
            throw new RuntimeException(GoodsErrorCode.NOT_EXIST);
        }
        //判断如果是已审核的就更新为审核通过，其他的更新为待审核
        request.setEnterpriseGoodsAuditFlag(EnterpriseAuditState.CHECKED.equals(goodsInfoByIdResponse.getEnterPriseAuditState())
                ? DefaultFlag.YES : DefaultFlag.NO);
        //更新库
        enterpriseGoodsInfoProvider.updateEnterprisePrice(request);
        //更新es
        esGoodsInfoElasticProvider.delete(EsGoodsDeleteByIdsRequest.builder().deleteIds(Collections.singletonList(request.getGoodsInfoId())).build());
        esGoodsInfoElasticProvider.initEsGoodsInfo(EsGoodsInfoRequest.builder().skuIds(Collections.singletonList(request.getGoodsInfoId())).build());
        //入日志
        operateLogMQUtil.convertAndSend("应用", "修改企业专享价",
                "skuId: " + request.getGoodsInfoId() +
                        "\n 修改后的价格：" + request.getEnterPrisePrice());
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 删除企业购商品
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "单个删除企业购商品")
    @PostMapping(value = "/delete")
    public BaseResponse deleteEnterpriseGoodsInfoPrice(@RequestBody @Valid EnterpriseSkuDeleteRequest request) {
        BaseResponse response = enterpriseGoodsInfoProvider.deleteEnterpriseGoods(request);
        //入日志
        operateLogMQUtil.convertAndSend("应用", "删除企业购商品",
                "skuId: " + request.getGoodsInfoId());
        if (CommonErrorCode.SUCCESSFUL.equals(response.getCode())) {
            //更新es
            esGoodsInfoElasticProvider.delete(EsGoodsDeleteByIdsRequest.builder().deleteIds(Collections.singletonList(request.getGoodsInfoId())).build());
            esGoodsInfoElasticProvider.initEsGoodsInfo(EsGoodsInfoRequest.builder().skuIds(Collections.singletonList(request.getGoodsInfoId())).build());
        }
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 批量删除企业购商品
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "批量删除企业购商品")
    @PostMapping(value = "/batchDelete")
    public BaseResponse batchDeleteEnterpriseGoodsInfoPrice(@RequestBody @Valid EnterpriseSpuDeleteRequest request) {
        EnterpriseBatchDeleteResponse response = enterpriseGoodsInfoProvider.batchDeleteEnterpriseGoods(request).getContext();
        esGoodsInfoElasticProvider.deleteByGoods(EsGoodsDeleteByIdsRequest.builder().deleteIds(Collections.singletonList(request.getGoodsId())).build());
        esGoodsInfoElasticProvider.initEsGoodsInfo(EsGoodsInfoRequest.builder().goodsId(request.getGoodsId()).build());
        //入日志
        operateLogMQUtil.convertAndSend("应用", "批量删除企业购商品",
                "skuId: " + response.getGoodsInfoIds().toString());
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 检查商品中是否有企业购货品
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "检查商品中是否有企业购货品")
    @PostMapping(value = "/enterprise-check")
    public BaseResponse<EnterpriseCheckResponse> checkEnterpriseInSku(@RequestBody @Valid EnterpriseGoodsChangeRequest request) {
        return enterpriseGoodsInfoQueryProvider.checkEnterpriseInSku(request);
    }


    /**
     * 分页查询商家的企业购商品——用于选择商品时的接口
     *
     * @param pageRequest 商品 {@link EnterpriseGoodsInfoPageRequest}
     * @return 企业购商品分页
     */
    @ApiOperation(value = "分页查询商家的企业购商品")
    @RequestMapping(value = "/enterprise-sku", method = RequestMethod.POST)
    public BaseResponse<EnterpriseGoodsInfoPageResponse> page(@RequestBody EnterpriseGoodsInfoPageRequest pageRequest) {
        pageRequest.setStoreId(commonUtil.getStoreId());
        pageRequest.setGoodsStatus(BoolFlag.YES.toValue());
        return goodsInfoQueryProvider.enterpriseGoodsInfoPage(pageRequest);
    }

}
