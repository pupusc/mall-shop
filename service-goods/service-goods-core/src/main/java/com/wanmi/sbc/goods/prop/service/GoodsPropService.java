package com.wanmi.sbc.goods.prop.service;

import com.wanmi.sbc.goods.prop.model.root.GoodsPropCateRel;
import com.wanmi.sbc.goods.prop.repository.GoodsPropCateRelRepository;
import io.seata.spring.annotation.GlobalTransactional;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.goods.api.constant.GoodsPropErrorCode;
import com.wanmi.sbc.goods.cate.model.root.GoodsCate;
import com.wanmi.sbc.goods.cate.repository.GoodsCateRepository;
import com.wanmi.sbc.goods.info.model.root.Goods;
import com.wanmi.sbc.goods.info.model.root.GoodsPropDetailRel;
import com.wanmi.sbc.goods.info.repository.GoodsPropDetailRelRepository;
import com.wanmi.sbc.goods.info.repository.GoodsRepository;
import com.wanmi.sbc.goods.prop.model.root.GoodsProp;
import com.wanmi.sbc.goods.prop.model.root.GoodsPropDetail;
import com.wanmi.sbc.goods.prop.repository.GoodsPropDetailRepository;
import com.wanmi.sbc.goods.prop.repository.GoodsPropRepository;
import com.wanmi.sbc.goods.prop.request.GoodsPropQueryRequest;
import com.wanmi.sbc.goods.prop.request.GoodsPropRequest;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class GoodsPropService {
    @Autowired
    GoodsPropRepository goodsPropRepository;
    @Autowired
    GoodsPropDetailRepository goodsPropDetailRepository;
    @Autowired
    GoodsCateRepository goodsCateRepository;
    @Autowired
    GoodsPropDetailRelRepository goodsPropDetailRelRepository;
    @Autowired
    GoodsRepository goodsRepository;
    @Autowired
    GoodsPropCateRelRepository goodsPropCateRelRepository;


    /**
     *  根据类目ID查询商品属性
     * @param cateId
     * @return
     * @throws Exception
     */
    public List<GoodsProp> queryAllGoodPropsByCate(Long cateId){
        if(cateId == 0){
            return goodsPropRepository.findAllNew();
        }
        if (!isChildNode(cateId)) {
            throw new SbcRuntimeException(GoodsPropErrorCode.NOT_CHILD_NODE);
        }
        List<GoodsProp> goodsProps = goodsPropRepository.findAllByCateIdAndDelFlag(cateId);
        goodsProps.stream().map(goodsProp -> {
            if(goodsProp.getPropType() == 1 || goodsProp.getPropType() == 2){
                List<GoodsPropDetail> goodsPropDetails = goodsPropDetailRepository.findAllByPropIdAndDelFlagOrderBySortAsc(goodsProp.getPropId(),DeleteFlag.NO);
                StringBuilder stringBuilder = new StringBuilder();
                goodsPropDetails.stream().forEach(goodsPropDetail -> {
                    stringBuilder.append(goodsPropDetail.getDetailName());
                    stringBuilder.append("; ");
                });
                goodsProp.setPropDetailStr(stringBuilder.toString());
                goodsProp.setGoodsPropDetails(goodsPropDetails);
            }
            return goodsProp;
        }).collect(Collectors.toList());
        return goodsProps;
    }

    /**
     * 根据类目ID查询需要索引的商品属性列表
     * (供用户根据商品属性进行筛选商品)
     * @param cateId 分类id
     * @return List
     * @throws Exception
     */
    public List<GoodsProp> queryIndexGoodPropsByCate(Long cateId){
        if(Objects.nonNull(cateId)){
            GoodsCate goodsCate = goodsCateRepository.findById(cateId).orElse(null);
            // 必须是三级目录
            if (goodsCate != null && goodsCate.getCateGrade() == Constants.GOODS_LEAF_CATE_GRADE) {
                List<GoodsProp> goodsProps = goodsPropRepository.findAllByCateIdAndIndexFlagAndDelFlagOrderBySortAsc(cateId, DefaultFlag.YES, DeleteFlag.NO);
                return goodsProps.stream().map(goodsProp -> {
                    List<GoodsPropDetail> goodsPropDetails = goodsPropDetailRepository.findAllByPropIdAndDelFlagOrderBySortAsc(goodsProp.getPropId(),DeleteFlag.NO);
                    goodsProp.setGoodsPropDetails(goodsPropDetails);
                    return goodsProp;
                }).collect(Collectors.toList());
            }
        }
        return Collections.emptyList();
    }

    /**
     * 每次新增初始化排序
     * @param goodsPropRequest
     * @return
     * @throws Exception
     */
    public List<GoodsProp> initSort(GoodsPropRequest goodsPropRequest){
        Long lastPropId = goodsPropRequest.getLastPropId();
        //如何首次新增，返回空集合
        if (Objects.isNull(lastPropId)){
            return Collections.emptyList();
        }
        GoodsProp goodsProp = goodsPropRequest.getGoodsProp();
        Long cateId = goodsProp.getCateId();
        List<GoodsProp> goodsProps = goodsPropRepository.findAllByCateIdAndDelFlag(cateId);
        int lastIndex = 0;
        for (GoodsProp prop:goodsProps) {
            lastIndex++;
            if (lastPropId.equals(prop.getPropId())) {
                break;
            }
        }
        goodsProps.add(lastIndex,goodsProp);
        AtomicInteger sort = new AtomicInteger();
        goodsProps = goodsProps.stream().map(gProp -> {
            gProp.setSort(sort.intValue());
            sort.getAndIncrement();
            return gProp;
        }).collect(Collectors.toList());
        return goodsProps;
    }

    @Transactional
    public void editSort(List<GoodsProp> goodsProps){
        for (int i=0;i<goodsProps.size();i++) {
            GoodsProp goodsProp = goodsProps.get(i);
            goodsProp.setUpdateTime(LocalDateTime.now());
            int sort = i+1;
            goodsProp.setSort(sort);
        }
        goodsPropRepository.saveAll(goodsProps);
    }

    /**
     * 新增商品类目属性
     * @param goodsPropRequest
     * @return
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    public List<String> addGoodsProp(GoodsPropRequest goodsPropRequest){
        GoodsProp goodsProp = goodsPropRequest.getGoodsProp();
        Long cateId = goodsProp.getCateId();
        if(goodsProp.getPropType() == null){
            goodsProp.setPropType(1);
        }
        //验证是否是类目三级节点
        if (!isChildNode(cateId)) {
            throw new SbcRuntimeException(GoodsPropErrorCode.NOT_CHILD_NODE);
        }
        //验证是否商品类目下属性是否超限
        List<GoodsProp> goodsPropList = goodsPropRepository.findAllByCateIdAndDelFlag(cateId);
        if (goodsPropList.size()>=Constants.GOODS_PROP_REAL_SIZE) {
            throw new SbcRuntimeException(GoodsPropErrorCode.GOODSPROP_OVERSTEP);
        }
        //验证商品类目属性名称是否已存在
        if (isGoodsPropNameExist(goodsProp)>0){
            throw new SbcRuntimeException(GoodsPropErrorCode.GOODSPROPNAME_ALREADY_EXIST);
        }
        //验证商品类目属性值是否为空
        if(goodsProp.getPropType() == 1 && CollectionUtils.isEmpty(goodsProp.getGoodsPropDetails())){
            throw new SbcRuntimeException(GoodsPropErrorCode.GOODSPROP_NOT_EMPTY);
        }
        List<String> goodsIds = findGoodsIdsByCateId(cateId);
        goodsProp.setCreateTime(LocalDateTime.now());
        goodsProp.setDelFlag(DeleteFlag.NO);
        Long lastPropId = goodsPropRequest.getLastPropId();
        //第一次新增默认排序为1
        if (Objects.isNull(lastPropId)) {
            goodsProp.setSort(1);
        }
        //保存类目属性
        goodsPropRepository.save(goodsProp);

        saveGoodsPropCateRel(goodsProp);
        //排序后保存
        List<GoodsProp> goodsProps = initSort(goodsPropRequest);
        goodsPropRepository.saveAll(goodsProps);
        if(goodsProp.getPropType() == 1){
            //保存默认spu与默认属性的关联
            //saveDefaultRef(goodsIds,goodsProp.getPropId());
            List<GoodsPropDetail> goodsPropDetails = goodsProp.getGoodsPropDetails();
            //判断属性的属性值是否存在相同
            if (CollectionUtils.isEmpty(goodsPropDetails) || isGoodsPropDetailRepeat(goodsPropDetails)){
                throw new SbcRuntimeException(GoodsPropErrorCode.GOODSPROPDETAIL_REPEAT);
            }
            //检查属性值是否超限
            boolean detailOverStep = isDetailOverStep(goodsPropDetails);
            if (detailOverStep) {
                throw new SbcRuntimeException(GoodsPropErrorCode.GOODSPROPDETAIL_OVERSTEP);
            }
            //保存属性值
            Long propId = goodsProp.getPropId();
            goodsPropDetails.stream().forEach(goodsPropDetail -> {
                goodsPropDetail.setPropId(propId);
                goodsPropDetail.setCreateTime(LocalDateTime.now());
                goodsPropDetailRepository.save(goodsPropDetail);
                deleteRef(goodsPropDetail);
            });
        }
        return goodsIds;
    }

    public void saveGoodsPropCateRel(GoodsProp goodsProp){
        GoodsPropCateRel goodsPropCateRel = new GoodsPropCateRel();
        LocalDateTime now = LocalDateTime.now();
        goodsPropCateRel.setCateId(goodsProp.getCateId());
        goodsPropCateRel.setCreateTime(now);
        goodsPropCateRel.setUpdateTime(now);
        goodsPropCateRel.setDelFlag(DeleteFlag.NO);
        goodsPropCateRel.setPropId(goodsProp.getPropId());
        goodsPropCateRelRepository.save(goodsPropCateRel);
    }

    /**
     * 判断类目下属性名称是否已存在
     * @param goodsProp
     * @return 返回数据库中存在的记录数
     */
    public long isGoodsPropNameExist(GoodsProp goodsProp){
        GoodsPropQueryRequest goodsPropQueryRequest = new GoodsPropQueryRequest();
//        goodsPropQueryRequest.setCateId(goodsProp.getCateId());
        goodsPropQueryRequest.setPropName(goodsProp.getPropName());
        goodsPropQueryRequest.setDelFlag(DeleteFlag.NO);
        long count = goodsPropRepository.count(goodsPropQueryRequest.getWhereCriteria());
        return count;
    }

    /**
     * 判断属性的属性值是否存在相同
     * @param goodsPropDetails
     * @return 存在相同属性则返回true
     */
    public boolean isGoodsPropDetailRepeat(List<GoodsPropDetail> goodsPropDetails){
        for(int i = 0; i < goodsPropDetails.size() - 1; i++){
            for (int j = i + 1; j < goodsPropDetails.size(); j++){
                if (goodsPropDetails.get(i).getDetailName().equals(goodsPropDetails.get(j).getDetailName())){
                    return true;
                }
            }
        }
        return false;
    }
    /**
     * 判断属性值是否超限
     * @param goodsPropDetails
     * @return
     */
    public boolean isDetailOverStep(List<GoodsPropDetail> goodsPropDetails) {
        goodsPropDetails = goodsPropDetails.stream().filter(goodsPropDetail -> goodsPropDetail.getDelFlag() == DeleteFlag.NO ? true : false)
                .collect(Collectors.toList());
        int size = goodsPropDetails.size();
        if (size>Constants.GOODS_PROP_DETAIL_REAL_SIZE) {
            return true;
        }
        return false;
    }
    /**
     * 编辑商品类目属性
     * @param goodsProp
     * @return
     */
    @Transactional
    public Boolean editGoodsProp(GoodsProp goodsProp){
        Long cateId = goodsProp.getCateId();
        List<GoodsPropDetail> goodsPropDetails = goodsProp.getGoodsPropDetails();
        if(goodsProp.getPropType() == null){
            goodsProp.setPropType(1);
        }
        //判断属性的属性值是否存在相同
        if (isGoodsPropDetailRepeat(goodsPropDetails)){
            throw new SbcRuntimeException(GoodsPropErrorCode.GOODSPROPDETAIL_REPEAT);
        }
        //检查类目是否是3级节点
        if (!isChildNode(cateId)) {
            throw new SbcRuntimeException(GoodsPropErrorCode.NOT_CHILD_NODE);
        }
        goodsProp.setUpdateTime(LocalDateTime.now());
        //属性编辑入库
        goodsPropRepository.editGoodsProp(goodsProp.getCateId(),goodsProp.getPropName(),goodsProp.getIndexFlag(),goodsProp.getUpdateTime(),goodsProp.getPropId());
        //修改完成数据库后验证商品类目属性名称是否已存在,正常记录数应为1
        if (isGoodsPropNameExist(goodsProp)>1){
            throw new SbcRuntimeException(GoodsPropErrorCode.GOODSPROPNAME_ALREADY_EXIST);
        }
        //检查属性值是否超限
        boolean detailOverStep = isDetailOverStep(goodsPropDetails);
        if (detailOverStep) {
            throw new SbcRuntimeException(GoodsPropErrorCode.GOODSPROPDETAIL_OVERSTEP);
        }
        if(goodsProp.getPropType() == 1){
            //保存属性值
            goodsPropDetails.stream().forEach(goodsPropDetail -> {
                goodsPropDetail.setPropId(goodsProp.getPropId());
                if (Objects.isNull(goodsPropDetail.getDetailId())) {
                    goodsPropDetail.setCreateTime(LocalDateTime.now());
                    goodsPropDetailRepository.save(goodsPropDetail);
                } else {
                    goodsPropDetail.setUpdateTime(LocalDateTime.now());
                    goodsPropDetailRepository.editPropDetail(goodsPropDetail.getPropId(),goodsPropDetail.getDetailName(),goodsPropDetail.getUpdateTime(),goodsPropDetail.getDelFlag(),goodsPropDetail.getSort(),goodsPropDetail.getDetailId());
                }
                //解除SPU与属性值的关联
                deleteRef(goodsPropDetail);
            });
        }
        return true;
    }

    /**
     * 删除商品类目属性
     * @param propId
     * @return
     * @throws Exception
     */
    @Transactional
    public Boolean deleteProp(Long propId) {
        GoodsProp goodsProp = goodsPropRepository.findByPropId(propId);
        goodsProp.setDelFlag(DeleteFlag.YES);
        //删除类目属性
        goodsPropRepository.save(goodsProp);
        //解除与SPU的关联
        deleteRef(propId);
        List<GoodsPropDetail> goodsPropDetails = goodsPropDetailRepository.findAllByPropId(propId);
        goodsPropDetails.stream().forEach(goodsPropDetail -> {
            goodsPropDetail.setDelFlag(DeleteFlag.YES);
            //删除类目属性值
            goodsPropDetailRepository.save(goodsPropDetail);
        });
        List<GoodsProp> goodsProps = goodsPropRepository.findAllByCateIdAndDelFlagOrderBySortAsc(goodsProp.getCateId(),DeleteFlag.NO);
        editSort(goodsProps);
        return true;
    }

    /**
     * 判断是否是商品属性三级节点
     * @param cateId
     * @return
     */
    public Boolean isChildNode(Long cateId){
        GoodsCate goodsCate = goodsCateRepository.findById(cateId).orElse(null);
        if (Objects.isNull(goodsCate)) {
            return false;
        }
        Integer cateGrade = goodsCate.getCateGrade();
        if (cateGrade != Constants.GOODS_LEAF_CATE_GRADE) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 解除商品类目属性与SPU的关联
     * @param propId
     */
    public void deleteRef(Long propId) {
        List<GoodsPropDetailRel> goodsPropDetailRels = goodsPropDetailRelRepository.findAllByPropId(propId);
        deleteGoodsPropDetailRels(goodsPropDetailRels);
    }

    /**
     * 解除商品类目属性值与SPU的关联
     * @param goodsPropDetail
     */
    public void deleteRef(GoodsPropDetail goodsPropDetail) {
        if (Objects.equals(goodsPropDetail.getDelFlag(),DeleteFlag.YES)) {
            Long detailId = goodsPropDetail.getDetailId();
            List<GoodsPropDetailRel> goodsPropDetailRels = goodsPropDetailRelRepository.findAllByDetailId(detailId);
            deleteGoodsPropDetailRels(goodsPropDetailRels);
        }
    }

    /**
     * 解除关联操作
     * @param goodsPropDetailRels
     */
    public void deleteGoodsPropDetailRels(List<GoodsPropDetailRel> goodsPropDetailRels)  {
        goodsPropDetailRels.stream().forEach(goodsPropDetailRel -> {
            goodsPropDetailRel.setDelFlag(DeleteFlag.YES);
            goodsPropDetailRel.setUpdateTime(LocalDateTime.now());
            goodsPropDetailRelRepository.save(goodsPropDetailRel);
        });
    }

    /**
     * 编辑索引
     * @param goodsProp
     */
    public void editIndex(GoodsProp goodsProp) {
        GoodsProp oldGoodsProp = goodsPropRepository.findById(goodsProp.getPropId()).orElse(new GoodsProp());
        oldGoodsProp.setIndexFlag(goodsProp.getIndexFlag());
        oldGoodsProp.setUpdateTime(LocalDateTime.now());
        goodsPropRepository.save(oldGoodsProp);
    }

    /**
     * 根据类别Id查询该类别下所有spuId
     * @param cateId
     * @return
     */
    public List<String> findGoodsIdsByCateId(Long cateId) {
        List<Goods> goodsList = goodsRepository.findAllByCateId(cateId);
        List<String> goodsIds = goodsList.stream().map(goods -> {
            String gId = goods.getGoodsId();
            return gId;
        }).collect(Collectors.toList());
        return goodsIds;
    }


    /**
     * 保存默认spu与默认属性的关联
     * @param goodsIds
     * @param propId
     * @return
     */
    public boolean saveDefaultRef(List<String> goodsIds,Long propId ) {
        List<GoodsPropDetailRel> goodsPropDetailRelList = new ArrayList<>();
        goodsIds.stream().forEach(goodsId ->{
            GoodsPropDetailRel goodsPropDetailRel = new GoodsPropDetailRel();
            goodsPropDetailRel.setGoodsId(goodsId);
            goodsPropDetailRel.setDetailId(Constants.GOODS_DEFAULT_REL);
            goodsPropDetailRel.setPropId(propId);
            goodsPropDetailRel.setCreateTime(LocalDateTime.now());
            goodsPropDetailRel.setDelFlag(DeleteFlag.NO);
            goodsPropDetailRelList.add(goodsPropDetailRel);
        });
        goodsPropDetailRelRepository.saveAll(goodsPropDetailRelList);
        return true;
    }

    /**
     * 删除商品类目属性
     * @param cateIds
     * @return
     * @throws Exception
     */
    @GlobalTransactional
    @Transactional
    public Boolean deletePropByCateId(List<Long> cateIds) {
        List<GoodsProp> goodsPropList = goodsPropRepository.findAllByCateIsAndAndDelFlag(cateIds);
        if(CollectionUtils.isNotEmpty(goodsPropList)){
            goodsPropList.stream().forEach(goodsProp ->{
            goodsProp.setDelFlag(DeleteFlag.YES);
            //删除类目属性
            goodsPropRepository.save(goodsProp);
            //解除与SPU的关联
            deleteRef(goodsProp.getPropId());
            List<GoodsPropDetail> goodsPropDetails = goodsPropDetailRepository.findAllByPropId(goodsProp.getPropId());
            goodsPropDetails.stream().forEach(goodsPropDetail -> {
                goodsPropDetail.setDelFlag(DeleteFlag.YES);
                //删除类目属性值
                goodsPropDetailRepository.save(goodsPropDetail);
            });
            List<GoodsProp> goodsProps =
                    goodsPropRepository.findAllByCateIdAndDelFlagOrderBySortAsc(goodsProp.getCateId(), DeleteFlag.NO);
            editSort(goodsProps);
            });
        }
        return true;
    }

}
