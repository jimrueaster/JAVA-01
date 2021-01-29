package com.xjr.filter.filter.filter.impl;

import com.xjr.filter.filter.filter.HttpResponseFilter;
import io.netty.handler.codec.http.FullHttpResponse;

import java.util.Random;

public class AddRandomNumResponseFilter implements HttpResponseFilter {
    @Override
    public void filter(FullHttpResponse response) {
        response.headers().set("rand-int", (new Random()).nextInt());
    }
}
