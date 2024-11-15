package com.fido.common.generatefile

/**
 * @author: FiDo
 * @date: 2024/11/12
 * @des:
 */

object TestJson {
    val TestJsonStr = """
    {
      "global": {
        "Downshadow": {
          "value": {
            "color": "#0000000f",
            "type": "dropShadow",
            "x": "0",
            "y": "-2",
            "blur": "6",
            "spread": "0"
          },
          "type": "boxShadow"
        },
        "Upshadow": {
          "value": {
            "color": "#0000000f",
            "type": "dropShadow",
            "x": "0",
            "y": "2",
            "blur": "6",
            "spread": "0"
          },
          "type": "boxShadow"
        },
        "Radius_22": {
          "value": "22",
          "type": "borderRadius",
          "description": "大按钮圆角"
        },
        "Radius_20": {
          "value": "20",
          "type": "borderRadius",
          "description": "中按钮圆角"
        },
        "Radius_16": {
          "value": "16",
          "type": "borderRadius",
          "description": "小按钮圆角、半弹窗"
        },
        "Radius_12": {
          "value": "12",
          "type": "borderRadius",
          "description": "特小按钮圆角"
        },
        "Radius_8": {
          "value": "8",
          "type": "borderRadius",
          "description": "楼层圆角/卡片圆角/吐司圆角"
        },
        "Radius_6": {
          "value": "6",
          "type": "borderRadius",
          "description": "暂定"
        },
        "Radius_4": {
          "value": "4",
          "type": "borderRadius",
          "description": "标签圆角"
        },
        "Radius_2": {
          "value": "2",
          "type": "borderRadius",
          "description": "标签圆角"
        },
        "Padding_32": {
          "value": "32",
          "type": "spacing",
          "description": "组件内间距"
        },
        "Padding_24": {
          "value": "24",
          "type": "spacing",
          "description": "组件内间距"
        },
        "Padding_20": {
          "value": "20",
          "type": "spacing",
          "description": "组件内间距"
        },
        "Padding_16": {
          "value": "16",
          "type": "spacing",
          "description": "组件内间距/标题下间距/\n页面左右间距"
        },
        "Padding_14": {
          "value": "14",
          "type": "spacing",
          "description": "内容间距（尽量少用）"
        },
        "Padding_12": {
          "value": "12",
          "type": "spacing",
          "description": "楼层/卡片间距"
        },
        "Padding_10": {
          "value": "10",
          "type": "spacing",
          "description": "暂定"
        },
        "Padding_8": {
          "value": "8",
          "type": "spacing",
          "description": "分隔条/楼层间距"
        },
        "Padding_6": {
          "value": "6",
          "type": "spacing",
          "description": "组内间距"
        },
        "Padding_4": {
          "value": "4",
          "type": "spacing",
          "description": "组内间距"
        },
        "Padding_2": {
          "value": "2",
          "type": "spacing",
          "description": "组内间距"
        },
        "Primary_1": {
          "value": "#fff3f0",
          "type": "color",
          "description": "定义：浅色填充底色\n\ntoken：Primary_1"
        },
        "Primary_2": {
          "value": "#ffcfc7",
          "type": "color",
          "description": "定义：线性填充_不可用状态\n\ntoken：Primary_2"
        },
        "Primary_3": {
          "value": "#ffa99e",
          "type": "color",
          "description": "定义：面形填充 禁用\n\ntoken：Primary_3\n"
        },
        "Primary_4": {
          "value": "#ff8175",
          "type": "color"
        },
        "Primary_5": {
          "value": "#ff554d",
          "type": "color"
        },
        "Primary_6": {
          "value": "#f32823",
          "type": "color",
          "description": "定义：品牌主色\n\ntoken：Primary_6"
        },
        "Primary_7": {
          "value": "#cc1215",
          "type": "color",
          "description": "定义：面性填充-按下状态\n\ntoken: Primary_7"
        },
        "Primary_8": {
          "value": "#a6070f",
          "type": "color"
        },
        "Primary_9": {
          "value": "#80000b",
          "type": "color"
        },
        "Primary_10": {
          "value": "#59000a",
          "type": "color"
        },
        "Grey_Text1": {
          "value": "#282b2e",
          "type": "color",
          "description": "定义：1级字体颜色\n\ntoken：Grey_Text1"
        },
        "Grey_Text2": {
          "value": "#6e737d",
          "type": "color",
          "description": "定义：2级字体颜色\n\ntoken：Grey_Text2"
        },
        "Grey_Text3": {
          "value": "#989fa9",
          "type": "color",
          "description": "定义：3级字体颜色\n\ntoken：Grey_Text3"
        },
        "Grey_Text4": {
          "value": "#c5c9cd",
          "type": "color",
          "description": "定义：4级字体颜色\n\ntoken：Grey_Text4"
        },
        "Grey_Divider": {
          "value": "#e2e5e9",
          "type": "color",
          "description": "定义：分割线颜色\n\ntoken：Grey_Divider"
        },
        "Grey_Bg": {
          "value": "#eff2f6",
          "type": "color",
          "description": "定义：浅色背景色\n\ntoken：Grey_Bg"
        },
        "Mask_Color": {
          "value": "#00000099",
          "type": "color",
          "description": "定义：全局蒙层颜色\n\ntoken：Mask"
        },
        "Toast_Bg": {
          "value": "#000000a6",
          "type": "color",
          "description": "定义：toast背景色\n\ntoken：Toast_Bg"
        },
        "White_Text1": {
          "value": "#ffffff",
          "type": "color",
          "description": "定义：1级字体颜色（深色背景下）\n\ntoken：Grey_Text1"
        },
        "White_Text2": {
          "value": "#ffffffcc",
          "type": "color",
          "description": "定义：2级字体颜色（深色背景下）\n\ntoken：Grey_Text2"
        },
        "White_Text3": {
          "value": "#ffffffa6",
          "type": "color",
          "description": "定义：3级字体颜色（深色背景下）\n\ntoken：Grey_Text3"
        },
        "White_Text4": {
          "value": "#ffffff80",
          "type": "color",
          "description": "定义：4级字体颜色（深色背景下）\n\ntoken：Grey_Text4"
        },
        "White_Divider": {
          "value": "#ffffff33",
          "type": "color",
          "description": "定义：分割线（深色背景下）\n\ntoken：White_Divider"
        },
        "White_Bg": {
          "value": "#ffffff",
          "type": "color",
          "description": "定义：容器背景（深色背景下）\n\ntoken：White_Bg"
        },
        "Error_1": {
          "value": "#fff3f0",
          "type": "color",
          "description": "定义：错误浅色背景\n\ntoken：Error_1"
        },
        "Error_2": {
          "value": "#ffcfc7",
          "type": "color"
        },
        "Error_3": {
          "value": "#ffa99e",
          "type": "color"
        },
        "Error_4": {
          "value": "#ff8175",
          "type": "color"
        },
        "Error_5": {
          "value": "#ff554d",
          "type": "color"
        },
        "Error_6": {
          "value": "#f32823",
          "type": "color",
          "description": "定义：错误颜色\n\ntoken：Error_6"
        },
        "Error_7": {
          "value": "#cc1215",
          "type": "color",
          "description": "定义：错误文字颜色\n\ntoken：Error_7"
        },
        "Error_8": {
          "value": "#a6070f",
          "type": "color"
        },
        "Error_9": {
          "value": "#80000b",
          "type": "color"
        },
        "Success_1": {
          "value": "#e6ffef",
          "type": "color",
          "description": "定义：成功浅色背景\n\ntoken：Success_1"
        },
        "Error_10": {
          "value": "#59000a",
          "type": "color"
        },
        "Success_2": {
          "value": "#a0f2c1",
          "type": "color"
        },
        "Success_3": {
          "value": "#73e6a4",
          "type": "color"
        },
        "Success_4": {
          "value": "#4ad98c",
          "type": "color"
        },
        "Success_5": {
          "value": "#25cc78",
          "type": "color"
        },
        "Success_6": {
          "value": "#04bf68",
          "type": "color",
          "description": "定义：成功颜色\n\ntoken：Success_6"
        },
        "Success_7": {
          "value": "#009957",
          "type": "color",
          "description": "定义：成功文字颜色\n\ntoken：Success_7"
        },
        "Success_8": {
          "value": "#007345",
          "type": "color"
        },
        "Success_9": {
          "value": "#004d30",
          "type": "color"
        },
        "Success_10": {
          "value": "#002619",
          "type": "color"
        },
        "Link_1": {
          "value": "#e6f8ff",
          "type": "color"
        },
        "Link_2": {
          "value": "#b8eaff",
          "type": "color"
        },
        "Link_3": {
          "value": "#82d5ff",
          "type": "color"
        },
        "Link_4": {
          "value": "#59c2ff",
          "type": "color"
        },
        "Link_5": {
          "value": "#30acff",
          "type": "color"
        },
        "Link_6": {
          "value": "#0892fc",
          "type": "color",
          "description": "定义：提示颜色\n\ntoken：Link_6"
        },
        "Link_7": {
          "value": "#0072d6",
          "type": "color",
          "description": "定义：链接文字，提示文字颜色\n\ntoken：Link_7"
        },
        "Link_8": {
          "value": "#0058b0",
          "type": "color"
        },
        "Link_9": {
          "value": "#00408a",
          "type": "color"
        },
        "Link_10": {
          "value": "#002b63",
          "type": "color"
        },
        "Warning_1": {
          "value": "#fff4e6",
          "type": "color",
          "description": "定义：警告浅色背景\n\ntoken：Warning_1"
        },
        "Warning_2": {
          "value": "#ffd8ad",
          "type": "color"
        },
        "Warning_3": {
          "value": "#ffc085",
          "type": "color"
        },
        "Warning_4": {
          "value": "#ffa55c",
          "type": "color"
        },
        "Warning_5": {
          "value": "#ff8833",
          "type": "color"
        },
        "Warning_6": {
          "value": "#f5640a",
          "type": "color",
          "description": "定义：警告颜色\n\ntoken：Warning_6"
        },
        "Warning_7": {
          "value": "#cf4800",
          "type": "color",
          "description": "定义：警告文字背景\n\ntoken：Warning_7"
        },
        "Warning_8": {
          "value": "#a83500",
          "type": "color"
        },
        "Warning_9": {
          "value": "#822500",
          "type": "color"
        },
        "Warning_10": {
          "value": "#5c1700",
          "type": "color"
        },
        "Red_1": {
          "value": "#fff3f0",
          "type": "color",
          "description": "定义：-\n\ntoken：Red_1"
        },
        "Red_2": {
          "value": "#ffcfc7",
          "type": "color",
          "description": "定义：-\n\ntoken：Red_2"
        },
        "Red_3": {
          "value": "#ffa99e",
          "type": "color",
          "description": "定义：-\n\ntoken：Red_3"
        },
        "Red_4": {
          "value": "#ff8175",
          "type": "color",
          "description": "定义：-\n\ntoken：Red_4"
        },
        "Red_5": {
          "value": "#ff554d",
          "type": "color",
          "description": "定义：-\n\ntoken：Red_5"
        },
        "Red_6": {
          "value": "#f32823",
          "type": "color",
          "description": "定义：-\n\ntoken：Red_6"
        },
        "Red_7": {
          "value": "#cc1215",
          "type": "color",
          "description": "定义：-\n\ntoken：Red_7"
        },
        "Red_8": {
          "value": "#a6070f",
          "type": "color",
          "description": "定义：-\n\ntoken：Red_8"
        },
        "Red_9": {
          "value": "#80000b",
          "type": "color",
          "description": "定义：-\n\ntoken：Red_9"
        },
        "Red_10": {
          "value": "#59000a",
          "type": "color",
          "description": "定义：-\n\ntoken：Red_10"
        },
        "Deeporange_1": {
          "value": "#fff4e6",
          "type": "color",
          "description": "定义：-\n\ntoken：Deeporange_1"
        },
        "Deeporange_2": {
          "value": "#ffd8ad",
          "type": "color",
          "description": "定义：-\n\ntoken：Deeporange_2"
        },
        "Deeporange_3": {
          "value": "#ffc085",
          "type": "color",
          "description": "定义：-\n\ntoken：Deeporange_3"
        },
        "Deeporange_4": {
          "value": "#ffa55c",
          "type": "color",
          "description": "定义：-\n\ntoken：Deeporange_4"
        },
        "Deeporange_5": {
          "value": "#ff8833",
          "type": "color",
          "description": "定义：-\n\ntoken：Deeporange_2"
        },
        "Deeporange_6": {
          "value": "#f5640a",
          "type": "color",
          "description": "定义：-\n\ntoken：Deeporange_6"
        },
        "Deeporange_7": {
          "value": "#cf4800",
          "type": "color",
          "description": "定义：-\n\ntoken：Deeporange_7"
        },
        "Deeporange_8": {
          "value": "#a83500",
          "type": "color",
          "description": "定义：-\n\ntoken：Deeporange_8"
        },
        "Deeporange_9": {
          "value": "#822500",
          "type": "color",
          "description": "定义：-\n\ntoken：Deeporange_9"
        },
        "Deeporange_10": {
          "value": "#5c1700",
          "type": "color",
          "description": "定义：-\n\ntoken：Deeporange_10"
        },
        "Orange_1": {
          "value": "#fff9e6",
          "type": "color",
          "description": "定义：-     \n\ntoken：Orange_1"
        },
        "Orange_2": {
          "value": "#ffe7a3",
          "type": "color",
          "description": "定义：-     \n\ntoken：Orange_2"
        },
        "Orange_3": {
          "value": "#ffd77a",
          "type": "color",
          "description": "定义：-     \n\ntoken：Orange_3"
        },
        "Orange_4": {
          "value": "#ffc552",
          "type": "color",
          "description": "定义：-     \n\ntoken：Orange_4"
        },
        "Orange_5": {
          "value": "#ffb029",
          "type": "color",
          "description": "定义：-     \n\ntoken：Orange_5"
        },
        "Orange_6": {
          "value": "#fa9600",
          "type": "color",
          "description": "定义：-     \n\ntoken：Orange_6"
        },
        "Orange_7": {
          "value": "#d47800",
          "type": "color",
          "description": "定义：-     \n\ntoken：Orange_7"
        },
        "Orange_8": {
          "value": "#ad5c00",
          "type": "color",
          "description": "定义：-     \n\ntoken：Orange_8"
        },
        "Orange_9": {
          "value": "#874400",
          "type": "color",
          "description": "定义：-     \n\ntoken：Orange_9"
        },
        "Orange_10": {
          "value": "#612d00",
          "type": "color",
          "description": "定义：-     \n\ntoken：Orange_10"
        },
        "Yellow_1": {
          "value": "#ffffe6",
          "type": "color",
          "description": "定义：-   \n\ntoken：Yellow_1"
        },
        "Yellow_2": {
          "value": "#fffcad",
          "type": "color",
          "description": "定义：-  \n\n token：Yellow_2"
        },
        "Yellow_3": {
          "value": "#fff785",
          "type": "color",
          "description": "定义：-    \n\ntoken：Yellow_3"
        },
        "Yellow_4": {
          "value": "#ffef5c",
          "type": "color",
          "description": "定义：-     \n\ntoken：Yellow_4"
        },
        "Yellow_5": {
          "value": "#ffe433",
          "type": "color",
          "description": "定义：-     \n\ntoken：Yellow_5"
        },
        "Yellow_6": {
          "value": "#fcd40a",
          "type": "color",
          "description": "定义：-    \n\ntoken：Yellow_6"
        },
        "Yellow_7": {
          "value": "#d6ab00",
          "type": "color",
          "description": "定义：-     \n\ntoken：Yellow_7"
        },
        "Yellow_8": {
          "value": "#b08700",
          "type": "color",
          "description": "定义：-     \n\ntoken：Yellow_8"
        },
        "Yellow_9": {
          "value": "#8a6500",
          "type": "color",
          "description": "定义：-     \n\ntoken：Yellow_9"
        },
        "Yellow_10": {
          "value": "#634600",
          "type": "color",
          "description": "定义：-     \n\ntoken：Yellow_10"
        },
        "Green_1": {
          "value": "#e6ffef",
          "type": "color",
          "description": "定义：-\n\ntoken：Green_1"
        },
        "Green_2": {
          "value": "#a0f2c1",
          "type": "color",
          "description": "定义：-\n\ntoken：Green_2"
        },
        "Green_3": {
          "value": "#73e6a4",
          "type": "color",
          "description": "定义：-\n\ntoken：Green_3"
        },
        "Green_4": {
          "value": "#4ad98c",
          "type": "color",
          "description": "定义：-\n\ntoken：Green_4"
        },
        "Green_5": {
          "value": "#25cc78",
          "type": "color",
          "description": "定义：-\n\ntoken：Green_5"
        },
        "Green_6": {
          "value": "#04bf68",
          "type": "color",
          "description": "定义：-\n\ntoken：Green_6"
        },
        "Green_7": {
          "value": "#009957",
          "type": "color",
          "description": "定义：-\n\ntoken：Green_7"
        },
        "Green_8": {
          "value": "#007345",
          "type": "color",
          "description": "定义：-\n\ntoken：Green_8"
        },
        "Green_9": {
          "value": "#004d30",
          "type": "color",
          "description": "定义：-\n\ntoken：Green_9"
        },
        "Green_10": {
          "value": "#002619",
          "type": "color",
          "description": "定义：-\n\ntoken：Green_10"
        },
        "Cyan_1": {
          "value": "#e6fffb",
          "type": "color",
          "description": "定义：- \n\ntoken：Cyan_1"
        },
        "Cyan_2": {
          "value": "#a3fff3",
          "type": "color",
          "description": "定义：- \n\ntoken：Cyan_2"
        },
        "Cyan_3": {
          "value": "#74f2e6",
          "type": "color",
          "description": "定义：- \n\ntoken：Cyan_3"
        },
        "Cyan_4": {
          "value": "#49e6db",
          "type": "color",
          "description": "定义：- \n\ntoken：Cyan_4"
        },
        "Cyan_5": {
          "value": "#23d9d3",
          "type": "color",
          "description": "定义：- \n\ntoken：Cyan_5"
        },
        "Cyan_6": {
          "value": "#00cccc",
          "type": "color",
          "description": "定义：- \n\ntoken：Cyan_6"
        },
        "Cyan_7": {
          "value": "#00a0a6",
          "type": "color",
          "description": "定义：- \n\ntoken：Cyan_7"
        },
        "Cyan_8": {
          "value": "#007780",
          "type": "color",
          "description": "定义：- \n\ntoken：Cyan_8"
        },
        "Cyan_9": {
          "value": "#005059",
          "type": "color",
          "description": "定义：- \n\ntoken：Cyan_9"
        },
        "Cyan_10": {
          "value": "#002c33",
          "type": "color",
          "description": "定义：- \n\ntoken：Cyan_10"
        },
        "Blue_1": {
          "value": "#e6f8ff",
          "type": "color",
          "description": "定义：-\n\ntoken：Blue_1"
        },
        "Blue_2": {
          "value": "#b8eaff",
          "type": "color",
          "description": "定义：-\n\ntoken：Blue_2"
        },
        "Blue_3": {
          "value": "#82d5ff",
          "type": "color",
          "description": "定义：-\n\ntoken：Blue_3"
        },
        "Blue_4": {
          "value": "#59c2ff",
          "type": "color",
          "description": "定义：-\n\ntoken：Blue_4"
        },
        "Blue_5": {
          "value": "#30acff",
          "type": "color",
          "description": "定义：-\n\ntoken：Blue_5"
        },
        "Blue_6": {
          "value": "#0892fc",
          "type": "color",
          "description": "定义：-\n\ntoken：Blue_6"
        },
        "Blue_7": {
          "value": "#0072d6",
          "type": "color",
          "description": "定义：-\n\ntoken：Blue_7"
        },
        "Blue_8": {
          "value": "#0058b0",
          "type": "color",
          "description": "定义：-\n\ntoken：Blue_8"
        },
        "Blue_9": {
          "value": "#00408a",
          "type": "color",
          "description": "定义：-\n\ntoken：Blue_9"
        },
        "Blue_10": {
          "value": "#002b63",
          "type": "color",
          "description": "定义：-\n\ntoken：Blue_10"
        },
        "Deepblue_1": {
          "value": "#f0f2ff",
          "type": "color",
          "description": "定义：-\n\ntoken：Deep Blue_1"
        },
        "Deepblue_2": {
          "value": "#e0e4ff",
          "type": "color",
          "description": "定义：-\n\ntoken：Deep Blue_2"
        },
        "Deepblue_3": {
          "value": "#b8bfff",
          "type": "color",
          "description": "定义：-\n\ntoken：Deep Blue_3"
        },
        "Deepblue_4": {
          "value": "#8f96ff",
          "type": "color",
          "description": "定义：-\n\ntoken：Deep Blue_4"
        },
        "Deepblue_5": {
          "value": "#666bff",
          "type": "color",
          "description": "定义：-\n\ntoken：Deep Blue_5"
        },
        "Deepblue_6": {
          "value": "#3b3bf5",
          "type": "color",
          "description": "定义：-\n\ntoken：Deep Blue_6"
        },
        "Deepblue_7": {
          "value": "#2d27cf",
          "type": "color",
          "description": "定义：-\n\ntoken：Deep Blue_7"
        },
        "Deepblue_8": {
          "value": "#2118a8",
          "type": "color",
          "description": "定义：-\n\ntoken：Deep Blue_8"
        },
        "Deepblue_9": {
          "value": "#180c82",
          "type": "color",
          "description": "定义：-\n\ntoken：Deep Blue_9"
        },
        "Deepblue_10": {
          "value": "#13075c",
          "type": "color",
          "description": "定义：-\n\ntoken：Deep Blue_10"
        },
        "Purple_1": {
          "value": "#f9f0ff",
          "type": "color",
          "description": "定义：-\n\ntoken：Purple_1"
        },
        "Purple_2": {
          "value": "#f3e0ff",
          "type": "color",
          "description": "定义：-\n\ntoken：Purple_2"
        },
        "Purple_3": {
          "value": "#e0b8ff",
          "type": "color",
          "description": "定义：-\n\ntoken：Purple_3"
        },
        "Purple_4": {
          "value": "#cb8fff",
          "type": "color",
          "description": "定义：-\n\ntoken：Purple_4"
        },
        "Purple_5": {
          "value": "#ad63f7",
          "type": "color",
          "description": "定义：-\n\ntoken：Purple_5"
        },
        "Purple_6": {
          "value": "#8b38eb",
          "type": "color",
          "description": "定义：-\n\ntoken：Purple_6"
        },
        "Purple_7": {
          "value": "#6a25c4",
          "type": "color",
          "description": "定义：-\n\ntoken：Purple_7"
        },
        "Purple_8": {
          "value": "#4d169e",
          "type": "color",
          "description": "定义：-\n\ntoken：Purple_8"
        },
        "Purple_9": {
          "value": "#330b78",
          "type": "color",
          "description": "定义：-\n\ntoken：Purple_9"
        },
        "Purple_10": {
          "value": "#200752",
          "type": "color",
          "description": "定义：-\n\ntoken：Purple_10"
        },
        "Magenta_1": {
          "value": "#ffebf1",
          "type": "color",
          "description": "定义：-\n\ntoken：Magenta_1"
        },
        "Magenta_2": {
          "value": "#ffc2d8",
          "type": "color",
          "description": "定义：-\n\ntoken：Magenta_2"
        },
        "Magenta_3": {
          "value": "#ff99c2",
          "type": "color",
          "description": "定义：-\n\ntoken：Magenta_3"
        },
        "Magenta_4": {
          "value": "#ff70ae",
          "type": "color",
          "description": "定义：-\n\ntoken：Magenta_4"
        },
        "Magenta_5": {
          "value": "#fc479c",
          "type": "color",
          "description": "定义：-\n\ntoken：Magenta_5"
        },
        "Magenta_6": {
          "value": "#f01d86",
          "type": "color",
          "description": "定义：-\n\ntoken：Magenta_6"
        },
        "Magenta_7": {
          "value": "#c90e72",
          "type": "color",
          "description": "定义：-\n\ntoken：Magenta_7"
        },
        "Magenta_8": {
          "value": "#a3035e",
          "type": "color",
          "description": "定义：-\n\ntoken：Magenta_8"
        },
        "Magenta_9": {
          "value": "#7d004b",
          "type": "color",
          "description": "定义：-\n\ntoken：Magenta_9"
        },
        "Magenta_10": {
          "value": "#570037",
          "type": "color",
          "description": "定义：-\n\ntoken：Magenta_10"
        },
        "Grey_Boxbg": {
          "value": "#F5F7FA",
          "type": "color",
          "description": "容器背景色"
        }
      },
      "\$\themes": [],
      "\$\metadata": {
        "tokenSetOrder": [
          "global"
        ]
      }
    }
""".trimIndent()
}
