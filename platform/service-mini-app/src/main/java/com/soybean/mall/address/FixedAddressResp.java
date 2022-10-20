package com.soybean.mall.address;

import com.soybean.common.resp.BaseFixedAddressResp;
import lombok.Data;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/10/17 8:27 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class FixedAddressResp extends BaseFixedAddressResp {

    /**
     * 省份
     */
    private String provinceName;


    /**
     * 城市名称
     */
    private String cityName;


    /**
     * 区域id
     */
    private String areaId;

    /**
     * 区域名
     */
    private String areaName;

    /**
     * 街道id
     */
    private String streetId;

    /**
     * 街道名
     */
    private String streetName;
}
