package com.wanmi.sbc.goods.presellsale.service;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.goods.bean.vo.PresellSaleGoodsVO;
import com.wanmi.sbc.goods.bean.vo.PresellSaleVO;
import com.wanmi.sbc.goods.presellsale.model.root.PresellSaleGoods;
import com.wanmi.sbc.goods.presellsale.repository.PresellSaleGoodsRepository;
import com.wanmi.sbc.goods.api.request.presellsale.PresellSaleGoodsSaveRequest;
import com.wanmi.sbc.goods.redis.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Service
public class PresellSaleGoodsService {

    /**
     * 预售商品购买数量缓存Redis，key固定前缀
     */
    public static final String PRESELL_SALE_COUNT = "presellSaleCount";

    @Autowired
    private PresellSaleGoodsRepository presellSaleGoodsRepository;

    @Autowired
    private RedisService redisService;


    /**
     * 单个新增活动关联商品记录
     * @param presellSaleGoodsSaveRequest
     * @param milli
     */
    public void addPresellSaleGoods(PresellSaleGoodsSaveRequest presellSaleGoodsSaveRequest, long milli){
        PresellSaleGoods presellSaleGoods = new PresellSaleGoods();
        KsBeanUtil.convert(presellSaleGoodsSaveRequest, PresellSaleGoods.class);
        presellSaleGoods.setHandselNum(0);
        presellSaleGoods.setFinalPaymentNum(0);
        presellSaleGoods.setFullPaymentNum(0);
        presellSaleGoods.setCreateTime(LocalDateTime.now());
        presellSaleGoods.setUpdateTime(LocalDateTime.now());
        presellSaleGoods.setDelFlag(DeleteFlag.NO);



        //将活动商品的购买量写入redis
        presellSaleGoodsRepository.saveAndFlush(presellSaleGoods);
        String presellSaleCount = presellSaleGoods.getPresellSaleCount().toString();
        String id = presellSaleGoods.getId();
        redisService.setString(PRESELL_SALE_COUNT+id,presellSaleCount,milli);
    }
    /**
     * 创建预售活动关联商品记录
     * @param presellSaleGoodsSaveRequestList
     */
    public List<PresellSaleGoodsVO> add(List<PresellSaleGoodsSaveRequest> presellSaleGoodsSaveRequestList, Long milli) {
        List<PresellSaleGoodsVO> list= new LinkedList<>();
        for (PresellSaleGoodsSaveRequest presellSaleGoodsSaveRequest : presellSaleGoodsSaveRequestList) {

            PresellSaleGoods presellSaleGoods = KsBeanUtil.convert(presellSaleGoodsSaveRequest, PresellSaleGoods.class);
            presellSaleGoods.setHandselNum(0);
            presellSaleGoods.setFinalPaymentNum(0);
            presellSaleGoods.setFullPaymentNum(0);
            presellSaleGoods.setCreateTime(LocalDateTime.now());
            presellSaleGoods.setUpdateTime(LocalDateTime.now());
            presellSaleGoods.setDelFlag(DeleteFlag.NO);
            presellSaleGoodsRepository.save(presellSaleGoods);

            //将活动商品的购买量写入redis
            String presellSaleCount = presellSaleGoods.getPresellSaleCount().toString();
            String id = presellSaleGoods.getId();
            //根据活动的结束时间设置过期时间往后加上整一天
            redisService.setString(PRESELL_SALE_COUNT+id,presellSaleCount,milli);

            PresellSaleGoodsVO presellSaleGoodsVO = KsBeanUtil.convert(presellSaleGoods, PresellSaleGoodsVO.class);
            presellSaleGoodsVO.setPresellSaleGoodsId(id);
            list.add(presellSaleGoodsVO);
        }
        return list;
    }

    /**
     * 根据预售活动id查询预售商品信息
     * @param presellSaleId
     */
    public List<PresellSaleGoodsVO>  findPresellSaleGoodsByPresellSaleId(String presellSaleId) {

        //根据预售活动id查询关联商品信息
        List<PresellSaleGoods> presellSaleGoodsList = presellSaleGoodsRepository.findAllByPresellSaleId(presellSaleId);
        List<PresellSaleGoodsVO> list= new LinkedList<>();
        presellSaleGoodsList.forEach(presellSaleGoods->{
            PresellSaleGoodsVO presellSaleGoodsVO = KsBeanUtil.convert(presellSaleGoods, PresellSaleGoodsVO.class);
            presellSaleGoodsVO.setPresellSaleGoodsId(presellSaleGoods.getId());
            list.add(presellSaleGoodsVO);
        });
        return list;
    }


    /**
     * 根据预售活动id删除所有相关连的商品
     * @param presellSaleId
     */
    public int deleteAllPresellSaleGoods(String presellSaleId){


        return presellSaleGoodsRepository.deletePresellSaleGoods(presellSaleId);
    }


    /**
     * 根据预售活动关联商品id查询关联商品信息
     * @param presellSaleGoodsId
     * @return
     */
    public PresellSaleGoodsVO findPresellSaleGoodsById(String presellSaleGoodsId){
        PresellSaleGoods presellSaleGoods = presellSaleGoodsRepository.findSaleGoods(presellSaleGoodsId);
        if(presellSaleGoods!=null){
            PresellSaleGoodsVO presellSaleGoodsVO = KsBeanUtil.convert(presellSaleGoods, PresellSaleGoodsVO.class);
            return presellSaleGoodsVO;
        }
       return null;

    }

    /**
     * 根据预售活动id和预售活动关联商品id查询预售活动关联商品信息
     * @param presellSaleId
     * @param goodsInfoId
     * @return
     */
    public PresellSaleGoodsVO findPresellSaleGoodsByPresellSaleIdAndGoodsInfoId(String presellSaleId, String goodsInfoId) {
        PresellSaleGoods presellSaleGoods = presellSaleGoodsRepository.findSaleGoodsByGoodsInfoId(presellSaleId, goodsInfoId);
        if(presellSaleGoods!=null){
            PresellSaleGoodsVO presellSaleGoodsVO = KsBeanUtil.convert(presellSaleGoods, PresellSaleGoodsVO.class);
            return presellSaleGoodsVO;
        }
        return null;
    }
}
