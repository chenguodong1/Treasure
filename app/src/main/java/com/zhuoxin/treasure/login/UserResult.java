package com.zhuoxin.treasure.login;

/**
 * Created by MACHENIKE on 2017/8/2.
 */

public class UserResult {

    /**
     * errcode : 1
     * errmsg : 登录成功！
     * headpic : add.jpg
     * tokenid : 171
     */

    private int errcode;
    private String errmsg;
    private String headpic;
    private int tokenid;

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

    public String getHeadpic() {
        return headpic;
    }

    public void setHeadpic(String headpic) {
        this.headpic = headpic;
    }

    public int getTokenid() {
        return tokenid;
    }

    public void setTokenid(int tokenid) {
        this.tokenid = tokenid;
    }
}
