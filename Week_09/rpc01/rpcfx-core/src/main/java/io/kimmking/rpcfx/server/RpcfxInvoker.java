package io.kimmking.rpcfx.server;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.base.Throwables;
import io.kimmking.rpcfx.api.RpcfxRequest;
import io.kimmking.rpcfx.api.IRpcfxResolver;
import io.kimmking.rpcfx.api.RpcfxResponse;
import io.kimmking.rpcfx.exception.RpcfxException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class RpcfxInvoker {

    private IRpcfxResolver resolver;

    public RpcfxInvoker(IRpcfxResolver resolver){
        this.resolver = resolver;
    }

    public RpcfxResponse invoke(RpcfxRequest request) {
        // route: 2
        RpcfxResponse response = new RpcfxResponse();
        String serviceClass = request.getServiceClass();

//        Object service = resolver.resolve(serviceClass);//this.applicationContext.getBean(serviceClass);

        try {
            // 作业1：改成泛型和反射
            Class<?> klass = Class.forName(serviceClass);

            Object service = resolver.resolve(klass);//this.applicationContext.getBean(serviceClass);

            // 再从 request 提取它的 method
            Method method = resolveMethodFromClass(service.getClass(), request.getMethod());
            // 再调用 method
            Object result = method.invoke(service, request.getParams()); // dubbo, fastjson,
            // 两次json序列化能否合并成一个
            response.setResult(JSON.toJSONString(result, SerializerFeature.WriteClassName));
            response.setStatus(true);
            return response;
        } catch (IllegalAccessException | InvocationTargetException | ClassNotFoundException e) {

            // 3.Xstream

            // 2.封装一个统一的RpcfxException
            // 客户端也需要判断异常
            String exceptionStacktrace = Throwables.getStackTraceAsString(e);
            RpcfxException rpcfxException = new RpcfxException(e.getClass(), exceptionStacktrace);
            response.setException(rpcfxException);
            response.setStatus(false);
            return response;
        }
    }

    private Method resolveMethodFromClass(Class<?> klass, String methodName) {
        return Arrays.stream(klass.getMethods()).filter(m -> methodName.equals(m.getName())).findFirst().get();
    }

}
