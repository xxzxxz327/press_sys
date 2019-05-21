package com.xxzxxz.press.web;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.xxzxxz.press.model.*;
import com.xxzxxz.press.param.*;
import com.xxzxxz.press.repository.*;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
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

@Controller
public class DictionaryController {

	@Autowired
	private DepartRepository dR;

	@Autowired
	private DeliverRepository deliverRepository;

	@Autowired
	private DepartmentRepository departmentR;

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
	private MeritDicRepository mdR;

	@Autowired
	private OrdersRepository ordersRepository;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");


	// private static Integer page = 0;
	// private static Integer size = 6;

	@RequestMapping("/dicDepart")
	public String getdepart(@RequestParam(value = "page", defaultValue = "0") Integer page,
							@RequestParam(value = "size", defaultValue = "6") Integer size,
							@RequestParam(value = "departName") String departName,
							ModelMap model) {

		List<String> listDepartName=CenterTool.getDepartNameList();
		model.addAttribute("listDepartName", listDepartName);

		System.out.println("传入name为:");
		System.out.println(departName);

		Pageable pageable = PageRequest.of(page, size);
		model.addAttribute("selectName", departName);


		// 根据name查找
		if (departName != "") {
			Page<Object[]> list = dR.findByDepartNameShow(departName, pageable);
			model.addAttribute("objectlist", list);
			return "dicdepart";
		}

		// name不存在的话查询全部
		System.out.println("进入全部查找");
		Page<Object[]> list = dR.findAllShow(pageable);
		model.addAttribute("objectlist", list);
		return "dicdepart";
	}

	@RequestMapping("/todepartedit")
	public String todepartedit(@RequestParam(value = "id") Integer Id,
							   @RequestParam(value = "errormessage", required = false, defaultValue = "") String errormessage,
							   @RequestParam(value = "selectName", required = false, defaultValue = "") String selectName,
							   ModelMap model) {

		int id = Id.intValue();

		Department depart = dR.findById(id);

		model.addAttribute("depart", depart);

		List<Staff> liststaff=staffR.findAll();

		List<Object[]> stafflist=new ArrayList<Object[]>();


		for(int i=0;i<liststaff.size();i++){
			Object[] objects=new Object[2];
			objects[0]=liststaff.get(i).getId();
			objects[1]=String.valueOf(liststaff.get(i).getId())+" "+liststaff.get(i).getStaffName();
			stafflist.add(objects);
		}

		List<Station> stationlist=stationR.findAll();

		model.addAttribute("stafflist", stafflist);
		model.addAttribute("stationlist", stationlist);

		model.addAttribute(errormessage, errormessage);
		System.out.println(errormessage);

		model.addAttribute("selectName", selectName);

		return "departedit";
	}

	@RequestMapping("/departedit")
	public String departedit(@Valid DepartParam departP, BindingResult result,
							 @RequestParam(value = "selectName", required = false, defaultValue = "") String selectName,
							 ModelMap model, RedirectAttributes rA) {
		String errormessage = "";
		rA.addAttribute("selectName", selectName);
		if (result.hasErrors()) {
			errormessage = "数据不合法";
			rA.addAttribute("errormessage", errormessage);
			System.out.println(departP.getId().intValue());
			rA.addAttribute("id", departP.getId());
			return "redirect:/todepartedit";
		}

		Department depart = dR.findById(departP.getId().intValue());
		try {
			depart.setId(departP.getId().intValue());
			depart.setDepartName(departP.getDepartName());
			if (departP.getPrincipalId() != null) {
				depart.setPrincipalId(departP.getPrincipalId().intValue());
			}
			if (departP.getStationId() != null) {
				depart.setStationId(departP.getStationId().intValue());
			}
		} catch (Exception e) {
			System.out.println("数据格式不正确");
			errormessage = "数据格式不正确";
			rA.addAttribute("errormessage", errormessage);
			rA.addAttribute("id", departP.getId());
			return "redirect:/todepartedit";
		}

		try {
			if(staffR.findById(depart.getPrincipalId())==null){throw new Exception();}
			dR.save(depart);
		} catch (Exception e) {
			System.out.println("请输入正确的数据，负责人与上级部门应已录入");
			errormessage = "请输入正确的数据，负责人与上级部门应已录入";
			rA.addAttribute("errormessage", errormessage);
			rA.addAttribute("id", departP.getId());
			return "redirect:/todepartedit";
		}
		rA.addAttribute("departName", selectName);
		return "redirect:/dicDepart";
	}

	@RequestMapping("/todepartadd")
	public String todepartadd(@RequestParam(value = "selectName", required = false, defaultValue = "") String selectName,
							  @RequestParam(value = "errormessage", required = false, defaultValue = "") String errormessage,
							  ModelMap model){

		List<Staff> liststaff=staffR.findAll();

		List<Object[]> stafflist=new ArrayList<Object[]>();


		for(int i=0;i<liststaff.size();i++){
			Object[] objects=new Object[2];
			objects[0]=liststaff.get(i).getId();
			objects[1]=String.valueOf(liststaff.get(i).getId())+" "+liststaff.get(i).getStaffName();
			stafflist.add(objects);
		}

		List<Station> stationlist=stationR.findAll();

		model.addAttribute("stafflist", stafflist);
		model.addAttribute("stationlist", stationlist);

		model.addAttribute("errormessage", errormessage);
		System.out.println(errormessage);

		model.addAttribute("selectName", selectName);

		return "departadd";
	}

	@RequestMapping("/departadd")
	public String departadd(@Valid DepartParam departP,
							BindingResult result,
							@RequestParam(value = "selectName", required = false, defaultValue = "") String selectName,RedirectAttributes rA) {


		rA.addAttribute("selectName", selectName);
		String errormessage = "";
		if (result.hasErrors()) {
			errormessage = "数据不合法";
			rA.addAttribute("errormessage", errormessage);
			System.out.println("添加数据不合法");
			return "redirect:/todepartadd";
		}

		Department depart = new Department();

		try {
			depart.setDepartName(departP.getDepartName());
			depart.setPrincipalId(departP.getPrincipalId().intValue());
			depart.setStationId(departP.getStationId().intValue());
		} catch (Exception e) {
			System.out.println("数据格式不正确");
			rA.addAttribute("errormessage", "数据格式不正确");
			return "redirect:/todepartadd";
		}

		try {
			if(staffR.findById(depart.getPrincipalId())==null){throw new Exception();}
			dR.save(depart);
		} catch (Exception e) {
			System.out.println("请输入正确的数据，负责人与上级部门应已录入");
			rA.addAttribute("errormessage", "请输入正确的数据，负责人与上级部门应已录入");
			return "redirect:/todepartadd";
		}

		rA.addAttribute("departName", selectName);
		return "redirect:/dicDepart";
	}

	@RequestMapping("/todepartdelete")
	public String todepartdelete(@RequestParam(value = "id") Integer Id,
								 @RequestParam(value = "selectName", required = false, defaultValue = "") String selectName,
								 RedirectAttributes rA) {

		int id = Id.intValue();

		Department depart = dR.findById(id);

		try{
			dR.delete(depart);
		}catch(Exception e){

		}
		rA.addAttribute("departName", selectName);
		return "redirect:/dicDepart";
	}


	@RequestMapping("/uploaddepart")
	public String uploaddepart(Model model, @RequestParam("file") MultipartFile file,
							   RedirectAttributes redirectAttributes) throws Exception {
		XSSFWorkbook book = new XSSFWorkbook(file.getInputStream());
		XSSFSheet sheet = book.getSheetAt(0);
		for (int i = 1; i < sheet.getLastRowNum() + 1; i++) {
			Department depart = new Department();
			XSSFRow row = sheet.getRow(i);
			try {
				// depart.setId((int)row.getCell(0).getNumericCellValue());
				depart.setDepartName(row.getCell(0).getStringCellValue());
				depart.setPrincipalId((int) row.getCell(1).getNumericCellValue());
				depart.setStationId((int) row.getCell(2).getNumericCellValue());
			} catch (Exception e) {
				System.out.println("数据转换错误");
			}

			try {
				if(staffR.findById(depart.getPrincipalId())==null){throw new Exception();}
				dR.save(depart);
			} catch (Exception e) {
				System.out.println("存储失败");
			}
		}

		return "redirect:/todicdepart";
	}

	// 以下是Office处理
	@RequestMapping("/dicOffice")
	public String getoffice(@RequestParam(value = "page", defaultValue = "0") Integer page,
							@RequestParam(value = "size", defaultValue = "6") Integer size,
							@RequestParam(value = "officeName") String officeName,
							ModelMap model) {


		List<String> listOfficeName=CenterTool.getOfficeNameList();
		model.addAttribute("listOfficeName", listOfficeName);

		System.out.println("传入name为:");
		System.out.println(officeName);

		Sort sort = new Sort(Sort.Direction.ASC, "id");
		Pageable pageable = PageRequest.of(page, size, sort);


		model.addAttribute("selectName", officeName);



		// 根据name查找
		if (officeName != "") {
			Page<OfficeInfo> list = oR.findByOfficeName(officeName, pageable);
			model.addAttribute("objectlist", list);
			return "dic/office/dicoffice";
		}

		System.out.println("进入全部查找");
		Page<OfficeInfo> list = oR.findAll(pageable);
		model.addAttribute("objectlist", list);
		System.out.println("全部查找结束");

		return "dic/office/dicoffice";
	}

	@RequestMapping("/toofficeedit")
	public String toofficeedit(@RequestParam(value = "id") Integer Id,
							   @RequestParam(value = "errormessage", required = false, defaultValue = "") String errormessage,
							   @RequestParam(value = "selectName", required = false, defaultValue = "") String selectName,
							   ModelMap model) {

		int id = Id.intValue();

		OfficeInfo office = oR.findById(id);

		model.addAttribute("office", office);

		model.addAttribute("errormessage", errormessage);
		System.out.println(errormessage);

		model.addAttribute("selectName", selectName);

		return "dic/office/officeedit";
	}

	@RequestMapping("/officeedit")
	public String officeedit(@Valid OfficeParam officeP, BindingResult result,
							 @RequestParam(value = "selectName", required = false, defaultValue = "") String selectName,
							 ModelMap model, RedirectAttributes rA) {
		String errormessage = "";
		rA.addAttribute("selectName", selectName);
		if (result.hasErrors()) {
			errormessage = "数据不合法";
			rA.addAttribute("errormessage", errormessage);
			System.out.println(officeP.getId().intValue());
			rA.addAttribute("id", officeP.getId());
			return "redirect:/toofficeedit";
		}

		OfficeInfo office = oR.findById(officeP.getId().intValue());
		try {
			office.setId(officeP.getId().intValue());
			office.setOfficeName(officeP.getOfficeName());
		} catch (Exception e) {
			System.out.println("数据格式不正确");
			errormessage = "数据格式不正确";
			rA.addAttribute("errormessage", errormessage);
			rA.addAttribute("id", officeP.getId());
			return "redirect:/toofficeedit";
		}

		try {
			oR.save(office);
		} catch (Exception e) {
			System.out.println("请输入正确的数据，报社名称不能重复");
			errormessage = "请输入正确的数据，报社名称不能重复";
			rA.addAttribute("errormessage", errormessage);
			rA.addAttribute("id", officeP.getId());
			return "redirect:/toofficeedit";
		}

		rA.addAttribute("officeName", selectName);
		return "redirect:/dicOffice";
	}

	@RequestMapping("/toofficeadd")
	public String toofficeadd(@RequestParam(value = "selectName", required = false, defaultValue = "") String selectName,
							  @RequestParam(value = "errormessage", required = false, defaultValue = "") String errormessage,
							  ModelMap model){
		model.addAttribute("errormessage", errormessage);
		model.addAttribute("selectName", selectName);
		return "dic/office/officeadd";
	}

