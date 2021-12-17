package com.fangdeng.server.client.response.bookuu;

import com.fangdeng.server.dto.GoodsItemDTO;
import com.fangdeng.server.dto.OrderPackItemDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;


@Data
@XmlRootElement(name = "QueryOrderPackStatusRsp")
@XmlAccessorType(XmlAccessType.FIELD)
public class BookuuPackStatusQueryResponse implements Serializable {

    private static final long serialVersionUID = 5755635183720991535L;
    @XmlElement(name = "StatusList")
    private List<OrderStatusDTO> statusDTOS;

    @Data
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlRootElement(name="StatusList")
    public static  class  OrderStatusDTO implements Serializable{
        private static final long serialVersionUID = -4913902300559869078L;
        @XmlElement(name = "PackRec")
        private List<OrderPackItemDTO> packRecs;

    }


}
