package com.wanmi.sbc.goods.cate.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.SortType;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.redis.CacheKeyConstant;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.goods.api.constant.GoodsCateErrorCode;
import com.wanmi.sbc.goods.api.request.cate.GoodsCateListCouponDetailRequest;
import com.wanmi.sbc.goods.api.request.cate.GoodsCateModifyRequest;
import com.wanmi.sbc.goods.ares.GoodsAresService;
import com.wanmi.sbc.goods.bean.dto.CouponInfoForScopeNamesDTO;
import com.wanmi.sbc.goods.bean.dto.CouponMarketingScopeDTO;
import com.wanmi.sbc.goods.bean.enums.*;
import com.wanmi.sbc.goods.bean.vo.CouponInfoForScopeNamesVO;
import com.wanmi.sbc.goods.bean.vo.GoodsCateShenceBurialSiteVO;
import com.wanmi.sbc.goods.bean.vo.GoodsCateVO;
import com.wanmi.sbc.goods.brand.entity.ContractBrandBase;
import com.wanmi.sbc.goods.brand.entity.GoodsBrandBase;
import com.wanmi.sbc.goods.brand.service.ContractBrandService;
import com.wanmi.sbc.goods.brand.service.GoodsBrandService;
import com.wanmi.sbc.goods.cate.entity.GoodsCateBase;
import com.wanmi.sbc.goods.cate.model.root.ContractCate;
import com.wanmi.sbc.goods.cate.model.root.GoodsCate;
import com.wanmi.sbc.goods.cate.repository.GoodsCateRepository;
import com.wanmi.sbc.goods.cate.request.ContractCateQueryRequest;
import com.wanmi.sbc.goods.cate.request.GoodsCateQueryRequest;
import com.wanmi.sbc.goods.cate.request.GoodsCateSaveRequest;
import com.wanmi.sbc.goods.cate.request.GoodsCateSortRequest;
import com.wanmi.sbc.goods.info.reponse.GoodsInfoResponse;
import com.wanmi.sbc.goods.info.repository.GoodsInfoRepository;
import com.wanmi.sbc.goods.info.repository.GoodsRepository;
import com.wanmi.sbc.goods.info.request.GoodsInfoQueryRequest;
import com.wanmi.sbc.goods.info.request.GoodsQueryRequest;
import com.wanmi.sbc.goods.info.service.GoodsInfoService;
import com.wanmi.sbc.goods.redis.RedisService;
import com.wanmi.sbc.goods.storecate.entity.StoreCateBase;
import com.wanmi.sbc.goods.storecate.service.StoreCateService;
import com.wanmi.sbc.setting.api.provider.SystemGrowthValueConfigQueryProvider;
import com.wanmi.sbc.setting.api.response.SystemGrowthValueConfigQueryResponse;
import com.wanmi.sbc.setting.bean.enums.GrowthValueRule;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 商品分类服务
 * Created by daiyitian on 2017/4/11.
 */
@Service
@Transactional(readOnly = true, timeout = 10)
public class GoodsCateService {

    private final String SPLIT_CHAR = "|";
    //上级类目为2，则本级为3级类目
    private final int PARENT_GRADE = 2;

    @Autowired
    GoodsAresService goodsAresService;

    @Autowired
    private GoodsCateRepository goodsCateRepository;

    @Autowired
    private GoodsRepository goodsRepository;

    @Resource
    private GoodsInfoRepository goodsInfoRepository;

    @Autowired
    private RedisService redisService;

    @Autowired
    private ContractCateService contractCateService;

    @Autowired
    private SystemGrowthValueConfigQueryProvider systemGrowthValueConfigQueryProvider;

    @Autowired
    private ContractBrandService contractBrandService;

    @Autowired
    private GoodsBrandService goodsBrandService;

    @Autowired
    private StoreCateService storeCateService;

    @Autowired
    private GoodsInfoService goodsInfoService;


    /**
     * 条件查询商品分类
     *
     * @param request 参数
     * @return list
     */
    public List<GoodsCate> query(GoodsCateQueryRequest request) {
        Sort sort = request.getSort();
        List<GoodsCate> list;
        if (Objects.nonNull(sort)) {
            list = goodsCateRepository.findAll(request.getWhereCriteria(), sort);
        } else {
            list = goodsCateRepository.findAll(request.getWhereCriteria());
        }
        return ListUtils.emptyIfNull(list);
    }

    /**
     * 根据ID查询商品分类
     *
     * @param cateId 分类ID
     * @return list
     */
    public GoodsCate findById(Long cateId) {
        return goodsCateRepository.findById(cateId).orElse(null);
    }


    /**
     * 根据ID批量查询商品分类
     *
     * @param cateIds 多个分类ID
     * @return list
     */
    public List<GoodsCate> findByIds(List<Long> cateIds) {
        return goodsCateRepository.findAll(GoodsCateQueryRequest.builder().cateIds(cateIds).build().getWhereCriteria());
    }

