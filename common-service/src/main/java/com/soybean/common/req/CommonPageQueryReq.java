package com.soybean.common.req;

import lombok.Data;

import java.io.Serializable;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/6/9 1:48 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class CommonPageQueryReq implements Serializable {

    private int pageNum = 0;

    private int pageSize = 10;
}
