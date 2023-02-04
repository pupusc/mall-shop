package com.wanmi.sbc.setting.defaultsearchterms.repository;


import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.setting.defaultsearchterms.model.DefaultSearchTerms;
import com.wanmi.sbc.setting.popularsearchterms.model.PopularSearchTerms;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @Description: 默认搜索词
 * @Author zh
 * @Date 2023/2/4 11:14
 */
public interface DefaultSearchTermsRespository extends JpaRepository<DefaultSearchTerms,Long>,
        JpaSpecificationExecutor<DefaultSearchTerms> {

    /**
     * @Description 查询父默认搜索词
     * @Author zh
     * @Date  2023/2/4 14:13
     */
    List<DefaultSearchTerms> findByDelFlagAndDefaultChannelAndIsParentOrderBySortNumberAscCreateTimeDesc(DeleteFlag delFlag, Integer defaultChannel, Boolean isParent);

    /**
     * @Description 查询子默认搜索词
     * @Author zh
     * @Date  2023/2/4 14:13
     */
    List<DefaultSearchTerms> findByDelFlagAndDefaultChannelAndParentIdAndIsParentOrderBySortNumberAscCreateTimeDesc(DeleteFlag delFlag, Integer defaultChannel, Long parentId, Boolean isParent);

}
