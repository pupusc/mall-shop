package com.wanmi.sbc.erp.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/3/23 8:00 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class ERPBatch implements Serializable {

    /**
     * 库存数量
     */
    @JsonProperty("qty")
    private int qty;

    /**
     * 可配数量
     */
    @JsonProperty("available_qty")
    private int availableQty;

    /**
     * 批次号
     */
    @JsonProperty("batch_no")
    private String batchNo;

    /**
     * 生产日期
     */
    @JsonProperty("production_date")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime productionDate;

    /**
     * 有效日期
     */
    @JsonProperty("available_date")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime availableDate;

    /**
     * 入库日期
     */
    @JsonProperty("warehousing_date")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime warehousingDate;

    /**
     * 保质期
     */
    @JsonProperty("shelf_life")
    private int shelfLife;
}
