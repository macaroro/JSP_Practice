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
    public List<BoardDTO> selectList(Map<String, Object> map) { 
        List<BoardDTO> bbs = new ArrayList<BoardDTO>();  //게시물을 담아놓는 변수

        String query = "SELECT * FROM board "; 
        if (map.get("searchWord") != null) {
            query += " WHERE " + map.get("searchField") + " "
                   + " LIKE '%" + map.get("searchWord") + "%' ";
        }
        query += " ORDER BY num DESC "; 

        try {
            stmt = con.createStatement();   //쿼리문 생성
            rs = stmt.executeQuery(query);  // 쿼리문 실행

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

    // 寃뚯떆湲� �뜲�씠�꽣瑜� 諛쏆븘 DB�뿉 異붽��빀�땲�떎. 
    public int insertWrite(BoardDTO dto) {
        int result = 0;
        
        try {
            // INSERT 荑쇰━臾� �옉�꽦 
            String query = "INSERT INTO board ( "
                         + " num,title,content,id,visitcount) "
                         + " VALUES ( "
                         + " seq_board_num.NEXTVAL, ?, ?, ?, 0)";  

            psmt = con.prepareStatement(query);  // �룞�쟻 荑쇰━ 
            psmt.setString(1, dto.getTitle());  
            psmt.setString(2, dto.getContent());
            psmt.setString(3, dto.getId());  
            
            result = psmt.executeUpdate(); 
        }
        catch (Exception e) {
            System.out.println("寃뚯떆臾� �엯�젰 以� �삁�쇅 諛쒖깮");
            e.printStackTrace();
        }
        
        return result;
    }


    // 吏��젙�븳 寃뚯떆臾쇱쓣 李얠븘 �궡�슜�쓣 諛섑솚�빀�땲�떎.
    public BoardDTO selectView(String num) { 
        BoardDTO dto = new BoardDTO();
        
        // 荑쇰━臾� 以�鍮�
        String query = "SELECT B.*, M.name " 
                     + " FROM member M INNER JOIN board B " 
                     + " ON M.id=B.id "
                     + " WHERE num=?";

        try {
            psmt = con.prepareStatement(query);
            psmt.setString(1, num);    // �씤�뙆�씪誘명꽣瑜� �씪�젴踰덊샇濡� �꽕�젙 
            rs = psmt.executeQuery();  // 荑쇰━ �떎�뻾 

            // 寃곌낵 泥섎━
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
            System.out.println("寃뚯떆臾� �긽�꽭蹂닿린 以� �삁�쇅 諛쒖깮");
            e.printStackTrace();
        }
        
        return dto; 
    }

    // 吏��젙�븳 寃뚯떆臾쇱쓽 議고쉶�닔瑜� 1 利앷��떆�궢�땲�떎.
    public void updateVisitCount(String num) { 
        // 荑쇰━臾� 以�鍮� 
        String query = "UPDATE board SET "
                     + " visitcount=visitcount+1 "
                     + " WHERE num=?";
        
        try {
            psmt = con.prepareStatement(query);
            psmt.setString(1, num);  // �씤�뙆�씪誘명꽣瑜� �씪�젴踰덊샇濡� �꽕�젙 
            psmt.executeQuery();     // 荑쇰━ �떎�뻾 
        } 
        catch (Exception e) {
            System.out.println("寃뚯떆臾� 議고쉶�닔 利앷� 以� �삁�쇅 諛쒖깮");
            e.printStackTrace();
        }
    }
    
    // 吏��젙�븳 寃뚯떆臾쇱쓣 �닔�젙�빀�땲�떎.
    public int updateEdit(BoardDTO dto) { 
        int result = 0;
        
        try {
            // 荑쇰━臾� �뀥�뵆由� 
            String query = "UPDATE board SET "
                         + " title=?, content=? "
                         + " WHERE num=?";
            
            // 荑쇰━臾� �셿�꽦
            psmt = con.prepareStatement(query);
            psmt.setString(1, dto.getTitle());
            psmt.setString(2, dto.getContent());
            psmt.setString(3, dto.getNum());
            
            // 荑쇰━臾� �떎�뻾 
            result = psmt.executeUpdate();
        } 
        catch (Exception e) {
            System.out.println("寃뚯떆臾� �닔�젙 以� �삁�쇅 諛쒖깮");
            e.printStackTrace();
        }
        
        return result; // 寃곌낵 諛섑솚 
    }

    // 吏��젙�븳 寃뚯떆臾쇱쓣 �궘�젣�빀�땲�떎.
    public int deletePost(BoardDTO dto) { 
        int result = 0;

        try {
            // 荑쇰━臾� �뀥�뵆由�
            String query = "DELETE FROM board WHERE num=?"; 

            // 荑쇰━臾� �셿�꽦
            psmt = con.prepareStatement(query); 
            psmt.setString(1, dto.getNum()); 

            // 荑쇰━臾� �떎�뻾
            result = psmt.executeUpdate(); 
        } 
        catch (Exception e) {
            System.out.println("寃뚯떆臾� �궘�젣 以� �삁�쇅 諛쒖깮");
            e.printStackTrace();
        }
        
        return result; // 寃곌낵 諛섑솚
    }
}
