package com.wanmi.sbc.customer.quicklogin.repository;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.customer.bean.enums.ThirdLoginType;
import com.wanmi.sbc.customer.quicklogin.model.root.ThirdLoginRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @Author: songhanlin
 * @Date: Created In 10:31 AM 2018/8/8
 * @Description: 第三方登录关系Repository
 */
@Repository
public interface ThirdLoginRelationRepository extends JpaRepository<ThirdLoginRelation, String> {

    /**
     * 根据 用户Id&第三方登录方式  查询所有第三方登录关系
     *
     * @param customerId     用户Id
     * @param thirdLoginType 第三方登录方式
     * @return
     */
    @Query("from ThirdLoginRelation t where t.customerId = ?1 and t.thirdLoginType = ?2 ")
    List<ThirdLoginRelation> findAllByCustomerIdAndThirdType(String customerId, ThirdLoginType thirdLoginType);

     /**
     * 根据 用户Id&第三方登录方式  查询所有第三方登录关系
     *
     * @param customerId     用户Id
     * @param thirdLoginType 第三方登录方式
     * @return
     */
    @Query("from ThirdLoginRelation t where t.customerId = ?1 and t.thirdLoginType = ?2 and t.delFlag = ?3 ")
    List<ThirdLoginRelation> findAllByCustomerIdAndThirdTypeAndDelFlag(String customerId, ThirdLoginType
            thirdLoginType,DeleteFlag
            delFlag);

    /**
     * 根据 用户Id&第三方登录方式  查询所有第三方登录关系
     *
     * @param customerId     用户Id
     * @param thirdLoginType 第三方登录方式
     * @return
     */
    @Query("from ThirdLoginRelation t where t.customerId = ?1 and t.thirdLoginType = ?2 and t.delFlag = ?3 and t" +
            ".storeId = ?4 ")
    Optional<ThirdLoginRelation> findByCustomerIdAndThirdTypeAndDelFlagAndStoreId(String customerId, ThirdLoginType
            thirdLoginType,DeleteFlag delFlag, Long storeId);


    Optional<ThirdLoginRelation> findTop1ByCustomerIdAndThirdLoginTypeAndDelFlagOrderByBindingTimeDesc(String customerId,
                            ThirdLoginType thirdLoginType, DeleteFlag delFlag);

    /**
     * 根据 关联Id&第三方登录方式 查询所有第三方登录关系
     *
     * @param thirdLoginUid  第三方关联Id
     * @param thirdLoginType 第三方登录方式
     * @return
     */
    Optional<ThirdLoginRelation> findTop1ByThirdLoginUidAndThirdLoginTypeAndStoreId(String thirdLoginUid, ThirdLoginType thirdLoginType, Long storeId);


    /**
     * 根据 关联Id&第三方登录方式 查询所有第三方登录关系
     *
     * @param thirdLoginUid  第三方关联Id
     * @param thirdLoginType 第三方登录方式
     * @return
     */
    Optional<ThirdLoginRelation> findTop1ByThirdLoginUidAndThirdLoginTypeAndDelFlagAndStoreId(String thirdLoginUid, ThirdLoginType
            thirdLoginType, DeleteFlag delFlag, Long storeId);

    @Query("from ThirdLoginRelation t where t.thirdLoginUid = ?1 and t.thirdLoginType = ?2  and t.delFlag = ?3 and t" +
            ".storeId = ?4 ")
    Optional<ThirdLoginRelation> findByUnionIdAndThirdTypeAndDeleteFlagAndStoreId(String thirdLoginUid, ThirdLoginType
            thirdLoginType, DeleteFlag delFlag, Long storeId);

    /**
     * 更新第三方登录信息
     *
     * @param
     */
    @Modifying
    @Query("update ThirdLoginRelation d set d.delFlag =:#{#info.delFlag},d.thirdLoginUid = :#{#info.thirdLoginUid},d.thirdLoginOpenId" +
            " =:#{#info.thirdLoginOpenId}, d.bindingTime = :#{#info.bindingTime},d.nickname = :#{#info.nickname}," +
            " d.headimgurl= :#{#info.headimgurl} where customerId =:#{#info.customerId}")
    void updateInfo(@Param("info") ThirdLoginRelation thirdLoginRelation);



    /**
     * 根据会员ID集合批量查询第三方登录信息
     *
     * @param customerIds
     * @return
     */
    @Query("from ThirdLoginRelation c where  c.customerId in :customerIds and c.delFlag = '0' ")
    List<ThirdLoginRelation> findAnyByCustomerIds(@Param("customerIds") List<String> customerIds);
}
