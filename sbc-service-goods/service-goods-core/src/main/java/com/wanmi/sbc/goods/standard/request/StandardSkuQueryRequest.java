package com.wanmi.sbc.goods.standard.request;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.common.util.StringUtil;
import com.wanmi.sbc.goods.standard.model.root.StandardSku;
import com.wanmi.sbc.goods.util.XssUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 商品库SKU查询请求
 * Created by daiyitian on 2017/3/24.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StandardSkuQueryRequest extends BaseQueryRequest implements Serializable {

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
     * 分类编号
     */
    private Long cateId;

    /**
     * 模糊条件-商品名称
     */
    private String likeGoodsName;

    /**
     * 删除标记
     */
    private Integer delFlag;

    /**
     * 非GoodsId
     */
    private String notGoodsId;

    /**
     * 非GoodsInfoId
     */
    private String notGoodsInfoId;

    private String likeGoodsInfoNo;

    /**
     * 封装公共条件
     *
     * @return
     */
    public Specification<StandardSku> getWhereCriteria() {
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
            //批量SPU编号
            if(CollectionUtils.isNotEmpty(goodsIds)){
                predicates.add(root.get("goodsId").in(goodsIds));
            }
            //模糊查询名称
            if(StringUtils.isNotEmpty(likeGoodsName)){
                predicates.add(cbuild.like(root.get("goodsInfoName"), StringUtil.SQL_LIKE_CHAR.concat(XssUtils.replaceLikeWildcard(likeGoodsName.trim())).concat(StringUtil.SQL_LIKE_CHAR)));
            }
            //SKU编码
            if(StringUtils.isNotEmpty(likeGoodsInfoNo)){
                predicates.add(cbuild.like(root.get("goodsInfoNo"), StringUtil.SQL_LIKE_CHAR.concat(XssUtils.replaceLikeWildcard(likeGoodsInfoNo.trim())).concat(StringUtil.SQL_LIKE_CHAR)));
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
            Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);
            return p.length == 0 ? null : p.length == 1 ? p[0] : cbuild.and(p);
        };
    }
}
