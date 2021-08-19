package com.wanmi.osd.bean;


import com.wanmi.osd.ExceptionHandle.OSDException;

public enum OsdType {


    ALIYUN("aliYun", 0),
    HUAWEIYUN("hwYun", 1),
    TXYUN("txYun", 2);


    OsdType(String name, int id) {
        _name = name;
        _id = id;
    }

    private String _name;

    private int _id;

    public String getName() {
        return _name;
    }

    public int getId() {
        return _id;
    }



    public static OsdType getOsdType(String name) {
        switch (name) {
            case "aliYun":
                return OsdType.ALIYUN;
            case "hwYun":
                return OsdType.HUAWEIYUN;
            case "txYun":
                return OsdType.TXYUN;
            default:
                throw new OSDException("参数错误！请检查configType是否正确！！");
        }
    }
}
