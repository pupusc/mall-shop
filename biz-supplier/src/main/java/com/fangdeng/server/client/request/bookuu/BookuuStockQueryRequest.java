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
@XmlRootElement(name="BookReq")
@XmlAccessorType(XmlAccessType.FIELD)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookuuStockQueryRequest implements Serializable {

    @ApiModelProperty("时间戳，YYYYMMDDhhmmss")
    @XmlElement(name = "TimeStamp")
    protected String timeStamp;

    @XmlElement(name = "channelID")
    @ApiModelProperty("合作伙伴ID")
    protected String channelID;

    @XmlElement(name = "BookID")
    @ApiModelProperty("博库商品ID")
    private String BookID;

    @XmlElement(name = "Page")
    @ApiModelProperty("用上架日期时，此参数为必填，默认为1")
    protected Integer page;

    @XmlElement(name = "stime")
    @ApiModelProperty("上架日期开始，格式：2014-09-23（时间与ID，必填一项，起止时间间隔不大于2天）")
    protected String stime;

    @XmlElement(name = "etime")
    @ApiModelProperty("上架日期结束，格式：2014-09-23")
    protected String etime;










}