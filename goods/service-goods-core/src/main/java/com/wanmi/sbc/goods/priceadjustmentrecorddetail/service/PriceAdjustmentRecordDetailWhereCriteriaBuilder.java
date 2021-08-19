package com.wanmi.sbc.goods.priceadjustmentrecorddetail.service;

import com.wanmi.sbc.common.util.StringUtil;
import com.wanmi.sbc.goods.api.request.priceadjustmentrecorddetail.PriceAdjustmentRecordDetailQueryRequest;
import com.wanmi.sbc.goods.priceadjustmentrecorddetail.model.root.PriceAdjustmentRecordDetail;
import com.wanmi.sbc.goods.util.XssUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>调价单详情表动态查询条件构建器</p>
 * @author chenli
 * @date 2020-12-09 19:55:41
 */
public class PriceAdjustmentRecordDetailWhereCriteriaBuilder {
    public static Specification<PriceAdjustmentRecordDetail> build(PriceAdjustmentRecordDetailQueryRequest queryRequest) {
        return (root, cquery, cbuild) -> {
            List<Predicate> predicates = new ArrayList<>();
            // 批量查询-idList
            if (CollectionUtils.isNotEmpty(queryRequest.getIdList())) {
                predicates.add(root.get("id").in(queryRequest.getIdList()));
            }

            // id
            if (queryRequest.getId() != null) {
                predicates.add(cbuild.equal(root.get("id"), queryRequest.getId()));
            }

            // 模糊查询 - 调价单号
            if (StringUtils.isNotEmpty(queryRequest.getPriceAdjustmentNo())) {
                predicates.add(cbuild.like(root.get("priceAdjustmentNo"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getPriceAdjustmentNo()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 - 商品名称
            if (StringUtils.isNotEmpty(queryRequest.getGoodsInfoName())) {
                predicates.add(cbuild.like(root.get("goodsInfoName"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getGoodsInfoName()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 - SKU编码
            if (StringUtils.isNotEmpty(queryRequest.getGoodsInfoNo())) {
                predicates.add(cbuild.like(root.get("goodsInfoNo"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getGoodsInfoNo()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 - 商品规格
            if (StringUtils.isNotEmpty(queryRequest.getGoodsSpecText())) {
                predicates.add(cbuild.like(root.get("goodsSpecText"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getGoodsSpecText()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 是否独立设价：0 否、1 是
            if (queryRequest.getAloneFlag() != null) {
                predicates.add(cbuild.equal(root.get("aloneFlag"), queryRequest.getAloneFlag()));
            }

            // 销售类别(0:批发,1:零售)
            if (queryRequest.getSaleType() != null) {
                predicates.add(cbuild.equal(root.get("saleType"), queryRequest.getSaleType()));
            }

            // 设价类型,0:按客户(等级)1:按订货量(阶梯价)2:按市场价
            if (queryRequest.getPriceType() != null) {
                predicates.add(cbuild.equal(root.get("priceType"), queryRequest.getPriceType()));
            }

            // 原市场价
            if (queryRequest.getOriginalMarketPrice() != null) {
                predicates.add(cbuild.equal(root.get("originalMarketPrice"), queryRequest.getOriginalMarketPrice()));
            }

            // 调整后市场价
            if (queryRequest.getAdjustedMarketPrice() != null) {
                predicates.add(cbuild.equal(root.get("adjustedMarketPrice"), queryRequest.getAdjustedMarketPrice()));
            }

            // 差异
            if (queryRequest.getPriceDifference() != null) {
                predicates.add(cbuild.equal(root.get("priceDifference"), queryRequest.getPriceDifference()));
            }

            // 模糊查询 - 等级价 eg:[{},{}...]
            if (StringUtils.isNotEmpty(queryRequest.getLeverPrice())) {
                predicates.add(cbuild.like(root.get("leverPrice"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getLeverPrice()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 - 阶梯价 eg:[{},{}...]
            if (StringUtils.isNotEmpty(queryRequest.getIntervalPrice())) {
                predicates.add(cbuild.like(root.get("intervalPrice"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getIntervalPrice()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 执行结果：0 未执行、1 执行成功、2 执行失败
            if (queryRequest.getAdjustResult() != null) {
                predicates.add(cbuild.equal(root.get("adjustResult"), queryRequest.getAdjustResult()));
            }

            // 模糊查询 - 失败原因
            if (StringUtils.isNotEmpty(queryRequest.getFailReason())) {
                predicates.add(cbuild.like(root.get("failReason"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getFailReason()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 是否确认：0 未确认、1 已确认
            if (queryRequest.getConfirmFlag() != null) {
                predicates.add(cbuild.equal(root.get("confirmFlag"), queryRequest.getConfirmFlag()));
            }

            Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);
            return p.length == 0 ? null : p.length == 1 ? p[0] : cbuild.and(p);
        };
    }
}
