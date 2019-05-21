package com.xxzxxz.press.web;

import com.xxzxxz.press.model.vo.*;
import com.xxzxxz.press.repository.ComplainRepository;
import com.xxzxxz.press.repository.ConsumerRepository;
import com.xxzxxz.press.repository.OrdersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Controller
public class LeaderSystemcontroller {

    @Autowired
    private ComplainRepository complainRepository;

    @Autowired
    private ConsumerRepository consumerRepository;

    @Autowired
    private OrdersRepository orderRepository;


    @RequestMapping("/leaderSearch")//主页
    public String index(Model model){
        List<Object> totalCount =orderRepository.TotalCount();
        model.addAttribute("totalCount",totalCount);
        return "leader_index";
    }

    @RequestMapping("/leader_index")//报刊订量分站统计
    public String leaderIndex(Model model){
        List<Object> totalCount =orderRepository.TotalCount();
        //List<Result> list= ObjectConvert.objectToBean(totalCount,Result.class);
        model.addAttribute("totalCount",totalCount);
        return "leader_index";
    }

    @RequestMapping("/s1_yearsta")//订量分站统计
    public String s1Yearsta(Model map,String station_name){
        List<Order_monthcount> monthCount =orderRepository.MonthCount(station_name);
        List<Order_yearcount> yearCount =orderRepository.YearCount(station_name);
        List<Double> mCount = new ArrayList<Double>();
        List<String> mName = new ArrayList<String>();
        List<Integer> yCount = new ArrayList<Integer>();
        List<String> yName = new ArrayList<String>();
        for(Order_monthcount o:monthCount){
            mCount.add(o.getCount());
        }
        for(Order_monthcount o:monthCount){
            mName.add(o.getMonth());
        }
        for(Order_yearcount o:yearCount){
            yCount.add(o.getCount());
        }
        for(Order_yearcount o:yearCount){
            yName.add(o.getYear());
        }
        map.addAttribute("monthCount",mCount);
        map.addAttribute("monthName",mName);
        map.addAttribute("YearCount",yCount);
        map.addAttribute("YearName",yName);
        map.addAttribute("station_name",station_name);
        return "s1_yearsta";
    }

    @RequestMapping("/sum_yearsta")//报刊订量年统计
    public String sumYearsta(Model map){
        List<Order_monthcount> monthPrice=orderRepository.YearMap();
        List<Double> mCount = new ArrayList<Double>();
        List<String> mName = new ArrayList<String>();
        for(Order_monthcount o:monthPrice){
            mCount.add(o.getCount());
        }
        for(Order_monthcount o:monthPrice){
            mName.add(o.getMonth());
        }
        map.addAttribute("monthCount",mCount);
        map.addAttribute("monthName",mName);
        return "sum_yearsta";
    }

    @RequestMapping("/sum_pastyearsta")//历年报刊订量统计
    public String sumPastyearsta(Model map){
        List<Order_yearcount> history=orderRepository.HistoryCount();
        List<Integer> yCount = new ArrayList<Integer>();
        List<String> yName = new ArrayList<String>();
        for(Order_yearcount o:history){
            yCount.add(o.getCount());
        }
        for(Order_yearcount o:history){
            yName.add(o.getYear());
        }
        map.addAttribute("YearCount",yCount);
        map.addAttribute("YearName",yName);
        return "sum_pastyearsta";
    }

    @RequestMapping("buy_order")//订阅 订购对比分析
    public String buyOrder(Model map){
        List<Object> contrast=orderRepository.Contrast();
        map.addAttribute("contrast",contrast);
        return "buy_order";
    }

    @RequestMapping("/money_index")//订阅金额分站统计
    public String moneyIndex(Model map){
        List<Object> TotalPrice =orderRepository.TotalPrice();
        map.addAttribute("totalPrice",TotalPrice);
        return "money_index";
    }

    @RequestMapping("/s1_moneysta")//辽宁订阅金额分站统计
    public String s1Moneysta(Model map,String station_name){
        List<Order_monthcount>monthPrice=orderRepository.MonthPrice(station_name);
        List<Order_yearcount>yearPrice=orderRepository.YearPrice(station_name);
        List<Double> mCount = new ArrayList<Double>();
        List<String> mName = new ArrayList<String>();
        List<Integer> yCount = new ArrayList<Integer>();
        List<String> yName = new ArrayList<String>();
        for(Order_monthcount o:monthPrice){
            mCount.add(o.getCount());
        }
        for(Order_monthcount o:monthPrice){
            mName.add(o.getMonth());
        }
        for(Order_yearcount o:yearPrice){
            yCount.add(o.getCount());
            System.out.println(o.getCount());
        }
        for(Order_yearcount o:yearPrice){
            yName.add(o.getYear());
        }
        map.addAttribute("monthCount",mCount);
        map.addAttribute("monthName",mName);
        map.addAttribute("YearCount",yCount);
        map.addAttribute("YearName",yName);
        map.addAttribute("station_name",station_name);
        return "s1_moneysta";
    }

