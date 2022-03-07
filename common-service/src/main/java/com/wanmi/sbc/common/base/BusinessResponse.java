package com.wanmi.sbc.common.base;

import com.wanmi.sbc.common.util.CommonErrorCode;

/**
 * @author Liang Jun
 * @date 2022-02-16 14:02:00
 */
public class BusinessResponse<T> extends BaseResponse<T> {
    private Page page;

    public BusinessResponse() {
    }

    public BusinessResponse(String code) {
        super(code);
    }

    public BusinessResponse(String code, String msg) {
        setCode(code);
        setMessage(msg);
    }

    public static BusinessResponse success() {
        return new BusinessResponse(CommonErrorCode.SUCCESSFUL);
    }

    public static<T> BusinessResponse<T> success(T data){
        BusinessResponse response = new BusinessResponse(CommonErrorCode.SUCCESSFUL);
        response.setContext(data);
        return response;
    }

    public static<T> BusinessResponse<T> success(T data, Page page){
        BusinessResponse response = new BusinessResponse(CommonErrorCode.SUCCESSFUL);
        response.setContext(data);
        response.setPage(page);
        return response;
    }

    public static BusinessResponse error() {
        return new BusinessResponse(CommonErrorCode.FAILED);
    }

    public static<T> BusinessResponse<T> error(String code){
        return new BusinessResponse(code);
    }

    public static<T> BusinessResponse<T> error(String code, String msg){
        BusinessResponse response = new BusinessResponse(code);
        response.setCode(code);
        response.setMessage(msg);
        return response;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }
}
