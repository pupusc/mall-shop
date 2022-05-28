package com.soybean.mall.wx.mini.common.bean.request;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Data
public class UrlschemeRequest implements Serializable {


    @JSONField(name = "expire_type")
    private Integer expireType=0;
    /**
     * 失效的天数
     */
    @JSONField(name = "expire_interval")
    private Integer expireInterval;

    /**
     * 到期失效的 scheme 码的失效时间
     */
    @JSONField(name="expire_time")
    private Integer expireTime;
    /**
     *
     */
    @JSONField(name="jump_wxa")
    private Wxa wxa;

    @Data
    public static class Wxa{
        @NotEmpty
        private String path;

        private String query;
        @JSONField(name="env_version")
        private String env = "release";
    }

}
