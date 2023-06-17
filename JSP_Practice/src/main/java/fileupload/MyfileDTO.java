package fileupload;

public class MyfileDTO {
    // 변수
    private String idx; 
    private String name;   // 작성자
    private String title;  // 제목
    private String cate;   // 카테고리
    private String ofile;  // 원본파일
    private String sfile;  // 저장된 파일
    private String postdate; // 작성날짜

    // getter/setter
    public String getIdx() { 
        return idx;
    }
    public void setIdx(String idx) { 
        this.idx = idx;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getCate() {
        return cate;
    }
    public void setCate(String cate) {
        this.cate = cate;
    }
    public String getOfile() {
        return ofile;
    }
    public void setOfile(String ofile) {
        this.ofile = ofile;
    }
    public String getSfile() {
        return sfile;
    }
    public void setSfile(String sfile) {
        this.sfile = sfile;
    }
    public String getPostdate() {
        return postdate;
    }
    public void setPostdate(String postdate) {
        this.postdate = postdate;
    }
}