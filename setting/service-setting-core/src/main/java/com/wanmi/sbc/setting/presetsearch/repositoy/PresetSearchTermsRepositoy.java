package com.wanmi.sbc.setting.presetsearch.repositoy;

import com.wanmi.sbc.setting.presetsearch.model.PresetSearchTerms;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PresetSearchTermsRepositoy  extends JpaRepository<PresetSearchTerms,Integer>,
        JpaSpecificationExecutor<PresetSearchTerms> {
}
