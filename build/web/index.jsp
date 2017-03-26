<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>MultiPC</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" type="text/css" href="design/bootstrap/css/bootstrap.css">
    <script src="design/bootstrap/js/jquery-1.3.2.min.js"></script>
    <script src="design/bootstrap/js/jquery.min.js"></script>
    <script src="design/bootstrap/js/bootstrap.min.js"></script>

    <style type="text/css">
        html,body { 
            margin:0; 
            padding: 0; 
        }
        
        #main_block {
            display: block; 
            width: 100%;    
            height: 100%;   
            font-size:0;
        }
        
        #left_frame {
          width: 800px;
        }
        
        #right_frame {
          width: calc(100% - 800px);
        }
        
        #left_frame,#right_frame {
          display: inline-block;
          box-sizing: border-box;
          height: calc(100vh - 50px);
        }
    
        #projectLabel{
            color: #337ab7;
            width: 14em;
            font-size: 16px;
        }
    
        #projectLabel:hover {
            background-color: #eee;
        }
    </style>
    <script type="text/javascript">
        $(document).ready(function(){     
//            console.log(document.getElementById('left_frame').contentWindow.document.getElementsByClassName('contentBtns'));    
            document.getElementById('txtFileUpload').addEventListener('change', upload, false);

            // Method that checks that the browser supports the HTML5 File API
            function browserSupportFileUpload() {
                var isCompatible = false;
                if (window.File && window.FileReader && window.FileList && window.Blob) {
                isCompatible = true;
                }
                return isCompatible;
            }
            
            function upload(evt) {
                if (!browserSupportFileUpload()) {
                    alert('The File APIs are not fully supported in this browser!');
                } else {
                    var data = [];
                    var file = evt.target.files[0];
                    var reader = new FileReader();
                    reader.readAsText(file);
                    reader.onload = function(event) {
                        var csvData = event.target.result;
                        data = csvData.split("\n");
//                        console.log(data)
                        if (data && data.length > 0) {
                          console.log('Imported -' + data.length + '- rows successfully!');
                           $("#spinnerModal").modal('show');
                           $.ajax({
                                type: "POST",
                                url: "DBConnectServlet",
                                data: { 'dbName': $(".dbName").html(),
                                        'table': $("#thisTable").val(),
                                        'csv': csvData,
                                        'method': 'storeCSV'},
                                dataType: "json",
                                success: function (data) {
                                    console.log(data);
                                    if(data.csv == 0){
                                        $("#spinnerModal").modal('hide');
                                        $("#uploadCSVModal").modal('hide');
                                        alert("CSV added!");

                                        loadTables();
                                    }
                                }
                        });
                        } else {
                            console.log('No data to import!');
                        }
                    };
                    reader.onerror = function() {
                        alert('Unable to read ' + file.fileName);
                    };
                }
            }
            
            $.ajax({
                type: "GET",
                url: "DBConnectServlet",
                data: {'method': 'loadProjectList'},
                dataType: "json",
                success: function (data) {
                    console.log(data);  
                    if(data.projectID.length != 0){
                        var content = '';
                        for(var i = 0; i < data.projectID.length; i++){
                            content += '<li id="'+data.projectID[i]+'"><label class="tree-toggler nav-header" onclick="showHideContents('+data.projectID[i]+')"><span class="glyphicon glyphicon-home" style="cursor: pointer;"></span></label>';
                            content += ' <span class="glyphicon" style="cursor: pointer; margin-left: 8px;" id="projectLabel" ondblclick="loadProjectDetails('+data.projectID[i]+')">'+data.projectName[i]+'</span>';
                            content += '<ul class="nav nav-list tree" hidden>';
                            content += ' <li style="margin-left: 45px;"><label class="nav-header"><span class="glyphicon">'+data.projectName[i]+'_sever.mdl</span></label></li>';
                            content += ' <li style="margin-left: 45px;"><label class="nav-header"><span class="glyphicon">'+data.projectName[i]+'_volunteer.mdl</span></label></li>';
                            content += '</ul>';
                            content += '</li>';
                            content += '<li class="divider"></li>';
                        }
                        $("#projectList").html(content);
                    }
                }
            });
            
            $("#createProject").click(function(){
                $.ajax({
                    type: "GET",
                    url: "DBConnectServlet",
                    data: {'method': 'getBtnState'},
                    dataType: "json",
                    success: function (data) {
                        console.log(data);
                        if(data.serverBtn == 'block' || data.volunteerBtn == 'block'){
                            alert(data.projectName+' has been modified. Save or Discard changes first!');
                        }else{
                            $("#createProjectModal").modal('show');
                        }
                    }
                });
            });
        
            $("#createProjectBtn").click(function(e){
                if($("#projectName").val() == ''){
                    alert('Enter Project Name');
                }else{
                    $("#spinnerModal").modal('show');
                    $.ajax({
                        type: "POST",
                        url: "DBConnectServlet",
                        data: {'projectName': $("#projectName").val(),
                                'method': 'createProject'},
                        dataType: "json",
                        success: function (data) {
                            console.log(data);  
                            if(data.status == 1){
                                $("#project").val(data.projectID);
                                $("#spinnerModal").modal('hide');
                                $("#createProjecModal").modal('hide');
                                alert('Saved!');
                                location.reload();
                            }
                        }
                    });
                }  
            });
     
            $("#openProject").click(function(){
                $.ajax({
                        type: "GET",
                        url: "DBConnectServlet",
                        data: {'method': 'getBtnState'},
                        dataType: "json",
                        success: function (data) {
                            console.log(data);
                            if(data.serverBtn == 'block' || data.volunteerBtn == 'block'){
                                alert(data.projectName+' has been modified. Save or Discard changes first!');
                            }else{
                                $("#openProjectModal").modal('show');
                            }
                        }
                    });
            });
            
            $("#compileBtn").click(function(){
//                console.log($("#left_frame").contents().find('#serverContent').val());
//                console.log($("#right_frame").contents().find('#volunteerContent').val());
//                if($("#left_frame").contents().find('#contentBtn').css('display') == 'block'){
//                    $.ajax({
//                        type: "POST",
//                        url: "DBConnectServlet",
//                        data: {'projectID': $("#left_frame").contents().find('#project').val(),
//                               'serverContent': $("#left_frame").contents().find('#serverContent').val(),
//                                'method': 'saveServerContent'},
//                        dataType: "json",
//                        success: function(data){
//                            $("#left_frame").contents().find('#contentBtn').css('display', 'none');
//                        }
//                    });
//                }
                if($("#right_frame").contents().find('#contentBtn').css('display') == 'block'){
                    $.ajax({
                        type: "POST",
                        url: "DBConnectServlet",
                        data: {'projectID': $("#right_frame").contents().find('#project').val(),
                               'serverContent': $("#right_frame").contents().find('#volunteerContent').val(),
                                'method': 'saveVolunteerContent'},
                        dataType: "json",
                        success: function(data){
                            $("#right_frame").contents().find('#contentBtn').css('display', 'none');
                        }
                    });
                }
                $.ajax({
                    type: "GET",
                    url: "DBConnectServlet",
                    data: {'method': 'compileProject',
//                            'serverContent': $("#left_frame").contents().find('#serverContent').val(),
                            'volunteerContent': $("#right_frame").contents().find('#volunteerContent').val()},
                    dataType: "json",
                    success: function (data) {
                        console.log("Compiled", data);
                        var serverErrors = "Successfully compiled!",
                            volErrors = "Successfully compiled!";
//                        if(data.serverErrors.length != 0){
//                            serverErrors = '';
//                            for(var i = 0; i < data.serverErrors.length; i++){
//                                   serverErrors += data.serverErrors[i];
//                                   serverErrors += "<br>";
//                            }
//                        }
                        if(data.volunteerErrors.length != 0){
                            volErrors = '';
                            for(var i = 0; i < data.volunteerErrors.length; i++){
                                   volErrors += data.volunteerErrors[i];
                                   volErrors += "<br>";
                            }
                        }
//                        $("#left_frame").contents().find('#serverErrors').html(serverErrors);
                        $("#right_frame").contents().find('#volunteerErrors').html(volErrors);
                    }
                });
            });
            
            $("#runBtn").click(function(){
//                window.location.href = "dashboard.jsp";
                $.ajax({
                    type: "POST",
                    url: "DBConnectServlet",
                    data: {'volunteerContent': $("#right_frame").contents().find('#volunteerContent').val(),
                            'dbName': $(".dbName").html(),
                           'method': 'run'},
                    dataType: "json",
                    success: function (data) {
                        alert(data.javaConverted);
                    }
                });
            });
            
            $("#addTable").click(function(){
                $(".dbName").html($("#right_frame").contents().find('#projectName').html());
            });
            
            $("#showDB").click(function(){
                $(".dbName").html($("#right_frame").contents().find('#projectName').html());
                loadTables();
            })
            
            $("#addTC").click(function(){
                $("#DBContentModal").modal('hide');
                $("#spinnerModal").modal('show');
                $.ajax({
                    type: "POST",
                    url: "DBConnectServlet",
                    data: { 'dbName': $(".dbName").html(),
                            'tableName': $(".tableName").val(),
                            'method': 'addTable'},
                    dataType: "json",
                    success: function (data) {
                        if(data.rows == 0){
                            $("#spinnerModal").modal('hide');
                            $("#addTableModal").modal('hide');
                            alert("Table(s) Added!");
                            loadTables();
                        }
                    }
                });
//                console.log($("#elemTable").val())
//                if($("#elemTable").val() != undefined){
//                    var tables = '';
//                    for(var i = 0; i < $(".tableName").length; i++){
//                        if($(".tableName")[i].value != ""){
//                            tables += $(".tableName")[i].value+"-";
//                        }
//                    }
//                    console.log(tables);
//                    $.ajax({
//                        type: "POST",
//                        url: "DBConnectServlet",
//                        data: { 'dbName': $(".dbName").html(),
//                                'tableName': tables,
//                               'method': 'addTable'},
//                        dataType: "json",
//                        success: function (data) {
//                            if(data.rows == 0){
//                                $("#spinnerModal").modal('hide');
//                                $("#addTableModal").modal('hide');
//                                alert("Table(s) Added!");
//                            }
//                        }
//                    });
//                }else{
//                    if($("#tableList").val() == null){
//                        alert("Choose table ...");
//                    }else{
//                        var cols = '';
//                        for(var i = 0; i < $(".colName").length; i++){
//                            if($(".colName")[i].value != ""){
//                                cols += $(".colName")[i].value+"-";
//                            }
//                        }
//                        console.log(cols);
//                        $.ajax({
//                            type: "POST",
//                            url: "DBConnectServlet",
//                            data: { 'dbName': $(".dbName").html(),
//                                    'table': $("#tableList").val(),
//                                    'columns': cols,
//                                   'method': 'addColumns'},
//                            dataType: "json",
//                            success: function (data) {
//                                if(data.rows == 0){
//                                    $("#spinnerModal").modal('hide');
//                                    $("#addTableModal").modal('hide');
//                                    alert("Column(s) Added!");
//                                }
//                            }
//                        });
//                    }
//                }
            });
            
            $("#addCol").click(function(){
                $("#DBContentModal").modal('hide');
                $("#spinnerModal").modal('show');
                var cols = '';
                for(var i = 0; i < $(".colName").length; i++){
                    if($(".colName")[i].value != ""){
                        cols += $(".colName")[i].value+"-";
                    }
                }
                $.ajax({
                    type: "POST",
                            url: "DBConnectServlet",
                            data: { 'dbName': $(".dbName").html(),
                                    'table': $("#thisTable").val(),
                                    'columns': cols,
                                   'method': 'addColumns'},
                            dataType: "json",
                            success: function (data) {
                                if(data.rows == 0){
                                    $("#spinnerModal").modal('hide');
                                    $("#addColumnModal").modal('hide');
                                    alert("Column(s) Added!");
                                    
                                    loadTables();
                                }
                            }
                        });
            });
            $("#elemTable .addButton").click(function(){
                add();
            });
            
            $("#addTableModal").on('hidden.bs.modal', function (e) {
//                show('table');
//                $("#tbl").addClass('active');
//                $("#clm").removeClass('active');
            });
            
            $("#clear").on('click', function(){
               if(confirm("Delete table content?")){
                   $("#DBContentModal").modal('hide');
                   $("#spinnerModal").modal('show');
                   var cols = '';
                   $('#dbContentTable > thead > tr > th').each(function() {
                        cols += this.innerText+"-";
                   });
                   $.ajax({
                            type: "POST",
                            url: "DBConnectServlet",
                            data: { 'dbName': $(".dbName").html(),
                                    'table': $("#thisTable").val(),
                                    'columns': cols,
                                    'method': 'clearTable'},
                            dataType: "json",
                            success: function (data) {
                                console.log(data);
                                if(data.columns == 0){
                                    $("#spinnerModal").modal('hide');
                                    alert("Column(s) Deleted!");
                                    
                                    loadTables();
                                }
                            }
                    });
               } 
            });
            
            $("#delete").on('click', function(){
               if(confirm("Table will be deleted. Continue anyway?")){
                   $("#DBContentModal").modal('hide');
                   $("#spinnerModal").modal('show');
                    $.ajax({
                            type: "POST",
                            url: "DBConnectServlet",
                            data: { 'dbName': $(".dbName").html(),
                                    'table': $("#thisTable").val(),
                                    'method': 'deleteTable'},
                            dataType: "json",
                            success: function (data) {
                                console.log(data);
                                if(data.table == 0){
                                    $("#spinnerModal").modal('hide');
                                    alert("Table Deleted!");
                                    
                                    loadTables();
                                }
                            }
                    });
               } 
            });
        });
        
        function showHideContents(projectID){
            $('#'+projectID+' .tree-toggler').parent().children('ul.tree').toggle(300);
        }
    
        function loadProjectDetails (projectID){
            $.ajax({
                type: "POST",
                url: "DBConnectServlet",
                data: {'projectID': projectID,
                        'method': 'loadProjectDetails'},
                dataType: "json",
                success: function (data) {
                   console.log(data);
                   if(data == projectID){
                       $("#project").val(data);
                       location.reload();
                   }else{
                       alert('Something went wrong');
                   }
                }
            });
        }
        
        
