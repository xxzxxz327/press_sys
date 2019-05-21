package com.xxzxxz.press.web;


import com.xxzxxz.press.model.*;
import com.xxzxxz.press.repository.DepartmentRepository;
import com.xxzxxz.press.repository.MessageRepository;
import com.xxzxxz.press.repository.StaffRepository;
import com.xxzxxz.press.repository.StationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.jws.WebParam;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@Controller
public class MessageController {
    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private StaffRepository staffRepository;

    @RequestMapping("/sendMessage")
    public String addMessage(@RequestParam(value = "toId")List<Integer> toIds,
                             @RequestParam(value="content")String content,
                             @RequestParam(value = "title")String title,
                             HttpServletRequest request,
                             Model model){
        Staff staff=null;
        for(int toId:toIds){
            Message message=new Message();
            message.setContent(content);
            staff=(Staff)request.getSession().getAttribute("staff");
            int fromId=staff.getDepartId();
            message.setFromId(fromId);
            message.setToId(toId);
            message.setTime(new Date());
            message.setTitle(title);
            messageRepository.save(message);
        }
        String successMsg="成功发送！";
        model.addAttribute("successMsg",successMsg);
        List<Department> departments=departmentRepository.findByIdExceptNow(staff.getDepartId());
        System.out.println(departments.size());
        model.addAttribute("departments",departments);
        return "message/sendMessageList";
    }

    @RequestMapping("/unReadMessageList")
    public String messageList(Model model, HttpServletRequest request){
        Staff staff=(Staff) request.getSession().getAttribute("staff");
        List<Object[]> messages=messageRepository.findListByToIdUnRead(staff.getDepartId());
        model.addAttribute("messages",messages);
        return "message/recieveUnReadMessageList";
    }

    @RequestMapping("/readMessageList")
    public String readMessageList(Model model, HttpServletRequest request){
        Staff staff=(Staff) request.getSession().getAttribute("staff");
        List<Object[]> messages=messageRepository.findListByToIdRead(staff.getDepartId());
        model.addAttribute("messages",messages);
        return "message/readMessageList";
    }

    @RequestMapping("/toSendMessage")
    public String toSendMessage(HttpServletRequest request,Model model){
        Staff staff=(Staff)request.getSession().getAttribute("staff");
        List<Department> departments=departmentRepository.findByIdExceptNow(staff.getDepartId());
        model.addAttribute("departments",departments);
        return "message/sendMessageList";
    }

    @RequestMapping("/readMessageDetail")
    public String readMessageDetail(Model model,int messageId,String fromName){
        Message message=messageRepository.findById(messageId);
        model.addAttribute("message",message);
        model.addAttribute("fromName",fromName);
        return "message/readMessageDetail";
    }

    @RequestMapping("/messageRead")
    public String messageRead(@RequestParam(value = "id")int id){
        Message message=messageRepository.findById(id);
        message.setStatus(1);
        messageRepository.save(message);
        return "redirect:/readMessageList";
    }

    @RequestMapping("/deleteMessage")
    public String deleteMessage(int messageId){
        messageRepository.deleteById(messageId);
        return "redirect:/readMessageList";
    }

    @RequestMapping("/tochangePassword")
    public String changePassword(HttpServletRequest request, Model model){
        Staff staff=(Staff) request.getSession().getAttribute("staff");
        model.addAttribute("id",staff.getId());
        model.addAttribute("staffName",staff.getStaffName());
        return "staff/staffPasswordChange";
    }

    @RequestMapping("/changePassword")
    public String changePassword(@RequestParam(value = "id")int id,
                                 @RequestParam(value = "oldPwd")String oldPwd,
                                 @RequestParam(value = "newPwd")String newPwd,
                                 Model model){
        Staff staff=staffRepository.findById(id);
        String errorMsg="";
        if(oldPwd.equals("")||newPwd.equals("")){
            errorMsg="请输入原密码和新密码";
            model.addAttribute("errorMsg",errorMsg);
            model.addAttribute("id",staff.getId());
            model.addAttribute("staffName",staff.getStaffName());
            return "staff/staffPasswordChange";
        }
        else if(staff.getPassword().equals(oldPwd)){
            staff.setPassword(newPwd);
        }else{
            errorMsg="原密码错误，请重新输入";
            model.addAttribute("errorMsg",errorMsg);
            model.addAttribute("id",staff.getId());
            model.addAttribute("staffName",staff.getStaffName());
            return "staff/staffPasswordChange";
        }
        staffRepository.save(staff);
        String successMsg="修改成功";
        model.addAttribute("successMsg",successMsg);
        model.addAttribute("id",staff.getId());
        model.addAttribute("staffName",staff.getStaffName());
        return "staff/staffPasswordChange";
    }
}
