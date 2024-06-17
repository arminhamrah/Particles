import java.awt.Color;
public class Spark extends Particle
{
    private int dr;
    private int dc;
    private int sparkCounter;
    public Spark(int startingDR, int startingDC)
    {
        super(ParticlesProgram.SPARK, 
        new Color((int)(Math.random()*255), 
        (int)(Math.random()*255), 
        (int)(Math.random()*255)));
        sparkCounter = 0;
        dr = startingDR;
        dc = startingDC;
    }
    
    public int getDR()
    {
        return dr;
    }
    
    public int getDC()
    {
        return dc;
    }
}