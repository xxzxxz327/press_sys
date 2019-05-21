package com.xxzxxz.press.web;


import com.xxzxxz.press.model.*;
import com.xxzxxz.press.param.*;
import com.xxzxxz.press.repository.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Controller
public class indexCtrl {

    @RequestMapping("/tomap")
    public String tomap(Model model,int id){
        String s= routeRepository.findRouteById(id);
        model.addAttribute("start",s);
        String e= routeRepository.findFirstById(id);
        model.addAttribute("end",e);
        return "map";
    }

    @RequestMapping("/torgmap")
    public String torgmap(Model model,int id){
        List list = routeGroupRepository.findRouteGroupById(id);
        model.addAttribute("rg",list);
        String start= routeGroupRepository.findFirstById(id);
        model.addAttribute("start",start);
        return "rgmap";
    }

    @Autowired
    private PrintRepository printRepository;

    @RequestMapping("/printOne")
    public String findOne(Model model, @RequestParam(value = "id")Integer id,RedirectAttributes ra, @RequestParam(value="page",defaultValue = "0")Integer page,
                          @RequestParam(value="size",defaultValue = "6")Integer size){
    if (id==null){
        String errorMsg="id不能为空";
        ra.addAttribute("errorMsg",errorMsg);
        System.out.println(222);
        return"redirect:/sysPrint";
    }
//  Consumer consumers=consumerRepository.findById(id);
        Pageable pageable=PageRequest.of(page,size);
        Page<Print> prints = printRepository.findById1(id.intValue(),pageable);
        model.addAttribute("prints",prints);
        return "sys/print/sysPrint";
    }
    @RequestMapping("/sysPrint")
    public String printlist(Model model, @RequestParam(value="page",defaultValue = "0")Integer page,
                       @RequestParam(value="size",defaultValue = "6")Integer size){
        Sort sort=new Sort(Sort.Direction.DESC,"id");
        Pageable pageable= PageRequest.of(page,size,sort);
        Page<Print> prints=printRepository.findList(pageable);
        model.addAttribute("prints",prints);
        return "sys/print/sysPrint";
    }

    @RequestMapping("/printadd")
    public String printadd(@Valid PrintParam printParam, BindingResult result,Model model){
        String errorMsg="";
        //参数校验
        if(result.hasErrors()){
            List<ObjectError> list=result.getAllErrors();
            for(ObjectError error:list){
                errorMsg=errorMsg+error.getCode()+"-"+error.getDefaultMessage()+";";
            }
            model.addAttribute("errorMsg",errorMsg);
            return "sys/print/printadd";
        }
        Print print = new Print();
        BeanUtils.copyProperties(printParam,print);
        try{
        printRepository.save(print);
         }catch (Exception e){
        errorMsg="一个区域只能有一个印点";
        model.addAttribute("errorMsg",errorMsg);
            return "sys/print/printadd";
        }
        System.out.println(111);
        System.out.println(print.getId());
        return "redirect:/sysPrint";
    }
//
    @RequestMapping("/printdelete")
    public String printdelete(int id){
        printRepository.deleteById(id);
        return "redirect:/sysPrint";
}
    @RequestMapping("/toprintedit")
    public String toprintedit(Model model,int id){
//        Consumer consumer=consumerRepository.findById(id);
        Print print = printRepository.findById(id);
        model.addAttribute("print",print);
        return "sys/print/printedit";
    }

    @RequestMapping("/printedit")
    public String printedit(@Valid PrintParam printParam,BindingResult result,Model model){
        String errorMsg="";
        //参数校验
        if(result.hasErrors()){
            List<ObjectError> list=result.getAllErrors();
            for(ObjectError error:list){
                errorMsg=errorMsg+error.getDefaultMessage()+";";
            }
            model.addAttribute("errorMsg",errorMsg);
            model.addAttribute("print",printParam);
            return "sys/print/sysPrint";
        }
        System.out.println("123123");
//        Consumer consumer=new Consumer();
//        BeanUtils.copyProperties(consumerParam,consumer);
//        consumerRepository.save(consumer);
        Print print = new Print();
        BeanUtils.copyProperties(printParam,print);
        printRepository.save(print);
        return "redirect:/sysPrint";

    }


