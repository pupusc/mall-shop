package com.fangdeng.server.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.math.BigDecimal;


@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="PackRec")
public  class OrderPackItemDTO implements Serializable {
    private static final long serialVersionUID = -3205969819398353174L;
    @XmlElement(name = "BookID")
    private String bookId;
    @XmlElement(name = "BookNum")
    private Integer bookNum;
    @XmlElement(name = "SendNum")
    private Integer sendNum;
    @XmlElement(name = "STATUS")
    private Integer status;
    @XmlElement(name = "BOOKUUID")
    private String bookUUID;
    @XmlElement(name = "YSGSYH")
    private String postNumber;
    @XmlElement(name = "SHGS")
    private String post;
    @XmlElement(name = "SendTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private String postDate;

    private String sourceSpbs;
}