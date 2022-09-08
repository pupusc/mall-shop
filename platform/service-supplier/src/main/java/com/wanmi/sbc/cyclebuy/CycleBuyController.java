package com.wanmi.sbc.cyclebuy;


import com.google.common.collect.Lists;
import com.sbc.wanmi.erp.bean.vo.MetaStockInfoVO;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.constant.RedisKeyConstant;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.common.util.OsUtil;
import com.wanmi.sbc.elastic.api.provider.goods.EsGoodsInfoElasticProvider;
import com.wanmi.sbc.elastic.api.provider.spu.EsSpuQueryProvider;
import com.wanmi.sbc.elastic.api.provider.standard.EsStandardProvider;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsDeleteByIdsRequest;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsInfoModifyAddedStatusRequest;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsInfoRequest;
import com.wanmi.sbc.elastic.api.request.standard.EsStandardInitRequest;
import com.wanmi.sbc.erp.api.provider.GuanyierpProvider;
import com.wanmi.sbc.erp.api.provider.ShopCenterProvider;
import com.wanmi.sbc.erp.api.request.NewGoodsInfoRequest;
import com.wanmi.sbc.erp.api.request.SynGoodsInfoRequest;
import com.wanmi.sbc.erp.api.response.SyncGoodsInfoResponse;
import com.wanmi.sbc.goods.GoodsController;
import com.wanmi.sbc.goods.api.provider.appointmentsale.AppointmentSaleQueryProvider;
import com.wanmi.sbc.goods.api.provider.ares.GoodsAresProvider;
import com.wanmi.sbc.goods.api.provider.bookingsale.BookingSaleQueryProvider;
import com.wanmi.sbc.goods.api.provider.brand.GoodsBrandQueryProvider;
import com.wanmi.sbc.goods.api.provider.cate.GoodsCateQueryProvider;
import com.wanmi.sbc.goods.api.provider.classify.ClassifyProvider;
import com.wanmi.sbc.goods.api.provider.cyclebuy.CycleBuyQueryProvider;
import com.wanmi.sbc.goods.api.provider.cyclebuy.CycleBuySaveProvider;
import com.wanmi.sbc.goods.api.provider.freight.FreightTemplateGoodsQueryProvider;
import com.wanmi.sbc.goods.api.provider.goods.GoodsProvider;
import com.wanmi.sbc.goods.api.provider.goods.GoodsQueryProvider;
import com.wanmi.sbc.goods.api.provider.info.GoodsInfoQueryProvider;
import com.wanmi.sbc.goods.api.provider.spec.GoodsInfoSpecDetailRelQueryProvider;
import com.wanmi.sbc.goods.api.provider.storecate.StoreCateQueryProvider;
import com.wanmi.sbc.goods.api.request.ares.DispatcherFunctionRequest;
import com.wanmi.sbc.goods.api.request.brand.GoodsBrandByIdsRequest;
import com.wanmi.sbc.goods.api.request.cate.GoodsCateByIdsRequest;
import com.wanmi.sbc.goods.api.request.cyclebuy.CycleBuyAddRequest;
import com.wanmi.sbc.goods.api.request.cyclebuy.CycleBuyByIdRequest;
import com.wanmi.sbc.goods.api.request.cyclebuy.CycleBuyDelByIdRequest;
import com.wanmi.sbc.goods.api.request.cyclebuy.CycleBuyModifyRequest;
import com.wanmi.sbc.goods.api.request.cyclebuy.CycleBuyPageRequest;
import com.wanmi.sbc.goods.api.request.cyclebuy.CycleBuySaleRequest;
import com.wanmi.sbc.goods.api.request.freight.FreightTemplateGoodsExistsByIdRequest;
import com.wanmi.sbc.goods.api.request.goods.GoodsByConditionRequest;
import com.wanmi.sbc.goods.api.request.goods.GoodsByIdRequest;
import com.wanmi.sbc.goods.api.request.goods.GoodsDeleteByIdsRequest;
import com.wanmi.sbc.goods.api.request.goods.GoodsListByIdsRequest;
import com.wanmi.sbc.goods.api.request.goods.GoodsModifyAddedStatusRequest;
import com.wanmi.sbc.goods.api.request.goods.GoodsModifyRequest;
import com.wanmi.sbc.goods.api.request.goods.GoodsViewByIdRequest;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoListByConditionRequest;
import com.wanmi.sbc.goods.api.request.spec.GoodsInfoSpecDetailRelBySkuIdsRequest;
import com.wanmi.sbc.goods.api.response.cyclebuy.CycleBuyAddResponse;
import com.wanmi.sbc.goods.api.response.cyclebuy.CycleBuyByIdResponse;
import com.wanmi.sbc.goods.api.response.cyclebuy.CycleBuyModifyResponse;
import com.wanmi.sbc.goods.api.response.cyclebuy.CycleBuyPageResponse;
import com.wanmi.sbc.goods.api.response.goods.GoodsByIdResponse;
import com.wanmi.sbc.goods.api.response.goods.GoodsListByIdsResponse;
import com.wanmi.sbc.goods.api.response.goods.GoodsModifyResponse;
import com.wanmi.sbc.goods.api.response.goods.GoodsViewByIdResponse;
import com.wanmi.sbc.goods.api.response.info.GoodsInfoListByConditionResponse;
import com.wanmi.sbc.goods.bean.enums.AddedFlag;
import com.wanmi.sbc.goods.bean.enums.EnterpriseAuditState;
import com.wanmi.sbc.goods.bean.enums.GoodsType;
import com.wanmi.sbc.goods.bean.enums.SaleType;
import com.wanmi.sbc.goods.bean.vo.CycleBuyGiftVO;
import com.wanmi.sbc.goods.bean.vo.CycleBuyVO;
import com.wanmi.sbc.goods.bean.vo.GoodsBrandVO;
import com.wanmi.sbc.goods.bean.vo.GoodsCateVO;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoSpecDetailRelVO;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.goods.bean.vo.GoodsSpecDetailVO;
import com.wanmi.sbc.goods.bean.vo.GoodsSpecVO;
import com.wanmi.sbc.goods.bean.vo.GoodsVO;
import com.wanmi.sbc.redis.RedisService;
import com.wanmi.sbc.util.CommonUtil;
import com.wanmi.sbc.util.OperateLogMQUtil;
import io.seata.spring.annotation.GlobalTransactional;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>周期购活动服务</p>
 *
 * @author weiwenhao
 * @date 2021-01-21 09:15:37
 */
