package com.fangdeng.server.client.response.bookuu;

import com.fangdeng.server.dto.GoodsItemDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@XmlRootElement(name = "QueryOrderStatusRsp")
@XmlAccessorType(XmlAccessType.FIELD)
public class BookuuOrderStatusQueryResponse implements Serializable {

    private static final long serialVersionUID = 114467375725045694L;
    @XmlElement(name = "StatusList")
    private List<OrderStatusDTO> statusDTOS;

    @Data
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlRootElement(name="StatusList")
    public static  class  OrderStatusDTO implements Serializable{
        private static final long serialVersionUID = -2448368164865875340L;
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
        private String postDate;
        @XmlElement(name = "Unpacking")
        private Integer unpacking;
        @XmlElement(name = "BookRec")
        private List<GoodsItemDTO> bookRecs;

    }


}