	@RequestMapping("/officeadd")
	public String officeadd(@Valid OfficeParam officeP, BindingResult result,
							@RequestParam(value = "selectName", required = false, defaultValue = "") String selectName,
							RedirectAttributes rA
	) {
		rA.addAttribute("selectName", selectName);
		String errormessage = "";
		if (result.hasErrors()) {
			errormessage = "数据不合法";
			rA.addAttribute("errormessage", errormessage);
			return "redirect:/toofficeadd";
		}

		OfficeInfo office = new OfficeInfo();

		try {
			office.setOfficeName(officeP.getOfficeName());
		} catch (Exception e) {
			System.out.println("数据格式不正确");
			rA.addAttribute("errormessage", "数据格式不正确");
			return "redirect:/toofficeadd";
		}

		try {
			oR.save(office);
		} catch (Exception e) {
			System.out.println("请输入正确的数据，报社名称不能重复");
			rA.addAttribute("errormessage", "请输入正确的数据，报社名称不能重复");
			return "redirect:/toofficeadd";
		}

		rA.addAttribute("officeName", selectName);
		return "redirect:/dicOffice";
	}

	@RequestMapping("/toofficedelete")
	public String toofficedelete(@RequestParam(value = "id") Integer Id,
								 @RequestParam(value = "selectName", required = false, defaultValue = "") String selectName,
								 RedirectAttributes rA) {

		int id = Id.intValue();

		OfficeInfo office = oR.findById(id);

		try{
			oR.delete(office);
		}catch(Exception e){

		}
		rA.addAttribute("officeName", selectName);
		return "redirect:/dicOffice";
	}

	@RequestMapping("/uploadoffice")
	public String uploadoffice(Model model, @RequestParam("file") MultipartFile file,
							   RedirectAttributes redirectAttributes) throws Exception {
		XSSFWorkbook book = new XSSFWorkbook(file.getInputStream());
		XSSFSheet sheet = book.getSheetAt(0);
		for (int i = 1; i < sheet.getLastRowNum() + 1; i++) {
			OfficeInfo office = new OfficeInfo();
			XSSFRow row = sheet.getRow(i);
			try {
				office.setOfficeName(row.getCell(0).getStringCellValue());
			} catch (Exception e) {
				System.out.println("数据转换错误");
			}

			try {
				oR.save(office);
			} catch (Exception e) {
				System.out.println("存储失败");
			}
		}

		return "redirect:/todicoffice";
	}

	// 以下是Reason处理
	@RequestMapping("/dicReason")
	public String getreason(@RequestParam(value = "page", defaultValue = "0") Integer page,
							@RequestParam(value = "size", defaultValue = "6") Integer size,
							@RequestParam(value = "reasonName") String reasonName,
							ModelMap model) {

		List<String> listReasonName=CenterTool.getReasonNameList();
		model.addAttribute("listReasonName", listReasonName);

		System.out.println("传入name为:");
		System.out.println(reasonName);

		Sort sort = new Sort(Sort.Direction.ASC, "id");
		Pageable pageable = PageRequest.of(page, size, sort);


		model.addAttribute("selectName", reasonName);



		// 根据name查找
		if (reasonName != "") {
			Page<ChangeReason> list = rR.findByReasonName(reasonName, pageable);
			model.addAttribute("objectlist", list);
			return "dic/reason/dicreason";
		}

		System.out.println("进入全部查找");
		Page<ChangeReason> list = rR.findAll(pageable);
		model.addAttribute("objectlist", list);
		System.out.println("全部查找结束");

		return "dic/reason/dicreason";
	}

	@RequestMapping("/toreasonedit")
	public String toreasonedit(@RequestParam(value = "id") Integer Id,
							   @RequestParam(value = "errormessage", required = false, defaultValue = "") String errormessage,
							   @RequestParam(value = "selectName", required = false, defaultValue = "") String selectName,
							   ModelMap model) {

		int id = Id.intValue();

		ChangeReason reason = rR.findById(id);

		model.addAttribute("reason", reason);

		model.addAttribute("errormessage", errormessage);
		System.out.println(errormessage);

		model.addAttribute("selectName", selectName);

		return "dic/reason/reasonedit";
	}

	@RequestMapping("/reasonedit")
	public String reasonedit(@Valid ReasonParam reasonP, BindingResult result,
							 @RequestParam(value = "selectName", required = false, defaultValue = "") String selectName,
							 ModelMap model, RedirectAttributes rA) {
		String errormessage = "";
		rA.addAttribute("selectName", selectName);
		if (result.hasErrors()) {
			errormessage = "数据不合法";
			rA.addAttribute("errormessage", errormessage);
			System.out.println(reasonP.getId().intValue());
			rA.addAttribute("id", reasonP.getId());
			return "redirect:/toreasonedit";
		}

		ChangeReason reason = rR.findById(reasonP.getId().intValue());
		try {
			reason.setId(reasonP.getId().intValue());
			reason.setReasonName(reasonP.getReasonName());
			reason.setStatus(reasonP.getStatus());
		} catch (Exception e) {
			System.out.println("数据格式不正确");
			errormessage = "数据格式不正确";
			rA.addAttribute("errormessage", errormessage);
			rA.addAttribute("id", reasonP.getId());
			return "redirect:/toreasonedit";
		}

		try {
			rR.save(reason);
		} catch (Exception e) {
			System.out.println("请输入正确的数据,变更原因类型与对应类型ID可能已存在");
			errormessage = "请输入正确的数据,变更原因类型与对应类型ID可能已存在";
			rA.addAttribute("errormessage", errormessage);
			rA.addAttribute("id", reasonP.getId());
			return "redirect:/toreasonedit";
		}
		rA.addAttribute("reasonName", selectName);
		return "redirect:/dicReason";
	}

	@RequestMapping("/toreasonadd")
	public String toreasonadd(@RequestParam(value = "selectName", required = false, defaultValue = "") String selectName,
							  @RequestParam(value = "errormessage", required = false, defaultValue = "") String errormessage,
							  ModelMap model){
		model.addAttribute("errormessage", errormessage);
		System.out.println(errormessage);

		model.addAttribute("selectName", selectName);
		return "dic/reason/reasonadd";
	}

	@RequestMapping("/reasonadd")
	public String reasonadd(@Valid ReasonParam reasonP, BindingResult result,
							@RequestParam(value = "selectName", required = false, defaultValue = "") String selectName,
							RedirectAttributes rA) {

		String errormessage = "";
		rA.addAttribute("selectName", selectName);
		if (result.hasErrors()) {
			errormessage = "数据不合法";
			rA.addAttribute("errormessage", errormessage);
			return "redirect:/toreasonadd";
		}

		ChangeReason reason = new ChangeReason();

		try {
			reason.setReasonName(reasonP.getReasonName());
			reason.setStatus(reasonP.getStatus());
		} catch (Exception e) {
			System.out.println("数据格式不正确");
			rA.addAttribute("errormessage", "数据格式不正确");
			return "redirect:/toreasonadd";
		}

		try {
			rR.save(reason);
		} catch (Exception e) {
			System.out.println("请输入正确的数据,变更原因类型与对应类型ID可能已存在");
			rA.addAttribute("errormessage", "请输入正确的数据,变更原因类型与对应类型ID可能已存在");
			return "redirect:/toreasonadd";
		}

		rA.addAttribute("reasonName", selectName);
		return "redirect:/dicReason";
	}

	@RequestMapping("/toreasondelete")
	public String toreasondelete(@RequestParam(value = "id") Integer Id,
								 @RequestParam(value = "selectName", required = false, defaultValue = "") String selectName,
								 RedirectAttributes rA) {

		int id = Id.intValue();

		ChangeReason reason = rR.findById(id);

		try{
			rR.delete(reason);
		}catch(Exception e){

		}
		rA.addAttribute("reasonName", selectName);
		return "redirect:/dicReason";
	}

	@RequestMapping("/uploadreason")
	public String uploadreason(Model model, @RequestParam("file") MultipartFile file,
							   RedirectAttributes redirectAttributes) throws Exception {
		XSSFWorkbook book = new XSSFWorkbook(file.getInputStream());
		XSSFSheet sheet = book.getSheetAt(0);
		for (int i = 1; i < sheet.getLastRowNum() + 1; i++) {
			ChangeReason reason = new ChangeReason();
			XSSFRow row = sheet.getRow(i);
			try {
				reason.setReasonName(row.getCell(0).getStringCellValue());
				reason.setStatus((int)row.getCell(1).getNumericCellValue());
			} catch (Exception e) {
				System.out.println("数据转换错误");
			}

			try {
				rR.save(reason);
			} catch (Exception e) {
				System.out.println("存储失败");
			}
		}

		return "redirect:/todicreason";
	}

	// 以下是公司信息设置

	@RequestMapping("/toinfoshow")
	public String toinfoshow(ModelMap model) {

		Info info = iR.findById(1);
		if (info == null) {
			info = new Info();
			info.setId(1);
		}
		model.addAttribute("info", info);
		System.out.println(info.getMailaddress());
		return "dic/info/infoedit";
	}

	@RequestMapping("/toinfoedit")
	public String toinfoedit(@Valid InfoParam infoP, BindingResult result, ModelMap model) {
		String errormessage = "";
		// 参数校验
		if (result.hasErrors()) {
			List<ObjectError> list = result.getAllErrors();
			for (ObjectError error : list) {
				errormessage = errormessage + error.getDefaultMessage() + ";";
			}
			model.addAttribute("errormessage", errormessage);
			model.addAttribute("info", infoP);
			return "dic/info/infoedit";

		}

		Info info = new Info();
		info.setId(1);
		try {
			info.setAddress(infoP.getAddress());
			info.setMailaddress(infoP.getMailaddress());
			info.setName(infoP.getName());
			info.setPhoneNumber(infoP.getPhoneNumber());
			info.setPrincipal(infoP.getPrincipal());
		} catch (Exception e) {
			model.addAttribute("errormessage", "数据转换错误");
			model.addAttribute("info", infoP);
			return "dic/info/infoedit";
		}

		try {
			iR.save(info);
		} catch (Exception e) {
			model.addAttribute("errormessage", "修改失败");
			model.addAttribute("info", infoP);
			return "dic/info/infoedit";
		}

		model.addAttribute("info", info);
		return "dic/info/infoedit";
	}

	// 以下是运输单位设置
	@RequestMapping("/dicTransinfo")
	public String gettransinfo(@RequestParam(value = "page", defaultValue = "0") Integer page,
							   @RequestParam(value = "size", defaultValue = "6") Integer size,
							   @RequestParam(value = "Name") String Name, ModelMap model) {

		List<String> listTransinfoName=CenterTool.getTransinfoNameList();
		model.addAttribute("listTransinfoName", listTransinfoName);

		System.out.println("传入name为:");
		System.out.println(Name);

		Sort sort = new Sort(Sort.Direction.ASC, "id");
		Pageable pageable = PageRequest.of(page, size, sort);


		model.addAttribute("selectName", Name);



		// 根据name查找
		if (Name != "") {
			Page<Transinfo> list = tR.findByName(Name, pageable);
			model.addAttribute("objectlist", list);
			return "dic/transinfo/dictransinfo";
		}

		System.out.println("进入全部查找");
		Page<Transinfo> list = tR.findAll(pageable);
		model.addAttribute("objectlist", list);
		System.out.println("全部查找结束");

		return "dic/transinfo/dictransinfo";
	}

	@RequestMapping("/totransinfoedit")
	public String totransinfoedit(@RequestParam(value = "id") Integer Id,
								  @RequestParam(value = "errormessage", required = false, defaultValue = "") String errormessage,
								  @RequestParam(value = "selectName", required = false, defaultValue = "") String selectName,
								  ModelMap model) {

		int id = Id.intValue();

		Transinfo transinfo = tR.findById(id);

		model.addAttribute("transinfo", transinfo);

		model.addAttribute("errormessage", errormessage);
		System.out.println(errormessage);

		model.addAttribute("selectName", selectName);

		return "dic/transinfo/transinfoedit";
	}

