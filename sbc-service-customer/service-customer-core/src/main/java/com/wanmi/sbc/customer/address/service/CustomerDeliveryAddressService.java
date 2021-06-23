package com.wanmi.sbc.customer.address.service;

import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.address.model.root.CustomerDeliveryAddress;
import com.wanmi.sbc.customer.address.repository.CustomerDeliveryAddressRepository;
import com.wanmi.sbc.customer.address.request.CustomerDeliveryAddressEditRequest;
import com.wanmi.sbc.customer.address.request.CustomerDeliveryAddressQueryRequest;
import com.wanmi.sbc.customer.bean.vo.CustomerDeliveryAddressVO;
import com.wanmi.sbc.setting.api.provider.platformaddress.PlatformAddressQueryProvider;
import com.wanmi.sbc.setting.api.request.platformaddress.PlatformAddressListRequest;
import com.wanmi.sbc.setting.api.request.platformaddress.PlatformAddressVerifyRequest;
import com.wanmi.sbc.setting.bean.vo.PlatformAddressVO;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 客户收货地址服务
 * Created by CHENLI on 2017/4/20.
 */
@Service
public class CustomerDeliveryAddressService {

    @Autowired
    private CustomerDeliveryAddressRepository repository;

    @Autowired
    private PlatformAddressQueryProvider platformAddressQueryProvider;

    /**
     * 根据ID查询
     *
     * @param deliveredId
     * @return
     */
    public CustomerDeliveryAddress findById(String deliveredId) {
        return repository.findById(deliveredId).orElse(null);
    }

    /**
     * 查询客户默认的收货地址
     * 如果客户没有设置默认地址，则取该客户其中的一条收货地址
     *
     * @param queryRequest
     * @return
     */
    public CustomerDeliveryAddress findDefault(CustomerDeliveryAddressQueryRequest queryRequest) {
        queryRequest.setDelFlag(DeleteFlag.NO.toValue());
        queryRequest.setIsDefaltAddress(DefaultFlag.YES.toValue());
        CustomerDeliveryAddress deliveryAddress = repository.findOne(queryRequest.getWhereCriteria()).orElse(null);
        if (Objects.isNull(deliveryAddress)) {
            List<CustomerDeliveryAddress> addressList = this.findAddressList(queryRequest.getCustomerId());
            if (CollectionUtils.isNotEmpty(addressList)) {
                deliveryAddress = this.findAddressList(queryRequest.getCustomerId()).get(0);
            }
        }
        return deliveryAddress;
    }

    /**
     * 查询客户的收货地址
     *
     * @param customerId
     * @return
     */
    public List<CustomerDeliveryAddress> findAddressList(String customerId) {
        List<CustomerDeliveryAddress> addressList = repository.findByCustomerIdAndDelFlagOrderByCreateTimeDesc
                (customerId, DeleteFlag.NO);
        if (CollectionUtils.isNotEmpty(addressList)) {
            return addressList;
        }
        return Collections.emptyList();
    }

    /**
     * 保存客户的收货地址
     *
     * @param editRequest
     */
    @Transactional
    public CustomerDeliveryAddress saveAddress(CustomerDeliveryAddressEditRequest editRequest, String employeeId) {
        //如果设置为默认地址
        if (editRequest.getIsDefaltAddress().equals(DefaultFlag.YES)) {
            CustomerDeliveryAddress address = this.getDefault(editRequest.getCustomerId());
            if (Objects.nonNull(address)) {
                //把已设为默认的地址给取消默认
                address.setIsDefaltAddress(DefaultFlag.NO);
                repository.save(address);
            }
        }
        CustomerDeliveryAddress address = new CustomerDeliveryAddress();
        BeanUtils.copyProperties(editRequest, address);
        address.setDelFlag(DeleteFlag.NO);
        address.setCreatePerson(employeeId);
        address.setCreateTime(LocalDateTime.now());
        return repository.saveAndFlush(address);
    }


    /**
     * 修改客户的收货地址
     *
     * @param editRequest
     */
    @Transactional
    public CustomerDeliveryAddress update(CustomerDeliveryAddressEditRequest editRequest, String employeeId) {
        CustomerDeliveryAddress address = repository.findById(editRequest.getDeliveryAddressId()).orElse(null);
        if (Objects.isNull(address)){
            return null;
        }
        //如果设置为默认地址
        if (editRequest.getIsDefaltAddress().equals(DefaultFlag.YES) && address.getIsDefaltAddress().equals
                (DefaultFlag.NO)) {
            CustomerDeliveryAddress addressDefault = this.getDefault(address.getCustomerId());
            if (Objects.nonNull(addressDefault)) {
                //把已设为默认的地址给取消默认
                addressDefault.setIsDefaltAddress(DefaultFlag.NO);
                repository.save(addressDefault);
            }
        }

        KsBeanUtil.copyProperties(editRequest, address);
        if (Objects.isNull(editRequest.getCityId())) {
            address.setCityId(0L);
            address.setAreaId(0L);
        }
        address.setUpdatePerson(employeeId);
        address.setUpdateTime(LocalDateTime.now());
        return repository.saveAndFlush(address);
    }

