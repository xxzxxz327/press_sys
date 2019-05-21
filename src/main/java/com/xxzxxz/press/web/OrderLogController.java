package com.xxzxxz.press.web;

import com.xxzxxz.press.model.OrderLog;
import com.xxzxxz.press.repository.OrderLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class OrderLogController {
	@Autowired
	private OrderLogRepository orderLogRepository;

	@RequestMapping("/orderLogSearch")
	public String ordersAlter(){
		return "orderLog/orderLogSearch";
	}

	@RequestMapping("/orderLogSearchShow")
	public String orderLogSearchShow(@RequestParam(value = "principalId")Integer principalId,
							  @RequestParam(value = "status")Integer status,
							  Model model, @RequestParam(value="page",defaultValue = "0")Integer page,
							  @RequestParam(value = "size",defaultValue = "6")Integer size
							  ){
		Pageable pageable= PageRequest.of(page,size);
		Page<Object[]> orderLogs=null;
		if(principalId==null){
			if(status.intValue()==7){
				orderLogs=orderLogRepository.findList(pageable);
			}else{
				orderLogs=orderLogRepository.findListByStatus(status,pageable);
			}
		}else{
			if(status.intValue()==7){
				orderLogs=orderLogRepository.findListByPrincipalId(principalId,pageable);
			}else{
				orderLogs=orderLogRepository.findListByPrincipalIdAndStatus(principalId,status,pageable);
			}
		}
		model.addAttribute("orderLogs",orderLogs);
		return "orderLog/orderLogSearchShow";
	}
}
