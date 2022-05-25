package com.wanmi.sbc.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 数据来源场景
 */
@AllArgsConstructor
@Getter
public enum TerminalScene {


    MINI_PROGRAM(1,"MINI_PROGRAM", "小程序"),

    WECHAT_VIDEO(2,"WECHAT_VIDEO", "视频号");


    private Integer code;
    private String message;
    private String desc;


    public static TerminalScene getTerminalScene(String name) {
        for (TerminalScene terminal : values()) {
            if (terminal.getMessage().equals(name)) {
                return terminal;
            }
        }
        return TerminalScene.MINI_PROGRAM;
    }
}
