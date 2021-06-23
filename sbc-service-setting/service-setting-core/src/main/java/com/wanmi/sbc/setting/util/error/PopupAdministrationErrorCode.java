package com.wanmi.sbc.setting.util.error;

/**
 * <p>弹窗管理相关的异常码定义</p>
 */
public final class PopupAdministrationErrorCode {

    private PopupAdministrationErrorCode() {
    }

    /**
     * 弹窗管理不存在
     */
    public final static String  POPUP_MANAGEMENT_DOES_EXIST = "popup-management-does-not-exist";

    /**
     * * 弹窗管理无法启动
     */
    public final static String POPUP_MANAGEMENT_CANNOT_START = "popup-management-cannot-start";

    /**
     * * 弹窗管理无法暂停
     */
    public final static String POPUP_MANAGEMENT_CANNOT_SUSPEND = "popup-management-cannot-suspend";

    /**
     * 当前弹窗已开始或已结束，无法编辑
     */
    public final static String POPUP_MANAGEMENT_STARTED_OR_ENDED = "popup-management-stared-or-ended";

    /**
     * 当前弹窗已开始或已结束，无法删除
     */
    public final static String POPUP_MANAGEMENT_STARTED_OR_ENDED_CANNOT_DELETE = "popup-management-stared-or-ended-cannot-delete";

}
