package data.tasks;

import java.util.concurrent.TimeUnit;

import play.libs.Akka;

import scala.concurrent.duration.Duration;

public class TaskUtils {
    public static void runTask(Runnable task) {
        Akka.system().scheduler().scheduleOnce(
            Duration.create(0, TimeUnit.MILLISECONDS),
            task, Akka.system().dispatcher());
    }
}
