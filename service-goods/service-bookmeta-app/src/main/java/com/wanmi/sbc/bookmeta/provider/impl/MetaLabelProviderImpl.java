package com.wanmi.sbc.bookmeta.provider.impl;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.bookmeta.bo.*;
import com.wanmi.sbc.bookmeta.entity.*;
import com.wanmi.sbc.bookmeta.enums.LabelStatusEnum;
import com.wanmi.sbc.bookmeta.mapper.MetaBookMapper;
import com.wanmi.sbc.bookmeta.mapper.MetaLabelMapper;
import com.wanmi.sbc.bookmeta.provider.MetaLabelProvider;
import com.wanmi.sbc.bookmeta.service.MetaLabelService;
import com.wanmi.sbc.common.base.BusinessResponse;
import com.wanmi.sbc.common.base.Page;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.common.util.StringUtil;
import com.wanmi.sbc.goods.bean.vo.GoodsVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.*;

/**
 * 标签(MetaLabel)表服务实现类
 *
 * @author Liang Jun
 * @since 2022-05-17 11:38:03
 */
@Validated
@RestController
public class MetaLabelProviderImpl implements MetaLabelProvider {
    @Resource
    private MetaLabelMapper metaLabelMapper;
    @Resource
    private MetaLabelService metaLabelService;
    @Resource
    private MetaBookMapper metaBookMapper;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public BusinessResponse<MetaLabelBO> queryById(Integer id) {
        return BusinessResponse.success(DO2BOUtils.objA2objB(this.metaLabelMapper.queryById(id), MetaLabelBO.class));
    }

    /**
     * 分页查询
     *
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @Override
    public BusinessResponse<List<MetaLabelBO>> queryByPage(@Valid MetaLabelQueryByPageReqBO pageRequest) {
        Page page = pageRequest.getPage();
        MetaLabel metaLabel = JSON.parseObject(JSON.toJSONString(pageRequest), MetaLabel.class);

        page.setTotalCount((int) this.metaLabelMapper.countExt(metaLabel));
        if (page.getTotalCount() <= 0) {
            return BusinessResponse.success(Collections.EMPTY_LIST, page);
        }

        //List<MetaLabel> labels = this.metaLabelMapper.queryAllByLimit(metaLabel, page.getOffset(), page.getPageSize());
        List<MetaLabelExt> labels = this.metaLabelMapper.queryAllByLimitExt(metaLabel, page.getOffset(), page.getPageSize());
        return BusinessResponse.success(DO2BOUtils.objA2objB4List(labels, MetaLabelBO.class), page);
    }

    /**
     * 新增数据
     *
     * @param metaLabelBO 实例对象
     * @return 实例对象
     */
    @Override
    public BusinessResponse<Integer> insert(@Valid MetaLabelBO metaLabelBO) {
        MetaLabel metaLabel = DO2BOUtils.objA2objB(metaLabelBO, MetaLabel.class);
        validate(metaLabel, true);
        this.metaLabelMapper.insertSelective(metaLabel);
        return BusinessResponse.success(metaLabel.getId());
    }

    /**
     * 修改数据
     *
     * @param metaLabelBO 实例对象
     * @return 实例对象
     */
    @Override
    public BusinessResponse<Boolean> update(@Valid MetaLabelBO metaLabelBO) {
        MetaLabel metaLabel = DO2BOUtils.objA2objB(metaLabelBO, MetaLabel.class);
        validate(metaLabel, false);

        MetaLabel entity = this.metaLabelMapper.selectByPrimaryKey(metaLabelBO.getId());
        if (entity == null) {
            throw new SbcRuntimeException(CommonErrorCode.DATA_NOT_EXISTS);
        }
        entity.setParentId(metaLabelBO.getParentId());
        entity.setPath(metaLabelBO.getPath());
        entity.setName(metaLabelBO.getName());
        entity.setSeq(metaLabel.getSeq());
        entity.setScene(metaLabel.getScene());
        entity.setDescr(metaLabel.getDescr());
        entity.setStatus(metaLabel.getStatus());
        entity.setIsStatic(metaLabel.getIsStatic());
        entity.setIsRun(metaLabel.getIsRun());
        entity.setRunFromTime(metaLabel.getRunFromTime());
        entity.setRunToTime(metaLabel.getRunToTime());
        entity.setShowStatus(metaLabel.getShowStatus());
        entity.setShowImg(metaLabel.getShowImg());
        entity.setShowText(metaLabel.getShowText());
        entity.setRemark(metaLabel.getRemark());
        entity.setIsShow(metaLabel.getIsShow());
        this.metaLabelMapper.updateByPrimaryKey(entity);
        return BusinessResponse.success(true);
    }

