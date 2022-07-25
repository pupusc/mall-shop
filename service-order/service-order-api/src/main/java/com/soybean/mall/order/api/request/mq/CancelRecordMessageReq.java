package com.soybean.mall.order.api.request.mq;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/7/26 2:08 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class CancelRecordMessageReq {

    private Integer fromId;

    private LocalDateTime beginTime;

    private int pageSize;
}
