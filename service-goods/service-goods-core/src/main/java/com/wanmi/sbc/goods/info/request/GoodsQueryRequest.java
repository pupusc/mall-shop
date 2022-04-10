package com.wanmi.sbc.goods.info.request;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.common.enums.BoolFlag;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.ThirdPlatformType;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.common.util.StringUtil;
import com.wanmi.sbc.goods.bean.enums.AddedFlag;
import com.wanmi.sbc.goods.bean.enums.CheckStatus;
import com.wanmi.sbc.goods.bean.enums.GoodsSelectStatus;
import com.wanmi.sbc.goods.info.model.root.Goods;
import com.wanmi.sbc.goods.util.XssUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.ObjectUtils;

import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 商品查询请求
 * Created by daiyitian on 2017/3/24.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GoodsQueryRequest extends BaseQueryRequest {

    /**
     * 批量SPU编号
     */
    private List<String> goodsIds;

    /**
     * 精准条件-SPU编码
     */
    private String goodsNo;

    /**
     * 精准条件-批量SPU编码
     */
    private List<String> goodsNos;

    /**
     * 模糊条件-SPU编码
     */
    private String likeGoodsNo;

    /**
     * 模糊条件-SKU编码
     */
    private String likeGoodsInfoNo;

    /**
     * 模糊条件-商品名称
     */
    private String likeGoodsName;

    /**
     * 模糊条件-供应商名称
     */
    private String likeProviderName;

    /**
     * 模糊条件-关键词（商品名称、SPU编码）
     */
    private String keyword;

    /**
     * 商品分类
     */
    private Long cateId;

    /**
     * 三方渠道类目id
     */
    private Long thirdCateId;

    /**
     * 批量商品分类
     */
    private List<Long> cateIds;

    /**
     * 品牌编号
     */
    private Long brandId;

    /**
     * 批量品牌编号
     */
    private List<Long> brandIds;

    /**
     * 上下架状态
     */
    private Integer addedFlag;

    /**
     * 上下架状态-批量
     */
    private List<Integer> addedFlags;

    /**
     * 删除标记
     */
    private Integer delFlag;

    /**
     * 客户编号
     */
    private String customerId;

    /**
     * 公司信息ID
     */
    private Long companyInfoId;

    /**
     * 店铺ID
     */
    private Long storeId;

    /**
     * 非GoodsId
     */
    private String notGoodsId;

    /**
     * 商家名称
     */
    private String likeSupplierName;

    /**
     * 审核状态
     */
    private CheckStatus auditStatus;

    /**
     * 批量审核状态
     */
    private List<CheckStatus> auditStatusList;

    /**
     * 店铺分类Id
     */
    private Long storeCateId;

    /**
     * 店铺分类所关联的SpuIds
     */
    private List<String> storeCateGoodsIds;

    /**
     * 运费模板ID
     */
    private Long freightTempId;

    /**
     * 商品状态筛选
     */
    private List<GoodsSelectStatus> goodsSelectStatuses;

    /**
     * 销售类别
     */
    private Integer saleType;

    /**
     * 商品类型，0：实体商品，1：虚拟商品，2：卡券商品，3：周期购商品
     */
    private Integer goodsType;

    /**
     * 商品类型，0：实体商品，1：虚拟商品
     */
    private Integer goodsSource;

    /**
     * 批量供应商商品id
     */
    private List<String> providerGoodsIds;

    /**
     * 是否需要同步 0：不需要同步 1：需要同步
     */
    private BoolFlag needSynchronize;


    /**
     * 创建开始时间
     */
    private LocalDateTime createTimeBegin;

    /**
     * 创建结束时间
     */
    private LocalDateTime createTimeEnd;
    /**
     *需要排除的三方渠道
     */
    private List<Integer> notThirdPlatformType;

    /**
     *三方渠道
     */
    private ThirdPlatformType thirdPlatformType;

    /**
     * 三方spuid
     */
    private String thirdPlatformSpuId;

    /**
     * 供应商是否可售
     */
    private Integer vendibility;

    /**
     * 标签ID
     */
    private Long labelId;

    /**
     * 供应商id
     */
    private String providerId;


    /**
     * 封装公共条件
     *
     * @return
     */
    public Specification<Goods> getWhereCriteria() {
        return (root, cquery, cbuild) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (StringUtils.isNotBlank(providerId)) {
                predicates.add(cbuild.equal(root.get("providerId"), providerId));
            }
            //批量商品编号
            if (CollectionUtils.isNotEmpty(goodsIds)) {
                predicates.add(root.get("goodsId").in(goodsIds));
            }
            //批量店铺分类关联商品编号
            if (CollectionUtils.isNotEmpty(storeCateGoodsIds)) {
                predicates.add(root.get("goodsId").in(storeCateGoodsIds));
            }
            //查询SPU编码
            if (StringUtils.isNotBlank(goodsNo)) {
                predicates.add(cbuild.equal(root.get("goodsNo"), goodsNo));
            }
            //批量查询SPU编码
            if (CollectionUtils.isNotEmpty(goodsNos)) {
                predicates.add(root.get("goodsNo").in(goodsNos));
            }
            //查询品牌编号
            if (brandId != null && brandId > 0) {
                predicates.add(cbuild.equal(root.get("brandId"), brandId));
            }
            //查询分类编号
            if (cateId != null && cateId > 0) {
                predicates.add(cbuild.equal(root.get("cateId"), cateId));
            }
            //批量查询分类编号
            if (CollectionUtils.isNotEmpty(cateIds)) {
                predicates.add(root.get("cateId").in(cateIds));
            }
            //批量查询分类编号
            if (CollectionUtils.isNotEmpty(brandIds)) {
                predicates.add(root.get("brandId").in(brandIds));
            }
            //公司信息ID
            if (companyInfoId != null) {
                predicates.add(cbuild.equal(root.get("companyInfoId"), companyInfoId));
            }
            //店铺ID
            if (storeId != null) {
                predicates.add(cbuild.equal(root.get("storeId"), storeId));
            }
            if (thirdCateId!=null) {
                predicates.add(cbuild.equal(root.get("thirdCateId"), thirdCateId));
            }
            if (thirdPlatformType!=null) {
                predicates.add(cbuild.equal(root.get("thirdPlatformType"), thirdPlatformType));
            }
            //模糊查询SPU编码
            if (StringUtils.isNotEmpty(likeGoodsNo)) {
                predicates.add(cbuild.like(root.get("goodsNo"), StringUtil.SQL_LIKE_CHAR.concat(XssUtils.replaceLikeWildcard(likeGoodsNo.trim())).concat(StringUtil.SQL_LIKE_CHAR)));
            }
            //模糊查询名称
            if (StringUtils.isNotEmpty(likeGoodsName)) {
                predicates.add(cbuild.like(root.get("goodsName"), StringUtil.SQL_LIKE_CHAR.concat(XssUtils.replaceLikeWildcard(likeGoodsName.trim())).concat(StringUtil.SQL_LIKE_CHAR)));
            }
            //模糊查询商家名称
            if (StringUtils.isNotBlank(likeSupplierName)) {
                predicates.add(cbuild.like(root.get("supplierName"), StringUtil.SQL_LIKE_CHAR.concat(XssUtils.replaceLikeWildcard(likeSupplierName.trim())).concat(StringUtil.SQL_LIKE_CHAR)));
            }
            //模糊查询供应商名称
            if (StringUtils.isNotBlank(likeProviderName)) {
                predicates.add(cbuild.like(root.get("providerName"), StringUtil.SQL_LIKE_CHAR.concat(XssUtils.replaceLikeWildcard(likeProviderName.trim())).concat(StringUtil.SQL_LIKE_CHAR)));
            }
            //批量供应商商品编号
            if (CollectionUtils.isNotEmpty(providerGoodsIds)) {
                predicates.add(root.get("providerGoodsId").in(providerGoodsIds));
            }

            // 小于或等于 搜索条件:创建开始时间截止
            if (createTimeBegin != null) {
                predicates.add(cbuild.lessThanOrEqualTo(root.get("createTime"), createTimeBegin));
            }
            if(StringUtils.isNotBlank(thirdPlatformSpuId)){
                predicates.add(cbuild.equal(root.get("thirdPlatformSpuId"),thirdPlatformSpuId));
            }
            // 大于或等于 搜索条件:创建结束时间开始
            if (createTimeEnd != null) {
                predicates.add(cbuild.greaterThanOrEqualTo(root.get("createTime"), createTimeEnd));
            }

            //关键词搜索
            if (StringUtils.isNotBlank(keyword)) {
                String str = StringUtil.SQL_LIKE_CHAR.concat(XssUtils.replaceLikeWildcard(keyword.trim())).concat(StringUtil.SQL_LIKE_CHAR);
                predicates.add(cbuild.or(cbuild.like(root.get("goodsName"), str), cbuild.like(root.get("goodsNo"), str)));
            }

            //审核状态
            if(auditStatus != null){
                predicates.add(cbuild.equal(root.get("auditStatus"), auditStatus));
            }

            //上下架状态
            if (addedFlag != null) {
                predicates.add(cbuild.equal(root.get("addedFlag"), addedFlag));
            }

            //多个上下架状态
            if (CollectionUtils.isNotEmpty(addedFlags)) {
                predicates.add(root.get("addedFlag").in(addedFlags));
            }

            /**
             * 批量审核状态
             */
            if(CollectionUtils.isNotEmpty(auditStatusList)){
                predicates.add(root.get("auditStatus").in(auditStatusList));
            }

            //查询标签关联
            if(Objects.nonNull(labelId)){
                String str = StringUtil.SQL_LIKE_CHAR.concat(",").concat(String.valueOf(labelId)).concat(",").concat(StringUtil.SQL_LIKE_CHAR);
                predicates.add(cbuild.like(root.get("labelIdStr"), str));
            }

            //删除标记
            if (delFlag != null) {
                predicates.add(cbuild.equal(root.get("delFlag"), delFlag));
            }
            //需要同步标记
            if (needSynchronize != null) {
                predicates.add(cbuild.equal(root.get("needSynchronize"), needSynchronize));
            }
            //非商品编号
            if (StringUtils.isNotBlank(notGoodsId)) {
                predicates.add(cbuild.notEqual(root.get("goodsId"), notGoodsId));
            }

            //销售类型
            if (!ObjectUtils.isEmpty(saleType)) {
                predicates.add(cbuild.equal(root.get("saleType"), saleType));
            }

            //商品类型
            if (!ObjectUtils.isEmpty(goodsType)) {
                predicates.add(cbuild.equal(root.get("goodsType"), goodsType));
            }

            //商品来源
            if (!ObjectUtils.isEmpty(goodsSource)) {
                predicates.add(cbuild.equal(root.get("goodsSource"), goodsSource));
            }

            //运费模板ID
            if (freightTempId != null) {
                predicates.add(cbuild.equal(root.get("freightTempId"), freightTempId));
            }
            if(notThirdPlatformType !=null&&notThirdPlatformType.size()>0){
                predicates.add(cbuild.or(cbuild.not(root.get("thirdPlatformType").in(notThirdPlatformType)), root.get("thirdPlatformType").isNull()));
            }
            //商品状态筛选
            if(CollectionUtils.isNotEmpty(goodsSelectStatuses)){
                List<Predicate> orPredicate = new ArrayList<>();
                goodsSelectStatuses.forEach(goodsInfoSelectStatus -> {
                    if(goodsInfoSelectStatus != null){
                        if(goodsInfoSelectStatus == GoodsSelectStatus.ADDED){
                            orPredicate.add(cbuild.and(cbuild.equal(root.get("auditStatus"), CheckStatus.CHECKED), cbuild.equal(root.get("addedFlag"), AddedFlag.YES.toValue())));
                        } else if(goodsInfoSelectStatus == GoodsSelectStatus.NOT_ADDED){
                            orPredicate.add(cbuild.and(cbuild.equal(root.get("auditStatus"), CheckStatus.CHECKED), cbuild.equal(root.get("addedFlag"), AddedFlag.NO.toValue())));
                        }else if(goodsInfoSelectStatus == GoodsSelectStatus.PART_ADDED){
                            orPredicate.add(cbuild.and(cbuild.equal(root.get("auditStatus"), CheckStatus.CHECKED), cbuild.equal(root.get("addedFlag"), AddedFlag.PART.toValue())));
                        } else if(goodsInfoSelectStatus == GoodsSelectStatus.OTHER){
                            orPredicate.add(root.get("auditStatus").in(CheckStatus.FORBADE, CheckStatus.NOT_PASS, CheckStatus.WAIT_CHECK));
                        }
                    }
                });
                predicates.add(cbuild.or(orPredicate.toArray(new Predicate[orPredicate.size()])));
            }

            if(Objects.nonNull(vendibility)) {
                Predicate p1 = cbuild.equal(root.get("vendibility"), DefaultFlag.YES.toValue());
                Predicate p2 = cbuild.isNull(root.get("vendibility"));
                predicates.add(cbuild.or(p1, p2));
                Predicate p3 = cbuild.equal(root.get("providerStatus"), Constants.yes);
                Predicate p4 = cbuild.isNull(root.get("providerStatus"));
                predicates.add(cbuild.or(p3, p4));
            }

            Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);
            return p.length == 0 ? null : p.length == 1 ? p[0] : cbuild.and(p);
        };
    }

}
