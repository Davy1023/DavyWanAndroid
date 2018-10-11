package com.davy.davy_wanandroid.bean;

/**
 * author: Davy
 * date: 18/10/11
 */
public class BaseGankResponse<T> {

    public static final boolean ERROR = false;

    private boolean error;
    private T results;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public T getResults() {
        return results;
    }

    public void setResults(T results) {
        this.results = results;
    }

}
