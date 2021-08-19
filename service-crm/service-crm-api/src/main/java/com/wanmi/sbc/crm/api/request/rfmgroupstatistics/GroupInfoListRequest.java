package com.wanmi.sbc.crm.api.request.rfmgroupstatistics;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassName GroupInfoListRequest
 * @Description TODO
 * @Author lvzhenwei
 * @Date 2020/1/17 13:37
 **/
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupInfoListRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 分群名称
     */
    @ApiModelProperty(value = "分群名称")
    private String groupName;

    /**
     * 返回列表长度
     */
    @ApiModelProperty(value = "返回列表长度")
    private Integer limit;
}
