package com.pingfit.startup.dbversion;

import com.pingfit.startup.UpgradeDatabaseOneVersion;
import com.pingfit.systemexercises.SystemExercises;
import com.pingfit.systemexercises.SystemExerciseLists;
import com.pingfit.db.Db;
import org.apache.log4j.Logger;

/**
 * User: Joe Reger Jr
 * Date: Nov 26, 2006
 * Time: 11:57:46 AM
 */
public class Version11 implements UpgradeDatabaseOneVersion {

    Logger logger = Logger.getLogger(this.getClass().getName());

    public void doPreHibernateUpgrade(){
        logger.debug("doPreHibernateUpgrade() start");
        logger.debug("Not really doing anything.");
        logger.debug("doPreHibernateUpgrade() finish");
    }

    public void doPostHibernateUpgrade(){
        logger.debug("doPostHibernateUpgrade() start");


        //-----------------------------------
        //-----------------------------------
        //int count3 = Db.RunSQLUpdate("UPDATE user SET nickname=user.firstname+user.lastname");
        //-----------------------------------
        //-----------------------------------

        //-----------------------------------
        //-----------------------------------
        String[][] rstUsers= Db.RunSQL("SELECT userid, firstname, lastname FROM user");
        //-----------------------------------
        //-----------------------------------
        if (rstUsers!=null && rstUsers.length>0){
            for(int i=0; i<rstUsers.length; i++){
                //-----------------------------------
                //-----------------------------------
                int count = Db.RunSQLUpdate("UPDATE user SET nickname='"+rstUsers[i][1]+rstUsers[i][2]+"' WHERE userid='"+rstUsers[i][0]+"'");
                //-----------------------------------
                //-----------------------------------   
            }
        }




        logger.debug("doPostHibernateUpgrade() finish");
    }


    //Sample sql statements

    //-----------------------------------
    //-----------------------------------
    //int count = Db.RunSQLUpdate("CREATE TABLE `pltemplate` (`pltemplateid` int(11) NOT NULL auto_increment, logid int(11), plid int(11), type int(11), templateid int(11), PRIMARY KEY  (`pltemplateid`)) ENGINE=MyISAM DEFAULT CHARSET=latin1;");
    //-----------------------------------
    //-----------------------------------

    //-----------------------------------
    //-----------------------------------
    //int count = Db.RunSQLUpdate("ALTER TABLE megachart CHANGE daterangesavedsearchid daterangesavedsearchid int(11) NOT NULL default '0'");
    //-----------------------------------
    //-----------------------------------

    //-----------------------------------
    //-----------------------------------
    //int count = Db.RunSQLUpdate("ALTER TABLE account DROP gps");
    //-----------------------------------
    //-----------------------------------

    //-----------------------------------
    //-----------------------------------
    //int count = Db.RunSQLUpdate("ALTER TABLE megalogtype ADD isprivate int(11) NOT NULL default '0'");
    //-----------------------------------
    //-----------------------------------

    //-----------------------------------
    //-----------------------------------
    //int count = Db.RunSQLUpdate("DROP TABLE megafielduser");
    //-----------------------------------
    //-----------------------------------

    //-----------------------------------
    //-----------------------------------
    //int count = Db.RunSQLUpdate("CREATE INDEX name_of_index ON table (field1, field2)");
    //-----------------------------------
    //-----------------------------------


}