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
@XmlRootElement(name = "OrderRsp")
public class BookuuOrderAddResponse implements Serializable {
    @XmlElement(name = "Sequence")
    private String sequence;
    @XmlElement(name = "Status")
    private Integer status;
    @XmlElement(name = "PayStatus")
    private Integer payStatus;
    @XmlElement(name = "orderID")
    private String orderID;
    @XmlElement(name = "price")
    private BigDecimal price;
}