    @RequestMapping("/user_time")//订户订期分析
    public String userTime(Model map){
        List<Consumer_frequency>duration=consumerRepository.Frequency();
        String[] time_name = {"-","半月","月","季","半年","年","长期"};
        List<String> timename = new ArrayList<String>();
        List<Integer> count = new ArrayList<Integer>();
        List<Integer> sum = new ArrayList<Integer>();
        for (Consumer_frequency o:duration){
            timename.add(time_name[o.getduration()]);
            count.add(o.getcount());
            sum.add(o.getsum());
        }
        while(timename.size()<6){
            timename.add("-");
            count.add(0);
            sum.add(0);
        }
        map.addAttribute("timename",timename);
        map.addAttribute("count",count);
        map.addAttribute("sum",sum);
        return "user_time";
    }

    @RequestMapping("/user_character")//订户性质分析
    public String userCharacter(Model map){
        List<ConsumerVo>level=consumerRepository.ConsumerType();
        String[] level_name = {"普通客户","单位客户","会员客户","大客户"};
        List<String> type = new ArrayList<String>();
        List<Integer> count = new ArrayList<Integer>();
        for (ConsumerVo o:level){
            type.add(level_name[o.getLevel()]);
            count.add(o.getCount());
        }
        while(type.size()<4){
            type.add("-");
            count.add(0);
        }
        map.addAttribute("type",type);
        map.addAttribute("count",count);
        return "user_character";
    }

    @RequestMapping("/user_survey")//订户调查分析
    public String userSurvey(Model map){
         List<Consumer_age>Age=consumerRepository.ConsumerAge();
        List<ConsumerVo>language=consumerRepository.ConsumerLan();
        List<ConsumerVo>Province=consumerRepository.ConsumerProvince();
        List<ConsumerVo>Degree=consumerRepository.ConsumerDegree();
        List<ConsumerVo>Sex=consumerRepository.ConsumerSex();
        List<ConsumerVo>Occupation=consumerRepository.ConsumerOccupation();

        List<String> lan = new ArrayList<String>();        //语言
        List<Integer> lan_count = new ArrayList<Integer>();
        for (ConsumerVo o:language){
            lan.add(o.getLanguage());
            lan_count.add(o.getCount());
        }
        while(lan.size()<5){
            lan.add("-");
            lan_count.add(0);
        }
        map.addAttribute("lan",lan);
        map.addAttribute("lan_count",lan_count);

      List<String> age = new ArrayList<String>();        //年龄
        List<Integer> age_count = new ArrayList<Integer>();
        for (Consumer_age o:Age){
            age.add(o.getAgeratio());
            age_count.add(o.getCount());
        }
        map.addAttribute("age",age);
        map.addAttribute("age_count",age_count);

        String[] sex_name = {"女性","男性"};        //性别
        List<String> sex = new ArrayList<String>();
        List<Integer> sex_count = new ArrayList<Integer>();
        for (ConsumerVo o:Sex){
            sex.add(sex_name[o.getSex()]);
            sex_count.add(o.getCount());
        }
        while(sex.size()<2){
            sex.add("-");
            sex_count.add(0);
        }
        map.addAttribute("sex",sex);
        map.addAttribute("sex_count",sex_count);

        List<String> pro = new ArrayList<String>();        //省份
        List<Integer> pro_count = new ArrayList<Integer>();
        for (ConsumerVo o:Province){
            pro.add(o.getProvince());
            pro_count.add(o.getCount());
        }
        while(pro.size()<7){
            pro.add("-");
            pro_count.add(0);
        }
        map.addAttribute("pro",pro);
        map.addAttribute("pro_count",pro_count);

        List<String> deg = new ArrayList<String>();        //学历
        List<Integer> deg_count = new ArrayList<Integer>();
        for (ConsumerVo o:Degree){
            deg.add(o.getDegree());
            deg_count.add(o.getCount());
        }
        while(deg.size()<5){
            deg.add("-");
            deg_count.add(0);
        }
        map.addAttribute("deg",deg);
        map.addAttribute("deg_count",deg_count);

        List<String> occ = new ArrayList<String>();        //学历
        List<Integer> occ_count = new ArrayList<Integer>();
        for (ConsumerVo o:Occupation){
            occ.add(o.getOccupation());
            occ_count.add(o.getCount());
        }
        while(occ.size()<7){
            occ.add("-");
            occ_count.add(0);
        }
        map.addAttribute("occ",occ);
        map.addAttribute("occ_count",occ_count);

        return "user_survey";
    }

