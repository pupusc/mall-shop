package com.soybean.mall.miniapp.image.request;

import lombok.Data;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/5/20 2:03 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class WxImagePageRequest {

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
