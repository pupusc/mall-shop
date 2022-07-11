package com.wanmi.sbc.goods.api.request.index;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Description: 栏目对象
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/7/11 3:47 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class NormalModuleReq {

    private Integer id;
    /**
     * 名称
     */
    @NotNull
    private String name;

    /**
     * 开始时间
     */
    @NotNull
    private LocalDateTime beginTime;

    /**
     * 结束时间
     */
    @NotNull
    private LocalDateTime endTime;

    /**
     * 模块分类 {@link com.wanmi.sbc.goods.api.enums.NormalModuleCategoryEnum}
     */
    @NotNull
    private Integer modelCategory;


    /**
     * 标签名称
     */
    private String modelTag;


    @NotNull
    private List<NormalModuleSkuReq> normalModuleSkus;
}
