package com.choice.cloud.sysmonitor.core.bean;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.List;

/**
 * 饼状图，需要两个泛型：
 * T legend的数据类型，也是series的name类型； S series的数据类型
 * 由于后台只提供数据，这里的字段比较少，特别是控制样式相关的设置，都是由前台写死在页面上的。
 * 
 * @author zhaojufei
 */
@Data
@Getter
public class PieChart<T, S> {
    private Title title;
    private Legend<T> legend;
    private Series<T, S> series;

    private PieChart() {
    }

    /**
     * 初始化一个空的对象，将构建过程对程序透明，后台程序只需要调用下面add接口添加数据即可。
     */
    public static PieChart build() {
        PieChart pieChart = new PieChart();

        Title title = new Title();
        pieChart.setTitle(title);

        Legend legend = new Legend();
        List<String> legendData = Lists.newArrayList();
        legend.setData(legendData);
        pieChart.setLegend(legend);

        Series series = new Series();
        List<SeriesData> seriesData = Lists.newArrayList();
        series.setData(seriesData);
        pieChart.setSeries(series);

        return pieChart;
    }

    /**
     * 给应用设置标题
     * 
     * @param text
     * @param subText
     */
    public void setTitle(String text, String subText) {
        this.getTitle().setText(text);
        this.getTitle().setSubtext(subText);
    }

    /**
     * 添加
     * 
     * @param data
     */
    public void addLegendData(T data) {
        this.getLegend().getData().add(data);
    }

    /**
     * 添加
     */
    public void addSeriesData(T t, S s) {
        SeriesData<T, S> seriesData = new SeriesData(t, s);
        this.getSeries().getData().add(seriesData);
    }

    @Data
    private static class Title {
        private String text;
        private String subtext;
    }

    @Data
    private static class Legend<T> {
        private List<T> data;
    }

    @Data
    private static class Series<T, S> {
        private String name;
        private List<SeriesData<T, S>> data;
    }

    @Data
    @AllArgsConstructor
    private static class SeriesData<T, S> {
        private T name;
        private S value;
    }
}
