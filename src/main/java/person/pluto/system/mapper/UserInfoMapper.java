package person.pluto.system.mapper;

import person.pluto.system.entity.UserInfo;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Pluto
 * @since 2019-07-22 11:57:50
 */
@Mapper
public interface UserInfoMapper extends BaseMapper<UserInfo> {

}
