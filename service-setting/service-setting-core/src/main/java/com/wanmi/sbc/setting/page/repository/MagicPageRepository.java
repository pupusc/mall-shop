package com.wanmi.sbc.setting.page.repository;

import com.wanmi.sbc.setting.page.model.root.MagicPage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * <p>MagicPage DAO</p>
 *
 * @author lq
 */
@Repository
public interface MagicPageRepository extends JpaRepository<MagicPage, Integer>,
        JpaSpecificationExecutor<MagicPage> {

    @Query(value = "select * from magic_page order by id desc limit 1", nativeQuery = true)
    MagicPage findLast();

}
