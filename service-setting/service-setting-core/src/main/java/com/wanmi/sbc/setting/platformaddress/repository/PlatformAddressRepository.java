package com.wanmi.sbc.setting.platformaddress.repository;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.setting.platformaddress.model.root.PlatformAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * <p>平台地址信息DAO</p>
 * @author dyt
 * @date 2020-03-30 14:39:57
 */
@Repository
public interface PlatformAddressRepository extends JpaRepository<PlatformAddress, String>,
        JpaSpecificationExecutor<PlatformAddress> {

    /**
     * 单个删除平台地址信息
     * @author dyt
     */
    @Modifying
    @Query("update PlatformAddress set delFlag = 1, deleteTime = now() where id = ?1")
    void deleteById(String id);

    /**
     * 批量删除平台地址信息
     * @author dyt
     */
    @Modifying
    @Query("update PlatformAddress set delFlag = 1, deleteTime = now() where id in ?1")
    void deleteByIdList(List<String> idList);

    Optional<PlatformAddress> findByIdAndDelFlag(String id, DeleteFlag delFlag);

    @Query("from PlatformAddress p where p.delFlag = 0 and p.addrId in ?1 or p.addrParentId in ?1")
    List<PlatformAddress> findAllProvinceAndCity(List<String> addrIds);

    /**
     * 统计数量
     */
    @Query("select count(1) from PlatformAddress p where p.delFlag=0 and p.id = :id")
    int countById(@Param("id") String id);

    /**
     * 统计数量
     */
    @Query("select count(1) from PlatformAddress p where p.delFlag=0 and p.id = :id and p.addrParentId = :addrParentId")
    int countByIdAndParentId(@Param("id") String id,@Param("addrParentId") String addrParentId);

    /**
     * 统计数量
     */
    @Query("select count(1) from PlatformAddress p where p.delFlag=0 and p.addrParentId = :addrParentId")
    int countByParentId(@Param("addrParentId") String addrParentId);
}
