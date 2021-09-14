package com.wanmi.sbc.goods.api.response.classify;

import lombok.Data;

import java.io.Serializable;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/9/14 11:35 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class ClassifySimpleProviderResponse implements Serializable {

    /**
     * 分类id
     */
    private Integer id;

    /**
     * 分类名
     */
    private String classifyName;
}
