package com.wanmi.sbc.goods.standard.request;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.common.enums.ThirdPlatformType;
import com.wanmi.sbc.common.util.StringUtil;
import com.wanmi.sbc.goods.standard.model.root.StandardGoods;
import com.wanmi.sbc.goods.util.XssUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 商品库查询请求
 * Created by daiyitian on 2017/3/24.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StandardQueryRequest extends BaseQueryRequest {

    /**
     * 批量SPU编号
     */
    private List<String> goodsIds;

    /**
     * 模糊条件-商品名称
     */
    private String likeGoodsName;

    /**
     * 模糊条件-供应商名称
     */
    private String likeProviderName;

    private String likeGoodsInfoNo;

    private String likeGoodsNo;

    /**
     * 商品来源，0供应商，1商家
     */
    private Integer goodsSource;

    /**
     *
     */
    private List<Integer> goodsSourceList;

    /**
     * 商品分类
     */
    private Long cateId;

    /**
     * 批量商品分类
     */
    private List<Long> cateIds;

    /**
     * 品牌编号
     */
    private Long brandId;

    /**
     * 批量品牌分类
     */
    private List<Long> brandIds;

    /**
     * 批量品牌分类，可与NULL以or方式查询
     */
    private List<Long> orNullBrandIds;

    /**
     * 删除标记
     */
    private Integer delFlag;

    /**
     * 非GoodsId
     */
    private String notGoodsId;
    /**
     * 店铺主键
     */
    private Long storeId;
    /**
     * 导入状态  -1 全部 1 已导入 2未导入
     */
    private Integer toLeadType;
    /**
     * 上下架状态,0:下架1:上架2:部分上架
     */
    private Integer addedFlag;

    private ThirdPlatformType thirdPlatformType;

    /**
     * 创建开始时间
     */
    private LocalDateTime createTimeBegin;

    /**
     * 创建结束时间
     */
    private LocalDateTime createTimeEnd;

    /**
     * 封装公共条件
     *
     * @return
     */
    public Specification<StandardGoods> getWhereCriteria() {
        return (root, cquery, cbuild) -> {
            List<Predicate> predicates = new ArrayList<>();
            //商品库导入条件
            if(toLeadType!=null){
            if (toLeadType== 1){
                if (CollectionUtils.isNotEmpty(goodsIds)) {
                    predicates.add(root.get("goodsId").in(goodsIds));
                }
            }else if (toLeadType==2){
                if (CollectionUtils.isNotEmpty(goodsIds)) {
                    predicates.add(cbuild.not(root.get("goodsId").in(goodsIds)));
                }
               }
            }else {
                //批量商品编号
                if (CollectionUtils.isNotEmpty(goodsIds)) {
                    predicates.add(root.get("goodsId").in(goodsIds));
                }
            }

            //查询品牌编号
            if (brandId != null && brandId > 0) {
                predicates.add(cbuild.equal(root.get("brandId"), brandId));
            }
            //查询分类编号
            if (cateId != null && cateId > 0) {
                predicates.add(cbuild.equal(root.get("cateId"), cateId));
            }
            //批量查询品牌编号
            if (CollectionUtils.isNotEmpty(brandIds)) {
                predicates.add(root.get("brandId").in(brandIds));
            }
            //批量查询分类编号
            if (CollectionUtils.isNotEmpty(cateIds)) {
                predicates.add(root.get("cateId").in(cateIds));
            }
            //模糊查询SPU编码
            if (StringUtils.isNotEmpty(likeGoodsNo)) {
                predicates.add(cbuild.like(root.get("goodsNo"), StringUtil.SQL_LIKE_CHAR.concat(XssUtils.replaceLikeWildcard(likeGoodsNo.trim())).concat(StringUtil.SQL_LIKE_CHAR)));
            }
            //模糊查询名称
            if (StringUtils.isNotEmpty(likeGoodsName)) {
                predicates.add(cbuild.like(root.get("goodsName"), StringUtil.SQL_LIKE_CHAR.concat(XssUtils.replaceLikeWildcard(likeGoodsName.trim())).concat(StringUtil.SQL_LIKE_CHAR)));
            }
            //模糊查询供应商名称
            if (StringUtils.isNotEmpty(likeProviderName)) {
                predicates.add(cbuild.like(root.get("providerName"), StringUtil.SQL_LIKE_CHAR.concat(XssUtils.replaceLikeWildcard(likeProviderName.trim())).concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 小于或等于 搜索条件:创建开始时间截止
            if (createTimeBegin != null) {
                predicates.add(cbuild.lessThanOrEqualTo(root.get("createTime"), createTimeBegin));
            }

            // 大于或等于 搜索条件:创建结束时间开始
            if (createTimeEnd != null) {
                predicates.add(cbuild.greaterThanOrEqualTo(root.get("createTime"), createTimeEnd));
            }

            //批量查询品牌编号
            if (CollectionUtils.isNotEmpty(orNullBrandIds)) {
                predicates.add(cbuild.or(root.get("brandId").in(orNullBrandIds), root.get("brandId").isNull()));
            }

            //删除标记
            if (delFlag != null) {
                predicates.add(cbuild.equal(root.get("delFlag"), delFlag));
            }
            //非商品编号
            if (StringUtils.isNotBlank(notGoodsId)) {
                predicates.add(cbuild.notEqual(root.get("goodsId"), notGoodsId));
            }
            //商品来源，0供应商，1商家
            if (goodsSource != null && CollectionUtils.isEmpty(goodsSourceList)) {
                predicates.add(cbuild.equal(root.get("goodsSource"), goodsSource));
            }
            if(thirdPlatformType!=null){
                predicates.add(cbuild.equal(root.get("thirdPlatformType"),thirdPlatformType));
            }
            if(addedFlag!=null){
                predicates.add(cbuild.equal(root.get("addedFlag"), addedFlag));
            }

            if(CollectionUtils.isNotEmpty(goodsSourceList)){
                predicates.add(root.get("goodsSource").in(goodsSourceList));
            }


            Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);
            return p.length == 0 ? null : p.length == 1 ? p[0] : cbuild.and(p);
        };
    }

}