	@RequestMapping("/transinfoedit")
	public String transinfoedit(@Valid TransinfoParam transinfoP, BindingResult result,
								@RequestParam(value = "selectName", required = false, defaultValue = "") String selectName,
								ModelMap model,
								RedirectAttributes rA) {
		String errormessage = "";
		rA.addAttribute("selectName", selectName);
		if (result.hasErrors()) {
			List<ObjectError> list = result.getAllErrors();
			for (ObjectError error : list) {
				errormessage = errormessage + error.getDefaultMessage() + ";";
			}
			rA.addAttribute("errormessage", errormessage);
			System.out.println(transinfoP.getId().intValue());
			rA.addAttribute("id", transinfoP.getId());
			return "redirect:/totransinfoedit";
		}

		Transinfo transinfo = tR.findById(transinfoP.getId().intValue());
		try {
			transinfo.setId(transinfoP.getId().intValue());
			transinfo.setName(transinfoP.getName());
		} catch (Exception e) {
			System.out.println("数据格式不正确");
			errormessage = "数据格式不正确";
			rA.addAttribute("errormessage", errormessage);
			rA.addAttribute("id", transinfoP.getId());
			return "redirect:/totransinfoedit";
		}

		try {
			tR.save(transinfo);
		} catch (Exception e) {
			System.out.println("请输入正确的数据，运输单位名称不能重复");
			errormessage = "请输入正确的数据，运输单位名称不能重复";
			rA.addAttribute("errormessage", errormessage);
			rA.addAttribute("id", transinfoP.getId());
			return "redirect:/totransinfoedit";
		}
		rA.addAttribute("Name", selectName);
		return "redirect:/dicTransinfo";
	}

	@RequestMapping("/totransinfoadd")
	public String totransinfoadd(@RequestParam(value = "selectName", required = false, defaultValue = "") String selectName,
								 @RequestParam(value = "errormessage", required = false, defaultValue = "") String errormessage,
								 ModelMap model){
		model.addAttribute("errormessage", errormessage);
		model.addAttribute("selectName", selectName);
		return "dic/transinfo/transinfoadd";
	}


	@RequestMapping("/transinfoadd")
	public String transinfoadd(@Valid TransinfoParam transinfoP, BindingResult result,
							   @RequestParam(value = "selectName", required = false, defaultValue = "") String selectName,
							   RedirectAttributes rA) {

		rA.addAttribute("selectName", selectName);
		String errormessage = "";
		if (result.hasErrors()) {
			List<ObjectError> list = result.getAllErrors();
			for (ObjectError error : list) {
				errormessage = errormessage + error.getDefaultMessage() + ";";
			}
			rA.addAttribute("errormessage", errormessage);
			return "redirect:/totransinfoadd";
		}

		Transinfo transinfo = new Transinfo();

		try {
			transinfo.setName(transinfoP.getName());
		} catch (Exception e) {
			System.out.println("数据格式不正确");
			rA.addAttribute("errormessage", "数据格式不正确");
			return "redirect:/totransinfoadd";
		}

		try {
			tR.save(transinfo);
		} catch (Exception e) {
			System.out.println("请输入正确的数据，运输单位名称不能重复");
			rA.addAttribute("errormessage", "请输入正确的数据，运输单位名称不能重复");
			return "redirect:/totransinfoadd";
		}

		rA.addAttribute("Name", selectName);
		return "redirect:/dicTransinfo";
	}

	@RequestMapping("/totransinfodelete")
	public String totransinfodelete(@RequestParam(value = "id") Integer Id,
									@RequestParam(value = "selectName", required = false, defaultValue = "") String selectName,
									RedirectAttributes rA) {

		int id = Id.intValue();

		Transinfo transinfo = tR.findById(id);

		try{
			tR.delete(transinfo);
		}catch(Exception e){

		}
		rA.addAttribute("Name", selectName);
		return "redirect:/dicTransinfo";
	}

	@RequestMapping("/uploadtransinfo")
	public String uploadtransinfo(Model model, @RequestParam("file") MultipartFile file,
								  RedirectAttributes redirectAttributes) throws Exception {
		XSSFWorkbook book = new XSSFWorkbook(file.getInputStream());
		XSSFSheet sheet = book.getSheetAt(0);
		for (int i = 1; i < sheet.getLastRowNum() + 1; i++) {
			Transinfo transinfo = new Transinfo();
			XSSFRow row = sheet.getRow(i);
			try {
				transinfo.setName(row.getCell(0).getStringCellValue());
			} catch (Exception e) {
				System.out.println("数据转换错误");
			}

			try {
				tR.save(transinfo);
			} catch (Exception e) {
				System.out.println("存储失败");
			}
		}

		return "redirect:/todictransinfo";
	}

	// 以下是Duty处理
	@RequestMapping("/dicDuty")
	public String getduty(@RequestParam(value = "page", defaultValue = "0") Integer page,
						  @RequestParam(value = "size", defaultValue = "6") Integer size,
						  @RequestParam(value = "dutyName") String dutyName, ModelMap model) {


		List<String> listDutyName=CenterTool.getDutyNameList();
		model.addAttribute("listDutyName", listDutyName);

		System.out.println("传入name为:");
		System.out.println(dutyName);

		Sort sort = new Sort(Sort.Direction.ASC, "id");
		Pageable pageable = PageRequest.of(page, size);


		model.addAttribute("selectName", dutyName);



		// 根据name查找
		if (dutyName != "") {
			Page<Object[]> list = dutyR.findByDutyNameShow(dutyName, pageable);
			model.addAttribute("objectlist", list);
			return "dic/duty/dicduty";
		}

		System.out.println("进入全部查找");
		Page<Object[]> list = dutyR.findAllShow(pageable);
		model.addAttribute("objectlist", list);
		System.out.println("全部查找结束");

		return "dic/duty/dicduty";
	}

	@RequestMapping("/todutyedit")
	public String todutyedit(@RequestParam(value = "id") Integer Id,
							 @RequestParam(value = "errormessage", required = false, defaultValue = "") String errormessage,
							 @RequestParam(value = "selectName", required = false, defaultValue = "") String selectName,
							 ModelMap model) {

		int id = Id.intValue();

		Duty duty = dutyR.findById(id);

		model.addAttribute("duty", duty);

		model.addAttribute("errormessage", errormessage);
		System.out.println(errormessage);

		List<Merit> meritlist=mdR.findAll();
		model.addAttribute("meritlist", meritlist);

		List<Department> departlist=dR.findAll();
		model.addAttribute("departlist", departlist);

		model.addAttribute("selectName", selectName);
		return "dic/duty/dutyedit";
	}

	@RequestMapping("/dutyedit")
	public String dutyedit(@Valid DutyParam dutyP, BindingResult result,
						   @RequestParam(value = "selectName", required = false, defaultValue = "") String selectName,
						   ModelMap model, RedirectAttributes rA) {
		String errormessage = "";
		rA.addAttribute("selectName", selectName);
		if (result.hasErrors()) {
			errormessage = "数据不合法";
			rA.addAttribute("errormessage", errormessage);
			System.out.println(dutyP.getDutyId().intValue());
			rA.addAttribute("id", dutyP.getDutyId());
			return "redirect:/todutyedit";
		}

		Duty duty = dutyR.findById(dutyP.getDutyId().intValue());
		try {
			duty.setId(dutyP.getDutyId().intValue());
			duty.setDutyName(dutyP.getDutyName());
			duty.setDepartId(dutyP.getDutyDepartId());
			duty.setMeritId(dutyP.getDutyMeritId());

		} catch (Exception e) {
			System.out.println("数据格式不正确");
			errormessage = "数据格式不正确";
			rA.addAttribute("errormessage", errormessage);
			rA.addAttribute("id", dutyP.getDutyId());
			return "redirect:/todutyedit";
		}

		try {
			dutyR.save(duty);
		} catch (Exception e) {
			System.out.println("请输入正确的数据");
			errormessage = "请输入正确的数据";
			rA.addAttribute("errormessage", errormessage);
			rA.addAttribute("id", dutyP.getDutyId());
			return "redirect:/todutyedit";
		}
		rA.addAttribute("dutyName", selectName);
		return "redirect:/dicDuty";
	}

	@RequestMapping("/todutyadd")
	public String todutyaddBo(@RequestParam(value = "selectName", required = false, defaultValue = "") String selectName,
							  @RequestParam(value = "errormessage", required = false, defaultValue = "") String errormessage,
							  ModelMap model){

		List<Merit> meritlist=mdR.findAll();
		model.addAttribute("meritlist", meritlist);

		List<Department> departlist=dR.findAll();
		model.addAttribute("departlist", departlist);

		model.addAttribute("errormessage", errormessage);
		model.addAttribute("selectName", selectName);
		return "dic/duty/dutyadd";
	}

	@RequestMapping("/dutyadd")
	public String dutyadd(@Valid DutyParam dutyP, BindingResult result,@RequestParam(value = "selectName", required = false, defaultValue = "") String selectName,
						  RedirectAttributes rA) {

		rA.addAttribute("selectName", selectName);
		String errormessage = "";
		if (result.hasErrors()) {
			errormessage = "数据不合法";
			rA.addAttribute("errormessage", errormessage);
			return "redirect:/todutyadd";
		}

		Duty duty = new Duty();

		try {
			duty.setDutyName(dutyP.getDutyName());
			duty.setDepartId(dutyP.getDutyDepartId());
			duty.setMeritId(dutyP.getDutyMeritId());
		} catch (Exception e) {
			System.out.println("数据格式不正确");
			rA.addAttribute("errormessage", "数据格式不正确");
			return "redirect:/todutyadd";
		}

		try {
			dutyR.save(duty);
		} catch (Exception e) {
			System.out.println("请输入正确的数据");
			rA.addAttribute("errormessage", "请输入正确的数据");
			return "redirect:/todutyadd";
		}

		rA.addAttribute("dutyName", selectName);
		return "redirect:/dicDuty";
	}

	@RequestMapping("/todutydelete")
	public String todutydelete(@RequestParam(value = "id") Integer Id,
							   @RequestParam(value = "selectName", required = false, defaultValue = "") String selectName,
							   RedirectAttributes rA) {

		int id = Id.intValue();

		Duty duty = dutyR.findById(id);

		try{
			dutyR.delete(duty);
		}catch(Exception e){

		}
		rA.addAttribute("dutyName", selectName);
		return "redirect:/dicDuty";
	}



	@RequestMapping("/uploadduty")
	public String uploadduty(Model model, @RequestParam("file") MultipartFile file,
							 RedirectAttributes redirectAttributes) throws Exception {
		XSSFWorkbook book = new XSSFWorkbook(file.getInputStream());
		XSSFSheet sheet = book.getSheetAt(0);
		for (int i = 1; i < sheet.getLastRowNum() + 1; i++) {
			Duty duty = new Duty();
			XSSFRow row = sheet.getRow(i);
			try {
				duty.setDutyName(row.getCell(0).getStringCellValue());
			} catch (Exception e) {
				System.out.println("数据转换错误");
			}
			try {
				duty.setDepartId((int)row.getCell(1).getNumericCellValue());
			} catch (Exception e) {
				System.out.println("数据转换错误");
			}
			try {
				duty.setMeritId((int)row.getCell(2).getNumericCellValue());
			} catch (Exception e) {
				System.out.println("数据转换错误");
			}

			try {
				dutyR.save(duty);
			} catch (Exception e) {
				System.out.println("存储失败");
			}
		}

		return "redirect:/todicduty";
	}

	// 以下是地区设置
	@RequestMapping("/dicArea")
	public String getarea(@RequestParam(value = "page", defaultValue = "0") Integer page,
						  @RequestParam(value = "size", defaultValue = "6") Integer size,
						  @RequestParam(value = "Name") String Name, ModelMap model) {


		List<String> listAreaName=CenterTool.getAreaNameList();
		model.addAttribute("listAreaName", listAreaName);

		System.out.println("传入name为:");
		System.out.println(Name);

		Sort sort = new Sort(Sort.Direction.ASC, "id");
		Pageable pageable = PageRequest.of(page, size, sort);


		model.addAttribute("selectName", Name);



		// id不存在的话根据name查找
		if (Name != "") {
			Page<Area> list = aR.findByAreaName(Name, pageable);
			model.addAttribute("objectlist", list);
			return "dic/area/dicarea";
		}

		System.out.println("进入全部查找");
		Page<Area> list = aR.findAll(pageable);
		model.addAttribute("objectlist", list);
		System.out.println("全部查找结束");

		return "dic/area/dicarea";
	}

