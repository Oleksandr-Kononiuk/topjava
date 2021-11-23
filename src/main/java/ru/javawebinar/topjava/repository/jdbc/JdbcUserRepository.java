package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.*;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepository implements UserRepository {

    private static final BeanPropertyRowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    @Autowired
    public JdbcUserRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    @Transactional
    public User save(@NotNull User user) {
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);

        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
            insertRoles(user);
        } else {
            int isUpdated = namedParameterJdbcTemplate.update("UPDATE users " +
                            "SET name=:name, email=:email, password=:password, " +
                            "registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay " +
                            "WHERE id=:id", parameterSource);
            if (isUpdated == 0) {
                return null;
            } else {
                //lazy or compare roles and then update
                deleteRoles(user.id());
                insertRoles(user);
            }
        }
        return user;
    }

    @Override
    @Transactional
    public boolean delete(@Positive int id) {
        int isDeleted = jdbcTemplate.update("DELETE FROM users WHERE id=?", id);
        if (isDeleted != 0 ) {
            deleteRoles(id);
        }
        return isDeleted != 0;
    }

    @Override
    public User get(@Positive int id) {
        List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE id=?", ROW_MAPPER, id);
        return setRole(DataAccessUtils.singleResult(users));
    }

    @Override
    public User getByEmail(@Email String email) {
        List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        return setRole(DataAccessUtils.singleResult(users));
    }

    @Override
    public List<User> getAll() {
        Map<Integer, Set<Role>> userRoles = new HashMap<>();
        List<User> users =  jdbcTemplate.query("SELECT * FROM users ORDER BY name, email", ROW_MAPPER);

        jdbcTemplate.query("SELECT * FROM user_roles", rs -> {
            userRoles.computeIfAbsent(rs.getInt("user_id"),
                    roles -> EnumSet.noneOf(Role.class)).add(Role.valueOf(rs.getString("role")));
        });
        users.forEach(user -> user.setRoles(userRoles.get(user.getId())));
        return users;
    }

    private void deleteRoles(int id) {
        jdbcTemplate.update("DELETE FROM user_roles WHERE user_id=?", id);
    }

    private void insertRoles(User user) {
        if (user != null) {
            Set<Role> roles = user.getRoles();

            jdbcTemplate.batchUpdate(
                    "INSERT INTO user_roles(user_id, role) VALUES (?, ?)",
                    roles,
                    roles.size(),
                    (ps, role) -> {
                        ps.setInt(1, user.id());
                        ps.setString(2, role.name());
                    });
        }
    }

    private User setRole(User user) {
        if (user != null) {
            List<Role> roles = jdbcTemplate.queryForList(
                    "SELECT role FROM user_roles WHERE user_id = ?", Role.class, user.id());
            user.setRoles(roles);

        }
        return user;
    }
}
