package com.xxzxxz.press.web;

import com.xxzxxz.press.model.PageInfo;
import com.xxzxxz.press.model.PrintSend;
import com.xxzxxz.press.model.SendStation;
import com.xxzxxz.press.param.PrintSendParam;
import com.xxzxxz.press.param.SendParam;
import com.xxzxxz.press.repository.PageInfoRepository;
import com.xxzxxz.press.repository.PrintSendRepository;
import com.xxzxxz.press.repository.SendRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Controller
public class countCtrl {
    @Autowired
    private PageInfoRepository pageInfoRepository;
    @RequestMapping("/pageInfo")
    public String pageInfo(Model model, @RequestParam(value="page",defaultValue = "0")Integer page,
                            @RequestParam(value="size",defaultValue = "6")Integer size){
        Sort sort=new Sort(Sort.Direction.DESC,"id");
        Pageable pageable= PageRequest.of(page,size,sort);
        Page<PageInfo> pageInfos=pageInfoRepository.findListToday(pageable);
        model.addAttribute("pageInfos",pageInfos);
        return "count/pageInfo";
    }
    @Autowired
    private SendRepository sendRepository;

    @RequestMapping("/specialOne")
    public String findOne(Model model, @RequestParam(value = "id")Integer id, RedirectAttributes ra){
        if (id==null){
            String errorMsg="id不能为空";
            ra.addAttribute("errorMsg",errorMsg);
            System.out.println(222);
            return"redirect:/special";
        }
//  Consumer consumers=consumerRepository.findById(id);
        List<Object[]> specials = sendRepository.findById2(id.intValue());
        model.addAttribute("specials",specials);
        return "count/special";
    }
    @RequestMapping("/special")
    public String speciallist(Model model, @RequestParam(value="page",defaultValue = "0")Integer page,
                            @RequestParam(value="size",defaultValue = "6")Integer size){
        Sort sort=new Sort(Sort.Direction.DESC,"id");
        Pageable pageable= PageRequest.of(page,size,sort);
        Page<Object[]> specials=sendRepository.findSpeList(pageable);

        if(specials.getNumberOfElements()==1){
            if(specials.getContent().get(0)[0]==null){
                model.addAttribute("objectlist",null);
                return "count/special";
            }
        }

        /*List<Object[]> list=new ArrayList<Object[]>() ;
        for (int i=0;i<specials.getContent().size();i++){



            if(specials.getContent().get(i)[0]!=null){
                list.add(specials.getContent().get(i));
            }
        }
        Page<Object[]> sl=new PageImpl<Object[]>(list,pageable);*/
        model.addAttribute("objectlist",specials);
        return "count/special";
    }

    @RequestMapping("/specialadd")
    public String specialadd(@Valid SendParam sendParam, BindingResult result, Model model){
        String errorMsg="";
        //参数校验
        if(result.hasErrors()){
            List<ObjectError> list=result.getAllErrors();
            for(ObjectError error:list){
                errorMsg=errorMsg+error.getCode()+"-"+error.getDefaultMessage()+";";
            }
            model.addAttribute("errorMsg",errorMsg);
            return "count/specialadd";
        }
        SendStation sendStation = new SendStation();
        BeanUtils.copyProperties(sendParam,sendStation);
        sendStation.setType(2);
        sendRepository.save(sendStation);
        System.out.println(111);
        System.out.println(sendStation.getId());
        return "redirect:/special";
    }
    //
    @RequestMapping("/specialdelete")
    public String specialdelete(int id){
        sendRepository.deleteById(id);
        return "redirect:/special";
    }
    @RequestMapping("/tospecialedit")
    public String tospecialedit(Model model,int id){
//        Consumer consumer=consumerRepository.findById(id);
        List<Object[]> specials=sendRepository.findById2(id);
        System.out.println(id);
        System.out.println(specials.size());
        model.addAttribute("special",specials);
        return "count/specialedit";
    }

    @RequestMapping("/specialedit")
    public String specialedit(@Valid SendParam sendParam,BindingResult result,Model model){
        String errorMsg="";
        //参数校验
        if(result.hasErrors()){
            List<ObjectError> list=result.getAllErrors();
            for(ObjectError error:list){
                errorMsg=errorMsg+error.getDefaultMessage()+";";
            }
            model.addAttribute("errorMsg",errorMsg);
            model.addAttribute("special",sendParam);
            return "count/special";
        }
        System.out.println("123123");
//        Consumer consumer=new Consumer();
//        BeanUtils.copyProperties(consumerParam,consumer);
//        consumerRepository.save(consumer);
       SendStation sendStation =sendRepository.findById(sendParam.getId());
       sendStation.setName(sendParam.getName());
       sendStation.setAddress(sendParam.getAddress());
       sendStation.setPhone(sendParam.getPhone());
       sendRepository.save(sendStation);
        return "redirect:/special";

    }

    @Autowired
    private PrintSendRepository printSendRepository;

    @RequestMapping("/countDistribute")
    public String toCountDistribute(){
        return "count/countDistribute";
    }

    @RequestMapping("/countDistributeSearch")
    public String countDis(@RequestParam(value = "date")@DateTimeFormat(pattern = "yyyy-MM-dd")Date date, Model model) {

        List<ObjectError> list = null;
        if(date != null) {


            List<PrintSend> list1 = printSendRepository.findYBByTime(date);

            List<Integer> listNum = new ArrayList<Integer>();
            List<Integer> listsendId = new ArrayList<Integer>();

            for (int i = 0; i < list1.size(); i++) {
                PrintSend printSendtemp = list1.get(i);
                System.out.println(printSendtemp.getId());
                listsendId.add(printSendtemp.getSendId());
                System.out.println(printSendtemp.getSendId());
                if (printSendtemp.getNeedNum() != null) {
                    listNum.add(printSendtemp.getNeedNum());
                    System.out.println(printSendtemp.getNeedNum());
                } else {
                    listNum.add(0);
                }
            }

            model.addAttribute("listNum", listNum);
            model.addAttribute("listsendId", listsendId);
        }
        return "count/countDistribute";
    }

    @RequestMapping("/sumPrintnum")
    public String toSumPrint(){
        return "count/sumPrintnum";
    }

    @RequestMapping("/sumPrintnumSearch")
    public String sumPri(@RequestParam(value = "date")@DateTimeFormat(pattern = "yyyy-MM-dd")Date date, Model model) {

//        String errormessage = "";
//        List<ObjectError> list = null;
//        if (result.hasErrors()) {
//            list = result.getAllErrors();
//            for (ObjectError error : list) {
//                errormessage = errormessage + error.getDefaultMessage() + ";";
//            }
//            model.addAttribute("errormessage", errormessage);
//            return "count/countDistribute";
//        }
        if(date!=null) {
            List<PrintSend> list1 = printSendRepository.findYBByTime(date);
            List<Integer> listNum = new ArrayList<Integer>();
            List<Integer> listprintId = new ArrayList<Integer>();

            for (int i = 0; i < list1.size(); i++) {
                PrintSend printSendtemp = list1.get(i);
                listprintId.add(printSendtemp.getPrintId());
                if (printSendtemp.getPrintNum() != null) {
                    System.out.println(printSendtemp.getId()+"asdasd");
                    listNum.add(printSendtemp.getPrintNum());
                } else {
                    listNum.add(0);
                }
            }

            model.addAttribute("listNum", listNum);
            model.addAttribute("listprintId", listprintId);
        }
        return "count/sumPrintnum";
    }

}
