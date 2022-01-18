package com.wanmi.sbc.goods.freight.service;

import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.goods.api.request.freight.ExpressNotSupportCreateUpdateRequest;
import com.wanmi.sbc.goods.api.request.supplier.SecondLevelSupplierCreateUpdateRequest;
import com.wanmi.sbc.goods.freight.model.root.*;
import com.wanmi.sbc.goods.freight.repository.*;
import com.wanmi.sbc.goods.supplier.model.SupplierModel;
import com.wanmi.sbc.goods.supplier.repository.SupplierRepository;
import io.seata.spring.annotation.GlobalTransactional;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.handler.aop.MasterRouteOnly;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.customer.api.provider.store.StoreQueryProvider;
import com.wanmi.sbc.customer.api.request.store.NoDeleteStoreByIdRequest;
import com.wanmi.sbc.customer.bean.vo.StoreVO;
import com.wanmi.sbc.goods.api.constant.FreightTemplateErrorCode;
import com.wanmi.sbc.goods.api.request.freight.FreightTemplateGoodsSaveRequest;
import com.wanmi.sbc.goods.bean.dto.FreightTemplateGoodsExpressSaveDTO;
import com.wanmi.sbc.goods.bean.dto.FreightTemplateGoodsFreeSaveDTO;
import com.wanmi.sbc.goods.bean.enums.DeliverWay;
import com.wanmi.sbc.goods.bean.enums.ValuationType;
import com.wanmi.sbc.goods.info.repository.GoodsRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 单品运费模板服务
 * Created by sunkun on 2018/5/2.
 */
@Service
public class FreightTemplateGoodsService {

    @Resource
    private FreightTemplateGoodsRepository freightTemplateGoodsRepository;

    @Resource
    private FreightTemplateGoodsExpressRepository freightTemplateGoodsExpressRepository;

    @Resource
    private FreightTemplateGoodsFreeRepository freightTemplateGoodsFreeRepository;

    @Resource
    private FreightTemplateStoreRepository freightTemplateStoreRepository;

    @Resource
    private GoodsRepository goodsRepository;

    @Resource
//    private StoreRepository storeRepository;
    private StoreQueryProvider storeQueryProvider;

    @Resource
    private ExpressNotSupportRepository expressNotSupportRepository;

    @Resource
    private SupplierRepository supplierRepository;


