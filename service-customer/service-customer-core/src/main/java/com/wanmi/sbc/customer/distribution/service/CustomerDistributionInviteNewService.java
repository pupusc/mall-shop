package com.wanmi.sbc.customer.distribution.service;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.wanmi.sbc.common.base.MessageMQRequest;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.NodeType;
import com.wanmi.sbc.common.enums.node.DistributionType;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.request.customer.*;
import com.wanmi.sbc.customer.api.request.distribution.DistributionCustomerModifyRewardRequest;
import com.wanmi.sbc.customer.api.request.distribution.DistributorCustomerInviteInfoUpdateRequest;
import com.wanmi.sbc.customer.api.request.mq.CouponGroupAddRequest;
import com.wanmi.sbc.customer.api.request.mq.CustomerFundsGrantAmountRequest;
import com.wanmi.sbc.customer.api.response.customer.DistributionInviteNewListResponse;
import com.wanmi.sbc.customer.api.response.customer.DistributionInviteNewPageResponse;
import com.wanmi.sbc.customer.api.response.customer.DistributionInviteNewUpdateResponse;
import com.wanmi.sbc.customer.api.response.distribution.DistributionCountByRequestCustomerIdResponse;
import com.wanmi.sbc.customer.api.response.distribution.DistributionCountCouponByRequestCustomerIdResponse;
import com.wanmi.sbc.customer.api.response.distribution.DistributionCountInvitedCustResponse;
import com.wanmi.sbc.customer.bean.dto.DistributionRewardCouponDTO;
import com.wanmi.sbc.customer.bean.enums.*;
import com.wanmi.sbc.customer.bean.vo.DistributionInviteNewForPageVO;
import com.wanmi.sbc.customer.bean.vo.DistributionInviteNewListVO;
import com.wanmi.sbc.customer.bean.vo.DistributionInviteNewVo;
import com.wanmi.sbc.customer.detail.model.root.CustomerDetail;
import com.wanmi.sbc.customer.detail.service.CustomerDetailService;
import com.wanmi.sbc.customer.distribution.model.entity.DistributionCustomerInviteInfoModify;
import com.wanmi.sbc.customer.distribution.model.root.DistributionCustomer;
import com.wanmi.sbc.customer.distribution.model.root.DistributionCustomerInviteInfo;
import com.wanmi.sbc.customer.distribution.model.root.InviteNewRecord;
import com.wanmi.sbc.customer.distribution.repository.CustomerDistributionInviteNewRepository;
import com.wanmi.sbc.customer.model.root.Customer;
import com.wanmi.sbc.customer.model.root.CustomerBase;
import com.wanmi.sbc.customer.mq.ProducerService;
import com.wanmi.sbc.customer.quicklogin.model.root.ThirdLoginRelation;
import com.wanmi.sbc.customer.quicklogin.service.ThirdLoginRelationService;
import com.wanmi.sbc.customer.redis.DistributionCommissionRedisService;
import com.wanmi.sbc.customer.service.CustomerService;
import com.wanmi.sbc.customer.util.QueryConditionsUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by feitingting on 2019/2/21.
 */
@Slf4j
@Service
public class CustomerDistributionInviteNewService {

    @Autowired
    private CustomerDistributionInviteNewRepository customerDistributionInviteNewRepository;

    @Autowired
    private CustomerDetailService customerDetailService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private DistributionCustomerService distributionCustomerService;
    @Autowired
    private ThirdLoginRelationService thirdLoginRelationService;

    @Autowired
    private ProducerService producerService;

    @Autowired
    private DistributionCustomerInviteInfoService distributionCustomerInviteInfoService;

    @Autowired
    private DistributionCommissionRedisService distributionCommissionRedisService;

    public DistributionInviteNewPageResponse page(DistributionInviteNewPageRequest request) {
        Page<InviteNewRecord> inviteNewRecords = customerDistributionInviteNewRepository.findAll(QueryConditionsUtil
                .getWhereCriteria(request), request.getPageRequest());
        return pageHelper(inviteNewRecords, request.getPageNum());
    }

    private DistributionInviteNewPageResponse pageHelper(Page<InviteNewRecord> inviteNewRecords, int pageNum) {
        DistributionInviteNewPageResponse distributionInviteNewPageResponse = new DistributionInviteNewPageResponse();
        distributionInviteNewPageResponse.setCurrentPage(pageNum);

        //结果为空
        if (CollectionUtils.isEmpty(inviteNewRecords.getContent())) {
            distributionInviteNewPageResponse.setRecordList(Collections.emptyList());
            distributionInviteNewPageResponse.setTotal(0L);
            return distributionInviteNewPageResponse;
        }

        //先把属性值复制到CustomerDetailResponse对象里
        List<DistributionInviteNewForPageVO> distributionInviteNewForPageVOS = inviteNewRecords.getContent().stream()
                .map
                        (inviteNewRecord -> {
                            DistributionInviteNewForPageVO distributionInviteNewForPageVO = new
                                    DistributionInviteNewForPageVO();
                            //对象拷贝
                            BeanUtils.copyProperties(inviteNewRecord, distributionInviteNewForPageVO);

                            //填充受邀人的名称和账号
                            if (StringUtils.isNotBlank(inviteNewRecord.getInvitedNewCustomerId())) {
                                Customer customer = customerService.findById(inviteNewRecord.getInvitedNewCustomerId());
                                if (customer != null) {
                                    CustomerDetail customerDetail = customerDetailService.findByCustomerId(customer
                                            .getCustomerId());
                                    distributionInviteNewForPageVO.setInvitedNewCustomerName(customerDetail.getCustomerName());
                                    distributionInviteNewForPageVO.setInvitedNewCustomerAccount(customer.getCustomerAccount());
                                }
                                //填充受邀人的头像
                                ThirdLoginRelation thirdLoginRelation = thirdLoginRelationService
                                        .findFirstByCustomerIdAndThirdType(inviteNewRecord.getInvitedNewCustomerId(), ThirdLoginType.WECHAT);
                                if (Objects.nonNull(thirdLoginRelation)) {
                                    distributionInviteNewForPageVO.setInvitedNewCustomerHeadImg(thirdLoginRelation.getHeadimgurl());
                                }
                            }

                            //填充分销员的名称和账号
                            if (StringUtils.isNotBlank(inviteNewRecord.getRequestCustomerId())) {
                                Customer customer = customerService.findById(inviteNewRecord.getRequestCustomerId());
                                if (customer != null) {
                                    CustomerDetail customerDetail = customerDetailService.findByCustomerId(customer
                                            .getCustomerId());
                                    distributionInviteNewForPageVO.setRequestCustomerName(customerDetail.getCustomerName());
                                    distributionInviteNewForPageVO.setRequestCustomerAccount(customer.getCustomerAccount());
                                }
                                //填充分销员的头像
                                ThirdLoginRelation thirdLoginRelation = thirdLoginRelationService
                                        .findFirstByCustomerIdAndThirdType(inviteNewRecord.getRequestCustomerId(), ThirdLoginType.WECHAT);
                                if (Objects.nonNull(thirdLoginRelation)) {
                                    distributionInviteNewForPageVO.setRequestCustomerHeadImg(thirdLoginRelation.getHeadimgurl());
                                }
                            }
                            return distributionInviteNewForPageVO;
                        }).collect(Collectors.toList());


        distributionInviteNewPageResponse.setTotal(inviteNewRecords.getTotalElements());
        distributionInviteNewPageResponse.setRecordList(distributionInviteNewForPageVOS);
        return distributionInviteNewPageResponse;
    }

    /**
     * 根据条件查询所有邀新记录
     * @param request
     * @return
     */
    public DistributionInviteNewListResponse list(DistributionInviteNewListRequest request) {
        List<InviteNewRecord> inviteNewRecords =
                customerDistributionInviteNewRepository.findAll(QueryConditionsUtil.getWhereCriteria(request));
        return this.wrapperListVo(inviteNewRecords);
    }

