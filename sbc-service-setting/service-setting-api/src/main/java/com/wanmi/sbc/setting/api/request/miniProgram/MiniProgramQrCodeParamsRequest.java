package com.wanmi.sbc.setting.api.request.miniProgram;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel
@Data
public class MiniProgramQrCodeParamsRequest implements Serializable {
    private static final long serialVersionUID = 2881399978923294322L;

    /**
     * 最大32个可见字符，只支持数字，大小写英文以及部分特殊字符：!#$&'()*+,/:;=?@-._~，
     * 其它字符请自行编码为合法字符（因不支持%，中文无法使用 urlencode 处理，请使用其他编码方式）
     */
    @ApiModelProperty(value = "字符-最大32个可见字符，只支持数字，大小写英文以及部分特殊字符：!#$&'()*+,/:;=?@-._~")
    private String scene;

    /**
     * 必须是已经发布的小程序存在的页面（否则报错），例如 pages/index/index, 根路径前不要填加 /,
     * 不能携带参数（参数请放在scene字段里），如果不填写这个字段，默认跳主页面
     */
    @ApiModelProperty(value = "页面路径-必须是已经发布的小程序存在的页面（否则报错），例如 pages/index/index, 根路径前不要填加 /")
    private String page;

    /**
     * 二维码的宽度，单位 px，最小 280px，最大 1280px
     */
    @ApiModelProperty(value = "二维码的宽度")
    private Integer width;

    /**
     * 自动配置线条颜色，如果颜色依然是黑色，则说明不建议配置主色调，默认 false
     */
    @ApiModelProperty(value = "是否自动配置线条颜色-自动配置线条颜色，如果颜色依然是黑色，则说明不建议配置主色调，默认 false")
    private Boolean auto_color;

    /**
     * auto_color 为 false 时生效，使用 rgb 设置颜色 例如 {"r":"xxx","g":"xxx","b":"xxx"} 十进制表示
     */
    @ApiModelProperty(value = "线条颜色")
    private Object line_color;

    /**
     * 是否需要透明底色，为 true 时，生成透明底色的小程序
     */
    @ApiModelProperty(value = "是否需要透明底色")
    private Boolean is_hyaline;
}
