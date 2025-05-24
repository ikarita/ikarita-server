package com.github.ikarita.server.security;

import com.github.ikarita.server.service.user.UserSecurityService;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class IkaritaSecurityExpressionRoot extends SecurityExpressionRoot implements MethodSecurityExpressionOperations {

    private Object filterObject;
    private Object returnObject;
    private Object target;
    protected UserSecurityService userSecurityService;

    public IkaritaSecurityExpressionRoot() {
        super(SecurityContextHolder.getContext().getAuthentication());
    }

    @SuppressWarnings("unchecked")
    protected <T extends Authentication> Optional<T> get(Class<T> expectedAuthType) {
        return Optional.ofNullable(getAuthentication()).map(a -> a.getClass().isAssignableFrom(expectedAuthType) ? (T) a : null);
    }

    @Override
    public void setFilterObject(Object filterObject) {
        this.filterObject = filterObject;
    }

    @Override
    public Object getFilterObject() {
        return filterObject;
    }

    @Override
    public void setReturnObject(Object returnObject) {
        this.returnObject = returnObject;
    }

    @Override
    public Object getReturnObject() {
        return returnObject;
    }

    public void setThis(Object target) {
        this.target = target;
    }

    @Override
    public Object getThis() {
        return target;
    }

    public void setUserService(UserSecurityService userSecurityService) {
        this.userSecurityService = userSecurityService;
    }
}
