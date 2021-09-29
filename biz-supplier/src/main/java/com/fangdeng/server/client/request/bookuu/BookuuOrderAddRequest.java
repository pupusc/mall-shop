package com.fangdeng.server.client.request.bookuu;

import lombok.Data;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
@XmlRootElement(name = "OrderReq")
@XmlAccessorType(XmlAccessType.FIELD)
public class BookuuOrderAddRequest implements Serializable {
    @XmlElement(name = "channelID")
    private String channelID;
    @XmlElement(name = "Sequence")
    private String sequence;
    @XmlElement(name = "ShopName")
    private String shopName;
    @XmlElement(name = "TimeStamp")
    private String timeStamp;
    @XmlElement(name = "RecvINFO")
    private ReceiveInfo recvInfo;

    @XmlElement(name = "BookRec")
    @XmlElementWrapper(name = "ProductList")
    private List<Product> productList;

    @Data
    @XmlRootElement(name = "RecvINFO")
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class ReceiveInfo {
        @XmlElement(name = "AddressLevel1")
        private String addressLevel1;
        @XmlElement(name = "AddressLevel2")
        private String addressLevel2;
        @XmlElement(name = "AddressLevel3")
        private String addressLevel3;
        @XmlElement(name = "TotalAddress")
        private String totalAddress;
        @XmlElement(name = "ZipCode")
        private String zipCode;
        @XmlElement(name = "PostFee")
        private BigDecimal postFee;
        @XmlElement(name = "RecvName")
        private String recvName;
        @XmlElement(name = "RecvPhone")
        private String recvPhone;
        @XmlElement(name = "RecvMobile")
        private String recvMobile;
    }

    @XmlRootElement(name = "BookRec")
    @Data
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Product {
        @XmlElement(name = "BookID")
        private String bookID;
        @XmlElement(name = "BookNum")
        private Integer bookNum;
        @XmlElement(name = "UnitPrice")
        private BigDecimal unitPrice;
    }

}
