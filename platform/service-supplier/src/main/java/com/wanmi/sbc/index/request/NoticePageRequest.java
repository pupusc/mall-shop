package com.wanmi.sbc.index.request;

import lombok.Data;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/10/27 2:10 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class NoticePageRequest {


    private Integer pageNum = 0;

    private Integer pageSize = 10;

    /**
     * 发布状态 0未启用 1启用
     */
    private Integer publishState;

}
