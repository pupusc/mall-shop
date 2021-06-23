package com.wanmi.sbc.thirdplatform.linkedmall.goods.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LinkMallResponse implements Serializable {

    private String Code;

    private String Message;

    private String RequestId;

}
