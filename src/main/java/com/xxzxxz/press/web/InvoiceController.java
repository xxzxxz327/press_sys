package com.xxzxxz.press.web;

import com.xxzxxz.press.model.Invoice;
import com.xxzxxz.press.model.Orders;
import com.xxzxxz.press.repository.InvoiceRepository;
import com.xxzxxz.press.repository.OrdersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.HttpMediaTypeException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.persistence.criteria.CriteriaBuilder;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@Controller
public class InvoiceController {
    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private OrdersRepository ordersRepository;

    @RequestMapping("/setinvoice")
    public String setinvoice(){
        return "invoice/setInvoice";
    }

    @RequestMapping("/sendInvoice")
    public String sendInvoice(){
        return "invoice/sendInvoice";
    }

    @RequestMapping("/setoneinvoice")
    public String setoneinvoice(@RequestParam(value="num",defaultValue = "1")Integer Num, @RequestParam(value="type",defaultValue = "0")Integer Type, Model model){

        int num=Num.intValue();
        int type=Type.intValue();


        for(int cot=0;cot<num;cot++)
        {
            Invoice i=new Invoice();
            i.setType(type);
            i.setStatus(0);
            try {
                invoiceRepository.save(i);
            }catch(Exception e){
                System.out.println(i.getId());
                System.out.println(i.getType());
                System.out.println("这里出错了");
            }
        }
        return "invoice/setInvoice";
    }
    @RequestMapping("/sendinvoice")
    public String sendinvoice(@RequestParam(value="num",defaultValue = "0")Integer Num,
                              @RequestParam(value="id",defaultValue = "0") Integer Id,
                              @RequestParam(value="type",defaultValue = "0")Integer Type,
                              @RequestParam(value="page",defaultValue = "0")Integer page,
                              @RequestParam(value="size",defaultValue = "6")Integer size,
                              Model model)
    {
        int num = Num.intValue();
        int id = Id.intValue();
        int type = Type.intValue();
        String errorMsg = "";
        System.out.println(type);
        Pageable pageable= PageRequest.of(page,size);
        System.out.println(1);
        Page<Object[]> stations=invoiceRepository.getstationinvoice(pageable);
        System.out.println(2);
        System.out.println(stations.getSize());
        model.addAttribute("stations",stations);
        if(invoiceRepository.findinvoicenum(type) < num)
        {
            model.addAttribute("errorMsg", "发票数量不足");
            return "invoice/sendInvoice";
        }
        for(int cot=0; cot< num; cot++)
        {
            Invoice i =invoiceRepository.getoneinvoice(type);
            i.setStationId(id);
            invoiceRepository.save(i);
        }
        return "invoice/sendInvoice";
    }
    @RequestMapping("/displayinvoice")
    public String displayinvoice(Model model, @RequestParam(value="page",defaultValue = "0")Integer page,
                                 @RequestParam(value="size",defaultValue = "6")Integer size){
        Pageable pageable= PageRequest.of(page,size);
        Page<Object[]> stations=invoiceRepository.getstationinvoice(pageable);
        System.out.println(stations.getSize());
        model.addAttribute("stations",stations);
        return "invoice/sendInvoice";
    }
    @RequestMapping("/findinvoicebyid")
    public String findinvoicebyid(@RequestParam(value="page",defaultValue = "0")Integer page,
                                  @RequestParam(value="size",defaultValue = "6")Integer size,
                                  @RequestParam(value="id",defaultValue = "0")int Id, Model model){


        Pageable pageable= PageRequest.of(page,size);
        Page<Object[]> invoice=invoiceRepository.findById(Id,pageable);

        List<Object[]> inv=invoiceRepository.findById1(Id);
        if(inv.size()!=0){
            Object[] a=inv.get(0);
            model.addAttribute("id",a[0]);
            if((Integer) a[1]==1)
            {
                model.addAttribute("status",a[1].toString());
                String name=a[2].toString();
                model.addAttribute("name",name);
                System.out.println("客户姓名"+name);
                model.addAttribute("price",a[3].toString());
                model.addAttribute("phone",a[4].toString());
                model.addAttribute("priceCN",Data2Zh.rmbChange(a[3].toString()));
                System.out.println(Data2Zh.rmbChange(a[3].toString()));
            }else{
                System.out.println(a[1].toString());
                model.addAttribute("status",a[1].toString());
                model.addAttribute("name","1");
                model.addAttribute("price","1");
                model.addAttribute("phone","1");
                model.addAttribute("priceCN"," ");
            }
        }else{
            model.addAttribute("id"," ");
        }
        Date date=new Date();
        model.addAttribute("now",date);

        System.out.println("111111111111");
        model.addAttribute("orders",invoice);
        return "invoice/searchInvoice";
    }

