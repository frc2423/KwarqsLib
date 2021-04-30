package frc.team2423.devices;

import org.photonvision.PhotonCamera;
import org.photonvision.PhotonTrackedTarget;
import org.photonvision.PhotonUtils;

import edu.wpi.first.wpilibj.util.Units;
import edu.wpi.first.wpilibj.geometry.Pose2d;

public class BallTracker extends Device implements IBallTracker {
    private final double camHeightOffGroundFeet;
    private final double camPitchDegrees;
    PhotonCamera camera = new PhotonCamera("kwarqsPhotonVision1");

    public BallTracker(double camHeightOffGroundFeet, double camPitchDegrees) {
        this.camHeightOffGroundFeet = Units.feetToMeters(camHeightOffGroundFeet);
        this.camPitchDegrees = camPitchDegrees;
    }

    public boolean hasTargets() {
        return camera.getLatestResult().hasTargets();
    }

    public PhotonTrackedTarget getBestTarget() {
        return camera.getLatestResult().getBestTarget();
    }

    public double getDistanceFromTarget() { // uhhhhhh
        if (hasTargets()) {
            return PhotonUtils.calculateDistanceToTargetMeters(camHeightOffGroundFeet, 0.2,
                    Units.degreesToRadians(camPitchDegrees),
                    Units.degreesToRadians(camera.getLatestResult().getBestTarget().getPitch()));
        }
        return 0;
    }

    public double getAngleFromTarget() {
        if (hasTargets()) {
            return camera.getLatestResult().getBestTarget().getYaw();
        }
        return 0;
    }

    public void addSimulatedBall(double x, double y) {
    }

    public void giveRobotPose(Pose2d pose) {
    }

    public double getPitchFromTarget() {
        if (hasTargets()) {
            return camera.getLatestResult().getBestTarget().getPitch();
        }
        return 0;
    }

}