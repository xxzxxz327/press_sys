package com.xxzxxz.press.web;

import com.xxzxxz.press.model.Merit;
import com.xxzxxz.press.param.MeritParam;
import com.xxzxxz.press.repository.MeritDicRepository;
import com.xxzxxz.press.repository.StaffRepository;
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

import javax.validation.Valid;
import java.util.List;

@Controller
public class MeritController {
    // 以下是绩效设置

    @Autowired
    private MeritDicRepository meritR;

    @RequestMapping("/dicMerit")
    public String getmerit(@RequestParam(value = "page", defaultValue = "0") Integer page,
                          @RequestParam(value = "size", defaultValue = "6") Integer size, @RequestParam(value = "Id") Integer Id,
                           ModelMap model) {

        System.out.println("传入id为:");
        System.out.println(Id);

        Sort sort = new Sort(Sort.Direction.ASC, "id");
        Pageable pageable = PageRequest.of(page, size, sort);

        model.addAttribute("selectId", Id);

        // 优先id查找
        if (Id != null) {
            int meritId = Id.intValue();
            
            if(meritId==-1){
            	 Page<Merit> list = meritR.findAll(pageable);
                 model.addAttribute("objectlist", list);
                 return "dic/merit/dicmerit";
            }
            
            Page<Merit> list = meritR.findById(meritId, pageable);
            model.addAttribute("objectlist", list);

            return "dic/merit/dicmerit";

        }

        // id不存在的话根据name查找

        System.out.println("进入全部查找");
        Page<Merit> list = meritR.findAll(pageable);
        model.addAttribute("objectlist", list);
        System.out.println("全部查找结束");

        return "dic/merit/dicmerit";
    }

    @RequestMapping("/tomeritedit")
    public String tomeritedit(@RequestParam(value = "id") Integer Id,
                             @RequestParam(value = "errormessage", required = false, defaultValue = "") String errormessage,
                             @RequestParam(value = "selectId", required = false, defaultValue = "-1") Integer selectId,
                             ModelMap model) {

        int id = Id.intValue();

        Merit merit = meritR.findById(id);

        model.addAttribute("merit", merit);

        model.addAttribute("errormessage", errormessage);
        System.out.println(errormessage);
        model.addAttribute("selectId", selectId);
        return "dic/merit/meritedit";
    }

    @RequestMapping("/meritedit")
    public String meritedit(@Valid MeritParam meritP, BindingResult result, 
    		@RequestParam(value = "selectId", required = false, defaultValue = "-1") Integer selectId,
    		ModelMap model, RedirectAttributes rA) {
    	rA.addAttribute("selectId", selectId);
        String errormessage = "";
        if (result.hasErrors()) {
            List<ObjectError> list = result.getAllErrors();
            for (ObjectError error : list) {
                errormessage = errormessage + error.getDefaultMessage() + ";";
            }
            rA.addAttribute("errormessage", errormessage);
            System.out.println(meritP.getId().intValue());
            rA.addAttribute("id", meritP.getId());
            return "redirect:/tomeritedit";
        }

        Merit merit = meritR.findById(meritP.getId().intValue());
        try {
            merit.setId(meritP.getId().intValue());
            merit.setBase(meritP.getBase());
            merit.setTichen(meritP.getTichen());
            merit.setBonus(meritP.getBonus());
        } catch (Exception e) {
            System.out.println("数据格式不正确");
            errormessage = "数据格式不正确";
            rA.addAttribute("errormessage", errormessage);
            rA.addAttribute("id", meritP.getId());
            return "redirect:/tomeritedit";
        }

        try {
            meritR.save(merit);
        } catch (Exception e) {
            System.out.println("请输入正确的数据");
            errormessage = "请输入正确的数据";
            rA.addAttribute("errormessage", errormessage);
            rA.addAttribute("id", meritP.getId());
            return "redirect:/tomeritedit";
        }
        
        rA.addAttribute("Id", selectId);
        return "redirect:/dicMerit";
    }

    @RequestMapping("/tomeritadd")
    public String tomeritadd( @RequestParam(value = "selectId", required = false, defaultValue = "-1") Integer selectId,
    		@RequestParam(value = "errormessage", required = false, defaultValue = "") String errormessage,
			ModelMap model){
    	model.addAttribute("errormessage", errormessage);
    	model.addAttribute("selectId", selectId);
        return "dic/merit/meritadd";
    }
    
    @RequestMapping("/meritadd")
    public String meritadd(@Valid MeritParam meritP, BindingResult result,
    		@RequestParam(value = "selectId", required = false, defaultValue = "-1") Integer selectId,
    		 RedirectAttributes rA) {
    	
    	rA.addAttribute("selectId", selectId);
        String errormessage = "";
        if (result.hasErrors()) {
            List<ObjectError> list = result.getAllErrors();
            for (ObjectError error : list) {
                errormessage = errormessage + error.getDefaultMessage() + ";";
            }
            rA.addAttribute("errormessage", errormessage);
            return "redirect:/tomeritadd";
        }

        Merit merit = new Merit();

        try {
            merit.setBase(meritP.getBase());
            merit.setTichen(meritP.getTichen());
            merit.setBonus(meritP.getBonus());
        } catch (Exception e) {
            System.out.println("数据格式不正确");
            rA.addAttribute("errormessage", "数据格式不正确");
            return "redirect:/tomeritadd";
        }

        try {
            meritR.save(merit);
        } catch (Exception e) {
            System.out.println("请输入正确的数据");
            rA.addAttribute("errormessage", "请输入正确的数据");
            return "redirect:/tomeritadd";
        }

        rA.addAttribute("Id", selectId);
        return "redirect:/dicMerit";
    }

    @RequestMapping("/tomeritdelete")
    public String tomeritdelete(@RequestParam(value = "id") Integer Id,
    		@RequestParam(value = "selectId", required = false, defaultValue = "-1") Integer selectId,
    		RedirectAttributes rA) {

        int id = Id.intValue();

        Merit merit = meritR.findById(id);

        try{
            meritR.delete(merit);
        }catch(Exception e){

        }
        rA.addAttribute("Id", selectId);
        return "redirect:/dicMerit";
    }

    @RequestMapping("/uploadmerit")
    public String uploadmerit(Model model, @RequestParam("file") MultipartFile file,
                             RedirectAttributes redirectAttributes) throws Exception {
        XSSFWorkbook book = new XSSFWorkbook(file.getInputStream());
        XSSFSheet sheet = book.getSheetAt(0);
        for (int i = 1; i < sheet.getLastRowNum() + 1; i++) {
            Merit merit = new Merit();
            XSSFRow row = sheet.getRow(i);
            try {
                //merit.setMeritName(row.getCell(0).getStringCellValue());
                merit.setBase((int)row.getCell(0).getNumericCellValue());
                merit.setTichen(row.getCell(0).getNumericCellValue());
                merit.setBonus((int)row.getCell(0).getNumericCellValue());
            } catch (Exception e) {
                System.out.println("数据转换错误");
            }

            try {
                meritR.save(merit);
            } catch (Exception e) {
                System.out.println("存储失败");
            }
        }

        return "redirect:/todicmerit";
    }

    @RequestMapping("/todicmerit")
    public String todicMerit(@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "size", defaultValue = "6") Integer size,ModelMap model){
    	
    	  Sort sort = new Sort(Sort.Direction.ASC, "id");
          Pageable pageable = PageRequest.of(page, size, sort);
    	
    	 Page<Merit> list = meritR.findAll(pageable);
         model.addAttribute("objectlist", list);
        return "dic/merit/dicmerit";
    }

 
}
