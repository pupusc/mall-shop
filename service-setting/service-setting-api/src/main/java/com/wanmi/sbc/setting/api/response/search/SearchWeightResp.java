package com.wanmi.sbc.setting.api.response.search;

import lombok.Data;

import java.io.Serializable;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/6/7 2:42 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class SearchWeightResp implements Serializable {

//    private Integer id;

//    private String name;

    private String weightKey;

    private String weightValue;
}
