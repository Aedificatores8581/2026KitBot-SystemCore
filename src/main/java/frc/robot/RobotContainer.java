// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import static edu.wpi.first.units.Units.*;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.CommandPS4Controller;

// import frc.robot.generated.TunerConstants;
// import frc.robot.subsystems.CommandSwerveDrivetrain;

import static frc.robot.Constants.OperatorConstants.*;
import static frc.robot.Constants.FuelConstants.*;

import frc.robot.Constants.FuelConstants;
import frc.robot.commands.Drive;
import frc.robot.commands.Eject;
import frc.robot.commands.Intake;
import frc.robot.commands.SpinUp;
import frc.robot.commands.Launch;
import frc.robot.commands.LaunchSequence;
import frc.robot.commands.LaunchSequence;
import frc.robot.subsystems.CANDriveSubsystem;
import frc.robot.subsystems.CANFuelSubsystem;




public class RobotContainer {
    

    private final CommandPS4Controller mainController = new CommandPS4Controller(0);

 
    private final CANDriveSubsystem driveSubsystem = new CANDriveSubsystem();
    private final CANFuelSubsystem fuelSubsystem = new CANFuelSubsystem();

    private final SendableChooser<Command> autoChooser = new SendableChooser<>();

    public RobotContainer() {
        configureBindings();
    }

    private void configureBindings() {


         SequentialCommandGroup launchSequence = new SequentialCommandGroup(
            new SpinUp(fuelSubsystem).withTimeout(FuelConstants.SPIN_UP_SECONDS),
            new Launch(fuelSubsystem)
        );

        // Controls
        mainController.L1().whileTrue(new Intake(fuelSubsystem));

        mainController.R1().whileTrue(launchSequence);

        mainController.circle().whileTrue(new Eject(fuelSubsystem));


        
        driveSubsystem.setDefaultCommand(new Drive(driveSubsystem, mainController));

        fuelSubsystem.setDefaultCommand(fuelSubsystem.run(() -> fuelSubsystem.stop()));
 
}

    public Command getAutonomousCommand() {

        return autoChooser.getSelected();

    }
}
