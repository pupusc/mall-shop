package com.fangdeng.server.client.response.bookuu;

import com.alibaba.fastjson.annotation.JSONField;
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
import java.util.List;

@Data
@XmlRootElement(name = "UserPriceRsp")
@XmlAccessorType(XmlAccessType.FIELD)
public class BookuuPriceQueryResponse implements Serializable {


    @XmlElement(name = "ErrorReason")
    private Integer errorReason;

    @XmlElement(name = "Count")
    private Integer count;

    @XmlElement(name = "PriceList")
    private List<BookuuPrice> priceList;


    @Data
    @XmlRootElement(name = "PriceList")
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class BookuuPrice {
        @XmlElement(name = "BookID")
        private String bookID;
        @XmlElement(name = "ISBN")
        private String isbn;
        @XmlElement(name = "Price")
        private BigDecimal price;
        @XmlElement(name = "SellPrice")
        private BigDecimal sellPrice;
    }


}
