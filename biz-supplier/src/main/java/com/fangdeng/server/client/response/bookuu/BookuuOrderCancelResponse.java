package com.fangdeng.server.client.response.bookuu;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "OrderCancelRsp")
public class BookuuOrderCancelResponse implements Serializable {
    @XmlElement(name = "Msg")
    private String msg;
    @XmlElement(name = "Status")
    private Integer status;
    @XmlElement(name = "ErrorMsg")
    private String errorMsg;
    @XmlElement(name = "orderID")
    private String orderID;
}