    /**
     * 保存单品运费模板
     *
     * @param request 单品运费模板信息
     */
    @Transactional(rollbackFor = Exception.class)
    public void renewalFreightTemplateGoods(FreightTemplateGoodsSaveRequest request) {
        FreightTemplateGoods freightTemplateGoods = null;
        if (request.getFreightTempId() == null) {
//            int count = freightTemplateGoodsRepository.countByStoreIdAndDelFlag(request.getStoreId(), DeleteFlag.NO);
//            if (count >= Constants.FREIGHT_GOODS_MAX_SIZE) {
//                throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
//            }
            //校验模板名称是否重复
            this.freightTemplateNameIsRepetition(request.getStoreId(), request.getFreightTempName(),false);
            freightTemplateGoods = new FreightTemplateGoods();
            BeanUtils.copyProperties(request, freightTemplateGoods);
            freightTemplateGoods.setCreateTime(LocalDateTime.now());
            freightTemplateGoods.setDelFlag(DeleteFlag.NO);
        } else {
            freightTemplateGoods = freightTemplateGoodsRepository.findById(request.getFreightTempId()).orElse(null);
            if (freightTemplateGoods == null) {
                throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
            }
            //修改名称，校验模板名称是否重复
            if (!StringUtils.equals(request.getFreightTempName(), freightTemplateGoods.getFreightTempName())) {
                this.freightTemplateNameIsRepetition(freightTemplateGoods.getStoreId(), request.getFreightTempName(),false);
            }
            //组装并保存单品运费模板
            freightTemplateGoods.setStreetId(request.getStreetId());
            freightTemplateGoods.setAreaId(request.getAreaId());
            freightTemplateGoods.setCityId(request.getCityId());
            freightTemplateGoods.setProvinceId(request.getProvinceId());
            if (Objects.equals(freightTemplateGoods.getDefaultFlag(), DefaultFlag.NO)) {
                freightTemplateGoods.setFreightTempName(request.getFreightTempName());
            }
            freightTemplateGoods.setFreightFreeFlag(request.getFreightFreeFlag());
            freightTemplateGoods.setValuationType(request.getValuationType());
            freightTemplateGoods.setSpecifyTermFlag(request.getSpecifyTermFlag());
        }
        FreightTemplateGoods rFreightTemplateGoods = freightTemplateGoodsRepository.save(freightTemplateGoods);

        //过滤出单品运费模板快递运送下所有区域
        List<String> expressAreas = request.getFreightTemplateGoodsExpressSaveRequests().stream()
                .filter(info->Objects.equals(DeleteFlag.NO,info.getDelFlag())).map(info -> {
            if (info.getDestinationArea() == null || info.getDestinationArea().length == 0) {
                return null;
            }
            return StringUtils.join(info.getDestinationArea(), ",");
        }).filter(Objects::nonNull).collect(Collectors.toList());
        //过滤出单品运费模板指定包邮条件下所有区域
        List<String> freeAreas = request.getFreightTemplateGoodsFreeSaveRequests().stream()
                .filter(info->Objects.equals(DeleteFlag.NO,info.getDelFlag())).map(info -> {
            if (info.getDestinationArea() == null || info.getDestinationArea().length == 0) {
                return null;
            }
            return StringUtils.join(info.getDestinationArea(), ",");
        }).filter(Objects::nonNull).collect(Collectors.toList());
        //校验是否有重复区域
        if (this.verifyAreaRepetition(expressAreas) || this.verifyAreaRepetition(freeAreas)) {
            throw new SbcRuntimeException(FreightTemplateErrorCode.AREA_REPETITION_SETTING);
        }
        //保存单品运费模板
        this.batchRenewalFreightTemplateGoodsExpress(rFreightTemplateGoods, request.getFreightTemplateGoodsExpressSaveRequests());
        //保存单品运费模板指定包邮条件
        this.batchRenewalFreightTemplateGoodsFree(rFreightTemplateGoods, request.getFreightTemplateGoodsFreeSaveRequests());
    }

    public void saveOrUpdateNotSupportArea(ExpressNotSupportCreateUpdateRequest request) {
        if(request.getId() != null){
            //更新
            Optional<ExpressNotSupport> optional = expressNotSupportRepository.findById(request.getId());
            if(optional.isPresent()){
                ExpressNotSupport expressNotSupport = optional.get();
                expressNotSupport.setProvinceId(Arrays.toString(request.getProvinceId()));
                expressNotSupport.setProvinceName(Arrays.toString(request.getProvinceName()));
                expressNotSupport.setCityId(Arrays.toString(request.getCityId()));
                expressNotSupport.setCityName(Arrays.toString(request.getCityName()));
                expressNotSupport.setUpdateTime(LocalDateTime.now());
                expressNotSupportRepository.save(expressNotSupport);
            }
        }else {
            ExpressNotSupport expressNotSupport = new ExpressNotSupport();
            LocalDateTime now = LocalDateTime.now();
            expressNotSupport.setProvinceId(Arrays.toString(request.getProvinceId()));
            expressNotSupport.setProvinceName(Arrays.toString(request.getProvinceName()));
            expressNotSupport.setCityId(Arrays.toString(request.getCityId()));
            expressNotSupport.setCityName(Arrays.toString(request.getCityName()));
            expressNotSupport.setUpdateTime(now);
            expressNotSupport.setCreateTime(now);
            expressNotSupport.setDelFlag(DeleteFlag.NO);
            expressNotSupportRepository.save(expressNotSupport);
        }
    }

