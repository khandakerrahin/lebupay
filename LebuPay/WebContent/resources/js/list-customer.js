$('#myTable').on('click', 'a.edit', function (event) {
	event.preventDefault();
    var closestRow = $(this).closest('tr');
    var dataTarget = $(this).attr("data-target");
    var id = closestRow.find('td:nth-child(2)').html();
    window.location = $(this).attr("data-url")+"?"+dataTarget+"="+id;
});

$('#myTable').on('click', 'a.remove', function (event) {
	event.preventDefault();
    var closestRow = $(this).closest('tr');
    var dataTarget = $(this).attr("data-target");
    var id = closestRow.find('td:nth-child(2)').html();
    
    $('#confirm').find('form').attr('action', $(this).attr("data-url"));
    $('#confirm').find('form').find('input[type="hidden"]').attr('name', dataTarget).val(id);
    $('#confirm').modal('show');
});