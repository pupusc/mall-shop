package com.wanmi.sbc.goods.api.response.index;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/7/12 12:54 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/

@Data
public class NormalModuleResp {


    private Integer id;

    /**
     * 名称
     */
    private String name;

    /**
     * 开始时间
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime beginTime;

    /**
     * 结束时间
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime endTime;

    /**
     * 0未启用 1启用 {@link com.wanmi.sbc.goods.bean.enums.PublishState}
     */
    private Integer publishState;

    /**
     * 0未开始 1进行中 2结束
     * {@link com.wanmi.sbc.goods.api.enums.StateEnum}
     *
     */
    private Integer status;

}
