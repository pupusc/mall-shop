package com.wanmi.sbc.goods.api.response.notice;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/10/26 3:52 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class NoticeProviderResponse implements Serializable {

    /**
     * id
     */
    private Integer id;

    /**
     * 公告内容
     */
    private String content;

    /**
     * 公告有效开始时间
     */
    private LocalDateTime beginTime;


    /**
     * 结束时间
     */
    private LocalDateTime endTime;
}
