package com.wanmi.sbc.marketing.markup.repository;

import com.wanmi.sbc.marketing.markup.model.root.MarkupLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


/**
 * <p>加价购活动DAO</p>
 * @author he
 * @date 2021-02-04 16:11:01
 */
@Repository
public interface MarkupLevelRepository extends JpaRepository<MarkupLevel, Long>,
        JpaSpecificationExecutor<MarkupLevel> {
    /**
     * 根据id删除
     *
     * @param id
     */
    void deleteById(Long id);

    /**
     * 根据活动id 删除阶梯的数据
     * @param id
     */
    void deleteByMarkupId(Long id);
}
