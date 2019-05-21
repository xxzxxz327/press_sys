package com.xxzxxz.press.web;

import com.xxzxxz.press.model.*;
import com.xxzxxz.press.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.OnError;
import java.util.List;

@Controller
public class JianduController {

    @Autowired
    private ComplainRepository complainRepository;
    @Autowired
    private PraiseRepository praiseRepository;
    @Autowired
    private AdviceRepository adviceRepository;
    @Autowired
    private DepartmentRepository departmentRepository;

    @RequestMapping("/jianduControl")
    public String jianduControl(Model model, HttpServletRequest request){
        int stationId=(Integer) request.getSession().getAttribute("stationId");
        model.addAttribute("stationId",stationId);
        return "jiandu/jianduControl";
    }

    @RequestMapping("/jianduOne")
    public String list(Model model, @RequestParam(value="page",defaultValue = "0")Integer page,
                       @RequestParam(value="size",defaultValue = "6")Integer size,
                       @RequestParam(value = "stationId")int stationId,
                       @RequestParam(value = "type")int type,
                       @RequestParam(value = "status")int status){
        Pageable pageable= PageRequest.of(page,size);
        Page<Object[]> jiandus=null;
        if(type==0){
            jiandus=complainRepository.findListByStationIdAndStatus(stationId,status,pageable);
        }else if(type==1){
            jiandus=adviceRepository.findListByStationIdAndStatus(stationId,status,pageable);
        }else if(type==2){
            jiandus=praiseRepository.findListByStationIdAndStatus(stationId,status,pageable);
        }
        model.addAttribute("jiandu_type",type);
        model.addAttribute("jiandus",jiandus);
        model.addAttribute("stationId",stationId);
        return "jiandu/jianduList";
    }

    @RequestMapping("/toEditJiandu")
    public String toEditJiandu(Model model,int id,int stationId,int type){
        List<Object[]> jiandu=null;
        Object jiandu_type=null;
        Object[] contain=null;
        List<Department> departments=departmentRepository.findByStationId(stationId);
        if(type==0){
            jiandu=complainRepository.findById1(id);
            System.out.println(jiandu.size());
            contain=jiandu.get(0);
            jiandu_type="complain";
        }else if(type==1){
            jiandu=adviceRepository.findById1(id);
            contain=jiandu.get(0);
            jiandu_type="advice";
        }else if(type==2){
            jiandu=praiseRepository.findById(id);
            contain=jiandu.get(0);
            jiandu_type="praise";
        }
        model.addAttribute("stationId",stationId);
        model.addAttribute("jiandu_type",jiandu_type);
        model.addAttribute("jiandu",contain);
        model.addAttribute("departments",departments);
        model.addAttribute("type",type);
        return "jiandu/jianduEdit";
    }

    @RequestMapping("/jianduSave")
    public String jianduSave(@RequestParam(value = "id") int id,@RequestParam(value = "type")int type,
                             @RequestParam(value = "department")int department,@RequestParam(value = "status")int status,
                             @RequestParam(value = "stationId")int stationId){
        Object jiandu=null;
        if(type==0){
            jiandu=complainRepository.findById2(id);
            ((Complain) jiandu).setDepartId(department);
            ((Complain) jiandu).setStatus(status);
            complainRepository.save((Complain) jiandu);
        }else if(type == 1){
            jiandu=adviceRepository.findById2(id);
            ((Advice) jiandu).setDepartId(department);
            ((Advice) jiandu).setStatus(status);
            adviceRepository.save((Advice) jiandu);
        }else if(type == 2){
            jiandu=praiseRepository.findById2(id);
            ((Praise) jiandu).setDepartId(department);
            ((Praise) jiandu).setStatus(status);
            praiseRepository.save((Praise) jiandu);
        }
        return "redirect:/jianduOne?stationId="+stationId+"&type="+type+"&status="+status;
    }

}
