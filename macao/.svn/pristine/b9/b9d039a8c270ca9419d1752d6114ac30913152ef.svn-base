package com.yinghai.macao.common.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yinghai.macao.common.constant.Constant;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 拼接请求json字符串
 * Created by Administrator on 2017/2/17.
 */
public class RequestJson {
    /**
     * 管理员指定某一账号向另一账号发送消息(单个账号)  组装请求参数
     * @param syncOtherMachine 1：把消息同步到From_Account在线终端和漫游上；2：消息不同步至From_Account；若不填写默认情况下会将消息存From_Account漫游
     * @param fromAccount 消息发送方账号。（用于指定发送消息方账号）
     * @param toAccount 消息接收方账号。（数组形式）
     * @param msgRandom 消息随机数,由随机函数产生。（用作消息去重）
     * @param msgBodyContent 消息内容
     * @return
     */
    public static String sendMsgJsonString(Integer syncOtherMachine,String fromAccount,String toAccount,Integer msgRandom,JSONArray msgBodyContent,boolean offline,String msg){
        JSONObject object = new JSONObject();
        JSONObject offlineInfo = new JSONObject();
        if (syncOtherMachine==null||syncOtherMachine==0){
            syncOtherMachine=2;
        }
        if (StringUtils.isEmpty(fromAccount)){
            return null;
        }
        if (StringUtils.isEmpty(toAccount)){
            return null;
        }
        if(offline){
            offlineInfo.put("PushFlag", 0);
            offlineInfo.put("Desc", msg);
            offlineInfo.put("Ext", "");
            JSONObject apnsInfo = new JSONObject();
            apnsInfo.put("BadgeMode", 1);
            offlineInfo.put("ApnsInfo", apnsInfo);
        }else{
            offlineInfo.put("PushFlag", 1);
        }
        object.put("SyncOtherMachine",syncOtherMachine);
        object.put("From_Account",fromAccount);
        object.put("To_Account",toAccount);
        object.put("MsgRandom",msgRandom);
        object.put("MsgBody",msgBodyContent);
        object.put("OfflinePushInfo",offlineInfo);
        return object.toJSONString();
    }

    /**
     * 管理员指定某一账号向另一账号发送消息(多个个账号)  组装请求参数
     * @param syncOtherMachine 1：把消息同步到From_Account在线终端和漫游上；2：消息不同步至From_Account；若不填写默认情况下会将消息存From_Account漫游
     * @param fromAccount 消息发送方账号。（用于指定发送消息方账号）
     * @param toAccount 消息接收方账号。（数组形式）
     * @param msgRandom 消息随机数,由随机函数产生。（用作消息去重）
     * @param msgBodyContent 消息内容
     * @return
     */
    public static String sendMsgJsonString(Integer syncOtherMachine,String fromAccount,String[] toAccount,Integer msgRandom,JSONArray msgBodyContent){
        JSONObject object = new JSONObject();
        if (syncOtherMachine==null||syncOtherMachine==0){
            syncOtherMachine=2;
        }
        if (StringUtils.isEmpty(fromAccount)){
            return null;
        }
        if (toAccount.length==0||toAccount==null){
            return null;
        }
        object.put("SyncOtherMachine",syncOtherMachine);
        object.put("From_Account",fromAccount);
        object.put("To_Account",toAccount);
        object.put("MsgRandom",msgRandom);
        object.put("MsgBody",msgBodyContent);
        return object.toJSONString();
    }
    
    
    /**
     * 管理员指定某一账号向另一账号发送消息(多个个账号)  组装请求参数
     * @param toAccount 消息接收方账号。（数组形式）
     * @return
     */
    public static String queryStateJsonString(String[] toAccount){
        JSONObject object = new JSONObject();
        
        if (toAccount.length==0||toAccount==null){
            return null;
        }
        object.put("To_Account",toAccount);
        return object.toJSONString();
    }

    /**
     * 创建群组
     * @param ownerAccount 群主
     * @param type 群组类型：Private/Public/ChatRoom/AVChatRoom（必填）
     * @param name 群名称（必填）
     * @param introduction  群简介（选填）
     * @param notification  群公告（选填）
     * @param maxMemberCount  最大群成员数量（选填）
     * @param applyJoinOption 申请加群处理方式（选填） 包含FreeAccess（自由加入），NeedPermission（需要验证），DisableApply（禁止加群）
     * @return
     */
    public static String createGroupJsonString(String groupId,String ownerAccount,String type,String name,String introduction,String notification,Integer maxMemberCount,String applyJoinOption){
        JSONObject object = new JSONObject();
        if(!StringUtils.isEmpty(groupId)){
            object.put("GroupId",groupId);
        }
        if(StringUtils.isEmpty(ownerAccount)){
            return null;
        }
        if (StringUtils.isEmpty(type)){
           type = Constant.groupType.Public.toString();
        }
        if (StringUtils.isEmpty(name)){
            return null;
        }
        if (!StringUtils.isEmpty(introduction)&&!"".equals(introduction)){
            object.put("Introduction",introduction);
        }
        if (!StringUtils.isEmpty(notification)&&!"".equals(notification)){
            object.put("Notification",notification);
        }
        if (maxMemberCount==0||maxMemberCount==null){
            maxMemberCount=10000;
        }
        if (StringUtils.isEmpty(applyJoinOption)){
            applyJoinOption = Constant.applyJoinOption.FreeAccess.toString();
        }
        object.put("Owner_Account",ownerAccount);
        object.put("Type",type);
        object.put("Name",name);
        object.put("MaxMemberCount",maxMemberCount);
        object.put("ApplyJoinOption",applyJoinOption);
        return object.toJSONString();
    }