    /**
     * 删除客户的收货地址
     *
     * @param addressId
     */
    @Transactional
    public void deleteAddress(String addressId) {
        repository.deleteAddress(addressId);
    }

    /**
     * 设置为默认客户地址
     *
     * @param addressId
     * @param customerId
     */
    @Transactional
    public void setDefaultAddress(String addressId, String customerId) {
        //查询该客户是否设置默认地址
        CustomerDeliveryAddress address = this.getDefault(customerId);
        if (Objects.nonNull(address)) {
            //把已设为默认的地址给取消默认
            address.setIsDefaltAddress(DefaultFlag.NO);
            repository.save(address);
        }
        repository.updateDefault(addressId, customerId);
    }

    /**
     * 只查询客户默认地址
     *
     * @param customerId
     * @return
     */
    public CustomerDeliveryAddress getDefault(String customerId) {
        CustomerDeliveryAddressQueryRequest queryRequest = new CustomerDeliveryAddressQueryRequest();
        queryRequest.setDelFlag(DeleteFlag.NO.toValue());
        queryRequest.setIsDefaltAddress(DefaultFlag.YES.toValue());
        queryRequest.setCustomerId(customerId);
        CustomerDeliveryAddress deliveryAddress = repository.findOne(queryRequest.getWhereCriteria()).orElse(null);
        return deliveryAddress;
    }

    /**
     * 查询该客户有多少条收货地址
     *
     * @param customerId
     * @return
     */
    public int countCustomerAddress(String customerId) {
        return repository.countCustomerAddress(customerId);
    }

    /**
     * 填充省市区
     * @param details
     */
    public void fillArea(List<CustomerDeliveryAddressVO> details){
        if(CollectionUtils.isNotEmpty(details)){
            List<String> addrIds = new ArrayList<>();
            details.forEach(detail -> {
                addrIds.add(Objects.toString(detail.getProvinceId()));
                addrIds.add(Objects.toString(detail.getCityId()));
                addrIds.add(Objects.toString(detail.getAreaId()));
                addrIds.add(Objects.toString(detail.getStreetId()));
            });
            List<PlatformAddressVO> voList = platformAddressQueryProvider.list(PlatformAddressListRequest.builder().addrIdList(addrIds).build()).getContext().getPlatformAddressVOList();
            if(CollectionUtils.isNotEmpty(voList)){
                Map<String, String> addrMap = voList.stream().collect(Collectors.toMap(PlatformAddressVO::getAddrId, PlatformAddressVO::getAddrName));
                details.forEach(detail -> {
                    detail.setProvinceName(addrMap.get(Objects.toString(detail.getProvinceId())));
                    detail.setCityName(addrMap.get(Objects.toString(detail.getCityId())));
                    detail.setAreaName(addrMap.get(Objects.toString(detail.getAreaId())));
                    detail.setStreetName(addrMap.get(Objects.toString(detail.getStreetId())));
                });
            }
        }
    }

    /**
     * 填充是否需要完善
     * @param details
     */
    public void verifyAddress(List<CustomerDeliveryAddressVO> details){
        if(CollectionUtils.isNotEmpty(details)){
            details.forEach(detail -> {
                if(Objects.isNull(detail.getProvinceId()) || Objects.isNull(detail.getCityId()) || Objects.isNull(detail.getAreaId())) {
                    detail.setNeedComplete(Boolean.TRUE);
                } else {
                    PlatformAddressVerifyRequest request =  new PlatformAddressVerifyRequest();
                    request.setProvinceId(String.valueOf(detail.getProvinceId()));
                    request.setCityId(String.valueOf(detail.getCityId()));
                    request.setAreaId(String.valueOf(detail.getAreaId()));
                    if(Objects.nonNull(detail.getStreetId())) {
                        request.setStreetId(String.valueOf(detail.getStreetId()));
                    }
                    detail.setNeedComplete(platformAddressQueryProvider.verifyAddress(request).getContext());
                }
            });
        }
    }
}
