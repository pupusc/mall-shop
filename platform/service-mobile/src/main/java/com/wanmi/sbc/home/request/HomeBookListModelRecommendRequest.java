package com.wanmi.sbc.home.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/10/20 10:38 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class HomeBookListModelRecommendRequest {

    /**
     * 书单模板类型 1 排行榜 2 书单 3 编辑推荐 4 专题 5 名家推荐
     */
    @NotNull
    private Integer businessType;
}
