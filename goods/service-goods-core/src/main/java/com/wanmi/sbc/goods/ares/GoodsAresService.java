package com.wanmi.sbc.goods.ares;

import com.alibaba.fastjson.JSONObject;
import com.wanmi.ares.constants.MQConstant;
import com.wanmi.ares.request.mq.GoodsBrandRequest;
import com.wanmi.ares.request.mq.GoodsCateRequest;
import com.wanmi.ares.request.mq.GoodsInfoRequest;
import com.wanmi.ares.request.mq.StoreCateRequest;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.customer.api.provider.store.StoreQueryProvider;
import com.wanmi.sbc.customer.api.request.store.StoreByIdRequest;
import com.wanmi.sbc.customer.bean.vo.StoreVO;
import com.wanmi.sbc.goods.bean.enums.AddedFlag;
import com.wanmi.sbc.goods.bean.enums.CateParentTop;
import com.wanmi.sbc.goods.bean.enums.CheckStatus;
import com.wanmi.sbc.goods.brand.model.root.GoodsBrand;
import com.wanmi.sbc.goods.brand.repository.GoodsBrandRepository;
import com.wanmi.sbc.goods.cate.model.root.GoodsCate;
import com.wanmi.sbc.goods.cate.repository.GoodsCateRepository;
import com.wanmi.sbc.goods.cate.request.GoodsCateQueryRequest;
import com.wanmi.sbc.goods.info.model.root.Goods;
import com.wanmi.sbc.goods.info.model.root.GoodsInfo;
import com.wanmi.sbc.goods.info.repository.GoodsInfoRepository;
import com.wanmi.sbc.goods.info.repository.GoodsRepository;
import com.wanmi.sbc.goods.info.request.GoodsInfoQueryRequest;
import com.wanmi.sbc.goods.spec.model.root.GoodsInfoSpecDetailRel;
import com.wanmi.sbc.goods.spec.repository.GoodsInfoSpecDetailRelRepository;
import com.wanmi.sbc.goods.storecate.model.root.StoreCate;
import com.wanmi.sbc.goods.storecate.model.root.StoreCateGoodsRela;
import com.wanmi.sbc.goods.storecate.repository.StoreCateGoodsRelaRepository;
import com.wanmi.sbc.goods.storecate.repository.StoreCateRepository;
import com.wanmi.sbc.goods.storecate.request.StoreCateQueryRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.binding.BinderAwareChannelResolver;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 商品相关数据埋点 - 统计系统
 * Created by bail on 2017/10/16
 */
@Service
@Slf4j
@EnableBinding
public class GoodsAresService {

    @Autowired
    private GoodsCateRepository goodsCateRepository;

    @Autowired
    private GoodsRepository goodsRepository;

    @Autowired
    private GoodsInfoRepository goodsInfoRepository;

    @Autowired
    private GoodsInfoSpecDetailRelRepository goodsInfoSpecDetailRelRepository;

    @Autowired
    private GoodsBrandRepository goodsBrandRepository;

    @Autowired
    private StoreCateGoodsRelaRepository storeCateGoodsRelaRepository;

    @Autowired
    private StoreQueryProvider storeQueryProvider;

    @Autowired
    private StoreCateRepository storeCateRepository;

    @Autowired
    private BinderAwareChannelResolver resolver;

    private static final String S2B_COMPANY_ID = "0";//s2b平台端商户标识默认值

    private static final Map<CheckStatus, com.wanmi.ares.enums.CheckStatus> GOODS_CHECK_STAT_MAP =
            new EnumMap<>(CheckStatus.class);//商品审核状态转换策略map

    static {
        GOODS_CHECK_STAT_MAP.put(CheckStatus.WAIT_CHECK, com.wanmi.ares.enums.CheckStatus.WAIT_CHECK);
        GOODS_CHECK_STAT_MAP.put(CheckStatus.CHECKED, com.wanmi.ares.enums.CheckStatus.CHECKED);
        GOODS_CHECK_STAT_MAP.put(CheckStatus.NOT_PASS, com.wanmi.ares.enums.CheckStatus.NOT_PASS);
        GOODS_CHECK_STAT_MAP.put(CheckStatus.FORBADE, com.wanmi.ares.enums.CheckStatus.FORBADE);
    }

