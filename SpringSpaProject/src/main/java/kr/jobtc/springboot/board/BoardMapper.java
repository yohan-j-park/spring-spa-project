package kr.jobtc.springboot.board;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface BoardMapper {
	public int totList(PageVo pVo);
	public List<BoardVo> select(PageVo pVo);
	public List<BoardVo> board10();
	
	public void hitUp(int sno);
	public BoardVo view(int sno);
	public List<AttVo> attList(int sno);
	
	public int replCheck(BoardVo bVo);
	public int delete(BoardVo bVo);
	public List<String> delFileList(int sno);
	public int attDeleteAll(int sno);
	
	public int insertR(BoardVo bVo);
	public int insertAttList(List<AttVo> attList);
	
	public int update(BoardVo bVo);
	public int attUpdate(BoardVo bVo);
	public int attDelete(String[] delFiles);
	
	public void seqUp(BoardVo bVo);
	public int replR(BoardVo bVo);
	
}
