package com.wanmi.sbc.goods.goodsevaluate.service;

import com.wanmi.sbc.common.util.DateUtil;
import com.wanmi.sbc.common.util.StringUtil;
import com.wanmi.sbc.common.util.XssUtils;
import com.wanmi.sbc.goods.api.request.goodsevaluate.GoodsEvaluateQueryRequest;
import com.wanmi.sbc.goods.goodsevaluate.model.root.GoodsEvaluate;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>商品评价动态查询条件构建器</p>
 * @author liutao
 * @date 2019-02-25 15:14:16
 */
public class GoodsEvaluateWhereCriteriaBuilder {
    public static Specification<GoodsEvaluate> build(GoodsEvaluateQueryRequest queryRequest) {
        return (root, cquery, cbuild) -> {
            List<Predicate> predicates = new ArrayList<>();
            // 批量查询-评价idList
            if (CollectionUtils.isNotEmpty(queryRequest.getEvaluateIdList())) {
                predicates.add(root.get("evaluateId").in(queryRequest.getEvaluateIdList()));
            }

            // 评价id
            if (StringUtils.isNotEmpty(queryRequest.getEvaluateId())) {
                predicates.add(cbuild.equal(root.get("evaluateId"), queryRequest.getEvaluateId()));
            }

            // 店铺Id
            if (queryRequest.getStoreId() != null) {
                predicates.add(cbuild.equal(root.get("storeId"), queryRequest.getStoreId()));
            }

            // 模糊查询 - 店铺名称
            if (StringUtils.isNotEmpty(queryRequest.getStoreName())) {
                predicates.add(cbuild.like(root.get("storeName"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getStoreName()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 - 商品id(spuId)
            if (StringUtils.isNotEmpty(queryRequest.getGoodsId())) {
                predicates.add(cbuild.like(root.get("goodsId"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getGoodsId()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 - 货品id(skuId)
            if (StringUtils.isNotEmpty(queryRequest.getGoodsInfoId())) {
                predicates.add(cbuild.like(root.get("goodsInfoId"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getGoodsInfoId()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 - 商品名称
            if (StringUtils.isNotEmpty(queryRequest.getGoodsInfoName())) {
                predicates.add(cbuild.like(root.get("goodsInfoName"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getGoodsInfoName()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            //批量查询-订单号
            if (CollectionUtils.isNotEmpty(queryRequest.getOrderNos())) {
                predicates.add(root.get("orderNo").in(queryRequest.getOrderNos()));
            }

            // 模糊查询 - 订单号
            if (StringUtils.isNotEmpty(queryRequest.getOrderNo())) {
                predicates.add(cbuild.like(root.get("orderNo"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getOrderNo()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 - 会员Id
            if (StringUtils.isNotEmpty(queryRequest.getCustomerId())) {
                predicates.add(cbuild.like(root.get("customerId"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getCustomerId()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 - 会员名称
            if (StringUtils.isNotEmpty(queryRequest.getCustomerName())) {
                predicates.add(cbuild.like(root.get("customerName"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getCustomerName()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 - 会员登录账号|手机号
            if (StringUtils.isNotEmpty(queryRequest.getCustomerAccount())) {
                predicates.add(cbuild.like(root.get("customerAccount"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getCustomerAccount()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 商品评分
            if (queryRequest.getEvaluateScore() != null) {
                predicates.add(cbuild.equal(root.get("evaluateScore"), queryRequest.getEvaluateScore()));
            }

            // 模糊查询 - 商品评价内容
            if (StringUtils.isNotEmpty(queryRequest.getEvaluateContent())) {
                predicates.add(cbuild.like(root.get("evaluateContent"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getEvaluateContent()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 大于或等于 搜索条件:发表评价时间开始
            if (queryRequest.getEvaluateTimeBegin() != null) {
                predicates.add(cbuild.greaterThanOrEqualTo(root.get("evaluateTime"),
                        queryRequest.getEvaluateTimeBegin()));
            }
            // 小于或等于 搜索条件:发表评价时间截止
            if (queryRequest.getEvaluateTimeEnd() != null) {
                predicates.add(cbuild.lessThanOrEqualTo(root.get("evaluateTime"),
                        queryRequest.getEvaluateTimeEnd()));
            }

            // 模糊查询 - 评论回复
            if (StringUtils.isNotEmpty(queryRequest.getEvaluateAnswer())) {
                predicates.add(cbuild.like(root.get("evaluateAnswer"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getEvaluateAnswer()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 大于或等于 搜索条件:回复时间开始
            if (queryRequest.getEvaluateAnswerTimeBegin() != null) {
                predicates.add(cbuild.greaterThanOrEqualTo(root.get("evaluateAnswerTime"),
                        queryRequest.getEvaluateAnswerTimeBegin()));
            }
            // 小于或等于 搜索条件:回复时间截止
            if (queryRequest.getEvaluateAnswerTimeEnd() != null) {
                predicates.add(cbuild.lessThanOrEqualTo(root.get("evaluateAnswerTime"),
                        queryRequest.getEvaluateAnswerTimeEnd()));
            }

            // 模糊查询 - 回复人账号
            if (StringUtils.isNotEmpty(queryRequest.getEvaluateAnswerAccountName())) {
                predicates.add(cbuild.like(root.get("evaluateAnswerAccountName"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getEvaluateAnswerAccountName()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 - 回复员工Id
            if (StringUtils.isNotEmpty(queryRequest.getEvaluateAnswerEmployeeId())) {
                predicates.add(cbuild.like(root.get("evaluateAnswerEmployeeId"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getEvaluateAnswerEmployeeId()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 历史商品评分
            if (queryRequest.getHistoryEvaluateScore() != null) {
                predicates.add(cbuild.equal(root.get("historyEvaluateScore"), queryRequest.getHistoryEvaluateScore()));
            }

            // 模糊查询 - 历史商品评价内容
            if (StringUtils.isNotEmpty(queryRequest.getHistoryEvaluateContent())) {
                predicates.add(cbuild.like(root.get("historyEvaluateContent"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getHistoryEvaluateContent()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 大于或等于 搜索条件:历史发表评价时间开始
            if (queryRequest.getHistoryEvaluateTimeBegin() != null) {
                predicates.add(cbuild.greaterThanOrEqualTo(root.get("historyEvaluateTime"),
                        queryRequest.getHistoryEvaluateTimeBegin()));
            }
            // 小于或等于 搜索条件:历史发表评价时间截止
            if (queryRequest.getHistoryEvaluateTimeEnd() != null) {
                predicates.add(cbuild.lessThanOrEqualTo(root.get("historyEvaluateTime"),
                        queryRequest.getHistoryEvaluateTimeEnd()));
            }

            // 模糊查询 - 历史评论回复
            if (StringUtils.isNotEmpty(queryRequest.getHistoryEvaluateAnswer())) {
                predicates.add(cbuild.like(root.get("historyEvaluateAnswer"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getHistoryEvaluateAnswer()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 大于或等于 搜索条件:历史回复时间开始
            if (queryRequest.getHistoryEvaluateAnswerTimeBegin() != null) {
                predicates.add(cbuild.greaterThanOrEqualTo(root.get("historyEvaluateAnswerTime"),
                        queryRequest.getHistoryEvaluateAnswerTimeBegin()));
            }
            // 小于或等于 搜索条件:历史回复时间截止
            if (queryRequest.getHistoryEvaluateAnswerTimeEnd() != null) {
                predicates.add(cbuild.lessThanOrEqualTo(root.get("historyEvaluateAnswerTime"),
                        queryRequest.getHistoryEvaluateAnswerTimeEnd()));
            }

            // 模糊查询 - 历史回复人账号
            if (StringUtils.isNotEmpty(queryRequest.getHistoryEvaluateAnswerAccountName())) {
                predicates.add(cbuild.like(root.get("historyEvaluateAnswerAccountName"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getHistoryEvaluateAnswerAccountName()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 - 历史回复员工Id
            if (StringUtils.isNotEmpty(queryRequest.getHistoryEvaluateAnswerEmployeeId())) {
                predicates.add(cbuild.like(root.get("historyEvaluateAnswerEmployeeId"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getHistoryEvaluateAnswerEmployeeId()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 点赞数
            if (queryRequest.getGoodNum() != null) {
                predicates.add(cbuild.equal(root.get("goodNum"), queryRequest.getGoodNum()));
            }

            // 是否匿名 0：否，1：是
            if (queryRequest.getIsAnonymous() != null) {
                predicates.add(cbuild.equal(root.get("isAnonymous"), queryRequest.getIsAnonymous()));
            }

            // 是否已回复 0：否，1：是
            if (queryRequest.getIsAnswer() != null) {
                predicates.add(cbuild.equal(root.get("isAnswer"), queryRequest.getIsAnswer()));
            }

            // 是否已经修改 0：否，1：是
            if (queryRequest.getIsEdit() != null) {
                predicates.add(cbuild.equal(root.get("isEdit"), queryRequest.getIsEdit()));
            }

            // 是否展示 0：否，1：是
            if (queryRequest.getIsShow() != null) {
                predicates.add(cbuild.equal(root.get("isShow"), queryRequest.getIsShow()));
            }

            // 是否晒单 0：否，1：是
            if (queryRequest.getIsUpload() != null) {
                predicates.add(cbuild.equal(root.get("isUpload"), queryRequest.getIsUpload()));
            }

            // 是否删除标志 0：否，1：是
            if (queryRequest.getDelFlag() != null) {
                predicates.add(cbuild.equal(root.get("delFlag"), queryRequest.getDelFlag()));
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

            // 模糊查询 - 创建人
            if (StringUtils.isNotEmpty(queryRequest.getCreatePerson())) {
                predicates.add(cbuild.like(root.get("createPerson"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getCreatePerson()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 大于或等于 搜索条件:修改时间开始
            if (queryRequest.getUpdateTimeBegin() != null) {
                predicates.add(cbuild.greaterThanOrEqualTo(root.get("updateTime"),
                        queryRequest.getUpdateTimeBegin()));
            }
            // 小于或等于 搜索条件:修改时间截止
            if (queryRequest.getUpdateTimeEnd() != null) {
                predicates.add(cbuild.lessThanOrEqualTo(root.get("updateTime"),
                        queryRequest.getUpdateTimeEnd()));
            }

            // 模糊查询 - 修改人
            if (StringUtils.isNotEmpty(queryRequest.getUpdatePerson())) {
                predicates.add(cbuild.like(root.get("updatePerson"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getUpdatePerson()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 大于或等于 搜索条件:删除时间开始
            if (queryRequest.getDelTimeBegin() != null) {
                predicates.add(cbuild.greaterThanOrEqualTo(root.get("delTime"),
                        queryRequest.getDelTimeBegin()));
            }
            // 小于或等于 搜索条件:删除时间截止
            if (queryRequest.getDelTimeEnd() != null) {
                predicates.add(cbuild.lessThanOrEqualTo(root.get("delTime"),
                        queryRequest.getDelTimeEnd()));
            }

            // 模糊查询 - 删除人
            if (StringUtils.isNotEmpty(queryRequest.getDelPerson())) {
                predicates.add(cbuild.like(root.get("delPerson"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getDelPerson()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 大于或等于 搜索条件:发表评价时间开始
            if (StringUtils.isNotEmpty(queryRequest.getBeginTime())) {
                predicates.add(cbuild.greaterThanOrEqualTo(root.get("evaluateTime"),
                        DateUtil.parseDay(queryRequest.getBeginTime())));
            }
            // 小于或等于 搜索条件:发表评价时间截止
            if (queryRequest.getEndTime() != null) {
                predicates.add(cbuild.lessThanOrEqualTo(root.get("evaluateTime"),
                        DateUtil.parseDay(queryRequest.getEndTime()).plusDays(1)));
            }
            cquery.orderBy(cbuild.desc(root.get("evaluateTime")));
            Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);
            return p.length == 0 ? null : p.length == 1 ? p[0] : cbuild.and(p);
        };
    }
}
