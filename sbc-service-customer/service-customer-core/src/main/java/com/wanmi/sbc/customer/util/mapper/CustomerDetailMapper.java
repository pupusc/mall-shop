package com.wanmi.sbc.customer.util.mapper;

import com.wanmi.sbc.customer.bean.vo.CustomerDetailInitEsVO;
import com.wanmi.sbc.customer.bean.vo.CustomerDetailToEsVO;
import com.wanmi.sbc.customer.bean.vo.EnterpriseInfoVO;
import com.wanmi.sbc.customer.bean.vo.StoreCustomerRelaVO;
import com.wanmi.sbc.customer.detail.model.root.CustomerDetail;
import com.wanmi.sbc.customer.detail.model.root.CustomerDetailInitEs;
import com.wanmi.sbc.customer.enterpriseinfo.model.root.EnterpriseInfo;
import com.wanmi.sbc.customer.storecustomer.root.StoreCustomerRela;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CustomerDetailMapper {

    @Mappings({})
    CustomerDetailToEsVO customerDetailToCustomerDetailToEsVO(CustomerDetail customerDetail);

    @Mappings({})
    List<CustomerDetailInitEsVO> customerDetailInitEsToCustomerDetailInitEsVO(List<CustomerDetailInitEs> customerDetailInitEs);


    @Mappings({})
    List<StoreCustomerRelaVO> storeCustomerRelasToStoreCustomerRelaVO(List<StoreCustomerRela> storeCustomerRelas);


    @Mappings({})
    List<EnterpriseInfoVO> enterpriseInfosToEnterpriseInfoVO(List<EnterpriseInfo> enterpriseInfos);
}
