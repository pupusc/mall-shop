package com.wanmi.sbc.classify.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * Description: 获取市场分类下的商品和书单
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/10/21 2:11 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class HomeClassifyGoodsAndBookListModelRequest {

    private int pageNum = 0;

    private int pageSize = 10;

    /**
     * 分类信息
     */
    @NotNull
    private Integer classifyId;
}
