package com.wanmi.sbc.job;


import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.customer.api.provider.company.CompanyInfoQueryProvider;
import com.wanmi.sbc.customer.api.request.company.CompanyInfoByIdRequest;
import com.wanmi.sbc.customer.bean.vo.CompanyInfoVO;
import com.wanmi.sbc.elastic.api.provider.goods.EsGoodsInfoElasticProvider;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsInfoRequest;
import com.wanmi.sbc.goods.api.provider.ares.GoodsAresProvider;
import com.wanmi.sbc.goods.api.provider.freight.FreightTemplateGoodsQueryProvider;
import com.wanmi.sbc.goods.api.provider.goods.GoodsProvider;
import com.wanmi.sbc.goods.api.provider.goods.GoodsQueryProvider;
import com.wanmi.sbc.goods.api.request.ares.DispatcherFunctionRequest;
import com.wanmi.sbc.goods.api.request.freight.FreightTemplateGoodsExistsByIdRequest;
import com.wanmi.sbc.goods.api.request.goods.GoodsAddRequest;
import com.wanmi.sbc.goods.api.response.goods.GoodsAddResponse;
import com.wanmi.sbc.goods.bean.dto.*;
import com.wanmi.sbc.goods.bean.enums.GoodsType;
import com.wanmi.sbc.goods.bean.vo.GoodsSyncVO;
import com.wanmi.sbc.util.OperateLogMQUtil;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;


/**
 * 将同步的商品上架
 */
@JobHandler(value = "goodsSpuSyncJobHandler")
@Component
@Slf4j
public class GoodsSpuSyncJobHandler extends IJobHandler {

    @Autowired
    private GoodsProvider goodsProvider;

    @Autowired
    private GoodsQueryProvider goodsQueryProvider;

    @Autowired
    private CompanyInfoQueryProvider companyInfoQueryProvider;

    @Autowired
    private FreightTemplateGoodsQueryProvider freightTemplateGoodsQueryProvider;

    @Autowired
    private GoodsAresProvider goodsAresProvider;

    @Autowired
    private EsGoodsInfoElasticProvider esGoodsInfoElasticProvider;

    @Autowired
    private OperateLogMQUtil operateLogMQUtil;

    private static final String GOODS_BUY_TYPES = "0,1";

    // todo 跟产品确认
    @Value("${bookuu.default.companyId}")
    private Long defaultCompanyId;

    @Value("${bookuu.default.storeId}")
    private Long defaultStoreId;


    @Value("${prop.list}")
    private String propDetailStr;

    @Value("${bookuu.default.cateId}")
    private Long defaultCateId;

    @Value("${bookuu.default.store.cateId}")
    private Long defaultStoreCateId;

    @Value("${bookuu.default.freight.tempId}")
    private Long defaultFreightTempId;


    @Override
    public ReturnT<String> execute(String params) throws Exception {
        log.info("=====发布商品start======");
        //查询审核通过待发布的商品信息
        BaseResponse<List<GoodsSyncVO>> response = goodsQueryProvider.listGoodsSync();
        if(response == null || CollectionUtils.isEmpty(response.getContext())){
            log.info("没有审核通过待发布的商品");
            return SUCCESS;
        }
        response.getContext().forEach(g->{
            addGoods(g);
        });
        return SUCCESS;
    }

    private void addGoods(GoodsSyncVO goodsSync){
        try {
            GoodsAddRequest request = convertBean(goodsSync);
            request.setUpdatePerson("goodsSpuSyncJob");
            //默认模版
            Long fId = request.getGoods().getFreightTempId();
            if ((request.getGoods() == null || CollectionUtils.isEmpty(request.getGoodsInfos()) || Objects.isNull(fId))
                    && (request.getGoods().getGoodsType() != GoodsType.CYCLE_BUY.toValue() && request.getGoods().getGoodsType() == GoodsType.REAL_GOODS.toValue())) {
                throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
            }
            // 添加默认值, 适应云掌柜新增商品没有设置购买方式, 导致前台不展示购买方式问题
            if (StringUtils.isBlank(request.getGoods().getGoodsBuyTypes())) {
                request.getGoods().setGoodsBuyTypes(GOODS_BUY_TYPES);
            }

            CompanyInfoVO companyInfo = companyInfoQueryProvider.getCompanyInfoById(
                    CompanyInfoByIdRequest.builder().companyInfoId(defaultCompanyId).build()
            ).getContext();
            if (companyInfo == null) {
                throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
            }

            if (!Objects.isNull(fId)) {
                //判断运费模板是否存在
                freightTemplateGoodsQueryProvider.existsById(
                        FreightTemplateGoodsExistsByIdRequest.builder().freightTempId(fId).build());
            }
            request.getGoods().setCompanyInfoId(defaultCompanyId);
            request.getGoods().setCompanyType(companyInfo.getCompanyType());
            request.getGoods().setStoreId(defaultStoreId);
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
            return;
        }catch (Exception e){
            log.warn("同步商品失败",e);
        }
    }

