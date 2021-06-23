package com.wanmi.sbc.customer;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.StringUtil;
import com.wanmi.sbc.customer.bean.enums.CheckState;
import com.wanmi.sbc.customer.bean.enums.CustomerStatus;
import com.wanmi.sbc.customer.bean.enums.CustomerType;
import com.wanmi.sbc.customer.detail.model.root.CustomerDetail;
import com.wanmi.sbc.customer.model.root.Customer;
import com.wanmi.sbc.customer.storecustomer.root.StoreCustomerRela;
import com.wanmi.sbc.customer.util.XssUtils;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by hht on 2017/11/16.
 */
@Data
public class CustomerDetailQueryRequestS2b extends BaseQueryRequest {

    /**
     * 客户ID
     */
    private String customerId;

    /**
     * 会员名称
     */
    private String customerName;

    /**
     * 省
     */
    private Long provinceId;

    /**
     * 市
     */
    private Long cityId;

    /**
     * 区
     */
    private Long areaId;

    /**
     * 客户等级ID
     */
    private Long customerLevelId;

    /**
     * 账号状态 0：启用中  1：禁用中
     */
    private CustomerStatus customerStatus;

    /**
     * 客户账户
     */
    private String customerAccount;

    /**
     * 商铺Id
     */
    private Long storeId;

    /**
     * 商家Id
     */
    private Long companyInfoId;

    /**
     * 客户类型
     */
    private CustomerType customerType;

    /**
     * 关键字搜索，目前范围：会员名称、客户账户
     */
    private String keyword;

    /**
     * 所属业务员id
     */
    private String employeeId;

    /**
     * 封装公共条件
     *
     * @return
     */
    public Specification<Customer> getWhereCriteria() {
        return (root, cquery, cbuild) -> {
            List<Predicate> predicates = new ArrayList<>();

            //联查
            Join<Customer, CustomerDetail> customerDetailJoin = root.join("customerDetail");

            predicates.add(cbuild.equal(root.get("checkState"), CheckState.CHECKED));

            if (StringUtils.isNotEmpty(customerAccount)) {
                predicates.add(cbuild.like(root.get("customerAccount"), new StringBuffer().append("%").append
                        (XssUtils.replaceLikeWildcard(customerAccount.trim())).append("%").toString()));
            }
            if (StringUtils.isNotEmpty(customerId)) {
                predicates.add(cbuild.equal(root.get("customerId"), customerId.trim()));
            }
            if (StringUtils.isNotEmpty(customerName)) {
                predicates.add(cbuild.like(customerDetailJoin.get("customerName"), new StringBuffer().append("%")
                        .append(XssUtils.replaceLikeWildcard(customerName.trim())).append("%").toString()));
            }
            if (StringUtils.isNotEmpty(employeeId)) {
                predicates.add(cbuild.equal(customerDetailJoin.get("employeeId"), employeeId));
            }
            if (provinceId != null) {
                predicates.add(cbuild.equal(customerDetailJoin.get("provinceId"), provinceId));
            }
            if (cityId != null) {
                predicates.add(cbuild.equal(customerDetailJoin.get("cityId"), cityId));
            }
            if (areaId != null) {
                predicates.add(cbuild.equal(customerDetailJoin.get("areaId"), areaId));
            }
            if (customerStatus != null) {
                predicates.add(cbuild.equal(customerDetailJoin.get("customerStatus"), customerStatus));
            }
            //删除标记
            predicates.add(cbuild.equal(root.get("delFlag"), DeleteFlag.NO));
            predicates.add(cbuild.equal(customerDetailJoin.get("delFlag"), DeleteFlag.NO));

            //关键字搜索
            if (StringUtils.isNotBlank(keyword)) {
                String str = StringUtil.SQL_LIKE_CHAR.concat(XssUtils.replaceLikeWildcard(keyword.trim())).concat
                        (StringUtil.SQL_LIKE_CHAR);
                predicates.add(cbuild.or(cbuild.like(customerDetailJoin.get("customerName"), str), cbuild.like(root
                        .get("customerAccount"), str)));
            }

            //联查
            Join<Customer, StoreCustomerRela> storeCustomerJoin = root.join("storeCustomerRelaListByAll", JoinType
                    .LEFT);
            if (Objects.nonNull(storeId)) {
                predicates.add(cbuild.equal(storeCustomerJoin.get("storeId"), storeId));
            }
            if (customerLevelId != null) {
                predicates.add(cbuild.equal(storeCustomerJoin.get("customerLevelId"), customerLevelId));
            }
            if (Objects.nonNull(companyInfoId)) {
                predicates.add(cbuild.equal(storeCustomerJoin.get("companyInfoId"), companyInfoId));
            }
            if (Objects.nonNull(customerType)) {
                predicates.add(cbuild.equal(storeCustomerJoin.get("customerType"), customerType));
            }

            Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);

            return p.length == 0 ? null : p.length == 1 ? p[0] : cbuild.and(p);
        };
    }
}
