package com.geominfo.mlsql.utils;

import com.geominfo.mlsql.commons.GlobalConstant;
import com.geominfo.mlsql.commons.SystemCustomIdentification;
import com.geominfo.mlsql.enums.ResourceTypeAppEnum;

import java.util.HashSet;

/**
 * @program: geometry-bi
 * @description: ResourcesTransformUtil
 * @author: LF
 * @create: 2019-05-08 10:58
 * @version: 1.0.0
 */
public class ResourcesTransformUtil {
    /**
     * @param
     * @return
     * @description: 根据菜单ID获取资源类型(Integer)
     * @author: LF
     * @date: 2019-05-08
     */
    public static Integer getResourcesType(String menuId) {
        Integer resourcesType = null;
        if (SystemCustomIdentification.SJMH.equals(menuId)) {
            //数据门户
            resourcesType = GlobalConstant.SJMH;
        } else if (SystemCustomIdentification.ZZKB.equals(menuId)) {
            //指标看板
            resourcesType = GlobalConstant.ZBKB;
        } else if (SystemCustomIdentification.DZBG.equals(menuId)) {
            //电子表格
            resourcesType = GlobalConstant.DZBG;

        } else if (SystemCustomIdentification.TB.equals(menuId)) {
            //图表分析
            resourcesType = GlobalConstant.TB;

        } else if (SystemCustomIdentification.DS.equals(menuId)) {
            //数据集
            resourcesType = GlobalConstant.DS;
        }else if (SystemCustomIdentification.SJY.equals(menuId)) {
            //数据源
            resourcesType = GlobalConstant.SJY;
        }else if (SystemCustomIdentification.ZZBB.equals(menuId)) {
            //自助报表
            resourcesType = GlobalConstant.ZZBB;
        }else {
            //默认文件夹
            resourcesType = GlobalConstant.FOLDER;
        }
        return resourcesType;
    }
    /**
      * @description: 根据资源类型查找关联资源类型
      * @author: LF
      * @date: 2019-05-24
      * @param
      * @return
     */
    public static HashSet getRelationResourcesType(String resourcesType){
        HashSet relationResourcesTypes = new HashSet();
        //数据源
        if (String.valueOf(GlobalConstant.SJY).equals(resourcesType)){
            relationResourcesTypes.add(GlobalConstant.DS);
        }
        //数据集
        if (String.valueOf(GlobalConstant.DS).equals(resourcesType)){
            relationResourcesTypes.add(GlobalConstant.DZBG);
            relationResourcesTypes.add(GlobalConstant.TB);
        }
        //图表分析
        if (String.valueOf(GlobalConstant.TB).equals(resourcesType)){
            relationResourcesTypes.add(GlobalConstant.ZBKB);
        }
        //指标看板、电子表格
        if (String.valueOf(GlobalConstant.ZBKB).equals(resourcesType)||String.valueOf(GlobalConstant.DZBG).equals(resourcesType)){
           relationResourcesTypes.add(GlobalConstant.SJMH);
        }
        return relationResourcesTypes;
    }

    /*
    *@Description 根据应用枚举值转化为应用所拥有的资源类型集合
    *@Author LF
    *@Date  2019/9/9
    *@Param [resourceTypeAppEnum, resourcesTypes]
    *@return java.util.HashSet
    **/
    public static HashSet getResourcesType(ResourceTypeAppEnum resourceTypeAppEnum, HashSet resourcesTypes){
        if (resourceTypeAppEnum.equals(ResourceTypeAppEnum.ART)){
            resourcesTypes.add(GlobalConstant.APP);
            resourcesTypes.add(GlobalConstant.FOLDER);
            resourcesTypes.add(GlobalConstant.WEBFORM);
            resourcesTypes.add(GlobalConstant.OPT);
            resourcesTypes.add(GlobalConstant.DOPT);
        }else if (resourceTypeAppEnum.equals(ResourceTypeAppEnum.ORT)){
            resourcesTypes.add(GlobalConstant.JG);
            resourcesTypes.add(GlobalConstant.BM);
            resourcesTypes.add(GlobalConstant.GZZ);
        }else {
            return resourcesTypes;
        }
        return resourcesTypes;
    }
}