    private GoodsAddRequest convertBean(GoodsSyncVO goods) {
        GoodsAddRequest request = new GoodsAddRequest();

        GoodsDTO goodsDTO = new GoodsDTO();
        goodsDTO.setProviderId(goods.getProviderId());
        goodsDTO.setGoodsName(goods.getTitle());
        goodsDTO.setErpGoodsNo(goods.getGoodsNo());
        goodsDTO.setAllowPriceSet(0);
        goodsDTO.setGoodsType(0);
        //todo 写死默认值，等产品确认
        goodsDTO.setSaleType(1);
        goodsDTO.setGoodsBuyTypes("0,1");
        goodsDTO.setAddedFlag(1);
        goodsDTO.setAddedTimingFlag(false);
        goodsDTO.setGoodsUnit("本");
        goodsDTO.setGoodsWeight(new BigDecimal(0.25));
        goodsDTO.setGoodsCubage(new BigDecimal(0.006));
        goodsDTO.setStoreCateIds(Arrays.asList(defaultStoreCateId));
        goodsDTO.setCateId(defaultCateId);
        goodsDTO.setFreightTempId(defaultFreightTempId);
        goodsDTO.setGoodsNo(getRandomGoodsNo("P"));
        goodsDTO.setGoodsSource(0);

        request.setGoods(goodsDTO);

        GoodsInfoDTO goodsInfoDTO = new GoodsInfoDTO();
        goodsInfoDTO.setErpGoodsInfoNo(goods.getGoodsNo());
        goodsInfoDTO.setGoodsType(0);
        goodsInfoDTO.setErpGoodsNo(goods.getGoodsNo());
        goodsInfoDTO.setCombinedCommodity(false);
        goodsInfoDTO.setGoodsInfoNo(getRandomGoodsNo("8"));
        goodsInfoDTO.setStock(goods.getQty().longValue());
        goodsInfoDTO.setIsbnNo(goods.getIsbn());
        goodsInfoDTO.setRetailPrice(goods.getSalePrice());
        goodsInfoDTO.setCostPrice(goods.getBasePrice());
        //1. 定价规则：市场价=合作伙伴成本价 * 10% > 建议销售价【数字不填写，人工处理】
        //合作伙伴成本价 * 10% <= 建议销售价 <= 合作伙伴成本价 * 20%【使用，建议销售价】
        //合作伙伴成本价 * 20% <= 建议销售价【使用，合作伙伴成本价 * 20%】
        goodsInfoDTO.setMarketPrice(goods.getSalePrice());
        if(goods.getBasePrice() != null && goods.getSalePrice() !=null){
            BigDecimal math1= new BigDecimal(String.valueOf(goods.getBasePrice())).multiply(new BigDecimal("1.1")).setScale(2,BigDecimal.ROUND_UP);
            BigDecimal math2= new BigDecimal(String.valueOf(goods.getBasePrice())).multiply(new BigDecimal("1.2")).setScale(2,BigDecimal.ROUND_UP);
            if(goods.getSalePrice().compareTo(math1) >=0 && goods.getSalePrice().compareTo(math2) <= 0){
                goodsInfoDTO.setMarketPrice(goods.getSalePrice());
            }else if(goods.getSalePrice().compareTo(math2) > 0){
                goodsInfoDTO.setMarketPrice(goods.getBasePrice().multiply(new BigDecimal(1.2)));
            }else{
                goodsInfoDTO.setMarketPrice(null);
                goodsDTO.setAddedFlag(0);
            }
        }
        List<GoodsInfoDTO> infos = new ArrayList<>(1);
        infos.add(goodsInfoDTO);
        request.setGoodsInfos(infos);


        GoodsSpecDTO goodsSpecDTO = new GoodsSpecDTO();
        goodsSpecDTO.setMockSpecId(new Random().nextLong());
        goodsSpecDTO.setSpecName("规格1");
        List<GoodsSpecDTO> specs = new ArrayList<>(1);
        specs.add(goodsSpecDTO);
        request.setGoodsSpecs(specs);

        List<GoodsImageDTO> images = new ArrayList<>();
        //图片
        if(StringUtils.isNotEmpty(goods.getLargeImageUrl())){
            String[] imgs = goods.getLargeImageUrl().split("\\|");
            if(imgs!=null && imgs.length >0){
                for(int i=0;i<imgs.length;i++){
                    GoodsImageDTO image = new GoodsImageDTO();
                    image.setSort(i);
                    image.setArtworkUrl(i== 0? imgs[i] :("http://images.bookuu.com"+imgs[i]));
                    images.add(image);
                }
            }
        }
        request.setImages(images);
        //属性
        List<GoodsPropDetailRelDTO> propDetails = new ArrayList<>();
        JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(goods));
        List<GoodsPropDetailRelDTO> propDetail = JSONObject.parseArray(propDetailStr,GoodsPropDetailRelDTO.class);
        propDetail.forEach(p->{
            if(jsonObject.get(p.getPropValue()) != null){
                GoodsPropDetailRelDTO prop = new GoodsPropDetailRelDTO();
                prop.setDetailId(0L);
                prop.setPropId(p.getPropId());
                prop.setPropValue(String.valueOf(jsonObject.get(p.getPropValue())));
                propDetails.add(prop);
            }
        });
        request.setGoodsPropDetailRels(propDetails);
        return request;
    }

    private String getRandomGoodsNo(String prix) {
        StringBuilder sb = new StringBuilder();
        sb.append(prix).append(String.valueOf(new Date().getTime()).substring(4, 10)).append(String.valueOf(Math.random()).substring(2, 5));
        return sb.toString();

    }
}