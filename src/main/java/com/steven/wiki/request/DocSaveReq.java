package com.steven.wiki.request;

import jakarta.validation.constraints.NotNull;

public class DocSaveReq {
    private Long id;

    @NotNull(message = "[ebook id] cannot be null")
    private Long ebookId;

    @NotNull(message = "[parent doc] cannot be null")
    private Long parent;

    @NotNull(message = "[name] cannot be null")
    private String name;

    @NotNull(message = "[sort] cannot be null")
    private Integer sort;

    private Integer viewCount;

    private Integer likeCount;

    @NotNull(message = "[content] cannot be null")
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEbookId() {
        return ebookId;
    }

    public void setEbookId(Long ebookId) {
        this.ebookId = ebookId;
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

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

    public Integer getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

    @Override
    public String toString() {
        return "DocSaveReq{" +
               "id=" + id +
               ", ebookId=" + ebookId +
               ", parent=" + parent +
               ", name='" + name + '\'' +
               ", sort=" + sort +
               ", viewCount=" + viewCount +
               ", likeCount=" + likeCount +
               ", content='" + content + '\'' +
               '}';
    }
}