package com.wanmi.sbc.goods.info.repository;

import com.wanmi.sbc.goods.info.model.root.GoodsSync;
import com.wanmi.sbc.goods.info.model.root.GoodsSyncRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface GoodsSyncRelationRepository extends JpaRepository<GoodsSyncRelation, Long>, JpaSpecificationExecutor<GoodsSyncRelation> {




}




