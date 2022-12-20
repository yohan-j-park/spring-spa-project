package board;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
@MultipartConfig(
    location = "c:/temp",
    maxFileSize = 1024*1024*10, /* 10Mb */
    maxRequestSize = -1,
    fileSizeThreshold = 1024*1024 /* 1Mb */
)
@WebServlet(urlPatterns = "/board/fileUpload.do")
public class FileUploadServlet extends HttpServlet {
    public final static String uploadPath = "C:\\Users\\p_yoh\\eclipse-workspace\\SPABoard\\src\\main\\webapp\\upload\\";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        BoardDao dao = new BoardDao();
        PrintWriter out = resp.getWriter();
        BoardVo bVo = new BoardVo();
        PageVo pVo = new PageVo();
        List<AttVo> attList = new ArrayList<AttVo>();	//첨부 파일의 목록
        String[] delFiles = {};
        RequestDispatcher rd=null;
        String msg="";
        String job="";
        boolean flag=true; // 입력, 수정, 댓글 처리 결과
       
        // 파일 업로드
        Collection<Part> parts = req.getParts();
        for(Part p : parts) {
            if(p.getHeader("Content-Disposition").contains("filename=")){
                if(p.getSize()>0) {
                    long time = Calendar.getInstance().getTimeInMillis();
                    String sysFile = time + "-" + p.getSubmittedFileName();
                    String oriFile = p.getSubmittedFileName();
                    AttVo aVo = new AttVo();
                    aVo.setOriFile(oriFile);
                    aVo.setSysFile(sysFile);
                    attList.add(aVo);
                   
                    p.write(uploadPath + sysFile);
                    p.delete();
                   
                }
            }
        }
       
        job = req.getParameter("job");
       
        //BoardVo 만들기
        String id = (req.getParameter("id"));
        String subject = (req.getParameter("subject"));
        String doc = req.getParameter("doc");
        bVo.setId(id);
        bVo.setSubject(subject);
        bVo.setDoc(doc);
        bVo.setAttList(attList);
        if(req.getParameter("delFile") != null) {
            delFiles = req.getParameterValues("delFile");
        }
       
        // PageVo 만들기
        String findStr = req.getParameter("findStr");
        int nowPage = Integer.parseInt(req.getParameter("nowPage"));
        pVo.setFindStr(findStr);
        pVo.setNowPage(nowPage);
       
        // BoardDao에 호출
        switch(job) {
        case "insertR":
            flag = dao.insert(bVo);
            break;
        case "updateR":
            int sno = Integer.parseInt(req.getParameter("sno"));
            bVo.setSno(sno);
            flag  = dao.update(bVo, delFiles);
       
            break;
        case "replR":
            bVo.setGrp(Integer.parseInt(req.getParameter("grp")) );
            bVo.setSeq(Integer.parseInt(req.getParameter("seq")) );
            bVo.setDeep(Integer.parseInt(req.getParameter("deep")) );
            flag = dao.repl(bVo);
            break;
        }
    
        if( !flag) {
            out.print("<script>");
            out.print("   alert('자료 처리중 오류 발생') ");
            out.print("</script>");
        }
       
        //결과 저장.
        req.setAttribute("pVo", pVo);
       
        rd = req.getRequestDispatcher("board_main.jsp?job=select");
        rd.include(req, resp);
    }
}