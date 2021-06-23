package com.wanmi.sbc.account.funds.repository;

import com.wanmi.sbc.account.bean.enums.FundsType;
import com.wanmi.sbc.account.funds.model.root.CustomerFundsDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 会员资金明细-数据库交互层
 * @author: Geek Wang
 * @createDate: 2019/2/19 9:31
 * @version: 1.0
 */
@Repository
public interface CustomerFundsDetailRepository extends JpaRepository<CustomerFundsDetail, String>,JpaSpecificationExecutor<CustomerFundsDetail> {

    @Query("select sum(receiptPaymentAmount) from CustomerFundsDetail where customerId = ?1 and fundsType in (?2) and createTime >= ?3 and createTime <= ?4")
    BigDecimal getBalanceChange(String customerId, List<FundsType> fundsTypeList, LocalDateTime startTime, LocalDateTime endTime);
}
