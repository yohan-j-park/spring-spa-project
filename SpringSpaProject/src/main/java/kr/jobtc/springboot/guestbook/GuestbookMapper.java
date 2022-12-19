package kr.jobtc.springboot.guestbook;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface GuestbookMapper {
	public int totSize(GPageVo vo);
	public List<GuestbookVo> list(GPageVo vo);
//			매퍼의 resultType		parameterType
	public int insert(GuestbookVo vo);
	public int delete(GuestbookVo vo);
	public int update(GuestbookVo vo);
	public List<GuestbookVo> guestbook10();


}
