package top.kwseeker.mybatis.analysis.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import top.kwseeker.mybatis.analysis.domain.QueryCallTicketListReq;
import top.kwseeker.mybatis.analysis.domain.QueryCallTicketListResp;
import top.kwseeker.mybatis.analysis.domain.TbCallRecordInfo;

import java.util.List;

@Mapper
public interface TbCallRecordDao {

    @Select("<script>" +
            "select \n" +
            "    tnbi.tel_x,\n" +
            "    tcri.call_no,\n" +
            "    tcri.peer_no,\n" +
            "    tcri.finish_state,\n" +
            "    tcri.call_time,\n" +
            "    tcri.start_time,\n" +
            "    tcri.finish_time,\n" +
            "    tcri.call_duration\n" +
            "from tb_call_record_info tcri, tb_number_bind_info tnbi\n" +
            "where \n" +
            "    1=1\n" +
            "<if test = \"callNo != null and callNo != '' \">\n" +
            "    and call_no = #{callNo}\n" +
            "</if>\n" +
            "<if test = \"peerNo != null and peerNo != '' \">\n" +
            "    and peer_no = #{peerNo}\n" +
            "</if>\n" +
            "<if test = \"finishState != null and finishState != '' \">\n" +
            "    and finish_state = #{finishState}\n" +
            "</if>\n" +
            "<if test = \"callTime != null and callTime != '' \">\n" +
            "    and call_time = #{callTime}\n" +
            "</if>\n" +
            "and tcri.bind_id in (\n" +
            "    select bind_id from tb_number_info tni, tb_number_bind_info tnbi\n" +
            "    where tni.id = tnbi.number_info_id\n" +
            "       and tni.enterprise_id = #{enterpriseId}\n" +
            "    <if test = \"appId != null and appId != '' \">\n" +
            "       and tni.app_id = #{appId}\n" +
            "    </if>\n" +
            "    <if test = \"poolType != null and poolType != '' \">\n" +
            "       and tni.pool_type = #{poolType}\n" +
            "    </if>\n" +
            "    <if test = \"telX != null and telX != '' \">\n" +
            "       and tni.tel_x = #{telX}\n" +
            "    </if>\n" +
            ")\n" +
            "and tcri.bind_id = tnbi.bind_id\n" +
            "</script>")
    List<QueryCallTicketListResp> selectCallTicketList(QueryCallTicketListReq req);

    @Select("select * from tb_call_record_info where id = #{id}")
    TbCallRecordInfo selectCallRecordInfo(Integer id);
}
