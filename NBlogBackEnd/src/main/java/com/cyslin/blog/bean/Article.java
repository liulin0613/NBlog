package com.cyslin.blog.bean;

import lombok.Data;

@Data
public class Article {
    private int aId;
    private String aTitle;
    private String aTags;
    private String aContent;
    private String aTime;
    private String aScope;
    private String aAuthor;
    private String aState;
}
