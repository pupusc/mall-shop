package com.wanmi.ares.request.mq;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.ares.enums.CheckState;
import com.wanmi.ms.util.CustomLocalDateDeserializer;
import com.wanmi.ms.util.CustomLocalDateSerializer;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDate;

/**
 * 客户添加/注册信息
 * Created by bail on 2017/10/16
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
public class CustomerAddRequest extends CustomerBaseRequest {

    private static final long serialVersionUID = -2642066527599645406L;

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

    /**
     * 是否boss添加 true:boss添加 false:注册
     */
    private boolean isBoss;

}
