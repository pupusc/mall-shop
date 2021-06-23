package com.wanmi.sbc.account.api.response.customerdrawcash;

import com.wanmi.sbc.account.bean.vo.CustomerDrawCashVO;
import com.wanmi.sbc.common.base.MicroServicePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * <p>会员提现管理导出结果</p>
 * @author of2975
 * @date 2019-04-30 11:22:24
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDrawCashExportResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 会员提现管理导出结果
     */
    @ApiModelProperty(value = "会员提现管理导出结果")
    private MicroServicePage<CustomerDrawCashVO> customerDrawCashVOPage;
}
