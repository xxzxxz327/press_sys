package com.xxzxxz.press.web;

import com.xxzxxz.press.model.Consumer;
import com.xxzxxz.press.param.ConsumerParam;
import com.xxzxxz.press.repository.ConsumerRepository;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ConsumerController {
    public static List removeDuplicate(List list){
        List listTemp = new ArrayList();
        for(int i=0;i<list.size();i++){
            if(!listTemp.contains(list.get(i))){
                listTemp.add(list.get(i));
            }
        }
        return listTemp;
    }

    public int getConsumerId(String consumerName){
        String[] splited = consumerName.split("\\s+");
        return Integer.parseInt(splited[0]);
    }

    @Autowired
    private ConsumerRepository consumerRepository;


    @RequestMapping("/bigconsumer")
    public String bigconsumer(Model model, @RequestParam(value="page",defaultValue = "0")Integer page,
                              @RequestParam(value="size",defaultValue = "6")Integer size){
        Pageable pageable= PageRequest.of(page,size);
        Page<Object[]> orders=consumerRepository.findallconsumerorder(pageable);
        model.addAttribute("orders",orders);
        return "bigconsumer/bigConsumer";
    }

    @RequestMapping("/findallconsumerorder")
    public String findallconsumerorder(Model model, @RequestParam(value="page",defaultValue = "0")Integer page,
                                       @RequestParam(value="size",defaultValue = "6")Integer size){
        Pageable pageable= PageRequest.of(page,size);
        Page<Object[]> orders=consumerRepository.findallconsumerorder(pageable);
        model.addAttribute("orders",orders);
        return "bigconsumer/bigConsumer";
    }
    @RequestMapping("/findconsumeryphone")
    public String findconsumeryphone(@RequestParam(value="page",defaultValue = "0")Integer page,
                                     @RequestParam(value="size",defaultValue = "6")Integer size,
                                     @RequestParam(value="id")String Id, Model model){


        if(Id==null){
            String errorMsg="id不能为空";
            model.addAttribute("errorMsg",errorMsg);
            Pageable pageable= PageRequest.of(page,size);
            Page<Object[]> orders=consumerRepository.findallconsumerorder(pageable);
            model.addAttribute("orders",orders);
            return "bigconsumer/bigConsumer";
        }
        Pageable pageable= PageRequest.of(page,size);
        Page<Object[]> orders=consumerRepository.findbigconsumerbycid(Id,pageable);
        model.addAttribute("orders",orders);
        return "bigconsumer/bigConsumer";
    }

    @RequestMapping("/toAddConsumer")
    public String toAddConsumer(){
        return "consumer/consumerAdd";
    }

    @RequestMapping("/addConsumer")
    public String add(@Valid ConsumerParam consumerParam, BindingResult result,Model model){
        String errorMsg="";
        //参数校验
        if(result.hasErrors()){
            List<ObjectError> list=result.getAllErrors();
            for(ObjectError error:list){
                errorMsg=errorMsg+error.getCode()+"-"+error.getDefaultMessage()+";";
            }
            model.addAttribute("errorMsg",errorMsg);
            return "consumer/consumerAdd";
        }
        Consumer consumer=consumerRepository.findByName(consumerParam.getPhone());
        if(consumer!=null){
            model.addAttribute("errorMsg","用户电话号已存在");
            return "consumer/consumerAdd";
        }
        Consumer c=new Consumer();
        BeanUtils.copyProperties(consumerParam,c);
        consumerRepository.save(c);
        return "redirect:/consumerList";
    }

    @RequestMapping("/consumerOne")
    public String findOne(Model model,@RequestParam(value = "consumerName")String consumerName){
        Sort sort=new Sort(Sort.Direction.DESC,"id");
        Pageable pageable= PageRequest.of(0,6,sort);
        if(consumerName==null){
            String errorMsg="姓名不能为空";
            model.addAttribute("errorMsg",errorMsg);
            Page<Consumer> consumers=consumerRepository.findList(pageable);
            model.addAttribute("consumers",consumers);
            return "consumer/consumerList";
        }
        Page<Consumer> consumers=consumerRepository.findListById(getConsumerId(consumerName),pageable);
        List<Object[]> listConsumerName=consumerRepository.findNames();
        List<String> list=new ArrayList<String>();
        for(int i=0;i<listConsumerName.size();i++){
            list.add(listConsumerName.get(i)[0].toString()+" "+listConsumerName.get(i)[1].toString());
            System.out.println(listConsumerName.get(i)[0].toString()+listConsumerName.get(i)[1].toString());
        }
        model.addAttribute("listConsumerName",list);
        model.addAttribute("consumers",consumers);
        return "consumer/consumerList";
    }

    @RequestMapping("/consumerList")
    public String list(Model model, @RequestParam(value="page",defaultValue = "0")Integer page,
                       @RequestParam(value="size",defaultValue = "6")Integer size){
        Sort sort=new Sort(Sort.Direction.DESC,"id");
        Pageable pageable= PageRequest.of(page,size,sort);
        Page<Consumer> consumers=consumerRepository.findList(pageable);
        model.addAttribute("consumers",consumers);
        List<Object[]> listConsumerName=consumerRepository.findNames();
        List<String> list=new ArrayList<String>();
        for(int i=0;i<listConsumerName.size();i++){
            list.add(listConsumerName.get(i)[0].toString()+" "+listConsumerName.get(i)[1].toString());
            System.out.println(listConsumerName.get(i)[0].toString()+listConsumerName.get(i)[1].toString());
        }
        model.addAttribute("listConsumerName",list);
        return "consumer/consumerList";
    }

    @RequestMapping("/toEditConsumer")
    public String toEditConsumer(Model model,int id){
        Consumer consumer=consumerRepository.findById(id);
        model.addAttribute("consumer",consumer);
        return "consumer/consumerEdit";
    }

    @RequestMapping("/editConsumer")
    public String edit(@Valid ConsumerParam consumerParam,BindingResult result,Model model){
        String errorMsg="";
        //参数校验
        if(result.hasErrors()){
            List<ObjectError> list=result.getAllErrors();
            for(ObjectError error:list){
                errorMsg=errorMsg+error.getDefaultMessage()+";";
            }
            model.addAttribute("errorMsg",errorMsg);
            model.addAttribute("consumer",consumerParam);
            return "consumer/consumerEdit";
        }
        System.out.println("123123");
        Consumer consumer=new Consumer();
        BeanUtils.copyProperties(consumerParam,consumer);
        consumerRepository.save(consumer);
        return "redirect:/consumerList";

    }

    @RequestMapping("/deleteConsumer")
    public String delete(int id){
        consumerRepository.deleteById(id);
        return "redirect:/consumerList";
    }

    @RequestMapping("/uploadConsumers")
    public String singleFileUpload(Model model,@RequestParam("file")MultipartFile file, RedirectAttributes redirectAttributes)throws Exception{
        XSSFWorkbook book=new XSSFWorkbook(file.getInputStream());
        XSSFSheet sheet=book.getSheetAt(0);
        for(int i=1;i<sheet.getLastRowNum()+1;i++){
            Consumer consumer=new Consumer();
            XSSFRow row=sheet.getRow(i);
            consumer.setName(row.getCell(0).getStringCellValue());
            consumer.setAddress(row.getCell(1).getStringCellValue());
            consumer.setPhone(String.valueOf((long)row.getCell(2).getNumericCellValue()));
            consumer.setLevel((int)row.getCell(3).getNumericCellValue());
            consumer.setSex((int)row.getCell(4).getNumericCellValue());
            consumer.setAge((int)row.getCell(5).getNumericCellValue());
            consumer.setLanguage(row.getCell(6).getStringCellValue());
            consumer.setProvince(row.getCell(7).getStringCellValue());
            consumer.setOccupation(row.getCell(8).getStringCellValue());
            consumer.setDegree(row.getCell(9).getStringCellValue());
            consumerRepository.save(consumer);
        }
        return "redirect:/consumerList";
    }
}
