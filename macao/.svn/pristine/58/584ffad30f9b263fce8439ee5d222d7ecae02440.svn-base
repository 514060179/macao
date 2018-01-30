package com.yinghai.macao.common.constant;


import com.yinghai.macao.common.util.PropertyUtil;

/**
 * Created by Administrator on 2017/2/15.
 */
public class Constant {
	/**
	 *  进行推送的url，需要附带参数
	 *  完整写法示例 ：https://console.tim.qq.com/v4/openim/im_push?usersig=xxx&identifier=admin&sdkappid=88888888&random=99999999&contenttype=json
	 */
	public static final String im_push_url="https://console.tim.qq.com/v4/openim/im_push";
	/**
	 *  获取推送结果的url，需要附带参数
	 *  完整写法示例：https://console.tim.qq.com/v4/openim/im_get_push_report?usersig=xxx&identifier=admin&sdkappid=88888888&random=99999999&contenttype=json
	 */
	public static final String im_get_push_report_url="https://console.tim.qq.com/v4/openim/im_get_push_report";
	/**
	 *  向im导入账号的url，需要附带参数
	 *  完整写法示例：https://console.tim.qq.com/v4/im_open_login_svc/account_import?usersig=xxx&identifier=admin&sdkappid=88888888&random=99999999&contenttype=json
	 */
	public static final String im_account_import_url="https://console.tim.qq.com/v4/im_open_login_svc/account_import";
	/**
	 * 查询用户在线状态的url，需要附带参数
	 * 完整写法示例：https://console.tim.qq.com/v4/openim/querystate?usersig=xxx&identifier=admin&sdkappid=88888888&random=99999999&contenttype=json
	 */
	public static final String querystate_url="https://console.tim.qq.com/v4/openim/querystate";
	/**
	 * 添加用户标签的url,需要附带参数
	 * 完整写法示例：https://console.tim.qq.com/v4/openim/im_add_tag?usersig=xxx&identifier=admin&sdkappid=88888888&random=99999999&contenttype=json
	 */
	public static final String im_add_tag_url="https://console.tim.qq.com/v4/openim/im_add_tag";
	/**
	 * 获取用户标签的url，需要附带参数
	 * 完整写法示例：https://console.tim.qq.com/v4/openim/im_get_tag?usersig=xxx&identifier=admin&sdkappid=88888888&random=99999999&contenttype=json
	 */
	public static final String im_get_tag_url="https://console.tim.qq.com/v4/openim/im_get_tag";
	/**
	 * 微信支付的url
	 */
	public static final String wechat_pay_url="https://api.mch.weixin.qq.com/pay/unifiedorder";

	/**
	 * 关闭订单的URL
	 */
	public static final String wechat_CLOSE_url = "https://api.mch.weixin.qq.com/pay/closeorder";
	/**
	 * 申请退款的URL
	 */
	public static final String wechat_refund_url = "https://api.mch.weixin.qq.com/secapi/pay/refund";
	/**
	 * 查询退款的URL
	 */
	public static final String wechat_refundquery_url = "https://api.mch.weixin.qq.com/pay/refundquery";


	/**
	 * 单聊消息类型
	 * TIMTextElem(文本消息)，TIMFaceElem(表情消息)，TIMLocationElem(位置消息)，TIMCustomElem(自定义消息)。
	 */
	public enum msgType {
		TIMTextElem,TIMFaceElem,TIMLocationElem,TIMCustomElem
	};

	/**
	 * 群组形态，包括Public（公开群），Private（私密群），ChatRoom（聊天室），AVChatRoom（互动直播聊天室）
	 * 私有群（Private）：适用于较为私密的聊天场景，群组资料不公开，只能通过邀请的方式加入，类似于微信群。
	 * 公开群（Public）：适用于公开群组，具有较为严格的管理机制、准入机制，类似于QQ群。
	 * 聊天室（ChatRoom）：群成员可以随意进出，组织较为松散。
	 * 互动直播聊天室（AVChatRoom）：适用于互动直播场景，管理上与聊天室相似，但群成员人数无上限；支持以游客身份（不登录）接收消息。
	 */
	public enum groupType {
		Public,Private,ChatRoom,AVChatRoom
	};
	/**
	 * 申请加群处理方式
	 * 包含FreeAccess（自由加入），NeedPermission（需要验证），DisableApply（禁止加群）
	 */
	public enum applyJoinOption {
		FreeAccess,NeedPermission,DisableApply
	};
	/**
	 * 云通信单聊请求url
	 */
	public static final String SINGLE_MSG_URL = "https://console.tim.qq.com/v4/openim/sendmsg";
	/**
	 *云通信多聊请求url
	 */
	public static final String MULTIPLE_MSG_URL = "https://console.tim.qq.com/v4/openim/batchsendmsg";
	/**
	 *云通信 建群请求url
	 */
	public static final String CREATE_GROUP_URL = "https://console.tim.qq.com/v4/group_open_http_svc/create_group";

