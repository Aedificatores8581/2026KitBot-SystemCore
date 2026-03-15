// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

// import com.revrobotics.spark.SparkBase.PersistMode;
// import com.revrobotics.spark.SparkBase.ResetMode;
// import com.revrobotics.spark.SparkLowLevel.MotorType;
// import com.revrobotics.spark.config.SparkMaxConfig;
// import com.revrobotics.spark.SparkMax;

// import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
// import edu.wpi.first.wpilibj2.command.SubsystemBase;
// import static frc.robot.Constants.FuelConstants.*;

import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.RelativeEncoder;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import static frc.robot.Constants.FuelConstants.*;
import static frc.robot.Constants.CANBusConstants.*;

import edu.wpi.first.math.controller.BangBangController;
import edu.wpi.first.math.controller.PIDController;



public class CANFuelSubsystem extends SubsystemBase {
  static int counter = 0;

  private final SparkMax feederRoller;
  private final SparkMax intakeLauncherRoller;

  static RelativeEncoder launcher_encoder;
  static BangBangController controller = new BangBangController();
  static PIDController pidControl = new PIDController(LAUNCHER_P, 0.0, 0.0);


  /** Creates a new CANBallSubsystem. */
  public CANFuelSubsystem() {
    // create brushed motors for each of the motors on the launcher mechanism
    intakeLauncherRoller = new SparkMax(CANDBUS, INTAKE_LAUNCHER_MOTOR_ID, MotorType.kBrushless);
    feederRoller = new SparkMax(CANDBUS, FEEDER_MOTOR_ID, MotorType.kBrushless);


    //Bang-Bang controller
    launcher_encoder = intakeLauncherRoller.getEncoder();


    // create the configuration for the feeder roller, set a current limit and apply
    // the config to the controller
    SparkMaxConfig feederConfig = new SparkMaxConfig();
    feederConfig.smartCurrentLimit(FEEDER_MOTOR_CURRENT_LIMIT);
    feederRoller.configure(feederConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

    // create the configuration for the launcher roller, set a current limit, set
    // the motor to inverted so that positive values are used for both intaking and
    // launching, and apply the config to the controller
    SparkMaxConfig launcherConfig = new SparkMaxConfig();
    launcherConfig.inverted(true);
    launcherConfig.smartCurrentLimit(LAUNCHER_MOTOR_CURRENT_LIMIT);
    intakeLauncherRoller.configure(launcherConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

    // put default values for various fuel operations onto the dashboard
    // all commands using this subsystem pull values from the dashbaord to allow
    // you to tune the values easily, and then replace the values in Constants.java
    // with your new values. For more information, see the Software Guide.
    SmartDashboard.putNumber("Intaking feeder", INTAKING_FEEDER_VOLTAGE);
    SmartDashboard.putNumber("Intaking intake", INTAKING_INTAKE_VOLTAGE);

    SmartDashboard.putNumber("Launching feeder", LAUNCHING_FEEDER_VOLTAGE);
    SmartDashboard.putNumber("Launching launcher", LAUNCHING_LAUNCHER_VOLTAGE);
    
    SmartDashboard.putNumber("Spin-up feeder", SPIN_UP_FEEDER_VOLTAGE);

    SmartDashboard.putNumber("voltage setpoint", -8);
    SmartDashboard.putNumber("rpm setpoint", LAUNCHER_RPM_SETPOINT);

    SmartDashboard.putNumber("kS", LAUNCHER_KS);
    SmartDashboard.putNumber("kV", LAUNCHER_KV);

    SmartDashboard.putNumber("launcher rpm", launcher_encoder.getVelocity());

    SmartDashboard.putNumber("p value", LAUNCHER_P);
    SmartDashboard.putNumber("i value", LAUNCHER_I);
    SmartDashboard.putNumber("d value", LAUNCHER_D);



  }

  // A method to set the voltage of the intake roller
  public void setIntakeLauncherRoller(double voltage) {
    intakeLauncherRoller.setVoltage(voltage);
  }

  // A method to set the voltage of the intake roller
  public void setFeederRoller(double voltage) {
    feederRoller.setVoltage(voltage);
  }

  //method to return launcher encoder
  // public RelativeEncoder getLauncherEncoder() {
  //   return launcher_encoder;
  // }

  public static double calculateBangBang()
  {
    counter++;
    return -12 * controller.calculate(-1 * launcher_encoder.getVelocity(), 1 * SmartDashboard.getNumber("rpm setpoint", 0));
  }

  // public static double calculatePID()
  // {
  //   counter++;
  //   return pidControl.calculate(-1 * launcher_encoder.getVelocity(), SmartDashboard.getNumber("rpm setpoint", 0));
  // }


  public static double getVelocity()
  {
    return launcher_encoder.getVelocity();
  }


  // A method to stop the rollers
  public void stop() {
    feederRoller.set(0);
    intakeLauncherRoller.set(0);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    // SmartDashboard.putNumber("launcher encoder value", launcher_encoder.getVelocity());

    SmartDashboard.putNumber("launcher voltage", intakeLauncherRoller.getAppliedOutput() * intakeLauncherRoller.getBusVoltage());

    SmartDashboard.putNumber("launcher rpm", launcher_encoder.getVelocity());

    SmartDashboard.putNumber("counter", counter);

  }
}
