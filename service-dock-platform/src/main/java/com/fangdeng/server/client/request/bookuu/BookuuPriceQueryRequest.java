package com.fangdeng.server.client.request.bookuu;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@Data
@XmlRootElement(name = "UserPriceReq")
@XmlAccessorType(XmlAccessType.FIELD)
public class BookuuPriceQueryRequest implements Serializable {


    @ApiModelProperty("请求序列号")
    @XmlElement(name = "Sequence")
    private String sequence;

    @ApiModelProperty("博库商品ID, 逗号分割")
    @XmlElement(name = "BookID")
    private String bookID;

    @ApiModelProperty("书号ISBN,逗号分割")
    @XmlElement(name = "ISBN")
    private String isbn;

    @ApiModelProperty("时间戳，YYYYMMDDhhmmss")
    @XmlElement(name = "TimeStamp")
    protected String timeStamp;

    @XmlElement(name = "channelID")
    @ApiModelProperty("合作伙伴ID")
    protected String channelID;


}