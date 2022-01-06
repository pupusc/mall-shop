package com.wanmi.sbc.vote;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.bean.vo.GoodsVoteVo;
import com.wanmi.sbc.vote.service.GoodsVoteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/vote")
@Slf4j
public class VoteController {

    @Autowired
    private GoodsVoteService goodsVoteService;

    /**
     * @description 投票图片
     * @menu 投票
     * @param
     * @status done
     */
    @PostMapping(value = "/image")
    public BaseResponse<String> images() {
        return BaseResponse.success(goodsVoteService.getImage());
    }

    /**
     * @description 商品列表
     * @menu 投票
     * @param
     * @status done
     */
    @PostMapping(value = "/goods")
    public BaseResponse<List<GoodsVoteVo>> voteGoodsList(){
        return BaseResponse.success(goodsVoteService.voteGoodsList());
    }

    /**
     * @description 投票
     * @menu 投票
     * @param
     * @status done
     */
    @PostMapping(value = "/up")
    public BaseResponse vote(@RequestBody Map<String, String> param){
        goodsVoteService.vote(param.get("goodsId"));
        return BaseResponse.SUCCESSFUL();
    }

}
