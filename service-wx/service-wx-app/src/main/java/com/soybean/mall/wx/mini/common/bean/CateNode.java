package com.soybean.mall.wx.mini.common.bean;

import com.alibaba.fastjson.JSONObject;
import com.soybean.mall.wx.mini.goods.bean.response.WxGetAllCateResponse;
import lombok.Data;

import java.util.*;

@Data
public class CateNode {

    private Integer id;
    private String name;
    private Integer qualification;
    private List<CateNode> children;

    public CateNode(){}

    public CateNode(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public CateNode(Integer id, String name, Integer qualification) {
        this.id = id;
        this.name = name;
        this.qualification = qualification;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CateNode cateNode = (CateNode) o;
        return Objects.equals(id, cateNode.id);
    }

    @Override
    public int hashCode() {
        return id;
    }
    
    public static Set<CateNode> buildCateTree(List<WxGetAllCateResponse.WxCate> thirdCatList){

        Map<CateNode, Map<CateNode, List<CateNode>>> first = new HashMap<>();
        for (WxGetAllCateResponse.WxCate o : thirdCatList) {
            first.compute(new CateNode(o.getFirstCatId(), o.getFirstCatName()), (k, v) -> {
                if(v == null){
                    Map<CateNode, List<CateNode>> second = new HashMap<>();
                    List<CateNode> third = new ArrayList<>();
                    third.add(new CateNode(o.getThirdCatId(), o.getThirdCatName(), o.getQualificationType()));
                    second.put(new CateNode(o.getSecondCatId(), o.getSecondCatName()), third);
                    return second;
                }else{
                    v.compute(new CateNode(o.getSecondCatId(), o.getSecondCatName()), (k2, v2) -> {
                        if(v2 == null){
                            List<CateNode> third = new ArrayList<>();
                            third.add(new CateNode(o.getThirdCatId(), o.getThirdCatName(), o.getQualificationType()));
                            return third;
                        }else{
                            v2.add(new CateNode(o.getThirdCatId(), o.getThirdCatName(), o.getQualificationType()));
                            return v2;
                        }
                    });
                    return v;
                }
            });
        }

        first.forEach((k1, v1) -> {
            v1.forEach(CateNode::setChildren);
            k1.setChildren(new ArrayList<>(v1.keySet()));
        });
        return first.keySet();
    }
}
