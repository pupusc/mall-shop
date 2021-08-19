package com.wanmi.sbc.order.api.response.linkedmall;

import com.wanmi.sbc.order.bean.vo.LinkedMallReasonVO;
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
public class LinkedMallReturnReasonResponse implements Serializable {

    private static final long serialVersionUID = 4079600894275807085L;

    /**
     * 退货原因列表
     */
    @ApiModelProperty(value = "退货原因列表")
    private List<LinkedMallReasonVO> reasonList;

    /**
     * 退货说明
     */
    @ApiModelProperty(value = "退货说明")
    private String description;

    /**
     * 退单本身的附件
     */
    @ApiModelProperty(value = "退单本身的附件")
    private List<String> images;
}
