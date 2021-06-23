package com.wanmi.sbc.goods.livegoods.service;

import com.wanmi.sbc.common.util.StringUtil;
import com.wanmi.sbc.common.util.XssUtils;
import com.wanmi.sbc.goods.api.request.livegoods.LiveGoodsQueryRequest;
import com.wanmi.sbc.goods.livegoods.model.root.LiveGoods;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>直播商品动态查询条件构建器</p>
 * @author zwb
 * @date 2020-06-10 11:05:45
 */
public class LiveGoodsWhereCriteriaBuilder {
    public static Specification<LiveGoods> build(LiveGoodsQueryRequest queryRequest) {
        return (root, cquery, cbuild) -> {
            List<Predicate> predicates = new ArrayList<>();
            // 批量查询-主键idList
            if (CollectionUtils.isNotEmpty(queryRequest.getIdList())) {
                predicates.add(root.get("id").in(queryRequest.getIdList()));
            }

            // 主键id
            if (queryRequest.getId() != null) {
                predicates.add(cbuild.equal(root.get("id"), queryRequest.getId()));
            }

            if (queryRequest.getGoodsId() != null) {
                predicates.add(cbuild.equal(root.get("goodsId"), queryRequest.getGoodsId()));
            }

            // 模糊查询 - 商品标题
            if (StringUtils.isNotEmpty(queryRequest.getName())) {
                predicates.add(cbuild.like(root.get("name"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getName()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 - 填入mediaID
            if (StringUtils.isNotEmpty(queryRequest.getCoverImgUrl())) {
                predicates.add(cbuild.like(root.get("coverImgUrl"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getCoverImgUrl()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 价格类型，1：一口价，2：价格区间，3：显示折扣价
            if (queryRequest.getPriceType() != null) {
                predicates.add(cbuild.equal(root.get("priceType"), queryRequest.getPriceType()));
            }

            // 直播商品价格左边界
            if (queryRequest.getPrice() != null) {
                predicates.add(cbuild.equal(root.get("price"), queryRequest.getPrice()));
            }

            // 直播商品价格右边界
            if (queryRequest.getPrice2() != null) {
                predicates.add(cbuild.equal(root.get("price2"), queryRequest.getPrice2()));
            }

            // 模糊查询 - 商品详情页的小程序路径
            if (StringUtils.isNotEmpty(queryRequest.getUrl())) {
                predicates.add(cbuild.like(root.get("url"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getUrl()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 库存
            if (queryRequest.getStock() != null) {
                predicates.add(cbuild.equal(root.get("stock"), queryRequest.getStock()));
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

            // 商品详情id
            if (queryRequest.getGoodsInfoId() != null) {
                predicates.add(cbuild.equal(root.get("goodsInfoId"), queryRequest.getGoodsInfoId()));
            }

            // 店铺标识
            if (queryRequest.getStoreId() != null) {
                predicates.add(cbuild.equal(root.get("storeId"), queryRequest.getStoreId()));
            }

            // 大于或等于 搜索条件:提交审核时间开始
            if (queryRequest.getSubmitTimeBegin() != null) {
                predicates.add(cbuild.greaterThanOrEqualTo(root.get("submitTime"),
                        queryRequest.getSubmitTimeBegin()));
            }
            // 小于或等于 搜索条件:提交审核时间截止
            if (queryRequest.getSubmitTimeEnd() != null) {
                predicates.add(cbuild.lessThanOrEqualTo(root.get("submitTime"),
                        queryRequest.getSubmitTimeEnd()));
            }

            // 审核单ID
            if (queryRequest.getAuditId() != null) {
                predicates.add(cbuild.equal(root.get("auditId"), queryRequest.getAuditId()));
            }

            // 审核状态,0:未审核1 审核通过2审核失败3禁用中
            if (queryRequest.getAuditStatus() != null) {
                predicates.add(cbuild.equal(root.get("auditStatus"), queryRequest.getAuditStatus()));
            }

            // 模糊查询 - 审核原因
            if (StringUtils.isNotEmpty(queryRequest.getAuditReason())) {
                predicates.add(cbuild.like(root.get("auditReason"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getAuditReason()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 大于或等于 搜索条件:删除时间开始
            if (queryRequest.getDeleteTimeBegin() != null) {
                predicates.add(cbuild.greaterThanOrEqualTo(root.get("deleteTime"),
                        queryRequest.getDeleteTimeBegin()));
            }
            // 小于或等于 搜索条件:删除时间截止
            if (queryRequest.getDeleteTimeEnd() != null) {
                predicates.add(cbuild.lessThanOrEqualTo(root.get("deleteTime"),
                        queryRequest.getDeleteTimeEnd()));
            }

            // 模糊查询 - 删除人
            if (StringUtils.isNotEmpty(queryRequest.getDeletePerson())) {
                predicates.add(cbuild.like(root.get("deletePerson"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getDeletePerson()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 删除标记 0:未删除1:已删除
            if (queryRequest.getDelFlag() != null) {
                predicates.add(cbuild.equal(root.get("delFlag"), queryRequest.getDelFlag()));
            }

            Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);
            return p.length == 0 ? null : p.length == 1 ? p[0] : cbuild.and(p);
        };
    }
}
