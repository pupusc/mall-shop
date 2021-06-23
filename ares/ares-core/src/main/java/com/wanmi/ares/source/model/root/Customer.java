package com.wanmi.ares.source.model.root;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.ms.util.CustomLocalDateDeserializer;
import com.wanmi.ms.util.CustomLocalDateSerializer;
import com.wanmi.ares.enums.CheckState;
import com.wanmi.ares.source.model.root.base.BaseData;
import lombok.*;

import java.time.LocalDate;

/**
 * 客户基础信息
 * Created by sunkun on 2017/9/19.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
//@Document(indexName = EsConstants.ES_INDEX_BASIC, type = EsConstants.ES_TYPE_CUSTOMER)
@ToString(callSuper = true)
public class Customer extends BaseData{

    private static final long serialVersionUID = -8589285576638132475L;

    /**
     * 会员登录账号|手机号
     */
    private String account;

    /**
     * 客户级别
     */
    private String levelId;

    /**
     * 会员名称
     */
    private String name;

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
     * 市id
     */
    private String cityId;

    /**
     * 是否boss添加 true:boss添加 false:注册
     */
    private boolean isBoss;

    /**
     * 业务员信息ID
     */
    private String employeeId;

    /**
     * 商家id
     */
    private String companyId;

    /**
     * 绑定日期
     */
    @JsonSerialize(using = CustomLocalDateSerializer.class)
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
//    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "yyyy-MM-dd", index = false)
    private LocalDate bindDate;

}
