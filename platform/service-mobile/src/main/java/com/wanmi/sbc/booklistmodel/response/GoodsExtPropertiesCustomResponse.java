package com.wanmi.sbc.booklistmodel.response;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/9/18 2:00 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class GoodsExtPropertiesCustomResponse {

    /**
     * 作者
     */
    private String author;

    /**
     * 出版商
     */
    private String publisher;

    /**
     * 定价
     */
    private BigDecimal price;
}
