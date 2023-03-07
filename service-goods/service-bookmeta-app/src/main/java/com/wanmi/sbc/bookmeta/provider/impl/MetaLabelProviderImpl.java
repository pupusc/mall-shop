package com.wanmi.sbc.bookmeta.provider.impl;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.bookmeta.bo.*;
import com.wanmi.sbc.bookmeta.entity.*;
import com.wanmi.sbc.bookmeta.enums.LabelStatusEnum;
import com.wanmi.sbc.bookmeta.mapper.MetaLabelMapper;
import com.wanmi.sbc.bookmeta.provider.MetaLabelProvider;
import com.wanmi.sbc.bookmeta.service.MetaLabelService;
import com.wanmi.sbc.common.base.BusinessResponse;
import com.wanmi.sbc.common.base.Page;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.common.util.StringUtil;
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
        return BusinessResponse.success(true);
    }

    @Override
    public List<Map> queryAllLabel() {
        return metaLabelMapper.getAllLabel();
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
        return BusinessResponse.success(metaLabelBOS);
    }

    @Override
    public BusinessResponse<List<MetaLabelBO>> getLabelByGoodsId(MetaLabelQueryByPageReqBO metaLabelQueryByPageReqBO) {
        MetaLabelV2 convert1 = KsBeanUtil.convert(metaLabelQueryByPageReqBO, MetaLabelV2.class);
        List<MetaLabelV2> labelByGoodsId = metaLabelMapper.getLabelByGoodsIdOrLabelId(convert1);
        List<MetaLabelBO> convert = KsBeanUtil.convert(labelByGoodsId, MetaLabelBO.class);
        return BusinessResponse.success(convert);

    }

    @Override
    public BusinessResponse<Integer> insertGoodsLabel(GoodsLabelSpuReqBO bo) {
        GoodsLabelSpu convert = KsBeanUtil.convert(bo, GoodsLabelSpu.class);
        if (!metaLabelMapper.existsWithPrimaryKey(convert.getLabelId())){
            return BusinessResponse.error("Invalidate Label");
        }
        if (metaLabelMapper.isExistGoods(convert.getGoodsId()) < 1) {
            return BusinessResponse.error("Invalidate Goods");
        }
        convert.setCreateTime(new Date());
            int i = metaLabelMapper.addGoodsLabelSpu(convert);
            return BusinessResponse.success(i);
    }

    @Override
    public BusinessResponse<Integer> updateGoodsLabel(GoodsLabelSpuReqBO bo) {
        GoodsLabelSpu convert = KsBeanUtil.convert(bo, GoodsLabelSpu.class);
        if (!metaLabelMapper.existsWithPrimaryKey(convert.getLabelId())){
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
        for(int i=0;i<list.size();i++){
            Map map = (Map)list.get(i);
            String id = String.valueOf(map.get("id"));
            int intId = Integer.parseInt(id);
            List childList = metaLabelMapper.getLabelCate(intId);
            map.put("childList",childList);
        }
        return list;
    }

    public List<Map> getLabelCate2(String parent_id) {
        int int_id = Integer.parseInt(parent_id);
        List list = metaLabelMapper.getLabelCate(int_id);
        for(int i=0;i<list.size();i++){
            Map map = (Map)list.get(i);
            String id = String.valueOf(map.get("id"));
            int intId = Integer.parseInt(id);
            List childList = metaLabelMapper.getLabelCate(intId);
            map.put("childList",childList);
        }
        return list;
    }


    @Override
    public SkuDetailBO getGoodsInfoBySpuId(String id) {
        SkuDetailBO bo = new SkuDetailBO();
        Map map = metaLabelMapper.getSkuIdBySpuId(id);
        String skuId = (String) map.get("goods_info_id");
        if (StringUtils.isBlank(skuId)){
            map=metaLabelMapper.getSkuIdBySpuId1(id);
            skuId=(String) map.get("goods_info_id");
        }
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


}