//        var tableCtr = 1;
//        function add(){
//            $("#elemTable").append('<tr id='+tableCtr+'><td><input type="text" placeholder="Table name ..." class="form-control tableName" required style="display: block; width: 22em; margin-top: 1em; margin-right: 2em"></td><td><button type="button" class="btn btn-danger removeBtn" onclick="remove('+tableCtr+')" style="display: block; margin-top: 1em">-</button></td></tr>');
//            tableCtr++;
//        }
        
        var colCtr = 1;
        function addColumn(){
            $("#colTable").append('<tr id='+colCtr+'><td><input type="text" placeholder="Column name ..." class="form-control colName" required style="display: block; width: 22em; margin-top: 1em; margin-right: 2em"></td><td><button type="button" class="btn btn-danger removeBtn" onclick="removeColumn('+colCtr+')" style="display: block; margin-top: 1em">-</button></td></tr>');
            colCtr++;
        }
        
        
        function removeColumn(item){
            console.log($("#"+item))
            $("#"+item).remove();   
        }
//        function show (type){
//            var elem = '';
//            if(type == "table"){
//                tableCtr = 1;
//                elem = '<div class="form-group">';
//                elem += '<table id="elemTable"><tr>';
//                elem += '<td><label for="recipient-name" class="control-label">Table(s):</label></td>';
//                elem += '<td><button type="button" class="btn btn-primary addButton" onclick="add()" style="display: block">+</button></td>';
//                elem += '</tr><tr>';
//                elem += '<td><input type="text" placeholder="Table name ..." class="form-control tableName" required style="display: block; width: 22em; margin-top: 1em; margin-right: 2em"></td>';
//                elem += '</tr></table></div>';
//            }else{
//                $.ajax({
//                    type: "POST",
//                    url: "DBConnectServlet",
//                    data: {'dbName': $(".dbName").html(),
//                            'method': 'loadDBTables'},
//                    dataType: "json",
//                    success: function (data) {
//                        console.log(data);
//                        if(data.tables.length != 0){
//                            var tables = '';
//                            for(var i = 0; i < data.tables.length; i++){
//                               tables += '<option value='+data.tables[i]+'>'+data.tables[i]+'</option>';
//                            }
//                            $("#tableList").append(tables);
//                        }else{
//                            alert("No tables found in the Database...");
//                        }
//                    }
//                });
//                elem = '<div class="form-group">';
//                elem += '<label for="recipient-name" class="control-label"> Add to table:</label>';
//                elem += '<select class="form-control" style="width: 15em;" id="tableList">';
//                elem += '<option disabled selected value="default">Select table</option>';
//                elem += '</select><br><br>';
//                elem += '<table id="colTable"><tr>';
//                elem += '<td><label for="recipient-name" class="control-label">Column(s):</label></td>';
//                elem += '<td><button type="button" class="btn btn-primary addButton" onclick="addColumn()" style="display: block">+</button></td>';
//                elem += '</tr><tr>';
//                elem += '<td><input type="text" placeholder="Column name ..." class="form-control colName" required style="display: block; width: 22em; margin-top: 1em; margin-right: 2em"></td>';
//                elem += '</tr></table></div>';
//            } 
//            $("#addElem").html(elem);
//        }
        
        function loadTables(){
            $("#DBContentModal").modal('show');
            $("#spinnerModal").modal('show');
            $.ajax({
                    type: "POST",
                    url: "DBConnectServlet",
                    data: {'dbName': $(".dbName").html(),
                            'method': 'loadDBTables'},
                    dataType: "json",
                    success: function (data) {
                        console.log(data);
                        if(data.tables.length != 0){
                            var tables = '<li class="active pointer" style="cursor: pointer;" onclick="showTableContent(\''+data.tables[0]+'\')"><a data-toggle="tab">'+data.tables[0]+'</a></li>';     
                            showTableContent(data.tables[0]);
                            for(var i = 1; i < data.tables.length; i++){
                               tables += '<li class="pointer" style="cursor: pointer;" onclick="showTableContent(\''+data.tables[i]+'\')"><a data-toggle="tab">'+data.tables[i]+'</a></li>';     
                            }
                            $("#spinnerModal").modal('hide');
                            $("#tableMenu").html(tables);
                        }else{
                            $("#spinnerModal").modal('hide');
                            $("#btnAction").hide();
                            $("#tableMenu").html('No tables found in the database...');
                        }
                    }
            });
        }
        
        function showTableContent(table){
            $("#thisTable").val(table);
            $.ajax({
                    type: "POST",
                    url: "DBConnectServlet",
                    data: {'dbName': $(".dbName").html(),
                            'table': table,
                            'method': 'loadTableContent'},
                    dataType: "json",
                    success: function (data) {
                        console.log(data);
                         var columns = '<span style="text-align: center">No columns found...</span>';
                         if(data.columns.length > 0){
                            columns = '<thead style="background-color: black; color: white;">';
                            for(var i = 0; i < data.columns.length; i++){
                               columns += '<th style="width: 10em; text-align: center">'+data.columns[i]+'</th>';
                            }
                            columns += '</thead><tbody><div>';
                            for(var i = 0; i < data.data.length; i++){
                                columns += '<tr><td>'+data.data[i]+'</td><td>'+data.result[i]+'</td></tr>';
                            }
                            columns += '</div></tbody>';
                        }
                        $("#dbContentTable").html(columns);
                    }
            });
        }
    </script>
