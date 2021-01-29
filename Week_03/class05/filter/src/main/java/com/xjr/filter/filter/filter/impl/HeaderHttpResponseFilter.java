package com.xjr.filter.filter.filter.impl;

import com.xjr.filter.filter.filter.HttpResponseFilter;
import io.netty.handler.codec.http.FullHttpResponse;

public class HeaderHttpResponseFilter implements HttpResponseFilter {
    @Override
    public void filter(FullHttpResponse response) {
        response.headers().set("kk", "java-1-nio");
    }
}
