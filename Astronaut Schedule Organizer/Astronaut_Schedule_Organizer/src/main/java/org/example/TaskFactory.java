package org.example;

import java.time.LocalTime;

public class TaskFactory {

    public static Task createTask(String description, String startTime, String endTime, String priority) throws Exception {
        LocalTime localTime1 = LocalTime.parse(startTime);
        LocalTime localTime2 = LocalTime.parse(endTime);

        if(localTime1.isAfter(localTime2))
        {
            throw new Exception("StartTime is greater than EndTime");
        }


        return new Task(description, LocalTime.parse(startTime), LocalTime.parse(endTime), priority);
    }
}

