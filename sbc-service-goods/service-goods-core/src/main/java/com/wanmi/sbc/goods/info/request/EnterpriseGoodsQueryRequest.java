package com.wanmi.sbc.goods.info.request;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.common.enums.BoolFlag;
import com.wanmi.sbc.common.util.StringUtil;
import com.wanmi.sbc.goods.bean.enums.CheckStatus;
import com.wanmi.sbc.goods.bean.enums.EnterpriseAuditState;
import com.wanmi.sbc.goods.info.model.root.GoodsInfo;
import com.wanmi.sbc.goods.util.XssUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.ObjectUtils;

import javax.persistence.criteria.Predicate;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * com.wanmi.sbc.goods.api.request.info.DistributionGoodsPageRequest
 * 企业购商品商品分页请求对象
 *
 * @author CHENLI
 * @dateTime 2019/2/19 上午9:33
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EnterpriseGoodsQueryRequest extends BaseQueryRequest {

    /**
     * 模糊条件-商品名称
     */
    private String likeGoodsName;

    /**
     * 模糊条件-SKU编码
     */
    private String likeGoodsInfoNo;

    /**
     * 店铺ID
     */
    private Long storeId;

    /**
     * 平台类目-仅限三级类目
     */
    private Long cateId;

    /**
     * 批量商品分类
     */
    private List<Long> cateIds;

    /**
     * 店铺类目
     */
    private Long storeCateId;

    /**
     * 批量SPU编号
     */
    private List<String> goodsIds;

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
     * 删除标记
     */
    private Integer delFlag;

    /**
     * 市场价范围参数1
     */
    private BigDecimal salePriceFirst;

    /**
     * 市场价范围参数2
     */
    private BigDecimal salePriceLast;

    /**
     * 企业购佣金范围参数1
     */
    private BigDecimal distributionCommissionFirst;

    /**
     * 企业购佣金范围参数2
     */
    private BigDecimal distributionCommissionLast;


    /**
     * 佣金比例范围参数1
     */
    private BigDecimal commissionRateFirst;

    /**
     * 佣金比例范围参数2
     */
    private BigDecimal commissionRateLast;

    /**
     * 企业购销量范围参数1
     */
    private Integer distributionSalesCountFirst;

    /**
     * 企业购销量范围参数2
     */
    private Integer distributionSalesCountLast;

    /**
     * 库存范围参数1
     */
    private Long stockFirst;

    /**
     * 库存范围参数2
     */
    private Long stockLast;

    /**
     * 企业购商品审核状态 0:普通商品 1:待审核 2:已审核通过 3:审核不通过 4:禁止企业购
     */
    private Integer distributionGoodsAudit;

    /**
     * 是否过滤商品状态为失效的商品  0 否 1 是
     * 商品状态 0：正常 1：缺货 2：失效
     */
    private Integer goodsStatus ;

    /**
     * 销售类型 0:批发, 1:零售
     */
    private Integer saleType;

    /**
     * 企业购商品审核状态
     */
    @ApiModelProperty(value = "企业购商品审核状态", notes = "0：无状态 1：待审核 2：已审核 3：审核未通过")
    private EnterpriseAuditState enterPriseAuditState;

    /**
     * 标签ID
     */
    private Long labelId;

    @ApiModelProperty("可以作为 企业购商品-筛选 -- 不能是周期购商品")
    private Boolean enterpriseFlag;

    /**
     * 封装公共条件
     *
     * @return
     */
    public Specification<GoodsInfo> getWhereCriteria() {
        return (root, cquery, cbuild) -> {
            List<Predicate> predicates = new ArrayList<>();

            //店铺ID
            if(storeId != null){
                predicates.add(cbuild.equal(root.get("storeId"), storeId));
            }

            //模糊查询名称
            if (StringUtils.isNotEmpty(likeGoodsName)) {
                predicates.add(cbuild.like(root.get("goodsInfoName"), StringUtil.SQL_LIKE_CHAR.concat(XssUtils.replaceLikeWildcard(likeGoodsName.trim())).concat(StringUtil.SQL_LIKE_CHAR)));
            }

            //模糊查询SKU编码
            if (StringUtils.isNotEmpty(likeGoodsInfoNo)) {
                predicates.add(cbuild.like(root.get("goodsInfoNo"),
                        StringUtil.SQL_LIKE_CHAR.concat(XssUtils.replaceLikeWildcard(likeGoodsInfoNo.trim())).concat(StringUtil.SQL_LIKE_CHAR)));
            }

            //查询商品分类编号
            if (cateId != null && cateId > 0) {
                predicates.add(cbuild.equal(root.get("cateId"), cateId));
            }


            //查询品牌编号
            if (brandId != null && brandId > 0) {
                predicates.add(cbuild.equal(root.get("brandId"), brandId));
            }

            //上下架状态
            if (addedFlag != null) {
                predicates.add(cbuild.equal(root.get("addedFlag"), addedFlag));
            }

            //批量查询商品分类编号
            if (CollectionUtils.isNotEmpty(cateIds)) {
                predicates.add(root.get("cateId").in(cateIds));
            }

            //spu id 集合
            if (CollectionUtils.isNotEmpty(goodsIds)) {
                predicates.add(root.get("goodsId").in(goodsIds));
            }

            // 佣金比例范围
            if(Objects.nonNull(commissionRateFirst)){
                //大于等于
                predicates.add(cbuild.ge(root.get("commissionRate"), commissionRateFirst));
            }

            // 市场价范围
            if(Objects.nonNull(salePriceFirst)){
                //大于等于
                predicates.add(cbuild.ge(root.get("marketPrice"), salePriceFirst));
            }
            if (Objects.nonNull(salePriceLast)){
                //小于等于
                predicates.add(cbuild.le(root.get("marketPrice"), salePriceLast));
            }


            if (Objects.nonNull(distributionCommissionLast)){

                predicates.add(cbuild.le(root.get("distributionCommission"), distributionCommissionLast));
            }

            if (Objects.nonNull(commissionRateLast)){
                //小于等于
                predicates.add(cbuild.le(root.get("commissionRate"), commissionRateLast));
            }
            // 企业购销量范围
            if(Objects.nonNull(distributionSalesCountFirst)){
                //大于等于
                predicates.add(cbuild.ge(root.get("distributionSalesCount"), distributionSalesCountFirst));
            }
            if (Objects.nonNull(distributionSalesCountLast)){
                //小于等于
                predicates.add(cbuild.le(root.get("distributionSalesCount"), distributionSalesCountLast));
            }

            // 预估佣金范围
            if(Objects.nonNull(distributionCommissionFirst)){
                //大于等于
                predicates.add(cbuild.ge(root.get("distributionCommission"), distributionCommissionFirst));
            }

            // 库存范围
            if(Objects.nonNull(stockFirst)){
                //大于等于
                predicates.add(cbuild.ge(root.get("stock"), stockFirst));
            }
            if (Objects.nonNull(stockLast)){
                //小于等于
                predicates.add(cbuild.le(root.get("stock"), stockLast));
            }

            //企业购商品审核状态
            if(Objects.nonNull(distributionGoodsAudit)){
                predicates.add(cbuild.equal(root.get("distributionGoodsAudit"), distributionGoodsAudit));
            }

            //是否过滤商品状态为失效的商品  0 否 1 是
            if (Objects.nonNull(goodsStatus) && goodsStatus == BoolFlag.YES.toValue()) {
                predicates.add(cbuild.equal(root.get("auditStatus"), CheckStatus.CHECKED.toValue()));
            }

            //销售类型
            if (!ObjectUtils.isEmpty(saleType)) {
                predicates.add(cbuild.equal(root.get("saleType"), saleType));
            }

            /**
             * 企业购商品的审核状态
             */
            if(enterPriseAuditState != null && !EnterpriseAuditState.INIT.equals(enterPriseAuditState)){
                predicates.add(cbuild.equal(root.get("enterPriseAuditState"), enterPriseAuditState.toValue()));
            }

            //删除标记
            if (delFlag != null) {
                predicates.add(cbuild.equal(root.get("delFlag"), delFlag));
            }
            Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);
            return p.length == 0 ? null : p.length == 1 ? p[0] : cbuild.and(p);
        };
    }

}