@Api(tags = "CycleBuyController", description = "周期购活动服务 API")
@RestController
@RequestMapping("/marketing/cyclebuy")
public class CycleBuyController {


    @Autowired
    private CycleBuyQueryProvider cycleBuyQueryProvider;


    @Autowired
    private CycleBuySaveProvider cycleBuySaveProvider;


    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private EsSpuQueryProvider esSpuQueryProvider;


    @Autowired
    private EsGoodsInfoElasticProvider esGoodsInfoElasticProvider;

    @Autowired
    private GoodsAresProvider goodsAresProvider;

    @Autowired
    private GoodsController goodsController;

    @Autowired
    private GoodsQueryProvider goodsQueryProvider;

    @Autowired
    private GoodsInfoQueryProvider goodsInfoQueryProvider;

    @Autowired
    private GoodsBrandQueryProvider goodsBrandQueryProvider;

    @Autowired
    private GoodsCateQueryProvider goodsCateQueryProvider;

    @Autowired
    private GoodsInfoSpecDetailRelQueryProvider goodsInfoSpecDetailRelQueryProvider;

    @Autowired
    private StoreCateQueryProvider storeCateQueryProvider;


    @Autowired
    private OperateLogMQUtil operateLogMQUtil;

    @Autowired
    private GoodsProvider goodsProvider;

    @Autowired
    private RedisService redisService;

    @Autowired
    private GuanyierpProvider guanyierpProvider;
    @Autowired
    private ShopCenterProvider shopCenterProvider;

    @Autowired
    private OsUtil osUtil;

    @Autowired
    private AppointmentSaleQueryProvider appointmentSaleQueryProvider;

    @Autowired
    private BookingSaleQueryProvider bookingSaleQueryProvider;

    @Autowired
    private FreightTemplateGoodsQueryProvider freightTemplateGoodsQueryProvider;

    @Autowired
    private EsStandardProvider esStandardProvider;

    @Autowired
    private ClassifyProvider classifyProvider;


