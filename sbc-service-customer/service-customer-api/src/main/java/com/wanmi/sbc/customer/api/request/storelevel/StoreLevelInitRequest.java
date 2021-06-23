package com.wanmi.sbc.customer.api.request.storelevel;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.customer.api.request.CustomerBaseRequest;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * @author yang
 * @since 2019/3/14
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreLevelInitRequest extends CustomerBaseRequest {

    private static final long serialVersionUID = -2963773915256833234L;

    /**
     * 店铺编号
     */
    @NotNull
    private Long storeId;

    /**
     * 创建人
     */
    private String createPerson;

    /**
     * 创建时间
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createTime;
}
