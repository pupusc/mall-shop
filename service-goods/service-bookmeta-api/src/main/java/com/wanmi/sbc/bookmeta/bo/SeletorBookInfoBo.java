package com.wanmi.sbc.bookmeta.bo;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class SeletorBookInfoBo {

    private Integer id;
    private String name;
    private String rank;
    private String recommendation;
    List<RecomentBookBo> recomentBookBoList;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getRecommendation() {
        return recommendation;
    }

    public void setRecommendation(String recommendation) {
        this.recommendation = recommendation;
    }

    public List<RecomentBookBo> getRecomentBookBoList() {
        if(null==recomentBookBoList){
            List<RecomentBookBo> recomentBookBoListTemp=new ArrayList<>();
            return recomentBookBoList=recomentBookBoListTemp;
        }
        return recomentBookBoList;
    }

    public void setRecomentBookBoList(List<RecomentBookBo> recomentBookBoList) {
        this.recomentBookBoList.clear();;
        this.recomentBookBoList = recomentBookBoList;
    }
}
