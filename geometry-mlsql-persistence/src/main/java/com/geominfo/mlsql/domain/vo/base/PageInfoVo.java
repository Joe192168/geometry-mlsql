package com.geominfo.mlsql.domain.vo.base;

/**
 * @program: geometry-bi
 * @description: 分页查询实体
 * @author: LF
 * @create: 2021/5/21 11:13
 * @version: 1.0.0
 */
public class PageInfoVo {
    /*分页时页数*/
    protected Integer currentPage;

    /*每页的大小*/
    protected Integer pageSize;

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getCurrentPage() {
        //页数默认第一页
        if (currentPage == null || currentPage == 0)
            return 1;
        else
            return currentPage;
    }

    public Integer getPageSize() {
        //默认一页十条
        if (pageSize == null || pageSize == 0)
            return 10;
        else
            return pageSize;
    }
}
