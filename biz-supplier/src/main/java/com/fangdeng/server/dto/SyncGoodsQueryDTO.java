package com.fangdeng.server.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SyncGoodsQueryDTO {

    @ApiModelProperty("用上架日期时，此参数为必填，默认为1")
    private Integer page;

    @ApiModelProperty("上架日期开始，格式：2014-09-23（时间与ID，必填一项，起止时间间隔不大于5天）")
    private String stime;

    @ApiModelProperty("上架日期结束，格式：2014-09-23")
    private String etime;

    private String bookId;

    private Boolean isAllSync = false;

}