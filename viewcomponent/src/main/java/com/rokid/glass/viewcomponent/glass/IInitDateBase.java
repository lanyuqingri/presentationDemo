package com.rokid.glass.viewcomponent.glass;

/**
 * @author cc
 */
public interface IInitDateBase {

    /**
     * 初始化图片map数据库
     */
    void doInitBase();

    /**
     * 应用退出时候释放数据库
     */
    void onDestory();
}
