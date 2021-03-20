package io.kimmking.rpcfx.demo.provider;

import io.kimmking.rpcfx.api.IRpcfxResolver;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class DemoResolver implements IRpcfxResolver, ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    // 投机取巧，用了 spring 的 applicationContext 来读取 bean
    @Override
    public Object resolve(Class<?> serviceClass) {
        return this.applicationContext.getBean(serviceClass);
    }
}
