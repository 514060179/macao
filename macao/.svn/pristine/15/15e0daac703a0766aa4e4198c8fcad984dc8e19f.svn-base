package com.yinghai.macao.common.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yinghai.macao.common.constant.Constant;
import org.apache.commons.lang.StringUtils;

import java.util.List;

/**
 * Created by simon on 2017/2/22.
 *用云通信IM 消息单聊、多聊、群聊
 */
public class IMMsgRequestUtil {

    /**
     *发送单聊消息
     * @param sign 管理员签名
     * @param syncOtherMachine 1：把消息同步到From_Account在线终端和漫游上；2：消息不同步至From_Account；若不填写默认情况下会将消息存From_Account漫游
     * @param fromAccount 发送者
     * @param toAccount 接收者
     * @param array 消息集合 包含TIMTextElem(文本消息)，TIMFaceElem(表情消息)，TIMLocationElem(位置消息)，TIMCustomElem(自定义消息)
     * @return 发送响应是否成功
     * @throws Exception
     */
    public static boolean sendMsg(String sign,Integer syncOtherMachine,String fromAccount,String toAccount,JSONArray array) throws Exception {
        String requestJson = RequestJson.sendMsgJsonString(syncOtherMachine,fromAccount,toAccount,RandomUtil.getRandomInt(),array,false,"");
        if (StringUtils.isEmpty(requestJson)){
            return false;
        }
        JSONObject resultObj = HttpRequester.imRequestSend(Constant.SINGLE_MSG_URL,sign,Constant.manager,Constant.sdkAppId,"json", requestJson);
        System.out.println(resultObj);
        if ("OK".equals(resultObj.getString("ActionStatus"))){
            return true;
        }else{
            return false;
        }
    }
    /**
     *发送单聊消息
     * @param sign 管理员签名
     * @param syncOtherMachine 1：把消息同步到From_Account在线终端和漫游上；2：消息不同步至From_Account；若不填写默认情况下会将消息存From_Account漫游
     * @param fromAccount 发送者
     * @param toAccount 接收者
     * @return 发送响应是否成功
     * @throws Exception
     */
    public static boolean sendMsg(String sign,Integer syncOtherMachine,String fromAccount,String toAccount,JSONObject msgJsonject,boolean type,String msg) throws Exception {
    	JSONArray msgArray = new JSONArray();
    	msgArray.add(msgJsonject);
    	String requestJson = RequestJson.sendMsgJsonString(syncOtherMachine,fromAccount,toAccount,RandomUtil.getRandomInt(),msgArray,type,msg);
        System.out.println(requestJson);
        if (StringUtils.isEmpty(requestJson)){
            return false;
        }
        JSONObject resultObj = HttpRequester.imRequestSend(Constant.SINGLE_MSG_URL,sign,Constant.manager,Constant.sdkAppId,"json", requestJson);
        System.out.println(resultObj);
        if ("OK".equals(resultObj.getString("ActionStatus"))){
            return true;
        }else{
            return false;
        }
    }
    
    /**
     * 发送多聊消息
     * @param sign 管理员签名
     * @param syncOtherMachine 1：把消息同步到From_Account在线终端和漫游上；2：消息不同步至From_Account；若不填写默认情况下会将消息存From_Account漫游
     * @param fromAccount 发送者
     * @param toAccount 接收者数组
     * @param array 消息集合 包含TIMTextElem(文本消息)，TIMFaceElem(表情消息)，TIMLocationElem(位置消息)，TIMCustomElem(自定义消息)
     * @return 发送响应是否成功
     * @throws Exception
     */
    public static boolean sendMsg(String sign,Integer syncOtherMachine,String fromAccount,String[] toAccount,JSONArray array) throws Exception {
        String requestJson = RequestJson.sendMsgJsonString(syncOtherMachine,fromAccount,toAccount,RandomUtil.getRandomInt(),array);
        if (StringUtils.isEmpty(requestJson)){
            return false;
        }
        JSONObject resultObj = HttpRequester.imRequestSend(Constant.MULTIPLE_MSG_URL,sign,Constant.manager,Constant.sdkAppId,"json",requestJson );
        System.out.println(resultObj);
        if ("OK".equals(resultObj.getString("ActionStatus"))){
            return true;
        }else{
            return false;
        }
    }
    