    /**
     * 新增商品分类
     *
     * @param saveRequest 商品分类
     * @throws SbcRuntimeException
     */
    @Transactional
    public GoodsCate add(GoodsCateSaveRequest saveRequest) throws SbcRuntimeException {
        GoodsCate goodsCate = saveRequest.getGoodsCate();
        if (Objects.isNull(goodsCate.getCateParentId())) {
            goodsCate.setCateParentId((long) CateParentTop.ZERO.toValue());
        }

        //验证重复名称
        GoodsCateQueryRequest queryRequest = new GoodsCateQueryRequest();
        queryRequest.setCateParentId(goodsCate.getCateParentId());
        queryRequest.setCateName(goodsCate.getCateName());
        queryRequest.setDelFlag(DeleteFlag.NO.toValue());
        if (goodsCateRepository.count(queryRequest.getWhereCriteria()) > 0) {
            throw new SbcRuntimeException(GoodsCateErrorCode.NAME_ALREADY_EXIST);
        }

        //验证在同一父类下限制子类数
        queryRequest.setCateName(null);
        queryRequest.setCateParentId(goodsCate.getCateParentId());
        if (goodsCateRepository.count(queryRequest.getWhereCriteria()) >= Constants.GOODSCATE_MAX_SIZE) {
            if (Objects.equals(CateParentTop.ZERO.toValue(), goodsCate.getCateParentId().intValue())) {
                throw new SbcRuntimeException(GoodsCateErrorCode.LEVEL_ONE_MAX,
                        new Object[]{Constants.GOODSCATE_MAX_SIZE});
            }
            throw new SbcRuntimeException(GoodsCateErrorCode.CHILD_CATE_MAX,
                    new Object[]{Constants.GOODSCATE_MAX_SIZE});
        }

        goodsCate.setDelFlag(DeleteFlag.NO);
        goodsCate.setIsDefault(DefaultFlag.NO);
        goodsCate.setCreateTime(LocalDateTime.now());
        goodsCate.setUpdateTime(LocalDateTime.now());
        goodsCate.setCateGrade(1);
        if (goodsCate.getSort() == null) {
            goodsCate.setSort(0);//默认排序为0
        }
        if (Objects.isNull(goodsCate.getCateRate())) {
            goodsCate.setCateRate(BigDecimal.ZERO);
        }
        if (Objects.isNull(goodsCate.getGrowthValueRate())) {
            goodsCate.setGrowthValueRate(BigDecimal.ZERO);
        }
        if (Objects.isNull(goodsCate.getIsParentGrowthValueRate())) {
            goodsCate.setIsParentGrowthValueRate(DefaultFlag.YES);
        }
        if (Objects.isNull(goodsCate.getPointsRate())) {
            goodsCate.setPointsRate(BigDecimal.ZERO);
        }
        if (Objects.isNull(goodsCate.getIsParentPointsRate())) {
            goodsCate.setIsParentPointsRate(DefaultFlag.YES);
        }
        //填充分类路径，获取父类的分类路径进行拼凑,例01|001|0001
        String catePath = String.valueOf(CateParentTop.ZERO.toValue());
        if (!Objects.equals(goodsCate.getCateParentId().intValue(), CateParentTop.ZERO.toValue())) {
            GoodsCate parentGoodsCate = goodsCateRepository.findById(goodsCate.getCateParentId()).orElse(null);
            if (Objects.isNull(parentGoodsCate) || Objects.equals(parentGoodsCate.getDelFlag(), DeleteFlag.YES)) {
                throw new SbcRuntimeException(GoodsCateErrorCode.PARENT_CATE_NOT_EXIST);
            }
            catePath = parentGoodsCate.getCatePath().concat(String.valueOf(parentGoodsCate.getCateId()));
            goodsCate.setCateGrade(parentGoodsCate.getCateGrade() + 1);
            //使用上级类目扣率
            if (DefaultFlag.YES.equals(goodsCate.getIsParentCateRate())) {
                if (Objects.nonNull(parentGoodsCate.getCateRate())) {
                    goodsCate.setCateRate(parentGoodsCate.getCateRate());
                }
            } else {
                //如果该类目是3级类目 则扣率不允许为空
                if (Objects.isNull(goodsCate.getCateRate())) {
                    if (Objects.equals(PARENT_GRADE, parentGoodsCate.getCateGrade())) {
                        throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
                    } else {
                        goodsCate.setCateRate(BigDecimal.ZERO);
                    }
                }
            }
        }
        goodsCate.setCatePath(catePath.concat(SPLIT_CHAR));
        goodsCate = goodsCateRepository.saveAndFlush(goodsCate);
        //生成缓存
        this.fillRedis();

        //ares埋点-商品-后台添加商品分类
        goodsAresService.dispatchFunction("addGoodsCate", goodsCate);
        return goodsCate;
    }

