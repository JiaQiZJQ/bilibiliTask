package top.srcrs.util;

import lombok.extern.slf4j.Slf4j;
import top.srcrs.Task;
import top.srcrs.task.bigvip.BiCoinApply;
import top.srcrs.task.bigvip.CollectVipGift;
import top.srcrs.task.daily.DailyTask;
import top.srcrs.task.daily.ThrowCoinTask;
import top.srcrs.task.live.BiLiveTask;
import top.srcrs.task.live.GiveGiftTask;
import top.srcrs.task.live.Silver2CoinTask;
import top.srcrs.task.manga.MangaTask;

import java.util.Arrays;
import java.util.List;

/**
 * 运行所有已注册的Task
 *
 * @author srcrs
 * @Time 2020-10-13
 */
@Slf4j
public abstract class TaskRegistry {

    private static final List<Class<? extends Task>> REGISTERED_TASKS = Arrays.asList(
            BiCoinApply.class,
            CollectVipGift.class,
            DailyTask.class,
            ThrowCoinTask.class,
            BiLiveTask.class,
            GiveGiftTask.class,
            Silver2CoinTask.class,
            MangaTask.class
    );

    /**
     * 运行所有已注册任务
     */
    public void runTasks() {
        REGISTERED_TASKS.stream().map(Class::getName).forEach(this::dealClass);
    }

    /**
     * 获得真实的className
     *
     * @param className className
     */
    public abstract void dealClass(String className);
}
