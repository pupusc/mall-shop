package com.wanmi.sbc.convert;

import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.util.KsBeanUtil;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * ConvertUtils
 *
 * @author lipeng
 * @dateTime 2018/11/8 下午5:12
 */
public class ConvertUtils {

    public static <T> T convert(Object source, Class<T> targetClass) {
        String sourceJsonStr = JSONObject.toJSONString(source);
        T target = JSONObject.parseObject(sourceJsonStr, targetClass);
        return target;
    }

    public static <S,T> List<T> convertList(List<S> sourceList, Class<T> targetClass) {
        String sourceListJsonStr = JSONObject.toJSONString(sourceList);
        List<T> targetList = JSONObject.parseArray(sourceListJsonStr, targetClass);
        return targetList;
    }

    public static <S,T> MicroServicePage<T> convertPage(Page<S> sourcePage, Class<T> targetClass) {
        return new MicroServicePage<T>(
                convertList(sourcePage.getContent(), targetClass),
                 PageRequest.of(sourcePage.getNumber(), sourcePage.getSize(), sourcePage.getSort()),
                sourcePage.getTotalElements()
        );
    }

    @Test
    public void testConvert() {
        GoodsInfoDTO goodsInfoDTO = getGoodsInfoDTO();
        GoodsInfo goodsInfo = ConvertUtils.convert(goodsInfoDTO, GoodsInfo.class);
        List<GoodsMarket> goodsMarketList = goodsInfo.getGoodsMarketDTOList();
        for (GoodsMarket goodsMarket : goodsMarketList) {
            System.out.println("===============================");
            System.out.println(goodsMarket instanceof GoodsMarket);
            System.out.println(goodsMarket.getName());
            System.out.println("===============================");
        }
        System.out.println(JSONObject.toJSONString(goodsInfo));
    }

    @Test
    public void testConvertList() {
        List<GoodsInfoDTO> goodsInfoDTOList = getGoodsInfoDTOList();
        List<GoodsInfo> goodsInfoList = convertList(goodsInfoDTOList, GoodsInfo.class);
        for (GoodsInfo goodsInfo : goodsInfoList) {
            System.out.println("===============================");
            System.out.println(goodsInfo instanceof GoodsInfo);
            System.out.println(goodsInfo.getGoodsInfoName());
            System.out.println("===============================");
        }
        System.out.println(JSONObject.toJSON(goodsInfoList));
    }

    @Test
    public void testConvertPage() {
        List<GoodsInfoDTO> list = getGoodsInfoDTOList();
        Page<GoodsInfoDTO> page = new PageImpl<>(list, PageRequest.of(0, 10), list.size());
        MicroServicePage<GoodsInfo> infoPage = KsBeanUtil.convertPage(page, GoodsInfo.class);
        for (GoodsInfo goodsInfo : infoPage.getContent()) {
            System.out.println("===============================");
            System.out.println(goodsInfo instanceof GoodsInfo);
            System.out.println(goodsInfo.getGoodsInfoName());
            System.out.println("===============================");
        }
        System.out.println(JSONObject.toJSON(infoPage));
    }

