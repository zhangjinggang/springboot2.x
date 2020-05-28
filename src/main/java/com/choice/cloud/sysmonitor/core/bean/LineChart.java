package com.choice.cloud.sysmonitor.core.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Lists;
import lombok.Data;
import lombok.Getter;

import java.util.List;

/**
 * 折线图，优化版（尚未使用验证）
 * 需要三个泛型：T legend的data类型，也是series的name类型； X: x轴的数据类型；S series的数据类型
 * 由于后台只提供数据，这里的字段比较少，特别是控制样式相关的设置，都是由前台写死在页面上的。
 * 
 * @author zhaojufei
 */
@Data
@Getter
public class LineChart<T, X, S> {
    private Title title;

    private Legend<T> legend;

    @JsonProperty("xAxis")
    private XAxis<X> xAxis;

    @JsonProperty("yAxis")
    private YAxis yAxis;

    private List<Series<T, S>> series;

    private LineChart() {
    }

    /**
     * 初始化一个空的对象，将构建过程对程序透明，程序只需要调用下面add接口添加数据即可。
     */
    public static LineChart build() {
        LineChart lineChart = new LineChart();

        LineChart.Title title = new Title();
        lineChart.setTitle(title);

        Legend legend = new Legend();
        List legendData = Lists.newArrayList();
        legend.setData(legendData);

        XAxis xAxis = new XAxis();
        List xAxisData = Lists.newArrayList();
        xAxis.setData(xAxisData);
        lineChart.setXAxis(xAxis);

        List<Series> series = Lists.newArrayList();
        lineChart.setSeries(series);

        return lineChart;
    }

    /**
     * 设置标题
     * 
     * @param text
     */
    public void setTitleText(String text) {
        this.getTitle().setText(text);
    }

    /**
     * 设置
     * 
     * @param t
     */
    public void addLegendData(T t) {
        this.getLegend().getData().add(t);
    }

    /**
     * 
     * @param t
     * @param data
     */
    public void addSeriesData(T t, List<S> data) {
        Series series = new Series();
        series.setName(t);
        series.setData(data);
        this.getSeries().add(series);
    }

    @Data
    private static class Title {
        private String text;
    }

    @Data
    private static class Legend<T> {
        private List<T> data;
    }

    @Data
    private static class XAxis<X> {
        private String type;
        private String name;
        private List<X> data;

    }

    @Data
    private static class YAxis {
        private String type;
        private String name;
    }

    @Data
    private static class Series<T, S> {
        private String type;
        private T name;
        private List<S> data;
    }
}