    /**
     * 编辑商品分类
     *
     * @param newGoodsCate 商品分类
     * @throws SbcRuntimeException
     */
    @Transactional
    public List<GoodsCate> edit(GoodsCateModifyRequest newGoodsCate) throws SbcRuntimeException {
        GoodsCate oldGoodsCate = goodsCateRepository.findById(newGoodsCate.getCateId()).orElse(null);
        if (Objects.isNull(oldGoodsCate) || Objects.equals(oldGoodsCate.getDelFlag(), DeleteFlag.YES)) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        if (Objects.isNull(newGoodsCate.getCateParentId()) || Objects.equals(newGoodsCate.getCateParentId().intValue(), 0)) {
            newGoodsCate.setCateParentId((long) CateParentTop.ZERO.toValue());
            newGoodsCate.setCateGrade(1);
        }
        if (newGoodsCate.getCateRate() == null) {
            newGoodsCate.setCateRate(oldGoodsCate.getCateRate());
            newGoodsCate.setIsParentCateRate(oldGoodsCate.getIsParentCateRate());
        }
        if (newGoodsCate.getGrowthValueRate() == null) {
            newGoodsCate.setGrowthValueRate(oldGoodsCate.getGrowthValueRate());
            newGoodsCate.setIsParentGrowthValueRate(oldGoodsCate.getIsParentGrowthValueRate());
        }
        if (newGoodsCate.getPointsRate() == null) {
            newGoodsCate.setPointsRate(oldGoodsCate.getPointsRate());
            newGoodsCate.setIsParentPointsRate(oldGoodsCate.getIsParentPointsRate());
        }
        if (newGoodsCate.getSort() == null) {
            newGoodsCate.setSort(0);//默认排序为0
        }

        //验证重复名称
        GoodsCateQueryRequest queryRequest = new GoodsCateQueryRequest();
        queryRequest.setCateParentId(oldGoodsCate.getCateParentId());
        queryRequest.setCateName(newGoodsCate.getCateName());
        queryRequest.setDelFlag(DeleteFlag.NO.toValue());
        queryRequest.setNotCateId(newGoodsCate.getCateId());//除了自已的分类以外
        if (goodsCateRepository.count(queryRequest.getWhereCriteria()) > 0) {
            throw new SbcRuntimeException(GoodsCateErrorCode.NAME_ALREADY_EXIST);
        }

        //验证在同一父类限制子类数
        queryRequest.setCateName(null);
        queryRequest.setCateParentId(newGoodsCate.getCateParentId());
        if (goodsCateRepository.count(queryRequest.getWhereCriteria()) >= Constants.GOODSCATE_MAX_SIZE) {
            if (Objects.equals(CateParentTop.ZERO.toValue(), newGoodsCate.getCateParentId().intValue())) {
                throw new SbcRuntimeException(GoodsCateErrorCode.LEVEL_ONE_MAX,
                        new Object[]{Constants.GOODSCATE_MAX_SIZE});
            }
            throw new SbcRuntimeException(GoodsCateErrorCode.CHILD_CATE_MAX,
                    new Object[]{Constants.GOODSCATE_MAX_SIZE});
        }

        //填充分类路径，获取父类的分类路径进行拼凑,例01|001|0001
        String catePath = String.valueOf(CateParentTop.ZERO.toValue()).concat(SPLIT_CHAR);
        if (!Objects.equals(newGoodsCate.getCateParentId().intValue(), CateParentTop.ZERO.toValue())) {
            GoodsCate parentGoodsCate = goodsCateRepository.findById(newGoodsCate.getCateParentId()).orElse(null);
            if (Objects.isNull(parentGoodsCate) || Objects.equals(parentGoodsCate.getDelFlag(), DeleteFlag.YES)) {
                throw new SbcRuntimeException(GoodsCateErrorCode.PARENT_CATE_NOT_EXIST);
            }
            catePath =
                    parentGoodsCate.getCatePath().concat(String.valueOf(parentGoodsCate.getCateId())).concat(SPLIT_CHAR);
            newGoodsCate.setCateGrade(parentGoodsCate.getCateGrade() + 1);
        }
        newGoodsCate.setCatePath(catePath);

        List<GoodsCate> updateToEs = new ArrayList<>();

        //如果分类路径有变化，将所有子类进行更新路径
        if (!catePath.equals(oldGoodsCate.getCatePath())) {
            final String newCatePath = catePath.concat(String.valueOf(oldGoodsCate.getCateId())).concat(SPLIT_CHAR);
            GoodsCateQueryRequest request = new GoodsCateQueryRequest();
            request.setLikeCatePath(oldGoodsCate.getCatePath().concat(String.valueOf(oldGoodsCate.getCateId())).concat(SPLIT_CHAR));
            List<GoodsCate> goodsCateList = goodsCateRepository.findAll(request.getWhereCriteria());
            if (CollectionUtils.isNotEmpty(goodsCateList)) {
                goodsCateList.stream().forEach(goodsCate -> {
                    goodsCate.setCatePath(goodsCate.getCatePath().replace(request.getLikeCatePath(), newCatePath));
                    goodsCate.setCateGrade(goodsCate.getCatePath().split("\\" + SPLIT_CHAR).length - 1);
                    goodsCate.setUpdateTime(LocalDateTime.now());
                });
            }
            goodsCateRepository.saveAll(goodsCateList);
//            updateToEs.addAll(goodsCateList);
        }

        //如果扣率有变化
        if (!Objects.equals(newGoodsCate.getCateRate(), oldGoodsCate.getCateRate())) {
            //查出该分类下所有使用上级类目扣率的子类
            List<GoodsCate> goodsCates = this.setChildCateRate(oldGoodsCate, newGoodsCate.getCateRate());
            if (CollectionUtils.isNotEmpty(goodsCates)) {
                goodsCateRepository.saveAll(goodsCates);
                contractCateService.updateCateRate(newGoodsCate.getCateRate(),
                        goodsCates.stream().map(goodsCate -> goodsCate.getCateId()).collect(Collectors.toList()));
//                updateToEs.addAll(goodsCates);
            }
        }

        //如果成长值获取比例有变化
        if (!Objects.equals(newGoodsCate.getGrowthValueRate(), oldGoodsCate.getGrowthValueRate())) {
            //查出该分类下所有使用上级类目成长值获取比例的子类
            List<GoodsCate> goodsCates = this.setChildGrowthValueRate(oldGoodsCate, newGoodsCate.getGrowthValueRate());
            if (CollectionUtils.isNotEmpty(goodsCates)) {
                goodsCateRepository.saveAll(goodsCates);
//                updateToEs.addAll(goodsCates);
            }
        }

        //如果积分获取比例有变化
        if (!Objects.equals(newGoodsCate.getPointsRate(), oldGoodsCate.getPointsRate())) {
            //查出该分类下所有使用上级类目积分获取比例的子类
            List<GoodsCate> goodsCates = this.setChildPointsRate(oldGoodsCate, newGoodsCate.getPointsRate());
            if (CollectionUtils.isNotEmpty(goodsCates)) {
                goodsCateRepository.saveAll(goodsCates);
//                updateToEs.addAll(goodsCates);
            }
        }

        //如果积分获取比例有变化
        if (!Objects.equals(newGoodsCate.getCateName(), oldGoodsCate.getCateName())) {
            oldGoodsCate.setCateName(newGoodsCate.getCateName());
            updateToEs.add(oldGoodsCate);
        }

        if (!Objects.equals(newGoodsCate.getGrowthValueRate(), oldGoodsCate.getGrowthValueRate())) {
            //查出该分类下所有使用上级类目成长值获取比例的子类
            List<GoodsCate> goodsCates = this.setChildGrowthValueRate(oldGoodsCate, newGoodsCate.getGrowthValueRate());
            if (CollectionUtils.isNotEmpty(goodsCates)) {
                goodsCateRepository.saveAll(goodsCates);
            }
        }

        //更新分类
        newGoodsCate.setUpdateTime(LocalDateTime.now());
        KsBeanUtil.copyProperties(newGoodsCate, oldGoodsCate);
        goodsCateRepository.save(oldGoodsCate);

        //判断是否是同步积分规则
        SystemGrowthValueConfigQueryResponse systemGrowthValueConfigQueryResponse =
                systemGrowthValueConfigQueryProvider.querySystemGrowthValueConfig().getContext();
        if (GrowthValueRule.SYNCHRONIZE_POINTS.equals(systemGrowthValueConfigQueryResponse.getRule())) {
            goodsCateRepository.updateGrowthValueRuleByPoints();
        }

//        //持久化ES->CateBrand
//        updateToEs.add(oldGoodsCate);
//        Map<Long, GoodsCate> goodsCateMap = updateToEs.stream().collect(Collectors.toMap(GoodsCate::getCateId,
//        goodsCate -> goodsCate));
//        Iterable<EsCateBrand> esCateBrands = esCateBrandService.queryCateBrandByCateIds(updateToEs.stream().map
//        (GoodsCate::getCateId).collect(Collectors.toList()));
//        List<EsCateBrand> cateBrandList = new ArrayList<>();
//        esCateBrands.forEach(cateBrand -> {
//            cateBrand.setGoodsCate(goodsCateMap.get(cateBrand.getGoodsCate().getCateId()));
//            cateBrandList.add(cateBrand);
//        });
//        if (CollectionUtils.isNotEmpty(cateBrandList)) {
//            esCateBrandService.save(cateBrandList);
//        }

        //生成缓存
        this.fillRedis();

        //ares埋点-商品-后台编辑商品分类
        goodsAresService.dispatchFunction("editGoodsCate", oldGoodsCate);

        return updateToEs;
    }

