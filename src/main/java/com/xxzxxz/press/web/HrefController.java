package com.xxzxxz.press.web;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.xxzxxz.press.model.*;
import com.xxzxxz.press.repository.AdviceRepository;
import com.xxzxxz.press.repository.AreaRepository;
import com.xxzxxz.press.repository.ComplainRepository;
import com.xxzxxz.press.repository.ComplaintypeRepository;
import com.xxzxxz.press.repository.ConsumerRepository;
import com.xxzxxz.press.repository.DeliverRepository;
import com.xxzxxz.press.repository.DeliverpartRepository;
import com.xxzxxz.press.repository.DepartRepository;
import com.xxzxxz.press.repository.DutyRepository;
import com.xxzxxz.press.repository.InfoRepository;
import com.xxzxxz.press.repository.InvoiceRepository;
import com.xxzxxz.press.repository.MeritRepository;
import com.xxzxxz.press.repository.OfficeRepository;
import com.xxzxxz.press.repository.OrdersRepository;
import com.xxzxxz.press.repository.PraiseRepository;
import com.xxzxxz.press.repository.PressRepository;
import com.xxzxxz.press.repository.ReasonRepository;
import com.xxzxxz.press.repository.ReciRepository;
import com.xxzxxz.press.repository.StaffRepository;
import com.xxzxxz.press.repository.StationRepository;
import com.xxzxxz.press.repository.TransinfoRepository;
import com.xxzxxz.press.repository.ZenkaRepository;

@Controller
public class HrefController {
	
	@Autowired
	private DepartRepository dR;

	@Autowired
	private DeliverRepository deliverRepository;

	@Autowired
	private OfficeRepository oR;

	@Autowired
	private ReasonRepository rR;

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
	private DeliverpartRepository dpR;
	@Autowired
	private MeritRepository meritR;
	@Autowired
	private InvoiceRepository invoiceR;
	@Autowired
	private PressRepository pressR;
	@Autowired
	private ZenkaRepository zR;
	@Autowired
	private StationRepository sR;
	@Autowired
	private ReciRepository rnR;
	
	@Autowired
	private ConsumerRepository consumerR;

	@Autowired
	private OrdersRepository ordersRepository;
	
	@Autowired
	private ComplaintypeRepository ctR;
	
	@Autowired
	private ComplainRepository comR;


	@Autowired
	private PraiseRepository pR;

	@Autowired
	private AdviceRepository adviceR;
	
