//  1.bus_bar_infos 业务线组件
//  预售组件 5f0d5e55589de89d84977d66
db.getCollection("bus_bar_infos").insert({
    "_id": "5f0d5e55589de89d84977d66",
    "envCode": "test1",
    "key": "@wanmi/wechat-presalelist",
    "platform": "weixin",
    "systemCode": "d2cStore",
    "replicable": true,
    "img": "",
    "dependencies": [
        "@wanmi/wechat-presalelist"
    ],
    "isAdvanced": true,
    "packageName": "@wanmi/wechat-presalelist",
    "addible": true,
    "deletable": true,
    "version": "0.0.10",
    "title": "预售列表",
    "deleted": false,
    "props": {
        "props": {
            "showType": {
                "itemStyle": NumberInt(1),
                "showStyle": NumberInt(1),
                "cartButton": NumberInt(1),
                "goodsSetup": {
                    "cart": true,
                    "cartStyle": NumberInt(3),
                    "name": true,
                    "marketing": true,
                    "title": true,
                    "price": true,
                    "showSku": true,
                    "horn": true,
                    "hornStyle": NumberInt(2),
                    "customHorn": ""
                }
            },
            "bgColor": "#f7f7f7",
            "selectedSource": NumberInt(4),
            "sortSource": NumberInt(1),
            "sources": [
                {
                    "type": "1",
                    "size": NumberInt(4)
                },
                {
                    "type": "2",
                    "size": NumberInt(4)
                },
                {
                    "type": "3",
                    "size": NumberInt(4),
                    "cateID": "",
                    "cateName": ""
                },
                {
                    "type": "4"
                },
                {
                    "type": "5",
                    "size": NumberInt(4)
                }
            ],
            "items": [
                {
                    "imgHref": "",
                    "imgSrc": "//oss-hz.qianmi.com/x-site/public/x-site-ui-public-weixin/statics/image/goodsList/goodlist-item.png",
                    "name": "示例商品名称1",
                    "nameWithSku": "示例商品名称 示例SKU",
                    "specName": "示例SKU",
                    "title": "商品二级标题",
                    "handSelPrice": "10",
                    "inflationPrice": "40",
                    "price": "199",
                    "linePrice": "200",
                    "startTime": "",
                    "endTime": "",
                    "promotionLabels": {

                    }
                },
                {
                    "imgHref": "",
                    "imgSrc": "//oss-hz.qianmi.com/x-site/public/x-site-ui-public-weixin/statics/image/goodsList/goodlist-item.png",
                    "name": "示例商品名称2",
                    "nameWithSku": "示例商品名称 示例SKU",
                    "specName": "示例SKU",
                    "title": "商品二级标题",
                    "handSelPrice": "0",
                    "inflationPrice": "0",
                    "price": "199",
                    "linePrice": "200",
                    "startTime": "",
                    "endTime": "",
                    "promotionLabels": {

                    }
                }
            ],
            "version": "0.0.10"
        },
        "widgetNameSpace": "@wanmi/wechat-presalelist"
    },
    "updatedAt": ISODate("2020-07-14T17:48:42.040+08:00"),
    "type": "COMMON",
    "isCommon": true,
    "__v": NumberInt(0),
    "createdAt": ISODate("2020-07-14T15:27:20.638+08:00"),
    "cateId": "5add9b42e15b9a13a2a3bd26",
    "sortIndex": NumberInt(4)
})

// 二级分类组件 5f0fc639589de89d849f13ed

