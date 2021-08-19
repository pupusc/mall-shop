package com.wanmi.sbc.intervalprice;

import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.goods.api.provider.price.GoodsIntervalPriceProvider;
import com.wanmi.sbc.goods.api.request.price.GoodsIntervalPriceByCustomerIdRequest;
import com.wanmi.sbc.goods.api.request.price.GoodsIntervalPriceByGoodsAndSkuRequest;
import com.wanmi.sbc.goods.api.request.price.GoodsIntervalPriceRequest;
import com.wanmi.sbc.goods.api.response.price.GoodsIntervalPriceByCustomerIdResponse;
import com.wanmi.sbc.goods.api.response.price.GoodsIntervalPriceByGoodsAndSkuResponse;
import com.wanmi.sbc.goods.api.response.price.GoodsIntervalPriceResponse;
import com.wanmi.sbc.goods.bean.dto.GoodsDTO;
import com.wanmi.sbc.goods.bean.dto.GoodsInfoDTO;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.goods.bean.vo.GoodsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * com.wanmi.sbc.intervalprice.GoodsIntervalPriceService
 *
 * @author lipeng
 * @dateTime 2018/11/13 下午4:43
 */
@Service
public class GoodsIntervalPriceService {

    @Autowired
    private GoodsIntervalPriceProvider goodsIntervalPriceProvider;

    /**
     * 根据商品VO列表和客户编号获取区间价列表和填充最大/最小价的商品VO列表
     * @param goodsInfoVOList 商品VO列表
     * @param customerId 客户编号
     * @return 包含商品VO列表/区间价列表的响应结构 {@link GoodsIntervalPriceByCustomerIdResponse}
     */
    public GoodsIntervalPriceByCustomerIdResponse getGoodsIntervalPriceVOList(List<GoodsInfoVO> goodsInfoVOList, String customerId) {
        List<GoodsInfoDTO> goodsInfoDTOList = KsBeanUtil.convert(goodsInfoVOList, GoodsInfoDTO.class);
        return getGoodsIntervalPriceVOListByDTOList(goodsInfoDTOList, customerId);
    }

    /**
     * 根据商品DTO列表和客户编号获取区间价列表和填充最大/最小价的商品VO列表
     * @param goodsInfoDTOList 商品DTO列表
     * @param customerId 客户编号
     * @return 包含商品VO列表/区间价列表的响应结构 {@link GoodsIntervalPriceByCustomerIdResponse}
     */
    public GoodsIntervalPriceByCustomerIdResponse getGoodsIntervalPriceVOListByDTOList(
            List<GoodsInfoDTO> goodsInfoDTOList, String customerId) {
        return goodsIntervalPriceProvider.putByCustomerId(
                GoodsIntervalPriceByCustomerIdRequest.builder()
                        .goodsInfoDTOList(goodsInfoDTOList).customerId(customerId).build()).getContext();
    }


    /**
     * 根据商品VO列表获取区间价列表和填充最大/最小价的商品VO列表
     * @param goodsInfoVOList 商品VO列表
     * @return 包含商品VO列表/区间价列表的响应结构 {@link GoodsIntervalPriceByCustomerIdResponse}
     */
    public GoodsIntervalPriceResponse getGoodsIntervalPriceVOList(List<GoodsInfoVO> goodsInfoVOList) {
        List<GoodsInfoDTO> goodsInfoDTOList = KsBeanUtil.convert(goodsInfoVOList, GoodsInfoDTO.class);
        return goodsIntervalPriceProvider.put(
                GoodsIntervalPriceRequest.builder().goodsInfoDTOList(goodsInfoDTOList).build()).getContext();
    }

    /**
     * 根据商品以及SKU的VO列表获取区间价列表和填充最大/最小价的商品VO列表
     * @param goodsInfoVOList 商品SkuVO列表
     * @param goodsVOList 商品VO列表
     * @param customerId 会员id
     * @return 包含商品以及Sku的VO列表/区间价列表的响应结构 {@link GoodsIntervalPriceByCustomerIdResponse}
     */
    public GoodsIntervalPriceByGoodsAndSkuResponse getGoodsAndSku(List<GoodsInfoVO> goodsInfoVOList, List<GoodsVO> goodsVOList, String customerId) {
        List<GoodsInfoDTO> goodsInfoDTOList = KsBeanUtil.convert(goodsInfoVOList, GoodsInfoDTO.class);
        List<GoodsDTO> goodsDTOList = KsBeanUtil.convert(goodsVOList, GoodsDTO.class);
        return goodsIntervalPriceProvider.putGoodsAndSku(
                GoodsIntervalPriceByGoodsAndSkuRequest.builder().goodsInfoDTOList(goodsInfoDTOList)
                        .goodsDTOList(goodsDTOList).customerId(customerId).build()).getContext();
    }
}