    /**
     * 修改数据
     *
     * @param metaLabelBO 实例对象
     * @return 实例对象
     */
    @Override
    public BusinessResponse<Boolean> updateName(@Valid MetaLabelBO metaLabelBO) {
        MetaLabel metaLabel = DO2BOUtils.objA2objB(metaLabelBO, MetaLabel.class);
        validate(metaLabel, false);

        MetaLabel entity = this.metaLabelMapper.selectByPrimaryKey(metaLabelBO.getId());
        if (entity == null) {
            throw new SbcRuntimeException(CommonErrorCode.DATA_NOT_EXISTS);
        }
        entity.setName(metaLabelBO.getName());
        this.metaLabelMapper.updateByPrimaryKey(entity);
        return BusinessResponse.success(true);
    }

    @Override
    public BusinessResponse<Boolean> updateStatus(MetaLabelUpdateStatusReqBO metaLabel) {
        MetaLabel update = new MetaLabel();
        update.setId(metaLabel.getId());
        update.setStatus(Boolean.TRUE.equals(metaLabel.getEnable()) ? LabelStatusEnum.ENABLE.getCode() : LabelStatusEnum.DISABLE.getCode());
        this.metaLabelMapper.updateByPrimaryKeySelective(update);
        return BusinessResponse.success(true);
    }

