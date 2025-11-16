package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;
import ru.javawebinar.topjava.util.ValidationUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepository implements UserRepository {

    private static final BeanPropertyRowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    private static final ResultSetExtractor<List<User>> USER_EXTRACTOR = new UserResultSetExtractor();

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
    public User save(User user) {
        ValidationUtil.validate(user);

        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);

        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
            saveRoles(user);
        } else if (namedParameterJdbcTemplate.update("""
                   UPDATE users SET name=:name, email=:email, password=:password,
                   registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id
                """, parameterSource) == 0) {
            return null;
        } else {
            deleteRoles(user.getId());
            saveRoles(user);
        }
        return user;
    }

    private void saveRoles(User user) {
        Set<Role> roles = user.getRoles();
        List<Object[]> args = roles.stream()
                .map(role -> new Object[]{user.getId(), role.name()})
                .toList();
        jdbcTemplate.batchUpdate("""
                INSERT INTO user_role (user_id, role)
                VALUES (?,?);
                """, args);
    }

    private void deleteRoles(int userId) {
        jdbcTemplate.update("""
                DELETE FROM user_role
                WHERE user_id = ?
                """, userId);
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return jdbcTemplate.update("""
                DELETE FROM users
                WHERE id=?
                """, id) != 0;
    }

    @Override
    public User get(int id) {
        List<User> users = jdbcTemplate.query("""
                SELECT *
                FROM users
                LEFT JOIN user_role ur ON ur.user_id = users.id
                WHERE id=?
                """, USER_EXTRACTOR, id);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public User getByEmail(String email) {
//        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        List<User> users = jdbcTemplate.query("""
                SELECT *
                FROM users
                LEFT JOIN user_role ur ON ur.user_id = users.id
                WHERE email=?
                """, USER_EXTRACTOR, email);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query("""
                SELECT *
                FROM users
                LEFT JOIN user_role ur ON ur.user_id = users.id
                ORDER BY name, email
                """, USER_EXTRACTOR);
    }

    private static final class UserResultSetExtractor implements ResultSetExtractor<List<User>> {
        @Override
        public List<User> extractData(ResultSet rs) throws SQLException, DataAccessException {
            Map<Integer, User> users = new LinkedHashMap<>();
            while (rs.next()) {
                int userId = rs.getInt("id");

                User user = users.computeIfAbsent(userId, id -> {
                    try {
                        User u = ROW_MAPPER.mapRow(rs, rs.getRow());
                        u.setRoles(EnumSet.noneOf(Role.class));
                        return u;
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });

                String role = rs.getString("role");
                if (role != null) {
                    user.getRoles().add(Role.valueOf(role));
                }
            }
            return new ArrayList<>(users.values());
        }
    }
}