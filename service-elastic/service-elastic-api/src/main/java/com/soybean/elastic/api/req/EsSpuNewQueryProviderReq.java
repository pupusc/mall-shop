package com.soybean.elastic.api.req;

import lombok.Data;

import java.util.List;

/**
 * Description: 内部搜索es信息请求对象信息
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/7/4 1:09 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class EsSpuNewQueryProviderReq extends EsSortSpuNewQueryProviderReq {

    /**
     * 获取指定spuId信息
     */
    private List<String> spuIds;

    /**
     * 排除制定spuId信息
     */
    private List<String> unSpuIds;

    /**
     * 知识顾问专项商品 0 非知识顾问 其他为非知识顾问
     */
    private Integer cpsSpecial = 0;

    /**
     * 展示对应的skuId
     */
    private List<String> showSkuIds;
}
