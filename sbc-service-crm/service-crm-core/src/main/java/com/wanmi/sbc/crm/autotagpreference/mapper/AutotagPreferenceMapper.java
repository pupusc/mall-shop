package com.wanmi.sbc.crm.autotagpreference.mapper;

import com.wanmi.sbc.crm.api.request.autotagpreference.AutoPreferenceListRequest;
import com.wanmi.sbc.crm.api.request.autotagpreference.AutoPreferencePageRequest;
import com.wanmi.sbc.crm.autotagpreference.model.AutotagPreference;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AutotagPreferenceMapper {

    List<AutotagPreference> findByTagId(AutoPreferencePageRequest request);

    List<AutotagPreference> findByTagIdAndDimensionId(AutoPreferenceListRequest request);

    long countByTagIdAndDetailName(AutoPreferencePageRequest request);

    long countByTagId(Long tagId);
}
