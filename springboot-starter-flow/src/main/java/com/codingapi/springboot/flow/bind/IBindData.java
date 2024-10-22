package com.codingapi.springboot.flow.bind;

import com.alibaba.fastjson.JSONObject;

/**
 * 数据绑定接口
 */
public interface IBindData {

    /**
     * 获取数据ID
     *
     * @return 数据ID
     */
    long getId();

    /**
     * 数据快照
     *
     * @return 数据快照
     */
    default String toJsonSnapshot() {
        return JSONObject.toJSONString(this);
    }
}
