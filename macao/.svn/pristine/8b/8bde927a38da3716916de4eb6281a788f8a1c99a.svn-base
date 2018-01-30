package com.yinghai.macao.app.controller;

import com.yinghai.macao.app.vo.ResponseVo;
import com.yinghai.macao.common.model.OrderDirection;
import com.yinghai.macao.common.model.SpcarOrder;
import com.yinghai.macao.common.service.OrderDirectionService;
import com.yinghai.macao.common.service.SpcarOrderService;
import com.yinghai.macao.common.util.JsonDateValueProcessor;
import com.yinghai.macao.common.util.ResponseUtils;
import com.yinghai.macao.common.util.StringUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/6/14.
 */
@Controller
@RequestMapping("spcar/orderDirection")
public class OrderDirectionController {

    @Autowired
    private OrderDirectionService orderDirectionService;
    @Autowired
    private SpcarOrderService spcarOrderService;

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public void updateDirectionLoc(HttpServletRequest request, HttpServletResponse response){
        response.setContentType("application/json;charset=utf-8");
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String orderId = request.getParameter("orderId");
        if (StringUtil.empty(orderId)) {
            ResponseVo.send101Code(response,"orderId不能为空！");
            return;
        }
        String locX = request.getParameter("locX");
        if (StringUtil.empty(locX)) {
            ResponseVo.send101Code(response,"locX不能为空！");
            return;
        }
        String locY = request.getParameter("locY");
        if (StringUtil.empty(locY)) {
            ResponseVo.send101Code(response,"locY不能为空！");
            return;
        }
        String descript = request.getParameter("descript");
        if (StringUtil.empty(descript)) {
            ResponseVo.send101Code(response,"descript不能为空！");
            return;
        }
        SpcarOrder spcarOrder = spcarOrderService.findOneBykey(Integer.parseInt(orderId));
        if(SpcarOrder.PAY_PENDING_STATUS!=spcarOrder.getStatus().intValue()){
            ResponseVo.send103Code(response,spcarOrder.getStatus().intValue()+"订单不为进行，无法更新途径点！");
            return;
        }
        OrderDirection orderDirection = new OrderDirection();
        orderDirection.setOrderId(Integer.parseInt(orderId));
        orderDirection.setCreateTime(new Date());
        orderDirection.setLocx(Double.parseDouble(locX));
        orderDirection.setLocy(Double.parseDouble(locY));
        orderDirection.setDescript(descript);
        String remark = request.getParameter("remark");
        if (!StringUtil.empty(remark)) {
            orderDirection.setRemark(remark);
        }
        int i = orderDirectionService.saveOrderDirection(orderDirection);
        if (i>0){
            ResponseVo.send1Code(response,"success",new JSONObject());
        }else{
            ResponseVo.sendNoDataUpdateCode(response,"更新成功条数：0");
        }
    }
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public void list(HttpServletRequest request, HttpServletResponse response){

        String orderId = request.getParameter("orderId");
        if (StringUtil.empty(orderId)) {
//            ResponseVo.send101Code(response,"orderId不能为空！");
//            return;
        }
        orderId ="1372";
        OrderDirection orderDirection = new OrderDirection();
        orderDirection.setOrderId(1372);
        List<OrderDirection> list = orderDirectionService.findList(orderDirection);
        SpcarOrder spcarOrder = spcarOrderService.findOneBykey(Integer.parseInt(orderId));
        JsonConfig config = new JsonConfig();
        config.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor("yyyy-MM-dd HH:mm:ss"));
        JSONArray array = JSONArray.fromObject(list,config);
        JSONObject order = JSONObject.fromObject(spcarOrder,config);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("list",array);
        jsonObject.put("order",order);
        ResponseVo.send1Code(response,"success",jsonObject);
    }
}
