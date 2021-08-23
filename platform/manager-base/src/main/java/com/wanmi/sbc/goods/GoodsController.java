package com.wanmi.sbc.goods;

import com.sbc.wanmi.erp.bean.vo.ERPGoodsInfoVO;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.constant.RedisKeyConstant;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.OsUtil;
import com.wanmi.sbc.customer.api.provider.company.CompanyInfoQueryProvider;
import com.wanmi.sbc.customer.api.request.company.CompanyInfoByIdRequest;
import com.wanmi.sbc.customer.bean.vo.CompanyInfoVO;
import com.wanmi.sbc.elastic.api.provider.goods.EsGoodsInfoElasticProvider;
import com.wanmi.sbc.elastic.api.provider.standard.EsStandardProvider;
import com.wanmi.sbc.elastic.api.request.goods.*;
import com.wanmi.sbc.elastic.api.request.standard.EsStandardInitRequest;
import com.wanmi.sbc.erp.api.provider.GuanyierpProvider;
import com.wanmi.sbc.erp.api.request.SynGoodsInfoRequest;
import com.wanmi.sbc.goods.api.provider.appointmentsale.AppointmentSaleQueryProvider;
import com.wanmi.sbc.goods.api.provider.ares.GoodsAresProvider;
import com.wanmi.sbc.goods.api.provider.bookingsale.BookingSaleQueryProvider;
import com.wanmi.sbc.goods.api.provider.brand.GoodsBrandQueryProvider;
import com.wanmi.sbc.goods.api.provider.cate.GoodsCateQueryProvider;
import com.wanmi.sbc.goods.api.provider.cyclebuy.CycleBuyQueryProvider;
import com.wanmi.sbc.goods.api.provider.freight.FreightTemplateGoodsQueryProvider;
import com.wanmi.sbc.goods.api.provider.goods.GoodsProvider;
import com.wanmi.sbc.goods.api.provider.goods.GoodsQueryProvider;
import com.wanmi.sbc.goods.api.provider.info.GoodsInfoQueryProvider;
import com.wanmi.sbc.goods.api.provider.spec.GoodsInfoSpecDetailRelQueryProvider;
import com.wanmi.sbc.goods.api.provider.storecate.StoreCateQueryProvider;
import com.wanmi.sbc.goods.api.request.appointmentsale.AppointmentSaleByGoodsIdRequest;
import com.wanmi.sbc.goods.api.request.ares.DispatcherFunctionRequest;
import com.wanmi.sbc.goods.api.request.bookingsale.BookingSaleByGoodsIdRequest;
import com.wanmi.sbc.goods.api.request.brand.GoodsBrandByIdRequest;
import com.wanmi.sbc.goods.api.request.brand.GoodsBrandByIdsRequest;
import com.wanmi.sbc.goods.api.request.cate.GoodsCateByIdRequest;
import com.wanmi.sbc.goods.api.request.cate.GoodsCateByIdsRequest;
import com.wanmi.sbc.goods.api.request.cyclebuy.CycleBuyByGoodsIdRequest;
import com.wanmi.sbc.goods.api.request.freight.FreightTemplateGoodsByIdRequest;
import com.wanmi.sbc.goods.api.request.freight.FreightTemplateGoodsExistsByIdRequest;
import com.wanmi.sbc.goods.api.request.goods.*;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoListByConditionRequest;
import com.wanmi.sbc.goods.api.request.spec.GoodsInfoSpecDetailRelBySkuIdsRequest;
import com.wanmi.sbc.goods.api.request.storecate.StoreCateByStoreCateIdRequest;
import com.wanmi.sbc.goods.api.request.storecate.StoreCateListByGoodsRequest;
import com.wanmi.sbc.goods.api.response.appointmentsale.AppointmentSaleNotEndResponse;
import com.wanmi.sbc.goods.api.response.bookingsale.BookingSaleNotEndResponse;
import com.wanmi.sbc.goods.api.response.brand.GoodsBrandByIdResponse;
import com.wanmi.sbc.goods.api.response.cate.GoodsCateByIdResponse;
import com.wanmi.sbc.goods.api.response.freight.FreightTemplateGoodsByIdResponse;
import com.wanmi.sbc.goods.api.response.goods.*;
import com.wanmi.sbc.goods.api.response.info.GoodsInfoListByConditionResponse;
import com.wanmi.sbc.goods.api.response.storecate.StoreCateByStoreCateIdResponse;
import com.wanmi.sbc.goods.api.response.storecate.StoreCateListByGoodsResponse;
import com.wanmi.sbc.goods.bean.dto.GoodsInfoDTO;
import com.wanmi.sbc.goods.bean.enums.AddedFlag;
import com.wanmi.sbc.goods.bean.enums.GoodsType;
import com.wanmi.sbc.goods.bean.vo.*;
import com.wanmi.sbc.goods.service.GoodsExcelService;
import com.wanmi.sbc.redis.RedisService;
import com.wanmi.sbc.setting.api.provider.operatedatalog.OperateDataLogQueryProvider;
import com.wanmi.sbc.setting.api.provider.operatedatalog.OperateDataLogSaveProvider;
import com.wanmi.sbc.setting.api.request.operatedatalog.OperateDataLogAddRequest;
import com.wanmi.sbc.setting.api.request.operatedatalog.OperateDataLogDelByOperateIdRequest;
import com.wanmi.sbc.setting.api.request.operatedatalog.OperateDataLogListRequest;
import com.wanmi.sbc.setting.api.response.operatedatalog.OperateDataLogListResponse;
import com.wanmi.sbc.setting.bean.vo.OperateDataLogVO;
import com.wanmi.sbc.util.CommonUtil;
import com.wanmi.sbc.util.OperateLogMQUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @menu 商城配合知识顾问
 * @tag feature_d_cps3
 * @status undone
 */
@Api(tags = "GoodsController", description = "商品服务 Api")
@RestController
@RequestMapping("/goods")
public class GoodsController {

    /**
     * 默认全部支持的购买方式
     */
    private static final String GOODS_BUY_TYPES = "0,1";

