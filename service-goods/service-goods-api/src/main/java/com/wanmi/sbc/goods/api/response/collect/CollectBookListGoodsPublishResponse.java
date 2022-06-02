package com.wanmi.sbc.goods.api.response.collect;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import lombok.Data;


import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/6/3 1:54 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class CollectBookListGoodsPublishResponse implements Serializable {


    private Integer id;


    /**
     * 书单模板id或者书单类目id
     */
    private Integer bookListId;

    /**
     *
     */
    private String spuId;

    private String spuNo;

    private String skuId;

    private String skuNo;

    private String erpGoodsNo;

    private String erpGoodsInfoNo;

    private Integer orderNum;

    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createTime;

    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime updateTime;

    private Integer delFlag;
}
