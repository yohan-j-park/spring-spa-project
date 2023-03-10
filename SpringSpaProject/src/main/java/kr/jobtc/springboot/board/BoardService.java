package kr.jobtc.springboot.board;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

@Service
@Transactional
public class BoardService {
	PageVo pVo;

	@Autowired
	BoardMapper mapper;
	Object savePoint;

	@Autowired
	PlatformTransactionManager manager;
	TransactionStatus status;

	public BoardService() {
	}

	public boolean insertR(BoardVo bVo) {
		status = manager.getTransaction(new DefaultTransactionDefinition());
		savePoint = status.createSavepoint();
		int cnt = mapper.insertR(bVo);
		boolean flag = true;
		if (cnt < 1) {
			status.rollbackToSavepoint(savePoint);
			flag = false;
		} 
//		else if (bVo.getAttList().size() > 0) {
//			System.out.println("attSize : " + bVo.getAttList().size());
//			int attCnt = mapper.insertAttList(bVo.getAttList());
//			if (attCnt < 0) flag = false;
//			manager.commit(status);
//		} 
//		else {
//			manager.commit(status);
//			String[] delFile = new String[bVo.getAttList().size()];
//			for (int i = 0; i < bVo.getAttList().size(); i++) {
//				delFile[i] = bVo.getAttList().get(i).getSysFile();
//			}
//			fileDelete(delFile);
//		}
		return flag;
	}

	public void insertAttList(List<AttVo> attList) {
		int cnt = mapper.insertAttList(attList);
		if (cnt > 0) {
			manager.commit(status);

		} else {
			status.rollbackToSavepoint(savePoint);
		}
	}

	public boolean updateR(BoardVo bVo, String[] delFile) {
		System.out.println("service.update");
		System.out.println(bVo.getSno());
		System.out.println(bVo.getSubject());

		boolean b = true;

		status = manager.getTransaction(new DefaultTransactionDefinition());
		savePoint = status.createSavepoint();
		int cnt = mapper.update(bVo); // ?????? ????????????
		if (cnt < 1) {
			b = false;
		} else if (bVo.getAttList().size() > 0) {
			int attCnt = mapper.attUpdate(bVo); // ???????????? ??????
			if (attCnt < 1)
				b = false;
		}

		if (b) {
			manager.commit(status);
			if (delFile != null && delFile.length > 0) {
				// ?????? ?????? ????????? ??????
				cnt = mapper.attDelete(delFile);
				if (cnt > 0) {
					fileDelete(delFile); // ?????? ??????
				} else {
					b = false;
				}
			}
		} else {
			status.rollbackToSavepoint(savePoint);
			delFile = new String[bVo.getAttList().size()];
			for (int i = 0; i < bVo.getAttList().size(); i++) {
				delFile[i] = bVo.getAttList().get(i).getSysFile();
			}
			fileDelete(delFile);
		}

		return b;
	}

	public boolean replR(BoardVo bVo) {
		status = manager.getTransaction(new DefaultTransactionDefinition());
		savePoint = status.createSavepoint();
		boolean b = true;
		mapper.seqUp(bVo);
		int cnt = mapper.replR(bVo); // ?????? ????????????
		if (cnt < 1) {
			b = false;
		} else if (bVo.getAttList().size() > 0) {
			int attCnt = mapper.insertAttList(bVo.getAttList());
			if (attCnt < 0)
				b = false;
		}
		if (b)
			manager.commit(status);
		else {
			status.rollbackToSavepoint(savePoint);
			String[] delFile = new String[bVo.getAttList().size()];
			for (int i = 0; i < bVo.getAttList().size(); i++) {
				delFile[i] = bVo.getAttList().get(i).getSysFile();
			}
			fileDelete(delFile);
		}

		return b;
	}

	public List<BoardVo> select(PageVo pVo) {
		int totSize = mapper.totList(pVo);
		pVo.setTotSize(totSize);
		this.pVo = pVo;
		List<BoardVo> list = mapper.select(pVo);
		return list;
	}

	public List<BoardVo> board10() {
		List<BoardVo> list = mapper.board10();

		return list;
	}

	public BoardVo view(int sno, String up) {
		BoardVo bVo = null;
		if (up != null && up.equals("up")) {// ??????????????? ?????????
			mapper.hitUp(sno);
		}
		bVo = mapper.view(sno);
		List<AttVo> attList = mapper.attList(sno);
		bVo.setAttList(attList);
		return bVo;
	}

	public boolean delete(BoardVo bVo) {
		boolean b = true;

		// ????????? ?????? ????????? ????????? ????????????

		// ?????? grp?????? ????????? seq?????? 1??? ??? seq??? ????????????
		// deep??? ?????? ?????? ????????? ????????? ????????? ?????? ??????.
		int replCnt = mapper.replCheck(bVo);

		System.out.println("replCnt : " + replCnt);
		if (replCnt > 0) {
			b = false;
			return b;
		}

		// sno??? ???????????? ????????? ??????
		status = manager.getTransaction(new DefaultTransactionDefinition());
		Object savePoint = status.createSavepoint();

		int cnt = mapper.delete(bVo);
		System.out.println("delete cnt : " + cnt);
		if (cnt < 1) {
			b = false;
		} else {
			// sno??? pSno??? ????????? ?????? ??????????????? ???????????? ?????? ????????????
			List<String> attList = mapper.delFileList(bVo.getSno());
			System.out.println("att size : " + attList.size());
			// ?????? ????????? ??????
			if (attList.size() > 0) {
				cnt = mapper.attDeleteAll(bVo.getSno());
				if (cnt > 0) {
					// ?????? ?????? ??????
					if (attList.size() > 0) {
						String[] delList = attList.toArray(new String[0]);
						fileDelete(delList);
					}
				} else {
					b = false;
				}
			}

		}
		if (b)
			manager.commit(status);
		else
			status.rollbackToSavepoint(savePoint);

		return b;
	}

	public void fileDelete(String[] delFile) {
		for (String f : delFile) {
			File file = new File(FileUploadController.path + f);
			if (file.exists())
				file.delete();
		}
	}

	public PageVo getpVo() {
		return pVo;
	}

}
