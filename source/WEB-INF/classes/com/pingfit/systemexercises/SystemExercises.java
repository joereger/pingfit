package com.pingfit.systemexercises;

import com.pingfit.dao.Exercise;
import com.pingfit.dao.hibernate.HibernateUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;

/**
 * User: Joe Reger Jr
 * Date: Mar 18, 2008
 * Time: 9:28:25 PM
 */
public class SystemExercises {

    public static void makeSureDatabaseHasAllSystemExercises(){
        Logger logger = Logger.getLogger(SystemExercises.class);
        ArrayList<Exercise> systemExercises = getAllSystemExercises();
        for (Iterator<Exercise> iterator = systemExercises.iterator(); iterator.hasNext();) {
            Exercise ex = iterator.next();
            //Check db to see if this is in there
            List<Exercise> exercisesFromDb = HibernateUtil.getSession().createCriteria(Exercise.class)
                                               .add(Restrictions.eq("title", ex.getTitle()))
                                               .setCacheable(true)
                                               .list();
            if (exercisesFromDb!=null && exercisesFromDb.size()>0){
                for (Iterator<Exercise> iterator1 = exercisesFromDb.iterator(); iterator1.hasNext();) {
                    //Update the existing record
                    Exercise exerciseFromDb = iterator1.next();
                    exerciseFromDb.setDescription(ex.getDescription());
                    exerciseFromDb.setImage(ex.getImage());
                    exerciseFromDb.setReps(ex.getReps());
                    try{exerciseFromDb.save();}catch(Exception exx){logger.error("", exx);}
                }
            } else {
                //Create a new one
                try{ex.save();}catch(Exception exx){logger.error("", exx);}
            }
        }
    }

    private static ArrayList<Exercise> getAllSystemExercises(){
        Logger logger = Logger.getLogger(SystemExercises.class);
        ArrayList<Exercise> out = new ArrayList<Exercise>();

        //THE TITLE IS THE KEY... CHANGE IT AND YOU"LL SEE DUPES

        //@todo WTF is this?????

        //Grabbed the images from:
        //http://www.virtualdietcenter.com/dt/exerciseplanner/StrengthPronCobra.asp

        if (1==1){
            Exercise ex = new Exercise();
            ex.setTitle("Pushups");
            ex.setDescription(" Lay prone (face down) on the floor with hands resting under the shoulders. Push up with the arms lifting the body off the floor. You may move the hands slightly in any direction to make it more comfortable. Lower and repeat.");
            ex.setImage("pushups.gif");
            ex.setImagecredit("");
            ex.setReps(10);
            out.add(ex);
        }

        if (1==1){
            Exercise ex = new Exercise();
            ex.setTitle("Situps");
            ex.setDescription("Lay supine ( face up) on the floor and bend the knees so that your heels are just in front of your butt. Bring your hands to your ears. Contract the abs to curl up lifting the head and upper torso of the ground. You may stop there or continue up until your torso is touching the front of your thighs. Lower and repeat.");
            ex.setImage("situps.gif");
            ex.setImagecredit("");
            ex.setReps(20);
            out.add(ex);
        }

        if (1==1){
            Exercise ex = new Exercise();
            ex.setTitle("Squats");
            ex.setDescription("Stand feet shoulder width apart. Extend the arms in front of the body to help with balance. To descend bend the hips and knees to lower the body. Keep your heels on the floor and make sure that your knees are tracking over your feet. Do not round the low back be sure to maintain a slight lumbar lordosis or natural curve in the lumbar spine. To ascend squeeze the glutes together and push the hips forward. Follow the same path you used on the descent.");
            ex.setImage("squats.gif");
            ex.setImagecredit("");
            ex.setReps(20);
            out.add(ex);
        }

        if (1==1){
            Exercise ex = new Exercise();
            ex.setTitle("Lunges");
            ex.setDescription("Stand with your feet about hip width apart and take a big step forward. Lower your body so that your back knee almost touches the floor. Pause and then come back up. In the bottom position the shin of the front leg should be perpendicular to the ground. If the knee of the front leg is coming out over the toes then step farther out. This is very important for the health of the knee. The front knee should also be tracking over the front foot, do not allow it to fall in or out. Keep the torso upright throughout the course of the movement. After completing the set switch legs. For added resistance place a barbell on your back or grab a pair of dumbbells.");
            ex.setImage("lunges.gif");
            ex.setImagecredit("");
            ex.setReps(20);
            out.add(ex);
        }

        if (1==1){
            Exercise ex = new Exercise();
            ex.setTitle("Supermans");
            ex.setDescription("Lat prone (face down) on the floor. To perform the exercise lift an arm out at a 45 degree angle to the body thumb pointed up. Next lift the opposite leg up off the floor. Hold for 10 seconds and repeat with the opposite arm and leg.");
            ex.setImage("supermans.gif");
            ex.setImagecredit("");
            ex.setReps(5);
            out.add(ex);
        }

        if (1==1){
            Exercise ex = new Exercise();
            ex.setTitle("Prone Cobras");
            ex.setDescription("Lay face down (prone) arms at sides. Raise the torso off the ground using your low back muscles. You may initiate the movement by contracting the glutes. Once up however use the low back to hold the torso up and relax the gluteals. Now that you are up externally rotate the arms and point the thumbs toward the sky. Now squeeze your shoulder blades together. Keep the chin tucked and hold the position. NOTE: The model in the picture is not tucking her chin, this is a good example of poor form.");
            ex.setImage("pronecobra.gif");
            ex.setImagecredit("");
            ex.setReps(5);
            out.add(ex);
        }


        return out;
    }

}
