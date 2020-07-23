package com.geominfo.mlsql.util;

import com.geominfo.mlsql.domain.vo.TreeVo;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @program: geometry-bi
 * @description: 根据不规则的树实现转换成树结构工具
 * @author: 肖乔辉
 * @create: 2018-08-17 15:39
 * @version: 1.0.0
 */
public class TreeDataProcessor {
    private static TreeDataProcessor instance = new TreeDataProcessor();

    private TreeDataProcessor() {
    }

    public static TreeDataProcessor getInstance() {
        return instance;
    }

    public List<String> getRoutePids(String id, String pid, Map<String, List<String>> routeMap) {
        List<String> routePids = new ArrayList<String>();

        if (StringUtils.isNotBlank(pid)) {
            List<String> pidRoutePids = routeMap.get(pid);
            if (pidRoutePids != null && pidRoutePids.size() > 0) {
                routePids.addAll(pidRoutePids);
            }
            routePids.add(pid);
        }

        routeMap.put(id, routePids);
        return routePids;
    }

    /**
     * @Description (根据业务属性 重新计算 基本属性)
     * @param vo
     * @return
     */
    public <T> TreeVo<T> flashIdNamePid(TreeVo<T> vo) {
        if (vo.getO() == null) {
            return vo;
        }
        Class<?> cl = vo.getO().getClass();
        Class<?> type;
        Method meth;
        String name;
        String methodName;
        String value;
        Object obj;
        try {
            if (StringUtils.isNotBlank(vo.getIdKey()) || StringUtils.isNotBlank(vo.getNameKey())|| StringUtils.isNotBlank(vo.getPidKey())) {
                Field[] fields = cl.getDeclaredFields();
                if (fields != null && fields.length > 0) {
                    for (Field f : fields) {
                        name = f.getName();
                        if (name.equalsIgnoreCase(vo.getIdKey()) || name.equalsIgnoreCase(vo.getNameKey())|| name.equalsIgnoreCase(vo.getPidKey())) {
                            type = f.getType();
                            methodName = null;
                            if ("boolean".equals(type.getName())) {
                                methodName = "is" + name.substring(0, 1).toUpperCase()+ name.substring(1, name.length());
                            } else {
                                methodName = "get" + name.substring(0, 1).toUpperCase()+ name.substring(1, name.length());
                            }
                            value = "";
                            meth = null;
                            try {
                                meth = cl.getMethod(methodName);
                            } catch (NoSuchMethodException e) {
                                // 方法不存在
                                continue;
                            }
                            obj = meth.invoke(vo.getO());
                            value = obj == null ? null : obj.toString();
                            // System.out.println(name+":::"+value);
                            if (name.equalsIgnoreCase(vo.getIdKey())) {
                                vo.setId(value);
                                vo.setKey(value);
                            } else if (name.equalsIgnoreCase(vo.getNameKey())) {
                                vo.setOperatename(value);
                                vo.setLabel(value);
                                vo.setTitle(value);
                            } else if (name.equalsIgnoreCase(vo.getPidKey())) {
                                vo.setParentid(value);
                            }

                        }
                    }
                }
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return vo;
    }

    public <T> List<TreeVo<T>> getTreeVoList(List<T> list, String idKey, String nameKey, String pidKey) {
        List<TreeVo<T>> rList = null;
        Map<String, TreeVo<T>> tempMap = null;
        List<TreeVo<T>> tempList = null;
        Set<String> ids = new HashSet<String>();
        TreeVo<T> vo = null;
        Map<String, List<String>> routeMap = new HashMap<String, List<String>>();
        if (list != null && list.size() > 0) {
            for (T t : list) {
                if (tempMap == null){
                    tempMap = new LinkedHashMap<String, TreeVo<T>>();
                }
                if (tempList == null){
                    tempList = new ArrayList<TreeVo<T>>();
                }
                vo = flashIdNamePid(new TreeVo<T>(t, idKey, nameKey, pidKey));
                vo.setRoutePids(TreeDataProcessor.getInstance().getRoutePids(vo.getId(), vo.getParentid(), routeMap));
                tempMap.put(vo.getId(), vo);
                tempList.add(vo);
                ids.add(vo.getId());
            }
            TreeVo<T> temp;
            List<TreeVo<?>> children;
            for (int i = 0; i < tempList.size(); i++) {
                if (tempMap.get(tempList.get(i).getParentid()) != null && !tempList.get(i).getId().equals(tempList.get(i).getParentid())) {
                    temp = tempMap.get(tempList.get(i).getParentid());
                    children = temp.getChildren();
                    if (children == null){
                        children = new ArrayList<TreeVo<?>>();
                    }
                    children.add(tempList.get(i));
                    temp.setChildren(children);
                    tempMap.put(tempList.get(i).getParentid(), temp);
                } else {
                    if (rList == null){
                        rList = new ArrayList<TreeVo<T>>();
                    }
                    rList.add(tempList.get(i));
                }
            }
        }
        return rList;
    }

    // 扁平树结构至立体树结构 转化
    public List<TreeVo<?>> getTreeVoList(List<TreeVo<?>> list) {
        List<TreeVo<?>> rList = null;
        Map<String, TreeVo<?>> tempMap = null;
        List<TreeVo<?>> tempList = null;
        Set<String> ids = new HashSet<String>();
        TreeVo<?> vo = null;
        if (list != null && list.size() > 0) {
            for (TreeVo<?> t : list) {
                if (tempMap == null){
                    tempMap = new LinkedHashMap<String, TreeVo<?>>();
                }
                if (tempList == null){
                    tempList = new ArrayList<TreeVo<?>>();
                }
                vo = t;
                tempMap.put(vo.getId(), vo);
                tempList.add(vo);
                ids.add(vo.getId());
            }
            TreeVo<?> temp;
            List<TreeVo<?>> children;
            for (int i = 0; i < tempList.size(); i++) {
                if (tempMap.get(tempList.get(i).getParentid()) != null && !tempList.get(i).getId().equals(tempList.get(i).getParentid())) {
                    temp = tempMap.get(tempList.get(i).getParentid());
                    children = temp.getChildren();
                    if (children == null){
                        children = new ArrayList<TreeVo<?>>();
                    }
                    children.add(tempList.get(i));
                    temp.setChildren(children);
                    tempMap.put(tempList.get(i).getParentid(), temp);
                } else {
                    if (rList == null){
                        rList = new ArrayList<TreeVo<?>>();
                    }
                    rList.add(tempList.get(i));
                }
            }
        }
        return rList;
    }
}
