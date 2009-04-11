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
public class Version17 implements UpgradeDatabaseOneVersion {

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
        int count = Db.RunSQLUpdate("DROP TABLE genre");
        //-----------------------------------
        //-----------------------------------

        //-----------------------------------
        //-----------------------------------
        int counts = Db.RunSQLUpdate("DROP TABLE equipment");
        //-----------------------------------
        //-----------------------------------

        //-----------------------------------
        //-----------------------------------
        int count5 = Db.RunSQLUpdate("DROP TABLE musclegroup");
        //-----------------------------------
        //-----------------------------------

        //-----------------------------------
        //-----------------------------------
        int countg = Db.RunSQLUpdate("DROP TABLE exercisemusclegroupjoin");
        //-----------------------------------
        //-----------------------------------

        //-----------------------------------
        //-----------------------------------
        int countp = Db.RunSQLUpdate("DROP TABLE exercisegenrejoin");
        //-----------------------------------
        //-----------------------------------

        //-----------------------------------
        //-----------------------------------
        int counte = Db.RunSQLUpdate("DROP TABLE exerciseequipmentjoin");
        //-----------------------------------
        //-----------------------------------

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