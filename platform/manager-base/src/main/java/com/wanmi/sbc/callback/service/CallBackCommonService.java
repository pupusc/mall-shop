package com.wanmi.sbc.callback.service;

import com.alibaba.fastjson.JSONObject;
import com.soybean.mall.wx.mini.enums.AfterSalesReasonEnum;
import com.soybean.mall.wx.mini.order.bean.response.WxDetailAfterSaleResponse;
import com.wanmi.sbc.common.base.Operator;
import com.wanmi.sbc.order.bean.enums.ReturnReason;
import com.wanmi.sbc.order.bean.vo.ReturnOrderVO;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/4/22 1:47 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Component
public class CallBackCommonService {


    /**
     * 视频号退款原因转化
     * @param afterSalesOrder
     * @return
     */
    public ReturnReason wxReturnReason2ReturnReasonType(WxDetailAfterSaleResponse.AfterSalesOrder afterSalesOrder) {
        AfterSalesReasonEnum byCode = AfterSalesReasonEnum.getByCode(afterSalesOrder.getRefundReasonType());
        if (AfterSalesReasonEnum.AFTERSALES_TWO == byCode) {
            return ReturnReason.WRONGGOODS;
        } else if (AfterSalesReasonEnum.AFTERSALES_ONE == byCode) {
            return ReturnReason.ERRORGOODS;
        } else {
            return ReturnReason.OTHER;
        }
    }

    /**
     * 视频号退款原因内容
     * @param afterSalesOrder
     * @return
     */
    public String wxReturnReasonType2ReturnReasonStr(WxDetailAfterSaleResponse.AfterSalesOrder afterSalesOrder){
        AfterSalesReasonEnum byCode = AfterSalesReasonEnum.getByCode(afterSalesOrder.getRefundReasonType());
        StringBuilder sb = new StringBuilder();
        sb.append(afterSalesOrder.getRefundReason()).append(" --- ");
        if (byCode == null) {
            sb.append("未知原因");
        } else {
            sb.append(byCode.getMessage());
        }
        return sb.toString();
    }

    /**
     * 封装附件信息
     * @param mediaListInfoList
     * @return
     */
    public List<String> appendix(List<WxDetailAfterSaleResponse.MediaListInfo> mediaListInfoList){
        List<String> result = new ArrayList<>();
        //附件
        if (!CollectionUtils.isEmpty(mediaListInfoList)) {

            int i = 1;
            for (WxDetailAfterSaleResponse.MediaListInfo mediaListInfo : mediaListInfoList) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("uid", i);
                jsonObject.put("status", "done");
                jsonObject.put("url", mediaListInfo.getUrl());
                result.add(jsonObject.toJSONString());
                i++;
            }
        }
        return result;
    }


    /**
     * 自定义 operator
     * @param returnOrderVO
     * @return
     */
    public Operator packOperator(ReturnOrderVO returnOrderVO) {
        Operator operator = new Operator();
        operator.setUserId(returnOrderVO.getBuyer().getId());
        operator.setName(returnOrderVO.getBuyer().getName());
        operator.setStoreId(returnOrderVO.getCompany().getStoreId().toString());
        operator.setIp("127.0.0.1");
        operator.setAccount(returnOrderVO.getBuyer().getAccount());
        operator.setCompanyInfoId(returnOrderVO.getCompany().getCompanyInfoId());
        return operator;
    }
}
