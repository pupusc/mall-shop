package com.wanmi.sbc.customer.distribution.service;

import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CodeGenUtil;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.request.customer.CustomerAddRequest;
import com.wanmi.sbc.customer.api.request.customer.CustomerToDistributorModifyRequest;
import com.wanmi.sbc.customer.api.request.customer.DistributionCountInvitedCustRequest;
import com.wanmi.sbc.customer.api.request.distribution.*;
import com.wanmi.sbc.customer.api.response.customer.CustomerAddResponse;
import com.wanmi.sbc.customer.api.response.distribution.DistributionCountInvitedCustResponse;
import com.wanmi.sbc.customer.api.response.distribution.DistributionCustomerAddForBossResponse;
import com.wanmi.sbc.customer.api.response.distribution.DistributionCustomerAddResponse;
import com.wanmi.sbc.customer.bean.dto.DistributorDTO;
import com.wanmi.sbc.customer.bean.enums.*;
import com.wanmi.sbc.customer.bean.vo.DistributionCustomerVO;
import com.wanmi.sbc.customer.bean.vo.DistributorLevelVO;
import com.wanmi.sbc.customer.bean.vo.DistributionCustomerShowPhoneVO;
import com.wanmi.sbc.customer.detail.model.root.CustomerDetail;
import com.wanmi.sbc.customer.detail.service.CustomerDetailService;
import com.wanmi.sbc.customer.distribution.model.root.DistributionCustomer;
import com.wanmi.sbc.customer.distribution.model.root.DistributionCustomerInviteInfo;
import com.wanmi.sbc.customer.distribution.repository.DistributionCustomerRepository;
import com.wanmi.sbc.customer.model.root.Customer;
import com.wanmi.sbc.customer.model.root.CustomerBase;
import com.wanmi.sbc.customer.mq.ProducerService;
import com.wanmi.sbc.customer.quicklogin.model.root.ThirdLoginRelation;
import com.wanmi.sbc.customer.quicklogin.service.ThirdLoginRelationService;
import com.wanmi.sbc.customer.redis.DistributionCommissionRedisService;
import com.wanmi.sbc.customer.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>分销员业务逻辑</p>
 *
 * @author lq
 * @date 2019-02-19 10:13:15
 */
@Slf4j
@Service("DistributionCustomerService")
public class DistributionCustomerService {

    @Autowired
    private DistributionCustomerRepository distributionCustomerRepository;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerDetailService customerDetailService;

    @Autowired
    private ProducerService producerService;

    @Autowired
    private ThirdLoginRelationService thirdLoginRelationService;

    @Autowired
    DistributionCustomerInviteInfoService distributionCustomerInviteInfoService;

    @Autowired
    DistributionCommissionRedisService distributionCommissionRedisService;

    @Autowired
    CustomerDistributionInviteNewService customerDistributionInviteNewService;
    /**
     * 新增分销员
     *
     * @author lq
     */
    @Transactional
    public DistributionCustomer add(DistributionCustomer entity) {
        return distributionCustomerRepository.save(entity);
    }

    /**
     * 修改分销员
     *
     * @author lq
     */
    @Transactional
    public DistributionCustomer modify(DistributionCustomer entity) {
        distributionCustomerRepository.save(entity);
        return entity;
    }

    /**
     * 单个删除分销员
     *
     * @author lq
     */
    @Transactional
    public void deleteById(String id) {
        distributionCustomerRepository.deleteById(id);
    }

    /**
     * 批量删除分销员
     *
     * @author lq
     */
    @Transactional
    public void deleteByIdList(List<String> ids) {
        distributionCustomerRepository.deleteAll(ids.stream().map(id -> {
            DistributionCustomer entity = new DistributionCustomer();
            entity.setDistributionId(id);
            return entity;
        }).collect(Collectors.toList()));
    }

    /**
     * 单个查询分销员
     *
     * @author lq
     */
    public DistributionCustomer getById(String id) {
        return distributionCustomerRepository.findById(id).orElse(null);
    }

    /**
     * 根据分销员编号查询单个查询分销员(未删除)
     *
     * @param distributionId
     * @author lq
     */
    public DistributionCustomer getByDistributionIdAndDelFlag(String distributionId) {
        return distributionCustomerRepository.findByDistributionIdAndDelFlag(distributionId, DeleteFlag.NO);
    }
    /**
     * 根据会员编号查询单个查询分销员
     *
     * @param customerId
     * @author lq
     */
    public DistributionCustomer getByCustomerIdAndDistributorFlagAndDelFlag(String customerId,DefaultFlag distributorFlag) {
        return distributionCustomerRepository.findByCustomerIdAndDistributorFlagAndDelFlag(customerId,
                distributorFlag, DeleteFlag.NO);
    }

