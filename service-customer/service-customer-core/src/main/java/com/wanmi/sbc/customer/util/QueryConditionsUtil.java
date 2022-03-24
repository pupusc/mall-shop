package com.wanmi.sbc.customer.util;

import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.common.util.StringUtil;
import com.wanmi.sbc.customer.api.request.customer.*;
import com.wanmi.sbc.customer.api.request.distribution.DistributionCommissionPageRequest;
import com.wanmi.sbc.customer.bean.enums.CheckState;
import com.wanmi.sbc.customer.bean.enums.CustomerStatus;
import com.wanmi.sbc.customer.bean.enums.CustomerType;
import com.wanmi.sbc.customer.bean.enums.EnterpriseCheckState;
import com.wanmi.sbc.customer.detail.model.root.CustomerDetail;
import com.wanmi.sbc.customer.distribution.model.root.DistributionCustomer;
import com.wanmi.sbc.customer.distribution.model.root.InviteNewRecord;
import com.wanmi.sbc.customer.model.root.Customer;
import com.wanmi.sbc.customer.storecustomer.root.StoreCustomerRela;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

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

public class QueryConditionsUtil {
    /**
     * 封装公共条件
     *
     * @return
     */
    public static Specification<CustomerDetail> getWhereCriteria(CustomerDetailPageRequest request) {
        String equalCustomerAccount = request.getEqualCustomerAccount();
        String customerAccount = request.getCustomerAccount();
        Long customerLevelId = request.getCustomerLevelId();
        Integer checkState = request.getCheckState();
        CustomerType customerType = request.getCustomerType();
        String keyword = request.getKeyword();
        List<String> customerIds = request.getCustomerIds();
        String customerName = request.getCustomerName();
        String employeeId = request.getEmployeeId();
        List<String> employeeIds = request.getEmployeeIds();
        String customerDetailId = request.getCustomerDetailId();
        String customerId = request.getCustomerId();
        Long provinceId = request.getProvinceId();
        Long cityId = request.getCityId();
        Long areaId = request.getAreaId();
        Long streetId = request.getStreetId();
        CustomerStatus customerStatus = request.getCustomerStatus();
        Integer delFlag = request.getDelFlag();
        DefaultFlag isDistributor = request.getIsDistributor();
        Long pointsAvailableBegin = request.getPointsAvailableBegin();
        Long pointsAvailableEnd = request.getPointsAvailableEnd();
        Boolean isMyCustomer  = request.getIsMyCustomer();
        DefaultFlag defaultFlag = request.getDefaultFlag();
        return (root, cquery, cbuild) -> {
            List<Predicate> predicates = new ArrayList<>();

            //联查
            Join<Customer, CustomerDetail> customerDetailJoin = root.join("customer");

            if (StringUtils.isNotBlank(equalCustomerAccount)) {
                predicates.add(cbuild.equal(customerDetailJoin.get("customerAccount"), equalCustomerAccount.trim()));
            }

            if (Objects.nonNull(customerAccount) && StringUtils.isNotBlank(customerAccount.trim())) {
                predicates.add(cbuild.like(customerDetailJoin.get("customerAccount"), buildLike(customerAccount)));
            }
            if (customerLevelId != null) {
                predicates.add(cbuild.equal(customerDetailJoin.get("customerLevelId"), customerLevelId));
            }
            if (checkState != null) {
                predicates.add(cbuild.equal(customerDetailJoin.get("checkState"), checkState));
            }
            if (customerType != null) {
                predicates.add(cbuild.equal(customerDetailJoin.get("customerType"), customerType));
            }
            //关键字搜索
            if (StringUtils.isNotBlank(keyword)) {
                String str = StringUtil.SQL_LIKE_CHAR.concat(XssUtils.replaceLikeWildcard(keyword.trim())).concat
                        (StringUtil.SQL_LIKE_CHAR);
                predicates.add(cbuild.or(cbuild.like(root.get("customerName"), str), cbuild.like(customerDetailJoin
                        .get("customerAccount"), str)));
            }
            if (isDistributor != null){
                predicates.add(cbuild.equal(customerDetailJoin.get("customerDetail").get("isDistributor"), isDistributor));
            }
            // 会员可用积分段查询开始
            if (pointsAvailableBegin != null) {
                predicates.add(cbuild.greaterThanOrEqualTo(customerDetailJoin.get("pointsAvailable"), pointsAvailableBegin));
            }
            // 会员可用积分段查询结束
            if (pointsAvailableEnd != null) {
                predicates.add(cbuild.lessThanOrEqualTo(customerDetailJoin.get("pointsAvailable"), pointsAvailableEnd));
            }

            if (CollectionUtils.isNotEmpty(customerIds)) {
                predicates.add(root.get("customerId").in(customerIds));
//                predicates.add(root.get("customer").get("customerId").in(customerIds));
            }
            if (Objects.nonNull(customerName) && StringUtils.isNotBlank(customerName.trim())) {
                predicates.add(cbuild.like(root.get("customerName"), buildLike(customerName)));
            }
            if (Objects.nonNull(employeeId) && StringUtils.isNotBlank(employeeId.trim())) {
                predicates.add(cbuild.equal(root.get("employeeId"), employeeId));
            }
            if (CollectionUtils.isNotEmpty(employeeIds)){
                predicates.add(root.get("employeeId").in(employeeIds));
            }
            if (Objects.nonNull(customerDetailId) && StringUtils.isNotBlank(customerDetailId.trim())) {
                predicates.add(cbuild.equal(root.get("customerDetailId"), customerDetailId));
            }
            if (Objects.nonNull(customerId) && StringUtils.isNotBlank(customerId.trim())) {
                predicates.add(cbuild.equal(root.get("customer").get("customerId"), customerId));
            }
            if (provinceId != null) {
                predicates.add(cbuild.equal(root.get("provinceId"), provinceId));
            }
            if (cityId != null) {
                predicates.add(cbuild.equal(root.get("cityId"), cityId));
            }
            if (areaId != null) {
                predicates.add(cbuild.equal(root.get("areaId"), areaId));
            }
            if (streetId != null) {
                predicates.add(cbuild.equal(root.get("streetId"), streetId));
            }
            if (customerStatus != null) {
                predicates.add(cbuild.equal(root.get("customerStatus"), customerStatus));
            }
            if (Objects.nonNull(isMyCustomer)) {
                predicates.add(cbuild.equal(customerDetailJoin.get("customerType"), isMyCustomer?CustomerType
                        .SUPPLIER:CustomerType.PLATFORM));
            }
            //查询非企业会员
            if (defaultFlag ==null || defaultFlag.equals(DefaultFlag.NO)){
                predicates.add(cbuild.equal(customerDetailJoin.get("enterpriseCheckState"), EnterpriseCheckState.INIT));
            }
            //删除标记
            predicates.add(cbuild.equal(customerDetailJoin.get("delFlag"), delFlag));
            predicates.add(cbuild.equal(root.get("delFlag"), delFlag));
            Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);
            return p.length == 0 ? null : p.length == 1 ? p[0] : cbuild.and(p);
        };
    }


    /**
     * 封装公共条件
     *
     * @return
     */
    public static Specification<Customer> getWhereCriteria(CustomerDetailPageForSupplierRequest request) {
        String customerAccount = request.getCustomerAccount();
        String customerId = request.getCustomerId();
        String customerName = request.getCustomerName();
        String employeeId = request.getEmployeeId();
        List<String> employeeIds = request.getEmployeeIds();
        Long provinceId = request.getProvinceId();
        Long cityId = request.getCityId();
        Long areaId = request.getAreaId();
        CustomerStatus customerStatus = request.getCustomerStatus();
        String keyword = request.getKeyword();
        Long storeId = request.getStoreId();
        Long customerLevelId = request.getCustomerLevelId();
        Long companyInfoId = request.getCompanyInfoId();
        CustomerType customerType = request.getCustomerType();
        Boolean isMyCustomer  = request.getIsMyCustomer();
        List<String> customerIds = request.getCustomerIds();

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

            if(CollectionUtils.isNotEmpty(customerIds)){
                predicates.add(root.get("customerId").in(customerIds));
            }

            if (StringUtils.isNotEmpty(customerName)) {
                predicates.add(cbuild.like(customerDetailJoin.get("customerName"), new StringBuffer().append("%")
                        .append(XssUtils.replaceLikeWildcard(customerName.trim())).append("%").toString()));
            }
            if (StringUtils.isNotEmpty(employeeId)) {
                predicates.add(cbuild.equal(customerDetailJoin.get("employeeId"), employeeId));
            }
            if (CollectionUtils.isNotEmpty(employeeIds)) {
                predicates.add(customerDetailJoin.get("employeeId").in(employeeIds));
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
                predicates.add(cbuild.equal(storeCustomerJoin.get("storeLevelId"), customerLevelId));
            }
            if (Objects.nonNull(companyInfoId)) {
                predicates.add(cbuild.equal(storeCustomerJoin.get("companyInfoId"), companyInfoId));
            }
            if (Objects.nonNull(customerType)) {
                predicates.add(cbuild.equal(storeCustomerJoin.get("customerType"), customerType));
            }
            if (Objects.nonNull(isMyCustomer)) {
                predicates.add(cbuild.equal(storeCustomerJoin.get("customerType"), isMyCustomer?CustomerType
                        .SUPPLIER:CustomerType.PLATFORM));
            }

            Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);

            return p.length == 0 ? null : p.length == 1 ? p[0] : cbuild.and(p);
        };
    }

    /**
     * 封装公共条件
     *
     * @return
     */
    public static Specification<CustomerDetail> getWhereCriteria(CustomerDetailListByConditionRequest request) {
        String equalCustomerAccount = request.getEqualCustomerAccount();
        String customerAccount = request.getCustomerAccount();
        Long customerLevelId = request.getCustomerLevelId();
        Integer checkState = request.getCheckState();
        CustomerType customerType = request.getCustomerType();
        List<String> customerIds = request.getCustomerIds();
        String customerName = request.getCustomerName();
        String employeeId = request.getEmployeeId();
        List<String> employeeIds = request.getEmployeeIds();
        String customerDetailId = request.getCustomerDetailId();
        String customerId = request.getCustomerId();
        Long provinceId = request.getProvinceId();
        Long cityId = request.getCityId();
        Long areaId = request.getAreaId();
        CustomerStatus customerStatus = request.getCustomerStatus();
        Integer delFlag = request.getDelFlag();

        return (root, cquery, cbuild) -> {
            List<Predicate> predicates = new ArrayList<>();

            //联查
            Join<Customer, CustomerDetail> customerDetailJoin = root.join("customer");

            if (StringUtils.isNotBlank(equalCustomerAccount)) {
                predicates.add(cbuild.equal(customerDetailJoin.get("customerAccount"), equalCustomerAccount.trim()));
            }

            if (Objects.nonNull(customerAccount) && StringUtils.isNotBlank(customerAccount.trim())) {
                predicates.add(cbuild.like(customerDetailJoin.get("customerAccount"), buildLike(customerAccount)));
            }
            if (customerLevelId != null) {
                predicates.add(cbuild.equal(customerDetailJoin.get("customerLevelId"), customerLevelId));
            }
            if (checkState != null) {
                predicates.add(cbuild.equal(customerDetailJoin.get("checkState"), checkState));
            }
            if (customerType != null) {
                predicates.add(cbuild.equal(customerDetailJoin.get("customerType"), customerType));
            }

            if (CollectionUtils.isNotEmpty(customerIds)) {
                predicates.add(root.get("customerId").in(customerIds));
            }
            if (Objects.nonNull(customerName) && StringUtils.isNotBlank(customerName.trim())) {
                predicates.add(cbuild.like(root.get("customerName"), buildLike(customerName)));
            }
            if (Objects.nonNull(employeeId) && StringUtils.isNotBlank(employeeId.trim())) {
                predicates.add(cbuild.equal(root.get("employeeId"), employeeId));
            }
            if (CollectionUtils.isNotEmpty(employeeIds)) {
                predicates.add(root.get("employeeId").in(employeeIds));
            }
            if (Objects.nonNull(customerDetailId) && StringUtils.isNotBlank(customerDetailId.trim())) {
                predicates.add(cbuild.equal(root.get("customerDetailId"), customerDetailId));
            }
            if (Objects.nonNull(customerId) && StringUtils.isNotBlank(customerId.trim())) {
                predicates.add(cbuild.equal(root.get("customer").get("customerId"), customerId));
            }
            if (provinceId != null) {
                predicates.add(cbuild.equal(root.get("provinceId"), provinceId));
            }
            if (cityId != null) {
                predicates.add(cbuild.equal(root.get("cityId"), cityId));
            }
            if (areaId != null) {
                predicates.add(cbuild.equal(root.get("areaId"), areaId));
            }
            if (customerStatus != null) {
                predicates.add(cbuild.equal(root.get("customerStatus"), customerStatus));
            }
            //删除标记
            predicates.add(cbuild.equal(customerDetailJoin.get("delFlag"), delFlag));
            predicates.add(cbuild.equal(root.get("delFlag"), delFlag));
            Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);
            return p.length == 0 ? null : p.length == 1 ? p[0] : cbuild.and(p);
        };
    }

    /**
     * 封装公共条件
     *
     * @return
     */
    public static Specification<CustomerDetail> getWhereCriteria(CustomerDetailListByPageRequest request) {
        String equalCustomerAccount = request.getEqualCustomerAccount();
        String customerAccount = request.getCustomerAccount();
        String customerLevelId = request.getCustomerId();
        Integer checkState = request.getCheckState();
        CustomerType customerType = request.getCustomerType();
        List<String> customerIds = request.getCustomerIds();
        String customerName = request.getCustomerName();
        String employeeId = request.getEmployeeId();
        String customerDetailId = request.getCustomerDetailId();
        String customerId = request.getCustomerId();
        Long provinceId = request.getProvinceId();
        Long cityId = request.getCityId();
        Long areaId = request.getAreaId();
        CustomerStatus customerStatus = request.getCustomerStatus();
        Integer delFlag = request.getDelFlag();

        return (root, cquery, cbuild) -> {
            List<Predicate> predicates = new ArrayList<>();

            //联查
            Join<Customer, CustomerDetail> customerDetailJoin = root.join("customer");

            if (StringUtils.isNotBlank(equalCustomerAccount)) {
                predicates.add(cbuild.equal(customerDetailJoin.get("customerAccount"), equalCustomerAccount.trim()));
            }

            if (Objects.nonNull(customerAccount) && StringUtils.isNotEmpty(customerAccount.trim())) {
                predicates.add(cbuild.like(customerDetailJoin.get("customerAccount"), buildLike(customerAccount)));
            }
            if (customerLevelId != null) {
                predicates.add(cbuild.equal(customerDetailJoin.get("customerLevelId"), customerLevelId));
            }
            if (checkState != null) {
                predicates.add(cbuild.equal(customerDetailJoin.get("checkState"), checkState));
            }
            if (customerType != null) {
                predicates.add(cbuild.equal(customerDetailJoin.get("customerType"), customerType));
            }

            if (CollectionUtils.isNotEmpty(customerIds)) {
                predicates.add(root.get("customerId").in(customerIds));
//                predicates.add(root.get("customer").get("customerId").in(customerIds));
            }
            if (Objects.nonNull(customerName) && StringUtils.isNotEmpty(customerName.trim())) {
                predicates.add(cbuild.like(root.get("customerName"), buildLike(customerName)));
            }
            if (Objects.nonNull(employeeId) && StringUtils.isNotEmpty(employeeId.trim())) {
                predicates.add(cbuild.equal(root.get("employeeId"), employeeId));
            }
            if (Objects.nonNull(customerDetailId) && StringUtils.isNotEmpty(customerDetailId.trim())) {
                predicates.add(cbuild.equal(root.get("customerDetailId"), customerDetailId));
            }
            if (Objects.nonNull(customerId) && StringUtils.isNotEmpty(customerId.trim())) {
                predicates.add(cbuild.equal(root.get("customer").get("customerId"), customerId));
            }
            if (provinceId != null) {
                predicates.add(cbuild.equal(root.get("provinceId"), provinceId));
            }
            if (cityId != null) {
                predicates.add(cbuild.equal(root.get("cityId"), cityId));
            }
            if (areaId != null) {
                predicates.add(cbuild.equal(root.get("areaId"), areaId));
            }
            if (customerStatus != null) {
                predicates.add(cbuild.equal(root.get("customerStatus"), customerStatus));
            }
            //删除标记
            predicates.add(cbuild.equal(customerDetailJoin.get("delFlag"), delFlag));
            predicates.add(cbuild.equal(root.get("delFlag"), delFlag));
            Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);
            return p.length == 0 ? null : p.length == 1 ? p[0] : cbuild.and(p);
        };
    }

    /**
     * 封装公共条件
     *
     * @return
     */
    public static Specification<Customer> getWhereCriteria(CustomerListByConditionRequest request) {
        List<String> customerIds = request.getCustomerIds();
        String customerAccount = request.getCustomerAccount();
        String customerId = request.getCustomerId();
        Long customerLevelId = request.getCustomerLevelId();
        Integer checkState = request.getCheckState();
        Integer delFlag = request.getDelFlag();

        return (root, cquery, cbuild) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (CollectionUtils.isNotEmpty(customerIds)) {
                predicates.add(root.get("customerId").in(customerIds));
            }
            if (Objects.nonNull(customerAccount) && StringUtils.isNotEmpty(customerAccount.trim())) {
                predicates.add(cbuild.like(root.get("customerAccount"), buildLike(customerAccount)));
            }
            if (Objects.nonNull(customerId) && StringUtils.isNotEmpty(customerId.trim())) {
                predicates.add(cbuild.equal(root.get("customerId"), customerId));
            }
            if (customerLevelId != null) {
                predicates.add(cbuild.equal(root.get("customerLevelId"), customerLevelId));
            }
            if (checkState != null) {
                predicates.add(cbuild.equal(root.get("checkState"), checkState));
            }
            if (CollectionUtils.isNotEmpty(request.getCustomerLevelIds())){
                predicates.add(root.get("customerLevelId").in(request.getCustomerLevelIds()));
            }
            //删除标记
            if (delFlag != null) {
                predicates.add(cbuild.equal(root.get("delFlag"), delFlag));
            }
            Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);
            return p.length == 0 ? null : p.length == 1 ? p[0] : cbuild.and(p);
        };
    }


    public static Specification<Customer> getWhereCriteria(CustomerCountByStateRequest request) {
        CheckState checkState = request.getCheckState();
        DeleteFlag delFlag = request.getDeleteFlag();

        return (root, cquery, cbuild) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (checkState != null) {
                predicates.add(cbuild.equal(root.get("checkState"), checkState));
            }
            //删除标记
            if (delFlag != null) {
                predicates.add(cbuild.equal(root.get("delFlag"), delFlag));
            }
            Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);
            return p.length == 0 ? null : p.length == 1 ? p[0] : cbuild.and(p);
        };
    }


    private static String buildLike(String field) {
        StringBuilder stringBuilder = new StringBuilder();
        return stringBuilder.append("%").append(XssUtils.replaceLikeWildcard(field)).append("%").toString();
    }

    public static Specification<InviteNewRecord> getWhereCriteria(DistributionInviteNewPageRequest request) {
        String invitedNewCustomerId=request.getInvitedNewCustomerId();
        String requestCustomerId=request.getRequestCustomerId();
        String orderCode= request.getOrderCode();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return (root, cquery, cbuild) -> {
            List<Predicate> predicates = new ArrayList<>();

            // 批量查询-邀新记录表主键List
            if (CollectionUtils.isNotEmpty(request.getRecordIdList())) {
                predicates.add(root.get("recordId").in(request.getRecordIdList()));
            }

            //奖励是否入账
            if (null != request.getIsRewardRecorded()) {
                predicates.add(cbuild.equal(root.get("rewardRecorded"), request.getIsRewardRecorded().toValue()));
            }
            //入账类型
            if (null != request.getRewardFlag()) {
                predicates.add(cbuild.equal(root.get("rewardFlag"), request.getRewardFlag().toValue()));
            }
            //未入账原因
            if (null != request.getFailReasonFlag()) {
                predicates.add(cbuild.equal(root.get("failReasonFlag"), request.getFailReasonFlag().toValue()));
            }
            // 是否有效邀新
            if (null != request.getAvailableDistribution()) {
                predicates.add(cbuild.equal(root.get("availableDistribution"), request.getAvailableDistribution().toValue()));
            }

            //是否分销员(空：全部，0：否，1：是 )
            if(StringUtils.isNotBlank(request.getIsDistributor())){
                predicates.add(cbuild.equal(root.get("distributor"), Integer.parseInt(request.getIsDistributor())));
            }

            //受邀人ID
            if (StringUtils.isNotBlank(invitedNewCustomerId)){
                predicates.add(cbuild.equal(root.get("invitedNewCustomerId"), invitedNewCustomerId));
            }

            //邀请人ID
            if (StringUtils.isNotBlank(requestCustomerId)) {
                predicates.add(cbuild.equal(root.get("requestCustomerId"), requestCustomerId));
            }

            //订单编号
            if (StringUtils.isNotBlank(orderCode)) {
                predicates.add(cbuild.like(root.get("orderCode"), new StringBuffer().append("%")
                        .append(XssUtils.replaceLikeWildcard(orderCode)).append("%").toString()));
            }

            //下单开始时间
            if (StringUtils.isNotBlank(request.getFirstOrderStartTime())) {
                predicates.add(cbuild.greaterThanOrEqualTo(root.get("firstOrderTime"), LocalDateTime.of(LocalDate.parse(request.getFirstOrderStartTime(),
                        formatter), LocalTime.MIN)));
            }

            //下单结束时间
            if (StringUtils.isNotBlank(request.getFirstOrderEndTime())) {
                predicates.add(cbuild.lessThanOrEqualTo(root.get("firstOrderTime"), LocalDateTime.of(LocalDate.parse(request.getFirstOrderEndTime(),
                        formatter), LocalTime.MAX)));
            }

            //订单完成开始时间
            if (StringUtils.isNotBlank(request.getOrderFinishStartTime())) {
                predicates.add(cbuild.greaterThanOrEqualTo(root.get("orderFinishTime"), LocalDateTime.of(LocalDate.parse(request.getOrderFinishStartTime(),
                        formatter), LocalTime.MIN)));
            }

            //订单完成结束时间
            if (StringUtils.isNotBlank(request.getOrderFinishEndTime())) {
                predicates.add(cbuild.lessThanOrEqualTo(root.get("orderFinishTime"), LocalDateTime.of(LocalDate.parse(request.getOrderFinishEndTime(),
                        formatter), LocalTime.MAX)));
            }

            //入账开始时间
            if (StringUtils.isNotBlank(request.getRewardRecordedStartTime())) {
                predicates.add(cbuild.greaterThanOrEqualTo(root.get("rewardRecordedTime"), LocalDateTime.of(LocalDate.parse(request.getRewardRecordedStartTime(),
                        formatter), LocalTime.MIN)));
            }

            //入账结束时间
            if (StringUtils.isNotBlank(request.getRewardRecordedEndTime())) {
                predicates.add(cbuild.lessThanOrEqualTo(root.get("rewardRecordedTime"), LocalDateTime.of(LocalDate.parse(request.getRewardRecordedEndTime(),
                        formatter), LocalTime.MAX)));
            }
            return cbuild.and(predicates.toArray(new Predicate[]{}));
        };
    }

    /**
     * 邀新记录列表查询条件封装
     * @param request
     * @return
     */
    public static Specification<InviteNewRecord> getWhereCriteria(DistributionInviteNewListRequest request) {
        DistributionInviteNewPageRequest pageRequest = KsBeanUtil.convert(request,
                DistributionInviteNewPageRequest.class);
        return getWhereCriteria(pageRequest);
    }

    public static Specification<DistributionCustomer> getWhereCriteria(DistributionCommissionPageRequest request) {
        String distributionId=request.getDistributionId();
        return (root, cquery, cbuild) -> {
            List<Predicate> predicates = new ArrayList<>();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            // 批量查询-分销员IDList
            if (CollectionUtils.isNotEmpty(request.getDistributionIdList())) {
                predicates.add(root.get("distributionId").in(request.getDistributionIdList()));
            }

            // 分销员等级ID
            if (StringUtils.isNotEmpty(request.getDistributorLevelId())) {
                predicates.add(cbuild.equal(root.get("distributorLevelId"), request.getDistributorLevelId()));
            }

            //只统计已审核的分销员
            predicates.add(cbuild.equal(root.get("distributorFlag"), DefaultFlag.YES));

            //分销员ID
            if (StringUtils.isNotBlank(distributionId)){
                predicates.add(cbuild.equal(root.get("distributionId"), distributionId));
            }

            //创建开始时间
            if (StringUtils.isNotBlank(request.getCreateStartTime())) {
                predicates.add(cbuild.greaterThanOrEqualTo(root.get("createTime"), LocalDateTime.of(LocalDate.parse(request.getCreateStartTime(),
                        formatter), LocalTime.MIN)));
            }

            //创建结束时间
            if (StringUtils.isNotBlank(request.getCreateEndTime())) {
                predicates.add(cbuild.lessThanOrEqualTo(root.get("createTime"), LocalDateTime.of(LocalDate.parse(request.getCreateEndTime(),
                        formatter), LocalTime.MAX)));
            }

            return cbuild.and(predicates.toArray(new Predicate[]{}));
        };
    }

    public static Specification<Customer> getWhereCriteria(CustomerWithOpenIdPageRequest request) {

        return (root, cquery, cbuild) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cbuild.equal(root.get("checkState"), CheckState.CHECKED));
            predicates.add(cbuild.equal(root.get("delFlag"), DeleteFlag.NO));
            predicates.add(cbuild.notEqual(root.get("wxMiniOpenId"),""));
            if(request.getStartId()!=null){
                predicates.add(cbuild.greaterThan(root.get("id"),request.getStartId()));
            }
            Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);

            return p.length == 0 ? null : p.length == 1 ? p[0] : cbuild.and(p);
        };
    }
}
