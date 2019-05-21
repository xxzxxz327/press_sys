package com.xxzxxz.press.web;
import com.xxzxxz.press.model.transcount;
import com.xxzxxz.press.param.TranscountParam;
import com.xxzxxz.press.repository.TranscountRepository;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.annotation.Resource;
import javax.jws.WebParam;
import javax.validation.Valid;
import java.io.*;
import java.util.List;
@Controller
public class TranscountController {
    @Autowired
    private TranscountRepository transcoutRepository;

    @RequestMapping("/totranscount")
    public String totranscount(Model model,
                               @RequestParam(value="page",defaultValue = "0")Integer page,
                               @RequestParam(value="size",defaultValue = "6")Integer size)
    {
        Pageable pageable= PageRequest.of(page,size);
        Page<Object[]> orders=transcoutRepository.getalltranscount(pageable);
        model.addAttribute("orders",orders);
        return "Transcount/transcountSet";
    }

    @RequestMapping("/addtranscount")
    public String addtranscount(Model model,
                                @RequestParam(value="page",defaultValue = "0")Integer page,
                                @RequestParam(value="size",defaultValue = "6")Integer size){
        transcount t=new transcount();
        try {
            transcoutRepository.save(t);
        }catch(Exception e){
            System.out.println("这里出错了");
        }
        Pageable pageable= PageRequest.of(page,size);
        Page<Object[]> orders=transcoutRepository.getalltranscount(pageable);
        model.addAttribute("orders",orders);
        return "Transcount/transcountSet";
    }

    @RequestMapping("/settranscount")
    public String settranscount(@RequestParam(value="id")Integer Id,
                                @RequestParam(value="base")Integer Base,
                                @RequestParam(value="jiazhang",defaultValue = "0")Double Jiazhang,
                                @RequestParam(value="page",defaultValue = "0")Integer page,
                                @RequestParam(value="size",defaultValue = "6")Integer size,
                                Model model,RedirectAttributes redirectAttributes){
        try {
            int id = Id.intValue();
            int base = Base.intValue();
            double jiazhang = Jiazhang.doubleValue();
            transcount t = transcoutRepository.findById(id);
            t.setBase(base);
            t.setJiazhang(jiazhang);
            try {
                transcoutRepository.save(t);
            } catch (Exception e) {
                System.out.println("这里出错了");
            }
            Pageable pageable = PageRequest.of(page, size);
            Page<Object[]> orders = transcoutRepository.getalltranscount(pageable);
            model.addAttribute("orders", orders);
        }catch (Exception e){
            String errorMsg="数据出错";
            redirectAttributes.addAttribute("errorMsg",errorMsg);
            return "redirect:/totranscount";
        }
        return "Transcount/transcountSet";
    }

}
