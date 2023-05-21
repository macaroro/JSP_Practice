package board;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import javax.servlet.ServletContext;
import common.JDBConnect;

public class BoardDAO extends JDBConnect {
	
    public BoardDAO(ServletContext application) {
        super(application);
    }

    // 검색 조건이 맞는 게시물의 개수를 반환
    public int selectCount(Map<String, Object> map) {
        int totalCount = 0; // 게시물 수의 변수

        // 게시물 수를 얻어오는 쿼리문 작성
        String query = "SELECT COUNT(*) FROM board";
        // 검색 조건이 들어있다면 해당 부분의 쿼리 추가
        if (map.get("searchWord") != null) {
        	// searchField=> 검색한 주제
            query += " WHERE " + map.get("searchField") + " "
                   + " LIKE '%" + map.get("searchWord") + "%'";
        }

        try {
            stmt = con.createStatement();   // 쿼리문 생성
            rs = stmt.executeQuery(query);  // 쿼리 실행
            rs.next();  // 커서를 첫 번째 생으로 이동
            totalCount = rs.getInt(1);  // 첫번째 컬럼 값을 가져옴
        }
        catch (Exception e) {
            System.out.println("게시물 수를 구하는 중 예외 발생");
            e.printStackTrace();
        }

        return totalCount; 
    }
    
    // 게시물의 목록을 가져오는 것
//    public List<BoardDTO> selectList(Map<String, Object> map) { 
//        List<BoardDTO> bbs = new ArrayList<BoardDTO>();  //게시물을 담아놓는 변수
//
//        String query = "SELECT * FROM board "; 
//        if (map.get("searchWord") != null) {
//            query += " WHERE " + map.get("searchField") + " "
//                   + " LIKE '%" + map.get("searchWord") + "%' ";
//        }
//        query += " ORDER BY num DESC "; 
//
//        try {
//            stmt = con.createStatement();   //쿼리문 생성
//            rs = stmt.executeQuery(query);  // 쿼리문 실행
//
//            while (rs.next()) {  // 나온 결과 만큼 계속 반복
//                // 실행된 쿼리문의 한 행을 저장
//                BoardDTO dto = new BoardDTO(); 
//
//                dto.setNum(rs.getString("num"));          // 번호
//                dto.setTitle(rs.getString("title"));      // 제목
//                dto.setContent(rs.getString("content"));  // 내용
//                dto.setPostdate(rs.getDate("postdate"));  // 게시날짜
//                dto.setId(rs.getString("id"));            // 글쓴이
//                dto.setVisitcount(rs.getString("visitcount"));  // 조회수
//
//                bbs.add(dto);  // 만들어진 목록 변수에 저장
//            }
//        } 
//        catch (Exception e) {
//            System.out.println("계시물 조회시 예외 발생");
//            e.printStackTrace();
//        }
//
//        return bbs;
//    }
    
    //rownum을 이용한 select 
    public List<BoardDTO> selectList(Map<String, Object> map) { 
      List<BoardDTO> bbs = new ArrayList<BoardDTO>();  //게시물을 담아놓는 변수

      String query = "SELECT * FROM ("
    		       + " SELECT tb.*, ROWNUM rnum FROM ("
                   + " SELECT * FROM board";
                   
       
      if (map.get("searchWord") != null) {
          query += " WHERE " + map.get("searchField") + " "
                 + " LIKE '%" + map.get("searchWord") + "%' ";
      }
      query += " ORDER BY num DESC "
    		 +"  )tb "
    		 + " ) "
    		 + " WEHRE rnum BETWEEN ? AND ?";    		  

      try {
          psmt = con.prepareStatement(query);   //쿼리문 생성
          psmt.setString(1, map.get("start").toString());
          psmt.setString(2, map.get("end").toString());
          
          rs = psmt.executeQuery(query);  // 쿼리문 실행

          while (rs.next()) {  // 나온 결과 만큼 계속 반복
              // 실행된 쿼리문의 한 행을 저장
              BoardDTO dto = new BoardDTO(); 

              dto.setNum(rs.getString("num"));          // 번호
              dto.setTitle(rs.getString("title"));      // 제목
              dto.setContent(rs.getString("content"));  // 내용
              dto.setPostdate(rs.getDate("postdate"));  // 게시날짜
              dto.setId(rs.getString("id"));            // 글쓴이
              dto.setVisitcount(rs.getString("visitcount"));  // 조회수

              bbs.add(dto);  // 만들어진 목록 변수에 저장
          }
      } 
      catch (Exception e) {
          System.out.println("계시물 조회시 예외 발생");
          e.printStackTrace();
      }

      return bbs;
  }

    // 게시글 데이터를 받아 데이터 베이스에 추가
    public int insertWrite(BoardDTO dto) {
        int result = 0;
        
        try {
            // 쿼리문
            String query = "INSERT INTO board ( "
                         + " num,title,content,id,visitcount) "
                         + " VALUES ( "
                         + " seq_board_num.NEXTVAL, ?, ?, ?, 0)";  

            psmt = con.prepareStatement(query);  // 동적 쿼리
            psmt.setString(1, dto.getTitle());  //제목
            psmt.setString(2, dto.getContent());//내용
            psmt.setString(3, dto.getId());  //아이디
            
            result = psmt.executeUpdate(); 
        }
        catch (Exception e) {
            System.out.println("글쓰기 실패");
            e.printStackTrace();
        }
        
        return result;
    }


