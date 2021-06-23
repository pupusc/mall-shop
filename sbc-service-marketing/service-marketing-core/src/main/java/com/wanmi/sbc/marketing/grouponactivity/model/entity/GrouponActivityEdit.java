package com.wanmi.sbc.marketing.grouponactivity.model.entity;

import com.wanmi.sbc.goods.bean.dto.GrouponGoodsInfoForEditDTO;
import com.wanmi.sbc.marketing.grouponactivity.model.root.GrouponActivity;
import lombok.Data;

import java.util.List;

/**
 * @Author: gaomuwei
 * @Date: Created In 下午4:58 2019/5/16
 * @Description:
 */
@Data
public class GrouponActivityEdit {

    /**
     * 拼团活动
     */
    private GrouponActivity grouponActivity;

    /**
     * 拼团活动单品列表
     */
    private List<GrouponGoodsInfoForEditDTO> goodsInfos;

}
