package com.pingfit.startup;

import com.pingfit.cache.providers.CacheFactory;
import com.pingfit.systemprops.WebAppRootDir;
import com.pingfit.systemprops.InstanceProperties;
import com.pingfit.systemprops.SystemProperty;
import com.pingfit.dao.hibernate.HibernateUtil;
import com.pingfit.dao.hibernate.HibernateSessionQuartzCloser;
import com.pingfit.dao.Pl;
import com.pingfit.xmpp.SendXMPPMessage;
import com.pingfit.scheduledjobs.SystemStats;
import com.pingfit.pageperformance.PagePerformanceUtil;
import com.pingfit.systemexercises.SystemExercises;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.ObjectName;

import org.apache.log4j.Logger;
import org.quartz.SchedulerFactory;
import org.quartz.Scheduler;
import org.quartz.impl.StdSchedulerFactory;


import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.List;

/**
 * User: Joe Reger Jr
 * Date: Apr 17, 2006
 * Time: 10:50:54 AM
 */
public class ApplicationStartup implements ServletContextListener {

    private static boolean ishibernateinitialized = false;
    private static boolean iswabapprooddirdiscovered = false;
    private static boolean isdatabasereadyforapprun = false;
    private static boolean isappstarted = false;

    Logger logger = Logger.getLogger(this.getClass().getName());
    private static Scheduler scheduler = null;

    public void contextInitialized(ServletContextEvent cse) {
       System.out.println("PINGFIT: Application initialized");
        printBug();
       //Shut down mbeans, if they're running
       shutdownCacheMBean();
       //Configure some dir stuff
        WebAppRootDir ward = new WebAppRootDir(cse.getServletContext());
        iswabapprooddirdiscovered = true;
        //Connect to database
        if (InstanceProperties.haveValidConfig()){
            //Run pre-hibernate db upgrades
            DbVersionCheck dbvcPre = new DbVersionCheck();
            dbvcPre.doCheck(DbVersionCheck.EXECUTE_PREHIBERNATE);
            //Set infinispan jgroups vars
            String jgroupstcpaddress="127.0.0.1";
            try {
                InetAddress addr = InetAddress.getLocalHost();
                byte[] ipAddr = addr.getAddress();
                String hostname = addr.getHostName();
                String ipAddrStr = "";
                for (int i=0; i<ipAddr.length; i++) {
                    if (i > 0) {
                        ipAddrStr += ".";
                    }
                    ipAddrStr += ipAddr[i]&0xFF;
                }
                jgroupstcpaddress=ipAddrStr;
            } catch (UnknownHostException e) {
                logger.error("", e);
            } catch (Exception ex){
                logger.error("", ex);
            }
            System.setProperty("jgroups.tcp.address", jgroupstcpaddress);
            System.out.println("jgroups.tcp.address="+jgroupstcpaddress);
            System.setProperty("jgroups.tcpping.initial_hosts", InstanceProperties.getJgroupstcppinginitialhosts());
            System.out.println("jgroups.tcpping.initial_hosts="+InstanceProperties.getJgroupstcppinginitialhosts());
            System.setProperty("jgroups.tcp.port", InstanceProperties.getJgroupstcpport());
            System.out.println("jgroups.tcp.port="+InstanceProperties.getJgroupstcpport());
            //Initialize object cache so it only creates one instance of itself
            CacheFactory.getCacheProvider().get("applicationstartup", "applicationstartup");
            //Set up hibernate
            HibernateUtil.getSession();
            ishibernateinitialized = true;
            //Run post-hibernate db upgrades
            DbVersionCheck dbvcPost = new DbVersionCheck();
            dbvcPost.doCheck(DbVersionCheck.EXECUTE_POSTHIBERNATE);
            //Check to make sure we're good to go
            if (RequiredDatabaseVersion.getHavecorrectversion()){
                isdatabasereadyforapprun = true;
                isappstarted = true;
            }
            //Configure Log4j
            //Logger.getRootLogger().setLevel();
        } else {
            logger.info("InstanceProperties.haveValidConfig()=false");
        }
        if (isdatabasereadyforapprun){
            //Make sure at least one PL exists
            guaranteeAtLeastOnePlExists();
        }
        //Load SystemProps
        SystemProperty.refreshAllProps();
        //Refresh SystemStats
        SystemStats ss = new SystemStats();
        try{ss.execute(null);}catch(Exception ex){logger.error("",ex);}
        //Initialize Quartz
        initQuartz(cse.getServletContext());
        //Add Quartz listener
        try{
            SchedulerFactory schedFact = new StdSchedulerFactory();
            schedFact.getScheduler().addGlobalJobListener(new HibernateSessionQuartzCloser());
        } catch (Exception ex){logger.error("",ex);}
        //Report to log and XMPP
        logger.info("WebAppRootDir = " + WebAppRootDir.getWebAppRootPath());
        logger.info("PINGFIT Application Started!  Let's make some healthiness!");
        SendXMPPMessage xmpp = new SendXMPPMessage(SendXMPPMessage.GROUP_SYSADMINS, "pingFit Application started! ("+WebAppRootDir.getUniqueContextId()+")");
        xmpp.send();
    }

