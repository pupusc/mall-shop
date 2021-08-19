package com.wanmi.sbc.account.api.request.finance.record;

import com.wanmi.sbc.account.bean.enums.SettleStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <p>批量修改结算单状态request</p>
 * Created by daiyitian on 2018-10-13-下午6:29.
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SettlementBatchModifyStatusRequest extends AccountBaseRequest {

    private static final long serialVersionUID = -3013009710823545987L;

    /**
     * 多个结算编号
     */
    @ApiModelProperty(value = "多个结算编号")
    @NotEmpty
    private List<Long> settleIdList;

    /**
     * 结算状态 {@link SettleStatus}
     */
    @ApiModelProperty(value = "结算状态")
    @NotNull
    private SettleStatus status;

}
