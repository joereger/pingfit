package com.pingfit.session;

import com.pingfit.dao.User;

/**
 * User: Joe Reger Jr
 * Date: Sep 10, 2006
 * Time: 11:38:48 PM
 */
public interface AuthControlled {

    public boolean canRead(User user);
    public boolean canEdit(User user);


}
