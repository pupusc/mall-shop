package com.wanmi.sbc.setting.searchTerm.repository;

import com.wanmi.sbc.setting.searchTerm.model.SearchTerm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: pushicheng
 * @Date: 2023/03/22/9:46
 * @Description:
 */
@Repository
public interface SearchTermRepository extends JpaRepository<SearchTerm, Integer>,
        JpaSpecificationExecutor<SearchTerm> {
}