    /**
     * 根据被邀请会员id查询单个查询分销员
     *
     * @param inviteCustomerId
     * @param distributorFlag
     */
    public DistributionCustomer getByInviteCustomerId(String inviteCustomerId,DefaultFlag distributorFlag) {
        return distributionCustomerRepository.findByInviteCustomerId(inviteCustomerId,
                distributorFlag, DeleteFlag.NO);
    }

    /**
     * 根据会员编号查询单个查询分销员
     *
     * @param customerId
     * @author lq
     */
    public DistributionCustomer getByCustomerIdAndDelFlag(String customerId) {
        return distributionCustomerRepository.findByCustomerIdAndDelFlag(customerId, DeleteFlag.NO);
    }

    /**
     * 根据会员编号查询单个查询分销员
     *
     * @param customerId
     * @author lq
     */
    public DistributionCustomer getByCustomerId(String customerId) {
        return distributionCustomerRepository.findByCustomerId(customerId);
    }

    /**
     * 根据用户id查询分销员及其邀请人列表
     *
     * @param customerId
     * @return
     */
    public List<DistributionCustomer> listByCustomerId(String customerId) {

        // 1.查询用户信息
        DistributionCustomer customer = this.getByCustomerIdAndDelFlag(customerId);

        if (Objects.isNull(customer)) {
            return new ArrayList<>();
        }

        List<DistributionCustomer> customers = new ArrayList<>();
        customers.add(customer);

        // 2.查询用户邀请人列表
        String inviteCustomerIds = customer.getInviteCustomerIds();
        if (StringUtils.isNotEmpty(inviteCustomerIds)) {
            List<String> customerIds = Arrays.asList(inviteCustomerIds.split(","));
            List<DistributionCustomer> inviters = distributionCustomerRepository.findByCustomerIdsAndDelFlag(customerIds, DeleteFlag.NO);
            customerIds.forEach(id -> {
                Optional<DistributionCustomer> inviter = inviters.stream().filter(item -> item.getCustomerId().equals(id)).findFirst();
                if (inviter.isPresent()) {
                    customers.add(inviter.get());
                }
            });
        }

        return customers;
    }

    /**
     * 查询下单人的佣金受益人列表
     */
    public List<DistributionCustomer> listDistributorsForOrderCommit(
            DistributionCustomerListForOrderCommitRequest request) {
        // 查询下单人及其邀请人
        List<DistributionCustomer> buyerDistributors = this.listByCustomerId(
                request.getBuyerId()
        );

        // 查询店主及其邀请人
        List<DistributionCustomer> storeDistributors = new ArrayList<>();
        if (StringUtils.isNotEmpty(request.getInviteeId())) {
            storeDistributors = this.listByCustomerId(request.getInviteeId());
        }

        // 计算分销佣金受益人
        List<DistributionCustomer> inviteeCustomers = new ArrayList<>();

        // 1.根据是否自购、佣金返利优先级计算受益人
        if (StringUtils.isEmpty(request.getInviteeId())
                && DefaultFlag.YES.equals(request.getIsDistributor())) {
            // 分销员自购，受益人为下单人及其邀请人
            inviteeCustomers = buyerDistributors;
        } else {
            // 非分销员自购
            if (CommissionPriorityType.INVITOR.equals(request.getCommissionPriorityType())) {
                // 邀请人优先
                if (buyerDistributors.size() >= 2 &&
                        isDistributor(buyerDistributors.get(1))) {
                    // 下单人有邀请人且是分销员，受益人为邀请人及其邀请人
                    inviteeCustomers = buyerDistributors.subList(1, buyerDistributors.size());
                } else if (storeDistributors.size() >= 1) {
                    // 否则如果在店内购买，受益人为店主及其邀请人
                    inviteeCustomers = storeDistributors;
                }
            } else {
                // 店铺优先
                if (storeDistributors.size() >= 1) {
                    // 如果在店内购买，受益人为店主及其邀请人
                    inviteeCustomers = storeDistributors;
                } else if (buyerDistributors.size() >= 2 &&
                        isDistributor(buyerDistributors.get(1))) {
                    // 否则如果下单人有邀请人且是分销员，受益人为邀请人及其邀请人
                    inviteeCustomers = buyerDistributors.subList(1, buyerDistributors.size());
                }

            }
        }

        // 2.考虑不是分销员或分销员被禁用以及脱钩的情况
        if (CollectionUtils.isNotEmpty(inviteeCustomers)) {

            for (int idx = 0; idx < inviteeCustomers.size(); idx++) {

                DistributionCustomer customer = inviteeCustomers.get(idx);

                // 如果不是分销员，则停止受益
                if (!isDistributor(customer)) {
                    inviteeCustomers = inviteeCustomers.subList(0, idx);
                    break;
                }

                DistributorLevelVO level = request.getDistributorLevels().stream()
                        .filter(l -> l.getDistributorLevelId().equals(customer.getDistributorLevelId())).findFirst().orElse(null);
                if(Objects.isNull(level)){
                    break;
                }

                if (idx != 0) {
                    DistributionCustomer preCustomer = inviteeCustomers.get(idx - 1);
                    DistributorLevelVO preLevel = request.getDistributorLevels().stream()
                            .filter(l -> l.getDistributorLevelId().equals(preCustomer.getDistributorLevelId())).findFirst().get();

                    // 根据脱钩状态，判断邀请人是否脱钩
                    if (CommissionUnhookType.EQUAL.equals(request.getCommissionUnhookType())) {
                        // 当为平级脱钩，如果分销员等级大于等于邀请人则脱钩
                        if (preLevel.getSort() >= level.getSort()) {
                            inviteeCustomers = inviteeCustomers.subList(0, idx);
                            break;
                        }
                    } else if(CommissionUnhookType.HIGHER.equals(request.getCommissionUnhookType())) {
                        // 当为高于等级脱钩，如果分销员等级大于邀请人则脱钩
                        if (preLevel.getSort() > level.getSort()) {
                            inviteeCustomers = inviteeCustomers.subList(0, idx);
                            break;
                        }
                    }
                }
            }
        }

        // 3.因为目前只有一个提成人、一个返利人，所以这边取两个元素
        if (inviteeCustomers.size() > 2) {
            inviteeCustomers = inviteeCustomers.subList(0, 2);
        }

        return inviteeCustomers;
    }

