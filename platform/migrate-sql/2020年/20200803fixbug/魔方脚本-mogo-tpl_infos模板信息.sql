// tpl_infos 模板信息
// 5f0fccbe589de89d849f2afa    二级分类+商品列表页模板信息
db.getCollection("tpl_infos").insert({
    "_id": "5f0fccbe589de89d849f2afa",
    "envCode": "test1",
    "tplInfoCode": "wechat-lev2goodslist",
    "tplRuleCode": "wechat-lev2goodslist",
    "userId": "ADMIN",
    "sortIndex": NumberInt(2),
    "detailInfo": {
        "author": "峰",
        "previewHtml": "",
        "detailImage": "",
        "previewImage": "https://wanmi-x-site.oss-cn-shanghai.aliyuncs.com/x-site/public/images/6be09630-c716-11ea-948a-311050b854e6.png"
    },
    "systemCode": "d2cStore",
    "tag": [
        "499"
    ],
    "type": "ADMIN",
    "deleted": false,
    "online": true,
    "platform": "weixin",
    "configOrder": {
        "widgetNameSpace": "x-site-ui/widget/horizontal-layout",
        "children": [
            {
                "props": {
                    "key": 1534491500340,
                    "bgColor": "#fff",
                    "bgImg": "https://oss-hz.qianmi.com/x-site/publicx-site-ui-public-weixin/statics/image/nav/bgImg.png",
                    "columns": NumberInt(2),
                    "enableLazyLoad": true,
                    "bjColor": "rgba(247, 247, 247, .9)",
                    "type": "type2",
                    "rows": NumberInt(1),
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
                    "version": "0.0.1",
                    "showType": {
                        "itemStyle": NumberInt(3),
                        "showStyle": NumberInt(1),
                        "cartButton": NumberInt(1),
                        "goodsSetup": {
                            "title": true,
                            "customHorn": "",
                            "cartStyle": NumberInt(3),
                            "hornStyle": NumberInt(2),
                            "name": true,
                            "price": true,
                            "horn": true,
                            "cart": true,
                            "showSku": true
                        }
                    },
                    "selectedSource": NumberInt(4),
                    "textColor": "#333",
                    "checkedItemsId": NumberInt(1),
                    "placeholder": "搜索",
                    "cateItems": [
                        {
                            "lev2Items": [
                                {
                                    "items": [],
                                    "isGroup": false,
                                    "isSpu": false,
                                    "imgSrc": "http://wanmi-x-site.oss-cn-shanghai.aliyuncs.com/x-site/public/images/x-site-ui/classify-banner.png",
                                    "lev2Title": "希维尔",
                                    "id": NumberInt(1)
                                }
                            ],
                            "href": "#",
                            "imgSrc": "http://wanmi-x-site.oss-cn-shanghai.aliyuncs.com/x-site/public/images/x-site-ui/classify-banner.png",
                            "lev1Title": "数码",
                            "id": NumberInt(1)
                        }
                    ],
                    "style": {
                        "dot": "default",
                        "animateType": "none",
                        "animateTime": NumberInt(800),
                        "height": 167.5,
                        "size": "default",
                        "style": "default",
                        "bgColor": "#F9F9F9"
                    },
                    "img1": "https://web-img.qmimg.com/x-site/public/images/weixin/fashion/sousuo_shu.png",
                    "isBgShow": false,
                    "isFixed": true,
                    "lev2CheckedItemsId": NumberInt(1),
                    "isShow": true,
                    "isConfont": true,
                    "point_style": NumberInt(1)
                },
                "widgetNameSpace": "@wanmi/wechat-lev2goodslist"
            }
        ]
    },
    "cateId": "5aba137615dfc54212cd4246",
    "pageType": "classify",
    "tplInfoName": "二级分类+商品列表页",
    "updatedAt": ISODate("2020-07-16T11:47:15.038+08:00"),
    "__v": NumberInt(0),
    "createdAt": ISODate("2020-07-16T11:42:56.794+08:00")
})
// 5f0fcb80589de89d849f2684    一级分类模板信息
db.getCollection("tpl_infos").insert({
    "_id": "5f0fcb80589de89d849f2684",
    "envCode": "test1",
    "tplInfoCode": "wechat-lev1cate",
    "tplRuleCode": "wechat-lev1cate",
    "userId": "ADMIN",
    "sortIndex": NumberInt(3),
    "detailInfo": {
        "author": "峰",
        "previewHtml": "",
        "detailImage": "",
        "previewImage": "https://wanmi-x-site.oss-cn-shanghai.aliyuncs.com/x-site/public/images/ae3bd9a0-c715-11ea-948a-311050b854e6.png"
    },
    "systemCode": "d2cStore",
    "tag": [
        "499"
    ],
    "type": "ADMIN",
    "deleted": false,
    "online": true,
    "platform": "weixin",
    "configOrder": {
        "children": [
            {
                "widgetNameSpace": "@wanmi/wechat-lev1cate",
                "props": {
                    "key": 1534491500340,
                    "bgColor": "#fff",
                    "bgImg": "//oss-hz.qianmi.com/x-site/publicx-site-ui-public-weixin/statics/image/nav/bgImg.png",
                    "columns": NumberInt(2),
                    "enableLazyLoad": true,
                    "bjColor": "rgba(247, 247, 247, .9)",
                    "type": "type1",
                    "rows": NumberInt(1),
                    "sources": [
                        {
                            "size": NumberInt(4),
                            "type": "1"
                        },
                        {
                            "size": NumberInt(4),
                            "type": "2"
                        },
                        {
                            "cateName": "",
                            "cateID": "",
                            "size": NumberInt(4),
                            "type": "3"
                        },
                        {
                            "type": "4"
                        }
                    ],
                    "version": "0.0.1",
                    "showType": {
                        "goodsSetup": {
                            "title": true,
                            "customHorn": "",
                            "cartStyle": NumberInt(3),
                            "hornStyle": NumberInt(2),
                            "name": true,
                            "price": true,
                            "horn": true,
                            "cart": true,
                            "showSku": true
                        },
                        "cartButton": NumberInt(1),
                        "showStyle": NumberInt(1),
                        "itemStyle": NumberInt(3)
                    },
                    "selectedSource": NumberInt(4),
                    "textColor": "#333",
                    "checkedItemsId": NumberInt(1),
                    "placeholder": "搜索",
                    "cateItems": [
                        {
                            "items": [],
                            "isSpu": false,
                            "title": "数码",
                            "id": NumberInt(1)
                        }
                    ],
                    "style": {
                        "bgColor": "#F9F9F9",
                        "style": "default",
                        "size": "default",
                        "height": 167.5,
                        "animateTime": NumberInt(800),
                        "animateType": "none",
                        "dot": "default"
                    },
                    "img1": "http://web-img.qmimg.com/x-site/public/images/weixin/fashion/sousuo_shu.png",
                    "isBgShow": false,
                    "isFixed": true,
                    "isShow": true,
                    "isConfont": true,
                    "point_style": NumberInt(1)
                }
            }
        ],
        "widgetNameSpace": "x-site-ui/widget/horizontal-layout"
    },
    "cateId": "5aba137615dfc54212cd4246",
    "pageType": "classify",
    "tplInfoName": "一级分类+商品分类页",
    "updatedAt": ISODate("2020-07-16T11:47:31.826+08:00"),
    "__v": NumberInt(0),
    "createdAt": ISODate("2020-07-16T11:37:38.600+08:00")
})
// 5f0fcbd7589de89d849f27ce    一级分类模板信息
db.getCollection("tpl_infos").insert({
    "_id": "5f0fcbd7589de89d849f27ce",
    "envCode": "test1",
    "tplInfoCode": "wechat-lev1cate-v",
    "tplRuleCode": "wechat-lev1cate",
    "userId": "ADMIN",
    "sortIndex": NumberInt(4),
    "detailInfo": {
        "author": "峰",
        "previewHtml": "",
        "detailImage": "",
        "previewImage": "https://wanmi-x-site.oss-cn-shanghai.aliyuncs.com/x-site/public/images/e3024980-c715-11ea-948a-311050b854e6.png"
    },
    "systemCode": "d2cStore",
    "tag": [
        "499"
    ],
    "type": "ADMIN",
    "deleted": false,
    "online": true,
    "platform": "weixin",
    "configOrder": {
        "widgetNameSpace": "x-site-ui/widget/horizontal-layout",
        "children": [
            {
                "props": {
                    "key": 1534491500340,
                    "bgColor": "#fff",
                    "bgImg": "//oss-hz.qianmi.com/x-site/publicx-site-ui-public-weixin/statics/image/nav/bgImg.png",
                    "columns": NumberInt(2),
                    "enableLazyLoad": true,
                    "bjColor": "rgba(247, 247, 247, .9)",
                    "type": "type2",
                    "rows": NumberInt(1),
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
                    "version": "0.0.1",
                    "showType": {
                        "itemStyle": NumberInt(3),
                        "showStyle": NumberInt(1),
                        "cartButton": NumberInt(1),
                        "goodsSetup": {
                            "title": true,
                            "customHorn": "",
                            "cartStyle": NumberInt(3),
                            "hornStyle": NumberInt(2),
                            "name": true,
                            "price": true,
                            "horn": true,
                            "cart": true,
                            "showSku": true
                        }
                    },
                    "selectedSource": NumberInt(4),
                    "textColor": "#333",
                    "checkedItemsId": NumberInt(1),
                    "placeholder": "搜索",
                    "cateItems": [
                        {
                            "id": NumberInt(1),
                            "title": "数码",
                            "isSpu": false,
                            "items": []
                        }
                    ],
                    "style": {
                        "dot": "default",
                        "animateType": "none",
                        "animateTime": NumberInt(800),
                        "height": 167.5,
                        "size": "default",
                        "style": "default",
                        "bgColor": "#F9F9F9"
                    },
                    "img1": "http://web-img.qmimg.com/x-site/public/images/weixin/fashion/sousuo_shu.png",
                    "isBgShow": false,
                    "isFixed": true,
                    "isShow": true,
                    "isConfont": true,
                    "point_style": NumberInt(1)
                },
                "widgetNameSpace": "@wanmi/wechat-lev1cate"
            }
        ]
    },
    "cateId": "5aba137615dfc54212cd4246",
    "pageType": "classify",
    "tplInfoName": "一级分类+商品分类页",
    "updatedAt": ISODate("2020-07-16T11:47:39.976+08:00"),
    "__v": NumberInt(0),
    "createdAt": ISODate("2020-07-16T11:39:04.843+08:00")
})
// 5f0fcc77589de89d849f29fe    二级分类模板信息
db.getCollection("tpl_infos").insert({
    "_id": "5f0fcc77589de89d849f29fe",
    "envCode": "test1",
    "tplInfoCode": "wechat-lev2cate",
    "tplRuleCode": "wechat-lev2cate",
    "userId": "ADMIN",
    "sortIndex": NumberInt(1),
    "detailInfo": {
        "author": "峰",
        "previewHtml": "",
        "detailImage": "",
        "previewImage": "https://wanmi-x-site.oss-cn-shanghai.aliyuncs.com/x-site/public/images/43091ca0-c716-11ea-948a-311050b854e6.png"
    },
    "systemCode": "d2cStore",
    "tag": [
        "499"
    ],
    "type": "ADMIN",
    "deleted": false,
    "online": true,
    "platform": "weixin",
    "configOrder": {
        "widgetNameSpace": "x-site-ui/widget/horizontal-layout",
        "children": [
            {
                "props": {
                    "key": 1534491500340,
                    "bgColor": "#fff",
                    "bgImg": "https://oss-hz.qianmi.com/x-site/publicx-site-ui-public-weixin/statics/image/nav/bgImg.png",
                    "columns": NumberInt(2),
                    "enableLazyLoad": true,
                    "bjColor": "rgba(247, 247, 247, .9)",
                    "type": "type2",
                    "rows": NumberInt(1),
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
                    "version": "0.0.1",
                    "showType": {
                        "itemStyle": NumberInt(3),
                        "showStyle": NumberInt(1),
                        "cartButton": NumberInt(1),
                        "goodsSetup": {
                            "title": true,
                            "customHorn": "",
                            "cartStyle": NumberInt(3),
                            "hornStyle": NumberInt(2),
                            "name": true,
                            "price": true,
                            "horn": true,
                            "cart": true,
                            "showSku": true
                        }
                    },
                    "selectedSource": NumberInt(4),
                    "textColor": "#333",
                    "checkedItemsId": NumberInt(1),
                    "placeholder": "搜索",
                    "cateItems": [
                        {
                            "lev2Items": [
                                {
                                    "lev2Title": "笔记本电脑",
                                    "imgSrc": "http://wanmi-x-site.oss-cn-shanghai.aliyuncs.com/x-site/public/images/x-site-ui/lev3cate-img.png"
                                }
                            ],
                            "href": "#",
                            "imgSrc": "http://wanmi-x-site.oss-cn-shanghai.aliyuncs.com/x-site/public/images/x-site-ui/classify-banner.png",
                            "lev1Title": "数码",
                            "id": NumberInt(1)
                        }
                    ],
                    "style": {
                        "dot": "default",
                        "animateType": "none",
                        "animateTime": NumberInt(800),
                        "height": 167.5,
                        "size": "default",
                        "style": "default",
                        "bgColor": "#F9F9F9"
                    },
                    "img1": "https://web-img.qmimg.com/x-site/public/images/weixin/fashion/sousuo_shu.png",
                    "isBgShow": false,
                    "isFixed": true,
                    "isShow": true,
                    "isConfont": true,
                    "point_style": NumberInt(1)
                },
                "widgetNameSpace": "@wanmi/wechat-lev2cate"
            }
        ]
    },
    "cateId": "5aba137615dfc54212cd4246",
    "pageType": "classify",
    "tplInfoName": "二级分类页",
    "updatedAt": ISODate("2020-07-16T11:47:52.975+08:00"),
    "__v": NumberInt(0),
    "createdAt": ISODate("2020-07-16T11:41:45.398+08:00")
})
// 5f0fccf4589de89d849f2bc0    三级分类模板信息
db.getCollection("tpl_infos").insert({
    "_id": "5f0fccf4589de89d849f2bc0",
    "envCode": "test1",
    "tplInfoCode": "wechat-lev3cate",
    "tplRuleCode": "wechat-lev3cate",
    "userId": "ADMIN",
    "sortIndex": NumberInt(0),
    "detailInfo": {
        "author": "峰",
        "previewHtml": "",
        "detailImage": "",
        "previewImage": "https://wanmi-x-site.oss-cn-shanghai.aliyuncs.com/x-site/public/images/898871d0-c716-11ea-948a-311050b854e6.png"
    },
    "systemCode": "d2cStore",
    "tag": [
        "499"
    ],
    "type": "ADMIN",
    "deleted": false,
    "online": true,
    "platform": "weixin",
    "configOrder": {
        "children": [
            {
                "widgetNameSpace": "@wanmi/wechat-lev3cate",
                "props": {
                    "key": 1534491500340,
                    "bgColor": "#fff",
                    "bgImg": "https://oss-hz.qianmi.com/x-site/publicx-site-ui-public-weixin/statics/image/nav/bgImg.png",
                    "columns": NumberInt(2),
                    "enableLazyLoad": true,
                    "bjColor": "rgba(247, 247, 247, .9)",
                    "type": "type2",
                    "rows": NumberInt(1),
                    "sources": [
                        {
                            "size": NumberInt(4),
                            "type": "1"
                        },
                        {
                            "size": NumberInt(4),
                            "type": "2"
                        },
                        {
                            "cateName": "",
                            "cateID": "",
                            "size": NumberInt(4),
                            "type": "3"
                        },
                        {
                            "type": "4"
                        }
                    ],
                    "version": "0.0.1",
                    "showType": {
                        "goodsSetup": {
                            "title": true,
                            "customHorn": "",
                            "cartStyle": NumberInt(3),
                            "hornStyle": NumberInt(2),
                            "name": true,
                            "price": true,
                            "horn": true,
                            "cart": true,
                            "showSku": true
                        },
                        "cartButton": NumberInt(1),
                        "showStyle": NumberInt(1),
                        "itemStyle": NumberInt(3)
                    },
                    "selectedSource": NumberInt(4),
                    "textColor": "#333",
                    "checkedItemsId": NumberInt(1),
                    "placeholder": "搜索",
                    "cateItems": [
                        {
                            "lev2Items": [
                                {
                                    "lev3Items": [
                                        {
                                            "lev3Title": "笔记本电脑",
                                            "href": "#",
                                            "imgSrc": "http://wanmi-x-site.oss-cn-shanghai.aliyuncs.com/x-site/public/images/x-site-ui/lev3cate-img.png"
                                        }
                                    ],
                                    "imgSrc": "",
                                    "lev2Title": "数码配件"
                                }
                            ],
                            "href": "#",
                            "imgSrc": "http://wanmi-x-site.oss-cn-shanghai.aliyuncs.com/x-site/public/images/x-site-ui/classify-banner.png",
                            "lev1Title": "数码",
                            "id": NumberInt(1)
                        }
                    ],
                    "style": {
                        "bgColor": "#F9F9F9",
                        "style": "default",
                        "size": "default",
                        "height": 167.5,
                        "animateTime": NumberInt(800),
                        "animateType": "none",
                        "dot": "default"
                    },
                    "img1": "https://web-img.qmimg.com/x-site/public/images/weixin/fashion/sousuo_shu.png",
                    "isBgShow": false,
                    "isFixed": true,
                    "isShow": true,
                    "isConfont": true,
                    "point_style": NumberInt(1)
                }
            }
        ],
        "widgetNameSpace": "x-site-ui/widget/horizontal-layout"
    },
    "cateId": "5aba137615dfc54212cd4246",
    "pageType": "classify",
    "tplInfoName": "三级分类页",
    "updatedAt": ISODate("2020-07-16T11:48:00.269+08:00"),
    "__v": NumberInt(0),
    "createdAt": ISODate("2020-07-16T11:43:50.804+08:00")
})