    /**
     * 邀新记录列表转VO
     * @param inviteNewRecords
     * @return
     */
    private DistributionInviteNewListResponse wrapperListVo(List<InviteNewRecord> inviteNewRecords) {
        DistributionInviteNewListResponse inviteNewListResponse = new DistributionInviteNewListResponse();

        List<DistributionInviteNewListVO> inviteNewListVOs = inviteNewRecords.stream().map
                (inviteNewRecord -> {
                    DistributionInviteNewListVO distributionInviteNewListVO = new DistributionInviteNewListVO();
                    //对象拷贝
                    BeanUtils.copyProperties(inviteNewRecord, distributionInviteNewListVO);
                    return distributionInviteNewListVO;
                }).collect(Collectors.toList());

        if (CollectionUtils.isNotEmpty(inviteNewListVOs)) {
            // 封装查询结果中所有邀请人和受邀人的客户id
            Set<String> customerIdSet = new HashSet<>();
            for (DistributionInviteNewListVO vo : inviteNewListVOs) {
                if (StringUtils.isNotBlank(vo.getRequestCustomerId())) {
                    customerIdSet.add(vo.getRequestCustomerId());
                }

                if (StringUtils.isNotBlank(vo.getInvitedNewCustomerId())) {
                    customerIdSet.add(vo.getInvitedNewCustomerId());
                }
            }

            // 批量查询客户详情信息
            List<CustomerDetail> customerDetailList =
                    customerDetailService.findAnyByCustomerIds(new ArrayList<>(customerIdSet));
            // 批量查询客户信息
            List<Customer> customerList = customerService.findByCustomerIdIn(customerIdSet);

            // 填充分销员、受邀人的名称和账号
            for (DistributionInviteNewListVO vo : inviteNewListVOs) {
                if (CollectionUtils.isNotEmpty(customerDetailList)) {
                    for (CustomerDetail customerDetail : customerDetailList) {
                        if (customerDetail.getCustomerId().equals(vo.getRequestCustomerId())) {
                            // 填充分销员的名称
                            vo.setRequestCustomerName(customerDetail.getCustomerName());
                        }
                        if (customerDetail.getCustomerId().equals(vo.getInvitedNewCustomerId())) {
                            // 填充受邀人的名称
                            vo.setInvitedNewCustomerName(customerDetail.getCustomerName());
                        }
                    }
                }

                if (CollectionUtils.isNotEmpty(customerList)) {
                    for (Customer customer : customerList) {
                        if (customer.getCustomerId().equals(vo.getRequestCustomerId())) {
                            // 填充分销员的账号
                            vo.setRequestCustomerAccount(customer.getCustomerAccount());
                        }
                        if (customer.getCustomerId().equals(vo.getInvitedNewCustomerId())) {
                            // 填充受邀人的账号
                            vo.setInvitedNewCustomerAccount(customer.getCustomerAccount());
                        }
                    }
                }
            }
        }

        inviteNewListResponse.setInviteNewListVOList(inviteNewListVOs);
        return inviteNewListResponse;
    }

    /**
     * 新增邀新记录
     * @param inviteNewRecord
     * @return
     */
    @Transactional
    public DistributionInviteNewVo addInviteNewRecord(InviteNewRecord inviteNewRecord) {
        InviteNewRecord newRecord = customerDistributionInviteNewRepository.save(inviteNewRecord);
        DistributionInviteNewVo inviteNewVo = this.wrapperVo(newRecord);
        return inviteNewVo;
    }


    /**
     * 统计客户邀新数量(奖励奖金)
     * @param customerId
     * @return
     */
    public DistributionCountInvitedCustResponse countCashByRequestCustomerId(String customerId) {
        // 邀新总人数
        Long totalCount = customerDistributionInviteNewRepository.countByRequestCustomerIdAndRewardFlag(customerId, RewardFlag.CASH);
        // 有效邀新人数
        Long validCount = customerDistributionInviteNewRepository
                .countRecordsByRequestCustomerIdAndAvailableDistribution(
                        customerId, RewardFlag.CASH, InvalidFlag.YES);
        // 奖励已入账邀新人数
        Long recordedCount = customerDistributionInviteNewRepository.countRecordsByRequestCustomerIdAndRewardRecorded(
                customerId, RewardFlag.CASH, InvalidFlag.YES);

        return DistributionCountInvitedCustResponse.builder()
                .totalCount(Objects.nonNull(totalCount) ? totalCount : NumberUtils.LONG_ZERO)
                .validCount(Objects.nonNull(validCount) ? validCount : NumberUtils.LONG_ZERO)
                .recordedCount(Objects.nonNull(recordedCount) ? recordedCount : NumberUtils.LONG_ZERO)
                .build();
    }

    /**
     * 根据邀请人会员ID统计邀新总人数、有效邀新人数、奖励已入账邀新人数（优惠券）
     * @param customerId
     * @return
     */
    public DistributionCountCouponByRequestCustomerIdResponse countCouponByRequestCustomerId(String customerId) {
        // 邀新总人数（优惠券）
        Long totalCount = customerDistributionInviteNewRepository.countByRequestCustomerIdAndRewardFlag(customerId, RewardFlag.COUPON);
        // 有效邀新人数（优惠券）
        Long validCount = customerDistributionInviteNewRepository
                .countRecordsByRequestCustomerIdAndAvailableDistribution(
                        customerId, RewardFlag.COUPON, InvalidFlag.YES);
        // 奖励已入账邀新人数（优惠券）
        Long recordedCount = customerDistributionInviteNewRepository.countRecordsByRequestCustomerIdAndRewardRecorded(
                customerId, RewardFlag.COUPON, InvalidFlag.YES);

        return DistributionCountCouponByRequestCustomerIdResponse.builder()
                .totalCount(Objects.nonNull(totalCount) ? totalCount : NumberUtils.LONG_ZERO)
                .validCount(Objects.nonNull(validCount) ? validCount : NumberUtils.LONG_ZERO)
                .recordedCount(Objects.nonNull(recordedCount) ? recordedCount : NumberUtils.LONG_ZERO)
                .build();
    }

    /**
     * 根据邀请人会员ID统计邀新总人数、有效邀新人数、奖励已入账邀新人数（优惠券 & 奖金）
     * @param customerId
     * @return
     */
    public DistributionCountByRequestCustomerIdResponse countByRequestCustomerId(String customerId) {

        //邀新总人数（奖金）、有效邀新人数（奖金）、奖励已入账邀新人数（奖金）
        DistributionCountInvitedCustResponse cash = countCashByRequestCustomerId(customerId);

        // 邀新总人数（优惠券）、有效邀新人数（优惠券）、奖励已入账邀新人数（优惠券）
        DistributionCountCouponByRequestCustomerIdResponse coupon = countCouponByRequestCustomerId(customerId);

        return DistributionCountByRequestCustomerIdResponse.builder()
                .totalCountOfCash(cash.getTotalCount())
                .validCountOfCash(cash.getValidCount())
                .recordedCountOfCash(cash.getRecordedCount())
                .totalCountOfCoupon(coupon.getTotalCount())
                .validCountOfCoupon(coupon.getValidCount())
                .recordedCountOfCoupon(coupon.getRecordedCount())
                .build();
    }

