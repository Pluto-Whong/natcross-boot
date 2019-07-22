package person.pluto.system.service.impl;

import person.pluto.system.entity.UserInfo;
import person.pluto.system.mapper.UserInfoMapper;
import person.pluto.system.service.IUserInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;


/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Pluto
 * @since 2019-07-22 11:57:50
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements IUserInfoService {


}
