<%@ page import="edu.fudan.msg.constant.UserRole"%>
<%@ page import="edu.fudan.msg.pojo.User"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
//String path = request.getContextPath();
//String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
User user = (User)session.getAttribute("user");
String title = "", username = "";

if(null == user){
	response.sendRedirect("index.jsp"); 
}
else {
	username = user.getUsername();
}
Calendar now = Calendar.getInstance();
int year = now.get(Calendar.YEAR);
int mon = now.get(Calendar.MONTH) + 1;
int day = now.get(Calendar.DAY_OF_MONTH);
%>
<!DOCTYPE html>
<html lang="zh-cn">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>学生综合管理平台----</title>
    <link href="bootstrap/css/bootstrap.min.css" rel="stylesheet">
    <script src="js/jquery.min.js"></script>
    <script src="bootstrap/js/bootstrap.min.js"></script>
    <script src="js/toast.js"></script>
    <script type="text/javascript" src="js/birthday.js"></script>
    <link rel="stylesheet" type="text/css" href="editor/fontawesome-4.2.0/css/font-awesome.min.css">
    <link rel="stylesheet" type="text/css" href="editor/css/wangEditor-1.1.0-min.css">
    <script type="text/javascript" src='editor/js/wangEditor-1.1.0-min.js'></script>
    <script>  
	$(function () {
		$.ms_DatePicker({
            	YearSelector: ".sel_year",
            	MonthSelector: ".sel_month",
            	DaySelector: ".sel_day"
    	});
		$.ms_DatePicker();
	}); 
	</script> 
    <script src="js/admin.js"></script>
    <script src="js/right.js"></script>
    <script src="js/moveT.js"></script>
    
    <script src="js/treeview.js"></script>
    <script src="js/ajaxupload.js"></script>
    <link href="css/admin.css" rel="stylesheet">
    <style type="text/css">
      div.textarea {
		
	  }
    </style>
    
  </head>
  <body>

    <!-- Modal -->
    <div class="modal fade" id="editStudent" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
            <h4 class="modal-title" id="myModalLabel">编辑学生</h4>
          </div>
          <div class="modal-body editStudentModal">
            <table class="editTable">
              <tr style="height:3em;"><td>学号：</td><td><input type="text" class="form-control" id="editNo" disabled="disabled"><input type="hidden" id="editId"></td></tr>
              <tr style="height:3em;"><td>姓名：</td><td><input type="text" class="form-control" id="editName" disabled="disabled"></td></tr>
              <tr style="height:3em;"><td>电话：</td><td><input type="text" class="form-control" id="editPhone"></td></tr>
              <tr style="height:3em;"><td>邮箱：</td><td><input type="text" class="form-control" id="editMail" style="min-width:20em;"></td></tr>
              <tr style="height:6em;"><td>备注：</td><td><textarea class="form-control" rows="3" id="editComment"></textarea></td></tr>
            </table>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
            <button type="button" class="btn btn-primary" onClick="saveEdit();">保存</button>
          </div>
        </div>
      </div>
    </div>
    
    <!-- Modal -->
    <div class="modal fade" id="showWait" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
            <h4 class="modal-title" id="myModalLabel">上传</h4>
          </div>
          <div class="modal-body editStudentModal">
            <h2>上传中，请耐心等待......</h2>
          </div>          
        </div>
      </div>
    </div>
    
    <!-- Modal -->
    <div class="modal fade" id="addStudent" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
            <h4 class="modal-title" id="myModalLabel">添加学生</h4>
          </div>
          <div class="modal-body editStudentModal">
            <table class="editTable">
              <tr style="height:3em;"><td>学号：</td><td><input type="text" class="form-control" id="addNo"></td></tr>
              <tr style="height:3em;"><td>姓名：</td><td><input type="text" class="form-control" id="addName"></td></tr>
              <tr style="height:3em;"><td>电话：</td><td><input type="text" class="form-control" id="addPhone"></td></tr>
              <tr style="height:3em;"><td>邮箱：</td><td><input type="text" class="form-control" id="addMail" style="min-width:20em;"></td></tr>
              <tr style="height:6em;"><td>备注：</td><td><textarea class="form-control" rows="3" id="addComment"></textarea></td></tr>
            </table>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-default" data-dismiss="modal" onClick="cancleAdd();">取消</button>
            <button type="button" class="btn btn-primary" onClick="addStudent(1);">保存并继续</button>
            <button type="button" class="btn btn-primary" onClick="addStudent(2);">保存并退出</button>
          </div>
        </div>
      </div>
    </div>
    
    <!-- Modal -->
    <div class="modal fade" id="move" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
            <h4 class="modal-title">移动学生</h4>
          </div>
          <div class="modal-body editStudentModal">
            <div id="moveTree"></div>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>          
            <button type="button" class="btn btn-primary" data-dismiss="modal" onClick="moveOrg();">移动</button>
          </div>
        </div>
      </div>
    </div>
    
    <!-- Modal -->
    <div class="modal fade" id="superUser" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
            <h4 class="modal-title">管理用户</h4>
          </div>
          <div class="modal-body editStudentModal">
            <table style="width:90%; margin:auto;" class="table-bordered userTable">
              <tr><td style="width:15%; text-align:center;">用户名</td><td style="width:15%; text-align:center;">姓名</td><td style="width:20%; text-align:center;">电话</td><td style="width:40%; text-align:center;">邮箱</td><td style="width:10%; text-align:center;">操作</td></tr>
              <tbody id="userTable">
              </tbody>
            </table>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-default" data-dismiss="modal">确定</button>                      
          </div>
        </div>
      </div>
    </div>
    
    <!-- Modal -->
    <div class="modal fade" id="commentStudent" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
            <h4 class="modal-title" id="myModalLabel">学生评价</h4>
          </div>
          <div class="modal-body editStudentModal">
            <table class="editTable" style="width:80%">
                
              <tr style="height:4em;"><td>主题：</td><td style="padding-right:1em;"><input type="text" class="form-control" id="commentTopic"></td></tr>  
                
                
              <tr style="padding-top:1em;"><td style="width:5em;">评价：</td>
                                      <td style="padding-right:1em;"><textarea class="form-control" rows="5" id="commentDetail"></textarea></td>
              </tr>
              <tr style="height:4em;"><td>增加标签：</td><td>
                                                        <table>
                                                          <tr><td><input type="text" class="form-control" id="addTagName"></td>
                                                              <td><button type="button" class="btn btn-default" style="margin-left:1em;" onClick="addTag();">增加</button></td></tr>
                                                        </table>
                                                      </td></tr>
              <tr style="height:4em;">
                                      <td style="width:3em;">标签：</td>
                                      <td id=commentTag></td>
                                      <!--
                                      <td style="padding-right:1em;"><select class="form-control" id="commentTag">  
                                                                                       
                                            <option>学习</option>
                                            <option>生活</option>
                                            <option>科创</option>
                                            <option>竞赛</option>
                                            <option>无</option>
                                            
                                          </select>
                                      </td>
                                      -->
              </tr>
              <tr>
                <td>时间：</td>
                <td>
                  <table style="width:100%;">
                    <tr>
                                      <td style="padding-right:1em;">
                                        <select class="sel_year" rel="<%=year %>" id="s1"></select>年
                                        <select class="sel_month" rel="<%=mon %>" id="s2"></select>月
                                        <select class="sel_day" rel="<%=day %>" id="s3"></select>日</td>
                                      
                                      
                                      <td style="width:3em;">权限：</td>
                                      <td style="padding-right:1em;"><select class="form-control" id="commentVisibility">  
                                                                                       
                                            <option value="0">公开</option>
                                            <option value="1">私有</option>                                           
                                            
                                          </select>
                                      </td>
                                      
                    </tr>
                  </table>
                </td>
              </tr>
              <tr style="height:4em;"><td></td><td><button type="button" class="btn btn-primary" onClick="saveComment();">增加</button></td></tr>
              
            </table>
            <h4>历史评价</h4>
            <table style="width:95%; margin:1em auto;" class="formerComment table table-bordered">
              <tr><td style="width:34%; text-align:center;">主题</td>
                  
                  <td style="width:34%; text-align:center;">标签</td>
                  <td style="width:17%; text-align:center;">评价时间</td>
                  <td style="width:15%; text-align:center;">操作</td>
              </tr>
              <tbody id="commentTable">
              </tbody>
            </table>
            
          </div>
          <div class="modal-footer">           
            <button type="button" class="btn btn-primary" data-dismiss="modal">确定</button>
          </div>
        </div>
      </div>
    </div>
    
    
    
    <!-- Modal -->
    <div class="modal fade" id="commentStudentHis" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
            <h4 class="modal-title" id="myModalLabel">学生评价</h4>
          </div>
          <div class="modal-body editStudentModal">
            <table class="editTable" style="width:80%">
                
              <tr style="height:4em;"><td>主题：</td><td style="padding-right:1em;"><input type="text" class="form-control" id="commentTopicHis"></td></tr>  
                
                
              <tr style="padding-top:1em;"><td style="width:3em;">评价：</td>
                                      <td style="padding-right:1em;"><textarea class="form-control" rows="5" id="commentDetailHis"></textarea></td>
              </tr>
              
              <tr style="height:4em;"><td style="width:5em;">增加标签：</td><td>
                                                        <table>
                                                          <tr><td><input type="text" class="form-control" id="addTagNameHis"></td>
                                                              <td><button type="button" class="btn btn-default" style="margin-left:1em;" onClick="addTagAll();">增加</button></td></tr>
                                                        </table>
                                                      </td></tr>
              
              <tr style="height:4em;">
                                      <td style="width:3em;">标签：</td>
                                      <td id="commentTagHis"></td>
                                      <!--
                                      <td style="padding-right:1em;"><select class="form-control" id="commentTag">  
                                                                                       
                                            <option>学习</option>
                                            <option>生活</option>
                                            <option>科创</option>
                                            <option>竞赛</option>
                                            <option>无</option>
                                            
                                          </select>
                                      </td>
                                      -->
              </tr>
              <tr>
                <td>时间：</td>
                <td>
                  <table style="width:100%;">
                    <tr>
                                      <td style="padding-right:1em;">
                                        <select class="sel_year" rel="<%=year %>" id="s1h"></select>年
                                        <select class="sel_month" rel="<%=mon %>" id="s2h"></select>月
                                        <select class="sel_day" rel="<%=day %>" id="s3h"></select>日</td>
                                      
                                      <td style="width:3em;">权限：</td>
                                      <td style="padding-right:1em;"><select class="form-control" id="commentVisibilityHis">  
                                                                                       
                                            <option value="0">公开</option>
                                            <option value="1">私有</option>                                           
                                            
                                          </select>
                                      </td>
                                      
                    </tr>
                  </table>
                </td>
              </tr>
              
            </table>            
            
          </div>
          <div class="modal-footer">           
            <button type="button" class="btn btn-primary" data-dismiss="modal">取消</button>
            <button type="button" class="btn btn-primary" onClick="saveCommentAll();">保存并继续</button>
            <button type="button" class="btn btn-primary" data-dismiss="modal" onClick="saveCommentAll();">保存并退出</button>
          </div>
        </div>
      </div>
    </div>
    
    
    
    
    
    <nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
      <div class="container-fluid">
        <div class="navbar-header">
          <p class="navbar-text">学生综合管理平台</p>
        </div>
        <table class="nav navbar-nav navbar-right">
              <tr><td class="column"><p class="navbar-text">当前用户：<%=username %></p></td>
                  
                  <% if(username.equals("aaaaaa")) {%>
                    <td><button type="button" class="btn btn-primary" style="margin-right:1.2em;" data-toggle="modal" data-target="#superUser" onClick="manageUser();">管理员</button></td>
                  <%} %>
                  
                  <td><button type="button" class="btn btn-primary" style="margin-right:1.2em;" data-toggle="modal" data-target="#userSetting" onClick="userInfo();">个人中心</button></td>
                  
                  <td><button type="submit" class="btn btn-success" onClick="logout();">退出</button></td></tr>
        </table>
      </div>
    </nav>
    
    <div class="container-bottom">
    
    
    
      <div class="nav-side">
        <div id="tree" style="max-height:50%; overflow:auto;"></div>
        <div class="hideTheDiv" id="optionSide" style="height:68%;">
          <div style="padding-top:7px; overflow:auto;">
            <table>
              <tr><td><button type="button" class="btn btn-default" onClick="deleteAll();">全删</button></td>
              
                  <td><button type="button" class="btn btn-default" onClick="clearSelected();">清空选择</button></td>
                  <td><button type="button" class="btn btn-default" data-toggle="modal" data-target="#commentStudentHis" onClick="commentStuAll();">统一评价</button></td>
                  
                  <td>
                      <div class="input-group-btn">
                        <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">操作<span class="caret"></span></button>
                          <ul class="dropdown-menu" role="menu">
                            <li><a class="category" data-toggle="modal" data-target="#smail" onClick="iniEmail();">群发邮件</a></li>
                            <li><a class="category" data-toggle="modal" data-target="#msg">群发短信</a></li>                                                                          
                          </ul>
                      </div><!-- /btn-group -->
                      </td>                       
                    
                    
                  <td><button type="button" class="btn btn-default" data-toggle="modal" data-target="#move" onClick="showTree();">移动</button></td>
              </tr>
            </table>
          </div>
          <div style="overflow:auto; height:85%;">
            <table style="width:100%;">
              <tr><td style="width:40%; text-align:center;"><p class="handle" onClick="sortByno();">学号</p></td><td style="width:20%; text-align:center;"><p class="handle" onClick="sortByname();">姓名</p></td>
                  <td style="width:40%; text-align:center;">操作</td></tr>
              <tbody id="selectedStudent">
                
              </tbody>
            </table>
          </div>
        </div>
      </div>
      
      <div class="content">
      
      
        <!--  
        <div class="nav-content">
          <div class="masthead">          
            <ul class="nav nav-justified">
              <li id="li1" class="active"><input type="button" value="列表" id="btn1" onClick="btnClick();"></li>
              <li id="li2"><input type="button" value="短信" id="btn2" onClick="btnClick();"></li>
              <li id="li3"><input type="button" value="邮件" id="btn3" onClick="btnClick();"></li>
              <li id="li4"><input type="button" value="个人中心" id="btn4" onClick="btnClick();"></li>
            </ul>
          </div>
        </div>
        -->
      
      
      <div id="content-list">
        <div class="search-box">        
          <table class="search-area" style="width:100%">
            <tr><td><button type="button" class="btn btn-default" onClick="prePage();" id="pre">前页</button></td>
                <td><button type="button" class="btn btn-default" onClick="nextPage();">后页</button></td>
                <td style="width:10em;"><div class="input-group">
                      <div class="input-group-btn">
                        <button type="button" class="btn btn-default" id="eachPageNum">每页条数</button>                          
                      </div><!-- /btn-group -->                      
                      <select class="form-control" style="width:5.4em;" id="perPage1" onchange="changePer();">
                        <option>20</option>
                        <option>50</option>
                        <option>100</option>
                        <option>200</option>
                      </select>                      
                    </div></td>
                <td><p style="padding-left:0.5em; padding-right:0.5em; height:1.2em; font-size:1.2em; margin:auto 0;" id="showPage">1/1</p></td>
                 <!--  
                 <td><button type="button" class="btn btn-default" data-toggle="modal" data-target="#addStudent">发送记录</button></td>
                 -->
                <td><button type="button" class="btn btn-default" data-toggle="modal" data-target="#addStudent">添加学生</button></td>
                
                
                <td><button type="button" class="btn btn-default" id="uploader">批量导入</button></td>
                <td><a href="download?file=student.xlsx" class="btn btn-default" role="button">导入模板</a></td>
                <!--
                <td style="width:25%;"><input type="text" class="form-control" id="searchInput" placeholder="搜索"></td>
                -->
                <td style="width:28%;"><div class="input-group">
                      <div class="input-group-btn">
                        <button type="button" class="btn btn-default dropdown-toggle" id="searchCategory" data-toggle="dropdown">按姓名<span class="caret"></span></button>
                          <ul class="dropdown-menu" role="menu">
                            <li><a class="category" id="category1" onClick="changeSearchCategory()">按姓名</a></li>
                            <li><a class="category" id="category2" onClick="changeSearchCategory()">按学号</a></li>
                            <li><a class="category" id="category3" onClick="changeSearchCategory()">按标签</a></li>
                            <li><a class="category" id="category4" onClick="changeSearchCategory()">按评价</a></li>                                                   
                          </ul>
                      </div><!-- /btn-group -->
                      <input type="text" class="form-control" id="searchText" style="min-width:6em;">
                      <select class="form-control" id="searchTag" class="searchTagClass" style="min-width:6em;"></select>                      
                    </div></td>
                    
                <td><button type="button" class="btn btn-default" style="float:right;" onClick="searchButton();">搜索</button></td>
            </tr>
          </table>
        </div>
        
        <div class="list-title">
          <table class="table table-striped">
            <tr>
                  <td style="width:4%;"><input type="checkbox" onClick="selectAll();" id="selectAllBox"></td>
                  <td style="width:10%; text-align:center;">学号</td>
                  <td style="width:12%; text-align:center;">姓名</td>
                  <td style="width:12%; text-align:center;">电话</td>
                  <td style="width:23%; text-align:center;">邮箱</td>
                  <td style="width:27%; text-align:center;" id="commentAndRemart">辅导员</td>
                  <td style="width:8%;">操作</td>
                </tr>
          </table>
        </div>
        
        <div class="list">
          <div class="table-responsive">          
            <table class="table table-striped">
              <tbody id="list-table">
              
                <!-- 
                <tr style="height:0;">
                  <td style="width:4%;"></td>
                  <td style="width:14%;"></td>
                  <td style="width:13%;"></td>
                  <td style="width:14%;"></td>
                  <td style="width:25%;"></td>
                  <td style="width:20%;"></td>
                  <td style="width:8%;"></td>
                </tr>
                -->
                
              </tbody>
            </table>
          </div>
        </div>
        
        </div><!-- end for content-list -->
        
        
        
        
        
        
      </div> <!-- end for content -->
      
      <!-- Modal -->
      <div class="modal fade" id="userSetting" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
        <div class="modal-dialog">
          <div class="modal-content">
            <div class="modal-header">
              <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
              <h4 class="modal-title" id="myModalLabel">个人中心</h4>
            </div>
            <div class="modal-body editStudentModal">
              <table class="table-bordered" style="width:98%; margin:auto;">
                <tr><td style="width:10%; text-align:center;">用户名</td>
                    <td style="width:10%; text-align:center;">姓名</td>
                    <td style="width:20%; text-align:center;">密码<button type="button" class="btn btn-default" onClick="changePsw();">修改</button></td>
                    <td style="width:20%; text-align:center;">电话<button type="button" class="btn btn-default" onClick="addPhone();">增加</button></td>
                    <td style="width:40%; text-align:center;">邮箱<button type="button" class="btn btn-default" onClick="addMail();">增加</button></td>
                </tr>
                <tbody id="infoTable">
                </tbody>
              </table>
            </div> 
            <div class="modal-footer">                      
              <button type="button" class="btn btn-success" data-dismiss="modal">确定</button>
            </div>          
          </div>
        </div>
      </div><!-- end for modal -->
      
      <!-- Modal -->
      <div class="modal fade" id="msg" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
        <div class="modal-dialog">
          <div class="modal-content">
            <div class="modal-header">
              <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
              <h4 class="modal-title" id="myModalLabel">发送短信</h4>
            </div>
            <div class="modal-body editStudentModal">
              <div>
                <textarea class="form-control" rows="10" id="msg-content"></textarea>
              </div>
            </div> 
            <div class="modal-footer">
              <button type="button" class="btn btn-primary" onClick="cancleSend();">清空</button>             
              <button type="button" class="btn btn-success" data-dismiss="modal" onClick="sendMsg();">发送</button>
            </div>         
          </div>
        </div>
      </div><!-- end for modal -->
      
        
      <!-- Modal -->
      <div class="modal fade" id="smail" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
        <div class="modal-dialog" style="width:100%;">
          <div class="modal-content">
            <div class="modal-header">
              <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
              <h4 class="modal-title" id="myModalLabel">发送邮件</h4>
            </div>
            <div class="modal-body editStudentModal">
              <div>
                <table style="width:100%;">
              		<tr><td style="width:3em;padding-bottom:1em;">主题：</td><td style="padding-bottom:1em;"><textarea class="form-control" rows="1" id="mailTopic"></textarea></td></tr>
              		<tr><td></td><td><div id="editor_id" style="border:1px solid #cccccc; height:30em;"></div></td></tr>
              		<!--  
             		<tr><td style="padding-top:1em;">附件:</td><td style="padding-top:1em;"><table style="width:50%;"><tr><td style="80%;"><input type="file" style="float:left; width:100%;"></td><td><input type="button" class="btn btn-default" value="删除" style="width:5em;"></td></tr></table></td></tr>
             		-->
           	    </table>                                    
              </div>
            </div>
            <div class="modal-footer">
              <button type="button" class="btn btn-primary" data-dismiss="modal">取消</button>                       
              <button type="button" class="btn btn-success" onClick="sendMail();">发送</button>
            </div>         
          </div>
        </div>
      </div><!-- end for modal -->
     
    </div>
    
  </body>
</html>