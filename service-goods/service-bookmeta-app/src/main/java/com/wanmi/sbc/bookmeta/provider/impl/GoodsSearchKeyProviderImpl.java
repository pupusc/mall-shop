package com.wanmi.sbc.bookmeta.provider.impl;

import com.alibaba.fastjson.JSON;
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
import com.wanmi.sbc.common.util.DateUtil;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.goods.bean.vo.GoodsVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: pushicheng
 * @Date: 2023/03/03/16:49
 * @Description:
 */
@Validated
@Slf4j
@RestController
public class GoodsSearchKeyProviderImpl implements GoodsSearchKeyProvider {

    @Resource
    GoodsSearchKeyMapper goodsSearchKeyMapper;
    @Resource
    SaleNumMapper saleNumMapper;

    @Override
    public List<GoodsNameBySpuIdBO> getGoodsNameBySpuId(String name) {
        List<GoodsNameBySpuIdBO> goodsNameBySpuIdBOS = new ArrayList<>();
        try {
            List<GoodSearchKey> goodsNameBySpuId = goodsSearchKeyMapper.getGoodsNameBySpuId(name);
            if (goodsNameBySpuId.size() > 0) {
                goodsNameBySpuIdBOS = KsBeanUtil.convertList(goodsNameBySpuId, GoodsNameBySpuIdBO.class);
            }
        } catch (Exception e) {
            log.error("时间:{},方法:{},入口参数:{},执行异常,Cause:{}",
                    DateUtil.format(new Date(), DateUtil.FMT_TIME_1),
                    "getSaleNum",
                    Objects.isNull(name) ? "" : JSON.toJSONString(name),
                    e);
        }

        return goodsNameBySpuIdBOS;
    }

    @Override
    public BusinessResponse<List<GoodsNameBySpuIdBO>> getAllGoodsSearchKey(GoodsNameBySpuIdBO bo) {
        Page page = bo.getPage();
        List<GoodsNameBySpuIdBO> goodsNameBySpuIdBOS = new ArrayList<>();
        try {
            page.setTotalCount((int) goodsSearchKeyMapper.getAllGoodsSearchKeyCount(bo.getName(), bo.getSpuId()));
            if (page.getTotalCount() <= 0) {
                return BusinessResponse.success(Collections.EMPTY_LIST, page);
            }
            List<GoodSearchKey> allGoodsSearchKey = goodsSearchKeyMapper.getAllGoodsSearchKey(bo.getName(), bo.getSpuId(), page.getOffset(), page.getPageSize());
            goodsNameBySpuIdBOS = KsBeanUtil.convertList(allGoodsSearchKey, GoodsNameBySpuIdBO.class);
        } catch (Exception e) {
            log.error("时间:{},方法:{},入口参数:{},执行异常,Cause:{}",
                    DateUtil.format(new Date(), DateUtil.FMT_TIME_1),
                    "getAllGoodsSearchKey",
                    Objects.isNull(bo) ? "" : JSON.toJSONString(bo),
                    e);
        }
        return BusinessResponse.success(goodsNameBySpuIdBOS, page);
    }

    @Override
    public int addGoodsSearchKey(GoodsSearchKeyAddBo goodsSearchKeyAddBo) {
        int i = 0;
        try {
            if (goodsSearchKeyAddBo.getId() != null && goodsSearchKeyMapper.isExistGoodsSearchKeyById(goodsSearchKeyAddBo.getId()) > 0) {
                goodsSearchKeyMapper.deleteGoodsSearchKey(goodsSearchKeyAddBo.getId());
            }
            GoodSearchKey convert = KsBeanUtil.convert(goodsSearchKeyAddBo, GoodSearchKey.class);
            convert.setCreateTime(new Date());
            i = goodsSearchKeyMapper.insertGoodsSearchKey(convert);
        } catch (Exception e) {
            log.error("时间:{},方法:{},入口参数:{},执行异常,Cause:{}",
                    DateUtil.format(new Date(), DateUtil.FMT_TIME_1),
                    "addGoodsSearchKey",
                    Objects.isNull(goodsSearchKeyAddBo) ? "" : JSON.toJSONString(goodsSearchKeyAddBo),
                    e);
        }
        return i;
    }

    @Override
    public int updateGoodsSearchKey(GoodsSearchKeyAddBo goodsSearchKeyAddBo) {
        int i = 0;
        try {
            GoodSearchKey convert = KsBeanUtil.convert(goodsSearchKeyAddBo, GoodSearchKey.class);
            i = goodsSearchKeyMapper.updateGoodsSearchKey(convert);
        } catch (Exception e) {
            log.error("时间:{},方法:{},入口参数:{},执行异常,Cause:{}",
                    DateUtil.format(new Date(), DateUtil.FMT_TIME_1),
                    "updateGoodsSearchKey",
                    Objects.isNull(goodsSearchKeyAddBo) ? "" : JSON.toJSONString(goodsSearchKeyAddBo),
                    e);
        }

        return i;
    }

