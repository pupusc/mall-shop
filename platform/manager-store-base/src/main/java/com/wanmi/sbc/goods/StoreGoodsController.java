package com.wanmi.sbc.goods;

import com.alibaba.fastjson.JSON;
import com.alipay.api.domain.GoodsInfo;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.base.Operator;
import com.wanmi.sbc.common.constant.RedisKeyConstant;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.Platform;
import com.wanmi.sbc.common.enums.StoreType;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.HttpUtil;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.common.util.SiteResultCode;
import com.wanmi.sbc.customer.api.provider.company.CompanyInfoQueryProvider;
import com.wanmi.sbc.customer.api.request.company.CompanyInfoByIdRequest;
import com.wanmi.sbc.customer.bean.vo.CompanyInfoVO;
import com.wanmi.sbc.elastic.api.provider.goods.EsGoodsInfoElasticProvider;
import com.wanmi.sbc.elastic.api.provider.goods.EsGoodsInfoElasticQueryProvider;
import com.wanmi.sbc.elastic.api.provider.spu.EsSpuQueryProvider;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsInfoListRequest;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsInfoQueryRequest;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsInfoRequest;
import com.wanmi.sbc.elastic.api.request.spu.EsSpuPageRequest;
import com.wanmi.sbc.elastic.api.response.spu.EsSpuPageResponse;
import com.wanmi.sbc.elastic.bean.vo.goods.EsGoodsInfoVO;
import com.wanmi.sbc.goods.api.provider.ares.GoodsAresProvider;
import com.wanmi.sbc.goods.api.provider.classify.ClassifyProvider;
import com.wanmi.sbc.goods.api.provider.enterprise.EnterpriseGoodsInfoQueryProvider;
import com.wanmi.sbc.goods.api.provider.excel.GoodsSupplierExcelProvider;
import com.wanmi.sbc.goods.api.provider.goods.GoodsProvider;
import com.wanmi.sbc.goods.api.provider.goods.GoodsQueryProvider;
import com.wanmi.sbc.goods.api.provider.info.GoodsInfoQueryProvider;
import com.wanmi.sbc.goods.api.provider.mini.assistant.WxLiveAssistantProvider;
import com.wanmi.sbc.goods.api.provider.mini.goods.WxMiniGoodsProvider;
import com.wanmi.sbc.goods.api.provider.storecate.StoreCateQueryProvider;
import com.wanmi.sbc.goods.api.request.ares.DispatcherFunctionRequest;
import com.wanmi.sbc.goods.api.request.enterprise.goods.EnterpriseGoodsInfoPageRequest;
import com.wanmi.sbc.goods.api.request.excel.GoodsSupplierExcelExportTemplateByStoreIdRequest;
import com.wanmi.sbc.goods.api.request.goods.GoodsModifyCateRequest;
import com.wanmi.sbc.goods.api.request.goods.GoodsPageForGrouponRequest;
import com.wanmi.sbc.goods.api.request.goods.GoodsQueryNeedSynRequest;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoListByConditionRequest;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoPageRequest;
import com.wanmi.sbc.goods.api.request.storecate.StoreCateListByGoodsRequest;
import com.wanmi.sbc.goods.api.request.storecate.StoreCateListGoodsRelByStoreCateIdAndIsHaveSelfRequest;
import com.wanmi.sbc.goods.api.response.classify.ClassifyGoodsProviderResponse;
import com.wanmi.sbc.goods.api.response.goods.GoodsListNeedSynResponse;
import com.wanmi.sbc.goods.api.response.storecate.StoreCateListByGoodsResponse;
import com.wanmi.sbc.goods.api.response.storecate.StoreCateListGoodsRelByStoreCateIdAndIsHaveSelfResponse;
import com.wanmi.sbc.goods.bean.enums.CheckStatus;
import com.wanmi.sbc.goods.bean.enums.EnterpriseAuditState;
import com.wanmi.sbc.goods.bean.vo.*;
import com.wanmi.sbc.goods.bean.wx.request.WxGoodsSearchRequest;
import com.wanmi.sbc.goods.request.GoodsSupplierExcelImportRequest;
import com.wanmi.sbc.goods.service.GoodsExportService;
import com.wanmi.sbc.goods.service.SupplierGoodsExcelService;
import com.wanmi.sbc.marketing.api.provider.grouponactivity.GrouponActivityQueryProvider;
import com.wanmi.sbc.marketing.api.provider.markup.MarkupQueryProvider;
import com.wanmi.sbc.marketing.api.request.grouponactivity.GrouponActivityListSpuIdRequest;
import com.wanmi.sbc.marketing.api.request.market.MarketingIdRequest;
import com.wanmi.sbc.marketing.api.response.markup.MarkupSkuIdsResponse;
import com.wanmi.sbc.redis.RedisService;
import com.wanmi.sbc.util.CommonUtil;
import com.wanmi.sbc.util.OperateLogMQUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Decoder;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * 商品服务
 * Created by daiyitian on 17/4/12.
 */
