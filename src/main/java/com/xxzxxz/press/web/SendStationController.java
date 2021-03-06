package com.xxzxxz.press.web;

import com.xxzxxz.press.model.Orders;
import com.xxzxxz.press.repository.StationRepository;
import com.xxzxxz.press.repository.StationRepository;
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
public class SendStationController {
    @Autowired
    private StationRepository stationRepository;

    @RequestMapping("/SendStation")
    public String index(Model model, @RequestParam(value="page",defaultValue = "0")Integer page,
                        @RequestParam(value="size",defaultValue = "6")Integer size){
        Pageable pageable= PageRequest.of(page,size);
        Page<Object[]> orders=stationRepository.findallstationstu(pageable);
        List<String> listStationName=stationRepository.findstationname();
        model.addAttribute("listStationName",listStationName);
        model.addAttribute("orders",orders);
        return "SendStu/stationStu";
    }

    @RequestMapping("/findallsendstastion")
    public String findallsendstastion(Model model, @RequestParam(value="page",defaultValue = "0")Integer page,
                               @RequestParam(value="size",defaultValue = "6")Integer size){
        Pageable pageable= PageRequest.of(page,size);
        Page<Object[]> orders=stationRepository.findallstationstu(pageable);
        List<String> listStationName=stationRepository.findstationname();
        model.addAttribute("listStationName",listStationName);
        model.addAttribute("orders",orders);
        return "SendStu/stationStu";
    }
    @RequestMapping("/findstationstubyid")
    public String findstationstubyid(@RequestParam(value="page",defaultValue = "0")Integer page,
                                     @RequestParam(value="size",defaultValue = "6")Integer size,
                                     @RequestParam(value="id",defaultValue = "0")String Id, Model model){
        System.out.println("传入分站ID：");
        System.out.println(Id);

        Pageable pageable= PageRequest.of(page,size);
        Page<Object[]> orders=stationRepository.findAllByStationId(Id,pageable);
        List<String> listStationName=stationRepository.findstationname();
        model.addAttribute("listStationName",listStationName);
        model.addAttribute("orders",orders);
        return "SendStu/stationStu";
    }
}
