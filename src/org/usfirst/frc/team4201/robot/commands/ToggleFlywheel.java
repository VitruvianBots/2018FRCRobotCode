package org.usfirst.frc.team4201.robot.commands;

import org.usfirst.frc.team4201.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class ToggleFlywheel extends Command {
	double RPM;
	
    public ToggleFlywheel(double RPM) {
        // Use requires() here to declare subsystem dependencies
        requires(Robot.shooter);
        this.RPM = RPM;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	if(!Robot.shooter.getFlywheelEnable())
    		Robot.shooter.setFlywheelOutput(RPM);
    	else
    		Robot.shooter.disableFlywheel();
    		Robot.shooter.setFlywheelOutput(0);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return true;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	Robot.shooter.disableFlywheel();
    }
}