    @Transactional(rollbackFor = Exception.class)
    public DistributionInviteNewVo update(DistributionInviteNewUpdateRequest inviteNewRecord) {
        InviteNewRecord newRecord = this.queryById(inviteNewRecord.getInvitedNewCustomerId());
        DistributionInviteNewVo inviteNewVo = new DistributionInviteNewVo();
        if (null == newRecord || !Objects.isNull(newRecord.getOrderCode())) {
            return inviteNewVo;
        }
        //是否发放邀新奖励
        InvalidFlag rewardRecorded = InvalidFlag.NO;
        // 没有入账且未超过奖励上限的 的更新奖励入账时间、奖励金额
        if (InvalidFlag.NO == newRecord.getRewardRecorded()) {
            if (inviteNewRecord.getOverLimit() == DefaultFlag.NO) {
                newRecord.setRewardCash(newRecord.getSettingAmount());
                newRecord.setRewardRecorded(InvalidFlag.YES);
                newRecord.setRewardRecordedTime(inviteNewRecord.getRewardRecordedTime());
                rewardRecorded = InvalidFlag.YES;
            }
        }
        //如果是已经入账了的数据，则更新有效邀新、下单时间、订单编号、订单完成时间
        newRecord.setFirstOrderTime(inviteNewRecord.getOrderFinishTime());
        newRecord.setOrderCode(inviteNewRecord.getOrderCode());
        newRecord.setOrderFinishTime(inviteNewRecord.getOrderFinishTime());
        newRecord.setAvailableDistribution(InvalidFlag.YES);
        newRecord = customerDistributionInviteNewRepository.saveAndFlush(newRecord);
        KsBeanUtil.copyPropertiesThird(newRecord, inviteNewVo);
        inviteNewVo.setRewardRecorded(rewardRecorded);
        return inviteNewVo;
    }

    public List<InviteNewRecord> batchUpdate(List<InviteNewRecord> list) {
        return customerDistributionInviteNewRepository.saveAll(list);
    }

    /**
     * 补发更新分销邀新记录
     * @param inviteNewRecord
     * @return
     */
    @Transactional
    public DistributionInviteNewVo updateBySupplyAgain(DistributionInviteNewSupplyAgainRequest inviteNewRecord) {
        InviteNewRecord newRecord = this.queryById(inviteNewRecord.getRecordId());
        DistributionInviteNewVo inviteNewVo = new DistributionInviteNewVo();
        if (null == newRecord) {
            return inviteNewVo;
        }
        newRecord.setRewardRecorded(InvalidFlag.YES);
        newRecord.setRewardRecordedTime(LocalDateTime.now());
        newRecord.setRewardCash(inviteNewRecord.getRewardCash());
        newRecord.setRewardCoupon(inviteNewRecord.getRewardCoupon());
        //奖励未入账原因置空
        newRecord.setFailReasonFlag(null);
        newRecord = customerDistributionInviteNewRepository.saveAndFlush(newRecord);
        KsBeanUtil.copyPropertiesThird(newRecord, inviteNewVo);
        return inviteNewVo;
    }

    public InviteNewRecord queryById(String id) {
        return customerDistributionInviteNewRepository.findById(id).orElse(null);

    }

    /**
     * 查询有效的邀请记录
     * @param id
     * @return
     */
    public InviteNewRecord queryValidRecord(String id) {
        List<InviteNewRecord> list = customerDistributionInviteNewRepository.queryByInvitedNewCustomerId(id);
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        return list.get(0);
    }

    /**
     * 邀新记录实体转成VO
     * @param inviteNewRecord
     * @return
     */
    public DistributionInviteNewVo wrapperVo(InviteNewRecord inviteNewRecord) {
        if (inviteNewRecord != null) {
            DistributionInviteNewVo distributionInviteNewVo = new DistributionInviteNewVo();
            KsBeanUtil.copyPropertiesThird(inviteNewRecord, distributionInviteNewVo);
            return distributionInviteNewVo;
        }
        return null;
    }


    @Transactional
    public InviteNewRecord modify(InviteNewRecord inviteNewRecord) {
        customerDistributionInviteNewRepository.save(inviteNewRecord);
        return inviteNewRecord;
    }

    /**
     * 根据邀请人-会员ID查询邀新记录
     * @param requestCustomerId
     * @return
     */
    public List<InviteNewRecord> findByRequestCustomerId(String requestCustomerId) {
        return customerDistributionInviteNewRepository.findByRequestCustomerId(requestCustomerId);
    }

    /**
     * 根据受邀人-会员ID查询邀新记录
     * @param invitedCustomerId
     * @return
     */
    public List<InviteNewRecord> findByInvitedCustomerId(String invitedCustomerId) {
        return customerDistributionInviteNewRepository.findByInvitedNewCustomerId(invitedCustomerId);
    }


    /**
     * 新增邀新记录,发放奖励奖金、优惠券（邀新注册触发）
     * @param addRegisterRequest
     */
    @Transactional(rollbackFor = Exception.class)
    public void addDistributionInviteNewRecord(DistributionInviteNewAddRegisterRequest addRegisterRequest) {
        String customerId = addRegisterRequest.getCustomerId();
        String requestCustomerId = addRegisterRequest.getRequestCustomerId();
        CustomerBase customer = customerService.getBaseCustomerByCustomerIdAndDeleteFlag(requestCustomerId, DeleteFlag.NO);
        if (Objects.isNull(customer) || StringUtils.isBlank(requestCustomerId) || StringUtils.isBlank(customerId)) {
            log.error("=================== 新增邀新记录失败，查询不到邀请人客户信息！受邀人会员ID:{},邀请人会员ID:{}============== ", customerId, requestCustomerId);
            return;
        }
        // 判断邀请人是否存在分销员表中
        DistributionCustomer distributionCustomer = distributionCustomerService.getByCustomerIdAndDelFlag(requestCustomerId);

        LocalDateTime now = LocalDateTime.now();

        Integer distributor = Objects.isNull(distributionCustomer) ? DefaultFlag.NO.toValue() : distributionCustomer.getDistributorFlag().toValue();

        List<InviteNewRecord> inviteNewRecordList = new ArrayList<>(2);
        //组装发放奖金数据
        InviteNewRecord rewardCashResult = addRewardCash(addRegisterRequest, distributor, now);
        if (Objects.nonNull(rewardCashResult)) {
            inviteNewRecordList.add(rewardCashResult);
        }
        //组装发放优惠券数据
        InviteNewRecord rewardCouponResult = addRewardCoupon(addRegisterRequest, distributor, now);
        if (Objects.nonNull(rewardCouponResult)) {
            inviteNewRecordList.add(rewardCouponResult);
        }
        if (CollectionUtils.isEmpty(inviteNewRecordList)) {
            log.error("=================== 新增邀新记录失败，奖金数据和优惠券数据不存在！受邀人会员ID:{},邀请人会员ID:{}============== ", customerId, requestCustomerId);
            return;
        }

        List<InviteNewRecord> saveResult = customerDistributionInviteNewRepository.saveAll(inviteNewRecordList);

        if (CollectionUtils.isNotEmpty(saveResult)) {
            log.info("=================== 新增邀新记录成功，受邀人会员ID:{},邀请人会员ID:{}============== ", customerId, requestCustomerId);

            // 后台配置的奖励金额
            BigDecimal settingAmount = addRegisterRequest.getSettingAmount();
            Integer invitedCount = NumberUtils.INTEGER_ZERO;
            if (Objects.nonNull(distributionCustomer) && (Objects.nonNull(rewardCashResult) || Objects.nonNull(rewardCouponResult))) {
                BigDecimal rewardCash = Objects.nonNull(rewardCashResult) ? rewardCashResult.getRewardCash() : BigDecimal.ZERO;
                settingAmount = Objects.nonNull(rewardCashResult) ? settingAmount : BigDecimal.ZERO;
                distributionCustomer.setInviteCount(Integer.sum(distributionCustomer.getInviteCount(),NumberUtils.INTEGER_ONE));
                distributionCustomer.setRewardCash(distributionCustomer.getRewardCash().add(rewardCash));
                distributionCustomer.setRewardCashNotRecorded(settingAmount.subtract(rewardCash).add(distributionCustomer.getRewardCashNotRecorded()));
                distributionCustomer.setCommissionTotal(distributionCustomer.getCommissionTotal().add(rewardCash));
                distributionCustomer = distributionCustomerService.add(distributionCustomer);
                // 更新分销员邀新奖励信息
                this.modifyDistributionCustomerReward(distributionCustomer.getDistributionId(),
                        Objects.nonNull(rewardCashResult) ? settingAmount : BigDecimal.ZERO, Objects.nonNull(rewardCashResult) ? rewardCashResult.getRewardCash() : BigDecimal.ZERO);
                invitedCount = distributionCustomer.getInviteCount();
            }

            if (Objects.isNull(distributionCustomer)) {
                // 新增分销员信息
                distributionCustomer = distributionCustomerService.addDistributionCustomer(customer, now, Objects.nonNull(rewardCashResult) ? settingAmount : BigDecimal.ZERO, Objects.nonNull(rewardCashResult) ? rewardCashResult.getRewardCash() : BigDecimal.ZERO);
                invitedCount = NumberUtils.INTEGER_ONE;
            }

            Integer inviteCount = addRegisterRequest.getInviteCount();

            Boolean becomeDistributor = DefaultFlag.YES.equals(addRegisterRequest.getEnableFlag()) && DefaultFlag.NO.equals(addRegisterRequest.getLimitType()) && inviteCount <= invitedCount;
            Boolean distributorFlag =  Objects.nonNull(distributionCustomer) && distributionCustomer.getDistributorFlag() == DefaultFlag.YES;
            // 晋升分销员等级 || 尝试升级邀请人为分销员：判断是否可以升级，申请条件：邀请注册  邀新奖励限制：不限
            if (distributorFlag || becomeDistributor) {
                distributionCustomerService.upgrade(distributionCustomer,addRegisterRequest.getDistributorLevelVOList(),addRegisterRequest.getBaseLimitType());
            }

            // 发放奖励奖金-邀新奖励奖金对象不为空 & 奖励金额大于0 ,立即入账
            modifyInviteGrantAmountWithCustomerFunds(customer, rewardCashResult, distributionCustomer, now);

            //发放优惠券
            if (Objects.nonNull(rewardCouponResult) && InvalidFlag.YES == rewardCouponResult.getRewardRecorded()) {
                producerService.addCouponGroupFromInviteNew(new CouponGroupAddRequest(addRegisterRequest.getRequestCustomerId(), addRegisterRequest.getDistributionRewardCouponDTOList()));
            }

            //记录注册邀新-任务日志
            DistributionCustomerInviteInfoModify inviteInfoModify = DistributionCustomerInviteInfoModify.builder().distributionCustomer(distributionCustomer).rewardCashResult(rewardCashResult).rewardCouponResult(rewardCouponResult).build();
            modifyDistributionCustomerInviteInfo(inviteInfoModify);

            //好友注册成功通知触发
            this.dealMessage(addRegisterRequest, rewardCashResult, rewardCouponResult, customer.getCustomerName());

        }
        return;
    }

