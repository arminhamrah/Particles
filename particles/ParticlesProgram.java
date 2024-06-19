import java.awt.*;
import java.util.*;
import acm.program.*;

public class ParticlesProgram extends Program
{
    //constants for variable types
    public static final int EMPTY = 0;
    public static final int METAL = 1;
    public static final int WATER = 2;
    public static final int SAND = 3;
    public static final int ICE = 4;
    public static final int HELIUM = 5;
    public static final int BLACKHOLE = 6;
    public static final int FIREWORK = 7;
    public static final int TARGET = 8;
    public static final int MYSTERYPARTICLE = 9;
    public static final int SPARK = 50;
    public static final int TARGETHELPER = 51;
    public static final int TARGETHELPERTWO = 52;
    //do not add any more private instance variables
    private Particle[][] grid;
    private ParticlesDisplay display;

    public void init()
    {
        initVariables(120, 80);
    }

    public void initVariables(int numRows, int numCols)
    {
        String[] names;
        names = new String[10];
        names[EMPTY] = "Empty";
        names[METAL] = "Metal";
        names[WATER] = "Water";
        names[SAND] = "Sand";
        names[ICE] = "Ice";
        names[HELIUM] = "Helium";
        names[BLACKHOLE] = "BlackHole";
        names[FIREWORK] = "Firework";
        names[TARGET] = "Target";
        names[MYSTERYPARTICLE] = "MysteryParticle";
        //no need for SPARK, TARGETHELPER, TARGETHELPERTWO, since paired with other functions (firework & target)
        display = new ParticlesDisplay("Particles Game",
            numRows, numCols, names);
        // initialize the grid here (task 0.1)
        grid = new Particle[numRows][numCols];
        for (int r=0; r<grid.length; r++)
        {
            for (int c=0; c<grid[0].length; c++)
            {
                grid[r][c] = new Empty();
            }
        }
    }

    //Called when the user clicks on a location using the given particleType
    private void locationClicked(int row, int col, int particleType)
    {
        // finish this cascading if (task 0.2)
        if (particleType == EMPTY)
            grid[row][col] = new Empty();
        else if (particleType == METAL)
            grid[row][col] = new Metal();
        else if (particleType == WATER)
            grid[row][col] = new Water();
        else if (particleType == SAND)
            grid[row][col] = new Sand();
        else if (particleType == ICE)
            grid[row][col] = new Ice();
        else if (particleType == HELIUM)
            grid[row][col] = new Helium();
        else if (particleType == BLACKHOLE)
            grid[row][col] = new BlackHole();
        else if (particleType == FIREWORK)
            grid[row][col] = new Firework();
        else if (particleType == TARGET)
            grid[row][col] = new Target();
        else if (particleType == MYSTERYPARTICLE)
        {
            int randomParticle = (int)(Math.random()*8)+1;
            if (randomParticle == 1)
            {
                grid[row][col] = new Metal();
            }
            if (randomParticle == 2)
            {
                grid[row][col] = new Water();
            }
            if (randomParticle == 3)
            {
                grid[row][col] = new Sand();
            }
            if (randomParticle == 4)
            {
                grid[row][col] = new Ice();
            }
            if (randomParticle == 5)
            {
                grid[row][col] = new Helium();
            }
            if (randomParticle == 6)
            {
                grid[row][col] = new BlackHole();
            }
            if (randomParticle == 7)
            {
                grid[row][col] = new Firework();
            }
            if (randomParticle == 8)
            {
                grid[row][col] = new Target();
            }
        }
    }

    //called repeatedly
    public void step()
    {
        int row = (int)(Math.random()*grid.length);
        int col = (int)(Math.random()*grid[0].length);
        Particle particle = grid[row][col];
        if (particle.getType() == EMPTY)
        { }
        else if (particle.getType() == METAL)
        { }
        else if (particle.getType() == WATER)
        {
            waterBehavior(row, col);
        }
        else if (particle.getType() == SAND)
        {
            tryToMoveDown(row, col, true);
        }
        else if (particle.getType() == ICE)
        {
            Ice ice = (Ice)particle; //typecasting ice particle to Ice
            ice.incrementCounter();
            if (ice.isMelted())
            {
                grid[row][col] = new Water();
            }
        }
        else if (particle.getType() == HELIUM)
        {
            if (row == 0)
            {
                grid[row][col] = new Empty();
            }
            tryToMoveUp(row, col);
        }
        else if (particle.getType() == BLACKHOLE)
        {
            suckUpSurroundings(row, col);
        }
        else if (particle.getType() == FIREWORK)
        {
            fireworkBehavior(row, col);
        }
        else if (particle.getType() == SPARK)
        {
            sparkBehavior(row, col);
        }
        else if (particle.getType() == TARGET)
        {
            makeTarget(row, col);
        }
        else if (particle.getType() == TARGETHELPER)
        { }
        else if (particle.getType() == TARGETHELPERTWO)
        { }
    }

    public void fireworkBehavior(int row, int col)
    {
        Firework firework = (Firework)grid[row][col];
        firework.raiseCounter();
        if (firework.returnCounter()>50)
        {
            //top row
            grid[row+1][col-1] = new Spark(1, -1);
            grid[row+1][col] = new Spark(1, 0);
            grid[row+1][col+1] = new Spark(1, 1);
            //middle row edges
            grid[row][col-1] = new Spark(0, -1);
            grid[row][col+1] = new Spark(0, 1);
            //bottom row
            grid[row-1][col-1] = new Spark(-1, -1);
            grid[row-1][col] = new Spark(-1, 0);
            grid[row-1][col+1] = new Spark(-1, 1);
            //no more sparks please
            grid[row][col] = new Empty();
            return;
        }
        tryToMoveUp(row, col);
    }

