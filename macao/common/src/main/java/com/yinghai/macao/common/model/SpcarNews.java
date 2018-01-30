package com.yinghai.macao.common.model;

import java.util.Date;

public class SpcarNews {
    private Integer newsId;

    private String newsTitle;

    private String pushMsg;

    private Date publishSince;

    private Date publishTill;

    private String realm;

    private String content;
    
    private Integer pushTotalTimes;
    
    private Date pushLatestTime;
    
    public Integer getPushTotalTimes() {
		return pushTotalTimes;
	}

	public void setPushTotalTimes(Integer pushTotalTimes) {
		this.pushTotalTimes = pushTotalTimes;
	}

	public Date getPushLatestTime() {
		return pushLatestTime;
	}

	public void setPushLatestTime(Date pushLatestTime) {
		this.pushLatestTime = pushLatestTime;
	}

	public Integer getNewsId() {
        return newsId;
    }

    public void setNewsId(Integer newsId) {
        this.newsId = newsId;
    }

    public String getNewsTitle() {
        return newsTitle;
    }

    public void setNewsTitle(String newsTitle) {
        this.newsTitle = newsTitle == null ? null : newsTitle.trim();
    }

    public String getPushMsg() {
        return pushMsg;
    }

    public void setPushMsg(String pushMsg) {
        this.pushMsg = pushMsg == null ? null : pushMsg.trim();
    }

    public Date getPublishSince() {
        return publishSince;
    }

    public void setPublishSince(Date publishSince) {
        this.publishSince = publishSince;
    }

    public Date getPublishTill() {
        return publishTill;
    }

    public void setPublishTill(Date publishTill) {
        this.publishTill = publishTill;
    }

    public String getRealm() {
        return realm;
    }

    public void setRealm(String realm) {
        this.realm = realm == null ? null : realm.trim();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }
}