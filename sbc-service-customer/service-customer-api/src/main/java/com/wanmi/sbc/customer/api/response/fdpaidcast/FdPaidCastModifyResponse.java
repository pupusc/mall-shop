package com.wanmi.sbc.customer.api.response.fdpaidcast;

import com.wanmi.sbc.customer.bean.vo.FdPaidCastVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>樊登付费类型 映射商城付费类型修改结果</p>
 * @author tzx
 * @date 2021-01-29 14:13:37
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FdPaidCastModifyResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 已修改的樊登付费类型 映射商城付费类型信息
     */
    @ApiModelProperty(value = "已修改的樊登付费类型 映射商城付费类型信息")
    private FdPaidCastVO fdPaidCastVO;
}
