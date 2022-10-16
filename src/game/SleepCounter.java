package game;

public class SleepCounter {

    long nanoTime1;
    long nanoTime2;
    long millisecondsDifference;
    int nanosecondsDifference;

    public void update()
    {
        nanoTime2 = nanoTime1;
        nanoTime1 = System.nanoTime();
    }

    public void calculateSleepTime()
    {
        long nanoTime2 = System.nanoTime();
        long durationElapsed = nanoTime2 - nanoTime1;
        long durationDifference = Display.GAME_SPEED * 1000000 - durationElapsed;
        long millisecondsDifference = durationDifference / 1000000;
        int nanosecondsDifference = (int) (durationDifference % 1000000);

        //Game's speed
        //Thread.sleep(GAME_SPEED);
        if (millisecondsDifference < 0)
        {
            millisecondsDifference = 0;
        }
        if(nanosecondsDifference < 0)
        {
            nanosecondsDifference = 0;
        }

        this.millisecondsDifference = millisecondsDifference;
        this.nanosecondsDifference = nanosecondsDifference ;
    }

    public long getMillisecondsDifference() {
        return millisecondsDifference;
    }

    public int getNanosecondsDifference() {
        return nanosecondsDifference;
    }

    public void checkForLag()
    {
        long nanoTime2 = System.nanoTime();
        long durationElapsed = nanoTime2 - nanoTime1;
        long durationDifference = Display.GAME_SPEED * 1000000 - durationElapsed;

        System.out.println("Duration difference (If it varies significantly, there's lag): " + durationDifference);
    }
}
