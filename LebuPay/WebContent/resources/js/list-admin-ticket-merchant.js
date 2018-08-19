$('#myTable').on('click', 'a.reply', function (event) {
	event.preventDefault();
    var closestRow = $(this).closest('tr');
    var dataTarget = $(this).attr("data-target");
    var id = closestRow.find('td:nth-child(2)').html();
    window.location = $(this).attr("data-url")+"?"+dataTarget+"="+id;
});