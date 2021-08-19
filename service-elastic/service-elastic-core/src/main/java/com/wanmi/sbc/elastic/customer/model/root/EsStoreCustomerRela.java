package com.wanmi.sbc.elastic.customer.model.root;

import com.wanmi.sbc.customer.bean.enums.CustomerType;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;

/**
 * 店铺-会员(包含会员等级)关联实体类
 * Created by bail on 2017/11/13.
 */
@Data
public class EsStoreCustomerRela implements Serializable {

    @Field(type = FieldType.Keyword )
    private String id;

    /**
     * 用户标识
     */
    @Field(type = FieldType.Keyword )
    private String customerId;

    /**
     * 店铺标识
     */
    @Field(type = FieldType.Long )
    private Long storeId;

    /**
     * 商家标识
     */
    @Field(type = FieldType.Long )
    private Long companyInfoId;

    /**
     * 店铺等级标识
     */
    @Field(type = FieldType.Long )
    private Long storeLevelId;

    /**
     * 负责的业务员标识
     */
    @Field(type = FieldType.Keyword )
    private String employeeId;

    /**
     * 关系类型(0:店铺关联的客户,1:店铺发展的客户)
     */
    @Field(type = FieldType.Integer )
    private CustomerType customerType;

}

