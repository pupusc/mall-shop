package com.wanmi.sbc.iep;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.StoreType;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.HttpUtil;
import com.wanmi.sbc.common.util.OsUtil;
import com.wanmi.sbc.customer.api.provider.company.CompanyInfoQueryProvider;
import com.wanmi.sbc.customer.api.request.company.CompanyInfoByIdRequest;
import com.wanmi.sbc.customer.bean.vo.CompanyInfoVO;
import com.wanmi.sbc.elastic.api.provider.goods.EsGoodsInfoElasticProvider;
import com.wanmi.sbc.elastic.api.provider.standard.EsStandardProvider;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsInfoModifyAddedStatusRequest;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsInfoRequest;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsInitProviderGoodsInfoRequest;
import com.wanmi.sbc.elastic.api.request.standard.EsStandardInitRequest;
import com.wanmi.sbc.goods.api.provider.ares.GoodsAresProvider;
import com.wanmi.sbc.goods.api.provider.brand.GoodsBrandQueryProvider;
import com.wanmi.sbc.goods.api.provider.cate.GoodsCateQueryProvider;
import com.wanmi.sbc.goods.api.provider.classify.ClassifyProvider;
import com.wanmi.sbc.goods.api.provider.excel.GoodsSupplierExcelProvider;
import com.wanmi.sbc.goods.api.provider.freight.FreightTemplateGoodsQueryProvider;
import com.wanmi.sbc.goods.api.provider.goods.GoodsProvider;
import com.wanmi.sbc.goods.api.provider.goods.GoodsQueryProvider;
import com.wanmi.sbc.goods.api.provider.info.GoodsInfoProvider;
import com.wanmi.sbc.goods.api.provider.info.GoodsInfoQueryProvider;
import com.wanmi.sbc.goods.api.provider.standard.StandardGoodsQueryProvider;
import com.wanmi.sbc.goods.api.provider.standard.StandardImportProvider;
import com.wanmi.sbc.goods.api.provider.storecate.StoreCateQueryProvider;
import com.wanmi.sbc.goods.api.request.ares.DispatcherFunctionRequest;
import com.wanmi.sbc.goods.api.request.brand.GoodsBrandByIdRequest;
import com.wanmi.sbc.goods.api.request.cate.GoodsCateByIdRequest;
import com.wanmi.sbc.goods.api.request.excel.GoodsSupplierExcelExportTemplateIEPByStoreIdRequest;
import com.wanmi.sbc.goods.api.request.goods.*;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoListByIdsRequest;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoModifyRequest;
import com.wanmi.sbc.goods.api.request.standard.StandardIdsByGoodsIdsRequest;
import com.wanmi.sbc.goods.api.request.storecate.StoreCateByStoreCateIdRequest;
import com.wanmi.sbc.goods.api.request.storecate.StoreCateListByGoodsRequest;
import com.wanmi.sbc.goods.api.response.brand.GoodsBrandByIdResponse;
import com.wanmi.sbc.goods.api.response.cate.GoodsCateByIdResponse;
import com.wanmi.sbc.goods.api.response.goods.GoodsAddResponse;
import com.wanmi.sbc.goods.api.response.goods.GoodsByIdResponse;
import com.wanmi.sbc.goods.api.response.goods.GoodsModifyResponse;
import com.wanmi.sbc.goods.api.response.goods.GoodsViewByIdResponse;
import com.wanmi.sbc.goods.api.response.info.GoodsInfoListByIdsResponse;
import com.wanmi.sbc.goods.api.response.standard.StandardIdsByGoodsIdsResponse;
import com.wanmi.sbc.goods.api.response.standard.StandardImportStandardRequest;
import com.wanmi.sbc.goods.api.response.storecate.StoreCateByStoreCateIdResponse;
import com.wanmi.sbc.goods.api.response.storecate.StoreCateListByGoodsResponse;
import com.wanmi.sbc.goods.bean.enums.AddedFlag;
import com.wanmi.sbc.goods.bean.enums.CheckStatus;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.goods.bean.vo.GoodsSpecDetailVO;
import com.wanmi.sbc.goods.bean.vo.GoodsSpecVO;
import com.wanmi.sbc.goods.bean.vo.StoreCateGoodsRelaVO;
import com.wanmi.sbc.goods.request.GoodsSupplierExcelImportRequest;
import com.wanmi.sbc.goods.service.SupplierGoodsExcelService;
import com.wanmi.sbc.setting.api.provider.operatedatalog.OperateDataLogSaveProvider;
import com.wanmi.sbc.setting.api.request.operatedatalog.OperateDataLogAddRequest;
import com.wanmi.sbc.util.CommonUtil;
import com.wanmi.sbc.util.OperateLogMQUtil;
import io.seata.spring.annotation.GlobalTransactional;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Decoder;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 商品服务
 * Created by daiyitian on 17/4/12.
 */
@Api(tags = "StoreGoodsForIEPController", description = "商品服务 API")
@RestController
@Slf4j
@RequestMapping("/iep/goods")
public class StoreGoodsForIEPController {

    @Autowired
    GoodsAresProvider goodsAresProvider;


    @Autowired
    private GoodsProvider goodsProvider;

