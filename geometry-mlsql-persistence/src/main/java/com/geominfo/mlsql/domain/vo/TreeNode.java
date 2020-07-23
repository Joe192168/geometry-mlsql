package com.geominfo.mlsql.domain.vo;


import java.util.ArrayList;
import java.util.List;

/**
 * @program: geometry-bi
 * @description: tree 抽象节点 树可继承这个节点
 * @author: 肖乔辉
 * @create: 2018-08-17 15:39
 * @version: 1.0.0
 */
public abstract class TreeNode {

    protected Integer id;
    protected Integer parentId;
    protected List<TreeNode> children = new ArrayList<>();

    public void addChilren(TreeNode node) {
        this.children.add(node);
    }

    public List<TreeNode> getChildren() {
        return children;
    }

    public void setChildren(List<TreeNode> children) {
        this.children = children;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }
}
