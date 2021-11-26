package com.wanmi.sbc.crm.customgroup.model;

import com.wanmi.sbc.crm.bean.dto.RegionDTO;
import com.wanmi.sbc.crm.bean.vo.CustomGroupDetailVo;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: zgl
 * \* Date: 2019-11-21
 * \* Time: 14:33
 * \* To change this template use File | Settings | File Templates.
 * \* Description:
 * \
 */
@Data
public class CustomGroupDetail extends CustomGroupDetailVo {

    private Long id;
/*
    private List<String> provinceIdList;
*/

    private List<String> cityIdList;

   /* public List<String> getProvinceIdList(){
        if(CollectionUtils.isNotEmpty(getRegionList())){
            return getRegionList().stream().map(RegionDTO::getProvinceId).filter(provinceId-> StringUtils.isNotEmpty(provinceId)).collect(Collectors.toList());
        }
        return null;
    }*/
    public List<String> getCityIdList(){
        if(CollectionUtils.isNotEmpty(getRegionList())){
            return getRegionList().stream().map(RegionDTO::getRegionId).filter(cityId->StringUtils.isNotEmpty(cityId)).collect(Collectors.toList());
        }
        return null;
    }
}
