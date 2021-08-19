package com.wanmi.sbc.account.funds.service;
import com.google.common.collect.Lists;
import io.seata.spring.annotation.GlobalTransactional;
import com.wanmi.sbc.account.api.request.funds.CustomerFundsAddAmountRequest;
import com.wanmi.sbc.account.api.request.funds.CustomerFundsDetailAddRequest;
import com.wanmi.sbc.account.api.request.funds.CustomerFundsPageRequest;
import com.wanmi.sbc.account.api.request.funds.GrantAmountRequest;
import com.wanmi.sbc.account.bean.enums.FundsStatus;
import com.wanmi.sbc.account.bean.enums.FundsSubType;
import com.wanmi.sbc.account.bean.enums.FundsType;
import com.wanmi.sbc.account.bean.vo.CustomerFundsForEsVO;
import com.wanmi.sbc.account.funds.model.root.CustomerFunds;
import com.wanmi.sbc.account.funds.model.root.CustomerFundsDetail;
import com.wanmi.sbc.account.funds.model.root.CustomerFundsIdBase;
import com.wanmi.sbc.account.funds.model.root.CustomerFundsStatistics;
import com.wanmi.sbc.account.funds.repository.CustomerFundsDetailRepository;
import com.wanmi.sbc.account.funds.repository.CustomerFundsRepository;
import com.wanmi.sbc.account.redis.CustomerFundsRedisService;
import com.wanmi.sbc.common.handler.aop.MasterRouteOnly;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.common.util.XssUtils;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 会员资金-服务层
 *
 * @author: Geek Wang
 * @createDate: 2019/2/19 9:28
 * @version: 1.0
 */
@Service
@Slf4j
public class CustomerFundsService {

    private static final Integer ALL = Integer.valueOf(-1);

    @Autowired
    private CustomerFundsRepository customerFundsRepository;

    @Autowired
    private CustomerFundsDetailRepository customerFundsDetailRepository;

    @Autowired
    private CustomerFundsRedisService customerFundsRedisService;

    /**
     * 会员资金分页查询
     *
     * @param request
     * @return
     */
    public Page<CustomerFunds> page(CustomerFundsPageRequest request) {
        return customerFundsRepository.findAll(buildSpecification(request), request.getPageRequest());
    }

    /**
     * 根据会员资金ID查询会员资金
     *
     * @param customerFundsId
     * @return
     */
    public CustomerFunds findByCustomerFundsId(String customerFundsId) {
        return customerFundsRepository.findById(customerFundsId).orElse(null);
    }

    /**
     * 根据会员IDlist查询会员资金list
     *
     * @param customerIdList
     * @return
     */
    @MasterRouteOnly
    public List<CustomerFunds> findListByCustomerIds(List<String> customerIdList) {
        return customerFundsRepository.findByCustomerIdIn(customerIdList);
    }


    /**
     * 根据会员ID查询会员资金
     *
     * @param customerId
     * @return
     */
    @MasterRouteOnly
    public CustomerFunds findByCustomerId(String customerId) {
        return customerFundsRepository.findByCustomerId(customerId);
    }


    /**
     * 根据会员ID集合查询会员资金列表唯一主键
     *
     * @param customerIds
     * @return
     */
    @MasterRouteOnly
    public List< CustomerFundsIdBase> findByCustomerIds(List<String> customerIds) {
        return customerFundsRepository.getCustomerFundsId(customerIds);
    }


