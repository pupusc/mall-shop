package com.wanmi.ares.source.model.root;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.ares.source.model.root.base.BaseData;
import com.wanmi.ms.util.CustomLocalDateDeserializer;
import com.wanmi.ms.util.CustomLocalDateSerializer;
import lombok.*;

import java.time.LocalDate;

/**
 * 店铺
 * Created by hht on 2018/01/10.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
public class Store extends BaseData{

    private static final long serialVersionUID = 8386464364127088917L;

    /**
     * 商家Id
     */
    private String companyInfoId;

    /**
     * 店铺名称
     */
    private String storeName;

    /**
     * 商家名称
     */
    private String supplierName;

    /**
     * 店铺状态 0、开启 1、关店
     */
    private Integer storeState;

    /**
     * 签约开始日期
     */
    @JsonSerialize(using = CustomLocalDateSerializer.class)
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    private LocalDate contractStartDate;

    /**
     * 签约结束日期
     */
    @JsonSerialize(using = CustomLocalDateSerializer.class)
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    private LocalDate contractEndDate;

    /**
     * 商家类型 0、平台自营 1、第三方商家
     */
    private Integer companyType;

    /**
     * 审核状态 0、待审核 1、已审核 2、审核未通过
     */
    private Integer auditState;

    /**
     * 申请入驻时间
     */
    private LocalDate applyEnterTime;
}