db.getCollection("bus_bar_infos").insert({
    "_id": "5f0fc639589de89d849f13ed",
    "envCode": "test1",
    "key": "@wanmi/wechat-lev2cate",
    "platform": "weixin",
    "systemCode": "d2cStore",
    "replicable": false,
    "img": "",
    "dependencies": [
        "@wanmi/wechat-lev2cate"
    ],
    "isAdvanced": true,
    "packageName": "@wanmi/wechat-lev2cate",
    "addible": false,
    "deletable": false,
    "version": "0.0.38",
    "title": "二级分类",
    "deleted": false,
    "props": {
        "props": {
            "img1": "http://web-img.qmimg.com/x-site/public/images/weixin/fashion/sousuo_shu.png",
            "type": "type2",
            "isFixed": true,
            "isShow": true,
            "placeholder": "搜索",
            "bjColor": "rgba(247, 247, 247, .9)",
            "enableLazyLoad": true,
            "point_style": NumberInt(1),
            "isConfont": true,
            "columns": NumberInt(2),
            "rows": NumberInt(1),
            "isBgShow": false,
            "bgColor": "#fff",
            "textColor": "#333",
            "bgImg": "//oss-hz.qianmi.com/x-site/publicx-site-ui-public-weixin/statics/image/nav/bgImg.png",
            "checkedItemsId": NumberInt(1),
            "cateItems": [
                {
                    "id": NumberInt(1),
                    "lev1Title": "数码",
                    "imgSrc": "http://wanmi-x-site.oss-cn-shanghai.aliyuncs.com/x-site/public/images/x-site-ui/classify-banner.png",
                    "href": "#",
                    "lev2Items": [
                        {
                            "lev2Title": "笔记本电脑",
                            "imgSrc": "http://wanmi-x-site.oss-cn-shanghai.aliyuncs.com/x-site/public/images/x-site-ui/lev3cate-img.png"
                        }
                    ]
                }
            ],
            "version": "0.0.38"
        },
        "widgetNameSpace": "@wanmi/wechat-lev2cate"
    },
    "updatedAt": ISODate("2020-08-04T11:31:42.573+08:00"),
    "type": "COMMON",
    "isCommon": true,
    "__v": NumberInt(0),
    "createdAt": ISODate("2020-07-16T11:15:06.918+08:00")
})


// 三级分类组件 5f0fc652589de89d849f1463
db.getCollection("bus_bar_infos").insert({
    "_id": "5f0fc652589de89d849f1463",
    "envCode": "test1",
    "key": "@wanmi/wechat-lev3cate",
    "platform": "weixin",
    "systemCode": "d2cStore",
    "replicable": false,
    "img": "",
    "dependencies": [
        "@wanmi/wechat-lev3cate"
    ],
    "isAdvanced": true,
    "packageName": "@wanmi/wechat-lev3cate",
    "addible": false,
    "deletable": false,
    "version": "0.0.71",
    "title": "三级分类",
    "deleted": false,
    "props": {
        "props": {
            "img1": "http://web-img.qmimg.com/x-site/public/images/weixin/fashion/sousuo_shu.png",
            "type": "type2",
            "isFixed": true,
            "isShow": true,
            "placeholder": "搜索",
            "bjColor": "rgba(247, 247, 247, .9)",
            "enableLazyLoad": true,
            "point_style": NumberInt(1),
            "isConfont": true,
            "columns": NumberInt(2),
            "rows": NumberInt(1),
            "isBgShow": false,
            "bgColor": "#fff",
            "textColor": "#333",
            "bgImg": "//oss-hz.qianmi.com/x-site/publicx-site-ui-public-weixin/statics/image/nav/bgImg.png",
            "checkedItemsId": NumberInt(1),
            "cateItems": [
                {
                    "id": NumberInt(1),
                    "lev1Title": "数码",
                    "imgSrc": "http://wanmi-x-site.oss-cn-shanghai.aliyuncs.com/x-site/public/images/x-site-ui/classify-banner.png",
                    "href": "#",
                    "lev2Items": [
                        {
                            "lev2Title": "数码配件",
                            "imgSrc": "http://img.1000.com/qm-a-img/prod/1261651/f06824a5b87eae042453d9c1c2be3df4.png",
                            "lev3Items": [
                                {
                                    "imgSrc": "http://wanmi-x-site.oss-cn-shanghai.aliyuncs.com/x-site/public/images/x-site-ui/lev3cate-img.png",
                                    "href": "#",
                                    "lev3Title": "笔记本电脑"
                                }
                            ]
                        }
                    ]
                }
            ],
            "version": "0.0.71"
        },
        "widgetNameSpace": "@wanmi/wechat-lev3cate"
    },
    "updatedAt": ISODate("2020-08-04T11:36:36.469+08:00"),
    "type": "COMMON",
    "isCommon": true,
    "__v": NumberInt(0),
    "createdAt": ISODate("2020-07-16T11:15:32.665+08:00")
})

