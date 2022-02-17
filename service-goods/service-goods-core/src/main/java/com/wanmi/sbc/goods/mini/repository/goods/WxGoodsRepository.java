package com.wanmi.sbc.goods.mini.repository.goods;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.goods.mini.model.goods.WxGoodsModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface WxGoodsRepository extends JpaRepository<WxGoodsModel, Long>, JpaSpecificationExecutor<WxGoodsModel> {

    @Query(value = "select count(*) from t_wx_goods where status=2 and edit_status=3 and goods_id=? and del_flag=0", nativeQuery = true)
    Integer getOnShelfCount(String goodsId);

    WxGoodsModel findByGoodsIdAndDelFlag(String goodsId, DeleteFlag Flag);
}
