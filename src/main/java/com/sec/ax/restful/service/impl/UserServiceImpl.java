package com.sec.ax.restful.service.impl;

import java.util.Calendar;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;

import com.sec.ax.restful.common.Constant;
import com.sec.ax.restful.crypt.AxCryptException;
import com.sec.ax.restful.crypt.aes.AxCrypt;
import com.sec.ax.restful.persistence.UserPersistence;
import com.sec.ax.restful.pojo.Query;
import com.sec.ax.restful.pojo.User;
import com.sec.ax.restful.service.UserService;

/**
 * 
 * @author heesik.jeon
 *
 */

public class UserServiceImpl implements UserService {

    private static final Logger logger = Logger.getLogger(UserServiceImpl.class);
    
    @Autowired
    private UserPersistence persistence;
    
    /* 
     * @see com.sec.ax.restful.service.UserService#signup(com.sec.ax.restful.pojo.User)
     */
    @Override
    public int signup(User user) throws DataAccessException {

        logger.debug("..");
        
        return persistence.signup(user);
        
    }
    
    /* 
     * @see com.sec.ax.restful.service.UserService#sid(com.sec.ax.restful.pojo.User)
     */
    @Override
    public int sid(User user) throws DataAccessException {

        logger.debug("..");
        
        return persistence.sid(user);
        
    }

    /* 
     * @see com.sec.ax.restful.service.UserService#signin(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, com.sec.ax.restful.pojo.User)
     */
    @Override
    public User signin(HttpServletRequest request, HttpServletResponse response, User user) throws DataAccessException, AxCryptException {

        logger.debug("..");
        
        user = persistence.signin(user);
        
        return cookie(request, response, user);
        
    }
    
    /* 
     * @see com.sec.ax.restful.service.UserService#cookie(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, com.sec.ax.restful.pojo.User)
     */
    @Override
    public User cookie(HttpServletRequest request, HttpServletResponse response, User user) throws AxCryptException {

        if (user != null) {

            StringBuffer Ax = new StringBuffer();
            
            Ax.append(user.getName()).append("|");
            Ax.append(user.getSid()).append("|");
            Ax.append(user.getUsername()).append("|");
            Ax.append(user.getRole()).append("|");
            Ax.append(request.getRemoteAddr()).append("|");
            
            Calendar c = Calendar.getInstance();
            Ax.append(c.getTimeInMillis());
            
            String crypted = AxCrypt.encrypt(Ax);
            
            logger.debug(crypted);
            
            Cookie cookie = new Cookie("Ax", crypted);
            
//            cookie.setDomain(Constant.COOKIE_DOMAIN);
            cookie.setMaxAge(Constant.COOKIE_MAX_AGE);
            cookie.setPath("/");

            response.addCookie(cookie);

            return user;
            
        }

        return null;

    }
    
    /* 
     * @see com.sec.ax.restful.service.UserService#signout(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    public void signout(HttpServletRequest request, HttpServletResponse response) {
        
        Cookie[] cookies = request.getCookies();
        
        if (cookies != null) {
            
            for(Cookie cookie : cookies) {
                
                logger.debug(cookie.getName() + ": " + cookie.getValue());
                
                if (StringUtils.equals(Constant.COOKIE_USER_KEY, cookie.getName())) {

//                    cookie.setDomain(Constant.COOKIE_DOMAIN);
                    cookie.setMaxAge(Constant.COOKIE_EXPIRY);
                    cookie.setPath("/");

                    response.addCookie(cookie);
                    
                    break;
                    
                }
                
            }
            
        }

    }

    /* 
     * @see com.sec.ax.restful.service.UserService#update(com.sec.ax.restful.pojo.User)
     */
    @Override
    public int update(User user) throws DataAccessException {

        logger.debug("..");
        
        return persistence.update(user);
        
    }

    /* 
     * @see com.sec.ax.restful.service.UserService#delete(com.sec.ax.restful.pojo.User)
     */
    @Override
    public int delete(User user) throws DataAccessException {

        logger.debug("..");
        
        return persistence.delete(user);
        
    }
    
    /* 
     * @see com.sec.ax.restful.service.UserService#name(java.lang.String)
     */
    @Override
    public User name(String name) throws DataAccessException {

        logger.debug("..");
        
        return persistence.name(name);
        
    }
    
    /*
     * @see com.sec.ax.restful.service.UserService#count(com.sec.ax.restful.pojo.Query)
     */
    @Override
    public int count(Query query) throws DataAccessException {

        logger.debug("..");
        
        int cnt = persistence.count(query);
        
        return cnt;
        
    }
    
    /* 
     * @see com.sec.ax.restful.service.UserService#list(com.sec.ax.restful.pojo.Query)
     */
    @Override
    public List<User> list(Query query) throws DataAccessException {

        logger.debug("..");
        
        return persistence.list(query);
        
    }

}
