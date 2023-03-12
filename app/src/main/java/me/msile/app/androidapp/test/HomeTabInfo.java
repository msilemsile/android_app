package me.msile.app.androidapp.test;

/**
 * 首页底部Tab信息
 */
public class HomeTabInfo<T> {

    private int selectRes;
    private int unSelectRes;
    private boolean needTips;
    private boolean isSelect;
    private boolean isCenterHave;
    private T extraInfo;

    public HomeTabInfo() {
    }

    public HomeTabInfo(int selectRes, int unSelectRes) {
        this.selectRes = selectRes;
        this.unSelectRes = unSelectRes;
    }

    public boolean isCenterHave() {
        return isCenterHave;
    }

    public void setCenterHave(boolean centerHave) {
        isCenterHave = centerHave;
    }

    public int getSelectRes() {
        return selectRes;
    }

    public void setSelectRes(int selectRes) {
        this.selectRes = selectRes;
    }

    public int getUnSelectRes() {
        return unSelectRes;
    }

    public void setUnSelectRes(int unSelectRes) {
        this.unSelectRes = unSelectRes;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public T getExtraInfo() {
        return extraInfo;
    }

    public void setExtraInfo(T extraInfo) {
        this.extraInfo = extraInfo;
    }

    public boolean isNeedTips() {
        return needTips;
    }

    public void setNeedTips(boolean needTips) {
        this.needTips = needTips;
    }
}
