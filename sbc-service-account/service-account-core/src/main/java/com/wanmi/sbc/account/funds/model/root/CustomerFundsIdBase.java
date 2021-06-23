package com.wanmi.sbc.account.funds.model.root;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * 会员资金统计映射实体对象
 *
 * @author: yangzhen
 * @createDate: 2020/11/27 9:42
 * @version: 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerFundsIdBase implements Serializable {


    /**
     * customerId
     */
    private String customerId;


    /**
     * 主键
     */
    private String customerFundsId;

}
