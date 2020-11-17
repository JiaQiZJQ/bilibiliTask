package top.srcrs;

/**
 * 所有的任务功能都要实现这个接口，以达到动态加载
 * @author srcrs
 * @Time 2020-10-13
 */
public interface Task {
    /**
     * 任务功能开始的地方
     * @author srcrs
     * @Time 2020-10-13
     */
    void run();
}
