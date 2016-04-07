package com.tust.tools.bean;

/*
 * 所有项目
 * */
public class JZItem {
	//支出项目
 public static final String canyin="餐饮"; 
	public static final String cy_zaocan="早餐"; 
	public static final String cy_wucan="午餐"; 
	public static final String cy_wancan="晚餐"; 
	public static final String cy_yexiao="夜宵"; 
	public static final String cy_lingshi="零食"; 
	public static final String cy_yuanliao="原料"; 
	public static final String cy_qita="其他"; 
	public static String cy_s[]=new String []{cy_zaocan,cy_wucan,cy_wancan,cy_yexiao,cy_lingshi,cy_yuanliao,cy_qita};
 public static final String jiaotong="交通"; 
	public static final String jt_dadi="打的"; 
	public static final String jt_gongjiao="公交"; 
	public static final String jt_ditie="地铁"; 
	public static final String jt_huoche="火车";
	public static final String jt_chuanbo="船舶"; 
	public static final String jt_feiji="飞机"; 
	public static final String jt_jiayou="加油"; 
	public static final String jt_tingche="停车费"; 
	public static final String jt_qita="其他"; 
	public static String jt_s[]=new String []{jt_dadi,jt_gongjiao,jt_ditie,jt_huoche,jt_chuanbo,jt_feiji,jt_jiayou,jt_tingche,jt_qita};
 public static final String gouwu="购物"; 
	public static final String gw_yiwu="服饰"; 
	public static final String gw_riyong="日用品"; 
	public static final String gw_shuma="数码产品"; 
	public static final String gw_huzhuang="化妆品"; 
	public static final String gw_dinaqi="电器"; 
	public static final String gw_jiaju="家具"; 
	public static final String gw_yanjiu="烟酒"; 
	public static final String gw_qita="其他"; 
	public static String gw_s[]=new String []{gw_yiwu,gw_riyong,gw_shuma,gw_huzhuang,gw_dinaqi,gw_jiaju,gw_yanjiu,gw_qita};
 public static final String yule="娱乐"; 
	public static final String yl_changge="唱歌"; 
	public static final String yl_dianying="电影"; 
	public static final String yl_wangyou="网游"; 
	public static final String yl_dianshi="电视"; 
	public static final String yl_jianshen="健身"; 
	public static final String yl_xiyu="洗浴"; 
	public static final String yl_lvyou="旅游"; 
	public static final String yl_qita="其他";
	public static String yl_s[]=new String []{yl_changge,yl_dianying,yl_wangyou,yl_dianshi,yl_jianshen,yl_xiyu,yl_lvyou,yl_qita};
 public static final String yijiao="医教"; 
	public static final String yj_kanbing="看病买药"; 
	public static final String yj_peixun="培训考试"; 
	public static final String yj_jiaocai="学习教材"; 
	public static final String yj_jiajiao="家教补习"; 
	public static final String yj_qita="其他"; 
	public static String yj_s[]=new String []{yj_kanbing,yj_peixun,yj_jiaocai,yj_jiajiao,yj_qita};
 public static final String jujia="居家"; 
	public static final String jj_shudian="水电燃气"; 
	public static final String jj_tongxun="手机电话"; 
	public static final String jj_meifa="美容美发"; 
	public static final String jj_fangdai="房款房贷"; 
	public static final String jj_fangzu="住宿房租"; 
	public static final String jj_wuye="物业费用"; 
	public static final String jj_qita="其他"; 
	public static String jj_s[]=new String []{jj_shudian,jj_tongxun,jj_meifa,jj_fangdai,jj_fangzu,jj_wuye,jj_qita};
 public static final String touzi="投资"; 
	public static final String tz_zhengquan="证券期货"; 
	public static final String tz_huangjin="黄金实物"; 
	public static final String tz_gudong="古董书画"; 
	public static final String tz_daikuan="投资贷款"; 
	public static final String tz_baoxian="保险"; 
	public static final String tz_qita="其他"; 
	public static String tz_s[]=new String []{tz_zhengquan,tz_huangjin,tz_gudong,tz_daikuan,tz_baoxian,tz_qita};
 public static final String renqing="人情"; 
	public static final String rq_lijin="礼金"; 
	public static final String rq_wupin="物品"; 
	public static final String rq_daifukuan="代付款"; 
	public static final String rq_cishan="慈善捐款"; 
	public static final String rq_qita="其他"; 
	public static String rq_s[]=new String []{rq_lijin,rq_wupin,rq_daifukuan,rq_cishan,rq_qita};
 public static String leibie_s[]=new String []{canyin,jiaotong,gouwu,yule,yijiao,jujia,touzi,renqing};
 	//收入项目
 	public static final String gongzi="工资"; 
 	public static final String gupiao="股票"; 
 	public static final String jiangjin="奖金"; 
 	public static final String lixi="利息"; 
 	public static final String fenhong="分红"; 
 	public static final String butie="补贴"; 
 	public static final String baoxiao="报销"; 
 	public static final String qita="其他"; 
 	public static String shoru_s[]=new String []{gongzi,gupiao,jiangjin,lixi,fenhong,butie,baoxiao,qita};
 	//借贷项目
 	public static final String jieru="借入";
 	public static final String jiechu="借出";
 	public static final String huankuan="还款";
 	public static final String shoukuan="收款";
 	public static String jiedai_s[]=new String []{jieru,jiechu,huankuan,shoukuan};
}
