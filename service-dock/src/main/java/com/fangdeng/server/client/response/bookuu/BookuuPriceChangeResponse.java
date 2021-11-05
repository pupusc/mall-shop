package com.fangdeng.server.client.response.bookuu;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@XmlRootElement(name = "PriceChangeRsp")
@XmlAccessorType(XmlAccessType.FIELD)
public class BookuuPriceChangeResponse implements Serializable {


    @XmlElement(name = "CurrPage")
    private Integer currPage;

    @XmlElement(name = "MaxPages")
    private Integer maxPages;

    @XmlElement(name = "Count")
    private Integer count;

    @XmlElement(name = "BookItem")
    @XmlElementWrapper(name="BookList")
    private List<BookItem> bookList;




    @Data
    @XmlRootElement(name="BookItem")
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class BookItem {
        @XmlElement(name = "BookId")
        private String bookId;
        @XmlElement(name = "ChangeTime")
        private String changeTime;
    }


}
