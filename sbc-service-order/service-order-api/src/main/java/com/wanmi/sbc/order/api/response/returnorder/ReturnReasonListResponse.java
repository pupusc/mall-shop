package com.wanmi.sbc.order.api.response.returnorder;

import com.wanmi.sbc.order.bean.enums.ReturnReason;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 查询所有退货原因响应结构
 * Created by jinwei on 6/5/2017.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class ReturnReasonListResponse implements Serializable {

    private static final long serialVersionUID = 4079600894275807085L;

    /**
     * 退货原因列表
     */
    @ApiModelProperty(value = "退货原因列表")
    private List<ReturnReason> returnReasonList;
}