    @RequestMapping("/tobackinvoice")
    public  String tobackinvoice (@RequestParam(value="page",defaultValue = "0")Integer page,
                                  @RequestParam(value="size",defaultValue = "6")Integer size,
                                  Model model){
        Pageable pageable= PageRequest.of(page,size);
        Page<Object[]> invoices=invoiceRepository.getbackinvoice(pageable);
        model.addAttribute("invoices",invoices);
        return "invoice/backInvoice";}

    @RequestMapping("/backinvoice")
    public String backinvoice(@RequestParam(value="id") Integer Id,

                              @RequestParam(value="page",defaultValue = "0")Integer page,
                              @RequestParam(value="size",defaultValue = "6")Integer size,
                              Model model, RedirectAttributes redirectAttributes)
    {

        String errorMsg = "";
        if(Id==null){
            errorMsg="请输入ID";
            return "redirect:/tobackinvoice";
        }
        int id = Id.intValue();
        Invoice i = invoiceRepository.findById(id);

        if(i==null||i.getStationId()==null)
        {
            redirectAttributes.addAttribute("errorMsg", "发票不存在");
            return "redirect:/tobackinvoice";
        }
        i.setStatus(4);
        try {
            invoiceRepository.save(i);
        }catch(Exception e){

            System.out.println("这里出错了");
        }
        Pageable pageable= PageRequest.of(page,size);
        Page<Object[]> invoices=invoiceRepository.getbackinvoice(pageable);
        model.addAttribute("invoices",invoices);
        return "invoice/backInvoice";
    }


    @RequestMapping("/invoiceList")
    public String list(Model model, @RequestParam(value="page",defaultValue = "0")Integer page,
                       @RequestParam(value="size",defaultValue = "6")Integer size, HttpServletRequest request){
        Pageable pageable= PageRequest.of(page,size);
        int stationId=(Integer)request.getSession().getAttribute("stationId");
        Page<Object[]> invoices=invoiceRepository.findListByStationId(stationId,pageable);
        model.addAttribute("invoices",invoices);
        return "invoice/invoiceList";
    }

    @RequestMapping("/invoiceOne")
    public String list(Model model, @RequestParam(value="page",defaultValue = "0")Integer page,
                       @RequestParam(value="size",defaultValue = "6")Integer size,
                        @RequestParam(value="status")Integer status,HttpServletRequest request){
        int stationId=(Integer)request.getSession().getAttribute("stationId");
        Pageable pageable= PageRequest.of(page,size);
        Page<Object[]> invoices=invoiceRepository.findListByStationIdAndStatus(stationId,status.intValue(),pageable);
        model.addAttribute("invoices",invoices);
        return "invoice/invoiceList";
    }

    @RequestMapping("/toEditInvoice")
    public String toEditInvoice(Model model,int id,int stationId){
        model.addAttribute("invoiceId",id);
        model.addAttribute("stationId",stationId);
        return "invoice/invoiceEdit";
    }

    @RequestMapping("/invoiceEditOne")
    public String invoiceEdit(Model model,@RequestParam(value = "orderId")Integer orderId,
                              @RequestParam(value = "status")int status,
                              @RequestParam(value = "stationId")int stationId,
                              @RequestParam(value = "invoiceId")int invoiceId){
        String errorMsg="";
        if(orderId==null){
            errorMsg+="订单id不能为空，请重新输入";
            model.addAttribute("errorMsg",errorMsg);
            model.addAttribute("invoiceId",invoiceId);
            model.addAttribute("stationId",stationId);
            return "invoice/invoiceEdit";
        }
        Orders orders=ordersRepository.findById(orderId.intValue());
        if(orders==null){
            errorMsg+="不存在这个订单,请重新输入";
            model.addAttribute("errorMsg",errorMsg);
            model.addAttribute("invoiceId",invoiceId);
            model.addAttribute("stationId",stationId);
            return "invoice/invoiceEdit";
        }
        Invoice invoice=invoiceRepository.findById(invoiceId);
        invoice.setOrderId(orderId);
        invoice.setStatus(status);
        invoiceRepository.save(invoice);
        return "redirect:/invoiceList";
    }

