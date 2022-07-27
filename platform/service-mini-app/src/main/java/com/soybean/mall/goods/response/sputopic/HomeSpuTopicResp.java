package com.soybean.mall.goods.response.sputopic;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description: 栏目商品列表
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/7/13 7:18 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HomeSpuTopicResp<S, V> {

    /**
     * 标题
     */
    private S title;

    private V content;
}