    /**
     * 更新邀新记录，发放奖励奖金、优惠券（定时任务触发）
     * @param modifyRegisterRequest
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public DistributionInviteNewUpdateResponse modifyDistributionInviteNewRecord(DistributionInviteNewModifyRegisterRequest modifyRegisterRequest) {
        DistributionInviteNewUpdateResponse response = DistributionInviteNewUpdateResponse.builder().inviteAmount(BigDecimal.ZERO).inviteNum(NumberUtils.INTEGER_ZERO).build();
        log.info("====================更新邀新记录开始，受邀人会员ID：{}!================", modifyRegisterRequest.getOrderBuyCustomerId());
        List<InviteNewRecord> inviteNewRecordList = findByInvitedCustomerId(modifyRegisterRequest.getOrderBuyCustomerId());
        if (CollectionUtils.isEmpty(inviteNewRecordList)) {
            log.error("====================更新邀新记录失败，根据受邀人会员ID：{}，未查询到邀新记录!================", modifyRegisterRequest.getOrderBuyCustomerId());
            return response;
        }

        String requestCustomerId = inviteNewRecordList.get(0).getRequestCustomerId();
        if (StringUtils.isBlank(requestCustomerId)) {
            log.error("====================更新邀新记录失败，邀新记录表中邀请人会员ID：{}，不存在!================", requestCustomerId);
            return response;
        }
        // 根据邀请人id查询客户信息
        CustomerBase customer = customerService.getBaseCustomerByCustomerIdAndDeleteFlag(requestCustomerId, DeleteFlag.NO);
        if (Objects.isNull(customer)) {
            log.error("=================== 更新邀新记录失败，查询不到邀请人客户信息！受邀人会员ID:{},邀请人会员ID:{}============== ", modifyRegisterRequest.getOrderBuyCustomerId(), requestCustomerId);
            return response;
        }
        //奖金数据
        InviteNewRecord cash = inviteNewRecordList.stream().filter(d -> RewardFlag.CASH == d.getRewardFlag()).findFirst().orElse(null);
        //优惠券数据
        InviteNewRecord coupon = inviteNewRecordList.stream().filter(d -> RewardFlag.COUPON == d.getRewardFlag()).findFirst().orElse(null);

        //根据邀请人会员ID统计邀新总人数、有效邀新人数、奖励已入账邀新人数（优惠券 & 奖金）
        DistributionCountByRequestCustomerIdResponse distributionCountByRequestCustomerIdResponse = countByRequestCustomerId(requestCustomerId);
        if (Objects.nonNull(distributionCountByRequestCustomerIdResponse)) {
            log.info("=================== 更新邀新记录,邀请人会员ID:{},邀新总人数、有效邀新人数、奖励已入账邀新人数（优惠券 & 奖金）：{}============== ", requestCustomerId, distributionCountByRequestCustomerIdResponse);
        }


        // 奖金-未入账原因(普通邀新新增数据时)
        FailReasonFlag failReasonFlag_cash_old = Objects.nonNull(cash) ? cash.getFailReasonFlag() : null;
        //奖金-奖励是否入账(普通邀新新增数据时)
        InvalidFlag rewardRecorded_cash_old = Objects.nonNull(cash) ? cash.getRewardRecorded() : null;
        // 奖金已入账邀新人数
        Long count_cash = distributionCountByRequestCustomerIdResponse.getRecordedCountOfCash();
        //奖金-未入账原因（当前）
        FailReasonFlag failReasonFlag_cash = getRewardFailReasonOfCash(modifyRegisterRequest, cash, count_cash);

        // 优惠券-未入账原因(普通邀新新增数据时)
        FailReasonFlag failReasonFlag_coupon_old = Objects.nonNull(coupon) ? coupon.getFailReasonFlag() : null;
        //优惠券-奖励是否入账(普通邀新新增数据时)
        InvalidFlag rewardRecorded_coupon_old = Objects.nonNull(coupon) ? coupon.getRewardRecorded() : null;
        //奖励已入账邀新人数（优惠券）
        Long count_coupon = distributionCountByRequestCustomerIdResponse.getRecordedCountOfCoupon();
        //优惠券-未入账原因（当前）
        FailReasonFlag failReasonFlag_coupon = getRewardFailReasonOfCoupon(modifyRegisterRequest, coupon, count_coupon);

        cash = modifyRewardCash(cash,  failReasonFlag_cash, modifyRegisterRequest);
        coupon = modifyRewardCoupon(coupon, failReasonFlag_coupon, modifyRegisterRequest);

        inviteNewRecordList = new ArrayList<>(2);
        if (Objects.nonNull(cash)) {
            inviteNewRecordList.add(cash);
        }
        if (Objects.nonNull(coupon)) {
            inviteNewRecordList.add(coupon);
        }
        if (CollectionUtils.isEmpty(inviteNewRecordList)) {
            log.error("=================== 更新邀新记录失败，奖金数据和优惠券数据不存在或订单编号已存在！受邀人会员ID:{},邀请人会员ID:{}============== ", modifyRegisterRequest.getOrderBuyCustomerId(), requestCustomerId);
            return response;
        }
        List<InviteNewRecord> resultList = customerDistributionInviteNewRepository.saveAll(inviteNewRecordList);

        if (CollectionUtils.isNotEmpty(resultList)) {
            log.info("=================== 更新邀新记录成功，受邀人会员ID:{},邀请人会员ID:{}============== ", modifyRegisterRequest.getOrderBuyCustomerId(), requestCustomerId);

            // 判断邀请人是否存在分销员表中
            DistributionCustomer distributionCustomer = distributionCustomerService.getByCustomerIdAndDelFlag(requestCustomerId);

            //发放奖励奖金
            if (Objects.nonNull(cash) && Objects.isNull(failReasonFlag_cash) && InvalidFlag.NO == rewardRecorded_cash_old && InvalidFlag.YES == cash.getRewardRecorded()) {
                modifyInviteGrantAmountWithCustomerFunds(customer, cash, distributionCustomer, modifyRegisterRequest.getDateTime());
                response.setInviteAmount(cash.getSettingAmount());
                response.setInviteNum(NumberUtils.INTEGER_ONE);
            }

            //发放优惠券
            if (Objects.nonNull(coupon) && Objects.isNull(failReasonFlag_coupon) && InvalidFlag.NO == rewardRecorded_coupon_old && InvalidFlag.YES == coupon.getRewardRecorded()) {
                String settingCouponIdsCounts = coupon.getSettingCouponIdsCounts();
                List<DistributionRewardCouponDTO> list = JSONObject.parseArray(settingCouponIdsCounts, DistributionRewardCouponDTO.class);
                producerService.addCouponGroupFromInviteNew(new CouponGroupAddRequest(requestCustomerId, list));
                response.setInviteNum(NumberUtils.INTEGER_ONE);
            }

            //记录注册邀新-任务日志
            DistributionCustomerInviteInfoModify inviteInfoModify = DistributionCustomerInviteInfoModify.builder().distributionCustomer(distributionCustomer).rewardCashResult(cash)
                    .rewardCouponResult(coupon).failReasonFlag_cash_old(failReasonFlag_cash_old).failReasonFlag_coupon_old(failReasonFlag_coupon_old)
                    .rewardRecorded_cash_old(rewardRecorded_cash_old).rewardRecorded_coupon_old(rewardRecorded_coupon_old).build();
            modifyDistributionCustomerInviteInfo(inviteInfoModify);

            //奖励到账通知触发
            //受邀人账户
            CustomerBase customerBase = customerService.getBaseCustomerByCustomerIdAndDeleteFlag(modifyRegisterRequest.getOrderBuyCustomerId(), DeleteFlag.NO);
            String middle = "****";
            String account = customerBase.getCustomerAccount();
            account = account.substring(0, 3) + middle + account.substring(account.length() - 4);

            List<String> params = Lists.newArrayList(distributionCustomer.getCustomerName(), account);
            if(response.getInviteAmount() != null){
                params.add(response.getInviteAmount().toString());
            }else{
                params.add("0");
            }
            params.add(modifyRegisterRequest.getDenominationSum().toString());
            this.sendMessage(NodeType.DISTRIBUTION, DistributionType.INVITE_CUSTOMER_REWARD_RECEIPT, params, requestCustomerId);


        }

        return response;
    }

    /**
     * 新增或更新邀新记录-任务表
     * @param inviteInfoModify
     * @return
     */
    public void modifyDistributionCustomerInviteInfo(DistributionCustomerInviteInfoModify inviteInfoModify) {
        DistributionCustomer distributionCustomer = inviteInfoModify.getDistributionCustomer();
        log.info("=========查询邀请人ID:{},邀新记录任务表开始=======================", distributionCustomer.getCustomerId());
        DistributionCustomerInviteInfo info = distributionCustomerInviteInfoService.findByCustomerId(distributionCustomer.getCustomerId());
        DistributionCustomerInviteInfo distributionCustomerInviteInfo;
        InviteNewRecord rewardCashResult = inviteInfoModify.getRewardCashResult();
        InviteNewRecord rewardCouponResult = inviteInfoModify.getRewardCouponResult();
        FailReasonFlag failReasonFlag_cash_old = inviteInfoModify.getFailReasonFlag_cash_old();
        FailReasonFlag failReasonFlag_coupon_old = inviteInfoModify.getFailReasonFlag_coupon_old();
        InvalidFlag rewardRecorded_cash_old = inviteInfoModify.getRewardRecorded_cash_old();
        InvalidFlag rewardRecorded_coupon_old = inviteInfoModify.getRewardRecorded_coupon_old();
        if (Objects.isNull(failReasonFlag_cash_old) && Objects.isNull(failReasonFlag_coupon_old) && Objects.isNull(rewardRecorded_cash_old) && Objects.isNull(rewardRecorded_coupon_old)) {
            distributionCustomerInviteInfo = getDistributionCustomerInviteInfoFromAdd(distributionCustomer, rewardCashResult, rewardCouponResult);
        } else {
            distributionCustomerInviteInfo = getDistributionCustomerInviteInfoFromModify(inviteInfoModify);
        }
        if (Objects.isNull(info)) {
            distributionCustomerInviteInfo = distributionCustomerInviteInfoService.add(distributionCustomerInviteInfo);
            log.info("=========新增或更新邀新记录-任务表,是否成功：{},=======================", StringUtils.isNotBlank(distributionCustomerInviteInfo.getId()) ? "成功" : "失败");
        } else {
            DistributorCustomerInviteInfoUpdateRequest request = KsBeanUtil.convert(distributionCustomerInviteInfo, DistributorCustomerInviteInfoUpdateRequest.class);
            request.setDistributeId(distributionCustomerInviteInfo.getDistributionId());
            int result = distributionCustomerInviteInfoService.updatCount(request);
            log.info("=========新增或更新邀新记录-任务表,是否成功：{},更新数据详情信息：{}=======================", result > 0 ? "成功" : "失败", request);
        }
    }

