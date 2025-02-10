package great_bot;
import robocode.*;
// import java.awt.Color;

/**
 * GreatBot - a robot by (your name here)
 */
public class GreatBot extends Robot {
    double battlefieldWidth, battlefieldHeight;
    double lastEnemyVelocity = 0;
    double lastEnemyHeading = 0;

    public void run() {
        battlefieldWidth = getBattleFieldWidth();
        battlefieldHeight = getBattleFieldHeight();
        
        // Coordinates of the walls
        double leftWallX = 0;
        double leftWallY = battlefieldHeight / 2;
        double bottomWallX = battlefieldWidth / 2;
        double bottomWallY = 0;
        double rightWallX = battlefieldWidth;
        double rightWallY = battlefieldHeight / 2;
        double topWallX = battlefieldWidth / 2;
        double topWallY = battlefieldHeight;

        // Calculate angles between walls
        double angle1 = Math.atan2(topWallY - leftWallY, topWallX - leftWallX);
        double angle2 = Math.atan2(leftWallY - bottomWallY, leftWallX - bottomWallX);
        double angle3 = Math.atan2(bottomWallY - rightWallY, bottomWallX - rightWallX);
        double angle4 = Math.atan2(rightWallY - topWallY, rightWallX - topWallX);

        // Calculate distances between walls
        double distance1 = Math.sqrt(Math.pow(leftWallX - topWallX, 2) + Math.pow(leftWallY - topWallY, 2));
        double distance2 = Math.sqrt(Math.pow(bottomWallX - leftWallX, 2) + Math.pow(bottomWallY - leftWallY, 2));
        double distance3 = Math.sqrt(Math.pow(rightWallX - bottomWallX, 2) + Math.pow(rightWallY - bottomWallY, 2));
        double distance4 = Math.sqrt(Math.pow(topWallX - rightWallX, 2) + Math.pow(topWallY - rightWallY, 2));

        while (true) {
            ahead(100);
            turnGunRight(360);
            back(100);
            turnGunRight(360);
        }
    }

    public void onScannedRobot(ScannedRobotEvent e) {
        // Calculate the bearing to the enemy
        double enemyBearing = getHeading() + e.getBearing();

        // Update the enemy's movement data
        double enemyVelocityChange = e.getVelocity() - lastEnemyVelocity;
        lastEnemyHeading = e.getHeading();
        lastEnemyVelocity = e.getVelocity();

        // Predict enemy's future position
        double futureX = getX() + e.getDistance() * Math.sin(Math.toRadians(enemyBearing));
        double futureY = getY() + e.getDistance() * Math.cos(Math.toRadians(enemyBearing));

        // Adjust future position based on velocity change
        futureX += Math.sin(Math.toRadians(getHeading())) * enemyVelocityChange;
        futureY += Math.cos(Math.toRadians(getHeading())) * enemyVelocityChange;

        // Calculate angle to enemy's future position
        double targetAngle = Math.toDegrees(Math.atan2(futureX - getX(), futureY - getY()));

        // Turn the gun to target the enemy
        turnGunRight(normalizeBearing(targetAngle - getGunHeading()));

        // Fire at the enemy based on distance and energy level
        if (e.getDistance() < 50 && getEnergy() > 50) {
            fire(3);
        } else {
            fire(1);
        }
    }

    // Normalize bearing to be within -180 to 180 degrees
    public double normalizeBearing(double angle) {
        while (angle > 180) angle -= 360;
        while (angle < -180) angle += 360;
        return angle;
    }
}
