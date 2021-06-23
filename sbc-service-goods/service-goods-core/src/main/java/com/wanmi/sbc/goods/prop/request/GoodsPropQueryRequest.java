package com.wanmi.sbc.goods.prop.request;

import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.goods.prop.model.root.GoodsProp;
import com.wanmi.sbc.goods.prop.model.root.GoodsPropDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 商品类目属性查询request
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GoodsPropQueryRequest {

    /**
     * 属性Id
     */
    private Long propId;

    /**
     * 分类Id
     */
    private Long cateId;

    /**
     *属性名
     */
    private String propName;

    /**
     *是否开启索引
     */
    private DefaultFlag indexFlag;

    /**
     *创建时间
     */
    private LocalDateTime createTime;

    /**
     *修改时间
     */
    private LocalDateTime updateTime;

    /**
     *删除标记
     */
    private DeleteFlag delFlag;

    /**
     *排序
     */
    private Integer sort;

    /**
     *商品属性详情
     */
    private List<GoodsPropDetail> goodsPropDetails;

    /**
     *商品属性详情字符串
     */
    private String propDetailStr;

    /**
     * 封装公共条件
     * @return
     */
    public Specification<GoodsProp> getWhereCriteria() {
        return (root, cquery, cbuild) -> {
            List<Predicate> predicates = new ArrayList<>();
            if(cateId!=null){
                predicates.add(cbuild.equal(root.get("cateId"), cateId));
            }
            if(propName!=null){
                predicates.add(cbuild.equal(root.get("propName"), propName));
            }
            if(delFlag!=null){
                predicates.add(cbuild.equal(root.get("delFlag"), delFlag));
            }
            Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);
            return p.length == 0 ? null : p.length == 1 ? p[0] : cbuild.and(p);
        };
    }

}
