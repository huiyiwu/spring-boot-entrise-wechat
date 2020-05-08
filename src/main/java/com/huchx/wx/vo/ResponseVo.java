package com.huchx.wx.vo;

import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lijinlei on 2016/10/18.
 */
public class ResponseVo<T> {
    public static final String RESULT_STATUS_CODE = "code";
    public static final String RESULT_STATUS_MESSAGE = "message";
    private static final int RESULT_STATUS_CODE_VALUE_NOT_LOGIN = -999;

    private Map<String, Object> resultStatus;
    private T resultValue;

    public ResponseVo() {
    }

    public ResponseVo(T t) {
        this.resultValue = t;
    }

    public Map<String, Object> getResultStatus() {
        return resultStatus;
    }

    public void setResultStatus(Map<String, Object> resultStatus) {
        this.resultStatus = resultStatus;
    }

    public T getResultValue() {
        return resultValue;
    }

    public void setResultValue(T resultValue) {
        this.resultValue = resultValue;
    }

    public ResponseVo<T> ok(T resultValue) {
        return ok(resultValue, null);
    }

    public ResponseVo<T> ok(T resultValue, String message) {
        Map<String, Object> resultStatus = new HashMap<>();
        resultStatus.put(ResponseVo.RESULT_STATUS_CODE, 0);
        resultStatus.put(ResponseVo.RESULT_STATUS_MESSAGE, StringUtils.isEmpty(message) ? "操作成功。" : message);
        this.setResultStatus(resultStatus);
        this.resultValue = resultValue;

        return this;
    }


    public ResponseVo<T> error(T resultValue) {
        return error(resultValue, null);
    }

    public ResponseVo<T> error(T resultValue, String msg) {
        Map<String, Object> resultStatus = new HashMap<>();
        resultStatus.put(ResponseVo.RESULT_STATUS_CODE, 500);
        resultStatus.put(ResponseVo.RESULT_STATUS_MESSAGE, StringUtils.isEmpty(msg) ? "操作失败。" : msg);
        this.setResultStatus(resultStatus);
        this.resultValue = resultValue;

        return this;
    }

    public ResponseVo<T> error(T resultValue, String msg,Integer errCode) {
        Map<String, Object> resultStatus = new HashMap<>();
        resultStatus.put(ResponseVo.RESULT_STATUS_CODE, errCode);
        resultStatus.put(ResponseVo.RESULT_STATUS_MESSAGE, StringUtils.isEmpty(msg) ? "操作失败。" : msg);
        this.setResultStatus(resultStatus);
        this.resultValue = resultValue;

        return this;
    }
}
