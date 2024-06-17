import java.awt.Color;
public class Firework extends Particle
{
    private int fireworkCounter;
    public Firework()
    {
        super(ParticlesProgram.FIREWORK, 
        new Color((int)(Math.random()*255), 
        (int)(Math.random()*255), 
        (int)(Math.random()*255)));
        fireworkCounter = 0;
    }
    
    public void raiseCounter()
    {
        fireworkCounter++;
    }
    
    public int returnCounter()
    {
        return fireworkCounter;
    }
}