    /**
     * 初始化商品
     */
    public void initGoodsES() {
        dispatchFunction("addGoodsSku", goodsInfoRepository.findAll().stream().collect(Collectors.toList()));
    }

    /**
     * 初始化商品品牌
     */
    public void initGoodsBrandES() {
        goodsBrandRepository.findAll().stream().forEach(goodsBrand -> dispatchFunction("addGoodsBrand", goodsBrand));
    }

    /**
     * 初始化商品分类
     */
    public void initGoodsCateES() {
        goodsCateRepository.findAll().stream().forEach(goodsCate -> dispatchFunction("addGoodsCate", goodsCate));
    }

    /**
     * 初始化店铺分类
     */
    public void initStoreCateES() {
        storeCateRepository.findAll().stream().forEach(storeCate -> dispatchFunction("addStoreCate", storeCate));
    }

    /**
     * 埋点处理的分发方法
     *
     * @param funcType 类别,依据此进行分发
     * @param objs     多个入参对象
     */
    @Async
    public void dispatchFunction(String funcType, Object... objs) {
    }

    /**
     * 添加店铺分类
     *
     * @param objs [店铺分类]
     */
    public void addStoreCate(Object... objs) {
        StoreCateRequest storeCateRequest = getStoreCateReq(objs);
        resolver.resolveDestination(MQConstant.Q_ARES_STORE_CATE_CREATE).send(new GenericMessage<>(JSONObject.toJSONString(storeCateRequest)));
    }

    /**
     * 编辑店铺分类
     *
     * @param objs [店铺分类]
     */
    public void editStoreCate(Object... objs) {
        StoreCateRequest storeCateRequest = getStoreCateReq(objs);
        resolver.resolveDestination(MQConstant.Q_ARES_STORE_CATE_MODIFY).send(new GenericMessage<>(JSONObject.toJSONString(storeCateRequest)));
        getChildStoreCates((StoreCate) objs[0]).stream().forEach(storeCate ->
                resolver.resolveDestination(MQConstant.Q_ARES_STORE_CATE_MODIFY).send(new GenericMessage<>(JSONObject.toJSONString(getStoreCateReq(storeCate))))
        );
    }

    /**
     * 删除店铺分类
     *
     * @param objs [删除的店铺分类id]
     */
    public void delStoreCate(Object... objs) {
        List<Long> idList = (List<Long>) objs[0];
        //1.删除店铺分类
        resolver.resolveDestination(MQConstant.Q_ARES_STORE_CATE_DELETE).send(new GenericMessage<>(JSONObject.toJSONString(idList)));
        //2.更新店铺分类影响到的商品
        List<StoreCateGoodsRela> storeCateGoodsRelaList = (List<StoreCateGoodsRela>) objs[1];
        List<GoodsInfo> goodsInfoList =
                goodsInfoRepository.findByGoodsIdIn(storeCateGoodsRelaList.stream().map(StoreCateGoodsRela::getGoodsId).collect(Collectors.toList()));
        goodsInfoList.stream().forEach(goodsInfo -> editOneGoodsSku(goodsInfo));
    }

    /**
     * 根据父类获取所有子分类信息
     *
     * @param storeCate 初始的父分类信息
     * @return 所有子分类信息
     */
    private List<StoreCate> getChildStoreCates(StoreCate storeCate) {
        return storeCateRepository.findAll(StoreCateQueryRequest.builder()
                .likeCatePath(storeCate.getCatePath().concat(String.valueOf(storeCate.getStoreCateId())).concat("|")).build()
                .getWhereCriteria());
    }

