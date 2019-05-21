package com.xxzxxz.press.web;


import com.xxzxxz.press.repository.BalanceRepository;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
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

import javax.annotation.Resource;
import javax.jws.WebParam;
import javax.validation.Valid;
import java.io.*;
import java.util.List;
@Controller
public class BalanceController {
    @Autowired
    private BalanceRepository balanceRepository;

    @RequestMapping("/BalanceofStation")
    public String index(Model model, @RequestParam(value="page",defaultValue = "0")Integer page,
                        @RequestParam(value="size",defaultValue = "6")Integer size){
        Pageable pageable= PageRequest.of(page,size);
        Page<Object[]> orders=balanceRepository.findallstationbalance(pageable);
        List<String> listStationName=balanceRepository.findstationname();
        model.addAttribute("listStationName",listStationName);
        model.addAttribute("orders",orders);
        return "Balance/stationBalance";
    }
    @RequestMapping("/BalanceofOffice")
    public String BalanceofOffice(Model model, @RequestParam(value="page",defaultValue = "0")Integer page,
                                  @RequestParam(value="size",defaultValue = "6")Integer size){
        Pageable pageable= PageRequest.of(page,size);
        Page<Object[]> orders=balanceRepository.findallofficebalance(pageable);
        List<String> listOfficeName=balanceRepository.findofficename();
        model.addAttribute("listOfficeName",listOfficeName);
        model.addAttribute("orders",orders);
        return "Balance/officeBalance";
    }

    @RequestMapping("/findallbalanceofstastion")
    public String findallbalanceofstastion(Model model, @RequestParam(value="page",defaultValue = "0")Integer page,
                                      @RequestParam(value="size",defaultValue = "6")Integer size){
        Pageable pageable= PageRequest.of(page,size);
        Page<Object[]> orders=balanceRepository.findallstationbalance(pageable);
        List<String> listStationName=balanceRepository.findstationname();
        model.addAttribute("listStationName",listStationName);
        model.addAttribute("orders",orders);
        return "Balance/stationBalance";
    }
    @RequestMapping("/findbalancebystationid")
    public String findbalancebystationid(@RequestParam(value="page",defaultValue = "0")Integer page,
                                     @RequestParam(value="size",defaultValue = "6")Integer size,
                                     @RequestParam(value="id",defaultValue = "0")String Id, Model model){


        Pageable pageable= PageRequest.of(page,size);
        Page<Object[]> orders=balanceRepository.findAllByStationId(Id,pageable);
        List<String> listStationName=balanceRepository.findstationname();
        model.addAttribute("listStationName",listStationName);

        model.addAttribute("orders",orders);
        return "Balance/stationBalance";
    }
    @RequestMapping("/findallbalanceofoffice")
    public String findallbalanceofoffice(Model model, @RequestParam(value="page",defaultValue = "0")Integer page,
                                           @RequestParam(value="size",defaultValue = "6")Integer size){
        Pageable pageable= PageRequest.of(page,size);
        Page<Object[]> orders=balanceRepository.findallofficebalance(pageable);
        List<String> listOfficeName=balanceRepository.findofficename();
        model.addAttribute("listOfficeName",listOfficeName);
        model.addAttribute("orders",orders);
        return "Balance/officeBalance";
    }
    @RequestMapping("/findbalancebyofficeid")
    public String findbalancebyofficeid(@RequestParam(value="page",defaultValue = "0")Integer page,
                                     @RequestParam(value="size",defaultValue = "6")Integer size,
                                     @RequestParam(value="id",defaultValue = "0")String Id, Model model){


        Pageable pageable= PageRequest.of(page,size);
        Page<Object[]> orders=balanceRepository.findAllByofficeId(Id,pageable);
        List<String> listOfficeName=balanceRepository.findofficename();
        model.addAttribute("listOfficeName",listOfficeName);
        model.addAttribute("orders",orders);
        return "Balance/officeBalance";
    }
}