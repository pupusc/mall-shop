package com.wanmi.sbc.account.api.response.finance.record;

import com.wanmi.sbc.account.bean.vo.SettlementVO;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 最近一个结算单
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
public class SettlementLastResponse extends SettlementVO implements Serializable {

    private static final long serialVersionUID = -8841546927742638134L;
}
