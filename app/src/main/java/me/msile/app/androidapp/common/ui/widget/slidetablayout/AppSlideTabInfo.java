package me.msile.app.androidapp.common.ui.widget.slidetablayout;

public class AppSlideTabInfo {
    private Object extraData;
    private String tabName;
    private boolean isSelect;

    public AppSlideTabInfo(String tabName) {
        this.tabName = tabName;
        this.extraData = tabName;
    }

    public AppSlideTabInfo(Object extraData, String tabName) {
        this.extraData = extraData;
        this.tabName = tabName;
    }

    public Object getExtraData() {
        return extraData;
    }

    public void setExtraData(Object extraData) {
        this.extraData = extraData;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getTabName() {
        return tabName;
    }

    public void setTabName(String tabName) {
        this.tabName = tabName;
    }
}
