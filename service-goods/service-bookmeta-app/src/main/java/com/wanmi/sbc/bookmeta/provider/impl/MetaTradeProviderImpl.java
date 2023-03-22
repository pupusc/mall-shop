package com.wanmi.sbc.bookmeta.provider.impl;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.bookmeta.bo.MetaTradeBO;
import com.wanmi.sbc.bookmeta.bo.MetaTradePageQueryBO;
import com.wanmi.sbc.bookmeta.bo.MetaTradePageQueryRespBO;
import com.wanmi.sbc.bookmeta.entity.MetaTrade;
import com.wanmi.sbc.bookmeta.mapper.MetaTradeMapper;
import com.wanmi.sbc.bookmeta.provider.MetaTradeProvider;
import com.wanmi.sbc.common.util.DateUtil;
import com.wanmi.sbc.common.util.KsBeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
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
@Slf4j
@RestController
public class MetaTradeProviderImpl implements MetaTradeProvider {

    @Resource
    MetaTradeMapper metaTradeMapper;

    @Override
    public MetaTradePageQueryRespBO getMetaTadeTree(MetaTradePageQueryBO pageQueryBO) {
        MetaTradePageQueryRespBO metaTradePageQueryRespBO = new MetaTradePageQueryRespBO();
        try {
            List<MetaTrade> result = metaTradeMapper.getAllMetaTradeNode();
            HashMap nodeList = new HashMap();
            //最后结果节点列表
            HashMap nodeResultList = new HashMap();
            //根节点

            List<MetaTradeBO> resultList = new ArrayList<MetaTradeBO>();

            MetaTradeBO node = new MetaTradeBO();
            nodeResultList.put(0, resultList);
            for (int i = 0; i < result.size(); i++) {
                MetaTradeBO convert = KsBeanUtil.convert(result.get(i), MetaTradeBO.class);
                nodeList.put(result.get(i).getId(), convert);
            }
            Set entrySet = nodeList.entrySet();
            for (Iterator it = entrySet.iterator(); it.hasNext(); ) {
                node = (MetaTradeBO) ((Map.Entry) it.next()).getValue();
                int i = 1;
                for (Iterator it2 = entrySet.iterator(); it2.hasNext(); ) {
                    MetaTradeBO node2 = (MetaTradeBO) ((Map.Entry) it2.next()).getValue();
                    if (node.getParentId() == node2.getId()) {
                        ((MetaTradeBO) nodeList.get(node.getParentId())).addChild(node);
                        System.out.println(node.getParentId());
                        i++;
                        break;
                    }
                }
                if (i == 1) {
                    ((List<MetaTradeBO>) nodeResultList.get(0)).add(node);
                }
            }

            resultList = (List<MetaTradeBO>) nodeResultList.get(0);
            List<MetaTradeBO> ts = KsBeanUtil.convert(resultList, MetaTradeBO.class);
            List<MetaTradeBO> listSort = new ArrayList<>();
            int size = ts.size();
            Integer page = pageQueryBO.getPageNo();
            Integer rows = pageQueryBO.getPageNum();
            int pageStart = page == 1 ? 0 : (page - 1) * rows;
            int pageEnd = size < page * rows ? size : page * rows;
            if (size > pageStart) {
                listSort = ts.subList(pageStart, pageEnd);
            }
            int totalPage = 0;
            if (ts.size() % rows == 0) {
                totalPage = ts.size() / rows;
            } else {
                totalPage = ts.size() / rows + 1;
            }

            metaTradePageQueryRespBO.setMetaTrades(listSort);
            metaTradePageQueryRespBO.setTotalPage(totalPage);
            metaTradePageQueryRespBO.setTotal(size);
        } catch (Exception e) {
            log.error("时间:{},方法:{},入口参数:{},执行异常,Cause:{}",
                    DateUtil.format(new Date(), DateUtil.FMT_TIME_1),
                    "getMetaTadeTree",
                    Objects.isNull(pageQueryBO) ? "" : JSON.toJSONString(pageQueryBO),
                    e);
        }

        return metaTradePageQueryRespBO;
    }

    @Override
    public int addMetaTade(MetaTradeBO metaTradeBO) {
        int i = 0;
        try {
            metaTradeBO.setCreateTime(new Date());
            MetaTrade metaTrade = KsBeanUtil.convert(metaTradeBO, MetaTrade.class);
            i = metaTradeMapper.insertMetaTrade(metaTrade);
        }catch (Exception e) {
            log.error("时间:{},方法:{},入口参数:{},执行异常,Cause:{}",
                    DateUtil.format(new Date(), DateUtil.FMT_TIME_1),
                    "addMetaTade",
                    Objects.isNull(metaTradeBO) ? "" : JSON.toJSONString(metaTradeBO),
                    e);
        }

        return i;
    }

    @Override
    public int updateMetaTade(MetaTradeBO metaTradeBO) {
        int i = 0;
        try {
            MetaTrade metaTrade = KsBeanUtil.convert(metaTradeBO, MetaTrade.class);
            i = metaTradeMapper.updateMetaTrade(metaTrade);
        }catch (Exception e){
            log.error("时间:{},方法:{},入口参数:{},执行异常,Cause:{}",
                    DateUtil.format(new Date(), DateUtil.FMT_TIME_1),
                    "updateMetaTade",
                    Objects.isNull(metaTradeBO) ? "" : JSON.toJSONString(metaTradeBO),
                    e);
        }
        return i;
    }

    @Override
    @Transactional
    public int deleteMetaTade(int id) {
        int deleteCount = 0;

        try {
            metaTradeMapper.deleteMetaTrade(id);
            deleteCount++;
            List<MetaTrade> metaTradeTree = metaTradeMapper.getMetaTradeTree(id);
            for (MetaTrade metaTrade : metaTradeTree) {
                metaTradeMapper.deleteMetaTrade(metaTrade.getId());
                deleteCount++;
            }
        }catch (Exception e){
            log.error("时间:{},方法:{},入口参数:{},执行异常,Cause:{}",
                    DateUtil.format(new Date(), DateUtil.FMT_TIME_1),
                    "deleteMetaTade",
                    Objects.isNull(id) ? "" : JSON.toJSONString(id),
                    e);
        }
        return deleteCount;
    }
}
