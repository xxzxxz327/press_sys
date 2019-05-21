package com.xxzxxz.press.web;

import com.xxzxxz.press.model.*;
import com.xxzxxz.press.param.OrdersParam;
import com.xxzxxz.press.repository.*;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.w3c.dom.Attr;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class OrdersController {
    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private PressInfoRepository pressInfoRepository;

    @Autowired
    private ZenkaRepository zenkaRepository;

    @Autowired
     private ConsumerRepository consumerRepository;

    @Autowired
    private OrdersBackupRepository ordersBackupRepository;

    @Autowired
    private ChangeReasonRepository changeReasonRepository;

    @Autowired
    private OrderLogRepository orderLogRepository;

    @Autowired
    private VoucherRepository voucherRepository;

    @Autowired
    private PrintRepository printRepository;

    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private AttributeRepository attributeRepository;

    @Autowired
    private DiscountRepository discountRepository;


    public static List removeDuplicate(List list){
        List listTemp = new ArrayList();
        for(int i=0;i<list.size();i++){
            if(!listTemp.contains(list.get(i))){
                listTemp.add(list.get(i));
            }
        }
        return listTemp;
    }


    public void saveBackupOrder(OrdersBackup ordersBackup,Orders orders){
        ordersBackup.setAddress(orders.getAddress());
        ordersBackup.setDuration(orders.getDuration());
        ordersBackup.setEndDate(orders.getEndDate());
        ordersBackup.setId(orders.getId());
        ordersBackup.setPrice(orders.getPrice());
        ordersBackup.setStatus(orders.getStatus());
    }

    public void saveOrderLog(OrderLog orderLog,Orders orders,int status,int principalId){
        orderLog.setOrderId(orders.getId());
        orderLog.setDate(new Date());
        orderLog.setOperation(status);
        orderLog.setPrincipalId(principalId);

    }

    public int getConsumerId(String consumerName){
        String[] splited = consumerName.split("\\s+");
        return Integer.parseInt(splited[0]);
    }


    @RequestMapping("/toAddOrders")
    public String toAddOrders(Model model,HttpServletRequest request){
        int stationId=(Integer) request.getSession().getAttribute("stationId");
        model.addAttribute("stationId",stationId);
        List<Zenka> unuseZenkas=zenkaRepository.findListByFlagUnuse();
        List<Voucher> unuseVouchers=voucherRepository.findListByFlagUnuse();
        List<PressInfo> pressInfos=pressInfoRepository.findList();
        List<Print> prints=printRepository.findList();
        List<Station> stations=stationRepository.findListStationExceptNow(stationId);
        model.addAttribute("stations",stations);
        model.addAttribute("prints",prints);
        model.addAttribute("unuseVouchers",unuseVouchers);
        model.addAttribute("unuseZenkas",unuseZenkas);
        model.addAttribute("pressInfos",pressInfos);
        List<Object> areaNames=stationRepository.findAreaAddress();
        List<String> stationIds=stationRepository.findStationIdAddress();
        model.addAttribute("areaNames",areaNames);
        model.addAttribute("stationIds",stationIds);
        return "orders/ordersAdd";
    }

    @RequestMapping("/addOrders")
    public String addOrders(@Valid OrdersParam ordersParam,BindingResult result,Model model,@RequestParam(value = "address1")String address1,HttpServletRequest request){
        String errorMsg="";
        if(result.hasErrors()){
            List<ObjectError> list=result.getAllErrors();
            for(ObjectError error:list){
                errorMsg=errorMsg+error.getCode()+"-"+error.getDefaultMessage()+";";
            }
            model.addAttribute("errorMsg",errorMsg);
            int stationId=(Integer) request.getSession().getAttribute("stationId");
            List<Zenka> unuseZenkas=zenkaRepository.findListByFlagUnuse();
            List<Voucher> unuseVouchers=voucherRepository.findListByFlagUnuse();
            List<PressInfo> pressInfos=pressInfoRepository.findList();
            List<Print> prints=printRepository.findList();
            List<Station> stations=stationRepository.findListStationExceptNow(stationId);
            model.addAttribute("stations",stations);
            model.addAttribute("prints",prints);
            model.addAttribute("unuseVouchers",unuseVouchers);
            model.addAttribute("unuseZenkas",unuseZenkas);
            model.addAttribute("pressInfos",pressInfos);
            model.addAttribute("stationId",stationId);
            List<Object> areaNames=stationRepository.findAreaAddress();
            List<String> stationIds=stationRepository.findStationIdAddress();
            model.addAttribute("areaNames",areaNames);
            model.addAttribute("stationIds",stationIds);
            return "orders/ordersAdd";
        }
        if(address1==null){
            errorMsg="详细地址不能为空";
            model.addAttribute("errorMsg",errorMsg);
            int stationId=(Integer) request.getSession().getAttribute("stationId");
            List<Zenka> unuseZenkas=zenkaRepository.findListByFlagUnuse();
            List<Voucher> unuseVouchers=voucherRepository.findListByFlagUnuse();
            List<PressInfo> pressInfos=pressInfoRepository.findList();
            List<Print> prints=printRepository.findList();
            List<Station> stations=stationRepository.findListStationExceptNow(stationId);
            model.addAttribute("stations",stations);
            model.addAttribute("prints",prints);
            model.addAttribute("unuseVouchers",unuseVouchers);
            model.addAttribute("unuseZenkas",unuseZenkas);
            model.addAttribute("pressInfos",pressInfos);
            model.addAttribute("stationId",stationId);
            List<Object> areaNames=stationRepository.findAreaAddress();
            List<String> stationIds=stationRepository.findStationIdAddress();
            model.addAttribute("areaNames",areaNames);
            model.addAttribute("stationIds",stationIds);
            return "orders/ordersAdd";
        }
        Consumer consumer=consumerRepository.findById(ordersParam.getConsumerId().intValue());
        if(consumer==null){
            errorMsg="没有这个用户";
            model.addAttribute("errorMsg",errorMsg);
            int stationId=(Integer) request.getSession().getAttribute("stationId");
            List<Zenka> unuseZenkas=zenkaRepository.findListByFlagUnuse();
            List<Voucher> unuseVouchers=voucherRepository.findListByFlagUnuse();
            List<PressInfo> pressInfos=pressInfoRepository.findList();
            List<Print> prints=printRepository.findList();
            List<Station> stations=stationRepository.findListStationExceptNow(stationId);
            model.addAttribute("stations",stations);
            model.addAttribute("prints",prints);
            model.addAttribute("unuseVouchers",unuseVouchers);
            model.addAttribute("unuseZenkas",unuseZenkas);
            model.addAttribute("pressInfos",pressInfos);
            model.addAttribute("stationId",stationId);
            List<Object> areaNames=stationRepository.findAreaAddress();
            List<String> stationIds=stationRepository.findStationIdAddress();
            model.addAttribute("areaNames",areaNames);
            model.addAttribute("stationIds",stationIds);;
            return "orders/ordersAdd";
        }
        Orders orders=new Orders();
        BeanUtils.copyProperties(ordersParam,orders);
        orders.setAddress(address1+" "+orders.getAddress());
        int voucherId=ordersParam.getVoucherId();
        int zenkaId=ordersParam.getZenkaId();
        orders.setXiaTime(new Date());
        int duration=ordersParam.getDuration()*7;
        double price=0;
        PressInfo pressInfo=pressInfoRepository.findById(ordersParam.getPressId().intValue());
        if(duration<7){
            price=duration*pressInfo.getDayPrice();
        }else if(duration<30){
            price=duration*pressInfo.getWeekPrice();
        }
        else if(duration<90){
            price=duration*pressInfo.getMonthPrice();
        }else if(duration<180){
            price=duration*pressInfo.getSessionPrice();
        }else if(duration<365){
            price=duration*pressInfo.getHalfYearPrice();
        }else{
            price=duration*pressInfo.getYearPrice();
        }
        price*=ordersParam.getCopies();
        Date startDate=ordersParam.getStartDate();

        long startTime=startDate.getTime();
        long du=(long)duration*24*3600*1000;
        long endTime=startTime+du;
        if(ordersParam.getZenkaId()!=-1){
            endTime=endTime+(long)zenkaRepository.findById(ordersParam.getZenkaId().intValue()).getDuration()*7*24*3600*1000;
        }
        System.out.println(endTime);
        Date endDate=new Date();
        if(voucherId!=-1){
            Voucher voucher=voucherRepository.findById(voucherId);
            voucher.setFlag(1);
            price-=voucher.getSum();
        }
        if(zenkaId!=-1){
            Zenka zenka=zenkaRepository.findById(zenkaId);
            zenka.setFlag(1);
        }

        //计算报刊折扣
        Discount discount=discountRepository.findByPressId(ordersParam.getPressId());
        if(discount!=null){
            price*=discount.getStrategy();
        }

        endDate.setTime(endTime);
        orders.setEndDate(endDate);
        orders.setPrice(price);
        orders.setStatus(0);
        orders.setSolicitId((Integer)request.getSession().getAttribute("staffId"));

        Attribute attribute=attributeRepository.findByPressId(ordersParam.getPressId());
        orders.setUnsub(attribute.getUnsub());
        orders.setChange(attribute.getChange());
        if(ordersParam.getOrderType()==0){
            orders.setStationId((Integer)request.getSession().getAttribute("stationId"));
        }else{
            orders.setStationId(ordersParam.getStationId());
        }
        ordersRepository.save(orders);
        OrderLog orderLog=new OrderLog();
        saveOrderLog(orderLog,orders,0,(Integer)request.getSession().getAttribute("staffId"));
        orderLogRepository.save(orderLog);
        return "redirect:/ordersList";
    }

    @RequestMapping("/ordersList")
    public String list(Model model, @RequestParam(value="page",defaultValue = "0")Integer page,
                       @RequestParam(value="size",defaultValue = "6")Integer size,HttpServletRequest request){
        Sort sort=new Sort(Sort.Direction.DESC,"id");
        Pageable pageable= PageRequest.of(page,size,sort);
        Page<Object[]> orders=ordersRepository.findListByStationId((Integer)request.getSession().getAttribute("stationId"),pageable);
        List<Object[]> listConsumerName=ordersRepository.findConsumerNames();
        List<String> list=new ArrayList<String>();
        for(int i=0;i<listConsumerName.size();i++){
            list.add(listConsumerName.get(i)[0].toString()+" "+listConsumerName.get(i)[1].toString());
            System.out.println(listConsumerName.get(i)[0].toString()+listConsumerName.get(i)[1].toString());
        }
        model.addAttribute("listConsumerName",list);
        model.addAttribute("orders",orders);
        return "orders/ordersList";
    }

    @RequestMapping("/toOrdersOver")
    public String toOrdersOver(){
        return "orders/ordersOverSearch";
    }

    @RequestMapping("/ordersOver")
    public String ordersOver(Model model, @RequestParam(value="page",defaultValue = "0")Integer page,
                       @RequestParam(value="size",defaultValue = "6")Integer size,
                             @RequestParam(value="overDate")@DateTimeFormat(pattern="yyyy-MM-dd")Date overDate){
        Sort sort=new Sort(Sort.Direction.DESC,"id");
        Pageable pageable= PageRequest.of(page,size,sort);
        Page<Orders> orders=null;
        orders=ordersRepository.findListByEndDate(overDate,pageable);
        List<Object[]> listConsumerName=ordersRepository.findConsumerNames();
        List<String> list=new ArrayList<String>();
        for(int i=0;i<listConsumerName.size();i++){
            list.add(listConsumerName.get(i)[0].toString()+" "+listConsumerName.get(i)[1].toString());
            System.out.println(listConsumerName.get(i)[0].toString()+listConsumerName.get(i)[1].toString());
        }
        model.addAttribute("listConsumerName",list);
        model.addAttribute("orders",orders);
        return "orders/ordersOver";
    }

    @RequestMapping("/ordersOne")
    public String findOne(Model model,@RequestParam(value = "consumerName")String consumerName,HttpServletRequest request){
        Sort sort=new Sort(Sort.Direction.DESC,"id");
        Pageable pageable= PageRequest.of(0,6,sort);
        if(consumerName==null){
            String errorMsg="id不能为空";
            model.addAttribute("errorMsg",errorMsg);
            Page<Object[]> orders=ordersRepository.findListByStationId((Integer)request.getSession().getAttribute("stationId"),pageable);
            List<Object[]> listConsumerName=ordersRepository.findConsumerNames();
        List<String> list=new ArrayList<String>();
        for(int i=0;i<listConsumerName.size();i++){
            list.add(listConsumerName.get(i)[0].toString()+" "+listConsumerName.get(i)[1].toString());
            System.out.println(listConsumerName.get(i)[0].toString()+listConsumerName.get(i)[1].toString());
        }
        model.addAttribute("listConsumerName",list);
            model.addAttribute("orders",orders);
            return "orders/ordersList";
        }
       Page<Object[]> orders=ordersRepository.findOrdersByConsumerId(getConsumerId(consumerName),(Integer)request.getSession().getAttribute("stationId"),pageable);
        List<Object[]> listConsumerName=ordersRepository.findConsumerNames();
        List<String> list=new ArrayList<String>();
        for(int i=0;i<listConsumerName.size();i++){
            list.add(listConsumerName.get(i)[0].toString()+" "+listConsumerName.get(i)[1].toString());
            System.out.println(listConsumerName.get(i)[0].toString()+listConsumerName.get(i)[1].toString());
        }
        model.addAttribute("listConsumerName",list);
        model.addAttribute("orders",orders);
        return "orders/ordersList";
    }

    @RequestMapping("/ordersNotPay")
    public String findNotPay(Model model, @RequestParam(value="page",defaultValue = "0")Integer page,
                       @RequestParam(value="size",defaultValue = "6")Integer size){
        Sort sort=new Sort(Sort.Direction.DESC,"id");
        Pageable pageable= PageRequest.of(page,size,sort);
        Page<Object[]> orders=ordersRepository.findNotPay(pageable);
        List<Object[]> listConsumerName=ordersRepository.findConsumerNames();
        List<String> list=new ArrayList<String>();
        for(int i=0;i<listConsumerName.size();i++){
            list.add(listConsumerName.get(i)[0].toString()+" "+listConsumerName.get(i)[1].toString());
            System.out.println(listConsumerName.get(i)[0].toString()+listConsumerName.get(i)[1].toString());
        }
        model.addAttribute("listConsumerName",list);
        model.addAttribute("orders",orders);
        return "orders/ordersList";
    }

    @RequestMapping("/uploadOrders")
    public String singleFileUpload(Model model,@RequestParam("file")MultipartFile file, RedirectAttributes redirectAttributes,HttpServletRequest request)throws Exception{
        XSSFWorkbook book=new XSSFWorkbook(file.getInputStream());
        XSSFSheet sheet=book.getSheetAt(0);
        int staffId=(Integer) request.getSession().getAttribute("staffId");
        for(int i=1;i<sheet.getLastRowNum()+1;i++){
            Orders orders=new Orders();
            XSSFRow row=sheet.getRow(i);
            orders.setConsumerId((int) row.getCell(0).getNumericCellValue());
            orders.setPressId((int)row.getCell(1).getNumericCellValue());
            orders.setCopies((int)row.getCell(2).getNumericCellValue());
            orders.setDuration((int)row.getCell(3).getNumericCellValue());
            orders.setOrderType((int)row.getCell(4).getNumericCellValue());
            orders.setStationId((int)row.getCell(5).getNumericCellValue());
            orders.setAddress(row.getCell(6).getStringCellValue());
            orders.setStartDate(row.getCell(7).getDateCellValue());
            orders.setZenkaId((int)row.getCell(8).getNumericCellValue());
            orders.setXiaTime(new Date());
            Date endDate=new Date();
            int duration=orders.getDuration()*7;
            endDate.setTime(orders.getStartDate().getTime()+(long)duration*24*3600*1000);
            orders.setEndDate(endDate);
            orders.setPrintId(-1);
            orders.setVoucherId(-1);
            Attribute attribute=attributeRepository.findByPressId(orders.getPressId().intValue());
            orders.setChange(attribute.getChange());
            orders.setUnsub(attribute.getUnsub());
            orders.setSolicitId(staffId);
            orders.setStatus(0);
            double price=0;
            if(duration<30){
                price=duration*pressInfoRepository.findById(orders.getPressId().intValue()).getDayPrice();
            }else if(duration<90){
                price=duration*pressInfoRepository.findById(orders.getPressId().intValue()).getMonthPrice();
            }else if(duration<180){
                price=duration*pressInfoRepository.findById(orders.getPressId().intValue()).getSessionPrice();
            }else if(duration<365){
                price=duration*pressInfoRepository.findById(orders.getPressId().intValue()).getHalfYearPrice();
            }else{
                price=duration*pressInfoRepository.findById(orders.getPressId().intValue()).getYearPrice();
            }
            price*=orders.getCopies();
            orders.setPrice(price);
            try{
                ordersRepository.save(orders);
            }catch (Exception e){

            }

        }
        return "redirect:/ordersList";
    }

    @RequestMapping("/toPayOrder")
    public String toPayOrder(Model model,int id){
        Orders orders=ordersRepository.findById(id);
        List<Object[]> listConsumerName=ordersRepository.findConsumerNames();
        List<String> list=new ArrayList<String>();
        for(int i=0;i<listConsumerName.size();i++){
            list.add(listConsumerName.get(i)[0].toString()+" "+listConsumerName.get(i)[1].toString());
            System.out.println(listConsumerName.get(i)[0].toString()+listConsumerName.get(i)[1].toString());
        }
        model.addAttribute("listConsumerName",list);
        model.addAttribute("orders",orders);
        return "orders/ordersPay";
    }

    @RequestMapping("/payOrder")
    public String pay(@Valid OrdersParam ordersParam,BindingResult result,Model model,HttpServletRequest request){
        String errorMsg="";
        //参数校验
        if(result.hasErrors()){
            List<ObjectError> list=result.getAllErrors();
            for(ObjectError error:list){
                errorMsg=errorMsg+error.getDefaultMessage()+";";
            }
            model.addAttribute("errorMsg",errorMsg);
            model.addAttribute("orders",ordersParam);
            return "orders/ordersPay";
        }
        Orders orders=ordersRepository.findById(ordersParam.getId().intValue());
        orders.setStatus(ordersParam.getStatus());
        orders.setPayType(ordersParam.getPayType());
        orders.setPayDate(ordersParam.getPayDate());
        ordersRepository.save(orders);
        OrderLog orderLog=new OrderLog();
        saveOrderLog(orderLog,orders,6,(Integer)request.getSession().getAttribute("staffId"));
        orderLogRepository.save(orderLog);
        return "redirect:/ordersList";

    }

    @RequestMapping("/orderUnsub")
    public String orderUnsub(Model model, @RequestParam(value="page",defaultValue = "0")Integer page,
                       @RequestParam(value="size",defaultValue = "6")Integer size,HttpServletRequest request){
        Sort sort=new Sort(Sort.Direction.DESC,"id");
        Pageable pageable= PageRequest.of(page,size,sort);
        Page<Object[]> orders=ordersRepository.findListByStationId((Integer)request.getSession().getAttribute("stationId"),pageable);
        List<Object[]> listConsumerName=ordersRepository.findConsumerNames();
        List<String> list=new ArrayList<String>();
        for(int i=0;i<listConsumerName.size();i++){
            list.add(listConsumerName.get(i)[0].toString()+" "+listConsumerName.get(i)[1].toString());
            System.out.println(listConsumerName.get(i)[0].toString()+listConsumerName.get(i)[1].toString());
        }
        model.addAttribute("listConsumerName",list);
        model.addAttribute("orders",orders);
        return "orders/ordersUnsub";
    }

    @RequestMapping("/orderUnsubPart")
    public String orderUnsubPart(Model model, @RequestParam(value="page",defaultValue = "0")Integer page,
                             @RequestParam(value="size",defaultValue = "6")Integer size,
                                 @RequestParam(value = "consumerName")String consumerName,HttpServletRequest request){
        Sort sort=new Sort(Sort.Direction.DESC,"id");
        Pageable pageable= PageRequest.of(page,size,sort);
        Page<Object[]> orders=null;
        if(consumerName!=null){
            orders=ordersRepository.findOrdersByConsumerId(getConsumerId(consumerName),(Integer)request.getSession().getAttribute("stationId"),pageable);
        }else{
            orders=ordersRepository.findListByStationId((Integer)request.getSession().getAttribute("stationId"),pageable);
        }
        List<Object[]> listConsumerName=ordersRepository.findConsumerNames();
        List<String> list=new ArrayList<String>();
        for(int i=0;i<listConsumerName.size();i++){
            list.add(listConsumerName.get(i)[0].toString()+" "+listConsumerName.get(i)[1].toString());
            System.out.println(listConsumerName.get(i)[0].toString()+listConsumerName.get(i)[1].toString());
        }
        model.addAttribute("listConsumerName",list);
        model.addAttribute("orders",orders);
        return "orders/ordersUnsubPart";
    }

    @RequestMapping("/orderUnsubTotal")
    public String orderUnsubTotal(Model model,@RequestParam(value="page",defaultValue = "0")Integer page,
                                  @RequestParam(value = "size",defaultValue = "6")Integer size,HttpServletRequest request){
        Sort sort=new Sort(Sort.Direction.DESC,"id");
        Pageable pageable=PageRequest.of(page,size,sort);
        Page<Object[]> orders=ordersRepository.findListByStationId((Integer)request.getSession().getAttribute("stationId"),pageable);
        List<ChangeReason> changeReasons=changeReasonRepository.findListByStatus(3);
        model.addAttribute("changeReasons",changeReasons);
        List<Object[]> listConsumerName=ordersRepository.findConsumerNames();
        List<String> list=new ArrayList<String>();
        for(int i=0;i<listConsumerName.size();i++){
            list.add(listConsumerName.get(i)[0].toString()+" "+listConsumerName.get(i)[1].toString());
            System.out.println(listConsumerName.get(i)[0].toString()+listConsumerName.get(i)[1].toString());
        }
        model.addAttribute("listConsumerName",list);
        model.addAttribute("orders",orders);
        return "orders/ordersUnsubTotal";
    }

    @RequestMapping("/toUnsubOrder")
    public String toUnsubOrder(Model model,int id){
        Orders orders=ordersRepository.findById(id);
        List<ChangeReason> changeReasons=changeReasonRepository.findListByStatus(3);
        List<Object[]> listConsumerName=ordersRepository.findConsumerNames();
        List<String> list=new ArrayList<String>();
        for(int i=0;i<listConsumerName.size();i++){
            list.add(listConsumerName.get(i)[0].toString()+" "+listConsumerName.get(i)[1].toString());
            System.out.println(listConsumerName.get(i)[0].toString()+listConsumerName.get(i)[1].toString());
        }
        model.addAttribute("listConsumerName",list);
        model.addAttribute("orders",orders);
        model.addAttribute("changeReasons",changeReasons);
        model.addAttribute("consumerName",consumerRepository.findNameById(orders.getConsumerId()));
        return "orders/ordersUnsubOne";
    }

    @RequestMapping("/unsubOrderOne")
    public String unsubOrderOne(@Valid OrdersParam ordersParam,BindingResult result,Model model,HttpServletRequest request,RedirectAttributes redirectAttributes){
        String errorMsg="";
        //参数校验
        if(result.hasErrors()){
            List<ObjectError> list=result.getAllErrors();
            for(ObjectError error:list){
                errorMsg=errorMsg+error.getDefaultMessage()+";";
            }
            model.addAttribute("errorMsg",errorMsg);
            model.addAttribute("orders",ordersParam);
            return "orders/ordersUnsubOne";
        }
        Orders orders=ordersRepository.findById(ordersParam.getId().intValue());
        OrdersBackup ordersBackup=new OrdersBackup();
        orders.setUnsubReason(ordersParam.getUnsubReason());
        saveBackupOrder(ordersBackup,orders);
        orders.setStatus(3);
        ordersBackup.setStatusNow(3);
        ordersBackupRepository.save(ordersBackup);
        ordersRepository.save(orders);
        OrderLog orderLog=new OrderLog();
        saveOrderLog(orderLog,orders,1,(Integer)request.getSession().getAttribute("staffId"));
        orderLogRepository.save(orderLog);
        List<Object[]> name=consumerRepository.findNameById(orders.getConsumerId());
        String consumerName=name.get(0)[0].toString()+" "+name.get(0)[1].toString();
        redirectAttributes.addAttribute("consumerName",consumerName);
        return "redirect:/orderUnsubPart";
    }


    @RequestMapping("/orderUnsubAll")
    public String orderUnsubAll(Model model,@RequestParam(value = "unsubReason")int unsubReason,@RequestParam(value = "consumerName")String consumerName,HttpServletRequest request,RedirectAttributes redirectAttributes){
        if(consumerName==null){
            String errorMsg="请输入查询用户姓名";
            model.addAttribute("errorMsg",errorMsg);
            List<ChangeReason> changeReasons=changeReasonRepository.findListByStatus(3);
            List<Object[]> listConsumerName=ordersRepository.findConsumerNames();
        List<String> list=new ArrayList<String>();
        for(int i=0;i<listConsumerName.size();i++){
            list.add(listConsumerName.get(i)[0].toString()+" "+listConsumerName.get(i)[1].toString());
            System.out.println(listConsumerName.get(i)[0].toString()+listConsumerName.get(i)[1].toString());
        }
        model.addAttribute("listConsumerName",list);
            model.addAttribute("changeReasons",changeReasons);
            return "orders/ordersUnsubTotal";
        }
        List<Orders> ordersList=ordersRepository.findOrdersByConsumerId(getConsumerId(consumerName));
        for(Orders order:ordersList){
            if(order.getUnsub()==1){
                continue;
            }
            OrdersBackup ordersBackup=new OrdersBackup();
            saveBackupOrder(ordersBackup,order);
            order.setStatus(3);
            order.setUnsubReason(unsubReason);
            ordersBackup.setStatusNow(3);
            ordersBackupRepository.save(ordersBackup);
            ordersRepository.save(order);
            OrderLog orderLog=new OrderLog();
            saveOrderLog(orderLog,order,1,(Integer)request.getSession().getAttribute("staffId"));
            orderLogRepository.save(orderLog);
        }
        redirectAttributes.addAttribute("consumerName",consumerName);
        return "redirect:/orderUnsubPart";
    }

    @RequestMapping("/orderChange")
    public String orderChange(Model model, @RequestParam(value="page",defaultValue = "0")Integer page,
                             @RequestParam(value="size",defaultValue = "6")Integer size,HttpServletRequest request){
        Sort sort=new Sort(Sort.Direction.DESC,"id");
        Pageable pageable= PageRequest.of(page,size,sort);
        Page<Object[]> orders=ordersRepository.findListByStationId((Integer)request.getSession().getAttribute("stationId"),pageable);
        List<Object[]> listConsumerName=ordersRepository.findConsumerNames();
        List<String> list=new ArrayList<String>();
        for(int i=0;i<listConsumerName.size();i++){
            list.add(listConsumerName.get(i)[0].toString()+" "+listConsumerName.get(i)[1].toString());
            System.out.println(listConsumerName.get(i)[0].toString()+listConsumerName.get(i)[1].toString());
        }
        model.addAttribute("listConsumerName",list);
        model.addAttribute("orders",orders);
        return "orders/ordersChange";
    }

    @RequestMapping("/orderChangePart")
    public String orderChangePart(Model model, @RequestParam(value="page",defaultValue = "0")Integer page,
                                 @RequestParam(value="size",defaultValue = "6")Integer size,
                                 @RequestParam(value = "consumerName")String consumerName,HttpServletRequest request){
        Sort sort=new Sort(Sort.Direction.DESC,"id");
        Pageable pageable= PageRequest.of(page,size,sort);
        Page<Object[]> orders=null;
        if(consumerName!=null){
            orders=ordersRepository.findOrdersByConsumerId(getConsumerId(consumerName),(Integer)request.getSession().getAttribute("stationId"),pageable);
        }else{
            orders=ordersRepository.findListByStationId((Integer)request.getSession().getAttribute("stationId"),pageable);
        }

        List<Object[]> listConsumerName=ordersRepository.findConsumerNames();
        List<String> list=new ArrayList<String>();
        for(int i=0;i<listConsumerName.size();i++){
            list.add(listConsumerName.get(i)[0].toString()+" "+listConsumerName.get(i)[1].toString());
            System.out.println(listConsumerName.get(i)[0].toString()+listConsumerName.get(i)[1].toString());
        }
        model.addAttribute("listConsumerName",list);
        model.addAttribute("orders",orders);
        return "orders/ordersChangePart";
    }

    @RequestMapping("/orderChangeTotal")
    public String orderChangeTotal(Model model,@RequestParam(value="page",defaultValue = "0")Integer page,
                                  @RequestParam(value = "size",defaultValue = "6")Integer size,HttpServletRequest request){
        Sort sort=new Sort(Sort.Direction.DESC,"id");
        Pageable pageable=PageRequest.of(page,size,sort);
        Page<Object[]> orders=ordersRepository.findListByStationId((Integer)request.getSession().getAttribute("stationId"),pageable);
        List<Object[]> listConsumerName=ordersRepository.findConsumerNames();
        List<String> list=new ArrayList<String>();
        for(int i=0;i<listConsumerName.size();i++){
            list.add(listConsumerName.get(i)[0].toString()+" "+listConsumerName.get(i)[1].toString());
            System.out.println(listConsumerName.get(i)[0].toString()+listConsumerName.get(i)[1].toString());
        }
        model.addAttribute("listConsumerName",list);
        model.addAttribute("orders",orders);
        return "orders/ordersChangeTotal";
    }

    @RequestMapping("/orderChangeAll")
    public String orderChangeAll(@RequestParam(value = "consumerName")String consumerName,@RequestParam(value = "address")String address,HttpServletRequest request,RedirectAttributes redirectAttributes,Model model){
        if(consumerName==null){
            String errorMsg="请输入查询用户姓名";
            model.addAttribute("errorMsg",errorMsg);
            Sort sort=new Sort(Sort.Direction.DESC,"id");
            Pageable pageable=PageRequest.of(0,6,sort);
            Page<Object[]> orders=ordersRepository.findListByStationId((Integer)request.getSession().getAttribute("stationId"),pageable);
            List<Object[]> listConsumerName=ordersRepository.findConsumerNames();
        List<String> list=new ArrayList<String>();
        for(int i=0;i<listConsumerName.size();i++){
            list.add(listConsumerName.get(i)[0].toString()+" "+listConsumerName.get(i)[1].toString());
            System.out.println(listConsumerName.get(i)[0].toString()+listConsumerName.get(i)[1].toString());
        }
        model.addAttribute("listConsumerName",list);
            model.addAttribute("orders",orders);
            return "orders/ordersChangeTotal";
        }
        List<Orders> ordersList=ordersRepository.findOrdersByConsumerId(getConsumerId(consumerName));
        int staffId=(Integer)request.getSession().getAttribute("staffId");
        for(Orders order:ordersList){
            if(order.getChange()==1){
                continue;
            }
            OrdersBackup ordersBackup=new OrdersBackup();
            saveBackupOrder(ordersBackup,order);
            order.setStatus(4);
            ordersBackup.setStatusNow(4);
            ordersBackupRepository.save(ordersBackup);
            order.setAddress(address);
            ordersRepository.save(order);
            OrderLog orderLog=new OrderLog();
            System.out.println("1111111111");
            saveOrderLog(orderLog,order,2,staffId);
            orderLogRepository.save(orderLog);
        }
        redirectAttributes.addAttribute("consumerName",consumerName);
        return "redirect:/orderChangePart";
    }

    @RequestMapping("/toChangeOrder")
    public String toChangeOrder(Model model,int id){
        Orders orders=ordersRepository.findById(id);
        List<Object[]> listConsumerName=ordersRepository.findConsumerNames();
        List<String> list=new ArrayList<String>();
        for(int i=0;i<listConsumerName.size();i++){
            list.add(listConsumerName.get(i)[0].toString()+" "+listConsumerName.get(i)[1].toString());
            System.out.println(listConsumerName.get(i)[0].toString()+listConsumerName.get(i)[1].toString());
        }
        model.addAttribute("listConsumerName",list);
        model.addAttribute("orders",orders);
        System.out.println("123123");
        model.addAttribute("consumerName",consumerRepository.findNameById(orders.getConsumerId()));
        System.out.println(consumerRepository.findNameById(id));
        return "orders/ordersChangeOne";
    }

    @RequestMapping("/changeOrderOne")
    public String changeOrderOne(@Valid OrdersParam ordersParam,BindingResult result,Model model,HttpServletRequest request,RedirectAttributes redirectAttributes){
        String errorMsg="";
        //参数校验
        List<Object[]> name=consumerRepository.findNameById(ordersParam.getConsumerId());
        String consumerName=name.get(0)[0].toString()+" "+name.get(0)[1].toString();
        if(result.hasErrors()){
            List<ObjectError> list=result.getAllErrors();
            for(ObjectError error:list){
                errorMsg=errorMsg+error.getDefaultMessage()+";";
            }
            model.addAttribute("errorMsg",errorMsg);
            model.addAttribute("orders",ordersParam);
            model.addAttribute("consumerName",consumerName);
            return "orders/ordersChangeOne";
        }
        Orders orders=ordersRepository.findById(ordersParam.getId().intValue());
        orders.setAddress(ordersParam.getAddress());
        OrdersBackup ordersBackup=new OrdersBackup();
        saveBackupOrder(ordersBackup,orders);
        orders.setStatus(4);
        ordersBackup.setStatusNow(4);
        ordersBackupRepository.save(ordersBackup);
        ordersRepository.save(orders);
        OrderLog orderLog=new OrderLog();
        saveOrderLog(orderLog,orders,2,(Integer)request.getSession().getAttribute("staffId"));
        orderLogRepository.save(orderLog);
        redirectAttributes.addAttribute("consumerName",consumerName);
        return "redirect:/orderChangePart";

    }
    @RequestMapping("/orderLonger")
    public String orderLonger(Model model, @RequestParam(value="page",defaultValue = "0")Integer page,
                              @RequestParam(value="size",defaultValue = "6")Integer size,HttpServletRequest request){
        Sort sort=new Sort(Sort.Direction.DESC,"id");
        Pageable pageable= PageRequest.of(page,size,sort);
        Page<Object[]> orders=ordersRepository.findListByStationId((Integer)request.getSession().getAttribute("stationId"),pageable);
        List<Object[]> listConsumerName=ordersRepository.findConsumerNames();
        List<String> list=new ArrayList<String>();
        for(int i=0;i<listConsumerName.size();i++){
            list.add(listConsumerName.get(i)[0].toString()+" "+listConsumerName.get(i)[1].toString());
            System.out.println(listConsumerName.get(i)[0].toString()+listConsumerName.get(i)[1].toString());
        }
        model.addAttribute("listConsumerName",list);
        model.addAttribute("orders",orders);
        return "orders/ordersLonger";
    }

    @RequestMapping("/orderLongerPart")
    public String orderLongerPart(Model model, @RequestParam(value="page",defaultValue = "0")Integer page,
                                  @RequestParam(value="size",defaultValue = "6")Integer size,
                                  @RequestParam(value = "consumerName")String consumerName,HttpServletRequest request){
        Sort sort=new Sort(Sort.Direction.DESC,"id");
        Pageable pageable= PageRequest.of(page,size,sort);
        Page<Object[]> orders=null;
        if(consumerName!=null){
            orders=ordersRepository.findOrdersByConsumerId(getConsumerId(consumerName),(Integer)request.getSession().getAttribute("stationId"),pageable);
        }else{
            orders=ordersRepository.findListByStationId((Integer)request.getSession().getAttribute("stationId"),pageable);
        }
        List<Object[]> listConsumerName=ordersRepository.findConsumerNames();
        List<String> list=new ArrayList<String>();
        for(int i=0;i<listConsumerName.size();i++){
            list.add(listConsumerName.get(i)[0].toString()+" "+listConsumerName.get(i)[1].toString());
            System.out.println(listConsumerName.get(i)[0].toString()+listConsumerName.get(i)[1].toString());
        }
        model.addAttribute("listConsumerName",list);
        model.addAttribute("orders",orders);
        return "orders/ordersLongerPart";
    }

    @RequestMapping("/orderLongerTotal")
    public String orderLongerTotal(Model model,@RequestParam(value="page",defaultValue = "0")Integer page,
                                   @RequestParam(value = "size",defaultValue = "6")Integer size,HttpServletRequest request){
        Sort sort=new Sort(Sort.Direction.DESC,"id");
        Pageable pageable=PageRequest.of(page,size,sort);
        Page<Object[]> orders=ordersRepository.findListByStationId((Integer)request.getSession().getAttribute("stationId"),pageable);
        List<Object[]> listConsumerName=ordersRepository.findConsumerNames();
        List<String> list=new ArrayList<String>();
        for(int i=0;i<listConsumerName.size();i++){
            list.add(listConsumerName.get(i)[0].toString()+" "+listConsumerName.get(i)[1].toString());
            System.out.println(listConsumerName.get(i)[0].toString()+listConsumerName.get(i)[1].toString());
        }
        model.addAttribute("listConsumerName",list);
        model.addAttribute("orders",orders);
        return "orders/ordersLongerTotal";
    }

    @RequestMapping("/orderLongerAll")
    public String orderLongerAll(Model model,HttpServletRequest request,@RequestParam(value = "consumerName")String consumerName,@RequestParam(value = "longerTime")int longerTime,RedirectAttributes redirectAttributes){
        if(consumerName==null){
            String errorMsg="请输入查询用户姓名";
            model.addAttribute("errorMsg",errorMsg);
            Sort sort=new Sort(Sort.Direction.DESC,"id");
            Pageable pageable=PageRequest.of(0,6,sort);
            Page<Object[]> orders=ordersRepository.findListByStationId((Integer)request.getSession().getAttribute("stationId"),pageable);
            List<Object[]> listConsumerName=ordersRepository.findConsumerNames();
        List<String> list=new ArrayList<String>();
        for(int i=0;i<listConsumerName.size();i++){
            list.add(listConsumerName.get(i)[0].toString()+" "+listConsumerName.get(i)[1].toString());
            System.out.println(listConsumerName.get(i)[0].toString()+listConsumerName.get(i)[1].toString());
        }
        model.addAttribute("listConsumerName",list);
            model.addAttribute("orders",orders);
            return "orders/ordersLongerTotal";
        }
        List<Orders> ordersList=ordersRepository.findOrdersByConsumerId(getConsumerId(consumerName));
        for(Orders order:ordersList){
            OrdersBackup ordersBackup=new OrdersBackup();
            saveBackupOrder(ordersBackup,order);
            int duration=order.getDuration();
            int longertime=longerTime;
            duration+=longertime;
            longertime*=7;
            double priceLonger=0;
            if(longertime<30){
                priceLonger=longertime*pressInfoRepository.findById(order.getPressId().intValue()).getDayPrice();
            }else if(longertime<90){
                priceLonger=longertime*pressInfoRepository.findById(order.getPressId().intValue()).getMonthPrice();
            }else if(longertime<180){
                priceLonger=longertime*pressInfoRepository.findById(order.getPressId().intValue()).getSessionPrice();
            }else if(longertime<365){
                priceLonger=longertime*pressInfoRepository.findById(order.getPressId().intValue()).getHalfYearPrice();
            }else{
                priceLonger=longertime*pressInfoRepository.findById(order.getPressId().intValue()).getYearPrice();
            }
            priceLonger*=order.getCopies();
            double price=order.getPrice();
            price+=priceLonger;
            order.setPrice(price);
            order.setDuration(duration);
            order.setPriceLonger(priceLonger);
            order.setStatus(6);
            ordersBackup.setStatusNow(6);
            long endTime=order.getEndDate().getTime()+(long) longertime*24*3600*1000;
            System.out.println(longerTime);
            Date newEndDate=new Date();
            newEndDate.setTime(endTime);
            order.setEndDate(newEndDate);
            ordersBackupRepository.save(ordersBackup);
            ordersRepository.save(order);
            OrderLog orderLog=new OrderLog();
            saveOrderLog(orderLog,order,3,(Integer)request.getSession().getAttribute("staffId"));
            orderLogRepository.save(orderLog);
        }
        redirectAttributes.addAttribute("consumerName",consumerName);
        return "redirect:/orderLongerPart";
    }

    @RequestMapping("/toLongerOrder")
    public String toLongerOrder(Model model,int id){
        Orders orders=ordersRepository.findById(id);
        List<Object[]> listConsumerName=ordersRepository.findConsumerNames();
        List<String> list=new ArrayList<String>();
        for(int i=0;i<listConsumerName.size();i++){
            list.add(listConsumerName.get(i)[0].toString()+" "+listConsumerName.get(i)[1].toString());
            System.out.println(listConsumerName.get(i)[0].toString()+listConsumerName.get(i)[1].toString());
        }
        model.addAttribute("listConsumerName",list);
        model.addAttribute("orders",orders);
        List<Object[]> name=consumerRepository.findNameById(orders.getConsumerId());
        String consumerName=name.get(0)[0].toString()+" "+name.get(0)[1].toString();
        model.addAttribute("consumerName",consumerName);
        return "orders/ordersLongerOne";
    }

    @RequestMapping("/longerOrderOne")
    public String longerOrderOne(HttpServletRequest request,@RequestParam(value = "longerTime")int longerTime,@Valid OrdersParam ordersParam,BindingResult result,Model model,RedirectAttributes redirectAttributes){
        String errorMsg="";
        //参数校验
        if(result.hasErrors()){
            List<ObjectError> list=result.getAllErrors();
            for(ObjectError error:list){
                errorMsg=errorMsg+error.getDefaultMessage()+";";
            }
            model.addAttribute("errorMsg",errorMsg);
            model.addAttribute("orders",ordersParam);
            return "orders/ordersLongerOne";
        }
        Orders orders=ordersRepository.findById(ordersParam.getId().intValue());
        OrdersBackup ordersBackup=new OrdersBackup();
        saveBackupOrder(ordersBackup,orders);
        //续订订单时间累加,续订订单价格从零开始分等级计算
        int duration=orders.getDuration();
        int longertime=longerTime;
        duration+=longertime;
        longertime=longertime*7;
        double priceLonger=0;
        if(longertime<30){
            priceLonger=longertime*pressInfoRepository.findById(ordersParam.getPressId().intValue()).getDayPrice();
        }else if(longertime<90){
            priceLonger=longertime*pressInfoRepository.findById(ordersParam.getPressId().intValue()).getMonthPrice();
        }else if(longertime<180){
            priceLonger=longertime*pressInfoRepository.findById(ordersParam.getPressId().intValue()).getSessionPrice();
        }else if(longertime<365){
            priceLonger=longertime*pressInfoRepository.findById(ordersParam.getPressId().intValue()).getHalfYearPrice();
        }else{
            priceLonger=longertime*pressInfoRepository.findById(ordersParam.getPressId().intValue()).getYearPrice();
        }
        priceLonger*=ordersParam.getCopies();
        long endTime=orders.getEndDate().getTime()+(long) longertime*24*3600*1000;
        System.out.println(longerTime);
        System.out.println("元结束时间"+orders.getEndDate().getTime());
        System.out.println("现结束时间"+endTime);
        Date newEndDate=new Date();
        newEndDate.setTime(endTime);
        orders.setEndDate(newEndDate);
        double price=ordersParam.getPrice();
        price+=priceLonger;
        orders.setPrice(price);
        orders.setDuration(duration);
        orders.setPriceLonger(priceLonger);
        orders.setStatus(6);
        ordersBackup.setStatusNow(6);
        ordersBackupRepository.save(ordersBackup);
        ordersRepository.save(orders);
        OrderLog orderLog=new OrderLog();
        saveOrderLog(orderLog,orders,3,(Integer)request.getSession().getAttribute("staffId"));
        orderLogRepository.save(orderLog);
        List<Object[]> name=consumerRepository.findNameById(orders.getConsumerId());
        String consumerName=name.get(0)[0].toString()+" "+name.get(0)[1].toString();
        redirectAttributes.addAttribute("consumerName",consumerName);
        return "redirect:/orderLongerPart";

    }

    @RequestMapping("/orderPostpone")
    public String orderPostpone(Model model, @RequestParam(value="page",defaultValue = "0")Integer page,
                              @RequestParam(value="size",defaultValue = "6")Integer size,HttpServletRequest request){
        Sort sort=new Sort(Sort.Direction.DESC,"id");
        Pageable pageable= PageRequest.of(page,size,sort);
        Page<Object[]> orders=ordersRepository.findListByStationId((Integer)request.getSession().getAttribute("stationId"),pageable);
        List<Object[]> listConsumerName=ordersRepository.findConsumerNames();
        List<String> list=new ArrayList<String>();
        for(int i=0;i<listConsumerName.size();i++){
            list.add(listConsumerName.get(i)[0].toString()+" "+listConsumerName.get(i)[1].toString());
            System.out.println(listConsumerName.get(i)[0].toString()+listConsumerName.get(i)[1].toString());
        }
        model.addAttribute("listConsumerName",list);
        model.addAttribute("orders",orders);
        return "orders/ordersPostPone";
    }

    @RequestMapping("/orderPostponePart")
    public String orderPostponePart(Model model, @RequestParam(value="page",defaultValue = "0")Integer page,
                                  @RequestParam(value="size",defaultValue = "6")Integer size,
                                  @RequestParam(value = "consumerName")String consumerName,HttpServletRequest request){
        Sort sort=new Sort(Sort.Direction.DESC,"id");
        Pageable pageable= PageRequest.of(page,size,sort);
        Page<Object[]> orders=null;
        if(consumerName!=null){
            orders=ordersRepository.findOrdersByConsumerId(getConsumerId(consumerName),(Integer)request.getSession().getAttribute("stationId"),pageable);
        }else{
            orders=ordersRepository.findListByStationId((Integer)request.getSession().getAttribute("stationId"),pageable);
        }

        List<Object[]> listConsumerName=ordersRepository.findConsumerNames();
        List<String> list=new ArrayList<String>();
        for(int i=0;i<listConsumerName.size();i++){
            list.add(listConsumerName.get(i)[0].toString()+" "+listConsumerName.get(i)[1].toString());
            System.out.println(listConsumerName.get(i)[0].toString()+listConsumerName.get(i)[1].toString());
        }
        model.addAttribute("listConsumerName",list);
        model.addAttribute("orders",orders);
        return "orders/ordersPostponePart";
    }

    @RequestMapping("/orderPostponeTotal")
    public String orderPostponeTotal(Model model,@RequestParam(value="page",defaultValue = "0")Integer page,
                                   @RequestParam(value = "size",defaultValue = "6")Integer size,HttpServletRequest request){
        Sort sort=new Sort(Sort.Direction.DESC,"id");
        Pageable pageable=PageRequest.of(page,size,sort);
        Page<Object[]> orders=ordersRepository.findListByStationId((Integer)request.getSession().getAttribute("stationId"),pageable);
        List<Object[]> listConsumerName=ordersRepository.findConsumerNames();
        List<String> list=new ArrayList<String>();
        for(int i=0;i<listConsumerName.size();i++){
            list.add(listConsumerName.get(i)[0].toString()+" "+listConsumerName.get(i)[1].toString());
            System.out.println(listConsumerName.get(i)[0].toString()+listConsumerName.get(i)[1].toString());
        }
        model.addAttribute("listConsumerName",list);
        model.addAttribute("orders",orders);
        return "orders/ordersPostponeTotal";
    }

    @RequestMapping("/orderPostponeAll")
    public String orderPostponeAll(Model model,HttpServletRequest request,@RequestParam(value = "consumerName")String consumerName,@RequestParam(value = "postponeTime")int postponeTime,RedirectAttributes redirectAttributes){
        if(consumerName==null){
            String errorMsg="请输入查询用户姓名";
            model.addAttribute("errorMsg",errorMsg);
            Sort sort=new Sort(Sort.Direction.DESC,"id");
            Pageable pageable=PageRequest.of(0,6,sort);
            Page<Object[]> orders=ordersRepository.findListByStationId((Integer)request.getSession().getAttribute("stationId"),pageable);
            List<Object[]> listConsumerName=ordersRepository.findConsumerNames();
        List<String> list=new ArrayList<String>();
        for(int i=0;i<listConsumerName.size();i++){
            list.add(listConsumerName.get(i)[0].toString()+" "+listConsumerName.get(i)[1].toString());
            System.out.println(listConsumerName.get(i)[0].toString()+listConsumerName.get(i)[1].toString());
        }
        model.addAttribute("listConsumerName",list);
            model.addAttribute("orders",orders);
            return "orders/ordersPostponeTotal";
        }
        List<Orders> ordersList=ordersRepository.findOrdersByConsumerId(getConsumerId(consumerName));
        long postpone=(long)postponeTime*7*24*3600*1000;
        for(Orders order:ordersList){
            OrdersBackup ordersBackup=new OrdersBackup();
            saveBackupOrder(ordersBackup,order);
            Date endDate=order.getEndDate();
            endDate.setTime(endDate.getTime()+postpone);
            order.setEndDate(endDate);
            order.setStatus(5);
            ordersBackup.setStatusNow(5);
            ordersBackupRepository.save(ordersBackup);
            ordersRepository.save(order);
            OrderLog orderLog=new OrderLog();
            saveOrderLog(orderLog,order,4,(Integer)request.getSession().getAttribute("staffId"));
            orderLogRepository.save(orderLog);
        }
        redirectAttributes.addAttribute("consumerName",consumerName);
        return "redirect:/orderPostponePart";
    }

    @RequestMapping("/toPostponeOrder")
    public String toPostponeOrder(Model model,int id){
        Orders orders=ordersRepository.findById(id);
        List<Object[]> listConsumerName=ordersRepository.findConsumerNames();
        List<String> list=new ArrayList<String>();
        for(int i=0;i<listConsumerName.size();i++){
            list.add(listConsumerName.get(i)[0].toString()+" "+listConsumerName.get(i)[1].toString());
            System.out.println(listConsumerName.get(i)[0].toString()+listConsumerName.get(i)[1].toString());
        }
        model.addAttribute("listConsumerName",list);
        model.addAttribute("orders",orders);
        return "orders/ordersPostponeOne";
    }

    @RequestMapping("/postponeOrderOne")
    public String postponeOrderOne(HttpServletRequest request,@RequestParam(value = "id")Integer id
            ,@RequestParam(value = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate
            ,@RequestParam(value = "endDate")@DateTimeFormat(pattern = "yyyy-MM-dd")Date endDate
            ,@RequestParam(value = "postponeStart")@DateTimeFormat(pattern = "yyyy-MM-dd")Date postponeStart
            ,@DateTimeFormat(pattern = "yyyy-MM-dd")@RequestParam(value = "postponeEnd")Date postponeEnd
            ,Model model,RedirectAttributes redirectAttributes){
        Orders orders=ordersRepository.findById(id.intValue());
        List<Object[]> name=consumerRepository.findNameById(orders.getConsumerId());
        String consumerName=name.get(0)[0].toString()+" "+name.get(0)[1].toString();
        String errorMsg="";
        if(postponeStart.before(startDate)){
            errorMsg+="延期开始日期不能在订单开始日期之前";
            model.addAttribute("errorMsg",errorMsg);
            List<Object[]> listConsumerName=ordersRepository.findConsumerNames();
        List<String> list=new ArrayList<String>();
        for(int i=0;i<listConsumerName.size();i++){
            list.add(listConsumerName.get(i)[0].toString()+" "+listConsumerName.get(i)[1].toString());
            System.out.println(listConsumerName.get(i)[0].toString()+listConsumerName.get(i)[1].toString());
        }
        model.addAttribute("listConsumerName",list);
            model.addAttribute("orders",orders);
            model.addAttribute("consumerName");
            return "orders/ordersPostponeOne";
        }
        if(postponeStart.after(endDate)){
            errorMsg+="延期开始日期不能在订单结束日期之后";
            model.addAttribute("errorMsg",errorMsg);
            List<Object[]> listConsumerName=ordersRepository.findConsumerNames();
        List<String> list=new ArrayList<String>();
        for(int i=0;i<listConsumerName.size();i++){
            list.add(listConsumerName.get(i)[0].toString()+" "+listConsumerName.get(i)[1].toString());
            System.out.println(listConsumerName.get(i)[0].toString()+listConsumerName.get(i)[1].toString());
        }
        model.addAttribute("listConsumerName",list);
            model.addAttribute("orders",orders);
            model.addAttribute("consumerName");
            return "orders/ordersPostponeOne";
        }
        if(postponeEnd.before(postponeStart)){
            errorMsg+="延期结束日期不能在延期开始日期之前";
            model.addAttribute("errorMsg",errorMsg);
            List<Object[]> listConsumerName=ordersRepository.findConsumerNames();
        List<String> list=new ArrayList<String>();
        for(int i=0;i<listConsumerName.size();i++){
            list.add(listConsumerName.get(i)[0].toString()+" "+listConsumerName.get(i)[1].toString());
            System.out.println(listConsumerName.get(i)[0].toString()+listConsumerName.get(i)[1].toString());
        }
        model.addAttribute("listConsumerName",list);
            model.addAttribute("orders",orders);
            model.addAttribute("consumerName");
            return "orders/ordersPostponeOne";
        }
        OrdersBackup ordersBackup=new OrdersBackup();
        saveBackupOrder(ordersBackup,orders);
        long startTime=startDate.getTime();
        System.out.println("endTime1="+endDate.getTime());
        int duration=orders.getDuration();
        System.out.println("duration="+duration);
        long leftTime=endDate.getTime()-postponeStart.getTime();
        System.out.println("leftTime="+leftTime);
        long endTime=postponeEnd.getTime()+leftTime;
        System.out.println("endTime2="+endTime);
        endDate.setTime(endTime);
        orders.setEndDate(endDate);
        orders.setStatus(5);
        ordersBackup.setStatusNow(5);
        orders.setPostponeStart(postponeStart);
        orders.setPostponeEnd(postponeEnd);
        ordersBackupRepository.save(ordersBackup);
        ordersRepository.save(orders);
        OrderLog orderLog=new OrderLog();
        saveOrderLog(orderLog,orders,4,(Integer)request.getSession().getAttribute("staffId"));
        orderLogRepository.save(orderLog);
        redirectAttributes.addAttribute("consumerName",consumerName);
        return "redirect:/orderPostponePart";

    }

    @RequestMapping("toOrderFuzeng")
    public String toOrderFuzeng(Model model){
        List<Zenka> zenkas=zenkaRepository.findList();
        model.addAttribute("zenkas",zenkas);
        List<Object[]> listConsumerName=ordersRepository.findConsumerNames();
        List<String> list=new ArrayList<String>();
        for(int i=0;i<listConsumerName.size();i++){
            list.add(listConsumerName.get(i)[0].toString()+" "+listConsumerName.get(i)[1].toString());
            System.out.println(listConsumerName.get(i)[0].toString()+listConsumerName.get(i)[1].toString());
        }
        model.addAttribute("listConsumerName",list);
        return "orders/ordersFuzeng";
    }

    @RequestMapping("/orderFuzeng")
    public String orderFuzeng(@RequestParam(value = "consumerName")String consumerName,
                              @RequestParam(value = "zenkaId")int zenkaId,
                              Model model){
        System.out.println("zenkaId=="+zenkaId);
        Consumer consumer=consumerRepository.findById(getConsumerId(consumerName));
        if(consumer==null){
            String errorMsg="不存在此用户";
            model.addAttribute("errorMsg",errorMsg);
            List<Zenka> zenkas=zenkaRepository.findList();
            model.addAttribute("zenkas",zenkas);
            return "orders/ordersFuzeng";
        }
        System.out.println(1);
        consumer.setZenkaId(zenkaId);
        System.out.println(2);
        consumerRepository.save(consumer);
        return "redirect:/consumerList";
    }

    @RequestMapping("/ordersAlter")
    public String ordersAlter(Model model){
        List<Object[]> listConsumerName=ordersRepository.findConsumerNames();
        List<String> list=new ArrayList<String>();
        for(int i=0;i<listConsumerName.size();i++){
            list.add(listConsumerName.get(i)[0].toString()+" "+listConsumerName.get(i)[1].toString());
            System.out.println(listConsumerName.get(i)[0].toString()+listConsumerName.get(i)[1].toString());
        }
        model.addAttribute("listConsumerName",list);
        return "orders/ordersAlter";
    }

    @RequestMapping("/ordersAlterDo")
    public String ordersAlter(@RequestParam(value = "consumerName")String consumerName,
                              @RequestParam(value = "status")Integer status,
                              Model model,@RequestParam(value="page",defaultValue = "0")Integer page,
                              @RequestParam(value = "size",defaultValue = "6")Integer size,
                              HttpServletRequest request){
        int stationId=(Integer)request.getSession().getAttribute("stationId");
        Sort sort=new Sort(Sort.Direction.DESC,"id");
        Pageable pageable=PageRequest.of(page,size,sort);
        Page<Object[]> orders=null;
        if(!consumerName.equals("")&&status!=null){
            orders=ordersRepository.findByConsumerIdAndStatus(getConsumerId(consumerName),status,stationId,pageable);
        }else if(consumerName.equals("")&&status!=null){
            orders=ordersRepository.findByStatus(status,stationId,pageable);
        }else{
            String errorMsg="变更条件不能为空";
            model.addAttribute("errorMsg",errorMsg);
        }
        List<Object[]> listConsumerName=ordersRepository.findConsumerNames();
        List<String> list=new ArrayList<String>();
        for(int i=0;i<listConsumerName.size();i++){
            list.add(listConsumerName.get(i)[0].toString()+" "+listConsumerName.get(i)[1].toString());
            System.out.println(listConsumerName.get(i)[0].toString()+listConsumerName.get(i)[1].toString());
        }
        model.addAttribute("listConsumerName",list);
        model.addAttribute("orders",orders);
        return "orders/ordersAlterDo";
    }

    @RequestMapping("/ordersAlterImpl")
    public String ordersAlterImpl(Model model,Integer id,String consumerName,Integer status,HttpServletRequest request,RedirectAttributes redirectAttributes){
        Orders orders=ordersRepository.findById(id.intValue());
        OrdersBackup ordersBackup=ordersBackupRepository.findById(id.intValue());
        orders.setDuration(ordersBackup.getDuration());
        orders.setAddress(ordersBackup.getAddress());
        orders.setEndDate(ordersBackup.getEndDate());
        orders.setStatus(ordersBackup.getStatus());
        orders.setPrice(ordersBackup.getPrice());
        orders.setUnsubReason(-1);
        ordersRepository.save(orders);
        if(consumerName==null){
            System.out.println("consumerName为空");
            return "redirect:/ordersAlterDo?consumerName=&status="+status.toString();
        }
        System.out.println("consumerName"+consumerName);
        OrderLog orderLog=new OrderLog();
        saveOrderLog(orderLog,orders,5,(Integer)request.getSession().getAttribute("staffId"));
        orderLogRepository.save(orderLog);
        redirectAttributes.addAttribute("consumerName",consumerName);
        redirectAttributes.addAttribute("status",status);
        return "redirect:/ordersAlterDo";
    }


    @RequestMapping("/toCaiwu")
    public String toCaiwu(@RequestParam(value="page",defaultValue = "0")Integer page,
                          @RequestParam(value="size",defaultValue = "6")Integer size,
                          Model model){
        Pageable pageable= PageRequest.of(page,size);
        Page<Object[]> orders=ordersRepository.findallorderstationname(pageable);
        List<String> listConsumerName=removeDuplicate(ordersRepository.findConsumerNames());
        List<String> listStationName=ordersRepository.findstationname();
        model.addAttribute("listStationName",listStationName);
        model.addAttribute("listConsumerName",listConsumerName);
        model.addAttribute("orders",orders);
        return "Order/orderStu";
    }


    @RequestMapping("/findallorder")
    public String findallorder(Model model, @RequestParam(value="page",defaultValue = "0")Integer page,
                               @RequestParam(value="size",defaultValue = "6")Integer size){
        Pageable pageable= PageRequest.of(page,size);
        Page<Object[]> orders=ordersRepository.findallorderstationname(pageable);
        List<String> listConsumerName=removeDuplicate(ordersRepository.findConsumerNames());
        List<String> listStationName=ordersRepository.findstationname();
        model.addAttribute("listStationName",listStationName);
        model.addAttribute("listConsumerName",listConsumerName);
        model.addAttribute("orders",orders);
        return "Order/orderStu";
    }
    @RequestMapping("/findorderofstation")
    public String findorderofstation(@RequestParam(value="page",defaultValue = "0")Integer page,
                                     @RequestParam(value="size",defaultValue = "6")Integer size,
                                     @RequestParam(value="id",defaultValue = "0")String Id, Model model){
        System.out.println("传入分站ID：");
        System.out.println(Id);

        Pageable pageable= PageRequest.of(page,size);
        Page<Object[]> orders=ordersRepository.findAllByStationId(Id,pageable);
        List<String> listConsumerName=removeDuplicate(ordersRepository.findConsumerNames());
        List<String> listStationName=ordersRepository.findstationname();
        model.addAttribute("listStationName",listStationName);
        model.addAttribute("listConsumerName",listConsumerName);
        model.addAttribute("orders",orders);
        return "Order/orderStu";
    }
}
