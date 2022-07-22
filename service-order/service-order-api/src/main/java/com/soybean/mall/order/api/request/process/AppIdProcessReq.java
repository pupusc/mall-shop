package com.soybean.mall.order.api.request.process;

import lombok.Data;

import java.util.List;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/7/22 6:24 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class AppIdProcessReq {

    private List<String> orderIds;


    private Integer maxId;

    private int size = 100;
}
