package com.soybean.mall.wx.mini.goods.bean.response;

import lombok.Data;

import java.util.*;

@Data
public class WxCateNodeResponse {

    private Integer id;
    private String name;
    private Integer qualification;
    private List<WxCateNodeResponse> children;

    public WxCateNodeResponse(){}

    public WxCateNodeResponse(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public WxCateNodeResponse(Integer id, String name, Integer qualification) {
        this.id = id;
        this.name = name;
        this.qualification = qualification;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WxCateNodeResponse cateNode = (WxCateNodeResponse) o;
        return Objects.equals(id, cateNode.id);
    }

    @Override
    public int hashCode() {
        return id;
    }
    
    public static Set<WxCateNodeResponse> buildCateTree(List<WxGetAllCateResponse.WxCate> thirdCatList){

        Map<WxCateNodeResponse, Map<WxCateNodeResponse, List<WxCateNodeResponse>>> first = new HashMap<>();
        for (WxGetAllCateResponse.WxCate o : thirdCatList) {
            first.compute(new WxCateNodeResponse(o.getFirstCatId(), o.getFirstCatName()), (k, v) -> {
                if(v == null){
                    Map<WxCateNodeResponse, List<WxCateNodeResponse>> second = new HashMap<>();
                    List<WxCateNodeResponse> third = new ArrayList<>();
                    third.add(new WxCateNodeResponse(o.getThirdCatId(), o.getThirdCatName(), o.getQualificationType()));
                    second.put(new WxCateNodeResponse(o.getSecondCatId(), o.getSecondCatName()), third);
                    return second;
                }else{
                    v.compute(new WxCateNodeResponse(o.getSecondCatId(), o.getSecondCatName()), (k2, v2) -> {
                        if(v2 == null){
                            List<WxCateNodeResponse> third = new ArrayList<>();
                            third.add(new WxCateNodeResponse(o.getThirdCatId(), o.getThirdCatName(), o.getQualificationType()));
                            return third;
                        }else{
                            v2.add(new WxCateNodeResponse(o.getThirdCatId(), o.getThirdCatName(), o.getQualificationType()));
                            return v2;
                        }
                    });
                    return v;
                }
            });
        }

        first.forEach((k1, v1) -> {
            v1.forEach(WxCateNodeResponse::setChildren);
            k1.setChildren(new ArrayList<>(v1.keySet()));
        });
        return first.keySet();
    }
}
