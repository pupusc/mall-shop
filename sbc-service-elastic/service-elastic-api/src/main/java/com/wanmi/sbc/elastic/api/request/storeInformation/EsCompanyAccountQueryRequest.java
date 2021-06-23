package com.wanmi.sbc.elastic.api.request.storeInformation;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.StoreType;
import com.wanmi.sbc.common.util.ElasticCommonUtil;
import com.wanmi.sbc.common.util.EsConstants;
import com.wanmi.sbc.customer.bean.enums.AccountState;
import com.wanmi.sbc.customer.bean.enums.CheckState;
import com.wanmi.sbc.customer.bean.enums.StoreState;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;

import java.util.Objects;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * @Author yangzhen
 * @Description //商家结算账户分页查询
 * @Date 14:46 2020/12/9
 * @Param
 * @return
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class EsCompanyAccountQueryRequest extends BaseQueryRequest {


    /**
     * 公司信息ID
     */
    @ApiModelProperty(value = "公司信息ID")
    private Long companyInfoId;

    /**
     * 店铺ID
     */
    @ApiModelProperty(value = "店铺ID")
    private Long storeId;

    /**
     * 店铺名称
     */
    @ApiModelProperty(value = "店铺名称")
    private String storeName;

    /**
     * 店铺名称
     */
    @ApiModelProperty(value = "店铺名称")
    private String supplierName;

    /**
     * 商家编号
     */
    @ApiModelProperty(value = "商家编号")
    private String companyCode;

    /**
     * 商家账号
     */
    @ApiModelProperty(value = "商家账号")
    private String accountName;

    /**
     * 商家类型 0、平台自营 1、第三方商家
     */
    @ApiModelProperty(value = "商家类型(0、平台自营 1、第三方商家)")
    private Integer companyType;

    /**
     * 审核状态 0、待审核 1、已审核 2、审核未通过
     */
    @ApiModelProperty(value = "审核状态")
    private CheckState auditState;

    /**
     * 审核未通过原因
     */
    @ApiModelProperty(value = "审核未通过原因")
    private String auditReason;

    /**
     * 账号状态
     */
    @ApiModelProperty(value = "账号状态")
    private AccountState accountState;

    /**
     * 账号禁用原因
     */
    @ApiModelProperty(value = "账号禁用原因")
    private String accountDisableReason;

    /**
     * 店铺状态 0、开启 1、关店
     */
    @ApiModelProperty(value = "店铺状态")
    private StoreState storeState;

    /**
     * 账号关闭原因
     */
    @ApiModelProperty(value = "账号关闭原因")
    private String storeClosedReason;

    /**
     * 商家类型0品牌商城，1商家
     */
    @ApiModelProperty(value = "商家类型0品牌商城，1商家")
    private StoreType storeType;

    /**
     * 是否是主账号
     */
    @ApiModelProperty(value = "是否是主账号", dataType = "com.wanmi.sbc.common.enums.DefaultFlag")
    private Integer isMasterAccount;

    /**
     * 店铺删除状态
     */
    @ApiModelProperty(value = "店铺删除状态")
    private DeleteFlag storeDelFlag;

    /**
     * 公司删除状态
     */
    @ApiModelProperty(value = "公司删除状态")
    private DeleteFlag companyInfoDelFlag;

    /**
     * 员工删除状态
     */
    @ApiModelProperty(value = "员工删除状态")
    private DeleteFlag employeeDelFlag;

    /**
     * 是否确认打款 (-1:全部,0:否,1:是)
     */
    @ApiModelProperty(value = "是否确认打款(-1:全部,0:否,1:是)")
    private Integer remitAffirm;


    /**
     * 入驻时间
     */
    @ApiModelProperty(value = "入驻时间开始")
    private String applyEnterTimeStart;


    /**
     * 入驻时间
     */
    @ApiModelProperty(value = "入驻时间结束")
    private String applyEnterTimeEnd;


    /**
     * 封装公共条件
     * @return
     */
    public SearchQuery getSearchCriteria() {
        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
        builder.withIndices( EsConstants.DOC_STORE_INFORMATION_TYPE);
        QueryBuilder script = this.getWhereCriteria();
//        System.out.println("company list where===>"+script.toString());
        builder.withQuery(script);
        builder.withPageable(this.getPageable());
        builder.withSort(SortBuilders.fieldSort("applyEnterTime").order(SortOrder.DESC));

        return builder.build();
    }

    public QueryBuilder getWhereCriteria() {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        //根据店铺id查询
        if(Objects.nonNull(storeId)){
            boolQueryBuilder.must(termQuery("storeId",storeId));
        }
        //店铺名称模糊查询
        if (StringUtils.isNotBlank(storeName)) {
            boolQueryBuilder.must(wildcardQuery("storeName", ElasticCommonUtil.replaceEsLikeWildcard(storeName)));
        }
        //是否审核
        if(Objects.nonNull(auditState)){
            boolQueryBuilder.must(termQuery("auditState",auditState.toValue()));
        }

        //是否打款确认
        if(Objects.nonNull(remitAffirm) && remitAffirm != -1){
            boolQueryBuilder.must(termQuery("remitAffirm",remitAffirm));
        }
        /**
         * 入驻时间 开始
         */
        if (StringUtils.isNotEmpty(applyEnterTimeStart)) {
            boolQueryBuilder.must(rangeQuery("applyEnterTime").gte(ElasticCommonUtil.localDateFormat(applyEnterTimeStart)));
        }

        /**
         * 入驻时间 结束
         */
        if (StringUtils.isNotEmpty(applyEnterTimeEnd)) {
            boolQueryBuilder.must(rangeQuery("applyEnterTime").lte(ElasticCommonUtil.plusOneDayLocalDateFormat(applyEnterTimeEnd)));
        }
        return boolQueryBuilder;
    }

}