    /**
     * 加入群组
     * @param groupId 群组id
     * @param memberList 成员列表
     * @return
     */
    public static String addGroupJsonString(String groupId,List<String> memberList){
        JSONObject object = new JSONObject();
        if (StringUtils.isEmpty(groupId)){
            return null;
        }
        if (memberList.isEmpty()){
            return null;
        }
        object.put("GroupId",groupId);
        JSONArray array = new JSONArray();
        for (String str:memberList
             ) {
            JSONObject o = new JSONObject();
            o.put("Member_Account",str);
            array.add(o);
        }
        object.put("MemberList",array);
        return object.toJSONString();
    }

    /**
     * 删除群组成员
     * @param groupId 群组id
     * @param memberList 成员列表
     * @param silence 是否静默删人。0：非静默删人；1：静默删人。不填该字段默认为0。
     * @param reason 踢出用户原因。
     * @return
     */
    public static String removeGroupMemberJsonString(String groupId,List<String> memberList,Integer silence,String reason){
        JSONObject object = new JSONObject();
        if (StringUtils.isEmpty(groupId)){
            return null;
        }
        if (memberList.isEmpty()){
            return null;
        }
        String[] member = memberList.toArray(new String[memberList.size()]);
        object.put("GroupId",groupId);
        object.put("MemberToDel_Account",member);
        if (!StringUtils.isEmpty(reason)) {
            object.put("Reason", reason);
        }
        if (silence!=null){
            object.put("Silence",silence);
        }
        return object.toJSONString();
    }

    /**
     * 群发消息
     * @param groupId 群组id
     * @param fromAccount 群组成员（发送者）
     * @param random  消息随机数,由随机函数产生。（用作消息去重）
     * @param msgBodyContent 消息体
     * @return
     */
    public static  String sendGroupMsgJsonString(String groupId,String fromAccount,Integer random,JSONArray msgBodyContent){
        JSONObject object = new JSONObject();
        JSONObject offlineInfo = new JSONObject();
        if (StringUtils.isEmpty(groupId)){
            return null;
        }
        if (StringUtils.isEmpty(fromAccount)){
            return null;
        }
        if (random==null){
            return null;
        }
        offlineInfo.put("PushFlag", 1);
        object.put("GroupId",groupId);
        object.put("From_Account",fromAccount);
        object.put("OnlineOnlyFlag",0);
        object.put("Random",random);
        object.put("MsgBody",msgBodyContent);
        object.put("OfflinePushInfo",offlineInfo);
        return object.toJSONString();
    }
    public static void main(String[] orgs){
        //
//        JSONArray array = new JSONArray();
//        array.add(getTextJsonMsg("nihaocaishi"));
//        array.add(getLocationJsonMsg("未知位置",29.340656774469956,116.77497920478824));
//        System.out.println(singleMsg(1,"admin","test",1233,array));
//        System.out.println(getLocationJsonMsg("你好",29.340656774469956,116.77497920478824));
        //
//        JSONObject object = new JSONObject();
//        String[] s = {"admin","test"};
//        object.put("To_Account",s);
        List<String> list = new ArrayList<String>();
        list.add("test1");
        list.add("test");
        System.out.println(removeGroupMemberJsonString("myJavaGroupTest",list,1,""));
//        System.out.println(object);
    }

    /**
     * 文本消息 jsonObject 拼接
     * @param text 文本内容
     * @return
     */
    public static JSONObject getTextJsonMsg(String text){
        JSONObject textObject = new JSONObject();
        textObject.put("MsgType",Constant.msgType.TIMTextElem);
        JSONObject textContent = new JSONObject();
        textContent.put("Text",text);
        textObject.put("MsgContent",textContent);
        return textObject;
    }

    /**
     * 位置消息 jsonObject 拼接
     * @param desc 位置解析
     * @param locX 经度
     * @param locY 纬度
     * @return JSONObject
     */
    public static JSONObject getLocationJsonMsg(String desc,Double locX,Double locY){
        JSONObject locationObject = new JSONObject();
        locationObject.put("MsgType",Constant.msgType.TIMLocationElem);
        JSONObject locationJson = new JSONObject();
        locationJson.put("Desc",desc);
        locationJson.put("Latitude",locX);
        locationJson.put("Longitude",locY);
        locationObject.put("MsgContent",locationJson);
        return locationObject;
    }

    /**
     *发送群消息
     * @param groupId 群主ID
     * @param random 随机数
     * @param fromAccount 接收账号
     * @param msgBodyContent 消息体
     * @return
     */
    public static String groupMsg(String groupId,Integer random,String fromAccount,JSONArray msgBodyContent){
        JSONObject object = new JSONObject();
        object.put("From_Account",fromAccount);
        object.put("Random",random);
        JSONObject msgBody = new JSONObject();
        object.put("MsgBody",msgBody);
        object.put("From_Account",fromAccount);
        return "";
    }
}
