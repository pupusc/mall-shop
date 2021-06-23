package com.wanmi.sbc.customer.api.request.customer;

import com.wanmi.sbc.customer.api.request.CustomerBaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 会员查询参数
 * Created by CHENLI on 2017/4/19.
 */
@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerListByConditionRequest extends CustomerBaseRequest {
    private static final long serialVersionUID = 7514844965632390600L;
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
     * 名称
     */
    @ApiModelProperty(value = "名称")
    private String customerName;

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
     * 删除标志 0未删除 1已删除
     */
    @ApiModelProperty(value = "删除标志", dataType = "com.wanmi.sbc.customer.bean.enums.CheckState")
    private Integer delFlag;

    /**
     * 审核状态 0：待审核 1：已审核 2：审核未通过
     */
    @ApiModelProperty(value = "审核状态", dataType = "com.wanmi.sbc.customer.bean.enums.CheckState")
    private Integer checkState;

    /**
     * 审核驳回原因
     */
    @ApiModelProperty(value = "审核驳回原因")
    private String rejectReason;

    @ApiModelProperty(value = "客户等级ID集合")
    private List<Long> customerLevelIds;

//    /**
//     * 封装公共条件
//     *
//     * @return
//     */
//    public Specification<Customer> getWhereCriteria() {
//        return (root, cquery, cbuild) -> {
//            List<Predicate> predicates = new ArrayList<>();
//
//            if (CollectionUtils.isNotEmpty(customerIds)) {
//                predicates.add(root.get("customerId").in(customerIds));
//            }
//            if (Objects.nonNull(customerAccount) && StringUtils.isNotEmpty(customerAccount.trim())) {
//                predicates.add(cbuild.like(root.get("customerAccount"), buildLike(customerAccount)));
//            }
//            if (Objects.nonNull(customerId) && StringUtils.isNotEmpty(customerId.trim())) {
//                predicates.add(cbuild.equal(root.get("customerId"), customerId));
//            }
//            if (customerLevelId != null) {
//                predicates.add(cbuild.equal(root.get("customerLevelId"), customerLevelId));
//            }
//            if (checkState != null) {
//                predicates.add(cbuild.equal(root.get("checkState"), checkState));
//            }
//            //删除标记
//            if (delFlag != null) {
//                predicates.add(cbuild.equal(root.get("delFlag"), delFlag));
//            }
//            Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);
//            return p.length == 0 ? null : p.length == 1 ? p[0] : cbuild.and(p);
//        };
//    }
//
//    private static String buildLike(String field) {
//        StringBuilder stringBuilder = new StringBuilder();
//        return stringBuilder.append("%").append(XssUtils.replaceLikeWildcard(field)).append("%").toString();
//    }
}
