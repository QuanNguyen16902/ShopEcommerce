var dropDownCountries;
var dropDownStates;

$(document).ready(function(){
	dropDownCountries = $("#country");
	dropDownStates = $("#listStates");
	
	dropDownCountries.on("change", function(){
		loadStates4Country();
		$("#state").val("").focus();
	});
	
	loadStates4Country();
});

function loadStates4Country(){
	let selectedCountry = $("#country option:selected");
	countryId = selectedCountry.val();
	url = contextPath + "states/list_by_country/" + countryId;
	
	$.get(url, function(responseJSON){
		dropDownStates.empty();
		
		$.each(responseJSON, function(index, state){
			$("<option>").val(state.name).text(state.name).appendTo(dropDownStates);
		});	
	}).fail(function(){
		showErrorModal("ERROR Loading states/provinces for the selected country ");
	});
}