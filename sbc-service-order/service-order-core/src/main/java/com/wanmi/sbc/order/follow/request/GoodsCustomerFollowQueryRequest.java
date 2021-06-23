package com.wanmi.sbc.order.follow.request;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.order.enums.FollowFlag;
import com.wanmi.sbc.order.follow.model.root.GoodsCustomerFollow;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 商品客户收藏查询请求
 * Created by daiyitian on 2017/5/17.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GoodsCustomerFollowQueryRequest extends BaseQueryRequest {

    /**
     * 编号
     */
    private List<Long> followIds;

    /**
     * SKU编号
     */
    private String goodsInfoId;

    /**
     * 批量SKU编号
     */
    private List<String> goodsInfoIds;

    /**
     * 收藏标识
     */
    private Integer followFlag;

    /**
     * 会员编号
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
     * 门店id
     */
    private Long companyInfoId;


    /**
     * 封装公共条件
     *
     * @return
     */
    public Specification<GoodsCustomerFollow> getWhereCriteria() {
        return (root, cquery, cbuild) -> {
            List<Predicate> predicates = new ArrayList<>();
            //批量编号
            if(CollectionUtils.isNotEmpty(followIds)){
                predicates.add(root.get("followId").in(followIds));
            }
            //客户编号
            if(StringUtils.isNotBlank(customerId)){
                predicates.add(cbuild.equal(root.get("customerId"), customerId));
            }
            //SKU编号
            if(StringUtils.isNotBlank(goodsInfoId)){
                predicates.add(cbuild.equal(root.get("goodsInfoId"), goodsInfoId));
            }
            //批量SKU编号
            if(CollectionUtils.isNotEmpty(goodsInfoIds)){
                predicates.add(root.get("goodsInfoId").in(goodsInfoIds));
            }
            //收藏标识
            if(followFlag != null){
                predicates.add(root.get("followFlag").in(FollowFlag.ALL.toValue(), followFlag));
            }
            //公司信息ID
            if(companyInfoId != null){
                predicates.add(cbuild.equal(root.get("companyInfoId"), companyInfoId));
            }
            Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);
            return p.length == 0 ? null : p.length == 1 ? p[0] : cbuild.and(p);
        };
    }
}
