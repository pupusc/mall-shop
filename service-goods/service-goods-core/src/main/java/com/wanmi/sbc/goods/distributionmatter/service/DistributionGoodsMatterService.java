package com.wanmi.sbc.goods.distributionmatter.service;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.SortType;
import com.wanmi.sbc.goods.api.request.distributionmatter.DistributionGoodsMatterPageRequest;
import com.wanmi.sbc.goods.bean.enums.MatterType;
import com.wanmi.sbc.goods.cate.service.GoodsCateService;
import com.wanmi.sbc.goods.distributionmatter.model.root.DistributionGoodsMatter;
import com.wanmi.sbc.goods.distributionmatter.repository.DistributionGoodsMatterRepository;
import com.wanmi.sbc.goods.info.model.root.GoodsInfo;
import com.wanmi.sbc.goods.util.XssUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class DistributionGoodsMatterService {


    @Autowired
    private DistributionGoodsMatterRepository distributionGoodsMatterRepository;

    @Autowired
    private GoodsCateService goodsCateService;

    /**
     * 新增商品分销素材
     */
    @Transactional
    public void add(DistributionGoodsMatter distributionGoodsMatter) {
        distributionGoodsMatter.setRecommendNum(0);
        distributionGoodsMatter.setCreateTime(LocalDateTime.now());
        distributionGoodsMatter.setUpdateTime(LocalDateTime.now());
        distributionGoodsMatterRepository.save(distributionGoodsMatter);
    }

    /**
     * 修改商品分销素材
     */
    @Transactional
    public void update(DistributionGoodsMatter distributionGoodsMatter) {
        distributionGoodsMatter.setUpdateTime(LocalDateTime.now());
        distributionGoodsMatterRepository.update(distributionGoodsMatter);
    }

    /**
     * 更新分享次数，不更新时间
     *
     * @param distributionGoodsMatter
     */
    @Transactional
    public void updataRecomendNumById(DistributionGoodsMatter distributionGoodsMatter) {
        distributionGoodsMatterRepository.updataRecomendNumById(distributionGoodsMatter);
    }

    /**
     * 批量删除商品分销素材
     */
    @Transactional
    public void delete(List ids) {
        distributionGoodsMatterRepository.deleteByIds(ids);
    }

    /**
     * 分页商品分销素材
     */
    public Page<DistributionGoodsMatter> page(DistributionGoodsMatterPageRequest request) {
        //获取该分类的所有子分类
        if (Objects.nonNull(request.getCateId()) && request.getCateId() > 0) {
            request.setCateIds(goodsCateService.getChlidCateId(request.getCateId()));
            if (CollectionUtils.isNotEmpty(request.getCateIds())) {
                request.getCateIds().add(request.getCateId());
            } else {
                request.setCateIds(Stream.of(request.getCateId()).collect(Collectors.toList()));
            }
            request.setCateId(null);
        }
        return distributionGoodsMatterRepository.findAll(getWhereCriteria(request), request.getPageRequest());
    }


    public List<DistributionGoodsMatter> queryByIds(List<String> idList) {
        return distributionGoodsMatterRepository.queryByIds(idList);
    }

    /**
     * 根据ID单个查询分销素材
     *
     * @param id
     * @return
     */
    public DistributionGoodsMatter queryById(String id) {
        return distributionGoodsMatterRepository.queryById(id);
    }

    /**
     * 分页查询公共条件
     *
     * @return
     */
    private Specification<DistributionGoodsMatter> getWhereCriteria(DistributionGoodsMatterPageRequest request) {
        return (root, cquery, cbuild) -> {
            List<Predicate> predicates = new ArrayList<>();
            Join<DistributionGoodsMatter, GoodsInfo> goodsInfoJoin = root.join("goodsInfo", JoinType.LEFT);
            predicates.add(cbuild.equal(root.get("delFlag"), DeleteFlag.NO));
            // skuId
            if (StringUtils.isNotEmpty(request.getGoodsInfoId())) {

                predicates.add(cbuild.equal(goodsInfoJoin.get("goodsInfoId"), request.getGoodsInfoId()));
            }
            // 店铺id
            if (Objects.nonNull(request.getStoreId())) {
                predicates.add(cbuild.equal(goodsInfoJoin.get("storeId"), request.getStoreId()));
            }
            //predicates.add(cbuild.isNotNull(goodsInfoJoin.get("goodsInfoId")));
            // 发布者id
            if (request.getOperatorId() != null) {
                predicates.add(cbuild.equal(root.get("operatorId"), request.getOperatorId()));
            }
            // 引用次数范围
            if (request.getRecommendNumMin() != null) {
                predicates.add(cbuild.greaterThanOrEqualTo(root.get("recommendNum"), request.getRecommendNumMin()));
            }
            if (request.getRecommendNumMax() != null) {
                predicates.add(cbuild.lessThanOrEqualTo(root.get("recommendNum"), request.getRecommendNumMax()));
            }

            //商品名称
            if (StringUtils.isNotEmpty(request.getGoodsInfoName())) {
                predicates.add(cbuild.like(goodsInfoJoin.get("goodsInfoName"), buildLike(request.getGoodsInfoName())));
            }
            //商品编码
            if (StringUtils.isNotEmpty(request.getGoodsInfoNo())) {
                predicates.add(cbuild.like(goodsInfoJoin.get("goodsInfoNo"), buildLike(request.getGoodsInfoNo())));
            }
            //品牌id
            if (request.getBrandId() != null && request.getBrandId() > 0) {
                predicates.add(cbuild.equal(goodsInfoJoin.get("brandId"), request.getBrandId()));
            }
            //商品类目
            if (CollectionUtils.isNotEmpty(request.getCateIds())) {
                predicates.add(goodsInfoJoin.get("cateId").in(request.getCateIds()));
            }

            // 引用次数排序
            if (request.getSortByRecommendNum() != null) {
                request.putSort("recommendNum", request.getSortByRecommendNum().toValue());
            } else {
                request.putSort("updateTime", SortType.DESC.toValue());
            }

            //素材类型
            if (request.getMatterType() != null) {
                predicates.add(cbuild.equal(root.get("matterType"), request.getMatterType()));
                //商品素材-分销商品的审核状态
                if (request.getMatterType() == MatterType.GOODS && request.getDistributionGoodsAudit() != null) {
                    predicates.add(cbuild.equal(goodsInfoJoin.get("distributionGoodsAudit"), request.getDistributionGoodsAudit()));
                }
            }

            Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);
            return p.length == 0 ? null : p.length == 1 ? p[0] : cbuild.and(p);
        };
    }

    private static String buildLike(String field) {
        StringBuilder stringBuilder = new StringBuilder();
        return stringBuilder.append("%").append(XssUtils.replaceLikeWildcard(field)).append("%").toString();
    }

    /**
     * 分页商品分销素材
     */
    public Page<DistributionGoodsMatter> pageNew(DistributionGoodsMatterPageRequest request) {
        //获取该分类的所有子分类
        if (Objects.nonNull(request.getCateId()) && request.getCateId() > 0) {
            request.setCateIds(goodsCateService.getChlidCateIdNew(request.getCateId()));
            if (CollectionUtils.isNotEmpty(request.getCateIds())) {
                request.getCateIds().add(request.getCateId());
            } else {
                request.setCateIds(Stream.of(request.getCateId()).collect(Collectors.toList()));
            }
            request.setCateId(null);
        }
        return distributionGoodsMatterRepository.findAll(getWhereCriteria(request), request.getPageRequest());
    }

}