    public void contextDestroyed(ServletContextEvent cse) {
        //Record last of pageperformance numbers
        try{PagePerformanceUtil.recordAndFlush();}catch(Exception ex){logger.error("", ex);}
        //Notify sysadmins
        SendXMPPMessage xmpp = new SendXMPPMessage(SendXMPPMessage.GROUP_SYSADMINS, "pingFit Application shut down! ("+WebAppRootDir.getUniqueContextId()+")");
        xmpp.send();
        //Shut down Hibernate
        try{
            HibernateUtil.closeSession();
            HibernateUtil.killSessionFactory();
        } catch (Exception ex){logger.error("",ex);}
        //Shut down MBeans
        shutdownCacheMBean();
        //Log it
        System.out.println("PINGFIT: Application shut down! ("+InstanceProperties.getInstancename()+")");
    }

    public static void initQuartz(ServletContext sc){

        //
        //If there are errors in this code, check the org.quartz.ee.servlet.QuartzInitializerServlet
        //I grabbed this code from there instead of having the app server call it from web.xml
        //Potential problem with web.xml is that I may not add my listeners quickly enough.
        //
        Logger logger = Logger.getLogger(ApplicationStartup.class);
        logger.debug("Quartz Initializing");
        String QUARTZ_FACTORY_KEY = "org.quartz.impl.StdSchedulerFactory.KEY";
		StdSchedulerFactory factory;
		try {

			String configFile = null;
			if (configFile != null) {
				factory = new StdSchedulerFactory(configFile);
			} else {
				factory = new StdSchedulerFactory();
			}

			// Should the Scheduler being started now or later
			String startOnLoad = null;
			if (startOnLoad == null || (Boolean.valueOf(startOnLoad).booleanValue())) {
				// Start now
				scheduler = factory.getScheduler();
				scheduler.start();
				logger.debug("Quartz Scheduler has been started");
			} else {
				logger.debug("Quartz Scheduler has not been started - Use scheduler.start()");
			}

			logger.debug("Quartz Scheduler Factory stored in servlet context at key: " + QUARTZ_FACTORY_KEY);
			sc.setAttribute(QUARTZ_FACTORY_KEY, factory);

		} catch (Exception e) {
			logger.error("Quartz failed to initialize", e);
		}
    }

    public static void shutdownCacheMBean(){
        Logger logger = Logger.getLogger(ApplicationStartup.class);
        try{
            ArrayList servers = MBeanServerFactory.findMBeanServer(null);
            for (Iterator it = servers.iterator(); it.hasNext(); ) {
                try{
                    MBeanServer mBeanServer = (MBeanServer)it.next();
                    //List of beans to log
                    Set mBeanNames = mBeanServer.queryNames(null, null);
                    for (Iterator iterator = mBeanNames.iterator(); iterator.hasNext();) {
                        ObjectName objectName = (ObjectName) iterator.next();
                        //logger.debug("MBean -> Name:"+objectName.getCanonicalName()+" Domain:"+objectName.getDomain());
                        if (objectName.getCanonicalName().indexOf("pingFit-TreeCache-Cluster")>-1){
                            try{
                                logger.info("Unregistering MBean: "+objectName.getCanonicalName());
                                mBeanServer.unregisterMBean(objectName);
                            } catch (Exception ex){
                                logger.error("",ex);
                            }
                        }
                    }
                } catch (Exception ex){
                    logger.error("",ex);
                }
            }
        } catch (Exception ex){
            logger.error("",ex);
        }
    }