    /**
     * 新增会员，初始化会员资金信息
     *
     * @param customerId      会员ID
     * @param customerName    会员名称
     * @param customerAccount 会员账号
     * @param accountBalance  账户余额
     * @param distributor     是否分销员，0：否，1：是
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public CustomerFunds init(String customerId, String customerName, String customerAccount, BigDecimal accountBalance, Integer distributor) {
        CustomerFunds customerFunds = new CustomerFunds();
        customerFunds.setCustomerId(customerId);
        customerFunds.setCustomerAccount(customerAccount);
        customerFunds.setCustomerName(customerName);
        customerFunds.setAccountBalance(accountBalance);
        customerFunds.setBlockedBalance(BigDecimal.ZERO);
        customerFunds.setWithdrawAmount(accountBalance);
        customerFunds.setAlreadyDrawAmount(BigDecimal.ZERO);
        // 会员资金初始化 如果有分销员佣金提成，则另处理收入笔数和收入金额
        if (accountBalance.compareTo(BigDecimal.ZERO) > 0) {
            customerFunds.setIncome(NumberUtils.LONG_ONE);
            customerFunds.setAmountReceived(accountBalance);
        } else {
            // 正常会员资金初始化
            customerFunds.setIncome(NumberUtils.LONG_ZERO);
            customerFunds.setAmountReceived(BigDecimal.ZERO);
        }
        customerFunds.setExpenditure(NumberUtils.LONG_ZERO);
        customerFunds.setAmountPaid(BigDecimal.ZERO);
        customerFunds.setDistributor(distributor);
        customerFunds.setCreateTime(LocalDateTime.now());
        customerFunds.setUpdateTime(LocalDateTime.now());
        return customerFundsRepository.save(customerFunds);
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
        return customerFundsRepository.updateCustomerAccountByCustomerId(customerId, customerAccount, LocalDateTime.now());
    }

    /**
     * 根据会员ID更新已提现金额
     *
     * @param customerId            会员ID
     * @param alreadyDrawCashAmount 已提现金额
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public int updateAlreadyDrawCashAmountByCustomerId(String customerId, BigDecimal alreadyDrawCashAmount) {
        return customerFundsRepository.updateAlreadyDrawCashAmountByCustomerId(customerId, alreadyDrawCashAmount, LocalDateTime.now());
    }

    /**
     * 根据会员ID更新账户余额（用户初始余额为0,增加余额时使用）
     *
     * @param customerId     会员ID
     * @param accountBalance 账户余额
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public int updateAccountBalanceByCustomerId(String customerId, BigDecimal accountBalance) {
        int result = customerFundsRepository.updateAccountBalanceByCustomerId(customerId, accountBalance, LocalDateTime.now());
        if (result > 0) {
            customerFundsRedisService.incrAccountBalanceTotal(accountBalance.doubleValue());
        }
        return result;
    }

    /**
     * 根据会员ID更新冻结金额
     *
     * @param customerId     会员ID
     * @param blockedBalance 冻结金额
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public int updateBlockedBalanceByCustomerId(String customerId, BigDecimal blockedBalance) {
        int result = customerFundsRepository.updateBlockedBalanceByCustomerId(customerId, blockedBalance, LocalDateTime.now());
        if (result > 0) {
            customerFundsRedisService.incrBlockedBalanceTotal(blockedBalance.doubleValue());
        }
        return result;
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
        return customerFundsRepository.updateCustomerNameByCustomerId(customerId, customerName, LocalDateTime.now());
    }


    /**
     * 根据会员ID更新会员名称、会员账号
     *
     * @param customerId      会员ID
     * @param customerName    会员名称
     * @param customerAccount 会员账号
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public int updateCustomerNameAndAccountByCustomerId(String customerId, String customerName, String customerAccount) {
        return customerFundsRepository.updateCustomerNameAndAccountByCustomerId(customerId, customerName, customerAccount, LocalDateTime.now());
    }


    /**
     * 根据会员ID更新是否分销员字段
     *
     * @param customerId  会员ID
     * @param distributor 是否分销员，0：否，1：是
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public int updateIsDistributorByCustomerId(String customerId, Integer distributor) {
        return customerFundsRepository.updateIsDistributorByCustomerId(customerId, distributor, LocalDateTime.now());
    }

    /**
     * 根据会员资金ID和提现金额更新冻结余额、可提现余额（用户提交提现申请）
     *
     * @param customerFundsId 会员资金ID
     * @param withdrawAmount  提现金额
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public int submitWithdrawCashApply(String customerFundsId, BigDecimal withdrawAmount) {
        int result = customerFundsRepository.submitWithdrawCashApply(customerFundsId, withdrawAmount);
        if (result > 0) {
            customerFundsRedisService.incrBlockedBalanceTotalAnddecrWithdrawAmountTotal(withdrawAmount.doubleValue());
        }
        return result;
    }

    /**
     * 根据会员资金ID和提现金额更新账户余额、冻结余额（同意用户提现申请）
     *
     * @param customerFundsId 会员资金ID
     * @param withdrawAmount  提现金额
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public int agreeWithdrawCashApply(String customerFundsId, BigDecimal withdrawAmount) {
        int result = customerFundsRepository.agreeWithdrawCashApply(customerFundsId, withdrawAmount);
        if (result > 0) {
            customerFundsRedisService.decrBlockedBalanceTotalAndAccountBalanceTotal(withdrawAmount.doubleValue());
        }
        return result;
    }

    /**
     * 根据会员资金ID和提现金额更新支出金额、支出数（同意用户提现申请）
     *
     * @param customerFundsId 会员资金ID
     * @param withdrawAmount  提现金额
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public int agreeAmountPaidAndExpenditure(String customerFundsId, BigDecimal withdrawAmount) {
        return customerFundsRepository.agreeAmountPaidAndExpenditure(customerFundsId, withdrawAmount);
    }

    /**
     * 根据会员资金ID和提现金额更新冻结余额、可提现余额（驳回用户提现申请）
     *
     * @param customerFundsId 会员资金ID
     * @param withdrawAmount  提现金额
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public int rejectWithdrawCashApply(String customerFundsId, BigDecimal withdrawAmount) {
        int result = customerFundsRepository.rejectWithdrawCashApply(customerFundsId, withdrawAmount);
        if (result > 0) {
            customerFundsRedisService.decrBlockedBalanceTotalAndIncrWithdrawAmountTotal(withdrawAmount.doubleValue());
        }
        return result;
    }

    /**
     * 使用余额，更新余额，可提现金额，支出金额，支出数
     *
     * @param customerFundsId
     * @param expenseAmount
     * @return
     */
    @GlobalTransactional
    @Transactional(rollbackFor = Exception.class)
    public int balancePay(String customerFundsId, BigDecimal expenseAmount) {
        int result = customerFundsRepository.balancePay(customerFundsId, expenseAmount);
        if (result > 0) {
            customerFundsRedisService.decrAccountBalanceTotalAndWithdrawAmountTotal(expenseAmount.doubleValue());
        }
        return result;
    }