    private List<GoodsInfoDTO> getGoodsInfoDTOList() {
        List<GoodsInfoDTO> goodsInfoDTOList = new ArrayList<>();

        // 设置GoodsInfoDTO
        GoodsInfoDTO goodsInfoDTO = new GoodsInfoDTO();
        goodsInfoDTO.setGoodsInfoId(10001);
        goodsInfoDTO.setGoodsInfoName("goodsInfoDTO1");
        goodsInfoDTOList.add(goodsInfoDTO);

        // 设置GoodsMarketDTOList
        List<GoodsMarketDTO> goodsMarketDTOList = new ArrayList<>();
        GoodsMarketDTO goodsMarketDTO = new GoodsMarketDTO();
        goodsMarketDTO.setId(10001);
        goodsMarketDTO.setName("goodsMarketDTO1");
        goodsMarketDTOList.add(goodsMarketDTO);

        // 设置GoodsPriceDTO
        GoodsPriceDTO goodsPriceDTO = new GoodsPriceDTO();
        goodsPriceDTO.setId(10001);
        goodsPriceDTO.setName("goodsPriceDTO1");
        goodsMarketDTO.setGoodsPriceDTO(goodsPriceDTO);

        goodsMarketDTO = new GoodsMarketDTO();
        goodsMarketDTO.setId(10002);
        goodsMarketDTO.setName("goodsMarketDTO2");
        goodsMarketDTOList.add(goodsMarketDTO);

        goodsPriceDTO = new GoodsPriceDTO();
        goodsPriceDTO.setId(10002);
        goodsPriceDTO.setName("goodsPriceDTO2");
        goodsMarketDTO.setGoodsPriceDTO(goodsPriceDTO);

        goodsMarketDTO = new GoodsMarketDTO();
        goodsMarketDTO.setId(10003);
        goodsMarketDTO.setName("goodsMarketDTO3");
        goodsMarketDTOList.add(goodsMarketDTO);

        goodsPriceDTO = new GoodsPriceDTO();
        goodsPriceDTO.setId(10003);
        goodsPriceDTO.setName("goodsPriceDTO3");
        goodsMarketDTO.setGoodsPriceDTO(goodsPriceDTO);

        // 设置GoodsMarketDTOList
        goodsInfoDTO.setGoodsMarketDTOList(goodsMarketDTOList);

        goodsMarketDTO = new GoodsMarketDTO();
        goodsMarketDTO.setId(10004);
        goodsMarketDTO.setName("goodsMarketDTO4");
        // 设置GoodsMarketDTO
        goodsInfoDTO.setGoodsMarketDTO(goodsMarketDTO);

        goodsInfoDTO = new GoodsInfoDTO();
        goodsInfoDTO.setGoodsInfoId(10002);
        goodsInfoDTO.setGoodsInfoName("goodsInfoDTO2");
        goodsInfoDTOList.add(goodsInfoDTO);

        goodsInfoDTO = new GoodsInfoDTO();
        goodsInfoDTO.setGoodsInfoId(10003);
        goodsInfoDTO.setGoodsInfoName("goodsInfoDTO3");
        goodsInfoDTOList.add(goodsInfoDTO);

        return goodsInfoDTOList;
    }

    private GoodsInfoDTO getGoodsInfoDTO () {
        GoodsInfoDTO goodsInfoDTO = new GoodsInfoDTO();
        List<GoodsMarketDTO> goodsMarketDTOList = new ArrayList<>();

        GoodsMarketDTO goodsMarketDTO = new GoodsMarketDTO();
        GoodsPriceDTO goodsPriceDTO = new GoodsPriceDTO();
        goodsMarketDTO.setId(1);
        goodsMarketDTO.setName("market1");
        goodsPriceDTO.setId(1);
        goodsPriceDTO.setName("price1");
        goodsPriceDTO.setPrice(BigDecimal.ONE);
        goodsMarketDTO.setGoodsPriceDTO(goodsPriceDTO);
        goodsMarketDTOList.add(goodsMarketDTO);

        goodsMarketDTO = new GoodsMarketDTO();
        goodsPriceDTO = new GoodsPriceDTO();
        goodsMarketDTO.setId(2);
        goodsMarketDTO.setName("market2");
        goodsPriceDTO.setId(2);
        goodsPriceDTO.setName("price2");
        goodsPriceDTO.setPrice(BigDecimal.TEN);
        goodsMarketDTO.setGoodsPriceDTO(goodsPriceDTO);
        goodsMarketDTOList.add(goodsMarketDTO);

        goodsMarketDTO = new GoodsMarketDTO();
        goodsPriceDTO = new GoodsPriceDTO();
        goodsMarketDTO.setId(3);
        goodsMarketDTO.setName("market3");
        goodsPriceDTO.setId(3);
        goodsPriceDTO.setName("price3");
        goodsPriceDTO.setPrice(BigDecimal.TEN);
        goodsMarketDTO.setGoodsPriceDTO(goodsPriceDTO);
        goodsMarketDTOList.add(goodsMarketDTO);

        goodsMarketDTO = new GoodsMarketDTO();
        goodsPriceDTO = new GoodsPriceDTO();
        goodsMarketDTO.setId(66);
        goodsPriceDTO.setId(4);
        goodsPriceDTO.setName("price4");
        goodsPriceDTO.setPrice(BigDecimal.TEN);
        goodsMarketDTO.setGoodsPriceDTO(goodsPriceDTO);
        goodsMarketDTO.setName("market66");

        goodsInfoDTO.setGoodsInfoId(10001);
        goodsInfoDTO.setGoodsInfoName("goodsInfoName");
        goodsInfoDTO.setGoodsMarketDTO(goodsMarketDTO);
        goodsInfoDTO.setGoodsMarketDTOList(goodsMarketDTOList);
        return goodsInfoDTO;
    }
}
