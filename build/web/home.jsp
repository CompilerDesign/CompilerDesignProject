<%-- 
    Document   : home
    Created on : Jan 17, 2017, 11:22:58 AM
    Author     : jayvee_lugod
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
  <<title>Compiler Design</title>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" type="text/css" href="design/bootstrap/css/bootstrap.min.css">
  <script src="design/bootstrap/js/jquery-1.3.2.min.js"></script>
  <script src="design/bootstrap/js/jquery.min.js"></script>
  <script src="design/bootstrap/js/bootstrap.min.js"></script>
	
	<style type="text/css">
		h2 {
			font-family: Century Gothic;
			font-size: 18px;

		}
		body {
			background-image: url("design/bootstrap/images/background2.jpg");
			-moz-background-size: cover;
			-webkit-background-size: cover;
			background-size: cover;
			background-position: top center !important;
			background-repeat: no-repeat !important;
			background-attachment: fixed;
		}
		a {
			text-decoration: none;
		}
		.margin-button {
			margin-top: 485px;
			margin-left: 455px;
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
		// EXPANDABLE TREE JS
	    $(document).ready(function () {
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
                     }else{
                        $("#projectList").html('No projects available'); 
                     }
                }
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
                                window.location.href = "index.jsp";
                            }
                        }
                    });
                }  
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
                           window.location.href = "index.jsp";
                       }else{
                           alert('Something went wrong');
                       }
                    }
                });
            }
	</script>
</head>
<body>

  <div class="margin-button">
	<a href="#" id="createProject" data-toggle="modal" data-target="#createProjectModal" class="btn btn-primary" style="width: 250px; height: 80px;"><h2><span class="glyphicon glyphicon-pencil" style="cursor: pointer;"></span> &nbsp; CREATE PROJECT </h2></a>

        <a href="#" id="openProject" data-toggle="modal" data-target="#openProjectModal" class="btn btn-success" style="width: 250px; height: 80px; margin-left: 60px;"><h2><span class="glyphicon glyphicon-folder-open" style="cursor: pointer;"></span> &nbsp; OPEN PROJECT</h2></a>
  </div>


  <!-- MODAL -->
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
</body>
</html>


