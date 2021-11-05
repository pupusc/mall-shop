package com.wanmi.sbc.goods.common.repository;


import com.wanmi.sbc.goods.common.model.root.RiskVerify;
import org.omg.CORBA.INTERNAL;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Repository
public interface RiskVerifyRepository extends JpaRepository<RiskVerify, Long>, JpaSpecificationExecutor<RiskVerify> {


   @Transactional
   @Modifying
   @Query("update RiskVerify set status = ?1,updateTime =now(),requestId = ?3 where id = ?2")
   void updateStatusById(Integer status,Long id,String requestId);



   @Modifying
   @Query("update RiskVerify set status = ?1, errorMsg = ?2 , content = ?3,updateTime =now() where requestId = ?4")
   void updateStatus(Integer status,String errorMsg,String content,String requestId);



   @Query(value = "SELECT COUNT(id) FROM RiskVerify where deleted = 0 and status in(0,1,3) and goodsNo = :goodsNo and requestId != :requestId")
   Integer queryCount(@Param("goodsNo") String goodsNo,@Param("requestId")String requestId);

   @Query("from RiskVerify w where w.requestId = ?1")
   RiskVerify getByRequestId(@Param("requestId") String requestId);

   @Modifying
   @Query("update RiskVerify set status = ?1,updateTime =now() where goodsNo in ?2")
   int updateStatusByGoodsNos(Integer status, List<String> goodsNo);
}