package com.wanmi.sbc.index.request;

import lombok.Data;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/10/27 1:54 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class ImagePageRequest {

    private int pageNum = 0;

    private int pageSize = 10;

    /**
     * id
     */
    private Integer id;

    /**
     * 名称
     */
    private String name;

    /**
     *  0未开始 1进行中 2 已结束
     */
    private Integer status;

    /**
     * 启用状态 0未启用 1启用
     */
    private Integer publishState;

}
