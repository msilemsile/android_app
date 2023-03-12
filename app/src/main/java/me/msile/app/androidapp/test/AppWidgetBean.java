package me.msile.app.androidapp.test;

public class AppWidgetBean {

    //拖拽布局
    public static final int WIDGET_TYPE_DRAG_LAY = 0;
    //底部划线布局
    public static final int WIDGET_TYPE_UNDERLINE_LAY = 1;
    //循环ViewPager
    public static final int WIDGET_TYPE_LOOP_VIEWPAGER = 2;
    //上下滚动播报控件
    public static final int WIDGET_TYPE_NOTIFY_VIEW = 3;
    //viewpager指示器
    public static final int WIDGET_TYPE_PAGER_INDICATOR = 4;
    //比例布局
    public static final int WIDGET_TYPE_RATIO_LAY = 5;
    //阴影布局
    public static final int WIDGET_TYPE_SHADOW_LAY = 6;
    //自定义背景图形的布局
    public static final int WIDGET_TYPE_SHAPE_LAY = 7;
    //viewpager标签选择控件
    public static final int WIDGET_TYPE_SLIDE_TAB_LAY = 8;

    private int widgetType;
    private String widgetName;
    private String widgetDesc;

    public AppWidgetBean() {
    }

    public AppWidgetBean(int widgetType, String widgetName, String widgetDesc) {
        this.widgetType = widgetType;
        this.widgetName = widgetName;
        this.widgetDesc = widgetDesc;
    }

    public int getWidgetType() {
        return widgetType;
    }

    public void setWidgetType(int widgetType) {
        this.widgetType = widgetType;
    }

    public String getWidgetName() {
        return widgetName;
    }

    public void setWidgetName(String widgetName) {
        this.widgetName = widgetName;
    }

    public String getWidgetDesc() {
        return widgetDesc;
    }

    public void setWidgetDesc(String widgetDesc) {
        this.widgetDesc = widgetDesc;
    }

    private boolean isSpreadOut;

    public boolean isSpreadOut() {
        return isSpreadOut;
    }

    public void setSpreadOut(boolean spreadOut) {
        isSpreadOut = spreadOut;
    }
}