    /**
     * 根据分销员信息，判断是否分销员
     */
    private boolean isDistributor(DistributionCustomer customer) {
        return DefaultFlag.YES.equals(customer.getDistributorFlag())
                && DefaultFlag.NO.equals(customer.getForbiddenFlag());
    }

    /**
     * 分页查询分销员
     *
     * @author lq
     */
    public Page<DistributionCustomer> page(DistributionCustomerQueryRequest queryReq) {
        return distributionCustomerRepository.findAll(
                DistributionCustomerWhereCriteriaBuilder.build(queryReq),
                queryReq.getPageRequest());
    }

    /**
     * 列表查询分销员
     *
     * @author lq
     */
    public List<DistributionCustomer> list(DistributionCustomerQueryRequest queryReq) {
        return distributionCustomerRepository.findAll(DistributionCustomerWhereCriteriaBuilder.build(queryReq));
    }

    /**
     * 新增分销员API（运营后台）
     *
     * @param distributionCustomerAddForBossRequest 分销员新增参数结构 {@link DistributionCustomerAddForBossResponse}
     * @return 新增的分销员信息 {@link DistributionCustomerAddResponse}
     * @author lq
     */
    @Transactional
    public DistributionCustomerShowPhoneVO addForBoss(@RequestBody @Valid DistributionCustomerAddForBossRequest distributionCustomerAddForBossRequest) {
        //查询会员信息
        Customer customer = customerService.findByCustomerAccountAndDelFlag(distributionCustomerAddForBossRequest.getCustomerAccount());
        DistributionCustomer distributionCustomer = null;
        CustomerDetail customerDetail;
        if (Objects.nonNull(customer)){
            //分销账号是否存在
            distributionCustomer = this.getByCustomerIdAndDelFlag(customer.getCustomerId());
            if (Objects.nonNull(distributionCustomer) && DefaultFlag.YES == distributionCustomer.getDistributorFlag()) {
                throw new SbcRuntimeException("K-010002");
            }
            customerDetail = customerDetailService.findByCustomerId(customer.getCustomerId());
        }else{
            CustomerAddResponse customerAddResponse = customerService.bossSaveCustomer(getCustomer(distributionCustomerAddForBossRequest));
            customer = new Customer();
            customer.setCustomerId(customerAddResponse.getCustomerId());
            customer.setCustomerAccount(customerAddResponse.getCustomerAccount());
            customerDetail = new CustomerDetail();
            customerDetail.setCustomerName(customerAddResponse.getCustomerName());
        }
        if (Objects.isNull(distributionCustomer)){
            //创建分销员账号
            distributionCustomer = new DistributionCustomer();
            distributionCustomer.setCustomerId(customer.getCustomerId());
            distributionCustomer.setCustomerName(customerDetail.getCustomerName());
            distributionCustomer.setCustomerAccount(customer.getCustomerAccount());
            distributionCustomer.setCreatePerson(distributionCustomerAddForBossRequest.getEmployeeId());
            distributionCustomer.setCreateTime(LocalDateTime.now());
            distributionCustomer.setInviteCode(CodeGenUtil.toSerialCode(Long.valueOf(customer.getCustomerAccount()),8).toUpperCase());
        }
        distributionCustomer.setDistributorFlag(DefaultFlag.YES);
        distributionCustomer.setDistributorLevelId(distributionCustomerAddForBossRequest.getDistributorLevelId());
        this.add(distributionCustomer);
        DistributionCustomerInviteInfo distributionCustomerInviteInfo = distributionCustomerInviteInfoService.getDistributionCustomerInviteInfo(distributionCustomer);
        distributionCustomerInviteInfoService.add(distributionCustomerInviteInfo);
        return this.wrapperEsVo(distributionCustomer);

    }

