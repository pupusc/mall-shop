package com.wanmi.sbc.elastic.api.request.goods;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.common.util.StringUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author houshuai
 * @date 2020/12/10 10:26
 * @description <p> </p>
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EsGoodsBrandPageRequest extends BaseQueryRequest {

    private static final long serialVersionUID = 1L;

    /**
     * 批量品牌编号
     */
    @ApiModelProperty(value = "批量品牌编号")
    private List<Long> brandIds;

    /**
     * and 精准查询，品牌名称
     */
    @ApiModelProperty(value = "and 精准查询，品牌名称")
    private String brandName;

    /**
     * and 模糊查询，品牌名称
     */
    @ApiModelProperty(value = "and 模糊查询，品牌名称")
    private String likeBrandName;

    /**
     * and 精准查询，品牌昵称
     */
    @ApiModelProperty(value = "and 精准查询，品牌昵称")
    private String nickName;

    /**
     * and 模糊查询，品牌昵称
     */
    @ApiModelProperty(value = "and 模糊查询，品牌昵称")
    private String likeNickName;

    /**
     * 模糊查询，品牌拼音
     */
    @ApiModelProperty(value = "模糊查询，品牌拼音")
    private String likePinYin;

    /**
     * 删除标记
     */
    @ApiModelProperty(value = "删除标记", dataType = "com.wanmi.sbc.common.enums.DeleteFlag")
    private Integer delFlag;

    /**
     * 非品牌编号
     */
    @ApiModelProperty(value = "非品牌编号")
    private Long notBrandId;

    /**
     * 关键字查询，可能含空格
     */
    @ApiModelProperty(value = "关键字查询，可能含空格")
    private String keywords;


    public NativeSearchQuery esCriteria() {

        BoolQueryBuilder bool = QueryBuilders.boolQuery();
        //批量品牌编号

        if (CollectionUtils.isNotEmpty(brandIds)) {
            bool.must(QueryBuilders.termsQuery("brandId", brandIds));
        }
        //查询名称
        if (StringUtils.isNotEmpty(brandName)) {
            bool.must(QueryBuilders.termQuery("brandName", brandName.trim()));
        }
        //模糊查询名称
        if (StringUtils.isNotEmpty(likeBrandName)) {
            bool.must(QueryBuilders.wildcardQuery("brandName", "*" + likeBrandName.trim() + "*"));
        }
        //查询昵称
        if (StringUtils.isNotEmpty(nickName)) {
            bool.must(QueryBuilders.termQuery("nickName", nickName.trim()));
        }
        //模糊查询昵称
        if (StringUtils.isNotEmpty(likeNickName)) {
            bool.must(QueryBuilders.wildcardQuery("nickName", "*" + likeNickName.trim() + "*"));
        }
        //模糊查询拉莫
        if (StringUtils.isNotEmpty(likePinYin)) {
            bool.must(QueryBuilders.wildcardQuery("pinYin", "*" + likePinYin.trim() + "*"));
        }
        //删除标记
        if (delFlag != null) {
            bool.must(QueryBuilders.termQuery("delFlag", delFlag));
        }
        //非品牌编号
        if (notBrandId != null) {
            bool.mustNot(QueryBuilders.termQuery("brandId", notBrandId));
        }
        //关键字查询
        if (StringUtils.isNotBlank(keywords)) {
            BoolQueryBuilder nBool = QueryBuilders.boolQuery();
            String[] tKeywords = StringUtils.split(keywords);
            if (tKeywords.length > 0) {
                for (String keyword : tKeywords) {
                    nBool.should(QueryBuilders.wildcardQuery("brandName", "*" + keyword.trim() + "*"));
                }
                bool.must(nBool);
            }
        }

        FieldSortBuilder createTime = SortBuilders.fieldSort("createTime").order(SortOrder.DESC);
        FieldSortBuilder brandId = SortBuilders.fieldSort("_id").order(SortOrder.ASC);
        return new NativeSearchQueryBuilder()
                .withQuery(bool)
                .withPageable(this.getPageable())
                .withSort(createTime)
                .withSort(brandId)
                .build();

    }
}
