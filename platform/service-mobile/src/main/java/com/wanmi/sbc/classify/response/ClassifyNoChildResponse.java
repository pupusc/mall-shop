package com.wanmi.sbc.classify.response;

import lombok.Data;

/**
 * Description: 分类
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/10/21 2:03 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class ClassifyNoChildResponse {

    /**
     * 分类id
     */
    private Integer id;

    /**
     * 分类名
     */
    private String classifyName;
}