    /**
     * 修改邀新记录-奖励奖金
     * @param cash
     * @param failReasonFlag_cash
     * @param modifyRegisterRequest
     * @return
     */
    private InviteNewRecord modifyRewardCash(InviteNewRecord cash, FailReasonFlag failReasonFlag_cash, DistributionInviteNewModifyRegisterRequest modifyRegisterRequest) {
        if (Objects.isNull(cash) || Objects.nonNull(cash.getOrderCode())) {
            return null;
        }
        //订单完成时间
        LocalDateTime orderFinishTime = modifyRegisterRequest.getOrderFinishTime();
        //订单下单时间
        LocalDateTime orderCreateTime = modifyRegisterRequest.getOrderCreateTime();
        //订单编号
        String orderCode = modifyRegisterRequest.getOrderCode();
        //奖励入账时间
        LocalDateTime dateTime = modifyRegisterRequest.getDateTime();
        cash.setAvailableDistribution(InvalidFlag.YES);
        cash.setOrderCode(orderCode);
        cash.setOrderFinishTime(orderFinishTime);
        cash.setFirstOrderTime(orderCreateTime);
        // 没有入账且未超过奖励上限的,更新奖励入账时间、奖励金额
        if (InvalidFlag.NO == cash.getRewardRecorded() && RewardFlag.CASH == cash.getRewardFlag() && Objects.isNull(failReasonFlag_cash)) {
            cash.setRewardCash(cash.getSettingAmount());
            cash.setRewardRecorded(InvalidFlag.YES);
            cash.setRewardRecordedTime(dateTime);
            cash.setFailReasonFlag(null);
        }
        if (RewardFlag.CASH == cash.getRewardFlag() && Objects.nonNull(failReasonFlag_cash) && InvalidFlag.NO == cash.getRewardRecorded()) {
            cash.setFailReasonFlag(failReasonFlag_cash);
        }
        return cash;
    }

