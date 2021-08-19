package com.wanmi.sbc.crm.api.request.customgroup;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: zgl
 * \* Date: 2019-11-30
 * \* Time: 11:17
 * \* To change this template use File | Settings | File Templates.
 * \* Description:
 * \
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomGroupCheckTagRequest {

    @NotNull
    private Long tagId;
}
