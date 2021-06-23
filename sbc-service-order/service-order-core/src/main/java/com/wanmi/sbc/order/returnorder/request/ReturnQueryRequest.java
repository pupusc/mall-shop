package com.wanmi.sbc.order.returnorder.request;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.common.enums.ChannelType;
import com.wanmi.sbc.common.enums.ThirdPlatformType;
import com.wanmi.sbc.common.util.DateUtil;
import com.wanmi.sbc.order.bean.enums.ReturnFlowState;
import com.wanmi.sbc.order.bean.enums.ReturnType;
import com.wanmi.sbc.order.util.XssUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
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
@EqualsAndHashCode(callSuper = true)
@Data
public class ReturnQueryRequest extends BaseQueryRequest {

    private String rid;

    private String tid;

    private List<String> tids;

    private String ptid;

    private String businessTailId;

    /**
     * 购买人编号
     */
    private String buyerId;

    private String buyerName;

    private String buyerAccount;

    /**
     * 收货人
     */
    private String consigneeName;
    private String consigneePhone;

    /**
     * 供应商
     */
    private String providerName;
    private String providerCode;

    /**
     * 退单流程状态
     */
    private ReturnFlowState returnFlowState;

    /**
     * 商品名称
     */
    private String skuName;

    private String skuNo;

    /**
     * 退单编号列表
     */
    private String[] rids;

    /**
     * 退单创建开始时间，精确到天
     */
    private String beginTime;

    /**
     * 退单创建结束时间，精确到天
     */
    private String endTime;

    /**
     * 商家名称
     */
    private Long companyInfoId;

    /**
     * 店铺ID
     */
    private Long storeId;

    /**
     * 商家名称
     */
    private String supplierName;

    /**
     * 商家编号
     */
    private String supplierCode;

    /**
     * 业务员id
     */
    private String employeeId;


    /**
     * 业务员id集合
     */
    private List<String> employeeIds;


    /**
     * 客户id
     */
    private Object[] customerIds;

    /**
     * pc搜索条件
     */
    private String tradeOrSkuName;

    /**
     * 邀请人会员id
     */
    private String inviteeId;

    /**
     * 分销渠道类型
     */
    private ChannelType channelType;

    /**
     * 供应商公司id
     */
    private Long providerCompanyInfoId;

    /**
     * 订单所属第三方平台类型
     */
    private ThirdPlatformType thirdPlatformType;

    /**
     * 是否存在发起第三方平台申请
     */
    private Boolean thirdPlatFormApplyFlag;

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

        // 订单编号
        if (StringUtils.isNotBlank(tid)) {
            criteria.add(XssUtils.regex("tid", tid));
        }

        // 订单编号List
        if (CollectionUtils.isNotEmpty(tids)) {
            criteria.add(Criteria.where("tid").in(tids));
        }

        // 子订单编号
        if (StringUtils.isNotBlank(ptid)) {
            criteria.add(XssUtils.regex("ptid", ptid));
        }

        // 尾款退单编号
        if (StringUtils.isNotBlank(businessTailId)) {
            criteria.add(Criteria.where("businessTailId").is(businessTailId));
        }

        if (Objects.nonNull(customerIds) && customerIds.length > 0) {
            criteria.add(Criteria.where("buyer.id").in(Arrays.asList(customerIds)));
        }

        if (CollectionUtils.isNotEmpty(employeeIds)) {
            criteria.add(Criteria.where("buyer.employeeId").in(employeeIds));
        }


        if (StringUtils.isNotBlank(employeeId)) {
            criteria.add(Criteria.where("buyer.employeeId").is(employeeId));
        }

        if (StringUtils.isNotBlank(buyerId)) {
            criteria.add(Criteria.where("buyer.id").is(buyerId));
        }

        //邀请人id
        if (StringUtils.isNotBlank(inviteeId)) {
            criteria.add(Criteria.where("inviteeId").is(inviteeId));
        }

        //分销渠道类型
        if (Objects.nonNull(channelType)) {
            if (channelType == ChannelType.SHOP) {
                criteria.add(Criteria.where("channelType").is(ChannelType.SHOP.toString()));
            } else if (channelType == ChannelType.PC_MALL) {
                criteria.add(Criteria.where("channelType").is(ChannelType.PC_MALL.toString()));
            } else if (channelType == ChannelType.MALL) {
                criteria.add(Criteria.where("channelType").is(ChannelType.MALL.toString()));
            }
        }
        // 客户信息
        if (StringUtils.isNotBlank(buyerName)) {
            criteria.add(XssUtils.regex("buyer.name", buyerName));
        } else if (StringUtils.isNotBlank(buyerAccount)) {
            criteria.add(XssUtils.regex("buyer.account", buyerAccount));
        }
        //供应商编号
        if(StringUtils.isNotBlank(providerName)){
            criteria.add(XssUtils.regex("providerName",providerName));
        }
        if(StringUtils.isNotBlank(providerCode)){
            criteria.add(XssUtils.regex("providerCode",providerCode));
        }
        // 收货人
        if (StringUtils.isNotBlank(consigneeName)) {
            criteria.add(XssUtils.regex("consignee.name", consigneeName));
        } else if (StringUtils.isNotBlank(consigneePhone)) {
            criteria.add(XssUtils.regex("consignee.phone", consigneePhone));
        }

