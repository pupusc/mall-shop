package com.wanmi.sbc.goods.cate.request;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.common.util.StringUtil;
import com.wanmi.sbc.goods.cate.model.root.GoodsCate;
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
 * 分类查询请求
 * Created by daiyitian on 2017/3/24.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GoodsCateQueryRequest extends BaseQueryRequest {

    /**
     * 分类编号
     */
    private Long cateId;

    /**
     * 批量分类编号
     */
    private List<Long> cateIds;

    /**
     * 批量分类父编号
     */
    private List<Long> cateParentIds;

    /**
     * 分类父编号
     */
    private Long cateParentId;

    /**
     * 模糊查询，分类路径
     */
    private String likeCatePath;

    /**
     * 分类层级
     */
    private Integer cateGrade;

    /**
     * 删除标记
     */
    private Integer delFlag;

    /**
     * 是否默认
     */
    private Integer isDefault;

    /**
     * 父类名称
     */
    private String cateName;

    /**
     * 非分类编号
     */
    private Long notCateId;

    /**
     * 关键字查询，可能含空格
     */
    private String keywords;

    /**
     * 封装公共条件
     * @return
     */
    public Specification<GoodsCate> getWhereCriteria(){
        return (root, cquery, cbuild) -> {
            List<Predicate> predicates = new ArrayList<>();
            //批量分类编号
            if(CollectionUtils.isNotEmpty(cateIds)){
                predicates.add(root.get("cateId").in(cateIds));
            }
            //批量分类父编号
            if(CollectionUtils.isNotEmpty(cateParentIds)){
                predicates.add(root.get("cateParentId").in(cateParentIds));
            }
            //查询子类
            if(cateParentId != null){
                predicates.add(cbuild.equal(root.get("cateParentId"),cateParentId));
            }
            //分类名称
            if(StringUtils.isNotBlank(cateName)){
                predicates.add(cbuild.like(root.get("cateName"), XssUtils.replaceLikeWildcard(cateName.trim()).concat(StringUtil.SQL_LIKE_CHAR)));
            }
            //默认标记
            if(isDefault != null){
                predicates.add(cbuild.equal(root.get("isDefault"), isDefault));
            }
            //删除标记
            if(delFlag != null){
                predicates.add(cbuild.equal(root.get("delFlag"), delFlag));
            }
            //模糊查询，分类路径做为前缀
            if(StringUtils.isNotEmpty(likeCatePath)){
                predicates.add(cbuild.like(root.get("catePath"), XssUtils.replaceLikeWildcard(likeCatePath).concat(StringUtil.SQL_LIKE_CHAR)));
            }
            //分类层级
            if(cateGrade != null){
                predicates.add(cbuild.equal(root.get("cateGrade"),cateGrade));
            }
            //非分类编号
            if(notCateId != null){
                predicates.add(cbuild.notEqual(root.get("cateId"), notCateId));
            }

            //关键字查询
            if(StringUtils.isNotBlank(keywords)){
                String[] t_keywords = StringUtils.split(keywords);
                if(t_keywords.length > 0) {
                    List<Predicate> keywordPredicates = new ArrayList<>();
                    for (String keyword : t_keywords) {
                        keywordPredicates.add(cbuild.like(root.get("cateName"), StringUtil.SQL_LIKE_CHAR.concat(XssUtils.replaceLikeWildcard(keyword.trim())).concat(StringUtil.SQL_LIKE_CHAR)));
                    }
                    predicates.add(cbuild.or(keywordPredicates.toArray(new Predicate[keywordPredicates.size()])));
                }
            }

            Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);
            return p.length == 0 ? null : p.length == 1 ? p[0] : cbuild.and(p);
        };
    }
}
