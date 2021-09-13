package com.wanmi.sbc.elastic.api.request.goods;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/9/13 11:29 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class EsGoodsCustomProviderRequest implements Serializable {

    private int pageNum = 1;

    private int pageSize = 20;

    /**
     * 商品列表
     */
    private List<String> goodIdList;

    /**
     * 知识顾问专享 0:不是 ，1：是
     */
    private Integer cpsSpecial;

}
