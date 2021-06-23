package com.wanmi.sbc.pay.model.root;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 网关配置
 * Created by sunkun on 2017/8/3.
 */
@Data
@Entity
@Table(name = "pay_gateway_config")
@Accessors(chain = true)
public class PayGatewayConfig implements Serializable {

    private static final long serialVersionUID = -3080413392183116568L;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @JsonManagedReference
    @JoinColumn(name = "gateway_id", unique = true)
    @OneToOne(fetch = FetchType.EAGER)
    private PayGateway payGateway;


    /**
     * 商户id-boss端取默认值
     */
    @Column(name = "store_id")
    private Long storeId;


    /**
     * 身份标识
     */
    @Column(name = "api_key")
    private String apiKey;

    /**
     * secret key
     */
    @Column(name = "secret")
    private String secret;

    /**
     * 收款账户
     */
    @Column(name = "account")
    private String account;

    /**
     * 第三方应用标识
     */
    @Column(name = "app_id")
    private String appId;

    /**
     * 微信app_id
     */
    @Column(name = "app_id2")
    private String appId2;

    /**
     * 私钥
     */
    @Column(name = "private_key")
    private String privateKey;

    /**
     * 公钥
     */
    @Column(name = "public_key")
    private String publicKey;

    /**
     * 创建时间
     */
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @Column(name = "create_time")
    private LocalDateTime createTime;

    /**
     * PC前端后台接口地址
     */
    @Column(name = "pc_back_url")
    private String pcBackUrl;

    /**
     * PC前端web地址
     */
    @Column(name = "pc_web_url")
    private String pcWebUrl;

    /**
     * boss后台接口地址
     */
    @Column(name = "boss_back_url")
    private String bossBackUrl;

    /**
     * 微信开放平台app_id---微信app支付使用
     */
    @Column(name = "open_platform_app_id")
    private String openPlatformAppId;

    /**
     * 微信开放平台secret---微信app支付使用
     */
    @Column(name = "open_platform_secret")
    private String openPlatformSecret;

    /**
     * 微信开放平台api key---微信app支付使用
     */
    @Column(name = "open_platform_api_key")
    private String openPlatformApiKey;

    /**
     * 微信开放平台商户号---微信app支付使用
     */
    @Column(name = "open_platform_account")
    private String openPlatformAccount;

    /**
     * 微信公众平台支付证书
     */
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name=" wx_pay_certificate", columnDefinition="longblob", nullable=true)
    private byte[] wxPayCertificate;

    /**
     * 微信开放平台支付证书
     */
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name=" wx_open_pay_certificate", columnDefinition="longblob", nullable=true)
    private byte[] wxOpenPayCertificate;

    @Override
    public String toString() {
        return "PayGatewayConfig{" +
                "id=" + id +
                ", apiKey='" + apiKey + '\'' +
                ", secret='" + secret + '\'' +
                ", account='" + account + '\'' +
                ", appId='" + appId + '\'' +
                ", appId2='" + appId2 + '\'' +
                ", privateKey='" + privateKey + '\'' +
                ", createTime=" + createTime +
                ", pcBackUrl=" + pcBackUrl +
                ", pcWebUrl=" + pcWebUrl +
                ", bossBackUrl=" + bossBackUrl +
                ", openPlatformAppId=" + openPlatformAppId +
                ", openPlatformSecret=" + openPlatformSecret +
                ", openPlatformApiKey=" + openPlatformApiKey +
                ", openPlatformAccount=" + openPlatformAccount +
                '}';
    }
}
