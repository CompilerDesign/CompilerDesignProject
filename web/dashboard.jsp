<%-- 
    Document   : dashboard
    Created on : Feb 4, 2017, 10:03:37 PM
    Author     : jayvee_lugod
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Dashboard</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" type="text/css" href="design/bootstrap/css/bootstrap.min.css">
    <script src="design/bootstrap/js/jquery.min.js"></script>
    <script src="design/bootstrap/js/bootstrap.min.js"></script>
    <script src="design/bootstrap/js/jquery-3.1.1.min.js"></script>
    <script src="design/bootstrap/js/highcharts.js"></script>
    <script src="design/bootstrap/js/exporting.js"></script> 
    <script src="design/bootstrap/js/jquery.tablesorter.min.js"></script> 
    <script src="design/bootstrap/js/jquery.tablesorter.pager.js"></script> 
    
    
   

    <style>
        table {
            font-family: arial, sans-serif;
            border-collapse: collapse;
            width: 100%;
        }
        .headerTable {
            background-color: #dddddd;
        }
        td, th {
            /*border: 1px solid #dddddd;*/
            text-align: left;
            padding: 8px;
        }
        .rowTable:nth-child(even) {
            background-color: #f2f2f2;
        }
        h1, h2, h3, h4, h6 {
            font-family: Lucida Grande;
            color: white;
            text-align: center;
            padding: 6px;
        }

        h1, h2, h3 {            
            font-size: 15px;            
            border-radius: 10px; 
            width: 50%; 
            margin: 0 auto;   
        }
        h1,  h3, h4, h6 {
            background-color: #404040;
        }
        h2 {
            background-color: #333333;
            width: 20%;
        }
        h4, h6 {
            font-size: 14px;
            margin-right: auto;
            border-radius: 8px;
            width: 20%;
        }
        h6 {
            width: 25%;
        }
        tbody {
            overflow-y: scroll;
        }

        html,body { margin:0; padding: 0; }
        #main_block {
          display: block; 
          width: 100%;    
          height: 100%;   
          font-size:0;
        }
        #left_frame {
          width: 50%;
        }
        #right_frame {
          width: calc(100% - 50%);
        }
        #left_frame,#right_frame {
          display: inline-block;
          box-sizing: border-box;
          height: calc(70vh - 50px);
        }
    </style>

	<script type="text/javascript">
    	$(document).ready(function () {
            var allVol = 0;
            var dataArray = [];
            
            $.ajax({
                type: "POST",
                url: "DBConnectServlet",
                data: {'method': 'loadAllList'},
                dataType: "json",
                success: function(data){
                    console.log(data);
                    var tableElem2 = '<tr class="rowTable"><td style="text-align:center">No Records to Display...</td></tr>';
                    if(data.allList.length != 0){
                        allVol = data.allList.length;
                        console.log(allVol);
                        tableElem2 = '';
                            for(var i = 0; i < data.allList.length; i++){
                                tableElem2 += '<tr class="rowTable"><td style="text-align:center">'+data.allList[i]+'</td></tr>';
                            }
                        }
                        $("#allList tbody").html(tableElem2);
                        
                        }
                    });
            var options = {
                chart: {
                    plotBackgroundColor: null,
                    plotBorderWidth: null,
                    plotShadow: false,
                    type: 'pie'
                },
                title: {
                        text: 'Volunteer(s) connected as of ' + new Date().toDateString()
                },
                tooltip: {
                    pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
                },
                plotOptions: {
                        pie: {
                            size:150,
                            allowPointSelect: true,
                            cursor: 'pointer',
                        }
                },
                series: [{
                        colorByPoint: true,
                        name: 'Percentage',
                            data: []
                        }]
                }
                
            $("#start").click(function(){
               $.ajax({
                   type: "POST",
                    url: "DBConnectServlet",
                    data: {'method': 'loadAll'},
                        dataType: "json",
                    success: function(data){
//                        console.log(data);
                        var tableElem = '<tr class="rowTable"><td style="text-align:center">No Records to Display...</td></tr>';
                        if(data.allActive.length != 0){
                            tableElem = '';
                            var countActive = parseInt(allVol)/data.allActive.length;
                            var storeActive = [];
                            for(var i = 0; i < data.allActive.length; i++){
                                tableElem += '<tr class="rowTable"><td style="text-align:center">'+data.allActive[i]+'</td></tr>';
                                storeActive[0] = data.allActive[i];
                                storeActive[1] = countActive;
                                dataArray.push(storeActive);
                            }
                            console.log(dataArray);
                            options.series[0].data = dataArray;
                            $('#container').highcharts(options);
                        }
                        $("#activeList tbody").html(tableElem); 
                        $("#clientsConnected").html(data.allActive.length);
                        $("#serverStat").html('<img src="design/bootstrap/images/serverLoading.gif" alt="Server" style="width: 25%; height: 40%; margin-left: 37%"><br><span style="text-align:center; vertical-align: middle; margin-left: 35%; font-size: 20px; font-weight: bold">Server Running...</span>');
                        $("#volunteerStat, #volunteerStat2").css('display', 'block');   
                        }
                    });
            });
            
            $("#terminate").click(function(){
                $("#activeList tbody, #clientsConnected").html(''); 
                $("#serverStat").html('<img src="design/bootstrap/images/serverStop.png" alt="Server" style="width: 25%; height: 40%; margin-left: 37%"><br><span style="text-align:center; vertical-align: middle; margin-left: 35%; font-size: 20px; font-weight: bold">Server at Rest...</span>');
                $("#volunteerStat, #volunteerStat2").css('display', 'none');
                options.series[0].data = [];
                $('#container').highcharts(options);
            });
            
            $("#viewCode").click(function(){
//                $.ajax({
//                    type: "POST",
//                    url: "DBConnectServlet",
//                    data: {'method': 'viewCode'},
//                    dataType: "json",
//                    success: function(data){
//                        console.log(data);
//                    }
//                })
               $("#left_frame").attr('src', "textEditor1.jsp");
               $("#right_frame").attr('src', "textEditor2.jsp");
            })
            
        });
	</script>