	@RequestMapping("/toareaedit")
	public String toareaedit(@RequestParam(value = "id") Integer Id,
							 @RequestParam(value = "errormessage", required = false, defaultValue = "") String errormessage,
							 @RequestParam(value = "selectName", required = false, defaultValue = "") String selectName,
							 ModelMap model) {

		int id = Id.intValue();

		Area area = aR.findById(id);

		model.addAttribute("area", area);
		String areaName=area.getAreaName();

		String [] names = areaName.split("\\s+");
		try{
			model.addAttribute("provName", names[0]);
		}catch(Exception e){

		}

		try{
			model.addAttribute("cityName", names[1]);
		}catch(Exception e){

		}

		try{
			model.addAttribute("countryName", names[2]);
		}catch(Exception e){

		}

		model.addAttribute("errormessage", errormessage);
		System.out.println(errormessage);

		model.addAttribute("selectName", selectName);

		return "dic/area/areaedit";
	}

	@RequestMapping("/areaedit")
	public String areaedit(@Valid AreaParam areaP, BindingResult result,
						   @RequestParam(value = "selectName", required = false, defaultValue = "") String selectName,
						   ModelMap model,
						   RedirectAttributes rA) {
		String errormessage = "";
		rA.addAttribute("selectName", selectName);
		if (result.hasErrors()) {
			List<ObjectError> list = result.getAllErrors();
			for (ObjectError error : list) {
				errormessage = errormessage + error.getDefaultMessage() + ";";
			}
			rA.addAttribute("errormessage", errormessage);
			System.out.println(areaP.getId().intValue());
			rA.addAttribute("id", areaP.getId());
			return "redirect:/toareaedit";
		}

		Area area = aR.findById(areaP.getId().intValue());
		try {
			area.setId(areaP.getId().intValue());
			area.setAreaName(areaP.getName());
		} catch (Exception e) {
			System.out.println("数据格式不正确");
			errormessage = "数据格式不正确";
			rA.addAttribute("errormessage", errormessage);
			rA.addAttribute("id", areaP.getId());
			return "redirect:/toareaedit";
		}

		try {
			aR.save(area);
		} catch (Exception e) {
			System.out.println("请输入正确的数据，地区名称不能重复");
			errormessage = "请输入正确的数据，地区名称不能重复";
			rA.addAttribute("errormessage", errormessage);
			rA.addAttribute("id", areaP.getId());
			return "redirect:/toareaedit";
		}

		rA.addAttribute("Name", selectName);
		return "redirect:/dicArea";


	}

	@RequestMapping("/toareaadd")
	public String toareaadd(@RequestParam(value = "selectName", required = false, defaultValue = "") String selectName,
							@RequestParam(value = "errormessage", required = false, defaultValue = "") String errormessage,
							ModelMap model){

		model.addAttribute("errormessage", errormessage);
		System.out.println(errormessage);

		model.addAttribute("selectName", selectName);
		return "dic/area/areaadd";
	}

	@RequestMapping("/areaadd")
	public String areaadd(@Valid AreaParam areaP, BindingResult result,
						  @RequestParam(value = "selectName", required = false, defaultValue = "") String selectName,
						  RedirectAttributes rA) {

		rA.addAttribute("selectName", selectName);
		String errormessage = "";
		if (result.hasErrors()) {
			List<ObjectError> list = result.getAllErrors();
			for (ObjectError error : list) {
				errormessage = errormessage + error.getDefaultMessage() + ";";
			}
			rA.addAttribute("errormessage", errormessage);
			return "redirect:/toareaadd";
		}

		Area area = new Area();

		try {
			area.setAreaName(areaP.getName());
		} catch (Exception e) {
			System.out.println("数据格式不正确");
			rA.addAttribute("errormessage", "数据格式不正确");
			return "redirect:/toareaadd";
		}

		try {
			aR.save(area);
		} catch (Exception e) {
			System.out.println("请输入正确的数据，地区名称不能重复");
			rA.addAttribute("errormessage", "请输入正确的数据，地区名称不能重复");
			return "redirect:/toareaadd";
		}

		rA.addAttribute("Name", selectName);
		return "redirect:/dicArea";
	}

	@RequestMapping("/toareadelete")
	public String toareadelete(@RequestParam(value = "id") Integer Id,
							   @RequestParam(value = "selectName", required = false, defaultValue = "") String selectName,
							   RedirectAttributes rA) {

		int id = Id.intValue();

		Area area = aR.findById(id);

		try{
			aR.delete(area);
		}catch(Exception e){

		}
		rA.addAttribute("Name", selectName);
		return "redirect:/dicArea";
	}

	@RequestMapping("/uploadarea")
	public String uploadarea(Model model, @RequestParam("file") MultipartFile file,
							 RedirectAttributes redirectAttributes) throws Exception {
		XSSFWorkbook book = new XSSFWorkbook(file.getInputStream());
		XSSFSheet sheet = book.getSheetAt(0);
		for (int i = 1; i < sheet.getLastRowNum() + 1; i++) {
			Area area = new Area();
			XSSFRow row = sheet.getRow(i);
			try {
				area.setAreaName(row.getCell(0).getStringCellValue());
			} catch (Exception e) {
				System.out.println("数据转换错误");
			}

			try {
				aR.save(area);
			} catch (Exception e) {
				System.out.println("存储失败");
			}
		}

		return "redirect:/todicarea";
	}

	// 以下是分站设置
	@RequestMapping("/dicStation")
	public String getstation(@RequestParam(value = "page", defaultValue = "0") Integer page,
							 @RequestParam(value = "size", defaultValue = "6") Integer size,
							 @RequestParam(value = "Name") String Name,
							 @RequestParam(value = "errormessage", required = false, defaultValue = "") String errormessage,
							 ModelMap model) {


		List<String> listStationName=CenterTool.getStationNameList();
		model.addAttribute("listStationName", listStationName);

		System.out.println("传入name为:");
		System.out.println(Name);


		Pageable pageable = PageRequest.of(page, size);

		model.addAttribute("selectName", Name);


		// 根据name查找
		if (Name != "") {
			Page<Object[]> list = stationR.findByName(Name, pageable);
			model.addAttribute("objectlist", list);
			return "dic/station/dicstation";
		}

		System.out.println("进入全部查找");
		Page<Object[]> list = stationR.findAllStation(pageable);
		model.addAttribute("objectlist", list);
		System.out.println("全部查找结束");

		return "dic/station/dicstation";
	}

	@RequestMapping("/tostationedit")
	public String tostationedit(@RequestParam(value = "id") Integer Id,
								@RequestParam(value = "errormessage", required = false, defaultValue = "") String errormessage,
								@RequestParam(value = "selectName", required = false, defaultValue = "") String selectName,
								ModelMap model) {

		int id = Id.intValue();

		Station station = stationR.findById(id);

		model.addAttribute("station", station);

		List<Staff> liststaff=staffR.findAll();

		List<Object[]> stafflist=new ArrayList<Object[]>();


		for(int i=0;i<liststaff.size();i++){
			Object[] objects=new Object[2];
			objects[0]=liststaff.get(i).getId();
			objects[1]=String.valueOf(liststaff.get(i).getId())+" "+liststaff.get(i).getStaffName();
			stafflist.add(objects);
		}

		model.addAttribute("stafflist", stafflist);

		List<Area> arealist=aR.findAll();
		model.addAttribute("arealist", arealist);

		model.addAttribute("errormessage", errormessage);
		System.out.println(errormessage);
		model.addAttribute("selectName", selectName);

		return "dic/station/stationedit";
	}

	@RequestMapping("/stationedit")
	public String stationedit(@Valid StationParam stationP, BindingResult result,
							  @RequestParam(value = "selectName", required = false, defaultValue = "") String selectName,
							  ModelMap model,
							  RedirectAttributes rA) {
		String errormessage = "";
		rA.addAttribute("selectName", selectName);
		if (result.hasErrors()) {
			List<ObjectError> list = result.getAllErrors();
			for (ObjectError error : list) {
				errormessage = errormessage + error.getDefaultMessage() + ";";
			}
			rA.addAttribute("errormessage", errormessage);
			System.out.println(stationP.getId().intValue());
			rA.addAttribute("id", stationP.getId());
			return "redirect:/tostationedit";
		}

		Station station = stationR.findById(stationP.getId().intValue());
		try {
			station.setId(stationP.getId().intValue());
			station.setAreaId(stationP.getAreaId());
			station.setPrincipalId(stationP.getPrincipalId());
			station.setStationName(stationP.getStationName());
		} catch (Exception e) {
			System.out.println("数据格式不正确");
			errormessage = "数据格式不正确";
			rA.addAttribute("errormessage", errormessage);
			rA.addAttribute("id", stationP.getId());
			return "redirect:/tostationedit";
		}

		try {
			if(staffR.findById(station.getPrincipalId().intValue())==null){throw new Exception();}
			stationR.save(station);
		} catch (Exception e) {
			System.out.println("请输入正确的数据,地区以及负责人应已录入");
			errormessage = "请输入正确的数据,地区以及负责人应已录入";
			rA.addAttribute("errormessage", errormessage);
			rA.addAttribute("id", stationP.getId());
			return "redirect:/tostationedit";
		}

		rA.addAttribute("Name", selectName);

		return "redirect:/dicStation";
	}

	@RequestMapping("/tostationadd")
	public String tostationadd(@RequestParam(value = "selectName", required = false, defaultValue = "") String selectName,
							   @RequestParam(value = "errormessage", required = false, defaultValue = "") String errormessage,
							   ModelMap model){

		List<Staff> liststaff=staffR.findAll();

		List<Object[]> stafflist=new ArrayList<Object[]>();


		for(int i=0;i<liststaff.size();i++){
			Object[] objects=new Object[2];
			objects[0]=liststaff.get(i).getId();
			objects[1]=String.valueOf(liststaff.get(i).getId())+" "+liststaff.get(i).getStaffName();
			stafflist.add(objects);
		}

		model.addAttribute("stafflist", stafflist);

		List<Area> arealist=aR.findAll();
		model.addAttribute("arealist", arealist);
		model.addAttribute("errormessage", errormessage);
		model.addAttribute("selectName", selectName);

		return "dic/station/stationadd";
	}

	@RequestMapping("/stationadd")
	public String stationadd(@Valid StationParam stationP, BindingResult result,
							 @RequestParam(value = "selectName", required = false, defaultValue = "") String selectName,
							 RedirectAttributes rA) {

		rA.addAttribute("selectName", selectName);
		String errormessage = "";
		if (result.hasErrors()) {
			List<ObjectError> list = result.getAllErrors();
			for (ObjectError error : list) {
				errormessage = errormessage + error.getDefaultMessage() + ";";
			}
			rA.addAttribute("errormessage", errormessage);
			return "redirect:/tostationadd";
		}

		Station station = new Station();

		try {
			station.setAreaId(stationP.getAreaId());
			station.setPrincipalId(stationP.getPrincipalId());
			station.setStationName(stationP.getStationName());
		} catch (Exception e) {
			System.out.println("数据格式不正确");
			rA.addAttribute("errormessage", "数据格式不正确");
			return "redirect:/tostationadd";
		}

		try {

			if(staffR.findById(station.getPrincipalId().intValue())==null){throw new Exception();}
			stationR.save(station);

		} catch (Exception e) {
			System.out.println("请输入正确的数据,地区以及负责人应已录入");
			rA.addAttribute("errormessage", "请输入正确的数据,地区以及负责人应已录入");
			return "redirect:/tostationadd";
		}

		rA.addAttribute("Name", selectName);

		return "redirect:/dicStation";
	}