	/**
	 *云通信 加入群聊请求url
	 */
	public static final String ADD_GROUP_URL = "https://console.tim.qq.com/v4/group_open_http_svc/add_group_member";
	/**
	 *云通信 删除群组成员请求url
	 */
	public static final String DEELETE_GROUP_MEMBER_URL = "https://console.tim.qq.com/v4/group_open_http_svc/delete_group_member";
	/**
	 *云通信 发送群组消息请求url
	 */
	public static final String SEND_GROUP_MSG_URL = "https://console.tim.qq.com/v4/group_open_http_svc/send_group_msg";
	/**
	 *云通信 发送云短信请求url
	 */
	public static final String SEND_SMS_MSG_URL = "https://yun.tim.qq.com/v5/tlssmssvr/sendsms";
	/**
	 *云通信 发送云短信请求url
	 */
	public static final String SEND_GROUP_SMS_MSG_URL = "https://yun.tim.qq.com/v5/tlssmssvr/sendmultisms2";
	
	/**
	 *获取用户在线状态
	 */
	public static final String QUERY_STATUS_SMS_MSG_URL= "https://console.tim.qq.com/v4/openim/querystate";

	/**
	 * 微信訂單查詢的URL
	 */
	public static final String wechat_QUERY_url = "https://api.mch.weixin.qq.com/pay/orderquery";
	/**
	 * 云通信应用ID
	 */
	public static final String sdkAppId = PropertyUtil.getAppProperty("sdkAppId");
	/**
	 * 云通信独立模式管理员账号
	 */
	public static final String manager = PropertyUtil.getAppProperty("identifier");
	/**
	 * 云通信发送短信key
	 */
	public static final String appkey = PropertyUtil.getAppProperty("appkey");
	/**
	 * 云通信发送短信签名
	 */
	public static final String sig = PropertyUtil.getAppProperty("sig");
	/**
	 * 云通信发送短信模板
	 */
	public static final String splId = PropertyUtil.getAppProperty("splId");
	/**
	 * 云通信群组ID
	 */
	public static final String groupId = PropertyUtil.getAppProperty("GroupId");

	/**
	 * 微信支付APPId(应用ID)APP端
	 */
	public static final String weixinPayAppId = PropertyUtil.getAppProperty("weixinPayAppId");
	/**
	 * 微信支付商户号，微信支付分配的商户号APP
	 */
	public static final String mchId = PropertyUtil.getAppProperty("mch_id");
	/**
	 * 微信APP商店key
	 */
	public static final String weixinPayKey = PropertyUtil.getAppProperty("weixinPayKey");

	public static final String WEBCHAT_PAY_CALL_BACK = PropertyUtil.getAppProperty("weixinCallBackUrl");
	
	/**
	 * 微信支付APPId(应用ID)公众号
	 */
	public static final String JSAPIAppId = PropertyUtil.getAppProperty("JSAPIAppId");
	/**
	 * 微信支付商户号，微信支付分配的商户号（公众号）
	 */
	public static final String JSAPImchId = PropertyUtil.getAppProperty("JSAPImch_id");
	
	/**
	 * 微信公众号商店key
	 */
	public static final String JSAPIkey = PropertyUtil.getAppProperty("JSAPIkey");
	
	/**
	 * 微信支付APPId(应用ID)公众号(去澳门)
	 */
	public static final String JSAPIAppId2 = PropertyUtil.getAppProperty("JSAPIAppId2");
	/**
	 * 微信支付商户号，微信支付分配的商户号（公众号）(去澳门)
	 */
	public static final String JSAPImchId2 = PropertyUtil.getAppProperty("JSAPImch_id2");
	
