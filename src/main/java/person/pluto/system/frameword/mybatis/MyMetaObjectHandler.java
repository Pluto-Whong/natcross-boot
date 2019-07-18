package person.pluto.system.frameword.mybatis;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;

import java.time.LocalDateTime;

import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

/**
 * <p>
 * mybatis plus 自动填充器
 * </p>
 *
 * @author wangmin1994@qq.com
 * @since 2019-04-01 10:32:58
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        this.setInsertFieldValByName("gmtCreate", LocalDateTime.now(), metaObject);
        this.setInsertFieldValByName("gmtModify", LocalDateTime.now(), metaObject);
        
        this.setInsertFieldValByName("isDelete", false, metaObject);
        this.setInsertFieldValByName("version", 0, metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.setUpdateFieldValByName("gmtModify", LocalDateTime.now(), metaObject);
    }
}