	@RequestMapping("/tostationdelete")
	public String tostationdelete(@RequestParam(value = "id") Integer Id, @RequestParam(value = "selectName", required = false, defaultValue = "") String selectName,
								  RedirectAttributes rA) {

		int id = Id.intValue();

		Station station = stationR.findById(id);

		try{
			stationR.delete(station);
		}catch(Exception e){
			System.out.println("请先删除与该分站相关联的数据");
			rA.addAttribute("errormessage", "请先删除与该分站相关联的数据");

		}
		rA.addAttribute("Name", selectName);
		return "redirect:/dicStation";
	}

	@RequestMapping("/uploadstation")
	public String uploadstation(Model model, @RequestParam("file") MultipartFile file,
								RedirectAttributes redirectAttributes) throws Exception {
		XSSFWorkbook book = new XSSFWorkbook(file.getInputStream());
		XSSFSheet sheet = book.getSheetAt(0);
		for (int i = 1; i < sheet.getLastRowNum() + 1; i++) {
			Station station = new Station();
			XSSFRow row = sheet.getRow(i);
			try {
				station.setStationName(row.getCell(0).getStringCellValue());
				station.setAreaId((int)row.getCell(1).getNumericCellValue());
				station.setPrincipalId((int)row.getCell(2).getNumericCellValue());
			} catch (Exception e) {
				System.out.println("数据转换错误");
			}

			try {
				if(staffR.findById(station.getPrincipalId().intValue())==null){throw new Exception();}
				stationR.save(station);
			} catch (Exception e) {
				System.out.println("存储失败");
			}
		}

		return "redirect:/todicstation";
	}
				////////////////////////////////////////////
	//////////////////////////////////////////
	//////////////////////////////////////////

	//去重函数
	public static List removechongfu(List list){
		List listTemp = new ArrayList();
		for(int i=0;i<list.size();i++){
			if(!listTemp.contains(list.get(i))){
				listTemp.add(list.get(i));
			}
		}
		return listTemp;
	}


	////////////////////////////////////////////

	@RequestMapping("/todicdeliverpart")
	public String todeliverpart(ModelMap model){
		List<Object[]> dpAddresslist = removechongfu(dpR.showalldeliverpartAddress());
		model.addAttribute("deliverpartAddressList", dpAddresslist);

		return "dic1/deliverpart/dicdeliverpart" ;
	}

	//	@RequestMapping("/findalldeliverpart")
//	public String findalldeliverpart(@RequestParam(value = "page", defaultValue = "0") Integer page,
//			@RequestParam(value = "size", defaultValue = "6") Integer size,ModelMap model){
//		Sort sort = new Sort(Sort.Direction.ASC, "id");
//		Pageable pageable = PageRequest.of(page, size, sort);
//		Page<Object[]> list = dpR.findAllDeliverpart(pageable);
//		model.addAttribute("objectlist", list);
//		return "dic1/deliverpart/dicdeliverpart" ;
//	}
	@RequestMapping("/tofinddeliverpart")
	public String tofinddeliverpart(@RequestParam(value = "page", defaultValue = "0") Integer page,
									@RequestParam(value = "size", defaultValue = "6") Integer size,
									@RequestParam(value = "deliverpartAddress") String Address,
									HttpServletRequest request,ModelMap model){
		Sort sort = new Sort(Sort.Direction.ASC, "id");
		Pageable pageable = PageRequest.of(page, size, sort);
		int sid = (Integer)request.getSession().getAttribute("stationId");
		List<Object[]> dpAddresslist = removechongfu(dpR.showalldeliverpartAddress());
		model.addAttribute("deliverpartAddressList", dpAddresslist);
		if (Address.isEmpty()) {
			return "redirect:/findAllDeliverpart" ;
		}else {

			Page<Object[]> list = dpR.findByAddress(Address,sid,pageable);
			model.addAttribute("objectlist", list);
			model.addAttribute("objectlistp", list);
			return "dic1/deliverpart/dicdeliverpart" ;

		}
	}

	@RequestMapping("/findAllDeliverpart")
	public String findAllDeliverpart(@RequestParam(value = "page", defaultValue = "0") Integer page,
									 @RequestParam(value = "size", defaultValue = "6") Integer size,
									 HttpServletRequest request,ModelMap model){
		Sort sort = new Sort(Sort.Direction.ASC, "id");
		Pageable pageable = PageRequest.of(page, size, sort);
		int sid = (Integer)request.getSession().getAttribute("stationId");
		Page<Object[]> list = dpR.findAllDeliverpart(sid,pageable);
		model.addAttribute("objectlist", list);
		List<Object[]> listp = dpR.findAllDeliverpart(sid);
		model.addAttribute("objectlistp", listp);
		List<Object[]> dpAddresslist = removechongfu(dpR.showalldeliverpartAddress());
		model.addAttribute("deliverpartAddressList", dpAddresslist);
		return "dic1/deliverpart/dicdeliverpart" ;
	}

	@RequestMapping("/todeliverpartprint")
	public String todeliverpartprint(){
		return "dic1/deliverpart/deliverpartprint";
	}

	@RequestMapping("/todeliverpartadd")
	public String todeliverpartadd(HttpServletRequest request,Model model){
		List<Area> alist = aR.findAll();
		model.addAttribute("alist", alist);
		List<Object[]> slist = staffR.showAll();
		model.addAttribute("slist", slist);
		int sId=(Integer)request.getSession().getAttribute("stationId");
		System.out.println(sId);
		model.addAttribute("sId",sId);
		return "dic1/deliverpart/deliverpartadd";
	}
	@RequestMapping("/adddeliverpart")
	public String adddeliverpart(HttpServletRequest request,@Valid DeliverpartParam deliverpartP, BindingResult result, Model model){
		String errorMsg="";
		//参数校验
		if(result.hasErrors()){
			List<ObjectError> list=result.getAllErrors();
			for(ObjectError error:list){
				errorMsg=errorMsg+error.getCode()+"-"+error.getDefaultMessage()+";";
			}
			model.addAttribute("errorMsg",errorMsg);
			List<Area> alist = aR.findAll();
			model.addAttribute("alist", alist);
			List<Object[]> slist = staffR.showAll();
			model.addAttribute("slist", slist);
			model.addAttribute("sId",(Integer)request.getSession().getAttribute("stationId"));
			return "dic1/deliverpart/deliverpartadd";
		}

		Deliverpart dp=new Deliverpart();
		BeanUtils.copyProperties(deliverpartP,dp);
		dpR.save(dp);
		return "redirect:/findAllDeliverpart" ;
	}

	@RequestMapping("/toEditdeliverpart")
	public String toEditdeliverpart(@RequestParam(value = "id") Integer Id,HttpServletRequest request,
									@RequestParam(value = "errormessage", required = false, defaultValue = "") String errormessage,
									ModelMap model){
		List<Area> alist = aR.findAll();
		model.addAttribute("alist", alist);
		List<Object[]> slist = staffR.showAll();
		model.addAttribute("slist", slist);
		model.addAttribute("sId", (Integer)request.getSession().getAttribute("stationId"));
		int id = Id.intValue();
		Deliverpart deliverpart = dpR.findById(id);
		model.addAttribute("deliverpart",deliverpart);
		model.addAttribute(errormessage, errormessage);
		System.out.println(errormessage);
		return "dic1/deliverpart/deliverpartedit";
	}
	@RequestMapping("/deliverpartedit")
	public String deliverpartedit(HttpServletRequest request,@Valid DeliverpartParam deliverpartP, BindingResult result, ModelMap model){
		String errorMsg="";
		//参数校验
		if(result.hasErrors()){
			List<ObjectError> list=result.getAllErrors();
			for(ObjectError error:list){
				errorMsg=errorMsg+error.getDefaultMessage()+";";
			}
			model.addAttribute("errorMsg",errorMsg);
			Deliverpart deliverpart = dpR.findById(deliverpartP.getId());
			model.addAttribute("deliverpart",deliverpart);
			List<Area> alist = aR.findAll();
			model.addAttribute("alist", alist);
			List<Object[]> slist = staffR.showAll();
			model.addAttribute("slist", slist);
			model.addAttribute("sId", (Integer)request.getSession().getAttribute("stationId"));
			return "dic1/deliverpart/deliverpartedit";
		}
		Deliverpart deliverpart=new Deliverpart();
		BeanUtils.copyProperties(deliverpartP,deliverpart);
		dpR.save(deliverpart);
		return "redirect:/findAllDeliverpart" ;
	}

	@RequestMapping("/deletedeliverpart")
	public String deletedeliverpart(@RequestParam(value = "id") Integer Id, ModelMap model){
		int id = Id.intValue();

		Deliverpart deliverpart = dpR.findById(id);

		dpR.delete(deliverpart);

		return "redirect:/findAllDeliverpart" ;
	}

	//以下为投递段分配
	@RequestMapping("/todicorder")
	public String todicorder(ModelMap model){
		List<Object[]> cnList = removechongfu(ordersRepository.showallconsumerName());
		model.addAttribute("consumerNamelist", cnList);
		return "dic1/fenpeiorder/dicorder";
	}

	@RequestMapping("/findAllorder")
	public String findAllorder(@RequestParam(value = "page", defaultValue = "0") Integer page,
							   @RequestParam(value = "size", defaultValue = "6") Integer size,
							   ModelMap model,HttpServletRequest request){
		Pageable pageable = PageRequest.of(page, size);
		int sid = (Integer) request.getSession().getAttribute("stationId");
		Page<Object[]> list = ordersRepository.findAllorder(sid,pageable);
		model.addAttribute("objectlist", list);
		List<Object[]> cnList = removechongfu(ordersRepository.showallconsumerName());
		model.addAttribute("consumerNamelist", cnList);
		return "dic1/fenpeiorder/dicorder";
	}

	@RequestMapping("/tofindorder")
	public String tofindorder(@RequestParam(value = "page", defaultValue = "0") Integer page,
							  @RequestParam(value = "size", defaultValue = "6") Integer size,
							  @RequestParam(value = "consumerName") String cName,
							  ModelMap model, HttpServletRequest request){
		Pageable pageable = PageRequest.of(page, size);
		int sid = (Integer) request.getSession().getAttribute("stationId");
		List<Object[]> cnList = removechongfu(ordersRepository.showallconsumerName());
		model.addAttribute("consumerNamelist", cnList);
		if (cName.isEmpty()) {
			return "redirect:/findAllorder";
		}else {

			Page<Object[]> list = ordersRepository.findorderBycName(cName,sid, pageable);
			model.addAttribute("objectlist", list);
			return "dic1/fenpeiorder/dicorder";
		}

	}

	@RequestMapping("/tofenpeiorder")
	public String tofenpeiorder(@RequestParam(value = "id") Integer Id,
								HttpServletRequest request,ModelMap model){
		List<Object[]> dList = dpR.showAll();
		model.addAttribute("dlist", dList);
		int id = Id.intValue();
		model.addAttribute("id",id);
		model.addAttribute("sId",(Integer)request.getSession().getAttribute("stationId"));
		return "dic1/fenpeiorder/orderfenpei";
	}
	@RequestMapping("/orderfenpei")
	public String orderfenpei(@RequestParam(value = "id") Integer id,@RequestParam(value = "deliverpartId") Integer deliverpartId, ModelMap model){
		int Id = id.intValue();
		if(deliverRepository.findByorderId(Id)==null){
			System.out.println("xssdgchgccgcg");
			Deliver deliver = new Deliver();
			deliver.setOrderId(id);
			deliver.setDeliverpartId(deliverpartId);
			deliverRepository.save(deliver);
		}else {
			Deliver deliver = deliverRepository.findByorderId(Id);
			deliver.setDeliverpartId(deliverpartId);
			System.out.println(deliverpartId);
			deliverRepository.save(deliver);
		}
		return "redirect:/findAllorder";
	}

	//以下为投递排序

