package com.ofpay.rex.util;

import org.jsoup.safety.Whitelist;

/**
 * 白名单标签，用于富文本编辑器
 * @author of546
 */
public class QmWhitelist {

    /**
     * 白名单列表
     * 允许输入元素
     * @return
     */
    public static Whitelist relaxed() {
        return new Whitelist()
                .addTags(
                        "a", "b", "blockquote", "br", "caption", "cite", "code", "col",
                        "colgroup", "dd", "div", "dl", "dt", "em", "h1", "h2", "h3", "h4", "h5", "h6",
                        "i", "img", "li", "ol", "p", "pre", "q", "small", "strike", "strong",
                        "sub", "sup", "table", "tbody", "td", "tfoot", "th", "thead", "tr", "u",
                        "ul")

                .addAttributes("div", "id", "class", "style")
                .addAttributes("p", "class", "style")
                .addAttributes("a", "href", "title", "class", "style", "target")
                .addAttributes("blockquote", "cite")
                .addAttributes("col", "span", "width")
                .addAttributes("colgroup", "span", "width")
                .addAttributes("img", "align", "alt", "height", "src", "title", "width", "style")
                .addAttributes("ol", "start", "type", "style")
                .addAttributes("dl", "start", "type", "class", "style")
                .addAttributes("q", "cite")
                .addAttributes("table", "summary", "width", "style")
                .addAttributes("td", "abbr", "axis", "colspan", "rowspan", "width", "style")
                .addAttributes("th", "abbr", "axis", "colspan", "rowspan", "scope", "width", "style")
                .addAttributes("ul", "type", "style")
                .addAttributes("li", "style")

                .addProtocols("a", "href", "ftp", "http", "https", "mailto")
                .addProtocols("blockquote", "cite", "http", "https")
                .addProtocols("cite", "cite", "http", "https")
                .addProtocols("img", "src", "http", "https")
                .addProtocols("q", "cite", "http", "https")
                ;
    }

    /**
     * CSS 样式
     * @return
     */
    public static String[] cssStyle() {
        return new String[] {
                // background
                "background",            // 简写属性，作用是将背景属性设置在一个声明中
                "background-attachment", // 背景图像是否固定或者随着页面的其余部分滚动
                "background-color",      // 设置元素的背景颜色
                "background-image",      // 把图像设置为背景
                "background-position",   // 设置背景图像的起始位置
                "background-repeat",     // 设置背景图像是否及如何重复
                // text
                "color",                 // 设置文本颜色
                "line-height",           // 设置行高
                "letter-spacing",        // 设置字符间距
                "text-align",            // 对齐元素中的文本
                "text-decoration",       // 向文本添加修饰
                "text-indent",           // 缩进元素中文本的首行
                "white-space",           // 设置元素中空白的处理方式
                "word-spacing",          // 设置字间距
                // font
                "font",                  // 简写属性。作用是把所有针对字体的属性设置在一个声明中
                "font-family",           // 设置字体系列
                "font-size",             // 设置字体的尺寸
                "font-style",            // 设置字体风格
                "font-variant",          // 以小型大写字体或者正常字体显示文本
                "font-weight",           // 设置字体的粗细
                // link
                // list
                "list-style",            // 简写属性。用于把所有用于列表的属性设置于一个声明中
                "list-style-image",      // 将图象设置为列表项标志
                "list-style-position",   // 设置列表中列表项标志的位置
                "list-style-type",       // 设置列表项标志的类型
                // table
                "border-collapse",       // 设置是否把表格边框合并为单一的边框
                "border-spacing",        // 设置分隔单元格边框的距离
                "caption-side",          // 设置表格标题的位置
                "empty-cells",           // 设置是否显示表格中的空单元格
                "table-layout",          // 设置显示单元、行和列的算法
                // outline
                "outline",               // 在一个声明中设置所有的轮廓属性
                "outline-color",         // 设置轮廓的颜色
                "outline-style",         // 设置轮廓的样式
                "outline-width",         // 设置轮廓的宽度

                "padding",               // 内边距，也有资料将其翻译为填充
                "padding-bottom",        // 设置元素的下内边距
                "padding-left",          // 设置元素的左内边距
                "padding-right",         // 设置元素的右内边距
                "padding-top",           // 设置元素的上内边距

                "margin",                // 外边距，也有资料将其翻译为空白或空白边
                "margin-bottom",         // 设置元素的下外边距。
                "margin-left",           // 设置元素的左外边距。
                "margin-right",          // 设置元素的右外边距。
                "margin-top",            // 设置元素的上外边距。

                // border
                "border",                // 简写属性，用于把针对四个边的属性设置在一个声明。
                "border-style",          // 用于设置元素所有边框的样式，或者单独地为各边设置边框样式。
                "border-width",          // 简写属性，用于为元素的所有边框设置宽度，或者单独地为各边边框设置宽度。
                "border-color",          // 简写属性，设置元素的所有边框中可见部分的颜色，或为 4 个边分别设置颜色。
                "border-bottom",         // 简写属性，用于把下边框的所有属性设置到一个声明中。
                "border-bottom-color",   // 设置元素的下边框的颜色。
                "border-bottom-style",   // 设置元素的下边框的样式。
                "border-bottom-width",   // 设置元素的下边框的宽度。
                "border-left",           // 简写属性，用于把左边框的所有属性设置到一个声明中。
                "border-left-color",     // 设置元素的左边框的颜色。
                "border-left-style",     // 设置元素的左边框的样式。
                "border-left-width",     // 设置元素的左边框的宽度。
                "border-right",          // 简写属性，用于把右边框的所有属性设置到一个声明中。
                "border-right-color",    // 设置元素的右边框的颜色。
                "border-right-style",    // 设置元素的右边框的样式。
                "border-right-width",    // 设置元素的右边框的宽度。
                "border-top",            // 简写属性，用于把上边框的所有属性设置到一个声明中。
                "border-top-color",      // 设置元素的上边框的颜色。
                "border-top-style",      // 设置元素的上边框的样式。
                "border-top-width",      // 设置元素的上边框的宽度。

                "width",                 // 宽度
                "height"                 // 高度

        };
    }

}
