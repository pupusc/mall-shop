package com.fangdeng.server.dto;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.math.BigDecimal;


@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="BookRec")
public  class GoodsItemDTO implements Serializable {
    private static final long serialVersionUID = -3781688617709959387L;
    @XmlElement(name = "BookID")
    private String bookId;
    @XmlElement(name = "BookNum")
    private Integer bookNum;
    @XmlElement(name = "SourceSpbs")
    private String sourceSpbs;
    @XmlElement(name = "Status")
    private Integer status;
    @XmlElement(name = "Report")
    private String report;
    @XmlElement(name = "BookSendNum")
    private Integer bookSendNum;
    @XmlElement(name = "FixedPrice")
    private BigDecimal fixedPrice;
    @XmlElement(name = "Price")
    private BigDecimal price;
    @XmlElement(name = "CancelReason")
    private String cancelReason;
}
