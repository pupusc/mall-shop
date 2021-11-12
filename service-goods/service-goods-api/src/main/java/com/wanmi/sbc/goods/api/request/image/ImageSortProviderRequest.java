package com.wanmi.sbc.goods.api.request.image;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/10/22 2:38 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class ImageSortProviderRequest implements Serializable {


    @NotNull
    private Integer id;

    /**
     * 排序
     */
    @NotNull
    private Integer orderNum;
}