// 一级分类组件 5f0fc669589de89d849f14c7
db.getCollection("bus_bar_infos").insert({
    "_id": "5f0fc669589de89d849f14c7",
    "envCode": "test1",
    "key": "@wanmi/wechat-lev1cate",
    "platform": "weixin",
    "systemCode": "d2cStore",
    "replicable": false,
    "img": "",
    "dependencies": [
        "@wanmi/wechat-lev1cate"
    ],
    "isAdvanced": true,
    "packageName": "@wanmi/wechat-lev1cate",
    "addible": false,
    "deletable": false,
    "version": "0.0.93",
    "title": "一级分类",
    "deleted": false,
    "props": {
        "props": {
            "img1": "http://web-img.qmimg.com/x-site/public/images/weixin/fashion/sousuo_shu.png",
            "type": "type2",
            "isFixed": true,
            "isShow": true,
            "placeholder": "输入搜索内容",
            "bjColor": "rgba(247, 247, 247, .9)",
            "enableLazyLoad": true,
            "point_style": NumberInt(1),
            "isConfont": true,
            "columns": NumberInt(2),
            "rows": NumberInt(1),
            "isBgShow": false,
            "bgColor": "#fff",
            "textColor": "#333",
            "bgImg": "//oss-hz.qianmi.com/x-site/publicx-site-ui-public-weixin/statics/image/nav/bgImg.png",
            "checkedItemsId": NumberInt(1),
            "cateItems": [
                {
                    "id": NumberInt(1),
                    "title": "数码",
                    "isSpu": false,
                    "isGroup": false,
                    "items": [
                        {
                            "imgHref": "",
                            "imgSrc": "//oss-hz.qianmi.com/x-site/public/x-site-ui-public-weixin/statics/image/goodsList/goodlist-item.png",
                            "name": "示例商品名称洗发护理1示例商品名称洗发护理1示例商品名称洗发护理1示例商品名称洗发护理1",
                            "nameWithSku": "示例商品名称 示例SKU",
                            "specName": "示例SKU",
                            "title": "商品二级标题",
                            "price": "199.00",
                            "buyPoint": NumberInt(100),
                            "goodsType": NumberInt(0)
                        }
                    ]
                }
            ],
            "selectedSource": NumberInt(4),
            "showType": {
                "itemStyle": NumberInt(3),
                "showStyle": NumberInt(1),
                "cartButton": NumberInt(1),
                "goodsSetup": {
                    "cart": true,
                    "cartStyle": NumberInt(3),
                    "name": true,
                    "title": true,
                    "price": true,
                    "showSku": true,
                    "horn": true,
                    "hornStyle": NumberInt(2),
                    "customHorn": ""
                }
            },
            "sources": [
                {
                    "type": "1",
                    "size": NumberInt(4)
                },
                {
                    "type": "2",
                    "size": NumberInt(4)
                },
                {
                    "type": "3",
                    "size": NumberInt(4),
                    "cateID": "",
                    "cateName": ""
                },
                {
                    "type": "4"
                }
            ],
            "version": "0.0.93"
        },
        "widgetNameSpace": "@wanmi/wechat-lev1cate"
    },
    "updatedAt": ISODate("2020-08-04T11:31:40.470+08:00"),
    "type": "COMMON",
    "isCommon": true,
    "__v": NumberInt(0),
    "createdAt": ISODate("2020-07-16T11:15:55.523+08:00")
})


