package com.wanmi.sbc.message.pushUtil;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @program: sbc-micro-service
 * @description: Push参数
 * @create: 2020-01-06 19:22
 **/
@ApiModel
@Data
public class PushEntry {

    @ApiModelProperty(value = "ios设备token集合")
    private List<String> iosTokenList;

    @ApiModelProperty(value = "android设备token集合")
    private List<String> androidTokenList;

    @ApiModelProperty(value = "通知栏文字")
    private String ticker;

    @ApiModelProperty(value = "通知标题")
    private String title;

    @ApiModelProperty(value = "通知文本")
    private String text;

    @ApiModelProperty(value = "通知图片")
    private String image;

    @ApiModelProperty(value = "通知跳转路由")
    private String router;

    @ApiModelProperty(value = "发送时间（空则立即发送）")
    private LocalDateTime sendTime;

    /**
     * 服务器会根据这个标识避免重复发送
     * 有些情况下（例如网络异常）开发者可能会重复调用API导致
     * 消息多次下发到客户端。如果需要处理这种情况，可以考虑此参数。
     */
    @ApiModelProperty(value = "开发者对消息的唯一标识")
    private String outBizNo;
}