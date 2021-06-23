package com.wanmi.sbc.returnorder.request;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.common.util.DateUtil;
import com.wanmi.sbc.order.bean.enums.ReturnFlowState;
import com.wanmi.sbc.order.bean.enums.ReturnType;
import com.wanmi.sbc.order.bean.util.XssUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.mongodb.core.query.Criteria;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 查询接口
 * Created by jinwei on 6/5/2017.
 */
@ApiModel
@Data
public class ReturnQueryRequest extends BaseQueryRequest {

    @ApiModelProperty(value = "退单Id")
    private String rid;

    @ApiModelProperty(value = "订单Id")
    private String tid;

    @ApiModelProperty(value = "买家名称")
    private String buyerName;

    @ApiModelProperty(value = "买家账号")
    private String buyerAccount;

    /**
     * 收货人
     */
    @ApiModelProperty(value = "收货人")
    private String consigneeName;

    @ApiModelProperty(value = "收货人电话")
    private String consigneePhone;

    /**
     * 退单流程状态
     */
    @ApiModelProperty(value = "退单流程状态")
    private ReturnFlowState returnFlowState;

    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称")
    private String skuName;

    @ApiModelProperty(value = "商品编号")
    private String skuNo;

    /**
     * 退单编号列表
     */
    @ApiModelProperty(value = "退单编号列表")
    private String[] rids;

    /**
     * 退单创建开始时间，精确到天
     */
    @ApiModelProperty(value = "退单创建开始时间，精确到天")
    private String beginTime;

    /**
     * 退单创建结束时间，精确到天
     */
    @ApiModelProperty(value = "退单创建结束时间，精确到天")
    private String endTime;

    /**
     * 商家Id
     */
    @ApiModelProperty(value = "商家Id")
    private Long companyInfoId;

    /**
     * 店铺ID
     */
    @ApiModelProperty(value = "店铺ID")
    private Long storeId;

    /**
     * 商家名称
     */
    @ApiModelProperty(value = "商家名称")
    private String supplierName;

    /**
     * 商家编号
     */
    @ApiModelProperty(value = "商家编号")
    private String supplierCode;

    /**
     * 业务员id
     */
    @ApiModelProperty(value = "业务员id")
    private String employeeId;

    /**
     * 客户id
     */
    @ApiModelProperty(value = "客户id")
    private Object[] customerIds;

    /**
     * 供应商公司id
     */
    @ApiModelProperty(value = "供应商公司id")
    private Long providerCompanyInfoId;

    /**
     * ES中的createTime日期格式
     */
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    public Criteria build() {
        List<Criteria> criteria = new ArrayList<>();

        // ids 用于导出时的查询，如果传了ids，以ids为准
        if (Objects.nonNull(rids) && rids.length > 0) {
            criteria.add(Criteria.where("id").in(Arrays.asList(rids)));
        }

        // 退单编号
        if (StringUtils.isNoneBlank(rid)) {
            criteria.add(XssUtils.regex("id", rid));
        }

        // 订单编号
        if (StringUtils.isNoneBlank(tid)) {
            criteria.add(XssUtils.regex("tid", tid));
        }

        if(Objects.nonNull(customerIds) && customerIds.length > 0){
            criteria.add(Criteria.where("buyer.id").in(Arrays.asList(customerIds)));
        }

        if(StringUtils.isNotBlank(employeeId)){
            criteria.add(Criteria.where("buyer.employeeId").is(employeeId));
        }

        // 客户信息
        if (StringUtils.isNotBlank(buyerName)) {
            criteria.add(XssUtils.regex("buyer.name", buyerName));
        } else if (StringUtils.isNotBlank(buyerAccount)) {
            criteria.add(XssUtils.regex("buyer.account", buyerAccount));
        }

        // 收货人
        if (StringUtils.isNotBlank(consigneeName)) {
            criteria.add(XssUtils.regex("consignee.name", consigneeName));
        } else if (StringUtils.isNotBlank(consigneePhone)) {
            criteria.add(XssUtils.regex("consignee.phone", consigneePhone));
        }

        /**
         * 供应商名称
         */
        if (StringUtils.isNotBlank(supplierName)) {
            criteria.add(XssUtils.regex("company.supplierName", supplierName));
        }

        if (StringUtils.isNotBlank(supplierCode)) {
            criteria.add(XssUtils.regex("company.companyCode", supplierCode));
        }

        if (Objects.nonNull(companyInfoId)) {
            criteria.add(Criteria.where("company.companyInfoId").is(companyInfoId));
        }

        if (Objects.nonNull(storeId)) {
            criteria.add(Criteria.where("company.storeId").is(storeId));
        }

        // sku
        if (StringUtils.isNotBlank(skuName)) {
            criteria.add(XssUtils.regex("returnItems.skuName", skuName));
        } else if (StringUtils.isNotBlank(skuNo)) {
            criteria.add(XssUtils.regex("returnItems.skuNo", skuNo));
        }

        // createTime
        if (StringUtils.isNotBlank(beginTime)) {
            criteria.add(Criteria.where("createTime").gte(DateUtil.parseDay(beginTime)));
        }

        // 小与传入的结束时间+1天，零点前
        if (StringUtils.isNotBlank(endTime)) {
            criteria.add(Criteria.where("createTime").lt(DateUtil.parseDay(endTime).plusDays(1)));
        }

        if (Objects.nonNull(returnFlowState)) {
            // 页面的待填写物流，传的是AUDIT，这里必须查退货单
            if (Objects.equals(returnFlowState, ReturnFlowState.AUDIT)) {
                criteria.add(Criteria.where("returnFlowState").is(returnFlowState.getStateId()));
                criteria.add(Criteria.where("returnType").is(ReturnType.RETURN.toValue()));
            }
            // 页面的待退款，传的是RECEIVED，这里必须查退货单的RECEIVED状态 和 退款单的AUDIT状态
            else if (Objects.equals(returnFlowState, ReturnFlowState.RECEIVED)) {
                // 待退款查询条件
                Criteria waitRefund = new Criteria();
                // 或条件查询
                criteria.add(waitRefund.orOperator(
                        Criteria.where("returnFlowState").is(ReturnFlowState.RECEIVED.getStateId()).and("returnType").is(ReturnType.RETURN.toValue()),// 退货的待退款
                        Criteria.where("returnFlowState").is(ReturnFlowState.AUDIT.getStateId()).and("returnType").is(ReturnType.REFUND.toValue())));// 退款的待退款
            } else {
                criteria.add(Criteria.where("returnFlowState").is(returnFlowState.getStateId()));
            }
        }
        if(CollectionUtils.isEmpty(criteria)){
            return new Criteria();
        }
        return new Criteria().andOperator(criteria.toArray(new Criteria[criteria.size()]));

    }

    @Override
    public Integer getPageNum() {
        return super.getPageNum();
    }

    @Override
    public String getSortColumn() {
        return "createTime";
    }

    @Override
    public String getSortRole() {
        return "desc";
    }

    @Override
    public String getSortType() {
        return FieldType.Date.toString();
    }

    private String buildEsFuzzyStr(String str) {
        return String.format("\"*%s*\" OR *%s*", str, str);
    }
}
