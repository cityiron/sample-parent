package com.dbb.hyjal.boot.loader;

/**
 * @author tc
 * @date 2019-10-10
 */
@FunctionalInterface
public interface ConsumerException<R, E extends Exception> {

    void accept(R r) throws E;

}