// 二级分类+商品列表组件 5f0fc680589de89d849f1528
db.getCollection("bus_bar_infos").insert({
    "_id": "5f0fc680589de89d849f1528",
    "envCode": "test1",
    "key": "@wanmi/wechat-lev2goodslist",
    "platform": "weixin",
    "systemCode": "d2cStore",
    "replicable": false,
    "img": "",
    "dependencies": [
        "@wanmi/wechat-lev2goodslist"
    ],
    "isAdvanced": true,
    "packageName": "@wanmi/wechat-lev2goodslist",
    "addible": false,
    "deletable": false,
    "version": "0.0.65",
    "title": "二级分类+商品列表",
    "deleted": false,
    "props": {
        "props": {
            "img1": "http://web-img.qmimg.com/x-site/public/images/weixin/fashion/sousuo_shu.png",
            "type": "type2",
            "isFixed": true,
            "isShow": true,
            "placeholder": "搜索",
            "bjColor": "rgba(247, 247, 247, .9)",
            "enableLazyLoad": true,
            "point_style": NumberInt(1),
            "isConfont": true,
            "columns": NumberInt(2),
            "rows": NumberInt(1),
            "isBgShow": false,
            "bgColor": "#fff",
            "textColor": "#333",
            "bgImg": "//oss-hz.qianmi.com/x-site/publicx-site-ui-public-weixin/statics/image/nav/bgImg.png",
            "checkedItemsId": NumberInt(1),
            "lev2CheckedItemsId": NumberInt(1),
            "cateItems": [
                {
                    "id": NumberInt(1),
                    "lev1Title": "数码",
                    "imgSrc": "http://wanmi-x-site.oss-cn-shanghai.aliyuncs.com/x-site/public/images/x-site-ui/classify-banner.png",
                    "href": "#",
                    "lev2Items": [
                        {
                            "id": NumberInt(1),
                            "lev2Title": "希维尔",
                            "imgSrc": "http://wanmi-x-site.oss-cn-shanghai.aliyuncs.com/x-site/public/images/x-site-ui/classify-banner.png",
                            "isSpu": false,
                            "isGroup": false,
                            "items": [
                                {
                                    "imgHref": "",
                                    "imgSrc": "http://wanmi-x-site.oss-cn-shanghai.aliyuncs.com/x-site/public/images/x-site-ui/classify-banner.png",
                                    "name": "示例商品名称香水彩妆1",
                                    "nameWithSku": "示例商品名称 示例SKU",
                                    "specName": "示例SKU",
                                    "title": "商品二级标题",
                                    "price": "199.00",
                                    "buyPoint": NumberInt(100),
                                    "goodsType": NumberInt(0)
                                }
                            ]
                        }
                    ]
                }
            ],
            "selectedSource": NumberInt(4),
            "showType": {
                "itemStyle": NumberInt(3),
                "showStyle": NumberInt(1),
                "cartButton": NumberInt(1),
                "goodsSetup": {
                    "cart": true,
                    "cartStyle": NumberInt(3),
                    "name": true,
                    "title": true,
                    "price": true,
                    "showSku": true,
                    "horn": true,
                    "hornStyle": NumberInt(2),
                    "customHorn": ""
                }
            },
            "sources": [
                {
                    "type": "1",
                    "size": NumberInt(4)
                },
                {
                    "type": "2",
                    "size": NumberInt(4)
                },
                {
                    "type": "3",
                    "size": NumberInt(4),
                    "cateID": "",
                    "cateName": ""
                },
                {
                    "type": "4"
                }
            ],
            "version": "0.0.65"
        },
        "widgetNameSpace": "@wanmi/wechat-lev2goodslist"
    },
    "updatedAt": ISODate("2020-08-04T11:36:35.723+08:00"),
    "type": "COMMON",
    "isCommon": true,
    "__v": NumberInt(0),
    "createdAt": ISODate("2020-07-16T11:16:18.314+08:00")
})

