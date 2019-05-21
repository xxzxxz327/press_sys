package com.xxzxxz.press.web;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.xxzxxz.press.model.*;
import com.xxzxxz.press.repository.*;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.xxzxxz.press.param.AdviceParam;
import com.xxzxxz.press.param.ComplainParam;
import com.xxzxxz.press.param.PraiseParam;
import com.xxzxxz.press.param.QuestionParam;
import com.xxzxxz.press.param.QuestionitemParam;

@Controller
public class KefuController {
	@Autowired
	private ComplaintypeRepository ctR;

	@Autowired
	private ConsumerRepository cR;

	@Autowired
	private OrdersRepository oR;

	@Autowired
	private ComplainRepository comR;

	@Autowired
	private ComplaintypeRepository ctypeR;

	@Autowired
	private PraiseRepository pR;

	@Autowired
	private AdviceRepository aR;

	@Autowired
	private AskRepository askR;

	@Autowired
	private QuestionRepository questionR;

	@Autowired
	private QuestionitemRepository qitemR;

	@Autowired
	private StationRepository stationRepository;

	@Autowired
	private DepartmentRepository departmentRepository;

	@Autowired
	private ConsumerRepository consumerR;

	@Autowired
	private DepartRepository dR;

	public Integer findStationId(int departId){

		List<Department> departlist=departmentRepository.findAll();

		for(int i=0;i<departlist.size();i++){

			if(departId==departlist.get(i).getId()){
				return departlist.get(i).getStationId();
			}
		}

		return 0;
	}

