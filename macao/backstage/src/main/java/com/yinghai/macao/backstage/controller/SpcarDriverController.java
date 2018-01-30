package com.yinghai.macao.backstage.controller;

import com.alibaba.fastjson.JSONObject;
import com.yinghai.macao.backstage.model.SpcarManager;
import com.yinghai.macao.backstage.service.impl.SpcarManagerService;
import com.yinghai.macao.common.constant.Constant;
import com.yinghai.macao.common.constant.SpcarDriverPushCode;
import com.yinghai.macao.common.model.Location;
import com.yinghai.macao.common.model.SpcarDriver;
import com.yinghai.macao.common.model.SpcarOrder;
import com.yinghai.macao.common.model.SpcarPassenger;
import com.yinghai.macao.common.service.SpcarDriverService;
import com.yinghai.macao.common.service.SpcarLocationService;
import com.yinghai.macao.common.service.SpcarOrderService;
import com.yinghai.macao.common.service.SpcarPassengerService;
import com.yinghai.macao.common.util.*;

import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Administrator on 2017/3/23.
 */
@RequestMapping("/admin/driver")
@Controller
public class SpcarDriverController {
    private Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private SpcarDriverService spcarDriverService;
    @Autowired
    private SpcarPassengerService spcarPassengerService;
    @Autowired
    private SpcarOrderService spcarOrderService;
    @Autowired
    private SpcarLocationService spcarLocationService;
    /**
     * 专车司机列表
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("/list")
    public String list(HttpServletRequest request, ModelMap model) {
        log.debug("======获取司机列表======");
        try {
			request.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        SpcarDriver spcarDriver = new SpcarDriver();
        String id = request.getParameter("id");
        if(!StringUtil.empty(id)){
        	spcarDriver.setSpcarDriverId(Integer.valueOf(id));
        }
        String countryCode = request.getParameter("query.countryCode");//区号
        if(!StringUtil.empty(countryCode)){
        	spcarDriver.setCountryCode(countryCode);
        }
        String status = request.getParameter("query.status");//状态
        if(!StringUtil.empty(status)){
        	spcarDriver.setStatus(status);
        }
        String tel = request.getParameter("query.tel");//手机
        if(!StringUtil.empty(tel)){
        	spcarDriver.setTel("%"+tel.trim()+"%");//用来做模糊、相似查询
        }

        String name = request.getParameter("query.name");
        if(!StringUtil.empty(name)){
        	spcarDriver.setName("%"+name.trim()+"%");//用来做模糊、相似查询
        }
        String num = request.getParameter("page");//页数
        Integer pageNum;
        if(StringUtil.empty(num)){
            pageNum = 1;
        }else{
            pageNum = Integer.valueOf(num);
        }
        String size = request.getParameter("pageSize");//一页中的条数
        Integer pageSize;
        if(StringUtil.empty(size)){
            pageSize = 10;
        }else{
            pageSize = Integer.valueOf(size);
        }
        //根据条件查询list
       Page<SpcarDriver> page = spcarDriverService.findList(pageNum,pageSize,spcarDriver);
        model.addAttribute("page",page);
        //查詢條件數據，模糊查詢的需要去掉第一位與最後一位字節
        if(spcarDriver.getTel()!=null&&spcarDriver.getTel().startsWith("%")){
        	spcarDriver.setTel(tel.trim());
        }
        if(spcarDriver.getName()!=null&&spcarDriver.getName().startsWith("%")){
        	spcarDriver.setName(name.trim());
        }
        model.addAttribute("spcarDriver",spcarDriver);//返回前端的查询条件
        model.addAttribute("pageNo",page.getPageNum());
        model.addAttribute("pageSize",page.getPageSize());
        model.addAttribute("recordCount",page.getTotal());
        model.addAttribute("pageCount",page.getPages());
        return "driver/list";
    }
    
    @RequestMapping("/save")
    public void save( HttpServletRequest request, HttpServletResponse response,ModelMap model) throws Exception {
    	try {
			request.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	SpcarDriver spcarDriver = new SpcarDriver();
    	  JSONObject responseObject = new JSONObject();
    	String act = "";
    	//通过ID是否存在来判断该操作是新增还是修改
    	String id = request.getParameter("id");
    	if(StringUtil.notEmpty(id)){
    		spcarDriver.setSpcarDriverId(TransformUtils.toInt(id));
    		act="UPDATE";
    	}else{
    		act="ADD";
    	}
    	String familyName = request.getParameter("firstName");
    	if(StringUtil.notEmpty(familyName)){
    		spcarDriver.setFamilyName(familyName);
    	}
    	
    	String givenName = request.getParameter("lastName");
    	if(StringUtil.notEmpty(givenName)){
    		spcarDriver.setGivenName(givenName);
    	}
    	if(StringUtil.notEmpty(givenName)&&StringUtil.notEmpty(familyName)){
    		spcarDriver.setName(familyName+givenName);
    	}

    	
    	String license_true = request.getParameter("license_true");
    	if(StringUtil.notEmpty(license_true)){
    		spcarDriver.setLicenseTrue(license_true);
    	}
    	SimpleDateFormat sdf =   new SimpleDateFormat( "yyyy-MM-dd" );
    	String license_till = request.getParameter("license_till");
    	if(StringUtil.notEmpty(license_till)){
    		spcarDriver.setLicenseTill(sdf.parse(license_till));
    	}
    	String remark = request.getParameter("remark");
    	spcarDriver.setRemark(remark);
    	//新增操作需要给某些值赋值默认值
    	if("ADD".equals(act)){
        	spcarDriver.setStatus(SpcarDriver.DRIVER_OFFLINE_STATUS);
        	spcarDriver.setOrderCount(0);
        	spcarDriver.setCancelCount(0);
        	spcarDriver.setFinishCount(0);
        	spcarDriver.setFinishAmount(0);
        	spcarDriver.setDeleted(false);
    		spcarDriver.setCreateTime(new Date());
        	String countryCode = request.getParameter("countryCode");
        	if(StringUtil.notEmpty(countryCode)){
        		spcarDriver.setCountryCode(countryCode);
        	}
        	
        	String tel = request.getParameter("tel");
        	if(StringUtil.notEmpty(tel)){
        		spcarDriver.setTel(tel);
        	}
        	String countryCode2 = request.getParameter("countryCode2");
        	String tel2 = request.getParameter("tel2");
        	if(StringUtil.notEmpty(tel2)){
        		if(StringUtil.notEmpty(countryCode2)){
        			spcarDriver.setCountryCode2(countryCode2);
        		}
        		spcarDriver.setTel2(tel2);
        	}
    		int i = spcarDriverService.createSpcarDriver(spcarDriver);
    		if(i>0){
    			id = spcarDriver.getSpcarDriverId()+"";
    		}
    	}
    	//判断文件是否有做过修改，如果没有就直接跳过文件上传操作
    	String boochange = request.getParameter("boochange");
    	if(StringUtil.notEmpty(boochange)&&"true".equals(boochange)){
    	  CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
    	  if(multipartResolver.isMultipart(request)){  
              //转换成多部分request    
              MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest)request;  
              //取得request中的所有文件名  
              Iterator<String> iter = multiRequest.getFileNames();  
              while(iter.hasNext()){  
                  //记录上传过程起始时的时间，用来计算上传时间  
                  int pre = (int) System.currentTimeMillis();  
                  //取得上传文件  
                  MultipartFile file = multiRequest.getFile(iter.next());  
                  if(file != null){  
                      //取得当前上传文件的文件名称  
                      String myFileName = file.getOriginalFilename();  
                      //如果名称不为“”,说明该文件存在，否则说明该文件不存在  
                      if(myFileName.trim() !=""){  
                          System.out.println(myFileName);  
                          //重命名上传后的文件名  
                          String fileName = id + "."+file.getOriginalFilename().split("\\.")[1];  
                          //定义上传路径  
                          String realPath = Constant.filepath+Constant.spcardriverimage;
	                      	File fileIo = new File(realPath);
	        				//创建文件夹
	        				if (!fileIo.exists()) {
	        					fileIo.mkdirs();
	        				}
                          String path = realPath + fileName;  
                          File localFile = new File(path);  
                          file.transferTo(localFile);
                          
                          if("ADD".equals(act)){
                        	  spcarDriver = new SpcarDriver();
                        	  spcarDriver.setSpcarDriverId(TransformUtils.toInt(id));
                        	  spcarDriver.setImage(Constant.spcardriverimage + fileName);
                              int i =  spcarDriverService.updateSpcarDriver(spcarDriver);
                              if(i>0){
                            	  responseObject.put("msg", "create success");
                                  responseObject.put("code", "1");
                                  responseObject.put("data", new JSONObject());
                                  ResponseUtils.renderJson(response, responseObject.toString());
                                  return;
                              }
                          }else{
                        	  spcarDriver.setLastUpdate(new Date());
                        	  spcarDriver.setImage(Constant.spcardriverimage + fileName);
                              int i =  spcarDriverService.updateSpcarDriver(spcarDriver);
                              if(i>0){
                            	  responseObject.put("msg", "update success");
                                  responseObject.put("code", "2");
                                  responseObject.put("data", new JSONObject());
                                  ResponseUtils.renderJson(response, responseObject.toString());
                                  return;
                              }
                          }
                      }  
                  }  
                  //记录上传该文件后的时间  
                  int finaltime = (int) System.currentTimeMillis();  
                  System.out.println(finaltime - pre);  
              }  
                
          }  
    	}else{
    		//此时是无文件上传时操作
    		if("ADD".equals(act)){
               	  responseObject.put("msg", "create success");
                     responseObject.put("code", "1");
                     responseObject.put("data", new JSONObject());
                     ResponseUtils.renderJson(response, responseObject.toString());
                     return;
    		}
    		 spcarDriver.setLastUpdate(new Date());
    		 String countryCode2 = request.getParameter("countryCode2");
         	 String tel2 = request.getParameter("tel2");
         	 spcarDriver.setCountryCode2(countryCode2);
         	 spcarDriver.setTel2(tel2);
    		 int i =  spcarDriverService.updateSpcarDriver(spcarDriver);
    		 if(i>0){
    			  responseObject.put("msg", "update success");
                  responseObject.put("code", "2");
                  responseObject.put("data", new JSONObject());
                  ResponseUtils.renderJson(response, responseObject.toString());
                  return;
             }
    	}
    }
    @RequestMapping("/edit")
    public String editOrSave(HttpServletRequest request, HttpServletResponse response,ModelMap model) {
        log.debug("======edit or new one manager======");
        try {
			request.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        //根据状态判断是新增还是修改
        String act = request.getParameter("act");
        String result = "driver/edit";
        if("upd".equalsIgnoreCase(act)){
            String id = request.getParameter("id");
            if(!StringUtil.empty(id)){
                model.addAttribute("spcarDriver",spcarDriverService.findById(Integer.valueOf(id)));
                //System.out.println("===================spcarDriver订单总额："+spcarDriverService.findById(Integer.valueOf(id)).getFinishAmount());
                //获取所有完成订单，并按完成时间降序排序
                SpcarOrder spcarOrder = new SpcarOrder();
                spcarOrder.setDriverId(Integer.valueOf(id));
                spcarOrder.setStatus(SpcarOrder.ORDER_FINISH_STATUS);
                List<SpcarOrder> list = spcarOrderService.findList(spcarOrder);
                List<SpcarOrder> driverOrderList = null;
                Integer size = 0;
				if(list!=null&&list.size()>10){
                	driverOrderList = list.subList(0, 10);
                }
				if(list!=null&&list.size()>0&&list.size()<11){
					driverOrderList = list;
				}
				if(list!=null){
					size = list.size();
				}
                model.addAttribute("driverOrderList", driverOrderList);
                model.addAttribute("size", size);
            }else{
                log.error("======edit one manager.id can not null======");
                model.addAttribute("msg","id can not be null");
                return "500";
            }
        }
        return result;
    }

  
    @RequestMapping("/del")
    public void delManager(HttpServletRequest request, HttpServletResponse response,ModelMap model) {
        log.debug("======删除專車账号!======");
        try {
			request.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        JSONObject responseObject = new JSONObject();
        String id = request.getParameter("id");
        int  i = spcarDriverService.delete(Integer.valueOf(id));
        if(i<=0){
            responseObject.put("msg", "delete fail");
            responseObject.put("code", "110");
            responseObject.put("data", new JSONObject());
            ResponseUtils.renderJson(response, responseObject.toString());
        }else{
            responseObject.put("msg", "success");
            responseObject.put("code", "1");
            responseObject.put("data", new JSONObject());
            ResponseUtils.renderJson(response, responseObject.toString());
        }
    }
    /**
     * 新增操作时判断该手机号是否存在,只有在新增时才会进入
     * @param request
     * @param response
     * @param model
     */
    @RequestMapping("/checkTel")
    public void checkTel(HttpServletRequest request, HttpServletResponse response,ModelMap model) {
        log.debug("======检查手机是否存在!======");
        String tel = request.getParameter("tel");
        String id = request.getParameter("id");
        String countryCode = request.getParameter("countryCode");
        JSONObject responseObject = new JSONObject();
        SpcarDriver spcarDriver = spcarDriverService.findByTel(countryCode, tel);
        if(spcarDriver!=null&&StringUtil.empty(id)){//判断ID是否为空是为了以防万一修改时进入了此方法，几率不大，所以放在后面
        	   responseObject.put("msg", "fail");
               responseObject.put("code", "105");
               responseObject.put("data", new JSONObject());
               ResponseUtils.renderJson(response, responseObject.toString());
               return;
        }
        }

