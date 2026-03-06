// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.CANFuelSubsystem;
import static frc.robot.Constants.FuelConstants.*;
import com.revrobotics.RelativeEncoder;
import edu.wpi.first.math.controller.PIDController;




//Bang-Bang controller
 import edu.wpi.first.math.controller.BangBangController;
 import edu.wpi.first.math.controller.SimpleMotorFeedforward;


/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class Launch extends Command {
  /** Creates a new Intake. */

  CANFuelSubsystem fuelSubsystem;
  // RelativeEncoder encoder = CANFuelSubsystem.getLauncherEncoder();
  double tempVoltage = 0;

  public Launch(CANFuelSubsystem fuelSystem) {
    addRequirements(fuelSystem);
    this.fuelSubsystem = fuelSystem;
  }

  // BangBangController controller = new BangBangController();
  SimpleMotorFeedforward feedforward = new SimpleMotorFeedforward(LAUNCHER_KS, LAUNCHER_KV, 0);

  PIDController pidControl = new PIDController(LAUNCHER_P, LAUNCHER_I, LAUNCHER_D);



  // Called when the command is initially scheduled. Set the rollers to the
  // appropriate values for intaking
  @Override
  public void initialize() {
    feedforward = new SimpleMotorFeedforward(SmartDashboard.getNumber("kS", 0), SmartDashboard.getNumber("kV", 0), 0);
    tempVoltage = -1 * feedforward.calculate(SmartDashboard.getNumber("rpm setpoint", 0) / 917);
    fuelSubsystem.setFeederRoller(SmartDashboard.getNumber("Launching feeder", 0));

    pidControl = new PIDController(SmartDashboard.getNumber("p value", 0), SmartDashboard.getNumber("i value", 0), SmartDashboard.getNumber("d value", 0));
  }


  // Called every time the scheduler runs while the command is scheduled. This
  // command doesn't require updating any values while running
  @Override
  public void execute() {
    
    double rpm = CANFuelSubsystem.getVelocity();

    fuelSubsystem
        .setIntakeLauncherRoller(
            // SmartDashboard.getNumber("Launching launcher", LAUNCHING_LAUNCHER_VOLTAGE));
            // SmartDashboard.getNumber("Launching launcher", 0));

            // CANFuelSubsystem.calculateVoltage() + 
            // 0.90 * feedforward.calculate(SmartDashboard.getNumber("test launching setpoint", 0)));


            //working ish
            // CANFuelSubsystem.calculateBangBang() / 2
            // + 1 * tempVoltage);

            -1 * pidControl.calculate(rpm, SmartDashboard.getNumber("rpm setpoint", 0))
            + 0.9 * tempVoltage
            );
  }


  // Called once the command ends or is interrupted. Stop the rollers
  @Override
  public void end(boolean interrupted) {

  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() 
  {
    return false;
  }
}
