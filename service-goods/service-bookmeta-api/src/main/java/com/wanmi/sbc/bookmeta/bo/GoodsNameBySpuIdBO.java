package com.wanmi.sbc.bookmeta.bo;

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
    private String spuId;
    private int delFlag;
    private String createTime;
    private String updateTime;
}
