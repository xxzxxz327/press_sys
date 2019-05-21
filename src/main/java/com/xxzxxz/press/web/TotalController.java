package com.xxzxxz.press.web;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import com.xxzxxz.press.model.Reci;
import com.xxzxxz.press.model.Station;
import com.xxzxxz.press.repository.ReciRepository;
import com.xxzxxz.press.repository.StationRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.xxzxxz.press.param.EditReciParam;
import com.xxzxxz.press.param.TotalReciParam;
import com.xxzxxz.press.param.YaobaoParam;

@Controller
public class TotalController {

	@Autowired
	private ReciRepository rR;
	
	@Autowired
	private StationRepository stationR;

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	@RequestMapping("/totalxiadanall")
	public String totalxiadan(@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "size", defaultValue = "6") Integer size, ModelMap model) {
		
		List<Station> stationlist=stationR.findAll();

		model.addAttribute("stationlist", stationlist);
		
		Pageable pageable = PageRequest.of(page, size);
		Page<Reci> list = rR.findListAll(pageable);
		model.addAttribute("objectlist", list);
		return "total/totalxiadan";
		
	}
	
	@RequestMapping("/totalxiadan")
	public String totalxiadan(@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "size", defaultValue = "6") Integer size, @Valid TotalReciParam trP,
			BindingResult result, ModelMap model) {
		
		List<Station> stationlist=stationR.findAll();
		model.addAttribute("stationlist", stationlist);

		String errormessage = "";
		if (result.hasErrors()) {
			List<ObjectError> list = result.getAllErrors();
			for (ObjectError error : list) {
				errormessage = errormessage + error.getDefaultMessage() + ";";
			}
			model.addAttribute("errormessage", errormessage);

			return "total/totalxiadan";

		}
		
		Pageable pageable = PageRequest.of(page, size);
		if ((trP.getStationId() != null) && (trP.getTime() != null)) {

			int stationId = trP.getStationId();
			String time = sdf.format(trP.getTime());

			
			Page<Reci> list = rR.findOneByStationIdAndTime(stationId, time, pageable);

			model.addAttribute("objectlist", list);

			return "total/totalxiadan";
		}
		
		if((trP.getStationId() != null) && (trP.getTime() == null)){
			
			int stationId=trP.getStationId();
			Page<Reci> list = rR.findListByStationId(stationId, pageable);
			model.addAttribute("objectlist", list);
			return "total/totalxiadan";
		}
		
		if((trP.getStationId() == null) && (trP.getTime() != null)){
			String time = sdf.format(trP.getTime());
			Page<Reci> list = rR.findListByTime(time, pageable);
			model.addAttribute("objectlist", list);
			return "total/totalxiadan";
		}
		
		if((trP.getStationId() == null) && (trP.getTime() == null)){
			Page<Reci> list = rR.findListAll(pageable);
			model.addAttribute("objectlist", list);
			return "total/totalxiadan";
		}
		
		return "total/totalxiadan";
	}

	@RequestMapping("/toreciedit")
	public String toreciedit(@Valid TotalReciParam trP, BindingResult result,
			@RequestParam(value = "errormessage", required = false, defaultValue = "") String errormessage,
			ModelMap model) {

		if (result.hasErrors()) {
			model.addAttribute("errormessage", "编辑出错，请重试");
			return "total/totalxiadan";
		}
		Integer StationId = trP.getStationId();
		Date Time = trP.getTime();

		int stationId = StationId.intValue();
		String time = sdf.format(Time);

		Reci reci = rR.findRByStationIdAndTime(stationId, time);
		model.addAttribute("reci", reci);
		model.addAttribute("errormessage", errormessage);

		return "total/reciedit";
	}

	@RequestMapping("/reciedit")
	public String reciedit(@Valid EditReciParam erP, BindingResult result, ModelMap model, RedirectAttributes rA) {

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

		int stationId = erP.getStationId().intValue();
		String time = sdf.format(erP.getTime());

		Reci reci = rR.findRByStationIdAndTime(stationId, time);
		reci.setRealNum(erP.getRealNum());

		try {
			rR.save(reci);
		} catch (Exception e) {
			errormessage = "存储失败";
			rA.addAttribute("errormessage", errormessage);
			rA.addAttribute("stationId", erP.getStationId());
			rA.addAttribute("time", erP.getTime());
			return "redirect:/toreciedit";
		}

		return "redirect:/totalxiadanall";
	}

	@RequestMapping("/totalyaobao")
	public String totalyaobao(@Valid YaobaoParam yP, BindingResult result, ModelMap model) {

		String errormessage = "";
		if (result.hasErrors()) {
			List<ObjectError> list = result.getAllErrors();
			for (ObjectError error : list) {
				errormessage = errormessage + error.getDefaultMessage() + ";";
			}
			model.addAttribute("errormessage", errormessage);
			return "total/totalyaobao";
		}

		String time = sdf.format(yP.getTime());

		List<Reci> list = rR.findYBByTime(time);

		List<Integer> listNum = new ArrayList<Integer>();
		List<Integer> liststationId = new ArrayList<Integer>();

		for (int i = 0; i < list.size(); i++) {
			Reci recitemp = list.get(i);
			liststationId.add(recitemp.getStationId());
			if (recitemp.getNeedNum() != null) {
				listNum.add(recitemp.getNeedNum());
			} else {
				listNum.add(0);
			}
		}

		model.addAttribute("listNum", listNum);
		model.addAttribute("liststationId", liststationId);
		return "total/totalyaobao";
	}

}
