package com.wanmi.sbc.marketing.common.mapper;

import com.wanmi.sbc.customer.bean.dto.CustomerDTO;
import com.wanmi.sbc.customer.bean.vo.CustomerVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    @Mappings({})
    CustomerVO customerDTOToCustomerVO(CustomerDTO customerDTO);
}
