package com.wanmi.sbc.marketing.coupon.repository;

import com.wanmi.sbc.marketing.coupon.model.root.CouponCate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CouponCateRepository extends JpaRepository<CouponCate, String>, JpaSpecificationExecutor<CouponCate> {


    /**
     * 根据优惠券分类Id查询优惠券分类列表
     *
     * @return
     */
    @Query(" from CouponCate c where c.delFlag = '0' and c.couponCateId in (?1) ")
    List<CouponCate> listCouponCateLimitThreeByCouponCateIds(List<String> couponCateIds);


    /**
     * 查询优惠券分类列表
     *
     * @return
     */
    @Query(" from CouponCate c where c.delFlag = '0' order by c.cateSort asc, c.createTime desc")
    List<CouponCate> listCouponCate();

    /**
     * 根据Id查询单个优惠券分类
     *
     * @param couponCateId
     * @return
     */
    @Query(" from CouponCate c where c.delFlag = '0' and c.couponCateId = ?1 ")
    CouponCate getCouponCateByCouponCateId(String couponCateId);

    /**
     * 删除优惠券分类
     *
     * @param couponCateId
     */
    @Query(" update CouponCate c set c.delFlag = '1',  c.delPerson = ?2 , c.delTime = ?3 where c.couponCateId = ?1 ")
    @Modifying
    void deleteCouponCateByCouponCateId(String couponCateId, String delPerson, LocalDateTime delTime);

    /**
     * 查询除自身外的名称是否存在
     *
     * @param couponCateId   优惠券分类Id
     * @param couponCateName 优惠券分类名称
     * @return
     */
    @Query(" from CouponCate c where c.delFlag = '0' and c.couponCateId <> ?1 and c.couponCateName = ?2 ")
    CouponCate findByCouponCateNameNotSelf(String couponCateId, String couponCateName);


    /**
     * 优惠券分类排序
     *
     * @param couponCateId 优惠券分类Id
     * @param sort         优惠券分类顺序
     */
    @Query(" update CouponCate c set c.cateSort = ?2 where c.couponCateId = ?1 ")
    @Modifying
    void sortCouponCate(String couponCateId, Integer sort);


    /**
     * 查询优惠券分类列表提供给商家使用
     *
     * @return
     */
    @Query(" from CouponCate c where c.delFlag = '0' and c.onlyPlatformFlag = '0'" +
            " order by c.cateSort asc, c.createTime desc")
    List<CouponCate> listCouponCateForSupplier();

    @Query(" from CouponCate c where c.couponCateId in ?1")
    List<CouponCate> queryByIds(List<String> couponIdList);

}
