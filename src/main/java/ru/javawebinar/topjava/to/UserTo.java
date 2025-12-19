package ru.javawebinar.topjava.to;

import org.hibernate.validator.constraints.Range;
import ru.javawebinar.topjava.util.UsersUtil;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serial;
import java.io.Serializable;

public class UserTo extends BaseTo implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @NotBlank(message = "{exception.user.name}")
    @Size(min = 2, max = 100, message = "{exception.user.name.length}")
    private String name;

    @Email(message = "{exception.user.email}")
    @NotBlank(message = "{exception.user.email.empty}")
    @Size(max = 100, message = "{exception.user.email.size}")
    private String email;

    @NotBlank(message = "{exception.user.password.empty}")
    @Size(min = 5, max = 32, message = "{exception.user.password.length}")
    private String password;

    @Range(min = 10, max = 10000, message = "{exception.user.calories.range}")
    @NotNull(message = "{exception.user.password.empty}")
    private Integer caloriesPerDay = UsersUtil.DEFAULT_CALORIES_PER_DAY;

    public UserTo() {
    }

    public UserTo(Integer id, String name, String email, String password, int caloriesPerDay) {
        super(id);
        this.name = name;
        this.email = email;
        this.password = password;
        this.caloriesPerDay = caloriesPerDay;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCaloriesPerDay(Integer caloriesPerDay) {
        this.caloriesPerDay = caloriesPerDay;
    }

    public Integer getCaloriesPerDay() {
        return caloriesPerDay;
    }

    @Override
    public String toString() {
        return "UserTo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", caloriesPerDay='" + caloriesPerDay + '\'' +
                '}';
    }
}
