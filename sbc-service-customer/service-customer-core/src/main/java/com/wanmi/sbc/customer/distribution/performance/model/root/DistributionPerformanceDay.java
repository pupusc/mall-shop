package com.wanmi.sbc.customer.distribution.performance.model.root;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateSerializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>分销业绩日统计记录</p>
 * Created by of628-wenzhi on 2019-04-17-16:01.
 */
@Entity
@Table(name = "distribution_performance_day")
@Data
public class DistributionPerformanceDay implements Serializable {
    private static final long serialVersionUID = -9077929752220857131L;

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;

    @Column(name = "distribution_id")
    private String distributionId;

    @Column(name = "customer_id")
    private String customerId;

    @Column(name = "sale_amount")
    private BigDecimal saleAmount;

    @Column(name = "commission")
    private BigDecimal commission;

    @Column(name = "target_date")
    @Convert(converter = Jsr310JpaConverters.LocalDateConverter.class)
    @JsonSerialize(using = CustomLocalDateSerializer.class)
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    private LocalDate targetDate;

    @Column(name = "create_time")
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createTime;
}
