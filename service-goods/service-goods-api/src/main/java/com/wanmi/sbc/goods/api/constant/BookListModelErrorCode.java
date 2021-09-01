package com.wanmi.sbc.goods.api.constant;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/9/1 7:33 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
public class BookListModelErrorCode {

    private BookListModelErrorCode() {
    }

    /**
     * 书单发布状态码不存在
     */
    public final static String BOOK_LIST_MODEL_PUBLISH_STATE_UN_EXISTS = "FD-030001";
    public final static String BOOK_LIST_MODEL_PUBLISH_STATE_UN_EXISTS_MESSAGE = "书单状态码不存在";

    /**
     * 书单发布状态码错误
     */
    public final static String BOOK_LIST_MODEL_PUBLISH_STATE_ERROR = "FD-030002";
    public final static String BOOK_LIST_MODEL_PUBLISH_STATE_ERROR_MESSAGE = "书单状态码错误";
}