    public static void shutdownMBean(String name){
        Logger logger = Logger.getLogger(ApplicationStartup.class);
        try{
            ArrayList servers = MBeanServerFactory.findMBeanServer(null);
            for (Iterator it = servers.iterator(); it.hasNext(); ) {
                try{
                    MBeanServer mBeanServer = (MBeanServer)it.next();
                    //Do the remove
                    ObjectName tcObject = new ObjectName(name);
                    if (mBeanServer.isRegistered(tcObject)){
                        logger.info(tcObject.getCanonicalName()+" was already registered");
                        try{
                            logger.info("Unregistering MBean: "+tcObject.getCanonicalName());
                            mBeanServer.unregisterMBean(tcObject);
                        } catch (Exception ex){
                            logger.error("",ex);
                        }
                    } else {
                        logger.info(tcObject.getCanonicalName()+" was *not* already registered");
                    }
                } catch (Exception ex){
                    logger.error("",ex);
                }
            }
        } catch (Exception ex){
            logger.error("",ex);
        }
    }

    private static void guaranteeAtLeastOnePlExists(){
        Logger logger = Logger.getLogger(ApplicationStartup.class);
        List pls = HibernateUtil.getSession().createQuery("from Pl").list();
        if (pls==null || pls.size()<=0){
            Pl pl = new Pl();
            pl.setName("pingFit.com");
            pl.setSubdomain("");
            pl.setCustomdomain1("");
            pl.setCustomdomain2("");
            pl.setCustomdomain3("");
            pl.setEmailhtmlfooter("");
            pl.setEmailhtmlheader("");
            pl.setWebhtmlfooter("");
            pl.setWebhtmlheader("");
            pl.setIshttpson(false);
            pl.setNameforui("pingFit.com");
            pl.setTwitterusername("");
            pl.setTwitterpassword("");
            pl.setHomepagetemplate("");
            pl.setPeers("0");
            pl.setAirlogo("");
            pl.setAirbgcolor("");
            pl.setIsdefault(true);
            try{pl.save();}catch(Exception ex){logger.error(ex);}
        }
    }


    public static boolean getIswabapprooddirdiscovered() {
        return iswabapprooddirdiscovered;
    }

    public static boolean getIshibernateinitialized() {
        return ishibernateinitialized;
    }

    public static boolean getIsdatabasereadyforapprun() {
        return isdatabasereadyforapprun;
    }

    public static boolean getIsappstarted() {
        return isappstarted;
    }

