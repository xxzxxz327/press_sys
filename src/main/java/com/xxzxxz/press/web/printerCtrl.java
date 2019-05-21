package com.xxzxxz.press.web;

import com.xxzxxz.press.model.PrintSend;
import com.xxzxxz.press.param.PrintSendParam;
import com.xxzxxz.press.repository.PrintSendRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Controller
public class printerCtrl {
    @Autowired
    private PrintSendRepository printSendRepository;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @RequestMapping("/distrNum")
    public String dis(@Valid PrintSendParam printSendParam, BindingResult result, Model model) {

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
//

        List<PrintSend> list = printSendRepository.findYBByTime(printSendParam.getDate());

        List<Integer> listNum = new ArrayList<Integer>();
        List<Integer> listsendId = new ArrayList<Integer>();

        for (int i = 0; i < list.size(); i++) {
            PrintSend printSendtemp = list.get(i);
            listsendId.add(printSendtemp.getSendId());
            if (printSendtemp.getNeedNum() != null) {
                listNum.add(printSendtemp.getNeedNum());
            } else {
                listNum.add(0);
            }
        }

        model.addAttribute("listNum", listNum);
        model.addAttribute("listsendId", listsendId);

        return "printer/distrNum";
    }
    @RequestMapping("/printNum")
    public String sumPri(@Valid PrintSendParam printSendParam, BindingResult result, Model model) {

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
//


        List<PrintSend> list = printSendRepository.findYBByTime(printSendParam.getDate());

        List<Integer> listNum = new ArrayList<Integer>();
        List<Integer> listprintId = new ArrayList<Integer>();

        for (int i = 0; i < list.size(); i++) {
            PrintSend printSendtemp = list.get(i);
            listprintId.add(printSendtemp.getPrintId());
            if (printSendtemp.getPrintNum() != null) {
                listNum.add(printSendtemp.getPrintNum());
            } else {
                listNum.add(0);
            }
        }

        model.addAttribute("listNum", listNum);
        model.addAttribute("listprintId", listprintId);

        return "printer/printNum";
    }
    @RequestMapping("/transNum")
    public String trans(@Valid PrintSendParam printSendParam, BindingResult result, Model model) {

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
//

        List<PrintSend> list = printSendRepository.findYBByTime(printSendParam.getDate());

        List<Integer> listNum = new ArrayList<Integer>();
        List<Integer> listprintId = new ArrayList<Integer>();
        List<Integer> listsendId = new ArrayList<Integer>();

        for (int i = 0; i < list.size(); i++) {
            PrintSend printSendtemp = list.get(i);
            listprintId.add(printSendtemp.getPrintId());
            listsendId.add(printSendtemp.getSendId());
            if (printSendtemp.getNeedNum() != null) {
                listNum.add(printSendtemp.getNeedNum());
            } else {
                listNum.add(0);
            }
        }

        model.addAttribute("listNum", listNum);
        model.addAttribute("listprintId", listprintId);
        model.addAttribute("listsendId", listsendId);
        return "printer/transNum";
    }
}
