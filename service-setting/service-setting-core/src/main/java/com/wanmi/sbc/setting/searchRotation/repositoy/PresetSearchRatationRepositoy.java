package com.wanmi.sbc.setting.searchRotation.repositoy;

import com.wanmi.sbc.setting.page.model.root.MagicPage;
import com.wanmi.sbc.setting.presetsearch.model.PresetSearchTerms;
import com.wanmi.sbc.setting.searchRotation.model.PresetSearchRotation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

//@Repository
/*public interface PresetSearchRatationRepositoy extends JpaRepository<PresetSearchRotation,Integer>,
        JpaSpecificationExecutor<PresetSearchRotation> {

    @Query(value = "select * from preset_search_rotation order by order_num asc ", nativeQuery = true)
    List<PresetSearchRotation> findRotation();

}*/