	/**
	 * 查询在线的专车司机列表（接口）
	 * @param request
	 * @param response
	 */
    @RequestMapping(value = "findDriverOnLineList", method = RequestMethod.POST)
    public void findOnLineDriver(HttpServletRequest request, HttpServletResponse response){

        List<SpcarDriver> list = spcarDriverService.findOnlineList(); //在线司机
        JsonConfig config = new JsonConfig();
        config.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor("yyyy-MM-dd HH:mm:ss"));
        JSONArray data = JSONArray.fromObject(list, config);
        JSONObject responseObject = new JSONObject();
        responseObject.put("code","1");
        responseObject.put("msg","success");
        responseObject.put("data",data);
        ResponseUtils.renderJson(response,responseObject.toJSONString());
    }
    /**
     * 为所有的专车司机、乘客添加用户标签，所有专车司机统一拥有一个标签，所有的乘客统一拥有一个标签
     * @param request
     * @param response
     * @param model
     * @throws Exception
     */
    @RequestMapping("/updateTags")
    public void updateTags(HttpServletRequest request, HttpServletResponse response,ModelMap model) throws Exception {
    	//更新司机的标签
    	List<SpcarDriver> list = spcarDriverService.findAllList();
    	String sign = TlsSignUtil.getTlsSignKey(Constant.manager);
    	if(list!=null&&list.size()!=0){
    		for(int i=0;i<list.size();i++){
    			SpcarDriver spcarDriver = list.get(i);
    			String imName = spcarDriver.getImName();
    			//标签名字
    			JSONArray tags = new JSONArray();
    			tags.add(Constant.driver);
    			String addTags = TecentImUtils.addTags(sign, imName,tags);
    			net.sf.json.JSONObject addTagsJson = net.sf.json.JSONObject.fromObject(addTags);
    			if (!addTagsJson.getString("ActionStatus").equals("OK")) {
    				log.error("===================添加用戶標籤失敗"+imName);
    				continue;
    			}
    		}
    	}
    	//更新乘客的标签
    	List<SpcarPassenger> passengerList = spcarPassengerService.findAllList(new SpcarPassenger());
    	if(passengerList!=null&&passengerList.size()!=0){
    		for(int i=0;i<passengerList.size();i++){
    			SpcarPassenger spcarPassenger  = passengerList.get(i);
    			String imName = spcarPassenger.getImName();
    			//标签名字
    			JSONArray tags = new JSONArray();
    			tags.add(Constant.passenger);
    			String addTags = TecentImUtils.addTags(sign, imName,tags);
    			net.sf.json.JSONObject addTagsJson = net.sf.json.JSONObject.fromObject(addTags);
    			if (!addTagsJson.getString("ActionStatus").equals("OK")) {
    				log.error("===================添加用戶標籤失敗"+imName);
    				continue;
    			}
    		}
    	
    	}
        }
    
    /**
     * 通过标签推送信息给司机
     */
    @RequestMapping("/pushMsgToDriver")
    public void pushMsgToDriver(HttpServletRequest request, HttpServletResponse response,ModelMap model) throws Exception {
    	String str = request.getParameter("msg");//需要推送的信息
    	JSONObject responseObject = new JSONObject();
		responseObject.put("msg", "success");
		responseObject.put("code", SpcarDriverPushCode.PUSHBYTAGS);
		responseObject.put("data", str);
		String sign = TlsSignUtil.getTlsSignKey("admin");
		String type = request.getParameter("type");
        String groupTag = "";
        if("driver".equals(type)){
            groupTag = Constant.driver;
        }else{
            groupTag = Constant.passenger;
        }
        str = "推送消息:"+str;
		String restr = TecentImUtils.pushOne(sign, groupTag, str);
		net.sf.json.JSONObject addTagsJson = net.sf.json.JSONObject.fromObject(restr);
		if (!addTagsJson.getString("ActionStatus").equals("OK")) {
			log.error("===================推送901司机失败 ====SpcarDriverController/pushMsgToDriver："+restr);
			responseObject = new JSONObject();
			responseObject.put("msg", "fail");
			responseObject.put("code", "101");
			responseObject.put("data", new JSONObject());
			return;
		}
		responseObject = new JSONObject();
		responseObject.put("msg", "success");
		responseObject.put("code", "1");
		responseObject.put("data", new JSONObject());
		ResponseUtils.renderJson(response,responseObject.toJSONString());
		return;
    }
    
    @RequestMapping(value = "findDriverList", method = RequestMethod.POST)
    public void findDriverList(HttpServletRequest request, HttpServletResponse response){

        List<SpcarDriver> list = spcarDriverService.findAllList();
        JsonConfig config = new JsonConfig();
        config.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor("yyyy-MM-dd HH:mm:ss"));
        JSONArray data = JSONArray.fromObject(list, config);
        JSONObject responseObject = new JSONObject();
        responseObject.put("code","1");
        responseObject.put("msg","success");
        responseObject.put("data",data);
        ResponseUtils.renderJson(response,responseObject.toJSONString());
    }
    
    @RequestMapping(value = "pageDriverList", method = RequestMethod.POST)
    public void pageDriverList(HttpServletRequest request, HttpServletResponse response){
    	Integer pageNum = TransformUtils.toInt(request.getParameter("page"));
		if (pageNum == null || pageNum == 0) {
			pageNum = 1;
		}
		Integer pageSize = 10;
        List<SpcarDriver> list = spcarDriverService.findAllList();
        Page<SpcarDriver> page = spcarDriverService.getPageOnlineDriver(pageNum, pageSize, list);
        JsonConfig config = new JsonConfig();
        config.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor("yyyy-MM-dd HH:mm:ss"));
        JSONArray data = JSONArray.fromObject(page.getResult(), config);
        JSONObject driverList = new JSONObject();
        driverList.put("driverList", data);
        driverList.put("pages", page.getPages());
        JSONObject responseObject = new JSONObject();
        responseObject.put("code","1");
        responseObject.put("msg","success");
        responseObject.put("data",driverList);
        ResponseUtils.renderJson(response,responseObject.toJSONString());
    }
    
    @RequestMapping(value = "pageOnlineDriverList", method = RequestMethod.POST)
    public void pageOnlineDriverList(HttpServletRequest request, HttpServletResponse response){
    	Integer pageNum = TransformUtils.toInt(request.getParameter("page"));
		if (pageNum == null || pageNum == 0) {
			pageNum = 1;
		}
		Integer pageSize = 10;
        List<SpcarDriver> list = spcarDriverService.findOnlineList();
        Page<SpcarDriver> page = spcarDriverService.getPageOnlineDriver(pageNum, pageSize, list.size()==0?null:list);
        JsonConfig config = new JsonConfig();
        config.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor("yyyy-MM-dd HH:mm:ss"));
        JSONArray data = JSONArray.fromObject(page.getResult(), config);
        JSONObject driverList = new JSONObject();
        driverList.put("driverList", data);
        driverList.put("pages", page.getPages());
        JSONObject responseObject = new JSONObject();
        responseObject.put("code","1");
        responseObject.put("msg","success");
        responseObject.put("data",driverList);
        ResponseUtils.renderJson(response,responseObject.toJSONString());
    }
    
    /**
	 * 跳转到历史足迹页面
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/footprint")
	public String toFootPrint(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		log.info("======跳转到历史足迹页面======");
		Integer driverId  =TransformUtils.toInt(request.getParameter("id"));
		if(driverId>0){
			//查询乘客信息
			SpcarDriver driver = spcarDriverService.findById(driverId);
			if(driver!=null){
				model.put("driver", driver);
			}
		}
		return "order/footprint";
	}
	/**
	 * 查询对应日期司机的历史线路和已完成订单
	 * @param request
	 * @param response
	 */
	@RequestMapping("/findAllRouteAndOrder")
	public void findAllRouteAndOrder(HttpServletRequest request,HttpServletResponse response){
		log.info("查询对应日期司机的历史线路和已完成订单");
		Integer driverId = TransformUtils.toInt(request.getParameter("driverId"));
		String time = request.getParameter("time");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date d = null;
		Integer page = TransformUtils.toInt(request.getParameter("page"));
		Integer pageSize = TransformUtils.toInt(request.getParameter("pageSize"));
		page = (page!=null&&page>0)?page:1;
		pageSize = (pageSize!=null&&pageSize>0)?pageSize:1;
		JSONObject responseObject = new JSONObject();
		responseObject.put("code", "101");
		responseObject.put("data",new JSONObject());
		if(driverId<1){
			responseObject.put("msg", "driverId有误");
			ResponseUtils.renderJson(response, responseObject.toString());
			return;
		}
		if(time==null){
			responseObject.put("msg", "日期参数为空");
			ResponseUtils.renderJson(response, responseObject.toString());
			return;
		}
		try {
			d = sdf.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
			responseObject.put("msg", "日期参数格式有误");
			ResponseUtils.renderJson(response, responseObject.toString());
			return;
		}
		//判断司机是否存在
		SpcarDriver driver = spcarDriverService.findById(driverId);
		if(driver==null){
			responseObject.put("code", "102");
			responseObject.put("data",new JSONObject());
			responseObject.put("msg", "司机不存在");
			ResponseUtils.renderJson(response, responseObject.toString());
			return;
		}
		//查询该司机对应日期的完成订单
		Date d2 = new Date();
		d2.setTime(d.getTime()+24*60*60*1000-1);
		SpcarOrder spcarOrder = new SpcarOrder();
		spcarOrder.setDriverId(driverId);
		spcarOrder.setStatus(SpcarOrder.ORDER_FINISH_STATUS);
		spcarOrder.setCreateTimeS(d);
		spcarOrder.setCreateTimeE(d2);
		Page<SpcarOrder> orders = spcarOrderService.findFinishList(1, 10, spcarOrder);
		//查询当天所有的足迹
		List<Location> location = spcarLocationService.findLocation(driverId, d, d2);
		JSONObject data = new JSONObject();
		JsonConfig config = new JsonConfig();
		config.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor("yyyy-MM-dd HH:mm:ss"));
		data.put("order", JSONArray.fromObject(orders, config));
		data.put("location", JSONArray.fromObject(location, config));
		responseObject.put("code", "1");
		responseObject.put("data",data);
		responseObject.put("msg", "success");
		ResponseUtils.renderJson(response, responseObject.toString());
	}
	
    
    
}