    private void validate(MetaLabel metaLabel, boolean newly) {
        Example example = new Example(MetaLabel.class);
        Example.Criteria criteria = example.createCriteria()
                .andEqualTo("name", metaLabel.getName())
                .andEqualTo("delFlag", 0);
        if (!newly) {
            criteria.andNotEqualTo("id", metaLabel.getId());
        }

        if (this.metaLabelMapper.selectCountByExample(example) > 0) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR, "相同名称已存在");
        }
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public BusinessResponse<Boolean> deleteById(Integer id) {
        this.metaLabelService.deleteById(id);
        List<Map> labelCate = metaLabelMapper.getLabelCate(id);
        for (Map label : labelCate) {
            metaLabelMapper.deleteById((Integer) label.get("id"));
        }
        return BusinessResponse.success(true);
    }

    @Override
    public List<Map> queryAllLabel() {
        return metaLabelMapper.getAllLabel();
    }

    @Override
    public List<GoodsLabelSpuReqBO> queryAllGoodsLabel() {
        List<GoodsLabelSpu> goodsLabel = metaLabelMapper.getGoodsLabel();
        List<GoodsLabelSpuReqBO> goodsLabelSpuReqBOS = KsBeanUtil.convertList(goodsLabel, GoodsLabelSpuReqBO.class);
        return goodsLabelSpuReqBOS;
    }

    @Override
    public List<GoodsBO> queryAllGoods() {
        List<GoodsVO> goodsList = metaLabelMapper.getGoodsList();
        List<GoodsBO> goodsBOS = KsBeanUtil.convertList(goodsList, GoodsBO.class);
        return goodsBOS;
    }

    @Override
    public BusinessResponse<String> importGoodsLabel(GoodsLabelSpuReqBO goodsLabelSpuReqBO) {
        int updateCount = 0;
        int addCount = 0;
        if (StringUtils.isBlank(goodsLabelSpuReqBO.getGoodsId())) {
            return BusinessResponse.error("failed,GoodsId can't be blank");
        }
        if (StringUtils.isBlank(String.valueOf(goodsLabelSpuReqBO.getLabelId()))) {
            return BusinessResponse.error("failed,SKU_ID can't be blank");
        }
        boolean goodsExist = metaLabelMapper.existGoods(goodsLabelSpuReqBO.getGoodsId()) > 0;
        if (goodsExist) {
            boolean labelExit = metaLabelMapper.existLabel(goodsLabelSpuReqBO.getLabelId()) > 0;
            if (labelExit) {
                List<GoodsLabelSpu> goodsLabelSpuList = metaLabelMapper.getExistGoodsLabel(goodsLabelSpuReqBO.getGoodsId(), goodsLabelSpuReqBO.getLabelId());
                if (null != goodsLabelSpuList && goodsLabelSpuList.size() > 0) {//该数据已存在，更新该条数据
                    GoodsLabelSpu goodsLabelSpu = goodsLabelSpuList.get(0);
                    goodsLabelSpu.setGoodsId(goodsLabelSpuReqBO.getGoodsId());
                    goodsLabelSpu.setLabelId(goodsLabelSpuReqBO.getLabelId());
                    goodsLabelSpu.setFirstId(goodsLabelSpuReqBO.getFirstId());
                    goodsLabelSpu.setSecondId(goodsLabelSpuReqBO.getSecondId());
                    goodsLabelSpu.setOrderNum(goodsLabelSpuReqBO.getOrderNum());
                    updateCount = metaLabelMapper.updateGoodsLabelSpu(goodsLabelSpu);
                } else {
                    GoodsLabelSpu goodsLabelSpu = new GoodsLabelSpu();
                    goodsLabelSpu.setGoodsId(goodsLabelSpuReqBO.getGoodsId());
                    goodsLabelSpu.setLabelId(goodsLabelSpuReqBO.getLabelId());
                    goodsLabelSpu.setFirstId(goodsLabelSpuReqBO.getFirstId());
                    goodsLabelSpu.setSecondId(goodsLabelSpuReqBO.getSecondId());
                    goodsLabelSpu.setOrderNum(goodsLabelSpuReqBO.getOrderNum());
                    Date date = new Date();
                    goodsLabelSpuReqBO.setCreateTime(date);
                    goodsLabelSpuReqBO.setUpdateTime(date);
                    addCount = metaLabelMapper.addGoodsLabelSpu(goodsLabelSpu);
                }
            }
            else {
                return BusinessResponse.error("doesn't exist "+goodsLabelSpuReqBO.getLabelId()+" Label");
            }
        }
        else {
            return BusinessResponse.error("doesn't exist "+goodsLabelSpuReqBO.getGoodsId()+" Goods");
        }
        return BusinessResponse.success("Success,update " + updateCount + " and add " + addCount + "!");
    }

    @Override
    public BusinessResponse<List<MetaLabelBO>> getLabels(MetaLabelQueryByPageReqBO pageReqBO) {
        Page page = pageReqBO.getPage();
        page.setTotalCount((int) metaLabelMapper.getLabelsCount(pageReqBO.getName()));
        if (page.getTotalCount() <= 0) {
            return BusinessResponse.success(Collections.EMPTY_LIST, page);
        }
        List<MetaLabelV2> labels = metaLabelMapper.getLabels(pageReqBO.getName(), page.getOffset(), page.getPageSize());
        List<MetaLabelBO> metaLabelBOS = KsBeanUtil.convertList(labels, MetaLabelBO.class);
        return BusinessResponse.success(metaLabelBOS, page);
    }

    @Override
    public BusinessResponse<List<MetaLabelBO>> getLabelByGoodsId(MetaLabelQueryByPageReqBO metaLabelQueryByPageReqBO) {
        MetaLabelV2 convert1 = KsBeanUtil.convert(metaLabelQueryByPageReqBO, MetaLabelV2.class);
        Page page = metaLabelQueryByPageReqBO.getPage();
        page.setTotalCount((int) metaLabelMapper.getLabelByGoodsIdOrLabelIdCount(convert1));
        if (page.getTotalCount() <= 0) {
            return BusinessResponse.success(Collections.EMPTY_LIST, page);
        }
        List<MetaLabelV2> labelByGoodsId = metaLabelMapper.getLabelByGoodsIdOrLabelId(convert1,page.getOffset(), page.getPageSize());
        List<MetaLabelBO> convert = KsBeanUtil.convert(labelByGoodsId, MetaLabelBO.class);
        return BusinessResponse.success(convert,page);

    }

    @Override
    public BusinessResponse<String> insertGoodsLabel(GoodsLabelSpuReqBO bo) {
        GoodsLabelSpu convert = KsBeanUtil.convert(bo, GoodsLabelSpu.class);
        if (!metaLabelMapper.existsWithPrimaryKey(convert.getLabelId())) {
            return BusinessResponse.error("Invalidate Label");
        }
        if (metaLabelMapper.isExistGoods(convert.getGoodsId()) < 1) {
            return BusinessResponse.error("Invalidate Goods");
        }
        if (metaLabelMapper.getExistGoodsLabel(convert.getGoodsId(),convert.getLabelId()).size()>0){
            return BusinessResponse.success("failed,已有该商品标签");
        }
        convert.setCreateTime(new Date());
        int i = metaLabelMapper.addGoodsLabelSpu(convert);
        return BusinessResponse.success("success");
    }

    @Override
    public BusinessResponse<Integer> updateGoodsLabel(GoodsLabelSpuReqBO bo) {
        GoodsLabelSpu convert = KsBeanUtil.convert(bo, GoodsLabelSpu.class);
        if (!metaLabelMapper.existsWithPrimaryKey(convert.getLabelId())) {
            return BusinessResponse.error("Invalidate Label");
        }
        if (metaLabelMapper.isExistGoods(convert.getGoodsId()) < 1) {
            return BusinessResponse.error("Invalidate Goods");
        }
        int i = metaLabelMapper.updateGoodsLabelSpu(convert);
        return BusinessResponse.success(i);
    }

    @Override
    public Integer deleteGoodsLabel(GoodsLabelSpuReqBO bo) {
        return metaLabelMapper.deleteGoodsLabel(bo.getId());
    }

    @Override
    public List<Map> getLabelCate(int parent_id) {
        List list = metaLabelMapper.getLabelCate(parent_id);
        for (int i = 0; i < list.size(); i++) {
            Map map = (Map) list.get(i);
            String id = String.valueOf(map.get("id"));
            int intId = Integer.parseInt(id);
            List childList = metaLabelMapper.getLabelCate(intId);
            for (int j = 0; j < childList.size(); j++) {
                Map map1 = (Map) childList.get(j);
                String id1 = String.valueOf(map1.get("id"));
                int intId1 = Integer.parseInt(id1);
                List childList1 = metaLabelMapper.getLabelCate2(intId1);
                map1.put("childList", childList1);
            }
            map.put("childList", childList);
        }
        return list;
    }

    public List<Map> getLabelCate2(String parent_id) {
        int int_id = Integer.parseInt(parent_id);
        List list = metaLabelMapper.getLabelCate(int_id);
        for (int i = 0; i < list.size(); i++) {
            Map map = (Map) list.get(i);
            String id = String.valueOf(map.get("id"));
            int intId = Integer.parseInt(id);
            List childList = metaLabelMapper.getLabelCate(intId);
            map.put("childList", childList);
        }
        return list;
    }


    @Override
    public SkuDetailBO getGoodsInfoBySpuId(String id) {
        SkuDetailBO bo = new SkuDetailBO();
        Map map = metaLabelMapper.getSkuIdBySpuId(id);
        if (map == null) {
            map = metaLabelMapper.getSkuIdBySpuId1(id);
        }
        String skuId = (String) map.get("goods_info_id");
        String img = (String) map.get("goods_info_img");
        String SkuName = (String) map.get("goods_info_name");
        BigDecimal price = (BigDecimal) map.get("market_price");
        String score = metaLabelMapper.getScoreBySkuId(id);
        String Isbn = metaLabelMapper.getIsbnBySkuId(id);
        String saleNum = metaLabelMapper.getSaleNumSkuId(skuId);
        bo.setSkuId(skuId);
        bo.setImg(img);
        bo.setSkuId(skuId);
        bo.setScore(score);
        if (StringUtils.isNotBlank(Isbn)) {
            bo.setIsbn(Isbn);
        }
        bo.setSaleNum(saleNum);
        bo.setSkuName(SkuName);
        bo.setPrice(price);
        return bo;
    }

    @Override
    public SkuDetailBO getGoodsInfoBySkuId(String skuId) {
        SkuDetailBO bo = new SkuDetailBO();
        Map map = metaLabelMapper.getSkuIdBySkuId(skuId);
        String spuId = (String) map.get("goods_id");
        String img = (String) map.get("goods_info_img");
        String SkuName = (String) map.get("goods_info_name");
        BigDecimal price = (BigDecimal) map.get("market_price");
        String score = metaLabelMapper.getScoreBySkuId(spuId);
        String Isbn = metaLabelMapper.getIsbnBySkuId(spuId);
        String saleNum = metaLabelMapper.getSaleNumSkuId(skuId);
        bo.setSpuId(spuId);
        bo.setSkuId(skuId);
        bo.setImg(img);
        bo.setSkuId(skuId);
        bo.setScore(score);
        if (StringUtils.isNotBlank(Isbn)) {
            bo.setIsbn(Isbn);
        }
        bo.setSaleNum(saleNum);
        bo.setSkuName(SkuName);
        bo.setPrice(price);
        return bo;
    }

    @Override
    public BusinessResponse<List<GoodDetailOtherRespBO>> getGoodsDetailAndOther(GoodsOtherDetailBO bo) {
        Page page = bo.getPage();
        page.setTotalCount((int) metaLabelMapper.getGoodsOtherDetailCount(bo.getGoodsName()));
        if (page.getTotalCount() <= 0) {
            return BusinessResponse.success(Collections.EMPTY_LIST, page);
        }
        List<GoodsOtherDetail> goodsOtherDetail = metaLabelMapper.getGoodsOtherDetail(bo.getGoodsName(), page.getOffset(), page.getPageSize());
        List<GoodDetailOtherRespBO> goodDetailOtherRespBOS = KsBeanUtil.convertList(goodsOtherDetail, GoodDetailOtherRespBO.class);
        return BusinessResponse.success(goodDetailOtherRespBOS,page);
    }

    @Override
    public int updateGoodsDetailAndOther(GoodDetailOtherRespBO goodDetailOtherRespBO) {
        GoodsOtherDetail convert = KsBeanUtil.convert(goodDetailOtherRespBO, GoodsOtherDetail.class);
        return metaLabelMapper.updateGoodsOtherDetail(convert);
    }

    @Override
    public List<MetaLabelBO> getType2Label(LabelListReqVO reqVO) {
        List<MetaLabel> type2Label = metaLabelMapper.getType2Label(reqVO.getName());
        List<MetaLabelBO> metaLabelBOS = KsBeanUtil.convertList(type2Label, MetaLabelBO.class);
        return metaLabelBOS;
    }


}