    public void sparkBehavior(int row, int col)
    {
        Spark spark = (Spark)grid[row][col];
        int dr = spark.getDR();
        int dc = spark.getDC();
        if (row+dr>=0 && row+dr<=grid.length-1 && col+dc>=0 && col+dc<=grid[0].length-1) //row+dy in bounds & col+dx in bounds
        {
            grid[row+dr][col+dc] = new Spark(dr, dc);
            grid[row][col] = new Empty();
        }
        else
        {
            grid[row][col] = new Empty();
        }
    }

    public void makeTarget(int row, int col)
    {
        grid[row][col] = new Target();
        drawTargetHelpers(row, col);
        drawTargetHelperTwos(row, col);
    }

    public void drawTargetHelpers(int row, int col)
    {
        if (row>0 && col>0 && row<grid.length-1 && col<grid[0].length-1)
        {
            //top row
            grid[row-1][col-1] = new TargetHelper();
            grid[row-1][col] = new TargetHelper();
            grid[row-1][col+1] = new TargetHelper();
            //middle row minus center
            grid[row][col-1] = new TargetHelper();
            grid[row][col+1] = new TargetHelper();
            //bottom row
            grid[row+1][col-1] = new TargetHelper();
            grid[row+1][col] = new TargetHelper();
            grid[row+1][col+1] = new TargetHelper();
        }
    }

    public void drawTargetHelperTwos(int row, int col)
    {
        if (row>1 && col>1 && row<grid.length-2 && col<grid[0].length-2)
        {
            //full top row
            grid[row-2][col-2] = new TargetHelperTwo();
            grid[row-2][col-1] = new TargetHelperTwo();
            grid[row-2][col] = new TargetHelperTwo();
            grid[row-2][col+1] = new TargetHelperTwo();
            grid[row-2][col+2] = new TargetHelperTwo();
            //top-middle row edges
            grid[row-1][col-2] = new TargetHelperTwo();
            grid[row-1][col+2] = new TargetHelperTwo();
            //middle row edges
            grid[row][col-2] = new TargetHelperTwo();
            grid[row][col+2] = new TargetHelperTwo();
            //bottom-middle row edges
            grid[row+1][col-2] = new TargetHelperTwo();
            grid[row+1][col+2] = new TargetHelperTwo();
            //full bottom row
            grid[row+2][col-2] = new TargetHelperTwo();
            grid[row+2][col-1] = new TargetHelperTwo();
            grid[row+2][col] = new TargetHelperTwo();
            grid[row+2][col+1] = new TargetHelperTwo();
            grid[row+2][col+2] = new TargetHelperTwo();
        }
    }

    public void suckUpSurroundings(int row, int col)
    {
        Particle particle = grid[row][col];
        //above
        if(row !=0) 
        {
            grid[row-1][col] = new Empty();
        }
        //below
        if(row != grid.length-1)
        {
            grid[row+1][col] = new Empty();
        }
        //left
        if(col != 0)
        {
            grid[row][col-1] = new Empty();
        }
        //right
        if(col != grid[0].length-1)
        {
            grid[row][col+1] = new Empty();
        }
        //top left corner
        if(row!=0 && col!=0)
        {
            grid[row-1][col-1] = new Empty();
        }
        //top right corner
        if(row!=0 && col != grid[0].length-1)
        {
            grid[row-1][col+1] = new Empty();
        }
        //bottom left corner
        if(row!=grid.length-1 && col !=0)
        {
            grid[row+1][col-1] = new Empty();
        }
        //bottom right corner
        if(row!=grid.length-1 && col != grid[0].length-1)
        {
            grid[row+1][col+1] = new Empty();
        }
    }

    public void waterBehavior(int row, int col)
    {
        int num = (int)(Math.random()*3);
        if (num == 0) //down
        {
            tryToMoveDown(row, col, false);
        }
        if (num == 1) //left
        {
            if (col!=0 && grid[row][col-1].getType()==EMPTY)
            {
                grid[row][col-1] = new Water();
                grid[row][col] = new Empty();
            }
        }
        if (num == 2) //right
        {
            if (col!=grid[0].length-1 && grid[row][col+1].getType()==EMPTY)
            {
                grid[row][col+1] = new Water();
                grid[row][col] = new Empty();
            }
        }
    }

    public void tryToMoveDown(int row, int col, boolean canFallThroughWater)
    {
        Particle particle = grid[row][col];
        if (row!=grid.length-1 && (grid[row+1][col].getType()==EMPTY
            || canFallThroughWater && grid[row+1][col].getType()==WATER))
        {
            Particle below = grid[row+1][col];
            grid[row+1][col] = particle;
            grid[row][col] = below;
        }
    }

    // getType returns integer of which particle
    public void tryToMoveUp(int row, int col)
    {
        Particle particle = grid[row][col];
        if (row!=0 && grid[row-1][col].getType()==EMPTY)
        {
            Particle above = grid[row-1][col];
            grid[row-1][col] = particle;
            grid[row][col] = above;
        }
    }

    //copies each element of grid into the display (don't modify this)
    public void updateDisplay()
    {
        for (int r=0; r<grid.length; r++)
            for (int c=0; c<grid[0].length; c++)
                display.setColor(r, c, grid[r][c].getColor());
    }

    // repeatedly calls step and updates the display
    // (don't modify this)
    public void run()
    {
        while (true)
        {
            for (int i = 0; i < display.getSpeed(); i++)
                step();
            updateDisplay();
            display.repaint();
            display.pause(1);  //wait for redrawing and for mouse
            int[] mouseLoc = display.getMouseLocation();
            if (mouseLoc != null)  //test if mouse clicked
                locationClicked(mouseLoc[0], mouseLoc[1], display.getTool());
        }
    }
}