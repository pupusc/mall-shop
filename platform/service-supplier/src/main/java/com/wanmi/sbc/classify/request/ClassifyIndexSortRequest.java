package com.wanmi.sbc.classify.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/11/17 1:44 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class ClassifyIndexSortRequest {

    /**
     * id
     */
    @NotNull
    private Integer id;

    /**
     * 是否首页展示 0否 1是
     */
    @NotNull(message = "是否首页展示")
    private Integer hasShowIndex;


    /**
     * 首页展示顺序
     */
    @NotNull(message = "首页展示顺序")
    private Integer indexOrderNum;
}