        /**
         * 商家名称
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

        if (Objects.nonNull(thirdPlatformType)) {
            criteria.add(Criteria.where("thirdPlatformType").is(thirdPlatformType));
        }

        if (Boolean.TRUE.equals(thirdPlatFormApplyFlag)) {
            criteria.add(Criteria.where("thirdReasonId").exists(true));
            criteria.add(Criteria.where("thirdReasonId").ne(null));
        }

        if (StringUtils.isEmpty(tradeOrSkuName)) {
            // 退单编号
            if (StringUtils.isNotBlank(rid)) {
                criteria.add(XssUtils.regex("id", rid));
            }

            // sku
            if (StringUtils.isNotBlank(skuName)) {
                criteria.add(XssUtils.regex("returnItems.skuName", skuName));
            } else if (StringUtils.isNotBlank(skuNo)) {
                criteria.add(XssUtils.regex("returnItems.skuNo", skuNo));
            }
        } else {
            // 或条件查询
            criteria.add(new Criteria().orOperator(XssUtils.regex("id", tradeOrSkuName), XssUtils.regex("returnItems.skuName", tradeOrSkuName)));
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
                        Criteria.where("returnFlowState").is(ReturnFlowState.AUDIT.getStateId()).and("returnType").is(ReturnType.REFUND.toValue()),// 退款的待退款
                        Criteria.where("returnFlowState").is(ReturnFlowState.REFUND_FAILED.getStateId())));// 退款失败
            } else {
                criteria.add(Criteria.where("returnFlowState").is(returnFlowState.getStateId()));
            }
        }

        // 供应商公司id
        if (Objects.nonNull(providerCompanyInfoId)) {
            criteria.add(Criteria.where("providerCompanyInfoId").is(providerCompanyInfoId));
        }

        if(CollectionUtils.isEmpty(criteria)){
            return new Criteria();
        }
        return new Criteria().andOperator(criteria.toArray(new Criteria[criteria.size()]));

    }

//    public QueryBuilder buildEs() {
//
//        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
//
//        // ids 用于导出时的查询，如果传了ids，以ids为准
//        if (rids != null && rids.length > 0) {
//
//           // boolQueryBuilder.must(QueryBuilders.idsQuery("return_order").ids(rids));
//
//            return boolQueryBuilder;
//        }
//
//        // 退单编号
//        if (StringUtils.isNoneBlank(rid)) {
//            boolQueryBuilder.must(QueryBuilders.queryStringQuery(buildEsFuzzyStr(rid)).defaultField("id"));
//        }
//
//        // 订单编号
//        if (StringUtils.isNoneBlank(tid)) {
//            boolQueryBuilder.must(QueryBuilders.queryStringQuery(buildEsFuzzyStr(tid)).defaultField("tid"));
//        }
//
//        if(Objects.nonNull(customerIds) && customerIds.length > 0){
//            boolQueryBuilder.must(QueryBuilders.termsQuery("buyer.id",customerIds));
//        }
//
//        if(StringUtils.isNotBlank(employeeId)){
//            boolQueryBuilder.must(QueryBuilders.termQuery("buyer.employeeId",employeeId));
//        }
//
//        // 客户信息
//        if (StringUtils.isNotBlank(buyerName)) {
//            boolQueryBuilder.must(QueryBuilders.queryStringQuery(buildEsFuzzyStr(buyerName)).defaultField("buyer.name"));
//        } else if (StringUtils.isNotBlank(buyerAccount)) {
//            boolQueryBuilder.must(QueryBuilders.queryStringQuery(buildEsFuzzyStr(buyerAccount)).defaultField("buyer.account"));
//        }
//
//        // 收货人
//        if (StringUtils.isNotBlank(consigneeName)) {
//            boolQueryBuilder.must(QueryBuilders.queryStringQuery(buildEsFuzzyStr(consigneeName)).defaultField("consignee.name"));
//        } else if (StringUtils.isNotBlank(consigneePhone)) {
//            boolQueryBuilder.must(QueryBuilders.queryStringQuery(buildEsFuzzyStr(consigneePhone)).defaultField("consignee.phone"));
//        }
//
//        /**
//         * 商家名称
//         */
//        if (StringUtils.isNotBlank(supplierName)) {
//            boolQueryBuilder.must(QueryBuilders.queryStringQuery(buildEsFuzzyStr(supplierName)).defaultField("company.supplierName"));
//        }
//
//        if (StringUtils.isNotBlank(supplierCode)) {
//            boolQueryBuilder.must(QueryBuilders.queryStringQuery(buildEsFuzzyStr(supplierCode)).defaultField("company.companyCode"));
//        }
//
//        if (!Objects.isNull(companyInfoId)) {
//            boolQueryBuilder.must(QueryBuilders.matchQuery("company.companyInfoId",companyInfoId));
//        }
//
//        if (!Objects.isNull(storeId)) {
//            boolQueryBuilder.must(QueryBuilders.matchQuery("company.storeId",storeId));
//        }
//
//        // sku
//        if (StringUtils.isNotBlank(skuName)) {
//            boolQueryBuilder.must(QueryBuilders.queryStringQuery(buildEsFuzzyStr(skuName)).defaultField("returnItems.skuName"));
//        } else if (StringUtils.isNotBlank(skuNo)) {
//            boolQueryBuilder.must(QueryBuilders.queryStringQuery(buildEsFuzzyStr(skuNo)).defaultField("returnItems.skuNo"));
//        }
//
//        // createTime
//        if (StringUtils.isNotBlank(beginTime) || StringUtils.isNotBlank(endTime)) {
//            RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("createTime");
//            if (StringUtils.isNotBlank(beginTime)) {
//                rangeQueryBuilder.gte(DateUtil.parseDay(beginTime).format(formatter));
//            }
//            if (StringUtils.isNotBlank(endTime)) {
//                // 小与传入的结束时间+1天，零点前
//                rangeQueryBuilder.lt(DateUtil.parseDay(endTime).plusDays(1).format(formatter));
//            }
//            boolQueryBuilder.must(rangeQueryBuilder);
//        }
//
//        if (returnFlowState != null) {
//            // 页面的待填写物流，传的是AUDIT，这里必须查退货单
//            if (Objects.equals(returnFlowState, ReturnFlowState.AUDIT)) {
//                boolQueryBuilder.must(QueryBuilders.matchQuery("returnFlowState", returnFlowState.getStateId()));
//                boolQueryBuilder.must(QueryBuilders.matchQuery("returnType", ReturnType.RETURN.toValue()));
//            }
//            // 页面的待退款，传的是RECEIVED，这里必须查退货单的RECEIVED状态 和 退款单的AUDIT状态
//            else if (Objects.equals(returnFlowState, ReturnFlowState.RECEIVED)) {
//                // 待退款查询条件
//                BoolQueryBuilder waitRefundBuilder = QueryBuilders.boolQuery();
//
//                // 退货的待退款
//                BoolQueryBuilder returnQueryBuilder = QueryBuilders.boolQuery();
//                returnQueryBuilder.must(QueryBuilders.matchQuery("returnFlowState", ReturnFlowState.RECEIVED));
//                returnQueryBuilder.must(QueryBuilders.matchQuery("returnType", ReturnType.RETURN.toValue()));
//
//                // 退款的待退款
//                BoolQueryBuilder refundQueryBuilder = QueryBuilders.boolQuery();
//                refundQueryBuilder.must(QueryBuilders.matchQuery("returnFlowState", ReturnFlowState.AUDIT));
//                refundQueryBuilder.must(QueryBuilders.matchQuery("returnType", ReturnType.REFUND.toValue()));
//
//                // 退款失败
//                BoolQueryBuilder refundFailedBuilder = QueryBuilders.boolQuery();
//                refundFailedBuilder.must(QueryBuilders.matchQuery("returnFlowState", ReturnFlowState.REFUND_FAILED));
//
//                // 或条件查询
//                waitRefundBuilder.should(returnQueryBuilder).should(refundQueryBuilder).should(refundFailedBuilder);
//                boolQueryBuilder.must(waitRefundBuilder);
//            } else {
//                boolQueryBuilder.must(QueryBuilders.matchQuery("returnFlowState", returnFlowState.getStateId()));
//            }
//        }
//
//        return boolQueryBuilder;
//
//    }


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
        return "Date";
    }

//    private String buildEsFuzzyStr(String str) {
//        return String.format("\"*%s*\" OR *%s*", str, str);
//    }
}