    /**
     * 获取发送给统计系统的店铺类别全量信息
     *
     * @param objs [店铺类别]
     */
    public StoreCateRequest getStoreCateReq(Object... objs) {
        StoreCate storeCate = (StoreCate) objs[0];
        StoreCateRequest storeCateRequest = new StoreCateRequest();
        storeCateRequest.setId(storeCate.getStoreCateId().toString());
        storeCateRequest.setStoreId(storeCate.getStoreId().toString());
        StoreVO store =
                storeQueryProvider.getById(new StoreByIdRequest(storeCate.getStoreId())).getContext().getStoreVO();
        storeCateRequest.setCompanyInfoId(store.getCompanyInfo().getCompanyInfoId().toString());
        storeCateRequest.setCateName(storeCate.getCateName());
        storeCateRequest.setCateParentId(storeCate.getCateParentId().toString());
        storeCateRequest.setParentNames(getParentStoreCateNames(storeCate));//递归获取所有父类的名称
        storeCateRequest.setCatePath(storeCate.getCatePath());
        storeCateRequest.setCateGrade(storeCate.getCateGrade());
        storeCateRequest.setSort(storeCate.getSort());
        storeCateRequest.setDefault(DefaultFlag.YES.equals(storeCate.getIsDefault()));

        storeCateRequest.setOperationDate(storeCate.getCreateTime().toLocalDate());//创建时间 -- 操作时间
        storeCateRequest.setSendTime(LocalDateTime.now());
        storeCateRequest.setDelFlag(DeleteFlag.YES.equals(storeCate.getDelFlag()));
        return storeCateRequest;
    }

    /**
     * 获取指定格式的父类名称
     *
     * @param storeCate 初始的子分类信息
     * @return 指定格式的父类名称, 形如: 科教读物/图书音像
     */
    private String getParentStoreCateNames(StoreCate storeCate) {
        List<String> pareCateNmList = new ArrayList<>();
        getPareStoreCateNmRecursion(storeCate, pareCateNmList);//递归处理所有父类名称List
        if (CollectionUtils.isNotEmpty(pareCateNmList)) {
            return StringUtils.join(pareCateNmList, "/");//拼接输出
        } else {
            return "无";
        }
    }

    /**
     * 递归处理所有父类名称的List
     *
     * @param storeCate      初始的子分类信息
     * @param pareCateNmList 存储所有父类名称的集合
     */
    private void getPareStoreCateNmRecursion(StoreCate storeCate, List<String> pareCateNmList) {
        if (storeCate.getCateParentId() != null && storeCate.getCateParentId().longValue() != (long) CateParentTop.ZERO.toValue()) {//如果还有父级,则继续追溯
            StoreCate parentGoodsCate = storeCateRepository.findById(storeCate.getCateParentId()).orElse(new StoreCate());
            pareCateNmList.add(parentGoodsCate.getCateName());
            getPareStoreCateNmRecursion(parentGoodsCate, pareCateNmList);
        }
    }

    /**
     * 添加商品类别
     *
     * @param objs [商品类别]
     */
    public void addGoodsCate(Object... objs) {
        GoodsCateRequest goodsCateReq = getGoodsCateReq(objs);
        resolver.resolveDestination(MQConstant.Q_ARES_GOODS_CATE_CREATE).send(new GenericMessage<>(JSONObject.toJSONString(goodsCateReq)));
    }

    /**
     * 编辑商品类别
     *
     * @param objs [商品类别]
     */
    public void editGoodsCate(Object... objs) {
        GoodsCateRequest goodsCateReq = getGoodsCateReq(objs);
        resolver.resolveDestination(MQConstant.Q_ARES_GOODS_CATE_MODIFY).send(new GenericMessage<>(JSONObject.toJSONString(goodsCateReq)));
        getChildCates((GoodsCate) objs[0]).stream().forEach(goodsCate ->
                resolver.resolveDestination(MQConstant.Q_ARES_GOODS_CATE_MODIFY).send(new GenericMessage<>(JSONObject.toJSONString(getGoodsCateReq(goodsCate))))
        );
    }

    /**
     * 删除商品类别
     *
     * @param objs [删除的商品类别id]
     */
    public void delGoodsCate(Object... objs) {
        List<Long> idList = (List<Long>) objs[0];
        resolver.resolveDestination(MQConstant.Q_ARES_GOODS_CATE_DELETE).send(new GenericMessage<>(JSONObject.toJSONString(idList)));
    }

    /**
     * 根据父类获取所有子分类信息
     *
     * @param goodsCate 初始的父分类信息
     * @return 所有子分类信息
     */
    private List<GoodsCate> getChildCates(GoodsCate goodsCate) {
        return goodsCateRepository.findAll(GoodsCateQueryRequest.builder()
                .likeCatePath(goodsCate.getCatePath().concat(String.valueOf(goodsCate.getCateId())).concat("|")).build()
                .getWhereCriteria());
    }

