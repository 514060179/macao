<#macro pager pageNo pageSize recordCount pageCount toURL method>  
  <#-- 定义局部变量pageCount保存总頁数 -->
  <#if recordCount==0><#return/></#if>
<#-- 输出分頁样式 -->
<#-- 頁号越界处理 -->
  <#if (pageNo > pageCount)>
    <#assign pageNo=pageCount>
  </#if>
  <#if (pageNo < 1)>
    <#assign pageNo=1>
  </#if>
<#-- 输出分頁表单 -->
<form method="${method}" action="" name="qPagerForm">
<#-- 把请求中的所有参数当作隐藏表单域(无法解决一个参数对应多个值的情况) -->
    <div class="dataTables_paginate paging_bootstrap" id="simpledatatable_paginate">
        <ul  class="pagination" style="float:right;">
<#list RequestParameters?keys as key>
<#if (key!="page" && RequestParameters[key]??)>
    <li><input type="hidden" name="${key}" value="${RequestParameters[key]}"/></li>
</#if>
</#list>
    <li><input type="hidden" name="page" value="${pageNo}"/>
<span>共${recordCount}条记录，当前第<b>${pageNo}/${pageCount}</b>頁</span></li>
<#-- 上一頁处理 -->
  <#if (pageNo == 1)>
      <li class="prev disabled"><a href="#">上一頁</a></li>
  <#else>
      <li><a href="javascript:void(0)" onclick="turnOverPage(${pageNo - 1})">&laquo;&nbsp;上一頁</a></li>
  </#if>
<#-- 如果前面頁数过多,显示... -->
    <#assign start=1>
    <#if (pageNo > 4)>
    <#assign start=(pageNo - 1)>
<li><a href="javascript:void(0)" onclick="turnOverPage(1)">1</a>
<li><a href="javascript:void(0)" onclick="turnOverPage(2)">2</a></li>
<li><a href="#"">&hellip;</a></li>
    </#if>
<#-- 显示当前頁号和它附近的頁号 -->
    <#assign end=(pageNo + 1)>
    <#if (end > pageCount)>
        <#assign end=pageCount>
    </#if>
  <#list start..end as i>
    <#if (pageNo==i)>
        <li class="active"><a href="#">${i}</a></li>
    <#else>
            <li><a href="javascript:void(0)" onclick="turnOverPage(${i})">${i}</a>    </li>
    </#if>
  </#list>
<#-- 如果后面頁数过多,显示... -->
  <#if (end < pageCount - 2)>
<li><a href="#"">&hellip;</a></li>  
  </#if>
  <#if (end < pageCount - 1)>
      <li><a href="javascript:void(0)" onclick="turnOverPage(${pageCount - 1})">${pageCount-1}</a></li>
  </#if>
  <#if (end < pageCount)>
      <li><a href="javascript:void(0)" onclick="turnOverPage(${pageCount})">${pageCount}</a></li>
  </#if>
<#-- 下一頁处理 -->
  <#if (pageNo == pageCount)>
      <li class="prev disabled"><a href="#">下一頁&nbsp;&raquo;</a></li>
  <#else>
      <li><a href="javascript:void(0)" onclick="turnOverPage(${pageNo + 1})">下一頁&nbsp;&raquo;</a></li>
  </#if>
</ul>
</div>  
<script language="javascript">
  function turnOverPage(no){
    var qForm=document.qPagerForm;
    if(no>${pageCount}){no=${pageCount};}
    if(no<1){no=1;}
    qForm.page.value=no;
    qForm.action="${toURL}";
    qForm.submit();
  }
</script>
</div> 
</#macro>