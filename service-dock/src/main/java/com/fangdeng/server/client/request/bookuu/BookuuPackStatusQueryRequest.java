package com.fangdeng.server.client.request.bookuu;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.List;


@Data
@XmlRootElement(name="QueryOrderStatusReq")
@XmlAccessorType(XmlAccessType.FIELD)
public class BookuuPackStatusQueryRequest implements Serializable {


    @ApiModelProperty("时间戳，YYYYMMDDhhmmss")
    @JSONField(name="TimeStamp")
    @XmlElement(name = "TimeStamp")
    protected String timeStamp;

    @XmlElement(name = "channelID")
    @ApiModelProperty("合作伙伴ID")
    protected String channelID;

    @XmlElement(name="orderid")
    private String orderId;



}