    /**
     * 会员资金查询条件封装
     *
     * @param customerFundsPageRequest
     * @return
     */
    private Specification<CustomerFunds> buildSpecification(final CustomerFundsPageRequest customerFundsPageRequest) {
        return (Root<CustomerFunds> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            // 批量查询-会员资金IDList
            if (CollectionUtils.isNotEmpty(customerFundsPageRequest.getCustomerFundsIdList())) {
                predicates.add(root.get("customerFundsId").in(customerFundsPageRequest.getCustomerFundsIdList()));
            }

            //会员账号
            if (StringUtils.isNotBlank(customerFundsPageRequest.getCustomerAccount())) {
                predicates.add(cb.like(root.get("customerAccount"), buildLike(customerFundsPageRequest.getCustomerAccount())));
            }

            //会员名称
            if (StringUtils.isNotBlank(customerFundsPageRequest.getCustomerName())) {
                predicates.add(cb.like(root.get("customerName"), buildLike(customerFundsPageRequest.getCustomerName())));
            }

            //是否分销员
            if (Objects.nonNull(customerFundsPageRequest.getDistributor()) && !ALL.equals(customerFundsPageRequest.getDistributor())) {
                predicates.add(cb.equal(root.get("distributor"), customerFundsPageRequest.getDistributor()));
            }

            //账户余额开始范围
            if (Objects.nonNull(customerFundsPageRequest.getStartAccountBalance()) && customerFundsPageRequest.getStartAccountBalance().compareTo(BigDecimal.ZERO) > -1) {
                predicates.add(cb.ge(root.get("accountBalance"), customerFundsPageRequest.getStartAccountBalance()));
            }

            //账户余额结束范围
            if (Objects.nonNull(customerFundsPageRequest.getEndAccountBalance()) && customerFundsPageRequest.getEndAccountBalance().compareTo(BigDecimal.ZERO) > -1) {
                predicates.add(cb.le(root.get("accountBalance"), customerFundsPageRequest.getEndAccountBalance()));
            }

            //冻结余额开始范围
            if (Objects.nonNull(customerFundsPageRequest.getStartBlockedBalance()) && customerFundsPageRequest.getStartBlockedBalance().compareTo(BigDecimal.ZERO) > -1) {
                predicates.add(cb.ge(root.get("blockedBalance"), customerFundsPageRequest.getStartBlockedBalance()));
            }

            //冻结余额结束范围
            if (Objects.nonNull(customerFundsPageRequest.getEndBlockedBalance()) && customerFundsPageRequest.getEndBlockedBalance().compareTo(BigDecimal.ZERO) > -1) {
                predicates.add(cb.le(root.get("blockedBalance"), customerFundsPageRequest.getEndBlockedBalance()));
            }

            //可提现余额开始范围
            if (Objects.nonNull(customerFundsPageRequest.getStartWithdrawAmount()) && customerFundsPageRequest.getStartWithdrawAmount().compareTo(BigDecimal.ZERO) > -1) {
                predicates.add(cb.ge(root.get("withdrawAmount"), customerFundsPageRequest.getStartWithdrawAmount()));
            }

            //可提现余额结束范围
            if (Objects.nonNull(customerFundsPageRequest.getEndWithdrawAmount()) && customerFundsPageRequest.getEndWithdrawAmount().compareTo(BigDecimal.ZERO) > -1) {
                predicates.add(cb.le(root.get("withdrawAmount"), customerFundsPageRequest.getEndWithdrawAmount()));
            }

            return cb.and(predicates.toArray(new Predicate[]{}));
        };
    }