    @Override
    public int deleteGoodsSearchKey(GoodsNameBySpuIdBO goodsNameBySpuIdBO) {
        int i = 0;
        try {
            i = goodsSearchKeyMapper.deleteGoodsSearchKey(goodsNameBySpuIdBO.getId());
        } catch (Exception e) {
            log.error("时间:{},方法:{},入口参数:{},执行异常,Cause:{}",
                    DateUtil.format(new Date(), DateUtil.FMT_TIME_1),
                    "deleteGoodsSearchKey",
                    Objects.isNull(goodsNameBySpuIdBO) ? "" : JSON.toJSONString(goodsNameBySpuIdBO),
                    e);
        }
        return i;
    }

    @Override
    public BusinessResponse<List<GoodsBO>> getGoodsList(GoodsNameBySpuIdBO bo) {
        Page page = bo.getPage();
        List<GoodsBO> allGoods = new ArrayList<GoodsBO>();
        try {
            page.setTotalCount((int) goodsSearchKeyMapper.getAllGoodsCount(bo.getName()));
            if (page.getTotalCount() <= 0) {
                return BusinessResponse.success(Collections.EMPTY_LIST, page);
            }
            List<GoodsVO> allGoodsSearchKey = goodsSearchKeyMapper.getGoodsList(bo.getName(), page.getOffset(), page.getPageSize());

            if (allGoodsSearchKey.size() > 0) {
                allGoods = KsBeanUtil.convertList(allGoodsSearchKey, GoodsBO.class);
            }
        } catch (Exception e) {
            log.error("时间:{},方法:{},入口参数:{},执行异常,Cause:{}",
                    DateUtil.format(new Date(), DateUtil.FMT_TIME_1),
                    "getGoodsList",
                    Objects.isNull(bo) ? "" : JSON.toJSONString(bo),
                    e);
        }

        return BusinessResponse.success(allGoods, page);
    }

    @Override
    public List<GoodsKeyWordsDownLoadBO> downloadQuery() {
        List<GoodsKeyWordsDownLoadBO> goodsBOS = new ArrayList<>();
        try {
            List<GoodSearchKey> goodSearchKeys = goodsSearchKeyMapper.downLoadQuery();
            goodsBOS = KsBeanUtil.convertList(goodSearchKeys, GoodsKeyWordsDownLoadBO.class);
        } catch (Exception e) {
            log.error("时间:{},方法:{},入口参数:{},执行异常,Cause:{}",
                    DateUtil.format(new Date(), DateUtil.FMT_TIME_1),
                    "downloadQuery",
                    "无参数",
                    e);
        }

        return goodsBOS;
    }

    @Override
    public BusinessResponse<String> importGoodsSearchKey(GoodsSearchKeyAddBo goodsSearchKeyAddBo) {

        int addCount = 0;
        int updateCount = 0;
        try {
            boolean spuExit = saleNumMapper.existSpu(goodsSearchKeyAddBo.getSpuId()) > 0;
            GoodSearchKey convert = KsBeanUtil.convert(goodsSearchKeyAddBo, GoodSearchKey.class);

                if (goodsSearchKeyAddBo.getType() == 1) {
                    boolean relSpuExit = saleNumMapper.existSpuRelation(goodsSearchKeyAddBo.getRelSpuId(), goodsSearchKeyAddBo.getRelSkuId()) > 0;
                    if (spuExit && relSpuExit) {
                        if (StringUtils.isNotBlank(String.valueOf(convert.getId()))) {
                            goodsSearchKeyMapper.updateGoodsSearchKey(convert);
                            updateCount++;
                        }
                        goodsSearchKeyMapper.insertGoodsSearchKey(convert);
                        addCount++;
                    } else {
                        return BusinessResponse.success("failed spuId:" + goodsSearchKeyAddBo.getSpuId() + " is not exist");
                    }
                } else if (goodsSearchKeyAddBo.getType() == 2) {
                    if (spuExit) {
                        if (StringUtils.isNotBlank(String.valueOf(convert.getId()))) {
                            goodsSearchKeyMapper.updateGoodsSearchKey(convert);
                            updateCount++;
                        }
                        goodsSearchKeyMapper.insertGoodsSearchKey(convert);
                        addCount++;
                    }
                }

        } catch (Exception e) {
            log.error("时间:{},方法:{},入口参数:{},执行异常,Cause:{}",
                    DateUtil.format(new Date(), DateUtil.FMT_TIME_1),
                    "importGoodsSearchKey",
                    Objects.isNull(goodsSearchKeyAddBo) ? "" : JSON.toJSONString(goodsSearchKeyAddBo),
                    e);
        }
        return BusinessResponse.success("success add " + addCount + "update " + updateCount);
    }

    @Override
    public List<Map<String, Object>> getList(GoodsSearchKeyAddBo goodsSearchKeyAddBo) {
        return goodsSearchKeyMapper.getSpuAndSkuByName(goodsSearchKeyAddBo.getName());
    }
}