    /**
     * 删除商品分类
     *
     * @param cateId 分类编号
     * @throws SbcRuntimeException
     */
    @Transactional
    public List<Long> delete(Long cateId) throws SbcRuntimeException {
        GoodsCate goodsCate = goodsCateRepository.findById(cateId).orElse(null);
        if (goodsCate == null || goodsCate.getDelFlag().compareTo(DeleteFlag.YES) == 0) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }

        //查询默认分类
        GoodsCateQueryRequest request = new GoodsCateQueryRequest();
        request.setIsDefault(DefaultFlag.YES.toValue());
        List<GoodsCate> goodsCateList = goodsCateRepository.findAll(request.getWhereCriteria());
        //如果默认分类不存在，不允许删除
        if (CollectionUtils.isEmpty(goodsCateList)) {
            throw new SbcRuntimeException(GoodsCateErrorCode.DEFAULT_CATE_NOT_EXIST);
        }

        List<Long> allCate = new ArrayList<>();
        allCate.add(goodsCate.getCateId());

        String oldCatePath = goodsCate.getCatePath().concat(String.valueOf(goodsCate.getCateId())).concat(SPLIT_CHAR);
        //将所有子类也更新为删除
        request.setIsDefault(null);
        request.setLikeCatePath(oldCatePath);
        List<GoodsCate> childCateList = goodsCateRepository.findAll(request.getWhereCriteria());
        if (CollectionUtils.isNotEmpty(childCateList)) {
            childCateList.stream().forEach(cate -> {
                cate.setDelFlag(DeleteFlag.YES);
                allCate.add(cate.getCateId());
            });
            goodsCateRepository.saveAll(childCateList);
        }
        //更新分类
        goodsCate.setDelFlag(DeleteFlag.YES);
        goodsCateRepository.save(goodsCate);

        //持久化ES->CateBrand
        List<Long> delIds = childCateList.stream().map(GoodsCate::getCateId).collect(Collectors.toList());
        delIds.add(goodsCate.getCateId());
//        Iterable<EsCateBrand> esCateBrands = esCateBrandService.queryCateBrandByCateIds(delIds);
//        List<EsCateBrand> cateBrandList = new ArrayList<>();
//        esCateBrands.forEach(cateBrand -> {
//            cateBrand.setGoodsCate(goodsCateList.get(0));
//            cateBrandList.add(cateBrand);
//        });
//        if (CollectionUtils.isNotEmpty(cateBrandList)) {
//            esCateBrandService.save(cateBrandList);
//        }

        // 将spu关联分类迁移至默认分类
        goodsRepository.updateCateByCateIds(goodsCateList.get(0).getCateId(), allCate);
        //将sku关联分类迁移至默认分类
        goodsInfoRepository.updateSKUCateByCateIds(goodsCateList.get(0).getCateId(), allCate);

        //生成缓存
        this.fillRedis();

        //ares埋点-商品-后台删除商品分类
        goodsAresService.dispatchFunction("delGoodsCate", allCate);

