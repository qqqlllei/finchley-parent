package com.auth.server.security.integration;

import com.auth.server.security.constants.SecurityConstant;
import com.auth.server.security.integration.authenticator.IntegrationAuthenticator;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.http.HttpMethod;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;


@Component
public class IntegrationAuthenticationFilter extends OncePerRequestFilter implements ApplicationContextAware {



    private Collection<IntegrationAuthenticator> authenticators;

    private ApplicationContext applicationContext;

    private RequestMatcher requestMatcher;

    @Autowired
    private ClientDetailsService clientDetailsService;

    public IntegrationAuthenticationFilter(){
        this.requestMatcher = new OrRequestMatcher(
                new AntPathRequestMatcher(SecurityConstant.OAUTH_TOKEN_URL, HttpMethod.GET.name()),
                new AntPathRequestMatcher(SecurityConstant.OAUTH_TOKEN_URL, HttpMethod.POST.name()),
                new AntPathRequestMatcher(SecurityConstant.COMMON_LOGIN_URL, HttpMethod.POST.name())
        );
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(requestMatcher.matches(request)){
            IntegrationAuthentication integrationAuthentication = new IntegrationAuthentication();
            integrationAuthentication.setAuthType(request.getParameter(SecurityConstant.AUTH_TYPE_PARM_NAME));
            Enumeration<String> enumeration =  request.getParameterNames();
            Map<String,String> params = new HashMap<>();
            while (enumeration.hasMoreElements()){
                String key = enumeration.nextElement();
                String value = request.getParameter(key);
                params.put(key,value);
            }
            integrationAuthentication.setAuthParameters(params);
            IntegrationAuthenticationContext.set(integrationAuthentication);
            try{

                if(this.prepare(integrationAuthentication,request,response)){
                    filterChain.doFilter(request,response);
                    this.complete(integrationAuthentication,request,response);
                }

            }finally {
                IntegrationAuthenticationContext.clear();
            }
        }else{
            filterChain.doFilter(request,response);
        }
    }

    private boolean prepare(IntegrationAuthentication integrationAuthentication,HttpServletRequest request, HttpServletResponse response) {

        if(this.authenticators == null){
            synchronized (this){
                Map<String,IntegrationAuthenticator> integrationAuthenticatorMap = applicationContext.getBeansOfType(IntegrationAuthenticator.class);
                if(integrationAuthenticatorMap != null){
                    this.authenticators = integrationAuthenticatorMap.values();
                }
            }
        }

        if(this.authenticators == null){
            this.authenticators = new ArrayList<>();
        }

        for (IntegrationAuthenticator authenticator: authenticators) {
            if(authenticator.support(integrationAuthentication)){
                String clientId = integrationAuthentication.getAuthParameterByKey(SecurityConstant.WECHAT_CLIENT_ID_PARAM_NAME);
                ClientDetails clientDetails = clientDetailsService.loadClientByClientId(clientId);
                Map<String,Object> additionalInformation =  clientDetails.getAdditionalInformation();
                integrationAuthentication.setFindUserClassName(String.valueOf(additionalInformation.get(SecurityConstant.AUTH_FIND_USER_INTERFACE_CLASS)));
                return authenticator.prepare(integrationAuthentication,additionalInformation,request,response);
            }
        }

        return true;
    }

    private void complete(IntegrationAuthentication integrationAuthentication,HttpServletRequest request, HttpServletResponse response){
        for (IntegrationAuthenticator authenticator: authenticators) {
            if(authenticator.support(integrationAuthentication)){
                authenticator.complete(integrationAuthentication,request,response);
            }
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
