package com.fangdeng.server.client.request.bookuu;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
@XmlRootElement(name = "OrderCancelReq")
@XmlAccessorType(XmlAccessType.FIELD)
public class BookuuOrderCancelRequest implements Serializable {
    @XmlElement(name = "channelID")
    private String channelID;
    @XmlElement(name = "Sequence")
    private String sequence;
    @XmlElement(name = "TimeStamp")
    private String timeStamp;
    @ApiModelProperty("此参数为必填，1=>单品取消，2=>整单取消")
    @XmlElement(name = "type")
    private Integer type;
    @XmlElement(name = "orderid")
    private String orderId;
    @XmlElement(name = "bookid")
    private String bookId;
}
