package com.fangdeng.server.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.math.BigDecimal;


@Data
@XmlRootElement(name="BookList")
@XmlAccessorType(XmlAccessType.FIELD)
public class BookuuGoodsDTO implements Serializable {
    @XmlElement(name="bookid")
    private String bookId;
    @XmlElement(name="isbn")
    private String isbn;
    @XmlElement(name="store")
    private Integer store;
    @XmlElement(name="BC")
    private String BC;
    @XmlElement(name="YC")
    private String YC;
    @XmlElement(name="Publication")
    private String publication;
    @XmlElement(name="Printed")
    private String printed;
    @XmlElement(name="Category")
    private Integer category;
    @XmlElement(name="Publishing")
    private String publishing;
    @XmlElement(name="title")
    private String title;
    @XmlElement(name="Baseprice")
    private BigDecimal basePrice;
    @XmlElement(name="price")
    private BigDecimal price;
    @XmlElement(name="Pricing")
    private BigDecimal pricing;
    @XmlElement(name="author")
    private String author;
    @XmlElement(name="format")
    private String format;
    @XmlElement(name="Page")
    private Integer page;
    @XmlElement(name="guide")
    private String guide;
    @XmlElement(name="nrty")
    private String nrty;
    @XmlElement(name="jcy")
    private String jcy;
    @XmlElement(name="zzjj")
    private String zzjj;
    @XmlElement(name="xy")
    private String xy;
    @XmlElement(name="directory")
    private String directory;
    @XmlElement(name="picurl")
    private String picurl;
    @XmlElement(name="picurllarge")
    private String picurllarge;
    @XmlElement(name="bcy")
    private String bcy;
    @XmlElement(name="fd")
    private String fd;
    @XmlElement(name="xqt")
    private String xqt;
}
