package com.howshea.gankio.entity;

import java.util.List;

/**
 * Created by haipo on 2016/10/24.
 */

public class History {


    private boolean error;
    private List<String> results;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public List<String> getResults() {
        return results;
    }

    public void setResults(List<String> results) {
        this.results = results;
    }
}