    /**
     * 新增周期购活动信息
     *
     * @return
     */
    @ApiOperation(value = "新增周期购活动信息")
    @PostMapping("/add")
    @GlobalTransactional
    public BaseResponse<CycleBuyAddResponse> add(@RequestBody @Valid CycleBuyAddRequest cycleBuyAddRequest) {

        cycleBuyAddRequest.setStoreId(commonUtil.getStoreId());
        cycleBuyAddRequest.setCreatePerson(commonUtil.getOperatorId());
        //新增周期购活动信息
        cycleBuyAddRequest.setSendDateRules(cycleBuyAddRequest.getDateRule());
        cycleBuyAddRequest.setSendDateRule(null);

        //判断sku上面填写的sku的erp是否在填写的spu编码之内
        List<MetaStockInfoVO> goodsInfoList = shopCenterProvider.searchGoodsInfo(NewGoodsInfoRequest.builder().metaGoodsCode(cycleBuyAddRequest.getErpGoodsNo()).build()).getContext().getGoodsInfoList();

        if (CollectionUtils.isNotEmpty(goodsInfoList)) {
            cycleBuyAddRequest.getGoodsInfoDTOS().forEach(goodsInfoDTO -> {
                List<String> skuCodes = goodsInfoList.stream().map(infoVO -> infoVO.getSkuCode()).distinct().collect(Collectors.toList());
                if (!skuCodes.contains(goodsInfoDTO.getErpGoodsInfoNo())) {
                    throw new SbcRuntimeException("K-800002");
                }

            });
        } else {
            throw new SbcRuntimeException("K-800003");
        }

        CycleBuyVO cycleBuyVO = cycleBuySaveProvider.add(cycleBuyAddRequest).getContext().getCycleBuyVO();

        //ares埋点-商品-后台添加商品sku
        goodsAresProvider.dispatchFunction(new DispatcherFunctionRequest("addGoodsSpu", new String[]{cycleBuyVO.getGoodsId()}));

        //同步ES
        esGoodsInfoElasticProvider.initEsGoodsInfo(EsGoodsInfoRequest.builder().goodsId(cycleBuyVO.getGoodsId()).build());

        operateLogMQUtil.convertAndSend("周期购", "创建周期活动",
                "创建周期活动：SPU编码" + cycleBuyAddRequest.getGoodsNo());

        return BaseResponse.success(CycleBuyAddResponse.builder().cycleBuyVO(cycleBuyVO).build());
    }


    /**
     * 新增周期购活动信息查询ERP商品信息接口
     * @return
     */
    @ApiOperation(value = "新增周期购活动信息查询ERP商品信息接口")
    @PostMapping("/sync-goods-info")
    BaseResponse<SyncGoodsInfoResponse> syncGoodsInfo(@RequestBody @Valid SynGoodsInfoRequest erpSynGoodsStockRequest){
        return guanyierpProvider.syncGoodsInfo(erpSynGoodsStockRequest);
    }


