package com.steven.wiki.request;

/*
Use for transfer parameters to request; 用于向request传递参数
 */
public class EbookQueryReq extends PageReq {
    private Long id;

    private long category2;

    private String name;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getCategory2() {
        return category2;
    }

    public void setCategory2(long category2) {
        this.category2 = category2;
    }

    @Override
    public String toString() {
        return "EbookQueryReq{" +
               "id=" + id +
               ", category2=" + category2 +
               ", name='" + name + '\'' +
               "} " + super.toString();
    }
}