    /**
     * 新增分销员信息
     * @param distributionCustomerAddRequest
     * @return
     */
    @Transactional
    public DistributionCustomerVO add(DistributionCustomerAddRequest distributionCustomerAddRequest) {
        DistributionCustomer distributionCustomer = new DistributionCustomer();
        KsBeanUtil.copyProperties(distributionCustomerAddRequest, distributionCustomer);
        distributionCustomer.setCommissionTotal(distributionCustomer.getRewardCash().add(distributionCustomer.getCommission()));
        distributionCustomer.setInviteCode(CodeGenUtil.toSerialCode(Long.valueOf(distributionCustomer.getCustomerAccount()),8).toUpperCase());
        return this.wrapperVo(this.add(distributionCustomer));
    }

    /**
     * 批量启用/禁用分销员
     *
     * @param forbiddenFlag
     * @param distributionIds
     * @return
     */
    @Transactional
    public int updateForbiddenFlag(DefaultFlag forbiddenFlag, List<String> distributionIds, String forbidReason) {
        //账号状态 0：启用中  1：禁用中
        return distributionCustomerRepository.updateForbiddenFlag(forbiddenFlag, distributionIds, forbidReason);
    }

    /**
     * 根据会员ID更新会员账号
     *
     * @param customerId      会员ID
     * @param customerAccount 会员账号
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public int updateCustomerAccountByCustomerId(String customerId, String customerAccount) {
        return distributionCustomerRepository.updateCustomerAccountByCustomerId(customerId, customerAccount);
    }

    /**
     * 根据会员ID更新会员名称
     *
     * @param customerId   会员ID
     * @param customerName 会员名称
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public int updateCustomerNameByCustomerId(String customerId, String customerName) {
        return distributionCustomerRepository.updateCustomerNameByCustomerId(customerId, customerName);
    }
    /**
     * 根据会员ID更新会员名称和会员账号
     *
     * @param customerId   会员ID
     * @param customerName 会员名称
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public int updateCustomerNameAndAccountByCustomerId(String customerId,  String customerAccount,String customerName) {
        return distributionCustomerRepository.updateCustomerNameAndAccountByCustomerId(customerId,customerAccount, customerName);
    }
    /**
     * 将实体包装成VO
     *
     * @author lq
     */
    public DistributionCustomerVO wrapperVo(DistributionCustomer distributionCustomer) {
        if (distributionCustomer != null) {
            DistributionCustomerVO distributionCustomerVO = new DistributionCustomerVO();
            KsBeanUtil.copyPropertiesThird(distributionCustomer, distributionCustomerVO);
            ThirdLoginRelation thirdLoginRelation =thirdLoginRelationService.findFirstByCustomerIdAndThirdType(distributionCustomer.getCustomerId(), ThirdLoginType.WECHAT);
            if(Objects.nonNull(thirdLoginRelation)){
                distributionCustomerVO.setHeadImg(thirdLoginRelation.getHeadimgurl());
            }
            if(StringUtils.isNotBlank(distributionCustomer.getCustomerId())){
                DistributionCountInvitedCustRequest request = new DistributionCountInvitedCustRequest();
                request.setCustomerId(distributionCustomer.getCustomerId());
                DistributionCountInvitedCustResponse response = customerDistributionInviteNewService.distinctCountInvitedCustomers(request);
                if(response!=null){
                    if(response.getValidCount()!=null){
                        distributionCustomerVO.setInviteAvailableCount(response.getValidCount().intValue());
                    }
                }
            }
            return distributionCustomerVO;
        }
        return null;
    }


