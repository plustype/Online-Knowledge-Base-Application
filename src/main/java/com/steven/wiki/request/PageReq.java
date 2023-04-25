package com.steven.wiki.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;

/*
Use for transfer parameters to request; 用于向request传递参数
 */
public class PageReq {
    @NotNull(message = "page# cannot be null")
    private int page;  //start page number

    @NotNull(message = "records per page cannot be null")
    @Max(value = 1000, message = "records per page cannot over 1000")
    private int size; //number of items per page

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("PageReq{");
        sb.append("page=").append(page);
        sb.append(", size=").append(size);
        sb.append('}');
        return sb.toString();
    }
}