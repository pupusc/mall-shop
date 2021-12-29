package com.wanmi.sbc.goods.info.request;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.common.enums.BoolFlag;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.common.util.StringUtil;
import com.wanmi.sbc.goods.bean.enums.AddedFlag;
import com.wanmi.sbc.goods.bean.enums.CheckStatus;
import com.wanmi.sbc.goods.bean.enums.EnterpriseAuditState;
import com.wanmi.sbc.goods.bean.enums.GoodsInfoSelectStatus;
import com.wanmi.sbc.goods.info.model.root.GoodsInfo;
import com.wanmi.sbc.goods.util.XssUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 商品SKU查询请求
 * Created by daiyitian on 2017/3/24.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GoodsInfoQueryRequest extends BaseQueryRequest implements Serializable {

    private static final long serialVersionUID = 2067929276444140704L;

    /**
     * 批量SKU编号
     */
    private List<String> goodsInfoIds;

    /**
     * SPU编号
     */
    private String goodsId;

    /**
     * 批量SPU编号
     */
    private List<String> goodsIds;

    /**
     *品牌编号
     */
    private Long brandId;

    /**
     * 批量品牌编号
     */
    private List<Long> brandIds;

    /**
     * 分类编号
     */
    private Long cateId;

    /**
     * 店铺分类id
     */
    private Long storeCateId;

    /**
     * 模糊条件-商品名称
     */
    private String likeGoodsName;

    /**
     * 精确条件-批量SKU编码
     */
    private List<String> goodsInfoNos;

    /**
     * 精确条件-批量ERP SKU编码
     */
    private List<String> erpGoodsInfoNos;

    private List<String> erpGoodsNos;

    /**
     * 模糊条件-SKU编码
     */
    private String likeGoodsInfoNo;

    /**
     * 模糊条件-SPU编码
     */
    private String likeGoodsNo;

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
     * 客户等级
     */
    private Long customerLevelId;

    /**
     * 客户等级折扣
     */
    private BigDecimal customerLevelDiscount;

    /**
     * 非GoodsId
     */
    private String notGoodsId;

    /**
     * 非GoodsInfoId
     */
    private String notGoodsInfoId;

    /**
     * 公司信息ID
     */
    private Long companyInfoId;

    /**
     * 店铺ID
     */
    private Long storeId;

    /**
     * 批量店铺ID
     */
    private List<Long> storeIds;

    /**
     * 审核状态
     */
    private CheckStatus auditStatus;

    /**
     * 审核状态
     */
    private List<CheckStatus> auditStatuses;

    /**
     * 关键词，目前范围：商品名称、SKU编码
     */
    private String keyword;

    /**
     * 业务员app,商品状态筛选
     */
    private List<GoodsInfoSelectStatus> goodsSelectStatuses;

    /**
     * 商家类型
     */
    private BoolFlag companyType;

    /**
     * 销售类别
     */
    private Integer saleType;

    /**
     * 企业购商品审核状态
     */
    private EnterpriseAuditState enterPriseAuditState;

    /**
     * 批量供应商商品SKU编号
     */
    private List<String> providerGoodsInfoIds;

    /**
     * 商品来源，0供应商，1商家,2 linkedmall
     */
    private Integer goodsSource;

    /**
     * 第三方平台的skuId
     */
    private List<String> thirdPlatformSkuId;

    /**
     * 第三方平台的spuId
     */
    private String thirdPlatformSpuId;

    /**
     * 三方渠道类目id
     */
    private Long thirdCateId;

    /**
     * 是否定时上架
     */
    private Boolean addedTimingFlag;

    /**
     *需要排除的三方渠道
     */
    private List<Integer> notThirdPlatformType;
    /**
     *三方渠道
     */
    private Integer thirdPlatformType;

    /**
     * 是否可售
     */
    private Integer vendibility;

    /**
     * 是否过滤积分价商品 true是 false不过滤
     */
    private Boolean integralPriceFlag;

    /**
     * 标签ID
     */
    private Long labelId;

    /**
     * 商品类型，0：实体商品，1：虚拟商品，2：卡券商品，3：周期购商品
     */
    private Integer goodsType;

    /**
     * ERP商品编码
     */
    private String erpGoodsInfoNo;

    /**
     * 是否组合购
     */
    @ApiModelProperty(value = "是否组合购", notes = "是否是组合商品，0：否，1：是")
    private Boolean combinedCommodity;


    /**
     * 封装公共条件
     *
     * @return
     */
    public Specification<GoodsInfo> getWhereCriteria() {
        return (root, cquery, cbuild) -> {
            List<Predicate> predicates = new ArrayList<>();
            //批量SKU编号
            if(CollectionUtils.isNotEmpty(goodsInfoIds)){
                predicates.add(root.get("goodsInfoId").in(goodsInfoIds));
            }
            //SPU编号
            if(StringUtils.isNotBlank(goodsId)){
                predicates.add(cbuild.equal(root.get("goodsId"), goodsId));
            }
            //批量SKU编号
            if(CollectionUtils.isNotEmpty(goodsInfoNos)){
                predicates.add(root.get("goodsInfoNo").in(goodsInfoNos));
            }
            //ERP 批量SKU编号
            if(CollectionUtils.isNotEmpty(erpGoodsInfoNos)){
                predicates.add(root.get("erpGoodsInfoNo").in(erpGoodsInfoNos));
            }
            //ERP 批量SPU编号
            if(CollectionUtils.isNotEmpty(erpGoodsNos)){
                predicates.add(root.get("erpGoodsNo").in(erpGoodsNos));
            }
            //批量SPU编号
            if(CollectionUtils.isNotEmpty(goodsIds)){
                predicates.add(root.get("goodsId").in(goodsIds));
            }
            if (thirdCateId!=null) {
                predicates.add(cbuild.equal(root.get("thirdCateId"), thirdCateId));
            }
            //SKU编码
            if(StringUtils.isNotEmpty(likeGoodsInfoNo)){
                predicates.add(cbuild.like(root.get("goodsInfoNo"), StringUtil.SQL_LIKE_CHAR.concat(XssUtils.replaceLikeWildcard(likeGoodsInfoNo.trim())).concat(StringUtil.SQL_LIKE_CHAR)));
            }
            //店铺ID
            if(storeId != null){
                predicates.add(cbuild.equal(root.get("storeId"), storeId));
            }

            //分类ID
            if(cateId != null && cateId > 0){
                predicates.add(cbuild.equal(root.get("cateId"), cateId));
            }
            //公司信息ID
            if(companyInfoId != null){
                predicates.add(cbuild.equal(root.get("companyInfoId"), companyInfoId));
            }
            if(notThirdPlatformType!=null&&notThirdPlatformType.size()>0){
                predicates.add(cbuild.or(cbuild.not(root.get("thirdPlatformType").in(notThirdPlatformType)), root.get("thirdPlatformType").isNull()));
            }
            if (thirdPlatformType!=null) {
                predicates.add(cbuild.equal(root.get("thirdPlatformType"),thirdPlatformType));
            }
            //批量店铺ID
            if(CollectionUtils.isNotEmpty(storeIds)){
                predicates.add(root.get("storeId").in(storeIds));
            }
            //模糊查询名称
            if(StringUtils.isNotEmpty(likeGoodsName)){
                predicates.add(cbuild.like(root.get("goodsInfoName"), StringUtil.SQL_LIKE_CHAR.concat(XssUtils.replaceLikeWildcard(likeGoodsName.trim())).concat(StringUtil.SQL_LIKE_CHAR)));
            }

            //关键字搜索
            if (StringUtils.isNotBlank(keyword)) {
                String str = StringUtil.SQL_LIKE_CHAR.concat(XssUtils.replaceLikeWildcard(keyword.trim())).concat(StringUtil.SQL_LIKE_CHAR);
                predicates.add(cbuild.or(cbuild.like(root.get("goodsInfoName"), str), cbuild.like(root.get("goodsInfoNo"), str)));
            }
            //上下架状态
            if (addedFlag != null) {
                predicates.add(cbuild.equal(root.get("addedFlag"), addedFlag));
            }
            //多个上下架状态
            if (CollectionUtils.isNotEmpty(addedFlags)) {
                predicates.add(root.get("addedFlag").in(addedFlags));
            }
            //审核状态
            if(auditStatus != null){
                predicates.add(cbuild.equal(root.get("auditStatus"), auditStatus));
            }
            if(vendibility != null){
                Predicate p1 = cbuild.equal(root.get("vendibility"), DefaultFlag.YES.toValue());
                Predicate p2 = cbuild.isNull(root.get("vendibility"));
                predicates.add(cbuild.or(p1, p2));
                Predicate p3 = cbuild.equal(root.get("providerStatus"), Constants.yes);
                Predicate p4 = cbuild.isNull(root.get("providerStatus"));
                predicates.add(cbuild.or(p3, p4));
            }
            //多个审核状态
            if(CollectionUtils.isNotEmpty(auditStatuses)){
                predicates.add(root.get("auditStatus").in(auditStatuses));
            }
            //是否处于定时上架
            if(addedTimingFlag != null){
                predicates.add(cbuild.equal(root.get("addedTimingFlag"), addedTimingFlag));
                if(Boolean.TRUE.equals(addedTimingFlag)){
                    predicates.add(cbuild.lessThanOrEqualTo(root.get("addedTimingTime"), LocalDateTime.now()));
                }
            }
            // 过滤积分价商品
            if (Objects.nonNull(integralPriceFlag) && Objects.equals(Boolean.TRUE, integralPriceFlag)){
                Predicate p1 = cbuild.equal(root.get("buyPoint"), 0);
                Predicate p2 = cbuild.isNull(root.get("buyPoint"));
                predicates.add(cbuild.or(p1, p2));
            }
            //删除标记
            if (delFlag != null) {
                predicates.add(cbuild.equal(root.get("delFlag"), delFlag));
            }
            //非商品编号
            if(StringUtils.isNotBlank(notGoodsId)){
                predicates.add(cbuild.notEqual(root.get("goodsId"), notGoodsId));
            }
            //非商品SKU编号
            if(StringUtils.isNotBlank(notGoodsInfoId)){
                predicates.add(cbuild.notEqual(root.get("goodsInfoId"), notGoodsInfoId));
            }

            // 商家类型
            if(Objects.nonNull(companyType)){
                predicates.add(cbuild.equal(root.get("companyType"), companyType.toValue()));
            }

            //批量供应商商品SKU编号
            if(CollectionUtils.isNotEmpty(providerGoodsInfoIds)){
                predicates.add(root.get("providerGoodsInfoId").in(providerGoodsInfoIds));
            }
            if(thirdPlatformSkuId!=null&&thirdPlatformSkuId.size()>0){
                predicates.add(root.get("thirdPlatformSkuId").in(thirdPlatformSkuId));
            }
            if(StringUtils.isNotBlank(thirdPlatformSpuId)){
                predicates.add(cbuild.equal(root.get("thirdPlatformSpuId"),thirdPlatformSpuId));
            }
            //业务员app商品状态筛选
            if(CollectionUtils.isNotEmpty(goodsSelectStatuses)){
                List<Predicate> orPredicate = new ArrayList<>();
                goodsSelectStatuses.forEach(goodsInfoSelectStatus -> {
                    if(goodsInfoSelectStatus != null){
                        if(goodsInfoSelectStatus == GoodsInfoSelectStatus.ADDED){
                            orPredicate.add(cbuild.and(cbuild.equal(root.get("auditStatus"), CheckStatus.CHECKED), cbuild.equal(root.get("addedFlag"), AddedFlag.YES.toValue())));
                        } else if(goodsInfoSelectStatus == GoodsInfoSelectStatus.NOT_ADDED){
                            orPredicate.add(cbuild.and(cbuild.equal(root.get("auditStatus"), CheckStatus.CHECKED), cbuild.equal(root.get("addedFlag"), AddedFlag.NO.toValue())));
                        } else if(goodsInfoSelectStatus == GoodsInfoSelectStatus.OTHER){
                            orPredicate.add(root.get("auditStatus").in(CheckStatus.FORBADE, CheckStatus.NOT_PASS, CheckStatus.WAIT_CHECK));
                        }
                    }
                });
                predicates.add(cbuild.or(orPredicate.toArray(new Predicate[orPredicate.size()])));
            }

            if (saleType != null){
//                Join<Goods, GoodsInfo> join = root.join("goods", JoinType.LEFT);
//                predicates.add(cbuild.equal(join.get("saleType"), saleType));
                predicates.add(cbuild.equal(root.get("saleType"), saleType));
            }

            /**
             * 企业购商品的审核状态
             */
            if(enterPriseAuditState != null && !EnterpriseAuditState.INIT.equals(enterPriseAuditState)){
                predicates.add(cbuild.equal(root.get("enterPriseAuditState"), enterPriseAuditState.toValue()));
            }

            // 商品来源
            if(Objects.nonNull(goodsSource)){
                predicates.add(cbuild.equal(root.get("goodsSource"), goodsSource));
            }

            //商品类型
            if(goodsType != null){
                predicates.add(cbuild.notEqual(root.get("goodsType"), goodsType));
            }

            //是组合购商品
            if (combinedCommodity != null && combinedCommodity) {
                predicates.add(cbuild.equal(root.get("combinedCommodity"), 1));
            }

            //非组合购商品
            if (combinedCommodity != null && !combinedCommodity) {
                predicates.add(cbuild.equal(root.get("combinedCommodity"), 0));
            }

            predicates.add(cbuild.isNotNull(root.get("erpGoodsInfoNo")));
            Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);
            return p.length == 0 ? null : p.length == 1 ? p[0] : cbuild.and(p);
        };
    }
}