    public String importNotSupportArea(String areaStr, Long supplierId) {
        Map<String, List<String>> areas = JSONObject.parseObject(areaStr, Map.class);
        StringBuilder provinceIdsb = new StringBuilder();
        StringBuilder provinceNamesb = new StringBuilder();
        StringBuilder cityIdsb = new StringBuilder();
        StringBuilder cityNamesb = new StringBuilder();
        StringBuilder errorName = new StringBuilder();
        areas.forEach((k ,v) -> {
            Integer cityCount = CityAndCodeMapping.getCityCount(k);
            if(cityCount == null) {
                errorName.append(k).append(",");
            }else{
                if(v.size() >= cityCount){
                    String code = CityAndCodeMapping.getCode(k);
                    if(code == null) {
                        errorName.append(k).append(",");
                    }else{
                        provinceIdsb.append(code).append(",");
                        provinceNamesb.append(k).append(",");
                    }
                }else {
                    v.forEach(i -> {
                        String code = CityAndCodeMapping.getCode(i);
                        if(code == null) {
                            errorName.append(i).append(",");
                        }else{
                            cityIdsb.append(code).append(",");
                            cityNamesb.append(i).append(",");
                        }
                    });
                }
            }
        });
        if(errorName.length() > 0) return errorName.substring(0, errorName.length() - 1);
        ExpressNotSupport expressNotSupport = expressNotSupportRepository.findBySupplierIdAndDelFlag(supplierId, DeleteFlag.NO);
        if(expressNotSupport == null){
            expressNotSupport = new ExpressNotSupport();
            LocalDateTime now = LocalDateTime.now();
            if(provinceIdsb.length() > 0){
                expressNotSupport.setProvinceId(provinceIdsb.substring(0, provinceIdsb.length() - 1));
                expressNotSupport.setProvinceName(provinceNamesb.substring(0, provinceNamesb.length() - 1));
            }
            if(cityIdsb.length() > 0){
                expressNotSupport.setCityId(cityIdsb.substring(0, cityIdsb.length() - 1));
                expressNotSupport.setCityName(cityNamesb.substring(0, cityNamesb.length() - 1));
            }
            expressNotSupport.setSupplierId(supplierId);
            expressNotSupport.setUpdateTime(now);
            expressNotSupport.setCreateTime(now);
            expressNotSupport.setDelFlag(DeleteFlag.NO);
            expressNotSupportRepository.save(expressNotSupport);
        }else {
            expressNotSupport.setProvinceId(provinceIdsb.substring(0, provinceIdsb.length() - 1));
            expressNotSupport.setProvinceName(provinceNamesb.substring(0, provinceNamesb.length() - 1));
            expressNotSupport.setCityId(cityIdsb.substring(0, cityIdsb.length() - 1));
            expressNotSupport.setCityName(cityNamesb.substring(0, cityNamesb.length() - 1));
            expressNotSupport.setUpdateTime(LocalDateTime.now());
            expressNotSupportRepository.save(expressNotSupport);
        }
        return "";
    }

    public void deleteNotSupportArea(Long id) {
        Optional<ExpressNotSupport> optional = expressNotSupportRepository.findById(id);
        if(optional.isPresent()){
            ExpressNotSupport expressNotSupport = optional.get();
            expressNotSupport.setDelFlag(DeleteFlag.YES);
            expressNotSupportRepository.save(expressNotSupport);
        }
    }

    public ExpressNotSupport findNotSupportArea(Long id) {
        return expressNotSupportRepository.findBySupplierIdAndDelFlag(id, DeleteFlag.NO);
    }

    public void saveOrUpdateSecondLevelSupplier(SecondLevelSupplierCreateUpdateRequest request) {
        if(request.getId() != null){
            //更新
            Optional<SupplierModel> optional = supplierRepository.findById(request.getId());
            if(optional.isPresent()){
                SupplierModel supplierModel = optional.get();
                supplierModel.setName(request.getName());
                supplierModel.setCode(request.getCode());
                supplierModel.setUpdateTime(LocalDateTime.now());
                supplierRepository.save(supplierModel);
            }
        }else {
            SupplierModel supplierModel = new SupplierModel();
            LocalDateTime now = LocalDateTime.now();
            supplierModel.setName(request.getName());
            supplierModel.setCode(request.getCode());
            supplierModel.setUpdateTime(now);
            supplierModel.setCreateTime(now);
            supplierModel.setDelFlag(DeleteFlag.NO);
            supplierRepository.save(supplierModel);
        }
    }

    public List<SupplierModel> findSecondLevelSupplier() {
        return supplierRepository.findAllByDelFlag(DeleteFlag.NO);
    }

