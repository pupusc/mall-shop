package com.wanmi.sbc.customer.api.request.levelrights;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.customer.api.request.CustomerBaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>会员等级权益表删除请求参数</p>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CustomerLevelRightsDeleteRequest extends CustomerBaseRequest {
    private static final long serialVersionUID = 1L;

    /**
     * 批量查询-主键idList
     */
    private List<Integer> rightsIdList;

    /**
     * 主键id
     */
    private Integer rightsId;

    /**
     * 删除标识 0:未删除1:已删除
     */
    private DeleteFlag delFlag;

    /**
     * 删除时间
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime delTime;

    /**
     * 删除人
     */
    private String delPerson;

}