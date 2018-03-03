package org.usfirst.frc.team4201.robot.commands;

import org.usfirst.frc.team4201.robot.Robot;
import org.usfirst.frc.team4201.robot.subsystems.Arm;
import org.usfirst.frc.team4201.robot.subsystems.Intake;
import org.usfirst.frc.team4201.robot.subsystems.Wrist;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;

/**	This Command automatically retracts the wrist if it detects a cube.
 *	This is also done with a mutex to avoid issues with the wrist auto-retracting in certain cases.
 */
public class ToggleCubeIntakeWithRetraction extends Command {
	boolean cubeDetected;
	
	Timer stopwatch;
	
    public ToggleCubeIntakeWithRetraction() {
        // Use requires() here to declare subsystem dependencies
        requires(Robot.intake);
        //requires(Robot.wrist);
        requires(Robot.arm);
        requires(Robot.elevator);
        
        setInterruptible(false);
        
        stopwatch = new Timer();
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	cubeDetected = false;
    	Scheduler.getInstance().add(new HoldWristSetpoint());
    	Scheduler.getInstance().add(new SetWristArmElevatorSetpoints(0, -60, 2.8));
    }
    
    @Override
	protected void execute() {
    	Robot.intake.setIntakeMotorOutput(1, 0.75);
    }

	@Override
	protected boolean isFinished() {
		// TODO Auto-generated method stub
		cubeDetected = Robot.intake.intakeMotors[0].getOutputCurrent() > 15 && Robot.intake.intakeMotors[1].getOutputCurrent() > 15;
		return (cubeDetected || Robot.oi.leftButtons[0].get());
	}
	
    // Called once after isFinished returns true
    protected void end() {
    	if(cubeDetected && Wrist.state == 0 && Arm.state == 0){
        	Robot.intake.setIntakeMotorOutput(0);
    		stopwatch.start();
    		while(stopwatch.get() < 0.1){
    			// Do nothing pause
    		}
    		stopwatch.stop();
    		stopwatch.reset();
    		stopwatch.start();
    		while(stopwatch.get() < 0.1){
            	Robot.intake.setIntakeMotorOutput(1, 0.75);
    		}
    		stopwatch.stop();
    		stopwatch.reset();
    		//Intake.isCubePresent = true;
        	Robot.wrist.setSetpoint(120);
        	Scheduler.getInstance().add(new ReleaseWristSetpoint());
    	} else if(cubeDetected){
    		Robot.intake.setIntakeMotorOutput(0);
    		stopwatch.start();
    		while(stopwatch.get() < 0.1){
    			// Do nothing pause
    		}
    		stopwatch.stop();
    		stopwatch.reset();
    		stopwatch.start();
    		while(stopwatch.get() < 0.1){
            	Robot.intake.setIntakeMotorOutput(1, 0.75);
    		}
    		stopwatch.stop();
    		stopwatch.reset();
    		while(Robot.wrist.wristMotor.getOutputCurrent() < 10)
    			Robot.wrist.setDirectOutput(0.75);
    	}
        Robot.intake.setIntakeMotorOutput(0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }

}