        return delIds;
    }

    /**
     * 拖拽排序
     *
     * @param goodsCateList
     */
    @Transactional
    public void dragSort(List<GoodsCateSortRequest> goodsCateList) {
        if (CollectionUtils.isEmpty(goodsCateList)) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        if (CollectionUtils.isNotEmpty(goodsCateList) && goodsCateList.size() > Constants.GOODSCATE_MAX_SIZE) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        goodsCateList.forEach(cate ->
                goodsCateRepository.updateCateSort(cate.getCateId(),
                        cate.getCateSort()));
        //生成缓存
        fillRedis();
    }

    /**
     * 同步成长值购物规则为积分购物规则
     */
    @Transactional
    public void synchronizePointsRule() {
        goodsCateRepository.updateGrowthValueRuleByPoints();
    }

    /**
     * 验证是否有子类
     *
     * @param cateId
     */
    public Integer checkChild(Long cateId) {
        GoodsCate cate = goodsCateRepository.findById(cateId).orElse(null);
        if (cate == null || cate.getDelFlag().compareTo(DeleteFlag.YES) == 0) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }

        String oldCatePath = cate.getCatePath().concat(String.valueOf(cate.getCateId()).concat(SPLIT_CHAR));
        if (goodsCateRepository.count(GoodsCateQueryRequest.builder().delFlag(DeleteFlag.NO.toValue()).likeCatePath(oldCatePath).build().getWhereCriteria()) > 0) {
            return Constants.yes;
        }
        return Constants.no;
    }

    /**
     * 验证是否有图片
     *
     * @param cateId
     */
    public Integer checkGoods(Long cateId) {
        GoodsCate cate = goodsCateRepository.findById(cateId).orElse(null);
        if (cate == null || cate.getDelFlag().compareTo(DeleteFlag.YES) == 0) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }

        List<Long> allCate = new ArrayList<>();
        allCate.add(cate.getCateId());
        String oldCatePath = cate.getCatePath().concat(String.valueOf(cate.getCateId()).concat(SPLIT_CHAR));
        List<GoodsCate> childCateList =
                goodsCateRepository.findAll(GoodsCateQueryRequest.builder().delFlag(DeleteFlag.NO.toValue()).likeCatePath(oldCatePath).build().getWhereCriteria());
        if (CollectionUtils.isNotEmpty(childCateList)) {
            childCateList.stream().forEach(c -> {
                allCate.add(c.getCateId());
            });
        }

        if (goodsRepository.count(GoodsQueryRequest.builder().delFlag(DeleteFlag.NO.toValue()).cateIds(allCate).build().getWhereCriteria()) > 0) {
            return Constants.yes;
        }
        return Constants.no;
    }

    /**
     * 初始化分类，生成默认分类
     */
    @Transactional
    public void init() {
        GoodsCateQueryRequest request = new GoodsCateQueryRequest();
        request.setIsDefault(DefaultFlag.YES.toValue());
        List<GoodsCate> goodsCateList = goodsCateRepository.findAll(request.getWhereCriteria());
        if (CollectionUtils.isEmpty(goodsCateList)) {
            GoodsCate goodsCate = new GoodsCate();
            goodsCate.setCateName("默认分类");
            goodsCate.setCateParentId((long) (CateParentTop.ZERO.toValue()));
            goodsCate.setIsDefault(DefaultFlag.YES);
            goodsCate.setDelFlag(DeleteFlag.NO);
            goodsCate.setCateGrade(0);
            goodsCate.setCateRate(BigDecimal.valueOf(0.00));
            goodsCate.setIsParentCateRate(DefaultFlag.NO);
            goodsCate.setCatePath(String.valueOf(goodsCate.getCateParentId()).concat(this.SPLIT_CHAR));
            goodsCate.setCreateTime(LocalDateTime.now());
            goodsCate.setUpdateTime(goodsCate.getCreateTime());
            goodsCate.setSort(0);
            goodsCateRepository.save(goodsCate);

            //ares埋点-商品-后台添加商品分类
            goodsAresService.dispatchFunction("addGoodsCate", goodsCate);
        }
        //生成缓存
        this.fillRedis();
    }

    /**
     * 获取商品分类树形JOSN（缓存级）
     *
     * @return
     */
    public String getGoodsCatesByCache() {
        if (redisService.hasKey(CacheKeyConstant.GOODS_CATE_KEY)) {
            return redisService.getString(CacheKeyConstant.GOODS_CATE_KEY);
        }

        //生成缓存
        fillRedis();
        return redisService.getString(CacheKeyConstant.GOODS_CATE_KEY);
    }

    /**
     * 生成JSON字符串缓存Redis
     */
    public void fillRedis() {
        redisService.setString(CacheKeyConstant.GOODS_CATE_KEY, JSON.toJSONString(queryGoodsCate(),
                SerializerFeature.DisableCircularReferenceDetect));
    }

    /**
     * 使用上级类目扣率时，相关类目扣率会随着上级类目扣率的变化而变化
     *
     * @param oldGoodsCate
     */
    private List<GoodsCate> setChildCateRate(GoodsCate oldGoodsCate, BigDecimal cateRate) {
        List<GoodsCate> goodsCates = new ArrayList<>();
        GoodsCateQueryRequest request = new GoodsCateQueryRequest();
        request.setCateParentId(oldGoodsCate.getCateId());
        request.setDelFlag(DeleteFlag.NO.toValue());
        List<GoodsCate> goodsCateList = goodsCateRepository.findAll(request.getWhereCriteria());
        if (CollectionUtils.isNotEmpty(goodsCateList)) {
            goodsCateList.stream().filter(goodsCate -> Objects.equals(goodsCate.getIsParentCateRate(), DefaultFlag.YES))
                    .forEach(goodsCate -> {
                        goodsCate.setCateRate(cateRate);
                        goodsCates.add(goodsCate);
                        this.setChildCateRate(goodsCate, cateRate);
                    });
        }
        return goodsCates;
    }

    /**
     * 使用上级类目成长值获取比例时，相关类目成长值获取比例会随着上级类目成长值获取比例的变化而变化
     *
     * @param oldGoodsCate
     */
    private List<GoodsCate> setChildGrowthValueRate(GoodsCate oldGoodsCate, BigDecimal growthValueRate) {
        List<GoodsCate> goodsCates = new ArrayList<>();
        GoodsCateQueryRequest request = new GoodsCateQueryRequest();
        request.setCateParentId(oldGoodsCate.getCateId());
        request.setDelFlag(DeleteFlag.NO.toValue());
        List<GoodsCate> goodsCateList = goodsCateRepository.findAll(request.getWhereCriteria());
        if (CollectionUtils.isNotEmpty(goodsCateList)) {
            goodsCateList.stream().filter(goodsCate -> Objects.equals(goodsCate.getIsParentGrowthValueRate(),
                    DefaultFlag.YES))
                    .forEach(goodsCate -> {
                        goodsCate.setGrowthValueRate(growthValueRate);
                        goodsCates.add(goodsCate);
                        this.setChildGrowthValueRate(goodsCate, growthValueRate);
                    });
        }
        return goodsCates;
    }

    /**
     * 使用上级类目积分获取比例时，相关类目积分获取比例会随着上级类目积分获取比例的变化而变化
     *
     * @param oldGoodsCate
     */
    private List<GoodsCate> setChildPointsRate(GoodsCate oldGoodsCate, BigDecimal pointRate) {
        List<GoodsCate> goodsCates = new ArrayList<>();
        GoodsCateQueryRequest request = new GoodsCateQueryRequest();
        request.setCateParentId(oldGoodsCate.getCateId());
        request.setDelFlag(DeleteFlag.NO.toValue());
        List<GoodsCate> goodsCateList = goodsCateRepository.findAll(request.getWhereCriteria());
        if (CollectionUtils.isNotEmpty(goodsCateList)) {
            goodsCateList.stream().filter(goodsCate -> Objects.equals(goodsCate.getIsParentPointsRate(),
                    DefaultFlag.YES))
                    .forEach(goodsCate -> {
                        goodsCate.setPointsRate(pointRate);
                        goodsCates.add(goodsCate);
                        this.setChildPointsRate(goodsCate, pointRate);
                    });
        }
        return goodsCates;
    }

    /**
     * 获取所有子分类
     *
     * @param goodCateId 分类
     * @return 所有子分类
     */
    public List<Long> getChlidCateId(Long goodCateId) {
        List<Long> cateIds = new ArrayList<>();
        GoodsCate cate = goodsCateRepository.findById(goodCateId).orElse(null);
        if (Objects.nonNull(cate)) {
            GoodsCateQueryRequest cateRequest = new GoodsCateQueryRequest();
            cateRequest.setLikeCatePath(ObjectUtils.toString(cate.getCatePath()).concat(String.valueOf(cate.getCateId())).concat(this.SPLIT_CHAR));
            List<GoodsCate> t_cateList = goodsCateRepository.findAll(cateRequest.getWhereCriteria());
            if (CollectionUtils.isNotEmpty(t_cateList)) {
                cateIds.addAll(t_cateList.stream().map(GoodsCate::getCateId).collect(Collectors.toList()));
            }
        }
        return cateIds;
    }

    /**
     * 根据店铺获取叶子分类列表
     *
     * @param storeId 店铺
     * @return 叶子分类列表
     */
    public List<GoodsCate> queryLeafByStoreId(Long storeId) {
        ContractCateQueryRequest cateQueryRequest = new ContractCateQueryRequest();
        cateQueryRequest.setStoreId(storeId);
        List<Long> cateIds = contractCateService.queryContractCateList(cateQueryRequest)
                .stream().map(ContractCate::getGoodsCate).map(GoodsCate::getCateId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(cateIds)) {
            return Collections.emptyList();
        }
        return goodsCateRepository.queryLeaf(cateIds);
    }

    /**
     * 根据店铺获取叶子分类列表
     *
     * @return 叶子分类列表
     */
    public List<GoodsCate> queryLeaf() {
        return goodsCateRepository.queryLeaf();
    }

    /**
     * 查询有效分类平台分类（有3级分类的）
     *
     * @return
     */
    public List<GoodsCate> queryGoodsCate() {
        GoodsCateQueryRequest request = new GoodsCateQueryRequest();
        request.setDelFlag(DeleteFlag.NO.toValue());
        request.putSort("isDefault", SortType.DESC.toValue());
        request.putSort("sort", SortType.ASC.toValue());
        request.putSort("createTime", SortType.DESC.toValue());
        List<GoodsCate> goodsCateList;
        Sort sort = request.getSort();
        if(Objects.nonNull(sort)) {
            goodsCateList = goodsCateRepository.findAll(request.getWhereCriteria(), sort);
        }else {
            goodsCateList = goodsCateRepository.findAll(request.getWhereCriteria());
        }

        List<GoodsCate> treeList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(goodsCateList)) {
            treeList = this.recursiveTree(goodsCateList, (long) (CateParentTop.ZERO.toValue()));
            List<GoodsCate> tpList = new ArrayList<>();
            List<GoodsCate> chList = new ArrayList<>();
            for (GoodsCate t : treeList) {
                List<GoodsCate> cates = t.getGoodsCateList();
                for (GoodsCate i : cates) {
                    if (i.getGoodsCateList().isEmpty()) {
                        chList.add(i);
                    }
                }
                cates.removeAll(chList);
                if (cates.isEmpty()) {
                    tpList.add(t);
                }
            }
            treeList.removeAll(tpList);
        }
        return treeList;
    }

    //递归->树形结构
    private List<GoodsCate> recursiveTree(List<GoodsCate> source, Long parentId) {
        List<GoodsCate> res = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(source)) {
            source.stream().filter(goodsCate -> Objects.equals(parentId, goodsCate.getCateParentId())).forEach(goodsCate -> {
                goodsCate.setGoodsCateList(recursiveTree(source, goodsCate.getCateId()));
                res.add(goodsCate);
            });
        }
        return res;
    }


    /**
     * 神策埋点获取一二三级分类名称
     */
    public List<GoodsCateShenceBurialSiteVO> listGoodsCateShenceBurialSite(List<Long> cateIdList){
        List<GoodsCateShenceBurialSiteVO> goodsCateShenceBurialSiteVOList=new ArrayList<>();
        List<GoodsCate> goodsCateList= goodsCateRepository.queryCates(cateIdList,DeleteFlag.NO);
          for (GoodsCate cate:goodsCateList){
              GoodsCateShenceBurialSiteVO goodsCateShenceBurialSiteVO=new GoodsCateShenceBurialSiteVO();
               //三级分类
               goodsCateShenceBurialSiteVO.setThreeLevelGoodsCate(KsBeanUtil.convert(cate,GoodsCateVO.class));
              //二级分类
              if(Objects.nonNull(cate) || Objects.nonNull(cate.getCateId())){
                  GoodsCate cateSecond=goodsCateRepository.queryCatesShence(cate.getCateParentId());
                   goodsCateShenceBurialSiteVO.setSecondLevelGoodsCate(KsBeanUtil.convert(cateSecond,GoodsCateVO.class));
                   if(Objects.nonNull(cateSecond) || Objects.nonNull(cateSecond.getCateId())){
                      //一级分类
                      GoodsCate goodsCateOne=goodsCateRepository.queryCatesShence(cateSecond.getCateParentId());
                      goodsCateShenceBurialSiteVO.setOneLevelGoodsCate(KsBeanUtil.convert(goodsCateOne,GoodsCateVO.class));
                  }
              }
              goodsCateShenceBurialSiteVOList.add(goodsCateShenceBurialSiteVO);
           }
          return goodsCateShenceBurialSiteVOList;
    }

    /**
     * 获取所有子分类
     *
     * @param goodCateId 分类
     * @return 所有子分类
     */
    public List<Long> getChlidCateIdNew(Long goodCateId) {
        List<Long> cateIds = new ArrayList<>();
        GoodsCateBase cate = goodsCateRepository.queryBaseCatesById(goodCateId);
        if (Objects.nonNull(cate)) {
            GoodsCateQueryRequest cateRequest = new GoodsCateQueryRequest();
            cateRequest.setLikeCatePath(ObjectUtils.toString(cate.getCatePath()).concat(String.valueOf(cate.getCateId())).concat(this.SPLIT_CHAR));
            List<GoodsCate> t_cateList = goodsCateRepository.findAll(cateRequest.getWhereCriteria());
            if (CollectionUtils.isNotEmpty(t_cateList)) {
                cateIds.addAll(t_cateList.stream().map(GoodsCate::getCateId).collect(Collectors.toList()));
            }
        }
        return cateIds;
    }

    public List<CouponInfoForScopeNamesVO> copyToCouponInfoVoNew(GoodsCateListCouponDetailRequest request){
        List<CouponInfoForScopeNamesDTO> couponInfoList = request.getCouponInfoForScopeNamesDTOS();
        Map<String, List<CouponMarketingScopeDTO>> couponMarketingScopeMap = request.getCouponMarketingScopeMap();
        Map<String,List<String>> scopeIdsMap = request.getScopeIdsMap();
        return couponInfoList.stream().map(couponInfo -> {
            CouponInfoForScopeNamesVO vo = new CouponInfoForScopeNamesVO();
            vo.setCouponId(couponInfo.getCouponId());
            if (ScopeType.SKU != couponInfo.getScopeType() && CouponType.FREIGHT_VOUCHER != couponInfo.getCouponType()) {
//                        this.couponDetail(couponInfoResponse, couponInfo,couponMarketingScopeMap.get(couponInfo.getCouponId()));
                List<CouponMarketingScopeDTO> scopeList = new ArrayList<>();
                List<String> scopeIdList = new ArrayList<>();
                if (MapUtils.isNotEmpty(couponMarketingScopeMap)) {
                    scopeList = couponMarketingScopeMap.get(couponInfo.getCouponId());
                }else{
                    scopeIdList = scopeIdsMap.get(couponInfo.getCouponId());
                }
//                        if (CouponType.FREIGHT_VOUCHER == couponInfo.getCouponType()) {
                // return;
//                        }
                //品牌分类
                if (ScopeType.BRAND.equals(couponInfo.getScopeType())) {
                    //营销活动包含的所有品牌Id
                    List<Long> brandsIds = CollectionUtils.isNotEmpty(scopeList) ?
                            scopeList.stream().map(scope -> Long.valueOf(scope.getScopeId())).collect(Collectors.toList()) :
                            scopeIdList.stream().map(scope -> Long.valueOf(scope)).collect(Collectors.toList());
                    couponBrandDetail(couponInfo, brandsIds);
                }
                //店铺分类
                if (ScopeType.STORE_CATE.equals(couponInfo.getScopeType())) {
                    //营销活动包含的所有商品Id
                    List<Long> cateIdsFromStore = CollectionUtils.isNotEmpty(scopeList) ?
                            scopeList.stream().map(scope -> Long.valueOf(scope.getScopeId())).collect(Collectors.toList()) :
                            scopeIdList.stream().map(scope -> Long.valueOf(scope)).collect(Collectors.toList());
                    couponStoreCateDetail(couponInfo, cateIdsFromStore);
                }
                //平台分类
                if (ScopeType.BOSS_CATE.equals(couponInfo.getScopeType())) {
                    //营销活动包含的所有商品Id
                    List<Long> cateIdsFromBoss = CollectionUtils.isNotEmpty(scopeList) ?
                            scopeList.stream().map(scope -> Long.valueOf(scope.getScopeId())).collect(Collectors.toList()) :
                            scopeIdList.stream().map(scope -> Long.valueOf(scope)).collect(Collectors.toList());
                    couponBossCateDetailNew(couponInfo, cateIdsFromBoss);
                }
                //店铺可用
                if (ScopeType.SKU.equals(couponInfo.getScopeType())) {
                    //营销活动包含的所有商品Id
                    List<String> goodsInfoIds =  CollectionUtils.isNotEmpty(scopeList) ?
                            scopeList.stream().map(CouponMarketingScopeDTO::getScopeId).collect(Collectors.toList()) : scopeIdList;
                    couponGoodsDetail(couponInfo, goodsInfoIds);
                }

                vo.setScopeNames(couponInfo.getScopeNames());//关联商品

            }
            return vo;
        }).collect(Collectors.toList());
    }


    /**
     * 优惠券商品品牌信息
     *
     * @param couponInfo
     * @param brandsIds
     */
    private void couponBrandDetail(CouponInfoForScopeNamesDTO couponInfo, List<Long> brandsIds) {
        //优惠券品牌信息
        if (CollectionUtils.isNotEmpty(brandsIds)) {
            if (DefaultFlag.NO.equals(couponInfo.getPlatformFlag())) {
                //获取店铺签约的品牌
                List<ContractBrandBase> brandList = contractBrandService.findAllByStoreIdAndBrandIdIn(couponInfo.getStoreId(),brandsIds);
                //筛选出店铺签约的品牌信息
                brandsIds = brandList.stream().map(ContractBrandBase::getBrandId).collect(Collectors.toList());
                List<GoodsBrandBase> goodsBrandBases = goodsBrandService.findAllByBrandIdIn(brandsIds);
                couponInfo.setScopeNames(goodsBrandBases.stream().map(GoodsBrandBase::getBrandName).collect(Collectors.toList()));
            } else {
                //获取平台的品牌
                List<GoodsBrandBase> brandList = goodsBrandService.findAllByBrandIdIn(brandsIds);
                couponInfo.setScopeNames(brandList.stream().map(GoodsBrandBase::getBrandName).collect(Collectors.toList()));
            }
            couponInfo.setScopeIds(brandsIds.stream().map(id -> String.valueOf(id)).collect(Collectors.toList()));
        }

    }

    /**
     * 优惠券商品分类信息
     *
     * @param couponInfo
     * @param cateIds
     */
    private void couponStoreCateDetail(CouponInfoForScopeNamesDTO couponInfo, List<Long> cateIds) {

        //组装商品信息
        if (CollectionUtils.isNotEmpty(cateIds)) {
            //店铺分类
            if (Objects.equals(couponInfo.getPlatformFlag(), DefaultFlag.NO)) {
                List<StoreCateBase> storeCateList = storeCateService.findAllByStoreCateIdIn(cateIds);
                List<StoreCateBase> newCateList = storeCateList;
                //只显示父级的节点的名称
                List<StoreCateBase> nameGoodsCateList = storeCateList.stream().filter(item -> newCateList.stream().noneMatch(i -> i.getStoreCateId().equals(item.getCateParentId()))).collect(Collectors.toList());

                couponInfo.setScopeNames(nameGoodsCateList.stream().map(cate -> cate.getCateName()).collect(Collectors.toList()));
                couponInfo.setScopeIds(cateIds.stream().map(id -> String.valueOf(id)).collect(Collectors.toList()));
            }
        }
    }

    /**
     * 优惠券商品分类信息
     *
     * @param couponInfo
     * @param cateIds
     */
    private void couponBossCateDetail(CouponInfoForScopeNamesDTO couponInfo, List<Long> cateIds) {

        //组装商品信息
        if (CollectionUtils.isNotEmpty(cateIds)) {
            //平台分类
            if (Objects.equals(couponInfo.getPlatformFlag(), DefaultFlag.YES)) {
                GoodsCateQueryRequest request = new GoodsCateQueryRequest();
                request.setCateIds(cateIds);
                request.setDelFlag(DeleteFlag.NO.toValue());
                final List<GoodsCate> cateList = this.query(request);
                //只显示父级的节点的名称
                List<GoodsCate> nameGoodsCateList = cateList.stream().filter(item -> cateList.stream().noneMatch(i -> i.getCateId().equals(item.getCateParentId()))).collect(Collectors.toList());
                couponInfo.setScopeNames(nameGoodsCateList.stream().map(GoodsCate::getCateName).collect(Collectors.toList()));
                couponInfo.setScopeIds(cateIds.stream().map(id -> String.valueOf(id)).collect(Collectors.toList()));
            }
        }
    }

    /**
     * 优惠券商品信息
     *
     * @param couponInfo
     * @param goodsInfoIds
     */
    private void couponGoodsDetail(CouponInfoForScopeNamesDTO couponInfo, List<String> goodsInfoIds) {
        //组装商品信息
        if (CollectionUtils.isNotEmpty(goodsInfoIds)) {
            GoodsInfoQueryRequest queryRequest = new GoodsInfoQueryRequest();
            //FIXME 营销是平铺展示，但是数量达到一定层级，还是需要分页，先暂时这么控制
            queryRequest.setPageSize(10000);
            queryRequest.setStoreId(couponInfo.getStoreId());
            queryRequest.setAddedFlag(AddedFlag.YES.toValue());//上架
            queryRequest.setDelFlag(DeleteFlag.NO.toValue());//可用
            queryRequest.setAuditStatus(CheckStatus.CHECKED);//已审核
            queryRequest.setGoodsInfoIds(goodsInfoIds);

            GoodsInfoResponse goodsInfoResponse = goodsInfoService.pageView(queryRequest);
//            couponInfoResponse.setGoodsList(GoodsInfoResponse.builder()
//                    .goodsInfoPage(goodsInfoResponse.getGoodsInfoPage())
//                    .brands(goodsInfoResponse.getBrands())
//                    .cates(goodsInfoResponse.getCates())
//                    .goodses(goodsInfoResponse.getGoodses())
//                    .build());
            couponInfo.setScopeNames(goodsInfoResponse.getGoodsInfoPage().getContent().stream().map(goodsInfo -> goodsInfo.getGoodsInfoName()).collect(Collectors.toList()));
            couponInfo.setScopeIds(goodsInfoIds);
        }
    }

    /**
     * 优惠券商品分类信息
     *
     * @param couponInfo
     * @param cateIds
     */
    private void couponBossCateDetailNew(CouponInfoForScopeNamesDTO couponInfo, List<Long> cateIds) {

        //组装商品信息
        if (CollectionUtils.isNotEmpty(cateIds)) {
            //平台分类
            if (Objects.equals(couponInfo.getPlatformFlag(), DefaultFlag.YES)) {
                final List<GoodsCateBase> cateList = goodsCateRepository.findAllByCateIdIn(cateIds);
                //只显示父级的节点的名称
                List<GoodsCateBase> nameGoodsCateList = cateList.stream().filter(item -> cateList.stream().noneMatch(i -> i.getCateId().equals(item.getCateParentId()))).collect(Collectors.toList());
                couponInfo.setScopeNames(nameGoodsCateList.stream().map(GoodsCateBase::getCateName).collect(Collectors.toList()));
                couponInfo.setScopeIds(cateIds.stream().map(id -> String.valueOf(id)).collect(Collectors.toList()));
            }
        }
    }



    /**
     * 条件查询商品分类
     *
     * @param request 参数
     * @return list
     */
    public List<GoodsCate> pageByCondition(GoodsCateQueryRequest request) {
        List<GoodsCate> list = goodsCateRepository.findAll(request.getWhereCriteria());
        return ListUtils.emptyIfNull(list);
    }

    /**
     * 条件查询商品分类
     *
     * @param request 参数
     * @return list
     */
    public List<GoodsCate> getGoodsCateSImpleList(GoodsCateQueryRequest request) {
        List<GoodsCate> list  = goodsCateRepository.findAll(request.getWhereCriteria());
        return ListUtils.emptyIfNull(list);
    }


}
