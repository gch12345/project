package Fred.service;

import Fred.mapper.UserMapper;
import Fred.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public User login(String username, String password) {
        return userMapper.login(username, password);
    }

    public int insert(User user) {
        return userMapper.insert(user);
    }
}
