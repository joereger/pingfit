package com.pingfit.privatelabel;

import com.pingfit.dao.Pl;
import com.pingfit.util.Num;
import org.apache.log4j.Logger;

/**
 * User: Joe Reger Jr
 * Date: Feb 7, 2009
 * Time: 7:08:35 PM
 */
public class PlPeers {

    public static boolean isThereATwoWayTrustRelationship(Pl pl1, Pl pl2){
        if (isThereOneWayTrust(pl1, pl2) && isThereOneWayTrust(pl2, pl1)){
            return true;
        }
        return false;
    }

    public static boolean isThereOneWayTrust(Pl plDoI, Pl plTrustThisOne){
        Logger logger = Logger.getLogger(PlPeers.class);
        if (plDoI==null || plTrustThisOne==null || plDoI.getPlid()==0 || plTrustThisOne.getPlid()==0){
            //If either one's null, no trust
            return false;
        }
        //If it's the same plid
        if (plDoI.getPlid()==plTrustThisOne.getPlid()){
            return true;
        }
        try{
            //Find those selected in PlDoI
            String[] peersSelected = new String[0];
            if (plDoI.getPeers()!=null && plDoI.getPeers().length()>0){
                peersSelected = plDoI.getPeers().split(",");
            }
            //First, see if zero's in there... that means this pl trusts everybody
            if (1==1){
                for (int i=0; i<peersSelected.length; i++) {
                    String s=peersSelected[i];
                    if (Num.isinteger(s)){
                        if (Integer.parseInt(s)==0){
                            return true;    
                        }
                    }
                }
            }
            //Second, see if this specific one's in there
            if (1==1){
                for (int i=0; i<peersSelected.length; i++) {
                    String s=peersSelected[i];
                    if (Num.isinteger(s)){
                        if (Integer.parseInt(s)==plTrustThisOne.getPlid()){
                            return true;
                        }
                    }
                }
            }
        } catch (Exception ex){
            logger.error("", ex);
        }
        //No trust baby
        return false;
    }

}