    @RequestMapping("/toFengfa")
    public String toindexbasic(){
        System.out.println(111);
        return "fenfaindex";
    }


    @Autowired
    private SendRepository sendRepository;
    @RequestMapping("/sendOne")
    public String findOnes(Model model,@RequestParam(value = "id")Integer id,RedirectAttributes ra, @RequestParam(value="page",defaultValue = "0")Integer page,
                           @RequestParam(value="size",defaultValue = "6")Integer size){
//        Consumer consumers=consumerRepository.findById(id);
        if (id==null){
            String errorMsg="id不能为空";
            ra.addAttribute("errorMsg",errorMsg);
            System.out.println(222);
            return"redirect:/sysSend";
        }
        System.out.println(id.intValue());
        Sort sort=new Sort(Sort.Direction.DESC,"id");
        Pageable pageable= PageRequest.of(page,size,sort);
        Page<SendStation> sendStations = sendRepository.findById1(id.intValue(),pageable);
        model.addAttribute("sendStations",sendStations);
        return "sys/send/sysSend";
    }
    @RequestMapping("/sysSend")
    public String sendlist(Model model, @RequestParam(value="page",defaultValue = "0")Integer page,
                       @RequestParam(value="size",defaultValue = "6")Integer size){
        Sort sort=new Sort(Sort.Direction.DESC,"id");
        Pageable pageable= PageRequest.of(page,size,sort);
        Page<SendStation> sendStations=sendRepository.findList(pageable);
        model.addAttribute("sendStations",sendStations);
        return "sys/send/sysSend";
    }

    @RequestMapping("/sendadd")
    public String sendadd(@Valid SendParam sendParam, BindingResult result,Model model){

        String errorMsg="";
        //参数校验
        if(result.hasErrors()){
            List<ObjectError> list=result.getAllErrors();
            for(ObjectError error:list){
                errorMsg=errorMsg+error.getCode()+"-"+error.getDefaultMessage()+";";
            }
            model.addAttribute("errorMsg",errorMsg);
            return "sys/send/sendadd";
        }
//        Print print = new Print();
        SendStation sendStation =new SendStation();
        BeanUtils.copyProperties(sendParam,sendStation);
        sendRepository.save(sendStation);
        return "redirect:/sysSend";
    }
    //
    @RequestMapping("/senddelete")
    public String senddelete(int id){
        sendRepository.deleteById(id);
        return "redirect:/sysSend";
    }
    @RequestMapping("/tosendedit")
    public String tosendedit(Model model,int id){
//        Consumer consumer=consumerRepository.findById(id);
       SendStation sendStation = sendRepository.findById(id);
        model.addAttribute("sendStation",sendStation);
        return "sys/send/sendedit";
    }

    @RequestMapping("/sendedit")
    public String sendedit(@Valid SendParam sendParam,BindingResult result,Model model){
        String errorMsg="";
        //参数校验
        if(result.hasErrors()){
            List<ObjectError> list=result.getAllErrors();
            for(ObjectError error:list){
                errorMsg=errorMsg+error.getDefaultMessage()+";";
            }
            model.addAttribute("errorMsg",errorMsg);
            model.addAttribute("sendStation",sendParam);
            return "sys/send/sysSend";
        }
        System.out.println("123123");
//        Consumer consumer=new Consumer();
//        BeanUtils.copyProperties(consumerParam,consumer);
//        consumerRepository.save(consumer);
      SendStation sendStation =sendRepository.findById(sendParam.getId());
      sendStation.setName(sendParam.getName());
      sendStation.setAddress(sendParam.getAddress());
      sendStation.setPhone(sendParam.getPhone());
      sendStation.setAreaId(sendParam.getAreaId());
      sendRepository.save(sendStation);
        return "redirect:/sysSend";

    }


    @Autowired
    private PackRepository packRepository;