    /**
     * 将实体包装成VO
     *
     * @author lq
     */
    public DistributionCustomerShowPhoneVO wrapperEsVo(DistributionCustomer distributionCustomer) {
        if (distributionCustomer != null) {
            DistributionCustomerShowPhoneVO distributionCustomerVO = new DistributionCustomerShowPhoneVO();
            KsBeanUtil.copyPropertiesThird(distributionCustomer, distributionCustomerVO);
            ThirdLoginRelation thirdLoginRelation =thirdLoginRelationService.findFirstByCustomerIdAndThirdType(distributionCustomer.getCustomerId(), ThirdLoginType.WECHAT);
            if(Objects.nonNull(thirdLoginRelation)){
                distributionCustomerVO.setHeadImg(thirdLoginRelation.getHeadimgurl());
            }
            return distributionCustomerVO;
        }
        return null;
    }

    /**
     * 升级分销员
     */
    @Transactional(rollbackFor = Exception.class)
    public void upgrade(String customerId, List<DistributorLevelVO> distributorLevelVOList,DefaultFlag baseLimitType,Integer inviteNum) {
        // 1.新增或修改分销员
        DistributionCustomer distributionCustomer = distributionCustomerRepository.findByCustomerIdAndDelFlag(customerId, DeleteFlag.NO);
        Customer customer = customerService.findById(customerId);
        if (Objects.isNull(distributionCustomer)) {
            distributionCustomer = new DistributionCustomer();
            distributionCustomer.setCustomerId(customer.getCustomerId());
            distributionCustomer.setCustomerName(customer.getCustomerDetail().getCustomerName());
            distributionCustomer.setCustomerAccount(customer.getCustomerAccount());
            distributionCustomer.setCreateTime(LocalDateTime.now());
            distributionCustomer.setDistributorFlag(DefaultFlag.YES);
            distributionCustomer.setInviteCode(CodeGenUtil.toSerialCode(Long.valueOf(distributionCustomer.getCustomerAccount()),8).toUpperCase());
            distributionCustomer.setInviteCount(NumberUtils.INTEGER_ZERO);
        } else if(DefaultFlag.YES.equals(distributionCustomer.getDistributorFlag())) {
            // 已经是分销员，重新计算分销员等级
        } else {
            distributionCustomer.setCreateTime(LocalDateTime.now());
            distributionCustomer.setDistributorFlag(DefaultFlag.YES);
        }
        distributionCustomer.setInviteCount(Integer.sum(inviteNum,distributionCustomer.getInviteCount()));
        distributionCustomer.setDistributorLevelId(calDistributorLevel(distributionCustomer,distributorLevelVOList,baseLimitType));
        this.add(distributionCustomer);

        // 2.更新会员详情表
        customerService.updateCustomerToDistributor(
                CustomerToDistributorModifyRequest.builder().customerId(customerId).isDistributor(DefaultFlag.YES).build());

        // 3.更新会员资金
        producerService.modifyIsDistributorWithCustomerFunds(
                customerId,
                customer.getCustomerDetail().getCustomerName(),
                customer.getCustomerAccount());
    }

    /**
     * 升级分销员
     */
    @Transactional(rollbackFor = Exception.class)
    public void upgrade(DistributionCustomer distributionCustomer, List<DistributorLevelVO> distributorLevelVOList,DefaultFlag baseLimitType) {
        if (Objects.isNull(distributionCustomer)){
            return;
        }
        String customerId = distributionCustomer.getCustomerId();
        Customer customer = customerService.findById(distributionCustomer.getCustomerId());
        if(DefaultFlag.NO.equals(distributionCustomer.getDistributorFlag())) {
            distributionCustomer.setCreateTime(LocalDateTime.now());
            distributionCustomer.setDistributorFlag(DefaultFlag.YES);
        }
        distributionCustomer.setDistributorLevelId(calDistributorLevel(distributionCustomer,distributorLevelVOList,baseLimitType));
        this.add(distributionCustomer);

        // 2.更新会员详情表
        customerService.updateCustomerToDistributor(
                CustomerToDistributorModifyRequest.builder().customerId(customerId).isDistributor(DefaultFlag.YES).build());

        // 3.更新会员资金
        producerService.modifyIsDistributorWithCustomerFunds(
                customerId,
                customer.getCustomerDetail().getCustomerName(),
                customer.getCustomerAccount());
    }



