package board;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.apache.ibatis.session.SqlSession;

import mybatis.SpaFactory;

public class BoardDao {
    SqlSession session;
    PageVo pVo;

    public BoardDao() {
       session = SpaFactory.getFactory().openSession();
    }
    public List<BoardVo> select(PageVo pVo){
	       
	        //페이지 정보를 재설정해 주기위한 작업
	        int totSize = session.selectOne("board.totList", pVo);
	        pVo.setTotSize(totSize);
	        this.pVo = pVo;
	        List<BoardVo> list = session.selectList("board.select", pVo);
	        session.close();
	        return list;
	    }
    
    public synchronized boolean insert(BoardVo vo) {	
    	//입력할 때 시리얼 넘버가 1씩 증가하는데 두 명 이상이 동시에 작업할 수 없게 하기 위해서 synchronized를 달았음
        boolean b=true;
           
        int cnt = session.insert("board.insert", vo);
        if(cnt<1) {
            b=false;
        }else if(vo.getAttList().size()>0) {
            int attCnt = session.insert("board.attInsert", vo.getAttList());
            if(attCnt<1) b=false;
           
        }
       
        if(b) session.commit();
        else  session.rollback();
        session.close();
           
        return b;
    }

    public BoardVo view(int sno, String up) {
        BoardVo bVo = null;
        if(up !=null && up.equals("up")) {//상세보기인 경우만
            session.update("board.hitUp", sno);
            session.commit();
        }
        bVo = session.selectOne("board.view", sno);   
        List<AttVo> attList = session.selectList("board.attList", sno);
        bVo.setAttList(attList);
       
        session.close();
        return bVo;
    }
    
    
    public boolean update(BoardVo bVo, String[] delFiles) {
        System.out.println("dao.update");
        System.out.println(bVo.getSno());
        System.out.println(bVo.getSubject());
       
        boolean b=true;
        int cnt = session.update("board.update", bVo);		//내용 업데이트
        if(cnt<1) {
            b=false;
        }else if(bVo.getAttList().size()>0) {
            int attCnt = session.insert("board.attUpdate", bVo);	//첨부파일 추가
            if(attCnt<1) b=false;
        }
       
        if(b) {
            if(delFiles.length>0) {
                // 첨부 파일 데이터 삭제
                cnt = session.delete("board.attDelete", delFiles);
                if(cnt>0) {
                    fileDelete(delFiles); // 파일 삭제
                }else {
                    b=false;
                }
            }
        }
        if(b) session.commit();
        else  session.rollback();
       
        session.close();
       
        return b;
    }
    
    public void fileDelete(String[] delFiles) {
        System.out.println("file deleting....");
        System.out.println(Arrays.toString(delFiles));
       
        for(String f : delFiles) {
            System.out.println(f + " is delete..");
            File file = new File(FileUploadServlet.uploadPath + f);
            if(file.exists()) file.delete();
        }
    }    
    
    public boolean delete(BoardVo bVo) {
        boolean b=true;
       
        //자신의 글에 댓글이 있는지 판단하기
       
        System.out.println("delete check....");
        System.out.println("sno : " + bVo.getSno());
        System.out.println("grp : " + bVo.getGrp());
        System.out.println("seq : " + bVo.getSeq());
        System.out.println("deep : " + bVo.getDeep());
       
        // 같은 grp안에 자신의 seq보다 1더 큰 seq의 자료에서
        // deep이 자신 보다 큰것이 있으면 댓글이 있는 것임.
        int replCnt = session.selectOne("board.replCheck", bVo);
       
        System.out.println("replCnt : " + replCnt);
        if(replCnt>0) {
            b=false;
            return b;
        }
       
        // sno에 해당하는 테이블 삭제
        int cnt = session.delete("board.delete", bVo);
        System.out.println("delete cnt : " + cnt);
        if(cnt<1) {
            b=false;
        }else {
            // sno를 pSno로 바꾸어 첨부 테이블에서 첨부파일 목록 가져오기
            List<String> attList = session.selectList("board.delFileList", bVo.getSno());
            System.out.println("att size : " + attList.size());
            // 첨부 테이블 삭제
            if(attList.size()>0) {
                cnt = session.delete("board.attDeleteAll", bVo.getSno());
                if(cnt>0) {
                    // 첨부 파일 삭제
                    if(attList.size()>0) {
                        String[] delList = attList.toArray(new String[0]);
                        fileDelete(delList);
                    }
                }else {
                    b=false;
                }
            }
           
        }
       
        if(b) session.commit();
        else  session.rollback();
       
        session.close();
        return b;
    }
    
    public synchronized boolean repl(BoardVo bVo) {
        boolean b=true;
        session.update("board.seqUp", bVo);
        int cnt = session.insert("board.repl", bVo);
        if(cnt<1) {
            b=false;
        }else if(bVo.getAttList().size()>0) {
            int attCnt = session.insert("board.attInsert", bVo.getAttList());
            if(attCnt<1) b=false;
        }
       
        if(b) session.commit();
        else  session.rollback();
        session.close();
           
        return b;
    }
    
    public PageVo getpVo() {
    	return pVo;
    }
    
}
