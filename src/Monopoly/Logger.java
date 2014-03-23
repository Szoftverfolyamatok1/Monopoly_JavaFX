package Monopoly;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.SimpleFormatter;

/**
 * Created by Benjamin on 2014.03.18..
 */
public class Logger {
    public static class LoggerClass{
        private final static java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger(LoggerClass.class.getName());
        private static FileHandler fh;

        static {
            try{
                fh = new FileHandler("Log/ControlLog.log", false);
                java.util.logging.Logger l = java.util.logging.Logger.getLogger("");
                fh.setFormatter(new SimpleFormatter());
                l.addHandler(fh);
                l.setLevel(Level.CONFIG);
            }
            catch(Exception e){
                e.getStackTrace();
            }
        }

        public static void doLog(Level level, String message)
        {
            LOGGER.log(level, message);
        }
    }
}
