/**
 * 这个文件包含一个对UserDaoImpl类进行测试的类，
 * 测试是否可以正常对进行Users表进行增删改查操作。
 * 
 * @author 石振山
 * @version 2.2.3
 */
package com.ssvep.dao;

import org.junit.jupiter.api.*;
import com.ssvep.model.Users;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

public class UserDaoTest {
    private UserDao userDao;

    @BeforeEach
    public void setUp() {
        userDao = new UserDao(); 
    }

    @Test
    public void testSaveUser() {
        Users user = new Users("testuser", "password123", "Test User", null, Users.Role.USER);
        userDao.save(user);

        Users retrievedUser = userDao.getUserById(user.getUserId());

        assertNotNull(retrievedUser, "User should be saved and retrievable");
        assertEquals("testuser", retrievedUser.getUsername(), "Usernames should match");
    }

    @Test
    public void testGetAllUsers() {
        Users user1 = new Users("user1", "password1", "User One", null, Users.Role.USER);
        Users user2 = new Users("user2", "password2", "User Two", null, Users.Role.USER);
        userDao.save(user1);
        userDao.save(user2);

        List<Users> users = userDao.getAll();
        assertEquals(users.size(), users.size(), "There should be 2 users in the database");
    }

    @Test
    public void testUpdateUser() {
        Users user = new Users("updateuser1", "oldpassword", "Old User", null, Users.Role.USER);
        userDao.save(user);

        user.setUsername("updateduser2");
        userDao.update(user);

        Users updatedUser = userDao.getUserById(user.getUserId());
        assertEquals("updateduser2", updatedUser.getUsername(), "Username should be updated");
    }

    @Test
    public void testDeleteUser() {
        Users user = new Users("deleteuser", "password123", "Delete User", null, Users.Role.USER);
        userDao.save(user);

        userDao.delete(user.getUserId());

        Users deletedUser = userDao.getUserById(user.getUserId());
        assertNull(deletedUser, "User should be deleted and not retrievable");
    }

}
