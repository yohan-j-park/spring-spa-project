package kr.jobtc.springboot.guestbook;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class GuestbookController {
	
	@Autowired
	GuestbookDao dao; 	//
	
	@RequestMapping("/guestbook/guestbook_select")
	public ModelAndView select(GPageVo gVo) {
		ModelAndView mv = new ModelAndView();
		//service or dao
		System.out.println("now Page: " + gVo.getNowPage());
		System.out.println("tot Page: " + gVo.getTotPage());
		System.out.println("dao : " + dao);

		//검색어에 따른 List를 가져오기
		List<GuestbookVo> list = dao.select(gVo);
		
		//가져온 List를 mv에 추가
		gVo = dao.getgVo();		//새로 갱신된 페이지 정보
		mv.addObject("gVo",gVo);	//페이징처리 이후의 정보
		mv.addObject("list",list);
		mv.setViewName("guestbook/guestbook_select");	
		// view에 "guestbook/guestbook_select"를 담았기 때문에 
		// application.properties에서 mcv.view ~에서 접두사 접미사가 붙어 페이지url이 완성된다.
		return mv;
	}
	
	
	
	@RequestMapping("/guestbook/guestbook_insert")
	public String insert(GuestbookVo vo, HttpServletResponse resp) {		//form tag에 있는 setter들이 모두 돌아서 적용됨
		// 확장성을 고려하여 이곳에 mapper를 호출하지 않는다.
		boolean b = dao.insert(vo);
		String msg="";
			if(!b) msg="저장중 오류 발생";
		return msg;
	}
	
	@RequestMapping("/guestbook/guestbook_delete")
	public String delete(GuestbookVo vo, HttpServletResponse resp) {
		String msg="";
		boolean b = dao.delete(vo);
			if(!b) msg="삭제중 오류 발생";
			
		return msg;
	}
	
	@RequestMapping("/guestbook/guestbook_update")
	public String update(GuestbookVo vo, HttpServletResponse resp) {
		String msg="";
		boolean b = dao.update(vo);
			if(!b) msg="수정중 오류 발생";

		return msg;
	}
	
	@RequestMapping("/guestbook/guestbook10")
	public ModelAndView guestbook10(){
		ModelAndView mv = new ModelAndView();
		List<GuestbookVo> guestbook10 = dao.guestbook10();
		mv.addObject("guestbook10",guestbook10);
		mv.setViewName("guestbook/guestbook10");	
		return mv;
	}
}