	@RequestMapping("/todicpriority")
	public String todicpriority(ModelMap model){
		List<Object[]> cnList = removechongfu(ordersRepository.showallconsumerName());
		model.addAttribute("consumerNamelist", cnList);
		return "dic1/priority/dicpriority";
	}

	@RequestMapping("/findAllpriority")
	public String findAllpriority(@RequestParam(value = "page", defaultValue = "0") Integer page,
								  @RequestParam(value = "size", defaultValue = "6") Integer size,
								  HttpServletRequest request,ModelMap model){
		Pageable pageable = PageRequest.of(page, size);
		int sid = (Integer)request.getSession().getAttribute("stationId");
		Page<Object[]> list = deliverRepository.findAllpriority(sid,pageable);
		model.addAttribute("objectlist", list);
		List<Object[]> cnList = removechongfu(ordersRepository.showallconsumerName());
		model.addAttribute("consumerNamelist", cnList);
		return "dic1/priority/dicpriority" ;
	}

	@RequestMapping("/tofindpriority")
	public String tofindpriority(@RequestParam(value = "page", defaultValue = "0") Integer page,
								 @RequestParam(value = "size", defaultValue = "6") Integer size,
								 @RequestParam(value = "consumerName") String cName,
								 HttpServletRequest request,ModelMap model){
		Pageable pageable = PageRequest.of(page, size);
		int sid = (Integer)request.getSession().getAttribute("stationId");
		List<Object[]> cnList = removechongfu(ordersRepository.showallconsumerName());
		model.addAttribute("consumerNamelist", cnList);
		if (cName.isEmpty()) {
			return "redirect:/findAllpriority";
		}else {

			Page<Object[]> list = deliverRepository.findpriorityBycName(cName, sid,pageable);
			model.addAttribute("objectlist", list);
			return "dic1/priority/dicpriority" ;
		}
	}

	@RequestMapping("/topriorityedit")
	public String topriorityedit(@RequestParam(value = "id") Integer Id,
								 ModelMap model){
		int id = Id.intValue();
		model.addAttribute("id",id);
		return "dic1/priority/priorityedit";
	}
	@RequestMapping("/priorityrdit")
	public String priorityrdit(@RequestParam(value = "id") Integer id,@RequestParam(value = "priority") Integer priority, ModelMap model){
		int Id = id.intValue();
		Deliver deliver = deliverRepository.findById(Id);
		deliver.setPriority(priority);
		deliverRepository.save(deliver);
		model.addAttribute("order",deliver);
		return "redirect:/findAllpriority";
	}

	//以下为下数单管理（发行站）
	@RequestMapping("/todicrecinum")
	public String todicrecinum(){

		return "dic1/recinum/dicrecinum";
	}
	@RequestMapping("/tofindrecinum")
	public String tofindrecinum(@RequestParam(value = "page", defaultValue = "0") Integer page,
								@RequestParam(value = "size", defaultValue = "6") Integer size,@Valid TotalReciParam trP, BindingResult result,
								ModelMap model){
		String errormessage = "";
		if (result.hasErrors()) {
			List<ObjectError> list = result.getAllErrors();
			for (ObjectError error : list) {
				errormessage = errormessage + error.getDefaultMessage() + ";";
			}
			model.addAttribute("errormessage", errormessage);
			return "dic1/recinum/dicrecinum";
		}

		int stationId=trP.getStationId();

		if(trP.getTime() == null){
			Pageable pageable = PageRequest.of(page, size);
			Page<Reci> list=rnR.findByStationId(stationId, pageable);

			model.addAttribute("objectlist", list);

			return "dic1/recinum/dicrecinum";
		}else{
			String time=sdf.format(trP.getTime());
			Pageable pageable = PageRequest.of(page, size);
			Page<Reci> list=rnR.findOneByStationIdAndTime(stationId, time, pageable);

			model.addAttribute("objectlist", list);

			return "dic1/recinum/dicrecinum";
		}
	}

	@RequestMapping("/toreciedit1")
	public String toreciedit(@Valid TotalReciParam trP, BindingResult result,@RequestParam(value = "errormessage", required = false, defaultValue = "") String errormessage ,ModelMap model){

		if (result.hasErrors()) {
			model.addAttribute("errormessage", "编辑出错，请重试");
			return "dic1/recinum/dicrecinum";
		}
		Integer StationId=trP.getStationId();
		Date Time=trP.getTime();

		int stationId=StationId.intValue();
		String time=sdf.format(Time);

		Reci reci=rnR.findRByStationIdAndTime(stationId, time);
		model.addAttribute("reci", reci);
		model.addAttribute(errormessage, errormessage);

		return "dic1/recinum/recinumedit";
	}

	@RequestMapping("/reciedit1")
	public String reciedit(@Valid EditReciParam erP, BindingResult result, ModelMap model, RedirectAttributes rA){

		String errormessage = "";
		if (result.hasErrors()) {
			List<ObjectError> list = result.getAllErrors();
			for (ObjectError error : list) {
				errormessage = errormessage + error.getDefaultMessage() + ";";
			}
			rA.addAttribute("errormessage", errormessage);
			rA.addAttribute("stationId", erP.getStationId());
			rA.addAttribute("time", erP.getTime());
			return "redirect:/toreciedit";

		}

		int stationId=erP.getStationId().intValue();
		String time=sdf.format(erP.getTime());

		Reci reci=rnR.findRByStationIdAndTime(stationId, time);
		reci.setNeedNum(erP.getNeedNum());


		try{
			rnR.save(reci);
		}catch(Exception e){
			errormessage="存储失败";
			rA.addAttribute("errormessage", errormessage);
			rA.addAttribute("stationId", erP.getStationId());
			rA.addAttribute("time", erP.getTime());
			return "redirect:/toreciedit";
		}

		return "dic1/recinum/dicrecinum";
	}

	@RequestMapping("/toaddreci")
	public String toaddreci(ModelMap model){
		List<Station> list = sR.showAll();
		model.addAttribute("list", list);
		return "dic1/recinum/recinumadd";
	}
	@RequestMapping("/reciadd")
	public String reciadd(@Valid ReciAddParam rAddParam, BindingResult result, ModelMap model, RedirectAttributes rA){
		String errormessage = "";
		System.out.println("11111");
		if (result.hasErrors()) {
			List<Station> list1 = sR.showAll();
			model.addAttribute("list", list1);
			List<ObjectError> list = result.getAllErrors();
			for (ObjectError error : list) {
				errormessage = errormessage + error.getDefaultMessage() + ";";
			}
			model.addAttribute("errormessage", errormessage);
			model.addAttribute("stationId", rAddParam.getStationId());
			model.addAttribute("time", rAddParam.getTime());
			return "dic1/recinum/recinumadd";

		}

		Reci reci = new Reci();
		reci.setStationId(rAddParam.getStationId());
		reci.setNeedNum(rAddParam.getNeedNum());
		Date time = rAddParam.getTime();
		if(time!=null){System.out.println(time.toString());}else {
			System.out.println("null");
		}
		System.out.println(time.getTime());
		reci.setDate(rAddParam.getTime());
		try{
			rnR.save(reci);
		}catch(Exception e){
			errormessage="存储失败";
			rA.addAttribute("errormessage", errormessage);
			rA.addAttribute("stationId", rAddParam.getStationId());
			rA.addAttribute("time", rAddParam.getTime());
			rA.addAttribute("NeedNum", rAddParam.getNeedNum());
			return "redirect:/toaddreci";
		}
		return "dic1/recinum/dicrecinum";
	}

	@RequestMapping("/deletereci")
	public String deletereci(@Valid TotalReciParam trP,ModelMap model){
		Integer StationId=trP.getStationId();
		Date Time=trP.getTime();

		int stationId=StationId.intValue();
		String time=sdf.format(Time);

		Reci reci=rnR.findRByStationIdAndTime(stationId, time);
		rnR.delete(reci);

		return "dic1/recinum/dicrecinum";
	}


	//催款单打印
	@RequestMapping("/toshowrepay")
	public String toshowrepay(ModelMap model){
		List<Object[]> cnList = removechongfu(ordersRepository.showallconsumerName());
		model.addAttribute("consumerNamelist", cnList);
		return "dic1/repay/showrepay";
	}

	@RequestMapping("/findAllrepay")
	public String findAllrepay(@RequestParam(value = "page", defaultValue = "0") Integer page,
							   @RequestParam(value = "size", defaultValue = "6") Integer size,
							   HttpServletRequest request,ModelMap model){
		Pageable pageable = PageRequest.of(page, size);
		int sid = (Integer)request.getSession().getAttribute("stationId");
		List<Object[]> cnList = removechongfu(ordersRepository.showallconsumerName());
		model.addAttribute("consumerNamelist", cnList);
		Page<Object[]> list = ordersRepository.findAllrepay(sid,pageable);
		model.addAttribute("objectlist", list);
		return "dic1/repay/showrepay";
	}

	@RequestMapping("/tofindrepay")
	public String tofindrepay(@RequestParam(value = "page", defaultValue = "0") Integer page,
							  @RequestParam(value = "size", defaultValue = "6") Integer size,
							  @RequestParam(value="consumerName") String cName,
							  HttpServletRequest request,ModelMap model){
		Pageable pageable = PageRequest.of(page, size);
		int sid = (Integer)request.getSession().getAttribute("stationId");
		List<Object[]> cnList = removechongfu(ordersRepository.showallconsumerName());
		model.addAttribute("consumerNamelist", cnList);
		if (cName.isEmpty()) {
			return "redirect:/findAllrepay";
		}else {
			Page<Object[]> list = ordersRepository.findrepayBycName(cName,sid, pageable);
			model.addAttribute("objectlist", list);
			return "dic1/repay/showrepay";
		}

	}

	@RequestMapping("/toprintrepay")
	public String toprintrepay(@RequestParam(value = "page", defaultValue = "0") Integer page,
							   @RequestParam(value = "size", defaultValue = "6") Integer size,
							   @RequestParam(value="id") Integer Id,
							   HttpServletRequest request,ModelMap model){
		Pageable pageable = PageRequest.of(page, size);
		int sid = (Integer) request.getSession().getAttribute("stationId");
		int id = Id.intValue();
		Page<Object[]> list = ordersRepository.findrepayByid(id,sid, pageable);
		model.addAttribute("objectlist", list);
		return "dic1/repay/printrepay";
	}


	//完成情况登记
	@RequestMapping("/toshowcompletion")
	public String toshowcompletion(ModelMap model){
		List<Object[]> cnList = removechongfu(ordersRepository.showallconsumerName());
		model.addAttribute("consumerNamelist", cnList);
		return "dic1/completion/showcompletion";
	}

	@RequestMapping("/findAllforcom")
	public String findAllforcom(@RequestParam(value = "page", defaultValue = "0") Integer page,
								@RequestParam(value = "size", defaultValue = "6") Integer size,
								HttpServletRequest request,ModelMap model){
		Sort sort = new Sort(Sort.Direction.ASC, "id");
		Pageable pageable = PageRequest.of(page, size, sort);
		int sid = (Integer)request.getSession().getAttribute("stationId");
		Page<Object[]> list = deliverRepository.findAllforcom(sid,pageable);
		model.addAttribute("objectlist", list);
		List<Object[]> cnList = removechongfu(ordersRepository.showallconsumerName());
		model.addAttribute("consumerNamelist", cnList);
		return "dic1/completion/showcompletion";
	}

	@RequestMapping("/finddeliver")
	public String findonedeliver(@RequestParam(value = "page", defaultValue = "0") Integer page,
								 @RequestParam(value = "size", defaultValue = "6") Integer size,
								 @RequestParam(value="consumerName") String cName,
								 HttpServletRequest request,ModelMap model){
		Sort sort = new Sort(Sort.Direction.ASC, "id");
		Pageable pageable = PageRequest.of(page, size, sort);
		int sid = (Integer)request.getSession().getAttribute("stationId");
		List<Object[]> cnList = removechongfu(ordersRepository.showallconsumerName());
		model.addAttribute("consumerNamelist", cnList);
		if(cName.isEmpty()){
			return "redirect:/findAllforcom";
		}else {

			Page<Object[]> list = deliverRepository.findoneBycName(cName,sid,pageable);
			model.addAttribute("objectlist", list);
			return "dic1/completion/showcompletion";
		}

	}