    @Autowired
    private GoodsSupplierExcelProvider goodsSupplierExcelProvider;

    @Autowired
    private CompanyInfoQueryProvider companyInfoQueryProvider;


    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private OperateLogMQUtil operateLogMQUtil;

    @Autowired
    private SupplierGoodsExcelService supplierGoodsExcelService;

    @Autowired
    private FreightTemplateGoodsQueryProvider freightTemplateGoodsQueryProvider;

    @Autowired
    private GoodsInfoProvider goodsInfoProvider;

    @Autowired
    private GoodsQueryProvider goodsQueryProvider;

    @Autowired
    private OperateDataLogSaveProvider operateDataLogSaveProvider;

    @Autowired
    private StoreCateQueryProvider storeCateQueryProvider;

    @Autowired
    private GoodsCateQueryProvider goodsCateQueryProvider;

    @Autowired
    private OsUtil osUtil;

    @Autowired
    private GoodsBrandQueryProvider goodsBrandQueryProvider;

    @Autowired
    private EsGoodsInfoElasticProvider esGoodsInfoElasticProvider;

    @Autowired
    private StandardImportProvider standardImportProvider;

    @Autowired
    private StandardGoodsQueryProvider standardGoodsQueryProvider;

    @Autowired
    private GoodsInfoQueryProvider goodsInfoQueryProvider;

    @Autowired
    private EsStandardProvider esStandardProvider;

    @Autowired
    private ClassifyProvider classifyProvider;

    public static int GOODS_SOURCE_PROVIDER = 0;

    /**
     * 编辑商品SKU
     */
    @ApiOperation(value = "编辑商品SKU")
    @RequestMapping(value = "/sku", method = RequestMethod.PUT)
    public ResponseEntity<BaseResponse> edit(@RequestBody GoodsInfoModifyRequest saveRequest) {
        if (saveRequest.getGoodsInfo() == null || saveRequest.getGoodsInfo().getGoodsInfoId() == null) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        goodsInfoProvider.modify(saveRequest);
        GoodsByIdRequest goodsByIdRequest = new GoodsByIdRequest();
        goodsByIdRequest.setGoodsId(saveRequest.getGoodsInfo().getGoodsId());
        GoodsByIdResponse goodsByIdResponse = goodsQueryProvider.getById(goodsByIdRequest).getContext();
        if (goodsByIdResponse != null) {
            esGoodsInfoElasticProvider.initProviderEsGoodsInfo(EsGoodsInitProviderGoodsInfoRequest.builder().
                    storeId(null).providerGoodsIds(Collections.singletonList(goodsByIdRequest.getGoodsId())).build());

            //刷新商品库es
            esStandardProvider.init(EsStandardInitRequest.builder()
                    .relGoodsIds(Collections.singletonList(saveRequest.getGoodsInfo().getGoodsId())).build());
        }
        operateLogMQUtil.convertAndSend("商品", "编辑商品", "编辑商品：SKU编码" + saveRequest.getGoodsInfo().getGoodsInfoNo());

        return ResponseEntity.ok(BaseResponse.SUCCESSFUL());
    }

    /**
     * 新增商品
     */
    @ApiOperation(value = "新增商品")
    @RequestMapping(value = "/spu", method = RequestMethod.POST)
    public BaseResponse<String> add(@RequestBody @Valid GoodsAddRequest request) {
        CompanyInfoVO companyInfo = companyInfoQueryProvider.getCompanyInfoById(
                CompanyInfoByIdRequest.builder().companyInfoId(commonUtil.getCompanyInfoId()).build()
        ).getContext();
        if (companyInfo == null) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }

        request.getGoods().setCompanyInfoId(commonUtil.getCompanyInfoId());
        request.getGoods().setCompanyType(companyInfo.getCompanyType());
        request.getGoods().setStoreId(commonUtil.getStoreId());
        request.getGoods().setSupplierName(companyInfo.getSupplierName());
        BaseResponse<GoodsAddResponse> baseResponse = goodsProvider.add(request);
        GoodsAddResponse response = baseResponse.getContext();
        String goodsId = Optional.ofNullable(response)
                .map(GoodsAddResponse::getResult)
                .orElse(null);

        operateLogMQUtil.convertAndSend("商品", "直接发布",
                "直接发布：SPU编码" + request.getGoods().getGoodsNo());