    @Autowired
    private GoodsProvider goodsProvider;

    @Autowired
    private GoodsQueryProvider goodsQueryProvider;

    @Autowired
    private GoodsInfoQueryProvider goodsInfoQueryProvider;

    @Autowired
    private CompanyInfoQueryProvider companyInfoQueryProvider;

    @Autowired
    private GoodsExcelService goodsExcelService;

    @Autowired
    private StoreCateQueryProvider storeCateQueryProvider;

    @Autowired
    private EsGoodsInfoElasticProvider esGoodsInfoElasticProvider;

    @Autowired
    GoodsAresProvider goodsAresProvider;

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private OsUtil osUtil;

    @Autowired
    private FreightTemplateGoodsQueryProvider freightTemplateGoodsQueryProvider;

    @Autowired
    private OperateLogMQUtil operateLogMQUtil;

    @Autowired
    private OperateDataLogQueryProvider operateDataLogQueryProvider;

    @Autowired
    private OperateDataLogSaveProvider operateDataLogSaveProvider;

    @Autowired
    private GoodsCateQueryProvider goodsCateQueryProvider;

    @Autowired
    private GoodsBrandQueryProvider goodsBrandQueryProvider;

    @Autowired
    private AppointmentSaleQueryProvider appointmentSaleQueryProvider;

    @Autowired
    private BookingSaleQueryProvider bookingSaleQueryProvider;

    @Autowired
    private EsStandardProvider esStandardProvider;

    @Autowired
    private RedisService redisService;

    @Autowired
    private  CycleBuyQueryProvider cycleBuyQueryProvider;

    @Autowired
    private GoodsInfoSpecDetailRelQueryProvider goodsInfoSpecDetailRelQueryProvider;

    @Autowired
    private GuanyierpProvider guanyierpProvider;

    @Value("${default.providerId}")
    private Long defaultProviderId;

    /**
     * @description 新增商品
     * @menu 商城配合知识顾问
     * @tag feature_d_cps_v3
     * @status done
     */
    @ApiOperation(value = "新增商品")
    @RequestMapping(value = "/spu", method = RequestMethod.POST)
    public BaseResponse<String> add(@RequestBody @Valid GoodsAddRequest request) {
        request.setUpdatePerson(commonUtil.getOperatorId());
        request.getGoods().setProviderId(defaultProviderId);
        Long fId = request.getGoods().getFreightTempId();
        if ((request.getGoods() == null || CollectionUtils.isEmpty(request.getGoodsInfos()) || Objects.isNull(fId))
        &&( request.getGoods().getGoodsType() != GoodsType.CYCLE_BUY.toValue() && request.getGoods().getGoodsType() == GoodsType.REAL_GOODS.toValue() )) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        //查询ERP编码信息,校验sku填写的erp编码是否在查询的erp编码中
        List<GoodsInfoDTO> goodsInfoDTOS= request.getGoodsInfos();
        goodsInfoDTOS.forEach(goodsInfoDTO -> {
            if (StringUtils.isNotBlank(goodsInfoDTO.getErpGoodsInfoNo())) {
                List<ERPGoodsInfoVO> erpGoodsInfoVOList=guanyierpProvider.syncGoodsInfo(SynGoodsInfoRequest.builder().spuCode(goodsInfoDTO.getErpGoodsNo()).build()).getContext().getErpGoodsInfoVOList();
                if (CollectionUtils.isNotEmpty(erpGoodsInfoVOList)) {
                    List<String> skuCodes=erpGoodsInfoVOList.stream().map(erpGoodsInfoVO -> erpGoodsInfoVO.getSkuCode()).distinct().collect(Collectors.toList());
                    if (!skuCodes.contains(goodsInfoDTO.getErpGoodsInfoNo())) {
                        throw new SbcRuntimeException("K-800002");
                    }
                } else {
                    throw new SbcRuntimeException("K-800003");
                }
            }
        });

        // 添加默认值, 适应云掌柜新增商品没有设置购买方式, 导致前台不展示购买方式问题
        if (StringUtils.isBlank(request.getGoods().getGoodsBuyTypes())) {
            request.getGoods().setGoodsBuyTypes(GOODS_BUY_TYPES);
        }

        CompanyInfoVO companyInfo = companyInfoQueryProvider.getCompanyInfoById(
                CompanyInfoByIdRequest.builder().companyInfoId(commonUtil.getCompanyInfoId()).build()
        ).getContext();
        if (companyInfo == null) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }

        if (!Objects.isNull(fId)){
        //判断运费模板是否存在
        freightTemplateGoodsQueryProvider.existsById(
                FreightTemplateGoodsExistsByIdRequest.builder().freightTempId(fId).build());
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
        //ares埋点-商品-后台添加商品sku
        goodsAresProvider.dispatchFunction(new DispatcherFunctionRequest("addGoodsSpu", new String[]{goodsId}));
        esGoodsInfoElasticProvider.initEsGoodsInfo(EsGoodsInfoRequest.builder().goodsId(goodsId).build());

        operateLogMQUtil.convertAndSend("商品", "直接发布",
                "直接发布：SPU编码" + request.getGoods().getGoodsNo());
        return BaseResponse.success(goodsId);
    }


