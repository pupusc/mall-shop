package com.wanmi.sbc.crm.autotagother.mapper;

import com.wanmi.sbc.crm.api.request.autotagother.AutoTagOtherPageRequest;
import com.wanmi.sbc.crm.autotagother.model.AutotagOther;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AutotagOtherMapper {

    List<AutotagOther> findByIdAndType(AutoTagOtherPageRequest request);

    long countByIdAndType(AutoTagOtherPageRequest request);

}
