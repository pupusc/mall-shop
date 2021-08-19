package com.wanmi.sbc.order.mapper;

import com.wanmi.sbc.customer.bean.dto.CustomerDTO;
import com.wanmi.sbc.customer.bean.vo.CustomerVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface CustmerMapper {


    @Mappings({})
    CustomerDTO customerVOToCustomerDTO(CustomerVO customerVO);
}
