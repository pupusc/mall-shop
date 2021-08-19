package com.wanmi.sbc.elastic.customer.mapper;

import com.wanmi.sbc.customer.bean.vo.CustomerDetailInitEsVO;
import com.wanmi.sbc.elastic.bean.dto.customer.EsCustomerDetailDTO;
import com.wanmi.sbc.elastic.bean.vo.customer.EsCustomerDetailVO;
import com.wanmi.sbc.elastic.customer.model.root.EsCustomerDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EsCustomerDetailMapper {

    @Mappings({})
    EsCustomerDetail customerDetailDTOToEsCustomerDetail(EsCustomerDetailDTO customerDetailDTO);


    @Mappings({})
    List<EsCustomerDetailVO> customerDetailToEsCustomerDetailVO(List<EsCustomerDetail> esCustomerDetailList);

    @Mappings({})
    List<EsCustomerDetail> customerDetailInitEsVOToEsCustomerDetail(List<CustomerDetailInitEsVO> initEsVOList);
}
