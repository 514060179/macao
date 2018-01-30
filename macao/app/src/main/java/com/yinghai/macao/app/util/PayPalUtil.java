package com.yinghai.macao.app.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;

import static com.sun.xml.internal.ws.spi.db.BindingContextFactory.LOGGER;
import static com.yinghai.macao.app.util.SampleConstants.clientID;
import static com.yinghai.macao.app.util.SampleConstants.clientSecret;
import static com.yinghai.macao.app.util.SampleConstants.mode;

public class PayPalUtil {
	static String clientId = "AUmf6cX5ji-r-4Pq_Bgks-ZSClNmiM3_OEDdyCO0CFwGagI8_LprG40jQQbHNp5sUAlKQv9u2Aakg8vm";
	static String clientSecret = "EFFKGwJGLndP6Eko5WzYquqF5HvSvZ2VVxVC_WU-VJeW2ZV0ZkX3Dt3ZIRMsu39PWRdwwzfhqsj2o-_l";
public static void test(){
	//替换您的应用程序客户端ID和密码
		//字符串clientId =“您的应用程序客户端ID”; 

		
		APIContext context = new APIContext(clientId, clientSecret, "sandbox");
		
		
		// Set payer details
		Payer payer = new Payer();
		payer.setPaymentMethod("paypal");

		// Set redirect URLs
		RedirectUrls redirectUrls = new RedirectUrls();
		
		redirectUrls.setCancelUrl("http://localhost:3000/cancel");
		redirectUrls.setReturnUrl("http://localhost:3000/process");

		// Set payment details
		Details details = new Details();
		details.setShipping("1");
		details.setSubtotal("5");
		details.setTax("1");

		// Payment amount
		Amount amount = new Amount();
		amount.setCurrency("USD");
		// Total must be equal to sum of shipping, tax and subtotal.
		amount.setTotal("7");
		amount.setDetails(details);

		// Transaction information
		Transaction transaction = new Transaction();
		transaction.setAmount(amount);
		transaction.setDescription("This is the payment transaction description.");

		// Add transaction to a list
		List<Transaction> transactions = new ArrayList<Transaction>();
		transactions.add(transaction);

		// Add payment details
		Payment payment = new Payment();
		payment.setIntent("sale");
		payment.setPayer(payer);
		payment.setRedirectUrls(redirectUrls);
		payment.setTransactions(transactions);
		
		
		
		// Create payment 初始化付款并重定向用户
		try {
		  Payment createdPayment = payment.create(context);

		  Iterator<Links> links = createdPayment.getLinks().iterator();
		  while (links.hasNext()) {
		    Links link = links.next();
		    if (link.getRel().equalsIgnoreCase("approval_url")) {
		      // REDIRECT USER TO link.getHref()
		    }
		  }
		} catch (PayPalRESTException e) {
		    System.err.println(e.getDetails());
		}
		
		
		//完成付款
		Payment payment1 = new Payment();
//		payment1.setId(req.getParameter("paymentId"));
		PaymentExecution paymentExecution = new PaymentExecution();
//		paymentExecution.setPayerId(req.getParameter("PayerID"));
		try {
		  Payment createdPayment = payment.execute(context, paymentExecution);
		  System.out.println(createdPayment);
		} catch (PayPalRESTException e) {
		  System.err.println(e.getDetails());
		}
		//退款
		Refund refund = new Refund();
		Amount a = new Amount(); 
		a.setTotal("1.3"); 
		a.setCurrency("USD"); 
		refund.setAmount(a);
		//执行退款
		Sale sale = new Sale();
		sale.setId("7DY409201T7922549");

		try {
		  // Refund sale
		  sale.refund(context, refund);
		} catch (PayPalRESTException e) {
		  System.err.println(e.getDetails());
		}

}

	/**
	 * 创建支付 只支持港币
	 * @return
	 */
	public static String createPayment(String rebackUrl,String cancelUrl,String totalMoney,String mode){
		Payer payer = new Payer();
		payer.setPaymentMethod("paypal");

// Set redirect URLs
		RedirectUrls redirectUrls = new RedirectUrls();
		redirectUrls.setCancelUrl(cancelUrl);
		redirectUrls.setReturnUrl(rebackUrl);

// Set payment details
		Details details = new Details();
		details.setShipping("0");
		details.setSubtotal(totalMoney);
		details.setTax("0");

// Payment amount
		Amount amount = new Amount();
		amount.setCurrency("HKD");
// Total must be equal to sum of shipping, tax and subtotal.
		amount.setTotal(totalMoney);
		amount.setDetails(details);

// Transaction information
		Transaction transaction = new Transaction();
		transaction.setAmount(amount);
		transaction
				.setDescription("This is the payment transaction description.");

// Add transaction to a list
		List<Transaction> transactions = new ArrayList<Transaction>();
		transactions.add(transaction);

// Add payment details
		Payment payment = new Payment();
		payment.setIntent("sale");
		payment.setPayer(payer);
		payment.setRedirectUrls(redirectUrls);
		payment.setTransactions(transactions);

		APIContext context = new APIContext(clientId, clientSecret, mode);
		// Create payment
		Payment createdPayment = null;
		try {
			createdPayment = payment.create(context);

			Iterator links = createdPayment.getLinks().iterator();
			while (links.hasNext()) {
				Links link = (Links)links.next();
				if (link.getRel().equalsIgnoreCase("approval_url")) {
					// REDIRECT USER TO link.getHref()
						String url = link.getHref();
						return url;
				}
			}
		} catch (PayPalRESTException e) {
			e.printStackTrace();
			System.err.println(e.getDetails());
			return "";
		}
		return "";
	}

	/**
	 * 退款
	 */
	public static boolean reback(String total,String saleId,String mode){
		APIContext context = new APIContext(clientId, clientSecret, mode);
		//退款
		Refund refund = new Refund();
		Amount a = new Amount();
		a.setTotal(total);
		a.setCurrency("HKD");
		refund.setAmount(a);
		//执行退款
		Sale sale = new Sale();
		sale.setId(saleId);

		try {
			// Refund sale
			Refund r = new Refund();
			r = sale.refund(context, refund);
		} catch (PayPalRESTException e) {
			e.printStackTrace();
			System.err.println(e.getDetails());
		}
		return false;
	}

	public static void main(String[] args) {
//		new PayPalUtil().paymentDetail();
	}
}
