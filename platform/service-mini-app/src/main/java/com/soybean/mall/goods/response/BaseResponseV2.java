package com.soybean.mall.goods.response;

import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.MessageSourceUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.i18n.LocaleContextHolder;

import java.io.*;

/**
 * 响应基类
 * Created by aqlu on 15/11/30.
 */
@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BaseResponseV2<T> implements Serializable {

    public static BaseResponseV2 SUCCESSFUL() {
        return new BaseResponseV2(CommonErrorCode.SUCCESSFUL);
    }

    public static BaseResponseV2 FAILED() {
        return new BaseResponseV2(CommonErrorCode.FAILED);
    }

    public BaseResponseV2(String code) {
        this.code = code;
        this.message = MessageSourceUtil.getMessage(code, null, LocaleContextHolder
                .getLocale());
    }

    public BaseResponseV2(String code, Object[] args) {
        this.code = code;
        this.message = MessageSourceUtil.getMessage(code, args, LocaleContextHolder
                .getLocale());
    }

    /**
     * 结果码
     */
    @ApiModelProperty(value = "结果码", required = true)
    private String code;

    /**
     * 消息内容
     */
    @ApiModelProperty(value = "消息内容")
    private String message;

    /**
     * 错误内容
     */
    @ApiModelProperty(value = "错误内容")
    private Object errorData;

    /**
     * 内容
     */
    @ApiModelProperty(value = "内容")
    private T context;

    /**
     * 特殊提示
     *
     * @param errorCode 异常码
     * @param message   消息
     * @param obj       业务错误的时候，但是依旧要返回数据
     * @return
     */
    public static BaseResponseV2 info(String errorCode, String message, Object obj) {
        return new BaseResponseV2<>(errorCode, message, obj, null);
    }

    /**
     * 特殊提示
     *
     * @param errorCode 异常码
     * @param message   消息
     * @return
     */
    public static <T> BaseResponseV2<T> info(String errorCode, String message) {
        return new BaseResponseV2<>(errorCode, message, null, null);
    }

    /**
     * 失败
     *
     * @param message 消息
     * @return
     */
    public static <T> BaseResponseV2<T> error(String message) {
        return new BaseResponseV2<>(CommonErrorCode.FAILED, message, null, null);
    }

    /**
     * 成功
     *
     * @param context 内容
     * @return
     */
    public static <T> BaseResponseV2<T> success(T context,String message) {
        return new BaseResponseV2<>(CommonErrorCode.SUCCESSFUL, message, null, context);
    }

}
