package dLib.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import dLib.util.events.localevents.RunnableEvent;

public class Timer {
    /** Variables */
    private float minDuration;
    private float maxDuration;
    private boolean looping;

    private float currentDuration;
    private boolean finished = false;

    public RunnableEvent onTriggerEvent = new RunnableEvent();

    /** Constructors */
    public Timer(float duration){
        this(duration, duration);
    }
    public Timer(float duration, boolean looping){
        this(duration, duration, looping);
    }

    public Timer(float minDuration, float maxDuration){
        this(minDuration, maxDuration, true);
    }
    public Timer(float minDuration, float maxDuration, boolean looping){
        this.minDuration = minDuration;
        this.maxDuration = maxDuration;
        this.looping = looping;

        reset();
    }

    /** Update and Render */
    public void update(){
        if(finished) return;

        this.currentDuration -= Gdx.graphics.getDeltaTime();
        if(currentDuration <= 0){
            if(looping) {
                reset();
            }
            else{
                finished = true;
            }
            onTrigger();
        }
    }

    public void onTrigger(){
        onTriggerEvent.invoke();
    }
    private void reset(){
        currentDuration = MathUtils.random(minDuration, maxDuration);
    }

    public boolean isFinished() {
        return finished;
    }
}