    public void deleteSecondLevelSupplier(Long id) {
        Optional<SupplierModel> optional = supplierRepository.findById(id);
        if(optional.isPresent()){
            SupplierModel supplierModel = optional.get();
            supplierModel.setDelFlag(DeleteFlag.YES);
            supplierRepository.save(supplierModel);
            List<ExpressNotSupport> expressNotSupports = expressNotSupportRepository.findAllBySupplierId(id);
            expressNotSupports.forEach(i -> i.setDelFlag(DeleteFlag.YES));
            expressNotSupportRepository.saveAll(expressNotSupports);
        }
    }

    /**
     * 校验名称重复
     *
     * @param storeId 店铺id
     * @param freightTempName 单品运费模板名称
     * @param isCopy 是否复制
     */
    public void freightTemplateNameIsRepetition(Long storeId, String freightTempName,boolean isCopy) {
        FreightTemplateGoods freightTemplateGoods = freightTemplateGoodsRepository.queryByFreighttemplateName(storeId, freightTempName);
        if (freightTemplateGoods != null) {
            String errorCode = FreightTemplateErrorCode.STORE_NAME_EXIST;
            if(isCopy){
                errorCode = FreightTemplateErrorCode.NAME_EXIST;
            }
            throw new SbcRuntimeException(errorCode);
        }
    }

