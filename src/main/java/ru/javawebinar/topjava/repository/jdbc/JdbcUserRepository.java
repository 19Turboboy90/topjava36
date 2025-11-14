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

import javax.validation.Validator;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepository implements UserRepository {

    private static final String GET_USER = """
            SELECT *
            FROM users
            LEFT JOIN user_role ur ON ur.user_id = users.id
            """;

    private static final String SAVE_ROLE = """
            INSERT INTO user_role (user_id, role)
            VALUES (?,?);
            """;

    private static final BeanPropertyRowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;
    private final Validator validator;

    @Autowired
    public JdbcUserRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate, Validator validator) {
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.validator = validator;
    }

    @Override
    @Transactional
    public User save(User user) {
        ValidationUtil.validate(validator, user);

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
        jdbcTemplate.batchUpdate(SAVE_ROLE, args);
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
        List<User> users = jdbcTemplate.query(GET_USER + " WHERE id=?", new UserResultSetExtractor(), id);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public User getByEmail(String email) {
//        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        List<User> users = jdbcTemplate.query(GET_USER + " WHERE email=?", new UserResultSetExtractor(), email);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query(GET_USER + " ORDER BY name, email", new UserResultSetExtractor());
    }

    private static class UserResultSetExtractor implements ResultSetExtractor<List<User>> {
        @Override
        public List<User> extractData(ResultSet rs) throws SQLException, DataAccessException {
            Map<Integer, User> users = new LinkedHashMap<>();
            while (rs.next()) {
                int userId = rs.getInt("id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                String password = rs.getString("password");
                Date registered = rs.getTimestamp("registered");
                boolean enabled = rs.getBoolean("enabled");
                int caloriesPerDay = rs.getInt("calories_per_day");
                String role = rs.getString("role");
                User user = users.computeIfAbsent(userId, id ->
                        new User(id, name, email, password, caloriesPerDay, enabled, registered, EnumSet.noneOf(Role.class)));
                if (role != null) {
                    user.getRoles().add(Role.valueOf(role));
                }
            }
            return new ArrayList<>(users.values());
        }
    }
}