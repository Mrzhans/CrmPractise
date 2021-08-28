package com.bjpowernode.workbench.domain;

import java.util.List;

public class PaginationVO<T> {
    private List<T> pageList;
    private Integer total;

    public PaginationVO() {
    }

    public List<T> getPageList() {
        return pageList;
    }

    public void setPageList(List<T> pageList) {
        this.pageList = pageList;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }
}
