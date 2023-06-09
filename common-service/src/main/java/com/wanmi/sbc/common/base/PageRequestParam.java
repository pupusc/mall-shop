package com.wanmi.sbc.common.base;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.io.Serializable;

/**
 * 前端分页请求参数
 * Created by zhangjin on 15/12/10.
 */
@ApiModel
@Data
public class PageRequestParam implements Serializable {

    //当前页
    @ApiModelProperty(value = "当前页")
    private int pageNum = 0;

    //分页
    @ApiModelProperty(value = "分页")
    private int pageSize = 10;

    //开始
    @ApiModelProperty(value = "开始")
    private int start;

    public int getStart() {
        return (pageNum) * pageSize;
    }

    public Pageable getRequest() {
        return PageRequest.of(pageNum, pageSize);
    }
}
