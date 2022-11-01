package com.soybean.mall.address.req;

import lombok.Data;

/**
 * Description: 定位地址
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/10/17 8:05 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class FixedAddressReq {

    /**
     * 省份
     */
    private String provinceName;

    /**
     * 市
     */
    private String cityName;


    /**
     * 区域
     */
    private String areaName;


    /**
     * 街道
     */
    private String streetName;

    /**
     * 详细地址
     */
    private String address;
}