    /**
     * 封装SQL语句like模糊匹配字段
     *
     * @param field
     * @return
     */
    private String buildLike(String field) {
        StringBuilder stringBuilder = new StringBuilder();
        return stringBuilder.append("%").append(XssUtils.replaceLikeWildcard(field)).append("%").toString();
    }

    @Transactional(rollbackFor = Exception.class)
    public CustomerFunds grantAmount(GrantAmountRequest request) {
        CustomerFunds customerFundsResult = new CustomerFunds();
        // 查询是否有会员资金数据
        CustomerFunds customerFunds = customerFundsRepository.findByCustomerId(request.getCustomerId());
        if (Objects.isNull(customerFunds)) {
            // 如果没有，则新增一条
            customerFundsResult = this.init(request.getCustomerId(), request.getCustomerName(),
                    request.getCustomerAccount(), request.getAmount(), request.getDistributor());
        } else {
            // 如果有会员资金记录，则更新会员资金表
            customerFundsRepository.addAmount(request.getCustomerId(), request.getAmount());
        }
        // 保存余额明细
        CustomerFundsDetail fundsDetail = new CustomerFundsDetail();
        fundsDetail.setCreateTime(LocalDateTime.now());
        fundsDetail.setCustomerId(request.getCustomerId());
        fundsDetail.setBusinessId(request.getBusinessId());
        fundsDetail.setFundsType(request.getType());
        if (Objects.isNull(request.getSubType())) {
            fundsDetail.setSubType(FundsSubType.valueOf(request.getType().toString()));
        } else {
            fundsDetail.setSubType(request.getSubType());
        }
        fundsDetail.setFundsStatus(FundsStatus.YES);
        if (Objects.isNull(customerFunds)) {
            fundsDetail.setAccountBalance(request.getAmount());
        } else {
            fundsDetail.setAccountBalance(customerFunds.getAccountBalance().add(request.getAmount()));
        }
        fundsDetail.setReceiptPaymentAmount(request.getAmount());
        CustomerFundsDetail customerFundsDetail = customerFundsDetailRepository.save(fundsDetail);
        if (Objects.nonNull(customerFundsDetail)) {
            customerFundsRedisService.incrAccountBalanceTotalAndWithdrawAmountTotal(request.getAmount().doubleValue());
        }
        return customerFundsResult;
    }