//   预约组件  5ee862362dde71e43bbd90e9
db.getCollection("bus_bar_infos").insert({
    "_id": "5ee862362dde71e43bbd90e9",
    "envCode": "test1",
    "key": "@wanmi/wechat-preorderlist",
    "platform": "weixin",
    "systemCode": "d2cStore",
    "replicable": true,
    "img": "",
    "dependencies": [
        "@wanmi/wechat-preorderlist"
    ],
    "isAdvanced": true,
    "packageName": "@wanmi/wechat-preorderlist",
    "addible": true,
    "deletable": true,
    "version": "0.0.17",
    "title": "预约列表",
    "deleted": false,
    "props": {
        "props": {
            "showType": {
                "itemStyle": NumberInt(1),
                "showStyle": NumberInt(1),
                "cartButton": NumberInt(1),
                "goodsSetup": {
                    "cart": true,
                    "cartStyle": NumberInt(3),
                    "name": true,
                    "marketing": true,
                    "title": true,
                    "price": true,
                    "showSku": true,
                    "horn": true,
                    "hornStyle": NumberInt(2),
                    "customHorn": ""
                }
            },
            "bgColor": "#f7f7f7",
            "selectedSource": NumberInt(4),
            "sortSource": NumberInt(1),
            "sources": [
                {
                    "type": "1",
                    "size": NumberInt(4)
                },
                {
                    "type": "2",
                    "size": NumberInt(4)
                },
                {
                    "type": "3",
                    "size": NumberInt(4),
                    "cateID": "",
                    "cateName": ""
                },
                {
                    "type": "4"
                },
                {
                    "type": "5",
                    "size": NumberInt(4)
                }
            ],
            "items": [
                {
                    "imgHref": "",
                    "imgSrc": "//oss-hz.qianmi.com/x-site/public/x-site-ui-public-weixin/statics/image/goodsList/goodlist-item.png",
                    "name": "示例商品名称1",
                    "nameWithSku": "示例商品名称 示例SKU",
                    "specName": "示例SKU",
                    "title": "商品二级标题",
                    "price": NumberInt(199),
                    "buyPoint": NumberInt(0),
                    "marketPrice": NumberInt(200),
                    "startTime": "20200618"
                },
                {
                    "imgHref": "",
                    "imgSrc": "//oss-hz.qianmi.com/x-site/public/x-site-ui-public-weixin/statics/image/goodsList/goodlist-item.png",
                    "name": "示例商品名称2",
                    "nameWithSku": "示例商品名称 示例SKU",
                    "specName": "示例SKU",
                    "title": "商品二级标题",
                    "price": NumberInt(199),
                    "buyPoint": NumberInt(0),
                    "marketPrice": NumberInt(200),
                    "startTime": "20200618"
                }
            ],
            "version": "0.0.17"
        },
        "widgetNameSpace": "@wanmi/wechat-preorderlist"
    },
    "updatedAt": ISODate("2020-07-28T16:14:02.670+08:00"),
    "type": "COMMON",
    "isCommon": true,
    "__v": NumberInt(0),
    "createdAt": ISODate("2020-06-16T14:10:00.681+08:00"),
    "cateId": "5add9b42e15b9a13a2a3bd26",
    "sortIndex": NumberInt(2)
})


// 拼团组件 5ecf67b8904320f53808b843
db.getCollection("bus_bar_infos").insert({
    "_id": "5ecf67b8904320f53808b843",
    "envCode": "test1",
    "key": "@wanmi/wechat-grouponlist",
    "platform": "weixin",
    "systemCode": "d2cStore",
    "replicable": true,
    "img": "",
    "dependencies": [
        "@wanmi/wechat-grouponlist"
    ],
    "isAdvanced": true,
    "packageName": "@wanmi/wechat-grouponlist",
    "addible": true,
    "deletable": true,
    "version": "0.0.16",
    "title": "拼团列表",
    "deleted": false,
    "props": {
        "props": {
            "showType": {
                "itemStyle": NumberInt(1),
                "showStyle": NumberInt(1),
                "cartButton": NumberInt(1),
                "goodsSetup": {
                    "cart": true,
                    "cartStyle": NumberInt(3),
                    "name": true,
                    "marketing": true,
                    "title": true,
                    "price": true,
                    "showSku": true,
                    "horn": true,
                    "hornStyle": NumberInt(2),
                    "customHorn": ""
                }
            },
            "bgColor": "#f7f7f7",
            "selectedSource": NumberInt(4),
            "sortSource": NumberInt(1),
            "sources": [
                {
                    "type": "1",
                    "size": NumberInt(4)
                },
                {
                    "type": "2",
                    "size": NumberInt(4)
                },
                {
                    "type": "3",
                    "size": NumberInt(4),
                    "cateID": "",
                    "cateName": ""
                },
                {
                    "type": "4"
                },
                {
                    "type": "5",
                    "size": NumberInt(4)
                }
            ],
            "items": [
                {
                    "imgHref": "",
                    "imgSrc": "//oss-hz.qianmi.com/x-site/public/x-site-ui-public-weixin/statics/image/goodsList/goodlist-item.png",
                    "name": "示例商品名称1",
                    "specName": "示例SKU",
                    "price": "199.00",
                    "marketPrice": "1999.00",
                    "status": NumberInt(1),
                    "alreadyGrouponNum": NumberInt(1),
                    "grouponNum": NumberInt(2)
                }
            ],
            "version": "0.0.16"
        },
        "widgetNameSpace": "@wanmi/wechat-grouponlist"
    },
    "updatedAt": ISODate("2020-07-30T09:23:05.050+08:00"),
    "type": "COMMON",
    "isCommon": true,
    "__v": NumberInt(0),
    "createdAt": ISODate("2020-05-28T15:26:51.843+08:00"),
    "cateId": "5add9b42e15b9a13a2a3bd26",
    "sortIndex": NumberInt(1)
})

