package com.xxzxxz.press.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.xxzxxz.press.model.Department;
import com.xxzxxz.press.model.Staff;
import com.xxzxxz.press.param.LoginParam;
import com.xxzxxz.press.repository.DepartmentRepository;
import com.xxzxxz.press.repository.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
public class LoginController {
	
	@Autowired
	private StaffRepository staffRepository;

	@Autowired
    private DepartmentRepository departmentRepository;

	@RequestMapping("/")
    public String index(){
	    return "login";
    }

	@RequestMapping("/stafflogin")
	public String stafflogin(@Valid LoginParam loginParam, BindingResult result, ModelMap model, HttpServletRequest request){
		
		String errorMsg = "";
        if (result.hasErrors()) {
            List<ObjectError> list = result.getAllErrors();
            for (ObjectError error : list) {
                errorMsg = error.getDefaultMessage();
                model.addAttribute("errorMsg", errorMsg);
                return "login";
            }
        }
        try{
        Staff staff = staffRepository.findByStaffName(loginParam.getUserName());
        Department department=departmentRepository.findById(staff.getDepartId());
        if (staff == null) {
        	model.addAttribute("errorMsg", "用户名不存在!");
            return "login";
        }
        staff = staffRepository.findByStaffNameAndPassword(loginParam.getUserName(), loginParam.getPassword());
        if (staff == null) {
            model.addAttribute("errorMsg", "密码错误!");
            return "login";
        } else {
        	model.addAttribute("staff", staff);
        	request.getSession().setAttribute("staff",staff);
        	request.getSession().setAttribute("staffId",staff.getId());
        	request.getSession().setAttribute("stationId",department.getStationId());
        	if(department.getStationId()>5){
                return "redirect:/consumerList";
            }else if(department.getStationId()==2){
        	    return "redirect:/leaderSearch";
            }else if(department.getStationId()==3){
        	    return "redirect:/tokefuindex";
            }else if(department.getStationId()==1){
        	    return "redirect:/tocenterindex";
            }else if(department.getStationId()==5){
        	    return "redirect:/toCaiwu";
            }else if(department.getStationId()==4){
        	    return "redirect:/toFengfa";
            }

        	
        }}catch (Exception e){
            return "login";
        }
        
       
		return "";
	}

	@RequestMapping("/logout")
    public String logout(HttpServletRequest request){
	    request.getSession().removeAttribute("staff");
	    request.getSession().removeAttribute("stationId");
	    return "login";
    }
	
}
