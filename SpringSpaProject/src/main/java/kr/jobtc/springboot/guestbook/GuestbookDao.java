package kr.jobtc.springboot.guestbook;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

@Component
@Transactional
public class GuestbookDao {
	GPageVo gVo;
	
	@Autowired
	GuestbookMapper mapper;
	
	@Autowired
	PlatformTransactionManager manager;
	
	TransactionStatus status;


	public int getTotSize(GPageVo gVo) {
		int totSize = 0;
		totSize = mapper.totSize(gVo);		
		return totSize;
	}
	
	public List<GuestbookVo> select(GPageVo gVo){
		List<GuestbookVo> list = null;
		int totSize = getTotSize(gVo);
		gVo.setTotSize(totSize);
		this.gVo = gVo;
		list = mapper.list(gVo);
		
		return list;
	}
	
	public boolean insert(GuestbookVo vo) {
		boolean b = false;
		status = manager.getTransaction(new DefaultTransactionDefinition());
		Object savePoint = status.createSavepoint();	
		// savepoint를 만들고 난 이후의 작업들을 commit, rollback처리 할 수 있음
		
	
		int cnt = mapper.insert(vo);
		
		if(cnt>0) {
			b=true;
			manager.commit(status);
		}else {
			status.rollbackToSavepoint(savePoint);
		}
		return b;
	}
	
	public boolean delete(GuestbookVo vo) {
		boolean b = false;
		status = manager.getTransaction(new DefaultTransactionDefinition());
		Object savePoint = status.createSavepoint();
		
		int cnt = mapper.delete(vo);
		
		if(cnt>0) {
			b=true;
			manager.commit(status);
		}else {
			status.rollbackToSavepoint(savePoint);
		}
		return b;
		
	}
	
	public boolean update(GuestbookVo vo) {
		boolean b = false;
		status = manager.getTransaction(new DefaultTransactionDefinition());
		Object savePoint = status.createSavepoint();
		
		int cnt = mapper.update(vo);
		
		if(cnt>0) {
			b=true;
			manager.commit(status);
		}else {
			status.rollbackToSavepoint(savePoint);
		}
		return b;
		
	}
	
	public List<GuestbookVo> guestbook10(){
		List<GuestbookVo> list = null;

		list = mapper.guestbook10();
		
		return list;
	}
	public GPageVo getgVo() {return gVo;}
}
