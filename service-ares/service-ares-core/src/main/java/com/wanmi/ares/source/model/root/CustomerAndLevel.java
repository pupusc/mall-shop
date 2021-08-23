package com.wanmi.ares.source.model.root;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.ms.util.CustomLocalDateDeserializer;
import com.wanmi.ms.util.CustomLocalDateSerializer;
import com.wanmi.ares.enums.CustomerType;
import com.wanmi.ares.source.model.root.base.BaseData;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDate;

/**
 * 客户和等级关系表
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
public class CustomerAndLevel extends BaseData{

    private static final long serialVersionUID = 1791727470875246381L;

    /**
     * 用户标识
     */
    private String customerId;

    /**
     * 店铺标识
     */
    private String storeId;

    /**
     * 商家标识
     */
    private String companyInfoId;

    /**
     * 客户等级标识
     */
    private Long customerLevelId;

    /**
     * 负责的业务员标识
     */
    private String employeeId;

    /**
     * 关系类型(0:平台绑定客户,1:商家客户)
     */
    private CustomerType customerType;

    /**
     * 绑定时间
     */
    @JsonSerialize(using = CustomLocalDateSerializer.class)
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    private LocalDate bindTime;
}
