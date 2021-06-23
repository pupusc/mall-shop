package com.wanmi.sbc.elastic.api.request.storeInformation;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.StoreType;
import com.wanmi.sbc.common.util.ElasticCommonUtil;
import com.wanmi.sbc.common.util.EsConstants;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * @Author: songhanlin
 * @Date: Created In 16:02 2020/12/11
 * @Description: Es查询商家列表Request
 */
@ApiModel
@Data
public class EsCompanyPageRequest extends BaseQueryRequest {

    private static final long serialVersionUID = 8071214608591987184L;

    /**
     * 商家名称
     */
    @ApiModelProperty(value = "商家名称")
    private String supplierName;

    /**
     * 店铺名称
     */
    @ApiModelProperty(value = "店铺名称")
    private String storeName;

    /**
     * 商家账号
     */
    @ApiModelProperty(value = "商家账号")
    private String accountName;

    /**
     * 商家编号
     */
    @ApiModelProperty(value = "商家编号")
    private String companyCode;

    /**
     * 签约结束日期
     */
    @ApiModelProperty(value = "签约结束日期")
    private String contractEndDate;

    /**
     * 账户状态  -1:全部 0：启用   1：禁用
     */
    @ApiModelProperty(value = "账户状态(-1:全部,0:启用,1:禁用)")
    private Integer accountState;

    /**
     * 店铺状态 -1：全部,0:开启,1:关店,2:过期
     */
    @ApiModelProperty(value = "店铺状态(-1:全部,0:开启,1:关店,2:过期)")
    private Integer storeState;

    /**
     * 审核状态 -1全部 ,0:待审核,1:已审核,2:审核未通过
     */
    @ApiModelProperty(value = "审核状态(-1:全部,0:待审核,1:已审核,2:审核未通过)")
    private Integer auditState;

    /**
     * 商家删除状态
     */
    @ApiModelProperty(value = "商家删除状态")
    private DeleteFlag deleteFlag;

    /**
     * 商家类型 0、供应商 1、商家
     */
    @ApiModelProperty(value = "商家类型 0、供应商 1、商家")
    private StoreType storeType;

    /**
     * 是否是主账号
     */
    @ApiModelProperty(value = "是否是主账号", dataType = "com.wanmi.sbc.common.enums.DefaultFlag")
    private Integer isMasterAccount;

    /**
     * 排序参数
     */
    @ApiModelProperty(value = "排序参数")
    private List<SortBuilder> sorts = new ArrayList<>();

    /**
     * 填放排序参数
     * @param fieldName 字段名
     * @param sort      排序
     */
    public void putSort(String fieldName, SortOrder sort) {
        sorts.add(SortBuilders.fieldSort(fieldName).order(sort));
    }

    /**
     * 封装公共条件
     *
     * @return
     */
    public SearchQuery getSearchCriteria() {
        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
        builder.withIndices(EsConstants.DOC_STORE_INFORMATION_TYPE);
        QueryBuilder script = this.getWhereCriteria();
//        System.out.println("company list where===>"+script.toString());
        builder.withQuery(script);
        builder.withPageable(this.getPageable());
        builder.withSort(SortBuilders.fieldSort("applyEnterTime").order(SortOrder.DESC));
        return builder.build();
    }

    public QueryBuilder getWhereCriteria() {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        //商家名称模糊查询
        if (StringUtils.isNotBlank(supplierName)) {
            boolQueryBuilder.must(wildcardQuery("supplierName", ElasticCommonUtil.replaceEsLikeWildcard(supplierName)));
        }

        //店铺名称模糊查询
        if (StringUtils.isNotBlank(storeName)) {
            boolQueryBuilder.must(wildcardQuery("storeName", ElasticCommonUtil.replaceEsLikeWildcard(storeName)));
        }

        //商家账号模糊查询
        if (StringUtils.isNotBlank(accountName)) {
            boolQueryBuilder.must(wildcardQuery("accountName", ElasticCommonUtil.replaceEsLikeWildcard(accountName)));
        }

        //商家编号模糊查询
        if (StringUtils.isNotBlank(companyCode)) {
            boolQueryBuilder.must(wildcardQuery("companyCode", ElasticCommonUtil.replaceEsLikeWildcard(companyCode)));
        }

        // 到期时间 结束
        if (StringUtils.isNotEmpty(contractEndDate)) {
            boolQueryBuilder.must(rangeQuery("contractEndDate").lte(ElasticCommonUtil.plusOneDayLocalDateFormat(contractEndDate)));
        }

        // 账户状态  -1:全部 0：启用 1：禁用
        if (Objects.nonNull(accountState) && accountState != -1) {
            boolQueryBuilder.must(termQuery("accountState", accountState));
        }

        // 店铺状态 -1：全部,0:开启,1:关店,2:过期
        if (Objects.nonNull(storeState) && storeState != -1) {
            boolQueryBuilder.must(termQuery("storeState", storeState));
        }

        // 审核状态 -1全部 ,0:待审核,1:已审核,2:审核未通过
        if (Objects.nonNull(auditState) && auditState != -1) {
            boolQueryBuilder.must(termQuery("auditState", auditState));
        }

        // 商家删除状态
        if (Objects.nonNull(deleteFlag)) {
            boolQueryBuilder.must(termQuery("storeDelFlag", deleteFlag.toValue()))
                    .must(termQuery("companyInfoDelFlag", deleteFlag.toValue()))
                    .must(termQuery("employeeDelFlag", deleteFlag.toValue()));
        }

        // 商家类型 0、供应商 1、商家
        if (Objects.nonNull(storeType)) {
            boolQueryBuilder.must(termQuery("storeType", storeType.toValue()));
        }

        // 是否是主账号
        if (Objects.nonNull(isMasterAccount)) {
            boolQueryBuilder.must(termQuery("isMasterAccount", isMasterAccount));
        }

        return boolQueryBuilder;
    }
}
