package top.kwseeker.mybatis.analysis.service;

import top.kwseeker.mybatis.analysis.domain.QueryCallTicketListReq;
import top.kwseeker.mybatis.analysis.domain.QueryCallTicketListResp;
import top.kwseeker.mybatis.analysis.domain.TbCallRecordInfo;

import java.util.List;

public interface CallBillsService {

    List<QueryCallTicketListResp> queryCallTicketList(QueryCallTicketListReq req);

    TbCallRecordInfo queryCallRecordInfo(Integer id);
}
