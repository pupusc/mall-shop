package com.wanmi.sbc.order.api.request.returnorder;

import com.wanmi.sbc.order.bean.dto.ReturnOrderDTO;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**统一更新请求
 * @Author: zhanglingke
 * @Description:
 * @Date: 2018-11-16 16:39
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
public class ReturnOrderModifyRequest implements Serializable {

    private static final long serialVersionUID = -1076979847505660373L;

    /**
     * 新的退单
     */
    private ReturnOrderDTO returnOrderDTO;
}
