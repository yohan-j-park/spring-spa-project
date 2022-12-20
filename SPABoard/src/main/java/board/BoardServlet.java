package board;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = "/board/board.do")
public class BoardServlet extends HttpServlet{
	HttpServletRequest req;
	HttpServletResponse resp;
	
//	String job="select";
	RequestDispatcher rd;
	PageVo pVo;
	BoardVo bVo;
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.req = req;
		this.resp = resp;
		pVo = new PageVo();
		select();
	}
	
	  @Override
	    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	        this.req = req;
	        this.resp = resp;
	        String job = req.getParameter("job");
	        System.out.println("BoardServlet.doPost : " + job);
	       
	        pVo = (PageVo)req.getAttribute("pVo");
	        bVo = (BoardVo)req.getAttribute("bVo");
	       
	        switch(job) {
	        case "select":
	        	select();  
	        	break;
	        case "insert":
	        	insert();  
	        	break;
	        case "view":    
	        	view();   
	        	break;
	        case "update":  
	        	update();  
	        	break;
	        case "repl":    
	        	repl();    
	        	break;
	        case "deleteR": 
	        	deleteR(); 
	        	break;
	        }
	        /*
	        System.out.println("----------");
	        System.out.println("nowPage : " + pVo.getNowPage());
	        System.out.println("findStr : " + pVo.getFindStr());
	        System.out.println("id : " + bVo.getId());
	        System.out.println("subject : " + bVo.getSubject());
	        System.out.println("doc : " + bVo.getDoc());
	        */
	       
	    }
	
	 protected void select() throws ServletException, IOException {
	        BoardDao dao = new BoardDao();
	        List<BoardVo> list = dao.select(pVo);
	        req.setAttribute("list", list);
	       
	        // 기준 페이지 정보에서 pVo가 갱신되었기 때문에 다시 dao로 부터 읽어 재설정
	        req.setAttribute("pVo", dao.getpVo());
	       
	        rd = req.getRequestDispatcher("select.jsp");
	        rd.include(req, resp);
	    }
	
	 protected void insert() throws ServletException, IOException {
	        rd = req.getRequestDispatcher("insert.jsp");
	        rd.include(req, resp);
	    }
	
	 protected void view() throws ServletException, IOException {
	        BoardDao dao = new BoardDao();
	        //조회수 증가
	        bVo = dao.view(bVo.getSno(), "up");
	       
	        // doc안에 있는 \n 기호를 <br/>로 변경
	        String temp = bVo.getDoc();
	        bVo.setDoc(temp.replace("\n", "<br/>"));
	        req.setAttribute("bVo", bVo);
	        rd = req.getRequestDispatcher("view.jsp");
	        rd.include(req, resp);
	    }
	 
	 protected void update() throws ServletException, IOException {
	        BoardDao dao = new BoardDao();
	        bVo = dao.view(bVo.getSno(), null);
	        req.setAttribute("bVo", bVo);
	        rd = req.getRequestDispatcher("update.jsp");
	        rd.include(req, resp);
	    }
	 
	    protected void deleteR() throws ServletException, IOException {
	        BoardDao dao = new BoardDao();
	        boolean b = dao.delete(bVo);
	       
	        System.out.println("servlet.deleteR : " + b);
	       
	        if(!b) {
	            PrintWriter out = resp.getWriter();
	            out.print("<script>alert('fail')</script>");
	        }
	        select();
	    }
	    
	    protected void repl() throws ServletException, IOException {
	        rd = req.getRequestDispatcher("repl.jsp");
	        rd.include(req, resp);
	    }
}