    /**
     * 修改邀新记录-优惠券
     * @param coupon
     * @param failReasonFlag_coupon
     * @param modifyRegisterRequest
     * @return
     */
    private InviteNewRecord modifyRewardCoupon(InviteNewRecord coupon, FailReasonFlag failReasonFlag_coupon, DistributionInviteNewModifyRegisterRequest modifyRegisterRequest) {
        if (Objects.isNull(coupon) || Objects.nonNull(coupon.getOrderCode())) {
            return null;
        }
        //订单完成时间
        LocalDateTime orderFinishTime = modifyRegisterRequest.getOrderFinishTime();
        //订单下单时间
        LocalDateTime orderCreateTime = modifyRegisterRequest.getOrderCreateTime();
        //订单编号
        String orderCode = modifyRegisterRequest.getOrderCode();
        //奖励入账时间
        LocalDateTime dateTime = modifyRegisterRequest.getDateTime();

        coupon.setAvailableDistribution(InvalidFlag.YES);
        coupon.setOrderCode(orderCode);
        coupon.setOrderFinishTime(orderFinishTime);
        coupon.setFirstOrderTime(orderCreateTime);

        if (InvalidFlag.NO == coupon.getRewardRecorded() && RewardFlag.COUPON == coupon.getRewardFlag() && Objects.isNull(failReasonFlag_coupon)) {
            coupon.setRewardCoupon(coupon.getSettingCoupons());
            coupon.setRewardRecorded(InvalidFlag.YES);
            coupon.setRewardRecordedTime(dateTime);
            coupon.setFailReasonFlag(null);
        }
        if (RewardFlag.COUPON == coupon.getRewardFlag() && Objects.nonNull(failReasonFlag_coupon) && InvalidFlag.NO == coupon.getRewardRecorded()) {
            coupon.setFailReasonFlag(failReasonFlag_coupon);
        }
        return coupon;
    }

    /**
     * 验证奖励奖金是否可发放，并返回未发放原因
     * @param modifyRegisterRequest
     * @param cash
     * @param count                 奖金已入账邀新人数
     * @return
     */
    private FailReasonFlag getRewardFailReasonOfCash(DistributionInviteNewModifyRegisterRequest modifyRegisterRequest, InviteNewRecord cash, Long count) {
        if (Objects.nonNull(cash)) {
            DefaultFlag rewardCashFlag = modifyRegisterRequest.getRewardCashFlag();
            if (DefaultFlag.NO == rewardCashFlag) {
                return FailReasonFlag.UNOPENED;
            }
            //奖励上限类型
            DefaultFlag rewardCashType = modifyRegisterRequest.getRewardCashType();
            if (DefaultFlag.YES == rewardCashType) {
                Long rewardCashCount = modifyRegisterRequest.getRewardCashCount();
                if (rewardCashCount.compareTo(count) <= 0) {
                    return FailReasonFlag.LIMITED;
                }
            }
        }
        return null;
    }

    /**
     * 验证优惠券是否可发放，并返回未发放原因
     * @param modifyRegisterRequest
     * @param coupon
     * @param count                 奖励已入账邀新人数（优惠券）
     * @return
     */
    private FailReasonFlag getRewardFailReasonOfCoupon(DistributionInviteNewModifyRegisterRequest modifyRegisterRequest, InviteNewRecord coupon, Long count) {
        if (Objects.nonNull(coupon)) {
            DefaultFlag rewardCouponFlag = modifyRegisterRequest.getRewardCouponFlag();
            if (DefaultFlag.NO == rewardCouponFlag) {
                return FailReasonFlag.UNOPENED;
            }
            //优惠券上限组数
            Long rewardCouponCount = modifyRegisterRequest.getRewardCouponCount().longValue();
            if (rewardCouponCount.compareTo(count) <= 0) {
                return FailReasonFlag.LIMITED;
            }
        }
        return null;
    }

    /**
     * 组装邀新记录-任务表数据(普通邀新)
     * @param distributionCustomer
     * @param rewardCashResult
     * @param rewardCouponResult
     * @return
     */
    public DistributionCustomerInviteInfo getDistributionCustomerInviteInfoFromAdd(DistributionCustomer distributionCustomer, InviteNewRecord rewardCashResult, InviteNewRecord rewardCouponResult) {
        DistributionCustomerInviteInfo info = new DistributionCustomerInviteInfo();
        info.setCustomerId(distributionCustomer.getCustomerId());
        info.setDistributionId(distributionCustomer.getDistributionId());
        info.setRewardCashCount(Objects.nonNull(rewardCashResult) && InvalidFlag.YES == rewardCashResult.getRewardRecorded() ? NumberUtils.INTEGER_ONE : NumberUtils.INTEGER_ZERO);
        info.setRewardCashLimitCount(Objects.nonNull(rewardCashResult) && FailReasonFlag.LIMITED == rewardCashResult.getFailReasonFlag() ? NumberUtils.INTEGER_ONE : NumberUtils.INTEGER_ZERO);
        info.setRewardCashAvailableLimitCount(NumberUtils.INTEGER_ZERO);
        info.setRewardCouponCount(Objects.nonNull(rewardCouponResult) && InvalidFlag.YES == rewardCouponResult.getRewardRecorded() ? NumberUtils.INTEGER_ONE : NumberUtils.INTEGER_ZERO);
        info.setRewardCouponLimitCount(Objects.nonNull(rewardCouponResult) && FailReasonFlag.LIMITED == rewardCouponResult.getFailReasonFlag() ? NumberUtils.INTEGER_ONE : NumberUtils.INTEGER_ZERO);
        info.setRewardCouponAvailableLimitCount(NumberUtils.INTEGER_ZERO);
        return info;
    }

    /**
     * 组装邀新记录-任务表数据(有效邀新)
     * @param request
     * @return
     */
    public DistributionCustomerInviteInfo getDistributionCustomerInviteInfoFromModify(DistributionCustomerInviteInfoModify request) {
        DistributionCustomer distributionCustomer = request.getDistributionCustomer();
        InviteNewRecord rewardCashResult = request.getRewardCashResult();
        InviteNewRecord rewardCouponResult = request.getRewardCouponResult();
        FailReasonFlag failReasonFlag_cash_old = request.getFailReasonFlag_cash_old();
        FailReasonFlag failReasonFlag_coupon_old = request.getFailReasonFlag_coupon_old();
        InvalidFlag rewardRecorded_cash_old = request.getRewardRecorded_cash_old();
        InvalidFlag rewardRecorded_coupon_old = request.getRewardRecorded_coupon_old();
        DistributionCustomerInviteInfo info = new DistributionCustomerInviteInfo();
        info.setCustomerId(distributionCustomer.getCustomerId());
        info.setDistributionId(distributionCustomer.getDistributionId());
        Boolean rewardCashFlag = Objects.nonNull(rewardCashResult) && InvalidFlag.YES == rewardCashResult.getRewardRecorded() && InvalidFlag.NO == rewardRecorded_cash_old;
        info.setRewardCashCount(rewardCashFlag ? NumberUtils.INTEGER_ONE : NumberUtils.INTEGER_ZERO);
        //奖金未入账 & 超过设置上限
        Boolean rewardCashNotFlag = Objects.nonNull(rewardCashResult) && InvalidFlag.NO == rewardCashResult.getRewardRecorded() && FailReasonFlag.LIMITED == rewardCashResult.getFailReasonFlag();
        if (rewardCashNotFlag) {
            info.setRewardCashLimitCount(NumberUtils.INTEGER_ZERO);
            info.setRewardCashAvailableLimitCount(NumberUtils.INTEGER_ONE);
        }
        //有效邀新（注册时） & 奖金未入账 & 超过设置上限
        if (Objects.nonNull(failReasonFlag_cash_old) && FailReasonFlag.INVALID == failReasonFlag_cash_old && rewardCashNotFlag) {
            info.setRewardCashLimitCount(NumberUtils.INTEGER_ONE);
            info.setRewardCashAvailableLimitCount(NumberUtils.INTEGER_ONE);
        }
        // 超过设置上限（注册时） & 奖金入账
        if (Objects.nonNull(failReasonFlag_cash_old) && FailReasonFlag.LIMITED == failReasonFlag_cash_old && rewardCashFlag) {
            info.setRewardCashLimitCount(NumberUtils.INTEGER_MINUS_ONE);
        }
        Boolean rewardCouponFlag = Objects.nonNull(rewardCouponResult) && InvalidFlag.YES == rewardCouponResult.getRewardRecorded() && InvalidFlag.NO == rewardRecorded_coupon_old;
        info.setRewardCouponCount(rewardCouponFlag ? NumberUtils.INTEGER_ONE : NumberUtils.INTEGER_ZERO);
        //优惠券未入账 & 超过设置上限
        Boolean rewardCouponNotFlag = Objects.nonNull(rewardCouponResult) && InvalidFlag.NO == rewardCouponResult.getRewardRecorded() && FailReasonFlag.LIMITED == rewardCouponResult.getFailReasonFlag();
        if (rewardCouponNotFlag) {
            info.setRewardCouponLimitCount(NumberUtils.INTEGER_ZERO);
            info.setRewardCouponAvailableLimitCount(NumberUtils.INTEGER_ONE);
        }
        //有效邀新（注册时） & 优惠券未入账 & 超过设置上限
        if (Objects.nonNull(failReasonFlag_coupon_old) && FailReasonFlag.INVALID == failReasonFlag_coupon_old && rewardCouponNotFlag) {
            info.setRewardCouponLimitCount(NumberUtils.INTEGER_ONE);
            info.setRewardCouponAvailableLimitCount(NumberUtils.INTEGER_ONE);
        }
        //超过设置上限（注册时）& 优惠券入账
        if (Objects.nonNull(failReasonFlag_coupon_old) && FailReasonFlag.LIMITED == failReasonFlag_coupon_old && rewardCouponFlag) {
            info.setRewardCouponLimitCount(NumberUtils.INTEGER_MINUS_ONE);
        }
        return info;
    }

