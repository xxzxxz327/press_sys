package com.xxzxxz.press.web;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import com.xxzxxz.press.repository.AreaRepository;
import com.xxzxxz.press.repository.ConsumerRepository;
import com.xxzxxz.press.repository.DeliverRepository;
import com.xxzxxz.press.repository.DepartRepository;
import com.xxzxxz.press.repository.DutyRepository;
import com.xxzxxz.press.repository.InfoRepository;
import com.xxzxxz.press.repository.OfficeRepository;
import com.xxzxxz.press.repository.OrdersRepository;
import com.xxzxxz.press.repository.PressRepository;
import com.xxzxxz.press.repository.ReasonRepository;
import com.xxzxxz.press.repository.ReciRepository;
import com.xxzxxz.press.repository.StaffRepository;
import com.xxzxxz.press.repository.StationRepository;
import com.xxzxxz.press.repository.TransinfoRepository;
import com.xxzxxz.press.model.*;

@Component
public class CenterTool {
	
	@Autowired
	private  DepartRepository dR1;

	@Autowired
	private  DeliverRepository deliverRepository;

	@Autowired
	private  OfficeRepository oR1;

	@Autowired
	private  ReasonRepository rR1;

	@Autowired
	private  InfoRepository iR;

	@Autowired
	private  TransinfoRepository tR1;

	@Autowired
	private  DutyRepository dutyR1;
	
	@Autowired
	private  AreaRepository aR1;
	
	@Autowired
	private  StationRepository stationR1;
	
	@Autowired
	private  StaffRepository staffR1;
	

	@Autowired
	private  PressRepository pressR1;

	@Autowired
	private  StationRepository sR;
	@Autowired
	private  ReciRepository rnR;

	@Autowired
	private  OrdersRepository ordersRepository;
	
	@Autowired
	private ConsumerRepository consumerR1;
	
	
	private  static DepartRepository dR;
	private  static StationRepository stationR;
	private  static ReasonRepository rR;
	private  static TransinfoRepository tR;
	private  static DutyRepository dutyR;
	private  static OfficeRepository oR;
	private  static AreaRepository aR;
	private  static PressRepository pressR;
	private  static ConsumerRepository consumerR;
	private  static StaffRepository staffR;
	
	@PostConstruct
	   private void Init() {
	      dR = this.dR1;
	      stationR=this.stationR1;
	      rR=this.rR1;
	      tR=this.tR1;
	      dutyR=this.dutyR1;
	      oR=this.oR1;
	      aR=this.aR1;
	      pressR=this.pressR1;
	      consumerR=this.consumerR1;
	      staffR=this.staffR1;
	   }
	
	public static List removeDuplicate(List list){  
        List listTemp = new ArrayList();  
        for(int i=0;i<list.size();i++){  
            if(!listTemp.contains(list.get(i))){  
                listTemp.add(list.get(i));  
            }  
        }  
        return listTemp;  
	}
	
	public static List<String> getDepartNameList(){
		//System.out.println("到查询前");
		List<Department> listDepart=dR.findAll();
		//System.out.println("到查询后");
		List<String> listDepartDupName=new ArrayList<String>();
		
		if(listDepart!=null){
			for(int i=0;i<listDepart.size();i++){
				listDepartDupName.add(listDepart.get(i).getDepartName());
		}
		}	
		else{
			return null;	
			}
		
		List<String> listDepartName=removeDuplicate(listDepartDupName);
//		for(int i=0;i<listDepartName.size();i++){
//			System.out.println(listDepartName.get(i));
//		}
		return listDepartName;
	}
	
	public static List<String> getStationNameList(){

		List<Station> listStation=stationR.findAll();

		List<String> listStationDupName=new ArrayList<String>();
		
		if(listStation!=null){
			for(int i=0;i<listStation.size();i++){
				listStationDupName.add(listStation.get(i).getStationName());
		}
		}	
		else{
			return null;	
			}
		
		List<String> listStationName=removeDuplicate(listStationDupName);

		return listStationName;
	}
	
