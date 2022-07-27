package com.soybean.mall.order.api.request.record;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/7/22 2:48 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class OrderGiftRecordMqReq {

    @NotBlank
    private String message;
}