    /**
     * 获取用户在线信息
     * @param sign 管理员签名
     * @param toAccount 接收者数组
     * @return 发送响应是否成功
     * @throws Exception
     */
    public static JSONObject queryState(String sign,String[] toAccount) throws Exception {
        String requestJson = RequestJson.queryStateJsonString(toAccount);
        if (StringUtils.isEmpty(requestJson)){
            return null;
        }
        JSONObject resultObj = HttpRequester.imRequestSend(Constant.QUERY_STATUS_SMS_MSG_URL,sign,Constant.manager,Constant.sdkAppId,"json",requestJson );
        System.out.println(resultObj);
        if ("OK".equals(resultObj.getString("ActionStatus"))){
            return resultObj;
        }else{
            return null;
        }
    }

    /**
     *创建群组
     * @param sign 管理员签名
     * @param groupId 群组id （选填）
     * @param type 群组类型：Private/Public/ChatRoom/AVChatRoom（必填）
     * @param groupName 群名称（必填）
     * @param introduction  群简介（选填）
     * @param notification  群公告（选填）
     * @param maxMemberCount  最大群成员数量（选填）
     * @param applyJoinOption 申请加群处理方式（选填） 包含FreeAccess（自由加入），NeedPermission（需要验证），DisableApply（禁止加群）
     * @return
     * @throws Exception
     */
    public static  JSONObject createMsgGroup(String sign,String groupId,String type,String groupName,String introduction,String notification,Integer maxMemberCount,String applyJoinOption) throws Exception {
        String requestJson = RequestJson.createGroupJsonString(groupId,Constant.manager,type,groupName,introduction,notification,maxMemberCount,applyJoinOption);
        if (StringUtils.isEmpty(requestJson)){
            return null;
        }
        JSONObject resultObj = HttpRequester.imRequestSend(Constant.CREATE_GROUP_URL,sign,Constant.manager,Constant.sdkAppId,"json", requestJson);
        System.out.println(resultObj);
        if ("OK".equals(resultObj.getString("ActionStatus"))){
            return resultObj;
        }else{
            return null;
        }
    }

    /**
     *加入群聊
     * @param sign 管理员签名
     * @param groupId 群组id
     * @param memberList 加入的成员列表
     * @return
     * @throws Exception
     */
    public static boolean addGroupMember(String sign,String groupId,List<String> memberList) throws Exception {
        String requestJson = RequestJson.addGroupJsonString(groupId,memberList);
        if (StringUtils.isEmpty(requestJson)){
            return false;
        }
        JSONObject resultObj = HttpRequester.imRequestSend(Constant.ADD_GROUP_URL,sign,Constant.manager,Constant.sdkAppId,"json",requestJson);
        System.out.println(resultObj);
        if ("OK".equals(resultObj.getString("ActionStatus"))){
            return true;
        }else{
            return false;
        }
    }

