package com.wanmi.sbc.goods.api.request.notice;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/10/26 2:31 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class NoticeProviderRequest implements Serializable {

    private Integer id;

    /**
     * 内容
     */
    private String content;

    /**
     * 开始时间
     */
    private LocalDateTime beginTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;
}