// 秒杀组件 5eedd0f92dde71e43bcf4f71
db.getCollection("bus_bar_infos").insert({
    "_id": "5eedd0f92dde71e43bcf4f71",
    "envCode": "test1",
    "key": "@wanmi/wechat-flashlist",
    "platform": "weixin",
    "systemCode": "d2cStore",
    "replicable": true,
    "img": "",
    "dependencies": [
        "@wanmi/wechat-flashlist"
    ],
    "isAdvanced": true,
    "packageName": "@wanmi/wechat-flashlist",
    "addible": true,
    "deletable": true,
    "version": "0.0.69",
    "title": "秒杀组件",
    "deleted": false,
    "props": {
        "props": {
            "showType": {
                "itemStyle": NumberInt(1),
                "showStyle": NumberInt(1),
                "cartButton": NumberInt(1),
                "goodsSetup": {
                    "cart": true,
                    "cartStyle": NumberInt(3),
                    "name": true,
                    "marketing": true,
                    "title": true,
                    "price": true,
                    "showSku": true,
                    "horn": true,
                    "hornStyle": NumberInt(2),
                    "customHorn": ""
                }
            },
            "bgColor": "#f7f7f7",
            "selectedSource": NumberInt(4),
            "sortSource": NumberInt(1),
            "sources": [
                {
                    "type": "1",
                    "size": NumberInt(4)
                },
                {
                    "type": "2",
                    "size": NumberInt(4)
                },
                {
                    "type": "3",
                    "size": NumberInt(4),
                    "cateID": "",
                    "cateName": ""
                },
                {
                    "type": "4"
                },
                {
                    "type": "5",
                    "size": NumberInt(4)
                }
            ],
            "items": [
                {
                    "imgHref": "",
                    "imgSrc": "//oss-hz.qianmi.com/x-site/public/x-site-ui-public-weixin/statics/image/goodsList/goodlist-item.png",
                    "name": "示例商品名称1",
                    "nameWithSku": "示例商品名称 示例SKU",
                    "specName": "示例SKU",
                    "title": "商品二级标题",
                    "price": "19.00",
                    "buyPoint": NumberInt(0),
                    "marketPrice": NumberInt(200),
                    "progressRatio": NumberInt(10),
                    "promotionLabels": {

                    }
                },
                {
                    "imgHref": "",
                    "imgSrc": "//oss-hz.qianmi.com/x-site/public/x-site-ui-public-weixin/statics/image/goodsList/goodlist-item.png",
                    "name": "示例商品名称2",
                    "nameWithSku": "示例商品名称 示例SKU",
                    "specName": "示例SKU",
                    "title": "商品二级标题",
                    "price": "19.00",
                    "buyPoint": NumberInt(0),
                    "marketPrice": NumberInt(200),
                    "progressRatio": NumberInt(10),
                    "promotionLabels": {

                    }
                }
            ],
            "version": "0.0.69"
        },
        "widgetNameSpace": "@wanmi/wechat-flashlist"
    },
    "updatedAt": ISODate("2020-08-04T14:12:43.449+08:00"),
    "type": "COMMON",
    "isCommon": true,
    "__v": NumberInt(0),
    "createdAt": ISODate("2020-06-20T17:03:56.653+08:00"),
    "cateId": "5add9b42e15b9a13a2a3bd26",
    "sortIndex": NumberInt(3)
})

