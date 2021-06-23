package com.wanmi.sbc.order.api.response.returnorder;

import com.wanmi.sbc.order.bean.vo.ReturnOrderVO;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 根据退单id查询可退商品数响应结构
 * Created by jinwei on 6/5/2017.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel
public class CanReturnItemNumByIdResponse extends ReturnOrderVO {

    private static final long serialVersionUID = 4079600894275807085L;

}
