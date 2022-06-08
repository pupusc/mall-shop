package com.soybean.elastic.api.resp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/6/9 2:00 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EsCommonPageResp<S> implements Serializable {

    /**
     * 总数量
     */
    private Long total;

    /**
     * 内容信息
     */
    private S content;
}