// 商品组件 修改 5b693a88916d0427ef917769
db.bus_bar_infos.update({ _id: ObjectId("5b693a88916d0427ef917769") }, {
    $set: {
        "envCode": "test1",
        "key": "@wanmi/wechat-goodslist",
        "platform": "weixin",
        "systemCode": "d2cStore",
        "replicable": true,
        "img": "",
        "dependencies": [
            "@wanmi/wechat-goodslist"
        ],
        "isAdvanced": true,
        "packageName": "@wanmi/wechat-goodslist",
        "addible": true,
        "deletable": true,
        "version": "0.19.195",
        "title": "商品列表",
        "deleted": false,
        "props": {
            "props": {
                "showType": {
                    "itemStyle": NumberInt(2),
                    "showStyle": NumberInt(1),
                    "cartButton": NumberInt(1),
                    "goodsSetup": {
                        "cart": true,
                        "cartStyle": NumberInt(3),
                        "name": true,
                        "marketing": true,
                        "title": true,
                        "price": true,
                        "showSku": true,
                        "horn": true,
                        "hornStyle": NumberInt(1),
                        "customHorn": ""
                    }
                },
                "bgColor": "#f7f7f7",
                "selectedSource": NumberInt(2),
                "sortSource": NumberInt(1),
                "sources": [
                    {
                        "type": "1",
                        "size": NumberInt(4)
                    },
                    {
                        "type": "2",
                        "size": NumberInt(4)
                    },
                    {
                        "type": "3",
                        "size": NumberInt(4),
                        "cateID": "",
                        "cateName": ""
                    },
                    {
                        "type": "4"
                    }
                ],
                "items": [
                    {
                        "imgHref": "",
                        "imgSrc": "//oss-hz.qianmi.com/x-site/public/x-site-ui-public-weixin/statics/image/goodsList/goodlist-item.png",
                        "name": "示例商品名称",
                        "nameWithSku": "示例商品名称 示例SKU",
                        "specName": "示例SKU",
                        "title": "商品二级标题",
                        "price": "199.00",
                        "buyPoint": NumberInt(0),
                        "promotionLabels": {
                            "couponLabels": [
                                {
                                    "couponActivityId": "ff8080817163f8ef017171470c62001a",
                                    "couponDesc": "满56767减36545",
                                    "couponInfoId": "ff8080817163f8ef0171714373cc0014"
                                }
                            ],
                            "marketingLabels": [
                                {
                                    "marketingDesc": "满2件减1",
                                    "marketingId": NumberInt(356),
                                    "marketingType": NumberInt(0)
                                },
                                {
                                    "marketingDesc": "满3件获赠品，赠完为止",
                                    "marketingId": NumberInt(360),
                                    "marketingType": NumberInt(2)
                                }
                            ],
                            "grouponLabel": {
                                "grouponActivityId": "ff8080817163f8ef0171766442fa004c",
                                "marketingDesc": "拼团"
                            }
                        }
                    },
                    {
                        "imgHref": "",
                        "imgSrc": "//oss-hz.qianmi.com/x-site/public/x-site-ui-public-weixin/statics/image/goodsList/goodlist-item.png",
                        "name": "示例商品名称",
                        "nameWithSku": "示例商品名称 示例SKU",
                        "specName": "示例SKU",
                        "title": "商品二级标题",
                        "price": "199.00",
                        "buyPoint": NumberInt(0),
                        "promotionLabels": {
                            "couponLabels": [
                                {
                                    "couponActivityId": "ff8080817163f8ef017171470c62001a",
                                    "couponDesc": "满56767减36545",
                                    "couponInfoId": "ff8080817163f8ef0171714373cc0014"
                                }
                            ],
                            "marketingLabels": [
                                {
                                    "marketingDesc": "满2件减1",
                                    "marketingId": NumberInt(356),
                                    "marketingType": NumberInt(0)
                                },
                                {
                                    "marketingDesc": "满3件获赠品，赠完为止",
                                    "marketingId": NumberInt(360),
                                    "marketingType": NumberInt(2)
                                }
                            ],
                            "grouponLabel": {
                                "grouponActivityId": "ff8080817163f8ef0171766442fa004c",
                                "marketingDesc": "拼团"
                            }
                        }
                    }
                ],
                "version": "0.19.195"
            },
            "widgetNameSpace": "@wanmi/wechat-goodslist"
        },
        "updatedAt": ISODate("2020-08-04T17:19:46.435+08:00"),
        "type": "COMMON",
        "isCommon": true,
        "__v": NumberInt(0),
        "createdAt": ISODate("2018-08-07T14:21:59.106+08:00"),
        "cateId": "5add9b42e15b9a13a2a3bd26",
        "sortIndex": NumberInt(6)
    }
})