    @RequestMapping("/station_log")//分站日志查询
    public String stationLog( String logtime,Model map){
        if (logtime==null){
            java.util.Date date = new java.util.Date();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            logtime = format.format(date);
        }
        List<Object>DayLog=orderRepository.TotalDayCount(logtime);

        map.addAttribute("DayLog",DayLog);
        map.addAttribute("logtime",logtime);
        System.out.println(DayLog);
        return "station_log";
    }

    @RequestMapping("/s1_log")//辽宁分站日志查询
    public String s1Log(Model map,String station_name,String logtime){
        if(logtime==null){
            logtime="%";
        }
        //    System.out.println(logtime);
        List<Order_stationDayCount> dayLog=orderRepository.StationDayCount(station_name,logtime);
        List<Order_stationdaytotalpost> dayLogPost=orderRepository.StationDayTotalPost(station_name,logtime);
        int sumcount=0;
        int sumprice=0;
        int postcount=0;
        double postsum=0;
        if(dayLog != null)
            for (Order_stationDayCount o:dayLog){
                sumcount=o.getCount();
                sumprice=o.getPrice();
            }
        if(dayLogPost!=null)
            for (Order_stationdaytotalpost o:dayLogPost){
                System.out.println(o.toString());
                postcount=o.getCopies();
                postsum= o.getPrice();
            }
        map.addAttribute("sumcount",sumcount);
        map.addAttribute("sumprice",sumprice);
        map.addAttribute("postcount",postcount);
        map.addAttribute("postsum",postsum);
        map.addAttribute("station_name",station_name);
        return "s1_log";
    }

    @RequestMapping("/unsubscribe_reasonsta")//掉单原因统计分析
    public String unsubscribeReasonsta(Model map){
        List<Object>unsubReason=orderRepository.unsubreason();
        map.addAttribute("UnsubReason",unsubReason);
        return "unsubscribe_reasonsta";
    }

    @RequestMapping("/complain_month")//投诉情况月报
    public String complainMonth(Model map){
        List<ComplainResultVo> complains = complainRepository.ComplainCountByType();
        map.addAttribute("complains",complains);
        return "complain_month";
    }

    @RequestMapping("/complain")//投诉详情
    public String complain1(Integer type,Model map){
        List<String>content=complainRepository.ComplainContent(type.intValue());
        map.addAttribute("Content",content);
        map.addAttribute("type",type);
        return "complain";
    }

    @RequestMapping("/expire_daily")//每日投递到期单报表
    public String expireDaily(Model map){
        List<Object>ended=orderRepository.EndedDelivery();
        map.addAttribute("End",ended);
        return "expire_daily";
    }

    @RequestMapping("/daily_log")//每日汇总表
    public String dailyLog(Model map){
        List<Day> dayTotal=orderRepository.DayCount();
        List<Day> dayPost=orderRepository.DayTotalPost();
        int totalcount=0;
        double totalprice=0;
        int postcount=0;
        double postprice=0;
        for (Day o:dayTotal){
            try {
                totalcount=o.getCount();
                totalprice=o.getSum();
            }catch (Exception e){
                break;
            }
        }
        for (Day o:dayPost){
            try{
            postcount=o.getCount();
            postprice=o.getSum();
            }catch (Exception e){
                break;
            }
        }
        map.addAttribute("totalcount",totalcount);
        map.addAttribute("totalprice",totalprice);
        map.addAttribute("postcount",postcount);
        map.addAttribute("postprice",postprice);
        return "daily_log";
    }

    @RequestMapping("/station_monthsta")//分站月结算统计
    public String stationMonthsta(Model map){
        List<Object>monthStatistic=orderRepository.TotalMonthCount();
        map.addAttribute("month",monthStatistic);
        return "station_monthsta";
    }

    @RequestMapping("/s1_monthsta")//辽宁分站月结算统计
    public String s1Monthsta(Model map,String station_name){
        List<Day> StationMonth=orderRepository.StationMonthCount(station_name);
        List<Day> StationMonthPost=orderRepository.StationMonthTotalPost(station_name);
        int monthcount=0;
        double monthprice=0;
        int postcount=0;
        double postprice=0;
        for (Day o:StationMonth){
            monthcount=o.getCount();
            monthprice=o.getSum();
        }
        for (Day o:StationMonthPost){
            postcount=o.getCount();
            postprice=o.getSum();
        }
        map.addAttribute("monthcount",monthcount);
        map.addAttribute("monthprice",monthprice);
        map.addAttribute("postcount",postcount);
        map.addAttribute("postprice",postprice);
        map.addAttribute("station_name",station_name);
        return "s1_monthsta";
    }



}
