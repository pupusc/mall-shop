package com.fangdeng.server.client.request.bookuu;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

@Data
@XmlRootElement(name="BookReq")
@XmlAccessorType(XmlAccessType.FIELD)
public class BookuuGoodsQueryRequest implements Serializable {


        @ApiModelProperty("时间戳，YYYYMMDDhhmmss")
        @JSONField(name="TimeStamp")
        @XmlElement(name = "TimeStamp")
        protected String timeStamp;

        @XmlElement(name = "channelID")
        @ApiModelProperty("合作伙伴ID")
        protected String channelID;

        @XmlElement(name = "Page")
        @ApiModelProperty("用上架日期时，此参数为必填，默认为1")
        @JSONField(name="Page")
        protected Integer page;

        @XmlElement(name = "stime")
        @ApiModelProperty("上架日期开始，格式：2014-09-23（时间与ID，必填一项，起止时间间隔不大于5天）")
        protected String stime;

        @XmlElement(name = "etime")
        @ApiModelProperty("上架日期结束，格式：2014-09-23")
        protected String etime;

        @XmlElement(name ="id")
        @ApiModelProperty("博库商品ID")
        protected String id;

        @ApiModelProperty("书号ISBN")
        @JSONField(name="ISBN")
        @XmlElement(name ="ISBN")
        protected String isbn;



}