</head>
<body>

    <p align="right" style="margin-right: 10px; margin-top: 10px; margin-bottom: 30px;">
        <button type="button" class="btn btn-primary" id="start" >Start</button>
        <button type="button" class="btn btn-danger" id="terminate">Terminate</button>
        <button type="button" class="btn btn-success" id="viewCode" data-toggle="modal" data-target="#viewCodeModal">View Codes</button>
    </p>

    <!-- Active Volunteer Table -->
    <div class="container-fluid">
        <div class="row">

            <div class="col-md-6" style="width: 40%;">
                    <br><br>
                    <div id="serverStat">
                        <img src="design/bootstrap/images/serverStop.png" alt="Server" style="width: 25%; height: 40%; margin-left: 37%">
                        <br>
                        <span style="text-align:center; vertical-align: middle; margin-left: 35%; font-size: 20px; font-weight: bold">Server at Rest...</span>
                    </div>
                    <br><br><br>    
                    <div class="table-responsive" style="margin-top: 50px;">
                    <table class="table" style="width: 90%; margin: 0 auto">
                        <thead class="headerTable">
                            <tr>
                                <th style="text-align:left">Logs</th>
                                <th style="float: right"><button class="btn btn-secondary" style="background-color: black; color: white">Clear</button></th>
                            </tr>
                        </thead>
                        <tbody>
                          <tr>
                            <td>Logs here...</td>
                            <td></td>
                          </tr>
                           <tr>
                            <td>Logs here...</td>
                            <td></td>
                          </tr>
                           <tr>
                            <td>Logs here...</td>
                            <td></td>
                          </tr>
                           <tr>
                            <td>Logs here...</td>
                            <td></td>
                          </tr>
                           <tr>
                            <td>Logs here...</td>
                            <td></td>
                          </tr>
                           <tr>
                            <td>Logs here...</td>
                            <td></td>
                          </tr>
                        </tbody>
                    </table>
                    </div>
            </div>
            
            <div class="col-md-6" style="width: 20%; margin-top: 8em;">
                <span style="font-size: 900%; margin-bottom: -40px; margin-left: .75em;" id="clientsConnected"></span>
                <p align="left" style="margin-left: 4em;display: none" id="volunteerStat2">Volunteer(s) Connected </p>
            </div>
            
            <!-- Pie Chart -->
            <div class="col-md-6" style="width: 40%;">
                <h3 style="display:none" id="volunteerStat">Volunteer Stats</h3><br><br>
                <div id="container" style="min-width: 230px; height: 320px; max-width: 520px; margin: 0 auto"></div>
            </div>
        </div>
    </div>

    <br><br>
    
    <div class="container-fluid" style="width:100%;margin-top: -15em;">
        <div class="row">

            <div class="col-md-4" style="display: inline-block; float:right">
                
    <h2>All Volunteers</h2><br>
    <div class="table-responsive">          
    <table class="table" style="width: 80%; margin: 0 auto" id="allList">
        <thead>
            <tr>
                <th class="headerTable" style="text-align:center">IP Address</th>
                </tr>
        </thead>
        <tbody>
<!--          <tr class="rowTable">
            <td style="text-align:center">001</td>
          </tr>-->
        </tbody>
    </table>
    </div>
            </div>
            
        <div class="col-md-4" style="display: inline-block; float: right">
                
    <h2 style="width: 25%">Active Volunteers</h2><br>
    <div class="table-responsive">          
    <table class="table" style="width: 80%; margin: 0 auto" id="activeList">
        <thead>
            <tr>
                <th class="headerTable" style="text-align:center">IP Address</th>
                </tr>
        </thead>
        <tbody>
<!--          <tr class="rowTable">
            <td style="text-align:center">001</td>
          </tr>-->
        </tbody>
    </table>
    </div>
            </div>
        </div></div>
        <!-- View Code Modal -->
        <div class="modal fade" id="viewCodeModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
          <div class="modal-dialog" role="document" style="width: 90%; height: 90%; overflow-y: scroll;">
            <div class="modal-content">
              <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">&times;</button>
                <h5 class="modal-title" id="exampleModalLabel">CODE VIEWER</h5>
              </div>
              <div class="modal-body">
                  <div class="container-fluid">
                    <div class="row">
                        <div class="col-md-6">
                            <h4>Server Code</h4>
                        </div>
                        <div class="col-md-6">
                            <h6>Volunteer Code</h6>
                        </div>
                    </div>
                </div>
                <form id='viewCodeForm'>
                  <div class="form-group">
                    <div id="main_block" style="display: block; margin: 0 auto">
                        <iframe id="left_frame"></iframe>
                        <iframe id="right_frame"></iframe>
                    </div>
                  </div>
              </div>
              <div class="modal-footer">
                <button type="button" class="btn btn-primary" data-dismiss="modal">Close</button>
              </div>
                </form>
            </div>
          </div>
        </div>

</body>
</html>

