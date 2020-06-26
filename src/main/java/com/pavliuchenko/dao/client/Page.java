package com.pavliuchenko.dao.client;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Page<T> {
    private int pageNumber;
    private int pageSize;
    private int totalPages;
    private List<T> items;

    public Page(int pageNumber, int pageSize) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
    }
}