@Api(tags = "StoreGoodsController", description = "商品服务 API")
@RestController
@RequestMapping("/goods")
@Slf4j
@RefreshScope
public class StoreGoodsController {
    @Autowired
    GoodsAresProvider goodsAresProvider;

    @Autowired
    private GoodsQueryProvider goodsQueryProvider;

    @Autowired
    private GoodsProvider goodsProvider;

    @Autowired
    private GoodsSupplierExcelProvider goodsSupplierExcelProvider;

    @Autowired
    private EsGoodsInfoElasticQueryProvider esGoodsInfoElasticQueryProvider;

    @Autowired
    private CompanyInfoQueryProvider companyInfoQueryProvider;


    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private OperateLogMQUtil operateLogMQUtil;

    @Autowired
    private SupplierGoodsExcelService supplierGoodsExcelService;

    @Autowired
    private GrouponActivityQueryProvider grouponActivityQueryProvider;

    @Autowired
    private EsSpuQueryProvider esSpuQueryProvider;

    private AtomicInteger exportCount = new AtomicInteger(0);

    @Autowired
    private GoodsExportService goodsExportService;

    @Autowired
    private GoodsInfoQueryProvider goodsInfoQueryProvider;

    @Autowired
    private MarkupQueryProvider markupQueryProvider;

    @Value("${goods.export.pageSize}")
    private Integer exportPageSize;

    @Autowired
    private EnterpriseGoodsInfoQueryProvider enterpriseGoodsInfoQueryProvider;

    @Autowired
    private ClassifyProvider classifyProvider;


    @Autowired
    private WxMiniGoodsProvider wxMiniGoodsProvider;

    @Autowired
    private RedisService redisService;

