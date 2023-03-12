package me.msile.app.androidapp.test;

public class AppComBean {

    //网络
    public static final int COM_TYPE_NET = 0;
    //图片加载
    public static final int COM_TYPE_PIC_LOADER = 1;
    //本地数据
    public static final int COM_TYPE_LOCAL_DATA = 2;
    //WebView
    public static final int COM_TYPE_WEB_VIEW = 3;
    //选择器
    public static final int COM_TYPE_PICKER = 4;
    //播放器
    public static final int COM_TYPE_PLAYER = 5;
    //权限申请
    public static final int COM_TYPE_PERMISSION = 6;
    //二维码扫描
    public static final int COM_TYPE_QR_CODE = 7;
    //路由
    public static final int COM_TYPE_ROUTER = 8;

    private int comType;
    private String comName;
    private String comDesc;

    public AppComBean() {
    }

    public AppComBean(int comType, String comName, String comDesc, boolean isCanTest) {
        this.comType = comType;
        this.comName = comName;
        this.comDesc = comDesc;
        this.canTest = isCanTest;
    }

    public int getComType() {
        return comType;
    }

    public void setComType(int comType) {
        this.comType = comType;
    }

    public String getComName() {
        return comName;
    }

    public void setComName(String comName) {
        this.comName = comName;
    }

    public String getComDesc() {
        return comDesc;
    }

    public void setComDesc(String comDesc) {
        this.comDesc = comDesc;
    }

    private boolean isSpreadOut;

    public boolean isSpreadOut() {
        return isSpreadOut;
    }

    public void setSpreadOut(boolean spreadOut) {
        isSpreadOut = spreadOut;
    }

    private boolean canTest;

    public boolean isCanTest() {
        return canTest;
    }

    public void setCanTest(boolean canTest) {
        this.canTest = canTest;
    }
}
