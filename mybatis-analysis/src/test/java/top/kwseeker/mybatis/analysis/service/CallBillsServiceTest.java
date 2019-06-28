package top.kwseeker.mybatis.analysis.service;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import top.kwseeker.mybatis.analysis.domain.QueryCallTicketListReq;
import top.kwseeker.mybatis.analysis.domain.QueryCallTicketListResp;
import top.kwseeker.mybatis.analysis.domain.TbCallRecordInfo;

import java.util.List;

public class CallBillsServiceTest {

    private static CallBillsService callBillsService;

    @BeforeClass
    public static void beforeClass() {
        callBillsService = new CallBillsServiceImpl();
    }

    @Test
    public void queryCallTicketList() throws Exception {
        QueryCallTicketListReq req = new QueryCallTicketListReq();
        req.setEnterpriseId("1143821253963001857");
        //req.setAppId("1143827380477890561");
        //req.setPoolType("AXB");
        //req.setTelX("17034567908");
        //req.setCallNo("13834567908");
        //req.setPeerNo("13865782347");
        //req.setFinishState("1");
        //req.setCallTime("20190627120000");
        List<QueryCallTicketListResp> respList = callBillsService.queryCallTicketList(req);
        Assert.assertEquals(1, respList.size());
        Assert.assertEquals(180, respList.get(0).getCallDuration().intValue());
    }

    @Test
    public void queryCallRecordInfo() {
        TbCallRecordInfo info = callBillsService.queryCallRecordInfo(1);
        System.out.println();
    }
}