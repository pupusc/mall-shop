package com.wanmi.sbc.goods.bookingsalegoods.service;

import com.wanmi.sbc.common.util.StringUtil;
import com.wanmi.sbc.common.util.XssUtils;
import com.wanmi.sbc.goods.api.request.bookingsalegoods.BookingSaleGoodsQueryRequest;
import com.wanmi.sbc.goods.bookingsalegoods.model.root.BookingSaleGoods;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>预售商品信息动态查询条件构建器</p>
 *
 * @author dany
 * @date 2020-06-05 10:51:35
 */
public class BookingSaleGoodsWhereCriteriaBuilder {
    public static Specification<BookingSaleGoods> build(BookingSaleGoodsQueryRequest queryRequest) {
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

            // 预售id
            if (queryRequest.getBookingSaleId() != null) {
                predicates.add(cbuild.equal(root.get("bookingSaleId"), queryRequest.getBookingSaleId()));
            }

            // 批量查询-预售id
            if (CollectionUtils.isNotEmpty(queryRequest.getBookingSaleIdList())) {
                predicates.add(root.get("bookingSaleId").in(queryRequest.getBookingSaleIdList()));
            }

            // 商户id
            if (queryRequest.getStoreId() != null) {
                predicates.add(cbuild.equal(root.get("storeId"), queryRequest.getStoreId()));
            }

            // 模糊查询 - skuID
            if (StringUtils.isNotEmpty(queryRequest.getGoodsInfoId())) {
                predicates.add(cbuild.like(root.get("goodsInfoId"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getGoodsInfoId()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 批量查询-skuID-List
            if (CollectionUtils.isNotEmpty(queryRequest.getGoodsInfoIdList())) {
                predicates.add(root.get("goodsInfoId").in(queryRequest.getGoodsInfoIdList()));
            }

            // 模糊查询 - spuID
            if (StringUtils.isNotEmpty(queryRequest.getGoodsId())) {
                predicates.add(cbuild.like(root.get("goodsId"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getGoodsId()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 定金
            if (queryRequest.getHandSelPrice() != null) {
                predicates.add(cbuild.equal(root.get("handSelPrice"), queryRequest.getHandSelPrice()));
            }

            // 膨胀价格
            if (queryRequest.getInflationPrice() != null) {
                predicates.add(cbuild.equal(root.get("inflationPrice"), queryRequest.getInflationPrice()));
            }

            // 预售价
            if (queryRequest.getBookingPrice() != null) {
                predicates.add(cbuild.equal(root.get("bookingPrice"), queryRequest.getBookingPrice()));
            }

            // 预售数量
            if (queryRequest.getBookingCount() != null) {
                predicates.add(cbuild.equal(root.get("bookingCount"), queryRequest.getBookingCount()));
            }

            // 定金支付数量
            if (queryRequest.getHandSelCount() != null) {
                predicates.add(cbuild.equal(root.get("handSelCount"), queryRequest.getHandSelCount()));
            }

            // 尾款支付数量
            if (queryRequest.getTailCount() != null) {
                predicates.add(cbuild.equal(root.get("tailCount"), queryRequest.getTailCount()));
            }

            // 全款支付数量
            if (queryRequest.getPayCount() != null) {
                predicates.add(cbuild.equal(root.get("payCount"), queryRequest.getPayCount()));
            }

            // 大于或等于 搜索条件:创建时间开始
            if (queryRequest.getCreateTimeBegin() != null) {
                predicates.add(cbuild.greaterThanOrEqualTo(root.get("createTime"),
                        queryRequest.getCreateTimeBegin()));
            }
            // 小于或等于 搜索条件:创建时间截止
            if (queryRequest.getCreateTimeEnd() != null) {
                predicates.add(cbuild.lessThanOrEqualTo(root.get("createTime"),
                        queryRequest.getCreateTimeEnd()));
            }

            // 大于或等于 搜索条件:更新时间开始
            if (queryRequest.getUpdateTimeBegin() != null) {
                predicates.add(cbuild.greaterThanOrEqualTo(root.get("updateTime"),
                        queryRequest.getUpdateTimeBegin()));
            }
            // 小于或等于 搜索条件:更新时间截止
            if (queryRequest.getUpdateTimeEnd() != null) {
                predicates.add(cbuild.lessThanOrEqualTo(root.get("updateTime"),
                        queryRequest.getUpdateTimeEnd()));
            }

            // 模糊查询 - 创建人
            if (StringUtils.isNotEmpty(queryRequest.getCreatePerson())) {
                predicates.add(cbuild.like(root.get("createPerson"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getCreatePerson()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 - 更新人
            if (StringUtils.isNotEmpty(queryRequest.getUpdatePerson())) {
                predicates.add(cbuild.like(root.get("updatePerson"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getUpdatePerson()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);
            return p.length == 0 ? null : p.length == 1 ? p[0] : cbuild.and(p);
        };
    }
}
