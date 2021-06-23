package com.wanmi.ares.request.mq;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.ares.base.BaseMqRequest;
import com.wanmi.ares.enums.CheckState;
import com.wanmi.ms.util.CustomLocalDateDeserializer;
import com.wanmi.ms.util.CustomLocalDateSerializer;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDate;

/**
 * 客户审核信息
 * Created by bail on 2017/10/16
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
public class CustomerCheckRequest extends BaseMqRequest {

    private static final long serialVersionUID = -5280440228769573495L;

    /**
     * 账户的审核状态 0:待审核 1:已审核通过 2:审核未通过
     */
    private CheckState checkState;

    /**
     * 审核日期
     */
    @JsonSerialize(using = CustomLocalDateSerializer.class)
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    private LocalDate checkDate;

    /**
     * 绑定业务员日期
     */
    @JsonSerialize(using = CustomLocalDateSerializer.class)
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    private LocalDate bindDate;

}
