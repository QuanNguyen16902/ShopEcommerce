//Sales report by Category
var data
var chartOptions;
 


$(document).ready(function() {
	 setupButtonEventHandlers("_category", loadSalesReportByDateForCategory);
});

function loadSalesReportByDateForCategory(period) {
	if (period == "custom") {
		startDate = $("#startDate_date").val();
		endDate = $("#endDate_date").val();
		requestURL = contextPath + "reports/category/" + startDate + "/" + endDate;
	} else {
		requestURL = contextPath + "reports/category/" + period;
	}
	
	$.get(requestURL, function(responseJson) {
		prepareChartDataByCategory(responseJson);
		customizeChartByCategory();
		formatChartData(data, 1, 2);
		drawChartByCategory(period);
		setSalesAmount(period ,"_category", "Total Products");
	});
}
function prepareChartDataByCategory(responseJson) {
	data = new google.visualization.DataTable();
	data.addColumn('string', 'Category');
	data.addColumn('number', 'Gross Sales');
	data.addColumn('number', 'Net Sales');

	totalGrossSales = 0.0;
	totalNetSales = 0.0;
	totalItems = 0.0;

	$.each(responseJson, function(index, reportItem) {
		data.addRows([[reportItem.identifier, reportItem.grossSales, reportItem.netSales]]);
		totalGrossSales += parseFloat(reportItem.grossSales);
		totalNetSales += parseFloat(reportItem.netSales);
		totalItems += parseFloat(reportItem.productsCount);

	});
}
function customizeChartByCategory() {
	chartOptions = {
		height: 360,
		legend: { position: 'right' },
	};

	
}
function drawChartByCategory() {
	var salesChart = new google.visualization.PieChart(document.getElementById('chart_sales_by_category'));
	salesChart.draw(data, chartOptions);

	
}