    /**
     * 获取发送给统计系统的商品类别全量信息
     *
     * @param objs [商品类别]
     */
    public GoodsCateRequest getGoodsCateReq(Object... objs) {
        GoodsCate goodsCate = (GoodsCate) objs[0];
        GoodsCateRequest goodsCateReq = new GoodsCateRequest();
        goodsCateReq.setId(goodsCate.getCateId().toString());
        goodsCateReq.setName(goodsCate.getCateName());
        goodsCateReq.setParentId(goodsCate.getCateParentId());
        goodsCateReq.setParentNames(getParentCateNames(goodsCate));//递归获取所有父类的名称
        goodsCateReq.setImg(goodsCate.getCateImg());
        goodsCateReq.setGrade(goodsCate.getCateGrade());
        goodsCateReq.setSort(goodsCate.getSort());
        goodsCateReq.setDefault(DefaultFlag.YES.equals(goodsCate.getIsDefault()));
        goodsCateReq.setCompanyId(S2B_COMPANY_ID);
        goodsCateReq.setOperationDate(goodsCate.getCreateTime().toLocalDate());//创建时间 -- 操作时间
        goodsCateReq.setSendTime(LocalDateTime.now());
        goodsCateReq.setDelFlag(DeleteFlag.YES.equals(goodsCate.getDelFlag()));
        return goodsCateReq;
    }

    /**
     * 获取指定格式的父类名称
     *
     * @param goodsCate 初始的子分类信息
     * @return 指定格式的父类名称, 形如: 科教读物/图书音像
     */
    private String getParentCateNames(GoodsCate goodsCate) {
        List<String> pareCateNmList = new ArrayList<>();
        getPareCateNmRecursion(goodsCate, pareCateNmList);//递归处理所有父类名称List
        if (CollectionUtils.isNotEmpty(pareCateNmList)) {
            return StringUtils.join(pareCateNmList, "/");//拼接输出
        } else {
            return "无";
        }
    }

    /**
     * 递归处理所有父类名称的List
     *
     * @param goodsCate      初始的子分类信息
     * @param pareCateNmList 存储所有父类名称的集合
     */
    private void getPareCateNmRecursion(GoodsCate goodsCate, List<String> pareCateNmList) {
        if (goodsCate.getCateParentId() != null && goodsCate.getCateParentId().longValue() != (long) CateParentTop.ZERO.toValue()) {//如果还有父级,则继续追溯
            GoodsCate parentGoodsCate = goodsCateRepository.findById(goodsCate.getCateParentId()).orElse(new GoodsCate());
            pareCateNmList.add(parentGoodsCate.getCateName());
            getPareCateNmRecursion(parentGoodsCate, pareCateNmList);
        }
    }

    /**
     * 递归获取所有层级的商品分类标识的List
     *
     * @param goodsCate 初始的子分类标识
     * @param catIds    存储所有分类标识
     */
    private void getPareCateIdsRecursion(GoodsCate goodsCate, List<Long> catIds) {
        if (goodsCate.getCateParentId() != null && goodsCate.getCateParentId().longValue() != (long) CateParentTop.ZERO.toValue()) {//如果还有父级,则继续追溯
            GoodsCate parentGoodsCate = goodsCateRepository.findById(goodsCate.getCateParentId()).orElse(new GoodsCate());
            catIds.add(parentGoodsCate.getCateId());
            getPareCateIdsRecursion(parentGoodsCate, catIds);
        }
    }

    /**
     * 添加商品品牌
     *
     * @param objs [商品品牌]
     */
    public void addGoodsBrand(Object... objs) {
        GoodsBrandRequest goodsBrandReq = getGoodsBrandReq(objs);
        resolver.resolveDestination(MQConstant.Q_ARES_GOODS_BRAND_CREATE).send(new GenericMessage<>(JSONObject.toJSONString(goodsBrandReq)));
    }

