package com.wanmi.sbc.callback.service;

import com.alibaba.fastjson.JSONObject;
import com.soybean.mall.wx.mini.enums.AfterSalesReasonEnum;
import com.soybean.mall.wx.mini.order.bean.response.WxDetailAfterSaleResponse;
import com.wanmi.sbc.common.base.Operator;
import com.wanmi.sbc.common.enums.Platform;
import com.wanmi.sbc.order.api.provider.trade.TradeQueryProvider;
import com.wanmi.sbc.order.api.request.trade.TradeGetByIdRequest;
import com.wanmi.sbc.order.bean.enums.ReturnReason;
import com.wanmi.sbc.order.bean.vo.ReturnOrderVO;
import com.wanmi.sbc.order.bean.vo.TradeItemVO;
import com.wanmi.sbc.order.bean.vo.TradeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
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

    @Autowired
    private TradeQueryProvider tradeQueryProvider;

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
        operator.setPlatform(Platform.WX_VIDEO);
        operator.setUserId(returnOrderVO.getBuyer().getId());
        operator.setName(returnOrderVO.getBuyer().getName());
        operator.setStoreId(returnOrderVO.getCompany().getStoreId().toString());
        operator.setIp("127.0.0.1");
        operator.setAccount(returnOrderVO.getBuyer().getAccount());
        operator.setCompanyInfoId(returnOrderVO.getCompany().getCompanyInfoId());
        return operator;
    }


    /**
     * 自定义 operator
     * @param tradeVo
     * @return
     */
    public Operator packOperator(TradeVO tradeVo) {
        Operator operator = new Operator();
        operator.setUserId(tradeVo.getBuyer().getId());
        operator.setName(tradeVo.getBuyer().getName());
        operator.setStoreId(tradeVo.getSupplier().getStoreId().toString());
        operator.setIp("127.0.0.1");
        operator.setAccount(tradeVo.getBuyer().getAccount());
        operator.setCompanyInfoId(tradeVo.getSupplier().getSupplierId());
        return operator;
    }

    /**
     * 测试信息
     * @param outOrderId
     * @param aftersaleId
     */
    public WxDetailAfterSaleResponse.AfterSalesOrder test(String outOrderId, Long aftersaleId) {
        //根据订单号获取订单详细信息
        TradeVO tradeVo = tradeQueryProvider.getById(TradeGetByIdRequest.builder().tid(outOrderId).build()).getContext().getTradeVO();
        TradeItemVO tradeItemVO = tradeVo.getTradeItems().get(0);
        String spuId = tradeItemVO.getSpuId();
        String skuId = tradeItemVO.getSkuId();

        WxDetailAfterSaleResponse.AfterSalesOrder rr = new WxDetailAfterSaleResponse.AfterSalesOrder();
        rr.setOutOrderId(outOrderId);
        rr.setOrderId(0L);
        rr.setAftersaleId(aftersaleId);
        WxDetailAfterSaleResponse.ProductInfo productInfo = new WxDetailAfterSaleResponse.ProductInfo();
        productInfo.setOutProductId(spuId);
        productInfo.setOutSkuId(skuId);
        productInfo.setProductCnt(1L);
        rr.setProductInfo(productInfo);
        List<WxDetailAfterSaleResponse.MediaListInfo> mediaList = new ArrayList<>();
        WxDetailAfterSaleResponse.MediaListInfo mediaListInfo = new WxDetailAfterSaleResponse.MediaListInfo();
        mediaListInfo.setType(1);
        mediaListInfo.setUrl("https://store.mp.video.tencent-cloud.com/160/20304/snscosdownload/SH/reserved/6xykWLEnztLbbeOaJhzSoLdv6eLTdIlCPJNBGRFauL4TPyWk8k1IDSX32r0DQ2wE9szEIg41DGXIYKTQJyLANA?token=x5Y29zUxcibBnw6ckn64avUb5INaoz0SINpKmsU5PqgxdPEibq5kFVaGewLZzdYH0F&idx=1&expire=1650818887");
        mediaListInfo.setThumbUrl("https://store.mp.video.tencent-cloud.com/160/20304/snscosdownload/SH/reserved/6xykWLEnztLbbeOaJhzSoLdv6eLTdIlCPJNBGRFauL4TPyWk8k1IDSX32r0DQ2wE9szEIg41DGXIYKTQJyLANA?token=x5Y29zUxcibBnw6ckn64avUb5INaoz0SINpKmsU5PqgxdPEibq5kFVaGewLZzdYH0F&idx=1&expire=1650818887");
        mediaList.add(mediaListInfo);
        rr.setMediaList(mediaList);
        rr.setType(1);
        WxDetailAfterSaleResponse.ReturnInfo returnInfo = new WxDetailAfterSaleResponse.ReturnInfo();
        returnInfo.setOrderReturnTime(0L);
        returnInfo.setWaybillId("");
        rr.setReturnInfo(returnInfo);
        rr.setOrderamt(tradeItemVO.getSplitPrice().multiply(new BigDecimal("100")).longValue());
        rr.setRefundReasonType(2);
        rr.setRefundReason("111252345");
        rr.setStatus(2);
        rr.setCreate_time("1650546747341");
        rr.setUpdate_time("1650546747341");
        rr.setOpenid("oj6KP5A1Ca0rPVPCVq0kA0aQ6mQM");
        rr.setRefundPayDetail(new WxDetailAfterSaleResponse.RefundPayDetail());

        return rr;
    }
}