    @RequestMapping("/sysPackage")
    public String packlist(Model model, @RequestParam(value="page",defaultValue = "0")Integer page,
                            @RequestParam(value="size",defaultValue = "6")Integer size){
        Sort sort=new Sort(Sort.Direction.DESC,"id");
        Pageable pageable= PageRequest.of(page,size,sort);
        Page<Pack> packs=packRepository.findList(pageable);
        model.addAttribute("packs",packs);
        System.out.println(2323);
        return "sys/package/sysPackage";
    }

    @RequestMapping("topackageadd")
    public String toadd(){
        return "sys/package/packageadd";
    }

    @RequestMapping("/packageadd")
    public String packadd(@Valid PackParam packParam, BindingResult result,Model model){
//        String errorMsg="";
//        //参数校验
//        if(result.hasErrors()){
//            List<ObjectError> list=result.getAllErrors();
//            for(ObjectError error:list){
//                errorMsg=errorMsg+error.getCode()+"-"+error.getDefaultMessage()+";";
//            }
//            model.addAttribute("errorMsg",errorMsg);
//            return "sys/package/packageadd";
//        }
        Pack pack = new Pack();
        BeanUtils.copyProperties(packParam, pack);
        pack.setRegTime(new Date());
        packRepository.save(pack);
        return "redirect:/sysPackage";
    }
    //
    @RequestMapping("/packagedelete")
    public String packdelete(int id){
        packRepository.deleteById(id);
        return "redirect:/sysPackage";
    }
    @RequestMapping("/topackageedit")
    public String topackedit(Model model,int id){
//        Consumer consumer=consumerRepository.findById(id);
        Pack pack = packRepository.findById(id);
        model.addAttribute("pack",pack);
        return "sys/package/packageedit";
    }

    @RequestMapping("/packageedit")
    public String printedit(@Valid PackParam packParam,BindingResult result,Model model) {
        String errorMsg = "";
        //参数校验
        if (result.hasErrors()) {
            List<ObjectError> list = result.getAllErrors();
            for (ObjectError error : list) {
                errorMsg = errorMsg + error.getDefaultMessage() + ";";
            }
            model.addAttribute("errorMsg", errorMsg);
            model.addAttribute("pack", packParam);
            return "sys/package/sysPackage";
        }
        System.out.println("123123");
//        Consumer consumer=new Consumer();
//        BeanUtils.copyProperties(consumerParam,consumer);
//        consumerRepository.save(consumer);
//        Print print = new Print();
        Pack pack = new Pack();
        BeanUtils.copyProperties(packParam, pack);
        pack.setRegTime(new Date());
        packRepository.save(pack);
        return "redirect:/sysPackage";
    }


    @Autowired
    private PageInfoRepository pageInfoRepository;


    @RequestMapping("/page")
    public String pagelist(Model model, @RequestParam(value="page",defaultValue = "0")Integer page,
                           @RequestParam(value="size",defaultValue = "6")Integer size){
        Sort sort=new Sort(Sort.Direction.DESC,"id");
        Pageable pageable= PageRequest.of(page,size,sort);
        Page<PageInfo> pageInfos=pageInfoRepository.findList(pageable);
        model.addAttribute("pageInfos",pageInfos);
        System.out.println(2323);
        return "sys/page/page";
    }

    @RequestMapping("topageadd")
    public String topaadd(){
        System.out.println(888);
        return "sys/page/pageadd";
    }
    @RequestMapping("/pageadd")
    public String pageadd(@Valid PageInfoParam pageInfoParam, BindingResult result,Model model){
        String errorMsg="";
        //参数校验
        if(result.hasErrors()){
            List<ObjectError> list=result.getAllErrors();
            for(ObjectError error:list){
                errorMsg=errorMsg+error.getCode()+"-"+error.getDefaultMessage()+";";
            }
            model.addAttribute("errorMsg",errorMsg);
            return "sys/page/pageadd";
        }
        System.out.println(000);
        PageInfo pageInfo = new PageInfo();
        BeanUtils.copyProperties(pageInfoParam, pageInfo);
        pageInfoRepository.save(pageInfo);
        return "redirect:/page";
    }
    //
    @RequestMapping("/pagedelete")
    public String pagedelete(int id){
        pageInfoRepository.deleteById(id);
        return "redirect:/page";
    }
    @RequestMapping("/topageeedit")
    public String topageedit(Model model,int id){
//        Consumer consumer=consumerRepository.findById(id);
        PageInfo pageInfo = pageInfoRepository.findById(id);
        model.addAttribute("pageInfo",pageInfo);
        return "sys/page/pageedit";
    }

