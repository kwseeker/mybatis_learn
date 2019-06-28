package top.kwseeker.mybatis.analysis.domain;

import java.util.Date;

public class TbCallRecordInfo {

    private Integer id;

    /**
     * 业务规则ID
     */
    private String ruleId;

    /**
     * 用户网络类型
     */
    private String netType;

    /**
     * 绑定ID
     */
    private String bindId;

    /**
     * 通话ID，唯一确定一次通话
     */
    private String callId;

    /**
     * 主叫号码
     */
    private String callNo;

    /**
     * 被叫号码
     */
    private String peerNo;

    /**
     * 中间号
     */
    private String x;

    /**
     * 通话发生时间：14 位YYYYMMDDHHMMSS 格式，例如：20160906131816
     */
    private String callTime;

    /**
     * 通话发生时间：14 位YYYYMMDDHHMMSS 格式，例如：20160906131816
     */
    private String startTime;

    /**
     * 通话结束时间：14 位YYYYMMDDHHMMSS 格式，例如：20160906131816
     */
    private String finishTime;

    /**
     * 通话时长，单位秒
     */
    private Integer callDuration;

    /**
     * 结束发起方:0: 平台结束；1：主叫结束；2：被叫结束；
     */
    private String finishType;

    /**
     * 结束状态（即挂断原因）1: 主叫挂机; 2: 被叫挂机; 3: 主叫放弃; 4: 被叫无应答;
     * 5: 被叫忙; 6: 被叫不可及; 7: 路由失败; 8: 中间号状态异常; 9: 订单超过有效期;
     * 10: 平台系统异常; 11: 关机; 12: 停机; 13: 拒接; 14: 空号;
     * 注：11-14 状态值只出现在AS 呼叫
     */
    private String finishState;

    private Date createTime;

    private Date modifyTime;

    /**
     * 删除标记 0否1是
     */
    private Integer deleteFlag;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRuleId() {
        return ruleId;
    }

    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }

    public String getNetType() {
        return netType;
    }

    public void setNetType(String netType) {
        this.netType = netType;
    }

    public String getBindId() {
        return bindId;
    }

    public void setBindId(String bindId) {
        this.bindId = bindId;
    }

    public String getCallId() {
        return callId;
    }

    public void setCallId(String callId) {
        this.callId = callId;
    }

    public String getCallNo() {
        return callNo;
    }

    public void setCallNo(String callNo) {
        this.callNo = callNo;
    }

    public String getPeerNo() {
        return peerNo;
    }

    public void setPeerNo(String peerNo) {
        this.peerNo = peerNo;
    }

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public String getCallTime() {
        return callTime;
    }

    public void setCallTime(String callTime) {
        this.callTime = callTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(String finishTime) {
        this.finishTime = finishTime;
    }

    public Integer getCallDuration() {
        return callDuration;
    }

    public void setCallDuration(Integer callDuration) {
        this.callDuration = callDuration;
    }

    public String getFinishType() {
        return finishType;
    }

    public void setFinishType(String finishType) {
        this.finishType = finishType;
    }

    public String getFinishState() {
        return finishState;
    }

    public void setFinishState(String finishState) {
        this.finishState = finishState;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public Integer getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Integer deleteFlag) {
        this.deleteFlag = deleteFlag;
    }
}
