package frc.robot.subsystems;

import frc.robot.Constants;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;


public class DriveTrain {
    Motor leftMotors;
    Motor rightMotors;

    DifferentialDrive driveTrain;
    ADXRS450_Gyro gyro = new ADXRS450_Gyro();

    ShuffleboardTab shuffleboard;
    PIDController pid = new PIDController(0.006,0.001,0.0);

public DriveTrain(){
    rightMotors = new Motor(Constants.DriveTrainConstants.frontRigtId, Constants.DriveTrainConstants.backRigtId, false);
    leftMotors = new Motor(Constants.DriveTrainConstants.frontLeftId, Constants.DriveTrainConstants.backLeftId, true);


    driveTrain = new DifferentialDrive(rightMotors::set, leftMotors::set);
    
    shuffleboard = Shuffleboard.getTab("Gyro");
    shuffleboard.addDouble("Robot yaw", (()-> gyro.getAngle()));

    pid.enableContinuousInput(-1, 1.0);
} 
    

  public void moveRobot(double xSpeed, double rotation){
        driveTrain.arcadeDrive(xSpeed, rotation);
    }

    public double getGyroRead(){
        double angle = gyro.getAngle();
        while (angle > 360){
            angle -= 360;
        }
        while (0 > angle){
            angle += 360;

        }

        return angle;
    }

    public void rotate90Degrees(){
        double setPoint = 90.0;
        if (setPoint > getGyroRead()){
            moveRobot(0.0, -0.5);
        } else if (getGyroRead() > setPoint ){
            moveRobot(0.0, 0.5);
        }else{
            moveRobot(0.0, 0.0);
        }
        
    }

    public void rotate90DegreesWithPid(){   
        double setPoint = 90.0;
        double percentage = -pid.calculate(getGyroRead(), setPoint);

     
        if (percentage > 0){
            percentage = Math.max(1.0, percentage);
        } else if (-1.0 > percentage){
            percentage = Math.max(percentage, percentage);
        }

        System.out.println(percentage);

        moveRobot(0.0, percentage);
    }    
}