package com.fangdeng.server.client.response.bookuu;

import com.fangdeng.server.dto.BookuuGoodsDTO;
import jdk.nashorn.internal.ir.annotations.Ignore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;

@Data
@XmlRootElement(name ="BookReq")
@XmlAccessorType(XmlAccessType.FIELD)
public class BookuuGoodsQueryResponse implements Serializable {

        @XmlElement(name = "channelID")
        private String channelID;

        @XmlElement(name = "TimeStamp")
        private String timeStamp;

        @XmlElement(name = "Count")
        private Integer count;
        @XmlElement(name = "BookList")
        private List<BookuuGoodsDTO> bookList;

        @Ignore
        private Integer flag = 0;

}
