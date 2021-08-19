package com.wanmi.sbc.customer.company.request;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.StoreType;
import com.wanmi.sbc.customer.company.model.root.CompanyInfo;
import com.wanmi.sbc.customer.employee.model.root.Employee;
import com.wanmi.sbc.customer.store.model.root.Store;
import com.wanmi.sbc.customer.util.XssUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by sunkun on 2017/11/6.
 */
@Data
public class CompanyRequest extends BaseQueryRequest {

    /**
     * 商家id列表
     */
    private List<Long> companyInfoIds;

    /**
     * 模糊商家名称
     */
    private String supplierName;

    /**
     * 精确商家名称
     */
    private String equalSupplierName;

    /**
     * 店铺名称
     */
    private String storeName;

    /**
     * 商家账号
     */
    private String accountName;

    /**
     * 商家编号
     */
    private String companyCode;

    /**
     * 签约结束日期
     */
    private String contractEndDate;

    /**
     * 账户状态  -1:全部 0：启用   1：禁用
     */
    private Integer accountState;

    /**
     * 店铺状态 -1：全部,0:开启,1:关店,2:过期
     */
    private Integer storeState;

    /**
     * 审核状态 -1全部 ,0:待审核,1:已审核,2:审核未通过
     */
    private Integer auditState;

    /**
     * 商家删除状态
     */
    private DeleteFlag deleteFlag;

    /**
     * 申请入驻时间 开始时间
     */
    private String applyEnterTimeStart;

    /**
     * 申请入驻时间 结束时间
     */
    private String applyEnterTimeEnd;

    /**
     * 是否确认打款 (-1:全部,0:否,1:是)
     */
    private Integer remitAffirm;

    /**
     * 商家类型 0、供应商 1、商家
     */
    @ApiModelProperty(value = "商家类型 0、供应商 1、商家")
    private StoreType storeType;

    /**
     * 封装公共条件
     *
     * @return
     */
    public Specification<CompanyInfo> getWhereCriteria() {
        return (root, cquery, cbuild) -> {
            List<Predicate> predicates = new ArrayList<>();
            Join<CompanyInfo, Store> companyInfoStoreJoin = root.join("storeList", JoinType.LEFT);
            Join<CompanyInfo, Employee> companyInfoEmployeeJoin = root.join("employeeList", JoinType.LEFT);
            if (CollectionUtils.isNotEmpty(companyInfoIds)) {
                CriteriaBuilder.In in = cbuild.in(root.get("companyInfoId"));
                companyInfoIds.forEach(id -> in.value(id));
                predicates.add(in);
            }
            if (StringUtils.isNotBlank(equalSupplierName)) {
                predicates.add(cbuild.equal(root.get("supplierName"), XssUtils.replaceLikeWildcard(equalSupplierName)));
            }
            if (StringUtils.isNotBlank(supplierName)) {
                predicates.add(cbuild.like(root.get("supplierName"), "%" + XssUtils.replaceLikeWildcard(supplierName)
                        + "%"));
            }
            if (StringUtils.isNotBlank(companyCode)) {
                predicates.add(cbuild.like(root.get("companyCode"), "%" + XssUtils.replaceLikeWildcard(companyCode) +
                        "%"));
            }
            if (Objects.nonNull(remitAffirm) && remitAffirm >= 0) {
                predicates.add(cbuild.equal(root.get("remitAffirm"), remitAffirm));
            }
            if (Objects.nonNull(deleteFlag)) {
                predicates.add(cbuild.equal(root.get("delFlag"), deleteFlag.toValue()));
            }
            predicates.add(cbuild.greaterThan(root.get("companyInfoId"), 0));

            /** ################## employee join begin ################## **/
            if (StringUtils.isNotBlank(accountName)) {

                predicates.add(cbuild.like(companyInfoEmployeeJoin.get("accountName"), "%" + XssUtils
                        .replaceLikeWildcard(accountName) + "%"));
            }
            if (Objects.nonNull(accountState) && accountState >= 0) {
                predicates.add(cbuild.equal(companyInfoEmployeeJoin.get("accountState"), accountState));
            }
            /** ################## employee join end ################## **/

            /** ################## store join begin ################## **/
            if (StringUtils.isNotBlank(storeName)) {
                predicates.add(cbuild.like(companyInfoStoreJoin.get("storeName"), "%" + XssUtils.replaceLikeWildcard
                        (storeName) + "%"));
            }
            if (StringUtils.isNotBlank(applyEnterTimeStart)) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                predicates.add(cbuild.greaterThan(companyInfoStoreJoin.get("applyEnterTime"), LocalDateTime.of
                        (LocalDate.parse(applyEnterTimeStart, formatter), LocalTime.MIN)));
            }
            if (StringUtils.isNotBlank(applyEnterTimeEnd)) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                predicates.add(cbuild.lessThan(companyInfoStoreJoin.get("applyEnterTime"), LocalDateTime.of(LocalDate
                        .parse(applyEnterTimeEnd, formatter), LocalTime.MAX)));
            }
            if (StringUtils.isNotBlank(contractEndDate)) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                predicates.add(cbuild.lessThan(companyInfoStoreJoin.get("contractEndDate"), LocalDateTime.of
                        (LocalDate.parse(contractEndDate, formatter), LocalTime.MIN)));
            }
            if (Objects.nonNull(storeState) && storeState >= 0) {
                if (storeState == 2) {
                    predicates.add(cbuild.lessThan(companyInfoStoreJoin.get("contractEndDate"), LocalDateTime.now()));
                } else if (storeState == 0) {
                    predicates.add(cbuild.greaterThan(companyInfoStoreJoin.get("contractEndDate"), LocalDateTime.now
                            ()));
                    predicates.add(cbuild.equal(companyInfoStoreJoin.get("storeState"), storeState));
                } else {
                    predicates.add(cbuild.equal(companyInfoStoreJoin.get("storeState"), storeState));
                }
            }
            if (Objects.nonNull(auditState) && auditState >= 0) {
                predicates.add(cbuild.equal(companyInfoStoreJoin.get("auditState"), auditState));
            }
            if (Objects.nonNull(storeType)) {
                predicates.add(cbuild.equal(companyInfoStoreJoin.get("storeType"), storeType));
            }
            /** ################## store join end ################## **/
            Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);
            return p.length == 0 ? null : p.length == 1 ? p[0] : cbuild.and(p);
        };
    }

}
