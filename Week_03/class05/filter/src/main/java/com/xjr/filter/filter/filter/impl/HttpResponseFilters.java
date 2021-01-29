package com.xjr.filter.filter.filter.impl;

import com.xjr.filter.filter.filter.HttpResponseFilter;
import io.netty.handler.codec.http.FullHttpResponse;
import org.springframework.util.CollectionUtils;

import java.util.List;

public class HttpResponseFilters implements HttpResponseFilter {
    private final List<HttpResponseFilter> filterList;

    public HttpResponseFilters(List<HttpResponseFilter> filterList) {
        this.filterList = filterList;
    }

    @Override
    public void filter(FullHttpResponse response) {
        if (CollectionUtils.isEmpty(filterList)) {
            return;
        }
        filterList.forEach(httpResponseFilter -> httpResponseFilter.filter(response));
    }
}
