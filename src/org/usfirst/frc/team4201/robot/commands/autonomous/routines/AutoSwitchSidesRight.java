package org.usfirst.frc.team4201.robot.commands.autonomous.routines;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.CommandGroup;

import org.usfirst.frc.team4201.robot.commands.*;
import org.usfirst.frc.team4201.robot.commands.autonomous.*;

public class AutoSwitchSidesRight extends CommandGroup {
	String[] rightPaths = {
		"rightStartToRightSwitchReverseOne",
		"rightStartToRightSwitchReverseTwo",
		"rightStartToLeftSwitchNearReverseOne",
		"rightStartToLeftSwitchNearReverseTwo",
		"rightStartToLeftSwitchFarReverseOne",
		"rightStartToLeftSwitchFarReverseTwo"
	};
	
	public AutoSwitchSidesRight(boolean near) {
		addSequential(new SetIntakePistonsClose());
		addSequential(new SetIntakePressureHigh());
		
		if(DriverStation.getInstance().getGameSpecificMessage().charAt(0) == 'R') {
			addSequential(new AutoPathFinderInvertedToSwitch(rightPaths[0], true));
			addSequential(new PathFinderRead(rightPaths[1]));
		} else {
			addSequential(new PathFinderReadInverted(near ? rightPaths[2] : rightPaths[4], true));
			addSequential(new AutoPathFinderToSwitch(near ? rightPaths[3] : rightPaths[5]));
		}
		
		addSequential(new AutoDropCube());
		
		addSequential(new AutoReleaseWristSetpoint());
	}
}