	@RequestMapping("/querycomplain")
	public String querycomplain(@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "size", defaultValue = "6") Integer size,
			@RequestParam(value = "complainId") Integer ComplainId,
			@RequestParam(value = "consumerId") Integer ConsumerId, ModelMap model) {

		List<Consumer> consumerlist = consumerR.findAll();

		model.addAttribute("consumerlist", consumerlist);

		Pageable pageable = PageRequest.of(page, size);

		// 优先complainId查找
		if (ComplainId != null) {
			int complainId = ComplainId.intValue();
			Page<Object[]> list = comR.findBycomplainId(complainId, pageable);
			model.addAttribute("objectlist", list);

			return "conservice/complainmanage/querycomplain";

		}

		// complainId不存在的话根据consumerId查找
		if (ConsumerId != null) {
			if (ConsumerId != -1) {
				int consumerId = ConsumerId.intValue();
				Page<Object[]> list = comR.findByconsumerId(consumerId, pageable);
				model.addAttribute("objectlist", list);
				return "conservice/complainmanage/querycomplain";
			}
		}

		Page<Object[]> list = comR.findAllComplain(pageable);
		model.addAttribute("objectlist", list);

		return "conservice/complainmanage/querycomplain";
	}

	@RequestMapping("/toconordersitem")
	public String toconordersitem(@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "size", defaultValue = "6") Integer size, @RequestParam(value = "id") Integer Id,
			ModelMap model) {

		int id = Id.intValue();

		Consumer consumer = cR.findById(id);
		model.addAttribute("consumer", consumer);

		Pageable pageable = PageRequest.of(page, size);
		Page<Object[]> list = cR.findconordersitemByconId(id, pageable);
		model.addAttribute("objectlist", list);

		return "conservice/conmanage/conordersitem";
	}

	@RequestMapping("/toconordersdetail")
	public String toconordersdetail(@RequestParam(value = "id") Integer Id, ModelMap model) {

		int id = Id.intValue();

		Orders orders = oR.findById(id);

		model.addAttribute("orders", orders);

		return "conservice/conmanage/conordersdetail";
	}
	
	@RequestMapping("/queryconall")
	public String queryconall(@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "size", defaultValue = "6") Integer size,ModelMap model){
		List<String> listConsumerName=CenterTool.getConsumerNameList();
		model.addAttribute("listConsumerName", listConsumerName);
		Sort sort = new Sort(Sort.Direction.ASC, "id");
		Pageable pageable = PageRequest.of(page, size, sort);
		Page<Consumer> list = cR.findAll(pageable);
		model.addAttribute("objectlist", list);
		return "conservice/conmanage/querycon";
		
	}

	@RequestMapping("/querycon")
	public String querycon(@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "size", defaultValue = "6") Integer size,
			@RequestParam(value = "consumerName") String consumerName,
			ModelMap model) {
		
		List<String> listConsumerName=CenterTool.getConsumerNameList();
		model.addAttribute("listConsumerName", listConsumerName);

		Sort sort = new Sort(Sort.Direction.ASC, "id");
		Pageable pageable = PageRequest.of(page, size, sort);



		// 根据name查找
		if (consumerName != "") {
			Page<Consumer> list = cR.findByName(consumerName, pageable);
			model.addAttribute("objectlist", list);
			return "conservice/conmanage/querycon";
		}

		Page<Consumer> list = cR.findAll(pageable);
		model.addAttribute("objectlist", list);

		return "conservice/conmanage/querycon";
	}

	@RequestMapping("/complainadd")
	public String complainadd(@Valid ComplainParam complainP, BindingResult result, ModelMap model, RedirectAttributes rA) {

		String errormessage = "";
		if (result.hasErrors()) {
			List<ObjectError> list = result.getAllErrors();
			for (ObjectError error : list) {
				errormessage = errormessage + error.getDefaultMessage() + ";";
			}
			rA.addAttribute("errormessage", errormessage);
			return "redirect:/tocomplainadd";
		}

		Complain complain = new Complain();

		try {
			complain.setStatus(0);
			if (complainP.getConsumerId() != null) {
				complain.setConsumerId(complainP.getConsumerId());
			}
			if (complainP.getType() != null) {
				complain.setType(complainP.getType());
			}
			if (complainP.getContent() != null) {
				complain.setContent(complainP.getContent());
			}
			if (complainP.getDepartId() != null) {
				complain.setDepartId(complainP.getDepartId());
				complain.setStationId(findStationId(complainP.getDepartId()));
			}
			if (complainP.getOrderId() != null) {
				complain.setOrderId(complainP.getOrderId());
			}
		} catch (Exception e) {
			System.out.println("数据格式不正确");
			rA.addAttribute("errormessage", "数据格式不正确");
			return "redirect:/tocomplainadd";
		}

		try {
			comR.save(complain);
		} catch (Exception e) {
			System.out.println("请输入正确的数据,客户、部门与投诉类型应已录入");
			rA.addAttribute("errormessage", "请输入正确的数据,客户、部门与投诉类型应已录入");
			return "redirect:/tocomplainadd";
		}

		return "redirect:/findcomplainall";
	}

	@RequestMapping("/tocomplainedit")
	public String tocomplainedit(@RequestParam(value = "id") Integer Id,
			@RequestParam(value = "errormessage", required = false, defaultValue = "") String errormessage,
			ModelMap model) {

		List<Consumer> consumerlist = consumerR.findAll();
		model.addAttribute("consumerlist", consumerlist);
		List<Department> departlist = dR.findAll();
		model.addAttribute("departlist", departlist);
		List<Complaintype> complaintypelist = ctR.findAll();
		model.addAttribute("complaintypelist", complaintypelist);

		int id = Id.intValue();

		Complain complain = comR.findById(id);

		model.addAttribute("complain", complain);

		model.addAttribute("errormessage", errormessage);
		System.out.println(errormessage);

		return "conservice/complainmanage/complainedit";
	}

	@RequestMapping("/complainedit")
	public String complainedit(@Valid ComplainParam comP, BindingResult result, ModelMap model, RedirectAttributes rA) {
		String errormessage = "";
		if (result.hasErrors()) {
			List<ObjectError> list = result.getAllErrors();
			for (ObjectError error : list) {
				errormessage = errormessage + error.getDefaultMessage() + ";";
			}
			rA.addAttribute("errormessage", errormessage);
			System.out.println(comP.getId().intValue());
			rA.addAttribute("id", comP.getId());
			return "redirect:/tocomplainedit";
		}

		Complain complain = comR.findById2(comP.getId());
		try {
			complain.setId(comP.getId());
			if (comP.getConsumerId() != null) {
				complain.setConsumerId(comP.getConsumerId());
			}
			if (comP.getType() != null) {
				complain.setType(comP.getType());
			}
			if (comP.getContent() != null) {
				complain.setContent(comP.getContent());
			}
			if (comP.getStatus() != null) {
				complain.setStatus(comP.getStatus());
			}
			if (comP.getDepartId() != null) {
				complain.setDepartId(comP.getDepartId());
				complain.setStationId(findStationId(comP.getDepartId()));

			}
			if (comP.getOrderId() != null) {
				complain.setOrderId(comP.getOrderId());
			}

		} catch (Exception e) {
			System.out.println("数据格式不正确");
			errormessage = "数据格式不正确";
			rA.addAttribute("errormessage", errormessage);
			rA.addAttribute("id", comP.getId());
			return "redirect:/tocomplainedit";
		}

		try {
			comR.save(complain);
		} catch (Exception e) {
			System.out.println("请输入正确的数据,客户、部门与投诉类型应已录入");
			errormessage = "请输入正确的数据,客户、部门与投诉类型应已录入";
			rA.addAttribute("errormessage", errormessage);
			rA.addAttribute("id", comP.getId());
			return "redirect:/tocomplainedit";
		}

		return "redirect:/findcomplainall";
	}

	@RequestMapping("/tocomplaindelete")
	public String tocomplaindelete(@RequestParam(value = "id") Integer Id, ModelMap model) {

		int id = Id.intValue();

		Complain complain = comR.findById(id);

		try {
			comR.delete(complain);
		} catch (Exception e) {

		}
		return "redirect:/findcomplainall";
	}

	@RequestMapping("/uploadcomplain")
	public String uploadcomplain(Model model, @RequestParam("file") MultipartFile file,
			RedirectAttributes redirectAttributes) throws Exception {
		XSSFWorkbook book = new XSSFWorkbook(file.getInputStream());
		XSSFSheet sheet = book.getSheetAt(0);
		for (int i = 1; i < sheet.getLastRowNum() + 1; i++) {
			Complain complain = new Complain();
			XSSFRow row = sheet.getRow(i);
			try {
				complain.setConsumerId((int) row.getCell(0).getNumericCellValue());
			} catch (Exception e) {
				System.out.println("数据转换错误");
			}

			try {
				complain.setType((int) row.getCell(1).getNumericCellValue());
			} catch (Exception e) {
				System.out.println("数据转换错误");
			}

			try {
				complain.setContent(row.getCell(2).getStringCellValue());
			} catch (Exception e) {
				System.out.println("数据转换错误");
			}

			try {
				complain.setDepartId((int) row.getCell(3).getNumericCellValue());
				complain.setStationId(findStationId((int) row.getCell(3).getNumericCellValue()));
			} catch (Exception e) {
				System.out.println("数据转换错误");
			}

			try {
				complain.setOrderId((int) row.getCell(4).getNumericCellValue());
			} catch (Exception e) {
				System.out.println("数据转换错误");
			}

			complain.setStatus(0);

			try {
				comR.save(complain);
			} catch (Exception e) {
				System.out.println("存储失败");
			}
		}

		return "redirect:/toquerycomplain";
	}

	@RequestMapping("/tocomplaintypemanage")
	public String tocomplaintypemanage(@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "size", defaultValue = "6") Integer size,
			@RequestParam(value = "errormessage", required = false, defaultValue = "") String errormessage,
			ModelMap model) {
		Sort sort = new Sort(Sort.Direction.ASC, "id");
		Pageable pageable = PageRequest.of(page, size, sort);
		Page<Complaintype> list = ctypeR.findAll(pageable);
		model.addAttribute("errormessage", errormessage);
		model.addAttribute("objectlist", list);
		return "conservice/complainmanage/complaintypemanage";
	}

	@RequestMapping("/complaintypeadd")
	public String complaintypeadd(@RequestParam(value = "name") String name, RedirectAttributes rA) {

		if (name != "") {
			Complaintype ct = new Complaintype();

			ct.setName(name);
			try {
				ctypeR.save(ct);
			} catch (Exception e) {
				rA.addAttribute("errormessage", "投诉类型名称不能重复");
			}
		} else {
			rA.addAttribute("errormessage", "投诉类型名称不能为空");
		}

		return "redirect:/tocomplaintypemanage";
	}

	@RequestMapping("/tocomplaintypedelete")
	public String tocomplaintypedelete(@RequestParam(value = "id") Integer Id, ModelMap model) {

		int id = Id.intValue();

		Complaintype ctype = ctypeR.findById(id);

		try {
			ctypeR.delete(ctype);
		} catch (Exception e) {

		}
		return "redirect:/tocomplaintypemanage";
	}

	@RequestMapping("/tocomplaintypeedit")
	public String tocomplaintypeedit(@RequestParam(value = "id") Integer Id,
			@RequestParam(value = "errormessage", required = false, defaultValue = "") String errormessage,
			ModelMap model) {

		int id = Id.intValue();

		Complaintype ctype = ctypeR.findById(id);

		model.addAttribute("complaintype", ctype);
		model.addAttribute("errormessage", errormessage);
		return "conservice/complainmanage/complaintypeedit";
	}

	@RequestMapping("/complaintypeedit")
	public String complaintypeedit(@RequestParam(value = "id") Integer Id, @RequestParam(value = "name") String name,
			ModelMap model, RedirectAttributes rA) {

		Complaintype ctype = new Complaintype();
		ctype.setId(Id.intValue());

		if (name == "") {
			rA.addAttribute("errormessage", "投诉类型名称不能为空");
			rA.addAttribute("id", Id.intValue());
			return "redirect:/tocomplaintypeedit";
		}

		ctype.setName(name);

		try {
			ctypeR.save(ctype);
		} catch (Exception e) {
			rA.addAttribute("errormessage", "投诉类型名称不能重复");
			rA.addAttribute("id", Id.intValue());
			return "redirect:/tocomplaintypeedit";
		}

		return "redirect:/tocomplaintypemanage";
	}

	@RequestMapping("/querypraise")
	public String querypraise(@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "size", defaultValue = "6") Integer size,
			@RequestParam(value = "praiseId") Integer Id, @RequestParam(value = "consumerId") Integer ConsumerId,
			ModelMap model) {
		List<Consumer> consumerlist = consumerR.findAll();

		model.addAttribute("consumerlist", consumerlist);

		Pageable pageable = PageRequest.of(page, size);

		// 优先prasieId查找
		if (Id != null) {
			int praiseId = Id.intValue();
			Page<Object[]> list = pR.findByPraiseId(praiseId, pageable);
			model.addAttribute("objectlist", list);

			return "conservice/praisemanage/querypraise";

		}

		// praiseId不存在的话根据consumerId查找
		if (ConsumerId != null) {
			if (ConsumerId != -1) {
				int consumerId = ConsumerId.intValue();
				Page<Object[]> list = pR.findByConsumerId(consumerId, pageable);
				model.addAttribute("objectlist", list);
				return "conservice/praisemanage/querypraise";
			}
		}

		Page<Object[]> list = pR.findAllPraise(pageable);
		model.addAttribute("objectlist", list);

		return "conservice/praisemanage/querypraise";
	}

	@RequestMapping("/praiseadd")
	public String praiseadd(@Valid PraiseParam praiseP, BindingResult result, ModelMap model, RedirectAttributes rA) {

		String errormessage = "";
		if (result.hasErrors()) {
			List<ObjectError> list = result.getAllErrors();
			for (ObjectError error : list) {
				errormessage = errormessage + error.getDefaultMessage() + ";";
			}
			rA.addAttribute("errormessage", errormessage);
			return "redirect:/topraiseadd";
		}

		Praise praise = new Praise();
		Department department = departmentRepository.findById(praiseP.getDepartId().intValue());
		try {
			praise.setByParam(praiseP);
			praise.setStatus(0);
			praise.setStationId(department.getStationId());
		} catch (Exception e) {
			System.out.println("数据格式不正确");
			rA.addAttribute("errormessage", "数据格式不正确");
			return "redirect:/topraiseadd";
		}

		try {
			pR.save(praise);
		} catch (Exception e) {
			System.out.println("请输入正确的数据,客户、部门应已录入");
			rA.addAttribute("errormessage", "请输入正确的数据,客户、部门应已录入");
			return "redirect:/topraiseadd";
		}

		return "redirect:/findpraiseall";
	}

	@RequestMapping("/topraiseedit")
	public String topraiseedit(@RequestParam(value = "id") Integer Id,
			@RequestParam(value = "errormessage", required = false, defaultValue = "") String errormessage,
			ModelMap model) {

		List<Consumer> consumerlist = consumerR.findAll();
		model.addAttribute("consumerlist", consumerlist);
		List<Department> departlist = dR.findAll();
		model.addAttribute("departlist", departlist);

		int id = Id.intValue();

		Praise praise = pR.findById2(id);

		model.addAttribute("praise", praise);

		model.addAttribute("errormessage", errormessage);
		System.out.println(errormessage);

		return "conservice/praisemanage/praiseedit";
	}

	@RequestMapping("/praiseedit")
	public String areaedit(@Valid PraiseParam PP, BindingResult result, ModelMap model, RedirectAttributes rA) {
		String errormessage = "";
		if (result.hasErrors()) {
			List<ObjectError> list = result.getAllErrors();
			for (ObjectError error : list) {
				errormessage = errormessage + error.getDefaultMessage() + ";";
			}
			rA.addAttribute("errormessage", errormessage);
			System.out.println(PP.getId().intValue());
			rA.addAttribute("id", PP.getId());
			return "redirect:/topraiseedit";
		}

		Praise praise = pR.findById2(PP.getId().intValue());
		Department department = departmentRepository.findById(PP.getDepartId().intValue());
		try {
			praise.setByParam(PP);
			praise.setId(PP.getId());
			praise.setStationId(department.getStationId());
		} catch (Exception e) {
			System.out.println("数据格式不正确");
			errormessage = "数据格式不正确";
			rA.addAttribute("errormessage", errormessage);
			rA.addAttribute("id", PP.getId());
			return "redirect:/topraiseedit";
		}

		try {
			pR.save(praise);
		} catch (Exception e) {
			System.out.println("请输入正确的数据,客户、部门应已录入");
			errormessage = "请输入正确的数据,客户、部门应已录入";
			rA.addAttribute("errormessage", errormessage);
			rA.addAttribute("id", PP.getId());
			return "redirect:/topraiseedit";
		}

		return "redirect:/findpraiseall";
	}

	@RequestMapping("/topraisedelete")
	public String topraisedelete(@RequestParam(value = "id") Integer Id, ModelMap model) {

		int id = Id.intValue();

		Praise praise = pR.findById2(id);

		try {
			pR.delete(praise);
		} catch (Exception e) {

		}
		return "redirect:/findpraiseall";
	}

	@RequestMapping("/uploadpraise")
	public String uploadpraise(Model model, @RequestParam("file") MultipartFile file,
			RedirectAttributes redirectAttributes) throws Exception {
		XSSFWorkbook book = new XSSFWorkbook(file.getInputStream());
		XSSFSheet sheet = book.getSheetAt(0);
		for (int i = 1; i < sheet.getLastRowNum() + 1; i++) {
			Praise praise = new Praise();
			XSSFRow row = sheet.getRow(i);
			try {
				praise.setConsumerId((int) row.getCell(0).getNumericCellValue());
			} catch (Exception e) {
				System.out.println("数据转换错误");
			}

			try {
				praise.setContent(row.getCell(1).getStringCellValue());
			} catch (Exception e) {
				System.out.println("数据转换错误");
			}

			try {
				praise.setDepartId((int) row.getCell(2).getNumericCellValue());
			} catch (Exception e) {
				System.out.println("数据转换错误");
			}

			try {
				praise.setOrderId((int) row.getCell(3).getNumericCellValue());
			} catch (Exception e) {
				System.out.println("数据转换错误");
			}

			try {
				praise.setStationId((int) row.getCell(4).getNumericCellValue());
			} catch (Exception e) {
				System.out.println("数据转换错误");
			}

			praise.setStatus(0);

			try {
				pR.save(praise);
			} catch (Exception e) {
				System.out.println("存储失败");
			}
		}

		return "redirect:/toquerypraise";
	}

	@RequestMapping("/findadviceall")
	public String findadviceall(@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "size", defaultValue = "6") Integer size, ModelMap model) {

		Pageable pageable = PageRequest.of(page, size);
		Page<Object[]> list = aR.findAllAdvice(pageable);
		model.addAttribute("objectlist", list);

		List<Consumer> consumerlist = consumerR.findAll();
		model.addAttribute("consumerlist", consumerlist);

		return "conservice/advicemanage/queryadvice";

	}

	@RequestMapping("/findpraiseall")
	public String findpraiseall(@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "size", defaultValue = "6") Integer size, ModelMap model) {

		Pageable pageable = PageRequest.of(page, size);
		Page<Object[]> list = pR.findAllPraise(pageable);
		model.addAttribute("objectlist", list);

		List<Consumer> consumerlist = consumerR.findAll();
		model.addAttribute("consumerlist", consumerlist);

		return "conservice/praisemanage/querypraise";

	}

	@RequestMapping("/findcomplainall")
	public String findcomplainall(@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "size", defaultValue = "6") Integer size, ModelMap model) {

		Pageable pageable = PageRequest.of(page, size);
		Page<Object[]> list = comR.findAllComplain(pageable);
		model.addAttribute("objectlist", list);

		List<Consumer> consumerlist = consumerR.findAll();
		model.addAttribute("consumerlist", consumerlist);

		return "conservice/complainmanage/querycomplain";

	}

	@RequestMapping("/findcomplaintypeall")
	public String findcomplaintypeall(@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "size", defaultValue = "6") Integer size, ModelMap model) {

		Pageable pageable = PageRequest.of(page, size);
		List<Complaintype> list = ctypeR.findAll();
		model.addAttribute("objectlist", list);

		return "conservice/complainmanage/complaintypemanage";

	}

	@RequestMapping("/queryadvice")
	public String queryadvice(@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "size", defaultValue = "6") Integer size,
			@RequestParam(value = "adviceId") Integer Id, @RequestParam(value = "consumerId") Integer ConsumerId,
			ModelMap model) {

		List<Consumer> consumerlist = consumerR.findAll();

		model.addAttribute("consumerlist", consumerlist);

		Pageable pageable = PageRequest.of(page, size);

		// 优先prasieId查找
		if (Id != null) {
			int adviceId = Id.intValue();
			Page<Object[]> list = aR.findByAdviceId(adviceId, pageable);
			model.addAttribute("objectlist", list);

			return "conservice/advicemanage/queryadvice";

		}

		// adviceId不存在的话根据consumerId查找
		if (ConsumerId != null) {
			if (ConsumerId != -1) {
				int consumerId = ConsumerId.intValue();
				Page<Object[]> list = aR.findByConsumerId(consumerId, pageable);
				model.addAttribute("objectlist", list);
				return "conservice/advicemanage/queryadvice";
			}
		}

		Page<Object[]> list = aR.findAllAdvice(pageable);
		model.addAttribute("objectlist", list);

		return "conservice/advicemanage/queryadvice";
	}

	@RequestMapping("/adviceadd")
	public String adviceadd(@Valid AdviceParam adviceP, BindingResult result, ModelMap model, RedirectAttributes rA) {

		String errormessage = "";
		if (result.hasErrors()) {
			List<ObjectError> list = result.getAllErrors();
			for (ObjectError error : list) {
				errormessage = errormessage + error.getDefaultMessage() + ";";
			}
			rA.addAttribute("errormessage", errormessage);
			return "redirect:/toadviceadd";
		}

		Advice advice = new Advice();

		try {
			advice.setByParam(adviceP);
			advice.setStatus(0);
			advice.setStationId(findStationId(adviceP.getDepartId()));
		} catch (Exception e) {
			System.out.println("数据格式不正确");
			rA.addAttribute("errormessage", "数据格式不正确");
			return "redirect:/toadviceadd";
		}

		try {
			aR.save(advice);
		} catch (Exception e) {
			System.out.println("请输入正确的数据,客户、部门应已录入");
			rA.addAttribute("errormessage", "请输入正确的数据,客户、部门应已录入");
			return "redirect:/toadviceadd";
		}

		return "redirect:/findadviceall";
	}

	@RequestMapping("/toadviceedit")
	public String toadviceedit(@RequestParam(value = "id") Integer Id,
			@RequestParam(value = "errormessage", required = false, defaultValue = "") String errormessage,
			ModelMap model) {

		List<Consumer> consumerlist = consumerR.findAll();
		model.addAttribute("consumerlist", consumerlist);
		List<Department> departlist = dR.findAll();
		model.addAttribute("departlist", departlist);

		int id = Id.intValue();

		Advice advice = aR.findById(id);

		model.addAttribute("advice", advice);

		model.addAttribute("errormessage", errormessage);
		System.out.println(errormessage);

		return "conservice/advicemanage/adviceedit";
	}

	@RequestMapping("/adviceedit")
	public String areaedit(@Valid AdviceParam AP, BindingResult result, ModelMap model, RedirectAttributes rA) {
		String errormessage = "";
		if (result.hasErrors()) {
			List<ObjectError> list = result.getAllErrors();
			for (ObjectError error : list) {
				errormessage = errormessage + error.getDefaultMessage() + ";";
			}
			rA.addAttribute("errormessage", errormessage);
			System.out.println(AP.getId().intValue());
			rA.addAttribute("id", AP.getId());
			return "redirect:/toadviceedit";
		}

		Advice advice = new Advice();
		try {
			advice.setByParam(AP);
			advice.setId(AP.getId());
			advice.setStationId(findStationId(AP.getDepartId()));
		} catch (Exception e) {
			System.out.println("数据格式不正确");
			errormessage = "数据格式不正确";
			rA.addAttribute("errormessage", errormessage);
			rA.addAttribute("id", AP.getId());
			return "redirect:/toadviceedit";
		}

		try {
			aR.save(advice);
		} catch (Exception e) {
			System.out.println("请输入正确的数据,客户、部门应已录入");
			errormessage = "请输入正确的数据,客户、部门应已录入";
			rA.addAttribute("errormessage", errormessage);
			rA.addAttribute("id", AP.getId());
			return "redirect:/toadviceedit";
		}

		return "redirect:/findadviceall";
	}

	@RequestMapping("/toadvicedelete")
	public String toadvicedelete(@RequestParam(value = "id") Integer Id, ModelMap model) {

		int id = Id.intValue();

		Advice advice = aR.findById(id);

		try {
			aR.delete(advice);
		} catch (Exception e) {

		}
		return "redirect:/findadviceall";
	}

	@RequestMapping("/uploadadvice")
	public String uploadadvice(Model model, @RequestParam("file") MultipartFile file,
			RedirectAttributes redirectAttributes) throws Exception {
		XSSFWorkbook book = new XSSFWorkbook(file.getInputStream());
		XSSFSheet sheet = book.getSheetAt(0);
		for (int i = 1; i < sheet.getLastRowNum() + 1; i++) {
			Advice advice = new Advice();
			XSSFRow row = sheet.getRow(i);
			try {
				advice.setConsumerId((int) row.getCell(0).getNumericCellValue());
			} catch (Exception e) {
				System.out.println("数据转换错误");
			}

			try {
				advice.setContent(row.getCell(1).getStringCellValue());
			} catch (Exception e) {
				System.out.println("数据转换错误");
			}

			try {
				advice.setDepartId((int) row.getCell(2).getNumericCellValue());
				advice.setStationId(findStationId((int) row.getCell(2).getNumericCellValue()));
			} catch (Exception e) {
				System.out.println("数据转换错误");
			}

			try {
				advice.setOrderId((int) row.getCell(3).getNumericCellValue());
			} catch (Exception e) {
				System.out.println("数据转换错误");
			}

			advice.setStatus(0);

			try {
				aR.save(advice);
			} catch (Exception e) {
				System.out.println("存储失败");
			}
		}

		return "redirect:/toqueryadvice";
	}

	@RequestMapping("/toqueryask")
	public String toqueryask(@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "size", defaultValue = "6") Integer size,
			@RequestParam(value = "errormessage", required = false, defaultValue = "") String errormessage,
			ModelMap model) {

		Sort sort = new Sort(Sort.Direction.ASC, "orderid");
		Pageable pageable = PageRequest.of(page, size, sort);

		Page<Ask> list = askR.findAll(pageable);
		model.addAttribute("errormessage", errormessage);
		model.addAttribute("objectlist", list);

		return "conservice/askmanage/queryask";
	}

	@RequestMapping("/askadd")
	public String askadd(@RequestParam(value = "name") String name, @RequestParam(value = "orderid") Integer orderid,
			RedirectAttributes rA) {

		if (!((name != "") && (orderid != null))) {

			rA.addAttribute("errormessage", "问卷名称与问卷编号不能为空");
			return "redirect:/toqueryask";
		}

		Ask ask = new Ask();
		try {
			ask.setName(name);
			ask.setOrderid(orderid.intValue());
		} catch (Exception e) {
			rA.addAttribute("errormessage", "请输入正确格式的问卷名称与问卷编号");
			return "redirect:/toqueryask";
		}

		try {
			askR.save(ask);
		} catch (Exception e) {
			rA.addAttribute("errormessage", "问卷名称不能重复");
			return "redirect:/toqueryask";
		}

		return "redirect:/toqueryask";
	}

	@RequestMapping("/toaskedit")
	public String toaskedit(@RequestParam(value = "id") Integer id,
			@RequestParam(value = "errormessage", required = false, defaultValue = "") String errormessage,
			ModelMap model) {

		Ask ask = askR.findById(id.intValue());

		model.addAttribute("ask", ask);
		model.addAttribute("errormessage", errormessage);

		List<Question> list = questionR.findByAskIdOrderByOrderidAsc(id.intValue());
		model.addAttribute("objectlist", list);
		return "conservice/askmanage/askedit";
	}

	@RequestMapping("/askedit")
	public String askedit(@RequestParam(value = "id") Integer id, @RequestParam(value = "name") String name,
			@RequestParam(value = "orderid") Integer orderid, RedirectAttributes rA) {

		rA.addAttribute("id", id);
		if ((id == null) || (name == "") || (orderid == null)) {
			rA.addAttribute("errormessage", "信息不能为空");
			return "redirect:/toaskedit";
		}

		Ask ask = new Ask();
		try {
			ask.setId(id.intValue());
			ask.setName(name);
			ask.setOrderid(orderid.intValue());
		} catch (Exception e) {
			rA.addAttribute("errormessage", "请输入正确格式的信息");
			return "redirect:/toaskedit";
		}

		try {
			askR.save(ask);
		} catch (Exception e) {
			rA.addAttribute("errormessage", "问卷名称不能重复");
			return "redirect:/toaskedit";
		}

		// 成功的话返回查询
		return "redirect:/toqueryask";
	}

	@RequestMapping("/toaskdelete")
	public String toaskdelete(@RequestParam(value = "id") Integer Id, RedirectAttributes rA) {

		int id = Id.intValue();

		Ask ask = askR.findById(Id.intValue());

		try {
			askR.delete(ask);
		} catch (Exception e) {
			rA.addAttribute("errormessage", "请先删除该问卷下的题目");
		}
		return "redirect:/toqueryask";
	}

	@RequestMapping("/toquestiondelete")
	public String toquestiondelete(@RequestParam(value = "id") Integer Id, RedirectAttributes rA) {

		int id = Id.intValue();

		Question question = questionR.findById(id);

		try {
			questionR.delete(question);
		} catch (Exception e) {
			rA.addAttribute("errormessage", "请先删除该题目下的选项");
		}

		rA.addAttribute("id", question.getAskId());
		return "redirect:/toaskedit";
	}

	@RequestMapping("/toquestionedit")
	public String toquestionedit(@RequestParam(value = "id") Integer id,
			@RequestParam(value = "errormessage", required = false, defaultValue = "") String errormessage,
			ModelMap model) {

		Question question = questionR.findById(id.intValue());

		model.addAttribute("question", question);
		model.addAttribute("errormessage", errormessage);

		return "conservice/askmanage/questionedit";
	}

	@RequestMapping("/questionedit")
	public String questionedit(@Valid QuestionParam QP, BindingResult result, RedirectAttributes rA) {

		String errormessage = "";
		if (result.hasErrors()) {
			List<ObjectError> list = result.getAllErrors();
			for (ObjectError error : list) {
				errormessage = errormessage + error.getDefaultMessage() + ";";
			}
			rA.addAttribute("errormessage", errormessage);
			rA.addAttribute("id", QP.getId());
			return "redirect:/toquestionedit";
		}

		Question question = new Question();
		try {
			question.setByParam(QP);
		} catch (Exception e) {
			rA.addAttribute("errormessage", "请输入正确格式的信息");
			rA.addAttribute("id", QP.getId());
			return "redirect:/toquestionedit";
		}

		try {
			questionR.save(question);
		} catch (Exception e) {
			rA.addAttribute("errormessage", "请输入完整的信息");
			rA.addAttribute("id", QP.getId());
			return "redirect:/toquestionedit";
		}

		// 成功的话返回问卷编辑
		rA.addAttribute("id", QP.getAskId());
		return "redirect:/toaskedit";
	}

	@RequestMapping("/toquestionitemedit")
	public String toquestionitemedit(@RequestParam(value = "id") Integer id,
			@RequestParam(value = "errormessage", required = false, defaultValue = "") String errormessage,
			ModelMap model) {

		Question question = questionR.findById(id.intValue());

		model.addAttribute("question", question);
		model.addAttribute("errormessage", errormessage);

		List<Questionitem> list = qitemR.findByQuestionIdOrderByOrderidAsc(id.intValue());
		model.addAttribute("objectlist", list);

		return "conservice/askmanage/questionitemedit";
	}

	@RequestMapping("/questionitemadd")
	public String questionitemadd(@Valid QuestionitemParam QitemP, BindingResult result, ModelMap model,
			RedirectAttributes rA) {

		String errormessage = "";
		if (result.hasErrors()) {
			List<ObjectError> list = result.getAllErrors();
			for (ObjectError error : list) {
				errormessage = errormessage + error.getDefaultMessage() + ";";
			}
			rA.addAttribute("errormessage", errormessage);
			rA.addAttribute("id", QitemP.getQuestionId());
			return "redirect:/toquestionitemedit";
		}

		Questionitem qitem = new Questionitem();
		try {
			qitem.setByParam(QitemP);
		} catch (Exception e) {
			rA.addAttribute("errormessage", "请输入正确格式的信息");
			rA.addAttribute("id", QitemP.getQuestionId());
			return "redirect:/toquestionitemedit";
		}

		try {
			qitemR.save(qitem);
		} catch (Exception e) {
			rA.addAttribute("errormessage", "请输入正确的信息,问题应已录入");
			rA.addAttribute("id", QitemP.getQuestionId());
			return "redirect:/toquestionitemedit";
		}

		rA.addAttribute("id", QitemP.getQuestionId());
		return "redirect:/toquestionitemedit";
	}

	@RequestMapping("/toquestionitemdelete")
	public String toquestionitemdelete(@RequestParam(value = "id") Integer Id, RedirectAttributes rA) {

		int id = Id.intValue();

		Questionitem qitem = qitemR.findById(id);

		try {
			qitemR.delete(qitem);
		} catch (Exception e) {
			rA.addAttribute("errormessage", "删除选项失败");
		}

		rA.addAttribute("id", qitem.getQuestionId());
		return "redirect:/toquestionitemedit";
	}

	@RequestMapping("/toquestioniteminfoedit")
	public String toquestioniteminfoedit(@RequestParam(value = "id") Integer Id,
			@RequestParam(value = "errormessage", required = false, defaultValue = "") String errormessage,
			ModelMap model) {

		int id = Id.intValue();
		Questionitem qitem = qitemR.findById(id);
		model.addAttribute("questionitem", qitem);
		model.addAttribute("errormessage", errormessage);

		return "conservice/askmanage/questioniteminfoedit";
	}

	@RequestMapping("/questioniteminfoedit")
	public String questioniteminfoedit(@Valid QuestionitemParam QitemP, BindingResult result, RedirectAttributes rA) {

		String errormessage = "";
		if (result.hasErrors()) {
			List<ObjectError> list = result.getAllErrors();
			for (ObjectError error : list) {
				errormessage = errormessage + error.getDefaultMessage() + ";";
			}
			rA.addAttribute("errormessage", errormessage);
			rA.addAttribute("id", QitemP.getId());
			return "redirect:/toquestioniteminfoedit";
		}

		Questionitem qitem = qitemR.findById(QitemP.getId().intValue());
		try {
			qitem.setByParam(QitemP);
		} catch (Exception e) {
			rA.addAttribute("errormessage", "请输入正确格式的信息");
			rA.addAttribute("id", QitemP.getId());
			return "redirect:/toquestioniteminfoedit";
		}

		try {
			qitemR.save(qitem);
		} catch (Exception e) {
			rA.addAttribute("errormessage", "请输入正确的信息,问题应已录入");
			rA.addAttribute("id", QitemP.getId());
			return "redirect:/toquestioniteminfoedit";
		}

		rA.addAttribute("id", QitemP.getQuestionId());
		return "redirect:/toquestionitemedit";

	}

	@RequestMapping("/toquestionadd")
	public String toquestionadd(@RequestParam(value = "id") Integer id,
			@RequestParam(value = "errormessage", required = false, defaultValue = "") String errormessage,
			ModelMap model) {

		model.addAttribute("askId", id.intValue());

		model.addAttribute("errormessage", errormessage);

		return "conservice/askmanage/questionadd";
	}

	@RequestMapping("/questionadd")
	public String questionadd(@Valid QuestionParam QP, BindingResult result, RedirectAttributes rA) {

		String errormessage = "";
		if (result.hasErrors()) {
			List<ObjectError> list = result.getAllErrors();
			for (ObjectError error : list) {
				errormessage = errormessage + error.getDefaultMessage() + ";";
			}
			rA.addAttribute("errormessage", errormessage);
			rA.addAttribute("id", QP.getAskId());
			return "redirect:/toquestionadd";
		}

		Question question = new Question();
		try {
			question.setByParam(QP);
		} catch (Exception e) {
			rA.addAttribute("errormessage", "请输入正确格式的信息");
			rA.addAttribute("id", QP.getAskId());
			return "redirect:/toquestionadd";
		}

		try {
			questionR.save(question);
		} catch (Exception e) {
			rA.addAttribute("errormessage", "请输入正确的信息,问卷应已录入");
			rA.addAttribute("id", QP.getAskId());
			return "redirect:/toquestionadd";
		}

		rA.addAttribute("id", QP.getAskId());
		return "redirect:/toaskedit";

	}

	@RequestMapping("/towriteask")
	public String towriteask(@RequestParam(value = "id") Integer id,
			@RequestParam(value = "errormessage", required = false, defaultValue = "") String errormessage,
			ModelMap model) {

		Ask ask = askR.findById(id.intValue());
		model.addAttribute("ask", ask);

		List<Question> qlist = questionR.findByAskIdOrderByOrderidAsc(ask.getId().intValue());
		List<QAndQitem> list = new ArrayList<QAndQitem>();
		if (qlist != null) {
			for (int i = 0; i < qlist.size(); i++) {
				Question question = qlist.get(i);
				int questionId = question.getId().intValue();
				List<Questionitem> Qitemlist = qitemR.findByQuestionIdOrderByOrderidAsc(questionId);

				QAndQitem temp = new QAndQitem();
				temp.setQuestion(question);
				temp.setQitemlist(Qitemlist);

				list.add(temp);

			}
		}

		model.addAttribute("objectlist", list);

		return "conservice/askmanage/writeask";
	}

	@RequestMapping("/writeask")
	public String writeask(@RequestParam(value = "AskId") Integer AskId, HttpServletRequest request) {

		int askId = AskId.intValue();

		Ask ask = askR.findById(askId);
		try {
			if (ask.getWritenum() != null) {
				ask.setWritenum(ask.getWritenum() + 1);
			} else {
				ask.setWritenum(1);
			}
		} catch (Exception e) {
		}

		List<Question> questionlist = questionR.findByAskIdOrderByOrderidAsc(askId);

		if (questionlist != null) {
			for (int i = 0; i < questionlist.size(); i++) {

				Integer questionid = questionlist.get(i).getId();
				String[] itemlist = request.getParameterValues(questionid.toString());
				if (itemlist != null) {
					for (int j = 0; j < itemlist.length; j++) {
						Questionitem qitem = qitemR.findById(Integer.parseInt(itemlist[j]));
						if (qitem.getResult() != null) {
							qitem.setResult(qitem.getResult() + 1);
						} else {
							qitem.setResult(1);
						}
						try {
							qitemR.save(qitem);
						} catch (Exception e) {

						}
					}
				}

			}
		}

		return "redirect:/toqueryask";
	}

	@RequestMapping("/toaskresult")
	public String toaskresult(@RequestParam(value = "id") Integer id, ModelMap model) {

		Ask ask = askR.findById(id.intValue());
		model.addAttribute("ask", ask);
		
		
		if(ask.getWritenum()!=null){
		String errormessage = "共有" + ask.getWritenum() + "人填写这份问卷";
		model.addAttribute("errormessage", errormessage);}else{
			String errormessage = "共有0人填写这份问卷";
			model.addAttribute("errormessage", errormessage);
		}
		model.addAttribute("writeNum", ask.getWritenum());

		List<Question> qlist = questionR.findByAskIdOrderByOrderidAsc(ask.getId().intValue());
		List<QAndQitem> list = new ArrayList<QAndQitem>();
		if (qlist != null) {
			for (int i = 0; i < qlist.size(); i++) {
				Question question = qlist.get(i);
				int questionId = question.getId().intValue();
				List<Questionitem> Qitemlist = qitemR.findByQuestionIdOrderByOrderidAsc(questionId);

				QAndQitem temp = new QAndQitem();
				temp.setQuestion(question);
				temp.setQitemlist(Qitemlist);

				list.add(temp);

			}
		}
		model.addAttribute("objectlist", list);
		return "conservice/askmanage/askresult";
	}

}