    /**
     * @description 同时新增商品基本和商品设价
     * @menu 商城配合知识顾问
     * @tag feature_d_cps_v3
     * @status done
     */
    @ApiOperation(value = "同时新增商品基本和商品设价")
    @RequestMapping(value = "/spu/price", method = RequestMethod.POST)
    public BaseResponse<String> spuDetail(@RequestBody @Valid GoodsAddAllRequest request) {
        request.setUpdatePerson(commonUtil.getOperatorId());
        request.getGoods().setProviderId(defaultProviderId);
        Long fId = request.getGoods().getFreightTempId();
        if ((request.getGoods() == null || CollectionUtils.isEmpty(request.getGoodsInfos()) || Objects.isNull(fId))
                &&( request.getGoods().getGoodsType() != GoodsType.CYCLE_BUY.toValue() && request.getGoods().getGoodsType() == GoodsType.REAL_GOODS.toValue() )) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        //查询ERP编码信息,校验sku填写的erp编码是否在查询的erp编码中
        List<GoodsInfoDTO> goodsInfoDTOS= request.getGoodsInfos();
        goodsInfoDTOS.forEach(goodsInfoDTO -> {
            if (StringUtils.isNotBlank(goodsInfoDTO.getErpGoodsInfoNo())) {
                List<ERPGoodsInfoVO> erpGoodsInfoVOList=guanyierpProvider.syncGoodsInfo(SynGoodsInfoRequest.builder().spuCode(goodsInfoDTO.getErpGoodsNo()).build()).getContext().getErpGoodsInfoVOList();
                if (CollectionUtils.isNotEmpty(erpGoodsInfoVOList)) {
                    List<String> skuCodes=erpGoodsInfoVOList.stream().map(erpGoodsInfoVO -> erpGoodsInfoVO.getSkuCode()).distinct().collect(Collectors.toList());
                    if (!skuCodes.contains(goodsInfoDTO.getErpGoodsInfoNo())) {
                        throw new SbcRuntimeException("K-800002");
                    }
                } else {
                    throw new SbcRuntimeException("K-800003");
                }
            }
        });



        // 添加默认值, 适应云掌柜新增商品没有设置购买方式, 导致前台不展示购买方式问题
        if (StringUtils.isBlank(request.getGoods().getGoodsBuyTypes())) {
            request.getGoods().setGoodsBuyTypes(GOODS_BUY_TYPES);
        }

        CompanyInfoVO companyInfo = companyInfoQueryProvider.getCompanyInfoById(
                CompanyInfoByIdRequest.builder().companyInfoId(commonUtil.getCompanyInfoId()).build()
        ).getContext();
        if (companyInfo == null) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        if (!Objects.isNull(fId)){
            //判断运费模板是否存在
            freightTemplateGoodsQueryProvider.existsById(
                    FreightTemplateGoodsExistsByIdRequest.builder().freightTempId(fId).build());
        }
        request.getGoods().setCompanyInfoId(commonUtil.getCompanyInfoId());
        request.getGoods().setCompanyType(companyInfo.getCompanyType());
        request.getGoods().setStoreId(commonUtil.getStoreId());
        request.getGoods().setSupplierName(companyInfo.getSupplierName());

        BaseResponse<GoodsAddAllResponse> baseResponse = goodsProvider.addAll(request);
        GoodsAddAllResponse response = baseResponse.getContext();
        String goodsId = Optional.ofNullable(response).map(GoodsAddAllResponse::getGoodsId).orElse(null);
        //ares埋点-商品-后台添加商品sku
        goodsAresProvider.dispatchFunction(new DispatcherFunctionRequest("addGoodsSpu", new String[]{goodsId}));
        esGoodsInfoElasticProvider.initEsGoodsInfo(EsGoodsInfoRequest.builder().goodsId(goodsId).build());

        //更新redis商品基本数据
        String goodsDetailInfo =
                redisService.getString(RedisKeyConstant.GOODS_DETAIL_CACHE + request.getGoods().getGoodsId());
        if (StringUtils.isNotBlank(goodsDetailInfo)) {
            redisService.delete(RedisKeyConstant.GOODS_DETAIL_CACHE + request.getGoods().getGoodsId());
        }


        operateLogMQUtil.convertAndSend("商品", "直接发布",
                "直接发布：SPU编码" + request.getGoods().getGoodsNo());
        return BaseResponse.success(goodsId);
    }


    /**
     * @description 编辑商品
     * @menu 商城配合知识顾问
     * @tag feature_d_cps_v3
     * @status done
     */
    @ApiOperation(value = "编辑商品")
    @RequestMapping(value = "/spu", method = RequestMethod.PUT)
    public BaseResponse edit(@RequestBody @Valid GoodsModifyRequest request) {
        request.setUpdatePerson(commonUtil.getOperatorId());
        request.getGoods().setProviderId(defaultProviderId);
        Long fId = request.getGoods().getFreightTempId();
        if ((request.getGoods() == null || CollectionUtils.isEmpty(request.getGoodsInfos()) || Objects.isNull(fId))
                &&( request.getGoods().getGoodsType() != GoodsType.CYCLE_BUY.toValue() && request.getGoods().getGoodsType() == GoodsType.REAL_GOODS.toValue() )) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }


        //查询ERP编码信息,校验sku填写的erp编码是否在查询的erp编码中
        List<GoodsInfoVO> goodsInfoVOS= request.getGoodsInfos();
        goodsInfoVOS.forEach(goodsInfoVO -> {
            if (StringUtils.isNotBlank(goodsInfoVO.getErpGoodsInfoNo())) {
                List<ERPGoodsInfoVO> erpGoodsInfoVOList=guanyierpProvider.syncGoodsInfo(SynGoodsInfoRequest.builder().spuCode(goodsInfoVO.getErpGoodsNo()).build()).getContext().getErpGoodsInfoVOList();
                if (CollectionUtils.isNotEmpty(erpGoodsInfoVOList)) {
                    List<String> skuCodes=erpGoodsInfoVOList.stream().map(erpGoodsInfoVO -> erpGoodsInfoVO.getSkuCode()).distinct().collect(Collectors.toList());
                    if (!skuCodes.contains(goodsInfoVO.getErpGoodsInfoNo())) {
                        throw new SbcRuntimeException("K-800002");
                    }
                } else {
                    throw new SbcRuntimeException("K-800003");
                }
            }
        });


        // 添加默认值, 适应云掌柜编辑商品没有设置购买方式, 导致前台不展示购买方式问题
        if (StringUtils.isBlank(request.getGoods().getGoodsBuyTypes())) {
            request.getGoods().setGoodsBuyTypes(GOODS_BUY_TYPES);
        }