        //供应商商品审核成功直接加入到商品库
        //store_cate_goods_rela为联合主键，seata不支持联合主键，放弃使用seata,,加入商品库操作移至goodsService.add()
        if (StringUtils.isNotBlank(goodsId)) {

            GoodsByIdRequest goodsByIdRequest = new GoodsByIdRequest();
            goodsByIdRequest.setGoodsId(goodsId);
            GoodsByIdResponse goods = goodsQueryProvider.getById(goodsByIdRequest).getContext();

            //初始化ES
            esGoodsInfoElasticProvider.initEsGoodsInfo(EsGoodsInfoRequest.builder().goodsId(goodsId).build());

            if (goods.getAuditStatus() == CheckStatus.CHECKED && GOODS_SOURCE_PROVIDER == goods.getGoodsSource()) {
                StandardIdsByGoodsIdsResponse standardIdsResponse
                        = standardGoodsQueryProvider
                                .listStandardIdsByGoodsIds(StandardIdsByGoodsIdsRequest.builder().goodsIds(Arrays.asList(goodsId)).build())
                                .getContext();

                //初始化商品库ES
                if(CollectionUtils.isNotEmpty(standardIdsResponse.getStandardIds())){
                    esStandardProvider.init(EsStandardInitRequest.builder().goodsIds(standardIdsResponse.getStandardIds()).build());
                }
                //操作日志记录
                operateLogMQUtil.convertAndSend("商品", "加入商品库",
                        "加入商品库：SPU编码" + goods.getGoodsNo());
            }
        }
        return BaseResponse.success(goodsId);
    }

    /**
     * 编辑商品
     */
    @ApiOperation(value = "编辑商品")
    @RequestMapping(value = "/spu", method = RequestMethod.PUT)
    public BaseResponse edit(@RequestBody @Valid GoodsModifyRequest request) {
        GoodsViewByIdRequest goodsViewByIdRequest = new GoodsViewByIdRequest();
        goodsViewByIdRequest.setGoodsId(request.getGoods().getGoodsId());
        GoodsViewByIdResponse oldData = goodsQueryProvider.getViewById(goodsViewByIdRequest).getContext();
        //获取商品店铺分类
        if (osUtil.isS2b()) {
            Map<String, List<Integer>> storeCateIdMap = classifyProvider.searchGroupedClassifyIdByGoodsId(Collections.singletonList(request.getGoods().getGoodsId())).getContext();
            if(storeCateIdMap != null){
                oldData.getGoods().setStoreCateIds(storeCateIdMap.get(request.getGoods().getGoodsId()).stream().map(Integer::longValue).collect(Collectors.toList()));
            }
        }

        GoodsModifyResponse response = goodsProvider.modify(request).getContext();
        //更新关联的商家商品es
        esGoodsInfoElasticProvider.initProviderEsGoodsInfo(EsGoodsInitProviderGoodsInfoRequest.builder().storeId(null)
                .providerGoodsIds(Collections.singletonList(request.getGoods().getGoodsId())).build());

        //刷新商品库
        if(CollectionUtils.isNotEmpty(response.getStandardIds())){
            esStandardProvider.init(EsStandardInitRequest.builder().goodsIds(response.getStandardIds()).build());
        }

        //ares埋点-商品-后台修改商品sku,迁移至goods微服务下
        operateLogMQUtil.convertAndSend("商品", "编辑商品",
                "编辑商品：SPU编码" + request.getGoods().getGoodsNo());
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 组装日志
     *
     * @param newData
     * @return
     */
    private OperateDataLogAddRequest buildOperateDataLog(GoodsModifyRequest newData, GoodsViewByIdResponse oldData) {
        OperateDataLogAddRequest operateDataLogAddRequest = new OperateDataLogAddRequest();
        operateDataLogAddRequest.setOperateId(newData.getGoods().getGoodsId());
        if (StringUtils.isNotEmpty(commonUtil.getOperatorId())) {
            operateDataLogAddRequest.setOperateAccount(commonUtil.getOperatorId());
        }
        if (StringUtils.isNotEmpty(commonUtil.getAccountName())) {
            operateDataLogAddRequest.setOperateName(commonUtil.getAccountName());
        }
        operateDataLogAddRequest.setOperateTime(LocalDateTime.now());


        StringBuilder operateContent = new StringBuilder();
        StringBuilder operateBeforeData = new StringBuilder();
        StringBuilder operateAfterData = new StringBuilder();

        //基本信息
        //商品名称必填项
        operateBeforeData.append("商品名称：").append(oldData.getGoods().getGoodsName());
        operateAfterData.append("商品名称：").append(newData.getGoods().getGoodsName());
        if (!oldData.getGoods().getGoodsName().equals(newData.getGoods().getGoodsName())) {
            operateContent.append("修改商品名称,");
        }
        //SPU编码 必填项
        operateBeforeData.append("SPU编码：").append(oldData.getGoods().getGoodsNo());
        operateAfterData.append("SPU编码：").append(newData.getGoods().getGoodsNo());
        if (!oldData.getGoods().getGoodsNo().equals(newData.getGoods().getGoodsNo())) {
            operateContent.append("修改SPU编码,");
        }

        //平台类目 必填项
        GoodsCateByIdRequest goodsCateByIdRequest = new GoodsCateByIdRequest();
        goodsCateByIdRequest.setCateId(oldData.getGoods().getCateId());
        GoodsCateByIdResponse goodsCateByIdResponseOld =
                goodsCateQueryProvider.getById(goodsCateByIdRequest).getContext();
        goodsCateByIdRequest.setCateId(newData.getGoods().getCateId());
        GoodsCateByIdResponse goodsCateByIdResponseNew =
                goodsCateQueryProvider.getById(goodsCateByIdRequest).getContext();
        if (goodsCateByIdResponseOld != null && goodsCateByIdResponseNew != null) {
            operateBeforeData.append("平台类目：").append(goodsCateByIdResponseOld.getCateName());
            operateAfterData.append("平台类目：").append(goodsCateByIdResponseNew.getCateName());
            if (!oldData.getGoods().getCateId().equals(newData.getGoods().getCateId())) {
                operateContent.append("修改平台类目,");
            }
        }

        //店铺分类 必填项
        List<Long> storeCateIdsOld = oldData.getGoods().getStoreCateIds();
        StringBuilder cateNameOld = new StringBuilder();
        for (Long storeCateId : storeCateIdsOld) {
            StoreCateByStoreCateIdRequest storeCateByStoreCateIdRequest = new StoreCateByStoreCateIdRequest();
            storeCateByStoreCateIdRequest.setStoreCateId(storeCateId);
            StoreCateByStoreCateIdResponse storeCateByStoreCateIdResponse =
                    storeCateQueryProvider.getByStoreCateId(storeCateByStoreCateIdRequest).getContext();
            if (storeCateByStoreCateIdResponse != null) {
                cateNameOld.append(storeCateByStoreCateIdResponse.getStoreCateResponseVO().getCateName());
            }
        }

        List<Long> storeCateIdsNew = newData.getGoods().getStoreCateIds();
        StringBuilder cateNameNew = new StringBuilder();
        for (Long storeCateId : storeCateIdsNew) {
            StoreCateByStoreCateIdRequest storeCateByStoreCateIdRequest = new StoreCateByStoreCateIdRequest();
            storeCateByStoreCateIdRequest.setStoreCateId(storeCateId);
            StoreCateByStoreCateIdResponse storeCateByStoreCateIdResponse =
                    storeCateQueryProvider.getByStoreCateId(storeCateByStoreCateIdRequest).getContext();
            if (storeCateByStoreCateIdResponse != null) {
                cateNameNew.append(storeCateByStoreCateIdResponse.getStoreCateResponseVO().getCateName());
            }
        }
        operateBeforeData.append("店铺分类：").append(cateNameOld.toString());
        operateAfterData.append("店铺分类：").append(cateNameNew.toString());
        if (!cateNameOld.toString().equals(cateNameNew.toString())) {
            operateContent.append("修改店铺分类,");
        }

        //商品品牌 非必填项
        String brandNameOld = StringUtils.EMPTY;
        Long brandId = oldData.getGoods().getBrandId();
        if (brandId != null) {
            GoodsBrandByIdResponse goodsBrandByIdResponse =
                    goodsBrandQueryProvider.getById(GoodsBrandByIdRequest.builder().brandId(brandId).build()).getContext();
            if (goodsBrandByIdResponse != null) {
                operateBeforeData.append("品牌：").append(goodsBrandByIdResponse.getBrandName());
                brandNameOld = goodsBrandByIdResponse.getBrandName();
            }
        }

        String brandNameNew = StringUtils.EMPTY;
        Long brandIdNew = oldData.getGoods().getBrandId();
        if (brandId != null) {
            GoodsBrandByIdResponse goodsBrandByIdResponse =
                    goodsBrandQueryProvider.getById(GoodsBrandByIdRequest.builder().brandId(brandIdNew).build()).getContext();
            if (goodsBrandByIdResponse != null) {
                operateAfterData.append("品牌：").append(goodsBrandByIdResponse.getBrandName());
                brandNameNew = goodsBrandByIdResponse.getBrandName();
            }
        }
        if (!brandNameOld.equals(brandNameNew)) {
            operateContent.append("修改商品品牌,");
        }

        //计量单位 必填项
        operateBeforeData.append("计量单位：").append(oldData.getGoods().getGoodsUnit());
        operateAfterData.append("计量单位：").append(newData.getGoods().getGoodsUnit());
        if (!oldData.getGoods().getGoodsUnit().equals(newData.getGoods().getGoodsUnit())) {
            operateContent.append("修改计量单位");
        }

        //商品副标题 非必填项
        String titleOld = StringUtils.EMPTY;
        if (oldData.getGoods().getGoodsSubtitle() != null) {
            operateBeforeData.append("商品副标题：").append(oldData.getGoods().getGoodsSubtitle());
            titleOld = oldData.getGoods().getGoodsSubtitle();
        }

        String titleNew = StringUtils.EMPTY;
        if (newData.getGoods().getGoodsSubtitle() != null) {
            operateAfterData.append("商品副标题：").append(newData.getGoods().getGoodsSubtitle());
            titleNew = newData.getGoods().getGoodsSubtitle();
        }
        if (!titleOld.equals(titleNew)) {
            operateContent.append("修改商品副标题,");
        }

        //上下架 必填项
        operateBeforeData.append("上下架：").append(oldData.getGoods().getAddedFlag());
        operateAfterData.append("上下架：").append(newData.getGoods().getAddedFlag());
        if (!oldData.getGoods().getAddedFlag().equals(newData.getGoods().getAddedFlag())) {
            operateContent.append("修改上下架,");
        }

        //商品图片 非必填项 主图
        String goodsImgOld = StringUtils.EMPTY;
        if (oldData.getGoods().getGoodsImg() != null) {
            operateBeforeData.append("商品图片：").append(oldData.getGoods().getGoodsImg());
            goodsImgOld = oldData.getGoods().getGoodsImg();
        }

        String goodsImgNew = StringUtils.EMPTY;
        if (CollectionUtils.isNotEmpty(newData.getImages())) {
            operateAfterData.append("商品图片：").append(newData.getImages().get(0).getArtworkUrl());
            goodsImgNew = newData.getImages().get(0).getArtworkUrl();
        }

        if (!goodsImgOld.equals(goodsImgNew)) {
            operateContent.append("修改商品图片,");
        }

        //商品视频 非必填项
        String goodsVideOld = StringUtils.EMPTY;
        if (oldData.getGoods().getGoodsVideo() != null) {
            operateBeforeData.append("商品视频：").append(oldData.getGoods().getGoodsVideo());
            goodsVideOld = oldData.getGoods().getGoodsVideo();
        }
        String goodsVideNew = StringUtils.EMPTY;
        if (newData.getGoods().getGoodsVideo() != null) {
            operateAfterData.append("商品视频：").append(newData.getGoods().getGoodsVideo());
            goodsVideNew = oldData.getGoods().getGoodsVideo();
        }
        if (!goodsVideOld.equals(goodsVideNew)) {
            operateContent.append("修改商品视频,");
        }

        //属性信息
        boolean proInfoFlag = false;


        for (GoodsInfoVO goodsInfoVO : oldData.getGoodsInfos()) {
            //商品图片
            String goodsInfoImg = goodsInfoVO.getGoodsInfoImg() == null ? StringUtils.EMPTY :
                    goodsInfoVO.getGoodsInfoImg();
            //sku编码
            String sku = goodsInfoVO.getGoodsInfoNo() == null ? StringUtils.EMPTY : goodsInfoVO.getGoodsInfoNo();
            //供货价
            BigDecimal supplyPrice = goodsInfoVO.getSupplyPrice() == null ? BigDecimal.ZERO :
                    goodsInfoVO.getSupplyPrice();
            //库存
            Long stock = goodsInfoVO.getStock() == null ? 0L : goodsInfoVO.getStock();
            //条形码
            String barCode = goodsInfoVO.getGoodsInfoBarcode() == null ? StringUtils.EMPTY :
                    goodsInfoVO.getGoodsInfoBarcode();
            StringBuilder proInfo = new StringBuilder();
            if (CollectionUtils.isNotEmpty(goodsInfoVO.getMockSpecIds())) {
                Map<Long, String> specIdMap = new HashMap<>();
                Map<Long, String> specDetailIdMap = new HashMap<>();
                if (CollectionUtils.isNotEmpty(oldData.getGoodsSpecs())) {
                    for (GoodsSpecVO goodsSpecVO : oldData.getGoodsSpecs()) {
                        specIdMap.put(goodsSpecVO.getSpecId(), goodsSpecVO.getSpecName());
                    }
                }
                if (CollectionUtils.isNotEmpty(oldData.getGoodsSpecDetails())) {
                    for (GoodsSpecDetailVO goodsSpecDetailVO : oldData.getGoodsSpecDetails()) {
                        specDetailIdMap.put(goodsSpecDetailVO.getSpecDetailId(), goodsSpecDetailVO.getDetailName());
                    }
                }
                for (int i = 0; i < goodsInfoVO.getMockSpecIds().size(); i++) {
                    String specName = specIdMap.get(goodsInfoVO.getMockSpecIds().get(i));
                    String detailName = specDetailIdMap.get(goodsInfoVO.getMockSpecDetailIds().get(i));
                    if (StringUtils.isNotEmpty(specName) && StringUtils.isNotEmpty(detailName)) {
                        proInfo.append(specName).append(":").append(detailName);
                    }
                }
            }
            operateBeforeData.append("sku编码：").append(sku).append("图片：").append(goodsInfoImg).append(proInfo + ":").append("供货价：").append(supplyPrice).append("库存：").append(stock).append("条形码：").append(barCode);
        }

        for (GoodsInfoVO goodsInfoVO : newData.getGoodsInfos()) {
            //商品图片
            String goodsInfoImg = goodsInfoVO.getGoodsInfoImg() == null ? StringUtils.EMPTY :
                    goodsInfoVO.getGoodsInfoImg();
            //sku编码
            String sku = goodsInfoVO.getGoodsInfoNo() == null ? StringUtils.EMPTY : goodsInfoVO.getGoodsInfoNo();
            //供货价
            BigDecimal supplyPrice = goodsInfoVO.getSupplyPrice() == null ? BigDecimal.ZERO :
                    goodsInfoVO.getSupplyPrice();
            //库存
            Long stock = goodsInfoVO.getStock() == null ? 0L : goodsInfoVO.getStock();
            //条形码
            String barCode = goodsInfoVO.getGoodsInfoBarcode() == null ? StringUtils.EMPTY :
                    goodsInfoVO.getGoodsInfoBarcode();
            StringBuilder proInfo = new StringBuilder();
            if (CollectionUtils.isNotEmpty(goodsInfoVO.getMockSpecIds())) {
                Map<Long, String> specIdMap = new HashMap<>();
                Map<Long, String> specDetailIdMap = new HashMap<>();
                if (CollectionUtils.isNotEmpty(oldData.getGoodsSpecs())) {
                    for (GoodsSpecVO goodsSpecVO : oldData.getGoodsSpecs()) {
                        specIdMap.put(goodsSpecVO.getSpecId(), goodsSpecVO.getSpecName());
                    }
                }
                if (CollectionUtils.isNotEmpty(oldData.getGoodsSpecDetails())) {
                    for (GoodsSpecDetailVO goodsSpecDetailVO : oldData.getGoodsSpecDetails()) {
                        specDetailIdMap.put(goodsSpecDetailVO.getSpecDetailId(), goodsSpecDetailVO.getDetailName());
                    }
                }
                for (int i = 0; i < goodsInfoVO.getMockSpecIds().size(); i++) {
                    String specName = specIdMap.get(goodsInfoVO.getMockSpecIds().get(i));
                    String detailName = specDetailIdMap.get(goodsInfoVO.getMockSpecDetailIds().get(i));
                    if (StringUtils.isNotEmpty(specName) && StringUtils.isNotEmpty(detailName)) {
                        proInfo.append(specName).append(":").append(detailName);
                    }
                }
            }
            operateAfterData.append("sku编码：").append(sku).append("图片：").append(goodsInfoImg).append(proInfo + ":").append("供货价：").append(supplyPrice).append("库存：").append(stock).append("条形码：").append(barCode);

        }

        for (GoodsInfoVO goodsInfoVOOld : oldData.getGoodsInfos()) {
            for (GoodsInfoVO goodsInfoVONew : newData.getGoodsInfos()) {
                if (goodsInfoVOOld.getGoodsInfoId().equals(goodsInfoVONew.getGoodsInfoId())) {
                    if (goodsInfoVOOld.getGoodsInfoImg() != null && goodsInfoVONew.getGoodsInfoImg() != null && !goodsInfoVOOld.getGoodsInfoImg().equals(goodsInfoVONew.getGoodsInfoImg())) {
                        proInfoFlag = true;
                    }
                    if (goodsInfoVOOld.getGoodsInfoNo() != null && goodsInfoVONew.getGoodsInfoNo() != null && !goodsInfoVOOld.getGoodsInfoNo().equals(goodsInfoVONew.getGoodsInfoNo())) {
                        proInfoFlag = true;
                    }
                    if (goodsInfoVOOld.getSupplyPrice() != null && goodsInfoVONew.getSupplyPrice() != null && goodsInfoVOOld.getSupplyPrice().compareTo(goodsInfoVONew.getSupplyPrice()) != 0) {
                        proInfoFlag = true;
                    }
                    if (goodsInfoVOOld.getStock() != null && goodsInfoVONew.getStock() != null && !goodsInfoVOOld.getStock().equals(goodsInfoVONew.getStock())) {
                        proInfoFlag = true;
                    }
                    if (goodsInfoVOOld.getGoodsInfoBarcode() != null && goodsInfoVONew.getGoodsInfoBarcode() != null && !goodsInfoVOOld.getGoodsInfoBarcode().equals(goodsInfoVONew.getGoodsInfoBarcode())) {
                        proInfoFlag = true;
                    }
                }
            }
        }
        if ((CollectionUtils.isEmpty(oldData.getGoodsSpecs()) || CollectionUtils.isEmpty(newData.getGoodsSpecs()))
                && (CollectionUtils.isNotEmpty(oldData.getGoodsSpecs()) && CollectionUtils.isNotEmpty(newData.getGoodsSpecs()))) {
            proInfoFlag = true;

        }
        if (CollectionUtils.isNotEmpty(oldData.getGoodsSpecs()) && CollectionUtils.isNotEmpty(newData.getGoodsSpecDetails())) {
            List<Long> specsIdOld = oldData.getGoodsSpecs().stream().map(goodsSpecVO -> {
                return goodsSpecVO.getSpecId();
            }).collect(Collectors.toList());
            List<Long> specsIdNew = newData.getGoodsSpecs().stream().map(goodsSpecVO -> {
                return goodsSpecVO.getSpecId();
            }).collect(Collectors.toList());
            if (specsIdOld.size() == specsIdNew.size()) {
                if (!specsIdOld.containsAll(specsIdNew)) {
                    proInfoFlag = true;
                }
            } else {
                if (!specsIdOld.containsAll(specsIdNew)) {
                    proInfoFlag = true;
                }
            }

        }

        if ((CollectionUtils.isEmpty(oldData.getGoodsSpecDetails()) || CollectionUtils.isEmpty(newData.getGoodsSpecDetails()))
                && (CollectionUtils.isNotEmpty(oldData.getGoodsSpecDetails()) && CollectionUtils.isNotEmpty(newData.getGoodsSpecDetails()))) {
            proInfoFlag = true;

        }
        if (CollectionUtils.isNotEmpty(oldData.getGoodsSpecDetails()) && CollectionUtils.isNotEmpty(newData.getGoodsSpecDetails())) {
            List<Long> specsDetailIdOld = oldData.getGoodsSpecDetails().stream().map(goodsSpecDetailVO -> {
                return goodsSpecDetailVO.getSpecDetailId();
            }).collect(Collectors.toList());
            List<Long> specsDetailIdNew = newData.getGoodsSpecDetails().stream().map(goodsSpecDetailVO -> {
                return goodsSpecDetailVO.getSpecDetailId();
            }).collect(Collectors.toList());
            if (specsDetailIdOld.size() == specsDetailIdNew.size()) {
                if (!specsDetailIdOld.containsAll(specsDetailIdNew)) {
                    proInfoFlag = true;
                }
            } else {
                if (!specsDetailIdOld.containsAll(specsDetailIdNew)) {
                    proInfoFlag = true;
                }
            }
        }

        if (proInfoFlag) {
            operateContent.append("修改规格,");
        }

        //物流信息
        //运费模版
        operateBeforeData.append("运费模版：").append(oldData.getGoods().getFreightTempName());
        operateAfterData.append("运费模版：").append(newData.getGoods().getFreightTempName());
        if (!oldData.getGoods().getFreightTempName().equals(newData.getGoods().getFreightTempName())) {
            operateContent.append("修改运费模版,");
        }
        //物流重量
        operateBeforeData.append("物流重量：").append(oldData.getGoods().getGoodsWeight());
        operateAfterData.append("物流重量：").append(newData.getGoods().getGoodsWeight());
        if (oldData.getGoods().getGoodsWeight().compareTo(newData.getGoods().getGoodsWeight()) != 0) {
            operateContent.append("修改物流重量,");
        }
        //物流体积
        operateBeforeData.append("物流体积：").append(oldData.getGoods().getGoodsCubage());
        operateAfterData.append("物体积：").append(newData.getGoods().getGoodsCubage());
        if (oldData.getGoods().getGoodsCubage().compareTo(newData.getGoods().getGoodsCubage()) != 0) {
            operateContent.append("修改物流体积,");
        }
        //商品详情 非必填项
        String goodsDetailOld = StringUtils.EMPTY;
        if (oldData.getGoods().getGoodsDetail() != null) {
            operateBeforeData.append("商品详情：").append(oldData.getGoods().getGoodsDetail());
            goodsDetailOld = oldData.getGoods().getGoodsDetail();
        }
        String goodsDetailNew = StringUtils.EMPTY;
        if (newData.getGoods().getGoodsDetail() != null) {
            operateAfterData.append("商品详情：").append(newData.getGoods().getGoodsDetail());
            goodsDetailNew = newData.getGoods().getGoodsDetail();
        }
        if (!goodsDetailOld.equals(goodsDetailNew)) {
            operateContent.append("修改商品详情,");
        }
        operateDataLogAddRequest.setOperateBeforeData(operateBeforeData.toString());
        operateDataLogAddRequest.setOperateAfterData(operateAfterData.toString());
        operateDataLogAddRequest.setOperateContent(operateContent.toString());
        return operateDataLogAddRequest;
    }

    /**
     * 下载模板
     */
    @ApiOperation(value = "下载模板")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "encrypted", value = "加密", required = true)
    @RequestMapping(value = "/excel/template/{encrypted}", method = RequestMethod.GET)
    public void template(@PathVariable String encrypted) {
        GoodsSupplierExcelExportTemplateIEPByStoreIdRequest request =
                new GoodsSupplierExcelExportTemplateIEPByStoreIdRequest();
        request.setStoreId(commonUtil.getStoreId());
        String file = goodsSupplierExcelProvider.supplierExportTemplateIEP(request).getContext().getFile();
        if (StringUtils.isNotBlank(file)) {
            try {
                String fileName = URLEncoder.encode("商品导入模板.xls", "UTF-8");
                HttpUtil.getResponse().setHeader("Content-Disposition", String.format("attachment;filename=\"%s\";" +
                        "filename*=\"utf-8''%s\"", fileName, fileName));
                HttpUtil.getResponse().getOutputStream().write(new BASE64Decoder().decodeBuffer(file));
            } catch (Exception e) {
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
        if (!("xls".equalsIgnoreCase(ext) || "xlsx".equalsIgnoreCase(ext))) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }

        CompanyInfoVO companyInfo = companyInfoQueryProvider.getCompanyInfoById(
                CompanyInfoByIdRequest.builder().companyInfoId(commonUtil.getCompanyInfoId()).build()
        ).getContext();
        if (companyInfo == null) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }

        GoodsSupplierExcelImportRequest importRequest = new GoodsSupplierExcelImportRequest();
        importRequest.setExt(ext);
        importRequest.setUserId(commonUtil.getOperatorId());
        importRequest.setCompanyInfoId(commonUtil.getCompanyInfoId());
        importRequest.setStoreId(commonUtil.getStoreId());
        importRequest.setCompanyType(companyInfo.getCompanyType());
        importRequest.setSupplierName(companyInfo.getSupplierName());
        importRequest.setType(StoreType.PROVIDER);
        List<String> skuIds = supplierGoodsExcelService.implGoods(importRequest);

        //加入ES调整至MQ了
        /*if(CollectionUtils.isNotEmpty(skuIds)){
            esGoodsInfoElasticProvider.initEsGoodsInfo(EsGoodsInfoRequest.builder().skuIds(skuIds).build());
        }*/

        operateLogMQUtil.convertAndSend("商品", "商品模板导入", "商品模板导入");

        //供应商商品审核成功直接加入到商品库
        GoodsInfoListByIdsResponse goodsInfoListByIdsResponse =
                goodsInfoQueryProvider.listByIds(GoodsInfoListByIdsRequest.builder().goodsInfoIds(skuIds).build()).getContext();
        List<GoodsInfoVO> goodsInfoVOList = goodsInfoListByIdsResponse.getGoodsInfos();
        if (goodsInfoListByIdsResponse != null && CollectionUtils.isNotEmpty(goodsInfoVOList)) {
            List<String> goodsIds =
                    goodsInfoVOList.stream().map(goodsInfoVO -> goodsInfoVO.getGoodsId()).collect(Collectors.toList());
            GoodsByIdRequest goodsByIdRequest = new GoodsByIdRequest();
            goodsByIdRequest.setGoodsId(goodsIds.get(0));
            GoodsByIdResponse goods = goodsQueryProvider.getById(goodsByIdRequest).getContext();

            if (goods.getAuditStatus() == CheckStatus.CHECKED && GOODS_SOURCE_PROVIDER == goods.getGoodsSource()) {

                StandardImportStandardRequest standardImportStandardRequest = new StandardImportStandardRequest();
                standardImportStandardRequest.setGoodsIds(goodsIds);
                List<String> standardIds = standardImportProvider.importStandard(standardImportStandardRequest).getContext().getStandardIds();
                //初始化商品库ES
                if(CollectionUtils.isNotEmpty(standardIds)){
                    esStandardProvider.init(EsStandardInitRequest.builder().goodsIds(standardIds).build());
                }
                //操作日志记录
                operateLogMQUtil.convertAndSend("商品", "加入商品库",
                        "加入商品库：SPU编码" + goods.getGoodsNo());
            }
        }

        return BaseResponse.success(Boolean.TRUE);
    }


    /**
     * 批量上架商品
     */
    @ApiOperation(value = "批量上架商品")
    @RequestMapping(value = "/spu/sale", method = RequestMethod.PUT)
    public BaseResponse onSale(@RequestBody GoodsModifyAddedStatusRequest request) {
        if (CollectionUtils.isEmpty(request.getGoodsIds())) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        request.setAddedFlag(AddedFlag.YES.toValue());

        //供应商商品上架
        goodsProvider.providerModifyAddedStatus(request);

        //更新关联的商家商品es
        esGoodsInfoElasticProvider.initProviderEsGoodsInfo(EsGoodsInitProviderGoodsInfoRequest.builder().
                storeId(null).providerGoodsIds(request.getGoodsIds()).build());

        //ares埋点-商品-后台批量修改商品spu的所有sku上下架状态
        goodsAresProvider.dispatchFunction(new DispatcherFunctionRequest("editGoodsSpuUp",
                new Object[]{AddedFlag.YES.toValue(), request.getGoodsIds()}));

        if (1 == request.getGoodsIds().size()) {
            GoodsByIdRequest goodsByIdRequest = new GoodsByIdRequest();
            goodsByIdRequest.setGoodsId(request.getGoodsIds().get(0));
            GoodsByIdResponse response = goodsQueryProvider.getById(goodsByIdRequest).getContext();
            operateLogMQUtil.convertAndSend("供应商商品", "上架",
                    "上架：SPU编码" + response.getGoodsNo());
        } else {
            operateLogMQUtil.convertAndSend("供应商商品", "批量上架", "批量上架");
        }

        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 批量下架商品
     */
    @ApiOperation(value = "批量下架商品")
    @RequestMapping(value = "/spu/sale", method = RequestMethod.DELETE)
    public BaseResponse offSale(@RequestBody GoodsModifyAddedStatusRequest request) {
        List<String> goodsIds = request.getGoodsIds();
        if (CollectionUtils.isEmpty(request.getGoodsIds())) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        request.setAddedFlag(AddedFlag.NO.toValue());
        goodsProvider.providerModifyAddedStatus(request);
        //更新ES
        esGoodsInfoElasticProvider.updateAddedStatus(EsGoodsInfoModifyAddedStatusRequest.builder().
                addedFlag(AddedFlag.NO.toValue()).goodsIds(request.getGoodsIds()).goodsInfoIds(null).build());
        //更新关联的商家商品es
        esGoodsInfoElasticProvider.initProviderEsGoodsInfo(EsGoodsInitProviderGoodsInfoRequest.builder().
                storeId(null).providerGoodsIds(request.getGoodsIds()).build());
        //ares埋点-商品-后台批量修改商品spu的所有sku上下架状态
        goodsAresProvider.dispatchFunction(new DispatcherFunctionRequest("editGoodsSpuUp",
                new Object[]{AddedFlag.NO.toValue(), request.getGoodsIds()}));

        if (1 == request.getGoodsIds().size()) {
            GoodsByIdRequest goodsByIdRequest = new GoodsByIdRequest();
            goodsByIdRequest.setGoodsId(request.getGoodsIds().get(0));
            GoodsByIdResponse response = goodsQueryProvider.getById(goodsByIdRequest).getContext();
            operateLogMQUtil.convertAndSend("商品", "下架",
                    "下架：SPU编码" + response.getGoodsNo());
        } else {
            operateLogMQUtil.convertAndSend("商品", "批量下架", "批量下架");
        }
        return BaseResponse.SUCCESSFUL();
    }
}