    /**
     * 邀新注册-发放奖励奖金
     * @param customer
     * @param rewardCashResult
     * @param distributionCustomer
     * @param now
     */
    public void modifyInviteGrantAmountWithCustomerFunds(CustomerBase customer, InviteNewRecord rewardCashResult, DistributionCustomer distributionCustomer, LocalDateTime now) {
        if (Objects.nonNull(rewardCashResult) && InvalidFlag.YES == rewardCashResult.getRewardRecorded()) {
            // 邀新奖励入账
            CustomerFundsGrantAmountRequest request = new CustomerFundsGrantAmountRequest();
            request.setAmount(rewardCashResult.getRewardCash());
            request.setCustomerId(customer.getCustomerId());
            request.setDateTime(now);
            request.setType(FundsType.INVITE_NEW_AWARDS);
            request.setCustomerAccount(customer.getCustomerAccount());
            request.setCustomerName(customer.getCustomerAccount());
            request.setDistributor(DefaultFlag.NO.toValue());
            if (Objects.nonNull(distributionCustomer)) {
                request.setCustomerAccount(distributionCustomer.getCustomerAccount());
                request.setCustomerName(distributionCustomer.getCustomerName());
                request.setDistributor(distributionCustomer.getDistributorFlag().toValue());
            }
            producerService.modifyInviteGrantAmountWithCustomerFunds(request);
            log.info("======================邀新注册-发放奖励奖金，更新数据详情信息：{}==================", request);
        }
    }

    /**
     * 发放邀新奖励-奖金
     * @param addRegisterRequest
     * @param distributor
     * @param now
     * @return
     */
    public InviteNewRecord addRewardCash(DistributionInviteNewAddRegisterRequest addRegisterRequest, Integer distributor, LocalDateTime now) {
        InviteNewRecord inviteNewRecord = null;
        //1.判断发放奖励奖金开关是否打开
        DefaultFlag rewardCashFlag = addRegisterRequest.getRewardCashFlag();
        //2.发放奖励奖金-开关关闭
        if (DefaultFlag.NO == rewardCashFlag) {
            return inviteNewRecord;
        }

        FailReasonFlag failReasonFlag = getSendRewardFailReason(addRegisterRequest);
        //奖励上限
        DefaultFlag rewardCashType = addRegisterRequest.getRewardCashType();
        if (Objects.isNull(failReasonFlag) && DefaultFlag.YES == rewardCashType) {
            //奖励现金上限(人数)
            Long rewardCashCount = addRegisterRequest.getRewardCashCount().longValue();
            // 奖励已入账邀新人数
            Long count = customerDistributionInviteNewRepository.countRecordsByRequestCustomerIdAndRewardRecorded(
                    addRegisterRequest.getRequestCustomerId(), RewardFlag.CASH, InvalidFlag.YES);
            //奖励达到上限
            if (rewardCashCount.compareTo(count) <= 0) {
                failReasonFlag = FailReasonFlag.LIMITED;
            }
        }

        inviteNewRecord = new InviteNewRecord();
        inviteNewRecord.setInvitedNewCustomerId(addRegisterRequest.getCustomerId());
        inviteNewRecord.setRequestCustomerId(addRegisterRequest.getRequestCustomerId());
        inviteNewRecord.setRegisterTime(now);
        inviteNewRecord.setAvailableDistribution(InvalidFlag.NO);
        inviteNewRecord.setDistributor(distributor);
        inviteNewRecord.setSettingAmount(addRegisterRequest.getSettingAmount());
        inviteNewRecord.setRewardFlag(RewardFlag.CASH);
        inviteNewRecord.setTerminalSource(addRegisterRequest.getTerminalSource());
        //社销分销开关 & 仅限有效邀新 & 超过奖励现金人数上限
        if (Objects.nonNull(failReasonFlag)) {
            // 奖励是否入账
            inviteNewRecord.setRewardRecorded(InvalidFlag.NO);
            inviteNewRecord.setFailReasonFlag(failReasonFlag);
        } else {
            // 奖励是否入账
            inviteNewRecord.setRewardRecorded(InvalidFlag.YES);
            // 奖励入账时间
            inviteNewRecord.setRewardRecordedTime(now);
            // 邀新奖励金额
            inviteNewRecord.setRewardCash(addRegisterRequest.getSettingAmount());
            inviteNewRecord.setFailReasonFlag(null);
        }

        return inviteNewRecord;
    }

