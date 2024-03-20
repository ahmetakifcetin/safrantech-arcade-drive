package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
// import edu.wpi.first.cameraserver.CameraServer;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkLowLevel.MotorType;

public class Robot extends TimedRobot {
  Joystick m_stick = new Joystick(0);
  Timer m_timer = new Timer();
  WPI_VictorSPX m_leftMotor = new WPI_VictorSPX(13);
  WPI_VictorSPX m_rightMotor = new WPI_VictorSPX(12);
  WPI_VictorSPX intake_m = new WPI_VictorSPX(14);
  WPI_VictorSPX climb_m1 = new WPI_VictorSPX(11);
  CANSparkMax shoting_m1 = new CANSparkMax(20, MotorType.kBrushless);
  CANSparkMax shoting_m2 = new CANSparkMax(21, MotorType.kBrushless);
  DifferentialDrive m_robotDrive = new DifferentialDrive(m_leftMotor, m_rightMotor);
  boolean climbMode = false;
  boolean amfilemeMode = false;
  boolean intakeMode = false;
  double intake_speed = 0.8;
  double intake_speed_r = -0.8;
  double shoting_speed = 0.8;
  double shoting_speed_r = -0.8;

  @Override
  public void robotInit() {
    // CameraServer.startAutomaticCapture();
    m_leftMotor.setInverted(true);
  }

  @Override
  public void autonomousInit() {
    m_timer.reset();
    m_timer.start();
  }

  @Override
  public void autonomousPeriodic() {
    if (m_timer.get() < 5.0) {
      intake_m.set(0.3);
    } else if (m_timer.get() > 5.0 && m_timer.get() <= 8.0) {
      shoting_m1.set(shoting_speed);
      shoting_m2.set(shoting_speed);
    } else if (m_timer.get() > 8.0 && m_timer.get() <= 12.0) {
      intake_m.stopMotor();
      shoting_m1.stopMotor();
      m_robotDrive.arcadeDrive(0.6, 0.6);
    } else {
      m_robotDrive.stopMotor();
    }
  }

  @Override
  public void teleopInit() {
  }

  public void matchtime() {
    final Double[] values = new Double[1];
    values[0] = DriverStation.getMatchTime();
    SmartDashboard.putNumber("Mac Suresi", DriverStation.getMatchTime());
  }

  public void matchnum() {
    final Double[] values = new Double[1];
    values[0] = (double) DriverStation.getMatchNumber();
    SmartDashboard.putNumber("Mac Sayisi", DriverStation.getMatchNumber());
  }

  public void amfileme() {
    if (m_stick.getRawButton(9)) {
      amfilemeMode = true;
    }
    if (m_stick.getRawButton(10)) {
      amfilemeMode = false;
    }
    SmartDashboard.putBoolean("Amfileme Mod:", amfilemeMode);
    SmartDashboard.putBoolean("Intake Mode:", intakeMode);
    SmartDashboard.putBoolean("Tirmanma Modu:", climbMode);
    if (amfilemeMode == false) {
      intakeMode = true;
    }
    if (amfilemeMode == true) {
      intakeMode = false;
      if (m_stick.getRawButton(3)) {
        intake_m.set(intake_speed_r);
      } else if (m_stick.getRawButton(2)) {
        intake_m.set(intake_speed);
      } else if (m_stick.getRawButton(5)) {
        shoting_m1.set(shoting_speed);
        shoting_m2.set(shoting_speed);
      } else if (m_stick.getRawButton(6)) {
        intake_m.set(intake_speed_r);
        shoting_m1.set(shoting_speed);
        shoting_m2.set(shoting_speed);
      } else if (m_stick.getRawButton(4) && climbMode == false) {
        shoting_m1.set(shoting_speed_r);
        shoting_m2.set(shoting_speed_r);
      } else {
        intake_m.stopMotor();
        shoting_m1.stopMotor();
        shoting_m2.stopMotor();
      }
    }
  }

  public void joyst() {
    double forward = +m_stick.getRawAxis(1);;
    double turn = m_stick.getRawAxis(4);
    m_robotDrive.arcadeDrive(forward, turn);
    SmartDashboard.putNumber("Joystick X", m_stick.getX());
    SmartDashboard.putNumber("Joystick Y", m_stick.getY());
  }

  @Override
  public void teleopPeriodic() {
    m_robotDrive.feed();
    joyst();
    matchtime();
    matchnum();
    amfileme();
    // climbing
    if (m_stick.getRawButton(7)) {
      climbMode = true;
    }
    if (m_stick.getRawButton(8)) {
      climbMode = false;
    }
    if (climbMode == true) {
      if (m_stick.getRawButton(1)) {
        climb_m1.set(-1);
      } else if (m_stick.getRawButton(4)) {
        climb_m1.set(1);
      } else {
        climb_m1.stopMotor();
      }
    }
    // intake&shoting 
    if (amfilemeMode == false && intakeMode == true) {
      if (m_stick.getRawButton(3)) {
        intake_m.set(-0.7);
      } else if (m_stick.getRawButton(2)) {
        intake_m.set(0.7);
      } else if (m_stick.getRawButton(5)) {
        shoting_m1.set(1);
        shoting_m2.set(1);
      } else if (m_stick.getRawButton(6)) {
        intake_m.set(-0.8);
        shoting_m1.set(1);
        shoting_m2.set(1);
      } else if (m_stick.getRawButton(4) && climbMode == false) {
        shoting_m1.set(-0.6);
        shoting_m2.set(-0.6);
      } else {
        intake_m.stopMotor();
        shoting_m1.stopMotor();
        shoting_m2.stopMotor();
      }
    }
  }
}