    /**
     * 会员资金统计- 余额总额、冻结金额总额、可提现金额总额
     *
     * @return
     */
    public CustomerFundsStatistics statistics() {
        return customerFundsRepository.statistics();
    }


    /**
     * 初始化会员资金Redis缓存-余额总额、冻结金额总额、可提现金额总额
     *
     * @return
     */
    public Boolean initStatisticsCache() {
        CustomerFundsStatistics customerFundsStatistics = statistics();
        return customerFundsRedisService.incrAccountBalanceTotalAndBlockedBalanceTotalAndWithdrawAmountTotal(customerFundsStatistics.getAccountBalanceTotal().doubleValue(), customerFundsStatistics.getBlockedBalanceTotal().doubleValue(), customerFundsStatistics.getWithdrawAmountTotal().doubleValue());
    }

    /**
     * @return void
     * @Author lvzhenwei
     * @Description 余额支付订单退款增加账户余额金额
     * @Date 14:54 2019/7/16
     * @Param [customerFundsAddAmount]
     **/
    @Transactional
    public void addAmount(CustomerFundsAddAmountRequest customerFundsAddAmount) {
        CustomerFunds customerFunds = customerFundsRepository.findByCustomerId(customerFundsAddAmount.getCustomerId());
        customerFundsRepository.addAmount(customerFundsAddAmount.getCustomerId(), customerFundsAddAmount.getAmount());
        CustomerFundsDetailAddRequest customerFundsDetailAddRequest = new CustomerFundsDetailAddRequest();
        customerFundsDetailAddRequest.setCustomerId(customerFundsAddAmount.getCustomerId());
        customerFundsDetailAddRequest.setBusinessId(customerFundsAddAmount.getBusinessId());
        customerFundsDetailAddRequest.setFundsType(FundsType.BALANCE_PAY_REFUND);
        customerFundsDetailAddRequest.setReceiptPaymentAmount(customerFundsAddAmount.getAmount());
        customerFundsDetailAddRequest.setFundsStatus(FundsStatus.YES);
        customerFundsDetailAddRequest.setAccountBalance(customerFunds.getAccountBalance().add(customerFundsAddAmount.getAmount()));
        customerFundsDetailAddRequest.setSubType(FundsSubType.BALANCE_PAY_REFUND);
        customerFundsDetailAddRequest.setCreateTime(LocalDateTime.now());
        CustomerFundsDetail customerFundsDetail = new CustomerFundsDetail();
        KsBeanUtil.copyPropertiesThird(customerFundsDetailAddRequest, customerFundsDetail);
        customerFundsDetailRepository.save(customerFundsDetail);
    }



    /**
     * @Author yangzhen
     * @Description // 从mysql 中查询会员资金，用于刷入es
     * @Date 17:10 2020/12/15
     **/
    public List<CustomerFundsForEsVO> initEsPage(CustomerFundsPageRequest request) {
        List<String> customerFundsIds = customerFundsRepository.listByPage(request.getPageRequest());

        if (CollectionUtils.isEmpty(customerFundsIds)) {
            return Lists.newArrayList();
        }

        //查询结算单详情
        List<CustomerFunds> customerFundsList = customerFundsRepository.findAllById(customerFundsIds);

        List<CustomerFundsForEsVO> customerFundsVOS = new ArrayList<>();
        customerFundsList.forEach(customerFund -> {
            customerFundsVOS.add(KsBeanUtil.convert(customerFund, CustomerFundsForEsVO.class));
        });
        return customerFundsVOS;
    }


}