    /**
     * 计算分销员等级
     * @param distributionCustomer
     * @return
     */
    public String calDistributorLevel(DistributionCustomer distributionCustomer,List<DistributorLevelVO> distributorLevelVOList,DefaultFlag baseLimitType){
        log.info("===========>>分销员信息：{},分销设置信息：{},邀新奖励限制：{}，计算分销员等级开始！",distributionCustomer,distributorLevelVOList,baseLimitType);
        //邀新人数 || 有效邀新人数
        Integer inviteCount =  DefaultFlag.YES == baseLimitType ? distributionCustomer.getInviteAvailableCount() : distributionCustomer.getInviteCount();
        inviteCount = Objects.isNull(inviteCount) ? NumberUtils.INTEGER_ZERO : inviteCount;
        //销售额
        BigDecimal sales = Objects.nonNull(distributionCustomer.getSales()) ? distributionCustomer.getSales() : BigDecimal.ZERO ;
        //已入账佣金
        BigDecimal commission = Objects.nonNull(distributionCustomer.getCommission()) ? distributionCustomer.getCommission() : BigDecimal.ZERO;
        //分销员等级等级ID
        String distributorLevelId = distributionCustomer.getDistributorLevelId();

        DistributorLevelVO defaultDistributorLevel = distributorLevelVOList.stream().findFirst().orElse(new DistributorLevelVO());

        Map<String,DistributorLevelVO> map = distributorLevelVOList.stream().collect(Collectors.toMap(DistributorLevelVO::getDistributorLevelId, distributorLevelVO -> distributorLevelVO));

        if(StringUtils.isNotBlank(distributorLevelId)){
            DistributorLevelVO vo = map.get(distributorLevelId);
            final Integer sort = vo.getSort();
            distributorLevelVOList = distributorLevelVOList.stream().filter(distributorLevelVO -> distributorLevelVO.getSort() > sort).collect(Collectors.toList());
        }

        String resultLevelId = distributorLevelId;
        for (DistributorLevelVO distributorLevelVO : distributorLevelVOList){
            DefaultFlag salesFlag = distributorLevelVO.getSalesFlag();
            BigDecimal salesThreshold = distributorLevelVO.getSalesThreshold();

            DefaultFlag recordFlag = distributorLevelVO.getRecordFlag();
            BigDecimal recordThreshold = distributorLevelVO.getRecordThreshold();

            DefaultFlag inviteFlag = distributorLevelVO.getInviteFlag();
            Integer inviteThreshold = distributorLevelVO.getInviteThreshold();

            String levelId = distributorLevelVO.getDistributorLevelId();

            Boolean salesThresholdFlag = Objects.isNull(salesThreshold) ?  Boolean.TRUE : sales.compareTo(salesThreshold) == -1;

            Boolean recordThresholdFlag = Objects.isNull(recordThreshold) ?  Boolean.TRUE : commission.compareTo(recordThreshold) == -1;


            Boolean inviteThresholdFlag = Objects.isNull(inviteThreshold) ?  Boolean.TRUE : inviteCount.compareTo(inviteThreshold) == -1;

            Boolean salesThresholdResult = Boolean.TRUE;
            //勾选销售额
            if (DefaultFlag.YES == salesFlag && salesThresholdFlag){
                salesThresholdResult = Boolean.FALSE;
            }

            Boolean recordThresholdResult = Boolean.TRUE;
            //勾选到账收益额
            if (DefaultFlag.YES == recordFlag && recordThresholdFlag){
                recordThresholdResult = Boolean.FALSE;
            }

            Boolean inviteThresholdResult = Boolean.TRUE;
            //勾选邀请人数
            if (DefaultFlag.YES == inviteFlag && inviteThresholdFlag){
                inviteThresholdResult = Boolean.FALSE;
            }

            if (salesThresholdResult && recordThresholdResult && inviteThresholdResult){
                resultLevelId = levelId;
            }
        }
        log.info("===========>>最终计算等级ID:{},计算分销员等级结束！",resultLevelId);
        return StringUtils.isBlank(resultLevelId) ? defaultDistributorLevel.getDistributorLevelId() : resultLevelId;
    }

    /**
     * 新增会员设置初始值
     *
     * @param request
     * @return
     */
    private CustomerAddRequest getCustomer(DistributionCustomerAddForBossRequest request) {
        CustomerAddRequest customerAddRequest = new CustomerAddRequest();
        customerAddRequest.setS2bSupplier(false);
        customerAddRequest.setEmployeeId(request.getEmployeeId());
        customerAddRequest.setCustomerAccount(request.getCustomerAccount());
        customerAddRequest.setCustomerName(request.getCustomerAccount());
        customerAddRequest.setContactName(request.getCustomerAccount());
        customerAddRequest.setContactPhone(request.getCustomerAccount());
        customerAddRequest.setOperator(request.getEmployeeId());
        return customerAddRequest;
    }

