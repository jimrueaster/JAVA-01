package com.xjr.filter.filter.filter.impl;

import com.xjr.filter.filter.filter.HttpRequestFilter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import org.springframework.util.CollectionUtils;

import java.util.List;

public class HttpRequestFilters implements HttpRequestFilter {

    private final List<HttpRequestFilter> filterList;

    public HttpRequestFilters(List<HttpRequestFilter> filterList) {
        this.filterList = filterList;
    }

    @Override
    public void filter(FullHttpRequest fullRequest, ChannelHandlerContext ctx) {
        if (CollectionUtils.isEmpty(filterList)) {
            return;
        }
        filterList.forEach(httpRequestFilter -> httpRequestFilter.filter(fullRequest, ctx));
    }
}
