package com.steven.wiki.request;

/*
Use for transfer parameters to request; 用于向request传递参数
 */
public class DocQueryReq extends PageReq {
    private Long id;

    private Long parent;

    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getParent() {
        return parent;
    }

    public void setParent(Long parent) {
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "DocQueryReq{" +
               "id=" + id +
               ", parent=" + parent +
               ", name='" + name + '\'' +
               "} " + super.toString();
    }
}