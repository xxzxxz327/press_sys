package com.xxzxxz.press.web;

import java.util.List;

import com.xxzxxz.press.model.Area;
import com.xxzxxz.press.repository.AreaRepository;
import com.xxzxxz.press.repository.DeliverRepository;
import com.xxzxxz.press.repository.OrdersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.xxzxxz.press.model.Deliver;
import com.xxzxxz.press.model.Orders;


@Controller
public class DeliverController {
	
	@Autowired
	private DeliverRepository deliverR;

	@Autowired
	private OrdersRepository ordersR;

	@Autowired
	private AreaRepository areaR;
	
	@RequestMapping("/todicdeliveraddress")
	public String todicdeliveraddress(){
		return "/deliver/dicdeliveraddress";
	}
	
	@RequestMapping("/dicdeliveraddress")
	public String dicdeliveraddress(@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "size", defaultValue = "6") Integer size, @RequestParam(value = "Id") Integer Id,
			ModelMap model){
		
		Pageable pageable = PageRequest.of(page, size);
		
		if(Id!=null){
			int deliverId=Id.intValue();
			Page<Object[]> list=deliverR.findByDeliverIdInCenter(deliverId, pageable);
			model.addAttribute("objectlist", list);
			return "/deliver/dicdeliveraddress";
		}
		
		Page<Object[]> list=deliverR.findDeliverInCenter(pageable);
		
		model.addAttribute("objectlist", list);
		return "/deliver/dicdeliveraddress";
	}
	
	@RequestMapping("/tochangedeliveraddress")
	public String tochangedeliveraddress(@RequestParam(value = "id") Integer Id,
			@RequestParam(value = "errormessage", required = false, defaultValue = "") String errormessage,
			ModelMap model){

		List<Area> arealist=areaR.findAll();
		model.addAttribute("arealist", arealist);

		int id = Id.intValue();
		
		List<Object[]> list=deliverR.findByDeliverIdInEdit(id);
		
		Object[] objects=list.get(0);
		model.addAttribute("objects", objects);

		String orderAddress="";
		//String addressDetail="";
		//String addressAreaName="";
		if(objects[2]==null){
			orderAddress="";


		}else{
			orderAddress=objects[2].toString();
		}
		String [] names = orderAddress.split("\\s+");
		try{
			model.addAttribute("addressAreaName", names[0]+' '+names[1]+' '+names[2]);
		}catch(Exception e){
			model.addAttribute("addressAreaName", "");
		}

		try{
			model.addAttribute("addressDetail", names[3]);
		}catch(Exception e){
			model.addAttribute("addressDetail", "");
		}

		model.addAttribute("errormessage", errormessage);
		
		return "/deliver/changedeliveraddress";
	}
	
	@RequestMapping("/changedeliveraddress")
	public String tochangedeliveraddress(@RequestParam(value = "deliverId") Integer deliverId,
			@RequestParam(value="orderId") Integer orderId,
			@RequestParam(value = "address", defaultValue = "") String address,
			ModelMap model){
		
		Deliver deliver=deliverR.findById(deliverId.intValue());
		
		Orders order=ordersR.findById(orderId.intValue());
		
		if(order!=null){
			
			try{
			order.setAddress(address);
			ordersR.save(order);
			deliver.setStatus(0);
			deliverR.save(deliver);
			}catch(Exception e){
				
			}
		}
		
		return "/deliver/dicdeliveraddress";
		
	}
	
}
