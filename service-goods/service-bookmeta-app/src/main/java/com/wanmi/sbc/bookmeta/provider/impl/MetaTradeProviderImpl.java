package com.wanmi.sbc.bookmeta.provider.impl;

import com.wanmi.sbc.bookmeta.bo.MetaTradeBO;
import com.wanmi.sbc.bookmeta.bo.MetaTradePageQueryBO;
import com.wanmi.sbc.bookmeta.bo.MetaTradePageQueryRespBO;
import com.wanmi.sbc.bookmeta.entity.MetaTrade;
import com.wanmi.sbc.bookmeta.mapper.MetaTradeMapper;
import com.wanmi.sbc.bookmeta.provider.MetaTradeProvider;
import com.wanmi.sbc.common.util.KsBeanUtil;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.*;

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
    public MetaTradePageQueryRespBO getMetaTadeTree(MetaTradePageQueryBO pageQueryBO) {
/*        List<MetaTrade> parents= metaTradeMapper.getMetaTradeTree(parentId);
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
        }*/

        List<MetaTrade> result = metaTradeMapper.getAllMetaTradeNode();
        HashMap nodeList = new HashMap();
        //最后结果节点列表
        HashMap nodeResultList = new HashMap();
        //根节点

        List<MetaTradeBO> resultList = new ArrayList<MetaTradeBO>();

        MetaTradeBO node =new MetaTradeBO();
        //设置根节点，id为0
        nodeResultList.put(0, resultList);

        //根据结果集构造节点列表（存入散列表）
        for(int i=0;i<result.size();i++){
            MetaTradeBO convert = KsBeanUtil.convert(result.get(i), MetaTradeBO.class);
            nodeList.put(result.get(i).getId(), convert);
        }

        //构造无序的多叉树 //entrySet() 该方法返回值是这个map中各个键值对映射关系的集合
        Set entrySet = nodeList.entrySet();
        //value=entrySet.iterator().next().getValue() key=entrySet.iterator().next().getKey()
        for (Iterator it = entrySet.iterator(); it.hasNext();) {
            node = (MetaTradeBO) ((Map.Entry) it.next()).getValue();
            int i= 1;
            //如果parentId有对应的id，则作为孩子节点加入对应的父节点中
            for (Iterator it2 = entrySet.iterator(); it2.hasNext();) {
                MetaTradeBO node2 = (MetaTradeBO) ((Map.Entry) it2.next()).getValue();
                if (node.getParentId() == node2.getId()) {
                    ((MetaTradeBO) nodeList.get(node.getParentId())).addChild(node);
                    System.out.println(node.getParentId());
                    i++;
                    break;
                }
            }
            //若parentId都无对应的id，则为一级节点，加入根节点中
            if(i==1){
                ((List<MetaTradeBO>)nodeResultList.get(0)).add(node);
            }
        }

        resultList = (List<MetaTradeBO>) nodeResultList.get(0);
        System.out.println(resultList);
        List<MetaTradeBO> ts = KsBeanUtil.convert(resultList, MetaTradeBO.class);

        List<MetaTradeBO> listSort = new ArrayList<>();
        int size = ts.size();
        Integer page = pageQueryBO.getPageNo();
        Integer rows = pageQueryBO.getPageNum();
        int pageStart = page == 1 ? 0 : (page - 1) * rows;//截取的开始位置
        int pageEnd = size < page * rows ? size : page * rows;//截取的结束位置
        if (size > pageStart) {
            listSort = ts.subList(pageStart, pageEnd);
        }
        //总页数
        int totalPage = 0;
        if (ts.size() % rows == 0) {
            totalPage = ts.size() / rows;
        } else {
            totalPage = ts.size() / rows + 1;
        }
        MetaTradePageQueryRespBO metaTradePageQueryRespBO = new MetaTradePageQueryRespBO();
        metaTradePageQueryRespBO.setMetaTrades(listSort);
        metaTradePageQueryRespBO.setTotalPage(totalPage);
        metaTradePageQueryRespBO.setTotal(size);
        return metaTradePageQueryRespBO;
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