</head>
<body>
    <input type="hidden" id="project">    
    <nav class="navbar navbar-inverse navbar-fixed-top">
        <div class="container-fluid">
            <div class="navbar-header">
                <a class="navbar-brand" href="home.jsp">MDL</a>
            </div>
            <ul class="nav navbar-nav">
                 <li class="active"><a href="#">Home</a></li>
                 <li class="dropdown"><a class="dropdown-toggle" data-toggle="dropdown" href="#">File <span class="caret"></span></a>
                    <ul class="dropdown-menu">
                        <li>
                            <!-- CHANGED/ADDED -->
                            <a href="#" id="openProject">Open Project </a>
                        </li>
                        <li>
                            <a href="#" id="createProject">Create Project</a>
                        </li>
        <!--          <li><a href="#" id="saveProject" data-toggle="modal" data-target="#saveProjectModal">Save Project</a></li>-->
                    </ul>
                 </li>
            </ul>
        </div>
    </nav>
    
    <div id="tableCreate" style="float:left; margin-top: -3em; margin-left: 1em; display: block;">
        <!--<button type="button" id="addTable" class="btn btn-secondary btn-warning" data-toggle="modal" data-target="#addTableModal">Add table / column</button>-->
        <button type="button" class="btn btn-primary btn-info" id="showDB">Manage Data</button>
    </div>
    <div id="compile" style="float:right; margin-top: -3em; margin-right: 1em; display: block;">
        <button type="button" id="compileBtn" class="btn btn-secondary btn-success">Compile</button>
        <button type="button" id="runBtn" class="btn btn-secondary" style="background-color: black; color: white">Run</button>
    </div>
    
    <div id="main_block" style="display: block; margin-top: 100px;">
        <iframe id="left_frame" src="textEditor1.jsp"></iframe>
        <iframe id="right_frame" src="textEditor2.jsp"></iframe>
    </div>
