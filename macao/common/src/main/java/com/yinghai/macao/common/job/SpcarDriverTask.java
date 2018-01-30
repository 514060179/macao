package com.yinghai.macao.common.job;

import com.yinghai.macao.common.service.SpcarDriverService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2017/8/2.
 */
@Component
public class SpcarDriverTask {
    @Autowired
    private SpcarDriverService spcarDriverService;

    private Logger log = Logger.getLogger(this.getClass());

    public void updateDriverRate(){
        log.debug("更新司机评分！");
        int i = spcarDriverService.updateDriverRate();
        log.debug("一共成功更新条数："+i);
    }

}
