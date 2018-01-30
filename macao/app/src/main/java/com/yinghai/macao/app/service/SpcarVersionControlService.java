package com.yinghai.macao.app.service;

import com.yinghai.macao.app.model.SpcarVersionControl;

/**
 * Created by Administrator on 2017/6/22.
 */
public interface SpcarVersionControlService {

    SpcarVersionControl findNewVersion(SpcarVersionControl spcarVersionControl);
}
