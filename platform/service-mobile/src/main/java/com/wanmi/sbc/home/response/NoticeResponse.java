package com.wanmi.sbc.home.response;

import lombok.Data;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/10/26 6:00 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class NoticeResponse {

    /**
     * id
     */
    private Integer id;

    /**
     * 公告内容
     */
    private String content;
}
