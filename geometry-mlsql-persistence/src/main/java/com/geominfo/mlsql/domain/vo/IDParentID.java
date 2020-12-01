package com.geominfo.mlsql.domain.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: geometry-mlsql
 * @description: IDParentID
 * @author: BJZ
 * @create: 2020-11-23 17:55
 * @version: 1.0.0
 */
@Data
public class IDParentID {
    private Object id;
    private Object parentID;
    private String name;
    private List<IDParentID> children;

    public IDParentID(Object id, Object parentID, String name, List<IDParentID> children) {
        this.id = id;
        this.parentID = parentID;
        this.name = name;
        this.children = children;
    }
}