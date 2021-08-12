package com.wanmi.sbc.elastic.api.request.goods;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.common.util.EsConstants;
import com.wanmi.sbc.common.util.StringUtil;
import com.wanmi.sbc.elastic.bean.dto.goods.EsGoodsInfoDTO;
import com.wanmi.sbc.goods.bean.enums.DistributionGoodsAudit;
import com.wanmi.sbc.goods.bean.enums.EnterpriseAuditState;
import com.wanmi.sbc.goods.bean.enums.GoodsSource;
import com.wanmi.sbc.goods.bean.enums.GoodsType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AbstractAggregationBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.ScoreSortBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.elasticsearch.index.query.QueryBuilders.*;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;

/**
 * 商品SKU查询请求
 * Created by daiyitian on 2017/3/24.
 */
@Data
@ApiModel
@Slf4j
public class EsGoodsInfoQueryRequest extends BaseQueryRequest {

    /**
     * 未登录时,前端采购单缓存信息
     */
    @Valid
    @ApiModelProperty(value = "未登录时,前端采购单缓存信息")
    private List<EsGoodsInfoDTO> esGoodsInfoDTOList;

    /**
     * 批量商品ID
     */
    @ApiModelProperty(value = "批量商品ID")
    private List<String> goodsIds;

    /**
     * 批量SKU编号
     */
    @ApiModelProperty(value = "批量SKU编号")
    private List<String> goodsInfoIds;

    /**
     * 模糊条件-商品名称
     */
    @ApiModelProperty(value = "模糊条件-商品名称")
    private String likeGoodsName;

    /**
     * 上下架状态
     */
    @ApiModelProperty(value = "上下架状态")
    private Integer addedFlag;

    /**
     * 可售状态
     */
    @ApiModelProperty(value = "可售状态")
    private Integer vendibility;

    /**
     * 分类编号
     */
    @ApiModelProperty(value = "分类编号")
    private Long cateId;

    /**
     * 批量分类编号
     */
    @ApiModelProperty(value = "批量分类编号")
    private List<Long> cateIds;

    /**
     * 批量标签编号
     */
    @ApiModelProperty(value = "批量标签编号")
    private List<Long> labelIds;

    /**
     * 批量店铺分类编号
     */
    @ApiModelProperty(value = "批量店铺分类编号")
    private List<Long> storeCateIds;

    /**
     * 批量品牌编号
     */
    @ApiModelProperty(value = "批量品牌编号")
    private List<Long> brandIds;

    /**
     * 删除标记
     */
    @ApiModelProperty(value = "删除标记")
    private Integer delFlag;

    /**
     * 库存状态
     * null:所有,1:有货,0:无货
     */
    @ApiModelProperty(value = "库存状态")
    private Integer stockFlag;

    /**
     * 排序标识
     * 0: 销量倒序->时间倒序->市场价倒序
     * 1:上架时间倒序->销量倒序->市场价倒序
     * 2:市场价倒序->销量倒序
     * 3:市场价升序->销量倒序
     * 4:销量倒序->市场价倒序
     * 5:评论数倒序->销量倒序->市场价倒序
     * 6:好评倒序->销量倒序->市场价倒序
     * 7:收藏倒序->销量倒序->市场价倒序
     * 10:排序号倒序->创建时间倒序
     */
    @ApiModelProperty(value = "排序标识")
    private Integer sortFlag;

    /**
     * 会员ID
     */
    @ApiModelProperty(value = "会员ID")
    private String customerId;

    /**
     * 客户等级
     */
    @ApiModelProperty(value = "客户等级")
    private Long customerLevelId;

    /**
     * 客户等级折扣
     */
    @ApiModelProperty(value = "客户等级折扣")
    private BigDecimal customerLevelDiscount;

    /**
     * 关键字，可能含空格
     */
    @ApiModelProperty(value = "关键字，可能含空格")
    private String keywords;

    /**
     * 商家类型 0、平台自营 1、第三方商家
     */
    @ApiModelProperty(value = "商家类型")
    private Integer companyType;

    /**
     * 店铺ID
     */
    @ApiModelProperty(value = "店铺ID")
    private Long storeId;

    /**
     * 签约开始日期
     */
    @ApiModelProperty(value = "签约开始日期")
    private String contractStartDate;

