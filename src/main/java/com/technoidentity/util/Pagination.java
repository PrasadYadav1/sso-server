package com.technoidentity.util;

import java.util.List;

public class Pagination {
    private final List<?> data;
    private final int count;

    public Pagination(List<?> data, int count){
        this.data = data;
        this.count = count;
    }

    public List<?> getData(){
        return this.data;
    }

    public int getCount(){
        return this.count;
    }
}
