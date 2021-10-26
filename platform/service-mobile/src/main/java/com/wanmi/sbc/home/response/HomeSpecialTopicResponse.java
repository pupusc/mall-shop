package com.wanmi.sbc.home.response;

import lombok.Data;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/10/26 1:53 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class HomeSpecialTopicResponse {

    private Integer id;
    
    /**
     * 主标题
     */
    private String title;

    /**
     * 副标题
     */
    private String subTitle;

    /**
     * 图片地址
     */
    private String imgUrl;

    /**
     * 图片链接
     */
    private String imgHref;
}
