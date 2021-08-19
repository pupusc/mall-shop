package com.wanmi.sbc.setting.sysswitch;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Created by yuanlinling on 2017/4/26.
 */
@Repository
public interface SwitchRepository extends JpaRepository<Switch, Long> {

    /**
     * 根据id查询开关
     * @param id
     * @return
     */
    Optional<Switch> findById(String id);

    /**
     * 开关开启关闭
     *
     * @param id
     * @param status
     * @return
     */
    @Modifying
    @Query("update Switch s set s.status = ?2  where s.id = ?1")
    int updateSwitch(String id, Integer status);
}