    @RequestMapping("/pageedit")
    public String pageedit(@Valid PageInfoParam pageInfoParam,BindingResult result,Model model) {
        String errorMsg = "";
        //参数校验
        if (result.hasErrors()) {
            List<ObjectError> list = result.getAllErrors();
            for (ObjectError error : list) {
                errorMsg = errorMsg + error.getDefaultMessage() + ";";
            }
            model.addAttribute("errorMsg", errorMsg);
            model.addAttribute("pageInfo", pageInfoParam);
            return "sys/page/page";
        }
        System.out.println("123123");
//        Consumer consumer=new Consumer();
//        BeanUtils.copyProperties(consumerParam,consumer);
//        consumerRepository.save(consumer);
//        Print print = new Print();
        PageInfo pageInfo = new PageInfo();
        BeanUtils.copyProperties(pageInfoParam, pageInfo);
        pageInfoRepository.save(pageInfo);
        return "redirect:/page";
    }

@Autowired
private PrintSendRepository printSendRepository;
    @RequestMapping("/PsOne")
    public String psOne(Model model,
                        @RequestParam(value = "printId")Integer printId,
                        @RequestParam(value = "sendId")Integer sendId,
                        RedirectAttributes ra){
        if (printId!=null) {
            PrintSend printSend = printSendRepository.findById(printId.intValue());
            model.addAttribute("printSend", printSend);
        }else if (sendId != null) {
            PrintSend printSend = printSendRepository.findById(sendId.intValue());
            model.addAttribute("printSend", printSend);
        }else {
            String errorMsg="id不能为空";
            ra.addAttribute("errorMsg",errorMsg);
            System.out.println(222);
            return"redirect:/sysPs";
        }
        return "sys/PS/sysPs";
    }
    @RequestMapping("/sysPs")
    public String pslist(Model model, @RequestParam(value="page",defaultValue = "0")Integer page,
                            @RequestParam(value="size",defaultValue = "6")Integer size){
        Sort sort=new Sort(Sort.Direction.DESC,"id");
        Pageable pageable= PageRequest.of(page,size,sort);
        Page<PrintSend> printSends=printSendRepository.findList(pageable);
        model.addAttribute("printSends",printSends);
        return "sys/PS/sysPs";
    }

    @RequestMapping("toPsadd")
    public String toPsadd(){
        return "sys/PS/Psadd";
    }
    @RequestMapping("/Psadd")
    public String psadd(@Valid PrintSendParam printSendParam, BindingResult result,Model model){
        String errorMsg="";
        //参数校验
        if(result.hasErrors()){
            List<ObjectError> list=result.getAllErrors();
            for(ObjectError error:list){
                errorMsg=errorMsg+error.getCode()+"-"+error.getDefaultMessage()+";";
            }
            model.addAttribute("errorMsg",errorMsg);
            return "sys/PS/Psadd";
        }
        //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        Print print = new Print();
//        BeanUtils.copyProperties(printParam,print);
//        printRepository.save(print);
        PrintSend printSend = new PrintSend();
        System.out.println(printSendParam.getDate());
        BeanUtils.copyProperties(printSendParam,printSend);
        printSendRepository.save(printSend);

        return "redirect:/sysPs";
    }
    //
    @RequestMapping("/Psdelete")
    public String psdelete(int id){
        printSendRepository.deleteById(id);
        return "redirect:/sysPs";
    }
    @RequestMapping("/toPsedit")
    public String topsedit(Model model,int id){
//        Consumer consumer=consumerRepository.findById(id);
        PrintSend printSend = printSendRepository.findById(id);
        model.addAttribute("printSend",printSend);
        return "sys/PS/Psedit";
    }

