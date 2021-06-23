package com.wanmi.sbc.setting.api.request.page;

import com.wanmi.sbc.setting.api.request.SettingBaseRequest;
import io.swagger.annotations.ApiModel;
import lombok.*;

/**
 * <p>查询缓存在数据库中的首页dom请求对象</p>
 *
 * @author lq
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
public class MagicPageMainQueryRequest extends SettingBaseRequest {
    private static final long serialVersionUID = 1L;
}