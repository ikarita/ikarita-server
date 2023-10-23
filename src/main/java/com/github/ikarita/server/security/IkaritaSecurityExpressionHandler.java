package com.github.ikarita.server.security;

import lombok.RequiredArgsConstructor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.aop.support.AopUtils;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.function.Supplier;

@RequiredArgsConstructor
public class IkaritaSecurityExpressionHandler extends DefaultMethodSecurityExpressionHandler {
    private final Supplier<IkaritaSecurityExpressionRoot> expressionRootSupplier;

    /**
     * Creates the root object for expression evaluation.
     */
    @Override
    protected MethodSecurityExpressionOperations createSecurityExpressionRoot(Authentication authentication,
                                                                              MethodInvocation invocation) {
        return createSecurityExpressionRoot(() -> authentication, invocation);
    }

    @Override
    public EvaluationContext createEvaluationContext(Supplier<Authentication> authentication, MethodInvocation mi) {
        var root = createSecurityExpressionRoot(authentication, mi);
        var ctx = new IkaritaSecurityEvaluationContext(root, mi, getParameterNameDiscoverer());
        ctx.setBeanResolver(getBeanResolver());
        return ctx;
    }

    private MethodSecurityExpressionOperations createSecurityExpressionRoot(Supplier<Authentication> authentication,
                                                                            MethodInvocation invocation) {
        final var root = expressionRootSupplier.get();
        root.setThis(invocation.getThis());
        root.setPermissionEvaluator(getPermissionEvaluator());
        root.setTrustResolver(getTrustResolver());
        root.setRoleHierarchy(getRoleHierarchy());
        root.setDefaultRolePrefix(getDefaultRolePrefix());
        return root;
    }

    static class IkaritaSecurityEvaluationContext extends MethodBasedEvaluationContext {

        IkaritaSecurityEvaluationContext(MethodSecurityExpressionOperations root, MethodInvocation mi,
                                          ParameterNameDiscoverer parameterNameDiscoverer) {
            super(root, getSpecificMethod(mi), mi.getArguments(), parameterNameDiscoverer);
        }

        private static Method getSpecificMethod(MethodInvocation mi) {
            return AopUtils.getMostSpecificMethod(mi.getMethod(), AopProxyUtils.ultimateTargetClass(Objects.requireNonNull(mi.getThis())));
        }

    }

}
