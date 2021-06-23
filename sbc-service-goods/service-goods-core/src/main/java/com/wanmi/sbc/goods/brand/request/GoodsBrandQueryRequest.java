package com.wanmi.sbc.goods.brand.request;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.common.util.StringUtil;
import com.wanmi.sbc.goods.brand.model.root.GoodsBrand;
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
 * 品牌查询请求
 * Created by daiyitian on 2017/3/24.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GoodsBrandQueryRequest extends BaseQueryRequest {

    /**
     * 批量品牌编号
     */
    private List<Long> brandIds;

    /**
     * and 精准查询，品牌名称
     */
    private String brandName;

    /**
     * and 模糊查询，品牌名称
     */
    private String likeBrandName;

    /**
     * and 精准查询，品牌昵称
     */
    private String nickName;

    /**
     * and 模糊查询，品牌昵称
     */
    private String likeNickName;

    /**
     * 模糊查询，品牌拼音
     */
    private String likePinYin;

    /**
     * 删除标记
     */
    private Integer delFlag;

    /**
     * 非品牌编号
     */
    private Long notBrandId;

    /**
     * 关键字查询，可能含空格
     */
    private String keywords;

    /**
     * 封装公共条件
     * @return
     */
    public Specification<GoodsBrand> getWhereCriteria(){
        return (root, cquery, cbuild) -> {
            List<Predicate> predicates = new ArrayList<>();
            //批量品牌编号
            if(CollectionUtils.isNotEmpty(brandIds)){
                predicates.add(root.get("brandId").in(brandIds));
            }
            //查询名称
            if(StringUtils.isNotEmpty(brandName)){
                predicates.add(cbuild.equal(root.get("brandName"),brandName.trim()));
            }
            //模糊查询名称
            if(StringUtils.isNotEmpty(likeBrandName)){
                predicates.add(cbuild.like(root.get("brandName"), StringUtil.SQL_LIKE_CHAR.concat(XssUtils.replaceLikeWildcard(likeBrandName).trim()).concat(StringUtil.SQL_LIKE_CHAR)));
            }
            //查询昵称
            if(StringUtils.isNotEmpty(nickName)){
                predicates.add(cbuild.equal(root.get("nickName"),nickName.trim()));
            }
            //模糊查询昵称
            if(StringUtils.isNotEmpty(likeNickName)){
                predicates.add(cbuild.like(root.get("nickName"), StringUtil.SQL_LIKE_CHAR.concat(XssUtils.replaceLikeWildcard(likeNickName).trim()).concat(StringUtil.SQL_LIKE_CHAR)));
            }
            //模糊查询拉莫
            if(StringUtils.isNotEmpty(likePinYin)){
                predicates.add(cbuild.like(root.get("pinYin"), StringUtil.SQL_LIKE_CHAR.concat(XssUtils.replaceLikeWildcard(likePinYin.trim())).concat(StringUtil.SQL_LIKE_CHAR)));
            }
            //删除标记
            if(delFlag != null){
                predicates.add(cbuild.equal(root.get("delFlag"),delFlag));
            }
            //非品牌编号
            if(notBrandId != null){
                predicates.add(cbuild.notEqual(root.get("brandId"), notBrandId));
            }
            //关键字查询
            if(StringUtils.isNotBlank(keywords)){
                String[] t_keywords = StringUtils.split(keywords);
                if(t_keywords.length > 0) {
                    List<Predicate> keywordPredicates = new ArrayList<>();
                    for (String keyword : t_keywords) {
                        keywordPredicates.add(cbuild.like(root.get("brandName"), StringUtil.SQL_LIKE_CHAR.concat(XssUtils.replaceLikeWildcard(keyword.trim())).concat(StringUtil.SQL_LIKE_CHAR)));
                    }
                    predicates.add(cbuild.or(keywordPredicates.toArray(new Predicate[keywordPredicates.size()])));
                }
            }

            Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);
            return p.length == 0 ? null : p.length == 1 ? p[0] : cbuild.and(p);
        };
    }
}
