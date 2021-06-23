package com.wanmi.sbc.message.smssignfileinfo.repository;

import com.wanmi.sbc.message.smssignfileinfo.model.root.SmsSignFileInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>短信签名文件信息DAO</p>
 * @author lvzhenwei
 * @date 2019-12-04 14:19:35
 */
@Repository
public interface SmsSignFileInfoRepository extends JpaRepository<SmsSignFileInfo, Long>,
        JpaSpecificationExecutor<SmsSignFileInfo> {

    /**
     * 单个删除短信签名文件信息
     * @author lvzhenwei
     */
    @Modifying
    @Query("update SmsSignFileInfo set delFlag = 1 where id = ?1")
    int deleteByBeanId(Long id);

    /**
     * 批量删除短信签名文件信息
     * @author lvzhenwei
     */
    @Modifying
    @Query("update SmsSignFileInfo set delFlag = 1 where id in ?1")
    int deleteByIdList(List<Long> idList);

}
