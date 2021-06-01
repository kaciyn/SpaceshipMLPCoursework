//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package coursework;

import model.LunarParameters;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Observable;
import javax.imageio.ImageIO;

public class LunarLanderModified extends Observable implements Runnable {
    public double positionX = 300.0D;
    public double positionY = 300.0D;
    public double velocityX = 0.0D;
    public double velocityY = 0.0D;
    public boolean thrustUp = false;
    public boolean thrustLeft = false;
    public boolean thrustRight = false;
    public int fuel = 1000;
    public boolean running;
    public double fitness;
    private model.NeuralNetworkModified neuralNetwork;
    private double euclideanDistance = -1.0D;
    private double euclideanVelocity = -1.0D;
    private double fuelDelta = -1.0D;
    private static BufferedImage image = getImage("./img/Rocket.png");
    private static BufferedImage flameUp = getImage("./img/flameUp.png");
    private static BufferedImage flameLeft = getImage("./img/flameLeft.png");
    private static BufferedImage flameRight = getImage("./img/flameRight.png");
    private static BufferedImage death = getImage("./img/death");
    private static NumberFormat format = new DecimalFormat("00.000");
    
    public LunarLanderModified(model.NeuralNetworkModified neuralNetwork) {
        this.neuralNetwork = neuralNetwork;
        this.setRandom();
    }
    
    private static BufferedImage getImage(String path) {
        BufferedImage img = null;
        
        try {
            img = ImageIO.read(new File(path));
        } catch (IOException var3) {
        }
        
        return img;
    }
    
    void move() {
        synchronized(LunarParameters.lock) {
            double deltaVelocityY = 10.0D;
            this.velocityY += deltaVelocityY * 0.02D;
            if (this.thrustLeft && this.fuel > 0) {
                this.velocityX -= 20.0D * Math.sin(-1.5707963267948966D) * 0.02D;
                --this.fuel;
            }
            
            if (this.thrustRight && this.fuel > 0) {
                this.velocityX += 20.0D * Math.sin(-1.5707963267948966D) * 0.02D;
                --this.fuel;
            }
            
            if (this.thrustUp && this.fuel > 0) {
                this.velocityY -= 0.4D;
                --this.fuel;
            }
            
            if (this.velocityX > 100.0D) {
                this.velocityX = 100.0D;
            }
            
            if (this.velocityX < -100.0D) {
                this.velocityX = -100.0D;
            }
            
            if (this.velocityY > 100.0D) {
                this.velocityY = 100.0D;
            }
            
            if (this.velocityX < -100.0D) {
                this.velocityY = -100.0D;
            }
            
            this.positionX += this.velocityX * 0.02D;
            this.positionY += this.velocityY * 0.02D;
        }
    }
    
    public void draw(Graphics g) {
        RenderingHints renderHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        renderHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        Graphics2D g2d = (Graphics2D)g;
        double angle = 0.0D;
        if (this.thrustLeft && !this.thrustRight && this.fuel > 0) {
            angle = 0.1D;
        } else if (!this.thrustLeft && this.thrustRight && this.fuel > 0) {
            angle = -0.1D;
        }
        
        g2d.rotate(angle, this.positionX + 5.0D, this.positionY + 10.0D);
        g2d.setRenderingHints(renderHints);
        if (!this.running && !this.landedSafely()) {
            g2d.drawImage(death, (int)this.positionX - death.getWidth() / 2, (int)this.positionY - death.getHeight(), (ImageObserver)null);
        } else {
            g2d.drawImage(image, (int)this.positionX - image.getWidth() / 2, (int)this.positionY - image.getHeight(), (ImageObserver)null);
            if (this.thrustLeft && this.fuel > 0) {
                g2d.drawImage(flameLeft, (int)this.positionX - image.getWidth() / 2 - flameLeft.getWidth(), (int)this.positionY - image.getHeight() / 3, (ImageObserver)null);
            }
            
            if (this.thrustRight && this.fuel > 0) {
                g2d.drawImage(flameRight, (int)this.positionX + image.getWidth() / 2, (int)this.positionY - image.getHeight() / 3, (ImageObserver)null);
            }
            
            if (this.thrustUp && this.fuel > 0) {
                g2d.drawImage(flameUp, (int)this.positionX - flameUp.getWidth() / 2, (int)this.positionY, (ImageObserver)null);
            }
        }
        
        g2d.rotate(-angle, this.positionX + 5.0D, this.positionY + 10.0D);
    }
    
    public boolean landedSafely() {
        return !this.running && this.velocityX * this.velocityX <= 100.0D && this.velocityY <= 20.0D && this.positionX >= 250.0D && this.positionX <= 350.0D && Math.abs(this.positionY - 595.0D) > 1.0D;
    }
    
    public void run() {
        this.running = true;
        
        while(!this.stopCondition() && this.running) {
            this.decode();
            this.move();
            this.outputStatsTellGuiEtc();
        }
        
        this.running = false;
        this.fitness = this.calculateFitness();
        this.thrustLeft = false;
        this.thrustUp = false;
        this.thrustRight = false;
        if (this.countObservers() > 0) {
            synchronized(LunarParameters.lock) {
                double distance = Math.sqrt(Math.pow(300.0D - this.positionX, 2.0D) + Math.pow(595.0D - this.positionY, 2.0D));
                String str = "Fitness=" + format.format(this.fitness) + "\tFuel=" + this.fuel + "\tvelocityX=" + format.format(this.velocityX) + "\tvelocityY=" + format.format(this.velocityY);
                str = str + "\tDistance=" + format.format(distance);
                if (this.landedSafely()) {
                    System.out.println("Landed " + str);
                } else {
                    System.out.println("Crash  " + str);
                }
            }
        }
        
        this.outputStatsTellGuiEtc();
    }
    
