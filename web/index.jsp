<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>MultiPC</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" type="text/css" href="design/bootstrap/css/bootstrap.min.css">
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
                if($("#left_frame").contents().find('#contentBtn').css('display') == 'block'){
                    $.ajax({
                        type: "POST",
                        url: "DBConnectServlet",
                        data: {'projectID': $("#left_frame").contents().find('#project').val(),
                               'serverContent': $("#left_frame").contents().find('#serverContent').val(),
                                'method': 'saveServerContent'},
                        dataType: "json",
                        success: function(data){
                            $("#left_frame").contents().find('#contentBtn').css('display', 'none');
                        }
                    });
                }
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
                            'serverContent': $("#left_frame").contents().find('#serverContent').val(),
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
                        $("#left_frame").contents().find('#serverErrors').html(serverErrors);
                        $("#right_frame").contents().find('#volunteerErrors').html(volErrors);
                    }
                });
            });
            
            $("#runBtn").click(function(){
//                window.location.href = "dashboard.jsp";
                $.ajax({
                    type: "POST",
                    url: "DBConnectServlet",
                    data: {
                        'serverContent': $("#left_frame").contents().find('#serverContent').val(),
                        'volunteerContent': $("#right_frame").contents().find('#volunteerContent').val(),
                           'method': 'run'},
                    dataType: "json",
                    success: function (data) {
                        alert(data.fromVolunteer);
                    }
                });
               
            })
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

