package com.xxzxxz.press.web;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.validation.Valid;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.xxzxxz.press.repository.ReasonRepository;
import com.xxzxxz.press.repository.AttributeRepository;
import com.xxzxxz.press.repository.DiscountRepository;
import com.xxzxxz.press.repository.DutyRepository;
import com.xxzxxz.press.repository.OfficeRepository;
import com.xxzxxz.press.repository.PressRepository;
import com.xxzxxz.press.repository.StaffRepository;
import com.xxzxxz.press.repository.VoucherRepository;
import com.xxzxxz.press.repository.ZenkaRepository;

import com.xxzxxz.press.model.ChangeReason;
import com.xxzxxz.press.model.Zenka;
import com.xxzxxz.press.model.Voucher;
import com.xxzxxz.press.model.PressInfo;
import com.xxzxxz.press.model.OfficeInfo;
import com.xxzxxz.press.model.Attribute;
import com.xxzxxz.press.model.Discount;
import com.xxzxxz.press.model.PressVipDuration;
import com.xxzxxz.press.model.Staff;
import com.xxzxxz.press.model.Duty;
import com.xxzxxz.press.param.DiscountParam;
import com.xxzxxz.press.param.AttributeParam;
import com.xxzxxz.press.param.ReasonParam;
import com.xxzxxz.press.param.ZenkaParam;
import com.xxzxxz.press.param.VoucherParam;

@Controller
public class CenterController {
    @Autowired
    private ReasonRepository rR;

    @Autowired
    private OfficeRepository oR;

    @Autowired
    private AttributeRepository aR;

    @Autowired
    private ZenkaRepository zR;

    @Autowired
    private VoucherRepository vR;

    @Autowired
    private PressRepository pR;

    @Autowired
    private DiscountRepository disR;

    @Autowired
    private StaffRepository sR;

    @Autowired
    private DutyRepository duR;

    @Autowired
    private AttributeRepository attributeRepository;
    // 以下是Zenka处理

