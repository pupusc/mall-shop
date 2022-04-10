package com.wanmi.sbc.goods.api.request.goods;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.io.*;
import java.util.List;

/**
 * @author Liang Jun
 * @date 2022-04-06 21:34:00
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PackDetailByPackIdsRequest implements Serializable {
    /**
     * 商品包主键
     */
    @NotEmpty
    private List<String> packIds;
}