    /**
     * 查询商品
     *
     * @param queryRequest 商品
     * @return 商品详情
     */
    @ApiOperation(value = "查询商品")
    @RequestMapping(value = "/spus", method = RequestMethod.POST)
    public BaseResponse<EsSpuPageResponse> list(@RequestBody EsSpuPageRequest queryRequest) {
        queryRequest.setCompanyInfoId(commonUtil.getCompanyInfoId());
        queryRequest.setStoreId(commonUtil.getStoreId());
        queryRequest.setDelFlag(DeleteFlag.NO.toValue());
        //按创建时间倒序、ID升序
        /*if(Objects.nonNull(queryRequest.getGoodsSortType()) && StringUtils.isNotBlank(queryRequest.getSortRole())) {
            switch (queryRequest.getGoodsSortType()) {
                case MARKET_PRICE:queryRequest.putSort("skuMinMarketPrice", queryRequest.getSortRole());break;
                case STOCK:queryRequest.putSort("stock", queryRequest.getSortRole());break;
                case SALES_NUM:queryRequest.putSort("goodsSalesNum", queryRequest.getSortRole());break;
                default:queryRequest.putSort("createTime", SortType.DESC.toValue());break;//按创建时间倒序、ID升序
            }
        }else{
            queryRequest.putSort("createTime", SortType.DESC.toValue());
        }*/

        //补充店铺分类
        if(queryRequest.getStoreCateId() != null) {
            BaseResponse<List<ClassifyGoodsProviderResponse>> goodsIdsContext = classifyProvider.listGoodsIdByClassifyIdColl(Collections.singletonList(queryRequest.getStoreCateId().intValue()));
            if(CollectionUtils.isNotEmpty(goodsIdsContext.getContext())){
                List<ClassifyGoodsProviderResponse> goodsIds = goodsIdsContext.getContext();
                queryRequest.setStoreCateGoodsIds(goodsIds.stream().map(ClassifyGoodsProviderResponse::getGoodsId).collect(Collectors.toList()));
            }else {
                EsSpuPageResponse response = new EsSpuPageResponse();
                response.setGoodsPage(new MicroServicePage<>(Collections.emptyList(), queryRequest.getPageable(), 0));
                return BaseResponse.success(response);
            }
        }
        queryRequest.setShowVendibilityFlag(Boolean.TRUE);//显示可售性

        if(queryRequest.getWxAudit() != null){
            if(queryRequest.getWxAudit() == 0){
                //不在视频号商品池的
                BaseResponse<List<String>> wxGoodsList = wxMiniGoodsProvider.findAllId(new WxGoodsSearchRequest());
                List<String> wxGoodsIds = wxGoodsList.getContext();
                List<String> notGoodsIdList = queryRequest.getNotGoodsIdList();
                if(notGoodsIdList != null){
                    notGoodsIdList.addAll(wxGoodsIds);
                }else {
                    queryRequest.setNotGoodsIdList(wxGoodsIds);
                }
            }else if(queryRequest.getWxAudit() == 1){
                //微信视频号至少一次审核通过的
                WxGoodsSearchRequest wxGoodsSearchRequest = new WxGoodsSearchRequest();
                wxGoodsSearchRequest.setAuditPassedOnce(true);
                BaseResponse<List<String>> wxGoodsList = wxMiniGoodsProvider.findAllId(wxGoodsSearchRequest);
                List<String> wxGoodsIds = wxGoodsList.getContext();
                List<String> goodsIds = queryRequest.getGoodsIds();
                if(goodsIds != null){
                    goodsIds.addAll(wxGoodsIds);
                }else {
                    queryRequest.setGoodsIds(wxGoodsIds);
                }
            }
        }
        BaseResponse<EsSpuPageResponse> pageResponse = esSpuQueryProvider.page(queryRequest);
        EsSpuPageResponse response = pageResponse.getContext();
        List<GoodsPageSimpleVO> goodses = response.getGoodsPage().getContent();
        if(CollectionUtils.isNotEmpty(goodses)) {
            //计算库存

            for (GoodsPageSimpleVO goodsParam : goodses) {
                long totalFreezeStock = 0L;
                for (String goodsInfoId : goodsParam.getGoodsInfoIds()) {
                    //获取冻结
                    String freezeStockStr = redisService.getString(RedisKeyConstant.GOODS_INFO_STOCK_FREEZE_PREFIX + goodsInfoId);
                    long freezeStock = StringUtils.isBlank(freezeStockStr) ? 0L : Long.parseLong(freezeStockStr);
                    totalFreezeStock += freezeStock;
                }
                goodsParam.setFreezeStock(totalFreezeStock);
                goodsParam.setTotalStock(totalFreezeStock + goodsParam.getStock());
            }

            Map<String, List<GoodsPageSimpleVO>> goodsGroup = goodses.stream().collect(Collectors.groupingBy(GoodsPageSimpleVO::getGoodsId));
            List<String> goodsIds = new ArrayList<>(goodsGroup.keySet());
            BaseResponse<Map<String, List<Integer>>> mapBaseResponse = classifyProvider.searchGroupedClassifyIdByGoodsId(goodsIds);
            if(mapBaseResponse.getContext() != null){
                Map<String, List<Integer>> storeCateMap = mapBaseResponse.getContext();
                for (GoodsPageSimpleVO goods : goodses) {
                    List<Integer> classifies = storeCateMap.get(goods.getGoodsId());
                    if(classifies == null){
                        goods.setStoreCateIds(new ArrayList<>());
                    }else{
                        goods.setStoreCateIds(classifies.stream().map(Integer::longValue).collect(Collectors.toList()));
                    }
                }
            }
            // 商品是否在直播助手中
//            BaseResponse<List<String>> goodsInLiveRes = wxLiveAssistantProvider.ifGoodsInLive(goodsIds);
//            if(CollectionUtils.isNotEmpty(goodsInLiveRes.getContext())){
//                for (String goodsId : goodsInLiveRes.getContext()) {
//                    List<GoodsPageSimpleVO> goodsPageSimpleVOS = goodsGroup.get(goodsId);
//                    if(goodsPageSimpleVOS != null){
//                        goodsPageSimpleVOS.get(0).setGoodsInLive(true);
//                    }
//                }
//            }

            //商品是否在视频号商品池
            if(queryRequest.getWxAudit() != null && queryRequest.getWxAudit() == 0){
//                goodses.forEach(g -> g.setGoodsInWxGoodsPool(true));
            }else {
                BaseResponse<List<String>> wxGoodsListRes = wxMiniGoodsProvider.findByGoodsIds(goodsIds);
                if(CollectionUtils.isNotEmpty(wxGoodsListRes.getContext())){
                    for (String goodsId : wxGoodsListRes.getContext()) {
                        List<GoodsPageSimpleVO> goodsPageSimpleVOS = goodsGroup.get(goodsId);
                        if(goodsPageSimpleVOS != null){
                            goodsPageSimpleVOS.get(0).setGoodsInWxGoodsPool(true);
                        }
                    }
                }
            }
        }
        return BaseResponse.success(response);
    }

