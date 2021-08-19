package com.wanmi.sbc.elastic.api.request.customer;

import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.util.ElasticCommonUtil;
import com.wanmi.sbc.common.util.EsConstants;
import com.wanmi.sbc.customer.bean.enums.CustomerStatus;
import com.wanmi.sbc.customer.bean.enums.CustomerType;
import com.wanmi.sbc.elastic.api.request.base.EsBaseQueryRequest;
import com.wanmi.sbc.elastic.bean.constant.customer.CustomerConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;

import java.util.List;
import java.util.Objects;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * 会员详情查询参数
 * Created by CHENLI on 2017/4/19.
 */
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EsCustomerDetailPageRequest extends EsBaseQueryRequest {

    private static final long serialVersionUID = -1281379836937760934L;

    /**
     * 客户ID
     */
    @ApiModelProperty(value = "客户ID")
    private String customerId;

    /**
     * 账户
     */
    @ApiModelProperty(value = "账户")
    private String customerAccount;

    /**
     * 客户IDs
     */
    @ApiModelProperty(value = "客户IDs")
    private List<String> customerIds;

    /**
     * 客户等级ID
     */
    @ApiModelProperty(value = "客户等级ID")
    private Long customerLevelId;

    /**
     * 会员名称
     */
    @ApiModelProperty(value = "会员名称")
    private String customerName;

    /**
     * 省
     */
    @ApiModelProperty(value = "省")
    private Long provinceId;

    /**
     * 市
     */
    @ApiModelProperty(value = "市")
    private Long cityId;

    /**
     * 区
     */
    @ApiModelProperty(value = "区")
    private Long areaId;

    /**
     * 街道
     */
    @ApiModelProperty(value = "街道")
    private Long streetId;

    /**
     * 账号状态 0：启用中  1：禁用中
     */
    @ApiModelProperty(value = "账号状态")
    private CustomerStatus customerStatus;

    /**
     * 审核状态 0：待审核 1：已审核 2：审核未通过
     */
    @ApiModelProperty(value = "审核状态", dataType = "com.wanmi.sbc.customer.bean.enums.CheckState")
    private Integer checkState;

    /**
     * 负责业务员
     */
    @ApiModelProperty(value = "负责业务员")
    private String employeeId;

    /**
     * 负责业务员集合
     */
    @ApiModelProperty(value = "负责业务员集合")
    private List<String> employeeIds;

    /**
     * 用户类型
     */
    @ApiModelProperty(value = "用户类型")
    private CustomerType customerType;


    /**
     * 是否为分销员
     */
    @ApiModelProperty(value = "是否为分销员")
    private DefaultFlag isDistributor;

    /**
     * 商铺Id
     */
    @ApiModelProperty(value = "商铺Id")
    private Long storeId;

    /**
     * 商家Id
     */
    @ApiModelProperty(value = "商家Id")
    private Long companyInfoId;

    /**
     * 付费会员卡搜索关键词 0:全部 -1：非付费会员 其他：付费会员卡id
     */
    @ApiModelProperty(value = "付费会员卡搜索关键词 0:全部 -1：非付费会员 其他：付费会员卡id")
    private String paidCardKeyword;

//    /**
//     * 客户IDList
//     */
//    @ApiModelProperty(value = "客户IDList")
//    private List<String> customerIdList;
//
//    /**
//     * 搜索类型
//     */
//    private Integer searchType;


    /**
     * 会员列表查询条件封装
     * @return
     */
    public BoolQueryBuilder getBoolQueryBuilder() {
        BoolQueryBuilder bool = QueryBuilders.boolQuery();

        if (StringUtils.isNotBlank(customerName)){
            bool.must(QueryBuilders.wildcardQuery("customerName", ElasticCommonUtil.replaceEsLikeWildcard(customerName)));
        }

        if (Objects.nonNull(provinceId)) {
            bool.must(termQuery("provinceId",provinceId));
        }
        if (Objects.nonNull(cityId)) {
            bool.must(termQuery("cityId",cityId));
        }
        if (Objects.nonNull(areaId)) {
            bool.must(termQuery("areaId",areaId));
        }
        if (Objects.nonNull(streetId)) {
            bool.must(termQuery("streetId",streetId));
        }

        if (Objects.nonNull(checkState)) {
            bool.must(termQuery("checkState",checkState));
        }

        if (Objects.nonNull(customerId)) {
             bool.must(termQuery("customerId",customerId));
          }

        if (Objects.nonNull(customerStatus)) {
            bool.must(termQuery("customerStatus",customerStatus.toValue()));
        }

        if (StringUtils.isNotBlank(customerAccount)){
            bool.must(QueryBuilders.wildcardQuery("customerAccount", ElasticCommonUtil.replaceEsLikeWildcard(customerAccount)));
        }

        if (Objects.isNull(storeId) && Objects.nonNull(customerLevelId)) {
            bool.must(termQuery("customerLevelId",customerLevelId));
        }

        if (StringUtils.isNotBlank(employeeId)) {
            bool.must(termQuery("employeeId",employeeId));
        }

        if (Objects.nonNull(isDistributor)) {
            bool.must(termQuery("isDistributor",isDistributor.toValue()));
        }

        if (Objects.nonNull(storeId)) {
            bool.must(nestedQuery("esStoreCustomerRelaList",termQuery("esStoreCustomerRelaList.storeId",storeId), ScoreMode.None));
        }
        if (Objects.nonNull(storeId) && customerLevelId != null) {
            bool.must(nestedQuery("esStoreCustomerRelaList",termQuery("esStoreCustomerRelaList.storeLevelId",customerLevelId), ScoreMode.None));
        }
        if (Objects.nonNull(companyInfoId)) {
            bool.must(nestedQuery("esStoreCustomerRelaList",termQuery("esStoreCustomerRelaList.companyInfoId",companyInfoId), ScoreMode.None));
        }
        if (Objects.nonNull(customerType)) {
            bool.must(nestedQuery("esStoreCustomerRelaList",termQuery("esStoreCustomerRelaList.customerType",customerType.toValue()), ScoreMode.None));
        }

        if(StringUtils.isNotBlank(paidCardKeyword) && !CustomerConstant.ALL_CUSTOMER_TYPE.equals(paidCardKeyword)){
            //如果查询指定的付费卡会员
            if(CustomerConstant.COMMON_CUSTOMER_TYPE.equals(paidCardKeyword)){
                bool.mustNot(nestedQuery("esPaidCardList",existsQuery("esPaidCardList"), ScoreMode.None));
            }else{

                bool.must(nestedQuery("esPaidCardList",termQuery("esPaidCardList.paidCardId",paidCardKeyword), ScoreMode.None));
            }

        }

/*        if (Objects.nonNull(searchType) && CollectionUtils.isNotEmpty(customerIdList)) {
            // 说明需要按customerList查询
            if(searchType == CustomerConstant.CUSTOMER_SEARCH_TYPE_INCLUDE){
               // bool.should(termQuery("customerId",customerIdList));
                customerIdList.forEach(customerId->{
                    bool.(termQuery("customerId",customerId));
                });

            }else{
               // bool.mustNot(termQuery("customerId",customerIdList));
                customerIdList.forEach(customerId->{
                    bool.mustNot(termQuery("customerId",customerId));
                });
            }

        }*/


        return bool;

    }

    public SearchQuery getSearchQuery() {
        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
        builder.withIndices(EsConstants.DOC_CUSTOMER_DETAIL);
        builder.withQuery(getBoolQueryBuilder());
        builder.withPageable(this.getPageable());
        builder.withSort(SortBuilders.fieldSort("createTime").order(SortOrder.DESC));
        return builder.build();
    }
}
