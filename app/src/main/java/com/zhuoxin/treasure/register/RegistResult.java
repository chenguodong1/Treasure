package com.zhuoxin.treasure.register;

/**
 * Created by Administrator on 2017/8/2.
 */

public class RegistResult {

    /**
     * errcode : 1
     * errmsg : 登录成功！
     * tokenid : 171
     */

    private int errcode;
    private String errmsg;
    private int tokenid;

    public RegistResult(int errcode, String errmsg, int tokenid) {
        this.errcode = errcode;
        this.errmsg = errmsg;
        this.tokenid = tokenid;
    }

    public int getErrcode() {
        return errcode;
    }

    public void setErrcode(int errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public int getTokenid() {
        return tokenid;
    }

    public void setTokenid(int tokenid) {
        this.tokenid = tokenid;
    }
}