    /**
     * 查询商品列表(供添加拼团活动中选择商品用)
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "查询商品列表(供添加拼团活动中选择商品用)")
    @RequestMapping(value = "/groupon-spus", method = RequestMethod.POST)
    public BaseResponse<EsSpuPageResponse> listForGroupon(@RequestBody GoodsPageForGrouponRequest request) {
        // 1.查询商品列表接口
        EsSpuPageRequest pageRequest = KsBeanUtil.convert(request, EsSpuPageRequest.class);
        pageRequest.setAddedFlags(Arrays.asList(1, 2));
        pageRequest.setAuditStatus(CheckStatus.CHECKED);
        pageRequest.setGroupSearch(Boolean.TRUE);

        // 过滤加价购
        MarketingIdRequest marketingIdRequest = new MarketingIdRequest();
        marketingIdRequest.setStoreId(  commonUtil.getStoreId());
        List<String> marketingMarkupSku = markupQueryProvider.getMarkupSku(marketingIdRequest).getContext().getLevelList();
        if(CollectionUtils.isNotEmpty(marketingMarkupSku)){
            EsGoodsInfoQueryRequest esGoodsInfoQueryRequest = new EsGoodsInfoQueryRequest();
            esGoodsInfoQueryRequest.setGoodsInfoIds(marketingMarkupSku);
            esGoodsInfoQueryRequest.setPageSize(marketingMarkupSku.size());
            List<EsGoodsInfoVO> esGoodsInfoVOS = esGoodsInfoElasticQueryProvider.page(esGoodsInfoQueryRequest).getContext().getEsGoodsInfoPage().getContent();
            if(CollectionUtils.isNotEmpty(esGoodsInfoVOS)){
                List<String> markupSpuIds = esGoodsInfoVOS.stream().map(i -> i.getGoodsId()).distinct().collect(Collectors.toList());
                pageRequest.setNotGoodsIdList(markupSpuIds);
            }
        }

        //创建周期购活动时排除企业购商品
        if (request.getFilterEnterprisePurchase()) {
            EnterpriseGoodsInfoPageRequest enterpriseGoodsInfoPageRequest=new EnterpriseGoodsInfoPageRequest();
            enterpriseGoodsInfoPageRequest.setStoreId(commonUtil.getStoreId());
            enterpriseGoodsInfoPageRequest.setEnterPriseAuditState(EnterpriseAuditState.CHECKED);
            enterpriseGoodsInfoPageRequest.setPageSize(100000);
            List<GoodsInfoVO> goodsInfoVOList=enterpriseGoodsInfoQueryProvider.page(enterpriseGoodsInfoPageRequest).getContext().getGoodsInfoPage().getContent();
            if(CollectionUtils.isNotEmpty(goodsInfoVOList)){
                List<String> spuIds = goodsInfoVOList.stream().map(i -> i.getGoodsId()).distinct().collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(spuIds)) {
                    if (CollectionUtils.isNotEmpty(pageRequest.getNotGoodsIdList())){
                        pageRequest.getNotGoodsIdList().addAll(spuIds);
                    }else {
                        pageRequest.setNotGoodsIdList(spuIds);
                    }
                }
            }

            //过滤组合商品
            GoodsInfoPageRequest goodsInfoPageRequest=new GoodsInfoPageRequest();
            goodsInfoPageRequest.setCombinedCommodity(Boolean.TRUE);
            goodsInfoPageRequest.setPageSize(100000);
            List<GoodsInfoVO> goodsInfos=goodsInfoQueryProvider.page(goodsInfoPageRequest).getContext().getGoodsInfoPage().getContent();
            if (CollectionUtils.isNotEmpty(goodsInfos)) {
                List<String> spuIds = goodsInfos.stream().map(i -> i.getGoodsId()).distinct().collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(spuIds)) {
                    if (CollectionUtils.isNotEmpty(pageRequest.getNotGoodsIdList())){
                        pageRequest.getNotGoodsIdList().addAll(spuIds);
                    }else {
                        pageRequest.setNotGoodsIdList(spuIds);
                    }
                }
            }

        }



        BaseResponse<EsSpuPageResponse> response = this.list(pageRequest);
        List<GoodsPageSimpleVO> goodses = response.getContext().getGoodsPage().getContent();

        // 2.标记不可选择的商品
        if (goodses.size() != 0
                && request.getActivityStartTime() != null
                && request.getActivityEndTime() != null) {
            List<String> goodsIds = grouponActivityQueryProvider.listActivityingSpuIds(
                    new GrouponActivityListSpuIdRequest(
                            goodses.stream().map(GoodsPageSimpleVO::getGoodsId).collect(Collectors.toList()),
                            request.getActivityStartTime(),
                            request.getActivityEndTime())
            ).getContext().getGoodsIds();

            if (CollectionUtils.isNotEmpty(goodsIds)) {
                goodses.forEach(goods -> {
                    if (goodsIds.contains(goods.getGoodsId())) {
                        goods.setGrouponForbiddenFlag(true);
                    }
                });
            }
        }

        return response;
    }


    /**
     * 批量设置分类
     */
    @ApiOperation(value = "批量设置分类")
    @RequestMapping(value = "/spu/cate", method = RequestMethod.PUT)
    public BaseResponse updateCate(@RequestBody GoodsModifyCateRequest request) {

        if (CollectionUtils.isEmpty(request.getGoodsIds())) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        if (CollectionUtils.isEmpty(request.getStoreCateIds())) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }

