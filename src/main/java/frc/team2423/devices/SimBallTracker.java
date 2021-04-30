package frc.team2423.devices;

import org.photonvision.PhotonTrackedTarget;
import org.photonvision.SimVisionTarget;
import org.photonvision.SimVisionSystem;
import org.photonvision.PhotonCamera;
import org.photonvision.PhotonUtils;

import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.util.Units;
import edu.wpi.first.wpilibj.geometry.Transform2d;

public class SimBallTracker extends Device implements IBallTracker {

    // Simulated Vision System.
    // Configure these to match your PhotonVision Camera,
    // pipeline, and LED setup.
    PhotonCamera camera = new PhotonCamera("photonvision");
    private final double camDiagFOV = 170.0; // degrees - assume wide-angle camera
    private final double maxLEDRange = 20; // meters
    private final int camResolutionWidth = 640; // pixels
    private final int camResolutionHeight = 480; // pixels
    private final double minTargetArea = 10; // square pixels
    private final double camPitchDegrees;
    private final double camHeightOffGroundFeet;

    private final SimVisionSystem simVision;

    public SimBallTracker(double camHeightOffGroundFeet, double camPitchDegrees) {
        this.camHeightOffGroundFeet = Units.feetToMeters(camHeightOffGroundFeet);
        this.camPitchDegrees = camPitchDegrees;
        simVision = new SimVisionSystem("photonvision", camDiagFOV, camPitchDegrees, new Transform2d(),
            Units.feetToMeters(camHeightOffGroundFeet), maxLEDRange, camResolutionWidth, camResolutionHeight,
            minTargetArea);
    }

    public boolean hasTargets() {
        return camera.getLatestResult().hasTargets();
    }

    public PhotonTrackedTarget getBestTarget() {
        return camera.getLatestResult().getBestTarget();
    }

    public double getDistanceFromTarget() {
        if (hasTargets()) {
            return PhotonUtils.calculateDistanceToTargetMeters(Units.feetToMeters(camHeightOffGroundFeet), 0.2,
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
        simVision.addSimVisionTarget(new SimVisionTarget(new Pose2d(x, y, new Rotation2d()), 0.2, 0.2, 0.2));
    }

    public void giveRobotPose(Pose2d pose) {
        simVision.processFrame(pose);
    }

    public double getPitchFromTarget() {
        if (hasTargets()) {
            return camera.getLatestResult().getBestTarget().getPitch();
        }
        return 0;
    }
}
