
package frc.team2423.devices;

import edu.wpi.first.wpilibj.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj.PWMVictorSPX;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.Encoder;
import java.util.ArrayList;
import edu.wpi.first.wpilibj.simulation.EncoderSim;

public class SimMotor extends Device implements IMotor {

    private PWMVictorSPX  motor;
    protected ArrayList<SimMotor> followers = new ArrayList<SimMotor>();
    private String controlType = "voltage";
    private double desiredDistance = 0;
    private double desiredSpeed = 0;
    private double desiredPercent = 0;

    public SimMotor(int port, int channelA, int channelB, String name) {
        motor = new PWMVictorSPX(port);
        setPercent(0);
    }

    public void setSpeed(double speed) {
        desiredSpeed = speed;
        controlType = "speed";
    }

    public double getSpeed(){
        return 0;
    }

    public void setPercent(double percent) {
        desiredPercent = percent;
        controlType = "percent";
    }

    public double getPercent(){
        return motor.get();
    }


    public double getEncoderCount() {
        return getDistance() / getConversionFactor();
    }

    public void setDistance(double dist) {
        desiredDistance = dist;
        controlType = "distance";
    }

    public void resetEncoder(double distance) {}

    public double getDistance() {
        return 0;
    }

    public void setConversionFactor(double factor){}

    public double getConversionFactor(){return 0;}

    public void setInverted(boolean isInverted) {
        motor.setInverted(isInverted);
    }

    public boolean getInverted() {
        return motor.getInverted();
    }

    public void setPid(double kP, double kI, double kD){}

    public void setPidf(double kP, double kI, double kD, double kF){}

    public void setP(double kP){}

    public void setI(double kI){}

    public void setD(double kD){}

    public void setF(double kF) {}

    public double getP(){
        return 0;
    }

    public double getI(){
        return 0;
    }

    public double getD(){
        return 0;
    }

    public double getF() {
        return 0.0;
    }

    public void follow(IMotor leader){
        if (leader.getClass() == SimMotor.class) {
            SimMotor leadDriveMotor = (SimMotor)leader;
            leadDriveMotor.followers.add(this);
        }
    }

    public void setEncoderPositionAndRate(double position, double rate){}

    public void execute() {
        if (controlType == "distance") {

        } else if (controlType == "speed") {
            motor.setVoltage(desiredSpeed);
                            
            for (SimMotor follower : followers) {
                follower.setSpeed(desiredSpeed);
            }
        } else if (controlType == "percent") {
            motor.setVoltage(desiredPercent);

            for (SimMotor follower : followers) {
                follower.setPercent(desiredPercent);
            }
        }
    }
}
