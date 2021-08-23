package com.wanmi.ares.base;

import com.wanmi.ares.util.MessageSourceUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.i18n.LocaleContextHolder;

import java.io.Serializable;

/**
 * 响应基类
 * Created by aqlu on 15/11/30.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BaseResponse<T> implements Serializable {

    public static BaseResponse SUCCESSFUL() {
        return new BaseResponse(ResultCode.SUCCESSFUL);
    }

    public static BaseResponse FAILED() {
        return new BaseResponse(ResultCode.FAILED);
    }

    public BaseResponse(String code){
        this.code = code;
        this.message = MessageSourceUtil.getMessage(code, null, LocaleContextHolder.getLocale());
    }

    public BaseResponse(String code, Object[] args){
        this.code = code;
        this.message = MessageSourceUtil.getMessage(code, args, LocaleContextHolder.getLocale());
    }

    /**
     * 结果码
     */
    private String code;

    /**
     * 消息内容
     */
    private String message;

    /**
     * 内容
     */
    private T context;

    /**
     * 失败
     * @param message 消息
     * @return
     */
    public static <T> BaseResponse<T> error(String message){
        return new BaseResponse<>(ResultCode.FAILED, message, null);
    }

    /**
     * 成功
     * @param context 内容
     * @return
     */
    public static <T> BaseResponse<T> success(T context){
        return new BaseResponse<>(ResultCode.SUCCESSFUL, null, context);
    }

}
