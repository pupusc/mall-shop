package com.wanmi.sbc.customer.detail.model.root;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.GenderType;
import com.wanmi.sbc.common.util.CustomLocalDateDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateSerializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.common.annotation.CanEmpty;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.customer.bean.enums.CustomerStatus;
import com.wanmi.sbc.customer.model.root.Customer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 客户详细信息
 * Created by CHENLI on 2017/4/13.
 */
@Entity
@Table(name = "customer_detail")
@Data
@NamedEntityGraphs(
        @NamedEntityGraph(
                name="customerDetail.all",
                attributeNodes = {
                        @NamedAttributeNode("customer")
                }
        )
)
public class CustomerDetail implements Serializable {

    /**
     * 会员详细信息标识UUID
     */
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "customer_detail_id")
    private String customerDetailId;

    /**
     * 会员ID
     */
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "customer_id", insertable = false, updatable = false)
    @JsonBackReference
    private Customer customer;

    /**
     * 会员ID
     */
    @Column(name = "customer_id")
    private String customerId;

    /**
     * 会员名称
     */
    @Column(name = "customer_name")
    private String customerName;

    /**
     * 省
     */
    @Column(name = "province_id")
    @CanEmpty
    private Long provinceId;

    /**
     * 市
     */
    @Column(name = "city_id")
    @CanEmpty
    private Long cityId;

    /**
     * 区
     */
    @Column(name = "area_id")
    @CanEmpty
    private Long areaId;

    /**
     * 街道
     */
    @Column(name = "street_id")
    @CanEmpty
    private Long streetId;

    /**
     * 详细地址
     */
    @Column(name = "customer_address")
    private String customerAddress;

    /**
     * 删除标志 0未删除 1已删除
     */
    @Column(name = "del_flag")
    @Enumerated
    private DeleteFlag delFlag;

    /**
     * 账号状态 0：启用中  1：禁用中
     */
    @Column(name = "customer_status")
    @Enumerated
    private CustomerStatus customerStatus;

    /**
     * 联系人名字
     */
    @Column(name = "contact_name")
    private String contactName;

    /**
     * 联系方式
     */
    @Column(name = "contact_phone")
    private String contactPhone;

    /**
     * 负责业务员
     */
    @Column(name = "employee_id")
    private String employeeId;

    /**
     * 创建时间
     */
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @Column(name = "create_time")
    private LocalDateTime createTime;

    /**
     * 创建人
     */
    @Column(name = "create_person")
    private String createPerson;

    /**
     * 修改时间
     */
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @Column(name = "update_time")
    private LocalDateTime updateTime;

    /**
     * 修改人
     */
    @Column(name = "update_person")
    private String updatePerson;

    /**
     * 删除时间
     */
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @Column(name = "delete_time")
    private LocalDateTime deleteTime;

    /**
     * 是否为分销员 0：否  1：是
     */
    @ApiModelProperty(value = "是否有子类")
    @Column(name = "is_distributor")
    private DefaultFlag isDistributor;

    /**
     * 删除人
     */
    @Column(name = "delete_person")
    private String deletePerson;

    /**
     * 审核驳回理由
     */
    @Column(name = "reject_reason")
    private String rejectReason;

    /**
     * 禁用原因
     */
    @Column(name = "forbid_reason")
    private String forbidReason;


    /**
     * 生日
     */
    @Column(name = "birth_day")
    @JsonSerialize(using = CustomLocalDateSerializer.class)
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    private LocalDate birthDay;

    /**
     * 性别
     */
    @Column(name = "gender")
    private GenderType gender;

    /**
     * 是否为员工 0：否 1：是
     */
    @Column(name = "is_employee")
    private Integer isEmployee;

}
