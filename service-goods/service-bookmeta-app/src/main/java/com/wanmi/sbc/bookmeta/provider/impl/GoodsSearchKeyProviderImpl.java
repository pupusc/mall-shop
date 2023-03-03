package com.wanmi.sbc.bookmeta.provider.impl;

import com.wanmi.sbc.bookmeta.bo.GoodsNameBySpuIdBO;
import com.wanmi.sbc.bookmeta.bo.GoodsSearchKeyAddBo;
import com.wanmi.sbc.bookmeta.entity.GoodSearchKey;
import com.wanmi.sbc.bookmeta.mapper.AuthorityMapper;
import com.wanmi.sbc.bookmeta.mapper.GoodsSearchKeyMapper;
import com.wanmi.sbc.bookmeta.provider.GoodsSearchKeyProvider;
import com.wanmi.sbc.common.base.BusinessResponse;
import com.wanmi.sbc.common.base.Page;
import com.wanmi.sbc.common.util.KsBeanUtil;
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
        List<GoodSearchKey> allGoodsSearchKey = goodsSearchKeyMapper.getAllGoodsSearchKey(bo.getSpuId(), page.getOffset(), page.getPageSize());
        List<GoodsNameBySpuIdBO> goodsNameBySpuIdBOS = new ArrayList<>();
        if (allGoodsSearchKey.size() > 0) {
            goodsNameBySpuIdBOS = KsBeanUtil.convertList(allGoodsSearchKey, GoodsNameBySpuIdBO.class);
        }
        return BusinessResponse.success(goodsNameBySpuIdBOS);
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
}
