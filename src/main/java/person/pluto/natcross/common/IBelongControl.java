package person.pluto.natcross.common;

/**
 * 
 * <p>
 * 通知上次停止的统一类，为适应不同的类型进行不同的函数封装
 * </p>
 *
 * @author Pluto
 * @since 2019-07-12 08:39:25
 */
public interface IBelongControl {

    default void noticeStop() {
        // TODO 用消息队列更加合适
    }

    default boolean stopSocketPart(String socketPartKey) {
        return true;
    }

}
