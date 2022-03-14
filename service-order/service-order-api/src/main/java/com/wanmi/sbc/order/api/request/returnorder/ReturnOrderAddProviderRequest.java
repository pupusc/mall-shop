package com.wanmi.sbc.order.api.request.returnorder;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Description: 新增售后单
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/12/23 7:55 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class ReturnOrderAddProviderRequest implements Serializable {

    /**
     * 退单号
     */
    private String returnOrderId;

    /**
     *  主单号
     */
    private String tId;

    /**
     * 子单号
     */
    private List<String> ptId;

    /**
     * 樊登用户id
     */
    private String fanDengUserNo;


    /**
     * 退货商品id列表信息
     */
    private List<String> skuIdList;
}
