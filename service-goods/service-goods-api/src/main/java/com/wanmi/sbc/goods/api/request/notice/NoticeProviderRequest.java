package com.wanmi.sbc.goods.api.request.notice;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/10/26 2:31 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class NoticeProviderRequest implements Serializable {

    @NotBlank(groups = {Update.class}, message = "内容不能为空")
    private Integer id;

    /**
     * 内容
     */
    @NotBlank(groups = {Add.class}, message = "内容不能为空")
    private String content;

    /**
     * 开始时间
     */
    @NotNull(groups = {Add.class}, message = "开始时间不能为空")
    private LocalDateTime beginTime;

    /**
     * 结束时间
     */
    @NotNull(groups = {Add.class}, message = "结束时间不能为空")
    private LocalDateTime endTime;

    public interface Add{}
    public interface Update{}
}
