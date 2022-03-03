package com.wanmi.sbc.goods.mini.repository.goods;

import com.wanmi.sbc.goods.mini.model.goods.WxLiveAssistantModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WxLiveAssistantRepository extends JpaRepository<WxLiveAssistantModel, Long>, JpaSpecificationExecutor<WxLiveAssistantModel> {

}
