package com.wanmi.sbc.bookmeta.provider.impl;

import com.wanmi.sbc.bookmeta.bo.MetaTradeBO;
import com.wanmi.sbc.bookmeta.entity.MetaTrade;
import com.wanmi.sbc.bookmeta.mapper.MetaTradeMapper;
import com.wanmi.sbc.bookmeta.provider.MetaTradeProvider;
import com.wanmi.sbc.common.util.KsBeanUtil;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: pushicheng
 * @Date: 2023/03/03/16:33
 * @Description:
 */
@Validated
@RestController
public class MetaTradeProviderImpl implements MetaTradeProvider {

    @Resource
    MetaTradeMapper metaTradeMapper;
    @Override
    public List<MetaTradeBO> getMetaTadeTree(int parentId) {
        List<MetaTrade> parents= metaTradeMapper.getMetaTradeTree(parentId);
        List<MetaTradeBO> metaTradeBOList = KsBeanUtil.convertList(parents, MetaTradeBO.class);
        for (MetaTradeBO trade:metaTradeBOList) {
            List<MetaTrade> child = metaTradeMapper.getMetaTradeTree(trade.getId());
            List<MetaTradeBO> metaTradeBOS = KsBeanUtil.convertList(child, MetaTradeBO.class);
            for (MetaTradeBO son:metaTradeBOS) {
                List<MetaTrade> sons = metaTradeMapper.getMetaTradeTree(son.getId());
                List<MetaTradeBO> tradeBOS = KsBeanUtil.convertList(sons, MetaTradeBO.class);
                son.setChildrenList(tradeBOS);
            }
            trade.setChildrenList(metaTradeBOS);
        }
        return metaTradeBOList;
    }

    @Override
    public int addMetaTade(MetaTradeBO metaTradeBO) {
        metaTradeBO.setCreateTime(new Date());
        MetaTrade metaTrade = KsBeanUtil.convert(metaTradeBO, MetaTrade.class);
        return metaTradeMapper.insertMetaTrade(metaTrade);
    }

    @Override
    public int updateMetaTade(MetaTradeBO metaTradeBO) {
        MetaTrade metaTrade = KsBeanUtil.convert(metaTradeBO, MetaTrade.class);
        return metaTradeMapper.updateMetaTrade(metaTrade);
    }

    @Override
    public int deleteMetaTade(int id) {
        return metaTradeMapper.deleteMetaTrade(id);
    }
}
