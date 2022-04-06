package com.fangdeng.server.vo;

import lombok.Data;

import javax.xml.bind.annotation.XmlElement;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/3/31 2:01 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class GoodsCostPriceVO implements Serializable {

    private String erpGoodsNo;

    private String isbn;
    //成本价
    private BigDecimal costPrice;
    private BigDecimal sellPrice;
}