        GoodsViewByIdRequest goodsViewByIdRequest = new GoodsViewByIdRequest();
        goodsViewByIdRequest.setGoodsId(request.getGoods().getGoodsId());
        GoodsViewByIdResponse oldData = goodsQueryProvider.getViewById(goodsViewByIdRequest).getContext();

        Integer saleType = request.getGoods().getSaleType();

        Integer oldSaleType = oldData.getGoods().getSaleType();

        // 参与预约活动的商品不可修改
        AppointmentSaleNotEndResponse appointmentSaleNotEndResponse =
                appointmentSaleQueryProvider.getNotEndActivity(AppointmentSaleByGoodsIdRequest.builder().goodsId(request.getGoods().getGoodsId()).build()).getContext();
        if (Objects.nonNull(appointmentSaleNotEndResponse) && !CollectionUtils.isEmpty(appointmentSaleNotEndResponse.getAppointmentSaleVOList()) && !saleType.equals(oldSaleType)) {
            throw new SbcRuntimeException("K-600010");
        }
        // 参与预售活动的商品不可修改
        BookingSaleNotEndResponse bookingSaleNotEndResponse =
                bookingSaleQueryProvider.getNotEndActivity(BookingSaleByGoodsIdRequest.builder().goodsId(request.getGoods().getGoodsId()).build()).getContext();
        if (Objects.nonNull(bookingSaleNotEndResponse) && !CollectionUtils.isEmpty(bookingSaleNotEndResponse.getBookingSaleVOList()) && !saleType.equals(oldSaleType)) {
            throw new SbcRuntimeException("K-600010");
        }


        if (!Objects.isNull(fId)){
            //判断运费模板是否存在
            freightTemplateGoodsQueryProvider.existsById(
                    FreightTemplateGoodsExistsByIdRequest.builder().freightTempId(fId).build());
        }
        //获取商品店铺分类
        if (osUtil.isS2b()) {
            StoreCateListByGoodsRequest storeCateListByGoodsRequest =
                    new StoreCateListByGoodsRequest(Collections.singletonList(request.getGoods().getGoodsId()));
            BaseResponse<StoreCateListByGoodsResponse> baseResponse =
                    storeCateQueryProvider.listByGoods(storeCateListByGoodsRequest);
            StoreCateListByGoodsResponse storeCateListByGoodsResponse = baseResponse.getContext();
            if (Objects.nonNull(storeCateListByGoodsResponse)) {
                List<StoreCateGoodsRelaVO> storeCateGoodsRelaVOList =
                        storeCateListByGoodsResponse.getStoreCateGoodsRelaVOList();
                oldData.getGoods().setStoreCateIds(storeCateGoodsRelaVOList.stream()
                        .filter(rela -> rela.getStoreCateId() != null)
                        .map(StoreCateGoodsRelaVO::getStoreCateId)
                        .collect(Collectors.toList()));
            }
        }

        GoodsModifyResponse response = goodsProvider.modify(request).getContext();
        Map<String, Object> returnMap = response.getReturnMap();
        if (CollectionUtils.isNotEmpty((List<String>) returnMap.get("delStoreGoodsInfoIds"))) {
            esGoodsInfoElasticProvider.delete(EsGoodsDeleteByIdsRequest.builder()
                    .deleteIds((List<String>) returnMap.get("delStoreGoodsInfoIds")).build());
        }
        esGoodsInfoElasticProvider.deleteByGoods(EsGoodsDeleteByIdsRequest.builder().
                deleteIds(Collections.singletonList(request.getGoods().getGoodsId())).build());
        esGoodsInfoElasticProvider.initEsGoodsInfo(EsGoodsInfoRequest.builder().goodsId(request.getGoods().getGoodsId()).build());

        //更新redis商品基本数据
        String goodsDetailInfo =
                redisService.getString(RedisKeyConstant.GOODS_DETAIL_CACHE + request.getGoods().getGoodsId());
        if (StringUtils.isNotBlank(goodsDetailInfo)) {
            redisService.delete(RedisKeyConstant.GOODS_DETAIL_CACHE + request.getGoods().getGoodsId());
        }

        //刷新商品库
        if(CollectionUtils.isNotEmpty(response.getStandardIds())){
            esStandardProvider.init(EsStandardInitRequest.builder().goodsIds(response.getStandardIds()).build());
        }

        //ares埋点-商品-后台修改商品sku,迁移至goods微服务下
        operateLogMQUtil.convertAndSend("商品", "编辑商品",
                "编辑商品：SPU编码" + request.getGoods().getGoodsNo());
//        if(isRemove){
//            throw new SbcRuntimeException("K-030507");
//        }
        /*if(StringUtils.isNotEmpty(request.getGoods().getProviderGoodsId())){
            //供商品商品编辑添加日志
            OperateDataLogAddRequest operateDataLogAddRequest = buildOperateDataLog(request , oldData);
            operateDataLogSaveProvider.add(operateDataLogAddRequest);
        }*/
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
//        operateBeforeData.append("SPU编码：").append(oldData.getGoods().getGoodsNo());
//        operateAfterData.append("SPU编码：").append(newData.getGoods().getGoodsNo());
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
        //划线价 非必填项
        if (oldData.getGoods().getLinePrice() != null) {
            operateBeforeData.append("划线价：").append(oldData.getGoods().getLinePrice());
        }
        if (newData.getGoods().getLinePrice() != null) {
            operateAfterData.append("划线价：").append(newData.getGoods().getLinePrice());
        }
        if (oldData.getGoods().getLinePrice() != null && newData.getGoods().getLinePrice() != null && !oldData.getGoods().getLinePrice().equals(newData.getGoods().getLinePrice())) {
            operateContent.append("修改划线价,");
        }

