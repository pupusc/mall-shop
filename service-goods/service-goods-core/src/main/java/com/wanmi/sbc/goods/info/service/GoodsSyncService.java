package com.wanmi.sbc.goods.info.service;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.constant.RedisKeyConstant;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.common.util.StringUtil;
import com.wanmi.sbc.goods.api.request.goods.GoodsAuditQueryRequest;
import com.wanmi.sbc.goods.bean.enums.GoodsAdAuditStatus;
import com.wanmi.sbc.goods.bean.vo.GoodsAdAuditVO;
import com.wanmi.sbc.goods.bean.vo.GoodsSyncVO;
import com.wanmi.sbc.goods.common.RiskVerifyService;
import com.wanmi.sbc.goods.common.model.root.RiskVerify;
import com.wanmi.sbc.goods.info.repository.GoodsSyncRepository;
import com.wanmi.sbc.goods.info.request.GoodsStockSyncQueryRequest;
import com.wanmi.sbc.goods.info.request.GoodsSyncQueryRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import com.wanmi.sbc.goods.cate.model.root.GoodsCateSync;
import com.wanmi.sbc.goods.cate.repository.GoodsCateSyncRepository;
import com.wanmi.sbc.goods.common.repository.RiskVerifyRepository;
import com.wanmi.sbc.goods.tag.repository.TagRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import com.wanmi.sbc.goods.redis.RedisService;
import com.wanmi.sbc.goods.info.model.root.GoodsSync;
import org.springframework.util.CollectionUtils;
import com.wanmi.sbc.goods.api.response.goods.GoodsViewByIdResponse;
import com.wanmi.sbc.goods.bean.vo.*;
import javax.swing.text.html.HTML;
import com.wanmi.sbc.goods.tag.model.Tag;
import com.wanmi.sbc.goods.info.repository.GoodsSyncRelationRepository;

@Slf4j
@Service
public class GoodsSyncService {

    @Autowired
    private GoodsSyncRepository goodsSyncRepository;

    @Autowired
    private GoodsCateSyncRepository goodsCateSyncRepository;

    @Autowired
    private RiskVerifyRepository riskVerifyRepository;

    @Autowired
    private TagRepository tagRepository;

    private static String cateKey = "goods:sync:cate";

    @Autowired
    private RedisService redisService;

    @Value("${bookuu.default.cateId}")
    private Long defaultCateId;

    @Value("${bookuu.default.store.cateId}")
    private Long defaultStoreCateId;

    @Value("${bookuu.default.freight.tempId}")
    private Long defaultFreightTempId;

    @Value("${prop.list}")
    private String propDetailStr;

    @Autowired
    private GoodsSyncRelationRepository goodsSyncRelationRepository;

