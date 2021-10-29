package com.wanmi.sbc.goods.info.request;

import com.wanmi.ares.enums.CheckStatus;
import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.common.util.StringUtil;
import com.wanmi.sbc.common.util.XssUtils;
import com.wanmi.sbc.goods.bean.enums.GoodsAdAuditStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ObjectUtils;
import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.data.jpa.domain.Specification;
import com.wanmi.sbc.goods.info.model.root.GoodsSync;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GoodsSyncQueryRequest extends BaseQueryRequest {


    @ApiModelProperty("isbn")
    private List<String> isbn;
    @ApiModelProperty("商品名称")
    private String goodsName;

    @ApiModelProperty("作者")
    private String author;

    @ApiModelProperty("分类")
    private Integer category;

    @ApiModelProperty("出版社")
    private String publishName;

    @ApiModelProperty("供应商名称")
    private String supplierName;

    @ApiModelProperty("广告法审核状态")
    private Integer adAuditStatus;

    @ApiModelProperty("广告法人工审核状态")
    private Integer adManualAuditStatus;

    @ApiModelProperty("上架审核状态")
    private Integer launchAuditStatus;

    @ApiModelProperty("上架状态")
    private Integer status;

    @ApiModelProperty("出版时间-开始")
    private LocalDateTime publishTimeBegin;

    @ApiModelProperty("出版时间-结束")
    private LocalDateTime publishTimeEnd;



    public Specification<GoodsSync> getWhereCriteria() {
        return (root, cquery, cbuild) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(isbn)) {
                predicates.add(root.get("isbn").in(isbn));
            }

            //模糊查询
            if (StringUtils.isNotEmpty(goodsName)) {
                predicates.add(cbuild.like(root.get("title"), StringUtil.SQL_LIKE_CHAR.concat(XssUtils.replaceLikeWildcard(goodsName.trim())).concat(StringUtil.SQL_LIKE_CHAR)));
            }
            if (StringUtils.isNotBlank(author)) {
                predicates.add(cbuild.like(root.get("author"), StringUtil.SQL_LIKE_CHAR.concat(XssUtils.replaceLikeWildcard(author.trim())).concat(StringUtil.SQL_LIKE_CHAR)));
            }
            if (StringUtils.isNotEmpty(publishName)) {
                predicates.add(cbuild.like(root.get("publishName"), StringUtil.SQL_LIKE_CHAR.concat(XssUtils.replaceLikeWildcard(publishName.trim())).concat(StringUtil.SQL_LIKE_CHAR)));
            }
            if (StringUtils.isNotEmpty(supplierName)) {
                predicates.add(cbuild.like(root.get("supplierName"), StringUtil.SQL_LIKE_CHAR.concat(XssUtils.replaceLikeWildcard(supplierName.trim())).concat(StringUtil.SQL_LIKE_CHAR)));
            }
            if (publishTimeBegin != null) {
                predicates.add(cbuild.lessThanOrEqualTo(root.get("pulicateDate"), publishTimeBegin));
            }
            if (publishTimeEnd != null) {
                predicates.add(cbuild.greaterThanOrEqualTo(root.get("pulicateDate"), publishTimeEnd));
            }
            //审核状态
            if(adAuditStatus != null){
                //审核中包含两个状态
                if(adAuditStatus.equals(GoodsAdAuditStatus.WAITTOAUDIT.toValue())){
                    predicates.add(root.get("adAuditStatus").in(GoodsAdAuditStatus.WAITTOAUDIT.toValue(),GoodsAdAuditStatus.AUDITING.toValue()));
                }else {
                    predicates.add(cbuild.equal(root.get("adAuditStatus"), adAuditStatus));
                }
            }
            if(adManualAuditStatus != null){
                predicates.add(cbuild.equal(root.get("adManualAuditStatus"), adManualAuditStatus));
            }
            if(launchAuditStatus != null){
                predicates.add(cbuild.equal(root.get("launchAuditStatus"), launchAuditStatus));
            }
            if(status != null){
                predicates.add(cbuild.equal(root.get("status"), status));
            }
            Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);
            return p.length == 0 ? null : p.length == 1 ? p[0] : cbuild.and(p);
        };
    }
}
