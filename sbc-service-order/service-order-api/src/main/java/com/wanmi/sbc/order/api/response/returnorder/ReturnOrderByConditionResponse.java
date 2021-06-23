package com.wanmi.sbc.order.api.response.returnorder;

import com.wanmi.sbc.order.bean.vo.ReturnOrderVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 根据动态条件查询退单列表请求结构
 * Created by jinwei on 6/5/2017.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class ReturnOrderByConditionResponse implements Serializable {

    private static final long serialVersionUID = 4079600894275807085L;

    /**
     * 退单列表
     */
    @ApiModelProperty(value = "退单列表")
    private List<ReturnOrderVO> returnOrderList;

}