    /**
     * 签约结束日期
     */
    @ApiModelProperty(value = "签约结束日期")
    private String contractEndDate;

    /**
     * 店铺状态 0、开启 1、关店
     */
    @ApiModelProperty(value = "店铺状态",dataType = "com.wanmi.sbc.customer.bean.enums.StoreState")
    private Integer storeState;

    /**
     * 禁售状态
     */
    @ApiModelProperty(value = "禁售状态")
    private Integer forbidStatus;

    /**
     * 审核状态
     */
    @ApiModelProperty(value = "审核状态",dataType = "com.wanmi.sbc.goods.bean.enums.CheckStatus")
    private Integer auditStatus;

    /**
     * 聚合参数
     */
    @ApiModelProperty(value = "聚合参数")
    private List<AbstractAggregationBuilder> aggs = new ArrayList<>();

    /**
     * 排序参数
     */
    @ApiModelProperty(value = "排序参数")
    private List<SortBuilder> sorts = new ArrayList<>();

    /**
     * 精确查询-规格值参数
     */
    @ApiModelProperty(value = "精确查询-规格值参数")
    private List<EsSpecQueryRequest> specs = new ArrayList<>();

    /**
     * 营销Id
     */
    @ApiModelProperty(value = "营销Id")
    private Long marketingId;

    /**
     * 是否需要反查分类
     */
    @ApiModelProperty(value = "是否需要反查分类",dataType = "com.wanmi.sbc.common.enums.BoolFlag")
    private boolean cateAggFlag;

    /**
     * 多个 属性与对应的属性值id列表
     */
    @ApiModelProperty(value = "多个 属性与对应的属性值id列表")
    private List<EsPropQueryRequest> propDetails = new ArrayList<>();

    @ApiModelProperty(value = "是否查询商品")
    private boolean isQueryGoods = false;

    @ApiModelProperty(value = "分销商品审核状态 0:普通商品 1:待审核 2:已审核通过 3:审核不通过 4:禁止分销")
    private Integer distributionGoodsAudit;


    @ApiModelProperty(value = "企业购商品审核状态 0:无状态 1:待审核 2:已审核通过 3:审核不通过 4:禁止分销")
    private Integer enterPriseAuditStatus;

    @ApiModelProperty(value = "隐藏已选分销商品,false:不隐藏，true:隐藏")
    private boolean hideSelectedDistributionGoods = Boolean.FALSE;

    @ApiModelProperty(value = "排除分销商品")
    private boolean excludeDistributionGoods = Boolean.FALSE;

    /**
     * 分销商品状态，配合分销开关使用
     */
    @ApiModelProperty(value = "分销商品状态，配合分销开关使用")
    private Integer distributionGoodsStatus;

    /**
     * 企业购商品过滤
     */
    @ApiModelProperty(value = "分销商品状态，配合分销开关使用")
    private Integer enterPriseGoodsStatus;

    /**
     * 批量分销商品SKU编号
     */
    @ApiModelProperty(value = "批量分销商品SKU编号")
    private List<String> distributionGoodsInfoIds;

    /**
     * 批量分销商品SKU编号
     */
    @ApiModelProperty(value = "积分抵扣选项")
    private Boolean pointsUsageFlag;

    /**
     * 是否魔方商品列表
     */
    @ApiModelProperty(value = "是否魔方商品列表 true:是")
    private Boolean moFangFlag;

    /**
     * 不显示linkedMall商品
     */
    @ApiModelProperty(value = "不显示linkedMall商品 true:是")
    private Boolean notShowLinkedMallFlag;

    @ApiModelProperty(value = "商品来源，0供应商，1商家 2 linkedMall")
    private GoodsSource goodsSource;

