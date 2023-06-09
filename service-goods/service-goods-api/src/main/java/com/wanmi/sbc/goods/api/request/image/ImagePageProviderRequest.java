package com.wanmi.sbc.goods.api.request.image;

import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;


/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/10/20 1:59 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class ImagePageProviderRequest implements Serializable {

    private int pageNum = 0;

    private int pageSize = 10;

    private Integer id;

    private Collection<Integer> idColl;

    private String name;

    /**
     *  0未开始 1进行中 2 已结束
     */
    private Integer status;

    /**
     * 启用状态 0未启用 1启用
     */
    private Integer publishState;

    /**
     * 图片类型 1首页轮播 2 广告图  3卖点图
     */
    private List<Integer> imageTypeList;
}
