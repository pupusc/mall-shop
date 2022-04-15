package com.soybean.mall.wx.mini.customerserver.response;

import com.alibaba.fastjson.annotation.JSONField;
import com.soybean.mall.wx.mini.goods.bean.response.WxResponseBase;
import lombok.Data;

import java.util.List;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/4/13 2:36 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class WxCustomerServerOnlineResponse extends WxResponseBase {

    @JSONField(name="kf_online_list")
    private List<CustomerServerOnline> customerServerOnlineList;


    @Data
    public static class CustomerServerOnline {

        @JSONField(name = "kf_id")
        private String id;

        @JSONField(name = "kf_openid")
        private String openId;

        @JSONField(name = "status")
        private Integer status;
    }
}
