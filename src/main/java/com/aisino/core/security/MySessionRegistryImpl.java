package com.aisino.core.security;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.data.redis.core.*;
import org.springframework.security.core.session.SessionDestroyedEvent;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;


/**
 * Created by 为 on 2017-6-9.
 */

public class MySessionRegistryImpl implements SessionRegistry, ApplicationListener<SessionDestroyedEvent> {


    private static final String SESSIONIDS = "sessionIds";

    private static final String PRINCIPALS = "principals";

    @Resource
    private RedisTemplate redisTemplate;

    protected final Log logger = LogFactory.getLog(SessionRegistryImpl.class);
//    private final ConcurrentMap<Object, Set<String>> principals = new ConcurrentHashMap();
//    private final Map<String, SessionInformation> sessionIds = new ConcurrentHashMap();

    public MySessionRegistryImpl() {
    }

    public List<Object> getAllPrincipals() {
        return new ArrayList(this.getPrincipalsKeySet());
    }

    public List<SessionInformation> getAllSessions(Object principal, boolean includeExpiredSessions) {
        Set<String> sessionsUsedByPrincipal =  this.getPrincipals(((UserDetails)principal).getUsername());
        if (sessionsUsedByPrincipal == null) {
            return Collections.emptyList();
        } else {
            List<SessionInformation> list = new ArrayList(sessionsUsedByPrincipal.size());
            Iterator var5 = sessionsUsedByPrincipal.iterator();

            while (true) {
                SessionInformation sessionInformation;
                do {
                    do {
                        if (!var5.hasNext()) {
                            return list;
                        }

                        String sessionId = (String) var5.next();
                        sessionInformation = this.getSessionInformation(sessionId);
                    } while (sessionInformation == null);
                } while (!includeExpiredSessions && sessionInformation.isExpired());

                list.add(sessionInformation);
            }
        }
    }

    public SessionInformation getSessionInformation(String sessionId) {
        Assert.hasText(sessionId, "SessionId required as per interface contract");
        return (SessionInformation) this.getSessionInfo(sessionId);
    }

    public void onApplicationEvent(SessionDestroyedEvent event) {
        String sessionId = event.getId();
        this.removeSessionInformation(sessionId);
    }

    public void refreshLastRequest(String sessionId) {
        Assert.hasText(sessionId, "SessionId required as per interface contract");
        SessionInformation info = this.getSessionInformation(sessionId);
        if (info != null) {
            info.refreshLastRequest();
        }

    }

    public void registerNewSession(String sessionId, Object principal) {
        Assert.hasText(sessionId, "SessionId required as per interface contract");
        Assert.notNull(principal, "Principal required as per interface contract");
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Registering session " + sessionId + ", for principal " + principal);
        }

        if (this.getSessionInformation(sessionId) != null) {
            this.removeSessionInformation(sessionId);
        }

        this.addSessionInfo(sessionId, new SessionInformation(principal, sessionId, new Date()));

//        this.sessionIds.put(sessionId, new SessionInformation(principal, sessionId, new Date()));
        Set<String> sessionsUsedByPrincipal = (Set) this.getPrincipals(principal.toString());
        if (sessionsUsedByPrincipal == null) {
            sessionsUsedByPrincipal = new CopyOnWriteArraySet();
            Set<String> prevSessionsUsedByPrincipal = (Set) this.putIfAbsentPrincipals(principal.toString(), sessionsUsedByPrincipal);
            if (prevSessionsUsedByPrincipal != null) {
                sessionsUsedByPrincipal = prevSessionsUsedByPrincipal;
            }
        }

        ((Set) sessionsUsedByPrincipal).add(sessionId);
        this.putPrincipals(principal.toString(), sessionsUsedByPrincipal);
        if (this.logger.isTraceEnabled()) {
            this.logger.trace("Sessions used by '" + principal + "' : " + sessionsUsedByPrincipal);
        }

    }

    public void removeSessionInformation(String sessionId) {
        Assert.hasText(sessionId, "SessionId required as per interface contract");
        SessionInformation info = this.getSessionInformation(sessionId);
        if (info != null) {
            if (this.logger.isTraceEnabled()) {
                this.logger.debug("Removing session " + sessionId + " from set of registered sessions");
            }

            this.removeSessionInfo(sessionId);
            Set<String> sessionsUsedByPrincipal = this.getPrincipals(info.getPrincipal().toString());
            if (sessionsUsedByPrincipal != null) {
                if (this.logger.isDebugEnabled()) {
                    this.logger.debug("Removing session " + sessionId + " from principal's set of registered sessions");
                }

                sessionsUsedByPrincipal.remove(sessionId);
                if (sessionsUsedByPrincipal.isEmpty()) {
                    if (this.logger.isDebugEnabled()) {
                        this.logger.debug("Removing principal " + info.getPrincipal() + " from registry");
                    }

                    try{
                        this.removePrincipal(((UserDetails)info.getPrincipal()).getUsername());
                    }catch (Exception ex){
                        this.removePrincipal(String.valueOf(info.getPrincipal()));
                    }

                }

                if (this.logger.isTraceEnabled()) {
                    this.logger.trace("Sessions used by '" + info.getPrincipal() + "' : " + sessionsUsedByPrincipal);
                }

            }
        }
    }


    public void addSessionInfo(final String sessionId, final SessionInformation sessionInformation) {
        BoundHashOperations<String, String, SessionInformation> hashOperations = redisTemplate.boundHashOps(SESSIONIDS);
        hashOperations.put(sessionId, sessionInformation);
    }

    public SessionInformation getSessionInfo(final String sessionId) {
        BoundHashOperations<String, String, SessionInformation> hashOperations = redisTemplate.boundHashOps(SESSIONIDS);
        return hashOperations.get(sessionId);
    }

    public void removeSessionInfo(final String sessionId) {
        BoundHashOperations<String, String, SessionInformation> hashOperations = redisTemplate.boundHashOps(SESSIONIDS);
        hashOperations.delete(sessionId);
    }

    public Set<String> putIfAbsentPrincipals(final String key, final Set<String> set) {
        BoundHashOperations<String, String, Set<String>> hashOperations = redisTemplate.boundHashOps(PRINCIPALS);
        hashOperations.putIfAbsent(key, set);
        return hashOperations.get(key);
    }

    public void putPrincipals(final String key, final Set<String> set) {
        BoundHashOperations<String, String, Set<String>> hashOperations = redisTemplate.boundHashOps(PRINCIPALS);
        hashOperations.put(key,set);
    }

    public Set<String> getPrincipals(final String key) {
        BoundHashOperations<String, String, Set<String>> hashOperations = redisTemplate.boundHashOps(PRINCIPALS);
        return hashOperations.get(key);
    }

    public Set<String> getPrincipalsKeySet() {
        BoundHashOperations<String, String, Set<String>> hashOperations = redisTemplate.boundHashOps(PRINCIPALS);
        return hashOperations.keys();
    }

    public void removePrincipal(final String key) {
        BoundHashOperations<String, String, Set<String>> hashOperations = redisTemplate.boundHashOps(PRINCIPALS);
        hashOperations.delete(key);
    }


}
