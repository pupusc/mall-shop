package com.wanmi.sbc.goods.provider.impl.nacos;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.provider.nacos.GoodsNacosConfigProvider;
import com.wanmi.sbc.goods.api.response.nacos.GoodsNacosConfigResp;
import com.wanmi.sbc.goods.nacos.GoodsNacosConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/10/24 2:37 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@RestController
public class GoodsNacosConfigController implements GoodsNacosConfigProvider {

    @Autowired
    private GoodsNacosConfig goodsNacosConfig;

    @Override
    public BaseResponse<GoodsNacosConfigResp> getNacosConfig() {
        GoodsNacosConfigResp goodsNacosConfigResp = new GoodsNacosConfigResp();
        goodsNacosConfigResp.setFreeDelivery49(goodsNacosConfig.getFreeDelivery49());
        return BaseResponse.success(goodsNacosConfigResp);
    }
}
