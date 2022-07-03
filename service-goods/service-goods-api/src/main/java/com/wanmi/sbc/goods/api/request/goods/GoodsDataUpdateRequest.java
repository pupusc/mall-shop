package com.wanmi.sbc.goods.api.request.goods;

import lombok.Data;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/6/25 1:38 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class GoodsDataUpdateRequest {


    private String goodsNo;

    /**
     * 无背景图地址
     */
    private String unbackgroundImg;
}
