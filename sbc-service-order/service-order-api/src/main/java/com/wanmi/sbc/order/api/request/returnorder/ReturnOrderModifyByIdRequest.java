package com.wanmi.sbc.order.api.request.returnorder;

import com.wanmi.sbc.order.bean.dto.ReturnOrderDTO;
import io.swagger.annotations.ApiModel;
import lombok.*;

/**
 * 根据客户id更新退单请求结构
 * @Author: daiyitian
 * @Description:
 * @Date: 2018-11-16 16:39
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel
public class ReturnOrderModifyByIdRequest extends ReturnOrderDTO {

    private static final long serialVersionUID = -1076979847505660373L;
}
