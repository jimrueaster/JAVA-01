package io.kimmking.rpcfx.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RpcfxException {

    Class<?> exceptionType;
    String stackTrace;

}
