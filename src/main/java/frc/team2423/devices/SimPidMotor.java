
package frc.team2423.devices;

import edu.wpi.first.wpilibj.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj.PWMVictorSPX;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.Encoder;
import java.util.ArrayList;
import edu.wpi.first.wpilibj.simulation.EncoderSim;

public class SimPidMotor extends Device implements IMotor {

    private PWMVictorSPX  motor;
    private PIDController pidController;  
    private Encoder encoder; 
    protected ArrayList<SimPidMotor> followers = new ArrayList<SimPidMotor>();
    private final SimpleMotorFeedforward feedForward = new SimpleMotorFeedforward(1, 3);
    private double encoderOffset = 0;
    private EncoderSim encoderSim;
    private String controlType = "voltage";
    private double desiredDistance = 0;
    private double desiredSpeed = 0;
    private double desiredPercent = 0;

    public SimPidMotor(int port, int channelA, int channelB) {
        motor = new PWMVictorSPX(port);
        encoder = new Encoder(channelA, channelB);
        encoderSim = new EncoderSim(encoder);
        pidController = new PIDController(0, 0, 0);
        setPercent(0);
    }

    public void setSpeed(double speed) {
        desiredSpeed = speed;
        controlType = "speed";
        double output = pidController.calculate(getSpeed(), desiredSpeed);
        motor.setVoltage(output + feedForward.calculate(desiredSpeed));
                        
        for (SimPidMotor follower : followers) {
            follower.setSpeed(desiredSpeed);
        }
    }

    public double getSpeed(){
        double rate = encoder.getRate();
        return motor.getInverted() ? -rate : rate;
    }

    public void setPercent(double percent) {
        desiredPercent = percent;
        controlType = "percent";
        motor.setVoltage(desiredPercent);

        for (SimPidMotor follower : followers) {
            follower.setPercent(desiredPercent);
        }
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
        double output = pidController.calculate(getDistance(), desiredDistance);
        pidController.setSetpoint(output);
    }

    public void resetEncoder(double distance) {
        encoder.reset();
        encoderOffset = distance;
    }

    public double getDistance() {
        double distance =  encoder.getDistance() + encoderOffset;
        return motor.getInverted() ? -distance : distance;
    }

    public void setConversionFactor(double factor){
        encoder.setDistancePerPulse(factor);
    }

    public double getConversionFactor(){
        return encoder.getDistancePerPulse();
    }

    public void setInverted(boolean isInverted) {
        motor.setInverted(isInverted);
    }

    public boolean getInverted() {
        return motor.getInverted();
    }

    public void setPid(double kP, double kI, double kD){
        setP(kP);
        setI(kI);
        setD(kD);       
    }

    public void setPidf(double kP, double kI, double kD, double kF){
        setP(kP);
        setI(kI);
        setD(kD);
        setF(kF); 
    }

    public void setP(double kP){
        pidController.setP(kP);
    }

    public void setI(double kI){
        pidController.setI(kI);
    }

    public void setD(double kD){
        pidController.setD(kD);
    }

    public void setF(double kF) {
        
    }

    public double getP(){
        return pidController.getP();
    }

    public double getI(){
        return pidController.getI();
    }

    public double getD(){
        return pidController.getD();
    }

    public double getF() {
        return 0.0;
    }

    public void follow(IMotor leader){
        if (leader.getClass() == SimPidMotor.class) {
            SimPidMotor leadDriveMotor = (SimPidMotor)leader;
            leadDriveMotor.followers.add(this);
        }
    }

    public void setEncoderPositionAndRate(double position, double rate){
        encoderSim.setDistance(position);
        encoderSim.setRate(rate);
    }

}
