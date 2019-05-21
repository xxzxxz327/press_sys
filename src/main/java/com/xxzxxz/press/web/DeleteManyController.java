package com.xxzxxz.press.web;

import javax.servlet.http.HttpServletRequest;

import com.xxzxxz.press.model.*;
import com.xxzxxz.press.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class DeleteManyController {
	
	@Autowired
	private DepartRepository dR;

	@Autowired
	private OfficeRepository oR;

	@Autowired
	private ChangeReasonRepository rR;

	@Autowired
	private InfoRepository iR;

	@Autowired
	private TransinfoRepository tR;

	@Autowired
	private DutyRepository dutyR;
	
	@Autowired
	private AreaRepository aR;
	
	@Autowired
	private StationRepository stationR;
	
	@Autowired
	private StaffRepository staffR;

	@Autowired
	private PraiseRepository praiseR;

	@Autowired
	private AdviceRepository adviceR;

	@Autowired
	private ComplainRepository complainR;

	@Autowired
	private MeritDicRepository mdR;


	@RequestMapping("/deletemanymerit")
	public String deletemanymerit(HttpServletRequest request) {
		String[] list = request.getParameterValues("dx");
		if (list != null) {
			for (int i = 0; i < list.length; i++) {
				try{
					Merit merit=mdR.findById(Integer.parseInt(list[i]));
					mdR.delete(merit);
				}catch(Exception e){

				}
			}
		}
		
		return "redirect:/todicmerit";
	}

	
	@RequestMapping("/deletemanyarea")
	public String deletemanyarea(HttpServletRequest request) {
		String[] list = request.getParameterValues("dx");
		if (list != null) {
			for (int i = 0; i < list.length; i++) {
				try{
				Area area=aR.findById(Integer.parseInt(list[i]));
				aR.delete(area);
				}catch(Exception e){
					
				}
			}
		}
		return "redirect:/todicarea";
	}
	
	@RequestMapping("/deletemanyduty")
	public String deletemanyduty(HttpServletRequest request) {
		String[] list = request.getParameterValues("dx");
		if (list != null) {
			for (int i = 0; i < list.length; i++) {
				try{
				Duty duty=dutyR.findById(Integer.parseInt(list[i]));
				dutyR.delete(duty);
				}catch(Exception e){
					
				}
			}
		}
		return "redirect:/todicduty";
	}
	
	@RequestMapping("/deletemanyoffice")
	public String deletemanyoffice(HttpServletRequest request) {
		String[] list = request.getParameterValues("dx");
		if (list != null) {
			for (int i = 0; i < list.length; i++) {
				try{
				OfficeInfo office=oR.findById(Integer.parseInt(list[i]));
				oR.delete(office);
				}catch(Exception e){
					
				}
			}
		}
		return "redirect:/todicoffice";
	}
	
	@RequestMapping("/deletemanystation")
	public String deletemanystation(HttpServletRequest request) {
		String[] list = request.getParameterValues("dx");
		if (list != null) {
			for (int i = 0; i < list.length; i++) {
				try{
				Station station=stationR.findById(Integer.parseInt(list[i]));
				stationR.delete(station);
				}catch(Exception e){
					
				}
			}
		}
		return "redirect:/todicstation";
	}
	
	@RequestMapping("/deletemanytransinfo")
	public String deletemanytransinfo(HttpServletRequest request) {
		String[] list = request.getParameterValues("dx");
		if (list != null) {
			for (int i = 0; i < list.length; i++) {
				try{
				Transinfo transinfo=tR.findById(Integer.parseInt(list[i]));
				tR.delete(transinfo);
				}catch(Exception e){
					
				}
			}
		}
		return "redirect:/todictransinfo";
	}
	
	@RequestMapping("/deletemanyreason")
	public String deletemanyreason(HttpServletRequest request) {
		String[] list = request.getParameterValues("dx");
		if (list != null) {
			for (int i = 0; i < list.length; i++) {
				try{
				ChangeReason reason=rR.findById(Integer.parseInt(list[i]));
				rR.delete(reason);
				}catch(Exception e){
					
				}
			}
		}
		return "redirect:/todicreason";
	}
	
	@RequestMapping("/deletemanydepart")
	public String deletemanydepart(HttpServletRequest request) {
		String[] list = request.getParameterValues("dx");
		if (list != null) {
			for (int i = 0; i < list.length; i++) {
				try{
				Department depart=dR.findById(Integer.parseInt(list[i]));
				dR.delete(depart);
				}catch(Exception e){
					
				}
			}
		}
		return "redirect:/todicdepart";
	}

	@RequestMapping("/deletemanycomplain")
	public String deletemanycomplain(HttpServletRequest request) {
		String[] list = request.getParameterValues("dx");
		if (list != null) {
			for (int i = 0; i < list.length; i++) {
				try{
					Complain complain=complainR.findById2(Integer.parseInt(list[i]));
					complainR.delete(complain);
				}catch(Exception e){

				}
			}
		}
		return "redirect:/toquerycomplain";
	}

	@RequestMapping("/deletemanypraise")
	public String deletemanypraise(HttpServletRequest request) {
		String[] list = request.getParameterValues("dx");
		if (list != null) {
			for (int i = 0; i < list.length; i++) {
				try{
					Praise praise=praiseR.findById2(Integer.parseInt(list[i]));
					praiseR.delete(praise);
				}catch(Exception e){

				}
			}
		}
		return "redirect:/toquerypraise";
	}

	@RequestMapping("/deletemanyadvice")
	public String deletemanyadvice(HttpServletRequest request) {
		String[] list = request.getParameterValues("dx");
		if (list != null) {
			for (int i = 0; i < list.length; i++) {
				try{
					Advice advice=adviceR.findById2(Integer.parseInt(list[i]));
					adviceR.delete(advice);
				}catch(Exception e){

				}
			}
		}
		return "redirect:/toqueryadvice";
	}

}