    /**
     * 编辑周期购商品
     *
     * @param cycleBuyModifyRequest
     * @return
     */
    @ApiOperation(value = "编辑周期购商品")
    @PutMapping("/modify")
    @GlobalTransactional
    public BaseResponse<CycleBuyModifyResponse> modify(@RequestBody @Valid CycleBuyModifyRequest cycleBuyModifyRequest) {

        cycleBuyModifyRequest.setUpdatePerson(commonUtil.getOperatorId());

        CycleBuyVO cycleBuyVO = cycleBuyQueryProvider.getById(CycleBuyByIdRequest.builder().id(cycleBuyModifyRequest.getId()).build()).getContext().getCycleBuyVO();

        //判断sku上面填写的sku的erp是否在填写的spu编码之内
        List<MetaStockInfoVO> goodsInfoList = shopCenterProvider.searchGoodsInfo(NewGoodsInfoRequest.builder().metaGoodsCode(cycleBuyModifyRequest.getErpGoodsNo()).build()).getContext().getGoodsInfoList();

        if (CollectionUtils.isNotEmpty(goodsInfoList)) {
            cycleBuyModifyRequest.getGoodsInfoDTOS().forEach(goodsInfoDTO -> {
                List<String> skuCodes = goodsInfoList.stream().map(infoVO -> infoVO.getSkuCode()).distinct().collect(Collectors.toList());
                if (!skuCodes.contains(goodsInfoDTO.getErpGoodsInfoNo())) {
                    throw new SbcRuntimeException("K-800002");
                }
            });
        } else {
            throw new SbcRuntimeException("K-800003");
        }


        GoodsViewByIdRequest goodsViewByIdRequest = new GoodsViewByIdRequest();
        goodsViewByIdRequest.setGoodsId(cycleBuyVO.getGoodsId());
        GoodsViewByIdResponse oldData = goodsQueryProvider.getViewById(goodsViewByIdRequest).getContext();

        Long fId = oldData.getGoods().getFreightTempId();


        GoodsModifyRequest goodsModifyRequest = new GoodsModifyRequest();
        //sku列表
        List<GoodsInfoVO> goodsInfos = KsBeanUtil.convert(cycleBuyModifyRequest.getGoodsInfoDTOS(), GoodsInfoVO.class);
        goodsInfos.forEach(goodsInfoVO -> {
            goodsInfoVO.setGoodsType(GoodsType.CYCLE_BUY.toValue());
            //周期购商品必须是零售
            goodsInfoVO.setSaleType(SaleType.RETAIL.toValue());
            //如果选择的是企业购商品，需要设置成不是企业购的商品
            goodsInfoVO.setEnterPriseAuditState(EnterpriseAuditState.INIT);

            //设置spu(erp)编码
            goodsInfoVO.setErpGoodsNo(cycleBuyModifyRequest.getErpGoodsNo());

            //设置是否组合商品
            goodsInfoVO.setCombinedCommodity(Boolean.FALSE);
        });
        goodsModifyRequest.setGoodsInfos(goodsInfos);
        //商品规格
        List<GoodsSpecVO> goodsSpecs = KsBeanUtil.convert(cycleBuyModifyRequest.getGoodsSpecs(), GoodsSpecVO.class);
        goodsModifyRequest.setGoodsSpecs(goodsSpecs);
        //商品规格值列表
        List<GoodsSpecDetailVO> goodsSpecDetails = KsBeanUtil.convert(cycleBuyModifyRequest.getGoodsSpecDetails(), GoodsSpecDetailVO.class);
        goodsModifyRequest.setGoodsSpecDetails(goodsSpecDetails);

        //设置商品信息
        goodsModifyRequest.setGoods(oldData.getGoods());

        //商品详情模板关联
        if (CollectionUtils.isNotEmpty(oldData.getGoodsTabRelas())) {
            goodsModifyRequest.setGoodsTabRelas(oldData.getGoodsTabRelas());
        }

        //查询店铺分类ID
        Map<String, List<Integer>> storeCateIdMap = classifyProvider.searchGroupedClassifyIdByGoodsId(Arrays.asList(cycleBuyVO.getGoodsId())).getContext();
        if(storeCateIdMap != null){
            List<Integer> cateIds = storeCateIdMap.get(cycleBuyVO.getGoodsId());
            if(cateIds != null){
                oldData.getGoods().setStoreCateIds(cateIds.stream().map(Integer::longValue).collect(Collectors.toList()));
            }
        }
        if (!Objects.isNull(fId)){
            //判断运费模板是否存在
            freightTemplateGoodsQueryProvider.existsById(
                    FreightTemplateGoodsExistsByIdRequest.builder().freightTempId(fId).build());
        }
        GoodsModifyResponse response = goodsProvider.modify(goodsModifyRequest).getContext();
        Map<String, Object> returnMap = response.getReturnMap();
        if (org.apache.commons.collections.CollectionUtils.isNotEmpty((List<String>) returnMap.get("delStoreGoodsInfoIds"))) {
            esGoodsInfoElasticProvider.delete(EsGoodsDeleteByIdsRequest.builder()
                    .deleteIds((List<String>) returnMap.get("delStoreGoodsInfoIds")).build());
        }
        esGoodsInfoElasticProvider.deleteByGoods(EsGoodsDeleteByIdsRequest.builder().
                deleteIds(Collections.singletonList(cycleBuyVO.getGoodsId())).build());
        esGoodsInfoElasticProvider.initEsGoodsInfo(EsGoodsInfoRequest.builder().goodsId(cycleBuyVO.getGoodsId()).build());

        //更新redis商品基本数据
        String goodsDetailInfo =
                redisService.getString(RedisKeyConstant.GOODS_DETAIL_CACHE + cycleBuyVO.getGoodsId());
        if (StringUtils.isNotBlank(goodsDetailInfo)) {
            redisService.delete(RedisKeyConstant.GOODS_DETAIL_CACHE + cycleBuyVO.getGoodsId());
        }

        //刷新商品库
        if(CollectionUtils.isNotEmpty(response.getStandardIds())){
            esStandardProvider.init(EsStandardInitRequest.builder().goodsIds(response.getStandardIds()).build());
        }


        cycleBuyModifyRequest.setSendDateRules(cycleBuyModifyRequest.getDateRule());

        cycleBuySaveProvider.modify(cycleBuyModifyRequest).getContext().getCycleBuyVO();
        operateLogMQUtil.convertAndSend("周期购", "编辑周期活动",
                "编辑周期活动：商品ID" + oldData.getGoods().getGoodsNo());

        return BaseResponse.success(CycleBuyModifyResponse.builder().cycleBuyVO(cycleBuyVO).build());
    }


