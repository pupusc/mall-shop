package com.wanmi.sbc.home.response;

import lombok.Data;

import java.util.List;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/10/25 2:01 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class HomeImageResponse {

    /**
     * 轮播图
     */
    private List<ImageResponse> rotationChartImageList;

    /**
     * 广告图
     */
    private List<ImageResponse> advertImageList;
}
