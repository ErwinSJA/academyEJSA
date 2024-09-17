package com.ejsa.academy.utils;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SimplePageResponse<T> {
    private List<T> content;

    public List<T> getContent() { return content; }
    public void setContent(List<T> content) { this.content = content; }
}