    /**
     * 发放邀新奖励-优惠券
     * @param addRegisterRequest
     * @param distributor
     * @param now
     * @return
     */
    public InviteNewRecord addRewardCoupon(DistributionInviteNewAddRegisterRequest addRegisterRequest, Integer distributor, LocalDateTime now) {
        InviteNewRecord inviteNewRecord = null;
        //1.判断发放优惠券开关是否打开
        DefaultFlag rewardCouponFlag = addRegisterRequest.getRewardCouponFlag();
        //2.发放优惠券-开关关闭
        if (DefaultFlag.NO == rewardCouponFlag) {
            return inviteNewRecord;
        }
        FailReasonFlag failReasonFlag = getSendRewardFailReason(addRegisterRequest);
        //2.开关打开发放奖励优惠券
        if (Objects.isNull(failReasonFlag)) {
            //奖励优惠券上限(组数)
            Long rewardCouponCount = addRegisterRequest.getRewardCouponCount().longValue();
            //查询已发放的优惠券人数
            Long count = customerDistributionInviteNewRepository.countRecordsByRequestCustomerIdAndRewardRecorded(
                    addRegisterRequest.getRequestCustomerId(), RewardFlag.COUPON, InvalidFlag.YES);
            if (rewardCouponCount.compareTo(count) <= 0) {
                //已入账人数小于奖励现金上限人数，继续发放奖励奖金
                failReasonFlag = FailReasonFlag.LIMITED;
            }
        }
        String separator = "，";
        inviteNewRecord = new InviteNewRecord();
        inviteNewRecord.setInvitedNewCustomerId(addRegisterRequest.getCustomerId());
        inviteNewRecord.setRequestCustomerId(addRegisterRequest.getRequestCustomerId());
        inviteNewRecord.setRegisterTime(now);
        inviteNewRecord.setAvailableDistribution(InvalidFlag.NO);
        inviteNewRecord.setDistributor(distributor);
        inviteNewRecord.setRewardFlag(RewardFlag.COUPON);
        inviteNewRecord.setSettingCoupons(Joiner.on(separator).join(addRegisterRequest.getCouponNameList()));
        inviteNewRecord.setTerminalSource(addRegisterRequest.getTerminalSource());
        inviteNewRecord.setSettingCouponIdsCounts(JSONObject.toJSONString(addRegisterRequest.getDistributionRewardCouponDTOList()));
        //社销分销开关 & 仅限有效邀新 & 超过优惠劵人数上限
        if (Objects.nonNull(failReasonFlag)) {
            // 奖励是否入账
            inviteNewRecord.setRewardRecorded(InvalidFlag.NO);
            inviteNewRecord.setFailReasonFlag(failReasonFlag);
        } else {
            // 奖励是否入账
            inviteNewRecord.setRewardRecorded(InvalidFlag.YES);
            // 奖励入账时间
            inviteNewRecord.setRewardRecordedTime(now);
            //邀新奖励优惠券
            inviteNewRecord.setRewardCoupon(Joiner.on(separator).join(addRegisterRequest.getCouponNameList()));
            inviteNewRecord.setFailReasonFlag(null);
        }

        return inviteNewRecord;
    }

    /**
     * 获取奖励未入账原因
     * @param addRegisterRequest
     * @return
     */
    public FailReasonFlag getSendRewardFailReason(DistributionInviteNewAddRegisterRequest addRegisterRequest) {

        //社销分销开关
        DefaultFlag openFlag = addRegisterRequest.getOpenFlag();
        //邀新奖励开关
        DefaultFlag inviteFlag = addRegisterRequest.getInviteFlag();
        //2：奖励未开启
        if (DefaultFlag.NO == openFlag || DefaultFlag.NO == inviteFlag) {
            return FailReasonFlag.UNOPENED;
        }

        // 查询是否开启邀新奖励限制
        DefaultFlag distributionLimitType = addRegisterRequest.getDistributionLimitType();
        //非有效邀新
        if (DefaultFlag.YES == distributionLimitType) {
            return FailReasonFlag.INVALID;
        }
        return null;
    }



    /**
     * 更新分销员奖励信息--分销佣金统计--redis同步
     * @param distributionId
     * @param settingAmount
     * @param rewardCash
     */
    private Map<String, Double> modifyDistributionCustomerReward(String distributionId, BigDecimal settingAmount,
                                                 BigDecimal rewardCash) {
        DistributionCustomerModifyRewardRequest rewardRequest = new DistributionCustomerModifyRewardRequest();
        // 分销员id
        rewardRequest.setDistributionId(distributionId);
        // 新增邀新人数
        rewardRequest.setInviteCount(1);
        // 实际入账的邀新奖励
        rewardRequest.setRewardCash(rewardCash);
        // 未入账邀新奖金 = 后台配置的奖励 - 实际入账的邀新奖励
        rewardRequest.setRewardCashNotRecorded(settingAmount.subtract(rewardCash));

        Map<String, Double> result =  distributionCommissionRedisService.modifyReward(rewardRequest);
        log.info("==============更新分销员邀新奖励信息：{},对应更新数据详情信息：{}===================", result, rewardRequest);
        return result;
    }


    /**
     * 根据条件分页查询邀新记录
     * @param request
     * @return
     */
    public DistributionInviteNewPageResponse findInviteNewRecordPage(DistributionInviteNewPageRequest request) {
        Page<InviteNewRecord> inviteNewRecords = null;
        if (request.getAvailableDistribution() != null) {
            inviteNewRecords = customerDistributionInviteNewRepository
                    .findInviteNewRecordPageByAvailableDistribution(request.getRequestCustomerId(), request
                            .getAvailableDistribution(), request
                            .getPageRequest());
        } else {
            inviteNewRecords = customerDistributionInviteNewRepository
                    .findInviteNewRecordPage(request.getRequestCustomerId(), request.getPageRequest());
        }

        return pageHelper(inviteNewRecords, request.getPageNum());
    }

    /**
     * 根据邀请人会员ID统计邀新总人数、有效邀新人数
     * @param request
     * @return
     */
    public DistributionCountInvitedCustResponse distinctCountInvitedCustomers(DistributionCountInvitedCustRequest request) {
        // 邀新总人数
        Long totalCount = customerDistributionInviteNewRepository.countByRequestCustomerId(request.getCustomerId());
        // 有效邀新人数
        Long validCount = customerDistributionInviteNewRepository
                .countRecordsByRequestCustomerIdAndAvailableDistribution(request.getCustomerId(), InvalidFlag.YES);

        return DistributionCountInvitedCustResponse.builder()
                .totalCount(totalCount)
                .validCount(validCount)
                .build();
    }

    private void dealMessage(DistributionInviteNewAddRegisterRequest addRegisterRequest, InviteNewRecord rewardCashResult, InviteNewRecord rewardCouponResult, String customerName){
        String customerId = addRegisterRequest.getCustomerId();
        String requestCustomerId = addRegisterRequest.getRequestCustomerId();
        //查询受邀人信息
        CustomerBase customerBase = customerService.getBaseCustomerByCustomerIdAndDeleteFlag(customerId, DeleteFlag.NO);
        //手机号掩码
        String middle = "****";
        String account = customerBase.getCustomerAccount();
        account = account.substring(0, 3) + middle + account.substring(account.length() - 4);

        List<String> params = Lists.newArrayList(customerName, account);
        if (Objects.nonNull(rewardCashResult)){
            params.add(rewardCashResult.getSettingAmount().toString());
        }else{
            params.add("0");
        }
        if(Objects.nonNull(rewardCouponResult)){
            params.add(addRegisterRequest.getDenominationSum().toString());
        }else{
            params.add("0");
        }

        if(DefaultFlag.NO.equals(addRegisterRequest.getBaseLimitType())){
            this.sendMessage(NodeType.DISTRIBUTION, DistributionType.FRIEND_REGISTER_SUCCESS_NO_REWARD, params, requestCustomerId);
        }else{
            this.sendMessage(NodeType.DISTRIBUTION, DistributionType.FRIEND_REGISTER_SUCCESS_HAS_REWARD, params, requestCustomerId);
        }
    }

    /**
     * 发送消息
     * @param nodeType
     * @param nodeCode
     * @param params
     * @param customerId
     */
    private void sendMessage(NodeType nodeType, DistributionType nodeCode, List<String> params, String customerId){
        Map<String, Object> map = new HashMap<>();
        map.put("type", nodeType.toValue());
        map.put("node", nodeCode.toValue());
        MessageMQRequest messageMQRequest = new MessageMQRequest();
        messageMQRequest.setNodeCode(nodeCode.getType());
        messageMQRequest.setNodeType(nodeType.toValue());
        messageMQRequest.setParams(params);
        messageMQRequest.setRouteParam(map);
        messageMQRequest.setCustomerId(customerId);
        messageMQRequest.setMobile(getCustomerAccount(customerId));
        producerService.sendMessage(messageMQRequest);
    }

    /**
     * 获取会员账号
     * @param customerId
     * @return
     */
    private String getCustomerAccount(String customerId){
        Customer customer = customerService.findById(customerId);
        if(Objects.isNull(customer)){
            return null;
        }
        return customer.getCustomerAccount();
    }
}
