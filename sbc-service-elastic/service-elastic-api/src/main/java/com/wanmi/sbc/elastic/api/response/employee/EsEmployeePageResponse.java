package com.wanmi.sbc.elastic.api.response.employee;

import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.elastic.bean.vo.customer.EsEmployeePageVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EsEmployeePageResponse implements Serializable {

    @ApiModelProperty(value = "业务员列表")
    private MicroServicePage<EsEmployeePageVO> employeePageVOPage;
}
