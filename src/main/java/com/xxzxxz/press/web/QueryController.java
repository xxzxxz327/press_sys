package com.xxzxxz.press.web;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import com.xxzxxz.press.model.*;
import com.xxzxxz.press.repository.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.xxzxxz.press.param.CuorderParam;
import com.xxzxxz.press.param.MonthSearchParam;
import com.xxzxxz.press.param.OrdersParam;
import com.xxzxxz.press.param.ReciSearchParam;

@Controller
public class QueryController {

	@Autowired
	private PressRepository pR;

	@Autowired
	private OrdersRepository oR;

	@Autowired
	private ReciRepository rR;

	@Autowired
	private DeliverRepository deliverR;

	@Autowired
	private ConsumerRepository consumerR;

	@Autowired
	private StationRepository stationR;
	
	@Autowired
	private ZenkaRepository zenkaR;
	
	@Autowired
	private PrintRepository printR;

	@Autowired
	private AreaRepository areaR;

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	@RequestMapping("/queryPress")
	public String getPress(@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "size", defaultValue = "6") Integer size,
			@RequestParam(value = "officeName") String officeName, @RequestParam(value = "pressName") String pressName,
			ModelMap model) {

		List<String> listPressName=CenterTool.getPressNameList();
		model.addAttribute("listPressName", listPressName);

		List<String> listOfficeName=CenterTool.getOfficeNameList();
		model.addAttribute("listOfficeName", listOfficeName);

		System.out.println("传入officeName为:");
		System.out.println(officeName);
		System.out.println("传入pressName为:");
		System.out.println(pressName);

		Pageable pageable = PageRequest.of(page, size);

		// 优先报刊名称查找
		if (pressName != "") {
			Page<Object[]> list = pR.findByPressName(pressName, pageable);
			model.addAttribute("objectlist", list);
			return "query/querypress";
		}

		if (officeName != "") {
			Page<Object[]> list = pR.findByOfficeName(officeName, pageable);
			model.addAttribute("objectlist", list);
			return "query/querypress";
		}

		Page<Object[]> list = pR.findAllPress(pageable);
		model.addAttribute("objectlist", list);

		return "query/querypress";
	}

	@RequestMapping("/querycuorder")
	public String getcuorder(@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "size", defaultValue = "6") Integer size, @Valid CuorderParam coP,
			BindingResult result, ModelMap model) {

		List<Consumer> consumerlist = consumerR.findAll();

		model.addAttribute("consumerlist", consumerlist);

		Pageable pageable = PageRequest.of(page, size);

		// Id和Time都存在的话联合查询
		if ((coP.getConsumerId() != null) && (coP.getStartTime() != null) && (coP.getEndTime() != null)) {

			Page<Object[]> list = oR.findByConsumerIdAndTime(coP.getConsumerId().intValue(),
					sdf.format(coP.getStartTime()), sdf.format(coP.getEndTime()), pageable);

			model.addAttribute("objectlist", list);

			return "query/querycuorder";
		}

		// 否则Id存在的话根据订户ID查询
		if (coP.getConsumerId() != null) {

			Page<Object[]> list = oR.findByConsumerIdShow(coP.getConsumerId(), pageable);

			model.addAttribute("objectlist", list);

			return "query/querycuorder";
		}

		// 订户ID不存在，开始时间与结束时间存在的话根据时间查询
		if ((coP.getStartTime() != null) && (coP.getEndTime() != null)) {

			Page<Object[]> list = oR.findByTimeShow(sdf.format(coP.getStartTime()), sdf.format(coP.getEndTime()),
					pageable);
			model.addAttribute("objectlist", list);

			return "query/querycuorder";
		}

		// 否则查询全部
		Page<Object[]> list = oR.findAllShow(pageable);
		model.addAttribute("objectlist", list);

		return "query/querycuorder";
	}
	
	@RequestMapping("/queryordersallcenter")
	public String queryordersallcenter(@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "size", defaultValue = "6") Integer size,ModelMap model){

		Pageable pageable = PageRequest.of(page, size);
		Page<Object[]> list = oR.findOrderAll(pageable);
		model.addAttribute("objectlist", list);
		
		List<String> listPressName = CenterTool.getPressNameList();
		model.addAttribute("listPressName", listPressName);

		List<String> listConsumerName = CenterTool.getConsumerNameList();
		model.addAttribute("listConsumerName", listConsumerName);
		
		return "query/queryorders";
	}
	

	@RequestMapping("/queryorders")
	public String getorders(@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "size", defaultValue = "6") Integer size,
			@RequestParam(value = "pressName") String pressName,
			@RequestParam(value = "consumerName") String consumerName, 
			@RequestParam(value = "orderId") Integer orderId,
			ModelMap model) {

		List<String> listPressName = CenterTool.getPressNameList();
		model.addAttribute("listPressName", listPressName);

		List<String> listConsumerName = CenterTool.getConsumerNameList();
		model.addAttribute("listConsumerName", listConsumerName);
		
//		System.out.println(pressName);
//		System.out.println(consumerName);
//		System.out.println(orderId);

		Pageable pageable = PageRequest.of(page, size);

		if (orderId != null) {
			//System.out.println("aaaaaaaaaaaaaaaaaaaaaa");
			Page<Object[]> list = oR.findOrdersByOrderId(orderId.intValue(), pageable);
			model.addAttribute("objectlist", list);
			return "query/queryorders";
			
		}

		if ((pressName != "") && (consumerName != "")) {
			//System.out.println("bbbbbbbbbbbbbbbbbbbbbbbbbbb");
			System.out.println("pressName="+pressName);
			System.out.println("consumerName="+consumerName);
			Page<Object[]> list = oR.findOrderByPressNameAndConsumerName(pressName, consumerName, pageable);
			model.addAttribute("objectlist", list);
			return "query/queryorders";

		}

		if (pressName != "") {
			//System.out.println("ccccccccccccccccccccccccccc");
			Page<Object[]> list = oR.findOrderByPressName(pressName, pageable);
			model.addAttribute("objectlist", list);
			return "query/queryorders";
		}

		if (consumerName != "") {
			//System.out.println("ddddddddddddddddddddddddddd");
			Page<Object[]> list = oR.findOrdersByConsumerName(consumerName, pageable);
			model.addAttribute("objectlist", list);
			return "query/queryorders";
		}
		//System.out.println("eeeeeeeeeeeeeeeeeeeeeeeeee");
		Page<Object[]> list = oR.findOrderAll(pageable);
		model.addAttribute("objectlist", list);
//		System.out.println("list size:");
//		System.out.println(list.getNumberOfElements());
		return "query/queryorders";
	}

	@RequestMapping("/toordersdetail")
	public String toordersdetail(@RequestParam(value = "id") Integer Id, ModelMap model) {

		int id = Id.intValue();

		Orders orders = oR.findById(id);
		
		if(orders.getPayType()==null){orders.setPayType(-1);}
		if(orders.getZenkaId()==null){orders.setZenkaId(-1);}
		if(orders.getStatus()==null){orders.setStatus(-1);}
		if(orders.getPrintId()==null){orders.setPrintId(-1);}

		model.addAttribute("orders", orders);
		
		String consumerName="";
		List<Consumer> listconsumer = consumerR.findAll();
		if (listconsumer != null && orders.getConsumerId()!=null) {
			for (int i = 0; i < listconsumer.size(); i++) {
				if(listconsumer.get(i).getId()==orders.getConsumerId()){
					consumerName=listconsumer.get(i).getName();
					break;
				}
			}
		}
		model.addAttribute("consumerName", consumerName);
		
		
		String pressName="";
		List<PressInfo> listPress=pR.findAll();
		if(listPress!=null && orders.getPressId()!=null){
			for(int i=0;i<listPress.size();i++){
				if(listPress.get(i).getId()==orders.getPressId()){
					pressName=listPress.get(i).getPressName();
					break;
				}
			}
		}
		model.addAttribute("pressName", pressName);
		
		
		String stationName="";
		List<Station> listStation=stationR.findAll();
		if(listStation!=null &&orders.getStationId()!=null){
			for(int i=0;i<listStation.size();i++){
				if(listStation.get(i).getId()==orders.getStationId()){
					stationName=listStation.get(i).getStationName();
					break;
				}
			}
		}
		model.addAttribute("stationName", stationName);
		
		List<Zenka> zenkalist=zenkaR.findAll();
		model.addAttribute("zenkalist", zenkalist);
		
		List<Print> printlist=printR.findAll();
		model.addAttribute("printlist", printlist);
		
		

		return "query/ordersdetail";
	}

	@RequestMapping("/toordersdetail1")
	public String toordersdetail1(int id, ModelMap model) {

		Orders orders = oR.findById(id);

		if(orders.getPayType()==null){orders.setPayType(-1);}
		if(orders.getZenkaId()==null){orders.setZenkaId(-1);}
		if(orders.getStatus()==null){orders.setStatus(-1);}
		if(orders.getPrintId()==null){orders.setPrintId(-1);}

		model.addAttribute("orders", orders);

		String consumerName="";
		List<Consumer> listconsumer = consumerR.findAll();
		if (listconsumer != null && orders.getConsumerId()!=null) {
			for (int i = 0; i < listconsumer.size(); i++) {
				if(listconsumer.get(i).getId()==orders.getConsumerId()){
					consumerName=listconsumer.get(i).getName();
					break;
				}
			}
		}
		model.addAttribute("consumerName", consumerName);


		String pressName="";
		List<PressInfo> listPress=pR.findAll();
		if(listPress!=null && orders.getPressId()!=null){
			for(int i=0;i<listPress.size();i++){
				if(listPress.get(i).getId()==orders.getPressId()){
					pressName=listPress.get(i).getPressName();
					break;
				}
			}
		}
		model.addAttribute("pressName", pressName);


		String stationName="";
		List<Station> listStation=stationR.findAll();
		if(listStation!=null &&orders.getStationId()!=null){
			for(int i=0;i<listStation.size();i++){
				if(listStation.get(i).getId()==orders.getStationId()){
					stationName=listStation.get(i).getStationName();
					break;
				}
			}
		}
		model.addAttribute("stationName", stationName);

		List<Zenka> zenkalist=zenkaR.findAll();
		model.addAttribute("zenkalist", zenkalist);

		List<Print> printlist=printR.findAll();
		model.addAttribute("printlist", printlist);



		return "query/ordersdetail2";
	}

	@RequestMapping("/toordersedit")
	public String toordersedit(@RequestParam(value = "id") Integer Id,
			@RequestParam(value = "errormessage", required = false, defaultValue = "") String errormessage,
			ModelMap model) {
		
		List<Consumer> consumerlist=consumerR.findAll();
		model.addAttribute("consumerlist", consumerlist);
		
		List<PressInfo> presslist=pR.findAll();
		model.addAttribute("presslist", presslist);
		
		List<Station> stationlist=stationR.findAll();
		model.addAttribute("stationlist", stationlist);
		
		List<Zenka> zenkalist=zenkaR.findAll();
		model.addAttribute("zenkalist", zenkalist);
		
		List<Print> printlist=printR.findAll();
		model.addAttribute("printlist", printlist);

		List<Area> arealist=areaR.findAll();
		model.addAttribute("arealist", arealist);

		int id = Id.intValue();

		Orders orders = oR.findById(id);
		
		if(orders.getPayType()==null){orders.setPayType(-1);}
		if(orders.getZenkaId()==null){orders.setZenkaId(-1);}
		if(orders.getStatus()==null){orders.setStatus(-1);}
		if(orders.getPrintId()==null){orders.setPrintId(-1);}
		
		
		model.addAttribute("orders", orders);

		String orderAddress=orders.getAddress();
		//String addressDetail="";
		//String addressAreaName="";
		if(orderAddress==null){
			orderAddress="";


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
		System.out.println(errormessage);

		return "query/ordersedit";
	}

	@RequestMapping("/ordersedit")
	public String ordersedit(@Valid OrdersParam oP, BindingResult result, ModelMap model, RedirectAttributes rA) {

		String errormessage = "";
		if (result.hasErrors()) {
			errormessage = "数据不合法，请输入正确的数据";
			rA.addAttribute("errormessage", errormessage);
			System.out.println(oP.getId().intValue());
			System.out.println(errormessage);
			rA.addAttribute("id", oP.getId());
			return "redirect:/toordersedit";
		}

		Orders orders = oR.findById(oP.getId().intValue());
		try {

			orders.setByParam(oP);

		} catch (Exception e) {
			errormessage = "数据转换错误，请输入正确的数据";
			rA.addAttribute("errormessage", errormessage);
			System.out.println(oP.getId().intValue());
			System.out.println(errormessage);
			rA.addAttribute("id", oP.getId());
			return "redirect:/toordersedit";
		}

		try {
			oR.save(orders);
		} catch (Exception e) {
			errormessage = "数据不合法，顾客、报刊、印点、分站、赠卡应已录入";
			rA.addAttribute("errormessage", errormessage);
			System.out.println(oP.getId().intValue());
			System.out.println(errormessage);
			rA.addAttribute("id", oP.getId());
			return "redirect:/toordersedit";
		}

		return "redirect:/queryordersallcenter";
	}

	@RequestMapping("/toordersdelete")
	public String toordersedit(@RequestParam(value = "id") Integer Id, ModelMap model) {

		Orders orders = oR.findById(Id.intValue());

		try {
			oR.delete(orders);
		} catch (Exception e) {

		}

		return "redirect:/queryordersallcenter";
	}

	@RequestMapping("/querydaycharts")
	public String querydaycharts(@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "size", defaultValue = "6") Integer size, @Valid ReciSearchParam rP,
			BindingResult result, ModelMap model) {

		List<Station> stationlist = stationR.findAll();

		model.addAttribute("stationlist", stationlist);

		String errormessage = "";
		if (result.hasErrors()) {
			List<ObjectError> list = result.getAllErrors();
			for (ObjectError error : list) {
				errormessage = errormessage + error.getDefaultMessage() + ";";
			}
			model.addAttribute("errormessage", errormessage);

			return "query/querydaycharts";

		}

		Pageable pageable = PageRequest.of(page, size);

		if ((rP.getStationId() != null) && (rP.getTimeMonth() != null)) {

			int stationId = rP.getStationId().intValue();
			Calendar c = Calendar.getInstance();
			c.setTime(rP.getTimeMonth());
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH) + 1;

			Page<Object[]> list = deliverR.findByStationIdAndTimeInCenter(stationId, year, month, pageable);
			model.addAttribute("objectlist", list);

		}

		return "query/querydaycharts";
	}

	@RequestMapping("/querymonthcharts")
	public String querymonthcharts(@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "size", defaultValue = "6") Integer size, @Valid MonthSearchParam msP,
			BindingResult result, ModelMap model) {

		String errormessage = "";
		if (result.hasErrors()) {
			List<ObjectError> list = result.getAllErrors();
			for (ObjectError error : list) {
				errormessage = errormessage + error.getDefaultMessage() + ";";
			}
			model.addAttribute("errormessage", errormessage);

			return "query/querymonthcharts";

		}

		Pageable pageable = PageRequest.of(page, size);

		String startTime = sdf.format(msP.getStartTime());
		String endTime = sdf.format(msP.getEndTime());

		Page<Object[]> list = oR.findMCByTime(startTime, endTime, pageable);
		model.addAttribute("objectlist", list);

		return "query/querymonthcharts";
	}

}
