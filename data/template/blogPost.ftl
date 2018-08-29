<#--
	newdate : [NEW] 표시 기준일
	today 	: 오늘일자 (YYYY-MM-DD)
	appendCategory : 카테고리 정보 표시여부
	couponList : 추출 된 쿠폰리스트
-->
<#assign preCategory="">

<#-- Header Start -->
<br/>
<p><b>기어베스트 쿠폰 리스트</b></p>
<br/>
<p><b>주의사항</b></p>
<p> - 쿠폰은 선착순이므로 만료되었을 수 도 있습니다.</p>
<p> - 한국에서 적용이 되지 않는 쿠폰이 있을 수 도 있습니다.</p>
<p> - 시작일자 / 종료일자는 UTC기준입니다. (한국시간은 9시간을 더해주세요.)</p>
<p> - 상품이 많기 때문에 원하시는 상품 <span style="color: rgb(255, 94, 0);"><b>[Ctrl+F]</b></span>로&nbsp;찾아보세요.</p>
<p> - 최근 3일이내 등록된 쿠폰은&nbsp;<b><span style="color: rgb(255, 0, 0);">[<span style="color: rgb(255, 187, 0);">N</span>E<span style="color: rgb(31, 218, 17);">W</span></span></b><span style="color: rgb(255, 0, 0);"><b>]</b>&nbsp;</span>표시</p>
<p> - 업데이트 일자 : ${today}</p>
<br/><br/>
<#-- Header end -->

<#-- Body Start -->
<#if couponList?has_content>
<#list couponList as item>
<#if item.imageUrl?exists && item.imageUrl !="">
<#if item.category != preCategory && appendCategory>
<blockquote>
	<p><b><span style="font-size:24pt;">${item.category}</span></b></p>
</blockquote>
<br/>
</#if>
<#if item.imageUrl?exists && item.imageUrl !="">
<p style="text-align: left; clear: none; float: none;">
	<img src="${item.imageUrl}" height="150" width="150" title="${item.couponName}">
</p>
</#if>
<p><b>${item.productName}</b></p>
<#if item.origPrice?exists && item.origPrice!="">
<p>기본가격 : ${item.origPrice}</p>
</#if>
<#if item.couponPrice?exists && item.couponPrice!="">
<p>쿠폰가격 : <b><span style="color: rgb(255, 0, 0);">${item.couponPrice}</span></b></p>
<#else>
<p>쿠폰가격 : <b><span style="color: rgb(255, 0, 0);">??????</span></b></p>
</#if>
<#if item.couponCode?exists && item.couponCode!="">
<p>쿠폰코드 : <b><span style="color: rgb(255, 94, 0);">${item.couponCode}</span></b></p>
</#if>
<#if item.startTime?exists && item.startTime!="">
<#if newdate?number gte item.compareDate?number>
<p>시작일자 : <b>${item.startTime?replace("00:00:00","")} </b><b><span style="color: rgb(255, 0, 0);">[<span style="color: rgb(255, 187, 0);">N</span>E<span style="color: rgb(31, 218, 17);">W</span></span></b><span style="color: rgb(255, 0, 0);"><b>]</b></span></p>
<#else>
<p>시작일자 : <b>${item.startTime?replace("00:00:00","")} </b></p>
</#if>
</#if>
<#if item.endTime?exists && item.endTime!="">
<p>종료일자 : ${item.endTime?replace("00:00:00","")}</p>
</#if>
<#if item.promotionUrl?exists && item.promotionUrl!="">
<p>상품보기 : <a href="${item.promotionUrl}" target="_blank">${item.couponUrl}</a></p>
</#if>
<br/>
<br/>
<#assign preCategory=item.category>
</#if>
</#list>
</#if>
<#-- Body End -->

<#-- Footer Start -->
<p style="text-align: center; " align="center">
	<span style="font-size: 12pt;">기어베스트 한국 공식 블로그</span>
</p>
<#-- Footer End -->