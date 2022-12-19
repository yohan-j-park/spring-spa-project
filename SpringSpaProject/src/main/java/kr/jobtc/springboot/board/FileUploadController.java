package kr.jobtc.springboot.board;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class FileUploadController {
	static String path="C:\\git-2208\\spring-spa-project\\SpringSpaProject\\src\\main\\resources\\static\\upload\\";
	
	@Autowired
	BoardService service;
	
	public FileUploadController() {}
	
	@RequestMapping("/board/board_insertR")
	public synchronized String insertR(@RequestParam("attFile")List<MultipartFile> mul,
						 @ModelAttribute BoardVo bVo, @ModelAttribute PageVo pVo) {
		String msg="";
		try {
			//본문 내용 저장
			boolean flag = service.insertR(bVo);
//			if(!flag) return "저장중 오류 발생";
			if(flag) {
				msg = "정상적으로 저장되었습니다.";
				for(MultipartFile m : mul) {
					if(!m.isEmpty()) {
						List<AttVo> attList = new ArrayList<AttVo>();
						//FileUpload 불러와야함
						attList = fileUpload(mul);
						service.insertAttList(attList);					
					}
				}
			}else msg = "저장중 오류 발생";
			
			
		}catch(Exception e) {
			e.printStackTrace();
		}

		return msg;		
		
	}
	
	@RequestMapping("/board/board_updateR")
	public String updateR(@RequestParam("attFile")List<MultipartFile> mul,
						 @ModelAttribute BoardVo bVo, @ModelAttribute PageVo pVo, @RequestParam(name = "delFile", required = false, defaultValue="") String[] delFile) {		// attFile이라는 파라미터가 들어온 경우에는 MultipartFile로 받고 나머지는 BoardVo로 받는다?
		// getter setter가 있을 때 ModelAttribute 사용, String같이 기본형일 때 RequestParam 사용
		String msg="";
		boolean flag = service.updateR(bVo,delFile);
		if(flag) {
			msg = "정상적으로 수정되었습니다.";
			try {		
				List<AttVo> attList = fileUpload(mul);
				bVo.setAttList(attList);				
				
			}catch(Exception e) {
				e.printStackTrace();
			}
			
		}else msg="수정중 오류 발생";
		
		return msg;
	}
	
	@RequestMapping("/board/board_replR")
	public synchronized String replR(@RequestParam("attFile")List<MultipartFile> mul,
						 @ModelAttribute BoardVo bVo, @ModelAttribute PageVo pVo) {
		String msg="";
		
		//본문 내용 저장
		boolean flag = service.replR(bVo);
		if(flag) {
			msg="댓글 작성 완료";
			try {
				List<AttVo> attList = new ArrayList<AttVo>();
				attList = fileUpload(mul);
				bVo.setAttList(attList);
				
			}catch(Exception e) {
				e.printStackTrace();
			}			
		}else msg="리플 작성 중 오류 발생";

		return msg;		//파일 업로드 누르고 다시 select Page로 이동하게 하는 코드
		
	}	
	
	
	//파일 업로드 공통부분(중복코드 제거: insertR, updateR, replR에서 공통으로 들어감)
	public List<AttVo> fileUpload(List<MultipartFile> mul) throws Exception{
		List<AttVo> attList = new ArrayList<AttVo>();
		for(MultipartFile m : mul) {
			if(m.isEmpty()) continue;
			UUID uuid = UUID.randomUUID();
			String oriFile = m.getOriginalFilename();
			String sysFile = ""; 
			File temp = new File(path + oriFile); 	// 임시저장 경로
			m.transferTo(temp);
			sysFile =(uuid.getLeastSignificantBits()*-1) + "-" + oriFile;
			File f = new File(path + sysFile);
			temp.renameTo(f);
			
			AttVo attVo = new AttVo();
			attVo.setOriFile(oriFile);
			attVo.setSysFile(sysFile);
			
			attList.add(attVo);
			System.out.println(m.getOriginalFilename());
			System.out.println(uuid.getLeastSignificantBits());
		}
		
		return attList;
	}
}
