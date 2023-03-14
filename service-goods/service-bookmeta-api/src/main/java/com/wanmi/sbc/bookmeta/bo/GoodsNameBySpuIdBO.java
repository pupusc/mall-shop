package com.wanmi.sbc.bookmeta.bo;

import com.wanmi.sbc.common.base.Page;
import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: pushicheng
 * @Date: 2023/03/02/13:11
 * @Description:
 */
@Data
public class GoodsNameBySpuIdBO {
    private int id;
    private String name;
    private String goodsName;
    private String spuId;
    private int delFlag;
    private Integer status;
    private String createTime;
    private String updateTime;
    private Page page = new Page(1, 10);
}
