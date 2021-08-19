package com.wanmi.sbc.elastic.customer.model.root;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
public class EsPaidCard {

    @Field(type = FieldType.Text )
    private String paidCardId;

    @Field(type = FieldType.Text )
    private String paidCardName;
}