        //销售类型 必填项
        operateBeforeData.append("销售类型：").append(oldData.getGoods().getSaleType());
        operateAfterData.append("销售类型：").append(newData.getGoods().getSaleType());
        if (!oldData.getGoods().getSaleType().equals(newData.getGoods().getSaleType())) {
            operateContent.append("修改销售类型,");
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
            operateBeforeData.append("图片：").append(goodsInfoImg).append(proInfo + ":").append("供货价：").append(supplyPrice).append("库存：").append(stock).append("条形码：").append(barCode);
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
            operateAfterData.append("图片：").append(goodsInfoImg).append(proInfo + ":").append("供货价：").append(supplyPrice).append("库存：").append(stock).append("条形码：").append(barCode);

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
     * @description 保存商品价格
     * @menu 商城配合知识顾问
     * @tag feature_d_cps_v3
     * @status done
     */
    @ApiOperation(value = "保存商品价格")
    @RequestMapping(value = "/spu/price", method = RequestMethod.PUT)
    public BaseResponse editSpuPrice(@RequestBody @Valid GoodsModifyAllRequest request) {
        request.setUpdatePerson(commonUtil.getOperatorId());
        request.getGoods().setProviderId(defaultProviderId);
        Long fId = request.getGoods().getFreightTempId();
        if ((request.getGoods() == null || CollectionUtils.isEmpty(request.getGoodsInfos()) || Objects.isNull(fId))
                &&( request.getGoods().getGoodsType() != GoodsType.CYCLE_BUY.toValue() && request.getGoods().getGoodsType() == GoodsType.REAL_GOODS.toValue() )) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        // 添加默认值, 适应云掌柜新增商品没有设置购买方式, 导致前台不展示购买方式问题
        if (StringUtils.isBlank(request.getGoods().getGoodsBuyTypes())) {
            request.getGoods().setGoodsBuyTypes(GOODS_BUY_TYPES);
        }

        //判断运费模板是否存在
        if (!Objects.isNull(fId)){
            //判断运费模板是否存在
            freightTemplateGoodsQueryProvider.existsById(
                    FreightTemplateGoodsExistsByIdRequest.builder().freightTempId(fId).build());
        }

        goodsProvider.modifyAll(request);

        esGoodsInfoElasticProvider.deleteByGoods(EsGoodsDeleteByIdsRequest.builder().
                deleteIds(Collections.singletonList(request.getGoods().getGoodsId())).build());
        esGoodsInfoElasticProvider.initEsGoodsInfo(EsGoodsInfoRequest.builder().goodsId(request.getGoods().getGoodsId()).build());


        //更新redis商品基本数据
        String goodsDetailInfo =
                redisService.getString(RedisKeyConstant.GOODS_DETAIL_CACHE + request.getGoods().getGoodsId());
        if (StringUtils.isNotBlank(goodsDetailInfo)) {
            redisService.delete(RedisKeyConstant.GOODS_DETAIL_CACHE + request.getGoods().getGoodsId());
        }

        //ares埋点-商品-后台修改商品sku,迁移至goods微服务下
        operateLogMQUtil.convertAndSend("商品", "设价",
                "设价：SPU编码" + request.getGoods().getGoodsNo());
        return BaseResponse.SUCCESSFUL();
    }


    /**
     * @description 获取商品详情信息
     * @menu 商城配合知识顾问
     * @tag feature_d_cps_v3
     * @status done
     */
    @ApiOperation(value = "获取商品详情信息")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "goodsId", value = "商品Id", required = true)
    @RequestMapping(value = "/spu/{goodsId}", method = RequestMethod.GET)
    public BaseResponse<GoodsViewByIdResponse> info(@PathVariable String goodsId) {
        GoodsViewByIdRequest request = new GoodsViewByIdRequest();
        request.setGoodsId(goodsId);
        request.setShowLabelFlag(true);
        GoodsViewByIdResponse response = goodsQueryProvider.getViewById(request).getContext();

        //周期购商品 封装周期购信息
        if (Objects.equals(response.getGoods().getGoodsType(),3)) {
            //查询周期购活动信息
            CycleBuyVO cycleBuyVO= cycleBuyQueryProvider.getByGoodsDetailsId(CycleBuyByGoodsIdRequest.builder().goodsId(goodsId).build()).getContext().getCycleBuyVO();

            if (CollectionUtils.isNotEmpty(cycleBuyVO.getCycleBuyGiftVOList())) {

                    List<String> goodsInfoIds = cycleBuyVO.getCycleBuyGiftVOList().stream().map(CycleBuyGiftVO::getGoodsInfoId).collect(Collectors.toList());

                    //查询赠品的sku信息
                    List<GoodsInfoVO> goodsInfos = goodsInfoQueryProvider.listByCondition(GoodsInfoListByConditionRequest.builder().
                            goodsInfoIds(goodsInfoIds).build()).getContext().getGoodsInfos();

                    //查询赠品的品牌信息
                    Map<Long, GoodsBrandVO> goodsBrandVOMap = goodsBrandQueryProvider.listByIds(GoodsBrandByIdsRequest.builder().brandIds(goodsInfos.stream().
                            map(GoodsInfoVO::getBrandId).collect(Collectors.toList())).build()).getContext().getGoodsBrandVOList()
                            .stream().collect(Collectors.toMap(GoodsBrandVO::getBrandId, m -> m));


                    //查询赠品的分类信息
                    Map<Long, GoodsCateVO> goodsCateVOMap = goodsCateQueryProvider.getByIds(new GoodsCateByIdsRequest(goodsInfos.stream().map(GoodsInfoVO::getCateId).
                            collect(Collectors.toList()))).getContext().getGoodsCateVOList().stream().collect(Collectors.toMap(GoodsCateVO::getCateId, m -> m));

                    Map<String, String> goodsInfoSpecDetailMap = new HashMap<>();

                    goodsInfoSpecDetailMap.putAll(goodsInfoSpecDetailRelQueryProvider.listBySkuIds(new GoodsInfoSpecDetailRelBySkuIdsRequest(goodsInfoIds))
                            .getContext().getGoodsInfoSpecDetailRelVOList().stream()
                            .filter(v -> StringUtils.isNotBlank(v.getDetailName()))
                            .collect(Collectors.toMap(GoodsInfoSpecDetailRelVO::getGoodsInfoId, GoodsInfoSpecDetailRelVO::getDetailName, (a, b) -> a.concat(" ").concat(b))));

                    if (CollectionUtils.isNotEmpty(goodsInfos)) {
                        cycleBuyVO.getCycleBuyGiftVOList().forEach(cycleBuyGiftVO -> {
                            goodsInfos.forEach(goodsInfoVO -> {
                                if (Objects.equals(cycleBuyGiftVO.getGoodsInfoId(), goodsInfoVO.getGoodsInfoId())) {
                                    if (Objects.nonNull(goodsInfoVO.getBrandId()) && goodsBrandVOMap.containsKey(goodsInfoVO.getBrandId())) {
                                        goodsInfoVO.setBrandName(goodsBrandVOMap.get(goodsInfoVO.getBrandId()).getBrandName());
                                    }
                                    if (goodsCateVOMap.containsKey(goodsInfoVO.getCateId())) {
                                        goodsInfoVO.setCateName(goodsCateVOMap.get(goodsInfoVO.getCateId()).getCateName());
                                    }
                                    //填充规格值
                                    goodsInfoVO.setSpecText(goodsInfoSpecDetailMap.get(goodsInfoVO.getGoodsInfoId()));
                                    cycleBuyGiftVO.setGoodsInfoVO(goodsInfoVO);
                                }
                            });
                        });
                    }
                    //设置周期购活动信息
                    response.setCycleBuyVO(cycleBuyVO);
            }
        }
        //获取商品店铺分类
        if (osUtil.isS2b()) {
            StoreCateListByGoodsRequest storeCateListByGoodsRequest =
                    new StoreCateListByGoodsRequest(Collections.singletonList(goodsId));
            BaseResponse<StoreCateListByGoodsResponse> baseResponse =
                    storeCateQueryProvider.listByGoods(storeCateListByGoodsRequest);
            StoreCateListByGoodsResponse storeCateListByGoodsResponse = baseResponse.getContext();
            if (Objects.nonNull(storeCateListByGoodsResponse)) {
                List<StoreCateGoodsRelaVO> storeCateGoodsRelaVOList =
                        storeCateListByGoodsResponse.getStoreCateGoodsRelaVOList();
                response.getGoods().setStoreCateIds(storeCateGoodsRelaVOList.stream()
                        .filter(rela -> rela.getStoreCateId() != null)
                        .map(StoreCateGoodsRelaVO::getStoreCateId)
                        .collect(Collectors.toList()));
            }
        }
        OperateDataLogListResponse operateDataLogListResponse =
                operateDataLogQueryProvider.list(OperateDataLogListRequest.builder().operateId(goodsId).delFlag(DeleteFlag.NO).build()).getContext();
        List<OperateDataLogVO> operateDataLogList = new ArrayList<OperateDataLogVO>();
        if (operateDataLogListResponse != null && CollectionUtils.isNotEmpty(operateDataLogListResponse.getOperateDataLogVOList())) {
            operateDataLogList = operateDataLogListResponse.getOperateDataLogVOList();
        }
        response.setOperateDataLogVOList(operateDataLogList);
        return BaseResponse.success(response);
    }

