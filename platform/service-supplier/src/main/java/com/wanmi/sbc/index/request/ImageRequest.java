package com.wanmi.sbc.index.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.goods.api.request.image.ImageProviderRequest;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/10/27 1:21 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class ImageRequest {

    @NotNull(groups = ImageRequest.Update.class, message = "id不能为空")
    private Integer id;

    /**
     * 名称
     */
    @NotBlank(groups = ImageRequest.Add.class, message = "名称不能为空")
    private String name;

    /**
     * 图片地址
     */
    @NotBlank(groups = ImageRequest.Add.class, message = "图片地址不能为空")
    private String imgUrl;

    /**
     * 图片跳转链接
     */
    @NotBlank(groups = ImageRequest.Add.class, message = "图片跳转地址不能为空")
    private String imgHref;

    /**
     * 开始时间
     */
    @NotNull(groups = ImageRequest.Add.class, message = "开始时间不能为空")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime beginTime;

    /**
     * 结束时间
     */
    @NotNull(groups = ImageRequest.Add.class, message = "结束时间不能为空")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime endTime;


    public interface Add{}
    public interface Update{}
}
