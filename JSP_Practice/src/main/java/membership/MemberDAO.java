package membership;

import common.JDBConnect;

public class MemberDAO extends JDBConnect {
    // ����� �����ͺ��̽����� ������ �Ϸ�� memberDAO ��ü�� �����մϴ�.
    public MemberDAO(String drv, String url, String id, String pw) {
        super(drv, url, id, pw);
    }

    // ����� ���̵�/ �н������ �����ϴ� ȸ�� ������ ��ȯ�մϴ�
    public MemberDTO getMemberDTO(String uid, String upass) {
        MemberDTO dto = new MemberDTO();  // ȸ�� ���� dto ��ü ����
        String query = "SELECT * FROM member WHERE id=? AND pass=?";  // ������ ���ø�

        try {
            // ���� ����
            psmt = con.prepareStatement(query); // ���� ������ �غ�
            psmt.setString(1, uid);    // �������� ù ��° ���Ķ���Ϳ� �� ����
            psmt.setString(2, upass);  // �������� �� ��° ���Ķ���Ϳ� �� ���� 
            rs = psmt.executeQuery();  // ������ ����

            // ���ó��
            if (rs.next()) {
                // ���� ����� ���� ȸ�� ������ dto ��ü�� ����
                dto.setId(rs.getString("id"));
                dto.setPass(rs.getString("pass"));
                dto.setName(rs.getString(3));
                dto.setRegidate(rs.getString(4));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return dto;  // DTO ��ü ��ȯ
    }
}
