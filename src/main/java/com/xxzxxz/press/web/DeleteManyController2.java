package com.xxzxxz.press.web;

import javax.servlet.http.HttpServletRequest;

import com.xxzxxz.press.model.*;
import com.xxzxxz.press.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class DeleteManyController2 {

	@Autowired
	private StaffRepository stR;

	@Autowired
	private DiscountRepository disR;

	@Autowired
	private PressInfoRepository pR;

	@Autowired
	private VoucherRepository vR;

	@Autowired
	private ZenkaRepository zR;

	@Autowired
	private AttributeRepository aR;


	@RequestMapping("/deletemanydiscount")
	public String deletemanydiscount(HttpServletRequest request) {
		System.out.println("xxxxxxxxxxxxxx");
		String[] list = request.getParameterValues("dx");
		String delimeter = "@";
		if (list != null) {
			for (int i = 0; i < list.length; i++) {
				try{
				String[] temp = list[i].split(delimeter);
				for(int j = 0;j<temp.length;j++)
					System.out.println(temp[j]);
				Discount discount=disR.find_one(Integer.parseInt(temp[0]), Integer.parseInt(temp[1]), Integer.parseInt(temp[2]));
				disR.delete(discount);
				}catch(Exception e){
					
				}
			}
		}
		return "redirect:/todicpressdiscount";
	}
		
	@RequestMapping("/deletemanypressprice")
	public String deletemanypressprice(HttpServletRequest request) {
		String[] list = request.getParameterValues("dx");
		if (list != null) {
			for (int i = 0; i < list.length; i++) {
				try{
				PressInfo press=pR.findById(Integer.parseInt(list[i]));
				press.setDayPrice(null);
				press.setWeekPrice(null);
				press.setMonthPrice(null);
				press.setSessionPrice(null);
				press.setHalfYearPrice(null);
				press.setYearPrice(null);
				pR.save(press);
				}catch(Exception e){
					
				}
			}
		}
		return "redirect:/todicpressprice";
	}


	@RequestMapping("/deletemanypressfrequency")
	public String deletemanypressfrequency(HttpServletRequest request) {
		String[] list = request.getParameterValues("dx");
		if (list != null) {
			for (int i = 0; i < list.length; i++) {
				try{
				PressInfo press=pR.findById(Integer.parseInt(list[i]));
				press.setFrequencyId(null);
				pR.save(press);
				}catch(Exception e){
					
				}
			}
		}
		return "redirect:/todicpressfrequency";
	}


	
	@RequestMapping("/deletemanyvoucher")
	public String deletemanyvoucher(HttpServletRequest request) {
		String[] list = request.getParameterValues("dx");
		if (list != null) {
			for (int i = 0; i < list.length; i++) {
				try{
				Voucher voucher=vR.findById(Integer.parseInt(list[i]));
				vR.delete(voucher);
				}catch(Exception e){
					
				}
			}
		}
		return "redirect:/todicvoucher";
	}
	
	@RequestMapping("/deletemanyzenka")
	public String deletemanyzenka(HttpServletRequest request) {
		String[] list = request.getParameterValues("dx");
		if (list != null) {
			for (int i = 0; i < list.length; i++) {
				try{
				Zenka zenka=zR.findById(Integer.parseInt(list[i]));
				zR.delete(zenka);
				}catch(Exception e){
					
				}
			}
		}
		return "redirect:/todiczenka";
	}
	
	@RequestMapping("/deletemanypress")
	public String deletemanypress(HttpServletRequest request) {
		String[] list = request.getParameterValues("dx");
		if (list != null) {
			for (int i = 0; i < list.length; i++) {
				try{
				PressInfo press=pR.findById(Integer.parseInt(list[i]));
				pR.delete(press);
				}catch(Exception e){
					
				}
			}
		}
		return "redirect:/todicpresssetting";
	}


	@RequestMapping("/deletemanyattribute")
	public String deletemanyattribute(HttpServletRequest request) {
		String[] list = request.getParameterValues("dx");
		if (list != null) {
			for (int i = 0; i < list.length; i++) {
				try{
					Attribute attr=aR.findByPressId(Integer.parseInt(list[i]));
					attr.setUnsub(0);
					attr.setChange(0);
					aR.save(attr);
				}catch(Exception e){

				}
			}
		}
		return "redirect:/todicattribute";
	}

	@RequestMapping("/deletemanystaff")
	public String deletemanystaff(HttpServletRequest request) {
		String[] list = request.getParameterValues("dx");
		if (list != null) {
			for (int i = 0; i < list.length; i++) {
				try {
					Staff staff = stR.findById(Integer.parseInt(list[i]));
					stR.delete(staff);
				} catch (Exception e) {

				}
			}
		}
		return "redirect:/todicstaff";
	}
}
