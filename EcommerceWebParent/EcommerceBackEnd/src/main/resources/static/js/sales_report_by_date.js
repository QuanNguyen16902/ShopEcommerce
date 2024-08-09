//Sales report by date
var data
var chartOptions;
var totalGrossSales;
var totalNetSales;
var totalOrders;


$(document).ready(function() {
	 setupButtonEventHandlers("_date", loadSalesReportByDate);
});

function loadSalesReportByDate(period) {
	if (period == "custom") {
		startDate = $("#startDate_date").val();
		endDate = $("#endDate_date").val();
		requestURL = contextPath + "reports/sales_by_date/" + startDate + "/" + endDate;
	} else {
		requestURL = contextPath + "reports/sales_by_date/" + period;
	}
	
	$.get(requestURL, function(responseJson) {
		prepareChartData(responseJson);
		customizeChart(period);
		formatChartData(data, 1, 2);
		drawChart(period);
		setSalesAmount(period ,"_date", "Total Orders");
	});
}
function prepareChartData(responseJson) {
	data = new google.visualization.DataTable();
	data.addColumn('string', 'Date');
	data.addColumn('number', 'Gross Sales');
	data.addColumn('number', 'Net Sales');
	data.addColumn('number', 'Orders');

	totalGrossSales = 0.0;
	totalNetSales = 0.0;
	totalOrders = 0.0;

	$.each(responseJson, function(index, reportItem) {
		data.addRows([[reportItem.identifier, reportItem.grossSales, reportItem.netSales, reportItem.ordersCount]]);
		totalGrossSales += parseFloat(reportItem.grossSales);
		totalNetSales += parseFloat(reportItem.netSales);
		totalOrders += parseFloat(reportItem.ordersCount);

	});
}
function customizeChart(period) {
	chartOptions = {
		title: getChartTitle(period),
		'height': 360,
		legend: { position: 'top' },

		series: {
			0: { targetAxisIndex: 0 },
			1: { targetAxisIndex: 0 },
			2: { targetAxisIndex: 1 }
		},

		vAxes: {
			0: { title: 'Sales Amount', format: 'currency' },
			1: { title: 'Numbers of Orders' }
		}
	};

	
}
function drawChart() {
	var salesChart = new google.visualization.ColumnChart(document.getElementById('chart_sales_by_date'));
	salesChart.draw(data, chartOptions);

	
}