<!-- MADE SOME CHANGES -->
    <div class="modal fade" id="createProjectModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
        <div class="modal-dialog" role="document" style="width: 400px;">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="exampleModalLabel">CREATE PROJECT</h5>
                </div>
                <div class="modal-body">
                    <div class="form-group">
                        <label for="recipient-name" class="control-label">Project Name:</label>
                        <input type="text" class="form-control" id="projectName" required>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" id="createProjectBtn" class="btn btn-primary">Save</button>
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                </div>
            </div>
        </div>
    </div>
    <!-- ADDED -->
    <div class="modal fade" id="openProjectModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
        <div class="modal-dialog" role="document" style="width: 300px;">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="exampleModalLabel">OPEN PROJECT</h5>
                </div>
                <div class="modal-body">
                    <form id='openProjectForm'>
                        <div style="overflow-y: scroll; overflow-x: hidden; height: 200px;">
                            <ul class="nav nav-list" id="projectList"></ul>
                        </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                </div>
                    </form>
            </div>
        </div>
    </div>
    
    <div class="modal fade" id="DBContentModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
        <div class="modal-dialog" role="document" style="width: 60%;">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="exampleModalLabel">DATABASE CONTENT</h5>
                </div>
                <div class="modal-body">
                    <div class="form-group">
                        <label for="recipient-name" class="control-label">DATABASE: <label class="control-label dbName"></label></label>
                        <br><br>
                        <button type="button" id="addTable" class="btn btn-secondary btn-warning" style="float:right" data-toggle="modal" data-target="#addTableModal">Add table</button>                    
                    </div>
                    <div class="form-group">
                        <label for="recipient-name" class="control-label">Tables:</label>
                    </div>
                    <section id="container" class="">
                        <!--main content start-->
                            <section class="wrapper">            
                                <!--overview start-->
                               <div class="">
                                <ul class="nav nav-tabs" id="tableMenu"></ul>
                                <br><br>
                                <div class="tab-content">
                                    <div class="tab-pane fade in active">
                                        <div class="form-group" style="float:right; margin-top: -2em;" id="btnAction">
                                            <input type="hidden" id="thisTable">
                                            <button type="button" id="upload" class="btn btn-primary btn-xs" data-toggle="modal" data-target="#uploadCSVModal">Upload CSV</button>
                                            <button type="button" id="column" class="btn btn-primary btn-success btn-xs" data-toggle="modal" data-target="#addColumnModal">Mange Column</button>
                                            <button type="button" id="clear" class="btn btn-primary btn-info btn-xs">Clear Table Content</button>
                                            <button type="button" id="delete" class="btn btn-primary btn-danger btn-xs">Delete table</button>
                                        </div>
                                        <div class="form-group" style="height: 20em; width: 100%; overflow-y: scroll;">
                                            <table id="dbContentTable" class="table table-bordered table-responsive"></table>
                                        </div>
                                    </div>
                                </div>
                                </div>
                        </section>
                    </section>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                </div>
            </div>
        </div>
    </div>
    
    <div class="modal fade" id="addTableModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
        <div class="modal-dialog" role="document" style="width: 400px;">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="exampleModalLabel">ADD TABLE</h5>
                </div>
                <div class="modal-body">
                    <div id="addElem" class="tab-pane fade in active">
                        <div class="form-group">
                            <table id="elemTable">
                                <tr>
                                    <td><label for="recipient-name" class="control-label">Table:</label></td>
                                </tr>
                                <tr>
                                    <td><input type="text" placeholder="Table name ..." class="form-control tableName" required style="display: block; width: 22em; margin-top: 1em; margin-right: 2em"></td>
                                </tr>
                            </table>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" id="addTC" class="btn btn-primary">Save</button>
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                </div>
            </div>
        </div>
    </div>
    
    <div class="modal fade" id="addColumnModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
        <div class="modal-dialog" role="document" style="width: 400px;">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="exampleModalLabel">ADD COLUMN</h5>
                </div>
                <div class="modal-body">
                    <div id="addElem" class="tab-pane fade in active">
                       <div class="form-group">
                            <label for="recipient-name" class="control-label"> Add to table:</label>
                            <br><br>
                            <table id="colTable">
                                <tr>
                                    <td><label for="recipient-name" class="control-label">Column(s):</label></td>
                                    <td><button type="button" class="btn btn-primary addButton" onclick="addColumn()" style="display: block">+</button></td>
                                </tr>
                                <tr>
                                    <td><input type="text" placeholder="Column name ..." class="form-control colName" required style="display: block; width: 22em; margin-top: 1em; margin-right: 2em"></td>
                                </tr>
                            </table>
                       </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" id="addCol" class="btn btn-primary">Save</button>
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                </div>
            </div>
        </div>
    </div>
    
    <div class="modal fade" id="uploadCSVModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
        <div class="modal-dialog" role="document" style="width: 400px;">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="exampleModalLabel">UPLOAD CSV</h5>
                </div>
                <div class="modal-body">
                    <div id="addElem" class="tab-pane fade in active">
                        <div class="form-group">
                          <table id="elemTable">
                                <tr>
                                    <td><legend for="recipient-name" class="control-label">Upload your CSV File</legend></td>
                                </tr>
                                <tr>
                                    <td><input type="file" name="File Upload" id="txtFileUpload" accept=".csv" /></td>
                                </tr>
                            </table>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <!--<button type="button" id="uploadCSV" class="btn btn-primary">Save</button>-->
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                </div>
            </div>
        </div>
    </div>
    
    <div class="modal fade" id="spinnerModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
        <div class="modal-dialog" role="document" style="width: 20%; margin-top: 5em;">
            <div class="modal-content" style="background-color: black; color: white;">
                <div class="modal-body">
                    <span style="margin-left: 25%;">
                        <img src="design/bootstrap/spinner.gif" width="100" height="100"><br><br>
                        <label for="recipient-name" class="control-label" style="margin-left: 25%;">Please Wait ...</label>
                    </span>
                </div>
            </div>
        </div>
    </div>

<!--<div class="modal fade" id="saveProjectModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
  <div class="modal-dialog" role="document" style="width: 400px;">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="exampleModalLabel">SAVE PROJECT</h5>
      </div>
      <div class="modal-body">
        <form id='saveProjectForm'>
          <div class="form-group">
            <label for="recipient-name" class="control-label">Project Name:</label>
            <input type="text" class="form-control" id="projectName" required>
          </div>
      </div>
      <div class="modal-footer">
        <button type="button" id="saveProjectBtn" class="btn btn-primary">Save</button>
        <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
      </div>
        </form>
    </div>
  </div>
</div>-->
</body>
</html>