    /**
     * 修改单品运费模板
     *
     * @param request 单品运费模板信息
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateFreightTemplateGoods(FreightTemplateGoodsSaveRequest request) {
        FreightTemplateGoods freightTemplateGoods = freightTemplateGoodsRepository.findById(request.getFreightTempId()).orElse(null);
        if (freightTemplateGoods == null) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        //组装并保存单品运费模板
        freightTemplateGoods.setStreetId(request.getStreetId());
        freightTemplateGoods.setAreaId(request.getAreaId());
        freightTemplateGoods.setCityId(request.getCityId());
        freightTemplateGoods.setProvinceId(request.getProvinceId());
        freightTemplateGoods.setFreightTempName(request.getFreightTempName());
        freightTemplateGoods.setFreightFreeFlag(request.getFreightFreeFlag());
        freightTemplateGoods.setValuationType(request.getValuationType());
        freightTemplateGoods.setSpecifyTermFlag(request.getSpecifyTermFlag());
        freightTemplateGoodsRepository.save(freightTemplateGoods);
        //批量更新单品运费模板快递运送
        this.batchRenewalFreightTemplateGoodsExpress(freightTemplateGoods, request.getFreightTemplateGoodsExpressSaveRequests());
        //批量更新单品运费模板指定包邮条件
        this.batchRenewalFreightTemplateGoodsFree(freightTemplateGoods, request.getFreightTemplateGoodsFreeSaveRequests());
    }

    /**
     * 查询所有单品运费模板列表
     *
     * @param storeId 店铺id
     * @return 单品运费模板列表
     */
    public List<FreightTemplateGoods> queryAll(Long storeId) {
        List<FreightTemplateGoods> list = freightTemplateGoodsRepository.queryAll(storeId, DeleteFlag.NO);
        List<Long> ids = list.stream().map(FreightTemplateGoods::getFreightTempId).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(ids)) {
            List<FreightTemplateGoodsExpress> freightTemplateGoodsExpresses = freightTemplateGoodsExpressRepository.findByFreightTempIds(ids);
            if (CollectionUtils.isNotEmpty(freightTemplateGoodsExpresses)) {
                list.forEach(info -> {
                    info.setFreightTemplateGoodsExpresses(freightTemplateGoodsExpresses.stream().filter(express ->
                            info.getFreightTempId().equals(express.getFreightTempId())).collect(Collectors.toList()));
                });
            }
        }
        return list;
    }


    /**
     * 根据主键列表查询单品运费模板列表
     *
     * @param ids 单品运费模板ids
     * @return 单品运费模板列表
     */
    @MasterRouteOnly
    public List<FreightTemplateGoods> queryAllByIds(List<Long> ids) {
        List<FreightTemplateGoods> list = freightTemplateGoodsRepository.queryByFreightTempIds(ids);
        if (CollectionUtils.isEmpty(list)) {
            return list;
        }
        List<FreightTemplateGoodsExpress> freightTemplateGoodsExpresses = freightTemplateGoodsExpressRepository.findByFreightTempIds(ids);
        List<FreightTemplateGoodsFree> freightTemplateGoodsFrees = freightTemplateGoodsFreeRepository.findByFreightTempIds(ids);
        list.forEach(info -> {
            info.setFreightTemplateGoodsExpresses(freightTemplateGoodsExpresses.stream().filter(express ->
                    info.getFreightTempId().equals(express.getFreightTempId())).collect(Collectors.toList()));
            info.setFreightTemplateGoodsFrees(freightTemplateGoodsFrees.stream().filter(frees ->
                    info.getFreightTempId().equals(frees.getFreightTempId())).collect(Collectors.toList()));
        });
        return list;
    }

    /**
     * 查询目标地区是否支持配送
     */
    public boolean queryIfnotSupportArea(Long provinceId, Long cityId) {
        List<ExpressNotSupport> expressNotSupports = expressNotSupportRepository.findAllByDelFlag(DeleteFlag.NO);
        String province = provinceId.toString();
        String city = cityId.toString();
        for (ExpressNotSupport expressNotSupport : expressNotSupports) {
            String provinceIds = expressNotSupport.getProvinceId();
            String cityIds = expressNotSupport.getCityId();
            String[] provinces = provinceIds.split(",");
            String[] citys = cityIds.split(",");
            for (String s : provinces) {
                if(s.equals(province)) return false;
            }
            for (String s : citys) {
                if(s.equals(city)) return false;
            }
        }
        return true;
    }

    /**
     * 查询单品运费模板
     *
     * @param freightTempId 单品运费模板id
     * @return 单品运费模板
     */
    public FreightTemplateGoods queryById(Long freightTempId) {
        FreightTemplateGoods freightTemplateGoods = freightTemplateGoodsRepository.queryById(freightTempId);
        if (freightTemplateGoods == null) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        freightTemplateGoods.setFreightTemplateGoodsExpresses(freightTemplateGoodsExpressRepository.findByFreightTempIdAndDelFlag(freightTempId, DeleteFlag.NO));
        freightTemplateGoods.setFreightTemplateGoodsFrees(freightTemplateGoodsFreeRepository.findByFreightTempIdAndDelFlag(freightTempId, DeleteFlag.NO));
        return freightTemplateGoods;
    }

    /**
     * 根据运费模板ID判断模板是否存在
     * @param freightTempId 单品运费模板id
     */
    public void hasFreightTemp(Long freightTempId) {
        FreightTemplateGoods freightTemplateGoods = freightTemplateGoodsRepository.queryById(freightTempId);
        if (Objects.isNull(freightTemplateGoods)) {
            //运费模板不存在异常，待common拆分后再改异常编号
            throw new SbcRuntimeException(FreightTemplateErrorCode.NOT_EXIST);
        }
    }

    /**
     * 根据主键和店铺id删除单品运费模板
     *
     * @param id 运费模板ID
     * @param storeId 店铺id
     */
    @Transactional(rollbackFor = Exception.class)
    public void delById(Long id, Long storeId) {
        FreightTemplateGoods freightTemplateGoods = this.queryById(id);
        if (!Objects.equals(freightTemplateGoods.getStoreId(), storeId) ||
                Objects.equals(freightTemplateGoods.getDefaultFlag(), DefaultFlag.YES)) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        freightTemplateGoods.setDelFlag(DeleteFlag.YES);
        freightTemplateGoodsRepository.save(freightTemplateGoods);
        FreightTemplateGoods defaultFreightTemplateGodos = freightTemplateGoodsRepository.queryByDefault(storeId);
        if (defaultFreightTemplateGodos == null) {
            //店铺下没有默认模板,系统数据错误
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        goodsRepository.updateFreightTempId(id, defaultFreightTemplateGodos.getFreightTempId());
    }

    /**
     * 复制单品运费模板
     *
     * @param freightTempId 运费模板ID
     * @param storeId 店铺id
     */
    @Transactional(rollbackFor = Exception.class)
    public void copyFreightTemplateGoods(Long freightTempId, Long storeId) {
//        //单品运费模板上限20
//        int count = freightTemplateGoodsRepository.countByStoreIdAndDelFlag(storeId, DeleteFlag.NO);
//        if (count >= Constants.FREIGHT_GOODS_MAX_SIZE) {
//            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
//        }
        //查询要复制单品运费模板
        FreightTemplateGoods freightTemplateGoods = this.queryById(freightTempId);
        if (!Objects.equals(freightTemplateGoods.getStoreId(), storeId)) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        FreightTemplateGoods newFreightTemplateGoods = new FreightTemplateGoods();
        BeanUtils.copyProperties(freightTemplateGoods, newFreightTemplateGoods);
        //拼接新模板名称
        newFreightTemplateGoods.setFreightTempName(freightTemplateGoods.getFreightTempName() + "的副本");
        if (newFreightTemplateGoods.getFreightTempName().length() > Constants.FREIGHT_GOODS_MAX_SIZE) {
            //名称长度超出限制
            throw new SbcRuntimeException(FreightTemplateErrorCode.NAME_OVER_LIMIT);
        }
        this.freightTemplateNameIsRepetition(storeId, newFreightTemplateGoods.getFreightTempName(),true);
        LocalDateTime date = LocalDateTime.now();
        newFreightTemplateGoods.setFreightTempId(null);
        newFreightTemplateGoods.setCreateTime(date);
        newFreightTemplateGoods.setDefaultFlag(DefaultFlag.NO);
        newFreightTemplateGoods.setSpecifyTermFlag(CollectionUtils.isEmpty(freightTemplateGoods.getFreightTemplateGoodsFrees()) ? DefaultFlag.NO : DefaultFlag.YES);
        //复制单品模板
        FreightTemplateGoods resultFreightTemplateGoods = freightTemplateGoodsRepository.save(newFreightTemplateGoods);
        //复制单品运费模板快递运送
        freightTemplateGoods.getFreightTemplateGoodsExpresses().forEach(info -> {
            FreightTemplateGoodsExpress newFreightTemplateGoodsExpress = new FreightTemplateGoodsExpress();
            BeanUtils.copyProperties(info, newFreightTemplateGoodsExpress);
            newFreightTemplateGoodsExpress.setId(null);
            newFreightTemplateGoodsExpress.setFreightTempId(resultFreightTemplateGoods.getFreightTempId());
            newFreightTemplateGoodsExpress.setCreateTime(date);
            freightTemplateGoodsExpressRepository.save(newFreightTemplateGoodsExpress);
        });
        //复制单品运费模板指定包邮条件
        freightTemplateGoods.getFreightTemplateGoodsFrees().forEach(info -> {
            FreightTemplateGoodsFree newFreightTemplateGoodsFree = new FreightTemplateGoodsFree();
            BeanUtils.copyProperties(info, newFreightTemplateGoodsFree);
            newFreightTemplateGoodsFree.setId(null);
            newFreightTemplateGoodsFree.setFreightTempId(resultFreightTemplateGoods.getFreightTempId());
            newFreightTemplateGoodsFree.setCreateTime(date);
            freightTemplateGoodsFreeRepository.save(newFreightTemplateGoodsFree);
        });
    }

    /**
     * 创建店铺后初始化运费模板
     *
     * @param storeId 店铺id
     */
    @GlobalTransactional
    @Transactional(rollbackFor = Exception.class)
    public void initFreightTemplate(Long storeId) {
//        Store store = storeRepository.findByStoreIdAndDelFlag(storeId, DeleteFlag.NO);
        StoreVO store = storeQueryProvider.getNoDeleteStoreById(new NoDeleteStoreByIdRequest(storeId)).getContext().getStoreVO();
        if (store == null) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        //组装单品运费模板数据
        FreightTemplateGoodsSaveRequest freightTemplateGoodsSaveRequest = new FreightTemplateGoodsSaveRequest();
        freightTemplateGoodsSaveRequest.setFreightTempName("默认模板");
        freightTemplateGoodsSaveRequest.setStoreId(storeId);
        freightTemplateGoodsSaveRequest.setCompanyInfoId(store.getCompanyInfo().getCompanyInfoId());
        freightTemplateGoodsSaveRequest.setDefaultFlag(DefaultFlag.YES);
        freightTemplateGoodsSaveRequest.setFreightFreeFlag(DefaultFlag.NO);
        freightTemplateGoodsSaveRequest.setSpecifyTermFlag(DefaultFlag.NO);
        freightTemplateGoodsSaveRequest.setValuationType(ValuationType.NUMBER);
        // 组装单品运费模板快递运送数据
        List<FreightTemplateGoodsExpressSaveDTO> list = new ArrayList<>();
        FreightTemplateGoodsExpressSaveDTO freightTemplateGoodsExpressSaveRequest = new FreightTemplateGoodsExpressSaveDTO();
        freightTemplateGoodsExpressSaveRequest.setDefaultFlag(DefaultFlag.YES);
        freightTemplateGoodsExpressSaveRequest.setDelFlag(DeleteFlag.NO);
        freightTemplateGoodsExpressSaveRequest.setDestinationArea(new String[]{});
        freightTemplateGoodsExpressSaveRequest.setDestinationAreaName(new String[]{"未被划分的配送地区自动归于默认运费"});
        BigDecimal defaultNum = BigDecimal.ZERO;
        freightTemplateGoodsExpressSaveRequest.setFreightStartNum(new BigDecimal(1));
        freightTemplateGoodsExpressSaveRequest.setFreightStartPrice(defaultNum);
        freightTemplateGoodsExpressSaveRequest.setFreightPlusNum(new BigDecimal(1));
        freightTemplateGoodsExpressSaveRequest.setFreightPlusPrice(defaultNum);
        list.add(freightTemplateGoodsExpressSaveRequest);
        freightTemplateGoodsSaveRequest.setFreightTemplateGoodsExpressSaveRequests(list);
        //保存单品运费模板
        this.renewalFreightTemplateGoods(freightTemplateGoodsSaveRequest);
        //组装店铺运费模板数据
        FreightTemplateStore freightTemplateStore = new FreightTemplateStore();
        freightTemplateStore.setFreightTempName("默认模板");
        freightTemplateStore.setDestinationAreaName("未被划分的配送地区自动归于默认运费");
        freightTemplateStore.setFixedFreight(defaultNum);
        freightTemplateStore.setSatisfyFreight(defaultNum);
        freightTemplateStore.setSatisfyPrice(defaultNum);
        freightTemplateStore.setStoreId(storeId);
        freightTemplateStore.setCompanyInfoId(store.getCompanyInfo().getCompanyInfoId());
        freightTemplateStore.setCreateTime(LocalDateTime.now());
        freightTemplateStore.setDefaultFlag(DefaultFlag.YES);
        freightTemplateStore.setDelFlag(DeleteFlag.NO);
        freightTemplateStore.setFreightType(DefaultFlag.YES);
        freightTemplateStore.setDeliverWay(DeliverWay.EXPRESS);
        freightTemplateStore.setDestinationArea("");
        freightTemplateStoreRepository.save(freightTemplateStore);
    }

    /**
     * 根据店铺id获取默认的单品运费模板
     * @param storeId 店铺id
     * @return 单品运费模板
     */
    public FreightTemplateGoods queryByDefaultByStoreId(Long storeId){
        return freightTemplateGoodsRepository.queryByDefault(storeId);
    }

    /**
     * 批量更新单品运费模板快递运送
     *
     * @param freightTemplateGoods  单品运费模板
     * @param list 单品运费模板费用
     */
    private void batchRenewalFreightTemplateGoodsExpress(FreightTemplateGoods freightTemplateGoods,
                                                         List<FreightTemplateGoodsExpressSaveDTO> list) {
        list.forEach(info -> {
            FreightTemplateGoodsExpress freightTemplateGoodsExpress = null;
            if (info.getId() != null) {
                //编辑单品运费模板快递运送
                freightTemplateGoodsExpress = freightTemplateGoodsExpressRepository.findById(info.getId()).orElse(null);
                if (freightTemplateGoodsExpress != null) {
                    //默认模板不支持删除，非法输入
                    if (Objects.equals(info.getDefaultFlag(), DefaultFlag.YES) && Objects.equals(info.getDelFlag(), DeleteFlag.YES)) {
                        throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
                    }
                    freightTemplateGoodsExpress.setDelFlag(info.getDelFlag());
                    freightTemplateGoodsExpress.setFreightPlusNum(info.getFreightPlusNum());
                    freightTemplateGoodsExpress.setFreightPlusPrice(info.getFreightPlusPrice());
                    freightTemplateGoodsExpress.setFreightStartNum(info.getFreightStartNum());
                    freightTemplateGoodsExpress.setFreightStartPrice(info.getFreightStartPrice());
                }
            } else {
                //新增单品运费模板快递运送
                freightTemplateGoodsExpress = new FreightTemplateGoodsExpress();
                BeanUtils.copyProperties(info, freightTemplateGoodsExpress);
                freightTemplateGoodsExpress.setCreateTime(LocalDateTime.now());
                freightTemplateGoodsExpress.setDelFlag(DeleteFlag.NO);
                freightTemplateGoodsExpress.setFreightTempId(freightTemplateGoods.getFreightTempId());
            }
            if (freightTemplateGoodsExpress != null) {
                freightTemplateGoodsExpress.setValuationType(freightTemplateGoods.getValuationType());
                freightTemplateGoodsExpress.setDestinationArea(StringUtils.join(info.getDestinationArea(), ","));
                freightTemplateGoodsExpress.setDestinationAreaName(StringUtils.join(info.getDestinationAreaName(), ","));
                freightTemplateGoodsExpressRepository.save(freightTemplateGoodsExpress);
            }
        });
    }

    /**
     * 批量更新单品运费模板指定包邮条件
     *
     * @param freightTemplateGoods 单品运费模板
     * @param list 单品运费模板费用
     */
    private void batchRenewalFreightTemplateGoodsFree(FreightTemplateGoods freightTemplateGoods,
                                                      List<FreightTemplateGoodsFreeSaveDTO> list) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        list.forEach(info -> {
            FreightTemplateGoodsFree freightTemplateGoodsFree = null;
            if (info.getId() != null) {
                //编辑单品运费模板指定包邮条件
                freightTemplateGoodsFree = freightTemplateGoodsFreeRepository.findById(info.getId()).orElse(new FreightTemplateGoodsFree());
                freightTemplateGoodsFree.setConditionType(info.getConditionType());
                freightTemplateGoodsFree.setConditionOne(info.getConditionOne());
                freightTemplateGoodsFree.setConditionTwo(info.getConditionTwo());
                freightTemplateGoodsFree.setDelFlag(info.getDelFlag());
            } else {
                //新增单品运费模板指定包邮条件
                freightTemplateGoodsFree = new FreightTemplateGoodsFree();
                BeanUtils.copyProperties(info, freightTemplateGoodsFree);
                freightTemplateGoodsFree.setCreateTime(LocalDateTime.now());
                freightTemplateGoodsFree.setFreightTempId(freightTemplateGoods.getFreightTempId());
                freightTemplateGoodsFree.setDelFlag(DeleteFlag.NO);
            }
            if (freightTemplateGoodsFree != null) {
                freightTemplateGoodsFree.setDestinationArea(StringUtils.join(info.getDestinationArea(), ","));
                freightTemplateGoodsFree.setValuationType(freightTemplateGoods.getValuationType());
                freightTemplateGoodsFree.setDestinationAreaName(StringUtils.join(info.getDestinationAreaName(), ","));
                freightTemplateGoodsFreeRepository.save(freightTemplateGoodsFree);
            }
        });
    }


    /**
     * 校验区域是否有重复
     *
     * @param list 区域
     * @return true:有重复 false:无重复
     */
    private boolean verifyAreaRepetition(List<String> list) {
        Set<String> set = new HashSet<>();
        List<String> strLists = list.stream().map(info -> {
            if (StringUtils.isBlank(info)) {
                return null;
            }
            return Arrays.asList(info.split(","));
        }).filter(Objects::nonNull).flatMap(Collection::stream).map(str -> {
            set.add(str);
            return str;
        }).collect(Collectors.toList());
        return strLists.size() != set.size();
    }
}
