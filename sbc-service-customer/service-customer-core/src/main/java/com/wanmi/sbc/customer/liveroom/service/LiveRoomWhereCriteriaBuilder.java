package com.wanmi.sbc.customer.liveroom.service;

import com.wanmi.sbc.common.util.StringUtil;
import com.wanmi.sbc.common.util.XssUtils;
import com.wanmi.sbc.customer.api.request.liveroom.LiveRoomQueryRequest;
import com.wanmi.sbc.customer.liveroom.model.root.LiveRoom;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>直播间动态查询条件构建器</p>
 * @author zwb
 * @date 2020-06-06 18:28:57
 */
public class LiveRoomWhereCriteriaBuilder {
    public static Specification<LiveRoom> build(LiveRoomQueryRequest queryRequest) {
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

            // 直播房间id
            if (queryRequest.getRoomId() != null) {
                predicates.add(cbuild.equal(root.get("roomId"), queryRequest.getRoomId()));
            }

            // 模糊查询 - 直播房间名
            if (StringUtils.isNotEmpty(queryRequest.getName())) {
                predicates.add(cbuild.like(root.get("name"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getName()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 - 是否推荐
            if (queryRequest.getRecommend() != null) {
                predicates.add(cbuild.equal(root.get("recommend"), queryRequest.getRecommend()));
            }

            // 大于或等于 搜索条件:开始时间开始
            if (queryRequest.getStartTime() != null) {
                predicates.add(cbuild.greaterThanOrEqualTo(root.get("startTime"),
                        queryRequest.getStartTime()));
            }
           /* // 小于或等于 搜索条件:开始时间截止
            if (queryRequest.getStartTimeEnd() != null) {
                predicates.add(cbuild.lessThanOrEqualTo(root.get("startTime"),
                        queryRequest.getStartTimeEnd()));
            }*/

           /* // 大于或等于 搜索条件:结束时间开始
            if (queryRequest.getEndTimeBegin() != null) {
                predicates.add(cbuild.greaterThanOrEqualTo(root.get("endTime"),
                        queryRequest.getEndTimeBegin()));
            }*/
            // 小于或等于 搜索条件:结束时间截止
            if (queryRequest.getEndTime() != null) {
                predicates.add(cbuild.lessThanOrEqualTo(root.get("endTime"),
                        queryRequest.getEndTime()));
            }

            // 模糊查询 - 主播昵称
            if (StringUtils.isNotEmpty(queryRequest.getAnchorName())) {
                predicates.add(cbuild.like(root.get("anchorName"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getAnchorName()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 - 主播微信
            if (StringUtils.isNotEmpty(queryRequest.getAnchorWechat())) {
                predicates.add(cbuild.like(root.get("anchorWechat"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getAnchorWechat()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 - 直播背景墙
            if (StringUtils.isNotEmpty(queryRequest.getCoverImg())) {
                predicates.add(cbuild.like(root.get("coverImg"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getCoverImg()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 - 分享卡片封面
            if (StringUtils.isNotEmpty(queryRequest.getShareImg())) {
                predicates.add(cbuild.like(root.get("shareImg"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getShareImg()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 直播状态 0: 直播中, 1: 未开始, 2: 已结束, 3: 禁播, 4: 暂停中, 5: 异常, 6: 已过期
            if (queryRequest.getLiveStatus() != null) {
                predicates.add(cbuild.equal(root.get("liveStatus"), queryRequest.getLiveStatus()));
            }

            // 直播类型，1：推流，0：手机直播
            if (queryRequest.getType() != null) {
                predicates.add(cbuild.equal(root.get("type"), queryRequest.getType()));
            }

            // 1：横屏，0：竖屏
            if (queryRequest.getScreenType() != null) {
                predicates.add(cbuild.equal(root.get("screenType"), queryRequest.getScreenType()));
            }

            // 1：关闭点赞 0：开启点赞，关闭后无法开启
            if (queryRequest.getCloseLike() != null) {
                predicates.add(cbuild.equal(root.get("closeLike"), queryRequest.getCloseLike()));
            }

            // 1：关闭货架 0：打开货架，关闭后无法开启
            if (queryRequest.getCloseGoods() != null) {
                predicates.add(cbuild.equal(root.get("closeGoods"), queryRequest.getCloseGoods()));
            }

            // 1：关闭评论 0：打开评论，关闭后无法开启
            if (queryRequest.getCloseComment() != null) {
                predicates.add(cbuild.equal(root.get("closeComment"), queryRequest.getCloseComment()));
            }

            // 店铺id
            if (queryRequest.getStoreId() != null) {
                predicates.add(cbuild.equal(root.get("storeId"), queryRequest.getStoreId()));
            }

            // 模糊查询 - 直播商户id
            if (StringUtils.isNotEmpty(queryRequest.getLiveCompanyId())) {
                predicates.add(cbuild.like(root.get("liveCompanyId"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getLiveCompanyId()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 - 创建人
            if (StringUtils.isNotEmpty(queryRequest.getCreatePerson())) {
                predicates.add(cbuild.like(root.get("createPerson"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getCreatePerson()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
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

            // 模糊查询 - 修改人
            if (StringUtils.isNotEmpty(queryRequest.getUpdatePerson())) {
                predicates.add(cbuild.like(root.get("updatePerson"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getUpdatePerson()))
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

            // 模糊查询 - 删除人
            if (StringUtils.isNotEmpty(queryRequest.getDeletePerson())) {
                predicates.add(cbuild.like(root.get("deletePerson"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getDeletePerson()))
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

            // 删除标识,0:未删除1:已删除
            if (queryRequest.getDelFlag() != null) {
                predicates.add(cbuild.equal(root.get("delFlag"), queryRequest.getDelFlag()));
            }

            //直播状态排序
            cquery.orderBy(cbuild.asc(root.get("liveStatus")),cbuild.asc(root.get("startTime")));

            Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);
            return p.length == 0 ? null : p.length == 1 ? p[0] : cbuild.and(p);
        };
    }
}
