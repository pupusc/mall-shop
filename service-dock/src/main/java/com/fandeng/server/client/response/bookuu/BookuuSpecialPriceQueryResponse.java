package com.fandeng.server.client.response.bookuu;

import lombok.Data;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
@XmlRootElement(name = "SpecialGiveRsp")
@XmlAccessorType(XmlAccessType.FIELD)
public class BookuuSpecialPriceQueryResponse implements Serializable {

    @XmlElement(name = "Count")
    private Integer count;

    @XmlElement(name = "MaxPages")
    private Integer maxPages;

    @XmlElement(name = "BookItem")
    @XmlElementWrapper(name="BookList")
    private List<BookuuSpecialPrice> bookList;

    @Data
    @XmlRootElement(name = "BookItem")
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class BookuuSpecialPrice {
        @XmlElement(name = "BookId")
        private String bookId;
        @XmlElement(name = "SpecialPrice")
        private BigDecimal specialPrice;
        @XmlElement(name = "StartTime")
        private String startTime;
        @XmlElement(name = "EndTime")
        private String endTime;
    }


}