package com.soybean.common.resp;

import lombok.Data;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/10/21 1:01 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class BaseFixedAddressResp {

    /**
     * 省份id
     */
    private String provinceId;

    /**
     * 城市 id
     */
    private String cityId;

}
