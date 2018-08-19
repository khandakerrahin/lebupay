/**
 * @author Suvankar Sen <suvankar.sen@indusnet.co.in>
 */


$(document).ready(function() {
	//
	// Pipelining function for DataTables. To be used to the `ajax` option of DataTables
	//
	$.fn.dataTable.pipeline = function ( opts ) {
	    // Configuration options
	    var conf = $.extend( {pages: 5}, opts );
	 
	    // Private variables for storing the cache
	    var cacheLower = -1;
	    var cacheUpper = null;
	    var cacheLastRequest = null;
	    var cacheLastJson = null;
	 
	    return function ( request, drawCallback, settings ) {
	        var ajax          = false;
	        var requestStart  = request.start;
	        var drawStart     = request.start;
	        var requestLength = request.length;
	        var requestEnd    = requestStart + requestLength;
	        //alert(request.search.value);
           // alert(request.date_from.value); 
	        if ( settings.clearCache ) {
	            // API requested that the cache be cleared
	            ajax = true;
	            settings.clearCache = false;
	        }
	        else if ( cacheLower < 0 || requestStart < cacheLower || requestEnd > cacheUpper ) {
	            // outside cached data - need to make a request
	            ajax = true;
	        }
	        else if ( JSON.stringify( request.order )   !== JSON.stringify( cacheLastRequest.order ) ||
	                  JSON.stringify( request.columns ) !== JSON.stringify( cacheLastRequest.columns ) ||
	                  JSON.stringify( request.search )  !== JSON.stringify( cacheLastRequest.search )  
	                 
	        ) {
	            // properties changed (ordering, columns, searching)
	            ajax = true;
	        }
	         
	        // Store the request for checking next time around
	        cacheLastRequest = $.extend( true, {}, request );
	 
	        if ( ajax ) {
	            // Need data from the server
	            if ( requestStart < cacheLower ) {
	                requestStart = requestStart - (requestLength*(conf.pages-1));
	 
	                if ( requestStart < 0 ) {
	                    requestStart = 0;
	                }
	            }
	             
	            cacheLower = requestStart;
	            cacheUpper = requestStart + (requestLength * conf.pages);
	 
	            request.start = requestStart;
	            request.length = requestLength*conf.pages;
	 
	            // Provide the same `data` options as DataTables.
	            if ( $.isFunction ( conf.data ) ) {
	                // As a function it is executed with the data object as an arg
	                // for manipulation. If an object is returned, it is used as the
	                // data object to submit
	                var d = conf.data( request );
	                if ( d ) {
	                    $.extend( request, d );
	                }
	            }
	            else if ( $.isPlainObject( conf.data ) ) {
	                // As an object, the data given extends the default
	                $.extend( request, conf.data );
	            }
	            
	            settings.jqXHR = $.ajax( {
	                "type":     conf.method,
	                "url":      conf.url,
	                "data":     request,
	                "dataType": "json",
	                "cache":    false,
	                "success":  function ( json ) {
	                    cacheLastJson = $.extend(true, {}, json);
	 
	                    if ( cacheLower != drawStart ) {
	                        json.data.splice( 0, drawStart-cacheLower );
	                    }
	                    if ( requestLength >= -1 ) {
	                        json.data.splice( requestLength, json.data.length );
	                    }
	                     
	                    drawCallback( json );
	                }
	            } );
	        }
	        else {
	            json = $.extend( true, {}, cacheLastJson );
	            json.draw = request.draw; // Update the echo for each response
	            json.data.splice( 0, requestStart-cacheLower );
	            json.data.splice( requestLength, json.data.length );
	 
	            drawCallback(json);
	        }
	    }
	};
	 
	// Register an API method that will empty the pipelined data, forcing an Ajax
	// fetch on the next draw (i.e. `table.clearPipeline().draw()`)
	$.fn.dataTable.Api.register( 'clearPipeline()', function () {
	    return this.iterator( 'table', function ( settings ) {
	        settings.clearCache = true;
	    } );
	} );
	
	
	drawDataTable = function(domObj, urlConfigObj, columns, columnsVSTable, isButton, autoIncrementColumnIndex, autoIncrementColumnName, noOfDataExistInitially, downloadExcelLink, rowCallback, dom, searchObj, excelObj, lengthOptObj, orderObj){
	//drawDataTable = function(domObj, urlConfigObj, columns, columnsVSTable, isButton, autoIncrementColumnIndex, autoIncrementColumnName, noOfDataExistInitially, downloadExcelLink, rowCallback, dom, searchObj, excelObj, lengthOptObj, orderObj, dateFromObj, dateToObj){
		
		
		var cObj = {
				responsive: true,
				rowReorder: {
		            selector: 'td:nth-child(2)'
		        },
			    /*pageLength: urlConfigObj.noOfRows,*/
		        "lengthMenu": [[10, 25, 50], [10, 25, 50]],
		        "processing": true,
		        "serverSide": true,
		        "pagingType": "full_numbers",
		        "ajax": $.fn.dataTable.pipeline( {
		        	pages: urlConfigObj.noOfPages,     // number of pages to cache
			        url: urlConfigObj.url,      // script url
			        data: function ( d ) {
		                d.orderColumn = d.columns[d.order[0].column].data;
		                d.orderBy = d.order[0].dir;
		                d.searchString = d.search.value;
		               
		                
		                if(typeof columnsVSTable[d.orderColumn] === "undefined"){
		                	d.orderColumn = d.orderColumn.replace(/([A-Z])/g, "_$1")
			                d.orderColumn = d.orderColumn.toLowerCase();
		                }else{
		                	d.orderColumn = columnsVSTable[d.orderColumn];
		                }
		            },
			        method: urlConfigObj.method // Ajax HTTP method
		        } )
		    };
		
		if(typeof noOfDataExistInitially !== "undefined" && noOfDataExistInitially != null && !isNaN(noOfDataExistInitially)){
			cObj.deferLoading = noOfDataExistInitially;
		}
		
		if(typeof autoIncrementColumnIndex !== "undefined" && autoIncrementColumnIndex != null && !isNaN(autoIncrementColumnIndex)){
			if(autoIncrementColumnIndex == 0){
				cObj.order = [[ 1, 'asc' ]];
			}
			else{
				cObj.order = [[ 0, 'asc' ]];
			}
			cObj.columnDefs = [ {
	            "searchable": false,
	            "orderable": false,
	            "targets": autoIncrementColumnIndex
	        } ];
			
			cObj.drawCallback = function( settings ) {
	            var api = this.api();
	            api.column(autoIncrementColumnIndex, {search:'applied', order:'applied'}).nodes().each( function (cell, i) {
		            cell.innerHTML = (api.page()*10)+i+1;
		        } );
	            
	        }
			
			
			if(typeof autoIncrementColumnName !== "undefined" && autoIncrementColumnName != null){
				domObj.find("thead tr").insertAt(autoIncrementColumnIndex, "<th>"+autoIncrementColumnName+"</th>");
				domObj.find("tfoot tr").insertAt(autoIncrementColumnIndex, "<th>"+autoIncrementColumnName+"</th>");
			}
			columns.splice(autoIncrementColumnIndex, 0, { "data": null });
		}
		
		if(typeof orderObj !== "undefined" && orderObj != null){
			cObj.order = orderObj;
		}


		if(typeof rowCallback !== "undefined" && rowCallback != null){
			cObj.rowCallback = function(  row, data, index  ) {
				rowCallback( row, data, index );
			}
		}
		
		if(isButton){
			if(typeof dom === "undefined" || dom == null){
				cObj.dom = "lBfrtip";
			}
			else{
				cObj.dom = dom;
			}
			
			cObj.buttons = [
				            {
				                text: 'Excel',
				                className: 'dt_excel_download',
				                action: function ( e, dt, node, config ) {
				                	var data = dt.ajax.params();
				                	if(typeof data === "undefined"){
				                		dt.rows().ajax.reload()
				                		data = dt.ajax.params();
				                	}
				                	var start = data.start;
				                	var length = data.length;
				                	var orderColumn = data.orderColumn;
				                	if(typeof orderColumn === "undefined"){
				                		orderColumn = data.columns[data.order[0].column].data;
				                	}
				                	
				                	if(typeof columnsVSTable[orderColumn] !== "undefined"){
				                		orderColumn = columnsVSTable[orderColumn];
				                	}
				                	
				                	var orderBy = data.orderBy;
				                	if(typeof orderBy === "undefined"){
				                		orderBy = data.order[0].dir;
				                	}
				                	var searchString = data.searchString;
				                	if(typeof searchString === "undefined"){
				                		searchString = data.search.value;
				                	}
				                	var noOfColumns = data.columns.length;
				                	
				                	var autoIndexQueryStr = "";
				                	if(typeof autoIncrementColumnIndex !== "undefined" && autoIncrementColumnIndex != null && !isNaN(autoIncrementColumnIndex)){
				                		autoIndexQueryStr = "&autoIncrementColumnIndex="+autoIncrementColumnIndex;
				                	}
				                	var columns = new Array();
				                	domObj.find("thead tr th").each(function(){
				                		if(!$(this).hasClass('datatableLinkButton')){
				                			columns.push($(this).html());
				                		}
				                	});
				                	
				                	
				                	if(typeof downloadExcelLink === "undefined" || downloadExcelLink == null){
				                		downloadExcelLink = "downloadExcel"
				                	}
				                	if(downloadExcelLink.indexOf("?") !== -1){
				                		window.location = downloadExcelLink+"&start="+start+"&length="+length+"&orderColumn="+orderColumn+"&orderBy="+orderBy+"&searchString="+searchString+"&noOfColumns="+noOfColumns+"&columnNames="+columns.join(",")+autoIndexQueryStr;
				                	}
				                	else{
				                		window.location = downloadExcelLink+"?start="+start+"&length="+length+"&orderColumn="+orderColumn+"&orderBy="+orderBy+"&searchString="+searchString+"&noOfColumns="+noOfColumns+"&columnNames="+columns.join(",")+autoIndexQueryStr;
				                	}
				                	
				                	
				                }
				            }/*,
				            {
				            	  extend: 'excel', text: 'Excel Visable Part'
				            }*/
				        ];
		}
		else{
			if(typeof dom === "undefined" || dom == null){
				cObj.dom = "lfrtip";
			}
			else{
				cObj.dom = dom;
			}
			
		}
		
		cObj.columns = columns;
		console.log(JSON.stringify(cObj))
		$table = domObj.DataTable(cObj);
		
		if(typeof excelObj !== "undefined" && excelObj != null){
			excelObj.click(function(){
				$table.button('.dt_excel_download').trigger();
			});
		}
		if(typeof searchObj !== "undefined" && searchObj != null){
			searchObj.keyup(function() {
				$table.search(this.value).draw();
		    });
		}
		if(typeof lengthOptObj !== "undefined" && lengthOptObj != null){
			lengthOptObj.change(function(){
				$table.page.len($(this).val()).draw();
			});
		}
		
		$table.on('click', 'a.edit', function (event) {
			event.preventDefault();
	        var closestRow = $(this).closest('tr');
	        var data = $table.row(closestRow).data();
	        var dataTarget = $(this).attr("data-target");
	        var id = data[dataTarget];
	        window.location = $(this).attr("data-url")+"?"+dataTarget+"="+id;
	    });
		
		$table.on('click', 'a.remove', function (event) {
			event.preventDefault();
	        var closestRow = $(this).closest('tr');
	        var data = $table.row(closestRow).data();
	        var dataTarget = $(this).attr("data-target");
	        var id = data[dataTarget];
	        
	        
	        
	        
	        if($('#confirm').length != 0){
	        	$('#confirm').find('form').attr('action', $(this).attr("data-url"));
		        $('#confirm').find('form').find('input[type="hidden"]').attr('name', dataTarget).val(id);
		        $('#confirm').modal('show');
	        }
	        else{
	        	if ( confirm( "Are you sure you want to delete this row?" ) ) {
		        	window.location = $(this).attr("data-url")+"?"+dataTarget+"="+id;
		        }
	        }
	        
	        
	    });
		
		$table.on('click', 'a.reply', function (event){
			event.preventDefault();
			
			var closestRow = $(this).closest('tr');
	        var data = $table.row(closestRow).data();
	        var dataTarget = $(this).attr("data-target");
	        var id = data[dataTarget];
	        window.location = $(this).attr("data-url")+"?"+dataTarget+"="+id;
		});
		
		return $table;
	}
	
});

jQuery.fn.insertAt = function(index, element) {
	  var lastIndex = this.children().length;
	  if (index < 0) {
	    index = Math.max(0, lastIndex + 1 + index);
	  }
	  this.append(element);
	  if (index < lastIndex) {
	    this.children().eq(index).before(this.children().last());
	  }
	  return this;
	}