    @RequestMapping("/Psedit")
    public String psedit(@Valid PrintSendParam printSendParam,BindingResult result,Model model){
        String errorMsg="";
        //参数校验
        if(result.hasErrors()){
            List<ObjectError> list=result.getAllErrors();
            for(ObjectError error:list){
                errorMsg=errorMsg+error.getDefaultMessage()+";";
            }
            model.addAttribute("errorMsg",errorMsg);
            model.addAttribute("printSend",printSendParam);
            return "sys/PS/sysPs";
        }
        System.out.println("123123");
//        Consumer consumer=new Consumer();
//        BeanUtils.copyProperties(consumerParam,consumer);
//        consumerRepository.save(consumer);

        PrintSend printSend = new PrintSend();
        BeanUtils.copyProperties(printSendParam,printSend);
        printSendRepository.save(printSend);
        return "redirect:/sysPs";
    }

    @Autowired
    private RouteRepository routeRepository;
    @RequestMapping("/routeOne")
    public String routeOne(Model model, @RequestParam(value = "id")Integer id,RedirectAttributes ra){
        if (id==null){
            String errorMsg="id不能为空";
            ra.addAttribute("errorMsg",errorMsg);
            System.out.println(222);
            return"redirect:/routeadd";
        }
//  Consumer consumers=consumerRepository.findById(id);
       Route routes = routeRepository.findById(id.intValue());
        model.addAttribute("routes",routes);
        return "sys/route/sysRoute";
    }
    @RequestMapping("/sysRoute")
    public String routelist(Model model, @RequestParam(value="page",defaultValue = "0")Integer page,
                            @RequestParam(value="size",defaultValue = "6")Integer size){
        Sort sort=new Sort(Sort.Direction.DESC,"id");
        Pageable pageable= PageRequest.of(page,size,sort);
        Page<Route> routes=routeRepository.findList(pageable);
        model.addAttribute("routes",routes);
        return "sys/route/sysRoute";
    }

    @RequestMapping("torouteadd")
    public String torouteadd(){
        return "sys/route/routeadd";
    }
    @RequestMapping("/routeadd")
    public String routeadd(@Valid RouteParam routeParam, BindingResult result,Model model){
        String errorMsg="";
        //参数校验
        if(result.hasErrors()){
            List<ObjectError> list=result.getAllErrors();
            for(ObjectError error:list){
                errorMsg=errorMsg+error.getCode()+"-"+error.getDefaultMessage()+";";
            }
            model.addAttribute("errorMsg",errorMsg);
            return "sys/route/routeadd";
        }
       Route route = new Route();
        BeanUtils.copyProperties(routeParam,route);
        routeRepository.save(route);
        System.out.println(111);
        System.out.println(route.getId());
        return "redirect:/sysRoute";
    }
    //
    @RequestMapping("/routedelete")
    public String routedelete(int id){
        routeRepository.deleteById(id);
        return "redirect:/sysRoute";
    }
    @RequestMapping("/torouteedit")
    public String torouteedit(Model model,int id){
//        Consumer consumer=consumerRepository.findById(id);
        Route route = routeRepository.findById(id);
        model.addAttribute("route",route);
        return "sys/route/routeedit";
    }

    @RequestMapping("/routeedit")
    public String routeedit(@Valid RouteParam routeParam,BindingResult result,Model model){
        String errorMsg="";
        //参数校验
        if(result.hasErrors()){
            List<ObjectError> list=result.getAllErrors();
            for(ObjectError error:list){
                errorMsg=errorMsg+error.getDefaultMessage()+";";
            }
            model.addAttribute("errorMsg",errorMsg);
            model.addAttribute("route",routeParam);
            return "sys/route/sysRoute";
        }
        System.out.println("123123");
//        Consumer consumer=new Consumer();
//        BeanUtils.copyProperties(consumerParam,consumer);
//        consumerRepository.save(consumer);
       Route route =new Route();
       BeanUtils.copyProperties(routeParam,route);
       routeRepository.save(route);
        return "redirect:/sysRoute";

    }

