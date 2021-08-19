package com.wanmi.sbc.goods.goodsevaluateimage.service;

import com.wanmi.sbc.common.util.StringUtil;
import com.wanmi.sbc.common.util.XssUtils;
import com.wanmi.sbc.goods.api.request.goodsevaluateimage.GoodsEvaluateImageQueryRequest;
import com.wanmi.sbc.goods.goodsevaluateimage.model.root.GoodsEvaluateImage;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>商品评价图片动态查询条件构建器</p>
 * @author liutao
 * @date 2019-02-26 09:56:17
 */
public class GoodsEvaluateImageWhereCriteriaBuilder {
    public static Specification<GoodsEvaluateImage> build(GoodsEvaluateImageQueryRequest queryRequest) {
        return (root, cquery, cbuild) -> {
            List<Predicate> predicates = new ArrayList<>();
            // 批量查询-图片IdList
            if (CollectionUtils.isNotEmpty(queryRequest.getImageIdList())) {
                predicates.add(root.get("imageId").in(queryRequest.getImageIdList()));
            }

            // 图片Id
            if (StringUtils.isNotEmpty(queryRequest.getImageId())) {
                predicates.add(cbuild.equal(root.get("imageId"), queryRequest.getImageId()));
            }

            if (StringUtils.isNotEmpty(queryRequest.getGoodsId())) {
                predicates.add(cbuild.equal(root.get("goodsId"), queryRequest.getGoodsId()));
            }

            // 模糊查询 - 评价id
            if (StringUtils.isNotEmpty(queryRequest.getEvaluateId())) {
                predicates.add(cbuild.like(root.get("evaluateId"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getEvaluateId()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 - 图片KEY
            if (StringUtils.isNotEmpty(queryRequest.getImageKey())) {
                predicates.add(cbuild.like(root.get("imageKey"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getImageKey()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 - 图片名称
            if (StringUtils.isNotEmpty(queryRequest.getImageName())) {
                predicates.add(cbuild.like(root.get("imageName"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getImageName()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 - 原图地址
            if (StringUtils.isNotEmpty(queryRequest.getArtworkUrl())) {
                predicates.add(cbuild.like(root.get("artworkUrl"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getArtworkUrl()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            if (queryRequest.getIsShow() != null) {
                predicates.add(cbuild.equal(root.get("isShow"), queryRequest.getIsShow()));
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

            // 删除标识,0:未删除1:已删除
            if (queryRequest.getDelFlag() != null) {
                predicates.add(cbuild.equal(root.get("delFlag"), queryRequest.getDelFlag()));
            }

            Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);
            return p.length == 0 ? null : p.length == 1 ? p[0] : cbuild.and(p);
        };
    }
}
