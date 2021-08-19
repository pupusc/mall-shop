package com.wanmi.sbc.goods.storecate.request;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.StringUtil;
import com.wanmi.sbc.goods.storecate.model.root.StoreCate;
import com.wanmi.sbc.goods.util.XssUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: bail
 * Time: 2017/11/13.10:22
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StoreCateQueryRequest extends BaseQueryRequest {

    /**
     * 店铺分类标识
     */
    private Long storeCateId;

    /**
     * 批量店铺分类标识
     */
    private List<Long> storeCateIds;

    /**
     * 店铺标识
     */
    private Long storeId;

    /**
     * 店铺分类名称
     */
    private String cateName;

    /**
     * 父分类标识
     */
    private Long cateParentId;

    /**
     * 分类图片
     */
    private String cateImg;

    /**
     * 分类路径
     */
    private String catePath;

    /**
     * 分类层次
     */
    private Integer cateGrade;

    /**
     * 删除标记
     */
    private DeleteFlag delFlag;

    /**
     * 默认标记
     */
    private DefaultFlag isDefault;

    /**
     * 模糊查询，分类路径
     */
    private String likeCatePath;

    /**
     * 封装公共的查询条件
     * @return
     */
    public Specification<StoreCate> getWhereCriteria(){
        return (root, cquery, cbuild) -> {
            List<Predicate> predicates = new ArrayList<>();
            //批量分类id
            if(CollectionUtils.isNotEmpty(storeCateIds)){
                predicates.add(root.get("storeCateId").in(storeCateIds));
            }
            //店铺id
            if(storeId != null){
                predicates.add(cbuild.equal(root.get("storeId"), storeId));
            }
            //商品分类名称
            if(cateName != null){
                predicates.add(cbuild.equal(root.get("cateName"), cateName));
            }
            //默认标记
            if(isDefault != null){
                predicates.add(cbuild.equal(root.get("isDefault"), isDefault.toValue()));
            }
            //删除标记
            if(delFlag != null){
                predicates.add(cbuild.equal(root.get("delFlag"), delFlag.toValue()));
            }
            //分类层级
            if(cateGrade != null){
                predicates.add(cbuild.equal(root.get("cateGrade"),cateGrade));
            }
            //父分类id
            if(cateParentId != null){
                predicates.add(cbuild.equal(root.get("cateParentId"), cateParentId));
            }
            //模糊查询，分类路径做为前缀
            if(StringUtils.isNotEmpty(likeCatePath)){
                predicates.add(cbuild.like(root.get("catePath"), XssUtils.replaceLikeWildcard(likeCatePath).concat(StringUtil.SQL_LIKE_CHAR)));
            }

            if(predicates.isEmpty()){
                return null;
            }else if(predicates.size() == 1){
                return predicates.get(0);
            }else{
                return cbuild.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
    }
}
