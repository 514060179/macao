package com.yinghai.macao.common.controller;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.yinghai.macao.common.model.SpcarOrder;
import com.yinghai.macao.common.service.SpcarOrderService;
import com.yinghai.macao.common.util.ResponseUtils;

import net.sf.json.JSONObject;
@Component
public class TaskCool {
    @Autowired
    private SpcarOrderService spcarOrderService;
    private Logger log = Logger.getLogger(this.getClass());
    /**
     * 第一个定时器测试方法
     */
    public void testJob(){
        System.out.println("test first taskJob .... ");
        SpcarOrder spcarOrder = new SpcarOrder();
        String[] statuss = {"999"}; 
        spcarOrder.setStatusArray(statuss);
        //spcarOrder.setPayStatus(-1);
        //查询出未支付的list记录
        List<SpcarOrder> list = spcarOrderService.findList(spcarOrder);
        if(list!=null&&list.size()!=0){
        	for(int i=0;i<list.size();i++){//每执行完一种情况直接跳到下一个循环
        		SpcarOrder order = list.get(i);
        		try {
        		int k=0;
        		//判断该订单是否是续单
        		if(order.getOrderId()!=null&&order.getOrderId()!=0){//说明该单为续单
        			//为续单时，先查询主单，如果主单为进行中，则不作处理，如果主单为其他状态，比如完成、取消时，将续单取消掉
        			SpcarOrder oldOrder = spcarOrderService.findOneBykey(order.getOrderId());
        			if(oldOrder!=null){//空判断处理
        				if(oldOrder.getStatus()==SpcarOrder.ORDER_FINISH_STATUS||oldOrder.getStatus()==SpcarOrder.ORDER_CANCEL_STATUS){//判断订单状态是否为取消或者完成
        					SpcarOrder updateOrder = new SpcarOrder();
               	        	updateOrder.setSpcarOrderId(order.getSpcarOrderId());
               	        	updateOrder.setStatus(SpcarOrder.ORDER_CANCAL_STATUS);
               	        	updateOrder.setUpdateTime(new Date());
               	        	int j = spcarOrderService.updateByPrimaryKeySelective(updateOrder,false);
               	        	if(j>0){//当j>0时，数据时更新成功的，k自加，用来显示成功的数量
               	        		k=k+1;
               	        		log.debug("======================操作的数据进程"+(k+1)+"/"+list.size());
               	        		continue;
               	        	}else{//如果就j<1时，无有效数据更新成功，报错，并将该订单的编号输出
               	        		log.error("======================操作的数据进程无效，无效记录的编号为"+order.getSpcarOrderId()+"/"+list.size());
               	        		continue;
               	        	}
        				}
        			}else{
        				log.error("TaskCool/testJob========该续单找不到原单号，数据问题");
        				continue;
        			}
        		}
        		//以下是非续单的处理
        		//判断开始时间与当前时间的时间差
           		 long to = (new Date()).getTime();  
           	        long  from= order.getStartTime().getTime();  
           	        int hours = (int) ((from - to)/(1000 * 60 * 60));  
           	        if(hours<1){//如果是小于一小时的，将paystatus状态改为-1
           	        	SpcarOrder updateOrder = new SpcarOrder();
           	        	updateOrder.setSpcarOrderId(order.getSpcarOrderId());
           	        	updateOrder.setStatus(SpcarOrder.ORDER_CANCAL_STATUS);
           	        	updateOrder.setUpdateTime(new Date());
           	        	int j = spcarOrderService.updateByPrimaryKeySelective(updateOrder,false);
           	        	if(j>0){//当j>0时，数据时更新成功的，k自加，用来显示成功的数量
           	        		k=k+1;
           	        		log.debug("======================操作的数据进程"+(k+1)+"/"+list.size());
           	        		continue;
           	        	}else{//如果就j<1时，无有效数据更新成功，报错，并将该订单的编号输出
           	        		log.error("======================操作的数据进程无效，无效记录的编号为"+order.getSpcarOrderId()+"/"+list.size());
           	        		continue;
           	        	}
           	        }
				} catch (Exception e) {//报错时打印报错信息并直接跳到下一个循环
					// TODO: handle exception
					log.error("======================操作的数据进程失败，失败记录的编号为"+order.getSpcarOrderId()+"/"+list.size()+e);
					continue;
				}
        
        	}
        	
        }
    }
}