    /**
     * 清空日志
     */
    @ApiOperation(value = "清空日志")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "goodsId", value = "商品Id", required = true)
    @RequestMapping(value = "/log/delete/{goodsId}", method = RequestMethod.GET)
    public BaseResponse logDeleteByGoodsId(@PathVariable String goodsId) {
        operateDataLogSaveProvider.deleteByOperateId(OperateDataLogDelByOperateIdRequest.builder().operateId(goodsId).build());
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 删除商品
     */
    @ApiOperation(value = "删除商品")
    @RequestMapping(value = "/spu", method = RequestMethod.DELETE)
    public BaseResponse delete(@RequestBody GoodsDeleteByIdsRequest request) {
        request.setStoreId(commonUtil.getStoreId());
        request.setUpdatePerson(commonUtil.getOperatorId());
        if (1 == request.getGoodsIds().size()) {
            GoodsByIdRequest goodsByIdRequest = new GoodsByIdRequest();
            goodsByIdRequest.setGoodsId(request.getGoodsIds().get(0));
            GoodsByIdResponse response = goodsQueryProvider.getById(goodsByIdRequest).getContext();
            operateLogMQUtil.convertAndSend("商品", "删除商品",
                    "删除商品：SPU编码" + response.getGoodsNo());
        } else {
            operateLogMQUtil.convertAndSend("商品", "批量删除",
                    "批量删除");
        }

        if (CollectionUtils.isEmpty(request.getGoodsIds())) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        goodsProvider.deleteByIds(request);
        //关联供应商商品下架
        GoodsListByIdsResponse goodsListByIdsResponse =
                goodsQueryProvider.listByProviderGoodsId(GoodsListByIdsRequest.builder().goodsIds(request.getGoodsIds()).build()).getContext();
        if (goodsListByIdsResponse != null && CollectionUtils.isNotEmpty(goodsListByIdsResponse.getGoodsVOList())) {
            List<String> providerOfGoodsIds =
                    goodsListByIdsResponse.getGoodsVOList().stream().map(GoodsVO::getGoodsId).collect(Collectors.toList());
            GoodsInfoListByConditionResponse goodsInfoListByConditionResponse =
                    goodsInfoQueryProvider.listByCondition(GoodsInfoListByConditionRequest.builder().goodsIds(providerOfGoodsIds).build()).getContext();
            if (goodsInfoListByConditionResponse != null && CollectionUtils.isNotEmpty(goodsInfoListByConditionResponse.getGoodsInfos())) {
                List<String> providerOfGoodInfoIds =
                        goodsInfoListByConditionResponse.getGoodsInfos().stream().map(GoodsInfoVO::getGoodsInfoId).collect(Collectors.toList());
                //更新上下架状态
                esGoodsInfoElasticProvider.updateAddedStatus(EsGoodsInfoModifyAddedStatusRequest.builder().
                        addedFlag(AddedFlag.NO.toValue()).goodsIds(providerOfGoodsIds).
                        goodsInfoIds(providerOfGoodInfoIds).build()
                );
            }
        }
        //更新ES
        esGoodsInfoElasticProvider.deleteByGoods(EsGoodsDeleteByIdsRequest.builder().deleteIds(request.getGoodsIds()).build());

        request.getGoodsIds().forEach(goodsId -> {
            //更新redis商品基本数据
            String goodsDetailInfo = redisService.getString(RedisKeyConstant.GOODS_DETAIL_CACHE + goodsId);
            if (StringUtils.isNotBlank(goodsDetailInfo)) {
                redisService.delete(RedisKeyConstant.GOODS_DETAIL_CACHE + goodsId);
            }
        });


        return BaseResponse.SUCCESSFUL();
    }


    /**
     * 供应商删除商品
     */
    @ApiOperation(value = "删除商品")
    @RequestMapping(value = "/spu/provider", method = RequestMethod.DELETE)
    public BaseResponse deleteProvider(@RequestBody GoodsDeleteByIdsRequest request) {

        if (1 == request.getGoodsIds().size()) {
            GoodsByIdRequest goodsByIdRequest = new GoodsByIdRequest();
            goodsByIdRequest.setGoodsId(request.getGoodsIds().get(0));
            GoodsByIdResponse response = goodsQueryProvider.getById(goodsByIdRequest).getContext();
            operateLogMQUtil.convertAndSend("商品", "删除商品",
                    "删除商品：SPU编码" + response.getGoodsNo());
        } else {
            operateLogMQUtil.convertAndSend("商品", "批量删除",
                    "批量删除");
        }

        if (CollectionUtils.isEmpty(request.getGoodsIds())) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        goodsProvider.deleteProviderGoodsByIds(request);
        //关联供应商商品下架
        GoodsListByIdsResponse goodsListByIdsResponse =
                goodsQueryProvider.listByProviderGoodsId(GoodsListByIdsRequest.builder().goodsIds(request.getGoodsIds()).build()).getContext();
        if (goodsListByIdsResponse != null && CollectionUtils.isNotEmpty(goodsListByIdsResponse.getGoodsVOList())) {
            List<String> providerOfGoodsIds =
                    goodsListByIdsResponse.getGoodsVOList().stream().map(GoodsVO::getGoodsId).collect(Collectors.toList());
            GoodsInfoListByConditionResponse goodsInfoListByConditionResponse =
                    goodsInfoQueryProvider.listByCondition(GoodsInfoListByConditionRequest.builder().goodsIds(providerOfGoodsIds).build()).getContext();
            if (goodsInfoListByConditionResponse != null && CollectionUtils.isNotEmpty(goodsInfoListByConditionResponse.getGoodsInfos())) {
                List<String> providerOfGoodInfoIds =
                        goodsInfoListByConditionResponse.getGoodsInfos().stream().map(GoodsInfoVO::getGoodsInfoId).collect(Collectors.toList());
                //更新上下架状态
                esGoodsInfoElasticProvider.updateAddedStatus(EsGoodsInfoModifyAddedStatusRequest.builder().
                        addedFlag(AddedFlag.NO.toValue()).goodsIds(providerOfGoodsIds).
                        goodsInfoIds(providerOfGoodInfoIds).build()
                );
            }
        }
        //更新ES
        esGoodsInfoElasticProvider.deleteByGoods(EsGoodsDeleteByIdsRequest.builder().deleteIds(request.getGoodsIds()).build());
        //更新关联的商家商品es
        esGoodsInfoElasticProvider.initProviderEsGoodsInfo(EsGoodsInitProviderGoodsInfoRequest.builder().
                storeId(null).providerGoodsIds(request.getGoodsIds()).build()
        );
        request.getGoodsIds().forEach(goodsId -> {
            //更新redis商品基本数据
            String goodsDetailInfo = redisService.getString(RedisKeyConstant.GOODS_DETAIL_CACHE + goodsId);
            if (StringUtils.isNotBlank(goodsDetailInfo)) {
                redisService.delete(RedisKeyConstant.GOODS_DETAIL_CACHE + goodsId);
            }
        });
        return BaseResponse.SUCCESSFUL();
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
        GoodsByConditionRequest conditionRequest =
                GoodsByConditionRequest.builder().goodsIds(request.getGoodsIds()).build();
        List<GoodsVO> goodsVOList =
                goodsQueryProvider.listByCondition(conditionRequest).getContext().getGoodsVOList();
            for (GoodsVO goodsVO : goodsVOList) {
                if (StringUtils.isBlank(goodsVO.getErpGoodsNo())) {
                    throw new SbcRuntimeException("K-900001");
                }
            }

        goodsProvider.modifyAddedStatus(request);
        //更新ES
        esGoodsInfoElasticProvider.updateAddedStatus(EsGoodsInfoModifyAddedStatusRequest.builder().
                addedFlag(AddedFlag.YES.toValue()).goodsIds(request.getGoodsIds()).goodsInfoIds(null).build());

        //ares埋点-商品-后台批量修改商品spu的所有sku上下架状态
        goodsAresProvider.dispatchFunction(new DispatcherFunctionRequest("editGoodsSpuUp",
                new Object[]{AddedFlag.YES.toValue(), request.getGoodsIds()}));

        if (1 == request.getGoodsIds().size()) {
            GoodsByIdRequest goodsByIdRequest = new GoodsByIdRequest();
            goodsByIdRequest.setGoodsId(request.getGoodsIds().get(0));
            GoodsByIdResponse response = goodsQueryProvider.getById(goodsByIdRequest).getContext();
            operateLogMQUtil.convertAndSend("商品", "上架",
                    "上架：SPU编码" + response.getGoodsNo());
        } else {
            operateLogMQUtil.convertAndSend("商品", "批量上架", "批量上架");
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
        //如果下架商品是供应商商品，商家商品同步下架
        GoodsListByIdsRequest goodsListByIdsRequest = new GoodsListByIdsRequest();
        goodsListByIdsRequest.setGoodsIds(request.getGoodsIds());
        List<GoodsVO> goodsVOList = goodsQueryProvider.listByCondition(
                GoodsByConditionRequest.builder().providerGoodsIds(goodsIds).build()).getContext().getGoodsVOList();
        if (CollectionUtils.isNotEmpty(goodsVOList)) {
            goodsVOList.forEach(s -> {
                goodsIds.add(s.getGoodsId());
            });
        }
        request.setGoodsIds(goodsIds);

        goodsProvider.modifyAddedStatus(request);
        //更新ES
        esGoodsInfoElasticProvider.updateAddedStatus(EsGoodsInfoModifyAddedStatusRequest.builder().
                addedFlag(AddedFlag.NO.toValue()).goodsIds(request.getGoodsIds()).goodsInfoIds(null).build());

        //更新redis商品基本数据
        String goodsDetailInfo = redisService.getString(RedisKeyConstant.GOODS_DETAIL_CACHE + request.getGoodsIds());
        if (StringUtils.isNotBlank(goodsDetailInfo)) {
            redisService.delete(RedisKeyConstant.GOODS_DETAIL_CACHE + request.getGoodsIds());
        }


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

    /**
     * 批量编辑运费模板
     */
    @ApiOperation(value = "批量编辑运费模板")
    @RequestMapping(value = "/spu/freight", method = RequestMethod.PUT)
    public BaseResponse setFeight(@RequestBody GoodsModifyFreightTempRequest request) {
        if (CollectionUtils.isEmpty(request.getGoodsIds())) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        if (Objects.isNull(request.getFreightTempId())) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        Long fId = request.getFreightTempId();
        //判断运费模板是否存在
        freightTemplateGoodsQueryProvider.existsById(
                FreightTemplateGoodsExistsByIdRequest.builder().freightTempId(fId).build());
        goodsProvider.modifyFreightTemp(request);

        FreightTemplateGoodsByIdResponse templateGoods = freightTemplateGoodsQueryProvider.getById(
                FreightTemplateGoodsByIdRequest.builder().freightTempId(fId).build()).getContext();
        if (1 == request.getGoodsIds().size()) {
            GoodsByIdRequest goodsByIdRequest = new GoodsByIdRequest();
            goodsByIdRequest.setGoodsId(request.getGoodsIds().get(0));
            GoodsByIdResponse response = goodsQueryProvider.getById(goodsByIdRequest).getContext();
            operateLogMQUtil.convertAndSend("商品", "更换运费模板",
                    "更换运费模板：" + response.getGoodsName()
                            + " 模板名称改为" + templateGoods.getFreightTempName());
        } else {
            operateLogMQUtil.convertAndSend("商品", "批量更换运费模板",
                    "批量更换运费模板：模板名称改为" + templateGoods.getFreightTempName());
        }

        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 修改注水销量
     */
    @ApiOperation(value = "修改注水销量")
    @RequestMapping(value = "/spu/sham", method = RequestMethod.PUT)
    public BaseResponse modifyShamSalesNum(@Valid @RequestBody GoodsModifyShamSalesNumRequest request) {
        GoodsByIdRequest goodsByIdRequest = new GoodsByIdRequest();
        goodsByIdRequest.setGoodsId(request.getGoodsId());
        GoodsByIdResponse response = goodsQueryProvider.getById(goodsByIdRequest).getContext();
        if (response == null || StringUtils.isEmpty(response.getGoodsId())) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        goodsProvider.modifyShamSalesNum(request);

        Long newSalesNum =
                (Objects.isNull(response.getGoodsSalesNum()) ? 0 : response.getGoodsSalesNum()) + request.getShamSalesNum();
        //更新ES
        esGoodsInfoElasticProvider.updateSalesNumBySpuId(EsGoodsModifySalesNumBySpuIdRequest.builder().
                spuId(request.getGoodsId()).salesNum(newSalesNum).build()
        );
        operateLogMQUtil.convertAndSend("商品", "修改注水销量",
                "修改注水销量：SPU编码" + response.getGoodsNo());
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 修改排序号
     */
    @ApiOperation(value = "修改排序号")
    @RequestMapping(value = "/spu/sort", method = RequestMethod.PUT)
    public BaseResponse modifySortNo(@Valid @RequestBody GoodsModifySortNoRequest request) {
        GoodsByIdRequest goodsByIdRequest = new GoodsByIdRequest();
        goodsByIdRequest.setGoodsId(request.getGoodsId());
        GoodsByIdResponse response = goodsQueryProvider.getById(goodsByIdRequest).getContext();
        if (response == null || StringUtils.isEmpty(response.getGoodsId())) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        goodsProvider.modifySortNo(request);
        esGoodsInfoElasticProvider.updateSortNoBySpuId(EsGoodsModifySortNoBySpuIdRequest.builder().
                spuId(request.getGoodsId()).sortNo(request.getSortNo()).build()
        );
        operateLogMQUtil.convertAndSend("商品", "修改排序号",
                "修改排序号：SPU编码" + response.getGoodsNo());
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 上传文件
     */
    @ApiOperation(value = "上传文件")
    @RequestMapping(value = "/excel/upload", method = RequestMethod.POST)
    public BaseResponse<String> upload(@RequestParam("uploadFile") MultipartFile uploadFile) {
        return BaseResponse.success(goodsExcelService.upload(uploadFile, commonUtil.getOperatorId()));
    }

    /**
     * 下载错误文档
     */
    @ApiOperation(value = "下载错误文档")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", dataType = "String",
                    name = "ext", value = "后缀", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "String",
                    name = "decrypted", value = "解密", required = true)
    })

    @RequestMapping(value = "/excel/err/{ext}/{decrypted}", method = RequestMethod.GET)
    public void downErrExcel(@PathVariable String ext, @PathVariable String decrypted) {
        if (!("xls".equalsIgnoreCase(ext) || "xlsx".equalsIgnoreCase(ext))) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }

        goodsExcelService.downErrExcel(commonUtil.getOperatorId(), ext);
    }

}