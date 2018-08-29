<#if excelList?has_content>
<#list excelList as item>
<#if isImgTag>

<img src="${item.imgUrl}" alt="${item.title}"/>
</#if>
✅ ${item.title}
► ${item.url}
<#if item.coupon??>
★ 할인코드 "${item.coupon}" 적용 가격 ${item.price} <#if item.count??>(한정 ${item.count}개)</#if>
<#else>
★ 플래시 세일 "${item.price}" <#if item.count??>(한정 ${item.count}개)</#if>
</#if>
<#if item.strDtm?has_content&&item.endDtm?has_content>
기간 : ${item.strDtm} 부터 ${item.endDtm} 까지
<#elseif item.strDtm?has_content>
기간 : ${item.strDtm} 부터
<#elseif item.endDtm?has_content>
기간 : ${item.endDtm} 까지
</#if>
</#list>
</#if>