	@RequestMapping("/tocompletionedit")
	public String tocompletionedit(@RequestParam(value = "id") Integer Id,ModelMap model){
		int id = Id.intValue();
		Deliver deliver = deliverRepository.findById(id);
		model.addAttribute("id",id);
		model.addAttribute("list",deliver);
		return "dic1/completion/completionedit";
	}

	@RequestMapping("/editcompletion")
	public String editcompletion(@RequestParam(value = "id") Integer id,@RequestParam(value = "status") Integer status, ModelMap model){
		int Id = id.intValue();
		Deliver deliver = deliverRepository.findById(Id);
		deliver.setStatus(status);
		if(status == 1){
			deliver.setFinishDate(new Date());
		}else{
			deliver.setFinishDate(null);
		}
		deliverRepository.save(deliver);
		model.addAttribute("order",deliver);
		return "redirect:/findAllforcom";
	}

	@RequestMapping("/sendback")
	public String sendback(int id){

		Deliver list = deliverRepository.findById(id);
		list.setStatus(4);
		list.setFinishDate(null);
		deliverRepository.save(list);
		return "redirect:/findAllforcom";
	}


	//员工职务设置
	@RequestMapping("/todicduty1")
	public String todicduty(HttpServletRequest request,ModelMap model){
		int sid = (Integer)request.getSession().getAttribute("stationId");
		List<Object[]> cnList = removechongfu(dutyR.showAllFXDutyName(sid));
		model.addAttribute("dutyNamelist", cnList);
		return "dic1/duty/dicduty";
	}

	@RequestMapping("/findAllFXDuty")
	public String findAllFXDuty(@RequestParam(value = "page", defaultValue = "0") Integer page,
								@RequestParam(value = "size", defaultValue = "6") Integer size,
								HttpServletRequest request, ModelMap model){
		Sort sort = new Sort(Sort.Direction.ASC, "id");
		Pageable pageable = PageRequest.of(page, size);
		int sid = (Integer)request.getSession().getAttribute("stationId");
		List<Object[]> cnList = removechongfu(dutyR.showAllFXDutyName(sid));
		model.addAttribute("dutyNamelist", cnList);
		Page<Object[]> list = dutyR.findAllShow(sid,pageable);
		model.addAttribute("objectlist", list);
		return "dic1/duty/dicduty";
	}

	@RequestMapping("/dicDuty1")
	public String getduty(@RequestParam(value = "page", defaultValue = "0") Integer page,
						  @RequestParam(value = "size", defaultValue = "6") Integer size,
						  @RequestParam(value = "dutyId") Integer Id,
						  @RequestParam(value = "dutyName") String dutyName,
						  HttpServletRequest request, ModelMap model) {

		System.out.println("传入id为:");
		System.out.println(Id);
		System.out.println("传入name为:");
		System.out.println(dutyName);

		Sort sort = new Sort(Sort.Direction.ASC, "id");
		Pageable pageable = PageRequest.of(page, size);


		model.addAttribute("dutyId", Id);
		model.addAttribute("dutyName", dutyName);
		int sid = (Integer)request.getSession().getAttribute("stationId");
		List<Object[]> cnList = removechongfu(dutyR.showAllFXDutyName(sid));
		model.addAttribute("consumerNamelist", cnList);
		// 优先id查找
		if (Id != null) {
			int dutyId = Id.intValue();
			Page<Object[]> list = dutyR.findByIdShow(dutyId,sid, pageable);
			model.addAttribute("objectlist", list);

			return "dic1/duty/dicduty";

		}

		// id不存在的话根据name查找
		if (dutyName != "") {
			Page<Object[]> list = dutyR.findByDutyNameShow(dutyName,sid, pageable);
			model.addAttribute("objectlist", list);
			return "dic1/duty/dicduty";
		}


		return "redirect:/findAllFXDuty";
	}


	@RequestMapping("/todutyadd1")
	public String todutyadd( HttpServletRequest request, ModelMap model){
		int sid = (Integer)request.getSession().getAttribute("stationId");
		List<Department> dList = departmentR.findByStationId(sid);
		model.addAttribute("departlist", dList);
		List<Merit> mList = meritR.findAll();
		model.addAttribute("meritlist", mList);
		return "dic1/duty/dutyadd";
	}

	@RequestMapping("/todutyedit1")
	public String todutyedit1(@RequestParam(value = "id") Integer Id,
							  @RequestParam(value = "errormessage", required = false, defaultValue = "") String errormessage,
							  HttpServletRequest request, ModelMap model) {

		int id = Id.intValue();

		Duty duty = dutyR.findById(id);

		model.addAttribute("duty", duty);

		model.addAttribute(errormessage, errormessage);
		System.out.println(errormessage);
		int sid = (Integer)request.getSession().getAttribute("stationId");
		List<Department> dList = departmentR.findByStationId(sid);
		model.addAttribute("departlist", dList);
		List<Merit> mList = meritR.findAll();
		model.addAttribute("meritlist", mList);
		return "dic1/duty/dutyedit";
	}

	@RequestMapping("/dutyedit1")
	public String dutyedit1(@Valid DutyParam dutyP, BindingResult result, ModelMap model, RedirectAttributes rA) {
		String errormessage = "";
		if (result.hasErrors()) {
			errormessage = "数据不合法";
			rA.addAttribute("errormessage", errormessage);
			System.out.println(dutyP.getDutyId().intValue());
			rA.addAttribute("id", dutyP.getDutyId());
			return "redirect:/todutyedit1";
		}

		Duty duty = new Duty();
		try {
			duty.setId(dutyP.getDutyId().intValue());
			duty.setDutyName(dutyP.getDutyName());
			duty.setDepartId(dutyP.getDutyDepartId());
			duty.setMeritId(dutyP.getDutyMeritId());

		} catch (Exception e) {
			System.out.println("数据格式不正确");
			errormessage = "数据格式不正确";
			rA.addAttribute("errormessage", errormessage);
			rA.addAttribute("id", dutyP.getDutyId());
			return "redirect:/todutyedit1";
		}

		try {
			dutyR.save(duty);
		} catch (Exception e) {
			System.out.println("请输入正确的数据");
			errormessage = "请输入正确的数据";
			rA.addAttribute("errormessage", errormessage);
			rA.addAttribute("id", dutyP.getDutyId());
			return "redirect:/todutyedit1";
		}

		return "redirect:/findAllFXDuty";
	}

	@RequestMapping("/dutyadd1")
	public String dutyadd1(@Valid DutyParam dutyP, BindingResult result, ModelMap model) {

		String errormessage = "";
		if (result.hasErrors()) {
			errormessage = "数据不合法";
			model.addAttribute("errormessage", errormessage);
			return "dic1/duty/dutyadd";
		}

		Duty duty = new Duty();

		try {
			duty.setDutyName(dutyP.getDutyName());
			duty.setDepartId(dutyP.getDutyDepartId());
			duty.setMeritId(dutyP.getDutyMeritId());
		} catch (Exception e) {
			System.out.println("数据格式不正确");
			model.addAttribute("errormessage", "数据格式不正确");
			return "dic1/duty/dutyadd";
		}

		try {
			dutyR.save(duty);
		} catch (Exception e) {
			System.out.println("请输入正确的数据");
			model.addAttribute("errormessage", "请输入正确的数据");
			return "dic1/duty/dutyadd";
		}

		return "redirect:/findAllFXDuty";
	}

	@RequestMapping("/todutydelete1")
	public String todutydelete1(@RequestParam(value = "id") Integer Id, ModelMap model) {

		int id = Id.intValue();

		Duty duty = dutyR.findById(id);

		try{
			dutyR.delete(duty);
		}catch(Exception e){

		}
		return "redirect:/findAllFXDuty";
	}

	//员工设置
	@RequestMapping("/todicstaff1")
	public String todicstaff(HttpServletRequest request,
							 ModelMap model){
		int sid = (Integer)request.getSession().getAttribute("stationId");
		List<Object[]> cnList = removechongfu(staffR.showAllFXStaffName(sid));
		model.addAttribute("staffNamelist", cnList);
		return "dic1/staff/dicstaff";
	}

	@RequestMapping("/findAllFXStaff")
	public String findAllFXStaff(@RequestParam(value = "page", defaultValue = "0") Integer page,
								 @RequestParam(value = "size", defaultValue = "6") Integer size,
								 HttpServletRequest request,
								 ModelMap model){
		Pageable pageable = PageRequest.of(page, size);
		int sid = (Integer)request.getSession().getAttribute("stationId");
		List<Object[]> cnList = removechongfu(staffR.showAllFXStaffName(sid));
		model.addAttribute("staffNamelist", cnList);
		System.out.println("进入全部查找");
		Page<Object[]> list = staffR.findAllShow(sid,pageable);
		model.addAttribute("objectlist", list);
		System.out.println("全部查找结束");

		return "dic1/staff/dicstaff";
	}

	@RequestMapping("/dicstaff1")
	public String getstaff(@RequestParam(value = "page", defaultValue = "0") Integer page,
						   @RequestParam(value = "size", defaultValue = "6") Integer size,
						   @RequestParam(value = "id") Integer Id,
						   @RequestParam(value = "staffName") String staffName,
						   HttpServletRequest request,
						   ModelMap model) {

		System.out.println("传入id为:");
		System.out.println(Id);
		System.out.println("传入name为:");
		System.out.println(staffName);

		Pageable pageable = PageRequest.of(page, size);

		model.addAttribute("id", Id);
		model.addAttribute("staffName", staffName);
		int sid = (Integer)request.getSession().getAttribute("stationId");
		List<Object[]> cnList = removechongfu(staffR.showAllFXStaffName(sid));
		model.addAttribute("staffNamelist", cnList);
		// 优先id查找
		if (Id != null) {
			int staffId = Id.intValue();
			Page<Object[]> list = staffR.findByIdShow(sid,staffId, pageable);
			model.addAttribute("objectlist", list);

			return "dic1/staff/dicstaff";

		}

		// id不存在的话根据name查找
		if (staffName != "") {
			System.out.println("根据name查找");
			Page<Object[]> list = staffR.findByStaffNameShow(sid,staffName, pageable);
			model.addAttribute("objectlist", list);
			return "dic1/staff/dicstaff";
		}
		return "redirect:/findAllFXStaff";

	}

	@RequestMapping("/tostaffedit1")
	public String tostaffedit(@RequestParam(value = "id") Integer Id,
							  @RequestParam(value = "errormessage", required = false,
									  defaultValue = "") String errormessage,HttpServletRequest request,
							  ModelMap model) {

		int id = Id.intValue();
		int sid = (Integer)request.getSession().getAttribute("stationId");
		List<Object[]> dList = dutyR.showAllFXDuty(sid);
		model.addAttribute("dutylist", dList);
		Staff staff = staffR.findById(id);

		model.addAttribute("staff", staff);

		model.addAttribute(errormessage, errormessage);
		System.out.println(errormessage);

		return "dic1/staff/staffedit";
	}

	@RequestMapping("/staffedit1")
	public String staffedit(@Valid FXStaffParam staffP, BindingResult result, ModelMap model, RedirectAttributes rA) {
		String errormessage = "";
		if (result.hasErrors()) {
			errormessage = "数据不合法";
			rA.addAttribute("errormessage", errormessage);
			rA.addAttribute("id", staffP.getId());
			return "redirect:/tostaffedit1";
		}
		int id = staffP.getId();
		Staff staff = staffR.findById(id);
		try {
			staff.setStaffName(staffP.getStaffName());
			staff.setDutyId(staffP.getDutyId());
			staff.setAuthority(staffP.getAuthority());
			staff.setPhone(staffP.getPhone());
			staff.setPassword(staffP.getPassword());

		} catch (Exception e) {
			System.out.println("数据格式不正确");
			errormessage = "数据格式不正确";
			rA.addAttribute("errormessage", errormessage);
			rA.addAttribute("id", staffP.getId());
			return "redirect:/tostaffedit1";
		}

		try {
			staffR.save(staff);
		} catch (Exception e) {
			System.out.println("请输入正确的数据");
			errormessage = "请输入正确的数据";
			rA.addAttribute("errormessage", errormessage);
			rA.addAttribute("id", staffP.getId());
			return "redirect:/tostaffedit1";
		}

		return "redirect:/findAllFXStaff";
	}

