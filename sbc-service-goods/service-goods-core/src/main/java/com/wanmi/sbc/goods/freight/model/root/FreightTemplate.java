package com.wanmi.sbc.goods.freight.model.root;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.goods.bean.enums.DeliverWay;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 运费模板
 * Created by sunkun on 2018/5/2.
 */
@Getter
@Setter
@MappedSuperclass
public class FreightTemplate implements Serializable{

    private static final long serialVersionUID = 7236002394207629461L;

    /**
     * 运费模板id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "freight_temp_id")
    private Long freightTempId;

    /**
     * 运费模板名称
     */
    @Column(name = "freight_temp_name")
    private String freightTempName;

    /**
     * 运送方式(1:快递配送)
     */
    @Column(name = "deliver_way")
    private DeliverWay deliverWay;

    /**
     * 店铺id
     */
    @Column(name = "store_id")
    private Long storeId;

    /**
     * 商家id
     */
    @Column(name = "company_info_id")
    private Long companyInfoId;

    /**
     * 创建时间
     */
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @Column(name = "create_time")
    private LocalDateTime createTime;

    /**
     * 默认标识
     */
    @Column(name = "default_flag")
    private DefaultFlag defaultFlag;

    /**
     * 删除标识
     */
    @Column(name = "del_flag")
    private DeleteFlag delFlag;

}
