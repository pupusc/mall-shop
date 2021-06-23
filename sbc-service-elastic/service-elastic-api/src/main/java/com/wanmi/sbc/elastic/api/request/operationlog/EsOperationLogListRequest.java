package com.wanmi.sbc.elastic.api.request.operationlog;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.common.util.DateUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;

import java.util.Objects;

import static java.util.Objects.nonNull;

@Slf4j
@EqualsAndHashCode(callSuper = true)
@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EsOperationLogListRequest extends BaseQueryRequest {

    private static final long serialVersionUID = 2584160228306434248L;

    @ApiModelProperty(value = "token")
    private String token;

    /**
     * 员工编号
     */
    @ApiModelProperty(value = "员工编号")
    private String employeeId;

    /**
     * 店铺Id
     */
    @ApiModelProperty(value = "店铺Id")
    private Long storeId;

    /**
     * 公司Id
     */
    @ApiModelProperty(value = "公司Id")
    private Long companyInfoId;

    /**
     * 商家编号
     */
    @ApiModelProperty(value = "商家编号")
    private String thirdId;

    /**
     * 操作人账号
     */
    @ApiModelProperty(value = "操作人账号")
    private String opAccount;

    /**
     * 操作人名称
     */
    @ApiModelProperty(value = "操作人名称")
    private String opName;

    /**
     * 操作模块
     */
    @ApiModelProperty(value = "操作模块")
    private String opModule;

    /**
     * 操作类型
     */
    @ApiModelProperty(value = "操作类型")
    private String opCode;

    /**
     * 操作内容
     */
    @ApiModelProperty(value = "操作内容")
    private String opContext;

    /**
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间")
    private String beginTime;

    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间")
    private String endTime;


    public NativeSearchQuery esCriteria() {
        BoolQueryBuilder bool = QueryBuilders.boolQuery();

        if (Objects.nonNull(this.getStoreId())) {
            bool.must(QueryBuilders.termQuery("storeId", this.getStoreId()));
        }
        if (Objects.nonNull(this.getCompanyInfoId())) {
            bool.must(QueryBuilders.termQuery("companyInfoId", this.getCompanyInfoId()));
        }
        if (StringUtils.isNotBlank(this.getOpModule())) {
            bool.must(QueryBuilders.termQuery("opModule", this.getOpModule()));
        }

        if (StringUtils.isNotBlank(this.getBeginTime()) && StringUtils.isNotBlank(this.getEndTime())) {
            bool.must(QueryBuilders.rangeQuery("opTime").gte(this.getBeginTime()).lte(this.getEndTime()));
        }

        if (StringUtils.isNotBlank(this.getOpAccount())) {
            bool.must(QueryBuilders.wildcardQuery("opAccount", "*" + this.getOpAccount() + "*"));
        }

        if (StringUtils.isNotBlank(this.getOpName())) {
            bool.must(QueryBuilders.wildcardQuery("opName", "*" + this.getOpName() + "*"));
        }

        if (StringUtils.isNotBlank(this.getOpCode())) {
            bool.must(QueryBuilders.wildcardQuery("opCode", "*" + this.getOpCode() + "*"));
        }

        if (StringUtils.isNotBlank(this.getOpContext())) {
            bool.must(QueryBuilders.wildcardQuery("opContext", "*" + this.getOpContext() + "*"));
        }

        log.info("{}", bool);
        FieldSortBuilder opTime = SortBuilders.fieldSort("opTime").order(SortOrder.DESC);
        return new NativeSearchQueryBuilder()
                .withQuery(bool)
                .withPageable(this.getPageable())
                .withSort(opTime)
                .build();
    }


}