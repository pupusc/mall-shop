package com.wanmi.sbc.booklistmodel.response;

import lombok.Data;

/**
 * Description: 商品列表简化对象
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/9/10 3:56 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class SortGoodsCustomResponse extends GoodsCustomResponse{

    /**
     * 排序
     */
    private Integer sort;


}