    /**
     * 编辑商品品牌
     *
     * @param objs [商品品牌]
     */
    public void editGoodsBrand(Object... objs) {
        GoodsBrandRequest goodsBrandReq = getGoodsBrandReq(objs);
        resolver.resolveDestination(MQConstant.Q_ARES_GOODS_BRAND_MODIFY).send(new GenericMessage<>(JSONObject.toJSONString(goodsBrandReq)));
    }

    /**
     * 删除商品品牌
     *
     * @param objs [删除的商品品牌id]
     */
    public void delGoodsBrand(Object... objs) {
        Long id = (Long) objs[0];
        resolver.resolveDestination(MQConstant.Q_ARES_GOODS_BRAND_DELETE).send(new GenericMessage<>(id));
    }

    /**
     * 获取发送给统计系统的商品品牌全量信息
     *
     * @param objs [商品品牌]
     */
    public GoodsBrandRequest getGoodsBrandReq(Object... objs) {
        GoodsBrand goodsBrand = (GoodsBrand) objs[0];
        GoodsBrandRequest goodsBrandReq = new GoodsBrandRequest();
        goodsBrandReq.setId(goodsBrand.getBrandId().toString());
        goodsBrandReq.setName(goodsBrand.getBrandName());
        goodsBrandReq.setCompanyId(S2B_COMPANY_ID);
        goodsBrandReq.setOperationDate(goodsBrand.getCreateTime().toLocalDate());//创建时间 -- 操作时间
        goodsBrandReq.setSendTime(LocalDateTime.now());
        goodsBrandReq.setDelFlag(DeleteFlag.YES.equals(goodsBrand.getDelFlag()));
        return goodsBrandReq;
    }

    /**
     * 根据spuId获取Sku
     *
     * @param goodsId spuId
     * @return
     */
    public List<GoodsInfo> findSkuByGoodsId(String goodsId) {
        return goodsInfoRepository.findAll(GoodsInfoQueryRequest.builder().goodsId(goodsId).build().getWhereCriteria());
    }

    /**
     * 根据spuIdList获取Sku
     *
     * @param goodsIds spuIdList
     * @return
     */
    public List<GoodsInfo> findSkuByGoodsIds(List<String> goodsIds) {
        return goodsInfoRepository.findAll(GoodsInfoQueryRequest.builder().goodsIds(goodsIds).build().getWhereCriteria());
    }

    /**
     * 根据skuIdList获取Sku
     *
     * @param goodsInfoIds
     * @return
     */
    public List<GoodsInfo> findSkuByGoodsInfoIds(List<String> goodsInfoIds) {
        return goodsInfoRepository.findAll(GoodsInfoQueryRequest.builder().goodsInfoIds(goodsInfoIds).build().getWhereCriteria());
    }

    /**
     * 添加商品sku
     *
     * @param objs [sku商品]
     */
    public void addGoodsSku(Object... objs) {
        List<GoodsInfo> goodsInfos = (List<GoodsInfo>) objs[0];
        goodsInfos.stream().forEach(goodsInfo -> {
            try {
                Goods goods = goodsRepository.findById(goodsInfo.getGoodsId()).orElse(new Goods());
                goodsInfo.setCateId(goods.getCateId());
                goodsInfo.setBrandId(goods.getBrandId());
                GoodsInfoRequest goodsInfoReq = getGoodsInfoReq(goodsInfo, goods.getSubmitTime());
                resolver.resolveDestination(MQConstant.Q_ARES_GOODS_SKU_CREATE).send(new GenericMessage<>(JSONObject.toJSONString(goodsInfoReq)));
            } catch (Exception e) {
                log.error("商品埋点数据错误", e);
            }
        });
    }