	@RequestMapping("/todicdepart")
	public String todicDepart(@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "size", defaultValue = "6") Integer size,ModelMap model){
		
		List<String> listDepartName=CenterTool.getDepartNameList();
		model.addAttribute("listDepartName", listDepartName);
		
		Pageable pageable = PageRequest.of(page, size);
		Page<Object[]> list = dR.findAllShow(pageable);
		model.addAttribute("objectlist", list);
		
		return "dicdepart";
	}

	

	@RequestMapping("/tocenterindex")
	public String tocenterindex(){
		return "centerindex";
	}

	@RequestMapping("/todicoffice")
	public String todicOffice(@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "size", defaultValue = "6") Integer size,ModelMap model){
		List<String> listOfficeName=CenterTool.getOfficeNameList();
		model.addAttribute("listOfficeName", listOfficeName);
		
		Sort sort = new Sort(Sort.Direction.ASC, "id");
		Pageable pageable = PageRequest.of(page, size, sort);
		
		Page<OfficeInfo> list = oR.findAll(pageable);
		model.addAttribute("objectlist", list);
		return "dic/office/dicoffice";
	}



	@RequestMapping("/todicreason")
	public String todicReason(@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "size", defaultValue = "6") Integer size,ModelMap model){
		List<String> listReasonName=CenterTool.getReasonNameList();
		model.addAttribute("listReasonName", listReasonName);
		
		Sort sort = new Sort(Sort.Direction.ASC, "id");
		Pageable pageable = PageRequest.of(page, size, sort);
		Page<ChangeReason> list = rR.findAll(pageable);
		model.addAttribute("objectlist", list);
		return "dic/reason/dicreason";
	}



	@RequestMapping("/todictransinfo")
	public String todicTransinfo(@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "size", defaultValue = "6") Integer size,ModelMap model){
		List<String> listTransinfoName=CenterTool.getTransinfoNameList();
		model.addAttribute("listTransinfoName", listTransinfoName);
		
		Sort sort = new Sort(Sort.Direction.ASC, "id");
		Pageable pageable = PageRequest.of(page, size, sort);

		Page<Transinfo> list = tR.findAll(pageable);
		model.addAttribute("objectlist", list);
		
		
		return "dic/transinfo/dictransinfo";
	}


	@RequestMapping("/todicduty")
	public String todicDuty(@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "size", defaultValue = "6") Integer size,ModelMap model){
		List<String> listDutyName=CenterTool.getDutyNameList();
		model.addAttribute("listDutyName", listDutyName);
		
		Sort sort = new Sort(Sort.Direction.ASC, "id");
		Pageable pageable = PageRequest.of(page, size);
		
		Page<Object[]> list = dutyR.findAllShow(pageable);
		model.addAttribute("objectlist", list);
		
		return "dic/duty/dicduty";
	}



	@RequestMapping("/todicarea")
	public String todicArea(@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "size", defaultValue = "6") Integer size,ModelMap model){
		List<String> listAreaName=CenterTool.getAreaNameList();
		model.addAttribute("listAreaName", listAreaName);
		
		Sort sort = new Sort(Sort.Direction.ASC, "id");
		Pageable pageable = PageRequest.of(page, size, sort);
		
		Page<Area> list = aR.findAll(pageable);
		model.addAttribute("objectlist", list);
		
		return "dic/area/dicarea";
	}



	@RequestMapping("/toquerypress")
	public String toquerypress(ModelMap model){
		
		List<String> listPressName=CenterTool.getPressNameList();
		model.addAttribute("listPressName", listPressName);
		
		List<String> listOfficeName=CenterTool.getOfficeNameList();
		model.addAttribute("listOfficeName", listOfficeName);
		
		return "query/querypress";
	}

	@RequestMapping("/tocuorder")
	public String tocuorder(ModelMap model){
		List<Consumer> consumerlist=consumerR.findAll();

		model.addAttribute("consumerlist", consumerlist);
		return "query/querycuorder";
	}

	@RequestMapping("/toqueryorders")
	public String toqueryorders(ModelMap model){
		
		List<String> listPressName=CenterTool.getPressNameList();
		model.addAttribute("listPressName", listPressName);
		
		List<String> listConsumerName=CenterTool.getConsumerNameList();
		model.addAttribute("listConsumerName", listConsumerName);
		
		return "query/queryorders";
	}

	@RequestMapping("/toquerydaycharts")
	public String toquerydaycharts(ModelMap model){
		List<Station> stationlist=stationR.findAll();

		model.addAttribute("stationlist", stationlist);
		return "query/querydaycharts";
	}

	@RequestMapping("/toquerymonthcharts")
	public String toquerymonthcharts(){
		return "query/querymonthcharts";
	}

	@RequestMapping("/tototalxiadan")
	public String tototalxiadan(ModelMap model){
		List<Station> stationlist=stationR.findAll();
		model.addAttribute("stationlist", stationlist);
		return "total/totalxiadan";
	}

	@RequestMapping("/tototalyaobao")
	public String tototalyaobao(){
		return "total/totalyaobao";
	}

	@RequestMapping("/todicstation")
	public String todicStation(@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "size", defaultValue = "6") Integer size,ModelMap model){
		
		Pageable pageable = PageRequest.of(page, size);
		
		Page<Object[]> list = stationR.findAllStation(pageable);
		model.addAttribute("objectlist", list);
		
		List<String> listStationName=CenterTool.getStationNameList();
		model.addAttribute("listStationName", listStationName);
		return "dic/station/dicstation";
	}

	


	@RequestMapping("/toquerycon")
	public String toquerycon(ModelMap model){
		List<String> listConsumerName=CenterTool.getConsumerNameList();
		model.addAttribute("listConsumerName", listConsumerName);
		return "conservice/conmanage/querycon";
	}
	
	@RequestMapping("/tokefuindex")
	public String tokefuindex(){
		return "kefuindex";
	}
	
	@RequestMapping("/toquerycomplain")
	public String toquerycomplain(@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "size", defaultValue = "6") Integer size,ModelMap model){
		List<Consumer> consumerlist=consumerR.findAll();

		model.addAttribute("consumerlist", consumerlist);
		
		Pageable pageable = PageRequest.of(page, size);
		Page<Object[]> list = comR.findAllComplain(pageable);
		model.addAttribute("objectlist", list);
		
		return "conservice/complainmanage/querycomplain";
	}
	
	@RequestMapping("/tocomplainadd")
	public String tocomplainadd(@RequestParam(value = "errormessage", required = false, defaultValue = "") String errormessage,ModelMap model){
		List<Consumer> consumerlist=consumerR.findAll();
		model.addAttribute("consumerlist", consumerlist);
		List<Department> departlist=dR.findAll();
		model.addAttribute("departlist", departlist);
		List<Complaintype> complaintypelist=ctR.findAll();
		model.addAttribute("complaintypelist", complaintypelist);
		model.addAttribute("errormessage", errormessage);
		return "conservice/complainmanage/complainadd";
	}
	
	@RequestMapping("/toquerypraise")
	public String toquerypraise(@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "size", defaultValue = "6") Integer size,ModelMap model){
		List<Consumer> consumerlist=consumerR.findAll();

		model.addAttribute("consumerlist", consumerlist);
		
		Pageable pageable = PageRequest.of(page, size);
		Page<Object[]> list = pR.findAllPraise(pageable);
		model.addAttribute("objectlist", list);
		return "conservice/praisemanage/querypraise";
	}
	
	@RequestMapping("/topraiseadd")
	public String topraiseadd( @RequestParam(value = "errormessage", required = false, defaultValue = "") String errormessage,ModelMap model){
		List<Consumer> consumerlist=consumerR.findAll();
		model.addAttribute("consumerlist", consumerlist);
		List<Department> departlist=dR.findAll();
		model.addAttribute("departlist", departlist);
		model.addAttribute("errormessage", errormessage);
		return "conservice/praisemanage/praiseadd";
	}
	
	@RequestMapping("/toqueryadvice")
	public String toqueryadvice(@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "size", defaultValue = "6") Integer size,ModelMap model){
		List<Consumer> consumerlist=consumerR.findAll();
		model.addAttribute("consumerlist", consumerlist);
		
		Pageable pageable = PageRequest.of(page, size);

		Page<Object[]> list = adviceR.findAllAdvice(pageable);
		model.addAttribute("objectlist", list);
		
		return "conservice/advicemanage/queryadvice";
	}
	
	@RequestMapping("/toadviceadd")
	public String toadviceadd(@RequestParam(value = "errormessage", required = false, defaultValue = "") String errormessage,
			ModelMap model){
		List<Consumer> consumerlist=consumerR.findAll();
		model.addAttribute("consumerlist", consumerlist);
		List<Department> departlist=dR.findAll();
		model.addAttribute("departlist", departlist);
		
		model.addAttribute("errormessage", errormessage);
		
		return "conservice/advicemanage/adviceadd";
	}
	
}
