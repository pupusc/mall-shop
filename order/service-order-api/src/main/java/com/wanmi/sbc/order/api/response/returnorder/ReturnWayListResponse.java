package com.wanmi.sbc.order.api.response.returnorder;

import com.wanmi.sbc.order.bean.enums.ReturnWay;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 查询所有退货方式响应结构
 * Created by jinwei on 6/5/2017.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class ReturnWayListResponse implements Serializable {

    private static final long serialVersionUID = 4079600894275807085L;

    /**
     * 退货方式列表
     */
    @ApiModelProperty(value = "退货方式列表")
    private List<ReturnWay> returnWayList;
}
