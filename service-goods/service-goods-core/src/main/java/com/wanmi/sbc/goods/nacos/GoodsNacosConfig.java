package com.wanmi.sbc.goods.nacos;

import lombok.Data;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/10/24 2:31 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@RefreshScope
@Service
@Data
public class GoodsNacosConfig {


    private String freeDelivery49;

    private List<String> unFreeDelivery49s;
}