    private void outputStatsTellGuiEtc() {
        if (this.countObservers() > 0) {
            this.setChanged();
            this.notifyObservers(this);
            
            try {
                Thread.sleep((long)LunarParameters.delay);
            } catch (InterruptedException var2) {
                var2.printStackTrace();
            }
        }
        
    }
    
    private boolean stopCondition() {
        return this.positionY > 600.0D || this.positionY < 0.0D || this.positionX > 600.0D || this.positionX < 0.0D;
    }
    
    private double calculateFitness() {
        double deltaX = this.positionX - 300.0D;
        double deltaY = this.positionY - 595.0D;
        this.euclideanDistance = Math.sqrt(Math.pow(deltaX, 2.0D) + Math.pow(deltaY, 2.0D)) / Math.sqrt(Math.pow(600.0D, 2.0D) + Math.pow(600.0D, 2.0D));
        this.euclideanVelocity = Math.sqrt(Math.pow(this.velocityX, 2.0D) + Math.pow(this.velocityY, 2.0D)) / Math.sqrt(20000.0D);
        this.fuelDelta = (double)this.fuel / 1000.0D;
        return (this.euclideanDistance + this.euclideanVelocity + this.fuelDelta) / 3.0D;
    }
    
    private void decode() {
        if (this.neuralNetwork != null) {
            double v1 = this.velocityX / 100.0D;
            double v2 = this.velocityY / 100.0D;
            double v3 = (this.positionX - 300.0D) / 300.0D;
            double v4 = (this.positionY - 595.0D) / 595.0D;
            double v5 = (double)this.fuel / 1000.0D;
            this.neuralNetwork.setInputs(new double[]{v1, v2, v3, v4, v5});
            double[] output = this.neuralNetwork.computeOutputs();
            this.thrustRight = false;
            this.thrustLeft = false;
            this.thrustUp = false;
            if (output[0] >= 0.0D) {
                this.thrustRight = true;
            }
            
            if (output[1] >= 0.0D) {
                this.thrustLeft = true;
            }
            
            if (output[2] >= 0.0D) {
                this.thrustUp = true;
            }
            
        }
    }
    
    public void stopRunning() {
        this.running = false;
    }
    
    public String toString() {
        return "Fitness = " + this.fitness + " Distance " + this.euclideanDistance + " Velocity " + this.euclideanVelocity + " Fuel " + this.fuelDelta;
    }
    
    public void setTrainingStartParameters(int idx) {
        switch(idx) {
            case 0:
                this.velocityX = 20.0D;
                this.velocityY = 0.0D;
                this.positionY = 300.0D;
                this.positionX = 400.0D;
                break;
            case 1:
                this.velocityX = -20.0D;
                this.velocityY = 0.0D;
                this.positionX = 400.0D;
                this.positionY = 300.0D;
                break;
            case 2:
                this.velocityX = 20.0D;
                this.velocityY = 0.0D;
                this.positionX = 200.0D;
                this.positionY = 300.0D;
                break;
            case 3:
                this.velocityX = -20.0D;
                this.velocityY = 0.0D;
                this.positionX = 200.0D;
                this.positionY = 300.0D;
                break;
            case 4:
                this.velocityX = 0.0D;
                this.velocityY = 0.0D;
                this.positionX = 300.0D;
                this.positionY = 300.0D;
                break;
            case 5:
                this.velocityX = 0.0D;
                this.velocityY = -20.0D;
                this.positionX = 300.0D;
                this.positionY = 400.0D;
                break;
            case 6:
                this.velocityX = 0.0D;
                this.velocityY = 20.0D;
                this.positionX = 300.0D;
                this.positionY = 200.0D;
                break;
            case 7:
                this.velocityX = 20.0D;
                this.velocityY = -20.0D;
                this.positionX = 200.0D;
                this.positionY = 400.0D;
        }
        
    }
    
    public void setTestStartParameters(int idx) {
        switch(idx) {
            case 0:
                this.velocityX = -30.0D;
                this.velocityY = -50.0D;
                this.positionY = 400.0D;
                this.positionX = 200.0D;
                break;
            case 1:
                this.velocityX = -30.0D;
                this.velocityY = 50.0D;
                this.positionX = 400.0D;
                this.positionY = 400.0D;
                break;
            case 2:
                this.velocityX = 30.0D;
                this.velocityY = 0.0D;
                this.positionX = 200.0D;
                this.positionY = 300.0D;
                break;
            case 3:
                this.velocityX = 30.0D;
                this.velocityY = 0.0D;
                this.positionX = 200.0D;
                this.positionY = 350.0D;
                break;
            case 4:
                this.velocityX = 40.0D;
                this.velocityY = -40.0D;
                this.positionX = 300.0D;
                this.positionY = 400.0D;
                break;
            case 5:
                this.velocityX = 30.0D;
                this.velocityY = -20.0D;
                this.positionX = 300.0D;
                this.positionY = 500.0D;
                break;
            case 6:
                this.velocityX = 0.0D;
                this.velocityY = 20.0D;
                this.positionX = 500.0D;
                this.positionY = 200.0D;
                break;
            case 7:
                this.velocityX = -40.0D;
                this.velocityY = -20.0D;
                this.positionX = 300.0D;
                this.positionY = 400.0D;
        }
        
    }
    
    void setRandom() {
        this.positionX = (double)(150 + LunarParameters.random.nextInt(300));
        this.positionY = (double)(150 + LunarParameters.random.nextInt(300));
        this.velocityX = (double)(-50 + LunarParameters.random.nextInt(100));
        this.velocityY = (double)(-50 + LunarParameters.random.nextInt(100));
    }
}
