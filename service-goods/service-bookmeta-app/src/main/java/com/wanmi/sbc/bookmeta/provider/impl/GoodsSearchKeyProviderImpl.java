package com.wanmi.sbc.bookmeta.provider.impl;

import com.google.common.collect.Lists;
import com.wanmi.sbc.bookmeta.bo.GoodsBO;
import com.wanmi.sbc.bookmeta.bo.GoodsKeyWordsDownLoadBO;
import com.wanmi.sbc.bookmeta.bo.GoodsNameBySpuIdBO;
import com.wanmi.sbc.bookmeta.bo.GoodsSearchKeyAddBo;
import com.wanmi.sbc.bookmeta.entity.GoodSearchKey;
import com.wanmi.sbc.bookmeta.mapper.AuthorityMapper;
import com.wanmi.sbc.bookmeta.mapper.GoodsSearchKeyMapper;
import com.wanmi.sbc.bookmeta.mapper.SaleNumMapper;
import com.wanmi.sbc.bookmeta.provider.GoodsSearchKeyProvider;
import com.wanmi.sbc.common.base.BusinessResponse;
import com.wanmi.sbc.common.base.Page;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.goods.bean.vo.GoodsVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: pushicheng
 * @Date: 2023/03/03/16:49
 * @Description:
 */
@Validated
@RestController
public class GoodsSearchKeyProviderImpl implements GoodsSearchKeyProvider {

    @Resource
    GoodsSearchKeyMapper goodsSearchKeyMapper;
    @Resource
    SaleNumMapper saleNumMapper;
    @Override
    public List<GoodsNameBySpuIdBO> getGoodsNameBySpuId(String name) {
        List<GoodSearchKey> goodsNameBySpuId = goodsSearchKeyMapper.getGoodsNameBySpuId(name);
        List<GoodsNameBySpuIdBO> goodsNameBySpuIdBOS = new ArrayList<>();
        if (goodsNameBySpuId.size() > 0) {
            goodsNameBySpuIdBOS = KsBeanUtil.convertList(goodsNameBySpuId, GoodsNameBySpuIdBO.class);
        }
        return goodsNameBySpuIdBOS;
    }

    @Override
    public BusinessResponse<List<GoodsNameBySpuIdBO>> getAllGoodsSearchKey(GoodsNameBySpuIdBO bo) {
        Page page = bo.getPage();
        page.setTotalCount((int) goodsSearchKeyMapper.getAllGoodsSearchKeyCount(bo.getSpuId()));
        if (page.getTotalCount() <= 0) {
            return BusinessResponse.success(Collections.EMPTY_LIST, page);
        }
        GoodSearchKey convert = KsBeanUtil.convert(bo, GoodSearchKey.class);
        List<GoodSearchKey> allGoodsSearchKey = goodsSearchKeyMapper.getAllGoodsSearchKey(bo.getName(), page.getOffset(), page.getPageSize());
        List<GoodsNameBySpuIdBO> goodsNameBySpuIdBOS = new ArrayList<>();
        if (allGoodsSearchKey.size() > 0) {
            for (GoodSearchKey g: allGoodsSearchKey) {
                GoodsNameBySpuIdBO convert1 = KsBeanUtil.convert(g, GoodsNameBySpuIdBO.class);
                convert1.setName(g.getName().substring(1,g.getName().length()-1));
                goodsNameBySpuIdBOS.add(convert1);
            }
        }
        return BusinessResponse.success(goodsNameBySpuIdBOS,page);
    }

    @Override
    public int addGoodsSearchKey(GoodsSearchKeyAddBo goodsSearchKeyAddBo) {
        GoodSearchKey convert = KsBeanUtil.convert(goodsSearchKeyAddBo, GoodSearchKey.class);
        convert.setCreateTime(new Date());
        int i = goodsSearchKeyMapper.insertGoodsSearchKey(convert);
        return i;
    }

    @Override
    public int updateGoodsSearchKey(GoodsSearchKeyAddBo goodsSearchKeyAddBo) {
        GoodSearchKey convert = KsBeanUtil.convert(goodsSearchKeyAddBo, GoodSearchKey.class);
        int i = goodsSearchKeyMapper.updateGoodsSearchKey(convert);
        return i;
    }

    @Override
    public int deleteGoodsSearchKey(GoodsNameBySpuIdBO goodsNameBySpuIdBO) {
        return goodsSearchKeyMapper.deleteGoodsSearchKey(goodsNameBySpuIdBO.getId());
    }

    @Override
    public BusinessResponse<List<GoodsBO>> getGoodsList(GoodsNameBySpuIdBO bo) {
        Page page = bo.getPage();
        page.setTotalCount((int) goodsSearchKeyMapper.getAllGoodsCount(bo.getName()));
        if (page.getTotalCount() <= 0) {
            return BusinessResponse.success(Collections.EMPTY_LIST, page);
        }
        List<GoodsVO> allGoodsSearchKey = goodsSearchKeyMapper.getGoodsList(bo.getName(), page.getOffset(), page.getPageSize());
        List<GoodsBO> allGoods = new ArrayList<GoodsBO>();
        if (allGoodsSearchKey.size() > 0){
            allGoods=KsBeanUtil.convertList(allGoodsSearchKey,GoodsBO.class);
        }
        return BusinessResponse.success(allGoods,page);
    }

    @Override
    public List<GoodsKeyWordsDownLoadBO> downloadQuery() {
        List<GoodSearchKey> goodSearchKeys = goodsSearchKeyMapper.downLoadQuery();
        List<GoodsKeyWordsDownLoadBO> goodsBOS = KsBeanUtil.convertList(goodSearchKeys, GoodsKeyWordsDownLoadBO.class);
        return goodsBOS;
    }

    @Override
    public BusinessResponse<String> importGoodsSearchKey(GoodsSearchKeyAddBo goodsSearchKeyAddBo) {
        boolean spuExit = saleNumMapper.existSpu(goodsSearchKeyAddBo.getSpuId()) > 0;
        int addCount = 0;
        int updateCount = 0;
        if (spuExit){
            boolean isExist= goodsSearchKeyMapper.isExistGoodsSearchKey(goodsSearchKeyAddBo.getName(),goodsSearchKeyAddBo.getSpuId())>0;
            GoodSearchKey convert = KsBeanUtil.convert(goodsSearchKeyAddBo, GoodSearchKey.class);
            if (isExist){
                goodsSearchKeyMapper.updateGoodsSearchKey(convert);
                updateCount++;
            }else {
                goodsSearchKeyMapper.insertGoodsSearchKey(convert);
                addCount++;
            }
        }else {
            return BusinessResponse.success("failed spuId:" +goodsSearchKeyAddBo.getSpuId()+" is not exist");
        }
        return BusinessResponse.success("success add "+addCount+"update "+updateCount);
    }
}
