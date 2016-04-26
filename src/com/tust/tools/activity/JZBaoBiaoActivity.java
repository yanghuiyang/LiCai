package com.tust.tools.activity;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.achartengine.ChartFactory;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.tust.tools.R;
import com.tust.tools.bean.JZshouru;
import com.tust.tools.bean.JZzhichu;
import com.tust.tools.db.ExpenditureTypeData;
import com.tust.tools.db.IncomeTypeData;
import com.tust.tools.db.JZData;
import com.tust.tools.service.GetTime;

public class JZBaoBiaoActivity extends Activity {
	private JZData dataHelper;
	public static final int ZCSR=1111;
	public static final int zcMingXi=2222;
	public static final int srMingXi=3333;
	private  String userName;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		dataHelper = new JZData(this);
		setContentView(setView(ZCSR));
    }
    
    /*
     * 设置走线图
     * */
    public View setView(int flag){
    	String[] titles=null;
    	int[] colors =null;
    	PointStyle[] styles= null;
    	List<double[]> values = null;
    	String title = "";
		//获取当前登陆用户
		SharedPreferences preferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
		userName = preferences.getString("userName", "");

		//expenditureTypeData = new ExpenditureTypeData(this);
    	switch (flag) {
		case ZCSR:
			 titles = new String[] { "月收入", "月支出"};
			 colors = new int[] { Color.RED, Color.GREEN};
			 styles = new PointStyle[] { PointStyle.CIRCLE, PointStyle.DIAMOND};
			 values = new ArrayList<double[]>();
			 values.add(getShouRuYear(""));
	         values.add(getZhiChuYear(""));
	         title = "月收入和支出走势图";
			 break;
		case zcMingXi:
			List<String> typenames= new ExpenditureTypeData(this).getTypesByUserName(userName);//获取该用户所有支出类型
			int typeSize = typenames.size();
			titles = new String[typeSize];
			colors = new int[typeSize];
			values = new ArrayList<double[]>();
			styles = new PointStyle[typeSize];
			//动态获取该用户现有的支出类型
			for(int i=0;i<typeSize;i++){
				titles[i] = typenames.get(i).toString();
				Random random = new Random();
				colors[i] = Color.rgb(random.nextInt(256),random.nextInt(256), random.nextInt(256));
				styles[i] = PointStyle.DIAMOND;
				String selection = " and "+JZzhichu.ZC_ITEM+" = '"+typenames.get(i)+"'";
				values.add(getZhiChuYear(selection));
				selection = "";
			}

	         title = "支出走势图";
			break;
		case srMingXi:
			List<String> typenames2= new IncomeTypeData(this).getTypesByUserName(userName);//获取该用户所有收入类型
			int typeSize2 = typenames2.size();
			titles = new String[typeSize2];
			colors = new int[typeSize2];
			values = new ArrayList<double[]>();
			styles = new PointStyle[typeSize2];
			//动态获取该用户现有的收入类型
			for(int i=0;i<typeSize2;i++){
				titles[i] = typenames2.get(i).toString();
				Random random = new Random();
				colors[i] = Color.rgb(random.nextInt(256),random.nextInt(256), random.nextInt(256));
				styles[i] = PointStyle.DIAMOND;
				String selection = " and "+JZshouru.SR_ITEM+" = '"+typenames2.get(i)+"'";
				values.add(getShouRuYear(selection));
				selection = "";
			}
			title = "收入走势图";
			break;
		}
         List<double[]> x = new ArrayList<double[]>();
         for (int i = 0; i < titles.length; i++) {
             x.add(new double[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 });
         }
         
         XYMultipleSeriesRenderer renderer = buildRenderer(colors, styles);
         int length = renderer.getSeriesRendererCount();
         for (int i = 0; i < length; i++) {
             ((XYSeriesRenderer) renderer.getSeriesRendererAt(i)).setFillPoints(true);
         }
         setChartSettings(renderer, title, "月份", "金额", 1, 12, 0, 10000, Color.BLACK, Color.BLACK);
         renderer.setXLabels(12);
         renderer.setYLabels(10);
         renderer.setShowGrid(true);
         renderer.setXLabelsAlign(Align.RIGHT);
         renderer.setYLabelsAlign(Align.CENTER);
         renderer.setZoomButtonsVisible(true);
         renderer.setPanLimits(new double[] {0, 12, 0, 10000 });//设置拖动时X轴Y轴允许的最大值最小值
         renderer.setZoomLimits(new double[] {0, 12, 0, 10000});//设置放大缩小时X轴Y轴允许的最大最小值.
         View view = ChartFactory.getLineChartView(this, buildDataset(titles, x, values), renderer);
         view.setBackgroundColor(Color.BLACK);
         return view;
    }
    
    /*
     * 从数据库中获得当年每月的支出信息
     * */
    public double [] getZhiChuYear(String select){
    	double d[]=new double[12];
	    ArrayList<JZzhichu> zhiChuList = new ArrayList<JZzhichu>();
	    for(int i=1;i<=12;i++){
	    	double countZhiChu=0;
		    String selectionzhichu = JZzhichu.ZC_USER + "='" +userName + "'" +" and " +JZzhichu.ZC_YEAR+"="+GetTime.getYear()+" and "+JZzhichu.ZC_MONTH+"="+i+select;
		    zhiChuList =  dataHelper.GetZhiChuList(selectionzhichu);
	    	for(JZzhichu zhichu:zhiChuList){
	    		countZhiChu += zhichu.getZc_Count();
	    	}
	    	d[i-1]=countZhiChu;
	    }
	    return d;
    }
    /*
     * 从数据库中获得当年每月的收入信息
     * */
    public double [] getShouRuYear(String select){
    	double d[]=new double[12];
	    ArrayList<JZshouru> shouRuList = new ArrayList<JZshouru>();
	    for(int i=1;i<=12;i++){
	    	double countShouRu=0;
		    String selectionshouru = JZshouru.SR_USER + "='" +userName + "'" +" and " + JZshouru.SR_YEAR+"="+GetTime.getYear()+" and "+JZshouru.SR_MONTH+"="+i+select;
		    shouRuList =  dataHelper.GetShouRuList(selectionshouru);
	    	for(JZshouru shouru:shouRuList){
	    		countShouRu += shouru.getSr_Count();
	    	}
	    	d[i-1]=countShouRu;
	    }
	    return d;
    }
    
    @Override
    protected void onResume() {
        overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
        super.onResume();
    }


    private XYMultipleSeriesRenderer buildRenderer(int[] colors, PointStyle[] styles) {
        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
        setRenderer(renderer, colors, styles);
        return renderer;
    }

    private void setRenderer(XYMultipleSeriesRenderer renderer, int[] colors, PointStyle[] styles) {
		renderer.setApplyBackgroundColor(true);//必须设置为true，颜色值才生效
		renderer.setBackgroundColor(Color.rgb(188,159,119));//设置表格背景色
		renderer.setMarginsColor(Color.rgb(135,102,51));//设置周边背景色
		renderer.setAxisTitleTextSize(20);
        renderer.setChartTitleTextSize(30);
        renderer.setLabelsTextSize(20);
        renderer.setLegendTextSize(20);
        renderer.setPointSize(6f);
        renderer.setMargins(new int[] { 35, 45, 20, 20 });//控制你图的边距 上,左,下,右
        int length = colors.length;
        for (int i = 0; i < length; i++) {
            XYSeriesRenderer r = new XYSeriesRenderer();
            r.setColor(colors[i]);
            r.setPointStyle(styles[i]);
            renderer.addSeriesRenderer(r);
        }
    }

    private void setChartSettings(XYMultipleSeriesRenderer renderer, String title, String xTitle, String yTitle, double xMin, double xMax, double yMin, double yMax, int axesColor, int labelsColor) {
        renderer.setChartTitle(title);
        renderer.setXTitle(xTitle);
        renderer.setYTitle(yTitle);
        renderer.setXAxisMin(xMin);
        renderer.setXAxisMax(xMax);
        renderer.setYAxisMin(yMin);
        renderer.setYAxisMax(yMax);
        renderer.setAxesColor(axesColor);
        renderer.setLabelsColor(labelsColor);
    }

    private XYMultipleSeriesDataset buildDataset(String[] titles, List<double[]> xValues, List<double[]> yValues) {
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        addXYSeries(dataset, titles, xValues, yValues, 0);
        return dataset;
    }

    private void addXYSeries(XYMultipleSeriesDataset dataset, String[] titles, List<double[]> xValues, List<double[]> yValues, int scale) {
        int length = titles.length;
        for (int i = 0; i < length; i++) {
            XYSeries series = new XYSeries(titles[i], scale);
            double[] xV = xValues.get(i);
            double[] yV = yValues.get(i);
            int seriesLength = xV.length;
            for (int k = 0; k < seriesLength; k++) {
                series.add(xV[k], yV[k]);
            }
            dataset.addSeries(series);
        }
    }
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
    	menu.add(0, 100, 0, "月收入和支出");
    	menu.add(0, 200, 0, "详细月收入");
    	menu.add(0, 300, 0, "详细月支出");

		return true;
	}

	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		int itemId = item.getItemId();
		switch (itemId) {
		case 100:
			setContentView(setView(ZCSR));
			break;
		case 200:
			setContentView(setView(srMingXi));
			break;
		case 300:
			setContentView(setView(zcMingXi));
			break;
		}
		return true;
	}
    
}