    /**
     * 编辑多个商品sku
     *
     * @param objs [需要被添加的sku,需要被删除的skuId,需要被更新的sku]
     */
    public void editGoodsSku(Object... objs) {
        List<GoodsInfo> newGoodsInfos = (List<GoodsInfo>) objs[0];//需要被添加的sku
        addGoodsSku(newGoodsInfos);
        List<String> delInfoIds = (List<String>) objs[1];//需要被删除的skuId
        delGoodsSku(delInfoIds);
        List<GoodsInfo> oldGoodsInfos = (List<GoodsInfo>) objs[2];//需要被更新的sku
        oldGoodsInfos.stream().forEach(goodsInfo -> {
            Goods goods = goodsRepository.findById(goodsInfo.getGoodsId()).orElse(new Goods());
            goodsInfo.setCateId(goods.getCateId());
            goodsInfo.setBrandId(goods.getBrandId());
            GoodsInfoRequest goodsInfoReq = getGoodsInfoReq(goodsInfo, goods.getSubmitTime());
            resolver.resolveDestination(MQConstant.Q_ARES_GOODS_SKU_MODIFY).send(new GenericMessage<>(JSONObject.toJSONString(goodsInfoReq)));
        });
    }

    /**
     * 编辑单个商品sku
     *
     * @param objs [sku商品]
     */
    public void editOneGoodsSku(Object... objs) {
        GoodsInfo goodsInfo = (GoodsInfo) objs[0];
        Goods goods = goodsRepository.findById(goodsInfo.getGoodsId()).orElse(new Goods());
        goodsInfo.setCateId(goods.getCateId());
        goodsInfo.setBrandId(goods.getBrandId());
        GoodsInfoRequest goodsInfoReq = getGoodsInfoReq(goodsInfo, goods.getSubmitTime());
        resolver.resolveDestination(MQConstant.Q_ARES_GOODS_SKU_MODIFY).send(new GenericMessage<>(JSONObject.toJSONString(goodsInfoReq)));
    }

    /**
     * 获取发送给统计系统的商品sku全量信息
     *
     * @param goodsInfo sku商品
     */
    public GoodsInfoRequest getGoodsInfoReq(GoodsInfo goodsInfo, LocalDateTime submitTime) {
        GoodsInfoRequest goodsInfoRequest = new GoodsInfoRequest();
        goodsInfoRequest.setId(goodsInfo.getGoodsInfoId());
        goodsInfoRequest.setGoodsInfoName(goodsInfo.getGoodsInfoName());
        goodsInfoRequest.setGoodsId(goodsInfo.getGoodsId());
        goodsInfoRequest.setGoodsInfoNo(goodsInfo.getGoodsInfoNo());
        goodsInfoRequest.setAddedTime(goodsInfo.getAddedTime().toLocalDate());
        goodsInfoRequest.setAddedFlag(AddedFlag.YES.toValue() == goodsInfo.getAddedFlag().intValue());
        goodsInfoRequest.setAuditStatus(GOODS_CHECK_STAT_MAP.get(goodsInfo.getAuditStatus()));//枚举根据规则转换
        goodsInfoRequest.setSubmitTime(submitTime == null ? null : submitTime.toLocalDate());
        if (submitTime != null && goodsInfo.getAddedTime() != null) {
            // 哪个时间大取哪个时间作为可销售时间
            goodsInfoRequest.setSaleDate(submitTime.isAfter(goodsInfo.getAddedTime()) ? submitTime.toLocalDate() :
                    goodsInfo.getAddedTime().toLocalDate());
        }
        List<GoodsInfoSpecDetailRel> goodsInfoSpecDetailRels =
                goodsInfoSpecDetailRelRepository.findByGoodsId(goodsInfo.getGoodsId());
        goodsInfoRequest.setDetailName(StringUtils.join(goodsInfoSpecDetailRels.stream().filter(specDetailRel -> goodsInfo.getGoodsInfoId().equals(specDetailRel.getGoodsInfoId())).map(GoodsInfoSpecDetailRel::getDetailName).collect(Collectors.toList()), " "));
        goodsInfoRequest.setLeafCateId(goodsInfo.getCateId());
        goodsInfoRequest.setCateIds(getPareGoodsCatIds(goodsInfo.getCateId()));
        goodsInfoRequest.setBrandId(goodsInfo.getBrandId());
        goodsInfoRequest.setCompanyId(goodsInfo.getCompanyInfoId().toString());
        goodsInfoRequest.setOperationDate(goodsInfo.getCreateTime().toLocalDate());//创建时间 -- 操作时间
        goodsInfoRequest.setSendTime(LocalDateTime.now());
        goodsInfoRequest.setDelFlag(DeleteFlag.YES.equals(goodsInfo.getDelFlag()));
        List<StoreCateGoodsRela> relas =
                storeCateGoodsRelaRepository.selectByGoodsId(Collections.singletonList(goodsInfo.getGoodsId()));
        if (CollectionUtils.isNotEmpty(relas)) {
            List<Long> cateIdList = relas.stream().map(StoreCateGoodsRela::getStoreCateId).collect(Collectors.toList());
            goodsInfoRequest.setStoreCateIds(cateIdList);
        }
        return goodsInfoRequest;
    }

