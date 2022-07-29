package com.wanmi.sbc.goods.api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/10/20 4:15 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Getter
@AllArgsConstructor
public enum IndexModuleEnum {

    EDIT_RECOMMEND("edit_recommend", "编辑推荐"),
    FAMOUS_RECOMMEND("famous_recommend" , "名家推荐"),
    NEW_BOOKS("new_books" , "新书上架"),
    SELL_WELL_BOOKS("sell_well_books","畅销榜"),
    SPECIAL_OFFER_BOOKS("special_offer_books","特价书"),
    UN_SELL_WELL_BOOKS("un_sell_well_books","不畅销专区"),
    SPU_TOPIC("spu_topic", "商品栏目信息");


    private final String code;
    private final String message;

    public static IndexModuleEnum getByCode(String code) {
        for (IndexModuleEnum p : values()) {
            if (Objects.equals(p.getCode(), code)) {
                return p;
            }
        }
        return null;
    }
}