        goodsProvider.modifyCate(request);
        operateLogMQUtil.convertAndSend("商品","批量设置店铺分类","批量设置店铺分类");
        return BaseResponse.SUCCESSFUL();
    }


    /**
     * 下载模板
     */
    @ApiOperation(value = "下载模板")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "encrypted", value = "加密", required = true)
    @RequestMapping(value = "/excel/template/{encrypted}", method = RequestMethod.GET)
    public void template(@PathVariable String encrypted) {
        GoodsSupplierExcelExportTemplateByStoreIdRequest request =
                new GoodsSupplierExcelExportTemplateByStoreIdRequest();
        request.setStoreId(commonUtil.getStoreId());
        String file = goodsSupplierExcelProvider.supplierExportTemplate(request).getContext().getFile();
        if(StringUtils.isNotBlank(file)){
            try {
                String fileName = URLEncoder.encode("商品导入模板.xls", "UTF-8");
                HttpUtil.getResponse().setHeader("Content-Disposition", String.format("attachment;filename=\"%s\";filename*=\"utf-8''%s\"", fileName, fileName));
                HttpUtil.getResponse().getOutputStream().write(new BASE64Decoder().decodeBuffer(file));
            }catch (Exception e){
                throw new SbcRuntimeException(CommonErrorCode.FAILED);
            }
        }
    }

    /**
     * 确认导入商品
     */
    @ApiOperation(value = "确认导入商品")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "ext", value = "后缀", required = true)
    @RequestMapping(value = "/import/{ext}", method = RequestMethod.GET)
    public BaseResponse<Boolean> implGoods(@PathVariable String ext) {
        if(!("xls".equalsIgnoreCase(ext) || "xlsx".equalsIgnoreCase(ext))){
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }

        CompanyInfoVO companyInfo = companyInfoQueryProvider.getCompanyInfoById(
                CompanyInfoByIdRequest.builder().companyInfoId(commonUtil.getCompanyInfoId()).build()
        ).getContext();
        if(companyInfo == null){
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }

        GoodsSupplierExcelImportRequest importRequest = new GoodsSupplierExcelImportRequest();
        importRequest.setExt(ext);
        importRequest.setUserId(commonUtil.getOperatorId());
        importRequest.setCompanyInfoId(commonUtil.getCompanyInfoId());
        importRequest.setStoreId(commonUtil.getStoreId());
        importRequest.setCompanyType(companyInfo.getCompanyType());
        importRequest.setSupplierName(companyInfo.getSupplierName());
        importRequest.setType(StoreType.SUPPLIER);
        List<String> skuIds = supplierGoodsExcelService.implGoods(importRequest);

        //加入ES调整至MQ了
        if(CollectionUtils.isNotEmpty(skuIds)){
//            esGoodsInfoElasticProvider.initEsGoodsInfo(EsGoodsInfoRequest.builder().skuIds(skuIds).build());
            //ares埋点-商品-后台导入商品sku
            goodsAresProvider.dispatchFunction(new DispatcherFunctionRequest("addGoodsSkuIds",skuIds.toArray()));
        }

        operateLogMQUtil.convertAndSend("商品","商品模板导入","商品模板导入");

        return BaseResponse.success(Boolean.TRUE);
    }

    /**
     * 查询需同步商品列表
     *
     * @param queryRequest 商品
     * @return 商品详情
     */
    @ApiOperation(value = "查询需同步商品列表")
    @RequestMapping(value = "/spus/need/syn", method = RequestMethod.POST)
    public BaseResponse<GoodsListNeedSynResponse> listNeedSyn(@RequestBody GoodsQueryNeedSynRequest queryRequest) {
        queryRequest.setCompanyInfoId(commonUtil.getCompanyInfoId());
        queryRequest.setStoreId(commonUtil.getStoreId());

        BaseResponse<GoodsListNeedSynResponse> goodsListNeedSynResponse = goodsQueryProvider.listNeedSyn(queryRequest);
        GoodsListNeedSynResponse response = goodsListNeedSynResponse.getContext();

        return BaseResponse.success(response);
    }

    /**
     * 导出商品
     *
     * @return
     */
    @ApiOperation(value = "导出商品")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "encrypted", value = "解密", required = true)
    @RequestMapping(value = "/export/params/{encrypted}", method = RequestMethod.GET)
    public void export(@PathVariable String encrypted, HttpServletResponse response) {
        try {
            if (exportCount.incrementAndGet() > 1) {
                throw new SbcRuntimeException(SiteResultCode.ERROR_000016);
            }

            String decrypted = new String(Base64.getUrlDecoder().decode(encrypted.getBytes()));
            Operator operator = commonUtil.getOperator();
            log.debug(String.format("/goods/export/params, employeeId=%s", operator.getUserId()));

            //同分页页面数据保持一致
            EsSpuPageRequest esSpuPageRequest = JSON.parseObject(decrypted, EsSpuPageRequest.class);
            esSpuPageRequest.setPageNum(0);
            esSpuPageRequest.setPageSize(exportPageSize);
            List<GoodsPageSimpleVO> goodsPageSimpleVOList = this.list(esSpuPageRequest).getContext().getGoodsPage().getContent();
            List<GoodsVO> goodsVOList = KsBeanUtil.copyListProperties(goodsPageSimpleVOList, GoodsVO.class);
            List<GoodsInfoVO> goodsInfos = new ArrayList<>();
            if(CollectionUtils.isNotEmpty(goodsVOList)) {
                List<String> goodsIds = goodsVOList.stream().map(GoodsVO::getGoodsId).collect(Collectors.toList());
                GoodsInfoListByConditionRequest request = GoodsInfoListByConditionRequest.builder()
                        .goodsIds(goodsIds)
                        .showPointFlag(true)
                        .delFlag(DeleteFlag.NO.ordinal()).build();
                goodsInfos = goodsInfoQueryProvider.listByCondition(request).getContext().getGoodsInfos();

            }

            String headerKey = "Content-Disposition";
            LocalDateTime dateTime = LocalDateTime.now();
            String fileName = String.format("批量导出商品_%s.xls", dateTime.format(DateTimeFormatter.ofPattern("yyyyMMddHHmm")));
            try {
                fileName = URLEncoder.encode(fileName, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                log.error("/goods/export/params, fileName={},", fileName, e);
            }
            String headerValue = String.format("attachment;filename=\"%s\";filename*=\"utf-8''%s\"", fileName, fileName);
            response.setHeader(headerKey, headerValue);

            try {
                List<GoodsExportVo> goodsExportVos = new ArrayList<>();
                if(CollectionUtils.isNotEmpty(goodsVOList)) {
                    goodsExportVos = goodsExportService.getExportData(goodsVOList, goodsInfos);
                }
                goodsExportService.export(goodsExportVos, response.getOutputStream(), Platform.SUPPLIER.equals(operator.getPlatform()));
                response.flushBuffer();
            } catch (IOException e) {
                throw new SbcRuntimeException(e);
            }
        } catch (Exception e) {
            log.error("/goods/export/params error: ", e);
            throw new SbcRuntimeException(SiteResultCode.ERROR_000001);
        } finally {
            exportCount.set(0);
        }
    }



}