    /**
     * 更新分销员奖励信息
     * @param request
     */
    @Transactional(rollbackFor = Exception.class)
    public int modifyReward(DistributionCustomerModifyRewardRequest request) {
        request.setCommissionTotal(request.getRewardCash());
        int result = distributionCustomerRepository.modifyReward(request.getInviteCount(),request.getRewardCash(),request.getRewardCashNotRecorded(),
                request.getDistributionTradeCount(),request.getSales(),request.getCommissionNotRecorded(),request.getCommissionTotal(),request.getDistributionId());
        //redis统计数据同步
        distributionCommissionRedisService.modifyReward(request);
        return result;
    }




    @Transactional(rollbackFor = Exception.class)
    public void afterSettleUpdate(AfterSettleUpdateDistributorRequest request) {
        String distributeId = request.getDistributeId();
        DistributionCustomer distributionCustomer = distributionCustomerRepository.findById(distributeId).orElse(null);
        if (Objects.isNull(distributionCustomer)){
            return;
        }
        distributionCustomer.setInviteAvailableCount(Integer.sum(distributionCustomer.getInviteAvailableCount(),request.getInviteNum()));
        distributionCustomer.setRewardCash(distributionCustomer.getRewardCash().add(request.getInviteAmount()));
        distributionCustomer.setRewardCashNotRecorded(distributionCustomer.getRewardCashNotRecorded().subtract(request.getInviteAmount()));
        distributionCustomer.setDistributionTradeCount(distributionCustomer.getDistributionTradeCount() - request.getOrderNum());
        distributionCustomer.setSales(distributionCustomer.getSales().subtract(request.getAmount()));
        distributionCustomer.setCommission(distributionCustomer.getCommission().add(request.getGrantAmount()));
        distributionCustomer.setCommissionTotal(distributionCustomer.getCommissionTotal().add(request.getGrantAmount()).add(request.getInviteAmount()));
        distributionCustomer.setCommissionNotRecorded(distributionCustomer.getCommissionNotRecorded().subtract(request.getTotalDistributeAmount()));
        if (request.getDistributorFlag()){
            distributionCustomer.setDistributorLevelId(calDistributorLevel(distributionCustomer,request.getDistributorLevelVOList(),request.getBaseLimitType()));
            distributionCustomer.setDistributorFlag(DefaultFlag.YES);
        }

       this.add(distributionCustomer);

        //redis统计同步
        distributionCommissionRedisService.afterSettleUpdate(request);


        // 2.更新会员详情表
        customerService.updateCustomerToDistributor(
                CustomerToDistributorModifyRequest.builder().customerId(distributionCustomer.getCustomerId()).isDistributor(DefaultFlag.YES).build());

        // 3.更新会员资金
        producerService.modifyIsDistributorWithCustomerFunds(
                distributionCustomer.getCustomerId(),
                distributionCustomer.getCustomerName(),
                distributionCustomer.getCustomerAccount());
    }

    /**
     * 补发邀新奖励
     * @param request
     */
    @Transactional(rollbackFor = Exception.class)
    public void afterSupplyAgainUpdate(AfterSupplyAgainUpdateDistributorRequest request) {
        distributionCustomerRepository.afterSupplyAgainUpdate(request.getRewardCash(),request.getRewardCashNotRecorded(),request.getDistributeId());
        //redis同步
        distributionCommissionRedisService.afterSupplyAgainUpdate(request.getRewardCash());
    }

    /**
     * 根据邀请码查询分销员信息
     * @param inviteCode
     * @return
     */
    public  DistributionCustomer findByInviteCode(String inviteCode){
        return distributionCustomerRepository.findByInviteCode(inviteCode);
    }


    /**
     * 新增分销员信息
     * @param customer
     * @param now
     * @param settingAmount
     * @param rewardCash
     */
    public DistributionCustomer addDistributionCustomer(CustomerBase customer, LocalDateTime now,
                                                         BigDecimal settingAmount, BigDecimal rewardCash) {
        // 查询不到分销员信息，则新增分销员记录
        DistributionCustomer distributionCustomer = new DistributionCustomer();
        distributionCustomer.setCustomerId(customer.getCustomerId());
        distributionCustomer.setCustomerAccount(customer.getCustomerAccount());
        distributionCustomer.setCustomerName(customer.getCustomerName());
        distributionCustomer.setCreateTime(now);
        // 初次添加，邀新人数为1
        distributionCustomer.setInviteCount(1);
        // 邀新奖励
        distributionCustomer.setRewardCash(rewardCash);
        // 未入账邀新奖励
        distributionCustomer.setRewardCashNotRecorded(settingAmount.subtract(rewardCash));
        distributionCustomer.setCommissionTotal(rewardCash.add(distributionCustomer.getCommission()));
        distributionCustomer.setInviteCode(CodeGenUtil.toSerialCode(Long.valueOf(distributionCustomer.getCustomerAccount()),8).toUpperCase());
        return this.add(distributionCustomer);
    }