	@RequestMapping("/tostaffadd1")
	public String tostaffadd(HttpServletRequest request,
							 ModelMap model){
		int sid = (Integer)request.getSession().getAttribute("stationId");
		List<Object[]> dList = dutyR.showAllFXDuty(sid);
		model.addAttribute("dutylist", dList);
		return "dic1/staff/staffadd";
	}

	@RequestMapping("/staffadd1")
	public String staffadd(@Valid FXStaffParam staffP, BindingResult result, ModelMap model) {

		String errormessage = "";
		if (result.hasErrors()) {
			errormessage = "数据不合法";
			model.addAttribute("errormessage", errormessage);
			return "dic1/staff/staffadd";
		}

		Staff staff = new Staff();

		try {
			staff.setStaffName(staffP.getStaffName());
			staff.setDutyId(staffP.getDutyId());
			staff.setDepartId(staffR.findDepartIdByDutyId(staffP.getDutyId()));
			staff.setAuthority(staffP.getAuthority());
			staff.setPhone(staffP.getPhone());
			staff.setPassword(staffP.getPassword());
		} catch (Exception e) {
			System.out.println("数据格式不正确");
			model.addAttribute("errormessage", "数据格式不正确");
			return "dic1/staff/staffadd";
		}

		try {
			staffR.save(staff);
		} catch (Exception e) {
			System.out.println("请输入正确的数据");
			model.addAttribute("errormessage", "请输入正确的数据");
			return "dic1/staff/staffadd";
		}

		return "redirect:/findAllFXStaff";
	}

	@RequestMapping("/tostaffdelete1")
	public String tostaffdelete(@RequestParam(value = "Id") Integer Id, ModelMap model) {

		int id = Id.intValue();

		Staff staff = staffR.findById(id);

		try{
			staffR.delete(staff);
		}catch(Exception e){

		}
		return "redirect:/findAllFXStaff";
	}


	//业绩统计
	@RequestMapping("/toshowmerit")
	public String toshowmerit(){
		return "dic1/merit/showmerit";
	}

	@RequestMapping("/findmerit")
	public String findmerit(@RequestParam(value = "page", defaultValue = "0") Integer page,
							@RequestParam(value = "size", defaultValue = "6") Integer size,@Valid MeritfindParam mParam,BindingResult result
			,ModelMap model){
		String errorMsg="";
		//参数校验
		if(result.hasErrors()){
			List<ObjectError> list=result.getAllErrors();
			for(ObjectError error:list){
				errorMsg=errorMsg+error.getCode()+"-"+error.getDefaultMessage()+";";
			}
			model.addAttribute("errorMsg",errorMsg);
			return "dic1/merit/showmerit";
		}

		Pageable pageable = PageRequest.of(page, size);
		Date startTime=mParam.getStartTime();
		Date endTime=mParam.getEndTime();
		if(endTime.before(startTime)){
			String errormsg = "请输入正确的时间";
			model.addAttribute("errorMsg",errormsg);
			return "dic1/merit/showmerit";
		}else {
			if (mParam.getDutyName().equals("征订员")) {
				Page<Object[]> list = meritR.findDingMerit(mParam.getStationId(), startTime, endTime, pageable);
				model.addAttribute("objectlist", list);
				return "dic1/merit/showmerit";
			} else {
				Page<Object[]> list = meritR.findSendMerit(mParam.getStationId(), startTime, endTime, pageable);
				model.addAttribute("objectlist", list);
				return "dic1/merit/showmerit";
			}
		}
	}

	//统计查询
	@RequestMapping("/toshowTongJi")
	public String toshowTongJi(){
		return "findAndShow/tongji/showTongJi";
	}

	@RequestMapping("/findTongJi")
	public String findTongJi(@Valid FindParam fParam,BindingResult result,ModelMap model){
		String errorMsg="";
		//参数校验
		if(result.hasErrors()){
			List<ObjectError> list=result.getAllErrors();
			for(ObjectError error:list){
				errorMsg=errorMsg+error.getCode()+"-"+error.getDefaultMessage()+";";
			}
			model.addAttribute("errorMsg",errorMsg);
			return "findAndShow/tongji/showTongJi";
		}
		int sId = fParam.getStationId();
		Date sDate=fParam.getStartTime();
		Date eDate=fParam.getEndTime();
		List<Object[]> oList=ordersRepository.findtongjiorder(sId, sDate, eDate);
		List<Object[]> dList=deliverRepository.findtongjideliver(sId, sDate, eDate);
		model.addAttribute("dlist", dList);
		model.addAttribute("olist", oList);
		return "findAndShow/tongji/showTongJi";
	}



	//发票查询
	@RequestMapping("/toshowfapiao")
	public String toshowfapiao(){
		return "findAndShow/fapiao/showfapiao";
	}

	@RequestMapping("/findinvoice")
	public String findinvoice(HttpServletRequest request,ModelMap model){
		int sid=(Integer)request.getSession().getAttribute("stationId");
		List<Object[]> list = invoiceR.findShowInvoice(sid);
		model.addAttribute("objectlist",list);
		return "findAndShow/fapiao/showfapiao";
	}

	//投递查询
	@RequestMapping("/toshowdeliver")
	public String toshowdeliver(HttpServletRequest request,ModelMap model){
		int sid = (Integer) request.getSession().getAttribute("stationId");
		List<Object[]> sList = removechongfu(staffR.showAllFXStaffNameByDuty(sid));
		model.addAttribute("staffNamelist", sList);
		return "findAndShow/deliver/showdeliver";
	}

	@RequestMapping("/findalldeliver")
	public String findalldeliver(@RequestParam(value = "page", defaultValue = "0") Integer page,
								 @RequestParam(value = "size", defaultValue = "6") Integer size,
								 HttpServletRequest request,ModelMap model){
		Sort sort = new Sort(Sort.Direction.ASC, "id");
		Pageable pageable = PageRequest.of(page, size,sort);
		int sid = (Integer) request.getSession().getAttribute("stationId");
		List<Object[]> sList = removechongfu(staffR.showAllFXStaffNameByDuty(sid));
		model.addAttribute("staffNamelist", sList);
		Page<Object[]> list = deliverRepository.showAll(sid, pageable);
		model.addAttribute("objectlist", list);
		return "findAndShow/deliver/showdeliver";
	}

	@RequestMapping("/tofinddeliver")
	public String tofinddeliver(@RequestParam(value = "page", defaultValue = "0") Integer page,
								@RequestParam(value = "size", defaultValue = "6") Integer size,
								HttpServletRequest request,
								@RequestParam(value="staffName")String sName,ModelMap model){
		Sort sort = new Sort(Sort.Direction.ASC, "id");
		Pageable pageable = PageRequest.of(page, size,sort);
		int sid = (Integer) request.getSession().getAttribute("stationId");
		List<Object[]> sList = removechongfu(staffR.showAllFXStaffNameByDuty(sid));
		model.addAttribute("staffNamelist", sList);
		if(sName.isEmpty()){
			return "redirect:/findalldeliver";
		}else{

			Page<Object[]> list = deliverRepository.showOneBysName(sid, sName, pageable);
			model.addAttribute("objectlist", list);
			return "findAndShow/deliver/showdeliver";
		}
	}


	//基本信息查询
	@RequestMapping("/toshowbaseinfo")
	public String toshowbaseinfo(ModelMap model){
		List<Object[]> pList = removechongfu(pressR.showAllpName());
		model.addAttribute("pressNamelist", pList);
		return "findAndShow/Baseinfo/showbaseinfo";
	}

	@RequestMapping("/showallPressInfo")
	public String showPressInfo(@RequestParam(value = "page", defaultValue = "0") Integer page,
								@RequestParam(value = "size", defaultValue = "6") Integer size,
								ModelMap model){
		Sort sort = new Sort(Sort.Direction.ASC, "id");
		Pageable pageable = PageRequest.of(page, size,sort);
		Page<Object[]> list = pressR.showPressInfo(pageable);
		model.addAttribute("objectlist", list);
		List<Object[]> pList = removechongfu(pressR.showAllpName());
		model.addAttribute("pressNamelist", pList);
		return "findAndShow/Baseinfo/showbaseinfo";
	}


	@RequestMapping("/tofindbaseinfo")
	public String tofindbaseinfo(@RequestParam(value = "page", defaultValue = "0") Integer page,
								 @RequestParam(value = "size", defaultValue = "6") Integer size,
								 @RequestParam(value="pressName")String pName,ModelMap model){
		Sort sort = new Sort(Sort.Direction.ASC, "id");
		Pageable pageable = PageRequest.of(page, size,sort);
		List<Object[]> pList = removechongfu(pressR.showAllpName());
		model.addAttribute("pressNamelist", pList);
		if(pName.isEmpty()){
			return "redirect:/showallPressInfo";
		}else {
			Page<Object[]> list = pressR.showonePressInfo(pName, pageable);
			model.addAttribute("objectlist", list);
			return "findAndShow/Baseinfo/showbaseinfo";
		}

	}


	//日常业务查询
	//款项查询
	@RequestMapping("/toshowpay")
	public String toshowpay(HttpServletRequest request,ModelMap model){
		List<Object[]> cnList = removechongfu(ordersRepository.showallconsumerName());
		model.addAttribute("consumerNamelist", cnList);
		return "findAndShow/daily/showpay";
	}

	@RequestMapping("/tofindorderpay")
	public String tofindorderpay(@RequestParam(value = "page", defaultValue = "0") Integer page,
								 @RequestParam(value = "size", defaultValue = "6") Integer size,
								 @RequestParam(value="consumerName")String cName,
								 HttpServletRequest request,ModelMap model){
		Sort sort = new Sort(Sort.Direction.ASC, "id");
		Pageable pageable = PageRequest.of(page, size,sort);
		int sid = (Integer) request.getSession().getAttribute("stationId");
		if(cName.isEmpty()){
			Page<Object[]> list = ordersRepository.findorderpay(sid, pageable);
			model.addAttribute("objectlist", list);
			return "findAndShow/daily/showpay";
		}else {

			Page<Object[]> list = ordersRepository.findoneorderpay(sid, cName, pageable);
			model.addAttribute("objectlist", list);
			return "findAndShow/daily/showpay";
		}
	}




	//报刊订阅情况查询
	@RequestMapping("/toshowpressorder")
	public String toshowpressorder(ModelMap model){
		List<Object[]> pList = removechongfu(pressR.showAllpName());
		model.addAttribute("pressNamelist", pList);
		return "findAndShow/daily/showpressorder";
	}

	@RequestMapping("/tofindpressoeder")
	public String tofindpressoeder(@RequestParam(value = "page", defaultValue = "0") Integer page,
								   @RequestParam(value = "size", defaultValue = "6") Integer size,
								   @RequestParam(value="pressName")String pName,
								   HttpServletRequest request,ModelMap model) {
		Sort sort = new Sort(Sort.Direction.ASC, "id");
		Pageable pageable = PageRequest.of(page, size, sort);
		int sid = (Integer) request.getSession().getAttribute("stationId");
		if (pName.isEmpty()) {

			Page<Object[]> list = pressR.showpressorder(sid, pageable);
			model.addAttribute("objectlist", list);
			return "findAndShow/daily/showpressorder";
		} else {
			Page<Object[]> list = pressR.showonepressorder(sid, pName, pageable);
			model.addAttribute("objectlist", list);
			return "findAndShow/daily/showpressorder";
		}

	}
}
