package com.wanmi.sbc.goods.fandeng;

import lombok.Data;

import java.io.*;
import java.util.List;

/**
 * @author Liang Jun
 * @date 2022-03-17 21:19:00
 */
@Data
public class SiteSearchNotifyModel implements Serializable {
    private String type;
    private List<String> ids;
}
