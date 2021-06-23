package com.wanmi.sbc.account.redis;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.wanmi.sbc.account.api.constant.AccountRedisKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 会员资金-Redis服务层
 *
 * @author: Geek Wang
 * @createDate: 2019/2/19 9:28
 * @version: 1.0
 */
@Slf4j
@Service
public class CustomerFundsRedisService {

    @Autowired
    private RedisService redisService;

    /**
     * 增加余额总额、可提现金额总额
     * @param amount 金额
     * @return
     */
    public Map<String, Double> incrAccountBalanceTotalAndWithdrawAmountTotal(double amount){
      Double accountBalanceTotal = redisService.incrByFloat(AccountRedisKey.ACCOUNT_BALANCE_TOTAL,amount);
      Double withdrawAmountTotal = redisService.incrByFloat(AccountRedisKey.WITHDRAW_AMOUNT_TOTAL,amount);
      return ImmutableMap.of(AccountRedisKey.ACCOUNT_BALANCE_TOTAL,accountBalanceTotal,AccountRedisKey.WITHDRAW_AMOUNT_TOTAL,withdrawAmountTotal);
    }

    /**
     * 减少冻结金额总额、增加可提现金额总额
     * @param amount
     * @return
     */
    public Map<String, Double> decrBlockedBalanceTotalAndIncrWithdrawAmountTotal(double amount){
        Double blockedBalanceTotal = redisService.decrByFloat(AccountRedisKey.BLOCKED_BALANCE_TOTAL,amount);
        Double withdrawAmountTotal = redisService.incrByFloat(AccountRedisKey.WITHDRAW_AMOUNT_TOTAL,amount);
        return ImmutableMap.of(AccountRedisKey.BLOCKED_BALANCE_TOTAL,blockedBalanceTotal,AccountRedisKey.WITHDRAW_AMOUNT_TOTAL,withdrawAmountTotal);
    }


    /**
     * 减少余额总额、冻结金额总额
     * @param amount
     * @return
     */
    public Map<String, Double> decrBlockedBalanceTotalAndAccountBalanceTotal(double amount){
        Double blockedBalanceTotal = redisService.decrByFloat(AccountRedisKey.BLOCKED_BALANCE_TOTAL,amount);
        Double accountBalanceTotal = redisService.decrByFloat(AccountRedisKey.ACCOUNT_BALANCE_TOTAL,amount);
        return ImmutableMap.of(AccountRedisKey.BLOCKED_BALANCE_TOTAL,blockedBalanceTotal,AccountRedisKey.ACCOUNT_BALANCE_TOTAL,accountBalanceTotal);
    }

    /**
     * 减少余额总额、可提现金额总额
     * @param amount
     * @return
     */
    public Map<String, Double> decrAccountBalanceTotalAndWithdrawAmountTotal(double amount){
        Double accountBalanceTotal = redisService.decrByFloat(AccountRedisKey.ACCOUNT_BALANCE_TOTAL,amount);
        Double withdrawAmountTotal = redisService.decrByFloat(AccountRedisKey.WITHDRAW_AMOUNT_TOTAL,amount);
        return ImmutableMap.of(AccountRedisKey.ACCOUNT_BALANCE_TOTAL,accountBalanceTotal,AccountRedisKey.WITHDRAW_AMOUNT_TOTAL,withdrawAmountTotal);
    }

    /**
     * 增加冻结金额总额、减少可提现金额总额
     * @param amount
     * @return
     */
    public Map<String, Double> incrBlockedBalanceTotalAnddecrWithdrawAmountTotal(double amount){
        Double blockedBalanceTotal = redisService.incrByFloat(AccountRedisKey.BLOCKED_BALANCE_TOTAL,amount);
        Double withdrawAmountTotal = redisService.decrByFloat(AccountRedisKey.WITHDRAW_AMOUNT_TOTAL,amount);
        return ImmutableMap.of(AccountRedisKey.BLOCKED_BALANCE_TOTAL,blockedBalanceTotal,AccountRedisKey.WITHDRAW_AMOUNT_TOTAL,withdrawAmountTotal);
    }

    /**
     * 增加余额总额
     * @param amount
     * @return
     */
    public Map<String, Double> incrAccountBalanceTotal(double amount){
        Double accountBalanceTotal = redisService.incrByFloat(AccountRedisKey.ACCOUNT_BALANCE_TOTAL,amount);
        return ImmutableMap.of(AccountRedisKey.ACCOUNT_BALANCE_TOTAL,accountBalanceTotal);
    }

    /**
     * 增加冻结金额总额
     * @param amount
     * @return
     */
    public Map<String, Double> incrBlockedBalanceTotal(double amount){
        Double blockedBalanceTotal = redisService.incrByFloat(AccountRedisKey.BLOCKED_BALANCE_TOTAL,amount);
        return ImmutableMap.of(AccountRedisKey.BLOCKED_BALANCE_TOTAL,blockedBalanceTotal);
    }

    /**
     * 增加余额总额、冻结金额总额、可提现金额总额
     * @param accountBalanceTotal 余额总额
     * @param blockedBalanceTotal 冻结金额总额
     * @param withdrawAmountTotal 可提现金额总额
     * @return
     */
    public Boolean incrAccountBalanceTotalAndBlockedBalanceTotalAndWithdrawAmountTotal(Double accountBalanceTotal, Double blockedBalanceTotal,Double withdrawAmountTotal) {
        List list = ImmutableList.of(AccountRedisKey.ACCOUNT_BALANCE_TOTAL,AccountRedisKey.BLOCKED_BALANCE_TOTAL,AccountRedisKey.WITHDRAW_AMOUNT_TOTAL);
        redisService.del(list);
        accountBalanceTotal = redisService.incrByFloat(AccountRedisKey.ACCOUNT_BALANCE_TOTAL, accountBalanceTotal);
        blockedBalanceTotal = redisService.incrByFloat(AccountRedisKey.BLOCKED_BALANCE_TOTAL, blockedBalanceTotal);
        withdrawAmountTotal = redisService.incrByFloat(AccountRedisKey.WITHDRAW_AMOUNT_TOTAL, withdrawAmountTotal);
        //Map<String, Double> map = ImmutableMap.of(AccountRedisKey.ACCOUNT_BALANCE_TOTAL, accountBalanceTotal, AccountRedisKey.BLOCKED_BALANCE_TOTAL, blockedBalanceTotal, AccountRedisKey.WITHDRAW_AMOUNT_TOTAL, withdrawAmountTotal);
        return Boolean.TRUE;

    }

}
