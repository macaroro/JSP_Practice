package fileupload;

import java.util.List;
import java.util.Vector;

import common.DBConnPool;

public class MyfileDAO extends DBConnPool {
	// 새로운 게시물 입력
    public int insertFile(MyfileDTO dto) {
        int applyResult = 0;
        try {
            String query = "INSERT INTO myfile ( "
                + " idx, name, title, cate, ofile, sfile) "
                + " VALUES ( "
                + " seq_board_num.nextval, ?, ?, ?, ?, ?)";
                        
            psmt = con.prepareStatement(query);
            psmt.setString(1, dto.getName());
            psmt.setString(2, dto.getTitle());
            psmt.setString(3, dto.getCate());
            psmt.setString(4, dto.getOfile());
            psmt.setString(5, dto.getSfile());
        
            applyResult = psmt.executeUpdate();
        }
        catch (Exception e) {
            System.out.println("INSERT 以� �삁�쇅 諛쒖깮");
            e.printStackTrace();
        }        
        return applyResult;
    }

    // 게시물 목록
    public List<MyfileDTO> myFileList() {
        List<MyfileDTO> fileList = new Vector<MyfileDTO>();

        
        String query = "SELECT * FROM myfile ORDER BY idx DESC";
        try {
            psmt = con.prepareStatement(query);  
            rs = psmt.executeQuery();  

            while (rs.next()) {  
                MyfileDTO dto = new MyfileDTO();
                dto.setIdx(rs.getString(1));
                dto.setName(rs.getString(2));
                dto.setTitle(rs.getString(3));
                dto.setCate(rs.getString(4));
                dto.setOfile(rs.getString(5));
                dto.setSfile(rs.getString(6));
                dto.setPostdate(rs.getString(7));
                
                fileList.add(dto);  
            }
        }
        catch (Exception e) {
            System.out.println("SELECT 성공");
            e.printStackTrace();
        }        
        
        return fileList;  
    }
}