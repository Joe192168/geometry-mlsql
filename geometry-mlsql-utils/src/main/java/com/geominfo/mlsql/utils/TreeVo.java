package com.geominfo.mlsql.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TreeVo<T> {

    private static final long serialVersionUID = 1L;

    public TreeVo() {
        super();
    }

    public TreeVo(T o, String idKey, String nameKey, String pidKey) {
        super();
        this.o = o;
        this.idKey = idKey;
        this.nameKey = nameKey;
        this.pidKey = pidKey;
    }

    public TreeVo(T o, String idKey, String nameKey, String pidKey, String typeKey) {
        super();
        this.o = o;
        this.idKey = idKey;
        this.nameKey = nameKey;
        this.pidKey = pidKey;
        this.typeKey = typeKey;
    }

    // 基本属性，无需维护根据业务属性自动生成
    private String id;
    private String operatename;
    private String parentid;
    private String type;
    private List<String> routePids;


    // 业务属性
    private T o;
    private String idKey;
    private String nameKey;
    private String pidKey;
    private String typeKey;

    // 子集属性
    private List<TreeVo<?>> children;

    // 其他参数
    private Map<String, Object> map;

    // iview UI cascader 组件所需的参数
    private String tvalue;
    private String label;
    private String title;
    private String key;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOperatename() {
        return operatename;
    }

    public void setOperatename(String operatename) {
        this.operatename = operatename;
    }

    public String getParentid() {
        return parentid;
    }

    public void setParentid(String parentid) {
        this.parentid = parentid;
    }

    public T getO() {
        return o;
    }

    public void setO(T o) {
        this.o = o;
    }

    public String getIdKey() {
        return idKey;
    }

    public void setIdKey(String idKey) {
        this.idKey = idKey;
    }

    public String getNameKey() {
        return nameKey;
    }

    public void setNameKey(String nameKey) {
        this.nameKey = nameKey;
    }

    public String getPidKey() {
        return pidKey;
    }

    public void setPidKey(String pidKey) {
        this.pidKey = pidKey;
    }

    public List<TreeVo<?>> getChildren() {
        return children;
    }

    public void setChildren(List<TreeVo<?>> children) {
        this.children = children;
    }

    public void addChildren(TreeVo<?> children) {
        if (this.children == null) {
            this.children = new ArrayList<TreeVo<?>>();
        }
        this.children.add(children);
    }

    public Map<String, Object> getMap() {
        return map;
    }

    public void setMap(Map<String, Object> map) {
        this.map = map;
    }

    public List<String> getRoutePids() {
        return routePids;
    }

    public void setRoutePids(List<String> routePids) {
        this.routePids = routePids;
    }

    public String getTvalue() {
        return tvalue;
    }

    public void setTvalue(String value) {
        this.tvalue = tvalue;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTypeKey() {
        return typeKey;
    }

    public void setTypeKey(String typeKey) {
        this.typeKey = typeKey;
    }
}
