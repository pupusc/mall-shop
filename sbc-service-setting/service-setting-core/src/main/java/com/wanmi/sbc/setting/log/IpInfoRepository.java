package com.wanmi.sbc.setting.log;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * IP信息归属仓库
 * Created by dyt on 2017/5/15.
 */
public interface IpInfoRepository extends JpaRepository<IpInfo, Long> {

    /**
     * 获取IP相关的信息
     * @param ip
     * @return
     */
    @Query("select s from IpInfo s where s.ip = ?1")
    List<IpInfo> findByIp(String ip);
}
