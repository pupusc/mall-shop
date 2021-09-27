package com.fangdeng.server.client.response.bookuu;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@XmlRootElement(name = "QueryOrderStatusRsp")
@XmlAccessorType(XmlAccessType.FIELD)
public class BookuuOrderStatusQueryResponse implements Serializable {

    @XmlElement(name = "StatusList")
    private List<OrderStatusDTO> statusDTOS;

    @Data
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlRootElement(name="StatusList")
    public static  class  OrderStatusDTO{
        @XmlElement(name = "OrderID")
        private String orderId;
        @XmlElement(name = "OrderStatus")
        private Integer orderStatus;
        @XmlElement(name = "Post")
        private String post;
        @XmlElement(name = "PostNumber")
        private String postNumber;
        @XmlElement(name = "PostDate")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
        private Date postDate;

    }
}
