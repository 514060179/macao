package com.yinghai.macao.freemarker;

import org.springframework.web.servlet.view.freemarker.FreeMarkerView;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class RichFreeMarkerView extends FreeMarkerView{
	/**
	 * 部署路径属性名称
	 */
	public static final String CONTEXT_PATH = "base";
	public static final String LABLE = "lable";
	/**
	 * 在model中增加部署路径base，方便处理部署路径问题。
	 */
	@Override
	@SuppressWarnings("unchecked")
	protected void exposeHelpers(Map model, HttpServletRequest request)
			throws Exception {
        super.exposeHelpers(model, request);
        String requestPath = request.getRequestURI();
        String[] lable =  requestPath.split("\\/");
        int lableIndex = lable.length-2;
        model.put(LABLE, lable[lableIndex]);
        model.put(CONTEXT_PATH, request.getContextPath());
    }
}