    @Autowired
    private RouteGroupRepository routeGroupRepository;
    @RequestMapping("/routeGroupOne")
    public String routeGroupOne(Model model, @RequestParam(value = "id")Integer id,RedirectAttributes ra){
        if (id==null){
            String errorMsg="id不能为空";
            ra.addAttribute("errorMsg",errorMsg);
            System.out.println(222);
            return"redirect:/sysRoutegroup";
        }
//  Consumer consumers=consumerRepository.findById(id);
        RouteGroup routeGroup = routeGroupRepository.findById(id.intValue());
        model.addAttribute("routeGroups",routeGroup);
        return "sys/routeGroup/sysRouteGroup";
    }
    @RequestMapping("/sysRoutegroup")
    public String rglist(Model model, @RequestParam(value="page",defaultValue = "0")Integer page,
                         @RequestParam(value="size",defaultValue = "6")Integer size){
        Sort sort=new Sort(Sort.Direction.DESC,"id");
        Pageable pageable= PageRequest.of(page,size,sort);
        Page<RouteGroup> routeGroups=routeGroupRepository.findList(pageable);
        model.addAttribute("routeGroups",routeGroups);
        return "sys/routeGroup/sysRoutegroup";
    }

    @RequestMapping("torouteGroupadd")
    public String toaRGdd(){
        return "sys/routeGroup/routegroupadd";
    }
    @RequestMapping("/routegroupadd")
    public String printadd(@Valid RouteGroupParam routeGroupParam, BindingResult result,Model model){
        String errorMsg="";
        //参数校验
        if(result.hasErrors()){
            List<ObjectError> list=result.getAllErrors();
            for(ObjectError error:list){
                errorMsg=errorMsg+error.getCode()+"-"+error.getDefaultMessage()+";";
            }
            model.addAttribute("errorMsg",errorMsg);
            return "sys/routeGroup/rutegroupadd";
        }
        RouteGroup routeGroup= new RouteGroup();
        BeanUtils.copyProperties(routeGroupParam,routeGroup);
        routeGroupRepository.save(routeGroup);
        return "redirect:/sysRoutegroup";
    }
    //
    @RequestMapping("/routegroupdelete")
    public String routegroupdelete(int id){
        routeGroupRepository.deleteById(id);
        System.out.println(44455);
        return "redirect:/sysRoutegroup";
    }
    @RequestMapping("/torouteGroupedit")
    public String torgedit(Model model,int id){
//        Consumer consumer=consumerRepository.findById(id);
        RouteGroup routeGroups = routeGroupRepository.findById(id);
        System.out.println(8989);
        model.addAttribute("routeGroups",routeGroups);
        return "sys/routeGroup/routeGroupedit";
    }

    @RequestMapping("/routeGroupedit")
    public String printedit(@Valid RouteGroupParam routeGroupParam,BindingResult result,Model model){
        String errorMsg="";
        //参数校验
        if(result.hasErrors()){
            List<ObjectError> list=result.getAllErrors();
            for(ObjectError error:list){
                errorMsg=errorMsg+error.getDefaultMessage()+";";
            }
            model.addAttribute("errorMsg",errorMsg);
            model.addAttribute("routeGroups",routeGroupParam);
            return "sys/routeGroup/sysRoutegroup";
        }
        System.out.println(6767);
        RouteGroup routeGroup =new RouteGroup();
        BeanUtils.copyProperties(routeGroupParam,routeGroup);
        routeGroupRepository.save(routeGroup);
        return "redirect:/sysRoutegroup";

    }


    @RequestMapping("/showPrintNum")
    public String showPrintNum(Model model, @RequestParam(value="page",defaultValue = "0")Integer page,
                               @RequestParam(value="size",defaultValue = "6")Integer size){
        Pageable pageable= PageRequest.of(page,size);
        Page<Object[]> sendNums=sendRepository.findPrintNumPage(pageable);
        model.addAttribute("objectlist",sendNums);
        return "fenfaPrint/showPrintNum";
    }