    /**
     * 商品类型
     */
    @ApiModelProperty(value = "商品类型")
    private GoodsType goodsType;
    /**
     * 知识顾问专享 0:不是 ，1：是
     */
    @ApiModelProperty(value = "知识顾问专享 0:不是 ，1：是")
    private Integer cpsSpecial;
    /**
     * 封装公共条件
     * @return
     */
    public SearchQuery getSearchCriteria() {
        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
        builder.withIndices(isQueryGoods ? EsConstants.DOC_GOODS_TYPE : EsConstants.DOC_GOODS_INFO_TYPE);
//        builder.withTypes(isQueryGoods ? EsConstants.DOC_GOODS_TYPE : EsConstants.DOC_GOODS_INFO_TYPE);
        builder.withQuery(this.getWhereCriteria());
//        System.out.println("where===>" + this.getWhereCriteria().toString());
        builder.withPageable(this.getPageable());
        if (CollectionUtils.isNotEmpty(sorts)) {
            sorts.forEach(builder::withSort);
        }
        if (CollectionUtils.isNotEmpty(aggs)) {
            aggs.forEach(builder::addAggregation);
        }
        return builder.build();
    }

    public QueryBuilder getWhereCriteria() {
        String queryName = isQueryGoods ? "goodsInfos" : "goodsInfo";
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        //批量商品ID
        if ( CollectionUtils.isNotEmpty(goodsIds)&& !isQueryGoods) {
            boolQueryBuilder.must(termsQuery(queryName.concat(".goodsId"), goodsIds));
        }


        //批量商品ID
        if (CollectionUtils.isNotEmpty(goodsIds) && isQueryGoods) {
            boolQueryBuilder.must(termsQuery("id", goodsIds));
        }

        // 批量SKU编号
        if (CollectionUtils.isNotEmpty(goodsInfoIds)) {
            boolQueryBuilder.must(termsQuery(queryName.concat(".goodsInfoId"), goodsInfoIds));
        }
        // 单个商品分类ID
        if (null != cateId) {
            boolQueryBuilder.must(termQuery("goodsCate.cateId", cateId));
        }
        //批量商品分类ID
        if (CollectionUtils.isNotEmpty(cateIds)) {
            boolQueryBuilder.must(termsQuery("goodsCate.cateId", cateIds));
        }

        //店铺ID
        if (storeId != null) {
            boolQueryBuilder.must(termQuery(queryName.concat(".storeId"), storeId));
        }

        //模糊查询
        if (StringUtils.isNotBlank(likeGoodsName)) {
            boolQueryBuilder
                    //.should(matchQuery(queryName.concat(".goodsInfoName"),likeGoodsName))
                    .must(matchPhraseQuery("lowGoodsName", likeGoodsName));
        }

        String labelVisibleField = "goodsLabelList.labelVisible";
        String labelDelFlagField = "goodsLabelList.delFlag";
        //模糊查询
        if (StringUtils.isNotBlank(keywords)) {
            BoolQueryBuilder bq = QueryBuilders.boolQuery();
            List<String> chss = StringUtil.pickChs(keywords);//中文
            List<String> engs = StringUtil.pickEng(keywords);//英文、拼音
            boolean chsRale = chss.size() < engs.size();//中文优先，中文比英文内容更多
            float chsBoost = 4f; //中文权重比
            float engBoost = 2f; //英文权重比
            //英文多于中文
            if (!chsRale) {
                chsBoost = 3.2f;
                engBoost = 4f;
            }
            //中文
            for (String keyword : chss) {
                bq.should(matchQuery(queryName.concat(".goodsInfoName"), keyword).boost(chsBoost));
                bq.should(matchQuery("goodsBrand.brandName", keyword).boost(chsBoost / 2));
                bq.should(matchQuery(queryName.concat(".specText"), keyword).boost(chsBoost/4));
                //店铺搜索不包含平台分类
                if (storeId == null) {
                    bq.should(matchQuery("goodsCate.cateName", keyword).boost(chsBoost / 2));
                }
            }

            //英文
            for (String keyword : engs) {
                bq.should(matchPhrasePrefixQuery("lowGoodsName", keyword).boost(engBoost));
                bq.should(matchQuery("pinyinGoodsName", keyword).boost(engBoost / 2));
                bq.should(matchQuery("goodsBrand.pinYin", keyword).boost(engBoost / 2));
            }

            //标签，标签最优先
            BoolQueryBuilder labelBqv = QueryBuilders.boolQuery();
            labelBqv.must(matchQuery("goodsLabelList.labelName", keywords).boost(chsBoost * 5));//靠前
            labelBqv.must(termQuery(labelVisibleField, Boolean.TRUE).boost(0));
            labelBqv.must(termQuery(labelDelFlagField, DeleteFlag.NO.toValue()).boost(0));
            bq.should(nestedQuery("goodsLabelList", labelBqv, ScoreMode.Total).boost(chsBoost * 5));

            bq.should(matchQuery(queryName.concat(".goodsInfoName"), keywords).boost(2));
            boolQueryBuilder.must(bq);
        }


//        if (CollectionUtils.isNotEmpty(specs)) {
//            BoolQueryBuilder bq = QueryBuilders.boolQuery();
//            specs.stream().forEach(spec -> {
//                List<String> values = spec.getValues();
//                if (CollectionUtils.isNotEmpty(values)) {
//                    BoolQueryBuilder bqv = QueryBuilders.boolQuery();
//                    bqv.must(termQuery("specDetails.specName", spec.getName()));
//                    bqv.must(termsQuery("specDetails.allDetailName", values));
//                    bq.should(nestedQuery("specDetails", bqv, ScoreMode.None));
//                }
//            });
//            boolQueryBuilder.must(bq);
//        }

        //批量属性值ID
        if (CollectionUtils.isNotEmpty(propDetails)) {
            List<Long> ids = propDetails.stream()
                    .filter(i -> i.getDetailIds() != null)
                    .flatMap(i -> i.getDetailIds().stream()).collect(Collectors.toList());
            if(CollectionUtils.isNotEmpty(ids)) {
                boolQueryBuilder.must(termsQuery("propDetailIds", ids));
            }
        }

        //批量商品分类ID
        if (CollectionUtils.isNotEmpty(brandIds)) {
            boolQueryBuilder.must(termsQuery("goodsBrand.brandId", brandIds));
        }

        //批量标签ID
        if(CollectionUtils.isNotEmpty(labelIds)){
            BoolQueryBuilder labelBq = QueryBuilders.boolQuery();
            labelBq.must(termsQuery("goodsLabelList.goodsLabelId", labelIds));
            labelBq.must(termQuery(labelVisibleField, Boolean.TRUE));
            labelBq.must(termQuery(labelDelFlagField, DeleteFlag.NO.toValue()));
            boolQueryBuilder.must(nestedQuery("goodsLabelList", labelBq, ScoreMode.None));
        }

        //批量商品分类ID
        if (companyType != null) {
            boolQueryBuilder.must(termQuery(queryName.concat(".companyType"), companyType.intValue()));
        }

        //库存状态
        if (stockFlag != null) {
            String tempName = isQueryGoods ? "stock" : "goodsInfo.stock";
            if (Constants.yes.equals(stockFlag)) {
                boolQueryBuilder.must(rangeQuery(tempName).gt(0));
            } else {
                boolQueryBuilder.must(rangeQuery(tempName).lte(0));
            }
        }
        //删除标记
        if (delFlag != null) {
            boolQueryBuilder.must(termQuery(queryName.concat(".delFlag"), delFlag));
        }
        //上下架状态
        if (addedFlag != null) {
            boolQueryBuilder.must(termQuery(queryName.concat(".addedFlag"), addedFlag));
        }
        //可售状态
        if(vendibility !=null){
            boolQueryBuilder.must(termQuery("vendibilityStatus", vendibility));
        }

        //批量店铺分类ID
        if (CollectionUtils.isNotEmpty(storeCateIds)) {
            boolQueryBuilder.must(termsQuery("storeCateIds", storeCateIds));
        }

        //店铺状态
        if (storeState != null) {
            boolQueryBuilder.must(termQuery("storeState", storeState));
        }

        //禁售状态
        if (forbidStatus != null) {
            boolQueryBuilder.must(termQuery("forbidStatus", forbidStatus));
        }

        //审核状态
        if (auditStatus != null) {
            boolQueryBuilder.must(termQuery("auditStatus", auditStatus));
        }

        //可抵扣积分的，非企业价
        if (Boolean.TRUE.equals(pointsUsageFlag)) {
            String tempName = isQueryGoods ? "buyPoint" : "goodsInfo.buyPoint";
            boolQueryBuilder.must(rangeQuery(tempName).gt(0L));
            boolQueryBuilder.mustNot(termQuery(queryName.concat(".enterPriseAuditStatus"), EnterpriseAuditState.CHECKED.toValue()));
        }

        //商品来源
        if (goodsSource != null) {
            boolQueryBuilder.must(termQuery("goodsSource", goodsSource.toValue()));
        }

        /**
         * 签约开始日期
         */
        if (StringUtils.isNotBlank(contractStartDate)) {
            boolQueryBuilder.must(rangeQuery("contractStartDate").lte(contractStartDate));
        }

        /**
         * 签约结束日期
         */
        if (StringUtils.isNotBlank(contractEndDate)) {
            boolQueryBuilder.must(rangeQuery("contractEndDate").gte(contractEndDate));
        }

        /**
         * 销商品审核状态 0:普通商品 1:待审核 2:已审核通过 3:审核不通过 4:禁止分销
         */
        if (distributionGoodsAudit != null) {
            boolQueryBuilder.must(termQuery(queryName.concat(".distributionGoodsAudit"), String.valueOf(distributionGoodsAudit)));
        }
        /**
         * 排除分销商品:排除(店铺分销开关开且商品审核通过)
         */
        if (excludeDistributionGoods) {
            BoolQueryBuilder bqv = QueryBuilders.boolQuery();
            // 某个属性prodId=1 && detailId in (1,2)
            bqv.must(termQuery(queryName.concat(".distributionGoodsAudit"), DistributionGoodsAudit.CHECKED.toValue()));
            bqv.must(termQuery("distributionGoodsStatus", DefaultFlag.NO.toValue()));
            boolQueryBuilder.mustNot(bqv);
        }

        if (distributionGoodsStatus != null) {
            boolQueryBuilder.must(termQuery("distributionGoodsStatus", distributionGoodsStatus));
        }

        //企业购商品过滤-企业购且非分销、非积分价
        if (enterPriseGoodsStatus != null) {
            boolQueryBuilder.must(termQuery(queryName.concat(".enterPriseAuditStatus"), enterPriseGoodsStatus));
            String tempName = isQueryGoods ? "buyPoint" : "goodsInfo.buyPoint";
            boolQueryBuilder.must(termQuery(tempName, 0));
            if (Objects.equals(EnterpriseAuditState.CHECKED.toValue(), enterPriseGoodsStatus)) {
                BoolQueryBuilder bqv = QueryBuilders.boolQuery();
                // 某个属性prodId=1 && detailId in (1,2)
                bqv.must(termQuery(queryName.concat(".distributionGoodsAudit"), DistributionGoodsAudit.CHECKED.toValue()));
                bqv.must(termQuery("distributionGoodsStatus", DefaultFlag.NO.toValue()));
                boolQueryBuilder.mustNot(bqv);
            }
        }

        if (hideSelectedDistributionGoods && CollectionUtils.isNotEmpty(distributionGoodsInfoIds)) {
            boolQueryBuilder.mustNot(termsQuery(queryName.concat(".goodsInfoId"), distributionGoodsInfoIds));
        }

        //不显示linkedMall商品
        if (Boolean.TRUE.equals(notShowLinkedMallFlag)) {
            String tempName = isQueryGoods ? "thirdPlatformType" : "goodsInfo.thirdPlatformType";
            boolQueryBuilder.mustNot(existsQuery(tempName));
        }

        //商品类型
        if(goodsType != null) {
            boolQueryBuilder.must(termQuery(queryName.concat(".goodsType"), goodsType.toValue()));
        }

         log.info(String.format("ES商口查询条件->%s", boolQueryBuilder.toString()));
        return boolQueryBuilder;
    }

    /**
     * 填放排序参数
     * @param fieldName 字段名
     * @param sort      排序
     */
    public void putSort(String fieldName, SortOrder sort) {
        sorts.add(new FieldSortBuilder(fieldName).order(sort));
    }

    /**
     * 按_score降序排列
     */
    public void putScoreSort() {
        sorts.add(new ScoreSortBuilder());
    }

    /**
     * 填放排序参数
     * @param builder
     */
    public void putSort(SortBuilder builder) {
        sorts.add(builder);
    }

    /**
     * 填放聚合参数
     * @param builder
     */
    public void putAgg(AbstractAggregationBuilder builder) {
        aggs.add(builder);
    }
}
