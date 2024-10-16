package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

public class Motor {
    int motor1id;
    VictorSPX motor1;
    int motor2id;
    VictorSPX motor2;

    public Motor(int motor1id, int motor2id, boolean invert) {
        this.motor1id = motor1id;
        this.motor1 = new VictorSPX(motor1id);
        this.motor2id = motor2id;
        this.motor2 = new VictorSPX(motor2id);

        this.motor1.setInverted(invert);
        this.motor2.setInverted(invert);

        this.motor2.follow(this.motor1);
    }

    // Método para mover el motor
    public void set(double percentage) {
        this.motor1.set(ControlMode.PercentOutput, percentage);
    }


}