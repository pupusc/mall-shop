// common_bar_infos  通用组件

// 5f0d5e4e589de89d84977d46    预售组件
db.getCollection("common_bar_infos").insert({
    "_id": "5f0d5e4e589de89d84977d46",
    "envCode": "test1",
    "key": "@wanmi/wechat-presalelist",
    "replicable": true,
    "img": "",
    "dependencies": [
        "@wanmi/wechat-presalelist"
    ],
    "isAdvanced": true,
    "packageName": "@wanmi/wechat-presalelist",
    "platform": "weixin",
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
    "updatedAt": ISODate("2020-07-14T17:48:42.034+08:00"),
    "__v": NumberInt(0),
    "createdAt": ISODate("2020-07-14T15:27:13.514+08:00"),
    "refs": [
        "d2cStore"
    ]
})


// 5f0fc62b589de89d849f13b3    二级分类组件
db.getCollection("common_bar_infos").insert({
    "_id": "5f0fc62b589de89d849f13b3",
    "envCode": "test1",
    "key": "@wanmi/wechat-lev2cate",
    "replicable": false,
    "img": "",
    "dependencies": [
        "@wanmi/wechat-lev2cate"
    ],
    "isAdvanced": true,
    "packageName": "@wanmi/wechat-lev2cate",
    "platform": "weixin",
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
    "updatedAt": ISODate("2020-08-04T11:31:42.559+08:00"),
    "__v": NumberInt(0),
    "createdAt": ISODate("2020-07-16T11:14:52.930+08:00"),
    "refs": [
        "d2cStore"
    ]
})
// 5f0fc64f589de89d849f1445    三级分类组件
db.getCollection("common_bar_infos").insert({
    "_id": "5f0fc64f589de89d849f1445",
    "envCode": "test1",
    "key": "@wanmi/wechat-lev3cate",
    "replicable": false,
    "img": "",
    "dependencies": [
        "@wanmi/wechat-lev3cate"
    ],
    "isAdvanced": true,
    "packageName": "@wanmi/wechat-lev3cate",
    "platform": "weixin",
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
    "updatedAt": ISODate("2020-08-04T11:36:36.462+08:00"),
    "__v": NumberInt(0),
    "createdAt": ISODate("2020-07-16T11:15:28.724+08:00"),
    "refs": [
        "d2cStore"
    ]
})
// 5f0fc664589de89d849f14aa    一级分类组件
db.getCollection("common_bar_infos").insert({
    "_id": "5f0fc664589de89d849f14aa",
    "envCode": "test1",
    "key": "@wanmi/wechat-lev1cate",
    "replicable": false,
    "img": "",
    "dependencies": [
        "@wanmi/wechat-lev1cate"
    ],
    "isAdvanced": true,
    "packageName": "@wanmi/wechat-lev1cate",
    "platform": "weixin",
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
    "updatedAt": ISODate("2020-08-04T11:31:40.439+08:00"),
    "__v": NumberInt(0),
    "createdAt": ISODate("2020-07-16T11:15:50.326+08:00"),
    "refs": [
        "d2cStore"
    ]
})
// 5f0fc67b589de89d849f150a    二级分类+商品列表组件
db.getCollection("common_bar_infos").insert({
    "_id": "5f0fc67b589de89d849f150a",
    "envCode": "test1",
    "key": "@wanmi/wechat-lev2goodslist",
    "replicable": false,
    "img": "",
    "dependencies": [
        "@wanmi/wechat-lev2goodslist"
    ],
    "isAdvanced": true,
    "packageName": "@wanmi/wechat-lev2goodslist",
    "platform": "weixin",
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
    "updatedAt": ISODate("2020-08-04T11:36:35.712+08:00"),
    "__v": NumberInt(0),
    "createdAt": ISODate("2020-07-16T11:16:12.718+08:00"),
    "refs": [
        "d2cStore"
    ]
})
// 5ee862302dde71e43bbd90ca    预约组件
db.getCollection("common_bar_infos").insert({
    "_id": "5ee862302dde71e43bbd90ca",
    "envCode": "test1",
    "key": "@wanmi/wechat-preorderlist",
    "replicable": true,
    "img": "",
    "dependencies": [
        "@wanmi/wechat-preorderlist"
    ],
    "isAdvanced": true,
    "packageName": "@wanmi/wechat-preorderlist",
    "platform": "weixin",
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
    "updatedAt": ISODate("2020-07-28T16:14:02.659+08:00"),
    "__v": NumberInt(0),
    "createdAt": ISODate("2020-06-16T14:09:55.395+08:00"),
    "refs": [
        "d2cStore"
    ]
})

// 5ecf67ad904320f53808b806    拼团组件
db.getCollection("common_bar_infos").insert({
    "_id": "5ecf67ad904320f53808b806",
    "envCode": "test1",
    "key": "@wanmi/wechat-grouponlist",
    "replicable": true,
    "img": "",
    "dependencies": [
        "@wanmi/wechat-grouponlist"
    ],
    "isAdvanced": true,
    "packageName": "@wanmi/wechat-grouponlist",
    "platform": "weixin",
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
    "updatedAt": ISODate("2020-07-30T09:23:05.043+08:00"),
    "__v": NumberInt(0),
    "createdAt": ISODate("2020-05-28T15:26:40.828+08:00"),
    "refs": [
        "d2cStore"
    ]
})
// 5eedd0f42dde71e43bcf4f51    秒杀组件

db.getCollection("common_bar_infos").insert({
    "_id": "5eedd0f42dde71e43bcf4f51",
    "envCode": "test1",
    "key": "@wanmi/wechat-flashlist",
    "replicable": true,
    "img": "",
    "dependencies": [
        "@wanmi/wechat-flashlist"
    ],
    "isAdvanced": true,
    "packageName": "@wanmi/wechat-flashlist",
    "platform": "weixin",
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
    "updatedAt": ISODate("2020-08-04T14:12:43.440+08:00"),
    "__v": NumberInt(0),
    "createdAt": ISODate("2020-06-20T17:03:51.582+08:00"),
    "refs": [
        "d2cStore"
    ]
})
// 5b693a83916d0427ef917745    商品组件 修改
db.common_bar_infos.update({ _id: ObjectId("5b693a83916d0427ef917745") }, {
    $set: {
        "envCode": "test1",
        "key": "@wanmi/wechat-goodslist",
        "replicable": true,
        "img": "",
        "dependencies": [
            "@wanmi/wechat-goodslist"
        ],
        "isAdvanced": true,
        "packageName": "@wanmi/wechat-goodslist",
        "platform": "weixin",
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
        "updatedAt": ISODate("2020-08-04T17:19:46.427+08:00"),
        "__v": NumberInt(0),
        "createdAt": ISODate("2018-08-07T14:21:53.580+08:00"),
        "refs": [
            "d2cStore"
        ]
    }
})