    @RequestMapping("/tofindPrintNum")
    public String tofindPrintNum(@RequestParam(value = "PrintId")Integer id,Model model, @RequestParam(value="page",defaultValue = "0")Integer page,
                                 @RequestParam(value="size",defaultValue = "6")Integer size){
            Pageable pageable=PageRequest.of(page,size);
            Page<Object[]> printNums=null;
            if(id==null){
                printNums=sendRepository.findPrintNumPage(pageable);
            }else{
                printNums=sendRepository.findPrintNumOne(id.intValue(),pageable);
            }
            model.addAttribute("objectlist",printNums);
            return "fenfaPrint/showPrintNum";
    }

    @RequestMapping("/printPrintNum")
    public  String printPrintNum(Integer id,Model model){
        List<Object[]> printNums=sendRepository.findPrintNumList(id.intValue());
        model.addAttribute("objectlist",printNums);
        return "fenfaPrint/printPrintNum";
    }

    @RequestMapping("/showSendNum")
    public String showSendNum(Model model, @RequestParam(value="page",defaultValue = "0")Integer page,
                               @RequestParam(value="size",defaultValue = "6")Integer size){
        Pageable pageable= PageRequest.of(page,size);
        Page<Object[]> sendNums=sendRepository.findSendNumPage(pageable);
        model.addAttribute("objectlist",sendNums);
        return "fenfaPrint/showSendNum";
    }

    @RequestMapping("/tofindSendNum")
    public String tofindSendNum(@RequestParam(value = "SendId")Integer id,Model model, @RequestParam(value="page",defaultValue = "0")Integer page,
                                 @RequestParam(value="size",defaultValue = "6")Integer size){
        Pageable pageable=PageRequest.of(page,size);
        Page<Object[]> sendNums=null;
        if(id==null){
            sendNums=sendRepository.findSendNumPage(pageable);
        }else{
            sendNums=sendRepository.findSendNumOne(id.intValue(),pageable);
        }
        model.addAttribute("objectlist",sendNums);
        return "fenfaPrint/showSendNum";
    }

    @RequestMapping("/printSendNum")
    public  String printSendNum(Integer id,Model model){
        List<Object[]> SendNums=sendRepository.findSendNumList(id.intValue());
        model.addAttribute("objectlist",SendNums);
        return "fenfaPrint/printSendNum";
    }

    @RequestMapping("/toShowTransNum")
    public String toshowTransNum(){
        return "fenfaPrint/showTransNum";
    }

    @RequestMapping("/showTransNum")
    public String showTransNum(@RequestParam(value = "SendId")Integer id,@RequestParam(value = "date")@DateTimeFormat(pattern = "yyyy-MM-dd") Date date,Model model){
        List<Object[]> transNums=null;
        String errorMsg="";
        if(id==null||date==null){
            errorMsg="输入参数不能为空";
            model.addAttribute("errorMsg",errorMsg);
           return "fenfaPrint/showTransNum";
        }else{
            SendStation sendStation=sendRepository.findById(id.intValue());
            if(sendStation==null){
                errorMsg="没有该送报点";
                model.addAttribute("errorMsg",errorMsg);
                return "fenfaPrint/showTransNum";
            }
            transNums=sendRepository.findBySendIdAndDate(id.intValue(),date);
            if(transNums.get(0)[0]==null){
                errorMsg="当日没有送报信息";
                model.addAttribute("errorMsg",errorMsg);
                return "fenfaPrint/showTransNum";
            }
        }
        model.addAttribute("id",id);
        model.addAttribute("objectlist",transNums);
        return "fenfaPrint/showTransNum";
    }

    @RequestMapping("/printTransNum")
    public  String printTransNum(Integer id, @DateTimeFormat(pattern = "yyyy-MM-dd") Date date, Model model){
        System.out.println(id.intValue()+"    "+date.getTime());
        List<Object[]> transNums=sendRepository.findBySendIdAndDate(id.intValue(),date);
        model.addAttribute("objectlist",transNums);
        return "fenfaPrint/printTransNum";
    }
}