    /**
     * 分页查询周期购活动
     *
     * @param cycleBuyPageReq
     * @return
     */
    @ApiOperation(value = "分页查询周期购活动")
    @PostMapping("/page")
    public BaseResponse<CycleBuyPageResponse> page(@RequestBody @Valid CycleBuyPageRequest cycleBuyPageReq) {
        MicroServicePage<CycleBuyVO> microPage = cycleBuyQueryProvider.page(cycleBuyPageReq).getContext().getCycleBuyVOS();
        List<String> goodsIds = microPage.stream().map(CycleBuyVO::getGoodsId).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(goodsIds)) {
            List<GoodsVO> goodsVOS = goodsQueryProvider.listByCondition(GoodsByConditionRequest.builder().goodsIds(goodsIds).build()).getContext().getGoodsVOList();
            microPage.stream().forEach(cycleBuyVO -> {
                goodsVOS.forEach(goodsVO -> {
                    if (Objects.equals(cycleBuyVO.getGoodsId(), goodsVO.getGoodsId())) {
                        cycleBuyVO.setGoodsVO(goodsVO);
                    }
                });
            });
        }
        return BaseResponse.success(CycleBuyPageResponse.builder().cycleBuyVOS(microPage).build());
    }


    /**
     * 查询单个周期购活动
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "查询单个周期购活动")
    @GetMapping("/cycle-buy-id/{id}")
    public BaseResponse<CycleBuyByIdResponse> getById(@PathVariable Long id) {
        CycleBuyByIdRequest cycleBuyByIdRequest = new CycleBuyByIdRequest();
        cycleBuyByIdRequest.setId(id);

        CycleBuyVO cycleBuyVO = cycleBuyQueryProvider.getById(cycleBuyByIdRequest).getContext().getCycleBuyVO();

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
        }
        return BaseResponse.success(CycleBuyByIdResponse.builder().cycleBuyVO(cycleBuyVO).build());
    }


    /**
     * 删除单个周期购活动
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "删除单个周期购活动")
    @GetMapping("/cycle-del-id/{id}")
    @GlobalTransactional
    public BaseResponse deleteById(@PathVariable Long id) {
        CycleBuyDelByIdRequest cycleBuyDelByIdRequest = new CycleBuyDelByIdRequest();
        cycleBuyDelByIdRequest.setId(id);
        CycleBuyVO cycleBuyVO = cycleBuySaveProvider.deleteById(cycleBuyDelByIdRequest).getContext().getCycleBuyVO();

        //删除周期购商品
        GoodsDeleteByIdsRequest request=new GoodsDeleteByIdsRequest();
        request.setStoreId(commonUtil.getStoreId());
        request.setGoodsIds(Lists.newArrayList(cycleBuyVO.getGoodsId()));
        request.setDeleteReason(commonUtil.getOperatorId());
        goodsProvider.deleteByIds(request);

        //关联供应商商品下架
        GoodsListByIdsResponse goodsListByIdsResponse =
                goodsQueryProvider.listByProviderGoodsId(GoodsListByIdsRequest.builder().goodsIds(request.getGoodsIds()).build()).getContext();
        if (goodsListByIdsResponse != null && org.apache.commons.collections.CollectionUtils.isNotEmpty(goodsListByIdsResponse.getGoodsVOList())) {
            List<String> providerOfGoodsIds =
                    goodsListByIdsResponse.getGoodsVOList().stream().map(GoodsVO::getGoodsId).collect(Collectors.toList());
            GoodsInfoListByConditionResponse goodsInfoListByConditionResponse =
                    goodsInfoQueryProvider.listByCondition(GoodsInfoListByConditionRequest.builder().goodsIds(providerOfGoodsIds).build()).getContext();
            if (goodsInfoListByConditionResponse != null && org.apache.commons.collections.CollectionUtils.isNotEmpty(goodsInfoListByConditionResponse.getGoodsInfos())) {
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
     * 上下架周期购活动
     */
    @ApiOperation(value = "上下架周期购活动")
    @PutMapping("/sale")
    @GlobalTransactional
    public BaseResponse onSale(@RequestBody CycleBuySaleRequest request) {

        CycleBuyVO cycleBuyVO = cycleBuySaveProvider.loading(request).getContext().getCycleBuyVO();
        //周期购商品上下架
        GoodsModifyAddedStatusRequest goodsModifyAddedStatusRequest = new GoodsModifyAddedStatusRequest();
        goodsModifyAddedStatusRequest.setGoodsIds(Arrays.asList(cycleBuyVO.getGoodsId()));
        List<String> goodsIds =new ArrayList<>();
        goodsIds.add(cycleBuyVO.getGoodsId());

        if (AddedFlag.NO.equals(request.getAddedFlag())) {

            goodsModifyAddedStatusRequest.setAddedFlag(AddedFlag.NO.toValue());
            //如果下架商品是供应商商品，商家商品同步下架
            GoodsListByIdsRequest goodsListByIdsRequest = new GoodsListByIdsRequest();
            goodsListByIdsRequest.setGoodsIds(Lists.newArrayList(cycleBuyVO.getGoodsId()));
            List<GoodsVO> goodsVOList = goodsQueryProvider.listByCondition(
                    GoodsByConditionRequest.builder().providerGoodsIds(goodsIds).build()).getContext().getGoodsVOList();
            if (CollectionUtils.isNotEmpty(goodsVOList)) {
                goodsVOList.forEach(s -> {
                    goodsIds.add(s.getGoodsId());
                });
            }
            goodsModifyAddedStatusRequest.setGoodsIds(goodsIds);

            goodsProvider.modifyAddedStatus(goodsModifyAddedStatusRequest);

            //更新ES
            esGoodsInfoElasticProvider.updateAddedStatus(EsGoodsInfoModifyAddedStatusRequest.builder().
                    addedFlag(AddedFlag.NO.toValue()).goodsIds(goodsIds).goodsInfoIds(null).build());

            //更新redis商品基本数据
            String goodsDetailInfo = redisService.getString(RedisKeyConstant.GOODS_DETAIL_CACHE + goodsIds);
            if (StringUtils.isNotBlank(goodsDetailInfo)) {
                redisService.delete(RedisKeyConstant.GOODS_DETAIL_CACHE + goodsIds);
            }


            //ares埋点-商品-后台批量修改商品spu的所有sku上下架状态
            goodsAresProvider.dispatchFunction(new DispatcherFunctionRequest("editGoodsSpuUp",
                    new Object[]{AddedFlag.NO.toValue(), goodsIds}));

            GoodsByIdRequest goodsByIdRequest = new GoodsByIdRequest();
            goodsByIdRequest.setGoodsId(cycleBuyVO.getGoodsId());
            GoodsByIdResponse response = goodsQueryProvider.getById(goodsByIdRequest).getContext();
            operateLogMQUtil.convertAndSend("商品", "下架",
                    "下架：SPU编码" + response.getGoodsNo());

        }
        if (AddedFlag.YES.equals(request.getAddedFlag())) {
            goodsModifyAddedStatusRequest.setAddedFlag(AddedFlag.YES.toValue());

            goodsProvider.modifyAddedStatus(goodsModifyAddedStatusRequest);
            //更新ES
            esGoodsInfoElasticProvider.updateAddedStatus(EsGoodsInfoModifyAddedStatusRequest.builder().
                    addedFlag(AddedFlag.YES.toValue()).goodsIds(goodsIds).goodsInfoIds(null).build());

            //ares埋点-商品-后台批量修改商品spu的所有sku上下架状态
            goodsAresProvider.dispatchFunction(new DispatcherFunctionRequest("editGoodsSpuUp",
                    new Object[]{AddedFlag.YES.toValue(), goodsIds}));

            GoodsByIdRequest goodsByIdRequest = new GoodsByIdRequest();
            goodsByIdRequest.setGoodsId(cycleBuyVO.getGoodsId());
            GoodsByIdResponse response = goodsQueryProvider.getById(goodsByIdRequest).getContext();
            operateLogMQUtil.convertAndSend("商品", "上架",
                    "上架：SPU编码" + response.getGoodsNo());

        }
        return BaseResponse.SUCCESSFUL();
    }
}
