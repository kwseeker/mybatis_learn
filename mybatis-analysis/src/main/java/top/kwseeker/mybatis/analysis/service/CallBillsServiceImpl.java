package top.kwseeker.mybatis.analysis.service;

import org.apache.ibatis.session.SqlSession;
import top.kwseeker.mybatis.analysis.dao.TbCallRecordDao;
import top.kwseeker.mybatis.analysis.domain.QueryCallTicketListReq;
import top.kwseeker.mybatis.analysis.domain.QueryCallTicketListResp;
import top.kwseeker.mybatis.analysis.domain.TbCallRecordInfo;
import top.kwseeker.mybatis.analysis.util.MybatisUtil;

import java.util.List;

public class CallBillsServiceImpl implements CallBillsService {

    private static final TbCallRecordDao tbCallRecordDao;

    static {
        SqlSession sqlSession = MybatisUtil.getSqlSession("mybatis-config.xml");
        tbCallRecordDao = sqlSession.getMapper(TbCallRecordDao.class);
    }

    @Override
    public List<QueryCallTicketListResp> queryCallTicketList(QueryCallTicketListReq req) {
        return tbCallRecordDao.selectCallTicketList(req);
    }

    @Override
    public TbCallRecordInfo queryCallRecordInfo(Integer id) {
        return tbCallRecordDao.selectCallRecordInfo(id);
    }
}