    /**
     * 根据初始分类标识,获取所有层级的分类标识List
     *
     * @param cateId 初始的分类标识
     * @return 所有层级的分类标识List
     */
    private List<Long> getPareGoodsCatIds(Long cateId) {
        List<Long> cateIds = new ArrayList<>();
        cateIds.add(cateId);
        getPareCateIdsRecursion(goodsCateRepository.findById(cateId).orElse(new GoodsCate()), cateIds);//递归处理所有父类名称List
        Collections.reverse(cateIds);//倒序
        return cateIds;
    }

    /**
     * 批量删除商品spu
     *
     * @param objs [删除的spu商品idList]
     */
    public void delGoodsSpu(Object... objs) {
        List<String> spuIdList = (List<String>) objs[0];
        resolver.resolveDestination(MQConstant.Q_ARES_GOODS_SPU_DELETE).send(new GenericMessage<>(JSONObject.toJSONString(spuIdList)));
    }

    /**
     * 批量删除商品sku
     *
     * @param objs [删除的sku商品idList]
     */
    public void delGoodsSku(Object... objs) {
        List<String> skuIdList = (List<String>) objs[0];
        resolver.resolveDestination(MQConstant.Q_ARES_GOODS_SKU_DELETE).send(new GenericMessage<>(JSONObject.toJSONString(skuIdList)));
    }

    /**
     * 批量修改商品sku上下架状态
     *
     * @param objs [上下架状态,sku商品idList]
     */
    public void editGoodsSpuUp(Object... objs) {
        Integer addedFlag = (Integer) objs[0];
        List<String> idList = (List<String>) objs[1];
        List<GoodsInfoRequest> goodsSkuUpList = getSkuIdList(idList).stream().map(id -> {
            GoodsInfoRequest goodsInfoRequest = new GoodsInfoRequest();
            goodsInfoRequest.setId(id);
            goodsInfoRequest.setAddedFlag(AddedFlag.YES.toValue() == addedFlag.intValue());

            GoodsInfo goodsInfo = goodsInfoRepository.findById(id).orElse(new GoodsInfo());
            Goods goods = goodsRepository.findById(goodsInfo.getGoodsId()).orElse(new Goods());
            goodsInfoRequest.setAddedTime(goodsInfo.getAddedTime().toLocalDate());
            if (goods.getSubmitTime() != null && goodsInfo.getAddedTime() != null) {
                // 哪个时间大取哪个时间作为可销售时间
                goodsInfoRequest.setSaleDate(goods.getSubmitTime().isAfter(goodsInfo.getAddedTime()) ?
                        goods.getSubmitTime().toLocalDate() : goodsInfo.getAddedTime().toLocalDate());
            }

            return goodsInfoRequest;
        }).collect(Collectors.toList());
        resolver.resolveDestination(MQConstant.Q_ARES_GOODS_SKU_UP).send(new GenericMessage<>(JSONObject.toJSONString(goodsSkuUpList)));
    }

    /**
     * 根据spuIdList获取skuIdList
     *
     * @param spuIdList
     * @return skuIdList
     */
    private List<String> getSkuIdList(List<String> spuIdList) {
        //查询并组装skuId列表
        return spuIdList.stream().flatMap(id -> {
            GoodsInfoQueryRequest infoQueryRequest = new GoodsInfoQueryRequest();
            infoQueryRequest.setGoodsId(id);
            infoQueryRequest.setDelFlag(DeleteFlag.NO.toValue());
            List<GoodsInfo> goodsInfos = goodsInfoRepository.findAll(infoQueryRequest.getWhereCriteria());
            return goodsInfos.stream();
        }).map(GoodsInfo::getGoodsInfoId).collect(Collectors.toList());
    }
}