    public static class Data2Zh {
        final static private String NUMBER[] = {"零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖"};
        final static private String NUMBER2[] = {"〇", "一", "二", "三", "四", "五", "六", "七", "八", "九"};
        final static private String CBit[] = {"", "拾", "佰", "仟"};

        /*
         * 将数值大写
         */
        public static String capitalization(String szNum) {
            StringBuilder resstr = new StringBuilder();
            String tmpstr = szNum.trim();
            int sl = tmpstr.length();
            int sp = 0;
            int dotpos = tmpstr.indexOf('.');
            if (dotpos != -1) {
                while (sl > 1 && tmpstr.charAt(sl - 1) == '0')
                    sl--;
                if (tmpstr.charAt(sl - 1) == '.')
                    sl--;
                if (sl != tmpstr.length()) {
                    tmpstr = tmpstr.substring(0, sl);
                }
            } else
                dotpos = sl;
            if (sl < 1)
                return NUMBER[0];
            if (tmpstr.charAt(0) == '-') {
                resstr.append("负");
                sp = 1;
            }
            String integerNum = tmpstr.substring(sp, dotpos - sp);
            String decimalNum = "";
            if (dotpos + 1 < sl)
                decimalNum = tmpstr.substring(dotpos + 1);
            sl = integerNum.length();
            sp = 0;
            while (sp < sl && integerNum.charAt(sp) == '0')
                sp++;
            if (sp > 0)
                integerNum = integerNum.substring(sp);
            int inl = integerNum.length();
            if (inl > 0) {
                int h = (inl - 1) % 4;
                int j = (inl - 1) / 4 + 1;
                sp = 0;
                boolean allzero = false;
                boolean preallzero = false;
                for (; j > 0; j--) {
                    int k = h;
                    h = 3;
                    boolean preiszero = allzero;
                    allzero = true;
                    for (; k >= 0; k--, sp++) {
                        if (integerNum.charAt(sp) == '0')
                            preiszero = true;
                        else {
                            allzero = false;
                            if (preiszero)
                                resstr.append("零");
                            preiszero = false;
                            resstr.append(NUMBER[(byte) (integerNum.charAt(sp)) - 48]).append(CBit[k]);
                        }
                    } // end for k
                    if (/* j!=0 && */ j % 2 == 0) {
                        if (!allzero)
                            resstr.append("万");
                    } else {
                        if (!allzero || !preallzero) {
                            int repyi = j / 2;
                            for (int i = 0; i < repyi; i++)
                                resstr.append("亿");
                        }
                    }
                    preallzero = allzero;
                } // end for j
            } else
                resstr.append("零");

            int dnl = decimalNum.length();
            if (dnl > 0) {
                resstr.append("点");
                for (int i = 0; i < dnl; i++) {
                    resstr.append(NUMBER[(byte) (decimalNum.charAt(i)) - 48]);
                }
            }
            return resstr.toString();
        }

        /*
         * 获得某一位上的数值，如果 nBit<0 则获得小数点后面的位数
         */
        static public char getNumByte(String szNum, int nBit) {
            int sl = szNum.length();
            int nPos = 0;
            while (nPos < sl && szNum.charAt(nPos) != '.')
                nPos++;
            if (nBit < 0)
                nPos = nPos - nBit;
            else
                nPos = nPos - nBit - 1;
            if (nPos < 0 || nPos >= sl)
                return '0';
            return szNum.charAt(nPos);
        }

        public static String rmbChange(String rmb) {
            return capitalization((rmb.indexOf('.') >= 0 ? rmb.substring(0, rmb.indexOf('.')) : rmb)) + "元"
                    + capitalization(String.valueOf(getNumByte(rmb, -1))) + "角"
                    + capitalization(String.valueOf(getNumByte(rmb, -2))) + "分";
        }

        /*
         * 仅仅是把 0~9 转换为 "〇","一","二","三","四","五","六","七","八","九"
         */
        public static String changeCN(String szNum) {
            StringBuilder sb = new StringBuilder();
            String str = szNum.trim();
            int sl = str.length();
            int sp = 0;

            if (sl < 1)
                return NUMBER2[0];
            for (; sp < sl; sp++)
                if (str.charAt(sp) >= '0' && str.charAt(sp) <= '9')
                    sb.append(NUMBER2[str.charAt(sp) - '0']);
                else
                    sb.append(str.charAt(sp));
            return sb.toString();
        }
    }


}
