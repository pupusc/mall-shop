package com.wanmi.sbc.account.api.request.finance.record;

import com.wanmi.sbc.account.bean.dto.SettlementDTO;
import io.swagger.annotations.ApiModel;
import lombok.*;

import java.io.Serializable;

/**
 * <p>单条结算单新增信息request</p>
 * Created by daiyitian on 2018-10-13-下午6:29.
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
public class SettlementAddRequest extends SettlementDTO implements Serializable {

    private static final long serialVersionUID = -4425818145487669386L;
}
