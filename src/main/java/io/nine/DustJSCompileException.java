package io.nine;

/**
 * @author chanwook
 */
public class DustJSCompileException extends RuntimeException {
    public DustJSCompileException(String msg) {
        super(msg);
    }

    public DustJSCompileException(String msg, Throwable ex) {
        super(msg, ex);
    }
}