    /**
     *移除群成员
     * @param sign 管理员签名
     * @param groupId 群组id
     * @param memberList 成员列表
     * @param silence  是否静默删人。0：非静默删人；1：静默删人。不填该字段默认为0。
     * @param reason 移除原因
     * @return
     * @throws Exception
     */
    public static  boolean removeMember(String sign,String groupId,List<String> memberList,Integer silence,String reason) throws Exception {
        String requestJson = RequestJson.removeGroupMemberJsonString(groupId,memberList,1,reason);
        if (StringUtils.isEmpty(requestJson)){
            return false;
        }
        JSONObject resultObj = HttpRequester.imRequestSend(Constant.DEELETE_GROUP_MEMBER_URL,sign,Constant.manager,Constant.sdkAppId,"json",requestJson);
        System.out.println(resultObj);
        if ("OK".equals(resultObj.getString("ActionStatus"))){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 发送群消息
     * @param sign 管理员签名
     * @param groupId 群组id
     * @param fromAccount 发送账号
     * @param msgArray 发送消息体
     * @return
     * @throws Exception
     */
    public static  boolean sendGroupMsg(String sign,String groupId,String fromAccount,JSONArray msgArray) throws Exception {
        String requestJson = RequestJson.sendGroupMsgJsonString(groupId,fromAccount,RandomUtil.getRandomInt(),msgArray);
        if (StringUtils.isEmpty(requestJson)){
            return false;
        }
        JSONObject resultObj = HttpRequester.imRequestSend(Constant.SEND_GROUP_MSG_URL,sign,Constant.manager,Constant.sdkAppId,"json",requestJson);
        System.out.println(resultObj);
        if ("OK".equals(resultObj.getString("ActionStatus"))){
            return true;
        }else{
            return false;
        }
    }
    /**
     * 发送群消息
     * @param sign 管理员签名
     * @param groupId 群组id
     * @param fromAccount 发送账号
     * @param msgJsonject 发送消息体
     * @return
     * @throws Exception
     */
    public static  boolean sendGroupMsg(String sign,String groupId,String fromAccount,JSONObject msgJsonject) throws Exception {
    	JSONArray msgArray = new JSONArray();
    	msgArray.add(msgJsonject);
        String requestJson = RequestJson.sendGroupMsgJsonString(groupId,fromAccount,RandomUtil.getRandomInt(),msgArray);
        if (StringUtils.isEmpty(requestJson)){
            return false;
        }
        JSONObject resultObj = HttpRequester.imRequestSend(Constant.SEND_GROUP_MSG_URL,sign,Constant.manager,Constant.sdkAppId,"json",requestJson);
        System.out.println(resultObj);
        if ("OK".equals(resultObj.getString("ActionStatus"))){
            return true;
        }else{
            return false;
        }
    }

    /**
     *
     * @param groupId
     * @param fromAccount
     * @param text
     * @return
     * @throws Exception
     */
    public static  boolean sendGroupMsg(String groupId,String fromAccount,String text) throws Exception {
        JSONArray msgArray = new JSONArray();
        msgArray.add( RequestJson.getTextJsonMsg(text));
        String requestJson = RequestJson.sendGroupMsgJsonString(groupId,fromAccount,RandomUtil.getRandomInt(),msgArray);
        if (StringUtils.isEmpty(requestJson)){
            return false;
        }
        String sign = TlsSignUtil.getTlsSignKey(Constant.manager);
        JSONObject resultObj = HttpRequester.imRequestSend(Constant.SEND_GROUP_MSG_URL,sign,Constant.manager,Constant.sdkAppId,"json",requestJson);
        System.out.println(resultObj);
        if ("OK".equals(resultObj.getString("ActionStatus"))){
            return true;
        }else{
            return false;
        }
    }
//    private String getParamString() throws Exception{
//        Map<String,String> map = new HashMap<String,String>();
//        String sign = TlsSignUtil.getTlsSignKey("admin");
//        map.put("usersig",sign);
//        map.put("identifier",Constant.manager);
//        map.put("sdkappid",Constant.sdkAppId);
//        map.put("random",RandomUtil.getRandomInt()+"");
//        map.put("contenttype","json");
//        System.out.println(HttpRequester.getIMURLString(map));
//        return HttpRequester.getIMURLString(map);
////        String urlString = urlStr+"?usersig="+usersig+"&identifier="+identifier+"&sdkappid="+sdkappid+"&random="+System.currentTimeMillis()+"&contenttype="+contenttype;
//    }
    public static void main(String[] args) throws Exception {
//        String text = "{\n" +
//                "    \"code\": \"902\",\n" +
//                "    \"msg\": \"location msg\",\n" +
//                "    \"data\": {\n" +
//                "        \"locX\": \"22.151266\",\n" +
//                "        \"locY\": \"113.570748\",\n" +
//                "        \"id\": \"2\",\n" +
//                "        \"name\": \"test\"\n" +
//                "    }\n" +
//                "}";
////        System.out.println(sendGroupMsg(Constant.groupId,"admin",text));
//        String sign = TlsSignUtil.getTlsSignKey(Constant.manager);
//        JSONArray msgArray = new JSONArray();
//        msgArray.add( RequestJson.getTextJsonMsg(text));
//        System.out.println(sendMsg(sign,1,"qps","test",msgArray));;
        String sign = TlsSignUtil.getTlsSignKey(Constant.manager);
//        createMsgGroup(sign,"ReceiveOrderGroup","Public","专车")
//        sendMsg(sign,1,"admin","10867Passenger",RequestJson.getTextJsonMsg("{\"msg\":\"????¦Ì¡¤???????¡§???o???????¡è¡ì??|¨¦??¨¨??\",\"code\":\"703\",\"data\":{\"amount\":125000,\"cancelType\":\"\",\"carId\":0,\"confirmId\":0,\"confirmType\":\"\",\"createTime\":\"\",\"createTimeE\":\"\",\"createTimeS\":\"\",\"driverId\":2,\"endAddress\":\"\",\"endX\":0,\"endY\":0,\"memo\":\"\",\"orderNo\":\"20170519160220\",\"passengerId\":10272,\"payStatus\":1,\"payTime\":\"2017-05-19 16:03:12\",\"payWay\":\"\",\"payWayCode\":0,\"rejectId\":\"\",\"rejectType\":\"\",\"spcar\":{\"createTime\":\"\",\"spcarColor\":\"银色\",\"spcarId\":1,\"spcarNo\":\"12574\",\"spcarSit\":7,\"spcarType\":\"东风标致\",\"spcarUsed\":true,\"updateTime\":\"2017-05-19 16:51:22\"},\"spcarDriver\":{\"cancelCount\":0,\"countryCode\":\"86\",\"createTime\":\"\",\"deleted\":false,\"deviceId\":\"\",\"deviceType\":\"1\",\"driverType\":\"1\",\"englishCapability\":false,\"familyName\":\"微信\",\"finishCount\":0,\"givenName\":\"我家有只小青蛙\",\"image\":\"empty-person-image.jpg\",\"lastUpdate\":\"2017-05-19 16:51:22\",\"license\":\"51245\",\"licenseTill\":\"\",\"licenseTrue\":\"\",\"locStr\":\"\",\"locX\":0,\"locY\":0,\"name\":\"\",\"orderCount\":0,\"profileImage\":\"\",\"rating\":0,\"shift\":\"\",\"spcarDriverId\":2,\"spcarType\":\"东风标致\",\"status\":\"4\",\"tel\":\"15919162256\",\"userid\":0,\"verification\":\"0\"},\"spcarId\":1,\"spcarOrderId\":418,\"spcarPassenger\":{\"cancelCount\":0,\"countryCode\":\"86\",\"createTime\":\"\",\"deleted\":false,\"deviceId\":\"\",\"deviceType\":\"1\",\"familyName\":\"微信\",\"finishCount\":0,\"givenName\":\"我家有只小青蛙\",\"imName\":\"10867Passenger\",\"isgn\":\"eJxlj01Pg0AURff8islsa3SYMgImLppakAJp*GrdEcIM9IV2QDqtrcb-rmITSXzbc27ufR8aQginQXJblGV7lCpXl05g9IAwwTd-sOuA54XKpz3-B8W5g17kRaVEP0CdMUYJGTvAhVRQwdUo*B7kCB94kw8dv3njO0wtRu2xAvUAw0U296Inp5FpTA13ldn0JUrl0mhXS*84u0jXZ879Wb6ZO9PvJyyLvJr7yXPY7qyNc9dtkmCylXFQlWu1Pc3XsWu*zrgOENTN*yJ8HFUq2IvrIJvpFjHp*KWT6A-QykGgRGc6nZKfw9qn9gXFBlyY\",\"lastLogin\":\"2017-05-19 15:58:59\",\"locX\":0,\"locY\":0,\"name\":\"\",\"orderCount\":0,\"passengerId\":\"10272\",\"rating\":0,\"sex\":true,\"spcarId\":10272,\"status\":4,\"tel\":\"15919162256\",\"verification\":\"\"},\"startAddress\":\"ç\u008F æµ·å¸\u0082ä¿\u009Dç¨\u008Eå\u008Cºå\u0088©æ\u0097¶å¤§å\u008E¦é\u0099\u0084è¿\u0091\",\"startTime\":\"2016-12-04 22:02:12\",\"startX\":22.206398,\"startY\":113.538163,\"status\":4,\"totalHour\":5,\"updateTime\":\"2017-05-19 16:51:22\"}}"));
//        IMMsgRequestUtil.sendMsg(sign,1,"admin","10867Passenger",RequestJson.getTextJsonMsg("nimeme"));
//{"From_Account":"admin","MsgBody":[{"MsgContent":{"Text":"nimeme"},"MsgType":"TIMTextElem"}],"MsgRandom":270322,"OfflinePushInfo":{"PushFlag":1},"SyncOtherMachine":1,"To_Account":"10867Passenger"}
//{"From_Account":"admin","MsgBody":[{"MsgContent":{"Text":"nimeme"},"MsgType":"TIMTextElem"}],"MsgRandom":453403,"OfflinePushInfo":{"PushFlag":1},"SyncOtherMachine":1,"To_Account":"10867Passenger"}

    }
}
