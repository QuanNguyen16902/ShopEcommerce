$(document).ready(function() {
	
	$("a[name='linkRemoveDetail']").each(function(index){
		$(this).click(function(){
			removeDetailSectionByIndex(index);
		});
	});
	
});

function addNextDetailSection(){
	
	allDivDetails = $("[id^='divDetail']");
	divDetailsCount = allDivDetails.length;
	
	htmlDetailSection = `
		<div class="row g-3" id="divDetail${divDetailsCount}">
		<input type="hidden" name="detailIDs" value="0"/>
			<div class="col-md-3">
				<label for="detailName" class="form-label">Name:</label>
				<input type="text" class="form-control" id="detailName" name="detailNames" maxlength="255">
			</div>
			<div class="col-md-3">
				<label for="detailValue" class="form-label">Value:</label>
				<input type="text" class="form-control" id="detailValue" name="detailValues" maxlength="255">
			</div>
		</div>
	`;
	$("#divProductDetails").append(htmlDetailSection);
	previousDivDetailSection = allDivDetails.last();
	previousDivDetailID = previousDivDetailSection.attr("id");

	htmlLinkRemove =
	 ` <div class=col-md-3>
		<a class="btn fas fa-times-circle fa-2x icon-dark" 
		href="javascript:removeDetailSectionById('${previousDivDetailID}')"
		title="Remove this detail"></a>
		</div>
	`;
		
	previousDivDetailSection.append(htmlLinkRemove);
	$("input[name='detailNames']").last().focus();
}

function removeDetailSectionById(id){
	$("#" + id).remove(); 
}
function removeDetailSectionByIndex(index){
	$("#divDetail" + index).remove();
}