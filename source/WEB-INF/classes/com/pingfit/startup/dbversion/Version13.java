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
public class Version13 implements UpgradeDatabaseOneVersion {

    Logger logger = Logger.getLogger(this.getClass().getName());

    public void doPreHibernateUpgrade(){
        logger.debug("doPreHibernateUpgrade() start");
        logger.debug("Not really doing anything.");
        logger.debug("doPreHibernateUpgrade() finish");
    }

    public void doPostHibernateUpgrade(){
        logger.debug("doPostHibernateUpgrade() start");

//        if (1==1){int identity = Db.RunSQLInsert("INSERT INTO musclegroup(name) VALUES('Abs:Lower')");}
//        if (1==1){int identity = Db.RunSQLInsert("INSERT INTO musclegroup(name) VALUES('Abs:Side')");}
//        if (1==1){int identity = Db.RunSQLInsert("INSERT INTO musclegroup(name) VALUES('Abs:Upper')");}
//        if (1==1){int identity = Db.RunSQLInsert("INSERT INTO musclegroup(name) VALUES('Back')");}
//        if (1==1){int identity = Db.RunSQLInsert("INSERT INTO musclegroup(name) VALUES('Back:Lower')");}
//        if (1==1){int identity = Db.RunSQLInsert("INSERT INTO musclegroup(name) VALUES('Biceps')");}
//        if (1==1){int identity = Db.RunSQLInsert("INSERT INTO musclegroup(name) VALUES('Buttocks')");}
//        if (1==1){int identity = Db.RunSQLInsert("INSERT INTO musclegroup(name) VALUES('Calves')");}
//        if (1==1){int identity = Db.RunSQLInsert("INSERT INTO musclegroup(name) VALUES('Chest')");}
//        if (1==1){int identity = Db.RunSQLInsert("INSERT INTO musclegroup(name) VALUES('Forearms')");}
//        if (1==1){int identity = Db.RunSQLInsert("INSERT INTO musclegroup(name) VALUES('Groin/Inner Thigh')");}
//        if (1==1){int identity = Db.RunSQLInsert("INSERT INTO musclegroup(name) VALUES('Hamstring')");}
//        if (1==1){int identity = Db.RunSQLInsert("INSERT INTO musclegroup(name) VALUES('Outer Thigh/Hips')");}
//        if (1==1){int identity = Db.RunSQLInsert("INSERT INTO musclegroup(name) VALUES('Quads')");}
//        if (1==1){int identity = Db.RunSQLInsert("INSERT INTO musclegroup(name) VALUES('Shoulders:Rear')");}
//        if (1==1){int identity = Db.RunSQLInsert("INSERT INTO musclegroup(name) VALUES('Shoulders:Side')");}
//        if (1==1){int identity = Db.RunSQLInsert("INSERT INTO musclegroup(name) VALUES('Shoulders:Front')");}
//        if (1==1){int identity = Db.RunSQLInsert("INSERT INTO musclegroup(name) VALUES('Neck/Trapezius')");}
//        if (1==1){int identity = Db.RunSQLInsert("INSERT INTO musclegroup(name) VALUES('Triceps')");}
//        if (1==1){int identity = Db.RunSQLInsert("INSERT INTO musclegroup(name) VALUES('Wrist')");}
//
//        if (1==1){int identity = Db.RunSQLInsert("INSERT INTO equipment(name) VALUES('Balance Board')");}
//        if (1==1){int identity = Db.RunSQLInsert("INSERT INTO equipment(name) VALUES('Bench')");}
//        if (1==1){int identity = Db.RunSQLInsert("INSERT INTO equipment(name) VALUES('Chair')");}
//        if (1==1){int identity = Db.RunSQLInsert("INSERT INTO equipment(name) VALUES('Desk')");}
//        if (1==1){int identity = Db.RunSQLInsert("INSERT INTO equipment(name) VALUES('Exercise Balls')");}
//        if (1==1){int identity = Db.RunSQLInsert("INSERT INTO equipment(name) VALUES('Exercise Mat')");}
//        if (1==1){int identity = Db.RunSQLInsert("INSERT INTO equipment(name) VALUES('Exercise Tube')");}
//        if (1==1){int identity = Db.RunSQLInsert("INSERT INTO equipment(name) VALUES('Free Weights')");}
//        if (1==1){int identity = Db.RunSQLInsert("INSERT INTO equipment(name) VALUES('Machines')");}
//        if (1==1){int identity = Db.RunSQLInsert("INSERT INTO equipment(name) VALUES('Medicine Ball')");}
//        if (1==1){int identity = Db.RunSQLInsert("INSERT INTO equipment(name) VALUES('Toning Bar')");}
//        if (1==1){int identity = Db.RunSQLInsert("INSERT INTO equipment(name) VALUES('Pullup Bar')");}
//        if (1==1){int identity = Db.RunSQLInsert("INSERT INTO equipment(name) VALUES('Situp Bar')");}
//
//        if (1==1){int identity = Db.RunSQLInsert("INSERT INTO genre(name) VALUES('General Strength')");}
//        if (1==1){int identity = Db.RunSQLInsert("INSERT INTO genre(name) VALUES('Stretching')");}
//        if (1==1){int identity = Db.RunSQLInsert("INSERT INTO genre(name) VALUES('Plyometrics')");}
//        if (1==1){int identity = Db.RunSQLInsert("INSERT INTO genre(name) VALUES('Pilates')");}
//        if (1==1){int identity = Db.RunSQLInsert("INSERT INTO genre(name) VALUES('Yoga')");}
//        if (1==1){int identity = Db.RunSQLInsert("INSERT INTO genre(name) VALUES('Aerobics')");}
//        if (1==1){int identity = Db.RunSQLInsert("INSERT INTO genre(name) VALUES('Relaxation')");}
//        if (1==1){int identity = Db.RunSQLInsert("INSERT INTO genre(name) VALUES('Step')");}
//        if (1==1){int identity = Db.RunSQLInsert("INSERT INTO genre(name) VALUES('Core Strength')");}
//        if (1==1){int identity = Db.RunSQLInsert("INSERT INTO genre(name) VALUES('Strength')");}
//        if (1==1){int identity = Db.RunSQLInsert("INSERT INTO genre(name) VALUES('Office Friendly')");}
//        if (1==1){int identity = Db.RunSQLInsert("INSERT INTO genre(name) VALUES('Pregnant Friendly')");}
//        if (1==1){int identity = Db.RunSQLInsert("INSERT INTO genre(name) VALUES('Geriatric Friendly')");}
//        if (1==1){int identity = Db.RunSQLInsert("INSERT INTO genre(name) VALUES('Obese Friendly')");}
//        if (1==1){int identity = Db.RunSQLInsert("INSERT INTO genre(name) VALUES('Multiple Person')");}

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