    public static void printBug(){
        StringBuffer out = new StringBuffer();
        out.append("\n\n\n"+"                                .::                       .\n" +
                "                               :::::::....                `::\n" +
                "                          .::::::::::::::::::..::::::::::::::.\n" +
                "                   .:  .::::::::::::::::::::::::::::::::::::::::\n" +
                "                  ::::::::::::::::::::::::::::::::::::::::::::::::\n" +
                "                  `:::::::::::::::``::::::::::: `::::::::::::::::::\n" +
                "                   ::::::::::'zc$$$b`:',cc,`:::' :''``,c=`:::::::::::'\n" +
                "                  ::::::::'::: $$$$$$$$$$$$$c,,u,zd$$$$$c,',zc,`::::'\n" +
                "                  ::::::'z$ccd$$$$$P\" . \"$$$$$$$$$$$$.. `?$$$$\".::::\n" +
                "                 :::::::dP?$$$$$$$\" d$$$$$$$$$$$$$$$$$$$b.\"$$L,`:::\n" +
                "               :::::::::.::$$$$$$ z$$$$$$$$$$$$$$$$$$$$$$$c\"$$$$,::..\n" +
                "                `'::::::'`.$$$$$$$\"$F<$\"3$$$$$$$$$$$$$$?$$?$$P:..:''\n" +
                "                  `:::::`$$$$$$$\"?   .. ?\"$$$$$$$$$$r`\" \" <\"3$c`:\n" +
                "                    :::::.\"?:`$$F    d$$.<$$$$$$$$P    4$c $\"\"\"/\n" +
                "                     :::::::::J$ .,,$$$$P $$$$$$$$>   ,$$$F  ::::\n" +
                "                  `:::'```'',d$$c`?$$$$P\"J$$$$$$$$.\"$$$$$P db`''\n" +
                "                      f,r4b4$$$$$c ,`\"\".-$$$$$$$$$$c \"\"\"\" c$$$ b\n" +
                "                      F ,$\"d$$$$$$$$c$bd$$$$$$$ $$$$b$bJ$L$$$P P\n" +
                "                            \"?$$$$$$$$$$$$$$$P??$$$$$$$$$$$P\" '\n" +
                "                               `\"\"\"???$PFFF\"\"    \"\"\"\"\"\"\"\"\n" +
                "                            4$$$$$$cdccccc$$$bcc$$$$$$$$$$$$bc\n" +
                "                           d$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$bc\n" +
                "                          $$$$$$$$$$$??????$$$FF?????P 3$$$$$$$$$$c.\n" +
                "                         d$$$$$$$$P':::::::`?'::::::::   \"?$$$$$$$$$$c.\n" +
                "                        4$$$$$P  $ :::::::::::::::::::      \"?$$$$$$$$$c\n" +
                "                        $$$$$P  `$.::::::::::::::::::          `\"\".$$$$$c\n" +
                "                      .$$$$$$'   `$-`::::::::::::::'             c$$$$$$$\n" +
                "                     z$$$$$$'      ::::::::::::::::            .$$$$$$$$\n" +
                "                   .d$$$$$$'       `:::::::::::::::           .$$$$$$$F'\n" +
                "                  d$$$$$$$'         `::::::::::::::          ,$$$$$$P\"\n" +
                "                .$$$$$$$F            ::::::::::::::: 3c,  . 4$$$$P\"'\n" +
                "               z$$$$$$$\"           :::::::::::::: ==$$$$$c % \"?\"\n" +
                "              z$$$$$$\"           .::::::::::::::::..:3$$$$P L\n" +
                "              `?$$$\"            ::::::::::::::::::::.?::: . $\n" +
                "   .,,,,ccc$L ? \"\"             :::::::::::::::::::::::::::.c%\n" +
                " :$$ ?=?P$$$%                 ::::::::::::::::::::::::::::\n" +
                ":\"???$%==\"\"  \"d               :::::::::::::::::::::::::::::    .\n" +
                "                             `:::::::::::::::::::::::::::::::::::\n" +
                "                              ::::::::::::::::::::::::::::::::::\n" +
                "                         :   :::::::::::::::::````,,,,zcc`''''`\n" +
                "                         :::::::::::'zc,,,,,cd$$ `???\"\"\"\"\n" +
                "                         `::::::::',d$$$$$$$$$$$      =\n" +
                "                           `````` $$$$$$$$$$$$$$ $$$$$c$F\n" +
                "                                  `$$$$$$$$$$$$$ $$$$$$F\n" +
                "                                   `$$$$$$$$$$$F<$$$$$$\n" +
                "                                    `$$$$$$$$$$'$$$$$$F\n" +
                "                                     4$$$$$$$$F<$$$$$P\n" +
                "                                    z$$$$$$$$F $$$$$P\n" +
                "                                   c$$$$$$$$$ J$$$$$F\n" +
                "                                 .$$$$$$$$$$\"z$$$$$$\n" +
                "                                 $$$$$$$$$P c$$$$$$$\n" +
                "                                $$$$$$$$$\" 4$$$$$$$F\n" +
                "                               d$$$$$$$$\"  $$$$$$$$'\n" +
                "                              .$$$$$$$P    4$$$$$$$\n" +
                "                              J$$$$$$\"      $$$$$$F\n" +
                "                              $$$$$P        ?$$$$$\n" +
                "                             d$$$$F         <$$$$F\n" +
                "                             $$$P\"           $$$P\n" +
                "                            J$$P            <$$$'\n" +
                "                           .$$P             $$$$\n" +
                "                          .$$$             `$$$L\n" +
                "                         z$$$F            / \"$$$\n" +
                "                        $$$$%/           ( ;. \"?.\n" +
                "                       ;\"?\"\">            ``  `.  \\\n" +
                "                       \\.- `                   `--'" +
                "          ;                            ' "+"\n\n\n\n");
        out.append("PingFit: NEW AND IMPROVED... NOW WITH MORE BUGS!"+"\n\n\n");

        System.out.print(out.toString());
    }


}
