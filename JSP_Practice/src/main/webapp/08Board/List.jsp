<%@ page import="java.util.List"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.Map"%>
<%@ page import="board.BoardDAO"%>
<%@ page import="board.BoardDTO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
// jsp 페이지서 함수 실행 및 자바 코드 들어가 있음


BoardDAO dao = new BoardDAO(application);

// map에 검색어를 담아서 dao로 가져다 준다.
Map<String, Object> map = new HashMap<>();
String searchField = request.getParameter("searchField");
String searchWord = request.getParameter("searchWord");

//검색어가 있다면 해당 필드(검색한 분야) 및 검색어를 map에 put해서 보내줌
if(searchWord != null)
{
	 map.put("searchField", searchField);
	 map.put("searchWord", searchWord);
}

//dao객체에서 만들어진 selectCount 함수 이용-게시물 수 확인
int cnt =  dao.selectCount(map);
// 목록 가져오기(select)- 참고로 db에서 커밋한다음 온전한 데이터 들어오는 듯 함
List<BoardDTO> list = dao.selectList(map);


dao.close();
%>    
    
<!DOCTYPE html>
<html>
<head>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.0/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-gH2yIJqKdNHPEq0n4Mqa/HGKIhSkIHeL5AyhkYV8i59U5AR6csBvApHHNl/vI1Bx" crossorigin="anonymous">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.0/dist/js/bootstrap.bundle.min.js" integrity="sha384-A3rJD856KowSb7dwlZdYEkO39Gagi7vIsF0jrRAoQmDKKtQBHUuLZ9AsSv4jD4Xa" crossorigin="anonymous"></script>
<meta charset="UTF-8">
<title>Insert title here</title>
<style>

th, td {
  text-align: center;
}
.btn btn-primary {
   float: right;
}
    </style>
</head>
<body>

<jsp:include page="../Common/Link.jsp" />  <!-- 공통 링크 -->

    <h2>자유게시판</h2>
    <!-- 검색폼 --> 
    <form method="get">  
    <table  width="90%">
    <tr>
        <td align="center">
            <select name="searchField"> 
                <option value="title">제목</option> 
                <option value="content">내용</option>
            </select>
            <input type="text" name="searchWord" />

            <button type="submit" class="btn btn-primary" >검색</button>
           
        </td>
    </tr>   
    </table>
    </form>
    <!-- 게시물 목록 테이블(표) --> 
    
    <table class="table table-hover">
        <!-- 각 칼럼의 이름 --> 
        <tr>
            <th>번호</th>
            <th >제목</th>
            <th >작성자</th>
            <th >조회수</th>
            <th >작성일</th>
        </tr>
        <!-- 목록의 내용 --> 
<%
if (list.isEmpty()) {
    // 게시물이 하나도 없을 때 
%>
        <tr>
            <td colspan="5" align="center">
                등록된 게시물이 없습니다. 게시물을 등록해 주세요
            </td>
        </tr>
<%
}
else {
    // 게시물이 있을 때 
    int virtualNum = 0;  // 화면상에서의 게시물 번호
    for (BoardDTO dto : list)
    {
        virtualNum = cnt--;  // 전체 게시물 수에서 시작해 1씩 감소
%>
        <tr align="center">
            <td><%= virtualNum %></td>  <!--게시물 번호-->
            <td align="left">  <!--제목(+ 하이퍼링크)-->
                <a href="View.jsp?num=<%= dto.getNum() %>"><%= dto.getTitle() %></a> 
            </td>
            <td align="center"><%= dto.getId() %></td>          <!--작성자 아이디-->
            <td align="center"><%= dto.getVisitcount() %></td>  <!--조회수-->
            <td align="center"><%= dto.getPostdate() %></td>    <!--작성일-->
        </tr>
<%
    }
}
%>
    </table>
    <!--목록 하단의 [글쓰기] 버튼-->
   
           <button class="btn btn-primary"  style="float: right;" onclick="location.href='Write.jsp';">글쓰기
                </button>
       

</body>
</html>