	/**
	 * 微信公众号商店key(去澳门)
	 */
	public static final String JSAPIkey2 = PropertyUtil.getAppProperty("JSAPIkey2");
	
	/**
	 * 微信支付类型APP
	 */
	public static final String weixinAPPPayType = "APP";
	/**
	 * 微信支付类型JSAP
	 */
	public static final String weixinJSAPPayType = "JSAPI";
	
	/**
	 * 微信支付类型JSAP(去澳门)
	 */
	public static final String weixinJSAPPayType2 = "JSAPI2";
	
	/**
	 * 微信支付类型JSAP
	 */
	public static final String PaypalPayType = "paypal";
	/**
	 * 微信APP退款
	 */
	public static final String apiclientCert = PropertyUtil.getAppProperty("apiclient_cert");
	
	/**
	 * 微信公眾號退款
	 */
	public static final String apiclientCertJsapi = PropertyUtil.getAppProperty("apiclient_certjsapi");
	
	/**
	 * 微信公眾號退款(去澳门)
	 */
	public static final String apiclientCertJsapi2 = PropertyUtil.getAppProperty("apiclient_certjsapi2");
	
	/**
	 * paypal支付
	 */
	public static final String paypalURL = "https://www.sandbox.paypal.com/cgi-bin/webscr";
	/**
	 * paypal 支付成功返回url
	 */
	public static final String paypalReturnUrl = PropertyUtil.getAppProperty("paypalReturnUrl");
	/**
	 * paypal 支付返回url
	 */
	public static final String paypalCancelUrl = PropertyUtil.getAppProperty("paypalCancelUrl");
	/**
	 * paypal账户
	 */
	public static final String clientId = PropertyUtil.getAppProperty("clientId");
	/**
	 * paypal密码
	 */
	public static final String clientSecret = PropertyUtil.getAppProperty("clientSecret");

	/**
	 * paypal密码
	 */
	public static final String mode = PropertyUtil.getAppProperty("mode");

	/**
	 * 微信公众号用户提醒url
	 */
	public static final String webchatRemind = PropertyUtil.getAppProperty("webchatRemind");
	/**
	 * 微信公众号用户提醒url(去澳门)
	 */
	public static final String webchatRemind2 = PropertyUtil.getAppProperty("webchatRemind2");

	/**
	 * 云通信司机群组ID
	 */
	public static final String DriverGroupId = PropertyUtil.getAppProperty("DriverGroupId");
	/**
	 * 云通信乘客群组ID
	 */
	public static final String passengerGroupId = PropertyUtil.getAppProperty("passengerGroupId");
	/**
	 * 司机标签
	 */
	public static final String driver = "Driver";
	/**
	 * 乘客标签
	 */
	public static final String passenger = "Passenger";
	/**
	 * 管理群组ID
	 */
	public static final String managerGroupId = PropertyUtil.getAppProperty("managerGroupId");
	
	/**
	 * 所有文件的提交路径
	 */
	public static final String filepath = PropertyUtil.getAppProperty("filepath");
	
	/**
	 * 專車圖片相对路径
	 */
	public static final String spcarimage = PropertyUtil.getAppProperty("spcarimage");
	
	/**
	 * 司机圖片相对路径
	 */
	public static final String spcardriverimage = PropertyUtil.getAppProperty("spcardriverimage");
	/**
	 * poi圖片相對路徑
	 */
	public static final String poiimage = PropertyUtil.getAppProperty("poiimage");
	/**
	 * 后台新增taxigo乘客接口URL
	 */
	public static final String ADD_TAXIGO_USER = PropertyUtil.getAppProperty("addTaxigoUser");
	
	/**
	 * 微信APPID司机登录
	 */
	public static final String APPID_LOGIN = PropertyUtil.getAppProperty("appid");
	
	/**
	 * 微信登录AppSecret
	 */
	public static final String APPSECRET = PropertyUtil.getAppProperty("appsecret");
	
	/**
	 * 微信APPID乘客登录
	 */
	public static final String APPID_PASSENGER = PropertyUtil.getAppProperty("passengerappid");
	
	/**
	 * 微信登录AppSecret
	 */
	public static final String APPSECRET_PASSENGER = PropertyUtil.getAppProperty("passengerappsecret");
	
	/**
	 * 微信登录类型
	 */
	public static final String WX_LOGIN = "1";
	
	
}