    public MicroServicePage<GoodsSyncVO> list(GoodsSyncQueryRequest goodsSyncQueryRequest) {
        List<GoodsCateSync> cate = listCate();
        if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(goodsSyncQueryRequest.getCategory())) {
            goodsSyncQueryRequest.setSubCategoryList(getParentCate(goodsSyncQueryRequest.getCategory(), cate));
        }
        if(org.apache.commons.collections4.CollectionUtils.isNotEmpty(goodsSyncQueryRequest.getSpuList())){
             goodsSyncQueryRequest.setGoodsNo(goodsSyncRelationRepository.findByGoodsNo(goodsSyncQueryRequest.getSpuList()));
        }
        Page<GoodsSync> page = goodsSyncRepository.findAll(goodsSyncQueryRequest.getWhereCriteria(), goodsSyncQueryRequest.getPageRequest());
        MicroServicePage<GoodsSyncVO> list = KsBeanUtil.convertPage(page, GoodsSyncVO.class);
        if (list == null || org.apache.commons.collections4.CollectionUtils.isEmpty(list.getContent())) {
            return list;
        }
        //查询同盾审核拒绝的数据
        List<RiskVerify> riskVerifies = new ArrayList<>();
        List<String> goodsNo = list.getContent().stream().filter(p -> p.getAdAuditStatus().equals(GoodsAdAuditStatus.FAIL.toValue())).map(GoodsSyncVO::getGoodsNo).collect(Collectors.toList());
        if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(goodsNo)) {
            GoodsStockSyncQueryRequest request = new GoodsStockSyncQueryRequest();
            request.setStatus(3);
            request.setGoodsNos(goodsNo);
            riskVerifies = riskVerifyRepository.findAll(request.getVerifyWhereCriteria());
        }
        List<RiskVerify> imageList = riskVerifies;
        list.getContent().forEach(p -> {
            //建议售价毛利率=（1-合作伙伴成本价/建议售价）%，四舍五入，保留1位小数，0.1%
            if (p.getBasePrice() != null && p.getSalePrice() != null && p.getSalePrice().compareTo(new BigDecimal(0)) > 0) {
                p.setRate(new BigDecimal("1").subtract((p.getBasePrice().divide(p.getSalePrice(), 3, RoundingMode.UP))));
            }
            GoodsSyncQueryRequest request = new GoodsSyncQueryRequest();
            request.setCategory(Lists.newArrayList(p.getCategory()));
            List<RiskVerify> lst = imageList.stream().filter(o->o.getGoodsNo().equals(p.getGoodsNo())).collect(Collectors.toList());
            if(org.apache.commons.collections4.CollectionUtils.isNotEmpty(lst)){
                p.setRejectList(KsBeanUtil.convertList(lst, GoodsAdAuditVO.class));
            }
            while (org.apache.commons.collections4.CollectionUtils.isNotEmpty(cate)) {
                if (!cate.stream().anyMatch(c -> request.getCategory().contains(c.getId()))) {
                    break;
                }
                GoodsCateSync cateSync = cate.stream().filter(c -> request.getCategory().contains(c.getId())).findFirst().get();
                p.getParentCategory().add(cateSync.getName());
                request.setCategory(Lists.newArrayList(cateSync.getParentId()));
            }
        });

        return list;
    }

    private List<Integer> getParentCate(List<Integer> category, List<GoodsCateSync> cate) {
        //类目查询
        GoodsSyncQueryRequest request = new GoodsSyncQueryRequest();
        List<Integer> cateIds = new ArrayList<>(cate.size());
        cateIds.addAll(category);
        List<Integer> parentId = new ArrayList<>(cate.size());
        request.setCategory(cateIds);
        while (cate.stream().anyMatch(p -> request.getCategory().contains(p.getParentId()))) {
            parentId = cate.stream().filter(p -> request.getCategory().contains(p.getParentId())).map(GoodsCateSync::getId).collect(Collectors.toList());
            cateIds.addAll(parentId);
            request.setCategory(parentId);
        }
        return cateIds;
    }

    private List<GoodsCateSync> listCate() {
        List<GoodsCateSync> cateList = null;//redisService.getList(cateKey,GoodsCateSync.class);
        if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(cateList)) {
            return cateList;
        }
        List<GoodsCateSync> cate = goodsCateSyncRepository.findAll();
        redisService.put(cateKey, JSONObject.toJSONString(cate));
        return cate;
    }

    /**
     * 提交广告法审核
     *
     * @param request
     */
    public void auditGoods(GoodsAuditQueryRequest request) {
        if (request == null || CollectionUtils.isEmpty(request.getIds())) {
            throw new SbcRuntimeException("");
        }
        goodsSyncRepository.batchUpdateAdStatus(request.getIds(), GoodsAdAuditStatus.WAITTOAUDIT.toValue(), GoodsAdAuditStatus.WAIT.toValue());
    }

    /**
     * 广告法人工审核通过
     *
     * @param request
     */
    public void approveAdManual(GoodsAuditQueryRequest request) {
        if (request == null || CollectionUtils.isEmpty(request.getIds())) {
            throw new SbcRuntimeException("");
        }
        goodsSyncRepository.batchApprove(request.getIds());
    }

    /**
     * 广告法人工审核拒绝
     *
     * @param request
     */
    public void rejectAdManual(GoodsAuditQueryRequest request) {
        if (request == null || CollectionUtils.isEmpty(request.getIds())) {
            throw new SbcRuntimeException("");
        }
        goodsSyncRepository.batchReject(request.getIds(), request.getRejectReason());
    }

    /**
     * 上架审核通过
     *
     * @param request
     */
    public void approveLaunch(GoodsAuditQueryRequest request) {
        if (request == null || CollectionUtils.isEmpty(request.getIds())) {
            throw new SbcRuntimeException("");
        }
        goodsSyncRepository.batchApproveLaunch(request.getIds());
    }

    /**
     * 上架审核拒绝
     *
     * @param request
     */
    public void rejectLaunch(GoodsAuditQueryRequest request) {
        if (request == null || CollectionUtils.isEmpty(request.getIds())) {
            throw new SbcRuntimeException("");
        }
        goodsSyncRepository.batchRejectLaunch(request.getIds(), request.getRejectReason());
    }

    /**
     * 发布商品
     */
    public void publish(GoodsAuditQueryRequest request) {
        if (request == null || CollectionUtils.isEmpty(request.getIds())) {
            throw new SbcRuntimeException("");
        }
        goodsSyncRepository.batchPublish(request.getIds());
    }

    public GoodsViewByIdResponse detail(String goodsNo){
        GoodsSync goodsSync = goodsSyncRepository.findByGoodsNo(goodsNo);
        return convertBean(goodsSync);
    }

    private GoodsViewByIdResponse convertBean(GoodsSync goods) {
        GoodsViewByIdResponse response = new GoodsViewByIdResponse();
        if(goods == null){
            return response;
        }
        GoodsVO goodsVO = new GoodsVO();
        goodsVO.setProviderId(goods.getProviderId());
        goodsVO.setGoodsName(goods.getTitle());
        goodsVO.setErpGoodsNo(goods.getGoodsNo());
        goodsVO.setAllowPriceSet(0);
        goodsVO.setGoodsType(0);
        goodsVO.setSaleType(1);
        goodsVO.setGoodsBuyTypes("0,1");
        goodsVO.setAddedFlag(1);
        goodsVO.setAddedTimingFlag(false);
        goodsVO.setGoodsUnit("本");
        goodsVO.setGoodsWeight(new BigDecimal("0.25"));
        goodsVO.setGoodsCubage(new BigDecimal("0.006"));
        goodsVO.setStoreCateIds(Arrays.asList(defaultStoreCateId));
        goodsVO.setCateId(defaultCateId);
        goodsVO.setFreightTempId(defaultFreightTempId);
        goodsVO.setGoodsSource(0);


        GoodsInfoVO goodsInfoVO = new GoodsInfoVO();
        goodsInfoVO.setGoodsType(0);
        goodsInfoVO.setErpGoodsNo(goods.getGoodsNo());
        goodsInfoVO.setCombinedCommodity(false);
        goodsInfoVO.setStock(goods.getQty().longValue());
        goodsInfoVO.setIsbnNo(goods.getIsbn());
        goodsInfoVO.setRetailPrice(goods.getSalePrice());
        goodsInfoVO.setCostPrice(goods.getBasePrice());
        //1. 定价规则：市场价=合作伙伴成本价/0.9 > 建议销售价【数字不填写，人工处理】
        //合作伙伴成本价/0.9<= 建议销售价 <= 合作伙伴成本价/0.8【使用，建议销售价】
        //合作伙伴成本价/0.8<= 建议销售价【使用，合作伙伴成本价/0.8】
        goodsInfoVO.setMarketPrice(goods.getSalePrice());
        if (goods.getBasePrice() != null && goods.getSalePrice() != null) {
            BigDecimal math1 = new BigDecimal(String.valueOf(goods.getBasePrice())).divide(new BigDecimal("0.9"),2,BigDecimal.ROUND_UP);
            BigDecimal math2 = new BigDecimal(String.valueOf(goods.getBasePrice())).divide(new BigDecimal("0.8"),2,BigDecimal.ROUND_UP);
            if (goods.getSalePrice().compareTo(math1) >= 0 && goods.getSalePrice().compareTo(math2) <= 0) {
                goodsInfoVO.setMarketPrice(goods.getSalePrice());
            } else if (goods.getSalePrice().compareTo(math2) > 0) {
                goodsInfoVO.setMarketPrice(math2);
            } else {
                goodsInfoVO.setMarketPrice(null);
            }
        }
        goodsVO.setMarketPrice(goodsInfoVO.getMarketPrice());
        goodsVO.setCostPrice(goodsInfoVO.getCostPrice());
        goodsVO.setRecommendedRetailPrice(goodsInfoVO.getRetailPrice());
        //所有商品都下架处理
        goodsVO.setAddedFlag(0);
        List<GoodsInfoVO> infos = new ArrayList<>(1);
        infos.add(goodsInfoVO);
        response.setGoodsInfos(infos);


        GoodsSpecVO goodsSpecVO = new GoodsSpecVO();
        goodsSpecVO.setMockSpecId(new Random().nextLong());
        goodsSpecVO.setSpecName("规格1");
        List<GoodsSpecVO> specs = new ArrayList<>(1);
        specs.add(goodsSpecVO);
        response.setGoodsSpecs(specs);

        List<GoodsImageVO> images = new ArrayList<>();
        //图片
        if (StringUtils.isNotEmpty(goods.getLargeImageUrl())) {
            String[] imgs = goods.getLargeImageUrl().split("\\|");
            if (imgs != null && imgs.length > 0) {
                for (int i = 0; i < imgs.length; i++) {
                    GoodsImageVO image = new GoodsImageVO();
                    image.setSort(i);
                    image.setArtworkUrl(i == 0 ? imgs[i] : ("http://images.bookuu.com" + imgs[i]));
                    images.add(image);
                }
            }
        }
        //详情图
        if(StringUtils.isNotEmpty(goods.getDetailImageUrl())){
            StringBuilder sb = new StringBuilder();
            String[] imgs = goods.getDetailImageUrl().split("\\|");
            if(imgs!=null && imgs.length >0){
                for(int i=0;i<imgs.length;i++){
                    sb.append("<p><img src=\"")
                            .append("http://images.bookuu.com"+imgs[i])
                            .append("\" title=\"\" alt=\"undefined/\"/></p><br/>");
                }
            }
            goodsVO.setGoodsDetail(sb.toString());
        }
        if(org.apache.commons.collections4.CollectionUtils.isNotEmpty(images)){
            goodsVO.setGoodsUnBackImg(images.get(0).getArtworkUrl());
        }


        response.setGoods(goodsVO);
        response.setImages(images);
        //属性
        List<GoodsPropDetailRelVO> propDetails = new ArrayList<>();
        JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(goods));
        List<GoodsPropDetailRelVO> propDetail = JSONObject.parseArray(propDetailStr, GoodsPropDetailRelVO.class);
        propDetail.forEach(p -> {
            if (jsonObject.get(p.getPropValue()) != null) {
                GoodsPropDetailRelVO prop = new GoodsPropDetailRelVO();
                prop.setDetailId(0L);
                prop.setPropId(p.getPropId());
                prop.setPropValue(String.valueOf(jsonObject.get(p.getPropValue())));
                propDetails.add(prop);
            }
        });
        response.setGoodsPropDetailRels(propDetails);
        //根据类目查询相应标签
        GoodsCateSync cateSync = goodsCateSyncRepository.findById(goods.getCategory().intValue()).orElse(null);
        if(cateSync != null && StringUtils.isNotEmpty(cateSync.getLabelIds())){
            List<Long> tagIds = Arrays.stream(cateSync.getLabelIds().split(",")).map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
            List<Tag> tags= tagRepository.findByIds(tagIds);
            if(org.apache.commons.collections4.CollectionUtils.isNotEmpty(tags)) {
                response.setTags(KsBeanUtil.convertList(tags,TagVO.class));
            }
        }
        return response;
    }
}
