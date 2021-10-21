package com.fangdeng.server.client.response.bookuu;

import com.fangdeng.server.dto.BookuuGoodsDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@XmlRootElement(name = "BookReq")
@XmlAccessorType(XmlAccessType.FIELD)
public class BookuuStockQueryResponse implements Serializable {

    @XmlElement(name = "channelID")
    private String channelID;

    @XmlElement(name = "TimeStamp")
    private String timeStamp;

    @XmlElement(name = "Count")
    private Integer count;

    @XmlElement(name = "BookList")
    private List<BookuuStock> bookList;

    @Data
    @XmlRootElement(name = "BookList")
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class BookuuStock implements Serializable{
        @XmlElement(name = "bookid")
        private String bookId;
        @XmlElement(name = "zjtbkcsj")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
        private String zjtbkcsj;
        @XmlElement(name = "Stock")
        private Integer stock;
    }


}
