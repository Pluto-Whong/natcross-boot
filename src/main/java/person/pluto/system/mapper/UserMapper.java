package person.pluto.system.mapper;

import person.pluto.system.entity.User;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Pluto
 * @since 2019-07-18 17:13:41
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
