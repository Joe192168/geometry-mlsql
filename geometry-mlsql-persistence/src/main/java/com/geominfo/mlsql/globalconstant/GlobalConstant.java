package com.geominfo.mlsql.globalconstant;

import java.math.BigDecimal;

/**
 * @program: geometry-bi
 * @description: 全局常量类
 * @author: lihaoxún
 * @create: 2019-01-02 17:28
 * @version: 3.0.0
 */
public class GlobalConstant {
    /**
     * 系统根
     */
    public static final int XTG = 1;

    /**
     * 业务域
     */
    public static final int YWY = 2;

    /**
     * 组织模型
     */
    public static final int ZZMX = 3;

    /**
     * 人员根
     */
    public static final int RYG = 4;

    /**
     * 岗位根
     */
    public static final int GWG = 5;

    /**
     * 组织机构根
     */
    public static final int ZZJGG = 6;

    /**
     * 人员
     */
    public static final int RY = 7;

    /**
     * 岗位
     */
    public static final int GW = 8;

    /**
     * 机构
     */
    public static final int JG = 9;

    /**
     * 部门
     */
    public static final int BM = 10;

    /**
     * 工作组
     */
    public static final int GZZ = 11;

    /**
     * 人员成员
     */
    public static final int RYCY = 12;

    /**
     * 岗位成员
     */
    public static final int GWCY = 13;

    /**
     * 工作组成员
     */
    public static final int GZZCY = 14;

    /**
     * 应用系统
     */
    public static final int APP = 15;

    /**
     * 文件夹
     */
    public static final int FOLDER = 16;

    /**
     * web窗体
     */
    public static final int WEBFORM = 17;

    /**
     * 资源操作
     */
    public static final int OPT = 18;

    /**
     * 数据权限
     */
    public static final int DOPT = 19;

    /**
     * 主题
     */
    public static final int ZT = 20;

    /**
     * 指标
     */
    public static final int ZB = 21;

    /**
     * 度量
     */
    public static final int DL = 22;

    /**
     * 数据集
     */
    public static final int DS = 23;

    /**
     * 数据源
     */
    public static final int SJY = 24;

    /**
     * 工作空间
     */
    public static final int GZKJ = 25;

    /**
     * 空间成员
     */
    public static final int KJCY = 26;

    /**
     * 探索文件
     */
    public static final int TSWJ = 27;

    /**
     * 图表
     */
    public static final int TB = 28;

    /**
     * 指标看板
     */
    public static final int ZBKB = 29;

    /**
     * 内置函数
     */
    public static final int NZHS = 30;

    /**
     * 数据门户
     */
    public static final int SJMH = 31;

    /**
     * 电子表格
     */
    public static final int DZBG = 32;

    /**
     * 全局参数（指标看板跳转）
     */
    public static final int QJCS = 33;

    /**
     * 自助报表
     */
    public static final int ZZBB = 34;

    /**
     * 监控指标
     */
    public static final int JKZB = 35;
    /**
     * 数据分析模板
     */
    public static final int SJFXMB = 36;

    /**
     * 默认系统标识
     */
    public static final String BIXT_ROOT = "10001";

    /**
     * java数据源时间类型
     */
    public static final String[] DATETIME_ARRY = {"date","time","timestamp"};

    /**
     * java数据源数值类型
     */
    public static final String[] NUMBER_ARRY = {"integer","long","double","float","short","bigdecimal","byte"};

    /**
     * schema对象时间类型
     */
    public static final String[] O_DATETIME_ARRY = {"date", "time", "timestamp"};

    /**
     * schema对象数值类型
     */
    public static final String[] O_NUMBER_ARRY = {"numeric","integer"};

    /**
     * 权限系统标识
     */
    public static final String BI_QXXT = "bi-qxxt";

    /**
     *工作空间权限标识
     */
    public static final String BI_GZKJ = "bi-gzkj";

    //新增接口操作类型
    public static final int INSERT_FLAG = 1;

    //修改接口操作类型
    public static final int UPDATE_FLAG = 2;

    //删除接口操作类型
    public static final int DELETE_FLAG = 3;

    //预览模式查询操作类型
    public static final int PREVIEW_FLAG = 4;

    //接口日志未知类型
    public static final int INTERFACE_LOG_FLAG = 999;

    //查询整个模块操作类型
    public static final int QUERY_FLAG = 5;

    //任务组名称，默认值：监控指标
    public static final String JOB_GROUP_JKZB_NAME = "jkzb";

    //任务组名称，默认值：监控告警
    public static final String JOB_GROUP_JKGJ_NAME = "jkgj";

    //任务组名称，默认值：更新相对时间
    public static final String JOB_GROUP_XDSJ_NAME = "xdsj";

    //图片文件夹

    public static final String UPLOAD_IMG = "./bi_file/img/";
    //生成zip存放位置
    public static final String GENERATE_ZIP = "./bi_file/generate_zip";
    //上传zip存放路径
    public static final String UPLOAD_ZIP = "./bi_file/upload_zip";

    //数据门户菜单ID
    public static final BigDecimal SJMH_MENU_ID = BigDecimal.valueOf(1);

    //指标看板菜单ID
    public static final BigDecimal ZBKB_MENU_ID = BigDecimal.valueOf(2);

    //电子表格菜单ID
    public static final BigDecimal DZBG_MENU_ID = BigDecimal.valueOf(3);

    //图标分析菜单ID
    public static final BigDecimal TBFX_MENU_ID = BigDecimal.valueOf(4);

    //数据集菜单ID
    public static final BigDecimal SJJ_MENU_ID = BigDecimal.valueOf(5);

    //数据源菜单ID
    public static final BigDecimal SJY_MENU_ID = BigDecimal.valueOf(6);

    //自助报表菜单ID
    public static final BigDecimal ZZBB_MENU_ID = BigDecimal.valueOf(7);
}