	public static List<String> getReasonNameList(){

		List<ChangeReason> listReason=rR.findAll();

		List<String> listReasonDupName=new ArrayList<String>();
		
		if(listReason!=null){
			for(int i=0;i<listReason.size();i++){
				listReasonDupName.add(listReason.get(i).getReasonName());
		}
		}	
		else{
			return null;	
			}
		
		List<String> listReasonName=removeDuplicate(listReasonDupName);

		return listReasonName;
	}
	
	public static List<String> getDutyNameList(){

		List<Duty> listDuty=dutyR.findAll();

		List<String> listDutyDupName=new ArrayList<String>();
		
		if(listDuty!=null){
			for(int i=0;i<listDuty.size();i++){
				listDutyDupName.add(listDuty.get(i).getDutyName());
		}
		}	
		else{
			return null;	
			}
		
		List<String> listDutyName=removeDuplicate(listDutyDupName);

		return listDutyName;
	}
	
	public static List<String> getTransinfoNameList(){

		List<Transinfo> listTransinfo=tR.findAll();

		List<String> listTransinfoDupName=new ArrayList<String>();
		
		if(listTransinfo!=null){
			for(int i=0;i<listTransinfo.size();i++){
				listTransinfoDupName.add(listTransinfo.get(i).getName());
		}
		}	
		else{
			return null;	
			}
		
		List<String> listTransinfoName=removeDuplicate(listTransinfoDupName);

		return listTransinfoName;
	}
	
	public static List<String> getOfficeNameList(){

		List<OfficeInfo> listOffice=oR.findAll();

		List<String> listOfficeDupName=new ArrayList<String>();
		
		if(listOffice!=null){
			for(int i=0;i<listOffice.size();i++){
				listOfficeDupName.add(listOffice.get(i).getOfficeName());
		}
		}	
		else{
			return null;	
			}
		
		List<String> listOfficeName=removeDuplicate(listOfficeDupName);

		return listOfficeName;
	}
	
	public static List<String> getPressNameList(){

		List<PressInfo> listPress=pressR.findAll();

		List<String> listPressDupName=new ArrayList<String>();
		
		if(listPress!=null){
			for(int i=0;i<listPress.size();i++){
				listPressDupName.add(listPress.get(i).getPressName());
		}
		}	
		else{
			return null;	
			}
		
		List<String> listPressName=removeDuplicate(listPressDupName);

		return listPressName;
	}
	
	public static List<String> getConsumerNameList(){

		List<Consumer> listConsumer=consumerR.findAll();

		List<String> listConsumerDupName=new ArrayList<String>();
		
		if(listConsumer!=null){
			for(int i=0;i<listConsumer.size();i++){
				listConsumerDupName.add(listConsumer.get(i).getName());
		}
		}	
		else{
			return null;	
			}
		
		List<String> listConsumerName=removeDuplicate(listConsumerDupName);

		return listConsumerName;
	}
	
	public static List<String> getAreaNameList(){

		List<Area> listArea=aR.findAll();

		List<String> listAreaDupName=new ArrayList<String>();
		
		if(listArea!=null){
			for(int i=0;i<listArea.size();i++){
				listAreaDupName.add(listArea.get(i).getAreaName());
		}
		}	
		else{
			return null;	
			}
		
		List<String> listAreaName=removeDuplicate(listAreaDupName);

		return listAreaName;
	}
	
	
	public static List<String> getStaffNameList(){

		System.out.println("0000000000000000000");
		List<Staff> listStaff=staffR.findAll();
		if(listStaff == null)
			System.out.println("11111111111111111111");
		
		List<String> listStaffDupName=new ArrayList<String>();
		
		if(listStaff!=null){
			for(int i=0;i<listStaff.size();i++){
				listStaffDupName.add(listStaff.get(i).getStaffName());
		}
		}	
		else{
			return null;	
			}
		
		List<String> listStaffName=removeDuplicate(listStaffDupName);
		
		if(listStaffName == null)
			System.out.println("22222222222222");
		
		System.out.println("333333333333333333");
		
		return listStaffName;
	}
	
	
}