    // 저장한 글 상세보기
    public BoardDTO selectView(String num) { 
        BoardDTO dto = new BoardDTO();
        
        // 쿼리
        String query = "SELECT B.*, M.name " 
                     + " FROM member M INNER JOIN board B " 
                     + " ON M.id=B.id "
                     + " WHERE num=?";

        try {
            psmt = con.prepareStatement(query);
            psmt.setString(1, num);    // 받아오는 id값
            rs = psmt.executeQuery();  // 결과

            // 결과 처리
            if (rs.next()) {
                dto.setNum(rs.getString(1)); 
                dto.setTitle(rs.getString(2));
                dto.setContent(rs.getString("content"));
                dto.setPostdate(rs.getDate("postdate"));
                dto.setId(rs.getString("id"));
                dto.setVisitcount(rs.getString(6));
                dto.setName(rs.getString("name")); 
            }
        } 
        catch (Exception e) {
            System.out.println("상세보기 실패");
            e.printStackTrace();
        }
        
        return dto; 
    }
    //  조회수 수정
    public void updateVisitCount(String num) { 
        // 해당 번호의 조회수를 +1함 
        String query = "UPDATE board SET "
                     + " visitcount=visitcount+1 "
                     + " WHERE num=?";
        
        try {
            psmt = con.prepareStatement(query);
            psmt.setString(1, num);  // 글의 번호 가져오기
            psmt.executeQuery();     // 실행
        } 
        catch (Exception e) {
            System.out.println("조회수 증가 실패");
            e.printStackTrace();
        }
    }
    
    
    // 게시물 수정
    public int updateEdit(BoardDTO dto) { 
        int result = 0;
        
        try {
            // 해당 일련번호를 받아서 수정
            String query = "UPDATE board SET "
                         + " title=?, content=? "
                         + " WHERE num=?";
            
           
            psmt = con.prepareStatement(query);
            psmt.setString(1, dto.getTitle());
            psmt.setString(2, dto.getContent());
            psmt.setString(3, dto.getNum());
            
            // 실행
            result = psmt.executeUpdate();
        } 
        catch (Exception e) {
            System.out.println("게시물 수정 실패");
            e.printStackTrace();
        }
        
        return result; // 결과로 수정되 행의 수를 반환
    }

    // 게시물 삭제
    public int deletePost(BoardDTO dto) { 
        int result = 0;

        try {
            // 번호를 받아 해당 게시물 삭제
            String query = "DELETE FROM board WHERE num=?"; 

           
            psmt = con.prepareStatement(query); 
            psmt.setString(1, dto.getNum()); 

           
            result = psmt.executeUpdate(); 
        } 
        catch (Exception e) {
            System.out.println("삭제 실패");
            e.printStackTrace();
        }
        
        return result; // 결과 반환(int)-삭제한 행의 개수
    }
    
    // 寃��깋 議곌굔�뿉 留욌뒗 寃뚯떆臾� 紐⑸줉�쓣 諛섑솚�빀�땲�떎(�럹�씠吏� 湲곕뒫 吏��썝).
    public List<BoardDTO> selectListPage(Map<String, Object> map) {
        List<BoardDTO> bbs = new Vector<BoardDTO>();  // 寃곌낵(寃뚯떆臾� 紐⑸줉)瑜� �떞�쓣 蹂��닔
        
        // 荑쇰━臾� �뀥�뵆由�  
        String query = " SELECT * FROM ( "
                     + "    SELECT Tb.*, ROWNUM rNum FROM ( "
                     + "        SELECT * FROM board ";

        // 寃��깋 議곌굔 異붽� 
        if (map.get("searchWord") != null) {
            query += " WHERE " + map.get("searchField")
                   + " LIKE '%" + map.get("searchWord") + "%' ";
        }
        
        query += "      ORDER BY num DESC "
               + "     ) Tb "
               + " ) "
               + " WHERE rNum BETWEEN ? AND ?"; 

        try {
            // 荑쇰━臾� �셿�꽦 
            psmt = con.prepareStatement(query);
            psmt.setString(1, map.get("start").toString());
            psmt.setString(2, map.get("end").toString());
            
            // 荑쇰━臾� �떎�뻾 
            rs = psmt.executeQuery();
            
            while (rs.next()) {
                // �븳 �뻾(寃뚯떆臾� �븯�굹)�쓽 �뜲�씠�꽣瑜� DTO�뿉 ���옣
                BoardDTO dto = new BoardDTO();
                dto.setNum(rs.getString("num"));
                dto.setTitle(rs.getString("title"));
                dto.setContent(rs.getString("content"));
                dto.setPostdate(rs.getDate("postdate"));
                dto.setId(rs.getString("id"));
                dto.setVisitcount(rs.getString("visitcount"));

                // 諛섑솚�븷 寃곌낵 紐⑸줉�뿉 寃뚯떆臾� 異붽�
                bbs.add(dto);
            }
        } 
        catch (Exception e) {
            System.out.println("寃뚯떆臾� 議고쉶 以� �삁�쇅 諛쒖깮");
            e.printStackTrace();
        }
        
        // 紐⑸줉 諛섑솚
        return bbs;
    }

}
