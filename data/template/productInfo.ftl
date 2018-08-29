<h1 style="text-align: center; ">${title}</h1>
<br>
<div style="text-align: center;" align="center">
	<img src="${imgUrl}" alt="${title}" title="${title}">
</div>
<p style="text-align: center;" align="center">
	<i><span style="color: rgb(255, 108, 0);">${summary}</span></i>
</p>
<br>
<p style="text-align: center; font-size: 15pt;" align="center">
세일가격 : <b>${origPrice}</b>
</p>
<p style="text-align: center; font-size: 15pt" align="center"><a href="${productRealLink}" target="_blank">${productLink}</a></p>
<br>

<#if description??>
${description}
<br>
</#if>

<#if youtubeList?has_content>
<#list youtubeList as youtube>
${youtube}
</#list>
</#if>
<br>

<#if imageList?has_content>
<#list imageList as item>
<img src="${item}" alt="${title}"/><br>
</#list>
</#if>
<br>

<p style="text-align: center; font-size: 15pt" align="center"><a href="${productRealLink}" target="_blank">▶ 구매하기 ◀</a></p>
<br>
<p style="text-align: center; " align="center">
	<span style="font-size: 12pt;"><b>기어베스트 한국 공식 블로그</b></span>
</p>