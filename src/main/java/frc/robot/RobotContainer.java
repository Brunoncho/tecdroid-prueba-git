package frc.robot;

import frc.robot.Constants.OperatorConstants;
import frc.robot.commands.Autos;
import frc.robot.commands.ExampleCommand;
import frc.robot.subsystems.DriveTrain;
import frc.robot.subsystems.ExampleSubsystem; // Asegúrate de importar la clase Motor desde el paquete correcto

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {

  // Declaración del motor como parte del robot
  private XboxController controller = new XboxController(0);
  private DriveTrain driveTrain = new DriveTrain();

  // The robot's subsystems and commands are defined here...
  private final ExampleSubsystem m_exampleSubsystem = new ExampleSubsystem();
  

  // Replace with CommandPS4Controller or CommandJoystick if needed
  private final CommandXboxController m_driverController =
      new CommandXboxController(OperatorConstants.kDriverControllerPort);

  
  //motor
  VictorSPX shooterMotor = new VictorSPX(6);

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    // Configure the trigger bindings
    configureBindings();
  }

  /**
   * Use this method to define your trigger->command mappings. Triggers can be created via the
   * {@link Trigger#Trigger(java.util.function.BooleanSupplier)} constructor with an arbitrary
   * predicate, or via the named factories in {@link
   * edu.wpi.first.wpilibj2.command.button.CommandGenericHID}'s subclasses for {@link
   * CommandXboxController Xbox}/{@link edu.wpi.first.wpilibj2.command.button.CommandPS4Controller
   * PS4} controllers or {@link edu.wpi.first.wpilibj2.command.button.CommandJoystick Flight
   * joysticks}.
   */
  private void configureBindings() {
    // Schedule ExampleCommand when exampleCondition changes to true
    new Trigger(m_exampleSubsystem::exampleCondition)
        .onTrue(new ExampleCommand(m_exampleSubsystem));


    new Trigger(() -> controller.getBButton())
    .whileTrue(new RunCommand(() -> shooterMotor.set(ControlMode.PercentOutput, 2.0)))
    .onFalse(new InstantCommand(() -> shooterMotor.set(ControlMode.PercentOutput, 0.0)));

    new JoystickButton(controller, XboxController.Button.kX.value)
    .whileTrue(new RunCommand(() -> shooterMotor.set(ControlMode.PercentOutput, 2.0)))
    .onFalse(new InstantCommand(() -> shooterMotor.set(ControlMode.PercentOutput, 0.0)));

    // Schedule exampleMethodCommand when the Xbox controller's B button is pressed,
    // cancelling on release.
    m_driverController.b().whileTrue(m_exampleSubsystem.exampleMethodCommand());
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // An example command will be run in autonomous
    return Autos.exampleAuto(m_exampleSubsystem);
  }

  public void teleopPeriodic() {
  
    if (controller.getAButton()){
      driveTrain.rotate90Degrees();
    } else{
      driveTrain.moveRobot(controller.getRightX(), -controller.getLeftY() * .8);
    }
  }

}