    /**
     * 更新分销员提成-已入账佣金包含提成
     * @param customerId
     * @param commission
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public int updateCommissionByCustomerId(String customerId, BigDecimal commission){
        return distributionCustomerRepository.updateCommissionByCustomerId(customerId,commission);
    }

    /**
     * 批量更新更新分销员提成-已入账佣金包含提成
     * @param list
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public int batchUpdateCommissionByCustomerId(List<DistributorDTO> list){
        int result = 0;
        for (DistributorDTO distributorDTO : list){
            result += updateCommissionByCustomerId(distributorDTO.getCustomerId(),distributorDTO.getCommission());
            DistributionCustomer distributionCustomer = distributionCustomerRepository.findByCustomerIdAndDelFlag(distributorDTO.getCustomerId(),DeleteFlag.NO);
            if(distributionCustomer!=null){
                BigDecimal commissionTotal = distributionCustomer.getCommissionTotal().add(distributorDTO.getCommission());
                distributionCustomer.setCommissionTotal(commissionTotal);
                distributionCustomerRepository.save(distributionCustomer);
            }
        }
        return result;
    }

    /**
     * 初始化历史分销员-邀请码
     * @return
     */
    public void initInviteCode(){
        for (int i = 0;;i++){
            List<DistributionCustomer> distributionCustomers = distributionCustomerRepository.findByInviteCodeIsNull( PageRequest.of(i,1000));
            if (CollectionUtils.isEmpty(distributionCustomers)){
                break;
            }
            distributionCustomers =  distributionCustomers.stream().map(distributionCustomer -> {
                distributionCustomer.setInviteCode(CodeGenUtil.toSerialCode(Long.valueOf(distributionCustomer.getCustomerAccount()),8).toUpperCase());
                return distributionCustomer;
            }).collect(Collectors.toList());
            distributionCustomerRepository.saveAll(distributionCustomers);
        }
    }

    /**
     * 初始化分销员的分销员等级
     */
    @Transactional(rollbackFor = Exception.class)
    public void initDistributorLevel(String levelId) {
        distributionCustomerRepository.initDistributorLevel(levelId);
    }

    /**
     * 验证是否分销员
     * @param request
     * @return
     */
    public Boolean isDistributor(DistributionCustomerCheckRequest request) {
        //会员ID
        String customerId = request.getCustomerId();

        //查询是否开启社交分销
        DefaultFlag openFlag = request.getOpenFlag();
        Boolean enable = Boolean.FALSE;
        //查询会员基础表信息
        Customer customer = customerService.findById(customerId);
        //如果会员基础信息是null 或者不是审核通过状态
        if (Objects.isNull(customer) || !CheckState.CHECKED.equals(customer.getCheckState())) {
            return Boolean.FALSE;
        }
        //查询会员detail表
        CustomerDetail customerDetail = customerDetailService.findAnyByCustomerId(customerId);
        //如果会员detail表信息是null 或者不是启用状态
        if (Objects.isNull(customerDetail) || !CustomerStatus.ENABLE.equals(customerDetail.getCustomerStatus())  ) {
            return Boolean.FALSE;
        }

        boolean distributionEnable = Boolean.FALSE;
        //验证分销员状态
        DistributionCustomer distributionCustomer = this.getByCustomerIdAndDelFlag(customerId);
        if (Objects.nonNull(distributionCustomer) && Objects.equals(DefaultFlag.YES,
                distributionCustomer.getDistributorFlag()) && Objects.equals(DefaultFlag.NO,
                distributionCustomer.getForbiddenFlag())) {
            //验证分销员状态--有分销员资格，且启用中
            distributionEnable = Boolean.TRUE;
        }

        if (DefaultFlag.YES.equals(openFlag) && Objects.nonNull(distributionCustomer) && distributionEnable) {
            enable = Boolean.TRUE;
        }
        return enable;
    }

    @Transactional
    public void modifyCustomerId(String oldCustomerId, String customerId) {
        distributionCustomerRepository.modifyCustomerId(oldCustomerId,customerId);
    }
}
