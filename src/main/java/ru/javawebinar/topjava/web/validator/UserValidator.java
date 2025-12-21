package ru.javawebinar.topjava.web.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;
import ru.javawebinar.topjava.to.UserTo;

@Component
public class UserValidator implements Validator {

    private final UserRepository repository;

    public UserValidator(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return UserTo.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserTo userTo = (UserTo) target;

        if (userTo.getEmail() == null || userTo.getEmail().isBlank()) {
            return;
        }

        User exist = repository.getByEmail(userTo.getEmail().toLowerCase());

        if (exist != null && !exist.getId().equals(userTo.getId())) {
            errors.rejectValue(
                    "email", "exception.user.duplicated.email"
            );
        }
    }
}