    @RequestMapping("/todiczenka")
    public String todicZenka(@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "size", defaultValue = "6") Integer size,ModelMap model) {
    	Sort sort = new Sort(Sort.Direction.ASC, "id");
		Pageable pageable = PageRequest.of(page, size, sort);
		Page<Zenka> list = zR.findAll(pageable);
		model.addAttribute("objectlist", list);
        return "dic/zenka/diczenka";
    }

    @RequestMapping("/tozenkaadd")
    public String tozenkaadd() {
        return "dic/zenka/zenkaadd";
    }

    @RequestMapping("/dicZenka")
    public String getzenka(@RequestParam(value = "page", defaultValue = "0") Integer page,
                           @RequestParam(value = "size", defaultValue = "6") Integer size, @RequestParam(value = "id") Integer Id,
                           @RequestParam(value = "duration") Integer duration, @RequestParam(value = "flag")Integer flag, ModelMap model) {

        System.out.println("传入id为:");
        System.out.println(Id);
        System.out.println("传入duration为:");
        System.out.println(duration);

        Sort sort = new Sort(Sort.Direction.ASC, "id");
        Pageable pageable = PageRequest.of(page, size, sort);

        model.addAttribute("Id", Id);
        model.addAttribute("duration", duration);

        int f=flag.intValue();
        // 优先id查找
        if (Id != null) {
            int id = Id.intValue();
            Page<Zenka> list = zR.findById(id, pageable);
            model.addAttribute("objectlist", list);

            return "dic/zenka/diczenka";

        }else if (duration != null) {
            int dura = duration.intValue();
            Page<Zenka> list = zR.findByDurationAndFlag(dura,flag, pageable);
            model.addAttribute("objectlist", list);
            return "dic/zenka/diczenka";
        }else if(duration ==null && Id==null){
            Page<Zenka> list=zR.findListByFlag(f,pageable);
            model.addAttribute("objectlist", list);
            return "dic/zenka/diczenka";
        }

        System.out.println("进入全部查找");
        Page<Zenka> list = zR.findAll(pageable);
        model.addAttribute("objectlist", list);
        System.out.println("全部查找结束");

        return "dic/zenka/diczenka";
    }

    @RequestMapping("/tozenkaedit")
    public String tozenkaedit(@RequestParam(value = "id") Integer Id,
                              @RequestParam(value = "errormessage", required = false, defaultValue = "") String errormessage,
                              ModelMap model) {

        int id = Id.intValue();

        Zenka zenka = zR.findById(id);

        model.addAttribute("zenka", zenka);

        model.addAttribute(errormessage, errormessage);
        System.out.println(errormessage);

        return "dic/zenka/zenkaedit";
    }

    @RequestMapping("/zenkaedit")
    public String zenkaedit(@Valid ZenkaParam zenkaP, BindingResult result, ModelMap model, RedirectAttributes rA) {
        String errormessage = "";
        if (result.hasErrors()) {
            errormessage = "数据不合法";
            rA.addAttribute("errormessage", errormessage);
            System.out.println(zenkaP.getId().intValue());
            rA.addAttribute("id", zenkaP.getId());
            return "redirect:/tozenkaedit";
        }

        Zenka zenka = new Zenka();
        try {
            zenka.setId(zenkaP.getId().intValue());
            zenka.setDuration(zenkaP.getDuration());
        } catch (Exception e) {
            System.out.println("数据格式不正确");
            errormessage = "数据格式不正确";
            rA.addAttribute("errormessage", errormessage);
            rA.addAttribute("id", zenkaP.getId());
            return "redirect:/tozenkaedit";
        }

        try {
            zR.save(zenka);
        } catch (Exception e) {
            System.out.println("请输入正确的数据，变更原因类型不能重复");
            errormessage = "请输入正确的数据，变更原因类型不能重复";
            rA.addAttribute("errormessage", errormessage);
            rA.addAttribute("id", zenkaP.getId());
            return "redirect:/tozenkaedit";
        }

        return "redirect:/todiczenka";
    }

    @RequestMapping("/zenkaadd")
    public String zenkaadd(@RequestParam(value = "duration", defaultValue = "0") Integer Duration,
                           @RequestParam(value = "num", defaultValue = "0") Integer Number, ModelMap model) {

        int duration = Duration.intValue();
        int num = Number.intValue();

        for (int i = 0; i < num; i++) {
            Zenka zenka = new Zenka();
            try {
                zenka.setDuration(duration);
                zenka.setFlag(0);
            } catch (Exception e) {
                System.out.println("数据格式不正确");
                model.addAttribute("errormessage", "数据格式不正确");
                return "dic/zenka/zenkaadd";
            }

            try {
                zR.save(zenka);
            } catch (Exception e) {
                System.out.println("请输入正确的数据，变更原因类型不能重复");
                model.addAttribute("errormessage", "请输入正确的数据，变更原因类型不能重复");
                return "dic/zenka/zenkaadd";
            }
            System.out.println("存储成功" + (i + 1) + "次");
        }

        return "redirect:/todiczenka";
    }

    @RequestMapping("/tozenkadelete")
    public String tozenkadelete(@RequestParam(value = "id") Integer Id, ModelMap model) {

        int id = Id.intValue();

        Zenka zenka = zR.findById(id);

        zR.delete(zenka);

        return "redirect:/todiczenka";
    }

    /////////////////////////////////////////////////////
    // 以下是Voucher处理

    @RequestMapping("/todicvoucher")
    public String todicVoucher(@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "size", defaultValue = "6") Integer size,ModelMap model) {
    	Sort sort = new Sort(Sort.Direction.ASC, "id");
		Pageable pageable = PageRequest.of(page, size, sort);
		Page<Voucher> list = vR.findAll(pageable);
		model.addAttribute("objectlist", list);
        return "dic/voucher/dicvoucher";
    }

    @RequestMapping("/tovoucheradd")
    public String tovoucheradd() {
        return "dic/voucher/voucheradd";
    }

    @RequestMapping("/dicVoucher")
    public String getvoucher(@RequestParam(value = "page", defaultValue = "0") Integer page,
                             @RequestParam(value = "size", defaultValue = "6") Integer size, @RequestParam(value = "id") Integer Id,
                             @RequestParam(value = "sum") Integer sum,@RequestParam(value = "flag")Integer flag, ModelMap model) {

        System.out.println("传入id为:");
        System.out.println(Id);
        System.out.println("传入sum为:");
        System.out.println(sum);

        Sort sort = new Sort(Sort.Direction.ASC, "id");
        Pageable pageable = PageRequest.of(page, size, sort);

        model.addAttribute("Id", Id);
        model.addAttribute("sum", sum);

        int f=flag.intValue();
        // 优先id查找
        if (Id != null) {
            int id = Id.intValue();
            Page<Voucher> list = vR.findById(id, pageable);
            model.addAttribute("objectlist", list);
            return "dic/voucher/dicvoucher";

        }else if (sum != null) {
            int limi = sum.intValue();
            Page<Voucher> list = vR.findBySumAndFlag(limi,f, pageable);
            model.addAttribute("objectlist", list);
            return "dic/voucher/dicvoucher";
        }else if(sum==null&&Id==null){
            Page<Voucher> list=vR.findListByFlag(f,pageable);
            model.addAttribute("objectlist", list);
            return "dic/voucher/dicvoucher";
        }


        System.out.println("进入全部查找");
        Page<Voucher> list = vR.findAll(pageable);
        model.addAttribute("objectlist", list);
        System.out.println("全部查找结束");

        return "dic/voucher/dicvoucher";
    }

    @RequestMapping("/tovoucheredit")
    public String tovoucheredit(@RequestParam(value = "id") Integer Id,
                                @RequestParam(value = "errormessage", required = false, defaultValue = "") String errormessage,
                                ModelMap model) {

        int id = Id.intValue();

        Voucher voucher = vR.findById(id);

        model.addAttribute("voucher", voucher);

        model.addAttribute(errormessage, errormessage);
        System.out.println(errormessage);

        return "dic/voucher/voucheredit";
    }

    @RequestMapping("/voucheredit")
    public String voucheredit(@Valid VoucherParam voucherP, BindingResult result, ModelMap model,
                              RedirectAttributes rA) {
        String errormessage = "";
        if (result.hasErrors()) {
            errormessage = "数据不合法";
            rA.addAttribute("errormessage", errormessage);
            System.out.println(voucherP.getId().intValue());
            rA.addAttribute("id", voucherP.getId());
            return "redirect:/tovoucheredit";
        }

        Voucher voucher = new Voucher();
        try {
            voucher.setId(voucherP.getId().intValue());
            voucher.setSum(voucherP.getSum());
        } catch (Exception e) {
            System.out.println("数据格式不正确");
            errormessage = "数据格式不正确";
            rA.addAttribute("errormessage", errormessage);
            rA.addAttribute("id", voucherP.getId());
            return "redirect:/tovoucheredit";
        }

        try {
            vR.save(voucher);
        } catch (Exception e) {
            System.out.println("请输入正确的数据，变更原因类型不能重复");
            errormessage = "请输入正确的数据，变更原因类型不能重复";
            rA.addAttribute("errormessage", errormessage);
            rA.addAttribute("id", voucherP.getId());
            return "redirect:/tovoucheredit";
        }

        return "redirect:/todicvoucher";
    }

    @RequestMapping("/voucheradd")
    public String voucheradd(@RequestParam(value = "sum", defaultValue = "0") Integer Sum,
                             @RequestParam(value = "num", defaultValue = "0") Integer Number, ModelMap model) {

        int sum = Sum.intValue();
        int num = Number.intValue();

        for (int i = 0; i < num; i++) {
            Voucher voucher = new Voucher();
            try {
                voucher.setSum(sum);
            } catch (Exception e) {
                System.out.println("数据格式不正确11111");
                model.addAttribute("errormessage", "数据格式不正确");
                return "dic/voucher/voucheradd";
            }

            try {
                vR.save(voucher);
            } catch (Exception e) {
                System.out.println("请输入正确的数据");
                model.addAttribute("errormessage", "请输入正确的数据");
                return "dic/voucher/voucheradd";
            }
            System.out.println("存储成功" + (i + 1) + "次");
        }

        return "redirect:/todicvoucher";
    }

    @RequestMapping("/tovoucherdelete")
    public String tovoucherdelete(@RequestParam(value = "id") Integer Id, ModelMap model) {

        int id = Id.intValue();

        Voucher voucher = vR.findById(id);

        vR.delete(voucher);

        return "redirect:/todicvoucher";
    }

    // 以下是Presssetting处理

    @RequestMapping("/todicpresssetting")
    public String todicpresssetting(
    		@RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "6") Integer size,
            ModelMap model) {
    	List<String> listPressName=CenterTool.getPressNameList();
		model.addAttribute("listPressName", listPressName);
		Sort sort = new Sort(Sort.Direction.ASC, "id");
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Object[]> list = pR.findPressAll(pageable);
        model.addAttribute("objectlist", list);
        return "dic/presssetting/dicpresssetting";
    }

    @RequestMapping("/topresssettingadd")
    public String topresssettingadd(Model model) {
        List<OfficeInfo> offices = oR.findList();
        model.addAttribute("offices", offices);
        return "dic/presssetting/presssettingadd";
    }

    @RequestMapping("/dicPresssetting")
    public String getpresssetting(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                  @RequestParam(value = "size", defaultValue = "6") Integer size, @RequestParam(value = "pressId") Integer Id,
                                  @RequestParam(value = "pressName") String pressName, ModelMap model) {

        System.out.println("传入id为:");
        System.out.println(Id);
        System.out.println("传入pressName为:");
        System.out.println(pressName);

        List<String> listPressName=CenterTool.getPressNameList();
		model.addAttribute("listPressName", listPressName);
        Sort sort = new Sort(Sort.Direction.ASC, "id");
        Pageable pageable = PageRequest.of(page, size, sort);

        model.addAttribute("Id", Id);
        model.addAttribute("pressName", pressName);

        // 优先id查找
        if (Id != null) {
            System.out.println("进入ID查找");
            int id = Id.intValue();
            Page<Object> list = pR.findByIdO(id, pageable);
            model.addAttribute("objectlist", list);

            return "dic/presssetting/dicpresssetting";

        }

        // id不存在的话根据pressName查找
        if (pressName != "") {
            System.out.println("进入pressName查找");
            Page<Object[]> list = pR.findByPressNameO(pressName, pageable);
            model.addAttribute("objectlist", list);
            return "dic/presssetting/dicpresssetting";
        }

        System.out.println("进入全部查找");
        Page<Object[]> list = pR.findPressAll(pageable);
        model.addAttribute("objectlist", list);
        System.out.println("全部查找结束");

        return "dic/presssetting/dicpresssetting";
    }

    @RequestMapping("/topresssettingedit")
    public String topresssettingedit(@RequestParam(value = "id") Integer Id,
                                     @RequestParam(value = "errormessage", required = false, defaultValue = "") String errormessage,
                                     ModelMap model) {

        int id = Id.intValue();

        PressInfo press = pR.findById(id);

        List<OfficeInfo> offices = oR.findList();
        model.addAttribute("offices", offices);

        model.addAttribute("press", press);

        model.addAttribute(errormessage, errormessage);
        System.out.println(errormessage);

        return "dic/presssetting/presssettingedit";
    }

    @RequestMapping("/presssettingedit")
    public String presssettingedit(@RequestParam(value = "id") Integer Id,
                                   @RequestParam(value = "pressName") String pressName, @RequestParam(value = "officeId") Integer officeId,
                                   Model model, RedirectAttributes rA) {

        String errormessage = "";

        PressInfo press = new PressInfo();
        try {
            int oid = officeId.intValue();
            press.setId(Id.intValue());
            press.setPressName(pressName);
            press.setOfficeId(oid);
        } catch (Exception e) {
            System.out.println("数据格式不正确");
            errormessage = "数据格式不正确";
            rA.addAttribute("errormessage", errormessage);
            rA.addAttribute("id", Id.intValue());

            return "redirect:/topresssettingedit";
        }

        try {
            pR.save(press);
        } catch (Exception e) {
            System.out.println("请输入正确的数据，变更原因类型不能重复");
            /*
             * errormessage = "请输入正确的数据，变更原因类型不能重复"; rA.addAttribute("errormessage",
             * errormessage); rA.addAttribute("id", zenkaP.getId());
             */
            return "redirect:/topresssettingedit";
        }

        return "redirect:/todicpresssetting";
    }

    @RequestMapping("/presssettingadd")
    public String presssettingadd(@RequestParam(value = "pressName") String pressName,
                                  @RequestParam(value = "officeId") Integer officeId, ModelMap model, RedirectAttributes rA) {

        PressInfo press = new PressInfo();
        String errormessage = "";
        if (pressName == "") {
            System.out.println("名称不能为空");
            errormessage = "名称不能为空";
            rA.addAttribute("errormessage", errormessage);
            return "redirect:/topresssettingadd";
        }

        try {
            press.setPressName(pressName);
        } catch (Exception e) {
            System.out.println("数据格式不正确");
            errormessage = "数据格式不正确";
            rA.addAttribute("errormessage", errormessage);
            return "redirect:/topresssettingadd";
        }

        try {
            press.setOfficeId(officeId);
        } catch (Exception e) {
            System.out.println("请选择出版报社");
            errormessage = "请选择出版报社";
            rA.addAttribute("errormessage", errormessage);
            rA.addAttribute("pressName", pressName);
            return "redirect:/topresssettingadd";
        }

        try {
            pR.save(press);
            Attribute attr=new Attribute();
            attr.setPressId(press.getId());
            attr.setChange(0);
            attr.setUnsub(0);
            attributeRepository.save(attr);
        } catch (Exception e) {
            System.out.println("请输入正确的数据，变更原因类型不能重复");
            model.addAttribute("errormessage", "保存失败");
            return "redirect:/topresssettingadd";
        }
        System.out.println("存储成功");

        return "redirect:/todicpresssetting";
    }

    @RequestMapping("/topresssettingdelete")
    public String topresssettingdelete(@RequestParam(value = "id") Integer Id, ModelMap model) {

        int id = Id.intValue();

        PressInfo press = pR.findById(id);

        pR.delete(press);

        return "redirect:/todicpresssetting";

    }

    // @RequestMapping("/uploadpresssetting")
    // public String uploadpresssetting(Model model, @RequestParam("file")
    // MultipartFile file,
    // RedirectAttributes redirectAttributes) throws Exception {
    // XSSFWorkbook book = new XSSFWorkbook(file.getInputStream());
    // XSSFSheet sheet = book.getSheetAt(0);
    // for (int i = 1; i < sheet.getLastRowNum() + 1; i++) {
    // Press press = new Press();
    // XSSFRow row = sheet.getRow(i);
    // try {
    // press.setReasonName(row.getCell(0).getStringCellValue());
    // } catch (Exception e) {
    // System.out.println("数据转换错误");
    // }
    //
    // try {
    // rR.save(reason);
    // } catch (Exception e) {
    // System.out.println("存储失败");
    // }
    // }
    //
    // return "redirect:/todicreason";
    // }

    // 以下是Attribute处理

    @RequestMapping("/todicattribute")
    public String todicattribute(
    		@RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "6") Integer size,
    		ModelMap model) {
    	List<String> listPressName=CenterTool.getPressNameList();
		model.addAttribute("listPressName", listPressName);
        Sort sort = new Sort(Sort.Direction.ASC, "press_id");
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Object[]> list = aR.find_all_press_attribute(pageable);
        model.addAttribute("objectlist", list);
        return "dic/attribute/dicattribute";
    }


    @RequestMapping("/dicattribute")
    public String getattribute(@RequestParam(value = "page", defaultValue = "0") Integer page,
                               @RequestParam(value = "size", defaultValue = "6") Integer size,
                               @RequestParam(value = "pressId") Integer Id,
                               ModelMap model) {

        System.out.println("传入id为:");
        System.out.println(Id);
        List<PressInfo> presses = pR.findList();
        model.addAttribute("presses", presses);
        List<String> listPressName=CenterTool.getPressNameList();
		model.addAttribute("listPressName", listPressName);

        Sort sort = new Sort(Sort.Direction.ASC, "press_id");
        Pageable pageable = PageRequest.of(page, size, sort);

        // 优先id查找
        if (Id != null) {
            int id = Id.intValue();
            Page<Object[]> list = aR.find_press_attribute_by_pId(id, pageable);
            model.addAttribute("objectlist", list);

            return "dic/attribute/dicattribute";

        }


        System.out.println("进入全部查找");
        Page<Object[]> list = aR.find_all_press_attribute(pageable);
        model.addAttribute("objectlist", list);
        System.out.println("全部查找结束");

        return "dic/attribute/dicattribute";
    }

    @RequestMapping("/toattributeedit")
    public String toattributeedit(@RequestParam(value = "pressId") Integer Id,
                                  @RequestParam(value = "errormessage", defaultValue = "") String errormessage,
                                  ModelMap model) {

        int id = Id.intValue();

        Attribute attribute = aR.findByPressId(id);

        model.addAttribute("attribute", attribute);

        model.addAttribute("errormessage", errormessage);
        System.out.println(errormessage);

        return "dic/attribute/attributeedit";
    }

    @RequestMapping("/attributeedit")
    public String attributeedit(@Valid AttributeParam attributeP, BindingResult result, ModelMap model,
                                RedirectAttributes rA) {
        String errormessage = "";
        if (result.hasErrors()) {
            errormessage = "数据不合法";
            rA.addAttribute("errormessage", errormessage);
            System.out.println(attributeP.getPressId().intValue());
            rA.addAttribute("id", attributeP.getPressId());
            return "redirect:/toattributeedit";
        }
        System.out.println(attributeP.getPressId());
        System.out.println(attributeP.getUnsub());
        System.out.println(attributeP.getChangeable());
        Attribute attribute = aR.findByPressId(attributeP.getPressId());

        try {
            attribute.setUnsub(attributeP.getUnsub().intValue());
            attribute.setChange(attributeP.getChangeable().intValue());
        } catch (Exception e) {
            System.out.println("数据格式不正确");
            errormessage = "数据格式不正确";
            rA.addAttribute("errormessage", errormessage);
            rA.addAttribute("pressId", attributeP.getPressId());
            return "redirect:/toattributeedit";
        }

        try {
            aR.save(attribute);
        } catch (Exception e) {
            System.out.println("请输入正确的数据");
            errormessage = "请输入正确的数据";
            rA.addAttribute("errormessage", errormessage);
            rA.addAttribute("pressId", attributeP.getPressId());
            return "redirect:/toattributeedit";
        }

        return "redirect:/todicattribute";
    }

    @RequestMapping("/toattributedelete")
    public String toattributedelete(@RequestParam(value = "pressId") Integer Id, ModelMap model) {

        int id = Id.intValue();

        Attribute attribute = aR.findByPressId(id);

        attribute.setChange(0);
        attribute.setUnsub(0);
        aR.save(attribute);

        return "redirect:/todicattribute";
    }
    /*
     * @RequestMapping("/uploadreason") public String uploadreason(Model
     * model, @RequestParam("file") MultipartFile file, RedirectAttributes
     * redirectAttributes) throws Exception { XSSFWorkbook book = new
     * XSSFWorkbook(file.getInputStream()); XSSFSheet sheet = book.getSheetAt(0);
     * for (int i = 1; i < sheet.getLastRowNum() + 1; i++) { Reason reason = new
     * Reason(); XSSFRow row = sheet.getRow(i); try {
     * reason.setReasonName(row.getCell(0).getStringCellValue()); } catch (Exception
     * e) { System.out.println("数据转换错误"); }
     *
     * try { rR.save(reason); } catch (Exception e) { System.out.println("存储失败"); }
     * }
     *
     * return "redirect:/todicreason"; }
     */

    // 以下是Pressfrequence处理

    @RequestMapping("/todicpressfrequency")
    public String todicpressfrequency(
    		@RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "6") Integer size,
            ModelMap model) {
    	List<String> listPressName=CenterTool.getPressNameList();
		model.addAttribute("listPressName", listPressName);
		Sort sort = new Sort(Sort.Direction.ASC, "id");
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<PressInfo> list = pR.findAll(pageable);
        model.addAttribute("objectlist", list);
        return "dic/pressfrequency/dicpressfrequency";
    }

    @RequestMapping("/dicpressfrequency")
    public String getpressfrequency(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                    @RequestParam(value = "size", defaultValue = "6") Integer size,
                                    @RequestParam(value = "frequencyId") Integer frequencyId,
                                    @RequestParam(value = "pressName") String pressName, ModelMap model) {

        System.out.println("传入frequencyid为:");
        System.out.println(frequencyId);
        System.out.println("传入pressName为:");
        System.out.println(pressName);

        Sort sort = new Sort(Sort.Direction.ASC, "id");
        Pageable pageable = PageRequest.of(page, size, sort);

        List<String> listPressName=CenterTool.getPressNameList();
		model.addAttribute("listPressName", listPressName);
		
        model.addAttribute("pressName", pressName);
        model.addAttribute("id", frequencyId);

        // pressName查找
        if (pressName != "") {
            System.out.println("进入pressName查找");
            Page<PressInfo> list = pR.findByPressName1(pressName, pageable);
            model.addAttribute("objectlist", list);
            return "dic/pressfrequency/dicpressfrequency";
        }


        // 优先Id查找
        if (frequencyId != null) {
            System.out.println("进入frequencyID查找");
            int id = frequencyId.intValue();
            Page<PressInfo> list = pR.findByFrequencyId(id, pageable);
            model.addAttribute("objectlist", list);

            return "dic/pressfrequency/dicpressfrequency";

        }



        System.out.println("进入全部查找");
        Page<PressInfo> list = pR.findAll(pageable);
        model.addAttribute("objectlist", list);
        System.out.println("全部查找结束");

        return "dic/pressfrequency/dicpressfrequency";
    }

    @RequestMapping("/topressfrequencyedit")
    public String topressfrequencyedit(@RequestParam(value = "id") Integer Id,
                                       @RequestParam(value = "errormessage", required = false, defaultValue = "") String errormessage,
                                       ModelMap model) {

        int id = Id.intValue();

        PressInfo press = pR.findById(id);

        model.addAttribute("press", press);

        model.addAttribute(errormessage, errormessage);
        System.out.println(errormessage);

        return "dic/pressfrequency/pressfrequencyedit";
    }

    @RequestMapping("/pressfrequencyedit")
    public String pressfrequencyedit(@RequestParam(value = "id") Integer Id,
                                     @RequestParam(value = "frequencyId") Integer frequencyId,
                                     Model model, RedirectAttributes rA) {

        String errormessage = "";

        PressInfo press = pR.findById(Id.intValue());

        if(frequencyId == null) {

            errormessage = "请选择刊期";
            System.out.println(errormessage);
            rA.addAttribute("errormessage", errormessage);
            rA.addAttribute("id", Id.intValue());
            return "redirect:/topressfrequencyedit";

        }

        try {
            press.setFrequencyId(frequencyId);

        } catch (Exception e) {
            System.out.println("数据格式不正确");

            errormessage = "数据格式不正确";
            rA.addAttribute("errormessage", errormessage);
            rA.addAttribute("id", Id.intValue());
            return "redirect:/topressfrequencyedit";
        }

        try {
            pR.save(press);
        } catch (Exception e) {
            System.out.println("保存失败");

            errormessage = "保存失败";
            rA.addAttribute("errormessage", errormessage);
            rA.addAttribute("id", Id.intValue());

            return "redirect:/topressfrequencyedit";
        }
        return "redirect:/todicpressfrequency";
    }

    @RequestMapping("/topressfrequencydelete")
    public String topressfrequencydelete(@RequestParam(value = "id") Integer Id, ModelMap model) {

        int id = Id.intValue();

        PressInfo press = pR.findById(id);

        press.setFrequencyId(null);

        pR.save(press);

        return "redirect:/todicpressfrequency";

    }
    // 以下是Pressprice处理

    @RequestMapping("/todicpressprice")
    public String todicpressprice(
    		@RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "6") Integer size,
            ModelMap model ) {
    	
    	List<String> listPressName=CenterTool.getPressNameList();
    	for(int i=0;i<listPressName.size();i++)
    		System.out.println(listPressName.get(i));
		model.addAttribute("listPressName", listPressName);
    	
    	Sort sort = new Sort(Sort.Direction.ASC, "id");
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<PressInfo> list = pR.findAll(pageable);
        model.addAttribute("objectlist", list);
        return "dic/pressprice/dicpressprice";
    }

    @RequestMapping("/dicpressprice")
    public String getpressprice(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                @RequestParam(value = "size", defaultValue = "6") Integer size,
                                @RequestParam(value = "pressId") Integer Id,
                                @RequestParam(value = "pressName") String pressName, ModelMap model) {

        System.out.println("传入id为:");
        System.out.println(Id);
        System.out.println("传入pressName为:");
        System.out.println(pressName);
        
        List<String> listPressName=CenterTool.getPressNameList();
		model.addAttribute("listPressName", listPressName);

        Sort sort = new Sort(Sort.Direction.ASC, "id");
        Pageable pageable = PageRequest.of(page, size, sort);

        model.addAttribute("pressName", pressName);
        model.addAttribute("id", Id);

        // 优先Id查找
        if (Id != null) {
            System.out.println("进入ID查找");
            int id = Id.intValue();
            Page<PressInfo> list = pR.findById(id, pageable);
            model.addAttribute("objectlist", list);

            return "dic/pressprice/dicpressprice";

        }

        // pressName查找
        if (pressName != "") {
            System.out.println("进入pressName查找");
            Page<PressInfo> list = pR.findByPressName1(pressName, pageable);
            model.addAttribute("objectlist", list);
            return "dic/pressprice/dicpressprice";
        }



        System.out.println("进入全部查找");
        Page<PressInfo> list = pR.findAll(pageable);
        model.addAttribute("objectlist", list);
        System.out.println("全部查找结束");

        return "dic/pressprice/dicpressprice";
    }

    @RequestMapping("/topresspriceedit")
    public String topresspriceedit(@RequestParam(value = "id") Integer Id,
                                   @RequestParam(value = "errormessage", required = false, defaultValue = "") String errormessage,
                                   ModelMap model) {

        int id = Id.intValue();

        PressInfo press = pR.findById(id);

        model.addAttribute("press", press);

        model.addAttribute(errormessage, errormessage);
        System.out.println(errormessage);

        return "dic/pressprice/presspriceedit";
    }

    @RequestMapping("/presspriceedit")
    public String presspriceedit(@RequestParam(value = "id") Integer Id,
                                 @RequestParam(value = "dayPrice") Double dayPrice,
                                 @RequestParam(value = "weekPrice") Double weekPrice,
                                 @RequestParam(value = "monthPrice") Double monthPrice,
                                 @RequestParam(value = "sessonPrice") Double sessonPrice,
                                 @RequestParam(value = "halfYearPrice") Double halfYearPrice,
                                 @RequestParam(value = "yearPrice") Double yearPrice,
                                 Model model, RedirectAttributes rA) {

        String errormessage = "";

        PressInfo press = pR.findById(Id.intValue());

        try {
            press.setDayPrice(dayPrice.doubleValue());
            press.setMonthPrice(monthPrice.doubleValue());
            press.setSessionPrice(sessonPrice.doubleValue());
            press.setHalfYearPrice(halfYearPrice.doubleValue());
            press.setYearPrice(yearPrice.doubleValue());
            press.setWeekPrice(weekPrice.doubleValue());

        } catch (Exception e) {
            System.out.println("数据格式不正确");

            errormessage = "数据格式不正确";
            rA.addAttribute("errormessage", errormessage);
            rA.addAttribute("id", Id.intValue());
            return "redirect:/topresspriceedit";
        }

        try {
            pR.save(press);
        } catch (Exception e) {
            System.out.println("保存失败");

            errormessage = "保存失败";
            rA.addAttribute("errormessage", errormessage);
            rA.addAttribute("id", Id.intValue());

            return "redirect:/topresspriceedit";
        }

        return "redirect:/todicpressprice";
    }

    @RequestMapping("/topresspricedelete")
    public String topresspricedelete(@RequestParam(value = "id") Integer Id, ModelMap model) {

        int id = Id.intValue();

        PressInfo press = pR.findById(id);

        press.setDayPrice(null);
        press.setMonthPrice(null);
        press.setSessionPrice(null);
        press.setHalfYearPrice(null);
        press.setYearPrice(null);
        press.setWeekPrice(null);

        pR.save(press);

        return "redirect:/todicpressprice";

    }

    // 以下是Pressdiscount处理

    @RequestMapping("/todicpressdiscount")
    public String todicpressdiscount(
    		@RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "6") Integer size,
            ModelMap model) {
    	Sort sort = new Sort(Sort.Direction.ASC, "press_id");
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Object[]> list = disR.find_dis_pressN_all(pageable);
        model.addAttribute("objectlist", list);
        List<PressInfo> presses = pR.findList();
        model.addAttribute("presses", presses);
        return "dic/pressdiscount/dicpressdiscount";
    }


    @RequestMapping("/dicpressdiscount")
    public String getpressdiscount(ModelMap model,
                                   @RequestParam(value = "page", defaultValue = "0") Integer page,
                                   @RequestParam(value = "size", defaultValue = "6") Integer size,
                                   @RequestParam(value = "pressId") Integer pressId,
                                   @RequestParam(value = "vipTypeId") Integer vipTypeId,
                                   @RequestParam(value = "duration") Integer duration,
                                   RedirectAttributes rA) {

        System.out.println("传入pressId为:");
        System.out.println(pressId);
        System.out.println("传入vipTypeId为:");
        System.out.println(vipTypeId);
        System.out.println("传入duration为:");
        System.out.println(duration);

        Sort sort = new Sort(Sort.Direction.ASC, "press_id");
        Pageable pageable = PageRequest.of(page, size, sort);


        List<PressInfo> presses = pR.findList();

        // 优先pressid查找
        if (pressId != null) {
            System.out.println("进入pressID查找");
            int pressid = pressId.intValue();
            Page<Object[]> list = disR.findDisPNbyPID(pressid, pageable);
            model.addAttribute("objectlist", list);
            model.addAttribute("presses", presses);

            return "dic/pressdiscount/dicpressdiscount";

        }

        // pressid不存在的话根据vipTypeId查找
        if (vipTypeId != null) {
            System.out.println("进入vipType查找");
            int viptypeid = vipTypeId.intValue();
            Page<Object[]> list = disR.findDisPNbyVTID(viptypeid, pageable);
            model.addAttribute("objectlist", list);
            model.addAttribute("presses", presses);

            return "dic/pressdiscount/dicpressdiscount";
        }

        if (duration != null) {
            System.out.println("进入duration查找");
            int dura = duration.intValue();
            Page<Object[]> list = disR.findDisPNbyDURA(dura, pageable);
            model.addAttribute("objectlist", list);
            model.addAttribute("presses", presses);

            return "dic/pressdiscount/dicpressdiscount";
        }

        System.out.println("进入全部查找");
        Page<Object[]> list = disR.find_dis_pressN_all(pageable);
        model.addAttribute("objectlist", list);
        model.addAttribute("presses", presses);
        System.out.println("全部查找结束");

        return "dic/pressdiscount/dicpressdiscount";
    }

    @RequestMapping("/topressdiscountedit")
    public String topressdiscountedit(
            @RequestParam(value = "pressId") Integer pressId,
            @RequestParam(value = "vipTypeId") Integer vipTypeId,
            @RequestParam(value = "duration") Integer duration,
            @RequestParam(value = "errormessage",defaultValue = "") String errormessage,
        ModelMap model) {

        int pressid = pressId.intValue();
        int viptypeid = vipTypeId.intValue();
        int dura = duration.intValue();

        Discount discount = disR.find_one(pressid, viptypeid, dura);
        System.out.println("找到"+discount.getPressId());
        System.out.println("找到"+discount.getVipTypeId());
        System.out.println("找到"+discount.getDuration());
        System.out.println("找到"+discount.getStrategy());

        model.addAttribute("discount", discount);

        System.out.println(errormessage);

        model.addAttribute("errormessage", errormessage);

        return "dic/pressdiscount/pressdiscountedit";
    }
    @RequestMapping("/pressdiscountedit")
    public String pressdiscountedit(
            @RequestParam(value = "pressId") Integer pressId,
            @RequestParam(value = "vipTypeId") Integer vipTypeId,
            @RequestParam(value = "duration") Integer duration,
            @RequestParam(value = "strategy") Double strategy,
            Model model, RedirectAttributes rA) {

        String errormessage = "";

        Discount discount = disR.find_one(pressId, vipTypeId, duration);


        try {
            if(strategy.intValue()<1||strategy.intValue()>99) {
                errormessage = "请输入1-99范围内的数字";
                System.out.println(errormessage);
                rA.addAttribute("errormessage", errormessage);
                rA.addAttribute("pressId", pressId);
                rA.addAttribute("vipTypeId", vipTypeId);
                rA.addAttribute("duration", duration);
                return "redirect:/topressdiscountedit";
            }
        }catch(Exception e) {
            errormessage = "请输入正确的折扣格式";
            rA.addAttribute("errormessage", errormessage);
            rA.addAttribute("pressId", pressId);
            rA.addAttribute("vipTypeId", vipTypeId);
            rA.addAttribute("duration", duration);
            return "redirect:/topressdiscountedit";
        }

        try {
            double stra = strategy.doubleValue()/100;
            discount.setStrategy(stra);
        } catch (Exception e) {
            System.out.println("折扣格式不正确");
            errormessage = "折扣格式不正确";
            rA.addAttribute("errormessage", errormessage);
            rA.addAttribute("pressId", pressId);
            rA.addAttribute("vipTypeId", vipTypeId);
            rA.addAttribute("duration", duration);

            return "redirect:/topressdiscountedit";
        }

        try {
            disR.save(discount);
        } catch (Exception e) {
            System.out.println("请输入正确的数据，变更原因类型不能重复");
            errormessage = "请输入正确的数据，变更原因类型不能重复";
            rA.addAttribute("errormessage", errormessage);
            rA.addAttribute("pressId", pressId);
            rA.addAttribute("vipTypeId", vipTypeId);
            rA.addAttribute("duration", duration);

            return "redirect:/topressdiscountedit";
        }

        return "redirect:/todicpressdiscount";
    }

    @RequestMapping("/topressdiscountadd")
    public String topressdiscountadd(
            @RequestParam(value="errormessage",defaultValue="")String errormessage,
            ModelMap model) {
        List<PressInfo> presses = pR.findList();
        model.addAttribute("presses", presses);
        model.addAttribute("errormessage", errormessage);
        return "dic/pressdiscount/pressdiscountadd";
    }

    @RequestMapping("/pressdiscountadd")
    public String pressdiscountadd(
            @RequestParam(value = "pressId") Integer pressId,
            @RequestParam(value = "vipTypeId") Integer vipTypeId,
            @RequestParam(value = "duration") Integer duration,
            @RequestParam(value = "strategy") Double strategy,
            ModelMap model, RedirectAttributes rA) {

        Discount discount = new Discount();
        String errormessage = "";
        if (pressId == null) {
            System.out.println("报刊不能为空");
            errormessage = "报刊不能为空";
            rA.addAttribute("errormessage", errormessage);
            return "redirect:/topressdiscountadd";
        }

        if (vipTypeId == null) {
            System.out.println("会员类型不能为空");
            errormessage = "会员类型不能为空";
            rA.addAttribute("errormessage", errormessage);
            return "redirect:/topressdiscountadd";
        }

        if (duration == null) {
            System.out.println("订阅时长不能为空");
            errormessage = "订阅时长不能为空";
            rA.addAttribute("errormessage", errormessage);
            return "redirect:/topressdiscountadd";
        }


        try {
            double s=strategy.doubleValue();
            if(strategy>99||strategy<1){
                errormessage="折扣范围出错";
                rA.addAttribute("errormessage",errormessage);
                return "redirect:/topressdiscountadd";
            }
        } catch (Exception e) {
            System.out.println("数据格式不正确");
            errormessage = "数据格式不正确";
            rA.addAttribute("errormessage", errormessage);
            return "redirect:/topressdiscountadd";
        }

        try {
            int pid = pressId.intValue();
            int vid = vipTypeId.intValue();
            int dura = duration.intValue();
            discount.setPressId(pid);
            discount.setVipTypeId(vid);
            discount.setDuration(dura);
            discount.setStrategy(strategy/100);

        } catch (Exception e) {
            System.out.println("数据格式不正确");
            errormessage = "数据格式不正确";
            rA.addAttribute("errormessage", errormessage);
            return "redirect:/topressdiscountadd";
        }

        try {
            disR.save(discount);
        } catch (Exception e) {
            System.out.println("保存失败");
            errormessage = "保存失败";
            rA.addAttribute("errormessage", errormessage);
            return "redirect:/topressdiscountadd";
        }
        System.out.println("存储成功");

        return "redirect:/todicpressdiscount";
    }

    @RequestMapping("/topressdiscountdelete")
    public String topressdiscountdelete(
            @RequestParam(value = "pressId") Integer pressId,
            @RequestParam(value = "vipTypeId") Integer vipTypeId,
            @RequestParam(value = "duration") Integer duration, ModelMap model) {

        int pid = pressId.intValue();
        int vid = vipTypeId.intValue();
        int dura = duration.intValue();

        Discount discount = disR.find_one(pid, vid, dura);

        disR.delete(discount);

        return "redirect:/todicpressdiscount";

    }
	/*
	// @RequestMapping("/uploadpresssetting")
	// public String uploadpresssetting(Model model, @RequestParam("file")
	// MultipartFile file,
	// RedirectAttributes redirectAttributes) throws Exception {
	// XSSFWorkbook book = new XSSFWorkbook(file.getInputStream());
	// XSSFSheet sheet = book.getSheetAt(0);
	// for (int i = 1; i < sheet.getLastRowNum() + 1; i++) {
	// Press press = new Press();
	// XSSFRow row = sheet.getRow(i);
	// try {
	// press.setReasonName(row.getCell(0).getStringCellValue());
	// } catch (Exception e) {
	// System.out.println("数据转换错误");
	// }
	//
	// try {
	// rR.save(reason);
	// } catch (Exception e) {
	// System.out.println("存储失败");
	// }
	// }
	//
	// return "redirect:/todicreason";
	// }
*/



    //以下是staff处理


    @RequestMapping("/todicstaff")
    public String todicstaff(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "6") Integer size,
            ModelMap model) {
    	
    	List<String> listStaffName=CenterTool.getStaffNameList();
		model.addAttribute("listStaffName", listStaffName);
    	
        Sort sort = new Sort(Sort.Direction.ASC, "id");
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Object[]> list = sR.find_st_duN_deN_all(pageable);
        model.addAttribute("objectlist", list);
        
        return "dic/staff/dicstaff";
    }

    @RequestMapping("/dicstaff")
    public String getstaff(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "6") Integer size,
            @RequestParam(value = "id") Integer Id,
            @RequestParam(value = "staffName") String staffName,
            ModelMap model) {
    	
    	List<String> listStaffName=CenterTool.getStaffNameList();
		model.addAttribute("listStaffName", listStaffName);

        System.out.println("传入id为:");
        System.out.println(Id);
        System.out.println("传入name为:");
        System.out.println(staffName);

        Sort sort = new Sort(Sort.Direction.ASC, "id");
        Pageable pageable = PageRequest.of(page, size, sort);

        model.addAttribute("id", Id);
        model.addAttribute("staffName", staffName);

        // 优先id查找
        if (Id != null) {
            int id = Id.intValue();
            Page<Object[]> list = sR.find_st_duN_deN_byId(id, pageable);
            model.addAttribute("objectlist", list);

            return "dic/staff/dicstaff";

        }

        // id不存在的话根据name查找
        if (staffName != "") {
            Page<Object[]> list = sR.find_st_duN_deN_bystN(staffName, pageable);
            model.addAttribute("objectlist", list);
            return "dic/staff/dicstaff";
        }

        System.out.println("进入全部查找");
        Page<Object[]> list = sR.find_st_duN_deN_all(pageable);
        model.addAttribute("objectlist", list);
        System.out.println("全部查找结束");

        return "dic/staff/dicstaff";
    }

    @RequestMapping("/tostaffedit")
    public String tostaffedit(
            @RequestParam(value = "id") Integer Id,
            @RequestParam(value = "errormessage", required = false, defaultValue = "") String errormessage,
            ModelMap model) {

        int id = Id.intValue();
        Staff staff = sR.findById(id);
        model.addAttribute("staff", staff);

        List<Object[]> duties = duR.findList();
        model.addAttribute("duties", duties);

        model.addAttribute("errormessage", errormessage);
        System.out.println(errormessage);

        return "dic/staff/staffedit";
    }

    @RequestMapping("/staffedit")
    public String staffedit(
            @RequestParam(value="id") Integer Id,
            @RequestParam(value="staffName") String staffName,
            @RequestParam(value="authority") Integer authority,
            @RequestParam(value="dutyId") Integer dutyId,
            ModelMap model, RedirectAttributes rA) {
        String errormessage = "";

        try{
        int id = Id.intValue();
        Staff staff = sR.findById(id);
        int dId = dutyId.intValue();
        Duty duty = duR.findById(dId);
        int deId = duty.getDepartId().intValue();
        try {
            staff.setStaffName(staffName);
            int auth = authority.intValue();
            staff.setAuthority(auth);
            staff.setDutyId(dId);
            staff.setDepartId(deId);

        } catch (Exception e) {
            System.out.println("数据格式不正确");
            errormessage = "数据格式不正确";
            rA.addAttribute("errormessage", errormessage);
            rA.addAttribute("id", Id);
            return "redirect:/tostaffedit";
        }

        try {
            sR.save(staff);
        } catch (Exception e) {
            System.out.println("保存失败");
            errormessage = "请保存失败";
            rA.addAttribute("errormessage", errormessage);
            rA.addAttribute("id", Id);
            return "redirect:/tostaffedit";
        }}catch (Exception e){
            errormessage = "数据输入错误";
            rA.addAttribute("errormessage", errormessage);
            rA.addAttribute("id", Id);
            return "redirect:/tostaffedit";
        }

        return "redirect:/todicstaff";
    }

    @RequestMapping("/tostaffadd")
    public String tostaffadd(
            @RequestParam(value = "errormessage", defaultValue = "") String errormessage,
            ModelMap model) {
        List<Object[]> duties = duR.findList();
        model.addAttribute("duties", duties);

        System.out.println("1111"+errormessage);
        model.addAttribute("errormessage", errormessage);
        System.out.println(errormessage);
        return "dic/staff/staffadd";
    }

    @RequestMapping("/staffadd")
    public String staffadd(
            @RequestParam(value="staffName")String staffName,
            @RequestParam(value="authority")Integer authority,
            @RequestParam(value="dutyId")Integer dutyId,
            @RequestParam(value="phone")String phone,
            ModelMap model,RedirectAttributes ra) {


        Staff staff = new Staff();

        try {
            int dId = dutyId.intValue();
            Duty duty = duR.findById(dId);
            staff.setStaffName(staffName);
            int auth = authority.intValue();
            staff.setAuthority(auth);
            staff.setDutyId(dId);
            staff.setDepartId(duty.getDepartId().intValue());
            staff.setPassword("123");
            staff.setPhone(phone);
        } catch (Exception e) {
            System.out.println("数据格式不正确");
            ra.addAttribute("errormessage", "数据格式不正确");
            return "redirect:/tostaffadd";
        }

        try {
            sR.save(staff);
        } catch (Exception e) {
            System.out.println("保存失败");
            model.addAttribute("errormessage", "保存失败");
            return "redirect:/todicstaff";
        }

        return "redirect:/todicstaff";
    }

    @RequestMapping("/tostaffdelete")
    public String tostaffdelete(@RequestParam(value = "id") Integer Id, ModelMap model) {

        int id = Id.intValue();

        Staff staff = sR.findById(id);

        sR.delete(staff);

        return "redirect:/todicstaff";
    }
	/*
	@RequestMapping("/uploadreason")
	public String uploadreason(Model model, @RequestParam("file") MultipartFile file,
			RedirectAttributes redirectAttributes) throws Exception {
		XSSFWorkbook book = new XSSFWorkbook(file.getInputStream());
		XSSFSheet sheet = book.getSheetAt(0);
		for (int i = 1; i < sheet.getLastRowNum() + 1; i++) {
			Reason reason = new Reason();
			XSSFRow row = sheet.getRow(i);
			try {
				reason.setReasonName(row.getCell(0).getStringCellValue());
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

	*/



    //以下是staff处理



    @RequestMapping("/todicperformance")
    public String todicperformance(
            @RequestParam(value="errormessage", defaultValue="", required=false)String errormessage,
            ModelMap model) {
    	List<String> listStationName=CenterTool.getStationNameList();
		model.addAttribute("listStationName", listStationName);

        model.addAttribute("errormessage", errormessage);
        return "dic/performance/dicperformance";
    }

    @RequestMapping("/dicperformance")
    public String getperformance(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "6") Integer size,
            @RequestParam(value = "dutyType") Integer dutyType,
            @RequestParam(value = "stationName", defaultValue = "", required=true) String stationName,
            @RequestParam(value = "startDate", required=true, defaultValue="1970-01-01") Date startDate,
            @RequestParam(value = "endDate", required=true, defaultValue="2099-12-31") Date endDate,
            //默认起止时间为1970-01-01至2099-12-31
            RedirectAttributes rA, ModelMap model) {
    	
    	List<String> listStationName=CenterTool.getStationNameList();
		model.addAttribute("listStationName", listStationName);

        System.out.println("传入dutyType为:");
        System.out.println(dutyType);
        System.out.println("传入stationName为:");
        System.out.println(stationName);
        System.out.println("传入startDate为:");
        System.out.println(startDate);
        System.out.println("传入endDate为:");
        System.out.println(endDate);

        String errormessage = "";
        //Sort sort = new Sort(Sort.Direction.ASC, "staff.id");
        Pageable pageable = PageRequest.of(page, size);


        // 分站ID不存在查找
        if (stationName.isEmpty()) {
            int dT = dutyType.intValue();

            if(dT==1) {
                //投递员查找
                Page<Object[]> list = sR.find_all_deliver(startDate, endDate, pageable);
                model.addAttribute("objectlist", list);
            }

            else if(dT==2) {
                //征订员查找
                Page<Object[]> list = sR.find_all_solicit(startDate, endDate, pageable);
                model.addAttribute("objectlist", list);
            }

            else {
                errormessage = "请输入正确的职务类型";
                rA.addAttribute("errormessage", errormessage);
                return "redirect:/todicperformance";
            }

            return "dic/performance/dicperformance";

        }

        // id不存在的话根据name查找
        else{
            int dT = dutyType.intValue();

            if(dT==1) {
                //投递员查找
                Page<Object[]> list = sR.find_deliver_by_station(stationName, startDate, endDate, pageable);
                model.addAttribute("objectlist", list);
            }

            else if(dT==2) {
                //征订员查找
                Page<Object[]> list = sR.find_solicit_by_station(stationName, startDate, endDate, pageable);
                model.addAttribute("objectlist", list);
            }

            else {
                errormessage = "请输入正确的职务类型";
                System.out.println(errormessage);
                rA.addAttribute("errormessage", errormessage);
                return "redirect:/todicperformance";
            }

            return "dic/performance/dicperformance";
        }

    }

}
