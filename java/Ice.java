import java.awt.Color;

public class Ice extends Particle
{
    private int counter;
    
    public Ice()
    {
        super(ParticlesProgram.ICE, Color.white);
        counter = 0;
    }
    
    public void incrementCounter()
    {
        counter++;
    }
    
    public boolean isMelted()
    {
        return counter >= 500;
    }
}